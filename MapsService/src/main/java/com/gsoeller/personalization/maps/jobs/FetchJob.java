package com.gsoeller.personalization.maps.jobs;

import io.dropwizard.jdbi.OptionalContainerFactory;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.apache.http.HttpResponse;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.RateLimiter;
import com.gsoeller.personalization.maps.MapsLogger;
import com.gsoeller.personalization.maps.PropertiesLoader;
import com.gsoeller.personalization.maps.StaticMapFetcher;
import com.gsoeller.personalization.maps.dao.FetchJobDao;
import com.gsoeller.personalization.maps.dao.ImageDao;
import com.gsoeller.personalization.maps.dao.MapDao;
import com.gsoeller.personalization.maps.dao.MapRequestDao;
import com.gsoeller.personalization.maps.data.Map;
import com.gsoeller.personalization.maps.data.MapRequest;
import com.gsoeller.personalization.maps.smtp.MapsEmail;
import com.gsoeller.personalization.maps.smtp.SmtpClient;
import com.gsoeller.personalization.maps.smtp.MapsEmail.MapsEmailBuilder;

public class FetchJob implements Job {

	private StaticMapFetcher fetcher = new StaticMapFetcher();
	private ImageDao imageDao = new ImageDao();

	private DBI dbi;
	private Handle handle;
	private MapDao mapDao;
	private FetchJobDao fetchJobDao;
	private MapRequestDao mapRequestDao;

	private final RateLimiter limiter = RateLimiter.create(.28);
	private ExecutorService executorService = Executors.newCachedThreadPool();

	private final int MINUTES_TO_RUN = 50;
	
	private SmtpClient smtpClient = new SmtpClient();
	
	private Logger LOG = MapsLogger.createLogger("com.gsoeller.personalization.maps.jobs.FetchJob");
	
	public FetchJob() throws IOException {
		PropertiesLoader propLoader = new PropertiesLoader();
		dbi = new DBI(propLoader.getProperty("db"), propLoader.getProperty("dbuser"), propLoader.getProperty("dbpwd"));
		dbi.registerContainerFactory(new OptionalContainerFactory());
		handle = dbi.open();
		mapDao = handle.attach(MapDao.class);
		fetchJobDao = handle.attach(FetchJobDao.class);
		mapRequestDao = handle.attach(MapRequestDao.class);
	}

	public void execute(JobExecutionContext context)
			throws JobExecutionException {	
		int mapNumber = (Integer)context.getJobDetail().getJobDataMap().get("mapNumber");
		LOG.info(String.format("Fetching maps for map number: %d", mapNumber));	
		List<Boolean> finished = fetchJobDao.isLastJobFinished();
		int currentFetchJob;
		int offset;
		if(!finished.isEmpty() && finished.get(0)) {
			LOG.info("Starting a new fetch job");
			currentFetchJob = fetchJobDao.createFetchJob();
			offset = 0;
		} else {
			List<Integer> lastFetchJob = fetchJobDao.getLastFetchJob();
			if(lastFetchJob.isEmpty()) {
				currentFetchJob = fetchJobDao.createFetchJob();
				offset = 0;
			} else {
				currentFetchJob = lastFetchJob.get(0);
				List<Integer> lastMapFetched = mapDao.getLastMap();
				if(lastMapFetched.isEmpty()) {
					offset = 0;
				} else {
					offset = lastMapFetched.get(0);
				}
			}
			LOG.info("Did not finish fetch job. Picking up on fetch job id: " + currentFetchJob + " and offset: " + offset);
		}
		final int fetchJob = currentFetchJob;
		int batchSize = 10;
		DateTime startTime = DateTime.now();
		while(true) {
			DateTime currentTime = DateTime.now();
			int runtimeInMinutes = Minutes.minutesBetween(startTime, currentTime).getMinutes();
			if(runtimeInMinutes >= MINUTES_TO_RUN) {
				LOG.info("Time limit is up. Exiting current job");
				break;
			}
			
			List<MapRequest> requests = getNextBatchOfRequests(batchSize, offset);
			if(requests.isEmpty()) {
				executorService.shutdown();
			}
			if(executorService.isTerminated()) {
				LOG.info("WERE DONE");
				fetchJobDao.finishFetchJob(fetchJob);
				return;
			}
			try {
				processRequests(requests, fetchJob);
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			offset+=batchSize;
		}
		//System.exit(0);
	}
	
	private void processRequests(List<MapRequest> requests, final int fetchJob) throws NoSuchAlgorithmException, IOException {
		for (final MapRequest request : requests) {
			limiter.acquire();
			executorService.execute(new Runnable() {

				public void run() {
					Optional<HttpResponse> response = fetcher.fetch(request);
					if(response.isPresent()) {
						String newImage = UUID.randomUUID().toString() + ".png";
						imageDao.saveImage(newImage, response.get().getEntity());
						Optional<String> existingPath = getExistingPath(newImage);
						//System.exit(0);
						Optional<String> oldImage = getPathForLastMapRequest(request.getId(), existingPath);

						if (oldImage.isPresent()) {
							boolean hasChanged;
							String imagePath;
							
							boolean sameImage;
							try {
								sameImage = sameImage(newImage, oldImage.get());
							} catch (NoSuchAlgorithmException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								throw new RuntimeException("error");
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								throw new RuntimeException("errorrrr");
							}
							if (sameImage) {
								hasChanged = false;
								imagePath = oldImage.get();
								imageDao.removeImage(newImage);
								LOG.info("Images are the same. Removing newest one to save space.");
							} else {
								hasChanged = true;
								imagePath = newImage;
								try {
									MapsEmail email = new MapsEmailBuilder()
										.setSubject("Tile Has Changed")
										.setmMessage(request.toString())
										.addTo("mapspersonalization@gmail.com")
										.build();
									smtpClient.sendEmail(email);
								} catch (IOException e) {
									LOG.severe("Could not send email");
									e.printStackTrace();
								}
								LOG.info("Images are different. Keeping both images in the filesystem");
							}
							saveImage(hasChanged, request.getId(), imagePath, fetchJob);
							LOG.info("Saved image");
						} else {
							saveImage(false, request.getId(), newImage, fetchJob);
						}
					}
					
				}
			});
		}
	}

	private List<MapRequest> getNextBatchOfRequests(int limit, int offset) {
		return mapRequestDao.getRequests(limit, offset);
	}
	
	private void saveImage(boolean hasChanged, int id, String path, int fetchJob) {
		try {
			mapDao.saveMap(false, id, path, getImageHash(path), fetchJob);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String getImageHash(String path) throws IOException,
			NoSuchAlgorithmException {
		BufferedImage image = imageDao.getImage(path);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ImageIO.write(image, "png", outputStream);
		byte[] data = outputStream.toByteArray();
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(data);
		byte[] hash = md.digest();
		LOG.info("Hash is\t" + returnHex(hash));
		return returnHex(hash);
	}

	private String returnHex(byte[] inBytes) {
		String hexString = "";
		for (int i = 0; i < inBytes.length; i++) {
			hexString += Integer.toString((inBytes[i] & 0xff) + 0x100, 16)
					.substring(1);
		}
		return hexString;
	}

	public boolean sameImage(String path1, String path2) throws NoSuchAlgorithmException, IOException {
		// This is just placeholder code. The next step is to actually compare
		// images here
		//return true;
		return getImageHash(path1).equals(getImageHash(path2));
	}
	
	public Optional<String> getExistingPath(String path) {
		try {
			String hash = getImageHash(path);
			List<String> hashPath = mapDao.getPathWithHash(hash);
			if (hashPath.isEmpty()) {
				return Optional.absent();
			}
			return Optional.fromNullable(hashPath.get(0));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Optional.absent();
	}

	public Optional<String> getPathForLastMapRequest(int mapRequestId, Optional<String> existingPath) {
		List<Map> map = mapDao.getMapMostRecentWithMapRequestId(mapRequestId);
		if (!map.isEmpty()) {
			LOG.info("Found an old image");
			return Optional.of(map.get(0).getPath());
		} else if(existingPath.isPresent()) {
			LOG.info("Found the same image on disk");
			return existingPath;
		}
		LOG.info("Did not find an old image");
		return Optional.absent();
	}
}

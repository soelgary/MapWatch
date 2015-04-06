package com.gsoeller.personalization.maps.jobs;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

import com.google.common.base.Optional;
import com.google.common.util.concurrent.RateLimiter;
import com.gsoeller.personalization.maps.MapsLogger;
import com.gsoeller.personalization.maps.amt.HitGenerator;
import com.gsoeller.personalization.maps.dao.BingFetchJobDao;
import com.gsoeller.personalization.maps.dao.BingMapDao;
import com.gsoeller.personalization.maps.dao.BingMapRequestDao;
import com.gsoeller.personalization.maps.dao.FetchJobDao;
import com.gsoeller.personalization.maps.dao.GoogleFetchJobDao;
import com.gsoeller.personalization.maps.dao.GoogleMapUpdateDao;
import com.gsoeller.personalization.maps.dao.ImageDao;
import com.gsoeller.personalization.maps.dao.GoogleMapDao;
import com.gsoeller.personalization.maps.dao.GoogleMapRequestDao;
import com.gsoeller.personalization.maps.dao.MapDao;
import com.gsoeller.personalization.maps.dao.MapRequestDao;
import com.gsoeller.personalization.maps.dao.MapUpdateDao;
import com.gsoeller.personalization.maps.data.Map;
import com.gsoeller.personalization.maps.data.MapChange;
import com.gsoeller.personalization.maps.data.MapProvider;
import com.gsoeller.personalization.maps.data.MapRequest;
import com.gsoeller.personalization.maps.fetchers.StaticMapFetcher;
import com.gsoeller.personalization.maps.health.ReverseHealthCheck;
import com.gsoeller.personalization.maps.smtp.MapsEmail;
import com.gsoeller.personalization.maps.smtp.SmtpClient;
import com.gsoeller.personalization.maps.smtp.MapsEmail.MapsEmailBuilder;

public class FetchJob implements Job {

	private StaticMapFetcher fetcher = new StaticMapFetcher();
	private ImageDao imageDao = new ImageDao();	
	private MapDao mapDao;
	private FetchJobDao fetchJobDao;
	private MapRequestDao mapRequestDao;
	private MapUpdateDao mapUpdateDao;

	private RateLimiter limiter;
	private ExecutorService executorService = Executors.newCachedThreadPool();

	private final int MINUTES_TO_RUN = 50;
	
	private SmtpClient smtpClient = new SmtpClient();
	
	private ReverseHealthCheck reverseHealthCheck;
	
	private HitGenerator hitGenerator;
	
	private static final double BING_RATE = .3;
	private static final double GOOGLE_RATE = .3;
	
	private Logger LOG = MapsLogger.createLogger("com.gsoeller.personalization.maps.jobs.FetchJob");
	
	public boolean configure(String mapProvider) throws IOException {
		if(mapProvider.equals("google")) {
			setGoogleMaps();
		} else if(mapProvider.equals("bing")) {
			setBingMaps();
		} else {
			return false;
		}
		return true;
	}
	
	public void setGoogleMaps() throws IOException {
		mapDao = new GoogleMapDao();
		mapRequestDao = new GoogleMapRequestDao();
		fetchJobDao = new GoogleFetchJobDao();
		mapUpdateDao = new GoogleMapUpdateDao();
		limiter = RateLimiter.create(GOOGLE_RATE);
	}
	
	public void setBingMaps() throws IOException {
		mapDao = new BingMapDao();
		mapRequestDao = new BingMapRequestDao();
		fetchJobDao = new BingFetchJobDao();
		limiter = RateLimiter.create(BING_RATE);
	}
	
	public void execute(JobExecutionContext context)
			throws JobExecutionException {	
		int mapNumber = (Integer)context.getJobDetail().getJobDataMap().get("mapNumber");
		String mapProvider = (String) context.getJobDetail().getJobDataMap().get("mapProvider");
		try {
			reverseHealthCheck = new ReverseHealthCheck();
		} catch (IOException e2) {
			e2.printStackTrace();
			System.exit(0);
		}
		boolean configured;
		try {
			configured = configure(mapProvider);
			hitGenerator = new HitGenerator();
		} catch (IOException e1) {
			LOG.severe("An error occurred while configuring the fetcher...");
			e1.printStackTrace();
			configured = false;
		}
		if(!configured) {
			LOG.severe(String.format("Cannot configure the given map provider, '%s'", mapProvider));
			System.exit(0);
		}
		LOG.info(String.format("Fetching maps for map number: %d and map provider: %s", mapNumber, mapProvider));
		boolean finished = fetchJobDao.isLastJobFinished();
		int currentFetchJob;
		int offset;
		if(finished) {
			LOG.info("Starting a new fetch job");
			currentFetchJob = fetchJobDao.createFetchJob(mapNumber);
			offset = 0;
		} else {
			Optional<Integer> lastFetchJob = fetchJobDao.getLastFetchJob();
			if(!lastFetchJob.isPresent()) {
				currentFetchJob = fetchJobDao.createFetchJob(mapNumber);
				offset = 0;
			} else {
				currentFetchJob = lastFetchJob.get();
				Optional<Integer> lastMapFetched = mapDao.getLastMap(currentFetchJob);
				if(!lastMapFetched.isPresent()) {
					offset = 0;
				} else {
					offset = lastMapFetched.get();
				}
			}
			LOG.info("Did not finish fetch job. Picking up on fetch job id: " + currentFetchJob + " and offset: " + offset);
		}
		final int fetchJob = currentFetchJob;
		int batchSize = 10;
		DateTime startTime = DateTime.now();
		while(true) {
			LOG.info("Starting to fetch batch");
			DateTime currentTime = DateTime.now();
			int runtimeInMinutes = Minutes.minutesBetween(startTime, currentTime).getMinutes();
			if(runtimeInMinutes >= MINUTES_TO_RUN) {
				LOG.info("Time limit is up. Exiting current job");
				break;
			}
			
			List<MapRequest> requests = getNextBatchOfRequests(batchSize, offset, mapNumber);
			if(requests.isEmpty()) {
				executorService.shutdown();
			}
			if(executorService.isTerminated()) {
				LOG.info("WERE DONE");
				fetchJobDao.finishFetchJob(fetchJob);
				return;
			}
			try {
				processRequests(requests, fetchJob, mapProvider);
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
	
	
	private void processRequests(List<MapRequest> requests, final int fetchJob, final String mapProvider) throws NoSuchAlgorithmException, IOException {
		for (final MapRequest request : requests) {
			System.out.println("TRYING TO AQCUIRE");
			limiter.acquire();
			System.out.println("ACQUIRED");
			executorService.execute(new Runnable() {

				public void run() {
					Optional<HttpResponse> response = fetcher.fetch(request);
					if(response.isPresent()) {
						String newImage = UUID.randomUUID().toString() + ".png";
						imageDao.saveImage(newImage, response.get().getEntity());
						Optional<String> existingPath = getExistingPath(newImage);
						Optional<String> oldImage = getPathForLastMapRequest(request.getId(), existingPath);
						
						boolean hashExists = hashExists(existingPath);

						if (oldImage.isPresent()) {
							boolean hasChanged;
							String imagePath;
							if (hashExists) {
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
							Optional<Map> oldMap = mapDao.getMapMostRecentWithMapRequestId(request.getId());
							int currentMapId = saveImage(hasChanged, request.getId(), imagePath, fetchJob);
							LOG.info("Saved image");
							if(hasChanged) {
								LOG.info("Tile changed, saving the map update");
								if(oldMap.isPresent()) {
									 int updateId = mapUpdateDao.save(oldMap.get().getId(), currentMapId); 
									 MapChange change = mapUpdateDao.getUpdate(updateId).get();
									 try {
										hitGenerator.addUpdate(MapProvider.valueOf(mapProvider), change);
									} catch (Exception e) {
										e.printStackTrace();
										LOG.severe("Error occurred trying to generate/update HIT's");
										System.exit(0);
									}
								} else {
									LOG.severe(String.format("Could not find an old map when the new map id is %s", currentMapId));
								}
							}
						} else {
							saveImage(false, request.getId(), newImage, fetchJob);
						}
					}
					try {
						reverseHealthCheck.sendReverseHealthCheck(mapProvider);
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}
	}

	private List<MapRequest> getNextBatchOfRequests(int limit, int offset, int mapNumber) {
		return mapRequestDao.getRequests(limit, offset, mapNumber);
	}
	
	private int saveImage(boolean hasChanged, int id, String path, int fetchJob) {
		try {
			return mapDao.saveMap(false, id, path, getImageHash(path), fetchJob);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;

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
		String hash1 = getImageHash(path1);
		String hash2 = getImageHash(path2);
		LOG.info(String.format("Comparing hashes '%s' and '%s'", hash1, hash2));
		return hash1.equals(hash2);
	}
	
	public Optional<String> getExistingPath(String path) {
		try {
			String hash = getImageHash(path);
			return mapDao.getPathWithHash(hash);
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
		Optional<Map> map = mapDao.getMapMostRecentWithMapRequestId(mapRequestId);
		if (map.isPresent()) {
			LOG.info("Found an old image");
			return Optional.of(map.get().getPath());
		} else if(existingPath.isPresent()) {
			LOG.info("Found the same image on disk");
			return existingPath;
		}
		LOG.info("Did not find an old image");
		return Optional.absent();
	}
	
	public boolean hashExists(Optional<String> existingPath) {
		if(existingPath.isPresent()) {
			try {
				String hash = getImageHash(existingPath.get());
				return mapDao.containsHash(hash);
			} catch (NoSuchAlgorithmException e) {
				LOG.severe(String.format("Could not get hash for image, '%s'", existingPath.get()));
				e.printStackTrace();
			} catch (IOException e) {
				LOG.severe(String.format("Could not get hash for image, '%s'", existingPath.get()));
				e.printStackTrace();
			}			
		}
		return false;
	}
}

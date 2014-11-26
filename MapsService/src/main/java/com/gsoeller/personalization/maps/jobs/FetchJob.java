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

import javax.imageio.ImageIO;

import org.apache.http.HttpResponse;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.RateLimiter;
import com.gsoeller.personalization.maps.PropertiesLoader;
import com.gsoeller.personalization.maps.StaticMapFetcher;
import com.gsoeller.personalization.maps.dao.FetchJobDao;
import com.gsoeller.personalization.maps.dao.ImageDao;
import com.gsoeller.personalization.maps.dao.MapDao;
import com.gsoeller.personalization.maps.dao.MapRequestDao;
import com.gsoeller.personalization.maps.data.Map;
import com.gsoeller.personalization.maps.data.MapRequest;

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
	private static final Logger LOG = LoggerFactory.getLogger(FetchJob.class);

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
		LOG.info("Fetching maps");
		System.out.println("fetching maps");
		final int fetchJob = fetchJobDao.createFetchJob();
		int offset = 0;
		int batchSize = 100;
		while(true) {
			List<MapRequest> requests = getNextBatchOfRequests(batchSize, offset);
			if(requests.isEmpty()) {
				executorService.shutdown();
			}
			if(executorService.isTerminated()) {
				System.out.println("WERE DONE");
				return;
				//System.exit(0);
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
					HttpResponse response = fetcher.fetch(request);
					String newImage = UUID.randomUUID().toString() + ".png";
					imageDao.saveImage(newImage, response.getEntity());
					//System.exit(0);
					Optional<String> oldImage = getPathForLastMapRequest(request.getId());

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
							System.out.println("Images are the same. Removing newest one to save space.");
						} else {
							hasChanged = true;
							imagePath = newImage;
							System.out.println("Images are different. Keeping both images in the filesystem");
						}
						saveImage(hasChanged, request.getId(), imagePath, fetchJob);
						System.out.println("Saved image");
					} else {
						saveImage(false, request.getId(), newImage, fetchJob);
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
		System.out.println("Hash is\t" + returnHex(hash));
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

	public Optional<String> getPathForLastMapRequest(int mapRequestId) {
		List<Map> map = mapDao.getMapMostRecentWithMapRequestId(mapRequestId);
		if (!map.isEmpty()) {
			System.out.println("Found an old image");
			return Optional.of(map.get(0).getPath());
		}
		System.out.println("Did not find an old image");
		return Optional.absent();
	}
}

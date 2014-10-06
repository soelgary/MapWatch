package com.gsoeller.personalization.maps.jobs;

import io.dropwizard.jdbi.OptionalContainerFactory;

import java.util.List;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.gsoeller.personalization.maps.StaticMapFetcher;
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
	
	private static final Logger LOG = LoggerFactory
			.getLogger(FetchJob.class);
	
	public FetchJob() {
		dbi = new DBI("jdbc:mysql://localhost/Maps", "root", "");
		dbi.registerContainerFactory(new OptionalContainerFactory());
		handle = dbi.open();
		mapDao = handle.attach(MapDao.class);
		
	}
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		LOG.info("Fetching maps");
		MapRequestDao dao = (MapRequestDao)context.getJobDetail().getJobDataMap().get("MapRequestDao");
		List<MapRequest> requests = dao.getRequests();
		for(MapRequest request: requests) {
			HttpResponse response = fetcher.fetch(request);
			String newImage = UUID.randomUUID().toString() + ".png";
			imageDao.saveImage(newImage, response.getEntity());
			
			
			Optional<String> oldImage = getPathForLastMapRequest(request.getId());
			
			if(oldImage.isPresent()) {
				boolean hasChanged;
				String imagePath;
				
				if(sameImage(newImage, oldImage.get())) {
					hasChanged = false;
					imagePath = oldImage.get();
					imageDao.removeImage(newImage);
					LOG.info("Images are the same. Removing newest one to save space.");
				} else {
					hasChanged = true;
					imagePath = newImage;
					LOG.info("Images are different. Keeping both images in the filesystem");
				}
				int id = mapDao.saveMap(hasChanged, request.getId(), imagePath);
			} else {
				int id = mapDao.saveMap(false, request.getId(), newImage);
			}
			
		}	
	}
	
	public boolean sameImage(String path1, String path2) {
		// This is just placeholder code. The next step is to actually compare images here
		return true;
	}
	
	public Optional<String> getPathForLastMapRequest(int mapRequestId) {
		List<Map> map = mapDao.getMapMostRecentWithMapRequestId(mapRequestId);
		if(!map.isEmpty()) {
			return Optional.of(map.get(0).getPath());
		}
		return Optional.absent();
	}
}

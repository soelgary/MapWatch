package com.gsoeller.personalization.maps.jobs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.common.base.Optional;
import com.gsoeller.personalization.maps.MapsLogger;
import com.gsoeller.personalization.maps.PropertiesLoader;
import com.gsoeller.personalization.maps.dao.GoogleMapDao;
import com.gsoeller.personalization.maps.dao.GoogleMapRequestDao;
import com.gsoeller.personalization.maps.data.GoogleMap;
import com.gsoeller.personalization.maps.data.Region;
import com.gsoeller.personalization.maps.image.GifGenerator;
import com.gsoeller.personalization.maps.image.GifGenerator.GifGeneratorBuilder;

public class GenerateGifJob implements Job {

	private GoogleMapDao dao;
	private GoogleMapRequestDao mapRequestDao;
	private String gifDirectory;
	
	private Logger LOG = MapsLogger.createLogger("com.gsoeller.personalization.maps.jobs.GenerateGifJob");

	public GenerateGifJob() throws IOException {
		PropertiesLoader propLoader = new PropertiesLoader();
		gifDirectory = propLoader.getProperty("gifdirectory");
	}

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			dao = new GoogleMapDao();
			mapRequestDao = new GoogleMapRequestDao();
		} catch (IOException e1) {
			e1.printStackTrace();
			System.exit(0);
		}
		BufferedReader br = null;
		try {
			String sCurrentLine;
			br = new BufferedReader(
					new FileReader(
							"/Users/garysoeller/dev/src/MapsPersonalization/MapsService/src/main/resources/ids.txt"));
			while ((sCurrentLine = br.readLine()) != null) {
				handleId(sCurrentLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		System.exit(0);
	}

	public void handleId(String id) throws Exception {
		List<GoogleMap> maps = dao.getMapsWithMapRequest(Integer.parseInt(id));
		GifGeneratorBuilder builder = new GifGenerator.GifGeneratorBuilder();
		builder.setFilename(id + ".gif")
			.setMapRequest(Integer.parseInt(id))
			.setGifDirectory(gifDirectory);
		Optional<Region> region = mapRequestDao.getRegion(Integer.parseInt(id));
		if (region.isPresent()) {
			for (GoogleMap map : maps) {
				builder.addImage(map.getPath(), region.get(), map.getFetchJob());
			}
			builder.build().createGif();
		} else {
			LOG.severe(String.format("ID: %s, does not have an associated region", id));
		}
	}
}

package com.gsoeller.personalization.maps.jobs;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import io.dropwizard.jdbi.OptionalContainerFactory;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

import com.google.common.base.Optional;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.gsoeller.personalization.maps.dao.FetchJobDao;
import com.gsoeller.personalization.maps.dao.MapDao;
import com.gsoeller.personalization.maps.dao.MapRequestDao;
import com.gsoeller.personalization.maps.data.Map;
import com.gsoeller.personalization.maps.data.Region;
import com.gsoeller.personalization.maps.smtp.MapsEmail;
import com.gsoeller.personalization.maps.smtp.MapsEmail.MapsEmailBuilder;
import com.gsoeller.personalization.maps.smtp.SmtpClient;

public class ComparisonJob implements Job {
	
	private DBI dbi;
	private Handle handle;
	private FetchJobDao fetchJobDao;
	private MapDao mapDao;
	private MapRequestDao mapRequestDao;
	private SmtpClient smtpClient = new SmtpClient();
	
	public ComparisonJob() {
		dbi = new DBI("jdbc:mysql://localhost/Maps", "root", "");
		dbi.registerContainerFactory(new OptionalContainerFactory());
		handle = dbi.open();
		fetchJobDao = handle.attach(FetchJobDao.class);
		mapDao = handle.attach(MapDao.class);
		mapRequestDao = handle.attach(MapRequestDao.class);
	}
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("Running comparisons");
		Optional<Integer> mostRecentFetchJob = getMostRecentFetchJob();
		Optional<Integer> previousFetchJob = getPreviousFetchJob();
		if(mostRecentFetchJob.isPresent()) {
			compareFetchJob(mostRecentFetchJob.get());
		} else {
			System.out.println("No jobs have been run yet");
			System.exit(0);
		}
		if(previousFetchJob.isPresent()) {
			compareFetchJobs(mostRecentFetchJob.get(), previousFetchJob.get());
		} else {
			System.out.println("Only 1 job has been run, therefore cannot compare 2 jobs");
		}
		System.exit(0);
	}
	
	public void compareFetchJob(int fetchJob) {
		// is there any difference between countries?
		List<Map> maps = mapDao.getMaps();//mapDao.getMapsFromFetchJob(fetchJob);
		// need to check hash of images
		
		// key -> hash ---- should probably be changes to MapRequest
		// value -> map
		// will show which maps have the same image and which ones do not
		Multimap<String, Map> mapHashes = HashMultimap.create();;
		for(Map map: maps) {
			mapHashes.put(map.getHash(), map);
		}
		
		// is there only 1 key?
		// no personalization..
		if(mapHashes.keySet().size() < 2) {
			System.out.println("No personalization for this map");
		} else {
			System.out.println("Personalization has been found");
			String emailBody = String.format("Hash\t\t\t\t\t\t\t\tPath\n");
			for(String hash: mapHashes.asMap().keySet()) {
				emailBody += String.format("%s\t%s\n", hash, mapHashes.get(hash).iterator().next().getPath());
			}
			try {
			MapsEmail email = new MapsEmailBuilder()
				.setSubject("Personalization Between Countries")
				.setmMessage(emailBody)
				.addTo("mapspersonalization@gmail.com")
				.build();
			smtpClient.sendEmail(email);
			} catch(IOException e) {
				System.out.println("Cannot read properties file");
				e.printStackTrace();
			}
		}
	}
	
	public HashMap<Region, Integer> pairRegionWithMapRequest() {
		HashMap<Region, Integer> regions = Maps.newHashMap();
		for(Region region: Lists.newArrayList(Region.en)){//Region.values()) {
			regions.put(region, mapRequestDao.getRequestByRegion(region.toString()).iterator().next());
		}
		return regions;
	}
	
	public void compareFetchJobs(int fetchJob1, int fetchJob2) {
		// is there any difference between the two given fetch jobs?
		HashMap<Region, Integer> regions = pairRegionWithMapRequest();
		for(Region region: Lists.newArrayList(Region.en)) {//Region.values()) {
			List<Map> job1Map = mapDao.getMapsFromFetchJobAndRegion(fetchJob1, regions.get(region));
			List<Map> job2Map =  mapDao.getMapsFromFetchJobAndRegion(fetchJob2, regions.get(region));
			if(job1Map.size() == job2Map.size() && job2Map.size() == 1) {
				Map map1 = job1Map.get(0);
				Map map2 = job2Map.get(0);
				if (!map1.getHash().equals(map2.getHash())) {
					System.out.println("PERSONALIZATION");
				}
			} else {
				System.out.println("Does not know what to compare");
			}
		}
	}
	
	public Optional<Integer> getMostRecentFetchJob() {
		List<Integer> fetchJobs = fetchJobDao.getFetchJobs();
		if(fetchJobs.isEmpty()) {
			return Optional.absent();
		}
		return Optional.fromNullable(fetchJobs.get(0));
	}
	
	public Optional<Integer> getPreviousFetchJob() {
		List<Integer> fetchJobs = fetchJobDao.getFetchJobs();
		if(fetchJobs.isEmpty() || fetchJobs.size() < 2) {
			return Optional.absent();
		}
		return Optional.fromNullable(fetchJobs.get(1));
	}
}
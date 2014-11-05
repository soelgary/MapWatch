package com.gsoeller.personalization.maps.jobs;

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
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.gsoeller.personalization.maps.dao.FetchJobDao;
import com.gsoeller.personalization.maps.dao.MapDao;
import com.gsoeller.personalization.maps.data.Map;
import com.gsoeller.personalization.maps.data.Region;

public class ComparisonJob implements Job {
	
	private DBI dbi;
	private Handle handle;
	private FetchJobDao fetchJobDao;
	private MapDao mapDao;
	
	public ComparisonJob() {
		dbi = new DBI("jdbc:mysql://localhost/Maps", "root", "");
		dbi.registerContainerFactory(new OptionalContainerFactory());
		handle = dbi.open();
		fetchJobDao = handle.attach(FetchJobDao.class);
		mapDao = handle.attach(MapDao.class);
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
		List<Map> maps = mapDao.getMapsFromFetchJob(fetchJob);
		// need to check hash of images
		
		// key -> hash
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
		}
	}
	
	public void compareFetchJobs(int fetchJob1, int fetchJob2) {
		// is there any difference between the two given fetch jobs?
		HashMap<Region, List<Map>> regionHashMap = Maps.newHashMap();
		for(Region region: regionHashMap.keySet()) {
			List<Map> maps = regionHashMap.get(region);
			if(maps.size() < 2) {
				System.out.println("No personalization has occurred");
			} else {
				Optional<String> hash = Optional.absent();
				for(Map currentMap: maps) {
					if(hash.isPresent() && currentMap.getHash().equals(hash.get())) {
						System.out.println("Personalization has occurred");
					} else if(!hash.isPresent()) {
						hash = Optional.of(currentMap.getHash());
					}
				}
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
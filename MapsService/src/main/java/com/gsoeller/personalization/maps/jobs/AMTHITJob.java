package com.gsoeller.personalization.maps.jobs;

import java.util.logging.Logger;

import com.google.common.base.Optional;
import com.gsoeller.personalization.maps.MapsLogger;
import com.gsoeller.personalization.maps.data.Map;
import com.gsoeller.personalization.maps.managers.CrawlManager;

public class AMTHITJob {
	
	private CrawlManager crawlManager;
	
	private Logger LOG = MapsLogger.createLogger("com.gsoeller.personalization.maps.jobs.AMTHITJob");
	
	/*
	 * 
	 * NEXT I NEED TO ADD THIS TO THE CLI AND TEST IT THEN FINALLY RUN IT FOR BING
	 * 
	 * 
	 */
	public AMTHITJob(final CrawlManager crawlManager) {
		this.crawlManager = crawlManager;
	}
	
	public void execute(int fetchJob) {
		int oldFetchJob = fetchJob - 1;
		if(fetchJob < 2) {
			throw new IllegalArgumentException(String.format("Cannot compare fetch job to non-existence crawl -- %s", fetchJob));
		}
		int numMapRequests = crawlManager.getNumRequests();
		for(int requestId = 1; requestId <= numMapRequests; requestId++) {
			Optional<Map> baselineMap = crawlManager.getMap(requestId, oldFetchJob);
			Optional<Map> updatedMap = crawlManager.getMap(requestId, fetchJob);
			if(!baselineMap.isPresent()) {
				LOG.severe("baseline is not present");
			}
			if(!updatedMap.isPresent()) {
				LOG.severe("updated is not present");
			}
			if(baselineMap.isPresent() && updatedMap.isPresent() && !baselineMap.get().getHash().equals(updatedMap.get().getHash())) {
				int similarUpdatesCount = crawlManager.countSimilarUpdates(baselineMap.get(), updatedMap.get());
				if(similarUpdatesCount < 1) {
					LOG.info(String.format("Creating a new update for map request, `%s`, and fetch job, `%s`", requestId, fetchJob));
					crawlManager.addUpdate(baselineMap.get(), updatedMap.get());
				}
			}
		}
		LOG.info("Finished");
	}
}
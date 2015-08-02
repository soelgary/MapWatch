package com.gsoeller.personalization.maps.jobs;

import java.io.IOException;
import java.util.logging.Logger;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.common.base.Optional;

import com.gsoeller.personalization.maps.MapsLogger;
import com.gsoeller.personalization.maps.amt.HitGenerator;
import com.gsoeller.personalization.maps.data.Map;
import com.gsoeller.personalization.maps.data.MapChange;
import com.gsoeller.personalization.maps.data.MapProvider;
import com.gsoeller.personalization.maps.managers.AMTHITManager;

public class AMTHITJob {

	private MapProvider mapProvider = MapProvider.google;
	private int numMapRequests = 156060;
	
	private HitGenerator hitGenerator;
	
	private AMTHITManager manager;
	
	private Logger LOG = MapsLogger.createLogger("com.gsoeller.personalization.maps.jobs.AMTHITJob");
	
	public AMTHITJob(final AMTHITManager manager) throws IOException {
		hitGenerator = new HitGenerator();
		this.manager = manager;
	}
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		int fetchJob = Integer.parseInt((String)(context.getJobDetail().getJobDataMap().get("fetchJob")));
		LOG.info(String.format("%s", numMapRequests));
		int baselineFetchJob = fetchJob - 1;
		for(int i = 1; i <= numMapRequests; i++) {
			int requestId = i;
			Optional<Map> baselineMap = manager.getMap(requestId, baselineFetchJob);
			Optional<Map> updatedMap = manager.getMap(requestId, fetchJob);
			if(!baselineMap.isPresent()) {
				System.out.println("baseline is not present");
			}
			if(!updatedMap.isPresent()) {
				System.out.println("updated is not present");
			}
			if(baselineMap.isPresent() && updatedMap.isPresent()) {
				if(!baselineMap.get().getHash().equals(updatedMap.get().getHash())) {
					// need to also check that a similar update doesnt already exist
					int similarUpdatesCount = manager.countUpdatesWithHashSets(baselineMap.get().getHash(), updatedMap.get().getHash());
					if(similarUpdatesCount < 1) {
						LOG.info(String.format("Creating a new update for map request, `%s`, and fetch job, `%s`", requestId, fetchJob));
						// need to add to a HIT
						MapChange change = new MapChange.MapChangeBuilder(baselineMap.get(), updatedMap.get(), MapProvider.google).build();
						try {
							hitGenerator.addUpdate(mapProvider, change);
						} catch (Exception e) {
							e.printStackTrace();
							throw new JobExecutionException("Unable to add update");
						}
					} else {
						LOG.info(String.format("Too many updates created with hashes, `%s`, and `%s`", baselineMap.get().getHash(), updatedMap.get().getHash()));
					}
					
				}
			} else {
				LOG.info(String.format("Both maps arent available for fetch job %s and map request %s", fetchJob, requestId));
			}
			
		}
		LOG.info("Finished amt hit job");
	}
}
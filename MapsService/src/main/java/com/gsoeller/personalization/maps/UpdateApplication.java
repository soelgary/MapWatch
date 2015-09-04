package com.gsoeller.personalization.maps;

import java.util.logging.Logger;

import com.gsoeller.personalization.maps.data.MapProvider;
import com.gsoeller.personalization.maps.jobs.AMTHITJob;
import com.gsoeller.personalization.maps.managers.BingCrawlManager;
import com.gsoeller.personalization.maps.managers.CrawlManager;
import com.gsoeller.personalization.maps.managers.GoogleCrawlManager;

import io.dropwizard.setup.Environment;

public class UpdateApplication extends AbstractApplication {

	private Logger LOG = MapsLogger.createLogger("com.gsoeller.personalization.maps.UpdateApplication");
	
	@Override
	public void run(MapsConfiguration configuration, Environment environment)
			throws Exception {
		super.initializeDaos(configuration, environment);
		MapProvider mapProvider = MapProvider.valueOf(configuration.getMapProvider());
		int fetchJob = configuration.getFetchJob();
		LOG.info(String.format("Adding updates to database for fetch job %s for %s", fetchJob, mapProvider));
		CrawlManager crawlManager;
		if(mapProvider == MapProvider.google) {
			crawlManager = new GoogleCrawlManager(googleMapRequestDao, 
					googleFetchJobDao, 
					googleMapUpdateDao, 
					googleMapDao, 
					googleAMTManager, 
					googleControlManager,
					googleHITUpdateManager);
		} else {
			crawlManager = new BingCrawlManager(bingMapRequestDao, 
					bingFetchJobDao, 
					bingMapUpdateDao, 
					bingMapDao, 
					bingAMTManager, 
					bingControlManager,
					bingHITUpdateManager);
		}
		LOG.info("Successfully configured crawl manager");
		AMTHITJob amtHITJob = new AMTHITJob(crawlManager);
		LOG.info("Running hit job...");
		amtHITJob.execute(fetchJob);
	}
}

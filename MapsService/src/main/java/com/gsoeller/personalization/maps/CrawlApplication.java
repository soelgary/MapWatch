package com.gsoeller.personalization.maps;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.gsoeller.personalization.maps.jobs.FetchJob;
import com.gsoeller.personalization.maps.managers.CrawlManager;
import com.gsoeller.personalization.maps.managers.GoogleCrawlManager;

import io.dropwizard.setup.Environment;

public class CrawlApplication extends AbstractApplication {

	@Override
	public void run(MapsConfiguration configuration, Environment environment) throws Exception {
		super.initializeDaos(configuration, environment);
		CrawlManager crawlManager = new GoogleCrawlManager(googleMapRequestDao, 
				googleFetchJobDao, 
				googleMapUpdateDao, 
				googleMapDao, 
				googleAMTManager, 
				googleControlManager,
				googleHITUpdateManager);
		SchedulerFactory schedFact = new StdSchedulerFactory();
		Scheduler sched = schedFact.getScheduler();
		sched.start();

		JobDetail job = JobBuilder.newJob(FetchJob.class)
				.withIdentity("Fetch Job", "group1").build();
		
		job.getJobDataMap().put("mapNumber", 1);
		job.getJobDataMap().put("manager", crawlManager);
		
		Trigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity("Fetch Trigger", "group1")
				.startNow()
				.withSchedule(SimpleScheduleBuilder.simpleSchedule()
						.withIntervalInMinutes(60)
						.repeatForever())
				.build();
		sched.scheduleJob(job, trigger);
		while(true){}
	}
}

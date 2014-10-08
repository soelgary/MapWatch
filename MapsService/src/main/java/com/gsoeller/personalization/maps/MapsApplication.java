package com.gsoeller.personalization.maps;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.skife.jdbi.v2.DBI;

import com.gsoeller.personalization.maps.dao.MapRequestDao;
import com.gsoeller.personalization.maps.jobs.FetchJob;
import com.gsoeller.personalization.maps.resources.MapsResource;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class MapsApplication extends Application<MapsConfiguration> {

	private MapRequestDao mapRequestDao;
	
	public static void main(String[] args) throws Exception {
		new MapsApplication().run(args);
	}

	@Override
	public void initialize(Bootstrap<MapsConfiguration> bootstrap) {
		bootstrap.addBundle(new MigrationsBundle<MapsConfiguration>() {
			public DataSourceFactory getDataSourceFactory(
					MapsConfiguration configuration) {
				return configuration.getDataSourceFactory();
			}
		});
	}

	@Override
	public void run(MapsConfiguration config, Environment environment)
			throws Exception {
		final DBIFactory factory = new DBIFactory();
		final DBI jdbi = factory.build(environment,
				config.getDataSourceFactory(), "mysql");
		mapRequestDao = jdbi.onDemand(MapRequestDao.class);
		environment.jersey().register(new MapsResource(mapRequestDao));
		startFetchJob();
	}

	private void startFetchJob() throws SchedulerException {
		SchedulerFactory schedFact = new StdSchedulerFactory();
		Scheduler sched = schedFact.getScheduler();
		sched.start();

		JobDetail job = JobBuilder.newJob(FetchJob.class)
				.withIdentity("Fetch Job", "group1").build();
		
		job.getJobDataMap().put("MapRequestDao", mapRequestDao);

		Trigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity("Fetch Trigger", "group1")
				.startNow()
				//.withSchedule(SimpleScheduleBuilder.simpleSchedule()
				//				.withIntervalInSeconds(5).repeatForever())
				.build();
		sched.scheduleJob(job, trigger);
	}

}

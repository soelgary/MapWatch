package com.gsoeller.personalization.maps;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.skife.jdbi.v2.DBI;

import com.gsoeller.personalization.maps.dao.LocationDao;
import com.gsoeller.personalization.maps.dao.MapRequestDao;
import com.gsoeller.personalization.maps.jobs.ComparisonJob;
import com.gsoeller.personalization.maps.jobs.FetchJob;
import com.gsoeller.personalization.maps.jobs.RequestJob;
import com.gsoeller.personalization.maps.resources.MapsResource;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class MapsApplication extends Application<MapsConfiguration> {

	private MapRequestDao mapRequestDao;
	private LocationDao locationDao;
	
	public static void main(String[] args) throws Exception {
		PropertiesLoader propLoader = new PropertiesLoader();
		Options options = new Options();
		options.addOption("s", false, "Run the dropwizard server to get REST api access");
		options.addOption("create", false, "Create the requests to the maps api to be run every time period");
		options.addOption("fetch", false, "Run the job to fetch requests for each map");
		options.addOption("compare", false, "Run the job to compare the latest fetched maps for personalization");
		Option option = new Option("h", "Help message");
		
		options.addOption(option);
		CommandLineParser parser = new BasicParser();
		CommandLine cmd = parser.parse(options, args);
		
		if(cmd.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Maps Personalization", options);
		} else if(cmd.hasOption("s")) {
			new MapsApplication().run(new String[] {"server", propLoader.getProperty("config")});
		} else if(cmd.hasOption("create")) {
			startRequestJob();
		} else if(cmd.hasOption("fetch")) {
			System.out.println("fetching");
			startFetchJob();
		} else if(cmd.hasOption("compare")) {
			startCompareJob();
		}
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
		locationDao = jdbi.onDemand(LocationDao.class);
		environment.jersey().register(new MapsResource(mapRequestDao, locationDao));
	}
	
	private static void startCompareJob() throws SchedulerException {
		SchedulerFactory schedFact = new StdSchedulerFactory();
		Scheduler sched = schedFact.getScheduler();
		sched.start();
		
		JobDetail job = JobBuilder.newJob(ComparisonJob.class)
				.withIdentity("Comparison Job", "group1").build();
		
		Trigger trigger = TriggerBuilder.newTrigger()
				.withIdentity("Comparison Trigger", "group1")
				.startNow()
				.build();
		sched.scheduleJob(job, trigger);
	}
	
	private static void startRequestJob() throws SchedulerException {
		SchedulerFactory schedFact = new StdSchedulerFactory();
		Scheduler sched = schedFact.getScheduler();
		sched.start();

		JobDetail job = JobBuilder.newJob(RequestJob.class)
				.withIdentity("Request Job", "group1").build();

		Trigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity("Request Trigger", "group1")
				.startNow()
				//.withSchedule(SimpleScheduleBuilder.simpleSchedule()
				//				.withIntervalInSeconds(5).repeatForever())
				.build();
		sched.scheduleJob(job, trigger);
	}

	private static void startFetchJob() throws SchedulerException {
		SchedulerFactory schedFact = new StdSchedulerFactory();
		Scheduler sched = schedFact.getScheduler();
		sched.start();

		JobDetail job = JobBuilder.newJob(FetchJob.class)
				.withIdentity("Fetch Job", "group1").build();
		
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

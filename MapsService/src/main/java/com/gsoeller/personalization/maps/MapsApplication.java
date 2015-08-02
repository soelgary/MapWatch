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
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.gsoeller.personalization.maps.jobs.FetchJob;
import com.gsoeller.personalization.maps.jobs.RequestJob;

public class MapsApplication {
	
	public static void main(String[] args) throws Exception {
		new CrawlApplication().run(args);
		Options options = new Options();
		options.addOption("test", false, "Run the application/job using the maps.properties file");
		options.addOption("s", false, "Run the dropwizard server to get REST api access");

		Option option = new Option("h", "Help message");
		
		Option fetch = new Option("fetch", "Run the job to fetch requests for each map with the given map number");
		fetch.hasArg();
		fetch.setType(Integer.class);
		fetch.setArgs(1);
		options.addOption(fetch);
		
		Option mapProvider = new Option("mp", "Map provider to run the job for");
		mapProvider.hasArg();
		mapProvider.setType(String.class);
		mapProvider.setArgs(1);
		options.addOption(mapProvider);
		
		Option create = new Option("create", "Create the requests to the maps api to be run every time period");
		create.hasArg();
		create.setType(Integer.class);
		create.setArgs(1);
		options.addOption(create);
		
		Option update = new Option("update", "Check for updates between jobs");
		update.hasArg();
		update.setType(Integer.class);
		update.setArgs(1);
		options.addOption(update);
		
		options.addOption(option);
		CommandLineParser parser = new BasicParser();
		CommandLine cmd = parser.parse(options, args);
		
		if(cmd.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Maps Personalization", options);
		} else if(cmd.hasOption("create")) {
			String mapNumber = (String) cmd.getOptionValue("create");
			String map = (String) cmd.getOptionValue("mp");
			startRequestJob(Integer.parseInt(mapNumber), map);
		} else if(cmd.hasOption("fetch")) {
			System.out.println("fetching");
			String mapNumber = (String) cmd.getOptionValue("fetch");
			String map = (String) cmd.getOptionValue("mp");
			//startFetchJob(Integer.parseInt(mapNumber), map);
		} else if(cmd.hasOption("update")) {
			String fetchJob = (String) cmd.getOptionValue("update");
			startUpdateJob(fetchJob);
		}
		else {
			new WebApplication().run(args);
		}
	}
	
	private static void startRequestJob(int mapNumber, String mapProvider) throws SchedulerException {
		SchedulerFactory schedFact = new StdSchedulerFactory();
		Scheduler sched = schedFact.getScheduler();
		sched.start();

		JobDetail job = JobBuilder.newJob(RequestJob.class)
				.withIdentity("Request Job", "group1").build();

		job.getJobDataMap().put("mapNumber", mapNumber);
		job.getJobDataMap().put("mapProvider", mapProvider);
		
		Trigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity("Request Trigger", "group1")
				.startNow()
				.build();
		sched.scheduleJob(job, trigger);
	}
	
	private static void startUpdateJob(String fetchJob) throws SchedulerException {
		//final AMTHITManager manager = new AMTHITManager();
		//AMTHITJob job = new AMTHITJob();		
	}
}
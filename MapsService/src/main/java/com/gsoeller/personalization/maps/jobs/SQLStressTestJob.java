package com.gsoeller.personalization.maps.jobs;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.gsoeller.personalization.maps.dao.GoogleMapDao;

public class SQLStressTestJob implements Job {

	final MetricRegistry metrics = new MetricRegistry();
	private final Timer responses = metrics.timer("responses");
	final ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
			.convertRatesTo(TimeUnit.SECONDS)
			.convertDurationsTo(TimeUnit.MILLISECONDS).build();
	
	private GoogleMapDao googleMapDao;

	public SQLStressTestJob() throws IOException {
		googleMapDao = new GoogleMapDao();
	}
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		int numQueries = (Integer)context.getJobDetail().getJobDataMap().get("numQueries");
		System.out.println(String.format("Running Sql Stress Tests On %d Queries...", numQueries));
		reporter.start(1, TimeUnit.MINUTES);
		for(int i = 0; i < numQueries; i++) {
			runQuery(i);
		}
		reporter.stop();
		reporter.report();
		System.exit(0);
	}

	public void runQuery(int id) {
		System.out.println("Running Query");
		final Timer.Context context = responses.time();
		try {
			googleMapDao.getMapMostRecentWithMapRequestId(id);
		} finally {
			context.stop();
		}
	}

}

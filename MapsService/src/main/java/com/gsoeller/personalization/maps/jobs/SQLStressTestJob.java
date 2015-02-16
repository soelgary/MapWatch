package com.gsoeller.personalization.maps.jobs;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.gsoeller.personalization.maps.dao.BingMapDao;
import com.gsoeller.personalization.maps.dao.GoogleMapDao;

public class SQLStressTestJob implements Job {

	final MetricRegistry metrics1 = new MetricRegistry();
	final MetricRegistry metrics2 = new MetricRegistry();
	final MetricRegistry metrics3 = new MetricRegistry();
	final MetricRegistry metrics4 = new MetricRegistry();
	private final Timer responses1 = metrics1.timer("responses");
	private final Timer responses2 = metrics2.timer("responses");
	private final Timer responses3 = metrics3.timer("responses");
	private final Timer responses4 = metrics4.timer("responses");
	final ConsoleReporter reporter1 = ConsoleReporter.forRegistry(metrics1)
			.convertRatesTo(TimeUnit.SECONDS)
			.convertDurationsTo(TimeUnit.MILLISECONDS).build();
	final ConsoleReporter reporter2 = ConsoleReporter.forRegistry(metrics2)
			.convertRatesTo(TimeUnit.SECONDS)
			.convertDurationsTo(TimeUnit.MILLISECONDS).build();
	final ConsoleReporter reporter3 = ConsoleReporter.forRegistry(metrics3)
			.convertRatesTo(TimeUnit.SECONDS)
			.convertDurationsTo(TimeUnit.MILLISECONDS).build();
	final ConsoleReporter reporter4 = ConsoleReporter.forRegistry(metrics4)
			.convertRatesTo(TimeUnit.SECONDS)
			.convertDurationsTo(TimeUnit.MILLISECONDS).build();
	
	private Random rand = new Random();
	
	private GoogleMapDao googleMapDao;
	private BingMapDao bingMapDao;
	
	private final int MAX = 1200000;
	private final int MIN = 0;
	
	public SQLStressTestJob() throws IOException {
		googleMapDao = new GoogleMapDao();
		bingMapDao = new BingMapDao();
	}
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		int numQueries = (Integer)context.getJobDetail().getJobDataMap().get("numQueries");
		System.out.println(String.format("Running Sql Stress Tests On %d Queries...", numQueries));
		testGoogleGetMapMostRecentWithMapRequestId(numQueries);
		testGoogleGetMapFromFetchJobByMapRequest(numQueries);
		testBingGetMapMostRecentWithMapRequestId(numQueries);
		testBingGetMapFromFetchJobByMapRequest(numQueries);
		System.exit(0);
	}
	
	private void testGoogleGetMapMostRecentWithMapRequestId(int numQueries) {
		System.out.println(String.format("Running Sql Stress Test For Google GetMapMostRecentWithMapRequestId On %d Queries...", numQueries));
		reporter1.start(1, TimeUnit.MINUTES);
		for(int i = 0; i < numQueries; i++) {
			int randomNum = nextRandom();
			runGoogleGetMapMostRecentWithMapRequestId(randomNum);
		}
		reporter1.stop();
		reporter1.report();
	}
	
	private void runGoogleGetMapMostRecentWithMapRequestId(int id) {
		final Timer.Context context = responses1.time();
		try {
			googleMapDao.getMapMostRecentWithMapRequestId(id);
		} finally {
			context.stop();
		}
	}
	
	private void testGoogleGetMapFromFetchJobByMapRequest(int numQueries) {
		System.out.println(String.format("Running Sql Stress Test For Google GetMapFromFetchJobByMapRequest On %d Queries...", numQueries));
		reporter2.start(1, TimeUnit.MINUTES);
		for(int i = 0; i < numQueries; i++) {
			int randomNum = nextRandom();
			runGoogleGetMapFromFetchJobByMapRequest(randomNum);
		}
		reporter2.stop();
		reporter2.report();
	}
	
	private void runGoogleGetMapFromFetchJobByMapRequest(int mapRequest) {
		final Timer.Context context = responses2.time();
		try {
			googleMapDao.getMapFromFetchJobByMapRequest(1, mapRequest);
		} finally {
			context.stop();
		}
	}

	private void testBingGetMapMostRecentWithMapRequestId(int numQueries) {
		System.out.println(String.format("Running Sql Stress Test For Bing GetMapMostRecentWithMapRequestId On %d Queries...", numQueries));
		reporter3.start(1, TimeUnit.MINUTES);
		for(int i = 0; i < numQueries; i++) {
			int randomNum = nextRandom();
			runBingGetMapMostRecentWithMapRequestId(randomNum);
		}
		reporter3.stop();
		reporter3.report();
	}
	
	private void runBingGetMapMostRecentWithMapRequestId(int id) {
		final Timer.Context context = responses3.time();
		try {
			bingMapDao.getMapMostRecentWithMapRequestId(id);
		} finally {
			context.stop();
		}
	}
	
	private void testBingGetMapFromFetchJobByMapRequest(int numQueries) {
		System.out.println(String.format("Running Sql Stress Test For Bing GetMapFromFetchJobByMapRequest On %d Queries...", numQueries));
		reporter4.start(1, TimeUnit.MINUTES);
		for(int i = 0; i < numQueries; i++) {
			int randomNum = nextRandom();
			runBingGetMapFromFetchJobByMapRequest(randomNum);
		}
		reporter4.stop();
		reporter4.report();
	}
	
	private void runBingGetMapFromFetchJobByMapRequest(int mapRequest) {
		final Timer.Context context = responses4.time();
		try {
			bingMapDao.getMapFromFetchJobByMapRequest(1, mapRequest);
		} finally {
			context.stop();
		}
	}

	private int nextRandom() {
		return rand.nextInt((MAX - MIN) + 1) + MIN;
	}
}

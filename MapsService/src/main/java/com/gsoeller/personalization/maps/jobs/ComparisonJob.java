package com.gsoeller.personalization.maps.jobs;

import java.util.List;

import io.dropwizard.jdbi.OptionalContainerFactory;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

import com.google.common.base.Optional;
import com.gsoeller.personalization.maps.dao.FetchJobDao;

public class ComparisonJob implements Job {
	
	private DBI dbi;
	private Handle handle;
	private FetchJobDao fetchJobDao;
	
	public ComparisonJob() {
		dbi = new DBI("jdbc:mysql://localhost/Maps", "root", "");
		dbi.registerContainerFactory(new OptionalContainerFactory());
		handle = dbi.open();
		fetchJobDao = handle.attach(FetchJobDao.class);
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
	}
	
	public void compareFetchJobs(int fetchJob1, int fetchJob2) {
		// is there any difference between the two given fetch jobs?
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
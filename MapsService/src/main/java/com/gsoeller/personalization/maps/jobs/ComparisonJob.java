package com.gsoeller.personalization.maps.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ComparisonJob implements Job {

	public void execute(JobExecutionContext context) throws JobExecutionException {
		// This job will comapre images to find personalization in the last round of fetches
		System.out.println("Running comparisons");
		
		System.exit(0);
	}

}

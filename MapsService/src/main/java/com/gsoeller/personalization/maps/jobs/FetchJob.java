package com.gsoeller.personalization.maps.jobs;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.gsoeller.personalization.maps.dao.MapRequestDao;
import com.gsoeller.personalization.maps.data.MapRequest;

public class FetchJob implements Job {

	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("Fetching pages");
		MapRequestDao dao = (MapRequestDao)context.getJobDetail().getJobDataMap().get("MapRequestDao");
		List<MapRequest> requests = dao.getRequests();
		for(MapRequest request: requests) {
			System.out.println(request.buildRequestUrl());
		}
		
	}

}

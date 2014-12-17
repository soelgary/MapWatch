package com.gsoeller.personalization.maps.dao;

import com.google.common.base.Optional;

public interface FetchJobDao {
	boolean isLastJobFinished();
	
	int createFetchJob(int mapNumber);
	
	Optional<Integer> getLastFetchJob();
	
	void finishFetchJob(int fetchJob);
}

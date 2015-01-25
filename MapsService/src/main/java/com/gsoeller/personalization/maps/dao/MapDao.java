package com.gsoeller.personalization.maps.dao;

import com.google.common.base.Optional;
import com.gsoeller.personalization.maps.data.Map;

public interface MapDao {
	public Optional<Integer> getLastMap(int currentFetchJob);
	
	public void saveMap(boolean hasChanged, int id, String path, String hash, int fetchJob);
	
	public Optional<String> getPathWithHash(String hash);
	
	public Optional<Map> getMapMostRecentWithMapRequestId(int mapRequestId);
	
	public Optional<Map> getMapFromFetchJobByMapRequest(int fetchJob, int mapRequest);
	
	public boolean containsHash(String hash);
}

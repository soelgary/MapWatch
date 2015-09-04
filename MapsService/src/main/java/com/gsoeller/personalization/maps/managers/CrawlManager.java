package com.gsoeller.personalization.maps.managers;

import java.util.List;

import com.google.common.base.Optional;
import com.gsoeller.personalization.maps.data.Map;
import com.gsoeller.personalization.maps.data.MapChange;
import com.gsoeller.personalization.maps.data.MapProvider;
import com.gsoeller.personalization.maps.data.MapRequest;


public interface CrawlManager {
	public boolean isLastJobFinished();
	
	public int createFetchJob(int mapNumber);
	
	public Optional<Integer> getLastFetchJob();
	
	public Optional<Integer> getLastMap(int currentFetchJob);
	
	public void finishFetchJob(int fetchJob);
	
	public Optional<Map> getMapMostRecentWithMapRequestId(int mapRequestId);
	
	public int save(int oldMap, int newMap);
	
	public Optional<MapChange> getUpdate(int id);
	
	public List<MapRequest> getRequests(int limit, int offset, int mapNumber);
	
	public int saveMap(boolean hasChanged, int id, String path, String hash, int fetchJob);
	
	public Optional<String> getPathWithHash(String hash);
	
	public boolean containsHash(String hash);
	
	public MapProvider getMapProvider();
	
	public Optional<Map> getMap(int id);
	
	public Optional<Map> getMap(int id, int fetchJob);
	
	public void addUpdate(Map oldMap, Map newMap);
	
	public int getNumRequests();
	
	public int countSimilarUpdates(Map oldMap, Map newMap);
	
}
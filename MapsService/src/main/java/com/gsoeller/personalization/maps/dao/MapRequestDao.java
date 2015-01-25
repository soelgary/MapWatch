package com.gsoeller.personalization.maps.dao;

import java.util.List;

import com.google.common.base.Optional;
import com.gsoeller.personalization.maps.data.BingMap;
import com.gsoeller.personalization.maps.data.Map;
import com.gsoeller.personalization.maps.data.MapRequest;
import com.gsoeller.personalization.maps.data.Region;

public interface MapRequestDao {
	public List<MapRequest> getRequests(int limit, int offset, int mapNumber);

	public void addMapRequest(MapRequest mapRequest);

	public Optional<Region> getRegion(int mapRequest);
	
	public int countTiles();
	
	public List<Integer> getTileNumbers();
	
	public List<Integer> getMapRequestsFromTileNumber(int tileNumber);
	
	public Optional<BingMap> getMapFromFetchJobAndMapRequest(int fetchJob, int mapRequestId);
	
	public List<Integer> getMapRequestsbyLocation(int location);
}
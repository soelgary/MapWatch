package com.gsoeller.personalization.maps.dao;

import java.util.List;
import com.google.common.base.Optional;
import com.gsoeller.personalization.maps.data.MapRequest;
import com.gsoeller.personalization.maps.data.Region;

public interface MapRequestDao {
	public List<MapRequest> getRequests(int limit, int offset, int mapNumber);

	public void addMapRequest(MapRequest mapRequest);

	public Optional<Region> getRegion(int mapRequest);
}
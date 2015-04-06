package com.gsoeller.personalization.maps.dao;

import com.google.common.base.Optional;
import com.gsoeller.personalization.maps.data.MapChange;

public interface MapUpdateDao {

	public int save(int oldMap, int newMap);
	
	public Optional<MapChange> getUpdate(int id);
	
}

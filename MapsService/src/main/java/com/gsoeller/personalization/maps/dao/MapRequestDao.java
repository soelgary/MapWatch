package com.gsoeller.personalization.maps.dao;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import com.gsoeller.personalization.maps.data.MapRequest;
import com.gsoeller.personalization.maps.mappers.MapRequestMapper;

public interface MapRequestDao {
	
	@SqlQuery("select * from MapRequest")
	@Mapper(MapRequestMapper.class)
	public List<MapRequest> getRequests();

}

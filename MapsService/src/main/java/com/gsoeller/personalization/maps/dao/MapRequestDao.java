package com.gsoeller.personalization.maps.dao;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import com.google.common.base.Optional;
import com.gsoeller.personalization.maps.data.MapRequest;
import com.gsoeller.personalization.maps.mappers.MapRequestMapper;

public interface MapRequestDao {
	
	@SqlQuery("select * from MapRequest cross join location where MapRequest.location = Location.id;")
	@Mapper(MapRequestMapper.class)
	public List<MapRequest> getRequests();
	
	@SqlQuery("select * from MapRequest cross join location where MapRequest.location = Location.id;")
	@Mapper(MapRequestMapper.class)
	public List<MapRequest> getRequest(@Bind("id") int id);

	@SqlUpdate("insert into MapRequest (location, zoom, xDimension, yDimension, region, language) values (:location, :zoom, :xDimension, :yDimension, :region, :language)")
	@GetGeneratedKeys
	public int addMapRequest(@Bind("location") int location, @Bind("zoom") int zoom, @Bind("xDimension") int xDimension, @Bind("yDimension") int yDimension, @Bind("region") String region, @Bind("language") String language);
}

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
	
	@SqlQuery("select * from MapRequest")
	@Mapper(MapRequestMapper.class)
	public List<MapRequest> getRequests();
	
	@SqlQuery("select * from MapRequest where id=:id")
	public Optional<MapRequest> getRequest(@Bind("id") int id);

	@SqlUpdate("insert into MapRequest (latitude, longitude, zoom, xDimension, yDimension, region, language) values (:latitude, :longitude, :zoom, :xDimension, :yDimension, :region, :language)")
	@GetGeneratedKeys
	public int addMapRequest(@Bind("latitude") double latitude, @Bind("longitude") double longitude, @Bind("zoom") int zoom, @Bind("xDimension") int xDimension, @Bind("yDimension") int yDimension, @Bind("region") String region, @Bind("language") String language);
}

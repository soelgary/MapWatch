package com.gsoeller.personalization.maps.dao;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import com.gsoeller.personalization.maps.data.MapRequest;
import com.gsoeller.personalization.maps.mappers.GoogleMapRequestMapper;

public interface GoogleMapRequestDao {
	@SqlQuery("select * from MapRequest where MapNumber = :mapNumber limit :offset,:limit;")
	@Mapper(GoogleMapRequestMapper.class)
	public List<MapRequest> getRequests(@Bind("limit") int limit, @Bind("offset") int offset, @Bind("mapNumber") int mapNumber);
	
	@SqlUpdate("insert into MapRequest (MapNumber, latitude, longitude, zoom, xDimension, yDimension, region, language) values (:mapNumber, :latitude, :longitude, :zoom, :xDimension, :yDimension, :region, :language)")
	@GetGeneratedKeys
	public int addMapRequest(@Bind("mapNumber") int mapNumber, @Bind("latitude") double latitude, @Bind("longitude") double longitude, @Bind("zoom") int zoom, @Bind("xDimension") int xDimension, @Bind("yDimension") int yDimension, @Bind("region") String region, @Bind("language") String language);
	
	@SqlQuery("Select region from MapRequest where id = :id")
	public List<String> getRegion(@Bind("id") int id);
	
	@SqlQuery("Select id from MapRequest where location = :location")
	public List<Integer> getMapRequestsbyLocation(@Bind("location") int location);
	
	@SqlQuery("Select count(distinct latitude, longitude) from MapRequest")
	public int countTiles();
	
	@SqlQuery("Select count(distinct id) from MapRequest")
	public int countAllTiles();
	
}
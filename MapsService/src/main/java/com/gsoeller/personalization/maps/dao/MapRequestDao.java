package com.gsoeller.personalization.maps.dao;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import com.gsoeller.personalization.maps.data.MapRequest;
import com.gsoeller.personalization.maps.mappers.MapRequestMapper;

public interface MapRequestDao {
	
	@SqlQuery("select count(*) from MapRequest")
	public int countRows();
	
	@SqlQuery("select * from MapRequest where MapNumber = :mapNumber limit :offset,:limit;")
	@Mapper(MapRequestMapper.class)
	public List<MapRequest> getRequests(@Bind("limit") int limit, @Bind("offset") int offset, @Bind("mapNumber") int mapNumber);
	
	//@SqlQuery("select * from MapRequest cross join Location where MapRequest.location = Location.id;")
	//@Mapper(MapRequestMapper.class)
	//public List<MapRequest> getRequests();
	
	//@SqlQuery("select * from MapRequest cross join Location where MapRequest.location = Location.id;")
	//@Mapper(MapRequestMapper.class)
	//public List<MapRequest> getRequest(@Bind("id") int id);

	@SqlUpdate("insert into MapRequest (MapNumber, latitude, longitude, zoom, xDimension, yDimension, region, language) values (:mapNumber, :latitude, :longitude, :zoom, :xDimension, :yDimension, :region, :language)")
	@GetGeneratedKeys
	public int addMapRequest(@Bind("mapNumber") int mapNumber, @Bind("latitude") double latitude, @Bind("longitude") double longitude, @Bind("zoom") int zoom, @Bind("xDimension") int xDimension, @Bind("yDimension") int yDimension, @Bind("region") String region, @Bind("language") String language);

	//@SqlQuery("select * from MapRequest cross join Location where region = :region")
	//@Mapper(MapRequestIDMapper.class)
	//public List<Integer> getRequestByRegion(@Bind("region") String region);
	
	@SqlQuery("Select region from MapRequest where id = :id")
	public List<String> getRegion(@Bind("id") int id);
	
	//@SqlQuery("Select id from MapRequest where location = :location")
	//public List<Integer> getMapRequestsbyLocation(@Bind("location") int location);
}

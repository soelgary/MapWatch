package com.gsoeller.personalization.maps.dao;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import com.gsoeller.personalization.maps.data.BingMap;
import com.gsoeller.personalization.maps.data.MapRequest;
import com.gsoeller.personalization.maps.mappers.BingMapMapper;
import com.gsoeller.personalization.maps.mappers.BingMapRequestMapper;


public interface BingMapRequestDao {
	
	@SqlQuery("Select * from BingMapRequest where id = :id")
	@Mapper(BingMapRequestMapper.class)
	public List<MapRequest> getRequest(@Bind("id") int id);
	
	@SqlQuery("select * from BingMapRequest where MapNumber = :mapNumber limit :offset,:limit;")
	@Mapper(BingMapRequestMapper.class)
	public List<MapRequest> getRequests(@Bind("limit") int limit, @Bind("offset") int offset, @Bind("mapNumber") int mapNumber);
	
	@SqlUpdate("insert into BingMapRequest (MapNumber, region, tileNumber) values (:mapNumber, :region, :tileNumber)")
	@GetGeneratedKeys
	public int addMapRequest(@Bind("mapNumber") int mapNumber, @Bind("region") String region, @Bind("tileNumber") String tileNumber);
	
	@SqlQuery("Select region from BingMapRequest where id = :id")
	public List<String> getRegion(@Bind("id") int id);
	
	@SqlQuery("Select count(tileNumber distinct) from BingMapRequest")
	public int countTiles();
	
	@SqlQuery("Select distinct tileNumber from BingMapRequest")
	public List<Integer> getTileNumbers();
	
	@SqlQuery("Select id from BingMapRequest where tileNumber = :tileNumber")
	public List<Integer> getMapRequestsFromTileNumber(@Bind("tileNumber") int tileNumber);
	
	@SqlQuery("Select * from BingMap where fetchJob = :fetchJob && bingMapRequest = :mapRequest;")
	@Mapper(BingMapMapper.class)
	public List<BingMap> getMapFromFetchJobAndMapRequest(@Bind("fetchJob") int fetchJob, @Bind("mapRequest") int mapRequest);
}
package com.gsoeller.personalization.maps.dao;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import com.gsoeller.personalization.maps.data.BingMap;
import com.gsoeller.personalization.maps.data.Map;
import com.gsoeller.personalization.maps.mappers.BingMapWrapper;


public interface BingMapDao {
	
	@SqlQuery("Select * from BingMap where id = :id")
	@Mapper(BingMapWrapper.class)
	public List<BingMap> getMap(@Bind("id") int id);
	
	@SqlQuery("Select mapRequest from BingMap where FetchJob = :fetchJob order by dateTime desc limit 1")
	public List<Integer> getLastMap(@Bind("fetchJob") int fetchJob);
	
	@SqlUpdate("Insert into BingMap (hasChanged, mapRequest, path, hash, FetchJob) values (:hasChanged, :mapRequest, :path, :hash, :fetchJob)")
	@GetGeneratedKeys
	public int saveMap(@Bind("hasChanged") boolean hasChanged, @Bind("mapRequest") int mapRequest, @Bind("path") String path, @Bind("hash") String hash, @Bind("fetchJob") int fetchJob);
	
	@SqlQuery("Select path from BingMap where hash = :hash limit 1")
	public List<String> getPathWithHash(@Bind("hash") String hash);

	@SqlQuery("Select * from BingMap cross join BingMapRequest on bingMapRequest = mapRequest.id where BingMap.bingMapRequest = :mapRequest order by dateTime DESC limit 1;")
	@Mapper(BingMapWrapper.class)
	public List<BingMap> getMapMostRecentWithMapRequestId(@Bind("mapRequest") int mapRequest);
	
	@SqlQuery("select * from BingMap where bingMapRequest = :mapRequest && FetchJob = :fetchJob;")
	@Mapper(BingMapWrapper.class)
	public List<BingMap> getMapFromFetchJobByMapRequest(@Bind("fetchJob") int fetchJob, @Bind("mapRequest") int mapRequest);
	
	@SqlQuery("Select * from BingMap where bingMapRequest = :mapRequest order by dateTime DESC")
	@Mapper(BingMapWrapper.class)
	public List<BingMap> getMapWithMapRequestId(@Bind("mapRequest") int mapRequest);
	
	@SqlQuery("Select hash from BingMap where hash = :hash")
	public List<String> countHashes(@Bind("hash") String hash);
	
	@SqlQuery("Select * from BingMap where bingMapRequest = :mapRequest && FetchJob = :fetchJob")
	@Mapper(BingMapWrapper.class)
	public List<Map> getMap(@Bind("mapRequest") int mapRequest, @Bind("fetchJob") int fetchJob);
}
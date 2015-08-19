package com.gsoeller.personalization.maps.dao;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import com.gsoeller.personalization.maps.data.GoogleMap;
import com.gsoeller.personalization.maps.data.Map;
import com.gsoeller.personalization.maps.mappers.GoogleMapWrapper;

public interface GoogleMapDao {
	
	@SqlQuery("Select * from Map where id = :id")
	@Mapper(GoogleMapWrapper.class)
	public List<GoogleMap> getMap(@Bind("id") int id);
	
	@SqlQuery("Select mapRequest from Map where FetchJob = :fetchJob order by dateTime desc limit 1")
	public List<Integer> getLastMap(@Bind("fetchJob") int fetchJob);
	
	@SqlUpdate("Insert into Map (hasChanged, mapRequest, path, hash, FetchJob) values (:hasChanged, :mapRequest, :path, :hash, :fetchJob)")
	@GetGeneratedKeys
	public int saveMap(@Bind("hasChanged") boolean hasChanged, @Bind("mapRequest") int mapRequest, @Bind("path") String path, @Bind("hash") String hash, @Bind("fetchJob") int fetchJob);
	
	@SqlQuery("Select path from Map where hash = :hash limit 1")
	public List<String> getPathWithHash(@Bind("hash") String hash);

	@SqlQuery("Select * from Map cross join MapRequest on mapRequest = MapRequest.id where Map.mapRequest = :mapRequest order by dateTime DESC limit 1;")
	@Mapper(GoogleMapWrapper.class)
	public List<GoogleMap> getMapMostRecentWithMapRequestId(@Bind("mapRequest") int mapRequest);
	
	@SqlQuery("select * from Map where mapRequest = :mapRequest && FetchJob = :fetchJob;")
	@Mapper(GoogleMapWrapper.class)
	public List<GoogleMap> getMapFromFetchJobByMapRequest(@Bind("fetchJob") int fetchJob, @Bind("mapRequest") int mapRequest);
	
	@SqlQuery("Select * from Map where mapRequest = :mapRequest order by dateTime DESC")
	@Mapper(GoogleMapWrapper.class)
	public List<GoogleMap> getMapWithMapRequestId(@Bind("mapRequest") int mapRequest);
	
	@SqlQuery("Select hash from Map where hash = :hash")
	public List<String> countHashes(@Bind("hash") String hash);
	
	@SqlQuery("Select * from Map where mapRequest = :mapRequest && FetchJob = :fetchJob")
	@Mapper(GoogleMapWrapper.class)
	public List<Map> getMap(@Bind("mapRequest") int mapRequest, @Bind("fetchJob") int fetchJob);
}
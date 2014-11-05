package com.gsoeller.personalization.maps.dao;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import com.gsoeller.personalization.maps.data.Map;
import com.gsoeller.personalization.maps.mappers.MapWrapper;

public interface MapDao {

	@SqlQuery("Select * from Map cross join MapRequest where MapRequest.id = Map.MapRequest")
	@Mapper(MapWrapper.class)
	public List<Map> getMaps();
	
	@SqlQuery("Select * from Map cross join MapRequest where MapRequest.id = Map.MapRequest & MapRequest.MapRequest = :mapRequest order by dateTime DESC limit 1")
	@Mapper(MapWrapper.class)
	public List<Map> getMapMostRecentWithMapRequestId(@Bind("mapRequest") int mapRequest);
	
	@SqlUpdate("Insert into Map (hasChanged, mapRequest, path, hash, FetchJob) values (:hasChanged, :mapRequest, :path, :hash, :fetchJob)")
	@GetGeneratedKeys
	public int saveMap(@Bind("hasChanged") boolean hasChanged, @Bind("mapRequest") int mapRequest, @Bind("path") String path, @Bind("hash") String hash, @Bind("fetchJob") int fetchJob);
	
	@SqlUpdate("Select * from Map cross join MapRequest where MapRequest.id = Map.MapRequest & Map.FetchJob = :fetchJob")
	public List<Map> getMapsFromFetchJob(@Bind("fetchJob") int fetchJob);
}

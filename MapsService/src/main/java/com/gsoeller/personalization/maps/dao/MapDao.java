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

	@SqlQuery("Select * from Map cross join MapRequest where MapRequest.id = Map.MapRequest limit 100")
	@Mapper(MapWrapper.class)
	public List<Map> getMaps();
	
	@SqlQuery("Select * from Map cross join MapRequest where mapRequest = :mapRequest order by dateTime DESC limit 1")
	@Mapper(MapWrapper.class)
	public List<Map> getMapMostRecentWithMapRequestId(@Bind("mapRequest") int mapRequest);
	
	@SqlUpdate("Insert into Map (hasChanged, mapRequest, path, hash, FetchJob) values (:hasChanged, :mapRequest, :path, :hash, :fetchJob)")
	@GetGeneratedKeys
	public int saveMap(@Bind("hasChanged") boolean hasChanged, @Bind("mapRequest") int mapRequest, @Bind("path") String path, @Bind("hash") String hash, @Bind("fetchJob") int fetchJob);
	
	//@SqlUpdate("Select * from Map cross join MapRequest where MapRequest.id = Map.MapRequest & Map.FetchJob = :fetchJob")
	@SqlQuery("Select * from Map cross join MapRequest where MapRequest.id = Map.MapRequest & Map.FetchJob = :fetchJob")
	@Mapper(MapWrapper.class)
	public List<Map> getMapsFromFetchJob(@Bind("fetchJob") int fetchJob);
	
	//@SqlQuery("select * from (select * from (select m.id, m.path, m.hash, mr.region, m.fetchjob, m.hasChanged, mr.id as mapRequest, m.dateTime from map m inner join maprequest mr on m.maprequest = mr.id inner join fetchjob f on m.fetchjob = f.id) as t where region = :region) as tt  where fetchjob = :fetchJob;")
	@SqlQuery("select * from (select * from Map as t where fetchjob = 10) as tt where maprequest = 1;")
	@Mapper(MapWrapper.class)
	public List<Map> getMapsFromFetchJobAndRegion(@Bind("fetchJob") int fetchJob, @Bind("mapRequest") int mapRequest);
	
	@SqlQuery("Select path from Map where hash = :hash limit 1")
	public List<String> getPathWithHash(@Bind("hash") String hash);
}

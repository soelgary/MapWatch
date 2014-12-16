package com.gsoeller.personalization.maps.dao;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import com.gsoeller.personalization.maps.data.FetchJob;
import com.gsoeller.personalization.maps.mappers.FetchJobMapper;

public interface FetchJobDao {
	
	@SqlUpdate("Insert into FetchJob (MapNumber) values (:mapNumber)")
	@GetGeneratedKeys
	public int createFetchJob(@Bind("mapNumber") int mapNumber);

	@SqlQuery("select * from FetchJob order by id desc")
	@Mapper(FetchJobMapper.class)
	public List<FetchJob> getFetchJobs();	
	
	@SqlQuery("select finished from FetchJob order by startTime desc limit 1")
	public List<Boolean> isLastJobFinished();
	
	@SqlQuery("select id from FetchJob order by startTime desc limit 1")
	public List<Integer> getLastFetchJob();
	
	@SqlUpdate("Update FetchJob set finished = true where id = :id")
	public int finishFetchJob(@Bind("id") int id);
}

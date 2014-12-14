package com.gsoeller.personalization.maps.dao;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface FetchJobDao {
	
	@SqlUpdate("Insert into FetchJob (id) values (null)")
	@GetGeneratedKeys
	public int createFetchJob();

	@SqlQuery("select id from FetchJob order by id desc")
	public List<Integer> getFetchJobs();	
	
	@SqlQuery("select finished from FetchJob order by startTime desc limit 1")
	public List<Boolean> isLastJobFinished();
	
	@SqlQuery("select id from FetchJob order by startTime desc limit 1")
	public List<Integer> getLastFetchJob();
	
	@SqlUpdate("Update FetchJob set finished = true where id = :id")
	public int finishFetchJob(@Bind("id") int id);
}

package com.gsoeller.personalization.maps.dao;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface FetchJobDao {
	
	@SqlUpdate("Insert into FetchJob (id) values (null)")
	@GetGeneratedKeys
	public int createFetchJob();

	@SqlQuery("select id from fetchjob order by id desc")
	public List<Integer> getFetchJobs();	
	
}

package com.gsoeller.personalization.maps.dao;

import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface FetchJobDao {
	
	@SqlUpdate("Insert into FetchJob (id) values (null)")
	@GetGeneratedKeys
	public int createFetchJob();
}

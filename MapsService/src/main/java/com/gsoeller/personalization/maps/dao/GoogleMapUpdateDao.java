package com.gsoeller.personalization.maps.dao;

import io.dropwizard.jdbi.OptionalContainerFactory;

import java.io.IOException;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

import com.gsoeller.personalization.maps.PropertiesLoader;

public class GoogleMapUpdateDao implements MapUpdateDao {

	private DBI dbi;
	private Handle handle;
	private GoogleMapUpdateDaoImpl dao;

	public GoogleMapUpdateDao() throws IOException {
		PropertiesLoader propLoader = new PropertiesLoader();
		dbi = new DBI(propLoader.getProperty("db"), propLoader.getProperty("dbuser"), propLoader.getProperty("dbpwd"));
		dbi.registerContainerFactory(new OptionalContainerFactory());
		handle = dbi.open();
		dao = handle.attach(GoogleMapUpdateDaoImpl.class);
	}
	
	public int save(int oldMap, int newMap) {
		return dao.saveMap(oldMap, newMap);
	}

	private interface GoogleMapUpdateDaoImpl {
		
		@SqlUpdate("Insert into GoogleMapUpdate (oldMap, newMap) values (:oldMap, :newMap)")
		@GetGeneratedKeys
		public int saveMap(@Bind("oldMap") int oldMap, @Bind("newMap") int newMap);
		
	}
}

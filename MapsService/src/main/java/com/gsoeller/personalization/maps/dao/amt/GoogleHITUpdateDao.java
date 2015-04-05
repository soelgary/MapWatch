package com.gsoeller.personalization.maps.dao.amt;

import io.dropwizard.jdbi.OptionalContainerFactory;

import java.io.IOException;
import java.util.List;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import com.gsoeller.personalization.maps.PropertiesLoader;
import com.gsoeller.personalization.maps.data.amt.GoogleHITUpdate;
import com.gsoeller.personalization.maps.mappers.amt.GoogleHITUpdateMapper;

public class GoogleHITUpdateDao {
	
	private DBI dbi;
	private Handle handle;
	private GoogleHITUpdateDaoImpl dao;

	public GoogleHITUpdateDao() throws IOException {
		PropertiesLoader propLoader = new PropertiesLoader();
		dbi = new DBI(propLoader.getProperty("db"), propLoader.getProperty("dbuser"), propLoader.getProperty("dbpwd"));
		dbi.registerContainerFactory(new OptionalContainerFactory());
		handle = dbi.open();
		dao = handle.attach(GoogleHITUpdateDaoImpl.class);
	}
	
	public List<GoogleHITUpdate> getUpdates(int hitId) {
		return dao.getHITUpdates(hitId);
	}
	
	private interface GoogleHITUpdateDaoImpl {
		@SqlQuery("Select * from GoogleHITUpdate where hitId = :hitId")
		@Mapper(GoogleHITUpdateMapper.class)
		public List<GoogleHITUpdate> getHITUpdates(@Bind("hitId") int hitId);
	}
}

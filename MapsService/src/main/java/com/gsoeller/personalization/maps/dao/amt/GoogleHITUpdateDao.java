package com.gsoeller.personalization.maps.dao.amt;

import io.dropwizard.jdbi.OptionalContainerFactory;

import java.io.IOException;
import java.util.List;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import com.google.common.base.Optional;
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
	
	public int createUpdate(int hitId, int oldMap, int newMap, boolean hasBorderChange, String notes) {
		return dao.createUpdate(hitId, oldMap, newMap, hasBorderChange, notes);
	}
	
	public int countUpdates(int hitId) {
		return dao.countUpdates(hitId);
	}
	
	public Optional<GoogleHITUpdate> getUpdate(String hitId, int id) {
		List<GoogleHITUpdate> updates = dao.getUpdate(hitId, id);
		if(updates.size() == 1) {
			return Optional.fromNullable(updates.get(0));
		}
		return Optional.absent();
	}
	
	public Optional<GoogleHITUpdate> update(int id, boolean hasBorderChanged) {
		dao.update(id, hasBorderChanged);
		return getUpdate("", id);
	}
	
	private interface GoogleHITUpdateDaoImpl {
		@SqlQuery("Select * from GoogleHITUpdate where hitId = :hitId")
		@Mapper(GoogleHITUpdateMapper.class)
		public List<GoogleHITUpdate> getHITUpdates(@Bind("hitId") int hitId);
		
		@SqlUpdate("Insert into GoogleHITUpdate (hitId, oldMap, newMap, hasBorderChange, notes) values (:hitId, :oldMap, :newMap, :hasBorderChange, :notes)")
		@GetGeneratedKeys
		public int createUpdate(@Bind("hitId") int hitId,
				@Bind("oldMap") int oldMap,
				@Bind("newMap") int newMap,
				@Bind("hasBorderChange") boolean hasBorderChange,
				@Bind("notes") String notes);
		
		@SqlQuery("Select count(*) from GoogleHITUpdate where hitId = :hitId")
		public int countUpdates(@Bind("hitId") int hitId);
		
		@SqlQuery("Select * from GoogleHITUpdate where id = :id")
		@Mapper(GoogleHITUpdateMapper.class)
		public List<GoogleHITUpdate> getUpdate(@Bind("hitId") String hitId, @Bind("id") int id);
		
		@SqlUpdate("Update GoogleHITUpdate set hasBorderChange=:hasBorderChange, finished=true")
		public int update(@Bind("id") int id, @Bind("hasBorderChange") boolean hasBorderChange);
	}
}

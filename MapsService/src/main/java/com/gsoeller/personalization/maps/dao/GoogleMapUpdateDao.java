package com.gsoeller.personalization.maps.dao;

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
import com.gsoeller.personalization.maps.data.MapChange;
import com.gsoeller.personalization.maps.mappers.GoogleMapUpdateMapper;

/*
public class GoogleMapUpdateDao extends MapUpdateDao {

	private DBI dbi;
	private Handle handle;
	private GoogleMapUpdateDaoImpl dao;

	public GoogleMapUpdateDao() throws IOException {
		dbi = new DBI(PropertiesLoader.getProperty("db"), PropertiesLoader.getProperty("dbuser"), PropertiesLoader.getProperty("dbpwd"));
		dbi.registerContainerFactory(new OptionalContainerFactory());
		handle = dbi.open();
		dao = handle.attach(GoogleMapUpdateDaoImpl.class);
	}
	
	public int save(int oldMap, int newMap) {
		return dao.saveMap(oldMap, newMap);
	}
	
	public List<MapChange> getUpdates(int offset, int count, boolean inProgress) {
		return dao.getUpdates(offset, count, inProgress);
	}
	
	public Optional<MapChange> getUpdate(int id) {
		List<MapChange> changes = dao.getUpdate(id);
		if(changes.isEmpty()) {
			return Optional.absent();
		}
		return Optional.fromNullable(changes.get(0));
	}
	
	public boolean reserve(int id) {
		Optional<MapChange> change = getUpdate(id);
		if(change.isPresent() && !change.get().inProgress()) {
			dao.reserve(id);
			return true;
		}
		return false;
	}
	
	public void free(int id) {
		dao.free(id);
	}
	
	public Optional<MapChange> findNext() {
		List<MapChange> next = dao.findNext();
		if(next.isEmpty()) {
			return Optional.absent();
		}
		return Optional.fromNullable(next.get(0));
	}
	
	public void update(int id, String notes, boolean needsInvestigation) {
		dao.update(id, needsInvestigation, notes);
	}

	private interface GoogleMapUpdateDaoImpl {
		
		@SqlUpdate("Insert into GoogleUpdate (oldMap, newMap) values (:oldMap, :newMap)")
		@GetGeneratedKeys
		public int saveMap(@Bind("oldMap") int oldMap, @Bind("newMap") int newMap);
		
		@SqlQuery("Select * from GoogleUpdate WHERE inProgress = :inProgress LIMIT :offset, :count")
		@Mapper(GoogleMapUpdateMapper.class)
		public List<MapChange> getUpdates(@Bind("offset")int offset, @Bind("count") int count, @Bind("inProgress") boolean inProgress);
		
		@SqlQuery("Select * from GoogleUpdate where id = :id")
		@Mapper(GoogleMapUpdateMapper.class)
		public List<MapChange> getUpdate(@Bind("id") int id);
		
		@SqlUpdate("Update GoogleUpdate SET inProgress = true WHERE id = :id")
		public void reserve(@Bind("id") int id);
		
		@SqlUpdate("Update GoogleUpdate SET inProgress = false WHERE id = :id")
		public void free(@Bind("id") int id);
		
		@SqlUpdate("Select * from GoogleUpdate WHERE inProgress = false LIMIT 1")
		@Mapper(GoogleMapUpdateMapper.class)
		public List<MapChange> findNext();
		
		@SqlUpdate("Update GoogleUpdate SET inProgress = false, needsInvestigation = :needsInvestigation, notes=CONCAT_WS('', notes, :notes, '\n'), lastUpdated=now(), inProgress = false WHERE id = :id")
		public void update(@Bind("id") int id, @Bind("needsInvestigation") boolean needsInvestigation, @Bind("notes") String notes);
	}
}
*/
public interface GoogleMapUpdateDao {
	
	@SqlUpdate("Insert into GoogleUpdate (oldMap, newMap) values (:oldMap, :newMap)")
	@GetGeneratedKeys
	public int saveMap(@Bind("oldMap") int oldMap, @Bind("newMap") int newMap);
	
	@SqlQuery("Select * from GoogleUpdate WHERE inProgress = :inProgress LIMIT :offset, :count")
	@Mapper(GoogleMapUpdateMapper.class)
	public List<MapChange> getUpdates(@Bind("offset")int offset, @Bind("count") int count, @Bind("inProgress") boolean inProgress);
	
	@SqlQuery("Select * from GoogleUpdate where id = :id")
	@Mapper(GoogleMapUpdateMapper.class)
	public List<MapChange> getUpdate(@Bind("id") int id);
	
	@SqlUpdate("Update GoogleUpdate SET inProgress = true WHERE id = :id")
	public void reserve(@Bind("id") int id);
	
	@SqlUpdate("Update GoogleUpdate SET inProgress = false WHERE id = :id")
	public void free(@Bind("id") int id);
	
	@SqlUpdate("Select * from GoogleUpdate WHERE inProgress = false LIMIT 1")
	@Mapper(GoogleMapUpdateMapper.class)
	public List<MapChange> findNext();
	
	@SqlUpdate("Update GoogleUpdate SET inProgress = false, needsInvestigation = :needsInvestigation, notes=CONCAT_WS('', notes, :notes, '\n'), lastUpdated=now(), inProgress = false WHERE id = :id")
	public void update(@Bind("id") int id, @Bind("needsInvestigation") boolean needsInvestigation, @Bind("notes") String notes);
}

package com.gsoeller.personalization.maps.dao;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import com.gsoeller.personalization.maps.data.MapChange;
import com.gsoeller.personalization.maps.mappers.BingMapUpdateMapper;

public interface BingMapUpdateDao {
	
	@SqlUpdate("Insert into BingUpdate (oldMap, newMap) values (:oldMap, :newMap)")
	@GetGeneratedKeys
	public int saveMap(@Bind("oldMap") int oldMap, @Bind("newMap") int newMap);
	
	@SqlQuery("Select * from BingUpdate WHERE inProgress = :inProgress LIMIT :offset, :count")
	@Mapper(BingMapUpdateMapper.class)
	public List<MapChange> getUpdates(@Bind("offset")int offset, @Bind("count") int count, @Bind("inProgress") boolean inProgress);
	
	@SqlQuery("Select * from BingUpdate where id = :id")
	@Mapper(BingMapUpdateMapper.class)
	public List<MapChange> getUpdate(@Bind("id") int id);
	
	@SqlUpdate("Update BingUpdate SET inProgress = true WHERE id = :id")
	public void reserve(@Bind("id") int id);
	
	@SqlUpdate("Update BingUpdate SET inProgress = false WHERE id = :id")
	public void free(@Bind("id") int id);
	
	@SqlUpdate("Select * from BingUpdate WHERE inProgress = false LIMIT 1")
	@Mapper(BingMapUpdateMapper.class)
	public List<MapChange> findNext();
	
	@SqlUpdate("Update BingUpdate SET inProgress = false, needsInvestigation = :needsInvestigation, notes=CONCAT_WS('', notes, :notes, '\n'), lastUpdated=now(), inProgress = false WHERE id = :id")
	public void update(@Bind("id") int id, @Bind("needsInvestigation") boolean needsInvestigation, @Bind("notes") String notes);
}
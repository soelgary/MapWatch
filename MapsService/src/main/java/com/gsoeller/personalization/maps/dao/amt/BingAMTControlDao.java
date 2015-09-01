package com.gsoeller.personalization.maps.dao.amt;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import com.gsoeller.personalization.maps.data.amt.BingControlUpdate;
import com.gsoeller.personalization.maps.mappers.amt.BingControlMapper;

public interface BingAMTControlDao {
	@SqlQuery("Select * from BingControlUpdate LIMIT :offset, :count")
	@Mapper(BingControlMapper.class)
	public List<BingControlUpdate> getControls(@Bind("offset") int offset, @Bind("count") int count);
	
	@SqlQuery("Select * from BingControlUpdate where id = :id")
	@Mapper(BingControlMapper.class)
	public List<BingControlUpdate> getControl(@Bind("id") int id);
	
	@SqlUpdate("Insert into BingControlUpdate (oldMap, newMap, hasBorderDifference) values (:oldMap, :newMap, :hasBorderDifference)")
	@GetGeneratedKeys
	public int createControl(@Bind("oldMap") int oldMap, @Bind("newMap") int newMap, @Bind("hasBorderDifference") boolean hasBorderDifference);
	
	public void close();
}

package com.gsoeller.personalization.maps.dao.amt;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import com.gsoeller.personalization.maps.data.amt.BingHITUpdate;
import com.gsoeller.personalization.maps.mappers.amt.BingHITUpdateJoinedMapper;
import com.gsoeller.personalization.maps.mappers.amt.BingHITUpdateMapper;

public interface BingHITUpdateDao {
	
	@SqlQuery("Select * from BingHITUpdate where hitId = :hitId")
	@Mapper(BingHITUpdateMapper.class)
	public List<BingHITUpdate> getHITUpdates(@Bind("hitId") int hitId);
	
	@SqlQuery("Select * from BingHITUpdate where id = :id")
	@Mapper(BingHITUpdateMapper.class)
	public List<BingHITUpdate> getHITUpdate(@Bind("id") int id);
	
	@SqlQuery("Select * from BingHITUpdate where finished = :finished LIMIT :offset, :count")
	@Mapper(BingHITUpdateMapper.class)
	public List<BingHITUpdate> getHITUpdates(@Bind("finished") boolean finished,
			@Bind("count") int count,
			@Bind("offset") int offset);
	
	@SqlQuery("Select * from BingHITUpdate where finished = :finished && hasBorderChange = :hasBorderDifference LIMIT :offset, :count")
	@Mapper(BingHITUpdateMapper.class)
	public List<BingHITUpdate> getHITUpdatesBasedOnBorderDifference(@Bind("hasBorderDifference") boolean hasBorderDifference,
			@Bind("count") int count,
			@Bind("offset") int offset,
			@Bind("finished") boolean finished);
	
	@SqlUpdate("Insert into BingHITUpdate (hitId, oldMap, newMap, hasBorderChange, notes) values (:hitId, :oldMap, :newMap, :hasBorderChange, :notes)")
	@GetGeneratedKeys
	public int createUpdate(@Bind("hitId") int hitId,
			@Bind("oldMap") int oldMap,
			@Bind("newMap") int newMap,
			@Bind("hasBorderChange") boolean hasBorderChange,
			@Bind("notes") String notes);
	
	@SqlQuery("Select count(*) from BingHITUpdate where hitId = :hitId")
	public int countUpdates(@Bind("hitId") int hitId);
	
	@SqlQuery("Select * from BingHITUpdate where id = :id")
	@Mapper(BingHITUpdateMapper.class)
	public List<BingHITUpdate> getUpdate(@Bind("hitId") String hitId, @Bind("id") int id);
	
	@SqlUpdate("Update BingHITUpdate set hasBorderChange=:hasBorderChange, finished=true where id = :id")
	public int update(@Bind("id") int id, @Bind("hasBorderChange") boolean hasBorderChange);
	
	@SqlUpdate("Update BingHITUpdate set controlResponse=:controlResponse where id = :id")
	public int setControlResponse(@Bind("id") int id, @Bind("controlResponse") boolean controlResponse);
	
	@SqlQuery("select count(*) from BingHITUpdate hit inner join BingMap o on hit.oldMap = o.id inner join BingMap n on hit.newMap = n.id where (o.hash = :firstHash && n.hash = :secondHash) || (n.hash = :firstHash && o.hash = :secondHash);")
	public int countUpdatesWithTiles(@Bind("firstHash") String firstHash, @Bind("secondHash") String secondHash);
	
	@SqlQuery("select * from BingMap old, Map new where new.hash = :newHash && old.hash = :oldHash && new.mapRequest = old.mapRequest && new.FetchJob = old.FetchJob + 1")
	@Mapper(BingHITUpdateJoinedMapper.class)
	public List<BingHITUpdate> getSimilarUpdates(@Bind("oldHash") String oldHash, @Bind("newHash") String newHash);
	
	public void close();
}

package com.gsoeller.personalization.maps.dao.amt;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import com.gsoeller.personalization.maps.data.amt.GoogleHITUpdate;
import com.gsoeller.personalization.maps.mappers.amt.GoogleHITUpdateMapper;

public interface GoogleHITUpdateDao {
	@SqlQuery("Select * from GoogleHITUpdate where hitId = :hitId")
	@Mapper(GoogleHITUpdateMapper.class)
	public List<GoogleHITUpdate> getHITUpdates(@Bind("hitId") int hitId);
	
	@SqlQuery("Select * from GoogleHITUpdate where id = :id")
	@Mapper(GoogleHITUpdateMapper.class)
	public List<GoogleHITUpdate> getHITUpdate(@Bind("id") int id);
	
	@SqlQuery("Select * from GoogleHITUpdate where finished = :finished LIMIT :offset, :count")
	@Mapper(GoogleHITUpdateMapper.class)
	public List<GoogleHITUpdate> getHITUpdates(@Bind("finished") boolean finished,
			@Bind("count") int count,
			@Bind("offset") int offset);
	
	@SqlQuery("Select * from GoogleHITUpdate where finished = true && hasBorderChange = :hasBorderDifference LIMIT :offset, :count")
	@Mapper(GoogleHITUpdateMapper.class)
	public List<GoogleHITUpdate> getHITUpdatesBasedOnBorderDifference(@Bind("hasBorderDifference") boolean hasBorderDifference,
			@Bind("count") int count,
			@Bind("offset") int offset);
	
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
	
	@SqlUpdate("Update GoogleHITUpdate set hasBorderChange=:hasBorderChange, finished=true where id = :id")
	public int update(@Bind("id") int id, @Bind("hasBorderChange") boolean hasBorderChange);
	
	@SqlUpdate("Update GoogleHITUpdate set controlResponse=:controlResponse where id = :id")
	public int setControlResponse(@Bind("id") int id, @Bind("controlResponse") boolean controlResponse);
	
	@SqlQuery("select count(*) from GoogleHITUpdate hit inner join Map o on hit.oldMap = o.id inner join Map n on hit.newMap = n.id where (o.hash = :firstHash && n.hash = :secondHash) || (n.hash = :firstHash && o.hash = :secondHash);")
	public int countUpdatesWithTiles(@Bind("firstHash") String firstHash, @Bind("secondHash") String secondHash);
	
	public void close();
}
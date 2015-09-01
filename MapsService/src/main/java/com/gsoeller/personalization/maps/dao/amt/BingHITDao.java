package com.gsoeller.personalization.maps.dao.amt;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import com.gsoeller.personalization.maps.data.amt.BingHIT;
import com.gsoeller.personalization.maps.mappers.amt.BingHITMapper;

public interface BingHITDao {
	@SqlQuery("Select * from BingHIT where id = :id")
	@Mapper(BingHITMapper.class)
	public List<BingHIT> getHIT(@Bind("id") int id);
	
	@SqlQuery("Select * from BingHIT where approved = :approved && readyForApproval = :readyForApproval && created >= :createdAfter LIMIT :offset, :count")
	@Mapper(BingHITMapper.class)
	public List<BingHIT> getHITS(@Bind("offset") int offset, 
			@Bind("count") int count, 
			@Bind("readyForApproval") boolean readyForApproval, 
			@Bind("approved") boolean approved,
			@Bind("createdAfter") String createdAfter);
	
	@SqlQuery("Select * from BingHIT where hitId = :hitId")
	@Mapper(BingHITMapper.class)
	public List<BingHIT> getHITFromMTurkHitId(@Bind("hitId") String hitId);
	
	@SqlUpdate("Insert into BingHIT (turkId, control, approved, readyForApproval) values (:turkId, :control, :approved, :readyForApproval)")
	@GetGeneratedKeys
	public int createHIT(@Bind("turkId") int turkId, 
			@Bind("control") int control, 
			@Bind("approved") boolean approved,
			@Bind("readyForApproval") boolean readyForApproval);
	
	@SqlQuery("Select * from BingHIT where readyForApproval = false LIMIT :offset, :count")
	@Mapper(BingHITMapper.class)
	public List<BingHIT> getNextAvailableHITs(@Bind("offset") int offset, @Bind("count") int count);
	
	@SqlUpdate("Update BingHIT SET readyForApproval=true where id = :id")
	public int markForApproval(@Bind("id") int id);
	
	@SqlUpdate("Update BingHIT SET approved=true where id = :hitId")
	public int approve(@Bind("hitId") int hitId);
	
	@SqlUpdate("Update BingHIT SET hitId=:hitId where id = :id")
	public int setMTurkHitId(@Bind("hitId") String hitId, @Bind("id") int id);
	
	@SqlUpdate("Update BingHIT SET controlResponse=:response, finished=true where hitId = :hitId")
	public int updateControlResponse(@Bind("hitId") String hitId, @Bind("response") boolean response);
	
	public void close();
}

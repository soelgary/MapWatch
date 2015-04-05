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
import com.gsoeller.personalization.maps.data.amt.GoogleHIT;
import com.gsoeller.personalization.maps.mappers.amt.GoogleHITMapper;

public class GoogleHITDao {

	private DBI dbi;
	private Handle handle;
	private GoogleHITDaoImpl dao;

	public GoogleHITDao() throws IOException {
		PropertiesLoader propLoader = new PropertiesLoader();
		dbi = new DBI(propLoader.getProperty("db"), propLoader.getProperty("dbuser"), propLoader.getProperty("dbpwd"));
		dbi.registerContainerFactory(new OptionalContainerFactory());
		handle = dbi.open();
		dao = handle.attach(GoogleHITDaoImpl.class);
	}
	
	public Optional<GoogleHIT> getHIT(int hitId) {
		List<GoogleHIT> hits = dao.getHIT(hitId);
		if(hits.size() == 1) {
			return Optional.fromNullable(hits.get(0));
		}
		return Optional.absent();
	}
	
	public List<GoogleHIT> getHITS(int offset, int count) {
		return dao.getHITS(offset, count);
	}
	
	public int createHIT(int id, int turkId, int control, boolean approved, boolean readyForApproval) {
		return dao.createHIT(id, turkId, control, approved, readyForApproval);
	}
	
	private interface GoogleHITDaoImpl {
		@SqlQuery("Select * from GoogleHIT where id = :id")
		@Mapper(GoogleHITMapper.class)
		public List<GoogleHIT> getHIT(@Bind("id") int id);
		
		@SqlQuery("Select * from GoogleHIT LIMIT :offset, :count")
		@Mapper(GoogleHITMapper.class)
		public List<GoogleHIT> getHITS(@Bind("offset") int offset, @Bind("count") int count);
		
		@SqlUpdate("Insert into GoogleHIT (id, turkId, control, approved, readyForApproval) values (:id, :turkId, :control, :approved, :readyForApproval)")
		@GetGeneratedKeys
		public int createHIT(@Bind("id") int id, 
				@Bind("turkId") int turkId, 
				@Bind("control") int control, 
				@Bind("approved") boolean approved,
				@Bind("readyForApproval") boolean readyForApproval);
	}
	
}

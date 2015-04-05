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
import com.gsoeller.personalization.maps.data.amt.GoogleControlUpdate;
import com.gsoeller.personalization.maps.data.amt.GoogleHIT;
import com.gsoeller.personalization.maps.mappers.amt.GoogleControlMapper;
import com.gsoeller.personalization.maps.mappers.amt.GoogleHITMapper;

public class GoogleAMTControlDao {

	private DBI dbi;
	private Handle handle;
	private GoogleAMTControlDaoImpl dao;

	public GoogleAMTControlDao() throws IOException {
		PropertiesLoader propLoader = new PropertiesLoader();
		dbi = new DBI(propLoader.getProperty("db"), propLoader.getProperty("dbuser"), propLoader.getProperty("dbpwd"));
		dbi.registerContainerFactory(new OptionalContainerFactory());
		handle = dbi.open();
		dao = handle.attach(GoogleAMTControlDaoImpl.class);
	}
	
	public List<GoogleControlUpdate> getControls(int offset, int count) {
		return dao.getControls(offset, count);
	}
	
	public Optional<GoogleControlUpdate> getControl(int id) {
		List<GoogleControlUpdate> controls = dao.getControl(id);
		if(controls.size() == 1) {
			return Optional.fromNullable(controls.get(0));
		}
		return Optional.absent();
	}
	
	public int createControl() {
		return dao.createControl();
	}
	
	private interface GoogleAMTControlDaoImpl {
		@SqlQuery("Select * from GoogleControlUpdate LIMIT :offset, :count")
		@Mapper(GoogleControlMapper.class)
		public List<GoogleControlUpdate> getControls(@Bind("offset") int offset, @Bind("count") int count);
		
		@SqlQuery("Select * from GoogleControlUpdate where id = :id")
		@Mapper(GoogleControlMapper.class)
		public List<GoogleControlUpdate> getControl(@Bind("id") int id);
		
		@SqlUpdate("Insert into GoogleControlUpdate () values ()")
		@GetGeneratedKeys
		public int createControl();
	}
}

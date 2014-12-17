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

import com.google.common.base.Optional;
import com.gsoeller.personalization.maps.PropertiesLoader;

public class GoogleFetchJobDao implements FetchJobDao {
	
	private DBI dbi;
	private Handle handle;
	private GoogleFetchJobDaoImpl dao;
	
	public GoogleFetchJobDao() throws IOException {
		PropertiesLoader propLoader = new PropertiesLoader();
		dbi = new DBI(propLoader.getProperty("db"), propLoader.getProperty("dbuser"), propLoader.getProperty("dbpwd"));
		dbi.registerContainerFactory(new OptionalContainerFactory());
		handle = dbi.open();
		dao = handle.attach(GoogleFetchJobDaoImpl.class);
	}
	public boolean isLastJobFinished() {
		List<Boolean> finished = dao.isLastJobFinished();
		if(finished.isEmpty()) {
			return false;
		} 
		return finished.get(0);
	}

	public int createFetchJob(int mapNumber) {
		return dao.createFetchJob(mapNumber);
	}

	public Optional<Integer> getLastFetchJob() {
		List<Integer> fetchJobs = dao.getLastFetchJob();
		if(fetchJobs.isEmpty()) {
			return Optional.absent();
		}
		return Optional.fromNullable(fetchJobs.get(0));
	}

	public void finishFetchJob(int fetchJob) {
		dao.finishFetchJob(fetchJob);
	}
	
	private interface GoogleFetchJobDaoImpl {

		@SqlUpdate("Insert into FetchJob (MapNumber) values (:mapNumber)")
		@GetGeneratedKeys
		public int createFetchJob(@Bind("mapNumber") int mapNumber);

		@SqlQuery("select finished from FetchJob order by startTime desc limit 1")
		public List<Boolean> isLastJobFinished();
		
		@SqlQuery("select id from FetchJob order by startTime desc limit 1")
		public List<Integer> getLastFetchJob();
		
		@SqlUpdate("Update FetchJob set finished = true where id = :id")
		public int finishFetchJob(@Bind("id") int id);
		
	}
}
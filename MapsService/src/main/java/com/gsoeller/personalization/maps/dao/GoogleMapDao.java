package com.gsoeller.personalization.maps.dao;

import io.dropwizard.jdbi.OptionalContainerFactory;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import com.google.common.base.Optional;
import com.gsoeller.personalization.maps.MapsLogger;
import com.gsoeller.personalization.maps.PropertiesLoader;
import com.gsoeller.personalization.maps.data.GoogleMap;
import com.gsoeller.personalization.maps.data.Map;
import com.gsoeller.personalization.maps.mappers.GoogleMapWrapper;

public class GoogleMapDao implements MapDao {
	
	private DBI dbi;
	private Handle handle;
	private GoogleMapDaoImpl dao;
	
	private Logger LOG = MapsLogger.createLogger("com.gsoeller.personalization.maps.jobs.GoogleMapDao");
	
	public GoogleMapDao() throws IOException {
		PropertiesLoader propLoader = new PropertiesLoader();
		dbi = new DBI(propLoader.getProperty("db"), propLoader.getProperty("dbuser"), propLoader.getProperty("dbpwd"));
		dbi.registerContainerFactory(new OptionalContainerFactory());
		handle = dbi.open();
		dao = handle.attach(GoogleMapDaoImpl.class);
	}

	public Optional<Integer> getLastMap(int currentFetchJob) {
		List<Integer> maps = dao.getLastMap(currentFetchJob);
		if(maps.isEmpty()) {
			return Optional.absent();
		}
		return Optional.fromNullable(maps.get(0));
	}

	public void saveMap(boolean hasChanged, int id, String path, String hash,
			int fetchJob) {
		dao.saveMap(hasChanged, id, path, hash, fetchJob);
	}

	public Optional<String> getPathWithHash(String hash) {
		List<String> paths = dao.getPathWithHash(hash);
		if(paths.isEmpty()) {
			return Optional.absent();
		}
		return Optional.fromNullable(paths.get(0));
	}

	public Optional<Map> getMapMostRecentWithMapRequestId(int mapRequestId) {
		List<GoogleMap> maps = dao.getMapMostRecentWithMapRequestId(mapRequestId);
		if(maps.isEmpty()) {
			return Optional.absent();
		}
		return Optional.<Map>fromNullable(maps.get(0));
	}
	
	public Optional<Map> getMapFromFetchJobByMapRequest(int fetchJob,
			int mapRequest) {
		List<GoogleMap> maps = dao.getMapFromFetchJobByMapRequest(fetchJob, mapRequest);
		if(maps.size() == 1) {
			return Optional.<Map>fromNullable(maps.get(0));
		} else if(maps.isEmpty()) {
			return Optional.absent();
		} else {
			LOG.severe(String.format("Found multiple maps for fetchjob '%d' and map request '%d'", fetchJob, mapRequest));
			throw new RuntimeException("Found multiple maps");
		}
	}
	
	public List<GoogleMap> getMapsWithMapRequest(int mapRequestId) {
		return dao.getMapWithMapRequestId(mapRequestId);
	}
	
	public boolean containsHash(String hash) {
		return dao.countHashes(hash).size() > 0;
	}

	private interface GoogleMapDaoImpl {
		
		@SqlQuery("Select mapRequest from Map where FetchJob = :fetchJob order by dateTime desc limit 1")
		public List<Integer> getLastMap(@Bind("fetchJob") int fetchJob);
		
		@SqlUpdate("Insert into Map (hasChanged, mapRequest, path, hash, FetchJob) values (:hasChanged, :mapRequest, :path, :hash, :fetchJob)")
		@GetGeneratedKeys
		public int saveMap(@Bind("hasChanged") boolean hasChanged, @Bind("mapRequest") int mapRequest, @Bind("path") String path, @Bind("hash") String hash, @Bind("fetchJob") int fetchJob);
		
		@SqlQuery("Select path from Map where hash = :hash limit 1")
		public List<String> getPathWithHash(@Bind("hash") String hash);

		@SqlQuery("Select * from Map cross join MapRequest where mapRequest = :mapRequest order by dateTime DESC limit 1")
		@Mapper(GoogleMapWrapper.class)
		public List<GoogleMap> getMapMostRecentWithMapRequestId(@Bind("mapRequest") int mapRequest);
		
		@SqlQuery("select * from (select * from Map as aa where FetchJob = :fetchJob) as a where mapRequest = :mapRequest")
		@Mapper(GoogleMapWrapper.class)
		public List<GoogleMap> getMapFromFetchJobByMapRequest(@Bind("fetchJob") int fetchJob, @Bind("mapRequest") int mapRequest);
		
		@SqlQuery("Select * from Map where mapRequest = :mapRequest order by dateTime DESC")
		@Mapper(GoogleMapWrapper.class)
		public List<GoogleMap> getMapWithMapRequestId(@Bind("mapRequest") int mapRequest);
		
		@SqlQuery("Select hash from Map where hash = :hash")
		public List<String> countHashes(@Bind("hash") String hash);
	}
}

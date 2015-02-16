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
import com.gsoeller.personalization.maps.data.BingMap;
import com.gsoeller.personalization.maps.data.Map;
import com.gsoeller.personalization.maps.mappers.BingMapWrapper;

public class BingMapDao implements MapDao {
	private DBI dbi;
	private Handle handle;
	private BingMapDaoImpl dao;
	
	private Logger LOG = MapsLogger.createLogger("com.gsoeller.personalization.maps.jobs.GoogleMapDao");
	
	public BingMapDao() throws IOException {
		PropertiesLoader propLoader = new PropertiesLoader();
		dbi = new DBI(propLoader.getProperty("db"), propLoader.getProperty("dbuser"), propLoader.getProperty("dbpwd"));
		dbi.registerContainerFactory(new OptionalContainerFactory());
		handle = dbi.open();
		dao = handle.attach(BingMapDaoImpl.class);
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
		List<BingMap> maps = dao.getMapMostRecentWithMapRequestId(mapRequestId);
		if(maps.isEmpty()) {
			return Optional.absent();
		}
		return Optional.<Map>fromNullable(maps.get(0));
	}
	
	public Optional<Map> getMapFromFetchJobByMapRequest(int fetchJob,
			int mapRequest) {
		List<BingMap> maps = dao.getMapFromFetchJobByMapRequest(fetchJob, mapRequest);
		if(maps.size() == 1) {
			return Optional.<Map>fromNullable(maps.get(0));
		} else if(maps.isEmpty()) {
			return Optional.absent();
		} else {
			LOG.severe(String.format("Found multiple maps for fetchjob '%d' and map request '%d'", fetchJob, mapRequest));
			throw new RuntimeException("Found multiple maps");
		}
	}
	
	public boolean containsHash(String hash) {
		return dao.countHashes(hash).size() > 0;
	}

	private interface BingMapDaoImpl {
		
		@SqlQuery("Select bingMapRequest from BingMap where FetchJob = :fetchJob order by dateTime desc limit 1")
		public List<Integer> getLastMap(@Bind("fetchJob") int fetchJob);
		
		@SqlUpdate("Insert into BingMap (hasChanged, bingMapRequest, path, hash, FetchJob) values (:hasChanged, :mapRequest, :path, :hash, :fetchJob)")
		@GetGeneratedKeys
		public int saveMap(@Bind("hasChanged") boolean hasChanged, @Bind("mapRequest") int mapRequest, @Bind("path") String path, @Bind("hash") String hash, @Bind("fetchJob") int fetchJob);
		
		@SqlQuery("Select path from BingMap where hash = :hash limit 1")
		public List<String> getPathWithHash(@Bind("hash") String hash);

		//@SqlQuery("Select SQL_NO_CACHE * from BingMap cross join BingMapRequest where bingMapRequest = :mapRequest order by dateTime DESC limit 1")
		@SqlQuery("Select SQL_NO_CACHE * from BingMap cross join BingMapRequest on bingMapRequest = BingMapRequest.id where BingMap.bingMapRequest = :mapRequest order by dateTime DESC limit 1;")
		@Mapper(BingMapWrapper.class)
		public List<BingMap> getMapMostRecentWithMapRequestId(@Bind("mapRequest") int mapRequest);
		
		//@SqlQuery("select SQL_NO_CACHE * from (select * from BingMap as aa where FetchJob = :fetchJob) as a where bingMapRequest = :mapRequest")
		@SqlQuery("select SQL_NO_CACHE * from Map where mapRequest = :mapRequest && FetchJob = : fetchJob;")
		@Mapper(BingMapWrapper.class)
		public List<BingMap> getMapFromFetchJobByMapRequest(@Bind("fetchJob") int fetchJob, @Bind("mapRequest") int mapRequest);
		
		@SqlQuery("Select id from BingMap where hash = :hash")
		public List<String> countHashes(@Bind("hash") String hash);
	}
}
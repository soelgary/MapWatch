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
import com.gsoeller.personalization.maps.data.BingMapRequest;
import com.gsoeller.personalization.maps.data.MapRequest;
import com.gsoeller.personalization.maps.data.Region;
import com.gsoeller.personalization.maps.mappers.BingMapRequestMapper;

public class BingMapRequestDao implements MapRequestDao {
	private DBI dbi;
	private Handle handle;
	private BingMapRequestDaoImpl dao;
	
	private Logger LOG = MapsLogger.createLogger("com.gsoeller.personalization.maps.data.GoogleMapRequestDao");
	
	public BingMapRequestDao() throws IOException {
		PropertiesLoader propLoader = new PropertiesLoader();
		dbi = new DBI(propLoader.getProperty("db"), propLoader.getProperty("dbuser"), propLoader.getProperty("dbpwd"));
		dbi.registerContainerFactory(new OptionalContainerFactory());
		handle = dbi.open();
		dao = handle.attach(BingMapRequestDaoImpl.class);
	}
	
	public List<MapRequest> getRequests(int limit, int offset, int mapNumber) {
		return dao.getRequests(limit, offset, mapNumber);
	}
	
	public void addMapRequest(MapRequest mapRequest) {
		dao.addMapRequest(mapRequest.getMapNumber(), mapRequest.getRegion().toString(), mapRequest.getTileNumber().getKey());
	}
	
	public Optional<Region> getRegion(int mapRequest) {
		List<String> regions = dao.getRegion(mapRequest);
		if(regions.isEmpty()) {
			return Optional.absent();
		} else if(regions.size() == 1) {
			return Optional.of(Region.findRegion(regions.get(0)));
		} else {
			LOG.severe(String.format("Found too many regions for map request '%d'", mapRequest));
			throw new RuntimeException("Too many regions were found");
		}
	
	}
	
	private interface BingMapRequestDaoImpl {
		@SqlQuery("select * from BingMapRequest where MapNumber = :mapNumber limit :offset,:limit;")
		@Mapper(BingMapRequestMapper.class)
		public List<MapRequest> getRequests(@Bind("limit") int limit, @Bind("offset") int offset, @Bind("mapNumber") int mapNumber);
		
		@SqlUpdate("insert into BingMapRequest (MapNumber, region, tileNumber) values (:mapNumber, :region, :tileNumber)")
		@GetGeneratedKeys
		public int addMapRequest(@Bind("mapNumber") int mapNumber, @Bind("region") String region, @Bind("tileNumber") String tileNumber);
		
		@SqlQuery("Select region from BingMapRequest where id = :id")
		public List<String> getRegion(@Bind("id") int id);
	}
}

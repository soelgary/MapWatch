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
import com.gsoeller.personalization.maps.data.MapRequest;
import com.gsoeller.personalization.maps.data.Region;
import com.gsoeller.personalization.maps.mappers.GoogleMapRequestMapper;

public class GoogleMapRequestDao implements MapRequestDao {
	
	private DBI dbi;
	private Handle handle;
	private GoogleMapRequestDaoImpl dao;
	
	private Logger LOG = MapsLogger.createLogger("com.gsoeller.personalization.maps.data.GoogleMapRequestDao");
	
	public GoogleMapRequestDao() throws IOException {
		PropertiesLoader propLoader = new PropertiesLoader();
		dbi = new DBI(propLoader.getProperty("db"), propLoader.getProperty("dbuser"), propLoader.getProperty("dbpwd"));
		dbi.registerContainerFactory(new OptionalContainerFactory());
		handle = dbi.open();
		dao = handle.attach(GoogleMapRequestDaoImpl.class);
	}
	
	public List<MapRequest> getRequests(int limit, int offset, int mapNumber) {
		return dao.getRequests(limit, offset, mapNumber);
	}
	
	public void addMapRequest(MapRequest mapRequest) {
		dao.addMapRequest(mapRequest.getMapNumber(),
				mapRequest.getLatitude(), 
				mapRequest.getLongitude(), 
				mapRequest.getZoom(), 
				mapRequest.getXDimension(), 
				mapRequest.getYDimension(), 
				mapRequest.getRegion().toString(), 
				mapRequest.getLanguage().toString());
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
	
	public int countTiles() {
		return 10;
		//return dao.countTiles();
	}
	
	public int countAllTiles() {
		return dao.countAllTiles();
	}
	
	public List<Integer> getTileNumbers() {
		throw new UnsupportedOperationException("Google does not yet support getting the tile numbers");
	}
	
	public List<Integer> getMapRequestsFromTileNumber(int tileNumber) {
		throw new UnsupportedOperationException("Google does not yet support getting id's from the tile numbers");
	}
	
	public Optional<BingMap> getMapFromFetchJobAndMapRequest(int fetchJob, int mapRequestId) {
		throw new UnsupportedOperationException("Google does not yet support getting maps fromfetch job and map request id");
	}
	
	public List<Integer> getMapRequestsbyLocation(int location) {
		return dao.getMapRequestsbyLocation(location);
	}
	
	private interface GoogleMapRequestDaoImpl {
		@SqlQuery("select * from MapRequest where MapNumber = :mapNumber limit :offset,:limit;")
		@Mapper(GoogleMapRequestMapper.class)
		public List<MapRequest> getRequests(@Bind("limit") int limit, @Bind("offset") int offset, @Bind("mapNumber") int mapNumber);
		
		@SqlUpdate("insert into MapRequest (MapNumber, latitude, longitude, zoom, xDimension, yDimension, region, language) values (:mapNumber, :latitude, :longitude, :zoom, :xDimension, :yDimension, :region, :language)")
		@GetGeneratedKeys
		public int addMapRequest(@Bind("mapNumber") int mapNumber, @Bind("latitude") double latitude, @Bind("longitude") double longitude, @Bind("zoom") int zoom, @Bind("xDimension") int xDimension, @Bind("yDimension") int yDimension, @Bind("region") String region, @Bind("language") String language);
		
		@SqlQuery("Select region from MapRequest where id = :id")
		public List<String> getRegion(@Bind("id") int id);
		
		@SqlQuery("Select id from MapRequest where location = :location")
		public List<Integer> getMapRequestsbyLocation(@Bind("location") int location);
		
		@SqlQuery("Select count(distinct latitude, longitude) from MapRequest")
		public int countTiles();
		
		@SqlQuery("Select count(distinct id) from MapRequest")
		public int countAllTiles();
	}
}
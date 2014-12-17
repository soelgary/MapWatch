package com.gsoeller.personalization.maps.data;

import io.dropwizard.jdbi.OptionalContainerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

import com.google.common.base.Optional;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.gsoeller.personalization.maps.MapsLogger;
import com.gsoeller.personalization.maps.PropertiesLoader;
import com.gsoeller.personalization.maps.dao.GoogleMapRequestDao;

public class GoogleTile {

	private List<Map> maps;
	private int fetchJob;
	
	private DBI dbi;
	private Handle handle;
	private GoogleMapRequestDao mapRequestDao;
	
	private Logger LOG = MapsLogger.createLogger("com.gsoeller.personalization.maps.data.Tile");
	
	public GoogleTile(int fetchJob) throws IOException {
		this.fetchJob = fetchJob;
		PropertiesLoader propLoader = new PropertiesLoader();
		dbi = new DBI(propLoader.getProperty("db"), propLoader.getProperty("dbuser"), propLoader.getProperty("dbpwd"));
		dbi.registerContainerFactory(new OptionalContainerFactory());
		handle = dbi.open();
		mapRequestDao = handle.attach(GoogleMapRequestDao.class);
		maps = Lists.newArrayList();
	}
	
	public void addMap(Map map) {
		maps.add(map);
	}
	
	public void compare() {
		LOG.info(String.format("About to compare %d maps for differences", maps.size()));
		Multimap<String, Map> mapHashes = HashMultimap.create();
		for(Map map: maps) {
			mapHashes.put(map.getHash(), map);
		}
		if(mapHashes.keySet().size() < 2) {
			LOG.info(String.format("Did not find any personalization for fetch job with id: %d", fetchJob));
		} else {
			LOG.info(String.format("Found %d different tiles for fetch job with id: %d", mapHashes.size(), fetchJob));
			System.out.println("YEP");
			String message = "";
			for(String hash: mapHashes.keySet()) {
				Collection<Map> sameMaps = mapHashes.get(hash);
				message += String.format("Hash '%s' is associated with %d maps. The id/region pairs are...\n" , hash, sameMaps.size());
				String pairs = "";
				for(Map map: sameMaps) {
					Optional<Region> region = mapRequestDao.getRegion(map.getMapRequest());
					if(region.isPresent()) {
						pairs += String.format("%d:%s, ", map.getId(), region.get());
					} else {
						LOG.severe("Query for region returned no regions");
					}
				}
				message += pairs + "\n";
			}
			LOG.info(message);
		}
	}
	
}

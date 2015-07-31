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
import com.gsoeller.personalization.maps.dao.BingMapRequestDao;

public class BingTile {
	
	private List<BingMap> maps;
	private int fetchJob;
	private int tileNumber;
	
	private DBI dbi;
	private Handle handle;
	private BingMapRequestDao mapRequestDao;
	private Logger LOG = MapsLogger.createLogger("com.gsoeller.personalization.maps.data.BingTile");

	public BingTile(int fetchJob) throws IOException {
		this.fetchJob = fetchJob;
		dbi = new DBI(PropertiesLoader.getProperty("db"), PropertiesLoader.getProperty("dbuser"), PropertiesLoader.getProperty("dbpwd"));
		dbi.registerContainerFactory(new OptionalContainerFactory());
		handle = dbi.open();
		mapRequestDao = handle.attach(BingMapRequestDao.class);
		maps = Lists.newArrayList();
	}
	
	public void addMap(Optional<BingMap> map) {
		if(map.isPresent()) {
			maps.add(map.get());
		}
	}
	
	public void setTile(int tileNumber) {
		this.tileNumber = tileNumber;
	}
	
	public void compare() {
		LOG.info(String.format("About to compare %d maps for differences for tile '%d'", maps.size(), tileNumber));
		Multimap<String, Map> mapHashes = HashMultimap.create();
		for(BingMap map: maps) {
			mapHashes.put(map.getHash(), map);
		}
		if(mapHashes.keySet().size() < 2) {
			LOG.info(String.format("Did not find any personalization for fetch job with id: %d and tile number '%d'", fetchJob, tileNumber));
		} else {
			LOG.info(String.format("Found %d different tiles for fetch job with id: %d and tile number '%d'", mapHashes.size(), fetchJob, tileNumber));
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

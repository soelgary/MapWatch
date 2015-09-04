package com.gsoeller.personalization.maps.managers;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.joda.time.DateTime;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.gsoeller.personalization.maps.MapsLogger;
import com.gsoeller.personalization.maps.dao.BingMapDao;
import com.gsoeller.personalization.maps.dao.BingMapRequestDao;
import com.gsoeller.personalization.maps.dao.amt.BingHITUpdateDao;
import com.gsoeller.personalization.maps.data.BingMap;
import com.gsoeller.personalization.maps.data.BingMapRequest;
import com.gsoeller.personalization.maps.data.Map;
import com.gsoeller.personalization.maps.data.MapRequest;
import com.gsoeller.personalization.maps.data.Region;
import com.gsoeller.personalization.maps.data.amt.BingHITUpdate;
import com.gsoeller.personalization.maps.data.amt.HITUpdateCountryData;

public class BingHITUpdateManager {
	private BingHITUpdateDao updateDao;
	private BingMapDao BingMapDao;
	private BingMapRequestDao mapRequestDao;
	
	private Logger LOG = MapsLogger.createLogger("com.gsoeller.personalization.maps.managers.BingHITUpdateManager");
	
	public BingHITUpdateManager(final BingHITUpdateDao updateDao, 
			final BingMapDao BingMapDao,
			final BingMapRequestDao mapRequestDao) throws IOException {
		this.updateDao = updateDao;
		this.BingMapDao = BingMapDao;
		this.mapRequestDao = mapRequestDao;
	}
	
	public int createUpdate(BingHITUpdate update) {
		return updateDao.createUpdate(update.getHitId(), 
				update.getOldMap().getId(), 
				update.getNewMap().getId(), 
				update.isHasBorderChange(), 
				update.getNotes());
	}
	
	public List<BingHITUpdate> getUpdates(int count, int offset, boolean finished) {
		List<BingHITUpdate> updates = updateDao.getHITUpdates(finished, count, offset);
		return setMaps(updates);
	}
	
	public List<BingHITUpdate> getUpdatesBasedOnBorderDifference(int count, int offset, boolean hasBorderDifference) {
		List<BingHITUpdate> updates = updateDao.getHITUpdatesBasedOnBorderDifference(hasBorderDifference, count, offset);
		return setMaps(updates);
	}
	
	public Optional<BingHITUpdate> getUpdate(int id) {
		List<BingHITUpdate> updates =  updateDao.getHITUpdate(id);
		if(updates.size() == 1) {
			BingHITUpdate update = updates.get(0);
			update.setNewMap(getMap(update.getNewMap().getId()).get());
			update.setOldMap(getMap(update.getOldMap().getId()).get());
			return Optional.fromNullable(update);
		}
		return Optional.absent();
	}
	
	/*
	 * Gets all of the country information for a specific update id.
	 * This information includes all of the pairs of Maps that have 
	 * both maps consecutively.
	 * 
	 */
	public List<HITUpdateCountryData> getCountryInformation(int id) {
		Optional<BingHITUpdate> update = getUpdate(id);
		if(update.isPresent()) {
			String oldHash = update.get().getOldMap().getHash();
			String newHash = update.get().getNewMap().getHash();
			List<BingHITUpdate> updates = setMaps(updateDao.getSimilarUpdates(oldHash, newHash));
			List<HITUpdateCountryData> countryData = Lists.newArrayList();
			for(BingHITUpdate mapUpdate: updates) {
				Optional<Region> region = getRegion(mapUpdate.getNewMap().getMapRequest().getId());
				DateTime dateTime = mapUpdate.getNewMap().getDateTime();
				countryData.add(new HITUpdateCountryData(region.get(), dateTime));
			}
			return countryData;
		}
		LOG.severe(String.format("Unable to find update in the database -- ID: ", id));
		throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
				.entity("Unable to find update in the database")
				.build());
	}
	
	private Optional<Region> getRegion(int mapId) {
		List<String> region = mapRequestDao.getRegion(mapId);
		if(region.size() == 1) {
			return Optional.fromNullable(Region.findRegion(region.get(0)));
		}
		LOG.severe(String.format("Unable to find region for mapId -- %s", mapId));
		return Optional.absent();
	}
	
	private List<BingHITUpdate> setMaps(List<BingHITUpdate> updates) {
		List<BingHITUpdate> completeUpdates = Lists.newArrayList();
		for(BingHITUpdate update: updates) {
			completeUpdates.add(setMap(update));
		}
		return completeUpdates;
	}
	
	private BingHITUpdate setMap(BingHITUpdate update) {
		update.setOldMap(getMap(update.getOldMap().getId()).get());
		update.setNewMap(getMap(update.getNewMap().getId()).get());
		return update;
	}
	
	
	private Optional<BingMap> getMap(int id) {
		List<BingMap> map = BingMapDao.getMap(id);
		if(map.isEmpty()) {
			return Optional.absent();
		}
		int mapRequest = map.get(0).getMapRequest().getId();
		List<MapRequest> requests = mapRequestDao.getRequest(mapRequest);
		BingMap BingMap = map.get(0);
		if(requests.size() == 1) {
			return Optional.of(new BingMap.MapBuilder()
				.setDateTime(BingMap.getDateTime())
				.setFetchJob(BingMap.getFetchJob())
				.setHasChanged(BingMap.hasChanged())
				.setHash(BingMap.getHash())
				.setId(BingMap.getId())
				.setMapRequest((BingMapRequest)requests.get(0))
				.setPath(BingMap.getPath())
				.build());
				
		}
		return Optional.fromNullable(map.get(0));
	}
	
	public int countSimilarUpdates(Map oldMap, Map newMap) {
		return updateDao.countUpdatesWithTiles(oldMap.getHash(), newMap.getHash());
	}
}

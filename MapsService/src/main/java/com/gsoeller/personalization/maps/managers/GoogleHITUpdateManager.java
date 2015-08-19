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
import com.gsoeller.personalization.maps.dao.GoogleMapDao;
import com.gsoeller.personalization.maps.dao.GoogleMapRequestDao;
import com.gsoeller.personalization.maps.dao.amt.GoogleHITUpdateDao;
import com.gsoeller.personalization.maps.data.GoogleMap;
import com.gsoeller.personalization.maps.data.GoogleMapRequest;
import com.gsoeller.personalization.maps.data.MapRequest;
import com.gsoeller.personalization.maps.data.Region;
import com.gsoeller.personalization.maps.data.amt.GoogleHITUpdate;
import com.gsoeller.personalization.maps.data.amt.GoogleHITUpdateCountryData;

public class GoogleHITUpdateManager {
	
	private GoogleHITUpdateDao updateDao;
	private GoogleMapDao googleMapDao;
	private GoogleMapRequestDao mapRequestDao;
	
	private Logger LOG = MapsLogger.createLogger("com.gsoeller.personalization.maps.managers.GoogleHITUpdateManager");
	
	public GoogleHITUpdateManager(final GoogleHITUpdateDao updateDao, 
			final GoogleMapDao googleMapDao,
			final GoogleMapRequestDao mapRequestDao) throws IOException {
		this.updateDao = updateDao;
		this.googleMapDao = googleMapDao;
		this.mapRequestDao = mapRequestDao;
	}
	
	public int createUpdate(GoogleHITUpdate update) {
		return updateDao.createUpdate(update.getHitId(), 
				update.getOldMap().getId(), 
				update.getNewMap().getId(), 
				update.isHasBorderChange(), 
				update.getNotes());
	}
	
	public List<GoogleHITUpdate> getUpdates(int count, int offset, boolean finished) {
		List<GoogleHITUpdate> updates = updateDao.getHITUpdates(finished, count, offset);
		return setMaps(updates);
	}
	
	public List<GoogleHITUpdate> getUpdatesBasedOnBorderDifference(int count, int offset, boolean hasBorderDifference) {
		List<GoogleHITUpdate> updates = updateDao.getHITUpdatesBasedOnBorderDifference(hasBorderDifference, count, offset);
		return setMaps(updates);
	}
	
	public Optional<GoogleHITUpdate> getUpdate(int id) {
		List<GoogleHITUpdate> updates =  updateDao.getHITUpdate(id);
		if(updates.size() == 1) {
			GoogleHITUpdate update = updates.get(0);
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
	public List<GoogleHITUpdateCountryData> getCountryInformation(int id) {
		Optional<GoogleHITUpdate> update = getUpdate(id);
		if(update.isPresent()) {
			String oldHash = update.get().getOldMap().getHash();
			String newHash = update.get().getNewMap().getHash();
			List<GoogleHITUpdate> updates = setMaps(updateDao.getSimilarUpdates(oldHash, newHash));
			List<GoogleHITUpdateCountryData> countryData = Lists.newArrayList();
			for(GoogleHITUpdate mapUpdate: updates) {
				Optional<Region> region = getRegion(mapUpdate.getNewMap().getMapRequest().getId());
				DateTime dateTime = mapUpdate.getNewMap().getDateTime();
				countryData.add(new GoogleHITUpdateCountryData(region.get(), dateTime));
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
	
	private List<GoogleHITUpdate> setMaps(List<GoogleHITUpdate> updates) {
		List<GoogleHITUpdate> completeUpdates = Lists.newArrayList();
		for(GoogleHITUpdate update: updates) {
			completeUpdates.add(setMap(update));
		}
		return completeUpdates;
	}
	
	private GoogleHITUpdate setMap(GoogleHITUpdate update) {
		update.setOldMap(getMap(update.getOldMap().getId()).get());
		update.setNewMap(getMap(update.getNewMap().getId()).get());
		return update;
	}
	
	
	private Optional<GoogleMap> getMap(int id) {
		List<GoogleMap> map = googleMapDao.getMap(id);
		if(map.isEmpty()) {
			return Optional.absent();
		}
		int mapRequest = map.get(0).getMapRequest().getId();
		List<MapRequest> requests = mapRequestDao.getRequest(mapRequest);
		GoogleMap googleMap = map.get(0);
		if(requests.size() == 1) {
			return Optional.of(new GoogleMap.MapBuilder()
				.setDateTime(googleMap.getDateTime())
				.setFetchJob(googleMap.getFetchJob())
				.setHasChanged(googleMap.hasChanged())
				.setHash(googleMap.getHash())
				.setId(googleMap.getId())
				.setMapRequest((GoogleMapRequest)requests.get(0))
				.setPath(googleMap.getPath())
				.build());
				
		}
		return Optional.fromNullable(map.get(0));
	}
}

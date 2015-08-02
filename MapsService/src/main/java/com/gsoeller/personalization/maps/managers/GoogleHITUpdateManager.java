package com.gsoeller.personalization.maps.managers;

import java.io.IOException;
import java.util.List;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.gsoeller.personalization.maps.dao.GoogleMapDao;
import com.gsoeller.personalization.maps.dao.amt.GoogleHITUpdateDao;
import com.gsoeller.personalization.maps.data.GoogleMap;
import com.gsoeller.personalization.maps.data.Map;
import com.gsoeller.personalization.maps.data.amt.GoogleHITUpdate;

public class GoogleHITUpdateManager {
	
	private GoogleHITUpdateDao updateDao;
	private GoogleMapDao googleMapDao;
	
	public GoogleHITUpdateManager(final GoogleHITUpdateDao updateDao, final GoogleMapDao googleMapDao) throws IOException {
		this.updateDao = updateDao;
		this.googleMapDao = googleMapDao;
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
		return Optional.fromNullable(map.get(0));
	}
}

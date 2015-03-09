package com.gsoeller.personalization.maps.managers;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.gsoeller.personalization.maps.dao.GoogleMapDao;
import com.gsoeller.personalization.maps.dao.GoogleMapUpdateDao;
import com.gsoeller.personalization.maps.data.MapChange;

public class GoogleUpdateManager extends UpdateManager {

	private GoogleMapUpdateDao updateDao;
	private GoogleMapDao mapDao;
	
	public GoogleUpdateManager() throws IOException {
		updateDao = new GoogleMapUpdateDao();
		mapDao = new GoogleMapDao();
	}
	
	public List<MapChange> getUpdates(int offset, int count, boolean inProgress, boolean reserve) {
		if(reserve) {
			return getUpdatesAndReserve(offset, count);
		}
		return injectMaps(updateDao.getUpdates(offset, count, inProgress));
	}
	
	public void update(MapChange change) {
		String notes = "";
		if(!change.getId().isPresent()) {
			throw new NoSuchElementException("No ID given for the update.");
		}
		if(change.getNotes().isPresent()) {
			notes = change.getNotes().get();
		}
		boolean needsInvestigation = change.getNeedsInvestigation();
		int id = change.getId().get();
		updateDao.update(id, notes, needsInvestigation);
	}
	
	private List<MapChange> getUpdatesAndReserve(int offset, int count) {
		boolean inProgress = false;
		List<MapChange> changes = injectMaps(updateDao.getUpdates(offset, count, inProgress));
		List<MapChange> reservedChanges = Lists.newArrayList();
		for(MapChange change: changes) {
			if(updateDao.reserve(change.getId().get())) {
				reservedChanges.add(change);
			} else {
				reservedChanges.add(findNext());
			}
		}
		return changes;
	}
	
	private MapChange findNext() {
		int numAttempts = 10;
		for(int i = 0; i < numAttempts; i++) {
			Optional<MapChange> change = updateDao.findNext();
			if(change.isPresent()) {
				if(updateDao.reserve(change.get().getId().get())) {
					return change.get();
				}
			} 
		}
		throw new NoSuchElementException("Exhausted retries for getting the next update for evaluation");
	}
	
	private List<MapChange> injectMaps(List<MapChange> changes) {
		List<MapChange> injected = Lists.newArrayList();
		for(MapChange change: changes) {
			int oldMap = change.getOldMap().getId();
			int newMap = change.getNewMap().getId();
			change.setNewMap(mapDao.getMap(newMap).get());
			change.setOldMap(mapDao.getMap(oldMap).get());
			injected.add(change);
		}
		return injected;
	}
}

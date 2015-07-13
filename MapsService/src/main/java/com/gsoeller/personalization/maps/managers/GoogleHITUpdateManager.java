package com.gsoeller.personalization.maps.managers;

import java.io.IOException;
import java.util.List;

import com.google.common.base.Optional;
import com.gsoeller.personalization.maps.dao.amt.GoogleHITUpdateDao;
import com.gsoeller.personalization.maps.data.amt.GoogleHITUpdate;

public class GoogleHITUpdateManager {
	
	private GoogleHITUpdateDao updateDao;
	
	public GoogleHITUpdateManager() throws IOException {
		updateDao = new GoogleHITUpdateDao();
	}
	
	public int createUpdate(GoogleHITUpdate update) {
		return updateDao.createUpdate(update.getHitId(), 
				update.getOldMap().getId(), 
				update.getNewMap().getId(), 
				update.isHasBorderChange(), 
				update.getNotes());
	}
	
	public List<GoogleHITUpdate> getUpdates(int count, int offset, boolean finished) {
		return updateDao.getUpdates(count, offset, finished);
	}
	
	public Optional<GoogleHITUpdate> getUpdate(int id) {
		return updateDao.getUpdate(id);
	}
}

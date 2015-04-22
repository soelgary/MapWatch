package com.gsoeller.personalization.maps.managers;

import java.io.IOException;

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
}

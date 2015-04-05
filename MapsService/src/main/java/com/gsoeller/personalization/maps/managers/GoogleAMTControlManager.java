package com.gsoeller.personalization.maps.managers;

import java.io.IOException;
import java.util.List;

import com.google.common.base.Optional;
import com.gsoeller.personalization.maps.dao.amt.GoogleAMTControlDao;
import com.gsoeller.personalization.maps.data.amt.GoogleControlUpdate;

public class GoogleAMTControlManager {
	
	public GoogleAMTControlDao dao;
	
	public GoogleAMTControlManager() throws IOException {
		this.dao = new GoogleAMTControlDao();
	}
	
	public List<GoogleControlUpdate> getControls(int offset, int count) {
		return dao.getControls(offset, count);
	}
	
	public Optional<GoogleControlUpdate> getControl(int id) {
		return dao.getControl(id);
	}
	
	public int createControl() {
		return dao.createControl();
	}
}

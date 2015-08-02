package com.gsoeller.personalization.maps.managers;

import java.io.IOException;
import java.util.List;

import com.google.common.base.Optional;
import com.gsoeller.personalization.maps.dao.amt.GoogleAMTControlDao;
import com.gsoeller.personalization.maps.data.amt.GoogleControlUpdate;

public class GoogleAMTControlManager {
	
	private GoogleAMTControlDao dao;
	
	public GoogleAMTControlManager(final GoogleAMTControlDao controlDao) throws IOException {
		this.dao = controlDao;
	}
	
	public List<GoogleControlUpdate> getControls(int offset, int count) {
		return dao.getControls(offset, count);
	}
	
	public Optional<GoogleControlUpdate> getControl(int id) {
		List<GoogleControlUpdate> controls = dao.getControl(id);
		if(controls.size() == 1) {
			return Optional.fromNullable(controls.get(0));
		}
		return Optional.absent();
	}
	
	public int createControl(GoogleControlUpdate update) {
		return dao.createControl(update.getOldMap().getId(), update.getNewMap().getId(), update.isHasBorderDifference());
	}
}

package com.gsoeller.personalization.maps.managers;

import java.io.IOException;
import java.util.List;

import com.google.common.base.Optional;
import com.gsoeller.personalization.maps.dao.amt.BingAMTControlDao;
import com.gsoeller.personalization.maps.data.amt.BingControlUpdate;

public class BingAMTControlManager {
	
	private BingAMTControlDao dao;

	public BingAMTControlManager(final BingAMTControlDao controlDao)
			throws IOException {
		this.dao = controlDao;
	}

	public List<BingControlUpdate> getControls(int offset, int count) {
		return dao.getControls(offset, count);
	}

	public Optional<BingControlUpdate> getControl(int id) {
		List<BingControlUpdate> controls = dao.getControl(id);
		if (controls.size() == 1) {
			return Optional.fromNullable(controls.get(0));
		}
		return Optional.absent();
	}

	public int createControl(BingControlUpdate update) {
		return dao.createControl(update.getOldMap().getId(), 
				update.getNewMap().getId(), 
				update.isHasBorderDifference());
	}
}

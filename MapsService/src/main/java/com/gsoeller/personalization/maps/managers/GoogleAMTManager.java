package com.gsoeller.personalization.maps.managers;

import java.io.IOException;
import java.util.List;

import com.google.common.base.Optional;
import com.gsoeller.personalization.maps.dao.amt.GoogleHITDao;
import com.gsoeller.personalization.maps.data.amt.GoogleHIT;

public class GoogleAMTManager {
	
	private GoogleHITDao dao;
	
	public GoogleAMTManager() throws IOException {
		this.dao = new GoogleHITDao();
	}
	
	public Optional<GoogleHIT> getHIT(int id) {
		return dao.getHIT(id);
	}
	
	public List<GoogleHIT> getHITS(int offset, int count) {
		return dao.getHITS(offset, count);
	}
	
	public int createHIT(GoogleHIT hit) {
		return dao.createHIT(hit.getId(), hit.getTurkId(), hit.getControl().getId(), hit.isApproved());
	}
}

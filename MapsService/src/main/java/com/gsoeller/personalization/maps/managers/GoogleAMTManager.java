package com.gsoeller.personalization.maps.managers;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.common.base.Optional;
import com.gsoeller.personalization.maps.dao.amt.GoogleHITDao;
import com.gsoeller.personalization.maps.dao.amt.GoogleHITUpdateDao;
import com.gsoeller.personalization.maps.data.amt.GoogleHIT;

public class GoogleAMTManager {
	
	private GoogleHITDao dao;
	private GoogleHITUpdateDao updateDao;
	
	private final int DEFAULT_OFFSET = 0;
	private final int DEFAULT_HIT_COUNT = 10;
	
	public GoogleAMTManager() throws IOException {
		this.dao = new GoogleHITDao();
		this.updateDao = new GoogleHITUpdateDao();
	}
	
	public int approveHIT(int hitId) throws WebApplicationException {
		Optional<GoogleHIT> hit = dao.getHIT(hitId);
		if(!hit.isPresent()) {
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST)
					.entity(String.format("HIT, `%s`, does not exist, therefore cannot approve it", hitId))
					.type(MediaType.APPLICATION_JSON)
					.build());
		}
		if(hit.get().isApproved()) {
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST)
					.entity(String.format("HIT, `%s`, was already approved. Not trying again...", hitId))
					.type(MediaType.APPLICATION_JSON)
					.build());
		}
		// need to actually send the HIT to AMT
		
		// need to tell the DB that the HIT was approved and sent
		return dao.approve(hitId);
	}
	
	public Optional<GoogleHIT> getHIT(int id) {
		return dao.getHIT(id);
	}
	
	public List<GoogleHIT> getHITS(int offset, int count) {
		return dao.getHITS(offset, count);
	}
	
	public List<GoogleHIT> getNextAvailableHits(int count) {
		return dao.getNextAvailableHITs(DEFAULT_OFFSET, count);
	}
	
	public int createHIT(GoogleHIT hit) {
		return dao.createHIT(hit.getTurkId(), hit.getControl().getId(), hit.isApproved(), hit.isReadyForApproval());
	}
	
	public void markHITSForApproval(List<GoogleHIT> hits) {
		for(GoogleHIT hit: hits) {
			markHITForApproval(hit);
		}
	}
	
	private void markHITForApproval(GoogleHIT hit) {
		int count = updateDao.countUpdates(hit.getId());
		if(count >= DEFAULT_HIT_COUNT) {
			dao.markForApproval(hit.getId());
		}
	}
}

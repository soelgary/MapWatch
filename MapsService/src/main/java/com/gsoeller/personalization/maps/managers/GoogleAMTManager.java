package com.gsoeller.personalization.maps.managers;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.amazonaws.mturk.addon.HITDataCSVReader;
import com.amazonaws.mturk.addon.HITDataCSVWriter;
import com.amazonaws.mturk.addon.HITDataInput;
import com.amazonaws.mturk.addon.HITDataOutput;
import com.amazonaws.mturk.addon.HITProperties;
import com.amazonaws.mturk.addon.HITQuestion;
import com.amazonaws.mturk.requester.HIT;
import com.amazonaws.mturk.service.axis.RequesterService;
import com.amazonaws.mturk.util.PropertiesClientConfig;
import com.google.common.base.Optional;
import com.gsoeller.personalization.maps.PropertiesLoader;
import com.gsoeller.personalization.maps.dao.amt.GoogleHITDao;
import com.gsoeller.personalization.maps.dao.amt.GoogleHITUpdateDao;
import com.gsoeller.personalization.maps.data.amt.GoogleHIT;
import com.gsoeller.personalization.maps.data.amt.GoogleHITUpdate;

public class GoogleAMTManager {
	
	private GoogleHITDao dao;
	private GoogleHITUpdateDao updateDao;
	
	private final int DEFAULT_OFFSET = 0;
	private final int DEFAULT_HIT_COUNT = 10;
		
	public GoogleAMTManager() throws IOException {
		this.dao = new GoogleHITDao();
		this.updateDao = new GoogleHITUpdateDao();
	}
	
	public List<GoogleHIT> approveHITS(int count) {
		List<GoogleHIT> hitsToApprove = dao.getHITS(0, count, true, false);
		System.out.println("START OF HITS");
		for(GoogleHIT hit: hitsToApprove) {
			System.out.println("HIT -> " + hit.getId());
			if(!approveHIT(hit)) {
				throw new WebApplicationException(Response.status(Status.BAD_REQUEST)
						.entity(String.format("Error approving HIT, `%s`", hit.getId()))
						.type(MediaType.APPLICATION_JSON)
						.build()); 
			}
		}
		System.out.println("END OF HITS");
		return hitsToApprove;
	}
	
	public boolean approveHIT(GoogleHIT hit) {
		if(hit.isApproved()) {
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST)
					.entity(String.format("HIT `%s` is already approved", hit.getId()))
					.type(MediaType.APPLICATION_JSON)
					.build());
		}
		try {
			Optional<String> hitId = sendHITToTurk(hit.getId());
			if(hitId.isPresent()) {
				dao.approve(hit.getId());
				dao.setMTurkHitId(hitId.get(), hit.getId());
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Optional<GoogleHIT> getHIT(int id) {
		return dao.getHIT(id);
	}
	
	public Optional<GoogleHIT> getHITFromMTurkHitId(String id) {
		return dao.getHITFromMTurkHitId(id);
	}
	
	public List<GoogleHIT> getHITS(int offset, int count, boolean readyForApproval, boolean approved) {
		return dao.getHITS(offset, count, readyForApproval, approved);
	}
	
	public Optional<GoogleHITUpdate> getUpdate(String hitId, int updateId) {
		return updateDao.getUpdate(hitId, updateId);
	}
	
	public Optional<GoogleHITUpdate> updateGoogleHITUpdate(int updateId, GoogleHITUpdate update) {
		return updateDao.update(updateId, update.isHasBorderChange());
	}
	
	public Optional<GoogleHIT> updateGoogleHITControlResponse(String hitId, GoogleHIT hit) {
		dao.updateControlResponse(hitId, hit.isControlResponse());
		return dao.getHITFromMTurkHitId(hitId);
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
	
	public Optional<String> sendHITToTurk(int hitId) throws Exception {
		System.out.println("Creating test HIT");

		PropertiesLoader prop = new PropertiesLoader();
		System.out.println(prop.getProperty("mturkproperties"));
		PropertiesClientConfig p = new PropertiesClientConfig(
				prop.getProperty("mturkproperties"));
		RequesterService service = new RequesterService(p);

		HITQuestion question = new HITQuestion();
		question.setQuestion(generateQuestionXML());
		HITProperties props = new HITProperties(
				prop.getProperty("mturkexternalproperties"));
		HITDataInput input = new HITDataCSVReader(
				prop.getProperty("mturkinput"));
	

		System.out.println("--[Loading HITs]----------");
		Date startTime = new Date();
		System.out.println("  Start time: " + startTime);

		HITDataOutput success = new HITDataCSVWriter(
				prop.getProperty("mturkinput") + ".success");
		HITDataOutput failure = new HITDataCSVWriter(
				prop.getProperty("mturkinput") + ".failure");

		HIT[] hits = service
				.createHITs(input, props, question, success, failure);
		System.out.println("--[End Loading HITs]----------");
		Date endTime = new Date();
		System.out.println("  End time: " + endTime);
		System.out.println("--[Done Loading HITs]----------");
		System.out.println("  Total load time: "
				+ (endTime.getTime() - startTime.getTime()) / 1000
				+ " seconds.");
		
		if(hits.length == 1) {
			return Optional.fromNullable(hits[0].getHITId());
		}
		System.out.println("Incorrect number of HITs created");
		System.out.println(hits);
		return Optional.absent();
	}
	
	private String generateQuestionXML() {
		return "<?xml version=\"1.0\"?>"
				+ "<ExternalQuestion xmlns=\"http://mechanicalturk.amazonaws.com/AWSMechanicalTurkDataSchemas/2006-07-14/ExternalQuestion.xsd\">"
				+ "<ExternalURL>https://achtung.ccs.neu.edu/~soelgary/maps/</ExternalURL>"
				+ "<FrameHeight>800</FrameHeight>"
				+ "</ExternalQuestion>";
	}
}

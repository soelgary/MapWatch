package com.gsoeller.personalization.maps.managers;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.joda.time.DateTime;

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
import com.google.common.collect.Lists;
import com.gsoeller.personalization.maps.PropertiesLoader;
import com.gsoeller.personalization.maps.dao.BingMapDao;
import com.gsoeller.personalization.maps.dao.amt.BingAMTControlDao;
import com.gsoeller.personalization.maps.dao.amt.BingHITDao;
import com.gsoeller.personalization.maps.dao.amt.BingHITUpdateDao;
import com.gsoeller.personalization.maps.data.BingMap;
import com.gsoeller.personalization.maps.data.amt.BingControlUpdate;
import com.gsoeller.personalization.maps.data.amt.BingHIT;
import com.gsoeller.personalization.maps.data.amt.BingHITUpdate;

public class BingAMTManager {
	
	private BingHITDao dao;
	private BingHITUpdateDao updateDao;
	private BingAMTControlDao controlDao;
	private BingMapDao bingMapDao;
	
	private final int DEFAULT_OFFSET = 0;
	private final int DEFAULT_HIT_COUNT = 10;
	
	private static final DateTime START_OF_TIME = new DateTime(0);
	
	public BingAMTManager(final BingHITDao dao, 
			final BingHITUpdateDao updateDao, 
			final BingAMTControlDao controlDao, 
			final BingMapDao bingMapDao) throws IOException {
		this.dao = dao;
		this.updateDao = updateDao;
		this.controlDao = controlDao;
		this.bingMapDao = bingMapDao;
	}
	
	public List<BingHIT> approveHITS(int count) {
		List<BingHIT> hitsToApprove = getHITS(0, count, true, false, START_OF_TIME);
		System.out.println("START OF HITS");
		for(BingHIT hit: hitsToApprove) {
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
	
	public boolean approveHIT(BingHIT hit) {
		if(hit.isApproved()) {
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST)
					.entity(String.format("HIT `%s` is already approved", hit.getId()))
					.type(MediaType.APPLICATION_JSON)
					.build());
		}
		try {
			Optional<String> hitId = sendHITsToTurk();
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
	
	public Optional<BingHIT> getHIT(int id) {
		List<BingHIT> hits = dao.getHIT(id);
		if(hits.size() == 1) {
			return Optional.fromNullable(hits.get(0));
		}
		return Optional.absent();
	}
	
	public Optional<BingHIT> getHITFromMTurkHitId(String id) {
		List<BingHIT> hits = dao.getHITFromMTurkHitId(id);
		if(hits.size() == 1) {
			return Optional.fromNullable(getHIT(hits.get(0)));
		}
		return Optional.absent();
	}
	
	public List<BingHIT> getHITS(int offset, int count, boolean readyForApproval, boolean approved, DateTime createdAfter) {
		List<BingHIT> hits = dao.getHITS(offset, count, readyForApproval, approved, createdAfter.toString());
		List<BingHIT> acc = Lists.newArrayList();
		for(BingHIT hit: hits) {
			acc.add(getHIT(hit));
		}
		return acc;
	}
	
	public Optional<BingHITUpdate> getUpdate(String hitId, int updateId) {
		List<BingHITUpdate> updates = updateDao.getUpdate(hitId, updateId);
		if(updates.size() == 1) {
			BingHITUpdate update = updates.get(0);
			update.setNewMap(getMap(update.getNewMap().getId()).get());
			update.setOldMap(getMap(update.getOldMap().getId()).get());
			return Optional.fromNullable(update);
		}
		return Optional.absent();
	}
	
	public Optional<BingHITUpdate> updateBingHITUpdate(int updateId, BingHITUpdate update) {
		updateDao.update(updateId, update.isHasBorderChange());
		return getUpdate("", updateId);
	}
	
	public Optional<BingHIT> updateBingHITControlResponse(String hitId, BingHIT hit) {
		dao.updateControlResponse(hitId, hit.isControlResponse());
		return getHITFromMTurkHitId(hitId);
	}
	
	public List<BingHIT> getNextAvailableHits(int count) {
		return dao.getNextAvailableHITs(DEFAULT_OFFSET, count);
	}
	
	public int createHIT(BingHIT hit) {
		return dao.createHIT(hit.getTurkId(), hit.getControl().getId(), hit.isApproved(), hit.isReadyForApproval());
	}
	
	public void markHITSForApproval(List<BingHIT> hits) {
		for(BingHIT hit: hits) {
			markHITForApproval(hit);
		}
	}
	
	private void markHITForApproval(BingHIT hit) {
		int count = updateDao.countUpdates(hit.getId());
		if(count >= DEFAULT_HIT_COUNT) {
			dao.markForApproval(hit.getId());
		}
	}
	
	public static void main(String[] args) throws IOException, Exception {
		new BingAMTManager(null, null, null, null).sendHITsToTurk();
	}
	
	public Optional<String> sendHITsToTurk() throws Exception {
		System.out.println("Creating test HIT");

		System.out.println(PropertiesLoader.getProperty("mturkproperties"));
		PropertiesClientConfig p = new PropertiesClientConfig(
				PropertiesLoader.getProperty("mturkproperties"));
		RequesterService service = new RequesterService(p);

		HITQuestion question = new HITQuestion();
		question.setQuestion(generateQuestionXML());
		HITProperties props = new HITProperties(
				PropertiesLoader.getProperty("mturkexternalproperties"));
		HITDataInput input = new HITDataCSVReader(
				PropertiesLoader.getProperty("mturkinput"));
	

		System.out.println("--[Loading HITs]----------");
		Date startTime = new Date();
		System.out.println("  Start time: " + startTime);

		HITDataOutput success = new HITDataCSVWriter(
				PropertiesLoader.getProperty("mturkinput") + ".success");
		HITDataOutput failure = new HITDataCSVWriter(
				PropertiesLoader.getProperty("mturkinput") + ".failure");

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
				+ "<ExternalURL>https://achtung.ccs.neu.edu/~soelgary/maps/?mapProvider=bing</ExternalURL>"
				+ "<FrameHeight>800</FrameHeight>"
				+ "</ExternalQuestion>";
	}
	
	private Optional<BingControlUpdate> getControl(int id) {
		List<BingControlUpdate> controls = controlDao.getControl(id);
		if(controls.size() == 1) {
			return Optional.fromNullable(controls.get(0));
		}
		return Optional.absent();
	}
	
	private Optional<BingMap> getMap(int id) {
		List<BingMap> map = bingMapDao.getMap(id);
		if(map.isEmpty()) {
			return Optional.absent();
		}
		return Optional.fromNullable(map.get(0));
	}
	
	private BingHIT getHIT(BingHIT hit) {
		BingControlUpdate control = getControl(hit.getControl().getId()).get();
		control.setNewMap(getMap(control.getNewMap().getId()).get());
		control.setOldMap(getMap(control.getOldMap().getId()).get());
		List<BingHITUpdate> updates = updateDao.getHITUpdates(hit.getId());
		for(BingHITUpdate update: updates) {
			update.setNewMap(getMap(update.getNewMap().getId()).get());
			update.setOldMap(getMap(update.getOldMap().getId()).get());
		}
		hit.setControl(control);
		hit.setUpdates(updates);
		return hit;
	}
}

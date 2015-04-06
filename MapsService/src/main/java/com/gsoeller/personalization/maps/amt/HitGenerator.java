package com.gsoeller.personalization.maps.amt;

import java.io.IOException;
import java.util.List;
import java.util.Random;

//import com.amazonaws.mturk.service.axis.RequesterService;
//import com.amazonaws.mturk.util.PropertiesClientConfig;
import com.google.common.base.Optional;
import com.gsoeller.personalization.maps.data.GoogleMap;
import com.gsoeller.personalization.maps.data.MapChange;
import com.gsoeller.personalization.maps.data.MapProvider;
import com.gsoeller.personalization.maps.data.amt.GoogleControlUpdate;
import com.gsoeller.personalization.maps.data.amt.GoogleHIT;
import com.gsoeller.personalization.maps.data.amt.GoogleHITUpdate;
import com.gsoeller.personalization.maps.managers.GoogleAMTControlManager;
import com.gsoeller.personalization.maps.managers.GoogleAMTManager;
import com.gsoeller.personalization.maps.managers.GoogleHITUpdateManager;

public class HitGenerator {

	private GoogleAMTManager hitManager;
	private GoogleAMTControlManager controlManager;
	private GoogleHITUpdateManager updateManager;
	
	private final int HIT_COUNT = 3;
	
	private Random random = new Random();
	
	public HitGenerator() throws IOException {
		hitManager = new GoogleAMTManager();
		controlManager = new GoogleAMTControlManager();
		updateManager = new GoogleHITUpdateManager();
	}
	
	public static void main(String[] args) {
		System.out.println("Creating test HIT");
		
		//PropertiesClientConfig p = new PropertiesClientConfig();
		//RequesterService service = new RequesterService(p);
	}
	
	public void addUpdate(MapProvider mapProvider, MapChange change) throws Exception {
		// need to get 3 HIT's
		// if 3 HIT's dont exist -> create as many as needed
		// add the update to each HIT
		// check if each HIT is ready for approval to be sent to AMT?
		if(mapProvider == MapProvider.bing) {
			return;
		}
		List<GoogleHIT> availableHits = hitManager.getNextAvailableHits(HIT_COUNT);
		if(availableHits.size() != HIT_COUNT) {
			// need to create more HIT's
			for(int i = availableHits.size(); i < HIT_COUNT; i++) {
				availableHits.add(generateNewHIT());
			}
		}
		addUpdatesToHITs(change, availableHits);
		hitManager.markHITSForApproval(availableHits);
	}
	
	private void addUpdatesToHITs(MapChange change, List<GoogleHIT> hits) {
		for(GoogleHIT hit: hits) {
			GoogleMap oldMap = new GoogleMap.MapBuilder().setId(change.getOldMap().getId()).build();
			GoogleMap newMap = new GoogleMap.MapBuilder().setId(change.getNewMap().getId()).build();
			GoogleHITUpdate update = new GoogleHITUpdate.GoogleHITUpdateBuilder()
				.setHITId(hit.getId())
				.setHasBorderChange(false)
				.setNotes("")
				.setNewMap(newMap)
				.setOldMap(oldMap)
				.build();
			updateManager.createUpdate(update);
		}
	}
	
	private GoogleControlUpdate getRandomControl() {
		List<GoogleControlUpdate> controls = controlManager.getControls(0, 10);
		int index = random.nextInt(controls.size());
		return controls.get(index);
	}
	
	private GoogleHIT generateNewHIT() throws Exception {
		GoogleHIT hit = new GoogleHIT.GoogleHITBuilder()
			.setApproved(false)
			.setControl(getRandomControl())
			.setReadyForApproval(false)
			.setTurkId(0)
			.build();
		int id = hitManager.createHIT(hit);
		Optional<GoogleHIT> created = hitManager.getHIT(id);
		if(created.isPresent()) {
			return created.get();
		}
		throw new Exception(String.format("Error occurred creating HIT with id %s", id));
	}
}

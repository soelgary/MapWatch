package com.gsoeller.personalization.maps.managers;

import java.util.List;
import java.util.Random;

import com.google.common.base.Optional;
import com.gsoeller.personalization.maps.dao.GoogleFetchJobDao;
import com.gsoeller.personalization.maps.dao.GoogleMapDao;
import com.gsoeller.personalization.maps.dao.GoogleMapRequestDao;
import com.gsoeller.personalization.maps.dao.GoogleMapUpdateDao;
import com.gsoeller.personalization.maps.data.GoogleMap;
import com.gsoeller.personalization.maps.data.Map;
import com.gsoeller.personalization.maps.data.MapChange;
import com.gsoeller.personalization.maps.data.MapProvider;
import com.gsoeller.personalization.maps.data.MapRequest;
import com.gsoeller.personalization.maps.data.amt.GoogleControlUpdate;
import com.gsoeller.personalization.maps.data.amt.GoogleHIT;
import com.gsoeller.personalization.maps.data.amt.GoogleHITUpdate;

public class GoogleCrawlManager implements CrawlManager {

	private GoogleMapRequestDao googleMapRequestDao;
	private GoogleFetchJobDao googleFetchJobDao;
	private GoogleMapUpdateDao googleMapUpdateDao;
	private GoogleMapDao googleMapDao;
	private GoogleAMTManager hitManager;
	private GoogleAMTControlManager controlManager;
	private GoogleHITUpdateManager updateManager;
	
	private final int HIT_COUNT = 3;
	private Random random = new Random();

	public GoogleCrawlManager(final GoogleMapRequestDao googleMapRequestDao,
			final GoogleFetchJobDao googleFetchJobDao,
			final GoogleMapUpdateDao googleMapUpdateDao,
			final GoogleMapDao googleMapDao,
			final GoogleAMTManager hitManager,
			final GoogleAMTControlManager controlManager,
			final GoogleHITUpdateManager updateManager) {
		this.googleMapRequestDao = googleMapRequestDao;
		this.googleFetchJobDao = googleFetchJobDao;
		this.googleMapUpdateDao = googleMapUpdateDao;
		this.googleMapDao = googleMapDao;
		this.hitManager = hitManager;
		this.controlManager = controlManager;
		this.updateManager = updateManager;
	}

	public boolean isLastJobFinished() {
		List<Boolean> finished = googleFetchJobDao.isLastJobFinished();
		if (finished.isEmpty()) {
			return false;
		}
		return finished.get(0);
	}
	
	public int createFetchJob(int mapNumber) {
		return googleFetchJobDao.createFetchJob(mapNumber);
	}

	public Optional<Integer> getLastFetchJob() {
		List<Integer> fetchJobs = googleFetchJobDao.getLastFetchJob();
		if(fetchJobs.isEmpty()) {
			return Optional.absent();
		}
		return Optional.fromNullable(fetchJobs.get(0));
	}

	public Optional<Integer> getLastMap(int currentFetchJob) {
		List<Integer> maps = googleMapDao.getLastMap(currentFetchJob);
		if(maps.isEmpty()) {
			return Optional.absent();
		}
		return Optional.fromNullable(maps.get(0));
	}

	public void finishFetchJob(int fetchJob) {
		googleFetchJobDao.finishFetchJob(fetchJob);
	}

	public Optional<Map> getMapMostRecentWithMapRequestId(int mapRequestId) {
		List<GoogleMap> maps = googleMapDao.getMapMostRecentWithMapRequestId(mapRequestId);
		if(maps.isEmpty()) {
			return Optional.absent();
		}
		return Optional.<Map>fromNullable(maps.get(0));
	}

	public int save(int oldMap, int newMap) {
		return googleMapUpdateDao.saveMap(oldMap, newMap);
	}

	public Optional<MapChange> getUpdate(int id) {
		List<MapChange> changes = googleMapUpdateDao.getUpdate(id);
		if(changes.isEmpty()) {
			return Optional.absent();
		}
		return Optional.fromNullable(changes.get(0));
	}

	public List<MapRequest> getRequests(int limit, int offset, int mapNumber) {
		return googleMapRequestDao.getRequests(limit, offset, mapNumber);
	}

	public int saveMap(boolean hasChanged, int id, String path, String hash,
			int fetchJob) {
		return googleMapDao.saveMap(hasChanged, id, path, hash, fetchJob);
	}

	public Optional<String> getPathWithHash(String hash) {
		List<String> paths = googleMapDao.getPathWithHash(hash);
		if(paths.isEmpty()) {
			return Optional.absent();
		}
		return Optional.fromNullable(paths.get(0));
	}

	public boolean containsHash(String hash) {
		return googleMapDao.countHashes(hash).size() > 0;
	}

	public MapProvider getMapProvider() {
		return MapProvider.google;
	}
	
	public Optional<Map> getMap(int id) {
		List<GoogleMap> maps = googleMapDao.getMap(id);
		if(maps.size() == 1) {
			return Optional.<Map>of(maps.get(0));
		}
		return Optional.absent();
	}
	
	public void addUpdate(Map oldMap, Map newMap) {
		List<GoogleHIT> availableHits = hitManager.getNextAvailableHits(HIT_COUNT);
		if(availableHits.size() != HIT_COUNT) {
			for(int i = availableHits.size(); i < HIT_COUNT; i++) {
				availableHits.add(generateNewHIT());
			}
		}
		addUpdatesToHITs((GoogleMap)oldMap, (GoogleMap)newMap, availableHits);
		hitManager.markHITSForApproval(availableHits);
	}
	
	private void addUpdatesToHITs(GoogleMap oldMap, GoogleMap newMap, List<GoogleHIT> hits) {
		for(GoogleHIT hit: hits) {
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
	
	private GoogleHIT generateNewHIT() {
		GoogleHIT hit = new GoogleHIT.GoogleHITBuilder()
			.setApproved(false)
			.setReadyForApproval(false)
			.setTurkId(0)
			.setControl(getRandomControl())
			.build();
		int id = hitManager.createHIT(hit);
		return hitManager.getHIT(id).get();
	}
	
	private GoogleControlUpdate getRandomControl() {
		List<GoogleControlUpdate> controls = controlManager.getControls(0, 10);
		int index = random.nextInt(controls.size());
		return controls.get(index);
	}
}

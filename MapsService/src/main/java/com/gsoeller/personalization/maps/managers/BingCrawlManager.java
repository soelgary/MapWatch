package com.gsoeller.personalization.maps.managers;

import java.util.List;
import java.util.Random;

import com.google.common.base.Optional;
import com.gsoeller.personalization.maps.dao.BingFetchJobDao;
import com.gsoeller.personalization.maps.dao.BingMapDao;
import com.gsoeller.personalization.maps.dao.BingMapRequestDao;
import com.gsoeller.personalization.maps.dao.BingMapUpdateDao;
import com.gsoeller.personalization.maps.data.BingMap;
import com.gsoeller.personalization.maps.data.Map;
import com.gsoeller.personalization.maps.data.MapChange;
import com.gsoeller.personalization.maps.data.MapProvider;
import com.gsoeller.personalization.maps.data.MapRequest;
import com.gsoeller.personalization.maps.data.amt.BingControlUpdate;
import com.gsoeller.personalization.maps.data.amt.BingHIT;
import com.gsoeller.personalization.maps.data.amt.BingHITUpdate;

public class BingCrawlManager implements CrawlManager {
	
	private BingMapRequestDao bingMapRequestDao;
	private BingFetchJobDao bingFetchJobDao;
	private BingMapUpdateDao bingMapUpdateDao;
	private BingMapDao bingMapDao;
	private BingAMTManager hitManager;
	private BingAMTControlManager controlManager;
	private BingHITUpdateManager updateManager;
	
	private final int HIT_COUNT = 3;
	private Random random = new Random();
	
	public BingCrawlManager(final BingMapRequestDao bingMapRequestDao,
			final BingFetchJobDao bingFetchJobDao,
			final BingMapUpdateDao bingMapUpdateDao,
			final BingMapDao bingMapDao,
			final BingAMTManager hitManager,
			final BingAMTControlManager controlManager,
			final BingHITUpdateManager updateManager) {
		this.bingMapRequestDao = bingMapRequestDao;
		this.bingFetchJobDao = bingFetchJobDao;
		this.bingMapUpdateDao = bingMapUpdateDao;
		this.bingMapDao = bingMapDao;
		this.controlManager = controlManager;
		this.updateManager = updateManager;
	}
	
	public boolean isLastJobFinished() {
		List<Boolean> finished = bingFetchJobDao.isLastJobFinished();
		if(finished.isEmpty()) {
			return false;
		} 
		return finished.get(0);
	}
	
	public int createFetchJob(int mapNumber) {
		return bingFetchJobDao.createFetchJob(mapNumber);
	}

	public Optional<Integer> getLastFetchJob() {
		List<Integer> fetchJobs = bingFetchJobDao.getLastFetchJob();
		if(fetchJobs.isEmpty()) {
			return Optional.absent();
		}
		return Optional.fromNullable(fetchJobs.get(0));
	}

	public Optional<Integer> getLastMap(int currentFetchJob) {
		List<Integer> maps = bingMapDao.getLastMap(currentFetchJob);
		if(maps.isEmpty()) {
			return Optional.absent();
		}
		return Optional.fromNullable(maps.get(0));
	}

	public void finishFetchJob(int fetchJob) {
		bingFetchJobDao.finishFetchJob(fetchJob);
	}

	public Optional<Map> getMapMostRecentWithMapRequestId(int mapRequestId) {
		List<BingMap> maps = bingMapDao.getMapMostRecentWithMapRequestId(mapRequestId);
		if(maps.isEmpty()) {
			return Optional.absent();
		}
		return Optional.<Map>fromNullable(maps.get(0));
	}

	public int save(int oldMap, int newMap) {
		return bingMapUpdateDao.saveMap(oldMap, newMap);
	}

	public Optional<MapChange> getUpdate(int id) {
		List<MapChange> changes = bingMapUpdateDao.getUpdate(id);
		if(changes.isEmpty()) {
			return Optional.absent();
		}
		return Optional.fromNullable(changes.get(0));
	}

	public List<MapRequest> getRequests(int limit, int offset, int mapNumber) {
		return bingMapRequestDao.getRequests(limit, offset, mapNumber);
	}

	public int saveMap(boolean hasChanged, int id, String path, String hash,
			int fetchJob) {
		return bingMapDao.saveMap(hasChanged, id, path, hash, fetchJob);
	}

	public Optional<String> getPathWithHash(String hash) {
		List<String> paths = bingMapDao.getPathWithHash(hash);
		if(paths.isEmpty()) {
			return Optional.absent();
		}
		return Optional.fromNullable(paths.get(0));
	}

	public boolean containsHash(String hash) {
		return bingMapDao.countHashes(hash).size() > 0;
	}

	public MapProvider getMapProvider() {
		return MapProvider.bing;
	}
	
	public Optional<Map> getMap(int id) {
		List<BingMap> maps = bingMapDao.getMap(id);
		if(maps.size() == 1) {
			return Optional.<Map>of(maps.get(0));
		}
		return Optional.absent();
	}
	
	public void addUpdate(Map oldMap, Map newMap) {
		List<BingHIT> availableHits = hitManager.getNextAvailableHits(HIT_COUNT);
		if(availableHits.size() != HIT_COUNT) {
			for(int i = availableHits.size(); i < HIT_COUNT; i++) {
				availableHits.add(generateNewHIT());
			}
		}
		addUpdatesToHITs((BingMap)oldMap, (BingMap)newMap, availableHits);
		hitManager.markHITSForApproval(availableHits);
	}
	
	private void addUpdatesToHITs(BingMap oldMap, BingMap newMap, List<BingHIT> hits) {
		for(BingHIT hit: hits) {
			BingHITUpdate update = new BingHITUpdate.BingHITUpdateBuilder()
				.setHITId(hit.getId())
				.setHasBorderChange(false)
				.setNotes("")
				.setNewMap(newMap)
				.setOldMap(oldMap)
				.build();
			updateManager.createUpdate(update);
		}
	}
	
	private BingHIT generateNewHIT() {
		BingHIT hit = new BingHIT.BingHITBuilder()
			.setApproved(false)
			.setReadyForApproval(false)
			.setTurkId(0)
			.setControl(getRandomControl())
			.build();
		int id = hitManager.createHIT(hit);
		return hitManager.getHIT(id).get();
	}
	
	private BingControlUpdate getRandomControl() {
		List<BingControlUpdate> controls = controlManager.getControls(0, 10);
		int index = random.nextInt(controls.size());
		return controls.get(index);
	}
}

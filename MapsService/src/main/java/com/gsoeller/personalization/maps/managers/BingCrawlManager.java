package com.gsoeller.personalization.maps.managers;

import java.util.List;

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

public class BingCrawlManager implements CrawlManager {
	
	private BingMapRequestDao bingMapRequestDao;
	private BingFetchJobDao bingFetchJobDao;
	private BingMapUpdateDao bingMapUpdateDao;
	private BingMapDao bingMapDao;
	
	public BingCrawlManager(final BingMapRequestDao bingMapRequestDao,
			final BingFetchJobDao bingFetchJobDao,
			final BingMapUpdateDao bingMapUpdateDao,
			final BingMapDao bingMapDao) {
		this.bingMapRequestDao = bingMapRequestDao;
		this.bingFetchJobDao = bingFetchJobDao;
		this.bingMapUpdateDao = bingMapUpdateDao;
		this.bingMapDao = bingMapDao;
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
}

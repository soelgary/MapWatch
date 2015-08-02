package com.gsoeller.personalization.maps.managers;

import java.util.List;

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

public class GoogleCrawlManager implements CrawlManager {

	private GoogleMapRequestDao googleMapRequestDao;
	private GoogleFetchJobDao googleFetchJobDao;
	private GoogleMapUpdateDao googleMapUpdateDao;
	private GoogleMapDao googleMapDao;

	public GoogleCrawlManager(final GoogleMapRequestDao googleMapRequestDao,
			final GoogleFetchJobDao googleFetchJobDao,
			final GoogleMapUpdateDao googleMapUpdateDao,
			final GoogleMapDao googleMapDao) {
		this.googleMapRequestDao = googleMapRequestDao;
		this.googleFetchJobDao = googleFetchJobDao;
		this.googleMapUpdateDao = googleMapUpdateDao;
		this.googleMapDao = googleMapDao;
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
}

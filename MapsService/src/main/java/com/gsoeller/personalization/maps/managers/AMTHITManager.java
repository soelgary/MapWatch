package com.gsoeller.personalization.maps.managers;

import java.util.List;

import com.google.common.base.Optional;
import com.gsoeller.personalization.maps.dao.GoogleMapDao;
import com.gsoeller.personalization.maps.dao.amt.GoogleHITUpdateDao;
import com.gsoeller.personalization.maps.data.Map;

public class AMTHITManager {

	private GoogleMapDao googleMapDao;
	private GoogleHITUpdateDao googleHITUpdateDao;
	
	public AMTHITManager(final GoogleMapDao googleMapDao,
			final GoogleHITUpdateDao googleHITUpdateDao) {
		this.googleMapDao = googleMapDao;
		this.googleHITUpdateDao = googleHITUpdateDao;
	}
	
	public Optional<Map> getMap(int mapRequest, int fetchJob) {
		List<Map> maps = googleMapDao.getMap(mapRequest, fetchJob);
		if(maps.size() > 0) {
			return Optional.fromNullable(maps.get(0));
		}
		return Optional.absent();
	}
	
	public int countUpdatesWithHashSets(String firstHash, String secondHash) {
		return googleHITUpdateDao.countUpdatesWithTiles(firstHash, secondHash);
	}
}

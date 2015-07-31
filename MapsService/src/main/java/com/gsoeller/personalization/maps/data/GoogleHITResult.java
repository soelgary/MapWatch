package com.gsoeller.personalization.maps.data;

import java.util.List;

import com.google.common.collect.Lists;
import com.gsoeller.personalization.maps.data.amt.GoogleHITUpdate;

public class GoogleHITResult {
	private MapChange mapChange;
	private List<GoogleHITUpdate> hitUpdate;
	
	public GoogleHITResult() {}
	
	public GoogleHITResult(MapChange mapChange) {
		this.mapChange = mapChange;
		this.hitUpdate = Lists.newArrayList();
	}

	public MapChange getMapChange() {
		return mapChange;
	}

	public void setMapChange(MapChange mapChange) {
		this.mapChange = mapChange;
	}

	public List<GoogleHITUpdate> getHitUpdate() {
		return hitUpdate;
	}

	public void setHitUpdate(List<GoogleHITUpdate> hitUpdate) {
		this.hitUpdate = hitUpdate;
	}
	
}

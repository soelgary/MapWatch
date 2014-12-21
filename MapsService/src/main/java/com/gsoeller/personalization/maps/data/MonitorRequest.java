package com.gsoeller.personalization.maps.data;

public class MonitorRequest {
	private String mapProvider;
	private long time;
	
	public MonitorRequest(String mapProvider, long time) {
		this.mapProvider = mapProvider;
		this.time = time;
	}

	public String getMapProvider() {
		return mapProvider;
	}

	public long getTime() {
		return time;
	}
	
}

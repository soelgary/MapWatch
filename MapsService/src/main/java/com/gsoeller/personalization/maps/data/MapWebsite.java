package com.gsoeller.personalization.maps.data;

public enum MapWebsite {
	GoogleMaps("http://www.google.com");
	
	private final String baseUrl;
	
	private MapWebsite(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	
	public String getBaseUrl() {
		return baseUrl;
	}
}

package com.gsoeller.personalization.maps.data;

public class BingQuadKey {
	
	private String key;
	
	public BingQuadKey(int key) {
		this.key = String.format("%04d", Integer.toString(key));
	}
	
	public BingQuadKey(String key) {
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
}

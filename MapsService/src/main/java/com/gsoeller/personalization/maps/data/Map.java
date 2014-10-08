package com.gsoeller.personalization.maps.data;

import org.joda.time.DateTime;

public class Map {

	private int mapRequest;
	private DateTime dateTime;
	private boolean hasChanged;
	private int id;
	private String path;
	private String hash;
	
	private Map(MapBuilder builder) {
		this.mapRequest = builder.mapRequest;
		this.dateTime = builder.dateTime;
		this.hasChanged = builder.hasChanged;
		this.id = builder.id;
		this.path = builder.path;
		this.hash = builder.hash;
	}
	
	public String getHash() {
		return hash;
	}
	
	public String getPath() {
		return path;
	}
	
	public int getId() {
		return id;
	}
	
	public int getMapRequest() {
		return mapRequest;
	}
	
	public DateTime getDateTime() {
		return dateTime;
	}
	
	public boolean hasChanged() {
		return hasChanged;
	}
	
	public static class MapBuilder {
		
		private int mapRequest;
		private DateTime dateTime;
		private boolean hasChanged;
		private int id;
		private String path;
		private String hash;
		
		public MapBuilder setHash(String hash) {
			this.hash = hash;
			return this;
		}
		
		public MapBuilder setPath(String path) {
			this.path = path;
			return this;
		}
		
		public MapBuilder setMapRequest(int mapRequest) {
			this.mapRequest = mapRequest;
			return this;
		}
		
		public MapBuilder setDateTime(DateTime dateTime) {
			this.dateTime = dateTime;
			return this;
		}
		
		public MapBuilder setHasChanged(boolean hasChanged) {
			this.hasChanged = hasChanged;
			return this;
		}
		public MapBuilder setId(int id) {
			this.id = id;
			return this;
		}
		
		public Map build() {
			return new Map(this);
		}
	}
}

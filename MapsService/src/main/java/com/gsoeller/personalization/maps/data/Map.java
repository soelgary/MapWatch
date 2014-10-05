package com.gsoeller.personalization.maps.data;

import org.joda.time.DateTime;

public class Map {

	private MapRequest mapRequest;
	private DateTime dateTime;
	private boolean hasChanged;
	private int id;
	
	private Map(MapBuilder builder) {
		this.mapRequest = builder.mapRequest;
		this.dateTime = builder.dateTime;
		this.hasChanged = builder.hasChanged;
		this.id = builder.id;
	}
	
	public int getId() {
		return id;
	}
	
	public MapRequest getMapRequest() {
		return mapRequest;
	}
	
	public DateTime getDateTime() {
		return dateTime;
	}
	
	public boolean hasChanged() {
		return hasChanged;
	}
	
	public static class MapBuilder {
		
		private MapRequest mapRequest;
		private DateTime dateTime;
		private boolean hasChanged;
		private int id;
		
		public MapBuilder setMapRequest(MapRequest mapRequest) {
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

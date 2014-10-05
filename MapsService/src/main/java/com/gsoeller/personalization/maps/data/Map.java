package com.gsoeller.personalization.maps.data;

import org.joda.time.DateTime;

public class Map {

	private MapRequest mapRequest;
	private DateTime dateTime;
	
	private Map(MapBuilder builder) {
		this.mapRequest = builder.mapRequest;
		this.dateTime = builder.dateTime;
	}
	
	public MapRequest getMapRequest() {
		return mapRequest;
	}
	
	public DateTime getDateTime() {
		return dateTime;
	}
	
	public static class MapBuilder {
		
		private MapRequest mapRequest;
		private DateTime dateTime;
		
		public MapBuilder setMapRequest(MapRequest mapRequest) {
			this.mapRequest = mapRequest;
			return this;
		}
		
		public MapBuilder setDateTime(DateTime dateTime) {
			this.dateTime = dateTime;
			return this;
		}
		
		public Map build() {
			return new Map(this);
		}
	}
}

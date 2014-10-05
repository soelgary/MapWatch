package com.gsoeller.personalization.maps.data;

import org.joda.time.DateTime;

public class Map {

	private MapRequest mapRequest;
	private DateTime dateTime;
	private boolean hasChanged;
	
	private Map(MapBuilder builder) {
		this.mapRequest = builder.mapRequest;
		this.dateTime = builder.dateTime;
		this.hasChanged = builder.hasChanged;
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
		
		public Map build() {
			return new Map(this);
		}
	}
}

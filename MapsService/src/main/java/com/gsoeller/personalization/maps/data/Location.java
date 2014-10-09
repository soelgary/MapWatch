package com.gsoeller.personalization.maps.data;

public class Location {

	private double latitude;
	private double longitude;
	private int id;
	
	public Location() {}
	
	private Location(LocationBuilder builder) {
		this.id = builder.id;
		this.latitude = builder.latitude;
		this.longitude = builder.longitude;
	}
	
	public Location(int id) {
		this.id = id;
	}
	
	public Location(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public int getId() {
		return id;
	}
	
	public static class LocationBuilder {
		
		private double latitude;
		private double longitude;
		private int id;
		
		public LocationBuilder setLatitude(double latitude) {
			this.latitude = latitude;
			return this;
		}
		
		public LocationBuilder setLongitude(double longitude) {
			this.longitude = longitude;
			return this;
		}
		
		public LocationBuilder setId(int id) {
			this.id = id;
			return this;
		}
		
		public Location build() {
			return new Location(this);
		}
	}
}

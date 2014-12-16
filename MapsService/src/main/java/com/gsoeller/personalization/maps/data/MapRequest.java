package com.gsoeller.personalization.maps.data;

public class MapRequest {
	private double latitude;
	private double longitude;
	private int mapNumber;
	private int zoom;
	private int xDimension;
	private int yDimension;
	private Region region;
	private Language language;
	private int id;
	
	private final String API_ENDPOINT = "http://maps.googleapis.com/maps/api/staticmap";
	private final String API_KEY = "AIzaSyCuoblPc_plcMWIOGi7C5Td2mRQccxkpuc";
	
	public MapRequest() {}
	
	public MapRequest(int mapNumber, double latitude, double longitude, int zoom, int xDimension, int yDimension, Region region, Language language) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.mapNumber = mapNumber;
		this.language = language;
		this.region = region;
		this.zoom = zoom;
		this.xDimension = xDimension;
		this.yDimension = yDimension;
	}
	
	public int getMapNumber() {
		return mapNumber;
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

	public int getZoom() {
		return zoom;
	}

	public int getxDimension() {
		return xDimension;
	}

	public int getyDimension() {
		return yDimension;
	}

	public Region getRegion() {
		return region;
	}

	public Language getLanguage() {
		return language;
	}
	
	public String buildRequestUrl() {
		return String.format("%s?center=%s,%s&size=%sx%s&zoom=%s&language=%s&region=%s&key=%s", API_ENDPOINT, latitude, longitude, xDimension, yDimension, zoom, language, region, API_KEY);
	}
	
	public String toString() {
		return String.format("ID:\t%d\nZoom:\t%d\nX Dimension:\t%d\nY Dimension:\t%d\nRegion:\t%s\nLanguage:\t%s", 
				id, zoom, xDimension, yDimension, region.toString(), language.toString());
	}

	private MapRequest(MapRequestBuilder builder) {
		this.zoom = builder.zoom;
		this.xDimension = builder.xDimension;
		this.yDimension = builder.yDimension;
		this.region = builder.region;
		this.language = builder.language;
		this.id = builder.id;
		this.mapNumber = builder.mapNumber;
		this.latitude = builder.latitude;
		this.longitude = builder.longitude;
	}
	
	public static class MapRequestBuilder {
		private double latitude;
		private double longitude;
		private int mapNumber;
		private int zoom;
		private int xDimension;
		private int yDimension;
		private Region region;
		private Language language;
		private int id;
		
		public MapRequestBuilder setLatitude(double latitude) {
			this.latitude = latitude;
			return this;
		}
		
		public MapRequestBuilder setLongitude(double longitude) {
			this.longitude = longitude;
			return this;
		}
		
		public MapRequestBuilder setMapNumber(int mapNumber) {
			this.mapNumber = mapNumber;
			return this;
		}
		
		public MapRequestBuilder setId(int id) {
			this.id = id;
			return this;
		}
		
		public MapRequestBuilder setZoom(int zoom) {
			this.zoom = zoom;
			return this;
		}
		
		public MapRequestBuilder setXDimension(int xDimension) {
			this.xDimension = xDimension;
			return this;
		}
		
		public MapRequestBuilder setYDimension(int yDimension) {
			this.yDimension = yDimension;
			return this;
		}
		
		public MapRequestBuilder setRegion(Region region) {
			this.region = region;
			return this;
		}
		
		public MapRequestBuilder setLanguage(Language language) {
			this.language = language;
			return this;
		}
		
		
		public MapRequest build() {
			return new MapRequest(this);
		}
	}
}

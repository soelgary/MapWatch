package com.gsoeller.personalization.maps.data;

public class MapRequest {
	private final int latitude;
	private final int longitude;
	private final int zoom;
	private final int xDimension;
	private final int yDimension;
	private final Region region;
	private final Language language;
	
	public int getLatitude() {
		return latitude;
	}

	public int getLongitude() {
		return longitude;
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

	private MapRequest(MapRequestBuilder builder) {
		this.latitude = builder.latitude;
		this.longitude = builder.longitude;
		this.zoom = builder.zoom;
		this.xDimension = builder.xDimension;
		this.yDimension = builder.yDimension;
		this.region = builder.region;
		this.language = builder.language;
	}
	
	public static class MapRequestBuilder {
		private int latitude;
		private int longitude;
		private int zoom;
		private int xDimension;
		private int yDimension;
		private Region region;
		private Language language;
		
		public MapRequestBuilder() {
			latitude = 26;
			longitude = 78;
			zoom = 5;
			xDimension = 600;
			yDimension = 600;
			region = Region.En;
			language = Language.English;
		}
		
		public MapRequestBuilder setLatitude(int latitude) {
			this.latitude = latitude;
			return this;
		}
		
		public MapRequestBuilder setLongitude(int longitude) {
			this.longitude = longitude;
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

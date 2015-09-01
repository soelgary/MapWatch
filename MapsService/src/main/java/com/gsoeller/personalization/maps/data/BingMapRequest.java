package com.gsoeller.personalization.maps.data;

public class BingMapRequest implements MapRequest {
	private int id;
	private BingQuadKey tileNumber;
	private int mapNumber;
	private Region region;
	
	private final String PATH = "http://ak.dynamic.t1.tiles.virtualearth.net/comp/ch/";
	private final String QUERY_PARAMS = "?mkt=en-us&it=G,VE,BX,L,LA&shading=hill&og=66&n=z&ur=";
	
	private BingMapRequest(int id, BingQuadKey tileNumber, int mapNumber, Region region) {
		this.id = id;
		this.tileNumber = tileNumber;
		this.mapNumber = mapNumber;
		this.region = region;
	}
	
	public String buildRequestUrl() {
		return PATH + tileNumber.getKey() + QUERY_PARAMS + region.toString();
	}
	
	public int getId() {
		return id;
	}
	
	public int getMapNumber() {
		return mapNumber;
	}
	
	public BingQuadKey getTileNumber() {
		return tileNumber;
	}
	
	public Region getRegion() {
		return region;
	}
	
	public double getLatitude() {
		return 0;
	}

	public double getLongitude() {
		return 0;
	}

	public int getZoom() {
		return 0;
	}

	public int getXDimension() {
		return 0;
	}

	public int getYDimension() {
		return 0;
	}

	public Language getLanguage() {
		return Language.English;
	}
	
	public static class BingMapRequestBuilder {
		private int id;
		private BingQuadKey tileNumber;
		private int mapNumber;
		private Region region;
		
		public BingMapRequestBuilder setId(int id) {
			this.id = id;
			return this;
		}
		
		public BingMapRequestBuilder setTileNumber(BingQuadKey tileNumber) {
			this.tileNumber = tileNumber;
			return this;
		}
		
		public BingMapRequestBuilder setMapNumber(int mapNumber) {
			this.mapNumber = mapNumber;
			return this;
		}
		
		public BingMapRequestBuilder setRegion(Region region) {
			this.region = region;
			return this;
		}
		
		public BingMapRequest build() {
			return new BingMapRequest(id, tileNumber, mapNumber, region);
		}
	}
}
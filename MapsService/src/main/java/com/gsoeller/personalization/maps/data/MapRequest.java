package com.gsoeller.personalization.maps.data;

public interface MapRequest {
	
	public String buildRequestUrl();

	public int getId();
	
	public int getMapNumber();
	
	public BingQuadKey getTileNumber();
	
	public Region getRegion();
	
	public double getLatitude();
	
	public double getLongitude();
	
	public int getZoom();
	
	public int getXDimension();
	
	public int getYDimension();
	
	public Language getLanguage();
}

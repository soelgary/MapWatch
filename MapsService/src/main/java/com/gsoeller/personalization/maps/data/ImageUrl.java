package com.gsoeller.personalization.maps.data;

import java.net.URL;

public class ImageUrl {

	private final MapWebsite mapWebsite;
	private final URL url;
	
	public ImageUrl(MapWebsite mapWebsite, URL url) {
		this.mapWebsite = mapWebsite;
		this.url = url;
	}
	
	public MapWebsite getMapWebsite() {
		return mapWebsite;
	}
	
	public URL getUrl() {
		return url;
	}
	
}

package com.gsoeller.personalization.maps.data.amt;

import org.joda.time.DateTime;

import com.gsoeller.personalization.maps.data.Region;

public class HITUpdateCountryData {
	
	private Region region;
	private DateTime dateTime;
	
	public HITUpdateCountryData(Region region, DateTime dateTime) {
		this.region = region;
		this.dateTime = dateTime;
	}
	
	public Region getRegion() {
		return region;
	}
	
	public void setRegion(Region region) {
		this.region = region;
	}
	
	public DateTime getDateTime() {
		return dateTime;
	}
	
	public void setDateTime(DateTime dateTime) {
		this.dateTime = dateTime;
	}
}

package com.gsoeller.personalization.maps.amt;

import com.amazonaws.mturk.service.axis.RequesterService;
import com.amazonaws.mturk.util.PropertiesClientConfig;

public class HitGenerator {

	public static void main(String[] args) {
		System.out.println("Creating test HIT");
		
		PropertiesClientConfig p = new PropertiesClientConfig();
		RequesterService service = new RequesterService(p);
	}
}

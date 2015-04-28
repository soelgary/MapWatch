package com.gsoeller.personalization.maps.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/")
public class TestResource {
	
	@GET
	public String testPage() {
		return "Thanks! It worked!";
	}
}

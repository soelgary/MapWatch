package com.gsoeller.personalization.maps.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.common.collect.Lists;
import com.gsoeller.personalization.maps.dao.MapRequestDao;
import com.gsoeller.personalization.maps.data.MapRequest;

@Path("/maps")
@Produces(MediaType.APPLICATION_JSON)
public class MapsResource {
	
	private final MapRequestDao dao;
	
	public MapsResource(MapRequestDao dao) {
		this.dao = dao;
	}
	
	@GET
	public List<MapRequest> getMapRequests() {
		return dao.getRequests();
	}
	
	@POST
	public MapRequest addRequest(MapRequest mapRequest) {
		dao.addMapRequest(mapRequest.getLatitude(), mapRequest.getLongitude(), mapRequest.getZoom(), mapRequest.getxDimension(), mapRequest.getyDimension(), mapRequest.getRegion().toString(), mapRequest.getLanguage().toString());
		return dao.getRequests().get(0);
	}

}

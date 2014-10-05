package com.gsoeller.personalization.maps.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.gsoeller.personalization.maps.StaticMapFetcher;
import com.gsoeller.personalization.maps.dao.MapRequestDao;
import com.gsoeller.personalization.maps.data.Map;
import com.gsoeller.personalization.maps.data.MapRequest;

@Path("/maps")
@Produces(MediaType.APPLICATION_JSON)
public class MapsResource {
	
	private final MapRequestDao dao;
	private final StaticMapFetcher fetcher = new StaticMapFetcher();
	
	public MapsResource(MapRequestDao dao) {
		this.dao = dao;
	}
	
	@GET
	public List<MapRequest> getMapRequests() {
		List<MapRequest> request = dao.getRequests();
		Map map = new Map.MapBuilder().setMapRequest(request.get(0)).build();
		System.out.println(request.get(0).buildRequestUrl());
		return request;
	}
	
	@POST
	public MapRequest addRequest(MapRequest mapRequest) {
		dao.addMapRequest(mapRequest.getLatitude(), mapRequest.getLongitude(), mapRequest.getZoom(), mapRequest.getxDimension(), mapRequest.getyDimension(), mapRequest.getRegion().toString(), mapRequest.getLanguage().toString());
		return dao.getRequests().get(0);
	}
	
	@POST
	@Path("/send")
	public void sendRequest() {
		fetcher.fetch(getMapRequests().get(0));
	}

}

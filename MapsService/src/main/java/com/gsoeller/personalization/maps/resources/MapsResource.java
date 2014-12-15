package com.gsoeller.personalization.maps.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.gsoeller.personalization.maps.StaticMapFetcher;
import com.gsoeller.personalization.maps.dao.LocationDao;
import com.gsoeller.personalization.maps.dao.MapRequestDao;
import com.gsoeller.personalization.maps.data.Location;
import com.gsoeller.personalization.maps.data.MapRequest;

@Path("/maps")
@Produces(MediaType.APPLICATION_JSON)
public class MapsResource {
	
	private final MapRequestDao dao;
	private final LocationDao locationDao;
	private final StaticMapFetcher fetcher = new StaticMapFetcher();
	
	public MapsResource(MapRequestDao dao, LocationDao locationDao) {
		this.dao = dao;
		this.locationDao = locationDao;
	}
	
	@GET
	public List<MapRequest> getMapRequests() {
		 return dao.getRequests();
	}
	
	@POST
	public MapRequest addRequest(MapRequest mapRequest) {
		int id = dao.addMapRequest(mapRequest.getLocation().getId(), mapRequest.getZoom(), mapRequest.getxDimension(), mapRequest.getyDimension(), mapRequest.getRegion().toString(), mapRequest.getLanguage().toString());
		//return dao.getRequests().get(0);
		return dao.getRequest(id).get(0);
	}
	
	@POST
	@Path("/send")
	public void sendRequest() {
		fetcher.fetch(getMapRequests().get(0));
	}
	
	@POST
	@Path("/location")
	public Location addLocation(Location location) {
		//int id = locationDao.addLocation(location.getLatitude(), location.getLongitude());
		//return locationDao.getLocation(id).get(0);
		return null;
	}
}

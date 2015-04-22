package com.gsoeller.personalization.maps.resources;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import com.google.common.base.Optional;
import com.gsoeller.personalization.maps.dao.GoogleMapUpdateDao;
import com.gsoeller.personalization.maps.data.MapChange;
import com.gsoeller.personalization.maps.data.MapProvider;
import com.gsoeller.personalization.maps.managers.GoogleUpdateManager;

@Path("/maps")
@Produces(MediaType.APPLICATION_JSON)
public class MapUpdateResource {
	
	private static final String GOOGLE = "google";
	private final String DEFAULT_OFFSET = "0";
	private final String DEFAULT_COUNT = "10";
	private final String DEFAULT_IN_PROGRESS = "false";
	private final String DEFAULT_RESERVE = "true";
	
	GoogleMapUpdateDao dao;
	GoogleUpdateManager manager;
	
	public MapUpdateResource() throws IOException {
		dao = new GoogleMapUpdateDao();
		manager = new GoogleUpdateManager();
	}
	
	@GET
	@Path("/{mapProvider}/updates")
	public List<MapChange> getUpdates(@PathParam("mapProvider") String mapProvider, 
			@QueryParam("offset") @DefaultValue(DEFAULT_OFFSET) int offset,
			@QueryParam("count") @DefaultValue(DEFAULT_COUNT) int count,
			@QueryParam("inProgress") @DefaultValue(DEFAULT_IN_PROGRESS) boolean inProgress,
			@QueryParam("reserve") @DefaultValue(DEFAULT_RESERVE) boolean reserve) {
		if(mapProvider.equals(GOOGLE)) {
			List<MapChange> changes = manager.getUpdates(offset, count, inProgress, reserve);
			return changes;
		}
		throw new WebApplicationException(Status.NOT_FOUND);
	}
	
	@PUT
	@Path("/{mapProvider}/updates")
	public void updateUpdates(MapChange change) {
		 manager.update(change);
	}
	
	@POST
	@Path("/{mapProvider}/updates")
	public Optional<MapChange> createUpdate(@PathParam("mapProvider") MapProvider mapProvider, MapChange change) {
		System.out.println(change);
		change.setMapProvider(mapProvider);
		if(mapProvider == MapProvider.google) {
			int id = dao.save(change.getOldMap().getId(), change.getNewMap().getId());
			return dao.getUpdate(id);
		}
		return Optional.absent();
	}
	
	@GET
	@Path("/{mapProvider}/updates/{id}")
	public Optional<MapChange> getUpdate(@PathParam("mapProvider") String mapProvider,
			@PathParam("id") int id) {
		return manager.getUpdate(id);
	}
}

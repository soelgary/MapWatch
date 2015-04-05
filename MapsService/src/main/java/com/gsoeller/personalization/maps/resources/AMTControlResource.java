package com.gsoeller.personalization.maps.resources;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.common.base.Optional;
import com.gsoeller.personalization.maps.data.amt.GoogleControlUpdate;
import com.gsoeller.personalization.maps.managers.GoogleAMTControlManager;

@Path("/maps/{mapProvider}/control")
@Produces(MediaType.APPLICATION_JSON)
public class AMTControlResource {

	private final String DEFAULT_OFFSET = "0";
	private final String DEFAULT_COUNT = "10";
	
	private GoogleAMTControlManager manager;
	
	public AMTControlResource() throws IOException {
		this.manager = new GoogleAMTControlManager();
	}
	
	@GET
	@Path("/{id}")
	public Optional<GoogleControlUpdate> getControl(@PathParam("mapProvider") String mapProvider, @PathParam("id") int id) {
		return manager.getControl(id);
	}
	
	@GET
	public List<GoogleControlUpdate> getControls(@PathParam("mapProvider") String mapProvider,
			@QueryParam("offset") @DefaultValue(DEFAULT_OFFSET) int offset,
			@QueryParam("count") @DefaultValue(DEFAULT_COUNT) int count) {
		return manager.getControls(offset, count);
	}
	
	@POST
	public int createControl(@PathParam("mapProvider") String mapProvider) {
		return manager.createControl();
	}
}


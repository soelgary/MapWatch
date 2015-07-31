package com.gsoeller.personalization.maps.resources;

import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.common.base.Optional;
import com.gsoeller.personalization.maps.data.amt.GoogleHITUpdate;
import com.gsoeller.personalization.maps.managers.GoogleAMTManager;
import com.gsoeller.personalization.maps.managers.GoogleHITUpdateManager;

@Path("/google/updates")
@Produces(MediaType.APPLICATION_JSON)
public class GoogleHITUpdateResource {

	private GoogleHITUpdateManager manager;
	private GoogleAMTManager amtManager;
	
	private static final String DEFAULT_COUNT = "20";
	private static final String DEFAULT_OFFSET = "0";
	private static final String DEFAULT_FINISHED = "false";
	
	public GoogleHITUpdateResource() throws Exception {
		this.manager = new GoogleHITUpdateManager();
		this.amtManager = new GoogleAMTManager();
	}
	
	@GET
	public List<GoogleHITUpdate> getUpdates(
			@QueryParam("finished") @DefaultValue(DEFAULT_FINISHED) boolean finished,
			@QueryParam("count") @DefaultValue(DEFAULT_COUNT) int count,
			@QueryParam("offset") @DefaultValue(DEFAULT_OFFSET) int offset) {
		return manager.getUpdates(count, offset, finished);
	}
	
	@GET
	@Path("{id}")
	public Optional<GoogleHITUpdate> getUpdate(@PathParam("id") int id) {
		return manager.getUpdate(id);
	}
	
	@PUT
	@Path("{id}")
	public Optional<GoogleHITUpdate> updateUpdate(@PathParam("id") int id,
			GoogleHITUpdate googleHITUpdate) {
		return amtManager.updateGoogleHITUpdate(id, googleHITUpdate);
	}
}

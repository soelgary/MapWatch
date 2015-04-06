package com.gsoeller.personalization.maps.resources;

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

import com.google.common.base.Optional;
import com.gsoeller.personalization.maps.data.amt.GoogleHIT;
import com.gsoeller.personalization.maps.managers.GoogleAMTManager;

@Path("/maps/{mapProvider}/hits")
@Produces(MediaType.APPLICATION_JSON)
public class AMTResource {
	
	private GoogleAMTManager manager;
	
	private final String DEFAULT_OFFSET = "0";
	private final String DEFAULT_COUNT = "10";
	
	public AMTResource() throws Exception {
		this.manager = new GoogleAMTManager();
	}
	
	@GET
	@Path("/{hitId}")
	public Optional<GoogleHIT> getHit(@PathParam("mapProvider") String mapProvider,
			@PathParam("hitId") int hitId) {
		return manager.getHIT(hitId);
	}
	
	@PUT
	@Path("/{hitId}")
	public int approveHit(@PathParam("mapProvider") String mapProvider,
			@PathParam("hitId") int hitId) throws WebApplicationException {
		return manager.approveHIT(hitId);
	}
	
	@GET
	public List<GoogleHIT> getHits(@PathParam("mapProvider") String mapProvider,
			@QueryParam("offset") @DefaultValue(DEFAULT_OFFSET) int offset,
			@QueryParam("count") @DefaultValue(DEFAULT_COUNT) int count) {
		return manager.getHITS(offset, count);
	}
	
	@POST
	public int createHit(@PathParam("mapProvider") String mapProvider, GoogleHIT hit) {
		return manager.createHIT(hit);
	}
}

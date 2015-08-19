package com.gsoeller.personalization.maps.resources;

import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.common.base.Optional;
import com.gsoeller.personalization.maps.auth.Role;
import com.gsoeller.personalization.maps.auth.User;
import com.gsoeller.personalization.maps.auth.managers.AuthManager;
import com.gsoeller.personalization.maps.data.amt.GoogleHITUpdate;
import com.gsoeller.personalization.maps.data.amt.GoogleHITUpdateCountryData;
import com.gsoeller.personalization.maps.managers.GoogleAMTManager;
import com.gsoeller.personalization.maps.managers.GoogleHITUpdateManager;

@Path("/google/updates")
@Produces(MediaType.APPLICATION_JSON)
public class GoogleHITUpdateResource {

	private GoogleHITUpdateManager manager;
	private GoogleAMTManager amtManager;
	private AuthManager authManager;
	
	private static final String DEFAULT_COUNT = "20";
	private static final String DEFAULT_OFFSET = "0";
	private static final String DEFAULT_FINISHED = "false";
	
	public GoogleHITUpdateResource(final GoogleAMTManager amtManager, 
			final GoogleHITUpdateManager manager,
			final AuthManager authManager) throws Exception {
		this.manager = manager;
		this.amtManager = amtManager;
		this.authManager = authManager;
	}
	
	@GET
	public List<GoogleHITUpdate> getUpdates(@QueryParam("token") String tokenValue,
			@QueryParam("hasBorderDifference") Optional<Boolean> hasBorderDifference,
			@QueryParam("finished") @DefaultValue(DEFAULT_FINISHED) boolean finished,
			@QueryParam("count") @DefaultValue(DEFAULT_COUNT) int count,
			@QueryParam("offset") @DefaultValue(DEFAULT_OFFSET) int offset) {
		Optional<User> user = authManager.getUser(tokenValue);
    	if(!authManager.isAuthorized(user, Role.RESEARCHER)) {
    		throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    	}
    	if(hasBorderDifference.isPresent()) {
			return manager.getUpdatesBasedOnBorderDifference(count, offset, hasBorderDifference.get());
		} else {
			return manager.getUpdates(count, offset, finished);
		}
	}
	
	@GET
	@Path("{id}")
	public Optional<GoogleHITUpdate> getUpdate(@PathParam("id") int id) {
		return manager.getUpdate(id);
	}
	
	@GET
	@Path("{id}/cctld")
	public List<GoogleHITUpdateCountryData> getCountryData(@PathParam("id") int id) {
		 return manager.getCountryInformation(id);
	}
	
	@PUT
	@Path("{id}")
	public Optional<GoogleHITUpdate> updateUpdate(@QueryParam("token") String tokenValue,
			@PathParam("id") int id,
			GoogleHITUpdate googleHITUpdate) {
		Optional<User> user = authManager.getUser(tokenValue);
		if(authManager.isAuthorized(user, Role.ADMIN)) {
			return amtManager.updateGoogleHITUpdate(id, googleHITUpdate);
		}
		throw new WebApplicationException(Response.Status.UNAUTHORIZED);
	}
}

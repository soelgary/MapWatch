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
import javax.ws.rs.core.MediaType;

import org.joda.time.DateTime;

import com.google.common.base.Optional;
import com.gsoeller.personalization.maps.data.amt.GoogleHIT;
import com.gsoeller.personalization.maps.data.amt.GoogleHITUpdate;
import com.gsoeller.personalization.maps.managers.GoogleAMTManager;

@Path("/maps/{mapProvider}/hits")
@Produces(MediaType.APPLICATION_JSON)
public class AMTResource {
	
	private GoogleAMTManager manager;
	
	private final String DEFAULT_OFFSET = "0";
	private final String DEFAULT_COUNT = "10";
	private final String READY_FOR_APPROVAL = "true";
	private final String APPROVED = "false";
	private final String DEFAULT_CREATED_AFTER = "0";
	
	public AMTResource() throws Exception {
		this.manager = new GoogleAMTManager();
	}
	
	@GET
	@Path("/{hitId}")
	public Optional<GoogleHIT> getHit(@PathParam("mapProvider") String mapProvider,
			@PathParam("hitId") int hitId) {
		return manager.getHIT(hitId);
	}
	
	@GET
	@Path("/mturk/{hitId}")
	public Optional<GoogleHIT> getHitFromMTurkHitId(@PathParam("mapProvider") String mapProvider,
			@PathParam("hitId") String hitId) {
		return manager.getHITFromMTurkHitId(hitId);
	}
	
	@GET
	@Path("/mturk/{hitId}/update/{updateId}")
	public Optional<GoogleHITUpdate> getUpdatesFromHIT(@PathParam("mapProvider") String mapProvider,
			@PathParam("hitId") String hitId,
			@PathParam("updateId") int updateId) {
		return manager.getUpdate(hitId, updateId);
	}
	
	@PUT
	@Path("/mturk/{hitId}/update/{updateId}")
	public Optional<GoogleHITUpdate> updateGoogleHITUpdate(@PathParam("mapProvider") String mapProvider,
			@PathParam("hitId") String hitId,
			@PathParam("updateId") int updateId,
			GoogleHITUpdate update) {
		return manager.updateGoogleHITUpdate(updateId, update);
	}
	
	@PUT
	@Path("/{hitId}/control")
	public Optional<GoogleHIT> updateGoogleHITControlResponse(@PathParam("mapProvider") String mapProvider,
			@PathParam("hitId") String hitId,
			GoogleHIT hit) {
		return manager.updateGoogleHITControlResponse(hitId, hit);
	}
	
	@GET
	public GoogleHITResponse getHits(@PathParam("mapProvider") String mapProvider,
			@QueryParam("offset") @DefaultValue(DEFAULT_OFFSET) int offset,
			@QueryParam("count") @DefaultValue(DEFAULT_COUNT) int count,
			@QueryParam("readyForApproval") @DefaultValue(READY_FOR_APPROVAL) boolean readyForApproval,
			@QueryParam("approved") @DefaultValue(APPROVED) boolean approved,
			@QueryParam("createdAfter") @DefaultValue(DEFAULT_CREATED_AFTER) long createdAfter) {
		System.out.println(createdAfter);
		System.out.println(new DateTime(createdAfter));
		return new GoogleHITResponse(manager.getHITS(offset, count, readyForApproval, approved, new DateTime(createdAfter)));
	}
	
	@POST
	public int createHit(@PathParam("mapProvider") String mapProvider, GoogleHIT hit) {
		return manager.createHIT(hit);
	}
	
	@POST
	@Path("/approve")
	public GoogleHITResponse approve(@PathParam("mapProvider") String mapProvider, Count count) {
		return new GoogleHITResponse(manager.approveHITS(count.getCount()));
	}
	
	public static class Count {
		public int count;
		
		public Count() {}
		
		public void setCount(int count) {
			this.count = count;
		}
		
		public int getCount() {
			return count;
		}
	}
	
	private class GoogleHITResponse {
		private int count;
		private List<GoogleHIT> hits;
		
		public GoogleHITResponse(List<GoogleHIT> hits) {
			this.hits = hits;
			this.count = hits.size();
		}

		public int getCount() {
			return count;
		}

		public List<GoogleHIT> getHits() {
			return hits;
		}
	}
}

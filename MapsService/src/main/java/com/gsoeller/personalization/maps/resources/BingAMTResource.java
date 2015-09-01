package com.gsoeller.personalization.maps.resources;

import java.util.List;

import javax.inject.Singleton;
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
import com.gsoeller.personalization.maps.data.amt.BingHIT;
import com.gsoeller.personalization.maps.data.amt.BingHITUpdate;
import com.gsoeller.personalization.maps.managers.BingAMTManager;

@Path("/maps/bing/hits")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class BingAMTResource {
	
	private BingAMTManager manager;
	
	private final String DEFAULT_OFFSET = "0";
	private final String DEFAULT_COUNT = "10";
	private final String READY_FOR_APPROVAL = "true";
	private final String APPROVED = "false";
	private final String DEFAULT_CREATED_AFTER = "0";
	
	public BingAMTResource(final BingAMTManager manager) throws Exception {
		this.manager = manager;
	}
	
	@GET
	@Path("/{hitId}")
	public Optional<BingHIT> getHit(@PathParam("mapProvider") String mapProvider,
			@PathParam("hitId") int hitId) {
		return manager.getHIT(hitId);
	}
	
	@GET
	@Path("/mturk/{hitId}")
	public Optional<BingHIT> getHitFromMTurkHitId(@PathParam("mapProvider") String mapProvider,
			@PathParam("hitId") String hitId) {
		return manager.getHITFromMTurkHitId(hitId);
	}
	
	@GET
	@Path("/mturk/{hitId}/update/{updateId}")
	public Optional<BingHITUpdate> getUpdatesFromHIT(@PathParam("mapProvider") String mapProvider,
			@PathParam("hitId") String hitId,
			@PathParam("updateId") int updateId) {
		return manager.getUpdate(hitId, updateId);
	}
	
	@PUT
	@Path("/mturk/{hitId}/update/{updateId}")
	public Optional<BingHITUpdate> updateBingHITUpdate(@PathParam("mapProvider") String mapProvider,
			@PathParam("hitId") String hitId,
			@PathParam("updateId") int updateId,
			BingHITUpdate update) {
		return manager.updateBingHITUpdate(updateId, update);
	}
	
	@PUT
	@Path("/{hitId}/control")
	public Optional<BingHIT> updateBingHITControlResponse(@PathParam("mapProvider") String mapProvider,
			@PathParam("hitId") String hitId,
			BingHIT hit) {
		return manager.updateBingHITControlResponse(hitId, hit);
	}
	
	@GET
	public BingHITResponse getHits(@PathParam("mapProvider") String mapProvider,
			@QueryParam("offset") @DefaultValue(DEFAULT_OFFSET) int offset,
			@QueryParam("count") @DefaultValue(DEFAULT_COUNT) int count,
			@QueryParam("readyForApproval") @DefaultValue(READY_FOR_APPROVAL) boolean readyForApproval,
			@QueryParam("approved") @DefaultValue(APPROVED) boolean approved,
			@QueryParam("createdAfter") @DefaultValue(DEFAULT_CREATED_AFTER) long createdAfter) {
		System.out.println(createdAfter);
		System.out.println(new DateTime(createdAfter));
		return new BingHITResponse(manager.getHITS(offset, count, readyForApproval, approved, new DateTime(createdAfter)));
	}
	
	@POST
	public int createHit(@PathParam("mapProvider") String mapProvider, BingHIT hit) {
		return manager.createHIT(hit);
	}
	
	@POST
	@Path("/approve")
	public BingHITResponse approve(@PathParam("mapProvider") String mapProvider, Count count) {
		return new BingHITResponse(manager.approveHITS(count.getCount()));
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
	
	private class BingHITResponse {
		private int count;
		private List<BingHIT> hits;
		
		public BingHITResponse(List<BingHIT> hits) {
			this.hits = hits;
			this.count = hits.size();
		}

		public int getCount() {
			return count;
		}

		public List<BingHIT> getHits() {
			return hits;
		}
	}
}

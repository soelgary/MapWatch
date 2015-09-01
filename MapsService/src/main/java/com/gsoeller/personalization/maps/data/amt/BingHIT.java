package com.gsoeller.personalization.maps.data.amt;

import java.util.List;

import org.joda.time.DateTime;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

public class BingHIT extends HIT {

	public List<BingHITUpdate> updates;
	public BingControlUpdate control;

	public BingHIT() {};
	
	private BingHIT(int id, 
			int turkId, 
			List<BingHITUpdate> updates, 
			BingControlUpdate control, 
			boolean approved, 
			boolean readyForApproval,
			String hitId,
			boolean controlResponse,
			boolean finished,
			Optional<DateTime> created) {
		super(id, turkId, approved, readyForApproval, hitId, controlResponse, finished, created);
		this.updates = updates;
		this.control = control;
	}
	
	public List<BingHITUpdate> getUpdates() {
		return updates;
	}

	public void setUpdates(List<BingHITUpdate> updates) {
		this.updates = updates;
	}

	public BingControlUpdate getControl() {
		return control;
	}

	public void setControl(BingControlUpdate control) {
		this.control = control;
	}

	public static class BingHITBuilder {
		private int id;
		private int turkId;
		private List<BingHITUpdate> updates = Lists.newArrayList();
		private BingControlUpdate control;
		private boolean approved;
		private boolean readyForApproval;
		private String hitId;
		private boolean controlResponse;
		private boolean finished;
		private Optional<DateTime> created = Optional.absent();
		
		public BingHITBuilder setId(int id) {
			this.id = id;
			return this;
		}
		
		public BingHITBuilder setTurkId(int turkId) {
			this.turkId = turkId;
			return this;
		}
		
		public BingHITBuilder setUpdates(List<BingHITUpdate> updates) {
			this.updates = updates;
			return this;
		}
		
		public BingHITBuilder setControl(BingControlUpdate control) {
			this.control = control;
			return this;
		}
		
		public BingHITBuilder setApproved(boolean approved) {
			this.approved = approved;
			return this;
		}
		
		public BingHITBuilder setReadyForApproval(boolean readyForApproval) {
			this.readyForApproval = readyForApproval;
			return this;
		}
		
		public BingHITBuilder setHitId(String hitId) {
			this.hitId = hitId;
			return this;
		}
		
		public BingHITBuilder setControlResponse(boolean controlResponse) {
			this.controlResponse = controlResponse;
			return this;
		}
		
		public BingHITBuilder setFinished(boolean finished) {
			this.finished = finished;
			return this;
		}
		
		public BingHITBuilder setCreated(DateTime created) {
			this.created = Optional.of(created);
			return this;
		}
		
		public BingHIT build() {
			return new BingHIT(id, turkId, updates, control, approved, readyForApproval, hitId, controlResponse, finished, created);
		}
	}
}

package com.gsoeller.personalization.maps.data.amt;

import java.util.List;

import com.google.common.collect.Lists;

public class GoogleHIT {
	
	public int id;
	public int turkId;
	public List<GoogleHITUpdate> updates;
	public GoogleControlUpdate control;
	public boolean approved;
	public boolean readyForApproval;
	
	public GoogleHIT() {};
	
	private GoogleHIT(int id, 
			int turkId, 
			List<GoogleHITUpdate> updates, 
			GoogleControlUpdate control, 
			boolean approved, 
			boolean readyForApproval) {
		this.id = id;
		this.turkId = turkId;
		this.updates = updates;
		this.control = control;
		this.approved = approved;
		this.readyForApproval = readyForApproval;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTurkId() {
		return turkId;
	}

	public void setTurkId(int turkId) {
		this.turkId = turkId;
	}

	public List<GoogleHITUpdate> getUpdates() {
		return updates;
	}

	public void setUpdates(List<GoogleHITUpdate> updates) {
		this.updates = updates;
	}

	public GoogleControlUpdate getControl() {
		return control;
	}

	public void setControl(GoogleControlUpdate control) {
		this.control = control;
	}
	
	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public boolean isReadyForApproval() {
		return readyForApproval;
	}

	public void setReadyForApproval(boolean readyForApproval) {
		this.readyForApproval = readyForApproval;
	}



	public static class GoogleHITBuilder {
		private int id;
		private int turkId;
		private List<GoogleHITUpdate> updates = Lists.newArrayList();
		private GoogleControlUpdate control;
		private boolean approved;
		private boolean readyForApproval;
		
		public GoogleHITBuilder setId(int id) {
			this.id = id;
			return this;
		}
		
		public GoogleHITBuilder setTurkId(int turkId) {
			this.turkId = turkId;
			return this;
		}
		
		public GoogleHITBuilder setId(List<GoogleHITUpdate> updates) {
			this.updates = updates;
			return this;
		}
		
		public GoogleHITBuilder setControl(GoogleControlUpdate control) {
			this.control = control;
			return this;
		}
		
		public GoogleHITBuilder setApproved(boolean approved) {
			this.approved = approved;
			return this;
		}
		
		public GoogleHITBuilder setReadyForApproval(boolean readyForApproval) {
			this.readyForApproval = readyForApproval;
			return this;
		}
		
		public GoogleHIT build() {
			return new GoogleHIT(id, turkId, updates, control, approved, readyForApproval);
		}
	}
}

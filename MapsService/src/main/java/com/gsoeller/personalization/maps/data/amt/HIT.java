package com.gsoeller.personalization.maps.data.amt;

import org.joda.time.DateTime;

import com.google.common.base.Optional;

public abstract class HIT {
	public int id;
	public String hitId;
	public int turkId;
	//public List<HITUpdate> updates;
	//public ControlUpdate control;
	public boolean approved;
	public boolean readyForApproval;
	public boolean controlResponse;
	public boolean finished;
	public Optional<DateTime> created;

	public HIT() {};

	protected HIT(int id, 
			int turkId, 
		//	List<HITUpdate> updates,
			//ControlUpdate control, 
			boolean approved,
			boolean readyForApproval, 
			String hitId, 
			boolean controlResponse,
			boolean finished, 
			Optional<DateTime> created) {
		this.id = id;
		this.turkId = turkId;
		//this.updates = updates;
		//this.control = control;
		this.approved = approved;
		this.readyForApproval = readyForApproval;
		this.hitId = hitId;
		this.controlResponse = controlResponse;
		this.finished = finished;
		this.created = created;
	}

	public Optional<DateTime> getCreated() {
		return created;
	}

	public void setCreated(DateTime created) {
		this.created = Optional.of(created);
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public boolean isControlResponse() {
		return controlResponse;
	}

	public void setControlResponse(boolean controlResponse) {
		this.controlResponse = controlResponse;
	}

	public String getHitId() {
		return hitId;
	}

	public void setHitId(String hitId) {
		this.hitId = hitId;
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

	//public List<HITUpdate> getUpdates() {
	//	return updates;
	//}

	//public void setUpdates(List<HITUpdate> updates) {
	//	this.updates = updates;
	//}

	//public ControlUpdate getControl() {
	//	return control;
	//}

	//public void setControl(ControlUpdate control) {
	//	this.control = control;
	//}

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
}

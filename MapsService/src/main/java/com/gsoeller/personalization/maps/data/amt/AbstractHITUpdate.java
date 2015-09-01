package com.gsoeller.personalization.maps.data.amt;

public class AbstractHITUpdate {
	
	public int id;
	public int hitId;
	public boolean hasBorderChange;
	public String notes;
	public boolean finished;
	
	public AbstractHITUpdate() {}
	
	public AbstractHITUpdate(int id, int hitId, boolean hasBorderChange, String notes, boolean finished) {
		this.id = id;
		this.hitId = hitId;
		this.hasBorderChange = hasBorderChange;
		this.notes = notes;
		this.finished = finished;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public int getHitId() {
		return hitId;
	}

	public void setHitId(int hitId) {
		this.hitId = hitId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isHasBorderChange() {
		return hasBorderChange;
	}

	public void setHasBorderChange(boolean hasBorderChange) {
		this.hasBorderChange = hasBorderChange;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
}

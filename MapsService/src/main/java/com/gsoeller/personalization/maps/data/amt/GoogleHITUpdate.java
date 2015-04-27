package com.gsoeller.personalization.maps.data.amt;

import com.gsoeller.personalization.maps.data.GoogleMap;

public class GoogleHITUpdate {

	public int id;
	public int hitId;
	public GoogleMap oldMap;
	public GoogleMap newMap;
	public boolean hasBorderChange;
	public String notes;
	public boolean finished;
	public boolean controlResponse;
	
	public GoogleHITUpdate() {}
	
	public GoogleHITUpdate(int id, int hitId, GoogleMap oldMap, GoogleMap newMap, boolean hasBorderChange, String notes, boolean finished, boolean controlResponse) {
		this.id = id;
		this.hitId = hitId;
		this.oldMap = oldMap;
		this.newMap = newMap;
		this.hasBorderChange = hasBorderChange;
		this.notes = notes;
		this.finished = finished;
		this.controlResponse = controlResponse;
	}

	public boolean isControlResponse() {
		return controlResponse;
	}

	public void setControlResponse(boolean controlResponse) {
		this.controlResponse = controlResponse;
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

	public GoogleMap getOldMap() {
		return oldMap;
	}

	public void setOldMap(GoogleMap oldMap) {
		this.oldMap = oldMap;
	}

	public GoogleMap getNewMap() {
		return newMap;
	}

	public void setNewMap(GoogleMap newMap) {
		this.newMap = newMap;
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
	};
	
	public static class GoogleHITUpdateBuilder {
		private int id;
		private int hitId;
		private GoogleMap oldMap;
		private GoogleMap newMap;
		private boolean hasBorderChange;
		private String notes;
		private boolean finished;
		private boolean controlResponse;
		
		public GoogleHITUpdateBuilder setId(int id) {
			this.id = id;
			return this;
		}
		
		public GoogleHITUpdateBuilder setOldMap(GoogleMap oldMap) {
			this.oldMap = oldMap;
			return this;
		}
		
		public GoogleHITUpdateBuilder setNewMap(GoogleMap newMap) {
			this.newMap = newMap;
			return this;
		}
		
		public GoogleHITUpdateBuilder setHasBorderChange(boolean hasBorderChange) {
			this.hasBorderChange = hasBorderChange;
			return this;
		}
		
		public GoogleHITUpdateBuilder setNotes(String notes) {
			this.notes = notes;
			return this;
		}
		
		public GoogleHITUpdateBuilder setHITId(int hitId) {
			this.hitId = hitId;
			return this;
		}
		
		public GoogleHITUpdateBuilder setFinished(boolean finished) {
			this.finished = finished;
			return this;
		}
		
		public GoogleHITUpdateBuilder setControlResponse(boolean controlResponse) {
			this.controlResponse = controlResponse;
			return this;
		}
		
		public GoogleHITUpdate build() {
			return new GoogleHITUpdate(id, hitId, oldMap, newMap, hasBorderChange, notes, finished, controlResponse);
		}
	}
}

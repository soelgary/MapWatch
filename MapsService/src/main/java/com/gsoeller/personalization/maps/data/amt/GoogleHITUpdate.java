package com.gsoeller.personalization.maps.data.amt;

import com.gsoeller.personalization.maps.data.GoogleMap;

public class GoogleHITUpdate extends AbstractHITUpdate implements HITUpdate {

	public GoogleMap oldMap;
	public GoogleMap newMap;
	
	public GoogleHITUpdate() {}
	
	public GoogleHITUpdate(int id, int hitId, GoogleMap oldMap, GoogleMap newMap, boolean hasBorderChange, String notes, boolean finished) {
		super(id, hitId, hasBorderChange, notes, finished);
		this.oldMap = oldMap;
		this.newMap = newMap;
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
	
	public static class GoogleHITUpdateBuilder {
		private int id;
		private int hitId;
		private GoogleMap oldMap;
		private GoogleMap newMap;
		private boolean hasBorderChange;
		private String notes;
		private boolean finished;
		
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
		
		public GoogleHITUpdate build() {
			return new GoogleHITUpdate(id, hitId, oldMap, newMap, hasBorderChange, notes, finished);
		}
	}
}

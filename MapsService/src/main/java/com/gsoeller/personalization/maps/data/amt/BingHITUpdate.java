package com.gsoeller.personalization.maps.data.amt;

import com.gsoeller.personalization.maps.data.BingMap;

public class BingHITUpdate extends AbstractHITUpdate implements HITUpdate {
	public BingMap oldMap;
	public BingMap newMap;
	
	public BingHITUpdate() {}
	
	public BingHITUpdate(int id, int hitId, BingMap oldMap, BingMap newMap, boolean hasBorderChange, String notes, boolean finished) {
		super(id, hitId, hasBorderChange, notes, finished);
		this.oldMap = oldMap;
		this.newMap = newMap;
	}

	public BingMap getOldMap() {
		return oldMap;
	}

	public void setOldMap(BingMap oldMap) {
		this.oldMap = oldMap;
	}

	public BingMap getNewMap() {
		return newMap;
	}

	public void setNewMap(BingMap newMap) {
		this.newMap = newMap;
	}
	
	public static class BingHITUpdateBuilder {
		private int id;
		private int hitId;
		private BingMap oldMap;
		private BingMap newMap;
		private boolean hasBorderChange;
		private String notes;
		private boolean finished;
		
		public BingHITUpdateBuilder setId(int id) {
			this.id = id;
			return this;
		}
		
		public BingHITUpdateBuilder setOldMap(BingMap oldMap) {
			this.oldMap = oldMap;
			return this;
		}
		
		public BingHITUpdateBuilder setNewMap(BingMap newMap) {
			this.newMap = newMap;
			return this;
		}
		
		public BingHITUpdateBuilder setHasBorderChange(boolean hasBorderChange) {
			this.hasBorderChange = hasBorderChange;
			return this;
		}
		
		public BingHITUpdateBuilder setNotes(String notes) {
			this.notes = notes;
			return this;
		}
		
		public BingHITUpdateBuilder setHITId(int hitId) {
			this.hitId = hitId;
			return this;
		}
		
		public BingHITUpdateBuilder setFinished(boolean finished) {
			this.finished = finished;
			return this;
		}
		
		public BingHITUpdate build() {
			return new BingHITUpdate(id, hitId, oldMap, newMap, hasBorderChange, notes, finished);
		}
	}
}

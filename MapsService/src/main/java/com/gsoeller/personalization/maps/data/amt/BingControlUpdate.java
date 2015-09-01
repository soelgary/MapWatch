package com.gsoeller.personalization.maps.data.amt;

import com.gsoeller.personalization.maps.data.BingMap;

public class BingControlUpdate extends ControlUpdate {
	
	private BingMap oldMap;
	private BingMap newMap;
	
	public BingControlUpdate() {}
	
	private BingControlUpdate(int id, BingMap oldMap, BingMap newMap, boolean hasBorderDifference) {
		super(id, hasBorderDifference);
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
	
	public static class BingControlUpdateBuilder {
		
		private int id;
		private BingMap oldMap;
		private BingMap newMap;
		private boolean hasBorderDifference;
		
		public BingControlUpdateBuilder setId(int id) {
			this.id = id;
			return this;
		}
		
		public BingControlUpdateBuilder setOldMap(BingMap oldMap) {
			this.oldMap = oldMap;
			return this;
		}
		
		public BingControlUpdateBuilder setNewMap(BingMap newMap) {
			this.newMap = newMap;
			return this;
		}
		
		public BingControlUpdateBuilder setHasBorderDifference(boolean hasBorderDifference) {
			this.hasBorderDifference = hasBorderDifference;
			return this;
		}
		
		public BingControlUpdate build() {
			return new BingControlUpdate(id, oldMap, newMap, hasBorderDifference);
		}
	}
}

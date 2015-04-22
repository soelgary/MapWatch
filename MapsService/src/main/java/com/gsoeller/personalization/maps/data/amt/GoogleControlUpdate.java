package com.gsoeller.personalization.maps.data.amt;

import com.gsoeller.personalization.maps.data.GoogleMap;

public class GoogleControlUpdate {
	
	private int id;
	private GoogleMap oldMap;
	private GoogleMap newMap;
	private boolean hasBorderDifference;
	
	public GoogleControlUpdate() {}
	
	private GoogleControlUpdate(int id, GoogleMap oldMap, GoogleMap newMap, boolean hasBorderDifference) {
		this.id = id;
		this.oldMap = oldMap;
		this.newMap = newMap;
		this.hasBorderDifference = hasBorderDifference;
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

	public boolean isHasBorderDifference() {
		return hasBorderDifference;
	}

	public void setHasBorderDifference(boolean hasBorderDifference) {
		this.hasBorderDifference = hasBorderDifference;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public static class GoogleControlUpdateBuilder {
		
		private int id;
		private GoogleMap oldMap;
		private GoogleMap newMap;
		private boolean hasBorderDifference;
		
		public GoogleControlUpdateBuilder setId(int id) {
			this.id = id;
			return this;
		}
		
		public GoogleControlUpdateBuilder setOldMap(GoogleMap oldMap) {
			this.oldMap = oldMap;
			return this;
		}
		
		public GoogleControlUpdateBuilder setNewMap(GoogleMap newMap) {
			this.newMap = newMap;
			return this;
		}
		
		public GoogleControlUpdateBuilder setHasBorderDifference(boolean hasBorderDifference) {
			this.hasBorderDifference = hasBorderDifference;
			return this;
		}
		
		public GoogleControlUpdate build() {
			return new GoogleControlUpdate(id, oldMap, newMap, hasBorderDifference);
		}
	}
}

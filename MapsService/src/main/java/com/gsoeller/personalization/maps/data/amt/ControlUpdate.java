package com.gsoeller.personalization.maps.data.amt;

public abstract class ControlUpdate {
	
	private int id;
	private boolean hasBorderDifference;
	
	public ControlUpdate() {}
	
	protected ControlUpdate(int id, boolean hasBorderDifference) {
		this.id = id;
		this.hasBorderDifference = hasBorderDifference;
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
}

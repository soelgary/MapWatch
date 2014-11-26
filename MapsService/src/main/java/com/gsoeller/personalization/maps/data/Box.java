package com.gsoeller.personalization.maps.data;


public class Box {

	private double left;
	private double right;
	private double bottom;
	private double top;
	
	public Box(double left, double right, double bottom, double top) {
		this.left = left;
		this.right = right;
		this.bottom = bottom;
		this.top = top;
	}
	
	public boolean inBox(double lat, double lon) {
		return lat >= bottom 
			&& lat <= top
			&& lon >= left
			&& lon <= right;
	}
}

package com.gsoeller.personalization.maps.data.amt;

public class GoogleControlUpdate {
	
	public int id;
	
	public GoogleControlUpdate() {}
	
	private GoogleControlUpdate(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public static class GoogleControlUpdateBuilder {
		
		private int id;
		
		public GoogleControlUpdateBuilder setId(int id) {
			this.id = id;
			return this;
		}
		
		public GoogleControlUpdate build() {
			return new GoogleControlUpdate(id);
		}
	}
}

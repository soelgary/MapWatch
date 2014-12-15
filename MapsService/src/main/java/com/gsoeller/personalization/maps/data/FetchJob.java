package com.gsoeller.personalization.maps.data;

import org.joda.time.DateTime;

public class FetchJob {
	private int id;
	private DateTime startTime;
	private boolean finished;
	
	private FetchJob(int id, DateTime startTime, boolean finished) {
		this.id = id;
		this.startTime = startTime;
		this.finished = finished;
	}
	
	public static class FetchJobBuilder {
		private int id;
		private DateTime startTime;
		private boolean finished;
		
		public FetchJobBuilder setId(int id) {
			this.id = id;
			return this;
		}
		
		public FetchJobBuilder setStartTime(DateTime startTime) {
			this.startTime = startTime;
			return this;
		}
		
		public FetchJobBuilder setFinished(boolean finished) {
			this.finished = finished;
			return this;
		}
		
		public FetchJob build() {
			return new FetchJob(id, startTime, finished);
		}
	}
}

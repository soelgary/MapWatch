package com.gsoeller.personalization.maps.data;

import org.joda.time.DateTime;

import com.google.common.base.Optional;

public class MapChange {
	
	private Map oldMap;
	private Map newMap;
	private Optional<Integer> id;
	private Optional<String> notes;
	private Optional<Integer> stage;
	private boolean needsInvestigation;
	private DateTime lastUpdated;
	private boolean inProgress;
	
	public MapChange(MapChangeBuilder builder) {
		this.id = builder.id;
		this.oldMap = builder.oldMap;
		this.newMap = builder.newMap;
		this.notes = builder.notes;
		this.stage = builder.stage; 
		this.needsInvestigation = builder.needsInvestigation;
		this.lastUpdated = builder.lastUpdated;
		this.inProgress = builder.inProgress;
	}
	
	public Map getOldMap() {
		return oldMap;
	}
	
	public Map getNewMap() {
		return newMap;
	}
	
	public Optional<Integer> getId() {
		return id;
	}
	
	public Optional<String> getNotes() {
		return notes;
	}
	
	public Optional<Integer> getStage() {
		return stage;
	}
	
	public boolean getNeedsInvestigation() {
		return needsInvestigation;
	}
	
	public DateTime lastUpdated() {
		return lastUpdated;
	}
	
	public boolean inProgress() {
		return inProgress;
	}
	
	public static class MapChangeBuilder {
		private Map oldMap;
		private Map newMap;
		private Optional<Integer> id = Optional.absent();
		private Optional<String> notes = Optional.absent();
		private Optional<Integer> stage = Optional.absent();
		private boolean needsInvestigation = false;
		private DateTime lastUpdated;
		private boolean inProgress = false;
		
		public MapChangeBuilder(Map oldMap, Map newMap) {
			this.newMap = newMap;
			this.oldMap = oldMap;
			lastUpdated = DateTime.now();
		}
		
		public MapChangeBuilder setId(int id) {
			this.id = Optional.of(id);
			return this;
		}
		
		public MapChangeBuilder setNotes(String notes) {
			this.notes = Optional.fromNullable(notes);
			return this;
		}
		
		public MapChangeBuilder setStage(int stage) {
			this.stage = Optional.of(stage);
			return this;
		}
		
		public MapChangeBuilder setNeedsInvestigation(boolean needsInvestigation) {
			this.needsInvestigation = needsInvestigation;
			return this;
		}
		
		public MapChangeBuilder setInProgress(boolean inProgress) {
			this.inProgress = inProgress;
			return this;
		}
		
		public MapChange build() {
			return new MapChange(this);
		}	
	}	
}

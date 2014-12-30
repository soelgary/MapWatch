package com.gsoeller.personalization.maps.data;

public class TileUpdate {

	private Tile oldTile;
	private Tile newTile;
	private String mapProvider;
	private Region country;
	private int date;
	private boolean read;
	
	private TileUpdate(int oldFetchJob, String oldHash, String oldPath, int newFetchJob, String newHash, String newPath, String mapProvider, Region country, int date) {
		this.oldTile = new Tile(oldFetchJob, oldHash, oldPath);
		this.newTile = new Tile(newFetchJob, newHash, newPath);
		this.mapProvider = mapProvider;
		this.country = country;
		this.date = date;
		this.read = false;
	}
	
	public boolean getRead() {
		return read;
	}

	public Tile getOldTile() {
		return oldTile;
	}
	
	public Tile getNewTile() {
		return newTile;
	}

	public String getMapProvider() {
		return mapProvider;
	}

	public Region getCountry() {
		return country;
	}

	public int getDate() {
		return date;
	}

	public static class Tile {
		private int fetchJob;
		private String hash;
		private String path;
		
		private Tile(int fetchJob, String hash, String path) {
			this.fetchJob = fetchJob;
			this.hash = hash;
			this.path = path;
		}
		
		public int getFetchJob() {
			return fetchJob;
		}
		
		public String getHash() {
			return hash;
		}
		
		public String getPath() {
			return path;
		}
	}
}
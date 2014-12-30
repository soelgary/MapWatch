package com.gsoeller.personalization.maps.data;

public class HealthUpdate {
  private MapProvider mapProvider;
  private Region country;
  private TileUpdate oldTile;
  private TileUpdate newTile;

  private HealthUpdate(MapProvider mapProvider, Region country, TileUpdate oldTile, TileUpdate newTile) {
    this.mapProvider = mapProvider;
    this.country = country;
    this.oldTile = oldTile;
    this.newTile = newTile;
  }

}

/*
  {
    "mapProvider": "bing",
    "country": "in",
    "old": {
      "fetchJob": 1,
      "hash": "vnavbeahlvbhvbhalw",
      "path": "olvwqvblwbd.png"
    },

    "new": {
      "fetchJob": 2,
      "hash": "vwnailvbwbaiuvhewuvblbv",
      "path": "neddddddw.png"
    }
  }

*/
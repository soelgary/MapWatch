package com.gsoeller.personalization.maps.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.joda.time.DateTime;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.gsoeller.personalization.maps.data.GoogleMap;
import com.gsoeller.personalization.maps.data.MapChange;
import com.gsoeller.personalization.maps.data.MapProvider;

public class GoogleMapUpdateMapper implements ResultSetMapper<MapChange> {

	public MapChange map(int index, ResultSet r, StatementContext ctx)
			throws SQLException {
		int id = r.getInt("id");
		String notes = r.getString("notes");
		int stage = r.getInt("stage");
		boolean needsInvestigation = r.getBoolean("needsInvestigation");
		DateTime lastUpdated = new DateTime(r.getTimestamp("lastUpdated"));
		boolean inProgress = r.getBoolean("inProgress");
		GoogleMap oldMap = new GoogleMap.MapBuilder().setId(r.getInt("oldMap")).build();
		GoogleMap newMap = new GoogleMap.MapBuilder().setId(r.getInt("newMap")).build();
		return new MapChange.MapChangeBuilder(oldMap, newMap, MapProvider.google)
			.setId(id)
			.setInProgress(inProgress)
			.setNeedsInvestigation(needsInvestigation)
			.setNotes(notes)
			.setStage(stage)
			.setLastUpdate(lastUpdated)
			.build();		
	}
}

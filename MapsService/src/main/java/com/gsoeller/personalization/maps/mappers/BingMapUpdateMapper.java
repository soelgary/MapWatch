package com.gsoeller.personalization.maps.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.joda.time.DateTime;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.gsoeller.personalization.maps.data.BingMap;
import com.gsoeller.personalization.maps.data.MapChange;
import com.gsoeller.personalization.maps.data.MapProvider;

public class BingMapUpdateMapper implements ResultSetMapper<MapChange> {

	public MapChange map(int index, ResultSet r, StatementContext ctx)
			throws SQLException {
		int id = r.getInt("id");
		String notes = r.getString("notes");
		int stage = r.getInt("stage");
		boolean needsInvestigation = r.getBoolean("needsInvestigation");
		DateTime lastUpdated = new DateTime(r.getTimestamp("lastUpdated"));
		boolean inProgress = r.getBoolean("inProgress");
		BingMap oldMap = new BingMap.MapBuilder().setId(r.getInt("oldMap")).build();
		BingMap newMap = new BingMap.MapBuilder().setId(r.getInt("newMap")).build();
		return new MapChange.MapChangeBuilder(oldMap, newMap, MapProvider.bing)
			.setId(id)
			.setInProgress(inProgress)
			.setNeedsInvestigation(needsInvestigation)
			.setNotes(notes)
			.setStage(stage)
			.setLastUpdate(lastUpdated)
			.build();		
	}
}
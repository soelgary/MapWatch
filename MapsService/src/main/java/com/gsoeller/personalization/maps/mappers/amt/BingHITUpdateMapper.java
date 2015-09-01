package com.gsoeller.personalization.maps.mappers.amt;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.gsoeller.personalization.maps.data.BingMap;
import com.gsoeller.personalization.maps.data.amt.BingHITUpdate;

public class BingHITUpdateMapper implements ResultSetMapper<BingHITUpdate> {
	
	public BingHITUpdate map(int index, ResultSet r, StatementContext ctx)
			throws SQLException {
		int id = r.getInt("id");
		boolean hasBorderChanged = r.getBoolean("hasBorderChange");
		String notes = r.getString("notes");
		boolean finished = r.getBoolean("finished");
		BingMap oldMap = new BingMap.MapBuilder().setId(r.getInt("oldMap")).build();
		BingMap newMap = new BingMap.MapBuilder().setId(r.getInt("newMap")).build();
		
		return new BingHITUpdate.BingHITUpdateBuilder()
			.setId(id)
			.setHasBorderChange(hasBorderChanged)
			.setNotes(notes)
			.setFinished(finished)
			.setNewMap(newMap)
			.setOldMap(oldMap)
			.build();
	}
}
package com.gsoeller.personalization.maps.mappers.amt;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.gsoeller.personalization.maps.data.GoogleMap;
import com.gsoeller.personalization.maps.data.amt.GoogleHITUpdate;

public class GoogleHITUpdateMapper implements ResultSetMapper<GoogleHITUpdate> {

	public GoogleHITUpdate map(int index, ResultSet r, StatementContext ctx)
			throws SQLException {
		int id = r.getInt("id");
		GoogleMap oldMap = new GoogleMap.MapBuilder().setId(r.getInt("oldMap")).build();
		GoogleMap newMap = new GoogleMap.MapBuilder().setId(r.getInt("newMap")).build();
		boolean hasBorderChanged = r.getBoolean("hasBorderChange");
		String notes = r.getString("notes");
		boolean finished = r.getBoolean("finished");
		return new GoogleHITUpdate.GoogleHITUpdateBuilder()
			.setId(id)
			.setOldMap(oldMap)
			.setNewMap(newMap)
			.setHasBorderChange(hasBorderChanged)
			.setNotes(notes)
			.setFinished(finished)
			.build();
	}
}

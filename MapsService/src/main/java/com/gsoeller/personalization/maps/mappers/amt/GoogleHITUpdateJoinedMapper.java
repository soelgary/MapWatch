package com.gsoeller.personalization.maps.mappers.amt;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.gsoeller.personalization.maps.data.GoogleMap;
import com.gsoeller.personalization.maps.data.amt.GoogleHITUpdate;

public class GoogleHITUpdateJoinedMapper implements ResultSetMapper<GoogleHITUpdate> {

	@Override
	public GoogleHITUpdate map(int index, ResultSet r, StatementContext ctx)
			throws SQLException {
		GoogleMap oldMap = new GoogleMap.MapBuilder().setId(r.getInt("old.id")).build();
		GoogleMap newMap = new GoogleMap.MapBuilder().setId(r.getInt("new.id")).build();
		return new GoogleHITUpdate.GoogleHITUpdateBuilder()
			.setNewMap(newMap)
			.setOldMap(oldMap)
			.build();
	}
}

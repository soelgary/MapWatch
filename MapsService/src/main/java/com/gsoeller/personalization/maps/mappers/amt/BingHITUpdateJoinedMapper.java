package com.gsoeller.personalization.maps.mappers.amt;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.gsoeller.personalization.maps.data.BingMap;
import com.gsoeller.personalization.maps.data.amt.BingHITUpdate;

public class BingHITUpdateJoinedMapper implements ResultSetMapper<BingHITUpdate> {

	@Override
	public BingHITUpdate map(int index, ResultSet r, StatementContext ctx)
			throws SQLException {
		BingMap oldMap = new BingMap.MapBuilder().setId(r.getInt("old.id")).build();
		BingMap newMap = new BingMap.MapBuilder().setId(r.getInt("new.id")).build();
		return new BingHITUpdate.BingHITUpdateBuilder()
			.setNewMap(newMap)
			.setOldMap(oldMap)
			.build();
	}
}

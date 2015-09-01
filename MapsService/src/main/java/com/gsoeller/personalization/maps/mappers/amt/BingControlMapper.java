package com.gsoeller.personalization.maps.mappers.amt;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.gsoeller.personalization.maps.data.BingMap;
import com.gsoeller.personalization.maps.data.amt.BingControlUpdate;

public class BingControlMapper implements ResultSetMapper<BingControlUpdate> {

	public BingControlUpdate map(int index, ResultSet r, StatementContext ctx)
			throws SQLException {
		int id = r.getInt("id");
		boolean hasBorderDifference = r.getBoolean("hasBorderDifference");
		BingMap oldMap = new BingMap.MapBuilder().setId(r.getInt("oldMap")).build();
		BingMap newMap = new BingMap.MapBuilder().setId(r.getInt("newMap")).build();
		return new BingControlUpdate.BingControlUpdateBuilder()
			.setId(id)
			.setHasBorderDifference(hasBorderDifference)
			.setNewMap(newMap)
			.setOldMap(oldMap)
			.build();
	}
}

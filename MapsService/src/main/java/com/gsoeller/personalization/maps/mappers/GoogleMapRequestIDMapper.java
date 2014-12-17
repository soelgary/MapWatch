package com.gsoeller.personalization.maps.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class GoogleMapRequestIDMapper implements ResultSetMapper<Integer> {

	public Integer map(int index, ResultSet r, StatementContext ctx)
			throws SQLException {
		return r.getInt("id");
	}
}

package com.gsoeller.personalization.maps.mappers.amt;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.gsoeller.personalization.maps.data.amt.GoogleControlUpdate;

public class GoogleControlMapper implements ResultSetMapper<GoogleControlUpdate> {

	public GoogleControlUpdate map(int index, ResultSet r, StatementContext ctx)
			throws SQLException {
		int id = r.getInt("id");
		return new GoogleControlUpdate.GoogleControlUpdateBuilder()
			.setId(id)
			.build();
	}
}

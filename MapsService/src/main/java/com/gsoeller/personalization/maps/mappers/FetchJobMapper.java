package com.gsoeller.personalization.maps.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.joda.time.DateTime;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.gsoeller.personalization.maps.data.FetchJob;

public class FetchJobMapper implements ResultSetMapper<FetchJob>{

	public FetchJob map(int index, ResultSet r, StatementContext ctx)
			throws SQLException {
		return new FetchJob.FetchJobBuilder()
			.setFinished(r.getBoolean("finished"))
			.setId(r.getInt("id"))
			.setStartTime(new DateTime(r.getTimestamp("startTime")))
			.build();
	}
}

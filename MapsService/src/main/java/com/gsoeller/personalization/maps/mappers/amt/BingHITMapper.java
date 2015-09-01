package com.gsoeller.personalization.maps.mappers.amt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.joda.time.DateTime;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.google.common.base.Optional;

import com.gsoeller.personalization.maps.data.amt.BingControlUpdate;
import com.gsoeller.personalization.maps.data.amt.BingHIT;

public class BingHITMapper implements ResultSetMapper<BingHIT> {

	public BingHIT map(int index, ResultSet r, StatementContext ctx)
			throws SQLException {
		int id = r.getInt("id");
		BingControlUpdate control = new BingControlUpdate.BingControlUpdateBuilder().setId(r.getInt("control")).build();
		boolean approved = r.getBoolean("approved");
		boolean readyForApproval = r.getBoolean("readyForApproval");
		BingHIT.BingHITBuilder builder = new BingHIT.BingHITBuilder();
		String hitId = r.getString("hitId");
		boolean controlResponse = r.getBoolean("controlResponse");
		boolean finished = r.getBoolean("finished");
		Optional<Timestamp> created = Optional.fromNullable(r.getTimestamp("created"));
		if(created.isPresent()) {
			builder.setCreated(new DateTime(created.get()));
		}
		return builder.setId(id)
			.setTurkId(r.getInt("turkId"))
			.setApproved(approved)
			.setReadyForApproval(readyForApproval)
			.setHitId(hitId)
			.setControlResponse(controlResponse)
			.setFinished(finished)
			.setControl(control)
			.build();
	}
}

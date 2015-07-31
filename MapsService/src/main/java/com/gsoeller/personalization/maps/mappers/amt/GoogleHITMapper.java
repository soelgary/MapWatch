package com.gsoeller.personalization.maps.mappers.amt;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.joda.time.DateTime;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.google.common.base.Optional;
import com.gsoeller.personalization.maps.dao.amt.GoogleAMTControlDao;
import com.gsoeller.personalization.maps.dao.amt.GoogleHITUpdateDao;
import com.gsoeller.personalization.maps.data.amt.GoogleControlUpdate;
import com.gsoeller.personalization.maps.data.amt.GoogleHIT;
import com.gsoeller.personalization.maps.data.amt.GoogleHITUpdate;

public class GoogleHITMapper implements ResultSetMapper<GoogleHIT> {

	
	private GoogleHITUpdateDao updateDao;
	private GoogleAMTControlDao controlDao;
	
	public GoogleHITMapper() throws IOException {
		updateDao = new GoogleHITUpdateDao();
		controlDao = new GoogleAMTControlDao();
	}
	
	public GoogleHIT map(int index, ResultSet r, StatementContext ctx)
			throws SQLException {
		int id = r.getInt("id");
		Optional<GoogleControlUpdate> control = getControl(r.getInt("control"));
		List<GoogleHITUpdate> updates = getUpdates(id);
		boolean approved = r.getBoolean("approved");
		boolean readyForApproval = r.getBoolean("readyForApproval");
		GoogleHIT.GoogleHITBuilder builder = new GoogleHIT.GoogleHITBuilder();
		String hitId = r.getString("hitId");
		boolean controlResponse = r.getBoolean("controlResponse");
		boolean finished = r.getBoolean("finished");
		Optional<Timestamp> created = Optional.fromNullable(r.getTimestamp("created"));
		if(control.isPresent()) {
			builder.setControl(control.get());
		}
		if(created.isPresent()) {
			builder.setCreated(new DateTime(created.get()));
		}
		return builder.setId(id)
			.setTurkId(r.getInt("turkId"))
			.setId(updates)
			.setApproved(approved)
			.setReadyForApproval(readyForApproval)
			.setHitId(hitId)
			.setControlResponse(controlResponse)
			.setFinished(finished)
			.build();
	}
	
	private Optional<GoogleControlUpdate> getControl(int controlId) {
		return controlDao.getControl(controlId);
	}
	
	private List<GoogleHITUpdate> getUpdates(int hitId) {
		return updateDao.getUpdates(hitId);
	}

}

package com.gsoeller.personalization.maps.mappers.amt;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
		GoogleHIT.GoogleHITBuilder builder = new GoogleHIT.GoogleHITBuilder();
		if(control.isPresent()) {
			builder.setControl(control.get());
		}
		return builder.setId(id)
			.setTurkId(r.getInt("turkId"))
			.setId(updates)
			.setApproved(approved)
			.build();
	}
	
	private Optional<GoogleControlUpdate> getControl(int controlId) {
		return controlDao.getControl(controlId);
	}
	
	private List<GoogleHITUpdate> getUpdates(int hitId) {
		return updateDao.getUpdates(hitId);
	}

}

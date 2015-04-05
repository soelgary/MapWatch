package com.gsoeller.personalization.maps.mappers.amt;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.gsoeller.personalization.maps.dao.amt.GoogleHITUpdateDao;
import com.gsoeller.personalization.maps.data.amt.GoogleControlUpdate;
import com.gsoeller.personalization.maps.data.amt.GoogleHIT;
import com.gsoeller.personalization.maps.data.amt.GoogleHITUpdate;

public class GoogleHITMapper implements ResultSetMapper<GoogleHIT> {

	
	private GoogleHITUpdateDao updateDao;
	
	public GoogleHITMapper() throws IOException {
		updateDao = new GoogleHITUpdateDao();
	}
	
	public GoogleHIT map(int index, ResultSet r, StatementContext ctx)
			throws SQLException {
		int id = r.getInt("id");
		GoogleControlUpdate control = getControl(r.getInt("control"));
		List<GoogleHITUpdate> updates = getUpdates(id);
		boolean approved = r.getBoolean("approved");
		return new GoogleHIT.GoogleHITBuilder()
			.setId(id)
			.setTurkId(r.getInt("turkId"))
			.setControl(control)
			.setId(updates)
			.setApproved(approved)
			.build();
	}
	
	private GoogleControlUpdate getControl(int controlId) {
		return new GoogleControlUpdate.GoogleControlUpdateBuilder()
			.setId(controlId)
			.build();
	}
	
	private List<GoogleHITUpdate> getUpdates(int hitId) {
		return updateDao.getUpdates(hitId);
	}

}

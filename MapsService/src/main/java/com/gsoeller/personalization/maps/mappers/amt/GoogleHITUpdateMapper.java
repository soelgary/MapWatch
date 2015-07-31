package com.gsoeller.personalization.maps.mappers.amt;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.google.common.base.Optional;
import com.gsoeller.personalization.maps.dao.GoogleMapDao;
import com.gsoeller.personalization.maps.data.GoogleMap;
import com.gsoeller.personalization.maps.data.Map;
import com.gsoeller.personalization.maps.data.amt.GoogleHITUpdate;

public class GoogleHITUpdateMapper implements ResultSetMapper<GoogleHITUpdate> {

	private GoogleMapDao mapDao;
	
	public GoogleHITUpdateMapper() throws IOException {
		mapDao = new GoogleMapDao();
	}
	
	public GoogleHITUpdate map(int index, ResultSet r, StatementContext ctx)
			throws SQLException {
		int id = r.getInt("id");
		Optional<Map> oldMap = mapDao.getMap(r.getInt("oldMap"));
		Optional<Map> newMap = mapDao.getMap(r.getInt("newMap"));
		boolean hasBorderChanged = r.getBoolean("hasBorderChange");
		String notes = r.getString("notes");
		boolean finished = r.getBoolean("finished");
		
		GoogleHITUpdate.GoogleHITUpdateBuilder builder = new GoogleHITUpdate.GoogleHITUpdateBuilder();
		if(oldMap.isPresent()) {
			builder.setOldMap((GoogleMap)oldMap.get());
		}
		if(newMap.isPresent()) {
			builder.setNewMap((GoogleMap)newMap.get());
		}
		
		builder.setId(id)
			.setHasBorderChange(hasBorderChanged)
			.setNotes(notes)
			.setFinished(finished);
		
		return builder.build();
	}
}

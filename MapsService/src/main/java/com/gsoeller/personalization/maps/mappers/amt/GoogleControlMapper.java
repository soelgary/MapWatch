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
import com.gsoeller.personalization.maps.data.amt.GoogleControlUpdate;

public class GoogleControlMapper implements ResultSetMapper<GoogleControlUpdate> {

	private GoogleMapDao mapDao;
	
	public GoogleControlMapper() throws IOException {
		mapDao = new GoogleMapDao();
	}
	
	public GoogleControlUpdate map(int index, ResultSet r, StatementContext ctx)
			throws SQLException {
		int id = r.getInt("id");
		boolean hasBorderDifference = r.getBoolean("hasBorderDifference");
		Optional<Map> oldMap = mapDao.getMap(r.getInt("oldMap"));
		Optional<Map> newMap = mapDao.getMap(r.getInt("newMap"));
		GoogleControlUpdate.GoogleControlUpdateBuilder builder = new GoogleControlUpdate.GoogleControlUpdateBuilder()
			.setId(id)
			.setHasBorderDifference(hasBorderDifference);
		if(oldMap.isPresent()) {
			builder.setOldMap((GoogleMap)oldMap.get());
		}
		if(newMap.isPresent()) {
			builder.setNewMap((GoogleMap)newMap.get());
		}
		return builder.build();
	}
}

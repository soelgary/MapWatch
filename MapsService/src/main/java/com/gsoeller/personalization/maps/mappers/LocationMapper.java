package com.gsoeller.personalization.maps.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.gsoeller.personalization.maps.data.Location;

public class LocationMapper implements ResultSetMapper<Location> {

	public Location map(int arg0, ResultSet r, StatementContext arg2)
			throws SQLException {
		return new Location.LocationBuilder()
			.setId(r.getInt("id"))
			.setLatitude(r.getDouble("latitude"))
			.setLongitude(r.getDouble("longitude"))
			.build();
	}

}

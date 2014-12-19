package com.gsoeller.personalization.maps.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.gsoeller.personalization.maps.data.BingMapRequest;
import com.gsoeller.personalization.maps.data.BingQuadKey;
import com.gsoeller.personalization.maps.data.Region;


public class BingMapRequestMapper implements ResultSetMapper<BingMapRequest>{

	public BingMapRequest map(int index, ResultSet r, StatementContext ctx)
			throws SQLException {
		return new BingMapRequest.BingMapRequestBuilder()
			.setId(r.getInt("id"))
			.setMapNumber(r.getInt("mapNumber"))
			.setRegion(Region.findRegion(r.getString("region")))
			.setTileNumber(new BingQuadKey(r.getString("tileNumber")))
			.build();
	}
}

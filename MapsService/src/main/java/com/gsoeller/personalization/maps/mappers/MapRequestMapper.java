package com.gsoeller.personalization.maps.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.gsoeller.personalization.maps.data.Language;
import com.gsoeller.personalization.maps.data.MapRequest;
import com.gsoeller.personalization.maps.data.Region;

public class MapRequestMapper implements ResultSetMapper<MapRequest>{

	public MapRequest map(int arg0, ResultSet r, StatementContext arg2) throws SQLException {
		Language language;
		String savedLanguage = r.getString("language");
		if(savedLanguage.equals("English")) {
			language = Language.English;
		} else {
			throw new RuntimeException("Invalid language was saved, " + savedLanguage);
		}
		Region region = Region.findRegion(r.getString("region"));

		
		MapRequest request = new MapRequest.MapRequestBuilder()
				.setXDimension(r.getInt("xDimension"))
				.setYDimension(r.getInt("yDimension"))
				.setRegion(region)
				.setLanguage(language)
				.setZoom(r.getInt("zoom"))
				.setId(r.getInt("id"))
				.setLatitude(r.getDouble("latitude"))
				.setLongitude(r.getDouble("longitude"))
				.setMapNumber(r.getInt("MapNumber"))
				.build();
		return request;
	}

}

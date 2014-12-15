package com.gsoeller.personalization.maps.dao;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import com.gsoeller.personalization.maps.data.Location;
import com.gsoeller.personalization.maps.mappers.LocationMapper;

public interface LocationDao {

	@SqlQuery("Select * from Location")
	@Mapper(LocationMapper.class)
	public List<Location> getLocations();
	
	@SqlUpdate("Insert into Location (latitude, longitude, MapNumber) values (:latitude, :longitude, :mapNumber)")
	@GetGeneratedKeys
	public int addLocation(@Bind("latitude") double latitude, @Bind("longitude") double longitude, @Bind("mapNumber") int mapNumber);
	
	@SqlQuery("Select * from Location where id = :id")
	@Mapper(LocationMapper.class)
	public List<Location> getLocation(@Bind("id") int id);
	
}

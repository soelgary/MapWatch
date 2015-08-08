package com.gsoeller.personalization.maps.auth.dao;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import com.gsoeller.personalization.maps.auth.User;
import com.gsoeller.personalization.maps.mappers.UserMapper;

public interface UserDao {
	
	@SqlUpdate("Insert into User (username, password, role, active) values (:username, :password, :role, false)")
	@GetGeneratedKeys
	public int createUser(@Bind("username") String username, @Bind("password") String password, @Bind("role") String role);
	
	@SqlQuery("Select * from User where username = :username")
	@Mapper(UserMapper.class)
	public List<User> getUser(@Bind("username") String username);
	
	@SqlQuery("Select * from User where id = :id")
	@Mapper(UserMapper.class)
	public List<User> getUser(@Bind("id") int id);
}
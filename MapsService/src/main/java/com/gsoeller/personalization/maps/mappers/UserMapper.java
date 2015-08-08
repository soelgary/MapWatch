package com.gsoeller.personalization.maps.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.gsoeller.personalization.maps.auth.Role;
import com.gsoeller.personalization.maps.auth.User;

public class UserMapper implements ResultSetMapper<User>{

	@Override
	public User map(int index, ResultSet r, StatementContext ctx)
			throws SQLException {
		int id = r.getInt("id");
		String username = r.getString("username");
		String password = r.getString("password");
		Role role = Role.valueOf(r.getString("role"));
		boolean active = r.getBoolean("active");
		return new User.UserBuilder()
			.setId(id)
			.setUsername(username)
			.setPassword(password)
			.setRole(role)
			.setActive(active)
			.build();
	}
}

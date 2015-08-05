package com.gsoeller.personalization.maps.auth.dao;

import java.util.List;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.gsoeller.personalization.maps.auth.Token;
import com.gsoeller.personalization.maps.auth.User;

public class UserDao {

	private List<User> users = Lists.newArrayList();
	
	public Optional<User> createUser(User newUser) {
		for(User user: users) {
			if(user.getUsername().equals(newUser.getUsername())) {
				return Optional.absent();
			}
		}
		users.add(newUser);
		return Optional.of(newUser);
	}
	
	public Optional<User> getUser(String username) {
		for(User user: users) {
			if(user.getUsername().equals(username)) {
				return Optional.of(user);
			}
		}
		return Optional.absent();
	}
	
	public Optional<User> getUser(Token token) {
		for(User user: users) {
			if(user.getUsername().equals(token.getUsername())) {
				return Optional.of(user);
			}
		}
		return Optional.absent();
	}
	
	public List<User> getUsers() {
		return users;
	}
}

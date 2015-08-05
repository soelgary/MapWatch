package com.gsoeller.personalization.maps.auth.managers;

import java.util.List;

import com.google.common.base.Optional;
import com.gsoeller.personalization.maps.auth.Role;
import com.gsoeller.personalization.maps.auth.Token;
import com.gsoeller.personalization.maps.auth.User;
import com.gsoeller.personalization.maps.auth.dao.TokenDao;
import com.gsoeller.personalization.maps.auth.dao.UserDao;

public class AuthManager {

	private TokenDao tokenDao;
	private UserDao userDao;
	
	public AuthManager(TokenDao tokenDao, UserDao userDao) {
		this.tokenDao = tokenDao;
		this.userDao = userDao;
	}
	
	public Optional<User> createUser(User user) {
		return userDao.createUser(user);
	}
	
	public List<User> getUsers() {
		return userDao.getUsers();
	}
	
	public Token generateToken(User user) {
		return tokenDao.createToken(user);
	}
	
	public Optional<User> getUser(String tokenValue) {
		Optional<Token> token = tokenDao.getToken(tokenValue);
		if(!token.isPresent()) {
			return Optional.absent();
		}
		return userDao.getUser(token.get());
	}
	
	/*
	 * Checks whether the user is authorized.
	 * If the user is admin, user is authorized,
	 * Otherwise user is authorized if the role is the same
	 * 
	 */
	public boolean isAuthorized(Optional<User> user, Role role) {
		if(user.isPresent()) {
			if(user.get().getRole() == Role.ADMIN
			|| user.get().getRole() == role) {
				return true;
			}
		}
		return false;
	}
}

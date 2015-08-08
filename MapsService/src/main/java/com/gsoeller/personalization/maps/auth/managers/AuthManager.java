package com.gsoeller.personalization.maps.auth.managers;

import java.util.List;

import org.mindrot.BCrypt;

import com.google.common.base.Optional;
import com.gsoeller.personalization.maps.auth.Role;
import com.gsoeller.personalization.maps.auth.Token;
import com.gsoeller.personalization.maps.auth.User;
import com.gsoeller.personalization.maps.auth.dao.TokenDao;
import com.gsoeller.personalization.maps.auth.dao.UserDao;

public class AuthManager {

	private TokenDao tokenDao;
	private UserDao userDao;

	private static final int LOG_ROUNDS = 12;
	
	public AuthManager(TokenDao tokenDao, UserDao userDao) {
		this.tokenDao = tokenDao;
		this.userDao = userDao;
	}
	
	public Optional<User> createUser(User user) {
		if(userExists(user.getUsername())) {
			return Optional.absent();
		}
		String salt = generateSalt();
		String hash = calculateHash(user.getPassword(), salt);
		user.setPassword(hash);
		user.setActive(false);
		Role role;
		if(user.getRole() != null) {
			role = user.getRole();
		} else {
			role = Role.RESEARCHER;
		}
		int created = userDao.createUser(user.getUsername(), hash, role.toString());
		List<User> createdUser = userDao.getUser(created);
		if(createdUser.size() == 1) {
			return Optional.of(createdUser.get(0));
		}
		
		return Optional.absent();
	}
	
	public Token generateToken(User user) {
		return tokenDao.createToken(user);
	}
	
	public Optional<User> getUser(String tokenValue) {
		Optional<Token> token = tokenDao.getToken(tokenValue);
		if(!token.isPresent()) {
			return Optional.absent();
		}
		List<User> users = userDao.getUser(token.get().getUsername());
		if(users.size() == 1) {
			return Optional.of(users.get(0));
		}
		return Optional.absent();
	}
	
	public boolean userExists(String username) {
		List<User> users = userDao.getUser(username);
		if(users.size() > 0) {
			return true;
		}
		return false;
	}
	
	public Optional<Token> getToken(String tokenValue) {
		return tokenDao.getToken(tokenValue);
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
	
	public String calculateHash(String password, String salt) {
		return BCrypt.hashpw(password, salt);
	}
	
	public boolean passwordMatches(String plaintext, String hashed) {
		System.out.println("checking password");
		System.out.println(BCrypt.checkpw(plaintext, hashed));
		return BCrypt.checkpw(plaintext, hashed);
	}
	
	public String generateSalt() {
		return BCrypt.gensalt(LOG_ROUNDS);
	}
}

package com.gsoeller.personalization.maps.auth;

import java.util.List;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import com.google.common.base.Optional;
import com.gsoeller.personalization.maps.auth.dao.UserDao;
import com.gsoeller.personalization.maps.auth.managers.AuthManager;

public class UserAuthenticator implements Authenticator<BasicCredentials, User> {
   
	private final UserDao userDao;
	private final AuthManager authManager;
	
	public UserAuthenticator(final UserDao userDao, final AuthManager authManager) {
		this.userDao = userDao;
		this.authManager = authManager;
	}
	
	@Override
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {
		List<User> users = userDao.getUser(credentials.getUsername());
		if(users.size() == 1) {
			User user = users.get(0);
			String saltedPlaintext = credentials.getPassword();
			if(authManager.passwordMatches(saltedPlaintext, user.getPassword())
					&& user.isActive()) {
				return Optional.of(user);
			}
		}
		System.out.println("Not authorized");
        return Optional.absent();
    }
}

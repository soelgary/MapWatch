package com.gsoeller.personalization.maps.auth;

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
		Optional<User> user = userDao.getUser(credentials.getUsername());
		if(user.isPresent()) {
			String saltedPlaintext = credentials.getPassword(); //user.get().getSalt() + credentials.getPassword();
			if(authManager.passwordMatches(saltedPlaintext, user.get().getPassword())) {
				return user;
			}
		}
		System.out.println("Not authorized");
        return Optional.absent();
    }
}

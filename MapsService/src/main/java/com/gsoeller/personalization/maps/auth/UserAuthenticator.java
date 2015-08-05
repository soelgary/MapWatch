package com.gsoeller.personalization.maps.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import com.google.common.base.Optional;
import com.gsoeller.personalization.maps.auth.dao.UserDao;

public class UserAuthenticator implements Authenticator<BasicCredentials, User> {
   
	private final UserDao userDao;
	
	public UserAuthenticator(final UserDao userDao) {
		this.userDao = userDao;
	}
	
	@Override
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {
		Optional<User> user = userDao.getUser(credentials.getUsername());
		if(user.isPresent()) {
			if(user.get().getPassword().equals(credentials.getPassword())) {
				return user;
			}
		}
        return Optional.absent();
    }
}

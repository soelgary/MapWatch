package com.gsoeller.personalization.maps.auth;

import io.dropwizard.auth.Auth;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicAuthProvider;
import io.dropwizard.auth.basic.BasicCredentials;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.model.Parameter;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;

public class PersonalizationAuthProvider<T> implements InjectableProvider<Auth, Parameter> {

	private final BasicAuthProvider<User> basicAuthProvider;

	public PersonalizationAuthProvider(Authenticator<BasicCredentials, User> authenticator, String realm) {
		basicAuthProvider = new BasicAuthProvider<User>(authenticator, realm);
	}

	@Override
	public ComponentScope getScope() {
		return basicAuthProvider.getScope();
	}

	@Override
	public Injectable<?> getInjectable(ComponentContext ic, Auth a, Parameter c) {
		@SuppressWarnings("unchecked")
		AbstractHttpContextInjectable<User> injectable = (AbstractHttpContextInjectable<User>) basicAuthProvider.getInjectable(ic, a, c);
		return new AuthInjectable<User>(injectable);
	}

	private static class AuthInjectable<T> extends AbstractHttpContextInjectable<T> {
		
		private final AbstractHttpContextInjectable<T> injectable;
		
		private AuthInjectable(AbstractHttpContextInjectable<T> injectable) {
			this.injectable = injectable;
		}

		@Override
		public T getValue(HttpContext c) {
			try {
				return injectable.getValue(c);
			} catch(WebApplicationException e) {
				if(e.getResponse().getStatus() == Response.Status.UNAUTHORIZED.getStatusCode()) {
					throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
                            .entity("Credentials are required to access this resource")
                            .type(MediaType.TEXT_PLAIN_TYPE)
                            .build());
				}
				throw e;
			}
		}
	}
}
package com.gsoeller.personalization.maps.resources;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import io.dropwizard.auth.Auth;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import com.google.common.base.Optional;
import com.gsoeller.personalization.maps.auth.Role;
import com.gsoeller.personalization.maps.auth.Token;
import com.gsoeller.personalization.maps.auth.User;
import com.gsoeller.personalization.maps.auth.managers.AuthManager;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticationResource {

	private AuthManager manager;
	
	public AuthenticationResource(AuthManager manager) {
		this.manager = manager;
	}
	
	@POST
	@Path("/user")
	public User createUser(User user) {
		Optional<User> created = manager.createUser(user);
		if(created.isPresent()) {
			return user;
		}
		throw new WebApplicationException("Invalid credentials. Try a different username/password combo", Response.Status.CONFLICT);
	}
	
    @POST
    @Path("/generate-token")
    public Response get(@Auth User user) {
    	Token token = manager.generateToken(user);
    	return Response.ok().cookie(new NewCookie("token", token.getValue()))
    			.entity(token)
    			.build();
    }
    
    @GET
    @Path("/user")
    @auth(allowed=Role.RESEARCHER_ROLE)
    public List<User> getUsers(@CookieParam(value="token") String tokenValue) {
    	Optional<User> user = manager.getUser(tokenValue);
    	if(manager.isAuthorized(user, Role.ADMIN)) {
    		return manager.getUsers();
    	}
    	throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface auth {
    	public String allowed() default Role.RESEARCHER_ROLE;
    }
}
package com.gsoeller.personalization.maps.resources;

import io.dropwizard.auth.Auth;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.common.base.Optional;

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
	public Response createUser(User user) {
		Optional<User> created = manager.createUser(user);
		if(!created.isPresent()) {
			throw new WebApplicationException(Response.Status.CONFLICT);
		}
		return Response.status(Status.CREATED).build();
	}
	
    @GET
    @Path("/generate-token")
    public Response get(@Auth User user) {
    	Token token = manager.generateToken(user);
    	return Response.ok().cookie(new NewCookie("token", token.getValue()))
    			.entity(token)
    			.build();
    }
    
    @GET
    @Path("/token/{token}")
    public Optional<Token> getToken(@PathParam("token") String token) {
    	return manager.getToken(token);
    }
}
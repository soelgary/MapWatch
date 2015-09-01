package com.gsoeller.personalization.maps;

import io.dropwizard.setup.Environment;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import org.eclipse.jetty.servlets.CrossOriginFilter;

import com.gsoeller.personalization.maps.auth.PersonalizationAuthProvider;
import com.gsoeller.personalization.maps.auth.User;
import com.gsoeller.personalization.maps.auth.UserAuthenticator;
import com.gsoeller.personalization.maps.auth.managers.AuthManager;
import com.gsoeller.personalization.maps.managers.GoogleAMTControlManager;
import com.gsoeller.personalization.maps.managers.GoogleAMTManager;
import com.gsoeller.personalization.maps.managers.GoogleHITUpdateManager;
import com.gsoeller.personalization.maps.resources.AMTControlResource;
import com.gsoeller.personalization.maps.resources.AMTResource;
import com.gsoeller.personalization.maps.resources.AuthenticationResource;
import com.gsoeller.personalization.maps.resources.BingAMTResource;
import com.gsoeller.personalization.maps.resources.BingHITUpdateResource;
import com.gsoeller.personalization.maps.resources.GoogleHITUpdateResource;

public class WebApplication extends AbstractApplication {

	@Override
	public void run(MapsConfiguration configuration, Environment environment) throws Exception {
		super.initializeDaos(configuration, environment);
		
		//environment.jersey().register(AuthFactory.binder(new BasicAuthFactory<User>(new UserAuthenticator(userDao),
        //        "SUPER SECRET STUFF",
        //        User.class)));
		final AuthManager authManager = new AuthManager(tokenDao, userDao);
		environment.jersey().register(new PersonalizationAuthProvider<User>(new UserAuthenticator(userDao, authManager),
                "SUPER SECRET STUFF"));
		
		
		environment.jersey().register(new AuthenticationResource(authManager));
        
		FilterRegistration.Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
	    filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
	    filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
	    filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
	    filter.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
	    filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
	    
		final GoogleAMTManager googleAMTManager = new GoogleAMTManager(googleHitDao, updateDao, controlDao, googleMapDao);
	    final GoogleHITUpdateManager googleHITUpdateManager = new GoogleHITUpdateManager(updateDao, googleMapDao, googleMapRequestDao);
	    final GoogleAMTControlManager googleControlManager = new GoogleAMTControlManager(controlDao);
	    
	    environment.jersey().register(new AMTResource(googleAMTManager));
		environment.jersey().register(new AMTControlResource(googleControlManager));
		environment.jersey().register(new GoogleHITUpdateResource(googleAMTManager, googleHITUpdateManager, authManager));
		environment.jersey().register(new BingHITUpdateResource(bingAMTManager, bingHITUpdateManager, authManager));
		environment.jersey().register(new BingAMTResource(bingAMTManager));
	}
}
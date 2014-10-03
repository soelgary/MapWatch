package com.gsoeller.personalization.maps;

import org.skife.jdbi.v2.DBI;

import com.gsoeller.personalization.maps.resources.MapsResource;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class MapsApplication extends Application<MapsConfiguration> {

	public static void main(String[] args) throws Exception {
		new MapsApplication().run(args);
	}
	
	@Override
	public void initialize(Bootstrap<MapsConfiguration> bootstrap) {
		 bootstrap.addBundle(new MigrationsBundle<MapsConfiguration>() {
		            public DataSourceFactory getDataSourceFactory(MapsConfiguration configuration) {
		                return configuration.getDataSourceFactory();
		            }
		    });
	}

	@Override
	public void run(MapsConfiguration config, Environment environment)
			throws Exception {
		final DBIFactory factory = new DBIFactory();
	    final DBI jdbi = factory.build(environment, config.getDataSourceFactory(), "mysql");
	    environment.jersey().register(new MapsResource());
	}

}

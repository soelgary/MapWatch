package com.gsoeller.personalization.maps;

import org.skife.jdbi.v2.DBI;

import com.gsoeller.personalization.maps.auth.dao.TokenDao;
import com.gsoeller.personalization.maps.auth.dao.UserDao;
import com.gsoeller.personalization.maps.dao.GoogleFetchJobDao;
import com.gsoeller.personalization.maps.dao.GoogleMapDao;
import com.gsoeller.personalization.maps.dao.GoogleMapRequestDao;
import com.gsoeller.personalization.maps.dao.GoogleMapUpdateDao;
import com.gsoeller.personalization.maps.dao.amt.GoogleAMTControlDao;
import com.gsoeller.personalization.maps.dao.amt.GoogleHITDao;
import com.gsoeller.personalization.maps.dao.amt.GoogleHITUpdateDao;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public abstract class AbstractApplication extends Application<MapsConfiguration> {
	
	protected GoogleHITDao googleHitDao;
    protected GoogleHITUpdateDao updateDao;
    protected GoogleAMTControlDao controlDao;
    protected GoogleMapDao googleMapDao;
    protected GoogleMapRequestDao googleMapRequestDao;
    protected GoogleFetchJobDao googleFetchJobDao;
    protected GoogleMapUpdateDao googleMapUpdateDao;
    
    protected TokenDao tokenDao;
    protected UserDao userDao;
    
	@Override
	public void initialize(Bootstrap<MapsConfiguration> bootstrap) {
		bootstrap.addBundle(new MigrationsBundle<MapsConfiguration>() {
			public DataSourceFactory getDataSourceFactory(
					MapsConfiguration configuration) {
				return configuration.getDataSourceFactory();
			}
		});
	}
	
	public void initializeDaos(MapsConfiguration config, Environment environment) throws ClassNotFoundException {
		final DBIFactory factory = new DBIFactory();
	    final DBI jdbi = factory.build(environment, config.getDataSourceFactory(), "mysql");
	    googleHitDao = jdbi.onDemand(GoogleHITDao.class);
	    updateDao = jdbi.onDemand(GoogleHITUpdateDao.class);
	    controlDao = jdbi.onDemand(GoogleAMTControlDao.class);
	    googleMapDao = jdbi.onDemand(GoogleMapDao.class);
	    googleMapRequestDao = jdbi.onDemand(GoogleMapRequestDao.class);
	    googleFetchJobDao = jdbi.onDemand(GoogleFetchJobDao.class);
	    googleMapUpdateDao = jdbi.onDemand(GoogleMapUpdateDao.class);
	    userDao = jdbi.onDemand(UserDao.class);
	    tokenDao = new TokenDao();
	}
}

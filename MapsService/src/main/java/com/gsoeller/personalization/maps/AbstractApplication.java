package com.gsoeller.personalization.maps;

import java.io.IOException;

import org.skife.jdbi.v2.DBI;

import com.gsoeller.personalization.maps.auth.dao.TokenDao;
import com.gsoeller.personalization.maps.auth.dao.UserDao;
import com.gsoeller.personalization.maps.dao.BingFetchJobDao;
import com.gsoeller.personalization.maps.dao.BingMapDao;
import com.gsoeller.personalization.maps.dao.BingMapRequestDao;
import com.gsoeller.personalization.maps.dao.BingMapUpdateDao;
import com.gsoeller.personalization.maps.dao.GoogleFetchJobDao;
import com.gsoeller.personalization.maps.dao.GoogleMapDao;
import com.gsoeller.personalization.maps.dao.GoogleMapRequestDao;
import com.gsoeller.personalization.maps.dao.GoogleMapUpdateDao;
import com.gsoeller.personalization.maps.dao.amt.BingAMTControlDao;
import com.gsoeller.personalization.maps.dao.amt.BingHITDao;
import com.gsoeller.personalization.maps.dao.amt.BingHITUpdateDao;
import com.gsoeller.personalization.maps.dao.amt.GoogleAMTControlDao;
import com.gsoeller.personalization.maps.dao.amt.GoogleHITDao;
import com.gsoeller.personalization.maps.dao.amt.GoogleHITUpdateDao;
import com.gsoeller.personalization.maps.managers.BingAMTControlManager;
import com.gsoeller.personalization.maps.managers.BingAMTManager;
import com.gsoeller.personalization.maps.managers.BingHITUpdateManager;
import com.gsoeller.personalization.maps.managers.GoogleAMTControlManager;
import com.gsoeller.personalization.maps.managers.GoogleAMTManager;
import com.gsoeller.personalization.maps.managers.GoogleHITUpdateManager;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class AbstractApplication extends Application<MapsConfiguration> {
	
	protected GoogleHITDao googleHitDao;
    protected GoogleHITUpdateDao updateDao;
    protected GoogleAMTControlDao controlDao;
    protected GoogleMapDao googleMapDao;
    protected GoogleMapRequestDao googleMapRequestDao;
    protected GoogleFetchJobDao googleFetchJobDao;
    protected GoogleMapUpdateDao googleMapUpdateDao;
    
    protected BingHITDao bingHitDao;
    protected BingHITUpdateDao bingUpdateDao;
    protected BingAMTControlDao bingControlDao;
    protected BingMapDao bingMapDao;
    protected BingMapRequestDao bingMapRequestDao;
    protected BingFetchJobDao bingFetchJobDao;
    protected BingMapUpdateDao bingMapUpdateDao;
    
    protected TokenDao tokenDao;
    protected UserDao userDao;
    
    protected GoogleAMTManager googleAMTManager;
    protected GoogleHITUpdateManager googleHITUpdateManager;
    protected GoogleAMTControlManager googleControlManager;
    
    protected BingAMTManager bingAMTManager;
    protected BingHITUpdateManager bingHITUpdateManager;
    protected BingAMTControlManager bingControlManager;
    
	@Override
	public void initialize(Bootstrap<MapsConfiguration> bootstrap) {
		bootstrap.addBundle(new MigrationsBundle<MapsConfiguration>() {
			public DataSourceFactory getDataSourceFactory(
					MapsConfiguration configuration) {
				return configuration.getDataSourceFactory();
			}
		});
		bootstrap.addCommand(new UpdateCommand(new UpdateApplication()));
	}
	
	public void initializeDaos(MapsConfiguration config, Environment environment) throws ClassNotFoundException, IOException {
		final DBIFactory factory = new DBIFactory();
	    final DBI jdbi = factory.build(environment, config.getDataSourceFactory(), "mysql");
	    googleHitDao = jdbi.onDemand(GoogleHITDao.class);
	    updateDao = jdbi.onDemand(GoogleHITUpdateDao.class);
	    controlDao = jdbi.onDemand(GoogleAMTControlDao.class);
	    googleMapDao = jdbi.onDemand(GoogleMapDao.class);
	    googleMapRequestDao = jdbi.onDemand(GoogleMapRequestDao.class);
	    googleFetchJobDao = jdbi.onDemand(GoogleFetchJobDao.class);
	    googleMapUpdateDao = jdbi.onDemand(GoogleMapUpdateDao.class);
	    googleAMTManager = new GoogleAMTManager(googleHitDao, updateDao, controlDao, googleMapDao);
	    googleHITUpdateManager = new GoogleHITUpdateManager(updateDao, googleMapDao, googleMapRequestDao);
	    googleControlManager = new GoogleAMTControlManager(controlDao);
	    
	    bingHitDao = jdbi.onDemand(BingHITDao.class);
	    bingUpdateDao = jdbi.onDemand(BingHITUpdateDao.class);
	    bingControlDao = jdbi.onDemand(BingAMTControlDao.class);
	    bingMapDao = jdbi.onDemand(BingMapDao.class);
	    bingMapRequestDao = jdbi.onDemand(BingMapRequestDao.class);
	    bingFetchJobDao = jdbi.onDemand(BingFetchJobDao.class);
	    bingMapUpdateDao = jdbi.onDemand(BingMapUpdateDao.class);
	    bingAMTManager = new BingAMTManager(bingHitDao, bingUpdateDao, bingControlDao, bingMapDao);
	    bingHITUpdateManager = new BingHITUpdateManager(bingUpdateDao, bingMapDao, bingMapRequestDao);
	    bingControlManager = new BingAMTControlManager(bingControlDao);
	    
	    userDao = jdbi.onDemand(UserDao.class);
	    tokenDao = new TokenDao();
	}

	@Override
	public void run(MapsConfiguration configuration, Environment environment)
			throws Exception {
		
	}
}

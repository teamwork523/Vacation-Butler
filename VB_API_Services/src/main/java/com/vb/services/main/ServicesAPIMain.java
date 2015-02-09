package com.vb.services.main;

import com.vb.services.configuration.ServicesAPIConfigurations;
import com.vb.services.health.LocationsHealthCheck;
import com.vb.services.locations.api.LocationsCRUDAPI;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ServicesAPIMain extends Application<ServicesAPIConfigurations> {
	
	public static void main(String[] args) throws Exception {
        new ServicesAPIMain().run(args);
    }
	
	@Override
	public void initialize(Bootstrap<ServicesAPIConfigurations> bootstrap) {
		// no-op	
	}

	@Override
	public void run(ServicesAPIConfigurations conf, Environment env)
			throws Exception {
		final LocationsCRUDAPI locAPI = new LocationsCRUDAPI();
		final LocationsHealthCheck locHealth = new LocationsHealthCheck();
		
		// Register Health Checks
		env.healthChecks().register(locHealth.getClass().getName(), locHealth);
		
		// Register APIs
		env.jersey().register(locAPI);
	}

}
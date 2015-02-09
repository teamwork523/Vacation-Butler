package com.vb.services.health;

import com.codahale.metrics.health.HealthCheck;

public class LocationsHealthCheck extends HealthCheck {

	@Override
	protected Result check() throws Exception {
		// TODO: add useful checks
		return Result.healthy();
	}

}

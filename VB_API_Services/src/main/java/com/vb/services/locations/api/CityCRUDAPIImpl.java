package com.vb.services.locations.api;

import javax.ws.rs.core.Response;

import com.vb.services.model.CreateCityRq;
import com.vb.services.model.UpdateCityRq;

public class CityCRUDAPIImpl implements CityCRUDAPI {

	@Override
	public Response createCity(CreateCityRq city) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response readCityByCityName(String cityName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response updateCityByNameAndID(Integer cityID, UpdateCityRq city) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Response deleteCityByNameAndID(Integer cityID) {
		// TODO Auto-generated method stub
		return null;
	}
}

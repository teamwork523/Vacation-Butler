package com.vb.services.locations.api;

import javax.ws.rs.core.Response;

import com.vb.services.model.CityRq;
import com.vb.services.model.CityRs;

public class CityCRUDAPIImpl implements CityCRUDAPI {

	@Override
	public Response createCity(CityRq city) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Response readCityByID(Integer cityID) {
		CityRs testCityRs = new CityRs();
		testCityRs.setResultCode(0);
		testCityRs.setCityID(cityID);
		testCityRs.setCityName("seattle");
		testCityRs.setCountryName("usa");
		return Response.ok(testCityRs).build();
	}

	@Override
	public Response readCityByCityName(String cityName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response updateCity(Integer cityID, CityRq city) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response deleteCityByID(Integer cityID) {
		// TODO Auto-generated method stub
		return null;
	}

}
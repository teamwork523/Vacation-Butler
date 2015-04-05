package com.vb.services.locations.api;

import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.vb.dynamodb.domain.CityDomainServiceImpl;
import com.vb.dynamodb.model.CityItem;
import com.vb.services.model.CreateCityRq;
import com.vb.services.model.CreateCityRs;
import com.vb.services.model.ReadCityRs;
import com.vb.services.model.UpdateCityRq;

public class CityCRUDAPIImpl implements ICityCRUDAPI {
	
	// TODO: Use Spring Injection here
	private CityDomainServiceImpl cityDomainService = new CityDomainServiceImpl();

	//////////////////////////////////////
	//////// API implementation //////////
	//////////////////////////////////////
	@Override
	public Response createCity(CreateCityRq city) {
		try {
			CityItem newCity = new CityItem(city.getCityName(), 
											city.getStateName(), 
											city.getCountryName());
			newCity = cityDomainService.addCity(newCity);
			CreateCityRs ccRs = new CreateCityRs(newCity.getCityID());
			return Response.ok(ccRs).build();
		} catch (Exception e) {
			// TODO: add logging
			CreateCityRs ccRs = new CreateCityRs(e);
			Status st = LocationServiceResultMapper.httpStatus(e);
			return Response.status(st).entity(ccRs).build();
		}
	}

	@Override
	public Response readCityByCityName(String cityName) {
		try {
			List<CityItem> cities = cityDomainService.searchCitiesByName(cityName);
			ReadCityRs rcRs = new ReadCityRs(cities);
			return Response.ok(rcRs).build();
		} catch (Exception e) {
			// TODO: add logging
			ReadCityRs rcRs = new ReadCityRs(e);
			Status st = LocationServiceResultMapper.httpStatus(e);
			return Response.status(st).entity(rcRs).build();
		}
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

package com.vb.services.locations.api;

import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.vb.dynamodb.domain.CityDomainServiceImpl;
import com.vb.dynamodb.model.CityItem;
import com.vb.services.logging.VBLogger;
import com.vb.services.model.CreateCityRq;
import com.vb.services.model.CreateCityRs;
import com.vb.services.model.DeleteCityRs;
import com.vb.services.model.ReadMultipleCitiesRs;
import com.vb.services.model.UpdateCityRq;

public class CityCRUDAPIImpl implements CityCRUDAPI {

	private static final VBLogger LOGGER = VBLogger.getLogger(CityCRUDAPIImpl.class);
	
	// TODO: Use Spring Injection here
	protected CityDomainServiceImpl cityDomainService = new CityDomainServiceImpl();

	//////////////////////////////////////
	//////// API implementation //////////
	//////////////////////////////////////
	@Override
	public Response createCity(CreateCityRq city) {
		LOGGER.info("Calling Create City API with city request " + city.toString());
		try {
			CityItem newCity = new CityItem(city.getCityName(), 
											city.getStateName(), 
											city.getCountryName());
			newCity = cityDomainService.addCity(newCity);
			CreateCityRs ccRs = new CreateCityRs(newCity.getCityID());
			return Response.ok(ccRs).build();
		} catch (Exception e) {
			CreateCityRs ccRs = new CreateCityRs(e);
			Status st = LocationServiceResultMapper.httpStatus(e);
			return Response.status(st).entity(ccRs).build();
		}
	}

	@Override
	public Response readCityByCityName(String cityName) {
		LOGGER.info("Calling Read City By City Name API with city name " + cityName);
		try {
			List<CityItem> cities = cityDomainService.getCitiesByCityName(cityName);
			ReadMultipleCitiesRs rcRs = new ReadMultipleCitiesRs(cities);
			return Response.ok(rcRs).build();
		} catch (Exception e) {
			ReadMultipleCitiesRs rcRs = new ReadMultipleCitiesRs(e);
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
	public Response deleteCityByNameAndID(String cityName, Integer cityID) {
		LOGGER.info("Calling Delete City By City Name and ID API with city name " + 
				    cityName + " and city ID " + cityID);
		try {
			cityDomainService.deleteCityByNameAndID(cityName, cityID);
			DeleteCityRs dcRs = new DeleteCityRs();
			return Response.ok(dcRs).build();
		} catch (Exception e) {
			DeleteCityRs dcRs = new DeleteCityRs(e);
			Status st = LocationServiceResultMapper.httpStatus(e);
			return Response.status(st).entity(dcRs).build();
		}
	}
}

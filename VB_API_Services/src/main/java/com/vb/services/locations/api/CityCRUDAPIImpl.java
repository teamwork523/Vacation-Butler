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
		return null;
//		
//		// validate input
//		String cvtCityName = convertToStoreLocationName(cityName);
//		ReadCityRs rcRs = new ReadCityRs();
//		if (!isValidLocationName(cvtCityName)) {
//			rcRs.setResultCode(LocationServiceResultMapper.resultCode(StatusType.INVALID_CITY_NAME));
//			rcRs.setDebugInfo(LocationServiceResultMapper.toReason(StatusType.INVALID_CITY_NAME));
//			return Response.status(Status.BAD_REQUEST).entity(rcRs).build();
//		}
//				
//		// call DynamoDB
//		try {
//			List<CityItem> cities = dynamoDBDAO.searchCitiesByName(cvtCityName);
//			// TODO: update the response object with Cities attribute
//			return Response.ok().build();
//		} catch (AmazonServiceException ase) {
//			rcRs.setResultCode(LocationServiceResultMapper.resultCode(StatusType.AWS_DYNAMO_SERVER_ERROR));
//			rcRs.setDebugInfo(LocationServiceResultMapper.toReason(StatusType.AWS_DYNAMO_SERVER_ERROR) +
//						      System.lineSeparator() +
//						      LocationServiceResultMapper.debugInfo(ase));
//			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(rcRs).build();
//		} catch (AmazonClientException ace) {
//			rcRs.setResultCode(LocationServiceResultMapper.resultCode(StatusType.AWS_DYNAMO_CLIENT_ERROR));
//			rcRs.setDebugInfo(LocationServiceResultMapper.toReason(StatusType.AWS_DYNAMO_CLIENT_ERROR) +
//				      		  System.lineSeparator() +
//				      		  LocationServiceResultMapper.debugInfo(ace));
//			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(rcRs).build();
//		}
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

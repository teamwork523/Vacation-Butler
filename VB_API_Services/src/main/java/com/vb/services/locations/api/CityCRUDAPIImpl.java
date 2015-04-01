package com.vb.services.locations.api;

import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.WordUtils;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.vb.dynamodb.DAO.CityDAOImpl;
import com.vb.dynamodb.model.CityItem;
import com.vb.services.locations.api.LocationResultAndExceptionMapper.StatusType;
import com.vb.services.model.CreateCityRq;
import com.vb.services.model.CreateCityRs;
import com.vb.services.model.ReadCityRs;
import com.vb.services.model.UpdateCityRq;
import com.vb.utility.VBNumberUtility;

public class CityCRUDAPIImpl implements CityCRUDAPI {
	
	// TODO: Use Spring Injection here
	private CityDAOImpl dynamoDBDAO = new CityDAOImpl();

	////////////////////////////////////
	//////// Helper Functions //////////
	////////////////////////////////////
	/**
	 * Convert a DB stored location name into display location name
	 */
	protected String convertToDisplayLocationName(String storeLocName) {
		// Step 1: replace all the underscore with a space
		String spacedLocName = storeLocName.replaceAll("_", " ");
		
		// Step 2: Capitalize the first letter in each word
		char[] dels = {' ', '-'};
		String capitalizedLocName = WordUtils.capitalize(spacedLocName, dels);
		
		return capitalizedLocName;
	}
	
	/**
	 * Convert a input/display location name into the location name stored in DB
	 * 
	 * @param displayLocName
	 * @return
	 */
	protected String convertToStoreLocationName(String displayLocName) {
		// Step 1: replace all the consecutive white spaces into underscore
		String noSpaceLocName = displayLocName.trim().replaceAll("\\s+", "_");
		
		// Step 2: make it lower case
		String lowerCaseLocName = noSpaceLocName.toLowerCase();
		
		return lowerCaseLocName;
	}
	
	/**
	 * A DB stored location name must only contains [a-z_-], and cannot be empty
	 * Also [_] cannot at the beginning or the end of the string
	 * 
	 * @param storedLocName
	 * @return whether the location name is valid or not
	 */
	protected boolean isValidLocationName(String storedLocName) {
		if (storedLocName == null) {
			return false;
		}
		String cityLocRegx = "(^[a-z][a-z_-]*[a-z]$)|(^[a-z]{1}$)";
		return storedLocName.matches(cityLocRegx);
	}
	
	//////////////////////////////////////
	//////// API implementation //////////
	//////////////////////////////////////
	@Override
	public Response createCity(CreateCityRq city) {
		
		// validate inputs
		String cvtCityName = convertToStoreLocationName(city.getCityName()),
			   cvtStateName = convertToStoreLocationName(city.getStateName()),
			   cvtCountryName = convertToStoreLocationName(city.getCountryName());
		CreateCityRs ccRs = new CreateCityRs();
		
		if (!isValidLocationName(cvtCityName)) {
			ccRs.setResultCode(LocationResultAndExceptionMapper.toStatusCode(StatusType.INVALID_CITY_NAME));
			ccRs.setDebugInfo(LocationResultAndExceptionMapper.toReason(StatusType.INVALID_CITY_NAME));
			return Response.status(Status.BAD_REQUEST).entity(ccRs).build();
		}
		if (!isValidLocationName(cvtStateName)) {
			ccRs.setResultCode(LocationResultAndExceptionMapper.toStatusCode(StatusType.INVALID_STATE_NAME));
			ccRs.setDebugInfo(LocationResultAndExceptionMapper.toReason(StatusType.INVALID_STATE_NAME));
			return Response.status(Status.BAD_REQUEST).entity(ccRs).build();
		}
		if (!isValidLocationName(cvtCountryName)) {
			ccRs.setResultCode(LocationResultAndExceptionMapper.toStatusCode(StatusType.INVALID_COUNTRY_NAME));
			ccRs.setDebugInfo(LocationResultAndExceptionMapper.toReason(StatusType.INVALID_COUNTRY_NAME));
			return Response.status(Status.BAD_REQUEST).entity(ccRs).build();
		}
		
		try {
			// Generate the new ID
			Integer newCityID = VBNumberUtility.getRandomInt();
			CityItem newCity = new CityItem(cvtCityName, newCityID, cvtStateName, cvtCountryName);
			
			// call DynamoDB
			dynamoDBDAO.addCity(newCity);
			
			// Update the response
			ccRs.setCityID(newCityID);
			
			return Response.ok().entity(ccRs).build();
		} catch (AmazonServiceException ase) {
			ccRs.setResultCode(LocationResultAndExceptionMapper.toStatusCode(StatusType.AWS_DYNAMO_SERVER_ERROR));
			ccRs.setDebugInfo(LocationResultAndExceptionMapper.toReason(StatusType.AWS_DYNAMO_SERVER_ERROR) +
						      System.lineSeparator() +
						      LocationResultAndExceptionMapper.debugInfo(ase));
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ccRs).build();
		} catch (AmazonClientException ace) {
			ccRs.setResultCode(LocationResultAndExceptionMapper.toStatusCode(StatusType.AWS_DYNAMO_CLIENT_ERROR));
			ccRs.setDebugInfo(LocationResultAndExceptionMapper.toReason(StatusType.AWS_DYNAMO_CLIENT_ERROR) +
				      		  System.lineSeparator() +
				      		  LocationResultAndExceptionMapper.debugInfo(ace));
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ccRs).build();
		}
	}

	@Override
	public Response readCityByCityName(String cityName) {
		
		// validate input
		String cvtCityName = convertToStoreLocationName(cityName);
		ReadCityRs rcRs = new ReadCityRs();
		if (!isValidLocationName(cvtCityName)) {
			rcRs.setResultCode(LocationResultAndExceptionMapper.toStatusCode(StatusType.INVALID_CITY_NAME));
			rcRs.setDebugInfo(LocationResultAndExceptionMapper.toReason(StatusType.INVALID_CITY_NAME));
			return Response.status(Status.BAD_REQUEST).entity(rcRs).build();
		}
				
		// call DynamoDB
		try {
			List<CityItem> cities = dynamoDBDAO.retriveCityByName(cvtCityName);
			// TODO: update the response object with Cities attribute
			return Response.ok().build();
		} catch (AmazonServiceException ase) {
			rcRs.setResultCode(LocationResultAndExceptionMapper.toStatusCode(StatusType.AWS_DYNAMO_SERVER_ERROR));
			rcRs.setDebugInfo(LocationResultAndExceptionMapper.toReason(StatusType.AWS_DYNAMO_SERVER_ERROR) +
						      System.lineSeparator() +
						      LocationResultAndExceptionMapper.debugInfo(ase));
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(rcRs).build();
		} catch (AmazonClientException ace) {
			rcRs.setResultCode(LocationResultAndExceptionMapper.toStatusCode(StatusType.AWS_DYNAMO_CLIENT_ERROR));
			rcRs.setDebugInfo(LocationResultAndExceptionMapper.toReason(StatusType.AWS_DYNAMO_CLIENT_ERROR) +
				      		  System.lineSeparator() +
				      		  LocationResultAndExceptionMapper.debugInfo(ace));
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(rcRs).build();
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

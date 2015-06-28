package com.vb.dynamodb.domain;

import com.vb.dynamodb.connector.DynamoDBConnector;

import java.util.List;

import org.apache.commons.lang.WordUtils;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.vb.dynamodb.model.CityItem;
import com.vb.services.logging.VBLogger;

/**
 * The implementation of interface CityDomainService
 * 
 * @author Haokun Luo
 *
 */
// TODO: Add Spring annotation
public class CityDomainServiceImpl implements CityDomainService {

	private static final VBLogger LOGGER = VBLogger.getLogger(CityDomainServiceImpl.class);
	private static final String STATE_NAME_INDEX_NAME = "StateName-index";
	private static final String COUNTRY_NAME_INDEX_NAME = "CountryName-index";
	public enum NameType {CITY_NAME_TYPE, 
						  STATE_NAME_TYPE,
						  COUNTRY_NAME_TYPE};
	
	/////////////////////////////////////////////////
	////////////// Failure Reporting ////////////////
	/////////////////////////////////////////////////
	
	/**
	 * If the failure condition is false, e.g. success, do nothing
	 * 
	 * While if the condition is true, report the failure by logs and
	 * throwing a CityServiceFailureException
	 * 
	 * @param condition
	 * @param reason -- the code for failure
	 * @param message -- debugging
	 * @throws CityServiceFailureException
	 */
	protected void failIf(boolean condition, CityServiceFailureReason reason, String message) 
		throws CityServiceFailureException {
		if (condition) {
			CityServiceFailureException e = new CityServiceFailureException(reason, message);
			LOGGER.warn("CityServiceFailureException", e);
			throw e;
		}
	}
	
	/**
	 * if an argument if null, log and throw a standard
	 * IlligalArgumentException. Otherwise, do nothing
	 *
	 * @param name -- describe the pass-in object
	 * @param value -- the actual object
	 * @throws CityServiceFailureException
	 */
	protected void failIfArgumentNull(String name, Object value) 
			throws CityServiceFailureException {
		failIf( null == value, 
				CityServiceFailureReason.ILLEGAL_ARGUMENT, 
				"Illegal argument: " + name + " may not be null" );
	}
	
	/**
	 * Deal with an exception 
	 *
	 * @param reason -- code describing the failure
	 * @param message -- debugging
	 * @param cause -- Exception causing the failure
	 * @throws CityServiceFailureException
	 */
	protected static void failBecauseOfException(CityServiceFailureReason reason, 
			                                     String message, Throwable cause) 
		throws CityServiceFailureException {
		CityServiceFailureException e = new CityServiceFailureException(reason, message, cause);
		LOGGER.error("CityServiceFailureException", e);
		throw e;
	}
	
	/////////////////////////////////////////////////
	////////////// Helper Functions /////////////////
	/////////////////////////////////////////////////

	/**
	 * Convert a DB stored location name into display location name
	 */
	protected static String convertToDisplayLocationName(String storeLocName) {
		if (storeLocName == null) {
			return storeLocName;
		}
		
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
	protected static String convertToStoreLocationName(String displayLocName) {
		if (displayLocName == null) {
			return displayLocName;
		}
		
		// Step 1: replace all the consecutive white spaces into underscore
		String noSpaceLocName = displayLocName.trim().replaceAll("\\s+", "_");
		
		// Step 2: make it lower case
		String lowerCaseLocName = noSpaceLocName.toLowerCase();
		
		return lowerCaseLocName;
	}
	
	/**
	 * A DB stored location name must only contains [a-z_-.], and cannot be empty
	 * Also [_] cannot at the beginning or the end of the string
	 * 
	 * @param storedLocName
	 * @return whether the location name is valid or not
	 */
	protected static boolean isValidLocationName(String storedLocName) {
//		if (storedLocName == null) {
//			return false;
//		}
//		String cityLocRegx = "(^[a-z][a-z_-|\\.]*[a-z]$)|(^[a-z]{1}$)";
//		return storedLocName.matches(cityLocRegx);
		// TODO: figure out a better regular expression later
		return true;
	}
	
	/**
	 * Convert all the fields in city into display name
	 */
	private void updateCityItemFieldsIntoDisplayedNames(CityItem city) {
		city.setCityName(convertToDisplayLocationName(city.getCityName()));
		city.setStateName(convertToDisplayLocationName(city.getStateName()));
		city.setCountryName(convertToDisplayLocationName(city.getCountryName()));
	}
	
	/**
	 * Convert all the fields in city into store name
	 */
	private void updateCityItemFieldsIntoStoredNames(CityItem city) {
		city.setCityName(convertToStoreLocationName(city.getCityName()));
		city.setStateName(convertToStoreLocationName(city.getStateName()));
		city.setCountryName(convertToStoreLocationName(city.getCountryName()));
	}
	
	/**
	 * Check whether City with same City Name/State Name/Country Name has exist in DB
	 * @return true if checkCity has all three fields matching with DB records
	 *         otherwise return false
	 */
	private boolean isCityExisted(CityItem checkCity, List<CityItem> existedCities) {
		for (CityItem city : existedCities) {
			if (city.getCityName() != null && 
				(!city.getCityName().equals(checkCity.getCityName()))) {
				continue;
			}
			if (city.getStateName() != null && 
				(!city.getStateName().equals(checkCity.getStateName()))) {
				continue;
			}
			if (city.getCountryName() != null && 
				(!city.getCountryName().equals(checkCity.getCountryName()))) {
				continue;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Retrieve matched cities based on city name
	 * @param nameType
	 */
	protected List<CityItem> retrieveCitiesByName(String name, NameType nameType) 
		throws CityServiceFailureException {
		CityItem cityKey = new CityItem();
		// query with name as hash key or index key
		DynamoDBQueryExpression<CityItem> queryExpression = null;
		if (nameType == NameType.CITY_NAME_TYPE) {
			cityKey.setCityName(name);
			queryExpression = new DynamoDBQueryExpression<CityItem>()
			        		      .withHashKeyValues(cityKey);
		} else if (nameType == NameType.STATE_NAME_TYPE) {
			cityKey.setStateName(name);
			queryExpression = new DynamoDBQueryExpression<CityItem>()
      		      				  .withIndexName(STATE_NAME_INDEX_NAME)
      		      				  .withHashKeyValues(cityKey);
			// GSI doesn't support consistent read
			queryExpression.setConsistentRead(false);
		} else {
			cityKey.setCountryName(name);
			queryExpression = new DynamoDBQueryExpression<CityItem>()
			      				  .withIndexName(COUNTRY_NAME_INDEX_NAME)
			      				  .withHashKeyValues(cityKey);
			// GSI doesn't support consistent read
			queryExpression.setConsistentRead(false);
		}
				
		
		// Calling DB
		List<CityItem> cityList = null;
		try {
			cityList = DynamoDBConnector.dynamoDBMapper.query(CityItem.class, queryExpression);
		} catch (AmazonServiceException ase) {
			failBecauseOfException(CityServiceFailureReason.AWS_DYNAMO_SERVER_ERROR, 
								   "Querying name " + name + 
								   " in CityTable failed on server side.", ase);
		} catch (AmazonClientException ace) {
			failBecauseOfException(CityServiceFailureReason.AWS_DYNAMO_CLIENT_ERROR, 
					   			   "Querying name " + name + 
					   			   " in CityTable failed on client side.", ace);
		}
		
		return cityList;
	}
	
	/**
	 * Find the largest ID in a city list
	 */
	private Integer findLargestID(List<CityItem> cityList) {
		Integer max = 0;
		for (CityItem city : cityList) {
			if (city.getCityID() != null && city.getCityID() > max) {
				max = city.getCityID();
			}
		}
		return max;
	}
	
	/////////////////////////////////////////////////
	//////// Domain Service implementation //////////
	/////////////////////////////////////////////////
	/**
	 * Create a new city in CityTable
	 */
	@Override
	public CityItem addCity(CityItem city) 
			throws CityServiceFailureException {
		
		LOGGER.info("Calling Domain Add City");
		
		// Prevent nulls
		failIfArgumentNull("City Name", city.getCityName());
		failIfArgumentNull("State Name", city.getStateName());
		failIfArgumentNull("Country Name", city.getCountryName());
		
		// Update City/State/Country into stored name
		updateCityItemFieldsIntoStoredNames(city);
		
		// Validate the converted names
		failIf(false == isValidLocationName(city.getCityName()),
			   CityServiceFailureReason.INVALID_CITY_NAME, 
			   "Invalid city name format with city name " + city.getCityName());
		failIf(false == isValidLocationName(city.getStateName()),
			   CityServiceFailureReason.INVALID_STATE_NAME, 
			   "Invalid state name format with state name " + city.getStateName());
		failIf(false == isValidLocationName(city.getCountryName()),
			   CityServiceFailureReason.INVALID_COUNTRY_NAME, 
			   "Invalid country name format with country name " + city.getCountryName());
		
		// Check city name existed in the database
		List<CityItem> existedCities = retrieveCitiesByName(city.getCityName(), NameType.CITY_NAME_TYPE);
		failIf(true == isCityExisted(city, existedCities), 
			   CityServiceFailureReason.CITY_EXISTED,
			   "The city already exists. " + city.toString());
		
		// Generate the ID based on the number of same named city in DB
		Integer newCityID = findLargestID(existedCities) + 1;
		city.setCityID(newCityID);
		
		// Add the new city in database
		try {
			DynamoDBConnector.dynamoDBMapper.save(city);
		} catch (AmazonServiceException ase) {
			failBecauseOfException(CityServiceFailureReason.AWS_DYNAMO_SERVER_ERROR, 
								   "Add city to CityTable failed on server side. " + city.toString(), ase);
		} catch (AmazonClientException ace) {
			failBecauseOfException(CityServiceFailureReason.AWS_DYNAMO_CLIENT_ERROR, 
					   			   "Add city to CityTable failed on client side. " + city.toString(), ace);
		}
		
		// convert back to the fancy display name
		updateCityItemFieldsIntoDisplayedNames(city);
		
		return city;
	}

	/**
	 * Search for a list of city with given city name ([a-z_]*)
	 * @return a list of city items
	 */
	@Override
	public List<CityItem> getCitiesByCityName(String cityName) 
			throws CityServiceFailureException {
		
		LOGGER.info("Calling Domain Get Cities by City Name");
		
		// Prevent Nulls
		failIfArgumentNull("City Name", cityName);
		
		// Check for city name format
		String storeCityName = convertToStoreLocationName(cityName);
		failIf(false == isValidLocationName(storeCityName),
			   CityServiceFailureReason.INVALID_CITY_NAME, 
			   "Invalid city name format with city name " + cityName);
		
		// Call DB
		List<CityItem> searchedCities = retrieveCitiesByName(storeCityName, NameType.CITY_NAME_TYPE);
		for (CityItem searchedCity : searchedCities) {
			updateCityItemFieldsIntoDisplayedNames(searchedCity);
		}
		return searchedCities;
	}

	@Override
	public void updateCityByNameAndID(CityItem city) 
			throws CityServiceFailureException {
		// TODO: check for city existence
		
	}

	@Override
	public void deleteCityByNameAndID(String cityName, Integer cityID) 
			throws CityServiceFailureException {
		
		LOGGER.info("Calling Domain Delete Cities by City Name and City ID");
		
		// Input Check
		failIfArgumentNull("City Name", cityName);
		failIfArgumentNull("City ID", cityID);
		
		// Check for city name format
		String storeCityName = convertToStoreLocationName(cityName);
		failIf(false == isValidLocationName(storeCityName),
			   CityServiceFailureReason.INVALID_CITY_NAME, 
			   "Invalid city name format with city name " + cityName);
		
		// Calling DB
		CityItem deleteCity = new CityItem(storeCityName, cityID);
		try {
			// Check if city exist
			CityItem city = DynamoDBConnector.dynamoDBMapper.load(deleteCity);
			failIf(null == city, 
				   CityServiceFailureReason.CITY_NOT_EXISTED,
				   "City with name " + cityName + " and ID " + cityID + " doesn't exist in City Table");
			DynamoDBConnector.dynamoDBMapper.delete(city);
		} catch (AmazonServiceException ase) {
			failBecauseOfException(CityServiceFailureReason.AWS_DYNAMO_SERVER_ERROR, 
								   "Delete city to CityTable failed on server side. " + deleteCity.toString(), ase);
		} catch (AmazonClientException ace) {
			failBecauseOfException(CityServiceFailureReason.AWS_DYNAMO_CLIENT_ERROR, 
					   			   "Delete city to CityTable failed on client side. " + deleteCity.toString(), ace);
		}
	}

}

package com.vb.dynamodb.domain;

import java.util.List;

import org.apache.commons.lang.WordUtils;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.vb.dynamodb.connector.DynamoDBConnector;
import com.vb.dynamodb.model.CityItem;
import com.vb.dynamodb.model.PlaceItem;
import com.vb.services.configuration.ServicesProperty;
import com.vb.services.logging.VBLogger;
import com.vb.utility.VBNumberUtility;

/**
 * The implementation of PlaceDomainService
 * 
 * @author Haokun Luo
 *
 */
public class PlaceDomainServiceImpl implements PlaceDomainService {

	private static final VBLogger LOGGER = VBLogger.getLogger(PlaceDomainServiceImpl.class);
	private static final String CITY_NAME_AND_ID_INDEX_NAME = "CityName-CityID-index";
	private static final String CITY_ID_RANGE_KEY_ATTRIBUTE_NAME = "CityID";
	private static final String PLACE_NAME_ATTRIBUTE_NAME = "PlaceName";
	private static final String PLACE_NAME_INDEX_NAME = "PlaceName-index";
	private static final int PLACES_SEARCH_BY_kEYWORD_PATIAL_MATCH_THREAD_NUM = 8;
	
	/////////////////////////////////////////////////
	////////////// Failure Reporting ////////////////
	/////////////////////////////////////////////////
	
	/**
	* If the failure condition is false, e.g. success, do nothing
	* 
	* While if the condition is true, report the failure by logs and
	* throwing a PlaceServiceFailureException
	* 
	* @param condition
	* @param reason -- the code for failure
	* @param message -- debugging
	* @throws PlaceServiceFailureException
	*/
	protected void failIf(boolean condition, PlaceServiceFailureReason reason, String message) 
		throws PlaceServiceFailureException {
		if (condition) {
			PlaceServiceFailureException e = new PlaceServiceFailureException(reason, message);
			LOGGER.warn("PlaceServiceFailureException", e);
			throw e;
		}
	}
	
	/**
	* if an argument if null, log and throw a standard
	* IlligalArgumentException. Otherwise, do nothing
	*
	* @param name -- describe the pass-in object
	* @param value -- the actual object
	* @throws PlaceServiceFailureException
	*/
	protected void failIfArgumentNull(String name, Object value) 
		throws PlaceServiceFailureException {
		failIf( null == value, 
				PlaceServiceFailureReason.ILLEGAL_ARGUMENT, 
				"Illegal argument: " + name + " may not be null" );
	}
	
	/**
	* Deal with an exception 
	*
	* @param reason -- code describing the failure
	* @param message -- debugging
	* @param cause -- Exception causing the failure
	* @throws PlaceServiceFailureException
	*/
	protected static void failBecauseOfException(PlaceServiceFailureReason reason, 
												 String message, Throwable cause) 
		throws PlaceServiceFailureException {
		PlaceServiceFailureException e = new PlaceServiceFailureException(reason, message, cause);
		LOGGER.error("PlaceServiceFailureException", e);
		throw e;
	}
	
	/////////////////////////////////////////////////
	////////////// Helper Functions /////////////////
	/////////////////////////////////////////////////
	/**
	 * Simply make place name lower case before storing into DB
	 * 
	 * @param placeName
	 * @return
	 */
	protected static String convertPlaceNameIntoStoredFormat(String placeName) {
		return placeName.toLowerCase();
	}
	
	/**
	 * Capitalize the first letter
	 * 
	 * @param placeName
	 * @return
	 */
	protected static String convertPlaceNameIntoDisplayFormat(String placeName) {
		char[] dels = {' ', '-'};
		String capitalizedPlaceName = WordUtils.capitalize(placeName, dels);
		
		return capitalizedPlaceName;
	}
	
	/**
	 * Convert place item into stored format
	 * 
	 * @param place
	 */
	private void updatePlaceItemFieldsIntoStoredFormat(PlaceItem place) {
		place.setCityName(CityDomainServiceImpl.convertToStoreLocationName(place.getCityName()));
		place.setPlaceName(convertPlaceNameIntoStoredFormat(place.getPlaceName()));
	}
	
	/**
	 * Convert place item into displayed format
	 * 
	 * @param place
	 */
	private void updatePlaceItemFieldsIntoDisplayedFormat(PlaceItem place) {
		place.setCityName(CityDomainServiceImpl.convertToDisplayLocationName(place.getCityName()));
		place.setPlaceName(convertPlaceNameIntoDisplayFormat(place.getPlaceName()));
	}
	
	/**
	 * Validate the following fields to be not null
	 * 	1. City Name
	 * 	2. City ID
	 * 	3. Place Name
	 * 	4. Zip Code
	 * 	5. Categories
	 * 	6. Hours
	 * 	7. Latitude
	 * 	8. Longitude
	 * 	9. Street Address
	 * 
	 * @param place
	 */
	private void validatePlaceItemFieldsNotNull(PlaceItem place) 
			throws PlaceServiceFailureException {
		failIfArgumentNull("cityName", place.getCityName());
		failIfArgumentNull("cityID", place.getCityID());
		failIfArgumentNull("placeName", place.getPlaceName());
		failIfArgumentNull("zipCode", place.getZipCode());
		failIfArgumentNull("categories", place.getCategories());
		failIfArgumentNull("hours", place.getHours());
		failIfArgumentNull("latitude", place.getLatitude());
		failIfArgumentNull("longitude", place.getLongitude());
		failIfArgumentNull("streetAddress", place.getStreetAddress());
	}
	
	/**
	 * Check place has the identical Place Name / City Name / City ID given a list of cities
	 * 
	 * @param checkPlace
	 * @param placeList
	 * @return the existing place if checked place exists in the list
	 * 		   null if not 
	 */
	private PlaceItem isPlaceExisted(PlaceItem checkPlace, List<PlaceItem> placeList) {
		for (PlaceItem curPlace : placeList) {
			if (curPlace.getPlaceName() != null && 
				(!curPlace.getPlaceName().equals(checkPlace.getPlaceName()))) {
				continue;
			}
			if (curPlace.getCityName() != null && 
				(!curPlace.getCityName().equals(checkPlace.getCityName()))) {
				continue;
			}
			if (curPlace.getCityID() != null && 
				(curPlace.getCityID() != checkPlace.getCityID())) {
				continue;
			}
			return curPlace;
		}
		
		return null;
	}
	
	/**
	 * Retrieve all the places given a city
	 * 
	 * @param cityName
	 * @param cityID
	 * @return
	 */
	private List<PlaceItem> retrivePlacesByCityNameAndID(String cityName, Integer cityID) 
		throws PlaceServiceFailureException {
		PlaceItem placeKey = new PlaceItem(cityName, cityID);
		
		// Construct the query
		DynamoDBQueryExpression<PlaceItem> queryExpression = null;
		Condition rangeKeyCondition =  new Condition()
			.withComparisonOperator(ComparisonOperator.EQ.toString())
			.withAttributeValueList(new AttributeValue().withN(placeKey.getCityID().toString()));
		queryExpression = new DynamoDBQueryExpression<PlaceItem>()
							  .withIndexName(CITY_NAME_AND_ID_INDEX_NAME)
							  .withHashKeyValues(placeKey)
							  .withRangeKeyCondition(CITY_ID_RANGE_KEY_ATTRIBUTE_NAME, rangeKeyCondition);
		// GSI doesn't support consistent read
		queryExpression.setConsistentRead(false);
		
		// Calling DB
		List<PlaceItem> results = null;
		try {
			results = DynamoDBConnector.dynamoDBMapper.query(PlaceItem.class, queryExpression);
		} catch (AmazonServiceException ase) {
			failBecauseOfException(PlaceServiceFailureReason.AWS_DYNAMO_SERVER_ERROR, 
					   "Querying city name (" + cityName + ") and ID (" + cityID + 
					   ") in PlaceTable failed on server side.", ase);
		} catch (AmazonClientException ace) {
			failBecauseOfException(PlaceServiceFailureReason.AWS_DYNAMO_CLIENT_ERROR, 
		   			   "Querying city name (" + cityName + ") and ID (" + cityID + 
					   ") in PlaceTable failed on client side.", ace);
		}
		
		return results;
	}
	
	/**
	 * Lookup a place based on ID
	 * 
	 * @param placeID
	 * @return
	 * @throws PlaceServiceFailureException
	 */
	private PlaceItem retrivePlaceByID(Long placeID) 
			throws PlaceServiceFailureException {
		PlaceItem result = null;
		try {
			result = DynamoDBConnector.dynamoDBMapper.load(PlaceItem.class, placeID);
		} catch (AmazonServiceException ase) {
			failBecauseOfException(PlaceServiceFailureReason.AWS_DYNAMO_SERVER_ERROR, 
					   "Querying Place ID (" + placeID + ") in PlaceTable failed on server side.", ase);
		} catch (AmazonClientException ace) {
			failBecauseOfException(PlaceServiceFailureReason.AWS_DYNAMO_CLIENT_ERROR, 
		   			   "Querying Place ID (" + placeID + ") in PlaceTable failed on client side.", ace);
		}
		return result;
	}
	
	/**
	 * Check whether the given city name and city id exist in CityTable
	 * 
	 * @param cityName
	 * @param cityID
	 * @return
	 */
	private boolean isCityExisted(String cityName, Integer cityID) 
			throws PlaceServiceFailureException{
		CityItem result = null;
		try {
			result = DynamoDBConnector.dynamoDBMapper.load(CityItem.class, cityName, cityID);
		}catch (AmazonServiceException ase) {
			failBecauseOfException(PlaceServiceFailureReason.AWS_DYNAMO_SERVER_ERROR, 
					   "Querying city name (" + cityName + ") and ID (" + cityID + 
					   ") in City Table failed on server side.", ase);
		} catch (AmazonClientException ace) {
				failBecauseOfException(PlaceServiceFailureReason.AWS_DYNAMO_CLIENT_ERROR, 
					   "Querying city name (" + cityName + ") and ID (" + cityID + 
					   ") in City Table failed on  client side.", ace);
		}
		return (result != null);
	}
	
	/**
	 * Find all the places with keyword in their place names
	 * WARN: this is a costly call
	 * 
	 * @param placeNameKeyword
	 * @return
	 */
	private List<PlaceItem> retrievePlacesByPartialMatchedPlaceName(String placeNameKeyword, int numOfThreads)
			throws PlaceServiceFailureException {		
		// Construct the query
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
		scanExpression.addFilterCondition(PLACE_NAME_ATTRIBUTE_NAME, 
					                      new Condition()
											  .withComparisonOperator(ComparisonOperator.CONTAINS)
											  .withAttributeValueList(new AttributeValue().withS(placeNameKeyword)));
		
		// Calling DB
		List<PlaceItem> results = null;
		try {
			results = DynamoDBConnector.dynamoDBMapper.parallelScan(PlaceItem.class, scanExpression, 
																	ServicesProperty.AWS_SCAN_PARALLEL_THREAD_NUM);
		} catch (AmazonServiceException ase) {
			failBecauseOfException(PlaceServiceFailureReason.AWS_DYNAMO_SERVER_ERROR, 
					   "Scan place name keyword (" + placeNameKeyword + ") in Place Table failed on server side.", ase);
		} catch (AmazonClientException ace) {
			failBecauseOfException(PlaceServiceFailureReason.AWS_DYNAMO_CLIENT_ERROR, 
					   "Scan place name keyword (" + placeNameKeyword + ") in Place Table failed on client side.", ace);
		}
		return results;
	}
	
	/**
	 * Find the places with exactly the same place name
	 * 
	 * @param placeName
	 * @return
	 * @throws PlaceServiceFailureException
	 */
	private List<PlaceItem> retrievePlacesByPlaceName(String placeName) 
			throws PlaceServiceFailureException {
		PlaceItem placeKey = new PlaceItem(placeName);
		
		// Construct the query
		DynamoDBQueryExpression<PlaceItem> queryExpression = null;
		queryExpression = new DynamoDBQueryExpression<PlaceItem>()
							  .withIndexName(PLACE_NAME_INDEX_NAME)
							  .withHashKeyValues(placeKey);
		queryExpression.setConsistentRead(false);
		
		// Calling DB
		List<PlaceItem> results = null;
		try {
			results = DynamoDBConnector.dynamoDBMapper.query(PlaceItem.class, queryExpression);
		} catch (AmazonServiceException ase) {
			failBecauseOfException(PlaceServiceFailureReason.AWS_DYNAMO_SERVER_ERROR, 
					   "Querying place name (" + placeName + ") in PlaceTable failed on server side.", ase);
		} catch (AmazonClientException ace) {
			failBecauseOfException(PlaceServiceFailureReason.AWS_DYNAMO_CLIENT_ERROR, 
		   			   "Querying place name (" + placeName + ") in PlaceTable failed on client side.", ace);
		}
		return results;
	}
	
	/////////////////////////////////////////////////
	//////// Domain Service implementation //////////
	/////////////////////////////////////////////////
	
	@Override
	public PlaceItem addPlace(PlaceItem place)
			throws PlaceServiceFailureException {
		
		LOGGER.info("Calling Domain Add Place.");
		
		// Checking cannot be null fields
		validatePlaceItemFieldsNotNull(place);
		
		// Convert into DB stored format
		updatePlaceItemFieldsIntoStoredFormat(place);
		
		// Validate user input
		failIf(false == CityDomainServiceImpl.isValidLocationName(place.getCityName()),
			   PlaceServiceFailureReason.INVALID_CITY_NAME, 
			   "Invalid city name format.");
		failIf(place.getCityID() < 0,
			   PlaceServiceFailureReason.INVALID_CITY_ID,
			   "City ID cannot be negative.");
		
		// Check existence
		failIf(false == isCityExisted(place.getCityName(), place.getCityID()),
			   PlaceServiceFailureReason.CITY_NOT_EXISTED,
			   "Cannot find city with city name (" + place.getCityName() + 
			   ") and ID (" + place.getCityID() + ")");
		List<PlaceItem> placesListInSameCity = retrivePlacesByCityNameAndID(place.getCityName(),
																			place.getCityID());
		PlaceItem existedPlace = isPlaceExisted(place, placesListInSameCity);
		failIf(null != existedPlace,
			   PlaceServiceFailureReason.PLACE_EXISTED,
			   "Place exists in DB. " + ((existedPlace != null) ? existedPlace.toString() : ""));
		
		// Create new place ID
		Long newPlaceID;
		PlaceItem tmpPlace;
		do {
			newPlaceID = VBNumberUtility.getRandomLong();
			tmpPlace = retrivePlaceByID(newPlaceID);
			if (tmpPlace == null) {
				break;
			}
			LOGGER.info("Place with ID " + newPlaceID + " already exits in DB. " + tmpPlace.toString());
		} while (true);
		place.setPlaceID(newPlaceID);
		
		// Call DB
		try {
			DynamoDBConnector.dynamoDBMapper.save(place);
		}catch (AmazonServiceException ase) {
			failBecauseOfException(PlaceServiceFailureReason.AWS_DYNAMO_SERVER_ERROR, 
					   "Add place to PlaceTable failed on server side. " + place.toString(), ase);
		} catch (AmazonClientException ace) {
			failBecauseOfException(PlaceServiceFailureReason.AWS_DYNAMO_CLIENT_ERROR, 
				   	   "Add place to PlaceTable failed on client side. " + place.toString(), ace);
		}
		
		// Convert stored name into display name
		updatePlaceItemFieldsIntoDisplayedFormat(place);
		
		return place;
	}

	@Override
	public List<PlaceItem> getPlacesByCityNameAndID(String cityName,
			Integer cityID) throws PlaceServiceFailureException {

		LOGGER.info("Calling Domain Get Places by City Name and City ID");
		
		// Input validation
		failIfArgumentNull("City Name", cityName);
		failIfArgumentNull("City ID", cityID);
		String storeCityName = CityDomainServiceImpl.convertToStoreLocationName(cityName);
		failIf(false == CityDomainServiceImpl.isValidLocationName(storeCityName),
			   PlaceServiceFailureReason.INVALID_CITY_NAME, 
			   "Invalid city name format.");
		failIf(cityID < 0,
			   PlaceServiceFailureReason.INVALID_CITY_ID,
			   "City ID cannot be negative.");
		
		// Call DB
		List<PlaceItem> places = retrivePlacesByCityNameAndID(storeCityName, cityID);
		for (PlaceItem place : places) {
			updatePlaceItemFieldsIntoDisplayedFormat(place);
		}
		
		return places;
	}

	@Override
	public PlaceItem getPlaceByPlaceID(Long placeID)
			throws PlaceServiceFailureException {

		LOGGER.info("Calling Domain Get Places by Place ID");
		
		// Input validation
		failIfArgumentNull("Place ID", placeID);
		failIf(placeID < 0, 
			   PlaceServiceFailureReason.INVALID_PLACE_ID,
			   "Place ID cannot be negative.");
		
		// Call DB
		PlaceItem place = retrivePlaceByID(placeID);
		updatePlaceItemFieldsIntoDisplayedFormat(place);
		
		return place;
	}

	@Override
	public List<PlaceItem> getPlacesByKeyword(String keyword,
			Boolean isPartialMatched) throws PlaceServiceFailureException {
		
		LOGGER.info("Calling Domain Get Places by Keyword");
		
		// Input validation
		failIfArgumentNull("Place Keyword", keyword);
		failIfArgumentNull("Is Partial Matched", isPartialMatched);
		
		// Call DB
		List<PlaceItem> places = null;
		if (isPartialMatched == true) {
			places = retrievePlacesByPartialMatchedPlaceName(convertPlaceNameIntoStoredFormat(keyword), 
															 PLACES_SEARCH_BY_kEYWORD_PATIAL_MATCH_THREAD_NUM);
		} else {
			places = retrievePlacesByPlaceName(convertPlaceNameIntoStoredFormat(keyword));
		}
		for (PlaceItem place : places) {
			updatePlaceItemFieldsIntoDisplayedFormat(place);
		}
		
		return places;
	}

	@Override
	public void deletePaceByPlaceID(Long placeID)
			throws PlaceServiceFailureException {
		
		LOGGER.info("Calling Domain Delete Place by Place ID");
		
		// Input validation
		failIfArgumentNull("Place ID", placeID);
		failIf(placeID < 0, 
			   PlaceServiceFailureReason.INVALID_PLACE_ID,
			   "Place ID cannot be negative.");
		
		// Call DB
		PlaceItem deletePlace = new PlaceItem(placeID);
		try {
			PlaceItem place = DynamoDBConnector.dynamoDBMapper.load(deletePlace);
			failIf(null == place,
				   PlaceServiceFailureReason.PLACE_NOT_EXISTED,
				   "Place with place ID " + placeID + " doesn't exist in Place Table");
			DynamoDBConnector.dynamoDBMapper.delete(place);
		}catch (AmazonServiceException ase) {
			failBecauseOfException(PlaceServiceFailureReason.AWS_DYNAMO_SERVER_ERROR, 
					   "Delete place to PlaceTable failed on server side. " + deletePlace.toString(), ase);
		} catch (AmazonClientException ace) {
			failBecauseOfException(PlaceServiceFailureReason.AWS_DYNAMO_CLIENT_ERROR, 
				   	   "Delete place to PlaceTable failed on client side. " + deletePlace.toString(), ace);
		}
	}

}

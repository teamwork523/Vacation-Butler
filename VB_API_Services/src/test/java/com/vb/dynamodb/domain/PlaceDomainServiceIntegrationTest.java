package com.vb.dynamodb.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

import com.vb.dynamodb.domain.CityDomainService.CityServiceFailureException;
import com.vb.dynamodb.domain.CityDomainService.CityServiceFailureReason;
import com.vb.dynamodb.domain.PlaceDomainService.PlaceServiceFailureException;
import com.vb.dynamodb.domain.PlaceDomainService.PlaceServiceFailureReason;
import com.vb.dynamodb.model.CityItem;
import com.vb.dynamodb.model.PlaceItem;

/**
 * Place domain service integration test
 * 
 * @author Haokun Luo
 */
public class PlaceDomainServiceIntegrationTest extends EasyMockSupport {
	
	private PlaceDomainService m_placeDomainService;
	private CityDomainService  m_cityDomainService;
	private String testCityName = "Test City",
				   testStateName = "Test State",
				   testCountryName = "Test Country";
	
	@Before
	public void setup() {
		m_placeDomainService = new PlaceDomainServiceImpl();
		m_cityDomainService = new CityDomainServiceImpl();
		
		// Create the city
		try {
			CityItem inputCity = new CityItem(testCityName, testStateName, testCountryName);
			m_cityDomainService.addCity(inputCity);
		} catch (CityServiceFailureException e) {
			// Assume only city exist could fail here
			assertEquals(CityServiceFailureReason.CITY_EXISTED, e.getReason());
		}
	}
	
	////////////////////////////////////////////////
	// Life Cycle Testing
	@Test
	public void placeDomain_HappyPath_LifeCycle() throws Exception {
		
		// Test data
		String testActivities = "Test Activities",
			   testCategories = "Test Categories",
			   testHours = "Test Hours",
			   testPlaceName = "Test Place Name",
			   testPhoneNumber = "Test Phone Number",
			   testRecommondedLengthOfVisit = "Test Recommoned Length of Visit",
			   testReviewURL = "Test Review URL",
			   testStreetAddress = "Test Street Address",
			   testZipCode = "Test ZipCode";
		Integer testCityID = 1;
		Double testLatitude = 0.0,
			   testLongitude = 0.0,
			   testRating = 0.0;
		Long testNumberOfReviews = 0L;
		PlaceItem newPlace = new PlaceItem(testActivities, testCategories,
									       testCityName, testCityID, testHours,
									       testLatitude, testLongitude, testNumberOfReviews,
									       testPlaceName, testPhoneNumber, testRating,
									       testRecommondedLengthOfVisit, testReviewURL,
									       testStreetAddress, testZipCode);
		
		// Create place, if exist, then remove and create
		PlaceItem createdPlace = null;
		try {
			createdPlace = m_placeDomainService.addPlace(newPlace);
		} catch (PlaceServiceFailureException e) {
			// Assume only place exist could fail here
			assertEquals(PlaceServiceFailureReason.PLACE_EXISTED, e.getReason());
			
			// Find the place
			List<PlaceItem> places = m_placeDomainService.getPlacesByCityNameAndID(testCityName, testCityID);
			for (PlaceItem place: places) {
				if (place.getPlaceName().equals(testPlaceName)) {
					// Delete the place
					m_placeDomainService.deletePaceByPlaceID(place.getPlaceID());
					// Create a new one
					createdPlace = m_placeDomainService.addPlace(newPlace);
					break;
				}
			}
		}
		
		// Validate the results
		assertNotNull(createdPlace);
		assertNotNull(createdPlace.getPlaceID());
		assertTrue(createdPlace.getPlaceID() > 0);
		assertEquals(testPlaceName, createdPlace.getPlaceName());
		assertEquals(testCityName, createdPlace.getCityName());
		assertEquals(testCityID, createdPlace.getCityID());
	}
}

package com.vb.services.locations.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.easymock.EasyMock.isA;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

import com.vb.dynamodb.domain.PlaceDomainService.PlaceServiceFailureException;
import com.vb.dynamodb.domain.PlaceDomainService.PlaceServiceFailureReason;
import com.vb.dynamodb.domain.PlaceDomainServiceImpl;
import com.vb.dynamodb.model.PlaceItem;
import com.vb.services.model.CreatePlaceRq;
import com.vb.services.model.CreatePlaceRs;
import com.vb.services.model.DeletePlaceRs;
import com.vb.services.model.ReadMultiplePlacesRs;
import com.vb.services.model.ReadPlacesByKeywordRq;

public class PlaceCRUDAPIUnitTest extends EasyMockSupport {
	
	// HTTP status
	private static final int HTTP_STATUS_OK = Status.OK.getStatusCode();
	private static final int HTTP_STATUS_BAD_REQUEST = Status.BAD_REQUEST.getStatusCode();
	private static final int HTTP_STATUS_INTERNAL_SERVER_ERROR = Status.INTERNAL_SERVER_ERROR.getStatusCode();
	
	private PlaceCRUDAPIImpl m_placeCRUDAPI;
	private PlaceDomainServiceImpl m_placeDomainService;
	
	@Before
	public void setUp() throws Exception {
		m_placeCRUDAPI = new PlaceCRUDAPIImpl();
		m_placeDomainService = createMock(PlaceDomainServiceImpl.class);
		m_placeCRUDAPI.placeDomainService = m_placeDomainService;
	}
	
	////////////////////////////////////////////////
	// Create a new place
	/**
	 * The OK path to create a place
	 * 
	 * @throws Exception
	 */
	@Test
	public void createPlace_HappyPath() throws Exception {
		
		// Test Data
		String testActivities = "Test Activities",
			   testCategories = "Test Categories",
			   testCityName = "Test City Name",
			   testHours = "Test Hours",
			   testPlaceName = "Test Place Name",
			   testPhoneNumber = "Test Phone Number",
			   testRecommondedLengthOfVisit = "Test Recommoned Length of Visit",
			   testReviewURL = "Test Review URL",
			   testStreetAddress = "Test Street Address",
			   testZipCode = "Test ZipCode";
		Integer testCityID = 0;
		Double testLatitude = 0.0,
			   testLongitude = 0.0,
			   testRating = 0.0;
		Long testNumberOfReviews = 0L;
		CreatePlaceRq rq = new CreatePlaceRq(testActivities, testCategories,
										     testCityName, testCityID, testHours,
										     testLatitude, testLongitude, testNumberOfReviews,
										     testPlaceName, testPhoneNumber, testRating,
										     testRecommondedLengthOfVisit, testReviewURL,
										     testStreetAddress, testZipCode);
		PlaceItem resultPlace = new PlaceItem(rq);
		resultPlace.setPlaceID(1L);
		
		// Setup Mock
		EasyMock.expect(m_placeDomainService.addPlace(isA(PlaceItem.class))).andReturn(resultPlace);
		replayAll();
		
		// Make the call
		Response rs = m_placeCRUDAPI.createPlace(rq);
		
		// Validate the result
		assertNotNull(rs);
		assertEquals(HTTP_STATUS_OK, rs.getStatus());
		CreatePlaceRs data = (CreatePlaceRs) rs.getEntity();
		assertNotNull(data);
		assertEquals(LocationServiceResultMapper.RESULT_CODE_FOR_SUCCESS, data.getResultCode());
		assertEquals(resultPlace.getPlaceID(), data.getPlaceID());
		
		// Check the mocks
		verifyAll();
	}
	
	/**
	 * Test invalid city ID
	 * 
	 * @throws Exception
	 */
	@Test
	public void createPlace_InvalidCityID() throws Exception {
		// Test Data
		String testActivities = "Test Activities",
			   testCategories = "Test Categories",
			   testCityName = "Test City Name",
			   testHours = "Test Hours",
			   testPlaceName = "Test Place Name",
			   testPhoneNumber = "Test Phone Number",
			   testRecommondedLengthOfVisit = "Test Recommoned Length of Visit",
			   testReviewURL = "Test Review URL",
			   testStreetAddress = "Test Street Address",
			   testZipCode = "Test ZipCode";
		Integer invalidCityID = -1;
		Double testLatitude = 0.0,
			   testLongitude = 0.0,
			   testRating = 0.0;
		Long testNumberOfReviews = 0L;
		CreatePlaceRq rq = new CreatePlaceRq(testActivities, testCategories,
										     testCityName, invalidCityID, testHours,
										     testLatitude, testLongitude, testNumberOfReviews,
										     testPlaceName, testPhoneNumber, testRating,
										     testRecommondedLengthOfVisit, testReviewURL,
										     testStreetAddress, testZipCode);
		PlaceServiceFailureReason reason = PlaceServiceFailureReason.INVALID_CITY_ID;
		PlaceServiceFailureException ex = new PlaceServiceFailureException(reason);
		
		// Setup Mock
		EasyMock.expect(m_placeDomainService.addPlace(isA(PlaceItem.class))).andThrow(ex);
		replayAll();
		
		// Make the call
		Response rs = m_placeCRUDAPI.createPlace(rq);
		
		// Validate the result
		assertNotNull(rs);
		assertEquals(HTTP_STATUS_BAD_REQUEST, rs.getStatus());
		CreatePlaceRs data = (CreatePlaceRs) rs.getEntity();
		assertNotNull(data);
		assertEquals(LocationServiceResultMapper.resultCode(ex), data.getResultCode());
		assertNotNull(data.getDebugInfo());
		assertTrue(data.getDebugInfo().contains(reason.toString()));
		assertTrue(data.getDebugInfo().contains(ex.getClass().getSimpleName()));
		assertNull(data.getPlaceID());
		
		// Check the mocks
		verifyAll();
	}
	
	/**
	 * Test on AWS server side error/
	 * 
	 * @throws Exception
	 */
	@Test
	public void createPlace_AWSServerSideError() throws Exception {
		// Test Data
		String testActivities = "Test Activities",
			   testCategories = "Test Categories",
			   testCityName = "Test City Name",
			   testHours = "Test Hours",
			   testPlaceName = "Test Place Name",
			   testPhoneNumber = "Test Phone Number",
			   testRecommondedLengthOfVisit = "Test Recommoned Length of Visit",
			   testReviewURL = "Test Review URL",
			   testStreetAddress = "Test Street Address",
			   testZipCode = "Test ZipCode";
		Integer testCityID = 0;
		Double testLatitude = 0.0,
			   testLongitude = 0.0,
			   testRating = 0.0;
		Long testNumberOfReviews = 0L;
		CreatePlaceRq rq = new CreatePlaceRq(testActivities, testCategories,
										     testCityName, testCityID, testHours,
										     testLatitude, testLongitude, testNumberOfReviews,
										     testPlaceName, testPhoneNumber, testRating,
										     testRecommondedLengthOfVisit, testReviewURL,
										     testStreetAddress, testZipCode);
		PlaceServiceFailureReason reason = PlaceServiceFailureReason.AWS_DYNAMO_SERVER_ERROR;
		PlaceServiceFailureException ex = new PlaceServiceFailureException(reason);
		
		// Setup Mock
		EasyMock.expect(m_placeDomainService.addPlace(isA(PlaceItem.class))).andThrow(ex);
		replayAll();
		
		// Make the call
		Response rs = m_placeCRUDAPI.createPlace(rq);
		
		// Validate the result
		assertNotNull(rs);
		assertEquals(HTTP_STATUS_INTERNAL_SERVER_ERROR, rs.getStatus());
		CreatePlaceRs data = (CreatePlaceRs) rs.getEntity();
		assertNotNull(data);
		assertEquals(LocationServiceResultMapper.resultCode(ex), data.getResultCode());
		assertNotNull(data.getDebugInfo());
		assertTrue(data.getDebugInfo().contains(reason.toString()));
		assertTrue(data.getDebugInfo().contains(ex.getClass().getSimpleName()));
		assertNull(data.getPlaceID());
		
		// Check the mocks
		verifyAll();
	}
	
	////////////////////////////////////////////////
	// Read places by city name and ID
	/**
	 * Test OK case for read places
	 * 
	 * @throws Exception
	 */
	@Test
	public void readPlacesByCityNameAndID_HappyPath() throws Exception {
		
		// Test data
		String testCityName = "Test City Name";
		Integer testCityID = 0;
		PlaceItem place = new PlaceItem(testCityName, testCityID);
		List<PlaceItem> places = new ArrayList<PlaceItem>();
		places.add(place);
		
		// Setup Mock
		EasyMock.expect(m_placeDomainService.getPlacesByCityNameAndID(testCityName, testCityID)).andReturn(places);
		replayAll();
		
		// Make the call
		Response rs = m_placeCRUDAPI.readPlaceByCityNameAndID(testCityName, testCityID);
		
		// Validate the result
		assertNotNull(rs);
		assertEquals(HTTP_STATUS_OK, rs.getStatus());
		ReadMultiplePlacesRs data = (ReadMultiplePlacesRs) rs.getEntity();
		assertNotNull(data);
		assertEquals(LocationServiceResultMapper.RESULT_CODE_FOR_SUCCESS, data.getResultCode());
		assertEquals(1, data.getPlaceList().size());
		assertEquals(testCityName, data.getPlaceList().get(0).getCityName());
		assertEquals(testCityID, data.getPlaceList().get(0).getCityID());
		
		// Check the mocks
		verifyAll();
	}
	
	/**
	 * Test when given city ID is invalid
	 * 
	 * @throws Exception
	 */
	@Test
	public void readPlacesByCityNameAndID_InvalidCityID() throws Exception {

		// Test data
		String testCityName = "Test City Name";
		Integer invalidCityID = -1;
		PlaceServiceFailureReason reason = PlaceServiceFailureReason.INVALID_CITY_ID;
		PlaceServiceFailureException ex = new PlaceServiceFailureException(reason);
		
		// Setup Mock
		EasyMock.expect(m_placeDomainService.getPlacesByCityNameAndID(testCityName, invalidCityID)).andThrow(ex);
		replayAll();
		
		// Make the call
		Response rs = m_placeCRUDAPI.readPlaceByCityNameAndID(testCityName, invalidCityID);
		
		// Validate the result
		assertNotNull(rs);
		assertEquals(HTTP_STATUS_BAD_REQUEST, rs.getStatus());
		ReadMultiplePlacesRs data = (ReadMultiplePlacesRs) rs.getEntity();
		assertEquals(LocationServiceResultMapper.resultCode(ex), data.getResultCode());
		assertNotNull(data.getDebugInfo());
		assertTrue(data.getDebugInfo().contains(reason.toString()));
		assertTrue(data.getDebugInfo().contains(ex.getClass().getSimpleName()));
		assertNull(data.getPlaceList());
		
		// Check the mocks
		verifyAll();
	}
	
	/**
	 * Test when AWS doesn't function properly
	 * 
	 * @throws Exception
	 */
	@Test
	public void readPlacesByCityNameAndID_AWSServerSideError() throws Exception {

		// Test data
		String testCityName = "Test City Name";
		Integer testCityID = 0;
		PlaceServiceFailureReason reason = PlaceServiceFailureReason.AWS_DYNAMO_SERVER_ERROR;
		PlaceServiceFailureException ex = new PlaceServiceFailureException(reason);
		
		// Setup Mock
		EasyMock.expect(m_placeDomainService.getPlacesByCityNameAndID(testCityName, testCityID)).andThrow(ex);
		replayAll();
		
		// Make the call
		Response rs = m_placeCRUDAPI.readPlaceByCityNameAndID(testCityName, testCityID);
		
		// Validate the result
		assertNotNull(rs);
		assertEquals(HTTP_STATUS_INTERNAL_SERVER_ERROR, rs.getStatus());
		ReadMultiplePlacesRs data = (ReadMultiplePlacesRs) rs.getEntity();
		assertEquals(LocationServiceResultMapper.resultCode(ex), data.getResultCode());
		assertNotNull(data.getDebugInfo());
		assertTrue(data.getDebugInfo().contains(reason.toString()));
		assertTrue(data.getDebugInfo().contains(ex.getClass().getSimpleName()));
		assertNull(data.getPlaceList());
		
		// Check the mocks
		verifyAll();
	}
	
	////////////////////////////////////////////////
	// Read place by place ID
	/**
	 * Test OK case for read place by place ID API
	 * 
	 * @throws Exception
	 */
	@Test
	public void readPlaceByPlaceID_HappyPath() throws Exception {
		
		// Test Data
		Long testPlaceID = 0L;
		PlaceItem place = new PlaceItem(testPlaceID);
		
		// Setup Mock
		EasyMock.expect(m_placeDomainService.getPlaceByPlaceID(testPlaceID)).andReturn(place);
		replayAll();
		
		// Make the call
		Response rs = m_placeCRUDAPI.readPlaceByPlaceID(testPlaceID);
		
		// Validate the result
		assertNotNull(rs);
		assertEquals(HTTP_STATUS_OK, rs.getStatus());
		ReadMultiplePlacesRs data = (ReadMultiplePlacesRs) rs.getEntity();
		assertNotNull(data);
		assertEquals(LocationServiceResultMapper.RESULT_CODE_FOR_SUCCESS, data.getResultCode());
		assertEquals(1, data.getPlaceList().size());
		assertEquals(testPlaceID, data.getPlaceList().get(0).getPlaceID());
		
		// Check the mocks
		verifyAll();
	}
	
	/**
	 * Test for invalid place id case
	 * 
	 * @throws Exception
	 */
	@Test
	public void readPlaceByPlaceID_InvalidPlaceID() throws Exception {
		// Test Data
		Long invalidPlaceID = -1L;
		PlaceServiceFailureReason reason = PlaceServiceFailureReason.INVALID_PLACE_ID;
		PlaceServiceFailureException ex = new PlaceServiceFailureException(reason);
		
		// Setup Mock
		EasyMock.expect(m_placeDomainService.getPlaceByPlaceID(invalidPlaceID)).andThrow(ex);
		replayAll();
		
		// Make the call
		Response rs = m_placeCRUDAPI.readPlaceByPlaceID(invalidPlaceID);
		
		// Validate the result
		assertNotNull(rs);
		assertEquals(HTTP_STATUS_BAD_REQUEST, rs.getStatus());
		ReadMultiplePlacesRs data = (ReadMultiplePlacesRs) rs.getEntity();
		assertEquals(LocationServiceResultMapper.resultCode(ex), data.getResultCode());
		assertNotNull(data.getDebugInfo());
		assertTrue(data.getDebugInfo().contains(reason.toString()));
		assertTrue(data.getDebugInfo().contains(ex.getClass().getSimpleName()));
		assertNull(data.getPlaceList());
		
		// Check the mocks
		verifyAll();
	}
	
	/**
	 * Test for AWS server side failure case
	 * 
	 * @throws Exception
	 */
	@Test
	public void readPlaceByPlaceID_AWSServerSideError() throws Exception {
		// Test Data
		Long testPlaceID = 0L;
		PlaceServiceFailureReason reason = PlaceServiceFailureReason.AWS_DYNAMO_SERVER_ERROR;
		PlaceServiceFailureException ex = new PlaceServiceFailureException(reason);
		
		// Setup Mock
		EasyMock.expect(m_placeDomainService.getPlaceByPlaceID(testPlaceID)).andThrow(ex);
		replayAll();
		
		// Make the call
		Response rs = m_placeCRUDAPI.readPlaceByPlaceID(testPlaceID);
		
		// Validate the result
		assertNotNull(rs);
		assertEquals(HTTP_STATUS_INTERNAL_SERVER_ERROR, rs.getStatus());
		ReadMultiplePlacesRs data = (ReadMultiplePlacesRs) rs.getEntity();
		assertEquals(LocationServiceResultMapper.resultCode(ex), data.getResultCode());
		assertNotNull(data.getDebugInfo());
		assertTrue(data.getDebugInfo().contains(reason.toString()));
		assertTrue(data.getDebugInfo().contains(ex.getClass().getSimpleName()));
		assertNull(data.getPlaceList());
		
		// Check the mocks
		verifyAll();
	}
	
	////////////////////////////////////////////////
	// Read place by keyword
	/**
	* Test OK case for read place by keyword API
	* 
	* @throws Exception
	*/
	@Test
	public void readPlaceByKeyword_HappyPath() throws Exception {
	
		// Test Data
		String testPlaceKeyword = "Test Keyword";
		Boolean testIsPartial = false;
		PlaceItem place = new PlaceItem(testPlaceKeyword);
		List<PlaceItem> places = new ArrayList<PlaceItem>();
		places.add(place);
		
		// Setup Mock
		EasyMock.expect(m_placeDomainService.getPlacesByKeyword(testPlaceKeyword, testIsPartial)).andReturn(places);
		replayAll();
		
		// Make the call
		ReadPlacesByKeywordRq request = new ReadPlacesByKeywordRq(testIsPartial);
		Response rs = m_placeCRUDAPI.readPlacesByKeyword(testPlaceKeyword, request);
		
		// Validate the result
		assertNotNull(rs);
		assertEquals(HTTP_STATUS_OK, rs.getStatus());
		ReadMultiplePlacesRs data = (ReadMultiplePlacesRs) rs.getEntity();
		assertNotNull(data);
		assertEquals(LocationServiceResultMapper.RESULT_CODE_FOR_SUCCESS, data.getResultCode());
		assertEquals(1, data.getPlaceList().size());
		assertEquals(testPlaceKeyword, data.getPlaceList().get(0).getPlaceName());
		
		// Check the mocks
		verifyAll();
	}
	
	/**
	 * Test for invalid place name
	 * 
	 * @throws Exception
	 */
	@Test
	public void readPlaceByKeyword_InvalidPlaceName() throws Exception {
		
		// Test Data
		String testPlaceKeyword = null;
		Boolean testIsPartial = false;
		PlaceServiceFailureReason reason = PlaceServiceFailureReason.ILLEGAL_ARGUMENT;
		PlaceServiceFailureException ex = new PlaceServiceFailureException(reason);
		
		// Setup Mock
		EasyMock.expect(m_placeDomainService.getPlacesByKeyword(testPlaceKeyword, testIsPartial)).andThrow(ex);
		replayAll();
		
		// Make the call
		ReadPlacesByKeywordRq request = new ReadPlacesByKeywordRq(testIsPartial);
		Response rs = m_placeCRUDAPI.readPlacesByKeyword(testPlaceKeyword, request);
		
		// Validate the result
		assertNotNull(rs);
		assertEquals(HTTP_STATUS_BAD_REQUEST, rs.getStatus());
		ReadMultiplePlacesRs data = (ReadMultiplePlacesRs) rs.getEntity();
		assertEquals(LocationServiceResultMapper.resultCode(ex), data.getResultCode());
		assertNotNull(data.getDebugInfo());
		assertTrue(data.getDebugInfo().contains(reason.toString()));
		assertTrue(data.getDebugInfo().contains(ex.getClass().getSimpleName()));
		assertNull(data.getPlaceList());
		
		// Check the mocks
		verifyAll();
	}
	
	/**
	 * Test when AWS server has issue
	 * 
	 * @throws Exception
	 */
	@Test
	public void readPlaceByKeyword_AWSServerSideError() throws Exception {
		
		// Test Data
		String testPlaceKeyword = null;
		Boolean testIsPartial = false;
		PlaceServiceFailureReason reason = PlaceServiceFailureReason.AWS_DYNAMO_SERVER_ERROR;
		PlaceServiceFailureException ex = new PlaceServiceFailureException(reason);
		
		// Setup Mock
		EasyMock.expect(m_placeDomainService.getPlacesByKeyword(testPlaceKeyword, testIsPartial)).andThrow(ex);
		replayAll();
		
		// Make the call
		ReadPlacesByKeywordRq request = new ReadPlacesByKeywordRq(testIsPartial);
		Response rs = m_placeCRUDAPI.readPlacesByKeyword(testPlaceKeyword, request);
		
		// Validate the result
		assertNotNull(rs);
		assertEquals(HTTP_STATUS_INTERNAL_SERVER_ERROR, rs.getStatus());
		ReadMultiplePlacesRs data = (ReadMultiplePlacesRs) rs.getEntity();
		assertEquals(LocationServiceResultMapper.resultCode(ex), data.getResultCode());
		assertNotNull(data.getDebugInfo());
		assertTrue(data.getDebugInfo().contains(reason.toString()));
		assertTrue(data.getDebugInfo().contains(ex.getClass().getSimpleName()));
		assertNull(data.getPlaceList());
		
		// Check the mocks
		verifyAll();
	}
	
	////////////////////////////////////////////////
	// Delete place by place ID
	/**
	* Test OK case for delete place by place ID API
	* 
	* @throws Exception
	*/
	@Test
	public void deletePlaceByPlaceID_HappyPath() throws Exception {
		
		// Test Data
		Long testPlaceID = 1L;
		
		// Setup the Mock
		m_placeDomainService.deletePaceByPlaceID(testPlaceID);
		EasyMock.expectLastCall();
		replayAll();
		
		// Make the call
		Response rs = m_placeCRUDAPI.deletePlaceByID(testPlaceID);
		
		// Validate the result
		assertNotNull(rs);
		assertEquals(HTTP_STATUS_OK, rs.getStatus());
		DeletePlaceRs data = (DeletePlaceRs) rs.getEntity();
		assertNotNull(data);
		assertEquals(LocationServiceResultMapper.RESULT_CODE_FOR_SUCCESS, data.getResultCode());
		
		// Check the mocks
		verifyAll();
	}
	
	/**
	 * Test for invalid place id
	 * 
	 * @throws Exception
	 */
	@Test
	public void deletePlaceByPlaceID_InvalidPlaceID() throws Exception {
		
		// Test Data
		Long invalidTestPlaceID = -1L;
		PlaceServiceFailureReason reason = PlaceServiceFailureReason.INVALID_PLACE_ID;
		PlaceServiceFailureException ex = new PlaceServiceFailureException(reason);
		
		// Setup the Mock
		m_placeDomainService.deletePaceByPlaceID(invalidTestPlaceID);
		EasyMock.expectLastCall().andThrow(ex);
		replayAll();
		
		// Make the call
		Response rs = m_placeCRUDAPI.deletePlaceByID(invalidTestPlaceID);
		
		// Validate the result
		assertNotNull(rs);
		assertEquals(HTTP_STATUS_BAD_REQUEST, rs.getStatus());
		DeletePlaceRs data = (DeletePlaceRs) rs.getEntity();
		assertEquals(LocationServiceResultMapper.resultCode(ex), data.getResultCode());
		assertNotNull(data.getDebugInfo());
		assertTrue(data.getDebugInfo().contains(reason.toString()));
		assertTrue(data.getDebugInfo().contains(ex.getClass().getSimpleName()));
		
		// Check the mocks
		verifyAll();
	}
	
	/**
	 * Test when AWS has server issue
	 * 
	 * @throws Exception
	 */
	@Test
	public void deletePlaceByPlaceID_AWSServerSideError() throws Exception {
		
		// Test Data
		Long invalidTestPlaceID = -1L;
		PlaceServiceFailureReason reason = PlaceServiceFailureReason.AWS_DYNAMO_SERVER_ERROR;
		PlaceServiceFailureException ex = new PlaceServiceFailureException(reason);
		
		// Setup the Mock
		m_placeDomainService.deletePaceByPlaceID(invalidTestPlaceID);
		EasyMock.expectLastCall().andThrow(ex);
		replayAll();
		
		// Make the call
		Response rs = m_placeCRUDAPI.deletePlaceByID(invalidTestPlaceID);
		
		// Validate the result
		assertNotNull(rs);
		assertEquals(HTTP_STATUS_INTERNAL_SERVER_ERROR, rs.getStatus());
		DeletePlaceRs data = (DeletePlaceRs) rs.getEntity();
		assertEquals(LocationServiceResultMapper.resultCode(ex), data.getResultCode());
		assertNotNull(data.getDebugInfo());
		assertTrue(data.getDebugInfo().contains(reason.toString()));
		assertTrue(data.getDebugInfo().contains(ex.getClass().getSimpleName()));
		
		// Check the mocks
		verifyAll();
	}
}

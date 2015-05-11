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

import com.vb.dynamodb.domain.CityDomainServiceImpl;
import com.vb.dynamodb.domain.CityDomainService.CityServiceFailureException;
import com.vb.dynamodb.domain.CityDomainService.CityServiceFailureReason;
import com.vb.dynamodb.model.CityItem;
import com.vb.services.model.CreateCityRq;
import com.vb.services.model.CreateCityRs;
import com.vb.services.model.ReadMultipleCitiesRs;

public class CityCRUDAPIUnitTest extends EasyMockSupport {
	
	// HTTP status
	private static final int HTTP_STATUS_OK = Status.OK.getStatusCode();
	private static final int HTTP_STATUS_BAD_REQUEST = Status.BAD_REQUEST.getStatusCode();
	private static final int HTTP_STATUS_INTERNAL_SERVER_ERROR = Status.INTERNAL_SERVER_ERROR.getStatusCode();
	
	private CityCRUDAPIImpl m_cityCRUDAPI;
	private CityDomainServiceImpl m_cityDomainService;
	
	@Before
	public void setUp() throws Exception {
		m_cityCRUDAPI = new CityCRUDAPIImpl();
		m_cityDomainService = createMock(CityDomainServiceImpl.class);
		m_cityCRUDAPI.cityDomainService = m_cityDomainService;
	}
	
	////////////////////////////////////////////////
	// Create a new city
	/**
	 * Test the OK case for create city API
	 */
	@Test
	public void createCity_HappyPath() throws Exception {
		
		// Test data
		String testCityName = "Test City", 
			   testStateName = "Test State",
			   testCountryName = "Test Country";
		Integer testCityID = 1;
		CityItem resultCity = new CityItem(testCityName, testStateName, testCountryName);
		resultCity.setCityID(testCityID);
		
		// Setup Mock
		EasyMock.expect(m_cityCRUDAPI.cityDomainService.addCity(isA(CityItem.class))).andReturn(resultCity);
		replayAll();
		
		// Create request object
		CreateCityRq rq = new CreateCityRq(testCityName, testStateName, testCountryName);
		
		// Make the call
		Response rs = m_cityCRUDAPI.createCity(rq);
		
		// Validate the result
		assertNotNull(rs);
		assertEquals(HTTP_STATUS_OK, rs.getStatus());
		CreateCityRs data = (CreateCityRs) rs.getEntity();
		assertNotNull(data);
		assertEquals(LocationServiceResultMapper.RESULT_CODE_FOR_SUCCESS, data.getResultCode());
		assertEquals(resultCity.getCityID(), data.getCityID());
		
		// Check the mocks
		verifyAll();
	}
	
	/**
	 * Test when the city name is invalid
	 * @throws Exception
	 */
	@Test
	public void createCity_InvalidCityName() throws Exception {
		
		// Test data
		String invalidCityName = "#$%^&InvalidName123", 
			   testStateName = "Test State",
			   testCountryName = "Test Country";
		CityServiceFailureReason reason = CityServiceFailureReason.INVALID_CITY_NAME;
		CityServiceFailureException ex = new CityServiceFailureException(reason);
		
		// Setup Mock
		EasyMock.expect(m_cityCRUDAPI.cityDomainService.addCity(isA(CityItem.class))).andThrow(ex);
		replayAll();
		
		// Create request object
		CreateCityRq rq = new CreateCityRq(invalidCityName, testStateName, testCountryName);
		
		// Make the call
		Response rs = m_cityCRUDAPI.createCity(rq);
		
		// Validate the result
		assertNotNull(rs);
		assertEquals(HTTP_STATUS_BAD_REQUEST, rs.getStatus());
		CreateCityRs data = (CreateCityRs) rs.getEntity();
		assertNotNull(data);
		assertEquals(LocationServiceResultMapper.resultCode(ex), data.getResultCode());
		assertNotNull(data.getDebugInfo());
		assertTrue(data.getDebugInfo().contains(reason.toString()));
		assertTrue(data.getDebugInfo().contains(ex.getClass().getSimpleName()));
		assertNull(data.getCityID());
		
		// Check the mocks
		verifyAll();
	}
	
	/**
	 * Test when AWS server service is down
	 * @throws Exception
	 */
	@Test
	public void createCity_AWSServerSideError() throws Exception {
		
		// Test data
		String testCityName = "Test City", 
			   testStateName = "Test State",
			   testCountryName = "Test Country";
		CityServiceFailureReason reason = CityServiceFailureReason.AWS_DYNAMO_SERVER_ERROR;
		CityServiceFailureException ex = new CityServiceFailureException(reason);
		
		// Setup Mock
		EasyMock.expect(m_cityCRUDAPI.cityDomainService.addCity(isA(CityItem.class))).andThrow(ex);
		replayAll();
		
		// Create request object
		CreateCityRq rq = new CreateCityRq(testCityName, testStateName, testCountryName);
		
		// Make the call
		Response rs = m_cityCRUDAPI.createCity(rq);
		
		// Validate the result
		assertNotNull(rs);
		assertEquals(HTTP_STATUS_INTERNAL_SERVER_ERROR, rs.getStatus());
		CreateCityRs data = (CreateCityRs) rs.getEntity();
		assertNotNull(data);
		assertEquals(LocationServiceResultMapper.resultCode(ex), data.getResultCode());
		assertNotNull(data.getDebugInfo());
		assertTrue(data.getDebugInfo().contains(reason.toString()));
		assertTrue(data.getDebugInfo().contains(ex.getClass().getSimpleName()));
		assertNull(data.getCityID());
		
		// Check the mocks
		verifyAll();
	}
	
	////////////////////////////////////////////////
	// Read a city by city name
	/**
	* Test the OK case for read city API
	*/
	@Test
	public void readCity_HappyPath() throws Exception {
	
		// Test data
		String testCityName = "Test City", 
			   testStateName = "Test State",
			   testCountryName = "Test Country";
		Integer testCityID = 1;
		CityItem resultCity = new CityItem(testCityName, testStateName, testCountryName);
		resultCity.setCityID(testCityID);
		List<CityItem> cityList = new ArrayList<CityItem>();
		cityList.add(resultCity);
		
		// Setup Mock
		EasyMock.expect(m_cityCRUDAPI.cityDomainService.searchCitiesByCityName(testCityName)).andReturn((List<CityItem>)cityList);
		replayAll();
		
		// Make the call
		Response rs = m_cityCRUDAPI.readCityByCityName(testCityName);
		
		// Validate the result
		assertNotNull(rs);
		assertEquals(HTTP_STATUS_OK, rs.getStatus());
		ReadMultipleCitiesRs data = (ReadMultipleCitiesRs) rs.getEntity();
		assertNotNull(data);
		assertEquals(LocationServiceResultMapper.RESULT_CODE_FOR_SUCCESS, data.getResultCode());
		assertEquals(1, data.getCityList().size());
		assertEquals(testCityID, data.getCityList().get(0).getCityID());
		assertEquals(testCityName, data.getCityList().get(0).getCityName());
		assertEquals(testStateName, data.getCityList().get(0).getStateName());
		assertEquals(testCountryName, data.getCityList().get(0).getCountryName());
		
		// Check the mocks
		verifyAll();
	}
	
	/**
	* Test when the city name is invalid
	* @throws Exception
	*/
	@Test
	public void readCity_InvalidCityName() throws Exception {
	
		// Test data
		String invalidCityName = "#$%^&InvalidName123";
		CityServiceFailureReason reason = CityServiceFailureReason.INVALID_CITY_NAME;
		CityServiceFailureException ex = new CityServiceFailureException(reason);
		
		// Setup Mock
		EasyMock.expect(m_cityCRUDAPI.cityDomainService.searchCitiesByCityName(invalidCityName)).andThrow(ex);
		replayAll();
		
		// Make the call
		Response rs = m_cityCRUDAPI.readCityByCityName(invalidCityName);
		
		// Validate the result
		assertNotNull(rs);
		assertEquals(HTTP_STATUS_BAD_REQUEST, rs.getStatus());
		ReadMultipleCitiesRs data = (ReadMultipleCitiesRs) rs.getEntity();
		assertNotNull(data);
		assertEquals(LocationServiceResultMapper.resultCode(ex), data.getResultCode());
		assertNotNull(data.getDebugInfo());
		assertTrue(data.getDebugInfo().contains(reason.toString()));
		assertTrue(data.getDebugInfo().contains(ex.getClass().getSimpleName()));
		assertNull(data.getCityList());
		
		// Check the mocks
		verifyAll();
	}
	
	/**
	* Test when AWS server service is down
	* @throws Exception
	*/
	@Test
	public void readCity_AWSServerSideError() throws Exception {
	
		// Test data
		String testCityName = "Test City";
		CityServiceFailureReason reason = CityServiceFailureReason.AWS_DYNAMO_SERVER_ERROR;
		CityServiceFailureException ex = new CityServiceFailureException(reason);
		
		// Setup Mock
		EasyMock.expect(m_cityCRUDAPI.cityDomainService.searchCitiesByCityName(testCityName)).andThrow(ex);
		replayAll();
		
		// Make the call
		Response rs = m_cityCRUDAPI.readCityByCityName(testCityName);
		
		// Validate the result
		assertNotNull(rs);
		assertEquals(HTTP_STATUS_INTERNAL_SERVER_ERROR, rs.getStatus());
		ReadMultipleCitiesRs data = (ReadMultipleCitiesRs) rs.getEntity();
		assertNotNull(data);
		assertEquals(LocationServiceResultMapper.resultCode(ex), data.getResultCode());
		assertNotNull(data.getDebugInfo());
		assertTrue(data.getDebugInfo().contains(reason.toString()));
		assertTrue(data.getDebugInfo().contains(ex.getClass().getSimpleName()));
		assertNull(data.getCityList());
		
		// Check the mocks
		verifyAll();
	}
	
}

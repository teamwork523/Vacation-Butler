package com.vb.services.locations.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.easymock.EasyMock.isA;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

import com.vb.dynamodb.domain.CityDomainServiceImpl;
import com.vb.dynamodb.model.CityItem;
import com.vb.services.model.CreateCityRq;
import com.vb.services.model.CreateCityRs;

public class CityCRUDAPIUnitTest extends EasyMockSupport {
	
	// HTTP status
	private static final int HTTP_STATUS_OK = Status.OK.getStatusCode();
	private static final int HTTP_STATUS_BAD_REQUEST = Status.BAD_REQUEST.getStatusCode();
	private static final int HTTP_STATUS_INTERNAL_SERVER_ERROR = Status.INTERNAL_SERVER_ERROR.getStatusCode();
	
	private CityDomainServiceImpl m_cityDomainService;
	private CityCRUDAPIImpl m_cityCRUDAPI;
	
	@Before
	public void setUp() throws Exception {
		m_cityCRUDAPI = new CityCRUDAPIImpl();
		m_cityDomainService = createMock(CityDomainServiceImpl.class);
		m_cityCRUDAPI.cityDomainService = m_cityDomainService;
	}
	
	/**
	 * Helper to instantiate the API with domain service mock
	 */
//	protected CityCRUDAPIImpl createCityCRUDAPIImpl() {
//		CityDomainServiceImpl m_cityDomainService = createMock(CityDomainServiceImpl.class);
//		CityCRUDAPIImpl m_cityCRUDAPI = new CityCRUDAPIImpl();
//		m_cityCRUDAPI.setCityDomainService(m_cityDomainService);
//		return m_cityCRUDAPI;
//	}
//	
	////////////////////////////////////////////////
	// Create a new city
	/**
	 * Test Happy Path
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
}

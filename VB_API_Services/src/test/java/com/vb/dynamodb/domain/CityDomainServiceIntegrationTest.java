package com.vb.dynamodb.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

import com.vb.dynamodb.domain.CityDomainService.CityServiceFailureException;
import com.vb.dynamodb.domain.CityDomainService.CityServiceFailureReason;
import com.vb.dynamodb.model.CityItem;

/**
 * City domain service integration test
 * 
 * @author Haokun Luo
 */
public class CityDomainServiceIntegrationTest extends EasyMockSupport {
	
	private CityDomainService m_cityDomainService;
	
	@Before
	public void setup() {
		m_cityDomainService = new CityDomainServiceImpl();
	}
	
	////////////////////////////////////////////////
	// Life Cycle Testing
	
	@Test
	public void cityDomain_HappyPath_LifeCycle() throws Exception {
		
		// Test data
		String testCityName = "Test City", 
			   testStateName = "Test State",
			   testCountryName = "Test Country";
		CityItem inputCity = new CityItem(testCityName, testStateName, testCountryName);
		
		// Create city
		CityItem resultCity = null;
		try {
			resultCity = m_cityDomainService.addCity(inputCity);
		} catch (CityServiceFailureException e) {
			// Assume only city exist could fail here
			assertEquals(CityServiceFailureReason.CITY_EXISTED, e.getReason());
			
			// Find the City
			List<CityItem> cities = m_cityDomainService.getCitiesByCityName(testCityName);
			for (CityItem city: cities) {
				if (city.getStateName().equals(testStateName) &&
					city.getCountryName().equals(testCountryName)) {
					// delete the city
					m_cityDomainService.deleteCityByNameAndID(city.getCityName(), city.getCityID());
					
					// Then add it back
					resultCity = m_cityDomainService.addCity(inputCity);
					break;
				}
			}
			
		}
		
		// Check the mocks
		assertNotNull(resultCity);
		assertNotNull(resultCity.getCityID());
		assertEquals(testCityName, resultCity.getCityName());
		assertEquals(testStateName, resultCity.getStateName());
		assertEquals(testCountryName, resultCity.getCountryName());
	}
}

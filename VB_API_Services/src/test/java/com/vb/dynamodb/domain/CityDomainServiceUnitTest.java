package com.vb.dynamodb.domain;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.easymock.EasyMockSupport;
import org.easymock.IExpectationSetters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedList;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.vb.dynamodb.connector.DynamoDBConnector;
import com.vb.dynamodb.model.CityItem;

/**
 * City domain service unit test
 * 
 * @author Haokun Luo
 */

// TODO: make a lifecycle integration test here

//@RunWith(PowerMockRunner.class)
//@PrepareForTest({DynamoDBConnector.class})
public class CityDomainServiceUnitTest extends EasyMockSupport {
	
	private CityDomainServiceImpl m_cityDomainService;
	private DynamoDBConnector     m_dynamoDBConnector;
	
	@Before
	public void setUp() throws Exception {
		PowerMock.mockStatic(DynamoDBMapper.class);
		m_cityDomainService = new CityDomainServiceImpl();
		//DynamoDBConnector.dynamoDBMapper = createMock(DynamoDBMapper.class);
	}
	
	////////////////////////////////////////////////
	// Add a city
	/**
	* Test the OK case for adding a city service
	*/
	@Test
	public void addCity_HappyPath() throws Exception {
		
		// Test data
		String testCityName = "Test City", 
			   testStateName = "Test State",
			   testCountryName = "Test Country";
		CityItem inputCity = new CityItem(testCityName, testStateName, testCountryName);
		PaginatedQueryList<CityItem> cityList = null;
		CityItem cityKey = new CityItem();
		cityKey.setCityName("test_city");
		DynamoDBQueryExpression<CityItem> queryExpression = 
				new DynamoDBQueryExpression<CityItem>()
			        .withHashKeyValues(cityKey);
	
		// Setup Mock
		//expect(DynamoDBConnector.dynamoDBMapper.query(CityItem.class, queryExpression)).andReturn(cityList);
		//DynamoDBConnector.dynamoDBMapper.save(isA(CityItem.class));
		//expectLastCall();
		
		replayAll();
		
		// Make the call
		CityItem resultCity = m_cityDomainService.addCity(inputCity);
		
		// Check the mocks
		assertNotNull(resultCity);
		assertNotNull(resultCity.getCityID());
		assertEquals(testCityName, resultCity.getCityName());
		
		verifyAll();
	}
}

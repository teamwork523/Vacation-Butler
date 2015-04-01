package com.vb.dynamodb.DAO;

import com.vb.dynamodb.connector.DynamoDBConnector;

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.vb.dynamodb.model.CityItem;

/**
 * The implementation of interface CityDAO
 * 
 * @author Haokun Luo
 *
 */
// TODO: Add Spring annotation
public class CityDAOImpl implements CityDAO {

	@Override
	public void addCity(CityItem city) {
		// Add the new city in database
		DynamoDBConnector.dynamoDBMapper.save(city);
	}

	/**
	 * Search for a list of city with given city name ([a-z_]*)
	 * @return a list of city items
	 */
	@Override
	public List<CityItem> retriveCityByName(String cityName) {
		CityItem cityKey = new CityItem();
		cityKey.setCityName(cityName);
		
		// query with city name hash key
		DynamoDBQueryExpression<CityItem> queryExpression = 
				new DynamoDBQueryExpression<CityItem>()
			        .withHashKeyValues(cityKey);
		List<CityItem> cityList = DynamoDBConnector.dynamoDBMapper.query(CityItem.class, queryExpression);
		return cityList;
	}

	@Override
	public void updateCity(CityItem city) {
		// TODO: check for city existence
		
	}

	@Override
	public void deleteCityByNameAndID(String cityName, Integer cityID) {
		// TODO Auto-generated method stub
		
	}

}

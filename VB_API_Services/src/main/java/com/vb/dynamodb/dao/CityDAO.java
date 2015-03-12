package com.vb.dynamodb.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

/**
 * Map DyamanoDB City table to Java object
 * 
 * @author Haokun Luo
 *
 */
@DynamoDBTable(tableName="CitiesTable")
public class CityDAO {
	
	private String cityID;
	private String cityName;
	private String stateName;
	private String countryName;
	
	@DynamoDBHashKey(attributeName="CityID")
	public String getCityID() {
		return cityID;
	}
	public void setCityID(String cityID) {
		this.cityID = cityID;
	}
	
	@DynamoDBAttribute(attributeName="CityName")
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	@DynamoDBAttribute(attributeName="StateName")
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	
	@DynamoDBAttribute(attributeName="CountryName")
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	
}

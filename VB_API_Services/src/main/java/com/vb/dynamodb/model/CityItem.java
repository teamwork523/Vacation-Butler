package com.vb.dynamodb.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBVersionAttribute;

/**
 * Map City table to Java object
 * 
 * @author Haokun Luo
 *
 */
@DynamoDBTable(tableName="CitiesTable")
public class CityItem {

	private String  cityName;
	private Integer cityID;
	private String  stateName;
	private String  countryName;
	private Long    version;
	
	// Constructor for City / State / Country Name
	public CityItem(String cityName, String stateName, String countryName) {
		super();
		this.cityName = cityName;
		this.stateName = stateName;
		this.countryName = countryName;
	}

	public CityItem(String cityName, Integer cityID) {
		super();
		this.cityName = cityName;
		this.cityID = cityID;
	}

	// Default constructor
	public CityItem() {
		super();
	}

	@DynamoDBHashKey(attributeName="CityName")
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	@DynamoDBRangeKey(attributeName="CityID")
	public Integer getCityID() {
		return cityID;
	}
	public void setCityID(Integer cityID) {
		this.cityID = cityID;
	}
	
	@DynamoDBIndexHashKey(attributeName="StateName", 
			 			  globalSecondaryIndexName="StateName-index")
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	
	@DynamoDBIndexHashKey(attributeName="CountryName", 
			  			  globalSecondaryIndexName="CountryName-index")
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	
	/**
	 * Version control to prevent unintended data operation conflict
	 * Detail: http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/JavaVersionSupportHLAPI.html
	 * 
	 * @return
	 */
	@DynamoDBVersionAttribute
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version;}
    
    /**
	 * Basic info about the city
	 */
	public String toString() {
		String DEL = ", ";
		return "City ID: " + this.cityID + DEL +
			   "City Name: " + this.cityName + DEL +
			   "State Name: " + this.stateName + DEL +
			   "Country Name: " + this.countryName;
	}
}

package com.vb.dynamodb.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
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
	
	private Integer cityID;
	private String cityName;
	private String stateName;
	private String countryName;
	private Long   version;
	
	@DynamoDBHashKey(attributeName="CityID")
	public Integer getCityID() {
		return cityID;
	}
	public void setCityID(Integer cityID) {
		this.cityID = cityID;
	}
	
	@DynamoDBIndexHashKey(attributeName="CityName", 
						  globalSecondaryIndexName = "CityName-index")
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	@DynamoDBIndexHashKey(attributeName="StateName", 
			 			   globalSecondaryIndexName = "StateName-index")
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	
	@DynamoDBIndexHashKey(attributeName="CountryName ", 
			   			   globalSecondaryIndexName = "CountryName-index")
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
}

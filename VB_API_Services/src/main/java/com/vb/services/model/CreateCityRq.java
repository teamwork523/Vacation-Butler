package com.vb.services.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Create city API request object
 * 
 * @author Haokun Luo
 *
 */
public class CreateCityRq {
	
	/**
	 * Stored data field in DynamoDB
	 */
	private String cityName;
	private String stateName;
	private String countryName;
	
	/**
	 * @return the cityName
	 */
	@JsonProperty("City Name")
	public String getCityName() {
		return cityName;
	}
	
	/**
	 * @param cityName the cityName to set
	 */
	@JsonProperty("City Name")
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	/**
	 * @return the stateName
	 */
	@JsonProperty("State Name")
	public String getStateName() {
		return stateName;
	}
	
	/**
	 * @param stateName the stateName to set
	 */
	@JsonProperty("State Name")
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	
	/**
	 * @return the countryName
	 */
	@JsonProperty("Country Name")
	public String getCountryName() {
		return countryName;
	}
	
	/**
	 * @param countryName the countryName to set
	 */
	@JsonProperty("Country Name")
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	
	/**
	 * Initializing constructor, useful in various context, e.g. unit testing
	 * 
	 * @param cityName
	 * @param stateName
	 * @param countryName
	 */
	public CreateCityRq(String cityName, String stateName, String countryName) {
		super();
		this.cityName = cityName;
		this.stateName = stateName;
		this.countryName = countryName;
	}

	/**
	 * No-parameter contructor, required for Jackson deserialization
	 */
	public CreateCityRq() {
		// no-op
	}
}

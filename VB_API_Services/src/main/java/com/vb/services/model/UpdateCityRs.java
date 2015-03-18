package com.vb.services.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateCityRs {
	
	/**
	 * Stored data field in DynamoDB
	 */
	private Integer cityID;
	private String  cityName;
	private String  stateName;
	private String  countryName;
	
	/**
	 * If request failed, why.
	 * Zero for success.
	 */
	private Integer resultCode;
	
	/**
	 * Optional string which may be useful to debug
	 */
	private String debugInfo;

	/**
	 * @return the cityID
	 */
	@JsonProperty("City ID")
	public Integer getCityID() {
		return cityID;
	}
	
	/**
	 * @param cityID the cityID to set
	 */
	@JsonProperty("City ID")
	public void setCityID(Integer cityID) {
		this.cityID = cityID;
	}
	
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
	 * @return the resultCode
	 */
	@JsonProperty("Status")
	public Integer getResultCode() {
		return resultCode;
	}

	/**
	 * @param resultCode the resultCode to set
	 */
	@JsonProperty("Status")
	public void setResultCode(Integer resultCode) {
		this.resultCode = resultCode;
	}

	/**
	 * @return the debugInfo
	 */
	@JsonProperty("Reason")
	public String getDebugInfo() {
		return debugInfo;
	}

	/**
	 * @param debugInfo the debugInfo to set
	 */
	@JsonProperty("Reason")
	public void setDebugInfo(String debugInfo) {
		this.debugInfo = debugInfo;
	}
	
}

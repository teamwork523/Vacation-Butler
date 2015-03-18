package com.vb.services.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Create city API response object
 * 
 * @author Haokun Luo
 *
 */
public class CreateCityRs {
	
	/**
	 * Stored data field in DynamoDB
	 */
	private Integer cityID;
	
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

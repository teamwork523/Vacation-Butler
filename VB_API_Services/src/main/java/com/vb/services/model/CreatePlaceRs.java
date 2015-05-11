package com.vb.services.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreatePlaceRs {
	
	/**
	 * Stored data field in DynamoDB
	 */
	private Long placeID;
	
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
	 * @return the placeID
	 */
	@JsonProperty("Place ID")
	public Long getPlaceID() {
		return placeID;
	}

	/**
	 * @param placeID the placeID to set
	 */
	@JsonProperty("Place ID")
	public void setPlaceID(Long placeID) {
		this.placeID = placeID;
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
	
	// TODO: construstor for success and error
}

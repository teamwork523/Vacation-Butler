package com.vb.services.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vb.services.locations.api.LocationServiceResultMapper;

/**
 * Delete city API response object
 * 
 * @author Haokun Luo
 *
 */
public class DeletePlaceRs {
	
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
	
	/**
	 * Constructor for Success
	 */
	public DeletePlaceRs() {
		this.resultCode = LocationServiceResultMapper.RESULT_CODE_FOR_SUCCESS;
		this.debugInfo = null;
	}
	
	/**
	 * Constructor for Errors
	 */
	public DeletePlaceRs(Exception e) {
		this.resultCode = LocationServiceResultMapper.resultCode(e);
		this.debugInfo = LocationServiceResultMapper.debugInfo(e);
	}
}

package com.vb.services.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Create city API request object
 * 
 * @author Haokun Luo
 *
 */
public class ReadPlacesByKeywordRq {
	
	/**
	 * Request parameters
	 */
	private boolean isPartialMatched;

	/**
	 * @return the isPartialMatched
	 */
	@JsonProperty("is Partial Matched")
	public boolean isPartialMatched() {
		return isPartialMatched;
	}

	/**
	 * @param isPartialMatched the isPartialMatched to set
	 */
	@JsonProperty("is Partial Matched")
	public void setPartialMatched(boolean isPartialMatched) {
		this.isPartialMatched = isPartialMatched;
	}
	
	/**
	 * Initializing constructor, useful in various context, e.g. unit testing
	 * 
	 * @param isPartialMatched
	 */
	public ReadPlacesByKeywordRq(boolean isPartialMatched) {
		super();
		this.isPartialMatched = isPartialMatched;
	}

	/**
	 * No-parameter contructor, required for Jackson deserialization
	 */
	public ReadPlacesByKeywordRq() {
		// no-op
	}
}

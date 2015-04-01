package com.vb.services.locations.api;

/**
 * Location API Response code, Exceptions and their mapping
 * 
 * @author Haokun Luo
 *
 */
public class LocationResultAndExceptionMapper {
	
	/**
	 * List all the status code
	 */
	public enum StatusType {
		SUCCESS,
		AWS_DYNAMO_SERVER_ERROR,
		AWS_DYNAMO_CLIENT_ERROR,
		INVALID_CITY_NAME, INVALID_STATE_NAME, INVALID_COUNTRY_NAME
	}
	
	/**
	 * Convert the status type to status code
	 */
	public static Integer toStatusCode(StatusType st) {
		switch (st) {
			case SUCCESS:
				return 0;
			case AWS_DYNAMO_CLIENT_ERROR: 
				return 10;
			case AWS_DYNAMO_SERVER_ERROR: 
				return 11;
			case INVALID_CITY_NAME:
				return 20;
			case INVALID_STATE_NAME:
				return 21;
			case INVALID_COUNTRY_NAME:
				return 22;
			default:					  
				return 1;
		}
	}
	
	/**
	 * Get reasons for an error status type
	 */
	public static String toReason(StatusType st) {
		switch (st) {
			case AWS_DYNAMO_CLIENT_ERROR: 
				return "ERROR: AWS client side error";
			case AWS_DYNAMO_SERVER_ERROR: 
				return "ERROR: AWS server side error";
			case INVALID_CITY_NAME:
				return "ERROR: Invalid format of the city name";
			case INVALID_STATE_NAME:
				return "ERROR: Invalid format of the state name";
			case INVALID_COUNTRY_NAME:
				return "ERROR: Invalid format of the country name";
			default:					  
				return "ERROR: Failures occur on VB location APIs";
		}
	}
	
	/**
	 * Extract useful information from Exceptions 
	 */
	public static String debugInfo(Exception e) {
		StringBuilder msg = new StringBuilder();
		String DEL = ": ";
		msg.append(e.getClass().getSimpleName());
		msg.append(DEL);
		msg.append(e.getMessage());
		return msg.toString();
	}
}

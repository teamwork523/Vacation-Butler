package com.vb.services.locations.api;

import javax.ws.rs.core.Response.Status;

import com.vb.dynamodb.domain.ICityDomainService;
import com.vb.dynamodb.domain.ICityDomainService.CityServiceFailureException;
import com.vb.dynamodb.domain.ICityDomainService.CityServiceFailureReason;

/**
 * Location API Response code, Exceptions and their mapping
 * 
 * @author Haokun Luo
 *
 */
public class LocationServiceResultMapper {
	
	/**
	 * Quick access to "success"
	 */
	public final static Integer RESULT_CODE_FOR_SUCCESS = resultCode(CityServiceFailureReason.SUCCESS);
	
	/**
	 * Quick access to "something really bad happens"
	 */
	public final static Integer RESULT_CODE_FOR_UNEXPECTED_EXCEPTION = resultCode(CityServiceFailureReason.UNKNOWN_FAILURE);
	
	/**
	 * Convert the status type to result code
	 */
	public static Integer resultCode(ICityDomainService.CityServiceFailureReason reason) {
		switch (reason) {
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
			case CITY_EXISTED:
				return 30;
			default:					  
				return 1;
		}
	}
	
	/**
	 * Look up the integer for a given exception
	 */
	public static Integer resultCode(Exception e) {
		if (e instanceof CityServiceFailureException) {
			return resultCode(((CityServiceFailureException)e).getReason());
		} 
		return RESULT_CODE_FOR_UNEXPECTED_EXCEPTION;
	}
	
	/**
	 * Build a string suitable for debugging
	 */
	public static String debugInfo(Exception e) {
		StringBuilder msg = new StringBuilder();
		String DEL = ": ";
		if (e instanceof CityServiceFailureException) {
			msg.append( ((CityServiceFailureException)e).getReason().toString() );
			msg.append(DEL);
		}
		msg.append(e.getClass().getSimpleName());
		msg.append(DEL);
		msg.append(e.getMessage());
		return msg.toString();
	}
	
	/**
	 * Map failure reasons into HTTP error status
	 */
	public static Status httpStatus(Exception e) {
		if (e instanceof CityServiceFailureException) {
			CityServiceFailureReason reason = ((CityServiceFailureException)e).getReason();
			if (reason == CityServiceFailureReason.ILLEGAL_ARGUMENT ||
				reason == CityServiceFailureReason.INVALID_CITY_NAME ||
				reason == CityServiceFailureReason.INVALID_COUNTRY_NAME ||
				reason == CityServiceFailureReason.INVALID_STATE_NAME) {
				return Status.BAD_REQUEST;
			}
		}
		return Status.INTERNAL_SERVER_ERROR;
	}
}

package com.vb.services.locations.api;

import javax.ws.rs.core.Response.Status;

import com.vb.dynamodb.domain.CityDomainService;
import com.vb.dynamodb.domain.PlaceDomainService.PlaceServiceFailureException;
import com.vb.dynamodb.domain.PlaceDomainService.PlaceServiceFailureReason;
import com.vb.dynamodb.domain.PlaceDomainServiceImpl;
import com.vb.dynamodb.domain.CityDomainService.CityServiceFailureException;
import com.vb.dynamodb.domain.CityDomainService.CityServiceFailureReason;

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
	 * Convert the status type to result code for City Domain
	 */
	public static Integer resultCode(CityDomainService.CityServiceFailureReason reason) {
		return reason.getResultCode();
	}
	
	/**
	 * Convert the status type to result code for Place Domain
	 */
	public static Integer resultCode(PlaceDomainServiceImpl.PlaceServiceFailureReason reason) {
		return reason.getResultCode();
	}
	
	/**
	 * Look up the integer for a given exception
	 */
	public static Integer resultCode(Exception e) {
		if (e instanceof CityServiceFailureException) {
			return resultCode(((CityServiceFailureException)e).getReason());
		} else if (e instanceof PlaceServiceFailureException) {
			return resultCode(((PlaceServiceFailureException)e).getReason());
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
			msg.append(((CityServiceFailureException)e).getReason().toString());
			msg.append(DEL);
		} else if (e instanceof PlaceServiceFailureException) {
			msg.append(((PlaceServiceFailureException)e).getReason().toString());
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
		} else if (e instanceof PlaceServiceFailureException) {
			PlaceServiceFailureReason reason = ((PlaceServiceFailureException)e).getReason();
			if (reason == PlaceServiceFailureReason.ILLEGAL_ARGUMENT ||
				reason == PlaceServiceFailureReason.INVALID_CITY_NAME ||
				reason == PlaceServiceFailureReason.INVALID_CITY_ID ||
				reason == PlaceServiceFailureReason.INVALID_PLACE_ID) {
				return Status.BAD_REQUEST;
			}
		}
		return Status.INTERNAL_SERVER_ERROR;
	}
}

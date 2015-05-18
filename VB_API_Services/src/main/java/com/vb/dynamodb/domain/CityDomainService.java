package com.vb.dynamodb.domain;

import java.util.List;

import com.vb.dynamodb.model.CityItem;

/**
 * Apply operations on City Table
 * 
 * @author Haokun Luo
 *
 */
public interface CityDomainService {
	
	/**
	 * Error codes describing ways/reasons CityDomainService methods can fail
	 */
	public enum CityServiceFailureReason {
		SUCCESS(0),
		AWS_DYNAMO_SERVER_ERROR(10),
		AWS_DYNAMO_CLIENT_ERROR(11),
		CITY_EXISTED(20),
		CITY_ID_EXISTED(21),
		ILLEGAL_ARGUMENT(30),
		INVALID_CITY_NAME(31),
		INVALID_STATE_NAME(32), 
		INVALID_COUNTRY_NAME(33),
		UNKNOWN_FAILURE(1);
		
		private final int resultCode;
		CityServiceFailureReason(int resultCode) { this.resultCode = resultCode; }
	    public int getResultCode() { return this.resultCode; }
	}
	
	/**
	 * Exception thrown when an CityDomainService method fails
	 */
	public class CityServiceFailureException extends Exception {

		private static final long serialVersionUID = 2820248209726983146L;
		
		private CityServiceFailureReason reason;

		public CityServiceFailureReason getReason() {
			return reason;
		}

		public CityServiceFailureException(CityServiceFailureReason reason) {
			super();
			this.reason = reason;
		}
		
		public CityServiceFailureException(CityServiceFailureReason reason, String message) {
			super(message);
			this.reason = reason;
		}
		
		public CityServiceFailureException(CityServiceFailureReason reason, String message, Throwable cause) {
			super(message, cause);
			this.reason = reason;
		}

	}
	
	/**
	 * Add a new city in DB
	 * 
	 * @param city
	 */
	CityItem addCity(CityItem city) throws CityServiceFailureException;
	
	/**
	 * Get cities by name
	 * 
	 * @param cityID
	 * @return
	 */
	List<CityItem> getCitiesByCityName(String cityName) throws CityServiceFailureException ;
	
	/**
	 * Update the city information based on name and ID
	 * 
	 * @param city
	 */
	void updateCityByNameAndID(CityItem city) throws CityServiceFailureException ;
	
	/**
	 * Delete a city based on city name and ID
	 * 
	 * @param cityID
	 */
	void deleteCityByNameAndID(String cityName, Integer cityID) throws CityServiceFailureException ;
}

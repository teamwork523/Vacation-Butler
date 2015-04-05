package com.vb.dynamodb.domain;

import java.util.List;

import com.vb.dynamodb.model.CityItem;

/**
 * Apply operations on City Table
 * 
 * @author Haokun Luo
 *
 */
public interface ICityDomainService {
	
	/**
	 * Error codes describing ways/reasons CityDomainService methods can fail
	 */
	public enum CityServiceFailureReason {
		SUCCESS,
		AWS_DYNAMO_SERVER_ERROR,
		AWS_DYNAMO_CLIENT_ERROR,
		CITY_EXISTED,
		ILLEGAL_ARGUMENT,
		INVALID_CITY_NAME, 
		INVALID_STATE_NAME, 
		INVALID_COUNTRY_NAME,
		UNKNOWN_FAILURE
	}
	
	/**
	 * Exception thrown when an ICityDomainService method fails
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
	 * Search cities by name
	 * 
	 * @param cityID
	 * @return
	 */
	List<CityItem> searchCitiesByName(String cityName) throws CityServiceFailureException ;
	
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

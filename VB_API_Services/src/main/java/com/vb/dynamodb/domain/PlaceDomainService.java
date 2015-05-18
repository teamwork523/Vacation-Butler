package com.vb.dynamodb.domain;

import java.util.List;

import com.vb.dynamodb.model.PlaceItem;

/**
 * Apply operations on Place Table
 * 
 * @author Haokun Luo
 *
 */
public interface PlaceDomainService {
	
	/**
	 * Error codes describing ways/reasons PlaceDomainService methods can fail
	 */
	public enum PlaceServiceFailureReason {
		SUCCESS(0),
		AWS_DYNAMO_SERVER_ERROR(10),
		AWS_DYNAMO_CLIENT_ERROR(11),
		PLACE_EXISTED(20),
		PLACE_ID_EXISTED(21),
		ILLEGAL_ARGUMENT(30),
		INVALID_CITY_NAME(31),
		INVALID_CITY_ID(32),
		UNKNOWN_FAILURE(1);
		
		private final int resultCode;
		PlaceServiceFailureReason(int resultCode) { this.resultCode = resultCode; }
	    public int getResultCode() { return this.resultCode; }
	}
	
	/**
	 * Exception thrown when an PlaceDomainService method fails
	 */
	public class PlaceServiceFailureException extends Exception {

		private static final long serialVersionUID = 4668057384846531714L;
		
		private PlaceServiceFailureReason reason;

		public PlaceServiceFailureReason getReason() {
			return reason;
		}

		public PlaceServiceFailureException(PlaceServiceFailureReason reason) {
			super();
			this.reason = reason;
		}
		
		public PlaceServiceFailureException(PlaceServiceFailureReason reason, String message) {
			super(message);
			this.reason = reason;
		}
		
		public PlaceServiceFailureException(PlaceServiceFailureReason reason, String message, Throwable cause) {
			super(message, cause);
			this.reason = reason;
		}
	}
	
	
	/**
	 * Add a place in DB
	 * 
	 * @param place
	 * @return
	 * @throws PlaceServiceFailureException
	 */
	PlaceItem addPlace(PlaceItem place) throws PlaceServiceFailureException;
	
	/**
	 * Get a list of places by searching for city name and ID
	 * 
	 * @param cityName
	 * @param cityID
	 * @return
	 * @throws PlaceServiceFailureException
	 */
	List<PlaceItem> getPlacesByCityNameAndID(String cityName, Integer cityID) throws PlaceServiceFailureException;
	
	/**
	 * Get a place by given place ID
	 * 
	 * @param placeID
	 * @return
	 * @throws PlaceServiceFailureException
	 */
	PlaceItem getPlaceByPlaceID(Long placeID) throws PlaceServiceFailureException;
}

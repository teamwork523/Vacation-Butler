package com.vb.dynamodb.domain;

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
		SUCCESS,
		AWS_DYNAMO_SERVER_ERROR,
		AWS_DYNAMO_CLIENT_ERROR,
		PLACE_EXISTED,
		PLACE_ID_EXISTED,
		ILLEGAL_ARGUMENT,
		UNKNOWN_FAILURE
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
	
	
	
}

package com.vb.dynamodb.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBVersionAttribute;
import com.vb.services.model.CreatePlaceRq;

/**
 * Map Place table to Java object
 * 
 * @author Haokun Luo
 *
 */
@DynamoDBTable(tableName="PlacesTable")
public class PlaceItem {

	// Hash Key
	private Long    placeID;
	
	// Attributes
	private String  activities;
	private String  categories;
	private String  cityName;
	private Integer cityID;
	private String  hours;
	private Double  latitude;
	private Double  longitude;
	private Long    numberOfReviews;
	private String  placeName;
	private String  phoneNumber;
	private Double  rating;
	private String  recommandedLengthOfVisit;
	private String  reviewURL;
	private String  streetAddress;
	private String  zipCode;
	private Long    version;
	
	// Default constructor
	public PlaceItem() {
		// no-op
	}
	
	public PlaceItem(Long placeID) {
		this.placeID = placeID;
	}

	public PlaceItem(String cityName, Integer cityID) {
		this.cityName = cityName;
		this.cityID = cityID;
	}
	
	public PlaceItem(String placeName) {
		this.placeName = placeName;
	}

	// Constructor based on request
	public PlaceItem(CreatePlaceRq CPRq) {
		this.activities = CPRq.getActivities();
		this.categories = CPRq.getCategories();
		this.cityID = CPRq.getCityID();
		this.cityName = CPRq.getCityName();
		this.hours = CPRq.getHours();
		this.latitude = CPRq.getLatitude();
		this.longitude = CPRq.getLongitude();
		this.numberOfReviews = CPRq.getNumberOfReviews();
		this.phoneNumber = CPRq.getPhoneNumber();
		this.placeName = CPRq.getPlaceName();
		this.rating = CPRq.getRating();
		this.recommandedLengthOfVisit = CPRq.getRecommandedLengthOfVisit();
		this.reviewURL = CPRq.getReviewURL();
		this.streetAddress = CPRq.getStreetAddress();
		this.zipCode = CPRq.getZipCode();
	}

	public PlaceItem(String activities, String categories,
			String cityName, Integer cityID, String hours, Double latitude,
			Double longitude, Long numberOfReviews, String placeName,
			String phoneNumber, Double rating, String recommandedLengthOfVisit,
			String reviewURL, String streetAddress, String zipCode) {
		super();
		this.activities = activities;
		this.categories = categories;
		this.cityName = cityName;
		this.cityID = cityID;
		this.hours = hours;
		this.latitude = latitude;
		this.longitude = longitude;
		this.numberOfReviews = numberOfReviews;
		this.placeName = placeName;
		this.phoneNumber = phoneNumber;
		this.rating = rating;
		this.recommandedLengthOfVisit = recommandedLengthOfVisit;
		this.reviewURL = reviewURL;
		this.streetAddress = streetAddress;
		this.zipCode = zipCode;
	}

	@DynamoDBHashKey(attributeName="PlaceID")
	public Long getPlaceID() {
		return placeID;
	}
	public void setPlaceID(Long placeID) {
		this.placeID = placeID;
	}
	
	@DynamoDBAttribute(attributeName="Activities")
	public String getActivities() {
		return activities;
	}
	public void setActivities(String activities) {
		this.activities = activities;
	}
	
	@DynamoDBAttribute(attributeName="Categories")
	public String getCategories() {
		return categories;
	}
	public void setCategories(String categories) {
		this.categories = categories;
	}
	
	@DynamoDBIndexHashKey(attributeName="CityName", 
			  			  globalSecondaryIndexName="CityName-CityID-index")
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	@DynamoDBIndexRangeKey(attributeName="CityID", 
			  			   globalSecondaryIndexName="CityName-CityID-index")
	public Integer getCityID() {
		return cityID;
	}
	public void setCityID(Integer cityID) {
		this.cityID = cityID;
	}
	
	@DynamoDBAttribute(attributeName="Hours")
	public String getHours() {
		return hours;
	}
	public void setHours(String hours) {
		this.hours = hours;
	}
	
	@DynamoDBAttribute(attributeName="Latitude")
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
	@DynamoDBAttribute(attributeName="Longitude")
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	@DynamoDBAttribute(attributeName="Number of Reviews")
	public Long getNumberOfReviews() {
		return numberOfReviews;
	}
	public void setNumberOfReviews(Long numberOfReviews) {
		this.numberOfReviews = numberOfReviews;
	}
	
	@DynamoDBIndexHashKey(attributeName="PlaceName", 
			              globalSecondaryIndexName="PlaceName-index")
	public String getPlaceName() {
		return placeName;
	}
	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}
	
	@DynamoDBAttribute(attributeName="Phone Number")
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	@DynamoDBAttribute(attributeName="Rating")
	public Double getRating() {
		return rating;
	}
	public void setRating(Double rating) {
		this.rating = rating;
	}
	
	@DynamoDBAttribute(attributeName="Recommended Length of Visit")
	public String getRecommandedLengthOfVisit() {
		return recommandedLengthOfVisit;
	}
	public void setRecommandedLengthOfVisit(String recommandedLengthOfVisit) {
		this.recommandedLengthOfVisit = recommandedLengthOfVisit;
	}
	
	@DynamoDBAttribute(attributeName="Review URL")
	public String getReviewURL() {
		return reviewURL;
	}
	public void setReviewURL(String reviewURL) {
		this.reviewURL = reviewURL;
	}
	
	@DynamoDBAttribute(attributeName="Street Address")
	public String getStreetAddress() {
		return streetAddress;
	}
	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}
	
	@DynamoDBIndexHashKey(attributeName="ZipCode", 
            			  globalSecondaryIndexName="ZipCode-index")
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	/**
	 * Version control to prevent unintended data operation conflict
	 * Detail: http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/JavaVersionSupportHLAPI.html
	 * 
	 * @return
	 */
	@DynamoDBVersionAttribute
	public Long getVersion() {
		return version;
	}
	public void setVersion(Long version) {
		this.version = version;
	}

	/**
	 * Basic info about the place
	 */
	public String toString() {
		String DEL = ", ";
		return "Place ID: " + this.placeID + DEL +
			   "Place Name: " + this.placeName + DEL +
			   "City ID: " + this.cityID + DEL +
			   "City Name: " + this.cityName;
	}
}

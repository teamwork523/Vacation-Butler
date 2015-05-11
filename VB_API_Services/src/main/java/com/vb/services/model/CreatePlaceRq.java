package com.vb.services.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Create place API object
 * 
 * @author Haokun Luo
 * 
 */
public class CreatePlaceRq {

	/**
	 * Stored data field in DynamoDB
	 */
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
	
	/**
	 * @return the activities
	 */
	@JsonProperty("Activities")
	public String getActivities() {
		return activities;
	}
	
	/**
	 * @param activities the activities to set
	 */
	@JsonProperty("Activities")
	public void setActivities(String activities) {
		this.activities = activities;
	}
	
	/**
	 * @return the categories
	 */
	@JsonProperty("Categories")
	public String getCategories() {
		return categories;
	}
	
	/**
	 * @param categories the categories to set
	 */
	@JsonProperty("Categories")
	public void setCategories(String categories) {
		this.categories = categories;
	}
	/**
	 * @return the cityName
	 */
	@JsonProperty("City Name")
	public String getCityName() {
		return cityName;
	}

	/**
	 * @param cityName the cityName to set
	 */
	@JsonProperty("City Name")
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	/**
	 * @return the cityID
	 */
	@JsonProperty("City ID")
	public Integer getCityID() {
		return cityID;
	}

	/**
	 * @param cityID the cityID to set
	 */
	@JsonProperty("City ID")
	public void setCityID(Integer cityID) {
		this.cityID = cityID;
	}
	
	/**
	 * @return the hours
	 */
	@JsonProperty("Hours")
	public String getHours() {
		return hours;
	}
	
	/**
	 * @param hours the hours to set
	 */
	@JsonProperty("Hours")
	public void setHours(String hours) {
		this.hours = hours;
	}
	
	/**
	 * @return the latitude
	 */
	@JsonProperty("Latitude")
	public Double getLatitude() {
		return latitude;
	}
	
	/**
	 * @param latitude the latitude to set
	 */
	@JsonProperty("Latitude")
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
	/**
	 * @return the longitude
	 */
	@JsonProperty("Longitude")
	public Double getLongitude() {
		return longitude;
	}
	
	/**
	 * @param longitude the longitude to set
	 */
	@JsonProperty("Longitude")
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	/**
	 * @return the numberOfReviews
	 */
	@JsonProperty("Number of Reviews")
	public Long getNumberOfReviews() {
		return numberOfReviews;
	}
	
	/**
	 * @param numberOfReviews the numberOfReviews to set
	 */
	@JsonProperty("Number of Reviews")
	public void setNumberOfReviews(Long numberOfReviews) {
		this.numberOfReviews = numberOfReviews;
	}
	
	/**
	 * @return the placeName
	 */
	@JsonProperty("Place Name")
	public String getPlaceName() {
		return placeName;
	}
	
	/**
	 * @param placeName the placeName to set
	 */
	@JsonProperty("Place Name")
	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}
	
	/**
	 * @return the phoneNumber
	 */
	@JsonProperty("Phone Number")
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	@JsonProperty("Phone Number")
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	/**
	 * @return the rating
	 */
	@JsonProperty("Rating")
	public Double getRating() {
		return rating;
	}
	
	/**
	 * @param rating the rating to set
	 */
	@JsonProperty("Rating")
	public void setRating(Double rating) {
		this.rating = rating;
	}
	
	/**
	 * @return the recommandedLengthOfVisit
	 */
	@JsonProperty("Recommended Length of Visit")
	public String getRecommandedLengthOfVisit() {
		return recommandedLengthOfVisit;
	}
	
	/**
	 * @param recommandedLengthOfVisit the recommandedLengthOfVisit to set
	 */
	@JsonProperty("Recommended Length of Visit")
	public void setRecommandedLengthOfVisit(String recommandedLengthOfVisit) {
		this.recommandedLengthOfVisit = recommandedLengthOfVisit;
	}
	
	/**
	 * @return the reviewURL
	 */
	@JsonProperty("Review URL")
	public String getReviewURL() {
		return reviewURL;
	}
	
	/**
	 * @param reviewURL the reviewURL to set
	 */
	@JsonProperty("Review URL")
	public void setReviewURL(String reviewURL) {
		this.reviewURL = reviewURL;
	}
	
	/**
	 * @return the streetAddress
	 */
	@JsonProperty("Street Address")
	public String getStreetAddress() {
		return streetAddress;
	}
	
	/**
	 * @param streetAddress the streetAddress to set
	 */
	@JsonProperty("Street Address")
	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}
	
	/**
	 * @return the zipCode
	 */
	@JsonProperty("Zip Code")
	public String getZipCode() {
		return zipCode;
	}
	
	/**
	 * @param zipCode the zipCode to set
	 */
	@JsonProperty("Zip Code")
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	/**
	 * @param activities
	 * @param categories
	 * @param cityName
	 * @param cityID
	 * @param hours
	 * @param latitude
	 * @param longitude
	 * @param numberOfReviews
	 * @param placeName
	 * @param phoneNumber
	 * @param rating
	 * @param recommandedLengthOfVisit
	 * @param reviewURL
	 * @param streetAddress
	 * @param zipCode
	 */
	public CreatePlaceRq(String activities, String categories, String cityName,
			Integer cityID, String hours, Double latitude, Double longitude,
			Long numberOfReviews, String placeName, String phoneNumber,
			Double rating, String recommandedLengthOfVisit, String reviewURL,
			String streetAddress, String zipCode) {
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

	/**
	 * No-parameter contructor, required for Jackson deserialization
	 */
	public CreatePlaceRq() {
		// no-op
	}
}

package com.vb.services.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vb.dynamodb.model.CityItem;
import com.vb.services.locations.api.LocationServiceResultMapper;

public class ReadMultipleCitiesRs {
	
	/**
	 * Support to return a list of Cities
	 */
	public class City {
		
		/**
		 * Constructor for all fields
		 */
		public City(Integer cityID, String cityName, String stateName,
				String countryName) {
			super();
			this.cityID = cityID;
			this.cityName = cityName;
			this.stateName = stateName;
			this.countryName = countryName;
		}

		/**
		 * Stored data field in DynamoDB
		 */
		private Integer cityID;
		private String  cityName;
		private String  stateName;
		private String  countryName;
		
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
		 * @return the stateName
		 */
		@JsonProperty("State Name")
		public String getStateName() {
			return stateName;
		}
		
		/**
		 * @param stateName the stateName to set
		 */
		@JsonProperty("State Name")
		public void setStateName(String stateName) {
			this.stateName = stateName;
		}
		
		/**
		 * @return the countryName
		 */
		@JsonProperty("Country Name")
		public String getCountryName() {
			return countryName;
		}
		
		/**
		 * @param countryName the countryName to set
		 */
		@JsonProperty("Country Name")
		public void setCountryName(String countryName) {
			this.countryName = countryName;
		}		
	}
	
	/**
	 * A list of matched cities.
	 * If something goes wrong, return null
	 */
	private List<City> cityList;
	
	/**
	 * If request failed, why.
	 * Zero for success.
	 */
	private Integer resultCode;
	
	/**
	 * Optional string which may be useful to debug
	 */
	private String debugInfo;


	/**
	 * @return the cityList
	 */
	@JsonProperty("Cities")
	public List<City> getCityList() {
		return cityList;
	}

	/**
	 * @param cityList the cityList to set
	 */
	@JsonProperty("Cities")
	public void setCityList(List<City> cityList) {
		this.cityList = cityList;
	}
	
	/**
	 * @return the resultCode
	 */
	@JsonProperty("Status")
	public Integer getResultCode() {
		return resultCode;
	}

	/**
	 * @param resultCode the resultCode to set
	 */
	@JsonProperty("Status")
	public void setResultCode(Integer resultCode) {
		this.resultCode = resultCode;
	}

	/**
	 * @return the debugInfo
	 */
	@JsonProperty("Reason")
	public String getDebugInfo() {
		return debugInfo;
	}

	/**
	 * @param debugInfo the debugInfo to set
	 */
	@JsonProperty("Reason")
	public void setDebugInfo(String debugInfo) {
		this.debugInfo = debugInfo;
	}
	
	/**
	 * Constructor for Success
	 */
	public ReadMultipleCitiesRs(List<CityItem> cities) {
		this.cityList = new ArrayList<City>();
		for (CityItem city : cities) {
			City retrivedCity = new City(city.getCityID(),
									     city.getCityName(),
									     city.getStateName(),
									     city.getCountryName());
			this.cityList.add(retrivedCity);
		}
		this.resultCode = LocationServiceResultMapper.RESULT_CODE_FOR_SUCCESS;
		this.debugInfo = null;
	}
	
	/**
	 * Constructor for Errors
	 */
	public ReadMultipleCitiesRs(Exception e) {
		this.cityList = null;
		this.resultCode = LocationServiceResultMapper.resultCode(e);
		this.debugInfo = LocationServiceResultMapper.debugInfo(e);
	}
}

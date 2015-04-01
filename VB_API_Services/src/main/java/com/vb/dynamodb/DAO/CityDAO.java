package com.vb.dynamodb.DAO;

import java.util.List;

import com.vb.dynamodb.model.CityItem;

/**
 * Apply operations on City Table
 * 
 * @author Haokun Luo
 *
 */
public interface CityDAO {
	
	/**
	 * @param city
	 */
	void addCity(CityItem city);
	
	// TODO: maybe a list of partial mapped cities?
	/**
	 * @param cityID
	 * @return
	 */
	List<CityItem> retriveCityByName(String cityName);
	
	/**
	 * @param city
	 */
	void updateCity(CityItem city);
	
	/**
	 * @param cityID
	 */
	void deleteCityByNameAndID(String cityName, Integer cityID);
}

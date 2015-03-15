package com.vb.dynamodb.DAO;

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
	
	/**
	 * @param cityID
	 * @return
	 */
	CityItem retriveCityByID(String cityID);
	
	// TODO: maybe a list of partial mapped cities?
	/**
	 * @param cityID
	 * @return
	 */
	CityItem retriveCityByName(String cityID);
	
	/**
	 * @param city
	 */
	void updateCity(CityItem city);
	
	/**
	 * @param cityID
	 */
	void deleteCityByID(String cityID);
}

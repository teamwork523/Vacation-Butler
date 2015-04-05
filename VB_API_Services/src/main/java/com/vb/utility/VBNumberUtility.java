package com.vb.utility;

import java.util.Random;

/**
 * Number related utility functions
 * 
 * @author Haokun Luo
 *
 */
public class VBNumberUtility {
	
	public static Random randomGenerator;
	
	static {
		randomGenerator = new Random();
	}
	
	/**
	 * Generate a random Integer between 0 and 2^32-1
	 * 
	 * @return
	 */
	public static Integer getRandomInt() {
		if (randomGenerator == null) {
			randomGenerator = new Random();
		}
		return randomGenerator.nextInt(Integer.MAX_VALUE);
	}
}
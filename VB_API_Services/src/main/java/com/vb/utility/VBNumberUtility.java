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
	 * Cannot be negative
	 * 
	 * @return
	 */
	public static Integer getRandomInt() {
		if (randomGenerator == null) {
			randomGenerator = new Random();
		}
		return Math.abs(randomGenerator.nextInt(Integer.MAX_VALUE));
	}
	
	/**
	 * Generate a random Long, notice that the security seed only covers up to 2^48
	 * Cannot be negative
	 * 
	 * @return
	 */
	public static Long getRandomLong() {
		if (randomGenerator == null) {
			randomGenerator = new Random();
		}
		return Math.abs(randomGenerator.nextLong());
	}
}

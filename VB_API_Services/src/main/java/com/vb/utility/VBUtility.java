package com.vb.utility;

import java.util.Random;

/**
 * All the utility functions
 * 
 * @author Haokun Luo
 *
 */
public class VBUtility {
	
	public static Random randomGenerator;
	
	static {
		randomGenerator = new Random();
	}
	
	/**
	 * Generate a random Integer between 0 and 2^32-1
	 * 
	 * @return
	 */
	public Integer getRandomInt() {
		return randomGenerator.nextInt(Integer.MAX_VALUE);
	}
}

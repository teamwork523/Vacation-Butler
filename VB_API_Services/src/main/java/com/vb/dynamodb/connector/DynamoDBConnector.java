package com.vb.dynamodb.connector;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

public class DynamoDBConnector {
	
	/**
	 * Use default configuration chain
	 * Detail: http://docs.aws.amazon.com/AWSSdkDocsJava/latest/DeveloperGuide/java-dg-roles.html
	 */
	public static AmazonDynamoDBClient dynamoDBClient;
	public static DynamoDBMapper dynamoDBMapper;
	
	static {
		dynamoDBClient = new AmazonDynamoDBClient();
		// Need to explicit the region endpoint since default is US_EAST_1
		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
		dynamoDBClient.setRegion(usWest2);
		dynamoDBMapper = new DynamoDBMapper(dynamoDBClient);
	}
}

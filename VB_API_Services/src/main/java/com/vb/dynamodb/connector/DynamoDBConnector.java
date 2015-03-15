package com.vb.dynamodb.connector;

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
		dynamoDBMapper = new DynamoDBMapper(dynamoDBClient);
	}
}

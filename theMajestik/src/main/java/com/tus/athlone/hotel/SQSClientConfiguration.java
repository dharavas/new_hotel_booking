package com.tus.athlone.hotel;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;

import software.amazon.awssdk.services.sns.SnsClient;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


public class SQSClientConfiguration {
    private static final String awsAccessKeyId = System.getenv("ACCESS_KEY");
    private static final String awsSecretKeyId = System.getenv("ACCESS_SECRET_KEY");


    public SnsClient getSQSUrl(String queueName) {
    	AWSCredentials credentials = new BasicAWSCredentials(awsAccessKeyId, awsSecretKeyId);
    
    	AmazonSQS sqs = AmazonSQSClientBuilder.standard()
    		  .withCredentials(new AWSStaticCredentialsProvider(credentials))
    		  .withRegion(Regions.US_EAST_2)
    		  .build();
    	SnsClient  snsClient = SnsClient.builder()
                .region(Region.US_EAST_2)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();
    
    CreateQueueRequest createStandardQueueRequest = new CreateQueueRequest(queueName);
    String standardQueueUrl = sqs.createQueue(createStandardQueueRequest).getQueueUrl();
    
    return snsClient;

    }
    
//    public void SendSQSMessage(String message, String queueName) {
//    	Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
//    	messageAttributes.put("AttributeOne", new MessageAttributeValue()
//    	  .withStringValue("This is an Payment Confirmation")
//    	  .withDataType("String")); 
//    	SendMessageRequest sendMessageStandardQueue = new SendMessageRequest()
//    		  .withQueueUrl(getSQSUrl(queueName));
//  
//    	
//    }
}
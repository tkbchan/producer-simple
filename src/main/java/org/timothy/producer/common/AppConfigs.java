package org.timothy.producer.common;

public class AppConfigs {
    public final static String applicationID = "Producer";
    public final static String consumerGroupID = "java-group-consumer";
    public final static String bootstrapServers = "localhost:9092,localhost:9093";
    public final static String topicName = "twitter-testing";
    public final static int numEvents = 5000;
    public static int MAX_NO_MESSAGE_FOUND_COUNT = 100;
    // for TwtProducer
    public final static String consumerKey = "API_KEY";
    public final static String consumerSecret = "API_KEY";
    public final static String token = "API_KEY";
    public final static String secret = "API_KEY";
}

package org.timothy.producer.common;

public class AppConfigs {
    public final static String applicationID = "Producer";
    public final static String consumerGroupID = "java-group-consumer";
    public final static String bootstrapServers = "localhost:9092,localhost:9093";
    public final static String topicName = "fourth-topic";
    public final static int numEvents = 12;
}

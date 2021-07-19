package org.timothy.producer.common;

public class AppConfigs {
    public final static String applicationID = "Producer";
    public final static String bootstrapServers = "localhost:9092,localhost:9093";
    public final static String topicName = "producer-simple";
    public final static int numEvents = 10;
}

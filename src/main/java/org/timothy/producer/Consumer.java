package org.timothy.producer;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.timothy.producer.common.AppConfigs;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;


public class Consumer{

    private static final Logger logger = LogManager.getLogger(Consumer.class);
    public static void main(String[] args) {
        Properties props = new Properties();
        props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfigs.bootstrapServers);
        props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class.getName());
        props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, AppConfigs.consumerGroupID);
        props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<Integer, String> consumer = new KafkaConsumer<>(props);

        consumer.subscribe(Arrays.asList("test-topic"));

        while(true){
            ConsumerRecords<Integer,String> records = consumer.poll(Duration.ofMillis(1000));
            for(ConsumerRecord record: records){
                logger.info("Received new record: " +
                        " Key: " + record.key() +
                        ", Value: " + record.value() +
                        ", Topic: " + record.topic() +
                        ", Partition: " + record.partition() +
                        ", Offset: " + record.offset() + "\n"
                );
            }
        }
    }
}

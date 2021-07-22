package org.timothy.producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.timothy.producer.common.AppConfigs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.timothy.producer.common.PropConfigs;


import java.util.Properties;

public class Producer{

    private static final Logger logger = LogManager.getLogger(Producer.class);
    public static void main(String[] args) {
        logger.info("Creating Kafka Producer...");
        PropConfigs p = new PropConfigs();
        p.ProdProps();
        Properties props = new Properties();
        props.setProperty(ProducerConfig.CLIENT_ID_CONFIG, AppConfigs.applicationID);
        props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfigs.bootstrapServers);
        props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.setProperty(ProducerConfig.ACKS_CONFIG, "all");
        props.setProperty(ProducerConfig.RETRIES_CONFIG, "3");
        props.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5");

        KafkaProducer<Integer, String> producer = new KafkaProducer<>(props);

        logger.info("Start sending messages...");

        for (int i = 1; i <= AppConfigs.numEvents; i++) {
            producer.send(new ProducerRecord<>(AppConfigs.topicName, i, "Message " + i + " Test"), new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if(e == null){
                        logger.info("\nReceived metadata" + " Topic:" + recordMetadata.topic() + " Partition: " + recordMetadata.partition() + " Offset: " + recordMetadata.offset() + " Time: " + recordMetadata.timestamp() + "\n");
                    } else {
                        logger.error("Error", e);
                    }
                }
            });
        }

        logger.info("Finished - Closing Kafka Producer.");
        producer.flush();
        producer.close();

    }
}

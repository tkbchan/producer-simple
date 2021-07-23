package org.timothy.producer;

import org.apache.kafka.clients.producer.*;
import org.timothy.producer.common.AppConfigs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.timothy.producer.common.PropConfigs;

public class Producer{

    private static final Logger logger = LogManager.getLogger(Producer.class);
    public static void main(String[] args) {
        logger.info("Creating Kafka Producer...");

        KafkaProducer<Integer, String> producer = PropConfigs.prodProps();

        logger.info("Start sending messages...");

        for (int i = 1; i <= AppConfigs.numEvents; i++) {
            producer.send(new ProducerRecord<>(AppConfigs.topicName, i, "Test " + i + " Message"), new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if(e == null){
                        logger.info("\nReceived metadata" + " Topic:" + recordMetadata.topic() + " Partition: " + recordMetadata.partition() + " Offset: " + recordMetadata.offset() + " Time: " + recordMetadata.timestamp() + "\n");
                    } else {
                        logger.error("Error", e);
                        e.printStackTrace();
                    }
                }
            });
        }

        logger.info("Finished - Closing Kafka Producer.");
        producer.flush();
        producer.close();

    }
}

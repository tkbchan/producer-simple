package org.timothy.producer;

import org.apache.kafka.clients.producer.*;
import org.timothy.producer.common.AppConfigs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.timothy.producer.common.PropConfigs;

import java.util.concurrent.ExecutionException;

public class Producer{

    private static final Logger logger = LogManager.getLogger(Producer.class);
    public static void main(String[] args) {
        logger.info("Creating Kafka Producer...");

        KafkaProducer<Integer, String> producer = PropConfigs.prodProps();

        logger.info("Start sending messages...");

        for (int i = 1; i <= AppConfigs.numEvents; i++) {
            ProducerRecord<Integer, String> record = new ProducerRecord<Integer, String>(AppConfigs.topicName, "This is Message: " + i);
            try {
                RecordMetadata metadata = producer.send(record).get();
                System.out.println("Record sent with key " + i + " to partition " + metadata.partition()
                        + " with offset " + metadata.offset());
            }
            catch (ExecutionException e) {
                System.out.println("Error in sending record");
                System.out.println(e);
            }
            catch (InterruptedException e) {
                System.out.println("Error in sending record");
                System.out.println(e);
            }
        }

        logger.info("Finished - Closing Kafka Producer.");
        producer.flush();
        producer.close();

    }
}

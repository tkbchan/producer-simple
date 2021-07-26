package org.timothy.producer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.timothy.producer.common.AppConfigs;
import org.timothy.producer.common.PropConfigs;
import java.time.Duration;
import java.util.Collections;


public class Consumer{

    private static final Logger logger = LogManager.getLogger(Consumer.class);
    public static void main(String[] args) {
        KafkaConsumer<Integer, String> consumer = PropConfigs.consProps();

        consumer.subscribe(Collections.singleton(AppConfigs.topicName));
        int noMessageFound = 0;

        while(true){
            ConsumerRecords<Integer,String> records = consumer.poll(Duration.ofMillis(1000));

            if (records.count() == 0) {
                noMessageFound++;
                if (noMessageFound > AppConfigs.MAX_NO_MESSAGE_FOUND_COUNT)
                    break;
                else
                    continue;
            }

            records.forEach(record -> {
                logger.info("Received new record: " +
                        " Key: " + record.key() +
                        ", Value: " + record.value() +
                        ", Topic: " + record.topic() +
                        ", Partition: " + record.partition() +
                        ", Offset: " + record.offset() + "\n"
                );
            });
            consumer.commitAsync();

        }

    }
}

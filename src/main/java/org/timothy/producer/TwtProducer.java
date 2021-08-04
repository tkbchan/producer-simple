package org.timothy.producer;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.event.Event;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.timothy.producer.common.AppConfigs;
import org.timothy.producer.common.PropConfigs;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TwtProducer {
    private static final Logger logger = LogManager.getLogger(TwtProducer.class);
    public TwtProducer() {}

    public static void main(String[] args) {
        new TwtProducer().run();
    }

    public void run(){
        logger.info("Setting up Kafka Twitter Producer...");
        BlockingQueue<String> msgQueue = new LinkedBlockingQueue<>(500);
        Client client = twtClient(msgQueue);
        client.connect(); //invokes the connection function
        KafkaProducer<Integer, String> producer = PropConfigs.prodProps();

        while (!client.isDone()) {
            String msg = null;
            try {
                msg = msgQueue.poll(5, TimeUnit.SECONDS);//specify the time
            } catch (InterruptedException e) {
                e.printStackTrace();
                client.stop();
            }
            if (msg != null) {
                logger.info(msg);
                producer.send(new ProducerRecord<>(AppConfigs.topicName, null, msg), new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                        if(e!=null){
                            logger.error("Something went wrong",e);
                        }
                    }
                });
            }

        }//Specify the topic name, key value, msg

        logger.info("Finished - Closing");
    }

    public Client twtClient(BlockingQueue<String> msgQueue) {

        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();
        List<String> terms = Lists.newArrayList("COVID");
        hosebirdEndpoint.trackTerms(terms);
        Authentication hosebirdAuth = new OAuth1(AppConfigs.consumerKey,AppConfigs.consumerSecret,AppConfigs.token,AppConfigs.secret);
        ClientBuilder builder = new ClientBuilder()
                .name("twitter-test")
                .hosts(hosebirdHosts)
                .authentication(hosebirdAuth)
                .endpoint(hosebirdEndpoint)
                .processor(new StringDelimitedProcessor(msgQueue));


        Client hosebirdClient = builder.build();
        return hosebirdClient;
    }
}

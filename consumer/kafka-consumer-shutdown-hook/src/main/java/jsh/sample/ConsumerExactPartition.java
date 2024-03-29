package jsh.sample;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerExactPartition {
    private final static Logger logger = LogManager.getLogger(ConsumerExactPartition.class);
    //Topic 이름 지정
    private final static String TOPIC_NAME = "test";
    //Kafka Broker Server 입력
    private final static String GROUP_ID = "test-group";
    private final static String BOOTSTRAP_SERVERS = "192.168.219.130:9092";
    private static KafkaConsumer<String, String> consumer;

    public static void main(String[] args) {
        //shutdown hook을 날리는 코드
        Runtime.getRuntime().addShutdownHook(new ShutdownThread());
        Properties configs = new Properties();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);


        consumer = new KafkaConsumer<>(configs);
        consumer.subscribe(Arrays.asList(TOPIC_NAME));

        try{
            //topic으로 부터 데이터를 전부 읽기
            while(true){
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
                for(ConsumerRecord<String, String> record:records){
                    logger.info("record:{}", record);
                }
                consumer.commitSync();
            }
        }catch(WakeupException e){
            logger.warn("Wakeup Consumer : " + e );

        }finally{
            logger.warn("Consumer close");
            consumer.close();
        }

    }

    static class ShutdownThread extends Thread {
        public void run() {
            logger.info("Shutdown Hook");
            consumer.wakeup();
        }
    }
}
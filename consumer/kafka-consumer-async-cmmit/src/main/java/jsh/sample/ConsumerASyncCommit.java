package jsh.sample;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

public class ConsumerASyncCommit {
    private final static Logger logger = LogManager.getLogger(ConsumerASyncCommit.class);
    //Topic 이름 지정
    private final static String TOPIC_NAME = "test";
    //Kafka Broker Server 입력
    private final static String BOOTSTRAP_SERVERS = "192.168.219.130:9092";
    private final static String GROUP_ID = "test-group";
    public static void main(String[] args) {
        Properties configs = new Properties();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        //auto commit 관련 설정
        configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(configs);
        consumer.subscribe(Arrays.asList(TOPIC_NAME));

        //topic으로 부터 데이터를 전부 읽기
        while(true){
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
            for(ConsumerRecord<String, String> record:records){
                logger.info("record:{}", record);
            }
            //데이터는 계속 처리하고 background에서 commit을 수행
            //commit이 실패한 경우 로그를 남기게 할 수 있다. callback Check를 통해서 확인 가능
            consumer.commitAsync(new OffsetCommitCallback() {
                @Override
                public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
                    if (exception != null){
                        logger.error("Commit failed for offsets {}", offsets, exception);
                    }else{
                        logger.info("Commit Success");
                    }
                }
            });
        }
    }
}
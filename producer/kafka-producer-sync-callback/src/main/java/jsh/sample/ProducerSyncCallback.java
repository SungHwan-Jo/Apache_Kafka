package jsh.sample;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class ProducerSyncCallback {
    private final static Logger logger = LogManager.getLogger(ProducerSyncCallback.class);
    //Topic 이름 지정
    private final static String TOPIC_NAME = "test";
    //Kafka Broker Server 입력
    private final static String BOOTSTRAP_SERVERS = "192.168.219.130:9092";
    public static void main(String[] args) {

        Properties configs = new Properties();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        //acks가 0인경우 broker로부터 데이터를 받았는지 확인하지 않기 때문에  offset 번호에 대한 응답을 확인할 수 없음
        //configs.put(ProducerConfig.ACKS_CONFIG, "0");

        KafkaProducer<String, String> producer = new KafkaProducer<>(configs);


        try{

            ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, "Busan", "Busan");
            RecordMetadata metadata = producer.send(record).get();

            logger.info("{}", metadata.toString());


        }catch(Exception e){
            e.printStackTrace();

        }finally {
            producer.flush();
            producer.close();
        }
    }
}
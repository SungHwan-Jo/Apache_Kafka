package jsh.sample;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class Main {
    private final static Logger logger = LogManager.getLogger(Main.class);
    //Topic 이름 지정
    private final static String TOPIC_NAME = "test";
    //Kafka Broker Server 입력
    private final static String BOOTSTRAP_SERVERS = "192.168.219.130:9092";
    public static void main(String[] args) {

        Properties configs = new Properties();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        //Custom Partition 지정
        configs.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, CustomPartitioner.class);

        KafkaProducer<String, String> producer = new KafkaProducer<>(configs);


        try{

            ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, "Pangyo", "Pangyo");
            producer.send(record);

            logger.info("{}", record);


        }catch(Exception e){
            e.printStackTrace();

        }finally {
            producer.flush();
            producer.close();
        }
    }
}
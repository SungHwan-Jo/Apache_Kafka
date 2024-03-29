package jsh.sample;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class KstreamKtableJoin {
    private final static Logger logger = LogManager.getLogger(KstreamKtableJoin.class);
    private static String APPLICATION_NAME = "order-join-application";
    private static String ADDRESS_TABLE = "address";
    private static String ORDER_STREAM = "order";
    private static String ORDER_JOIN_STREAM = "order_join";
    private static String STREAM_LOG_FILTER = "stream_log_filter";
    //Kafka Broker Server 입력
    private final static String BOOTSTRAP_SERVERS = "192.168.219.130:9092";

    public static void main(String[] args) {
        Properties configs = new Properties();
        configs.put(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_NAME);
        configs.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        configs.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        configs.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();

        KTable<String ,String> addressTable = builder.table(ADDRESS_TABLE);
        KStream<String, String> orderstream = builder.stream(ORDER_STREAM);

        orderstream.join(addressTable, (order, address) -> order + " send to " + address).to(ORDER_JOIN_STREAM);


        //kafka streams 실행
        KafkaStreams streams;
        streams = new KafkaStreams(builder.build(), configs);
        streams.start();

    }
}
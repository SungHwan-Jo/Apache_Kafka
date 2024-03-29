package jsh.sample;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class StreamsFilter {
    private final static Logger logger = LogManager.getLogger(StreamsFilter.class);
    private static String APPLICATION_NAME = "streams-filter-application";
    private static String STREAM_LOG = "stream_log";
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
        KStream<String, String> streamLog = builder.stream(STREAM_LOG);
        //value값 길이가 5초과인 경우만 stream_log_filter topic에 전달된다.
        streamLog.filter((key, value) -> value.length() > 5).to(STREAM_LOG_FILTER);

        //kafka streams 실행
        KafkaStreams streams;
        streams = new KafkaStreams(builder.build(), configs);
        streams.start();

    }
}
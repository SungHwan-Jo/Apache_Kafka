package jsh.sample;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.common.TopicPartition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;

public class RebalanceListener implements ConsumerRebalanceListener {
    private final static Logger logger = LogManager.getLogger(jsh.sample.ConsumerRebalanceListener.class);
    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
        logger.warn("Partitions are revoked : " + partitions.toString());
    }

    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
        logger.warn("Partitions are assigned : " + partitions.toString());
    }
}

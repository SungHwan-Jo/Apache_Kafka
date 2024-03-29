package jsh.sample;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.InvalidRecordException;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.utils.Utils;

import java.util.List;
import java.util.Map;

public class CustomPartitioner implements Partitioner {
    //partition 번호를 return
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        if(keyBytes == null){
            throw new InvalidRecordException("Need Message Key");
        }

        //판교 key값에 대해서는 파티션 넘버가 무조건 0으로 가게 설정
        if(((String)key).equals("Pangyo")){
            return 0;
        }

        // 그외 key 값은 다시 Hash값을 계산하여 파티션에 적재되게 return
        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
        int numPartitions = partitions.size();
        return Utils.toPositive(Utils.murmur2(keyBytes)) % numPartitions;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}

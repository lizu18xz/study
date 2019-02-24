package com.fayayo.study.kafka.partition;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.record.InvalidRecordException;
import org.apache.kafka.common.utils.Utils;

import javax.management.relation.InvalidRelationIdException;
import java.util.List;
import java.util.Map;

/**
 * @author dalizu on 2019/2/21.
 * @version v1.0
 * @desc
 */
public class CoustomPartitioner implements Partitioner{


    @Override
    public int partition(String topic, Object key, byte[] keyBytes,
                         Object value, byte[] valueBytes, Cluster cluster) {

        //获取所有的分区
        List<PartitionInfo>list=cluster.partitionsForTopic(topic);

        //分区个数
        int nums=list.size();


        if(null==keyBytes||!(key instanceof String)){

            throw new InvalidRecordException("kafka message must has key");
        }

        if(nums==1){
            return 0;
        }

        if(key.equals("name")){

            return nums-1;
        }

        return Math.abs(Utils.murmur2(keyBytes))%(nums-1);
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}

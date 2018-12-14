package com.fayayo.study.kafka.consumer.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.common.TopicPartition;

import java.util.Collection;

/**
 * @author dalizu on 2018/11/6.
 * @version v1.0
 * @desc 实现一个ConsumerRebalanceListener，在该listener内部执行
        consumer.seek(topicPartition,offset)，从指定的topic/partition的offset处启动。
 */
@Slf4j
public class MyConsumerRebalancerListener implements ConsumerRebalanceListener {

    private OffsetManager offsetManager = new OffsetManager("storage2");
    private Consumer<String, String> consumer;

    public MyConsumerRebalancerListener(Consumer<String, String> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
        for (TopicPartition partition : partitions) {
            offsetManager.saveOffsetInExternalStore(partition.topic(), partition.partition(),
                    consumer.position(partition));
        }
    }

    //重新分配partition之后和消费者开始读取消息之前被调用。
    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
        //所有分区的设置
        for (TopicPartition partition : partitions) {
            consumer.seek(partition, offsetManager.readOffsetFromExternalStore(partition.topic(),partition.partition()));
        }
    }

}

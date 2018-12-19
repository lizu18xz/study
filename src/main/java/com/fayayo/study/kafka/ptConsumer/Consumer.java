package com.fayayo.study.kafka.ptConsumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author dalizu on 2018/12/18.
 * @version v1.0
 * @desc  指定分区消费的例子,可以修改为多线程
 */
public class Consumer {

    public static void main(String[] args) {

        Properties props = new Properties();
        props.put("bootstrap.servers", "127.0.0.1:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        List<TopicPartition> tPartitions = new ArrayList<>();
        TopicPartition tPartition0 = new TopicPartition("data-push", 0);
        TopicPartition tPartition1 = new TopicPartition("data-push", 1);
        TopicPartition tPartition2 = new TopicPartition("data-push", 2);
        tPartitions.add(tPartition0);
        tPartitions.add(tPartition1);
        tPartitions.add(tPartition2);
        consumer.assign(tPartitions);
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records){
                System.out.printf("offset = %d, key = %s,partition = %s value = %s%n", record.offset(), record.key(),record.partition(), record.value());

            }
        }
    }
}

package com.fayayo.study.kafka.consumer;

import com.fayayo.study.kafka.consumer.util.MyConsumerRebalancerListener;
import com.fayayo.study.kafka.consumer.util.OffsetManager;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

/**
 * @author dalizu on 2018/11/6.
 * @version v1.0
 * @desc Exactly-once   只消费一次  consumer.seek(topicPartition,offset)，从指定的topic/partition的offset处启动。
 * 在处理消息的时候，要同时控制保存住每个消息的offset。以原子事务的方式保存offset和处理的消息结果。传统数据库实现原子事务比较简单。
 * 但对于非传统数据库，比如hdfs或者nosql，为了实现这个目标，只能将offset与消息保存在同一行。
 */
public class ExactlyOnceDynamicConsumer {

    public static void main(String[] args) {
        KafkaConsumer<String, String> consumer = createConsumer();
        // Manually controlling offset but register consumer to topics to get dynamically
        //  assigned partitions. Inside MyConsumerRebalancerListener use
        // consumer.seek(topicPartition,offset) to control offset which messages to be read.
        //实现一个ConsumerRebalanceListener，在该listener内部执行
        // consumer.seek(topicPartition,offset)，从指定的topic/partition的offset处启动。
        consumer.subscribe(Arrays.asList("test-topic"),
                new MyConsumerRebalancerListener(consumer));
        processRecords(consumer);
    }


    private static OffsetManager offsetManager = new OffsetManager("storage2");

    private static KafkaConsumer<String, String> createConsumer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.88.129:9092");
        String consumeGroup = "cg3";
        props.put("group.id", consumeGroup);
        // Below is a key setting to turn off the auto commit.
        props.put("enable.auto.commit", "false");
        props.put("heartbeat.interval.ms", "2000");
        props.put("session.timeout.ms", "6001");
        // Control maximum data on each poll, make sure this value is bigger than the maximum  // single message size
        props.put("max.partition.fetch.bytes", "5242880");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        return new KafkaConsumer<String, String>(props);
    }


    private static void processRecords(KafkaConsumer<String, String> consumer) {
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, partition = %s, key = %s, value = %s\n", record.offset(), record.partition(),
                        record.key(), record.value());
                // Save processed offset in external storage.
                //格式  主题  哪个分区   读取到 这个分区的哪个offset
                offsetManager.saveOffsetInExternalStore(record.topic(), record.partition(), record.offset());
            }
        }
    }

}

package com.fayayo.study.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

/**
 * @author dalizu on 2018/11/6.
 * @version v1.0
 * @desc 最多消费一次，会存在 丢失数据  也有可能会数据重复 自动提交 enable.auto.commit", "true"
 */
public class AtMostOnceConsumer {

    public static void main(String[] args) {


        KafkaConsumer<String, String> consumer = createConsumer();
        // Subscribe to all partition in that topic. 'assign' could be used here
        // instead of 'subscribe' to subscribe to specific partition.
        consumer.subscribe(Arrays.asList("test-topic"));
        processRecords(consumer);

    }


    private static KafkaConsumer<String, String> createConsumer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.88.129:9092");
        String consumeGroup = "cg1";
        props.put("group.id", consumeGroup);
        // Set this property, if auto commit should happen.
        props.put("enable.auto.commit", "true");
        // Auto commit interval, kafka would commit offset at this interval.
        /***
         * 消费者已经处理完了，但是offset还没提交，那么这个时候消费者挂了，就会导致消费者重复消费消息处理。
         * 但是由于auto.commit.interval.ms设置为一个较低的时间范围，会降低这种情况出现的概率。
         */
        props.put("auto.commit.interval.ms", "101");
        // This is how to control number of records being read in each poll
        props.put("max.partition.fetch.bytes", "135");
        // Set this if you want to always read from beginning.
        // props.put("auto.offset.reset", "earliest");
        props.put("heartbeat.interval.ms", "3000");
        props.put("session.timeout.ms", "6001");
        props.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        return new KafkaConsumer<String, String>(props);
    }

    private static void processRecords(KafkaConsumer<String, String> consumer) {
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            long lastOffset = 0;
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, partition = %s, key = %s, value = %s\n", record.offset(), record.partition(),
                        record.key(), record.value());
                //offset = 56, key = 10, value = producer_msg10   lastOffset read: 56
                lastOffset = record.offset();
            }
            process();
        }
    }

    private static void process() {
        // create some delay to simulate processing of the message. 模拟业务逻辑
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

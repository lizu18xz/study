package com.fayayo.study.kafka.immocStudy;

import org.apache.kafka.clients.consumer.CommitFailedException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

/**
 * 消费方式:
 1-自动提交位移
 2-手动提交当前位移
 3-手动异步提交当前位移
 4-手动异步提交当前位移带回调
 5-混合同步与异步提交当前位移
 */
public class MyConsumer {

    private static KafkaConsumer<String, String> consumer;
    private static Properties properties;

    static {

        properties = new Properties();

        properties.put("bootstrap.servers", "127.0.0.1:9092");
        properties.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("group.id", "KafkaStudy");
    }

    private static void generalConsumeMessageAutoCommit() {

        //自动提交位移
        properties.put("enable.auto.commit", true);
        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Arrays.asList("test-topic"));

        try {
            while (true) {

                boolean flag = true;
                //核心代码,不断调用 拉取数据,超时时间,没有数据会等待多久,  会判断是否到自动提交的时间,如果是会在poll里面进行提交位移
                ConsumerRecords<String, String> records = consumer.poll(100);

                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(String.format(
                            "topic = %s, partition = %s, key = %s, value = %s",
                            record.topic(), record.partition(),
                            record.key(), record.value()
                    ));
                    if (record.value().equals("done")) {
                        flag = false;
                    }
                }

                if (!flag) {
                    break;
                }
            }
        } finally {
            consumer.close();
        }
    }

    private static void generalConsumeMessageSyncCommit() {

        properties.put("auto.commit.offset", false);
        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList("imooc-kafka-study-x"));

        while (true) {
            boolean flag = true;

            ConsumerRecords<String, String> records =
                    consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.println(String.format(
                        "topic = %s, partition = %s, key = %s, value = %s",
                        record.topic(), record.partition(),
                        record.key(), record.value()
                ));
                if (record.value().equals("done")) {
                    flag = false;
                }
            }

            try {
                consumer.commitSync();
            } catch (CommitFailedException ex) {
                System.out.println("commit failed error: "
                        + ex.getMessage());
            }

            if (!flag) {
                break;
            }
        }
    }

    private static void generalConsumeMessageAsyncCommit() {

        properties.put("auto.commit.offset", false);
        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList("imooc-kafka-study-x"));

        while (true) {
            boolean flag = true;

            ConsumerRecords<String, String> records =
                    consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.println(String.format(
                        "topic = %s, partition = %s, key = %s, value = %s",
                        record.topic(), record.partition(),
                        record.key(), record.value()
                ));
                if (record.value().equals("done")) {
                    flag = false;
                }
            }

            // commit A, offset 2000
            // commit B, offset 3000
            consumer.commitAsync();

            if (!flag) {
                break;
            }
        }
    }

    private static void generalConsumeMessageAsyncCommitWithCallback() {

        properties.put("auto.commit.offset", false);
        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList("imooc-kafka-study-x"));

        while (true) {
            boolean flag = true;

            ConsumerRecords<String, String> records =
                    consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.println(String.format(
                        "topic = %s, partition = %s, key = %s, value = %s",
                        record.topic(), record.partition(),
                        record.key(), record.value()
                ));
                if (record.value().equals("done")) {
                    flag = false;
                }
            }

            consumer.commitAsync((map, e) -> {
                if (e != null) {
                    System.out.println("commit failed for offsets: " +
                    e.getMessage());
                }
            });

            if (!flag) {
                break;
            }
        }
    }

    @SuppressWarnings("all")
    private static void mixSyncAndAsyncCommit() {

        properties.put("auto.commit.offset", false);
        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList("imooc-kafka-study-x"));

        try {

            while (true) {
                ConsumerRecords<String, String> records =
                        consumer.poll(100);

                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(String.format(
                            "topic = %s, partition = %s, key = %s, " +
                                    "value = %s",
                            record.topic(), record.partition(),
                            record.key(), record.value()
                    ));
                }

                consumer.commitAsync();
            }
        } catch (Exception ex) {
            System.out.println("commit async error: " + ex.getMessage());
        } finally {
            try {
                consumer.commitSync();
            } finally {
                consumer.close();
            }
        }
    }

    public static void main(String[] args) {
        generalConsumeMessageAutoCommit();
    }
}

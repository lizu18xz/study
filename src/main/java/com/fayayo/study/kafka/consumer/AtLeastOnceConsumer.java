package com.fayayo.study.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

/**
 * @author dalizu on 2018/11/6.
 * @version v1.0
 * @desc 至少消费一次，会重复消费  这种方式就是要手动在处理完该次poll得到消息之后，调用offset异步提交函数consumer.commitSync()。
 *       "enable.auto.commit", "false"
 */
public class AtLeastOnceConsumer {


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
        props.put("enable.auto.commit", "false");
        // Make Auto commit interval to a big number so that auto commit does not happen,
        // we are going to control the offset commit via consumer.commitSync(); after processing             // message.
        props.put("auto.commit.interval.ms", "999999999999");
        // This is how to control number of messages being read in each poll
        props.put("max.partition.fetch.bytes", "135");
        props.put("heartbeat.interval.ms", "3000");
        props.put("session.timeout.ms", "6001");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        return new KafkaConsumer<String, String>(props);
    }


    private static void processRecords(KafkaConsumer<String, String> consumer) {
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            long lastOffset = 0;
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("\n\roffset = %d, partition = %s, key = %s, value = %s\n", record.offset(), record.partition(),
                        record.key(), record.value());
                lastOffset = record.offset();
            }
            System.out.println("\n\r===========>lastOffset read: " + lastOffset);
            process();
            // Below call is important to control the offset commit. Do this call after you
            // finish processing the business process.
            consumer.commitSync();
        }
    }

    private static void process() {
        // create some delay to simulate processing of the record.
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}

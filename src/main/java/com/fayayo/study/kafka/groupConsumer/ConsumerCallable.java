package com.fayayo.study.kafka.groupConsumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

/**
 * @author dalizu on 2018/12/14.
 * @version v1.0
 * @desc 线程消费
 */
@Slf4j
public class ConsumerCallable implements Runnable {

    private Properties props;

    KafkaConsumer<String, String> consumer;

    public ConsumerCallable(Properties props) {
        this.props = props;
        consumer = new KafkaConsumer<String, String>(props);

    }


    @Override
    public void run() {

        while (true) {
            consumer.subscribe(Arrays.asList("test-topic"));
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {

                log.info(Thread.currentThread().getName()+" - offset = {}, partition = {}, key = {}, value = {}", record.offset(), record.partition(),
                        record.key(), record.value());

            }
            process();//模拟处理事件
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

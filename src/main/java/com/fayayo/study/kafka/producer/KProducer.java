package com.fayayo.study.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * @author dalizu on 2018/11/6.
 * @version v1.0
 * @desc 生产者
 */
public class KProducer {

    public static void main(String[] args) throws InterruptedException {
        try {

            Producer<String, String> producer = createProducer();
            sendMessages(producer);
            // Allow the producer to complete sending of the messages before program exit.
            Thread.sleep(2000);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static Producer<String, String> createProducer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.88.129:9092");
        props.put("acks", "all");//这意味着leader需要等待所有备份都成功写入日志，这种策略会保证只要有一个备份存活就不会丢失数据。这是最强的保证。
        props.put("retries", 0);
        // Controls how much bytes sender would wait to batch up before publishing to Kafka.
        //控制发送者在发布到kafka之前等待批处理的字节数。
        props.put("batch.size", 10);
        props.put("linger.ms", 1);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return new KafkaProducer(props);
    }

    private static void sendMessages(Producer<String, String> producer) {
        System.out.println("start send message......");
        String topic = "test-topic";
        int partition = 0;//指定分区发送
        long record = 1;
        for (int i = 1; i <= 10; i++) {
            producer.send(
                    new ProducerRecord<String, String>(topic, partition,
                            Long.toString(record),"producer_msg"+Long.toString(record++)));
        }
        System.out.println("start send message......end");
    }



}

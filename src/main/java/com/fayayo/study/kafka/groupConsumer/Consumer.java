package com.fayayo.study.kafka.groupConsumer;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author dalizu on 2018/12/14.
 * @version v1.0
 * @desc 多线程消费，消费组模式
 */
public class Consumer {

    public static void main(String[] args) throws InterruptedException {

        long start = System.currentTimeMillis();

        Properties properties = createProps();

        int poolSize = 1;

        ExecutorService executorService = Executors.newFixedThreadPool(poolSize, new ConsumerThreadFactory("consumer-pool-thread-"));


        //启动消费者
        for (int i = 0; i < poolSize; i++) {
            executorService.submit(new ConsumerCallable(properties));
        }


        while (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
            System.out.println("消费者正在消费数据...");
        }
        long end = System.currentTimeMillis();
        System.out.println("一共处理了【{}】" + (end - start));

    }

    private static Properties createProps() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.88.129:9092");
        String consumeGroup = "cg1";
        props.put("group.id", consumeGroup);//消费组模式
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
        return props;
    }

    static class ConsumerThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        ConsumerThreadFactory(String namePrefix) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            this.namePrefix = namePrefix;
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }

}

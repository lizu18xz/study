package com.fayayo.study.disruptor;

import com.fayayo.study.disruptor.core.RecordEvent;
import com.fayayo.study.disruptor.core.RecordEventFactory;
import com.fayayo.study.disruptor.core.RecordWorkHandler;
import com.fayayo.study.disruptor.storage.Storage;
import com.fayayo.study.disruptor.storage.impl.DefaultStorage;
import com.fayayo.study.middleware.elements.Record;
import com.fayayo.study.middleware.elements.StringColumn;
import com.fayayo.study.middleware.record.DefaultRecord;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import java.util.concurrent.Executors;

/**
 * @author dalizu on 2018/11/7.
 * @version v1.0
 * @desc 利用disruptor实现 缓冲区 channel
 */
public class DisruptorMiddleWare {


    public static void main(String[] args) {

        int producerCount=1;

        int writerParallelism=1;//消费者的格式

        final RecordWorkHandler[] handlers = new RecordWorkHandler[writerParallelism];
        //配置disruptior的  消费者
        for (int i = 0; i < writerParallelism; i++) {
            handlers[i] = new RecordWorkHandler("consumer"+i);
        }

        int bufferSize = 4096;

        //构建disruptor
        ProducerType producerType;
        if (producerCount == 1) {
            producerType = ProducerType.SINGLE;
        } else {
            producerType = ProducerType.MULTI;
        }
        Disruptor<RecordEvent> disruptor = new Disruptor<>(new RecordEventFactory(), bufferSize,
                Executors.defaultThreadFactory(), producerType,
                new YieldingWaitStrategy());
        Storage storage = new DefaultStorage(disruptor, handlers);

        //生产数据
        new Thread(new Runnable() {

            public void run() {
                //提交到线程池,进行读取数据
                producerData(storage);

                disruptor.shutdown();//关闭 disruptor，方法会堵塞，直至所有的事件都得到处理；
            }
        }).start();

    }

    private static void producerData(Storage storage) {
        for (int i=0;i<100000000;i++){
            StringColumn stringColumn=new StringColumn("test"+i);
            Record record=new DefaultRecord();
            record.addColumn(stringColumn);
            storage.put(record);
        }
    }

}

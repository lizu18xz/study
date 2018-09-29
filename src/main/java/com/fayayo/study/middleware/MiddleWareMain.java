package com.fayayo.study.middleware;

import com.fayayo.study.middleware.buffer.BufferRecordExchange;
import com.fayayo.study.middleware.channel.Channel;
import com.fayayo.study.middleware.channel.impl.MemoryChannel;
import com.fayayo.study.middleware.elements.Record;
import com.fayayo.study.middleware.elements.StringColumn;

/**
 * @author dalizu on 2018/9/28.
 * @version v1.0
 * @desc 测试启动类
 */
public class MiddleWareMain {


    public static void main(String[] args) {


        //定义内存通道
        Channel channel=new MemoryChannel();

        //启动生产者,生产数据

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("生产者开始产生数据......");

                producerData(new BufferRecordExchange(channel));

            }
        }).start();



        //消费者对数据进行消费

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("消费者开始消费数据......");

                consumerData(new BufferRecordExchange(channel));

            }
        }).start();

    }


    private static void producerData(BufferRecordExchange bufferRecordExchange) {

        for (int i=0;i<98;i++){
            StringColumn stringColumn=new StringColumn("test"+i);

            Record record=bufferRecordExchange.createRecord();
            record.addColumn(stringColumn);
            bufferRecordExchange.sendToWriter(record);
        }
        bufferRecordExchange.terminate();//结束标志
    }

    private static void consumerData(BufferRecordExchange bufferRecordExchange) {

        Record record=null;
        while ((record=bufferRecordExchange.getFromReader())!=null){

            System.out.println("获取到的数据:"+record.getColumn(0));

        }

        System.out.println("数据处理完成!!!!!!");
    }



}

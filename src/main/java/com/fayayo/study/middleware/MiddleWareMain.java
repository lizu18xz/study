package com.fayayo.study.middleware;

import com.fayayo.study.middleware.buffer.BufferRecordExchange;
import com.fayayo.study.middleware.channel.Channel;
import com.fayayo.study.middleware.channel.impl.MemoryChannel;
import com.fayayo.study.middleware.elements.Column;
import com.fayayo.study.middleware.elements.Record;
import com.fayayo.study.middleware.elements.StringColumn;

/**
 * @author dalizu on 2018/9/28.
 * @version v1.0
 * @desc 测试启动类
 *
 * 思考:   有多个生产者?(这种情况 相当于 多个reader  写数据到 writer 只要循环遍历reader 就可以)
 *        或者有多个消费者
 *        (此时应该 一个 reader --- writer 对应一个通道)
 *
 */
public class MiddleWareMain {


    public static void main(String[] args) {

        long startTime=System.nanoTime();
        //定义内存通道(生产者 和 消费者 要使用 一个内存通道)
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

                consumerData(new BufferRecordExchange(channel),startTime);

            }
        }).start();

    }


    private static void producerData(BufferRecordExchange bufferRecordExchange) {

        for (int i=0;i<100000000;i++){
            StringColumn stringColumn=new StringColumn("test"+i);

            Record record=bufferRecordExchange.createRecord();
            record.addColumn(stringColumn);
            bufferRecordExchange.sendToWriter(record);
        }
        bufferRecordExchange.terminate();//结束标志
    }

    private static void consumerData(BufferRecordExchange bufferRecordExchange,long startTime) {

        Record record=null;
        while ((record=bufferRecordExchange.getFromReader())!=null){
            Column column=record.getColumn(0);
            if(Column.Type.STRING.equals(column.getType())){

                String value=column.getRawData().toString();
                //System.out.println("获取到的数据:"+value);
            }
        }

        System.out.println("数据处理完成!!!!!!");
        long endTime=System.nanoTime();
        System.out.println("耗时:"+(endTime-startTime)/1000000000+" s");
    }



}

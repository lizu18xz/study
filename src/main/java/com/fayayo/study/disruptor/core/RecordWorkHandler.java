package com.fayayo.study.disruptor.core;

import com.fayayo.study.middleware.elements.Column;
import com.fayayo.study.middleware.elements.Record;
import com.lmax.disruptor.WorkHandler;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author dalizu on 2018/11/7.
 * @version v1.0
 * @desc 消费事件处理
 */
public class RecordWorkHandler implements WorkHandler<RecordEvent>{

    private String consumerId;

    private long startTime;

    private static final AtomicLong sums = new AtomicLong(0);

    public RecordWorkHandler(String consumerId) {
        this.consumerId = consumerId;
        startTime=System.nanoTime();
    }


    @Override
    public void onEvent(RecordEvent event) throws Exception {

        //System.out.println("当前消费者: " + consumerId+":处理数据");
        Record record=event.getRecord();

        for (int i=0;i<record.getColumnNumber();i++){

            Column column=record.getColumn(i);

            if(Column.Type.STRING.equals(column.getType())){

                String value=column.getRawData().toString();
                //System.out.println("处理数据:"+value);
            }
        }

        sums.incrementAndGet();
        if (sums.get() == 100000000) {
            long endTime = System.nanoTime();
            System.out.println("耗时:"+(endTime-startTime)/1000000000+" s");
        }
    }

}

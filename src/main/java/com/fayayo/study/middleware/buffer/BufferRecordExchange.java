package com.fayayo.study.middleware.buffer;

import com.fayayo.study.middleware.Constant;
import com.fayayo.study.middleware.channel.Channel;
import com.fayayo.study.middleware.channel.impl.MemoryChannel;
import com.fayayo.study.middleware.elements.Record;
import com.fayayo.study.middleware.record.DefaultRecord;
import com.fayayo.study.middleware.record.RecordReceiver;
import com.fayayo.study.middleware.record.RecordSender;
import com.fayayo.study.middleware.record.TerminateRecord;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dalizu on 2018/9/28.
 * @version v1.0
 * @desc 数据的缓冲区
 */
public class BufferRecordExchange implements RecordSender,RecordReceiver{


    private final Channel channel;

    private List<Record> buffer;//保存record数据

    private int bufferSize;//一定大小后提交一个批次

    private int bufferIndex = 0;

    private volatile boolean shutdown = false;//是否停止的标志


    public BufferRecordExchange(final Channel channel) {

        this.channel=channel;
        bufferSize= Constant.EXCHANGE_SIZE;
        buffer=new ArrayList<>(bufferSize);
    }


    @Override
    public Record createRecord() {
        return new DefaultRecord();
    }

    @Override
    public void sendToWriter(Record record) {

        Validate.notNull(record, "record不能为空.");

        //TODO 单条记录大小限制/一定大小后提交一个批次

        if(this.bufferIndex >= this.bufferSize){
            flush();
        }

        this.buffer.add(record);
        this.bufferIndex++;

        //TODO 计算总数据量大小
    }

    @Override
    public void flush() {
        //写入数据到内存
        this.channel.pushAll(this.buffer);
        this.buffer.clear();
        this.bufferIndex = 0;
    }

    @Override
    public void terminate() {

        flush();
        this.channel.pushTerminate(TerminateRecord.get());
    }



    //生产者和消费者是两个不同的BufferRecordExchange对象，所以不会有影响
    @Override
    public Record getFromReader() {

        boolean isEmpty = (this.bufferIndex >= this.buffer.size()); //为空的话就去拉去数据，陷入等待
        if (isEmpty) {
            receive();//批量获取，然后处理
        }

        //不为空的话会一条条取出
        Record record = this.buffer.get(this.bufferIndex++);
        if (record instanceof TerminateRecord) {//reader结束标志
            record = null;
        }
        return record;

    }


    private void receive() {
        this.channel.pullAll(this.buffer);//拉取一个批次的数据
        this.bufferIndex = 0;
        this.bufferSize = this.buffer.size();
        //System.out.println("获取数据完毕:条数:"+this.buffer.size());
    }


    @Override
    public void shutdown() {
        shutdown = true;
        try{
            buffer.clear();
            channel.clear();
        }catch(Throwable t){
            t.printStackTrace();
        }
    }




}

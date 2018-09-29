package com.fayayo.study.middleware.record;

import com.fayayo.study.middleware.elements.Record;

/**
 * @author dalizu on 2018/9/28.
 * @version v1.0
 * @desc
 */
public interface RecordReceiver {

    public Record createRecord();//创建一条数据

    public void sendToWriter(Record record);//发送一条数据到缓冲区

    public void flush();//真正写入数据到内存

    public void terminate();//发送终止信号

    public void shutdown();


}

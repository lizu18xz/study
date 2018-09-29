package com.fayayo.study.middleware.record;

import com.fayayo.study.middleware.elements.Record;

/**
 * @author dalizu on 2018/9/28.
 * @version v1.0
 * @desc 定义数据接口
 */
public interface RecordSender {

    public Record getFromReader();//获取一行数据

    public void shutdown();

}

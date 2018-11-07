package com.fayayo.study.disruptor.core;

import com.fayayo.study.middleware.elements.Record;

/**
 * @author dalizu on 2018/11/7.
 * @version v1.0
 * @desc 包装对象
 */
public class RecordEvent {

    private Record record;

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

}

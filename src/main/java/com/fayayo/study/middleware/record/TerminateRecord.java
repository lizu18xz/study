package com.fayayo.study.middleware.record;

import com.fayayo.study.middleware.elements.Column;
import com.fayayo.study.middleware.elements.Record;

/**
 * @author dalizu on 2018/9/28.
 * @version v1.0
 * @desc 终止记录 作为标示 生产者已经完成生产的标志
 */
public class TerminateRecord implements Record{

    private final static TerminateRecord SINGLE = new TerminateRecord();

    private TerminateRecord() {
    }

    public static TerminateRecord get() {
        return SINGLE;
    }

    @Override
    public void addColumn(Column column) {

    }

    @Override
    public void setColumn(int i, Column column) {

    }

    @Override
    public Column getColumn(int i) {
        return null;
    }

    @Override
    public int getColumnNumber() {
        return 0;
    }
}

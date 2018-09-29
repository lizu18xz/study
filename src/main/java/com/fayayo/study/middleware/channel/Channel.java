package com.fayayo.study.middleware.channel;

import com.fayayo.study.middleware.elements.Record;
import com.fayayo.study.middleware.record.TerminateRecord;
import org.apache.commons.lang3.Validate;

import java.util.Collection;

/**
 * @author dalizu on 2018/9/28.
 * @version v1.0
 * @desc 通道传输抽象类
 */
public abstract class Channel {

    protected int capacity;


    public Channel() {
        this.capacity=512;
    }


    public void push(final Record r) {
        Validate.notNull(r, "record不能为空.");
        this.doPush(r);
    }

    public void pushAll(final Collection<Record> rs) {
        //TODO 可以做一些限速的操作

        Validate.notNull(rs);
        Validate.noNullElements(rs);
        this.doPushAll(rs);

    }

    public Record pull() {
        Record record = this.doPull();
        return record;
    }

    public void pullAll(final Collection<Record> rs) {

        Validate.notNull(rs);
        this.doPullAll(rs);

    }

    public void pushTerminate(final TerminateRecord r) {
        Validate.notNull(r, "record不能为空.");
        this.doPush(r);
    }

    //定义抽象方法
    protected abstract void doPush(Record r);

    protected abstract void doPushAll(Collection<Record> rs);

    protected abstract Record doPull();

    protected abstract void doPullAll(Collection<Record> rs);

    public abstract void clear();


    //get set
    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

}

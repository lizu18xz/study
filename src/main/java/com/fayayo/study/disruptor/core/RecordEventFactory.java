package com.fayayo.study.disruptor.core;

import com.lmax.disruptor.EventFactory;

/**
 * @author dalizu on 2018/11/7.
 * @version v1.0
 * @desc
 */
public class RecordEventFactory implements EventFactory<RecordEvent> {


    @Override
    public RecordEvent newInstance() {
        return new RecordEvent();
    }
}

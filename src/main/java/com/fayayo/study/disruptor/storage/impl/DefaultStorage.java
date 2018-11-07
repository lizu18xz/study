package com.fayayo.study.disruptor.storage.impl;

import com.fayayo.study.disruptor.core.RecordEvent;
import com.fayayo.study.disruptor.core.RecordEventExceptionHandler;
import com.fayayo.study.disruptor.core.RecordWorkHandler;
import com.fayayo.study.disruptor.storage.Storage;
import com.fayayo.study.middleware.elements.Record;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

/**
 * @author dalizu on 2018/11/7.
 * @version v1.0
 * @desc
 */
public class DefaultStorage implements Storage {


    private final Disruptor<RecordEvent> disruptor;
    private final RingBuffer<RecordEvent> ringBuffer;


    public DefaultStorage(Disruptor<RecordEvent> disruptor, RecordWorkHandler[] handlers) {
        this.disruptor = disruptor;
        disruptor.setDefaultExceptionHandler(new RecordEventExceptionHandler(disruptor));
        disruptor.handleEventsWithWorkerPool(handlers);
        ringBuffer = disruptor.start();
    }


    private static final EventTranslatorOneArg<RecordEvent, Record> TRANSLATOR = new EventTranslatorOneArg<RecordEvent, Record>() {

        @Override
        public void translateTo(RecordEvent event, long sequence, Record record) {
            event.setRecord(record);
        }
    };

    @Override
    public void put(Record record) {
        disruptor.publishEvent(TRANSLATOR, record);
    }

    @Override
    public void put(Record[] records) {
        for (Record record : records) {
            put(record);
        }
    }

    @Override
    public boolean isEmpty() {
        return ringBuffer.remainingCapacity() == ringBuffer.getBufferSize();
    }

    @Override
    public int size() {
        return ringBuffer.getBufferSize();
    }

    @Override
    public void close() {
        disruptor.shutdown();
    }
}

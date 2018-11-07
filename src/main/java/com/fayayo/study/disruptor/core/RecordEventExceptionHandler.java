package com.fayayo.study.disruptor.core;

import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.dsl.Disruptor;

public class RecordEventExceptionHandler implements ExceptionHandler<Object> {

    private final Disruptor<RecordEvent> disruptor;

    public RecordEventExceptionHandler(Disruptor<RecordEvent> disruptor) {
        this.disruptor = disruptor;
    }

    public void handleEventException(Throwable t, long sequence, Object event) {
        disruptor.shutdown();
    }

    public void handleOnShutdownException(Throwable t) {
        disruptor.shutdown();
    }

    public void handleOnStartException(Throwable t) {
        disruptor.shutdown();
    }
}

package com.fayayo.study.hadoop;

import org.apache.hadoop.yarn.event.Dispatcher;

/**
 * @author dalizu on 2019/1/25.
 * @version v1.0
 * @desc
 */
public class MasterContext {

    private Dispatcher dispatcher;

    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }
}

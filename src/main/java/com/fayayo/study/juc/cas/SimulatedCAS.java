package com.fayayo.study.juc.cas;

/**
 * @author dalizu on 2020/2/20.
 * @version v1.0
 * @desc 模拟CAS
 */
public class SimulatedCAS {


    private volatile int value;


    public synchronized int compareAndSwap(int expect,int newValue){

        int oldValue=value;
        if(oldValue==expect){
            value=newValue;
        }

        return oldValue;
    }






}

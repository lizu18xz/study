package com.fayayo.study.lock.ch3;

import com.fayayo.study.lock.BaseChannel;

/**
 * @author dalizu on 2018/12/10.
 * @version v1.0
 * @desc 条件队列
 * 条件队列可以让一组线程（叫做：等待集wait set）以某种方式等待相关条件变为真
 * 因为会有多个线程因为不同的原因在同一个条件队列中等待，因此，用notify而不用notifyAll是危险的
 */
public class MChannel<E> extends BaseChannel<E> {


    public MChannel(int capacity) {
        super(capacity);
    }


    public synchronized void put(E e) throws RuntimeException, InterruptedException {

        while (isFull()){
            wait();
        }

        doPut(e);
        notifyAll();

    }
    public synchronized E take() throws RuntimeException, InterruptedException {

        while (isEmpty()){
            wait();
        }

        E e=doTake();
        notifyAll();

        return e;
    }

}

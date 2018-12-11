package com.fayayo.study.lock.ch1;

import com.fayayo.study.lock.BaseChannel;

/**
 * @author dalizu on 2018/12/10.
 * @version v1.0
 * @desc 如果满了 直接抛出异常,但是 数组的内容在多线程环境下 是不断变化的。因此此方式不友好
 */
public class MChannel<E> extends BaseChannel<E> {


    public MChannel(int capacity) {
        super(capacity);
    }


    public synchronized void put(E e)throws RuntimeException{
        if(isFull()){//存的时候，如果是满的，就抛异常
            throw new RuntimeException();
        }
        doPut(e);
    }
    public synchronized E take()throws RuntimeException{
        if(isEmpty()){//取的时候，如果是空的，就抛异常
            throw new RuntimeException();
        }
        return doTake();
    }

}

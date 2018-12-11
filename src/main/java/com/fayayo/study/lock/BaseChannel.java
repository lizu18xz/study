package com.fayayo.study.lock;

/**
 * @author dalizu on 2018/12/10.
 * @version v1.0
 * @desc 基类
 */
public abstract class BaseChannel<E> {

    private final E[] buff;

    private int tail;
    private int head;
    private int size;//当前元素个数

    public BaseChannel(int capacity) {
        buff= (E[]) new Object[capacity];
    }


    //新增数据
    protected synchronized final void doPut(E e){
        buff[tail] = e;
        tail++;
        if(tail == buff.length){
            tail = 0;
        }
        size++;
    }

    //取出数据
    protected synchronized final E doTake(){
        E e = buff[head];
        buff[head] = null;
        head++;
        if(head == buff.length){
            head = 0;
        }
        size--;
        return e;
    }

    protected synchronized final boolean isFull(){//是否是满的
        return size == buff.length;
    }
    protected synchronized final boolean isEmpty(){//是否是空的
        return size == 0;
    }


}

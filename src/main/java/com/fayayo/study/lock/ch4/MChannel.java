package com.fayayo.study.lock.ch4;

import com.fayayo.study.lock.BaseChannel;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author dalizu on 2018/12/10.
 * @version v1.0
 * @desc lock
 */
public class MChannel<T> {

    private Lock lock=new ReentrantLock();//默认非公平

    private Condition notEmpty=lock.newCondition();//不为空

    private Condition notFull=lock.newCondition();//不满

    private final T[]items= (T[]) new Object[100];

    private int head,tail,size;

    public void put(T t){

        lock.lock();

        try {

            //条件判断
            while (size==items.length){
                notFull.await();//等待非满
            }

            items[tail] = t;
            tail ++;
            if(tail == items.length){
                tail = 0;
            }
            size++;
            notEmpty.signalAll(); //唤醒take阻塞的线程

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }

    }


    public T take(){

        lock.lock();

        try {
           while (size==0){
               notEmpty.await();//等待非空
           }

           T t=items[head];
           items[head]=null;
           head++;
           if(head==items.length){
              head=0;
           }
           size--;
           notFull.signalAll();//唤醒put操作
           return t;

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        return null;
    }

}

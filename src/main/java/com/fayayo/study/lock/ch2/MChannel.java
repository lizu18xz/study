package com.fayayo.study.lock.ch2;

import com.fayayo.study.lock.BaseChannel;

/**
 * @author dalizu on 2018/12/10.
 * @version v1.0
 * @desc 休眠时间不好控制，性能不佳
 */
public class MChannel<E> extends BaseChannel<E> {


    public MChannel(int capacity) {
        super(capacity);
    }


    public  void put(E e) throws  InterruptedException {

        while (true){
            synchronized (this){
                if(!isFull()){
                    doPut(e);
                    return;
                }
            }
            //如果是满的就暂停1秒，然后重试
            Thread.sleep(1000);
        }

    }
    public  E take() throws InterruptedException {
        while(true){
            synchronized(this){
                if(!isEmpty()){//如果不是空的，就可以取
                    return doTake();
                }
            }
            //如果是空的，休眠1秒钟，重试
            Thread.sleep(1000);
        }
    }

}

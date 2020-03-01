package com.fayayo.study.juc.aqs;

import java.util.concurrent.locks.AbstractQueuedLongSynchronizer;

/**
 * @author dalizu on 2020/3/1.
 * @version v1.0
 * @desc  实现一个简单的线程协作器
 */
public class OneShotLatch {

    private final Sync sync=new Sync();

    //获取
    public void await(){
        sync.acquireShared(0);
    }


    //释放
    public void signal(){
        sync.releaseShared(0);
    }


    private class Sync extends AbstractQueuedLongSynchronizer{

        //具体实现自己逻辑的方法
        @Override
        protected long tryAcquireShared(long arg) {
            return (getState()==1)?1:-1;
        }


        @Override
        protected boolean tryReleaseShared(long arg) {
            setState(1);
            return true;
        }
    }


    public static void main(String[] args) throws InterruptedException {

        OneShotLatch oneShotLatch=new OneShotLatch();

        for (int i=0;i<10;i++){

            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName()+" 尝试获取latch");

                    oneShotLatch.await();

                    System.out.println(Thread.currentThread().getName()+" 开闸继续运行");

                }
            }).start();

        }


        Thread.sleep(5000);


        oneShotLatch.signal();

    }

}

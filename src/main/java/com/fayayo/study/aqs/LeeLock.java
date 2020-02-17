package com.fayayo.study.aqs;

import java.util.concurrent.locks.AbstractQueuedLongSynchronizer;

/**
 * @author dalizu on 2020/2/17.
 * @version v1.0
 * @desc
 */
public class LeeLock {

    private static class Sync extends AbstractQueuedLongSynchronizer{


        @Override
        protected boolean tryAcquire(long arg) {
            return compareAndSetState(0,1);
        }


        @Override
        protected boolean tryRelease(long arg) {
            setState(0);
            return true;
        }

        @Override
        protected boolean isHeldExclusively () {
            return getState() == 1;
        }

        /*protected boolean isHeldExclusively()
        该线程是否正在独占资源。只有用到Condition才需要去实现它。*/
    }

    private Sync sync = new Sync();

    public void lock () {
        sync.acquire(1);
    }

    public void unlock () {
        sync.release(1);
    }



    //测试:

    static int count = 0;
    static LeeLock leeLock = new LeeLock();

    public static void main(String[] args) throws InterruptedException {

        Runnable runnable = new Runnable() {
            @Override
            public void run () {
                try {
                    leeLock.lock();
                    for (int i = 0; i < 10000; i++) {
                        count++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    leeLock.unlock();
                }

            }
        };
        Thread thread1 = new Thread(runnable);
        Thread thread2 = new Thread(runnable);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println(count);

    }

}

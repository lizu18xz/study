package com.fayayo.study.juc.cas;

/**
 * @author dalizu on 2020/2/20.
 * @version v1.0
 * @desc
 */
public class TowThreadsCompetitiom implements Runnable{

    private volatile int value;


    public synchronized int compareAndSwap(int expect,int newValue){

        int oldValue=value;
        if(oldValue==expect){
            value=newValue;
        }

        return oldValue;
    }


    @Override
    public void run() {

        compareAndSwap(0,1);

    }


    public static void main(String[] args) throws InterruptedException {

        TowThreadsCompetitiom r=new TowThreadsCompetitiom();
        r.value=0;

        Thread t1=new Thread(r);
        Thread t2=new Thread(r);

        t1.start();
        t2.start();

        t1.join();

        t2.join();

        System.out.println(r.value);

    }

}

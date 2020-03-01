package com.fayayo.study.juc.blockqueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author dalizu on 2020/2/26.
 * @version v1.0
 * @desc
 */
public class ArrayBlockQueueDemo {



    public static void main(String[] args) {
        final ArrayBlockingQueue<String> queue=new ArrayBlockingQueue(1);
        Interviewer ri= new Interviewer(queue);


        Consumer c1=new Consumer(queue);

        new Thread(ri).start();
        new Thread(c1).start();
    }



}

class Interviewer implements Runnable{
    BlockingQueue <String>blockingQueue;

    public Interviewer(BlockingQueue blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {

        System.out.println("10个人来了");

        for (int i=0;i<10;i++){
            String candidate="man"+i;
            try {
                blockingQueue.put(candidate);
                System.out.println("安排好了"+candidate);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        try {
            blockingQueue.put("stop");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


class Consumer implements Runnable{
    BlockingQueue<String> blockingQueue;

    public Consumer(BlockingQueue blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String msg;
        try {
            while (!(msg= blockingQueue.take()).equals("stop")){

                    System.out.println(msg+"到了");
            }
            System.out.println("结束");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println();

    }
}
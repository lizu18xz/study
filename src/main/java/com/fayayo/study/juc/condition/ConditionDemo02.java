package com.fayayo.study.juc.condition;

import java.util.PriorityQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author dalizu on 2020/2/26.
 * @version v1.0
 * @desc
 */
public class ConditionDemo02 {

    private int queueSize=10;

    private  PriorityQueue<Integer>queue=new PriorityQueue<Integer>();

    private ReentrantLock lock=new ReentrantLock();

    private Condition notFull=lock.newCondition();

    private Condition notEmpty=lock.newCondition();


    public static void main(String[] args) {
        ConditionDemo02 conditionDemo02=new ConditionDemo02();




    }


    class Consumer extends Thread{


        @Override
        public void run() {

            consume();
        }


        private void consume(){
            while (true){
               lock.lock();
                try {
                    while (queue.size()==0){
                        System.out.println("队列空,等待数据");
                        try {
                            notEmpty.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    queue.poll();
                    notFull.signal();
                    System.out.println("从队列取出一个数据,剩余:"+queue.size());
                }finally {
                    lock.unlock();
                }
            }
        }

    }

    class Producer extends Thread{


        @Override
        public void run() {

            producer();
        }


        private void producer(){
            while (true){
                lock.lock();
                try {
                    while (queue.size()==queueSize){
                        System.out.println("队列满了,等待消费");
                        try {
                            notFull.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    queue.offer(1);
                    notEmpty.signal();
                    System.out.println("从队列插入一个数据,剩余:"+(queueSize-queue.size()));
                }finally {
                    lock.unlock();
                }
            }
        }

    }



}

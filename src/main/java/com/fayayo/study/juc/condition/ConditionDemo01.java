package com.fayayo.study.juc.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author dalizu on 2020/2/26.
 * @version v1.0
 * @desc
 */
public class ConditionDemo01 {

    private ReentrantLock lock=new ReentrantLock();

    private Condition condition=lock.newCondition();


    void method1() throws InterruptedException {


        lock.lock();

        try {

            System.out.println("条件不满足,开始await");

            condition.await();

            System.out.println("条件满足,开始执行后续任务");
        }finally {
            lock.unlock();
        }


    }



    void method2() throws InterruptedException {

        lock.lock();

        try {

            System.out.println("准备完成,唤醒其他线程");

            condition.signal();


        }finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {

        ConditionDemo01 conditionDemo01=new ConditionDemo01();

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    conditionDemo01.method2();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();

        conditionDemo01.method1();


    }

}

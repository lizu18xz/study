package com.fayayo.study.juc.aqs;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author dalizu on 2020/3/1.
 * @version v1.0
 * @desc
 */
public class AQSDemo {


    public static void main(String[] args) {


        new Semaphore(3);

        new CountDownLatch(1);

        new ReentrantLock();

    }


}

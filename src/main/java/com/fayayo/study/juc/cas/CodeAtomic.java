package com.fayayo.study.juc.cas;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author dalizu on 2020/2/20.
 * @version v1.0
 * @desc
 */
public class CodeAtomic {

    public static void main(String[] args) {

        AtomicInteger atomicInteger=new AtomicInteger();
        atomicInteger.getAndAdd(1);

    }

}

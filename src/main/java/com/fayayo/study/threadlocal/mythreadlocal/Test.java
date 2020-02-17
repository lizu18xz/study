package com.fayayo.study.threadlocal.mythreadlocal;

/**
 * @author dalizu on 2020/2/17.
 * @version v1.0
 * @desc
 */
public class Test {

    static MyThreadLocal<Long>v =new MyThreadLocal<Long>(){

        @Override
        protected Long initialValue(){
            return Thread.currentThread().getId();
        }

    };



    public static void main(String[] args) {

        for (Integer i=0;i<100;i++){
            new Thread(()->{

                System.out.println(v.get());

            }).start();
        }

    }

}

package com.fayayo.study.juc.cache.compute;

/**
 * @author dalizu on 2020/3/1.
 * @version v1.0
 * @desc 耗时计算类
 */
public class ExpensiveFunction implements Computable<String,Integer>{


    @Override
    public Integer compute(String arg) throws Exception {

        Thread.sleep(5000);

        return new Integer(arg);
    }
}

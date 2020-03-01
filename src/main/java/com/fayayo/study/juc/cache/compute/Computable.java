package com.fayayo.study.juc.cache.compute;

/**
 * @author dalizu on 2020/3/1.
 * @version v1.0
 * @desc 有一个计算函数,用来代表耗时计算
 */
public interface Computable<A,V> {


    V compute(A arg) throws Exception;


}

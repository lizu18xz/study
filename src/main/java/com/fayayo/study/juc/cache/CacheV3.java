package com.fayayo.study.juc.cache;

import com.fayayo.study.juc.cache.compute.Computable;
import com.fayayo.study.juc.cache.compute.ExpensiveFunction;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dalizu on 2020/3/1.
 * @version v1.0
 * @desc 装饰者模式
 */
public class CacheV3<A,V> implements Computable<A,V>{

    //final
    private final ConcurrentHashMap<A,V> cache=new ConcurrentHashMap<>();


    private final Computable<A,V> c;

    public CacheV3(Computable<A, V> computable) {
        this.c = computable;
    }

    /**
     * 锁优化
     *
     * synchronized (this){
     cache.put(arg,result);
     } 这种方式也不能保证HashMap并发安全
     * */
    @Override
    public  V compute(A arg) throws Exception {
        System.out.println("进入缓存机制");
        V result=cache.get(arg);
        if(result==null){

            //如果计算时间久,第二个线程可能也会进来进行计算,重复计算
            result =c.compute(arg);
            cache.put(arg,result);
        }
        return result;
    }


    public static void main(String[] args) throws Exception {

        CacheV3<String,Integer> cache=new CacheV3<>(new ExpensiveFunction());

        cache.compute("666");

        //...


    }


}

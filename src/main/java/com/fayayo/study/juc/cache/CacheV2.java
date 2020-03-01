package com.fayayo.study.juc.cache;

import com.fayayo.study.juc.cache.compute.Computable;
import com.fayayo.study.juc.cache.compute.ExpensiveFunction;

import java.util.HashMap;

/**
 * @author dalizu on 2020/3/1.
 * @version v1.0
 * @desc 装饰者模式
 */
public class CacheV2<A,V> implements Computable<A,V>{

    //final
    private final HashMap<A,V> cache=new HashMap<>();


    private final Computable<A,V> c;

    public CacheV2(Computable<A, V> computable) {
        this.c = computable;
    }

    /**
     * synchronized性能差，阻塞其他线程
     * */
    @Override
    public synchronized V compute(A arg) throws Exception {
        System.out.println("进入缓存机制");

        V result=cache.get(arg);
        if(result==null){
            result =c.compute(arg);
            cache.put(arg,result);
        }
        return result;
    }


    public static void main(String[] args) throws Exception {

        CacheV2<String,Integer>cache=new CacheV2<>(new ExpensiveFunction());

        cache.compute("666");

        //...


    }


}

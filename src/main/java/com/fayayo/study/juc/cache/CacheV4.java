package com.fayayo.study.juc.cache;

import com.fayayo.study.juc.cache.compute.Computable;
import com.fayayo.study.juc.cache.compute.ExpensiveFunction;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * @author dalizu on 2020/3/1.
 * @version v1.0
 * @desc 利用Future,避免重复计算
 */
public class CacheV4<A,V> implements Computable<A,V>{

    //final
    private final ConcurrentHashMap<A,Future<V>> cache=new ConcurrentHashMap<>();


    private final Computable<A,V> c;

    public CacheV4(Computable<A, V> computable) {
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

        //如果两个线程同时调用此方法,那么此时就会有问题
        Future<V>f=cache.get(arg);
        if(f == null){
            Callable callable = new Callable<V>(){
                @Override
                public V call() throws Exception {
                    return c.compute(arg);
                }
            };

            FutureTask ft=new FutureTask<>(callable);
            f=ft;
            //计算之前先加入缓存
            cache.put(arg,ft);
            //执行计算
            System.out.println("从FutureTask调用计算逻辑");
            ft.run();
        }

        return f.get();
    }


    public static void main(String[] args) throws Exception {

        CacheV4<String,Integer> cache=new CacheV4<>(new ExpensiveFunction());

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Integer result=cache.compute("666");
                    System.out.println("第一次计算结果:"+result);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();

        //...

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Integer result= cache.compute("666");
                    System.out.println("第2次计算结果:"+result);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

}

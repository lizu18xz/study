package com.fayayo.study.juc.cache;

import com.fayayo.study.juc.cache.compute.Computable;
import com.fayayo.study.juc.cache.compute.ExpensiveFunction;
import com.fayayo.study.juc.cache.compute.MyFail;

import java.util.concurrent.*;

/**
 * @author dalizu on 2020/3/1.
 * @version v1.0
 * @desc 利用Future,避免重复计算
 */
public class CacheV6<A,V> implements Computable<A,V>{

    //final
    private final ConcurrentHashMap<A,Future<V>> cache=new ConcurrentHashMap<>();


    private final Computable<A,V> c;

    public CacheV6(Computable<A, V> computable) {
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
    public  V compute(A arg) throws InterruptedException, ExecutionException {

        while (true){
            //如果两个线程同时调用此方法,那么此时就会有问题,使用putIfAbsent解决
            Future<V>f=cache.get(arg);
            if(f == null){
                Callable callable = new Callable<V>(){
                    @Override
                    public V call() throws Exception {
                        return c.compute(arg);
                    }
                };

                FutureTask ft=new FutureTask<>(callable);

                //原子操作
                f=cache.putIfAbsent(arg,ft);
                if(f==null){
                    f=ft;
                    //执行计算
                    System.out.println("从FutureTask调用计算逻辑");
                    ft.run();
                }
            }

            try {
                return f.get();
            } catch (CancellationException e){
                System.out.println("被取消了");
                cache.remove(arg);
                throw e;
            }catch (InterruptedException e) {
                cache.remove(arg);
                throw e;
            } catch (ExecutionException e) {
                e.printStackTrace();
                //重试
                System.out.println("计算错误,需要重试");
                cache.remove(arg);
            }
        }

    }


    public static void main(String[] args) throws Exception {

        CacheV6<String,Integer> cache=new CacheV6<>(new MyFail());

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

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Integer result= cache.compute("777");
                    System.out.println("第3次计算结果:"+result);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();




    }

}

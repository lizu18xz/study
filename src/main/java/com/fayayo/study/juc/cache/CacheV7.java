package com.fayayo.study.juc.cache;

import com.fayayo.study.juc.cache.compute.Computable;
import com.fayayo.study.juc.cache.compute.ExpensiveFunction;
import com.fayayo.study.juc.cache.compute.MyFail;

import java.util.concurrent.*;

/**
 * @author dalizu on 2020/3/1.
 * @version v1.0
 * @desc 处于安全性, 缓存需要设置有效期, 自动失效
 */
public class CacheV7<A, V> implements Computable<A, V> {

    //final
    private final ConcurrentHashMap<A, Future<V>> cache = new ConcurrentHashMap<>();


    private final Computable<A, V> c;

    public CacheV7(Computable<A, V> computable) {
        this.c = computable;
    }

    /**
     * 锁优化
     * <p>
     * synchronized (this){
     * cache.put(arg,result);
     * } 这种方式也不能保证HashMap并发安全
     */
    @Override
    public V compute(A arg) throws InterruptedException, ExecutionException {

        while (true) {
            //如果两个线程同时调用此方法,那么此时就会有问题,使用putIfAbsent解决
            Future<V> f = cache.get(arg);
            if (f == null) {
                Callable callable = new Callable<V>() {
                    @Override
                    public V call() throws Exception {
                        return c.compute(arg);
                    }
                };

                FutureTask ft = new FutureTask<>(callable);

                //原子操作
                f = cache.putIfAbsent(arg, ft);
                if (f == null) {
                    f = ft;
                    //执行计算
                    System.out.println("从FutureTask调用计算逻辑");
                    ft.run();
                }
            }

            try {
                return f.get();
            } catch (CancellationException e) {
                System.out.println("被取消了");
                cache.remove(arg);
                throw e;
            } catch (InterruptedException e) {
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


    public final static ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);



    //随机缓存
    public V computeRandomExpire(A arg) throws ExecutionException, InterruptedException {
        long randomExpire= (long) (Math.random()*10000);
        return compute(arg,randomExpire);
    }


    public V compute(A arg, long expire) throws ExecutionException, InterruptedException {
        if (expire > 0) {

            executor.schedule(new Runnable() {
                @Override
                public void run() {

                    expire(arg);

                }
            }, expire, TimeUnit.MILLISECONDS);

        }

        return compute(arg);
    }

    private synchronized void expire(A key) {
        Future<V> future = cache.get(key);
        if (future != null) {
            if (!future.isDone()) {
                System.out.println("Future任务被取消");
                future.cancel(true);
            }
            System.out.println("过期时间到,缓存被清除");
            cache.remove(key);
        }

    }


    public static void main(String[] args) throws Exception {

        CacheV7<String, Integer> cache = new CacheV7<>(new ExpensiveFunction());

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Integer result = cache.compute("666");
                    System.out.println("第一次计算结果:" + result);
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
                    Integer result = cache.compute("666", 5000L);
                    System.out.println("第2次计算结果:" + result);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Integer result = cache.compute("777");
                    System.out.println("第3次计算结果:" + result);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();


        Thread.sleep(6000);
        Integer result = cache.compute("666");
        System.out.println("主线程第N次计算结果:" + result);

    }

}

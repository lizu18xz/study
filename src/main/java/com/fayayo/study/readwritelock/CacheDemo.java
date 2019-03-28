package com.fayayo.study.readwritelock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author dalizu on 2019/3/11.
 * @version v1.0
 * @desc
 */
public class CacheDemo {

    private  Map<String, Object> map = new HashMap<>(128);

    private  ReadWriteLock lock = new ReentrantReadWriteLock();

    public static void main(String[] args) {


    }


    public Object get(String id){

        Object value = null;
        lock.readLock().lock();//首先开启读锁，从缓存中去取
        try{
            value=map.get(id);
            if(value == null){  //如果缓存中没有,释放读锁，上写锁
                lock.readLock().unlock();
                lock.writeLock().lock();
                try{
                    if(value == null){ //防止多写线程重复查询赋值
                        value = "redis-value";  //此时可以去数据库中查找，这里简单的模拟一下
                    }
                    lock.readLock().lock(); //加读锁降级写锁
                }finally{
                    lock.writeLock().unlock(); //释放写锁
                }
            }
        }finally{
            lock.readLock().unlock(); //最后释放读锁
        }

        return value;
    }


}

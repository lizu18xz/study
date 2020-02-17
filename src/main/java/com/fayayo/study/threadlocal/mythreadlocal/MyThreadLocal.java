package com.fayayo.study.threadlocal.mythreadlocal;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author dalizu on 2020/2/17.
 * @version v1.0
 * @desc 问题:hashMap中直接存储了MyThreadLocal的引用，导致内存无法回收？
 * 可以使用整数ID替换
 */
public class MyThreadLocal<T> {

    //TODO使用整数ID替换MyThreadLocal
    static AtomicInteger atomic=new AtomicInteger();
    //哈希函数好，分散均匀
    Integer threadLocalHash=atomic.addAndGet(0x61c88647);



    static HashMap<Thread,HashMap<MyThreadLocal<?>,Object>>threadHashMapHashMap=new HashMap<>();

    synchronized static HashMap<MyThreadLocal<?>,Object>getMap(){
        Thread thread=Thread.currentThread();
        if(!threadHashMapHashMap.containsKey(thread)){
            threadHashMapHashMap.put(thread,new HashMap<>());
        }
        return threadHashMapHashMap.get(thread);
    }


    protected T initialValue(){
        return null;
    }

    //将this(MyThreadLocal对象)当做key
    public T get(){
        HashMap<MyThreadLocal<?>,Object> map=getMap();
        if(!map.containsKey(this)){
            map.put(this,initialValue());
        }
        return (T)map.get(this);
    }

    public void set(T v){
        HashMap<MyThreadLocal<?>,Object> map=getMap();

        map.put(this,v);

    }


}

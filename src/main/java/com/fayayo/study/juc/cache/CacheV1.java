package com.fayayo.study.juc.cache;

import java.util.HashMap;

/**
 * @author dalizu on 2020/3/1.
 * @version v1.0
 * @desc 并发情况不行,代码复用不行
 */
public class CacheV1 {

    //final
    private final HashMap<String,Integer>cache=new HashMap<>();


    private Integer computer(String userId) throws InterruptedException {
        Integer result=cache.get(userId);
        if(result == null){
            result=doCompute(userId);
            cache.put(userId,result);
        }
        return result;
    }


    private Integer doCompute(String userId) throws InterruptedException {
        System.out.println("开始计算了");
        Thread.sleep(5000);
        return new Integer(userId);
    }

}

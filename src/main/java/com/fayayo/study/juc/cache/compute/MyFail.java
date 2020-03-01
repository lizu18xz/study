package com.fayayo.study.juc.cache.compute;

import java.io.IOException;

/**
 * @author dalizu on 2020/3/1.
 * @version v1.0
 * @desc 耗时计算实现类,有概率计算失败
 */
public class MyFail implements Computable<String,Integer> {


    @Override
    public Integer compute(String arg) throws Exception {

        double random=Math.random();
        if(random > 0.5){
            throw new IOException("读取文件出错");
        }

        Thread.sleep(3000);

        return Integer.valueOf(arg);
    }
}

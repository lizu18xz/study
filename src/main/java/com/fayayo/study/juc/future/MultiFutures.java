package com.fayayo.study.juc.future;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author dalizu on 2020/3/1.
 * @version v1.0
 * @desc
 */
public class MultiFutures {

    public static void main(String[] args) {

      ExecutorService service= Executors.newFixedThreadPool(2);


        ArrayList <Future>futures=new ArrayList<>();

        for (int i=0;i<20;i++){
            Future<Integer>future=service.submit(new CallableTask());
            futures.add(future);
        }




    }


    static class CallableTask implements Callable<Integer>{


        @Override
        public Integer call() throws Exception {
            Thread.sleep(3000);
            return new Random().nextInt();
        }
    }


}

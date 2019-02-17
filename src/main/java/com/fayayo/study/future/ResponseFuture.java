package com.fayayo.study.future;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author dalizu on 2019/2/15.
 * @version v1.0
 * @desc 基于Future实现
 *
 * Future可以看成线程在执行时留给调用者的一个存根，通过这个存在可以进行查看线程执行状态(isDone)、
 * 取消执行(cancel)、阻塞等待执行完成并返回结果(get)、异步执行回调函数(callback)等操作。
 */
public class ResponseFuture implements Future<String>{


    private final ResponseCallBack responseCallBack;

    private String responsed;

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();


    public ResponseFuture(ResponseCallBack responseCallBack) {
        this.responseCallBack = responseCallBack;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCancelled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDone() {
        return null != this.responsed;
    }


    // 返回完成
    public void done(String responsed) throws Exception{
        this.responsed = responsed;
        try {
            this.lock.lock();

            System.out.println("done lock...");

            this.condition.signal();

            if(null != this.responseCallBack)
                this.responseCallBack.call(this.responsed);


        } finally {
            //如果不释放，其他会获取不到
            this.lock.unlock();
            System.out.println("done unlock...");
        }
    }


    @Override
    public String get() throws InterruptedException, ExecutionException {

        if(!isDone()){

            try {

                this.lock.lock();
                System.out.println("get lock...");
                while (!isDone()){
                    condition.await();//当调用condition.await()方法后会使得当前获取lock的线程进入到等待队列

                    if (this.isDone()) break;
                }

            }finally {
                this.lock.unlock();
                System.out.println("get unlock...");
            }

        }
        return this.responsed;
    }

    @Override
    public String get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        throw new UnsupportedOperationException();
    }


    public static void main(String[] args) {
        final ResponseFuture responseFuture = new ResponseFuture(new ResponseCallBack());

        new Thread(new Runnable() {// 请求线程
            public void run() {
                System.out.println("发送一个同步请求");
                try {
                    //如果不使用get 就是异步 send() 方法默认则是异步的，只要不手动调用 get() 方法,
                    //但这样就没法获知发送结果。为此需要一个callBack方法
                    System.out.println(responseFuture.get()); //放开这句，就是同步获取数据
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        new Thread(new Runnable() {// 请求线程
            public void run() {
                try {
                    Thread.sleep(2000);// 模拟处理一会
                    responseFuture.done("ok");// 处理完成,会调用回调函数
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


}

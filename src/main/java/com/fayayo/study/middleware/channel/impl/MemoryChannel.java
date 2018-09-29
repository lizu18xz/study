package com.fayayo.study.middleware.channel.impl;

import com.fayayo.study.middleware.channel.Channel;
import com.fayayo.study.middleware.elements.Record;

import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author dalizu on 2018/9/28.
 * @version v1.0
 * @desc 基于内存的通道的实现
 */
public class MemoryChannel extends Channel {

    private int bufferSize = 0;

    //private AtomicInteger memoryBytes = new AtomicInteger(0);

    //基于数组的阻塞队列，必须指定队列大小。比较简单。ArrayBlockingQueue中只有一个ReentrantLock对象，这意味着生产者和消费者无法并行运行
    private ArrayBlockingQueue<Record> queue = null;

    private ReentrantLock lock;

    private Condition notInsufficient, notEmpty;//队列剩余不充足的，不为空

    public MemoryChannel() {

        lock=new ReentrantLock();//用来控制数据到队列的速度
        //对于同一个锁，我们可以创建多个Condition，在不同的情况下使用不同的Condition。
        notInsufficient=lock.newCondition();
        notEmpty=lock.newCondition();

        this.bufferSize=32;
        this.queue = new ArrayBlockingQueue<Record>(this.getCapacity());//初始化队列的大小512
    }


    @Override
    protected void doPush(Record r) {

        try {
            queue.put(r);//放入不进去会阻塞
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }

    //保证集合大小不能大于整个队列的大小,队列大小暂时是512
    @Override
    protected void doPushAll(Collection<Record> rs) {

        try {
            lock.lockInterruptibly();//加锁控制

            //TODO 大小计算判断
            while (rs.size() > this.queue.remainingCapacity()) {//检查剩余可以插入的数量,如果插入数量大于队列数量会报错，所以在这里进行控制
                notInsufficient.await(200L, TimeUnit.MILLISECONDS);//设置超时时间自动重试
            }
            queue.addAll(rs);
            notEmpty.signalAll();//唤醒等待的线程
        }catch (Exception e) {
            throw new RuntimeException();
        } finally {
            lock.unlock();
        }

    }


    @Override
    protected Record doPull() {

        try {
            Record r=queue.take();//获取不到会阻塞
            return r;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(e);
        }
    }

    @Override
    protected void doPullAll(Collection<Record> rs) {
        assert rs != null;
        rs.clear();
        try {
            lock.lockInterruptibly();//加锁控制

            //从队列中获取数据   去bufferSize个数据到集合中
            while (this.queue.drainTo(rs,bufferSize)<=0){//说明暂时没有数据，需要去生产数据
                notEmpty.await(200L, TimeUnit.MILLISECONDS);
            }

            //TODO 计算总数据量的大小
            notInsufficient.signalAll();

        }catch (Exception e) {
            throw new RuntimeException();
        } finally {
            lock.unlock();
        }

    }

    @Override
    public void clear() {
        this.queue.clear();
    }
}

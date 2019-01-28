package com.fayayo.study.hadoop;

import com.fayayo.study.hadoop.dispatcher.JobEventDispatcher;
import com.fayayo.study.hadoop.dispatcher.TaskEventDispatcher;
import com.fayayo.study.hadoop.event.JobEventType;
import com.fayayo.study.hadoop.event.TaskEventType;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.service.CompositeService;
import org.apache.hadoop.service.Service;
import org.apache.hadoop.yarn.event.AsyncDispatcher;
import org.apache.hadoop.yarn.event.Dispatcher;
/**
 * @author dalizu on 2019/1/25.
 * @version v1.0
 * @desc hadoop 事件库的学习
 */
public class MasterService extends CompositeService {


    // 中央异步调度器
    private Dispatcher dispatcher;

    private String jobId;

    private int taskNumber; //该作业包含的任务数目

    private String[] taskIDs; //该作业内部包含的所有任务

    private MasterContext masterContext;

    public MasterService(MasterContext masterContext,String jobId , int taskNumber) {
        super(MasterService.class.getName());
        this.masterContext=masterContext;
        this.dispatcher=masterContext.getDispatcher();

        this.jobId = jobId ;
        this.taskNumber = taskNumber ;
        taskIDs = new String[taskNumber] ;

        for(int i=0 ; i<taskNumber ; i++){
            taskIDs[i] = new String(jobId + "_task_" + i) ;
        }
    }


    @Override
    public void serviceInit(Configuration conf) throws Exception {

        System.out.println("serviceInit......");
        JobEventDispatcher jobEventDispatcher=new JobEventDispatcher(masterContext);
        jobEventDispatcher.setTaskNumber(taskNumber);
        jobEventDispatcher.setTaskIDs(taskIDs);

        this.dispatcher.register(JobEventType.class,jobEventDispatcher);

        this.dispatcher.register(TaskEventType.class,new TaskEventDispatcher(masterContext));

        this.addService((Service) dispatcher);

        super.serviceInit(conf);
    }


    @Override
    protected void serviceStart() throws Exception {
        super.serviceStart();
    }


    @Override
    protected void serviceStop() throws Exception {
        super.serviceStop();
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }



}

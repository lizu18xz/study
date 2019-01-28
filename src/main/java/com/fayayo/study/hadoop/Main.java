package com.fayayo.study.hadoop;

import com.fayayo.study.hadoop.event.JobEvent;
import com.fayayo.study.hadoop.event.JobEventType;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.event.AsyncDispatcher;

/**
 * @author dalizu on 2019/1/25.
 * @version v1.0
 * @desc
 */
public class Main {

    //测试
    public static void main(String[] args) throws Exception {

        String jobID = "job_20131215_12";

        MasterContext masterContext=new MasterContext();
        masterContext.setDispatcher(new AsyncDispatcher());

        MasterService masterService=new MasterService(masterContext,jobID,5);

        YarnConfiguration conf = new YarnConfiguration(new Configuration()) ;

        masterService.init(conf);
        masterService.serviceStart();

        //往中央异步处理器放入了两个事件,中央异步处理器根据注册情况即可知晓什么类型的事件发往什么类型的调度器
        masterService.getDispatcher().getEventHandler().handle(new JobEvent(jobID, JobEventType.JOB_KILL));
        masterService.getDispatcher().getEventHandler().handle(new JobEvent(jobID, JobEventType.JOB_INIT));


        //masterService.serviceStop();

    }


}

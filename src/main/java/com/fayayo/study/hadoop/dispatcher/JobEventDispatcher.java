package com.fayayo.study.hadoop.dispatcher;

import com.fayayo.study.hadoop.MasterContext;
import com.fayayo.study.hadoop.event.JobEvent;
import com.fayayo.study.hadoop.event.JobEventType;
import com.fayayo.study.hadoop.event.TaskEvent;
import com.fayayo.study.hadoop.event.TaskEventType;
import org.apache.hadoop.yarn.event.EventHandler;

/**
 * @author dalizu on 2019/1/25.
 * @version v1.0
 * @desc
 */
public class JobEventDispatcher implements EventHandler<JobEvent> {

    private MasterContext masterContext;

    private int taskNumber;

    private String[] taskIDs;

    public JobEventDispatcher(MasterContext masterContext) {
        this.masterContext=masterContext;
    }


    public int getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    public String[] getTaskIDs() {
        return taskIDs;
    }

    public void setTaskIDs(String[] taskIDs) {
        this.taskIDs = taskIDs;
    }

    @Override
    public void handle(JobEvent event) {

        if(event.getType() == JobEventType.JOB_KILL){
            System.out.println("Receive JOB_KILL event, killing all the tasks");
            for(int i=0;i<taskNumber;i++){
                masterContext.getDispatcher().getEventHandler().handle(new TaskEvent(taskIDs[i],TaskEventType.T_KILL)) ;
            }
        }else if(event.getType() == JobEventType.JOB_INIT){
            System.out.println("Receive JOB_INIT event, scheduling tasks") ;
            for(int i=0;i<taskNumber;i++){
                masterContext.getDispatcher().getEventHandler().handle(new TaskEvent(taskIDs[i], TaskEventType.T_SCHEDULE)) ;
            }
        }

    }



}

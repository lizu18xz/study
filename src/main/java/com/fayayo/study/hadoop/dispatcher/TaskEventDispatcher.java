package com.fayayo.study.hadoop.dispatcher;

import com.fayayo.study.hadoop.MasterContext;
import com.fayayo.study.hadoop.event.TaskEvent;
import com.fayayo.study.hadoop.event.TaskEventType;
import org.apache.hadoop.yarn.event.EventHandler;

/**
 * @author dalizu on 2019/1/25.
 * @version v1.0
 * @desc
 */
public class TaskEventDispatcher implements EventHandler<TaskEvent> {

    private MasterContext masterContext;

    public TaskEventDispatcher(MasterContext masterContext) {
        this.masterContext = masterContext;
    }

    @Override
    public void handle(TaskEvent event) {

        if(event.getType() == TaskEventType.T_KILL){
            System.out.println("Receive T_KILL event of task " + event.getTaskId());
        }else if(event.getType() == TaskEventType.T_SCHEDULE){
            System.out.println("Receive T_SCHEDULE event of task " + event.getTaskId());
        }

    }

}

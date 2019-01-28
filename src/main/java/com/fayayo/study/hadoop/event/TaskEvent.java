package com.fayayo.study.hadoop.event;

import org.apache.hadoop.yarn.event.AbstractEvent;

/**
 * @author dalizu on 2019/1/25.
 * @version v1.0
 * @desc 定义Task事件
 */
public class TaskEvent extends AbstractEvent<TaskEventType>{

    private String taskId ; //Task ID
    public TaskEvent(String taskId , TaskEventType type){
        super(type) ;
        this.taskId = taskId ;
    }
    public String getTaskId(){
        return taskId ;
    }

}

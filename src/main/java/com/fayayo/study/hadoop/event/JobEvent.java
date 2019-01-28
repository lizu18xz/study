package com.fayayo.study.hadoop.event;

import org.apache.hadoop.yarn.event.AbstractEvent;

/**
 * @author dalizu on 2019/1/25.
 * @version v1.0
 * @desc 定义Job事件
 */
public class JobEvent extends AbstractEvent<JobEventType> {

    private String jobId;

    public JobEvent(String jobId,JobEventType jobEventType) {
        super(jobEventType);
        this.jobId=jobId;
    }

    public String getJobId() {
        return jobId;
    }
}

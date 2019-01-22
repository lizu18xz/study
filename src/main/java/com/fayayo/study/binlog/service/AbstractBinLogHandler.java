package com.fayayo.study.binlog.service;

import com.fayayo.study.binlog.dto.BaseLogInfo;
import com.fayayo.study.binlog.dto.TableMapInfo;
import com.fayayo.study.binlog.dto.WriteEventInfo;
import com.github.shyiko.mysql.binlog.event.Event;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author dalizu on 2019/1/18.
 * @version v1.0
 * @desc
 */
@Slf4j
public abstract class AbstractBinLogHandler {

    public static final Map<Long,TableMapInfo> TABLE_MAP_ID=new ConcurrentHashMap();

    public void handlerEvent(Event event){

        //处理事件
        BaseLogInfo baseLogInfo=doEvent(event);
        //发布
        if(baseLogInfo!=null){
            publish(baseLogInfo);
        }
    }

    protected abstract BaseLogInfo doEvent(Event event);


    private void publish(BaseLogInfo baseLogInfo) {
        log.info("发送消息到队列....");

        if(baseLogInfo.getType().equals("writer")){
            WriteEventInfo writeEventInfo=(WriteEventInfo)baseLogInfo;
        }



    }

}

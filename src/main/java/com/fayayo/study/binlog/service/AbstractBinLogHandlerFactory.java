package com.fayayo.study.binlog.service;

import com.fayayo.study.binlog.service.handler.*;
import com.github.shyiko.mysql.binlog.event.EventType;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dalizu on 2019/1/18.
 * @version v1.0
 * @desc
 */
@Slf4j
public class AbstractBinLogHandlerFactory {


    public static AbstractBinLogHandler getHandler(EventType eventType){

        if(EventType.isWrite(eventType)){

            return new WriterBinLogHandler();

        }else if(EventType.isDelete(eventType)){

            return new DeleteBinLogHandler();

        }else if(EventType.isUpdate(eventType)){

            return new UpdateBinLogHandler();

        }else if(EventType.TABLE_MAP.equals(eventType)){

            return new TableMapBinLogHandler();

        }else if(EventType.QUERY.equals(eventType)){

            return new QueryBinLogHandler();

        }else {
            log.info("不处理的事件");
            return new DefaultBinLogHandler();
        }
    }
}

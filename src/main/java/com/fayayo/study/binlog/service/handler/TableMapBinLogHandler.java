package com.fayayo.study.binlog.service.handler;

import com.fayayo.study.binlog.dto.BaseLogInfo;
import com.fayayo.study.binlog.dto.TableMapInfo;
import com.fayayo.study.binlog.service.AbstractBinLogHandler;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.TableMapEventData;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dalizu on 2019/1/18.
 * @version v1.0
 * @desc
 */
@Slf4j
public class TableMapBinLogHandler extends AbstractBinLogHandler {


    @Override
    protected BaseLogInfo doEvent(Event event) {

        TableMapEventData eventData=event.getData();

        log.info("收到数据库DDL操作:"+eventData.getTable());
        long tableId=eventData.getTableId();
        TableMapInfo tableMapInfo=TABLE_MAP_ID.get(tableId);
        if(tableMapInfo!=null){
            return null;
        }
        tableMapInfo=new TableMapInfo(eventData);
        //根据表名称获取列信息
        TABLE_MAP_ID.put(eventData.getTableId(),tableMapInfo);
        return null;
    }

}

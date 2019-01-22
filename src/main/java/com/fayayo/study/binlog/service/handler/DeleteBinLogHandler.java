package com.fayayo.study.binlog.service.handler;

import com.fayayo.study.binlog.dto.BaseLogInfo;
import com.fayayo.study.binlog.service.AbstractBinLogHandler;
import com.github.shyiko.mysql.binlog.event.DeleteRowsEventData;
import com.github.shyiko.mysql.binlog.event.Event;

/**
 * @author dalizu on 2019/1/18.
 * @version v1.0
 * @desc
 */
public class DeleteBinLogHandler extends AbstractBinLogHandler {

    @Override
    protected BaseLogInfo doEvent(Event event) {

        DeleteRowsEventData eventData=event.getData();
        return null;
    }


}

package com.fayayo.study.binlog.service.handler;

import com.fayayo.study.binlog.dto.BaseLogInfo;
import com.fayayo.study.binlog.service.AbstractBinLogHandler;
import com.github.shyiko.mysql.binlog.event.Event;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dalizu on 2019/1/18.
 * @version v1.0
 * @desc
 */
@Slf4j
public class DefaultBinLogHandler extends AbstractBinLogHandler {
    @Override
    protected BaseLogInfo doEvent(Event event) {

        log.info("不处理的事件:{}",event);

        return null;
    }
}

package com.fayayo.study.binlog;

import com.fayayo.study.binlog.service.AbstractBinLogHandler;
import com.fayayo.study.binlog.service.AbstractBinLogHandlerFactory;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author dalizu on 2019/1/17.
 * @version v1.0
 * @desc  Mysql解析binLog
 *
 *   构建 传输的对象
 *   库名称
 *   表名称
 *
 *   操作类型
 *   操作的数据
 *
 *
 *   表信息的保存 String sql = "show columns from `" + d.getTable() + "` from `" + d.getDatabase() + "`";
 *
 *   解析  发送数据到队列中消费
 *
 */
@Slf4j
public class Main {


    public static void main(String[] args) {

        //构建Client
        BinaryLogClient client=new BinaryLogClient("192.168.88.129",3309,"root","root123");

        client.setBinlogFilename("master-bin.000002");
        //TODO 从数据库获取,每次发送后修改到数据库
        client.setBinlogPosition(431);

        //启动监听
        client.registerEventListener(new BinaryLogClient.EventListener() {
            @Override
            public void onEvent(Event event) {
                //处理监听到的事件
                //System.out.println(client.getBinlogPosition());
                handlerEvent(event);
            }
        });

        //监听
        client.registerLifecycleListener(new BinaryLogClient.LifecycleListener() {

            @Override
            public void onConnect(BinaryLogClient client) {
                log.info("connect success");
            }

            @Override
            public void onCommunicationFailure(BinaryLogClient client, Exception ex) {
                log.error("communication fail", ex);
            }

            @Override
            public void onEventDeserializationFailure(BinaryLogClient client, Exception ex) {
                log.error("event deserialization fail", ex);
            }

            @Override
            public void onDisconnect(BinaryLogClient client) {
                log.warn("disconnect");
            }
        });

        try {
            client.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void handlerEvent(Event event) {

        EventType eventType=event.getHeader().getEventType();

        AbstractBinLogHandler handler= AbstractBinLogHandlerFactory.getHandler(eventType);

        handler.handlerEvent(event);

    }

}

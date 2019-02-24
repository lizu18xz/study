package com.fayayo.study.binlogTemplate.mysql;

import com.fayayo.study.binlogTemplate.mysql.listener.AggregationListener;
import com.fayayo.study.binlogTemplate.mysql.listener.IncrementListener;
import com.fayayo.study.binlogTemplate.sender.ISender;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * @author dalizu on 2019/2/21.
 * @version v1.0
 * @desc
 */
@Slf4j
public class BinlogClient {


    private BinaryLogClient client;

    private  BinlogConfig config;

    private  AggregationListener listener;

    public BinlogClient(BinlogConfig config, AggregationListener listener) {
        this.config = config;
        this.listener = listener;
    }

    public void connect() {

        new Thread(() -> {
            client = new BinaryLogClient(
                    config.getHost(),
                    config.getPort(),
                    config.getUsername(),
                    config.getPassword()
            );

            if (!StringUtils.isEmpty(config.getBinlogName()) &&
                    !config.getPosition().equals(-1L)) {
                client.setBinlogFilename(config.getBinlogName());
                client.setBinlogPosition(config.getPosition());
            }

            client.registerEventListener(listener);

            try {
                log.info("connecting to mysql start");
                client.connect();
                log.info("connecting to mysql done");
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }).start();
    }

    public void close() {
        try {
            client.disconnect();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {

        //配置连接信息
        BinlogConfig binlogConfig=new BinlogConfig();

        //注册
        AggregationListener listener=new AggregationListener();

        //注册发送事件
        ISender iSender=null;
        IncrementListener incrementListener=new IncrementListener(iSender,listener);
        incrementListener.register();

        BinlogClient binlogClient=new BinlogClient(binlogConfig,listener);

        //开始启动监听
        binlogClient.connect();

    }
}

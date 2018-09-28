package com.fayayo.study.im.protocol.command;
/**
 * @author dalizu on 2018/9/27.
 * @version v1.0
 * @desc 消息指令
 */
public interface Command {

    //登陆
    Byte LOGIN_REQUEST = 1;

    Byte LOGIN_RESPONSE = 2;

    //发送具体消息
    Byte MESSAGE_REQUEST = 3;

    Byte MESSAGE_RESPONSE = 4;
}

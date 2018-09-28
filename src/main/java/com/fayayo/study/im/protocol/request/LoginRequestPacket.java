package com.fayayo.study.im.protocol.request;

import com.fayayo.study.im.protocol.Packet;
import com.fayayo.study.im.protocol.command.Command;
import lombok.Data;

/**
 * @author dalizu on 2018/9/27.
 * @version v1.0
 * @desc 登陆请求消息
 */
@Data
public class LoginRequestPacket extends Packet{

    private String userId;

    private String username;

    private String password;


    public Byte getCommand() {
        return Command.LOGIN_REQUEST;
    }

    @Override
    public String toString() {
        return "LoginRequestPacket{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

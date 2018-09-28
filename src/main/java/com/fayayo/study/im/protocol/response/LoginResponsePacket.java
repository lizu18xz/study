package com.fayayo.study.im.protocol.response;

import com.fayayo.study.im.protocol.Packet;
import com.fayayo.study.im.protocol.command.Command;
import lombok.Data;
/**
 * @author dalizu on 2018/9/27.
 * @version v1.0
 * @desc 登陆响应消息
 */
@Data
public class LoginResponsePacket extends Packet {

    private boolean success;

    private String reason;


    @Override
    public Byte getCommand() {
        return Command.LOGIN_RESPONSE;
    }
}

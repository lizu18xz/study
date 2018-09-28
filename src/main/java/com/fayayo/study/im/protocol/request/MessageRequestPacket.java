package com.fayayo.study.im.protocol.request;

import com.fayayo.study.im.protocol.Packet;
import com.fayayo.study.im.protocol.command.Command;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dalizu on 2018/9/28.
 * @version v1.0
 * @desc 具体消息的请求类
 */
@Data
@NoArgsConstructor
public class MessageRequestPacket extends Packet{


    private String message;

    public MessageRequestPacket(String message) {
        this.message = message;
    }

    @Override
    public Byte getCommand() {
        return Command.MESSAGE_REQUEST;
    }
}

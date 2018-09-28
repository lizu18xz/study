package com.fayayo.study.im.protocol.response;

import com.fayayo.study.im.protocol.Packet;
import com.fayayo.study.im.protocol.command.Command;
import lombok.Data;

@Data
public class MessageResponsePacket extends Packet {

    private String message;

    @Override
    public Byte getCommand() {

        return Command.MESSAGE_RESPONSE;
    }
}

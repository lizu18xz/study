package com.fayayo.study.im.protocol.response;

import com.fayayo.study.im.protocol.Packet;
import com.fayayo.study.im.session.Session;
import lombok.Data;

import static com.fayayo.study.im.protocol.command.Command.GROUP_MESSAGE_RESPONSE;

@Data
public class GroupMessageResponsePacket extends Packet {

    private String fromGroupId;

    private Session fromUser;

    private String message;

    @Override
    public Byte getCommand() {

        return GROUP_MESSAGE_RESPONSE;
    }
}

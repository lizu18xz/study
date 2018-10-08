package com.fayayo.study.im.protocol.response;

import com.fayayo.study.im.protocol.Packet;
import lombok.Data;

import static com.fayayo.study.im.protocol.command.Command.JOIN_GROUP_RESPONSE;

@Data
public class JoinGroupResponsePacket extends Packet {
    private String groupId;

    private boolean success;

    private String reason;

    @Override
    public Byte getCommand() {

        return JOIN_GROUP_RESPONSE;
    }
}

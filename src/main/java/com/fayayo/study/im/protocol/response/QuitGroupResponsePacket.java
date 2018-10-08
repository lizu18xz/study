package com.fayayo.study.im.protocol.response;

import com.fayayo.study.im.protocol.Packet;
import lombok.Data;

import static com.fayayo.study.im.protocol.command.Command.QUIT_GROUP_RESPONSE;

@Data
public class QuitGroupResponsePacket extends Packet {

    private String groupId;

    private boolean success;

    private String reason;

    @Override
    public Byte getCommand() {

        return QUIT_GROUP_RESPONSE;
    }
}

package com.fayayo.study.im.protocol.request;

import com.fayayo.study.im.protocol.Packet;
import com.fayayo.study.im.protocol.command.Command;
import lombok.Data;

/**
 * @author dalizu on 2018/10/8.
 * @version v1.0
 * @desc
 */
@Data
public class JoinGroupRequestPacket extends Packet {

    private String groupId;//群ID

    @Override
    public Byte getCommand() {
        return Command.JOIN_GROUP_REQUEST;
    }
}

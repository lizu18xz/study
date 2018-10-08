package com.fayayo.study.im.protocol.response;

import com.fayayo.study.im.protocol.Packet;
import com.fayayo.study.im.protocol.command.Command;
import com.fayayo.study.im.session.Session;
import lombok.Data;

import java.util.List;

/**
 * @author dalizu on 2018/10/8.
 * @version v1.0
 * @desc
 */
@Data
public class ListGroupMembersResponsePacket extends Packet {

    private String groupId;

    private List<Session> sessionList;


    @Override
    public Byte getCommand() {
        return Command.LIST_GROUP_MEMBERS_RESPONSE;
    }
}

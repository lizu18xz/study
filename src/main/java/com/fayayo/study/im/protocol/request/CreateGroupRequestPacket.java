package com.fayayo.study.im.protocol.request;

import com.fayayo.study.im.protocol.Packet;
import com.fayayo.study.im.protocol.command.Command;
import lombok.Data;

import java.util.List;

/**
 * @author dalizu on 2018/10/8.
 * @version v1.0
 * @desc 发起群聊的消息
 */
@Data
public class CreateGroupRequestPacket extends Packet{

    private List<String> userIdList;//所有用户的列表

    @Override
    public Byte getCommand() {
        return Command.CREATE_GROUP_REQUEST;
    }
}

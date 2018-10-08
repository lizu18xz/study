package com.fayayo.study.im.protocol.request;


import com.fayayo.study.im.protocol.Packet;

import static com.fayayo.study.im.protocol.command.Command.HEARTBEAT_REQUEST;

public class HeartBeatRequestPacket extends Packet {
    @Override
    public Byte getCommand() {
        return HEARTBEAT_REQUEST;
    }
}

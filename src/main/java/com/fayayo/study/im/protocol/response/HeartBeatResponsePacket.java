package com.fayayo.study.im.protocol.response;


import com.fayayo.study.im.protocol.Packet;

import static com.fayayo.study.im.protocol.command.Command.HEARTBEAT_RESPONSE;

public class HeartBeatResponsePacket extends Packet {
    @Override
    public Byte getCommand() {
        return HEARTBEAT_RESPONSE;
    }
}

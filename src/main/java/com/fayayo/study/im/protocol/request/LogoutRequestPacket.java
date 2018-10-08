package com.fayayo.study.im.protocol.request;

import com.fayayo.study.im.protocol.Packet;
import lombok.Data;

import static com.fayayo.study.im.protocol.command.Command.LOGOUT_REQUEST;

@Data
public class LogoutRequestPacket extends Packet {
    @Override
    public Byte getCommand() {

        return LOGOUT_REQUEST;
    }
}

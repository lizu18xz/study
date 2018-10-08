package com.fayayo.study.im.protocol.response;

import com.fayayo.study.im.protocol.Packet;
import lombok.Data;

import static com.fayayo.study.im.protocol.command.Command.LOGOUT_RESPONSE;

@Data
public class LogoutResponsePacket extends Packet {

    private boolean success;

    private String reason;


    @Override
    public Byte getCommand() {

        return LOGOUT_RESPONSE;
    }
}

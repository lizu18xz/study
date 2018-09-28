package com.fayayo.study.im.client.handler;

import com.fayayo.study.im.protocol.response.MessageResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

/**
 * @author dalizu on 2018/9/28.
 * @version v1.0
 * @desc 处理消息的返回
 */
public class MessageResponseHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageResponsePacket messageResponsePacket)
            throws Exception {

        System.out.println(new Date() + ": 收到服务端的消息: " + messageResponsePacket.getMessage());

    }
}

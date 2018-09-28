package com.fayayo.study.im.server.handler;

import com.fayayo.study.im.protocol.request.MessageRequestPacket;
import com.fayayo.study.im.protocol.response.MessageResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

/**
 * @author dalizu on 2018/9/28.
 * @version v1.0
 * @desc 处理消息的请求
 */
public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {


    //获取到客户端发送的消息后
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket messageRequestPacket)
            throws Exception {

        MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
        System.out.println(new Date() + ": 收到客户端消息: " + messageRequestPacket.getMessage());
        messageResponsePacket.setMessage("服务端回复【" + messageRequestPacket.getMessage() + "】");

        ctx.channel().writeAndFlush(messageResponsePacket);

    }
}

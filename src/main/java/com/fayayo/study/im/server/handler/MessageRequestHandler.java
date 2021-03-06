package com.fayayo.study.im.server.handler;

import com.fayayo.study.im.protocol.request.MessageRequestPacket;
import com.fayayo.study.im.protocol.response.MessageResponsePacket;
import com.fayayo.study.im.session.Session;
import com.fayayo.study.im.util.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

/**
 * @author dalizu on 2018/9/28.
 * @version v1.0
 * @desc 处理消息的请求
 */
// 1. 加上注解标识，表明该 handler 是可以多个 channel 共享的
@ChannelHandler.Sharable
public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {

    //ThreadPool threadPool = xxx;

    public static final MessageRequestHandler INSTANCE = new MessageRequestHandler();

    private MessageRequestHandler(){

    }


    //获取到客户端发送的消息后
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket messageRequestPacket)
            throws Exception {

        long begin = System.currentTimeMillis();
        // 1.拿到消息发送方的会话信息
        Session session = SessionUtil.getSession(ctx.channel());

        // 2.通过消息发送方的会话信息构造要发送的消息
        MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
        messageResponsePacket.setFromUserId(session.getUserId());//谁发送的消息的信息
        messageResponsePacket.setFromUserName(session.getUserName());
        messageResponsePacket.setMessage(messageRequestPacket.getMessage());//收到客户端消息

        // 3.拿到消息接收方的 channel
        Channel toUserChannel = SessionUtil.getChannel(messageRequestPacket.getToUserId());//消息发送给谁

        // 4.将消息发送给消息接收方
        if (toUserChannel != null && SessionUtil.hasLogin(toUserChannel)) {
            toUserChannel.writeAndFlush(messageResponsePacket).addListener(future -> {
                if (future.isDone()) {
                    // balabala 其他的逻辑
                    long time =  System.currentTimeMillis() - begin;
                }
            });

        } else {
            System.err.println("[" + session.getUserId() + "] 不在线，发送失败!");
        }

    }
}

package com.fayayo.study.im.server.handler;

import com.fayayo.study.im.protocol.request.HeartBeatRequestPacket;
import com.fayayo.study.im.protocol.response.HeartBeatResponsePacket;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class HeartBeatRequestHandler extends SimpleChannelInboundHandler<HeartBeatRequestPacket> {

    public static final HeartBeatRequestHandler INSTANCE = new HeartBeatRequestHandler();

    private HeartBeatRequestHandler() {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartBeatRequestPacket requestPacket) {
        //System.out.println("服务端收到心跳包,向客户端发送心跳");
        ctx.channel().writeAndFlush(new HeartBeatResponsePacket());
    }
}

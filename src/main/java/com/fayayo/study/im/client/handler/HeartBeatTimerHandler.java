package com.fayayo.study.im.client.handler;

import com.fayayo.study.im.protocol.request.HeartBeatRequestPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.TimeUnit;

/**
 * @author dalizu on 2018/10/8.
 * @version v1.0
 * @desc 心跳包
 */
public class HeartBeatTimerHandler extends ChannelInboundHandlerAdapter {

    private static final int HEARTBEAT_INTERVAL = 5;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.executor().scheduleAtFixedRate(() -> {
            //System.out.println("向服务端发送心跳包.....");
            ctx.channel().writeAndFlush(new HeartBeatRequestPacket());
        }, HEARTBEAT_INTERVAL, HEARTBEAT_INTERVAL, TimeUnit.SECONDS);

        //每隔 5 秒，向服务端发送一个心跳数据包
        super.channelActive(ctx);
    }

}

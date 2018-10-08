package com.fayayo.study.im.server.handler;

import com.fayayo.study.im.protocol.request.JoinGroupRequestPacket;
import com.fayayo.study.im.protocol.response.JoinGroupResponsePacket;
import com.fayayo.study.im.util.SessionUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

/**
 * @author dalizu on 2018/10/8.
 * @version v1.0
 * @desc
 */
// 1. 加上注解标识，表明该 handler 是可以多个 channel 共享的
@ChannelHandler.Sharable
public class JoinGroupRequestHandler extends SimpleChannelInboundHandler<JoinGroupRequestPacket> {

    public static final JoinGroupRequestHandler INSTANCE = new JoinGroupRequestHandler();

    private JoinGroupRequestHandler(){


    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JoinGroupRequestPacket joinGroupRequestPacket) {

        String groupId=joinGroupRequestPacket.getGroupId();//获取群ID

        //获取群对应的 channelGroup，然后将当前用户的 channel 添加进去
        ChannelGroup channelGroup=SessionUtil.getChannelGroup(groupId);
        channelGroup.add(ctx.channel());

        // 2. 构造加群响应发送给客户端
        JoinGroupResponsePacket joinGroupResponsePacket=new JoinGroupResponsePacket();
        joinGroupResponsePacket.setSuccess(true);
        joinGroupResponsePacket.setGroupId(groupId);
        ctx.channel().writeAndFlush(joinGroupResponsePacket);

    }
}

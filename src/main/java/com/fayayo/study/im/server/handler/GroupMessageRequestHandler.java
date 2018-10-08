package com.fayayo.study.im.server.handler;

import com.fayayo.study.im.protocol.request.GroupMessageRequestPacket;
import com.fayayo.study.im.protocol.response.GroupMessageResponsePacket;
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
public class GroupMessageRequestHandler extends SimpleChannelInboundHandler<GroupMessageRequestPacket> {

    public static final GroupMessageRequestHandler INSTANCE = new GroupMessageRequestHandler();

    private GroupMessageRequestHandler(){

    }



    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMessageRequestPacket requestPacket) {

        //发送到哪个群
        String groupId=requestPacket.getToGroupId();

        //拿到 groupId 构造群聊消息的响应
        GroupMessageResponsePacket responsePacket = new GroupMessageResponsePacket();
        responsePacket.setFromGroupId(groupId);
        responsePacket.setMessage(requestPacket.getMessage());
        responsePacket.setFromUser(SessionUtil.getSession(ctx.channel()));

        ChannelGroup channelGroup=SessionUtil.getChannelGroup(groupId);
        if(channelGroup==null){
            throw new RuntimeException("群组不存在!!!");
        }

        channelGroup.writeAndFlush(responsePacket);

    }


}


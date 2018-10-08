package com.fayayo.study.im.server.handler;

import com.fayayo.study.im.protocol.request.CreateGroupRequestPacket;
import com.fayayo.study.im.protocol.response.CreateGroupResponsePacket;
import com.fayayo.study.im.util.IDUtil;
import com.fayayo.study.im.util.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dalizu on 2018/10/8.
 * @version v1.0
 * @desc 群聊创建的处理
 */
// 1. 加上注解标识，表明该 handler 是可以多个 channel 共享的
@ChannelHandler.Sharable
public class CreateGroupRequestHandler extends SimpleChannelInboundHandler<CreateGroupRequestPacket> {

    public static final CreateGroupRequestHandler INSTANCE = new CreateGroupRequestHandler();

    private CreateGroupRequestHandler(){

    }



    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupRequestPacket groupRequestPacket){

        //获取所有的群聊成员
        List<String>userIdList=groupRequestPacket.getUserIdList();
        List<String> userNameList = new ArrayList<>();

        //创建一个 channel 分组
        ChannelGroup channelGroup = new DefaultChannelGroup(ctx.executor());

        // 2. 筛选出待加入群聊的用户的 channel 和 userName

        for (String userId:userIdList){
            Channel channel = SessionUtil.getChannel(userId);//获取每个已经登陆用户的channel
            if (channel != null) {
                channelGroup.add(channel);
                userNameList.add(SessionUtil.getSession(channel).getUserName());
            }
        }

        //创建群聊成功的响应结果
        String groupId = IDUtil.randomId();
        CreateGroupResponsePacket createGroupResponsePacket=new CreateGroupResponsePacket();
        createGroupResponsePacket.setGroupId(groupId);
        createGroupResponsePacket.setUserNameList(userNameList);
        createGroupResponsePacket.setSuccess(true);

        // 4. 给每个客户端发送拉群通知
        channelGroup.writeAndFlush(createGroupResponsePacket);

        System.out.print("群创建成功，id 为[" + createGroupResponsePacket.getGroupId() + "], ");
        System.out.println("群里面有：" + createGroupResponsePacket.getUserNameList());

        SessionUtil.bindChannelGroup(groupId,channelGroup);

    }



}

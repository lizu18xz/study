package com.fayayo.study.im.client.handler;

import com.fayayo.study.im.protocol.request.LoginRequestPacket;
import com.fayayo.study.im.protocol.response.LoginResponsePacket;
import com.fayayo.study.im.util.LoginUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;
import java.util.UUID;

/**
 * @author dalizu on 2018/9/28.
 * @version v1.0
 * @desc 处理登陆返回
 */
public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {


    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // 创建登录对象
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setUserId(UUID.randomUUID().toString());
        loginRequestPacket.setUsername("username");
        loginRequestPacket.setPassword("pwd");

        // 写数据
        ctx.channel().writeAndFlush(loginRequestPacket);

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket loginResponsePacket){

        if (loginResponsePacket.isSuccess()) {
            System.out.println(new Date() + ": 客户端登录成功,绑定登陆成功标志");
            LoginUtil.markAsLogin(ctx.channel());
        } else {
            System.out.println(new Date() + ": 客户端登录失败，原因：" + loginResponsePacket.getReason());
        }

    }

}

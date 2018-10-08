package com.fayayo.study.im.server.handler;

import com.fayayo.study.im.protocol.request.LoginRequestPacket;
import com.fayayo.study.im.protocol.response.LoginResponsePacket;
import com.fayayo.study.im.session.Session;
import com.fayayo.study.im.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;
import java.util.UUID;

/**
 * @author dalizu on 2018/9/28.
 * @version v1.0
 * @desc 登陆消息处理类
 */
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket loginRequestPacket) {
        System.out.println(new Date() + ": 收到客户端登录请求……"+loginRequestPacket.toString());

        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        loginResponsePacket.setVersion(loginRequestPacket.getVersion());
        loginResponsePacket.setUserName(loginRequestPacket.getUsername());

        if (valid(loginRequestPacket)) {
            loginResponsePacket.setSuccess(true);
            String userId = randomUserId();
            loginResponsePacket.setUserId(userId);
            System.out.println("[" + loginRequestPacket.getUsername() + "]登录成功");
            SessionUtil.bindSession(new Session(userId, loginRequestPacket.getUsername()), ctx.channel());//服务端绑定登陆成功标志
        } else {
            loginResponsePacket.setReason("账号密码校验失败");
            loginResponsePacket.setSuccess(false);
            System.out.println(new Date() + ": 登录失败!");
        }

        // 登录响应
        ctx.channel().writeAndFlush(loginResponsePacket);
    }

    //TODO 校验账号和密码
    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }


    //用户ID
    private static String randomUserId() {
        return UUID.randomUUID().toString().split("-")[0];
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        SessionUtil.unBindSession(ctx.channel());
    }
}

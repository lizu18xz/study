package com.fayayo.study.im.server;

import com.fayayo.study.im.codec.PacketDecoder;
import com.fayayo.study.im.codec.PacketEncoder;
import com.fayayo.study.im.codec.Spliter;
import com.fayayo.study.im.handler.IMIdleStateHandler;
import com.fayayo.study.im.server.handler.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.util.Date;

/**
 * @author dalizu on 2018/9/28.
 * @version v1.0
 * @desc 服务端启动类
 */
public class NettyServer {


    private static final int PORT = 8000;

    public static void main(String[] args) {
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        final ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(boosGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    protected void initChannel(NioSocketChannel ch) {
                        //ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 7, 4));

                        // 空闲检测
                        ch.pipeline().addLast(new IMIdleStateHandler());

                        ch.pipeline().addLast(new Spliter());
                        ch.pipeline().addLast(new PacketDecoder());
                        // 登录请求处理器
                        ch.pipeline().addLast(LoginRequestHandler.INSTANCE);

                        ch.pipeline().addLast(HeartBeatRequestHandler.INSTANCE);

                        ch.pipeline().addLast(AuthHandler.INSTANCE);
                        // 单聊消息请求处理器
                        ch.pipeline().addLast(MessageRequestHandler.INSTANCE);
                        // 创建群请求处理器
                        ch.pipeline().addLast(CreateGroupRequestHandler.INSTANCE);
                        // 加群请求处理器
                        ch.pipeline().addLast(JoinGroupRequestHandler.INSTANCE);
                        // 退群请求处理器
                        ch.pipeline().addLast(QuitGroupRequestHandler.INSTANCE);
                        // 获取群成员请求处理器
                        ch.pipeline().addLast(ListGroupMembersRequestHandler.INSTANCE);

                        ch.pipeline().addLast(GroupMessageRequestHandler.INSTANCE);

                        // 登出请求处理器
                        ch.pipeline().addLast(LogoutRequestHandler.INSTANCE);
                        ch.pipeline().addLast(new PacketEncoder());


                    }
                });


        bind(serverBootstrap, PORT);
    }

    private static void bind(final ServerBootstrap serverBootstrap, final int port) {
        serverBootstrap.bind(port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println(new Date() + ": 端口[" + port + "]绑定成功!");
            } else {
                System.err.println("端口[" + port + "]绑定失败!");
            }
        });
    }

}

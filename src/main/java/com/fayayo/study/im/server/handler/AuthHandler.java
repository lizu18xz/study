package com.fayayo.study.im.server.handler;

import com.fayayo.study.im.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author dalizu on 2018/10/8.
 * @version v1.0
 * @desc 校验逻辑
 */
public class AuthHandler extends ChannelInboundHandlerAdapter{



    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!SessionUtil.hasLogin(ctx.channel())) {
            ctx.channel().close();
        } else {
            ctx.pipeline().remove(this);
            super.channelRead(ctx, msg);
        }
    }
}

package com.fayayo.study.im.codec;

import com.fayayo.study.im.protocol.Packet;
import com.fayayo.study.im.protocol.PacketCodeC;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author dalizu on 2018/9/28.
 * @version v1.0
 * @desc 编码
 */
public class PacketEncoder extends MessageToByteEncoder<Packet> {


    // 这里拿到业务对象msg的数据，然后调用 out.writeXXX()系列方法编码  可以看其父类的write方法
    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf out) {
        try {
            PacketCodeC.INSTANCE.encode(out,packet);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

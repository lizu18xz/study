package com.fayayo.study.im.client.console;

import com.fayayo.study.im.protocol.request.JoinGroupRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * @author dalizu on 2018/10/8.
 * @version v1.0
 * @desc 加入群聊
 */
public class JoinGroupConsoleCommand implements ConsoleCommand{


    @Override
    public void exec(Scanner scanner, Channel channel) {

        //发送请求

        JoinGroupRequestPacket joinGroupRequestPacket = new JoinGroupRequestPacket();

        System.out.print("输入 groupId，加入群聊：");
        String groupId = scanner.next();

        joinGroupRequestPacket.setGroupId(groupId);
        channel.writeAndFlush(joinGroupRequestPacket);

    }


}

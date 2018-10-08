package com.fayayo.study.im.client.console;

import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * @author dalizu on 2018/10/8.
 * @version v1.0
 * @desc 控制台命令
 */
public interface ConsoleCommand {


    void exec(Scanner scanner, Channel channel);

}

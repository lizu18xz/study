package com.fayayo.study.im.util;

import com.fayayo.study.im.Attributes.Attributes;
import io.netty.channel.Channel;
import io.netty.util.Attribute;

public class LoginUtil {

    //绑定登陆标志
    public static void markAsLogin(Channel channel) {
        channel.attr(Attributes.LOGIN).set(true);
    }

    public static boolean hasLogin(Channel channel) {
        Attribute<Boolean> loginAttr = channel.attr(Attributes.LOGIN);

        return loginAttr.get() != null;
    }
}

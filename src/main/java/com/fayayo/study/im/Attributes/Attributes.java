package com.fayayo.study.im.Attributes;

import com.fayayo.study.im.session.Session;
import io.netty.util.AttributeKey;

/**
 * @author dalizu on 2018/9/28.
 * @version v1.0
 * @desc
 */
public interface Attributes {

    AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("login");

    AttributeKey<Session> SESSION = AttributeKey.newInstance("session");
}

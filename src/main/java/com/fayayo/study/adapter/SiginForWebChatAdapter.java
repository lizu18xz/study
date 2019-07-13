package com.fayayo.study.adapter;

/**
 * @author dalizu on 2019/7/13.
 * @version v1.0
 * @desc
 */
public class SiginForWebChatAdapter extends SiginService implements ISiginForWebChat{


    @Override
    public String loginForWechat(String openId) {

        return login(openId,null);
    }
}

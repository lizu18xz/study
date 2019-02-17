package com.fayayo.study.future;

/**
 * @author dalizu on 2019/2/15.
 * @version v1.0
 * @desc
 */
public class ResponseCallBack {

    public String call(String o) {
        System.out.println("ResponseCallback：处理完成，返回："+o);
        return o;
    }

}

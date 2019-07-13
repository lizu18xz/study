package com.fayayo.study.decorator;

/**
 * @author dalizu on 2019/7/13.
 * @version v1.0
 * @desc
 */
public class DoRequest implements Request{


    @Override
    public String request() {

        System.out.println("request");

        return null;
    }
}

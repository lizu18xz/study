package com.fayayo.study.decorator;

/**
 * @author dalizu on 2019/7/13.
 * @version v1.0
 * @desc
 */
public class DoRequestFilter implements Request{

    private DoRequest doRequest;

    public DoRequestFilter(DoRequest doRequest) {
        this.doRequest = doRequest;
    }

    @Override
    public String request() {

        doFilter(doRequest);

        return null;
    }

    private void doFilter(DoRequest doRequest) {

        System.out.println("doFilter start...");

        doRequest.request();

        System.out.println("doFilter end...");


    }


}

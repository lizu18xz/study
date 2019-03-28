package com.fayayo.study.chain;

/**
 * @author dalizu on 2019/3/15.
 * @version v1.0
 * @desc 责任链模式
 */
public class Main {


    public static void main(String[] args) {

        Support limitSupport=new LimitSupport("有限支持",2);

        Support specialSupport=new LimitSupport("特殊支持",5);

        limitSupport.setAndReturnNext(specialSupport);


        limitSupport.support(new Trouble(3));



    }

}

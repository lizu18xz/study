package com.fayayo.study.socket;

/**
 * @author dalizu on 2019/1/16.
 * @version v1.0
 * @desc
 */
public class Demo01 {

    public static void main(String[] args) {
        //Java中输出一个值对应的二进制方法有很多，这里提供一个简单的方法：

        int value = 33;
        String bs = String.format("%32s", Integer.toBinaryString(value)).replace(" ", "0");
        System.out.println("二进制:"+bs);

        String bs1 = String.format("%8s", Integer.toBinaryString(value&0xff)).replace(" ", "0");
        System.out.println("byte:"+bs1);


    }

}

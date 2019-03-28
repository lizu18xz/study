package com.fayayo.study.chain;

/**
 * @author dalizu on 2019/3/15.
 * @version v1.0
 * @desc 数据结构
 */
public class Trouble {

    private int number;

    public Trouble(int number) {
        this.number = number;
    }


    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "Trouble{" +
                "number=" + number +
                '}';
    }
}

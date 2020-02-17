package com.fayayo.study.threadlocal;

/**
 * @author dalizu on 2020/2/17.
 * @version v1.0
 * @desc
 */
public class Val <T> {

    T v;

    public void set(T _v) {
        v = _v;
    }

    public T get() {
        return v;
    }
}

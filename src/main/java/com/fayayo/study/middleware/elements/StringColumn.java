package com.fayayo.study.middleware.elements;

/**
 * @author dalizu on 2018/9/28.
 * @version v1.0
 * @desc 字符串类型的column
 */
public class StringColumn extends Column {

    public StringColumn() {
        this((String)null);
    }

    public StringColumn(final Object rawData) {
        super(Type.STRING, rawData);
    }

}

package com.fayayo.study.middleware.elements;

import lombok.Data;

/**
 * @author dalizu on 2018/9/28.
 * @version v1.0
 * @desc 定义数据结构，代表每一列
 */
@Data
public abstract class Column {


    private Type type;//数据类型

    private Object rawData;//数据内容


    public Column(Type type, Object rawData) {
        this.type = type;
        this.rawData = rawData;
    }

    //数据类型
    public enum Type {
        BAD, NULL, INT, LONG, DOUBLE, STRING, BOOL, DATE, BYTES
    }

}

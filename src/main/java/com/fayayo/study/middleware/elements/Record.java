package com.fayayo.study.middleware.elements;

/**
 * @author dalizu on 2018/9/28.
 * @version v1.0
 * @desc 一条记录
 */
public interface Record {

     void addColumn(Column column);

     void setColumn(int i, final Column column);

     Column getColumn(int i);

     String toString();

     int getColumnNumber();

}

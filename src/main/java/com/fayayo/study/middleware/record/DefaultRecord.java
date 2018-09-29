package com.fayayo.study.middleware.record;

import com.fayayo.study.middleware.elements.Column;
import com.fayayo.study.middleware.elements.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dalizu on 2018/9/28.
 * @version v1.0
 * @desc 传输记录的方法
 */
public class DefaultRecord implements Record{

    private static final int RECORD_AVERGAE_COLUMN_NUMBER = 16;

    private List<Column>columns;//一条数据

    public DefaultRecord() {
        this.columns = new ArrayList<Column>(RECORD_AVERGAE_COLUMN_NUMBER);
    }

    @Override
    public void addColumn(Column column) {
        columns.add(column);
    }

    @Override
    public void setColumn(int i, Column column) {
        if (i < 0) {
            throw new RuntimeException("不能给index小于0的column设置值");
        }

        if (i >= columns.size()) {
            expandCapacity(i + 1);
        }

        this.columns.set(i, column);
    }

    @Override
    public Column getColumn(int i) {
        if (i < 0 || i >= columns.size()) {
            return null;
        }
        return columns.get(i);
    }

    @Override
    public int getColumnNumber() {
        return this.columns.size();
    }

    private void expandCapacity(int totalSize) {
        if (totalSize <= 0) {
            return;
        }

        int needToExpand = totalSize - columns.size();
        while (needToExpand-- > 0) {
            this.columns.add(null);
        }
    }

}

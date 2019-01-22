package com.fayayo.study.binlog.dto;

/**
 * @author dalizu on 2019/1/17.
 * @version v1.0
 * @desc
 */
public class BaseLogInfo {

    public BaseLogInfo(String database, String tableName) {
        this.database = database;
        this.tableName = tableName;
    }

    //库名称
    private String database;

    //表名称
    private String tableName;

    private String type;

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

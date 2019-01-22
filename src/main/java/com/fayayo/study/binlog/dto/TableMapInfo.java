package com.fayayo.study.binlog.dto;

import com.github.shyiko.mysql.binlog.event.TableMapEventData;

import java.sql.*;
import java.util.ArrayList;

/**
 * @author dalizu on 2019/1/18.
 * @version v1.0
 * @desc
 */
public class TableMapInfo {

    private ArrayList<String> columnNames;

    private long tableId;

    private String tableName;

    private String database;

    public TableMapInfo(TableMapEventData tableMapEventData) {
        tableId = tableMapEventData.getTableId();
        tableName = tableMapEventData.getTable();
        database = tableMapEventData.getDatabase();
        //获取实际的列信息
        columnNames = new ArrayList<>();
        jdbcGetColumns(database,tableName);
    }


    public ArrayList<String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(ArrayList<String> columnNames) {
        this.columnNames = columnNames;
    }

    public long getTableId() {
        return tableId;
    }

    public void setTableId(long tableId) {
        this.tableId = tableId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }


    private void jdbcGetColumns(String database,String tableName){

        String BETADBURL ="jdbc:mysql://192.168.88.129:3309/"+database+"?user=root&password=root123&useUnicode=true&characterEncoding=utf-8";

        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection(BETADBURL);
        } catch (Exception e) {
            System.out.println("[Get Connection Error]" + e.getMessage());
        }

        String SQL = "show columns from "+tableName;

        try {
            PreparedStatement ps = conn.prepareStatement(SQL);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                this.columnNames.add(resultSet.getString("Field"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

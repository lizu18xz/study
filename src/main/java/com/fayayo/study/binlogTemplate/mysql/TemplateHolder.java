package com.fayayo.study.binlogTemplate.mysql;

import com.alibaba.fastjson.JSON;
import com.fayayo.study.binlogTemplate.mysql.constants.OpType;
import com.fayayo.study.binlogTemplate.mysql.dto.ParseTemplate;
import com.fayayo.study.binlogTemplate.mysql.dto.TableTemplate;
import com.fayayo.study.binlogTemplate.mysql.dto.Template;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.List;
import java.util.Map;

/**
 * @author dalizu on 2019/2/21.
 * @version v1.0
 * @desc 解析模板文件
 */
@Slf4j
public class TemplateHolder {

    private String SQL_SCHEMA = "select table_schema, table_name, " +
            "column_name, ordinal_position from information_schema.columns " +
            "where table_schema = ? and table_name = ?";


    private ParseTemplate template;

    public TemplateHolder() {
        init();
    }

    //初始化
    private void init() {
        loadJson("template.json");
    }


    //对外服务
    public TableTemplate getTable(String tableName) {
        return template.getTableTemplateMap().get(tableName);
    }


    //加载json配置文件  json--->Template对象
    private void loadJson(String path) {

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream inStream = cl.getResourceAsStream(path);

        try {
            Template template = JSON.parseObject(
                    inStream,
                    Charset.defaultCharset(),
                    Template.class
            );
            this.template = ParseTemplate.parse(template);
            loadMeta();
        } catch (IOException ex) {
            log.error(ex.getMessage());
            throw new RuntimeException("fail to parse json file");
        }
    }


    //去数据库查询 列名称信息
    private void loadMeta() {

        for (Map.Entry<String, TableTemplate> entry :
                template.getTableTemplateMap().entrySet()) {

            TableTemplate table = entry.getValue();

            //获取字段信息,在json里面进行配置的信息
            List<String> updateFields = table.getOpTypeFieldSetMap().get(
                    OpType.UPDATE
            );
            List<String> insertFields = table.getOpTypeFieldSetMap().get(
                    OpType.ADD
            );
            List<String> deleteFields = table.getOpTypeFieldSetMap().get(
                    OpType.DELETE
            );

            //保存 表字段的(index,列名称) 到map,方面后面使用
            Connection conn=getConn();
            try {
                PreparedStatement ps = conn.prepareStatement(SQL_SCHEMA);
                ps.setString(1,template.getDatabase());
                ps.setString(2,table.getTableName());

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int pos = rs.getInt("ORDINAL_POSITION");
                    String colName = rs.getString("COLUMN_NAME");
                    if ((null != updateFields && updateFields.contains(colName))
                            || (null != insertFields && insertFields.contains(colName))
                            || (null != deleteFields && deleteFields.contains(colName))) {
                        //保存 表 字段 索引  index  对应 的 列名称
                        table.getPosMap().put(pos - 1, colName);
                    }
                }

            } catch (SQLException e) {

                e.printStackTrace();

            }finally {
                if(conn!=null){
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    private Connection getConn(){

        String BETADBURL ="jdbc:mysql://192.168.88.129:3309/test"+"?user=root&password=root123&useUnicode=true&characterEncoding=utf-8";

        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection(BETADBURL);
        } catch (Exception e) {
            System.out.println("[Get Connection Error]" + e.getMessage());
        }
        return conn;
    }

}

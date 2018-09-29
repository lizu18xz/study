package com.fayayo.study.dataLoad.conn;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnnectionManager {

    private static final String BETADBURL ="jdbc:mysql://localhost:3306/datax?user=root&password=root123&useUnicode=true&characterEncoding=utf-8";

    public static Connection getConnection() {
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

package com.fayayo.study.binlogTemplate.sender;


import com.fayayo.study.binlogTemplate.mysql.dto.MySqlRowData;


public interface ISender {

    void sender(MySqlRowData rowData);
}

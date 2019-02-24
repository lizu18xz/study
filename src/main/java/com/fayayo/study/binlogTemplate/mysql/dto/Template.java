package com.fayayo.study.binlogTemplate.mysql.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Template {

    //最外层是库名称
    private String database;
    //监听的多个表的信息
    private List<JsonTable> tableList;

}

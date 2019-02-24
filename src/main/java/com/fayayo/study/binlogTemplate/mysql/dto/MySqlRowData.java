package com.fayayo.study.binlogTemplate.mysql.dto;

import com.fayayo.study.binlogTemplate.mysql.constants.OpType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//用于  增量数据的投递
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MySqlRowData {

    private String tableName;

    private String level;

    //操作数据的类似
    private OpType opType;

    //after 部分  修改的字段名称和对应的值
    private List<Map<String, String>> fieldValueMap = new ArrayList<>();

}

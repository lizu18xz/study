package com.fayayo.study.binlog.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author dalizu on 2019/1/18.
 * @version v1.0
 * @desc
 */
public class WriteEventInfo extends BaseLogInfo{

    private List<Map<String, Serializable>> rowMaps;

    public WriteEventInfo(String database, String tableName, List<Map<String, Serializable>> rowMaps) {
        super(database, tableName);
        this.rowMaps = rowMaps;
    }

    public List<Map<String, Serializable>> getRowMaps() {
        return rowMaps;
    }

    public void setRowMaps(List<Map<String, Serializable>> rowMaps) {
        this.rowMaps = rowMaps;
    }
}

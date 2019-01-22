package com.fayayo.study.binlog.service.handler;

import com.fayayo.study.binlog.dto.BaseLogInfo;
import com.fayayo.study.binlog.dto.TableMapInfo;
import com.fayayo.study.binlog.dto.WriteEventInfo;
import com.fayayo.study.binlog.service.AbstractBinLogHandler;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;
import lombok.extern.slf4j.Slf4j;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author dalizu on 2019/1/18.
 * @version v1.0
 * @desc
 */
@Slf4j
public class WriterBinLogHandler extends AbstractBinLogHandler {

    @Override
    protected BaseLogInfo doEvent(Event event) {

        WriteRowsEventData eventData=event.getData();

        log.info("收到数据库DML操作:"+eventData.toString());

        long tableId=eventData.getTableId();

        TableMapInfo tableMapInfo=TABLE_MAP_ID.get(tableId);
        List<String> cols=tableMapInfo.getColumnNames();

        List<Serializable[]>rows= eventData.getRows();

        List<Map<String, Serializable>>rowList=new ArrayList<>();

        //转rows  -->  (columnName,value)
        rows.stream().map(e->{
            Map<String, Serializable>map=new HashMap<>();

            for (int i=0;i<cols.size();i++){
                map.put(cols.get(i),e[i]);
            }
            rowList.add(map);
            return rowList;
        }).collect(Collectors.toList());

        //保存 表信息,类型
        WriteEventInfo writeEventInfo=new WriteEventInfo(tableMapInfo.getDatabase(),tableMapInfo.getTableName(),rowList);
        writeEventInfo.setType("writer");
        return writeEventInfo;
    }
}

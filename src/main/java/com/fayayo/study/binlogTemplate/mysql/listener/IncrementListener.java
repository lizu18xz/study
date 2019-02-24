package com.fayayo.study.binlogTemplate.mysql.listener;

import com.fayayo.study.binlogTemplate.mysql.constants.Constant;
import com.fayayo.study.binlogTemplate.mysql.constants.OpType;
import com.fayayo.study.binlogTemplate.mysql.dto.BinlogRowData;
import com.fayayo.study.binlogTemplate.mysql.dto.MySqlRowData;
import com.fayayo.study.binlogTemplate.mysql.dto.TableTemplate;
import com.fayayo.study.binlogTemplate.sender.ISender;
import com.github.shyiko.mysql.binlog.event.EventType;
import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
public class IncrementListener implements Ilistener {

    //投递方式
    private ISender sender;

    private AggregationListener aggregationListener;

    public IncrementListener(ISender sender, AggregationListener aggregationListener) {
        this.sender = sender;
        this.aggregationListener = aggregationListener;
    }

    //注册处理器,要在初始化的时候就注册,系统启动的时候
    @Override
    public void register() {

        log.info("IncrementListener register db and table info");
        Constant.table2Db.forEach((k, v) ->
        aggregationListener.register(v, k, this));
    }

    //将BinlogRowData ---> MySqlRowData  然后发送出去,比如到kafka
    @Override
    public void onEvent(BinlogRowData eventData) {

        TableTemplate table = eventData.getTable();
        EventType eventType = eventData.getEventType();

        // 包装成最后需要投递的数据
        MySqlRowData rowData = new MySqlRowData();

        rowData.setTableName(table.getTableName());
        rowData.setLevel(eventData.getTable().getLevel());
        OpType opType = OpType.to(eventType);
        rowData.setOpType(opType);

        // 取出模板中该操作对应的字段列表
        List<String> fieldList = table.getOpTypeFieldSetMap().get(opType);
        if (null == fieldList) {
            log.warn("{} not support for {}", opType, table.getTableName());
            return;
        }

        for (Map<String, String> afterMap : eventData.getAfter()) {

            Map<String, String> _afterMap = new HashMap<>();

            for (Map.Entry<String, String> entry : afterMap.entrySet()) {

                String colName = entry.getKey();
                String colValue = entry.getValue();

                _afterMap.put(colName, colValue);
            }

            rowData.getFieldValueMap().add(_afterMap);
        }

        sender.sender(rowData);
    }
}

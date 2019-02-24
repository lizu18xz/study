package com.fayayo.study.binlogTemplate.mysql.listener;


import com.fayayo.study.binlogTemplate.mysql.dto.BinlogRowData;

public interface Ilistener {

    void register();

    void onEvent(BinlogRowData eventData);

}

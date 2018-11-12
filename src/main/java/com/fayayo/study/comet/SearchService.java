package com.fayayo.study.comet;

import com.fayayo.study.comet.form.RentSearch;
import com.fayayo.study.comet.templet.CometIndex;

import java.util.List;

/**
 * @author dalizu on 2018/11/11.
 * @version v1.0
 * @desc 定义接口，es 的操作 服务
 */
public interface SearchService {


    //索引操作
    void createOrUpdate(CometIndex cometIndex);

    void remove(Long cometId);

    //高级 查询 接口
    void query(RentSearch rentSearch);

    //自动补全接口
    List<String> suggest(String prefix);

    //分类聚合查询


    //批量写入接口


    //索引重置 scan写入接口


}

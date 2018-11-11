package com.fayayo.study.comet;

import com.fayayo.study.comet.templet.CometIndex;

/**
 * @author dalizu on 2018/11/11.
 * @version v1.0
 * @desc 定义接口，es 的操作 服务
 */
public interface ISearchService {


    //索引操作
    void createOrUpdate(CometIndex cometIndex);

    void remove();

    //查询


    //自动补全



}

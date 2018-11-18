package com.fayayo.study.comet;

import com.fayayo.study.comet.form.RentSearch;
import com.fayayo.study.comet.templet.CometIndex;
import com.fayayo.study.comet.vo.CometIndexVO;

import java.util.List;
import java.util.Map;

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
    List <CometIndexVO> query(RentSearch rentSearch);

    //自动补全接口
    List<String> suggest(String prefix);

    //分类聚合查询，按照类型聚合
    Map<Object,Long> aggregateCategory();

    //批量写入接口
    void bulk(List<CometIndex>list);

    //索引重置 scan写入接口
    void scrollScan();

    //别名使用

}

package com.fayayo.study.comet.form;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dalizu on 2018/11/12.
 * @version v1.0
 * @desc 请求参数
 *
 * 我们的搜索暂且定一下方式
 * 包括:
 *  标题搜索   （分词）
 *  作者搜索   （精确）
 *  种类搜索   （精确）
 *  描述搜索   （分词）
 *  内容搜索   （分词）
 *  时间范围搜索
 *  是否高亮展示
 *
 */
@Getter
@Setter
public class RentSearch {

    private String keywords;

    private String category;

    private boolean highlight=false;

    private Long startTime;

    private Long endTime;

    private String orderBy = "createTime";

    private String orderDirection = "desc";

    private int start = 0;

    private int size = 5;

}

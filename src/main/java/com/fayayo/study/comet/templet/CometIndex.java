package com.fayayo.study.comet.templet;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @author dalizu on 2018/11/11.
 * @version v1.0
 * @desc 索引字段常亮
 */
@Getter
@Setter
public class CometIndex {


    private Long cometId;          //唯一标示

    private String title;                   //标题

    private String author;                  //原创者

    private String editor;        //责任编辑

    private String category;                //分类

    private String description;             //描述

    private String content;                 //内容

    private Date createTime;             //创建时间

    private List<CometSuggest> suggest;  //固定格式 ES













}

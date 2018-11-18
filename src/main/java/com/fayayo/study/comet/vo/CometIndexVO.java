package com.fayayo.study.comet.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author dalizu on 2018/11/13.
 * @version v1.0
 * @desc
 */
@Getter
@Setter
public class CometIndexVO {

    private Long cometId;          //唯一标示

    private String title;                   //标题

    private String author;                  //原创者

    private String editor;        //责任编辑

    private String category;                //分类

    private String description;             //描述

    private String content;                 //内容

    private String createTime;             //创建时间

    @Override
    public String toString() {
        return "CometIndexVO{" +
                "cometId=" + cometId +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", editor='" + editor + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", content='" + content + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}

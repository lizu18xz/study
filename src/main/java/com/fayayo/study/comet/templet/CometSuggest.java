package com.fayayo.study.comet.templet;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dalizu on 2018/11/11.
 * @version v1.0
 * @desc 自动补全
 */
@Getter
@Setter
public class CometSuggest {

    private String input;

    private int weight=10;//权重

}

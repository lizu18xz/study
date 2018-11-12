package com.fayayo.study.comet;

import com.fayayo.study.comet.SearchService;
import com.fayayo.study.comet.templet.CometIndex;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @author dalizu on 2018/11/12.
 * @version v1.0
 * @desc
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CometServiceTest {

    @Autowired
    private SearchService searchService;

    @Test
    public void createOrUpdate(){

        CometIndex cometIndex=new CometIndex();
        cometIndex.setCometId(2l);
        cometIndex.setAuthor("大王叫我来巡山");
        cometIndex.setCategory("article");
        cometIndex.setContent("hey es ,我是来自西游记的大魔王,来巡山了。");
        cometIndex.setDescription("nothing");
        cometIndex.setEditor("cctv");
        cometIndex.setTitle("西游记故事新编");
        cometIndex.setCreateTime(new Date());
        searchService.createOrUpdate(cometIndex);
    }


    @Test
    public void testAnalyze(){


    }









}

package com.fayayo.study.comet;

import com.fayayo.study.comet.SearchService;
import com.fayayo.study.comet.form.RentSearch;
import com.fayayo.study.comet.templet.CometIndex;
import com.fayayo.study.comet.templet.CometIndexKey;
import com.fayayo.study.comet.vo.CometIndexVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

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
        cometIndex.setCometId(4l);
        cometIndex.setAuthor("大王叫我来巡山");
        cometIndex.setCategory("question");
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


    @Test
    public void testQuery(){

        //构造查询条件
        RentSearch rentSearch=new RentSearch();
        rentSearch.setCategory("article");
        rentSearch.setKeywords("西游记");
        rentSearch.setStart(0);
        rentSearch.setSize(5);
        rentSearch.setHighlight(true);

        List<CometIndexVO>result= searchService.query(rentSearch);

        for(CometIndexVO cometIndexVO:result){
            System.out.println(cometIndexVO.toString());
        }

    }


    @Test
    public void testSuggest(){

        List<String> list=searchService.suggest("西");

        for(String s:list){
            System.out.println(s);

        }
    }


    @Test
    public void testAgg(){

        Map<Object,Long>result=searchService.aggregateCategory();

        for (Map.Entry<Object,Long> entry : result.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }

    }


    @Test
    public void scrollScan(){

        searchService.scrollScan();

    }


    @Test
    public void bulkAdd(){

       List<CometIndex>list=new ArrayList<>();
       int count=0;
       for (int i=0;i<1000;i++){
           CometIndex cometIndex=new CometIndex();
           cometIndex.setCometId((long)i);
           cometIndex.setAuthor("心机boy");
           cometIndex.setCategory("article");
           cometIndex.setContent("人与人讲述的是"+i);
           cometIndex.setDescription("人与人讲述的是......");
           cometIndex.setEditor("cctv");
           cometIndex.setTitle("人与人"+i);
           cometIndex.setCreateTime(new Date());
           list.add(cometIndex);
           count++;
           if (count%100==0) {//批量100
               searchService.bulk(list);
               list.clear();
           }

       }

    }


    @Test
    public void reIndex(){
        searchService.reIndex("comet_v1","comet_v2");
    }



    @Test
    public void test(){
        System.out.println(500%100);
    }



}

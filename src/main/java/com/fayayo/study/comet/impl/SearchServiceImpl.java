package com.fayayo.study.comet.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fayayo.study.comet.ISearchService;
import com.fayayo.study.comet.templet.CometIndex;
import com.fayayo.study.comet.templet.CometIndexKey;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.DeleteByQueryRequestBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author dalizu on 2018/11/11.
 * @version v1.0
 * @desc
 */
@Slf4j
@Service
public class SearchServiceImpl implements ISearchService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestHighLevelClient client;

    @Override
    public void createOrUpdate(CometIndex cometIndex) {

        //判断是否已经存在
        long id=cometIndex.getCometId();

        //构建查询
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termQuery(CometIndexKey.COMET_ID,id));
        searchRequest.source(searchSourceBuilder);

        try {
            SearchResponse searchResponse= client.search(searchRequest);
            long totalHit=searchResponse.getHits().getTotalHits();
            log.info("create  index before totalHit:{}",totalHit);
            boolean success=false;
            if(totalHit==0){
                success=create(cometIndex);
            }else if(totalHit == 1){
                String esId=searchResponse.getHits().getAt(0).getId();//获取ID
                success=update(esId,cometIndex);
            }else {//同一个数据存了多个
                success=deleteAndCreate(totalHit,cometIndex);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private boolean create(CometIndex cometIndex){

        //TODO  自动补全

        try {

            IndexRequest indexRequest=new IndexRequest(CometIndexKey.INDEX_NAME)
                    .source(objectMapper.writeValueAsString(cometIndex), XContentType.JSON);

            IndexResponse response=client.index(indexRequest);

            if(response.getResult() == DocWriteResponse.Result.CREATED){
                return true;
            }else {
                return false;
            }

        }catch (Exception e){
            e.printStackTrace();
            log.info("Error Index with comet:{}",cometIndex.getCometId());
            return false;
        }

    }

    private boolean update(String esId,CometIndex cometIndex){

        //TODO  自动补全

        try {

            UpdateRequest updateRequest = new UpdateRequest(CometIndexKey.INDEX_NAME,null,esId)
                    .doc(objectMapper.writeValueAsString(cometIndex), XContentType.JSON);

            UpdateResponse response=client.update(updateRequest);

            if(response.getResult() == DocWriteResponse.Result.UPDATED){
                return true;
            }else {
                return false;
            }

        }catch (Exception e){
            e.printStackTrace();
            log.info("Error update Index with comet:{}",cometIndex.getCometId());
            return false;
        }

    }

    private boolean deleteAndCreate(long totalHit,CometIndex cometIndex){




        return false;
    }


    @Override
    public void remove() {

    }


}

package com.fayayo.study.comet.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fayayo.study.comet.SearchService;
import com.fayayo.study.comet.form.RentSearch;
import com.fayayo.study.comet.templet.CometIndex;
import com.fayayo.study.comet.templet.CometIndexKey;
import com.fayayo.study.comet.templet.CometSuggest;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequest;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author dalizu on 2018/11/11.
 * @version v1.0
 * @desc
 */
@Slf4j
@Service
public class SearchServiceImpl implements SearchService {

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
            SearchResponse searchResponse= client.search(searchRequest,RequestOptions.DEFAULT);
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
        log.info("create");
        if(!updateSuggest(cometIndex)){
            return false;
        }

        try {

            IndexRequest indexRequest=new IndexRequest(CometIndexKey.INDEX_NAME,CometIndexKey.INDEX_NAME)
                    .source(objectMapper.writeValueAsString(cometIndex), XContentType.JSON);

            IndexResponse response=client.index(indexRequest,RequestOptions.DEFAULT);

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
        log.info("update");
        if(!updateSuggest(cometIndex)){
            return false;
        }

        try {

            UpdateRequest updateRequest = new UpdateRequest(CometIndexKey.INDEX_NAME,CometIndexKey.INDEX_NAME,esId)
                    .doc(objectMapper.writeValueAsString(cometIndex), XContentType.JSON);

            UpdateResponse response=client.update(updateRequest,RequestOptions.DEFAULT);

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

        remove(cometIndex.getCometId());

        return  create(cometIndex);
    }


    private boolean updateSuggest(CometIndex cometIndex){

        //初始化一个analyze
        AnalyzeRequest request = new AnalyzeRequest();
        request.text(cometIndex.getTitle(),cometIndex.getAuthor());//对标题和作者 补全
        request.analyzer("ik_smart");////需要和创建索引时候保持一致

        try {
            AnalyzeResponse response = client.indices().analyze(request, RequestOptions.DEFAULT);
            List<AnalyzeResponse.AnalyzeToken> tokens = response.getTokens();
            if(tokens == null){
                log.warn("Can not analyze token for comet:{}",cometIndex.getCometId());
                return false;
            }
            List<CometSuggest>suggests= Lists.newArrayList();

            for (AnalyzeResponse.AnalyzeToken token:tokens){
                //排序数字类型 & 小于 两个字符的分词结果
                //ES里面的数字类型
                if("<NUM>".equals(token.getTerm()) || token.getTerm().length()<2){
                    continue;
                }

                CometSuggest cometSuggest=new CometSuggest();
                cometSuggest.setInput(token.getTerm());
                suggests.add(cometSuggest);
            }

            cometIndex.setSuggest(suggests);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


    @Override
    public void remove(Long cometId) {

        RestClient restClient=client.getLowLevelClient();//获取low api
        String endPoint = "/" + CometIndexKey.INDEX_NAME +"/" + CometIndexKey.INDEX_NAME+"/_delete_by_query";

        try {
            XContentBuilder builder = XContentFactory.jsonBuilder();

            builder.startObject()
                    .startObject("query")
                    .startObject("term")
                    .field(CometIndexKey.COMET_ID,cometId)
                    .endObject()
                    .endObject()
                    .endObject();

            IndexRequest indexRequest = new IndexRequest().source(builder);

            HttpEntity entity = new NStringEntity(indexRequest.source().utf8ToString(), ContentType.APPLICATION_JSON);

            Request request = new Request(
                    "POST",
                    endPoint);
            request.setEntity(entity);

            Response response=restClient.performRequest(request);

            log.info("response:{}",response.toString());

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void query(RentSearch rentSearch) {

        try {
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

            BoolQueryBuilder boolQuery=QueryBuilders.boolQuery();

            //先过滤分类 ，是否选择了分类

            if(StringUtils.isNoneBlank(rentSearch.getCategory())){
                boolQuery.filter(QueryBuilders.termQuery(CometIndexKey.CATEGORY,rentSearch.getCategory()));
            }

            //过滤日期范围


            //进行检索(精确/分词)



            //指定排序

            log.info("Builder:{}");


            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices("social-*");
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);


        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    @Override
    public List<String> suggest(String prefix) {
        return null;
    }


}

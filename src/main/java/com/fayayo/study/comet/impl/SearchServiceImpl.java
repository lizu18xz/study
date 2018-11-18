package com.fayayo.study.comet.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fayayo.study.comet.SearchService;
import com.fayayo.study.comet.form.RentSearch;
import com.fayayo.study.comet.templet.CometIndex;
import com.fayayo.study.comet.templet.CometIndexKey;
import com.fayayo.study.comet.templet.CometSuggest;
import com.fayayo.study.comet.vo.CometIndexVO;
import com.google.common.collect.Lists;
import com.google.common.primitives.Longs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequest;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.*;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

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
        long id = cometIndex.getCometId();

        //构建查询
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termQuery(CometIndexKey.COMET_ID, id));
        searchRequest.source(searchSourceBuilder);

        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            long totalHit = searchResponse.getHits().getTotalHits();
            log.info("create  index before totalHit:{}", totalHit);
            boolean success = false;
            if (totalHit == 0) {
                success = create(cometIndex);
            } else if (totalHit == 1) {
                String esId = searchResponse.getHits().getAt(0).getId();//获取ID
                success = update(esId, cometIndex);
            } else {//同一个数据存了多个
                success = deleteAndCreate(totalHit, cometIndex);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private boolean create(CometIndex cometIndex) {
        log.info("create");
        if (!updateSuggest(cometIndex)) {
            return false;
        }

        try {

            IndexRequest indexRequest = new IndexRequest(CometIndexKey.INDEX_NAME, CometIndexKey.INDEX_NAME)
                    .source(objectMapper.writeValueAsString(cometIndex), XContentType.JSON);

            IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);

            if (response.getResult() == DocWriteResponse.Result.CREATED) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("Error Index with comet:{}", cometIndex.getCometId());
            return false;
        }

    }

    private boolean update(String esId, CometIndex cometIndex) {
        log.info("update");
        if (!updateSuggest(cometIndex)) {
            return false;
        }

        try {

            UpdateRequest updateRequest = new UpdateRequest(CometIndexKey.INDEX_NAME, CometIndexKey.INDEX_NAME, esId)
                    .doc(objectMapper.writeValueAsString(cometIndex), XContentType.JSON);

            UpdateResponse response = client.update(updateRequest, RequestOptions.DEFAULT);

            if (response.getResult() == DocWriteResponse.Result.UPDATED) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("Error update Index with comet:{}", cometIndex.getCometId());
            return false;
        }

    }

    private boolean deleteAndCreate(long totalHit, CometIndex cometIndex) {

        remove(cometIndex.getCometId());

        return create(cometIndex);
    }


    private boolean updateSuggest(CometIndex cometIndex) {

        //初始化一个analyze
        AnalyzeRequest request = new AnalyzeRequest();
        request.text(cometIndex.getTitle(), cometIndex.getAuthor());//对标题和作者 补全
        request.analyzer("ik_smart");////需要和创建索引时候保持一致

        try {
            AnalyzeResponse response = client.indices().analyze(request, RequestOptions.DEFAULT);
            List<AnalyzeResponse.AnalyzeToken> tokens = response.getTokens();
            if (tokens == null) {
                log.warn("Can not analyze token for comet:{}", cometIndex.getCometId());
                return false;
            }
            List<CometSuggest> suggests = Lists.newArrayList();

            for (AnalyzeResponse.AnalyzeToken token : tokens) {
                //排序数字类型 & 小于 两个字符的分词结果
                //ES里面的数字类型
                if ("<NUM>".equals(token.getTerm()) || token.getTerm().length() < 2) {
                    continue;
                }

                CometSuggest cometSuggest = new CometSuggest();
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

        RestClient restClient = client.getLowLevelClient();//获取low api
        String endPoint = "/" + CometIndexKey.INDEX_NAME + "/" + CometIndexKey.INDEX_NAME + "/_delete_by_query";

        try {
            XContentBuilder builder = XContentFactory.jsonBuilder();

            builder.startObject()
                    .startObject("query")
                    .startObject("term")
                    .field(CometIndexKey.COMET_ID, cometId)
                    .endObject()
                    .endObject()
                    .endObject();

            IndexRequest indexRequest = new IndexRequest().source(builder);

            HttpEntity entity = new NStringEntity(indexRequest.source().utf8ToString(), ContentType.APPLICATION_JSON);

            Request request = new Request(
                    "POST",
                    endPoint);
            request.setEntity(entity);

            Response response = restClient.performRequest(request);

            log.info("response:{}", response.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<CometIndexVO> query(RentSearch rentSearch) {

        //TODO 考虑加入fuzziness 特性,拼写错误 也可以查询
        try {
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

            //先过滤分类 ，是否选择了分类(文章，问题，电影介绍...)

            if (StringUtils.isNoneBlank(rentSearch.getCategory())) {
                boolQuery.filter(QueryBuilders.termQuery(CometIndexKey.CATEGORY, rentSearch.getCategory()));
            }

            //过滤日期范围

            if (rentSearch.getStartTime() != null || rentSearch.getEndTime() != null) {
                RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(CometIndexKey.CREATE_TIME);
                if (rentSearch.getStartTime() != null) {
                    rangeQueryBuilder.gte(rentSearch.getStartTime());
                }

                if (rentSearch.getEndTime() != null) {
                    rangeQueryBuilder.lte(rentSearch.getEndTime());
                }
                boolQuery.filter(rangeQueryBuilder);
            }

            //如果 所有的 内容都不包括keyword,是否返回符合其他条件的数据。
            BoolQueryBuilder keyWordBoolQuery=QueryBuilders.boolQuery();

            //进行检索(精确/分词)  标题  权重 高于其他
            keyWordBoolQuery.should(
                    QueryBuilders.matchQuery(CometIndexKey.TITLE, rentSearch.getKeywords()).boost(2.0f)
            );

            keyWordBoolQuery.should(QueryBuilders.multiMatchQuery(rentSearch.getKeywords(),
                    CometIndexKey.DESCRIPTION,
                    CometIndexKey.CONTENT,
                    CometIndexKey.AUTHOR
            ));

            boolQuery.must(keyWordBoolQuery);

            //加入到builder
            sourceBuilder.query(boolQuery);

            //是否高亮
            boolean isHighlight=rentSearch.isHighlight();
            if(isHighlight){
                //暂时标题高亮展示
                HighlightBuilder highlightBuilder = new HighlightBuilder();
                HighlightBuilder.Field highlightTitle =
                        new HighlightBuilder.Field(CometIndexKey.TITLE);
                highlightTitle.highlighterType("unified");
                highlightBuilder.field(highlightTitle);
                sourceBuilder.highlighter(highlightBuilder);
            }

            //指定排序

            sourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));//得分
            sourceBuilder.sort(new FieldSortBuilder(CometIndexKey.CREATE_TIME).order(SortOrder.DESC));//时间

            //返回指定的字段(如果关联了 数据库  可以只返回id,然后 通过id 去查询其他信息)
            /*sourceBuilder.fetchSource(new String[]{CometIndexKey.COMET_ID,CometIndexKey.TITLE}, null);*/

            //分页
            sourceBuilder.from(rentSearch.getStart());
            sourceBuilder.size(rentSearch.getSize());
            log.info("Builder:{}", sourceBuilder.toString());
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices(CometIndexKey.INDEX_NAME);
            searchRequest.source(sourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            RestStatus status = searchResponse.status();
            List<CometIndexVO> list = Lists.newArrayList();
            if (status == RestStatus.OK) {

                TimeValue took = searchResponse.getTook();

                log.info("[请求花费的毫秒]:{},({}),{}", took, took.getMillis(), took.getSeconds());
                SearchHits hits = searchResponse.getHits();
                SearchHit[] searchHits = hits.getHits();

                long totalRecord=hits.getTotalHits();
                log.info("[请求返回总数]:{}",totalRecord);

                CometIndexVO cometIndexVO = null;
                for (SearchHit hit : searchHits) {
                    // do something with the SearchHit
                    //处理返回结果:如果数据和mysql或者其他数据库同步，可以只返回id,然后 其他信息从mysql查询。此处不引入mysql,所有所有内容从es中返回获取
                    cometIndexVO = new CometIndexVO();
                    convert(hit,cometIndexVO,isHighlight);
                    list.add(cometIndexVO);
                }

                return list;
            } else {
                log.warn("search status is not Ok");
                return list;
            }

        } catch (IOException e) {
            e.printStackTrace();
            log.error("search error");
            return null;
        }

    }

    @Override
    public List<String> suggest(String prefix) {

        try {
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            CompletionSuggestionBuilder completionSuggestionBuilder=SuggestBuilders.completionSuggestion("suggest").prefix(prefix).size(5);

            SuggestBuilder suggestBuilder = new SuggestBuilder();
            suggestBuilder.addSuggestion("search_autoComplete", completionSuggestionBuilder);
            searchSourceBuilder.suggest(suggestBuilder);

            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices(CometIndexKey.INDEX_NAME);
            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            Suggest suggest = searchResponse.getSuggest();
            //过滤避免重复
            int maxSuggest=0;
            Set<String> suggestSet=new HashSet<String>();

            CompletionSuggestion completionSuggestion = suggest.getSuggestion("search_autoComplete");
            for (CompletionSuggestion.Entry entry : completionSuggestion.getEntries()) {

                if(entry.getOptions().isEmpty()){
                    continue;
                }

                for (CompletionSuggestion.Entry.Option option : entry) {
                    String suggestText = option.getText().string();
                    if(suggestSet.contains(suggestText)){
                        continue;
                    }
                    suggestSet.add(suggestText);
                    maxSuggest++;
                }

                if(maxSuggest>5){
                    break;
                }
            }
            //返回补全的数据
            List <String>stringList=Lists.newArrayList(suggestSet.toArray(new String[]{}));
            return stringList;
        }catch (Exception e){
            e.printStackTrace();
            log.error("suggest error");
            return null;
        }
    }

    @Override
    public Map <Object,Long> aggregateCategory() {
        //按照分类 聚合 获取每种分类的个数

       Map <Object,Long>result=new HashMap<>();

       try {
           SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

           TermsAggregationBuilder aggregation = AggregationBuilders.terms(CometIndexKey.CATEGORY_AGG)
                   .field(CometIndexKey.CATEGORY).order((BucketOrder.aggregation("_count", false)));

           //聚合
           searchSourceBuilder.aggregation(aggregation).size(0);
           SearchRequest searchRequest = new SearchRequest();
           searchRequest.indices(CometIndexKey.INDEX_NAME);
           searchRequest.source(searchSourceBuilder);
           SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

           Aggregations aggregations = searchResponse.getAggregations();
           Terms byCategoryAggregation = aggregations.get(CometIndexKey.CATEGORY_AGG);

           if(byCategoryAggregation.getBuckets()!=null && !byCategoryAggregation.getBuckets().isEmpty()){

               List <? extends Terms.Bucket>list=byCategoryAggregation.getBuckets();

               for (Terms.Bucket bucket:list){
                   bucket.getDocCount();
                   bucket.getKey();
                   log.info("key:{},value:{}",bucket.getKey(),bucket.getDocCount());
                   result.put(bucket.getKey(),bucket.getDocCount());
               }
           }

       }catch (Exception e){
           log.error("agg error");
           return result;
       }

       return result;
    }

    @Override
    public void bulk(List<CometIndex> list) {

        //批量插入数据
        BulkRequest request = new BulkRequest();

        try {
            for (CometIndex cometIndex:list){
                request.add(new IndexRequest(CometIndexKey.INDEX_NAME, CometIndexKey.INDEX_NAME)
                        .source(objectMapper.writeValueAsString(cometIndex), XContentType.JSON));
            }

            BulkResponse bulkResponse = client.bulk(request, RequestOptions.DEFAULT);

            //The Bulk response provides a method to quickly check if one or more operation has failed:
            if (bulkResponse.hasFailures()) {
               log.info("all success");
            }
            TimeValue took = bulkResponse.getTook();
            log.info("[批量新增花费的毫秒]:{},({}),{}", took, took.getMillis(), took.getSeconds());
            //所有操作结果进行迭代
            /*for (BulkItemResponse bulkItemResponse : bulkResponse) {
                if (bulkItemResponse.isFailed()) {
                    BulkItemResponse.Failure failure = bulkItemResponse.getFailure();
                }
            }*/
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void scrollScan() {
        //scroll 查询  批量插入
        try {

            final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1L));
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices(CometIndexKey.INDEX_NAME);//设置指定的索引
            searchRequest.scroll(scroll);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.matchAllQuery());//查询所有
            searchSourceBuilder.size(1000);
            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            String scrollId = searchResponse.getScrollId();//获取第一次scrollId

            SearchHits searchHits=searchResponse.getHits();
            log.info("scrollId:{},total:{}",scrollId,searchHits.getTotalHits());
            SearchHit[] hits=searchHits.getHits();

            while (hits != null && hits.length > 0) {

                for (SearchHit hit : hits) {
                    // do something with the SearchHit
                    Map<String, Object> sourceAsMap =  hit.getSourceAsMap();
                    log.info("title:{}",sourceAsMap.get(CometIndexKey.TITLE));
                }

                log.info("scrollId:{}",scrollId);
                SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);//根据scrollId检索
                scrollRequest.scroll(scroll);
                searchResponse = client.scroll(scrollRequest, RequestOptions.DEFAULT);
                scrollId = searchResponse.getScrollId();//获取下一次scrollId
                log.info("scrollId:{}",scrollId);
                hits = searchResponse.getHits().getHits();

            }

            //release the search context
            ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
            clearScrollRequest.addScrollId(scrollId);
            ClearScrollResponse clearScrollResponse = client.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
            boolean succeeded = clearScrollResponse.isSucceeded();

            log.info("ScrollRequest result:{}",succeeded);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private void convert(SearchHit hit,CometIndexVO cometIndexVO,boolean isHighlight){

        Map<String, Object> sourceAsMap = hit.getSourceAsMap();

        Long cometId = Longs.tryParse(String.valueOf(sourceAsMap.get(CometIndexKey.COMET_ID)));
        String title = (String) sourceAsMap.get(CometIndexKey.TITLE);
        String category = (String) sourceAsMap.get(CometIndexKey.CATEGORY);
        String author = (String) sourceAsMap.get(CometIndexKey.AUTHOR);
        String editor = (String) sourceAsMap.get(CometIndexKey.EDITOR);
        String description = (String) sourceAsMap.get(CometIndexKey.DESCRIPTION);
        String content = (String) sourceAsMap.get(CometIndexKey.CONTENT);
        String createTime = (String) sourceAsMap.get(CometIndexKey.CREATE_TIME);

        cometIndexVO.setCometId(cometId);
        cometIndexVO.setCategory(category);
        cometIndexVO.setAuthor(author);
        cometIndexVO.setContent(content);
        cometIndexVO.setEditor(editor);
        cometIndexVO.setCreateTime(createTime);
        cometIndexVO.setDescription(description);
        cometIndexVO.setTitle(title);

        //是否需要高亮展示(暂时只设置了title字段)
        if(isHighlight){
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if(highlightFields!=null&&highlightFields.size()>0){
                HighlightField highlight = highlightFields.get(CometIndexKey.TITLE);
                Text[] fragments = highlight.fragments();
                String fragmentString = fragments[0].string();
                log.info("fragmentString:{}",fragmentString);
                cometIndexVO.setTitle(fragmentString);
            }
        }

    }

}

package com.fayayo.study.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dalizu on 2018/11/11.
 * @version v1.0
 * @desc
 */
@Slf4j
@Configuration
public class ElasticSearchConfig implements DisposableBean{


    @Bean
    public RestHighLevelClient client(){
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("192.168.88.130", 9200, "http")));

        return client;
    }


    @Override
    public void destroy() throws Exception {
        log.info("关闭连接");
        client().close();
    }
}

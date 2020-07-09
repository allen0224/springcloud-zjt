package com.meorient.mebuyerdiscovery.elasticsearch.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.ResultsMapper;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;

/**
 * @description:
 * @author: zjt
 * @time: 2020/5/18 20:44
 */
@Configuration
public class ElasticsearchConfig {

    /**
     * 使用sprnig-data-elasticsearch 自动提供的 RestHighLevelClient等构建 ElasticsearchRestTemplate
     *
     * @param client
     * @param converter
     * @param resultsMapper
     * @return
     */
    @Bean
    public ElasticsearchRestTemplate elasticsearchTemplate(RestHighLevelClient client, ElasticsearchConverter converter,
                                                           ResultsMapper resultsMapper) {
        return new ElasticsearchRestTemplate(client, converter, resultsMapper);
    }
}

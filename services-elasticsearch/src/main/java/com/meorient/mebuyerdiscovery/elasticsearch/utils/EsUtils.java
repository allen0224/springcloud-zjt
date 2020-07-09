package com.meorient.mebuyerdiscovery.elasticsearch.utils;

import com.meorient.mebuyerdiscovery.elasticsearch.doc.LadingBillSimpleDocument;
import com.meorient.mebuyerdiscovery.elasticsearch.doc.OneRecordDocument;
import lombok.extern.slf4j.Slf4j;
import com.meorient.mebuyerdiscovery.elasticsearch.MeorientdfServicesElasticsearchApplication;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.meorient.mebuyerdiscovery.elasticsearch.constant.ElasticSearchConstant.BILL_STANDARD_INDEX;
import static com.meorient.mebuyerdiscovery.elasticsearch.constant.ElasticSearchConstant.BILL_STANDARD_INDEX_TYPE;


/**
 * @description:
 * @author: zjt
 * @time: 2020/5/18 19:49
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes= MeorientdfServicesElasticsearchApplication.class)
@Slf4j
public  class EsUtils {
    @Resource
    private  ElasticsearchRestTemplate elasticsearchTemplate;


    public  void moveEsData(Integer page, Integer size){
        String uuid= String.valueOf(UUID.randomUUID());
        long startTime=System.currentTimeMillis();
        try {
            NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                    .withPageable(PageRequest.of(page,size))
                    //当前公司
                    .withQuery(QueryBuilders.matchAllQuery())
                    .build();
            List<OneRecordDocument> list=elasticsearchTemplate.queryForList(searchQuery, OneRecordDocument.class);
            List<LadingBillSimpleDocument> ladingBillList = new ArrayList<LadingBillSimpleDocument>();
            List<IndexQuery> collect = list.stream().map(one -> {
                LadingBillSimpleDocument ladingBill=new LadingBillSimpleDocument();
                BeanUtils.copyProperties(one, ladingBill);
                return new IndexQueryBuilder().withObject(ladingBill).withIndexName(BILL_STANDARD_INDEX).withType(BILL_STANDARD_INDEX_TYPE).build();
            }).collect(Collectors.toList());
            log.info(uuid+" moveEsData query pagesize:"+String.valueOf(collect.size()));

            elasticsearchTemplate.bulkIndex(collect, BulkOptions.defaultOptions());
            calculateTime(startTime,uuid+" moveEsData page:"+page+" size:"+size);
        }catch (Exception e){
            log.error("moveEsDataException page:"+page+" size:"+size,e);
        }


    }
    public static void calculateTime(long startTime, String query) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:S");
        Date startDate = new Date(startTime);
        Object object = null;
        try {
            log.info("-------------------------{}开始日志:{}-------------------------",query,simpleDateFormat.format(startDate));
            log.info("-------------------------{}结束日志:{}------------处理时长:{}-------------",query,simpleDateFormat.format(new Date()),System.currentTimeMillis() - startTime);
        } catch (Throwable throwable) {
            log.error(throwable.getMessage(), throwable);
        }
    }
}

package com.meorient.mebuyerdiscovery.elasticsearch.statistics;

import cn.hutool.core.util.NumberUtil;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.meorient.mebuyerdiscovery.elasticsearch.doc.CompanyPartStatistics;
import com.meorient.mebuyerdiscovery.elasticsearch.doc.CompanyStatistics;
import com.meorient.mebuyerdiscovery.elasticsearch.doc.CountryStatistics;
import com.meorient.mebuyerdiscovery.elasticsearch.mongdb.data.Company;
import com.meorient.mebuyerdiscovery.elasticsearch.mongdb.service.OneRecordRecordMongo;
import com.meorient.mebuyerdiscovery.elasticsearch.utils.CollectionUtil;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.ParsedSum;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.meorient.mebuyerdiscovery.elasticsearch.constant.ElasticSearchConstant.BILL_STANDARD_INDEX;
import static com.meorient.mebuyerdiscovery.elasticsearch.constant.ElasticSearchConstant.BILL_STANDARD_INDEX_TYPE;
import static com.meorient.mebuyerdiscovery.elasticsearch.doc.CompanyStatistics.*;
import static com.meorient.mebuyerdiscovery.elasticsearch.doc.CountryStatistics.*;
import static com.meorient.mebuyerdiscovery.elasticsearch.doc.Statistics.*;
import static java.math.RoundingMode.HALF_UP;
import static org.elasticsearch.action.search.SearchType.DFS_QUERY_THEN_FETCH;

@Component
public class StatisticsService {

    private Logger logger = LoggerFactory.getLogger(StatisticsService.class);

    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;

    @Autowired
    private OneRecordRecordMongo oneRecordRecordMongo;

    private ThreadPoolExecutor threadPoolExecutor = null;
    /**
     * 统计公司
     * @param startId
     * @param endId
     * @param count
     */
    public void statisticsCompany(Integer startId, Integer endId, Integer count) {
        ThreadFactory build = new ThreadFactoryBuilder().setNameFormat("statisticsCompany-ThreadPool-%d").setUncaughtExceptionHandler((t, e) -> logger.error(t.getName(), e)).build();
        threadPoolExecutor = new ThreadPoolExecutor(12, 12,
                60000L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(), build);
        List<Thread> list = new ArrayList<>();
        Integer a = startId;//开始数量
        Integer c = endId;//结束数量
        //相差数
        Integer d = c - a;
        //6个线程
        Integer b = count;
        for (int i = 0; i < b; i++) {
            Integer start = i * (d / b) + a;
            Integer end = (i + 1) * (d / b) + a;
            if (i == (b - 1)) {
                end = c;
            }
            Integer finalEnd = end;
            logger.info(">>>>>>>>>statisticsCompany start:"+start+"   finalEnd:"+finalEnd);
            Thread t = new Thread(() -> {
                stactistics(start,5000,Long.parseLong(finalEnd.toString()));
            });
//            list.add(t);
//            t.start();
            threadPoolExecutor.execute(t);
        }

//        try {
//            list.get(0).join();
//            list.get(1).join();
//            list.get(2).join();
//            list.get(3).join();
//            list.get(4).join();
//            list.get(5).join();
//
//        } catch (Exception e) {
//            logger.error("失败", e);
//        }
        logger.info("结束");
    }

    /**
     * 分页统计公司
     * @param skip
     * @param step
     * @param count
     */
    public void stactistics(int skip,int step,Long count){
        do {
            if(skip>=count){
                return;
            }
            List<Company> companyList=new ArrayList<>();
             if(skip+step>=count){
                 companyList = company(skip, (int) (count-skip));
             }else{
                 companyList = company(skip, step);
             }
            if(CollectionUtil.isEmpty(companyList)){
                return;
            }
            Long startTime=System.currentTimeMillis();
            for (Company company : companyList) {
              companyStactistics(company);
//              queryStactistics(company);
            }
            logger.info("-------------------------queryStactistics处理时长:{}-------------",System.currentTimeMillis() - startTime);
//            bulkQueryStactistics(companyList);
            skip=skip+step;
        }while (true);
    }

    private void queryStactistics(Company company) {
        NativeSearchQuery searchQuery = companyCondition(company.getCompanyName());
        TermsAggregationBuilder code = AggregationBuilders.terms(code_key).field(HSCODE + ".keyword");
        searchQuery.addAggregation(code);
        Long startTime=System.currentTimeMillis();
        logger.info(">>>>>>>>>>>>>统计公司分查询语句"+searchQuery.getQuery().toString()+searchQuery.getAggregations().toString());
//            Aggregations query = elasticsearchTemplate.query(searchQuery, SearchResponse::getAggregations);
        Map<String,Object> map = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Map<String, Object>>() {
            @Override
            public Map<String, Object> extract(SearchResponse response) {
                Map<String,Object> resultMap=new HashMap<String,Object>();
                Aggregations query =response.getAggregations();
                Long aLong=response.getHits().getTotalHits();
                resultMap.put("aLong",aLong);
                resultMap.put("query",query);
                return resultMap;
            }
        });
        calculateTime(startTime,company.getId() +"名称:"+company.getCompanyName()+"     查询分公司信息:" );
    }
//    private void bulkQueryStactistics(List<Company> company) {
//        NativeSearchQuery searchQuery = companyCondition(companyName);
//        TermsAggregationBuilder code = AggregationBuilders.terms(code_key).field(HSCODE + ".keyword");
//        searchQuery.addAggregation(code);
//        Long startTime=System.currentTimeMillis();
//        logger.info(">>>>>>>>>>>>>统计公司分查询语句"+searchQuery.getQuery().toString()+searchQuery.getAggregations().toString());
////            Aggregations query = elasticsearchTemplate.query(searchQuery, SearchResponse::getAggregations);
//        Map<String,Object> map = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Map<String, Object>>() {
//            @Override
//            public Map<String, Object> extract(SearchResponse response) {
//                Map<String,Object> resultMap=new HashMap<String,Object>();
//                Aggregations query =response.getAggregations();
//                Long aLong=response.getHits().getTotalHits();
//                resultMap.put("aLong",aLong);
//                resultMap.put("query",query);
//                return resultMap;
//            }
//        });
//        calculateTime(startTime,companyID +"名称:"+company.getCompanyName()+"     查询分公司信息:" );
//
//    }

    /**
     * 分页查询公司
     * @param skip
     * @param limit
     * @return
     */
    public List<Company> company(Integer skip, Integer limit) {
        List<Company> companies = oneRecordRecordMongo.companyPage(skip, limit);
        return companies;
    }


    /**
     * 从es当中查询所有国家
     * @return
     */
    public List<String> allCountry() {
        Set<String> allCountry = new HashSet<>();
        TermsAggregationBuilder field = AggregationBuilders.terms(COUNTRY).field(COUNTRY + ".keyword");
        //查询进口公司国家名称
        NativeSearchQuery searchIn = new NativeSearchQueryBuilder()
                .withPageable(EmptyPage.INSTANCE)
                .addAggregation(field)
                //当前公司
                .build();
        ParsedStringTerms in = elasticsearchTemplate.query(searchIn, response -> response.getAggregations().get(COUNTRY));
        for (Terms.Bucket bucket : in.getBuckets()) {
            String key = (String) bucket.getKey();
            allCountry.add(key);
        }
        TermsAggregationBuilder outfield = AggregationBuilders.terms(COUNTRY_EXPORT).field(COUNTRY_EXPORT + ".keyword");
        NativeSearchQuery searchOut = new NativeSearchQueryBuilder()
                .withPageable(EmptyPage.INSTANCE)
                .addAggregation(outfield)
                //当前公司
                .build();
        ParsedStringTerms out = elasticsearchTemplate.query(searchIn, response -> response.getAggregations().get(COUNTRY_EXPORT));
        for (Terms.Bucket bucket : out.getBuckets()) {
            String key = (String) bucket.getKey();
            allCountry.add(key);
        }
        List<String> countryList = new ArrayList<>(allCountry);
        return countryList;
    }

    /**
     * 获取公司的国家
     *
     * @param companyName
     * @return
     */
    public String companyCountry(String companyName) {
        NativeSearchQuery searchQuery = companyCondition(companyName);
        searchQuery.addAggregation(AggregationBuilders.terms(COUNTRY).field(COUNTRY + ".keyword"));
        Aggregations query = elasticsearchTemplate.query(searchQuery, response -> response.getAggregations());
        ParsedStringTerms country = query.get(COUNTRY);
        List<? extends Terms.Bucket> collect = country.getBuckets().stream().sorted((o1, o2) -> (int) (o1.getDocCount() - o2.getDocCount())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(collect)) {
            return "UNKNOWN";
        }
        return (String) collect.get(0).getKey();
    }

    //生成公司记录
    public CompanyStatistics companyStactistics(Company company) {
        try {
            Long startTime=null;
            Long companyStactisticsStartTime=System.currentTimeMillis();
            logger.info(" >>>>>>>>>>>>>>>>>>>>companyStactistics Start {}" ,companyStactisticsStartTime);
            String companyName = company.getCompanyName();
            String companyID = company.getId();
//            NativeSearchQuery nativeSearchQuery = companyCondition(companyName);
//            startTime=System.currentTimeMillis();
//            Long aa = elasticsearchTemplate.query(nativeSearchQuery, response -> response.getHits().getTotalHits());
//            calculateTime(startTime,company.getId() +">>>>>名称:"+companyName+"  query total account 查询总条数:"+aa);
            NativeSearchQuery searchQuery = companyCondition(companyName);
            TermsAggregationBuilder code = AggregationBuilders.terms(code_key).field(HSCODE + ".keyword");
            searchQuery.addAggregation(code);
            startTime=System.currentTimeMillis();
            logger.info(">>>>>>>>>>>>>统计公司分查询语句"+searchQuery.getQuery().toString()+searchQuery.getAggregations().toString());
//            Aggregations query = elasticsearchTemplate.query(searchQuery, SearchResponse::getAggregations);
            Map<String,Object> map = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Map<String, Object>>() {
                @Override
                public Map<String, Object> extract(SearchResponse response) {
                    Map<String,Object> resultMap=new HashMap<String,Object>();
                    Aggregations query =response.getAggregations();
                    Long aLong=response.getHits().getTotalHits();
                    resultMap.put("aLong",aLong);
                    resultMap.put("query",query);
                    return resultMap;
                }
            });
            calculateTime(startTime,companyID +"名称:"+company.getCompanyName()+"     查询分公司信息:" );
            Long aLong=new Long(0);
            if (map != null) {
                aLong= (Long) map.get("aLong");
            }
            logger.info(">>>>>>>>>>>>>"+company.getId()+">>>>>新查询条数"+aLong);
            Aggregations query= (Aggregations) map.get("query");
            if(aLong<=0){
                logger.info(" company {} hasnot billading" ,company);
                return null;
            }
            CompanyStatistics companyStatistics = new CompanyStatistics();
            companyStatistics.setCompanyId(companyID);
            companyStatistics.setCompanyName(companyName);
            //设置公司国家
            String country = companyCountry(companyName);
            companyStatistics.setCountry(country);
            //查询公司总采购数
//            startTime=System.currentTimeMillis();
//            NativeSearchQuery searchQuery = companyCondition(companyName);
//            Long totalPurchases = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Long>() {
//                @Override
//                public Long extract(SearchResponse response) {
//                    return response.getHits().getTotalHits();
//                }
//            });
//            calculateTime(startTime,company.getId() +"名称:"+companyName+"  query company totalbuy  account 查询公司总采购数:");
            companyStatistics.setTotalPurchases(aLong);
            startTime=System.currentTimeMillis();
            ProductVlUsd ProductVlUsd = companyProductVlUsd(companyName);
            calculateTime(startTime,company.getId() +"名称:"+companyName+"  companyProductVlUsd 查询公司采购金额:");
            //设置金额
            companyStatistics.setProductVlUsdCif(ProductVlUsd.getProductVlUsdCif());
            companyStatistics.setProductVlUsd(ProductVlUsd.getProductVlUsd());
            companyStatistics.setProductVlUsdFob(ProductVlUsd.getProductVlUsdFob());
            //查询从中国采购数量
            startTime=System.currentTimeMillis();
            Long purchasesINChina = purchasesINChina(companyName);
            calculateTime(startTime,company.getId() +"名称:"+companyName+"   purchasesINChina 查询从中国采购数量:");
            companyStatistics.setPurchasesINChina(purchasesINChina);
            if (purchasesINChina > 0) {
                companyStatistics.setInChina(true);
            }
//            //查询从中国的采购金额
//            startTime=System.currentTimeMillis();
//            ProductVlUsd chinaProductVlUsd = companyChinaProductVlUsd(companyName);
//            calculateTime(startTime,company.getId() +"名称:"+companyName+"   companyChinaProductVlUsd 查询从中国的采购金额:");
//            //设置中国的采购金额
//            companyStatistics.setChinaProductVlUsdCif(chinaProductVlUsd.getProductVlUsdCif());
//            companyStatistics.setChinaProductVlUsd(chinaProductVlUsd.getProductVlUsd());
//            companyStatistics.setChinaProductVlUsdFob(chinaProductVlUsd.getProductVlUsdFob());
            //公司总供应商数量
//            startTime=System.currentTimeMillis();
//            Long companySuppliers = companySuppliers(companyName);
//            calculateTime(startTime,company.getId() +"名称:"+companyName+"   companySuppliers 公司总供应商数量:");
//            companyStatistics.setTotalSuppliers(companySuppliers);
//            //中国供应商数量
//            startTime=System.currentTimeMillis();
//            Long chinaSuppliers = companyChinaSuppliers(companyName);
//            calculateTime(startTime,company.getId() +"名称:"+companyName+"   companyChinaSuppliers 中国供应商数量:");
//            companyStatistics.setChinaSuppliers(chinaSuppliers);
//            if (chinaSuppliers > 0&&companySuppliers!=0) {
//                float chinaRatio = Double.valueOf(NumberUtil.div(chinaSuppliers.longValue(), companySuppliers.longValue(), 2, HALF_UP)).floatValue();
//                companyStatistics.setChinaRatio((long) chinaRatio);
//            }else{
//                logger.error("{}获取中国供应商占比异常（该公司中国供应商数/该公司总供应商数）*100%",companyStatistics.getCompanyId());
//            }
            //公司各个部分汇总
            startTime=System.currentTimeMillis();
            List<CompanyPartStatistics> companyPartStatistics = companyPartStatistics(companyStatistics,query);
            calculateTime(startTime,company.getId() +"名称:"+companyName+"   companyPartStatistics 公司各个部分汇总:");
//            long peer = companyPartStatistics.stream().mapToLong(CompanyPartStatistics::getPeer).sum();
//            long sum = companyPartStatistics.stream().mapToLong(CompanyPartStatistics::getDomesticPeers).sum();
//            companyStatistics.setPeer(peer);
//            companyStatistics.setDomesticPeers(sum);
//
//            if (peer > 0&&sum!=0) {
//                float div = Double.valueOf(NumberUtil.div(peer, sum, 2, HALF_UP)).floatValue();
//                companyStatistics.setDomesticPeersRatio(div);
//            }else{
//                logger.error("{}获取本国同行占比异常 （采购相同编码的本国同行/全球同行数）*100%",companyStatistics.getCompanyId());
//            }
            startTime=System.currentTimeMillis();
            List<IndexQuery> collect = companyPartStatistics.stream().map(one -> {
                return new IndexQueryBuilder().withObject(one).withIndexName(COMPANY_PART_INDEX).withType(COMPANY_PART_TYPE).build();
            }).collect(Collectors.toList());
            elasticsearchTemplate.bulkIndex(collect,BulkOptions.defaultOptions());
            calculateTime(startTime,"索引名称:"+COMPANY_PART_INDEX+"   bulkIndex COMPANY_PART_INDEX 公司分批量插入文档:");
            startTime=System.currentTimeMillis();
            IndexQuery build = new IndexQueryBuilder().withObject(companyStatistics).withIndexName(COUNTRY_INDEX).withType(COUNTRY_TYPE).build();
            elasticsearchTemplate.index(build);
            calculateTime(startTime,"索引名称:"+COUNTRY_INDEX+"   index COMPANY_PART_INDEX 公司总插入文档:");
            calculateTime(companyStactisticsStartTime,">>>>>>>>>>>>>>>>>>>>companyStactistics end");
            return companyStatistics;
        }catch (Exception e) {
            logger.error("companyStactisticsinsertfail"+company.getId()+e.getMessage(), e);
            return null;
        }
    }


    /**
     * 公司在中国的进口金额
     *
     * @param companyName
     * @return
     */
    public ProductVlUsd companyChinaProductVlUsd(String companyName) {
        ProductVlUsd ProductVlUsdSum = new ProductVlUsd();
        BoolQueryBuilder china = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery(IMPORTER, companyName))
                .must((QueryBuilders.matchQuery(COUNTRY_EXPORT, CHINA)));

        //查询从中国进口的数量
        NativeSearchQuery chinaQuery = new NativeSearchQueryBuilder()
                .withSearchType(DFS_QUERY_THEN_FETCH)
                .withPageable(EmptyPage.INSTANCE)
                //当前公司
                .withQuery(QueryBuilders.boolQuery().filter((china)))
                .build();

        SumAggregationBuilder field = AggregationBuilders.sum(PRODUCT_VL_USD_CIF_SUM).field(PRODUCT_VL_USD_CIF);
        //查询采购金额
        chinaQuery.addAggregation(field);
        SumAggregationBuilder field1 = AggregationBuilders.sum(PRODUCT_VL_USD_SUM).field(PRODUCT_VL_USD);
        chinaQuery.addAggregation(field1);
        SumAggregationBuilder field2 = AggregationBuilders.sum(PRODUCT_VL_USD_FOB_SUM).field(PRODUCT_VL_USD_FOB);
        chinaQuery.addAggregation(field2);
        logger.info(">>>>>>>>>>>>>公司在中国的进口金额查询语句"+chinaQuery.getQuery().toString());
        Aggregations aggregations = elasticsearchTemplate.query(chinaQuery, response -> response.getAggregations());
        logger.info(">>>>>>>>>>>>>公司在中国的进口金额查询结果"+aggregations.toString());
        ParsedSum ProductVlUsdCif;
        ParsedSum ProductVlUsd;
        ParsedSum ProductVlUsdFob;
        ProductVlUsdCif = aggregations.get(PRODUCT_VL_USD_CIF_SUM);
        ProductVlUsdSum.setProductVlUsdCif(BigDecimal.valueOf(ProductVlUsdCif.getValue()));
        ProductVlUsd = aggregations.get(PRODUCT_VL_USD_SUM);
        ProductVlUsdSum.setProductVlUsd(BigDecimal.valueOf(ProductVlUsd.getValue()));
        ProductVlUsdFob = aggregations.get(PRODUCT_VL_USD_FOB_SUM);
        ProductVlUsdSum.setProductVlUsdFob(BigDecimal.valueOf(ProductVlUsdFob.getValue()));
        return ProductVlUsdSum;
    }

    /**
     * 公司在中国的采购数量
     *
     * @param companyName
     * @return
     */
    public Long purchasesINChina(String companyName) {
        BoolQueryBuilder china = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery(IMPORTER, companyName))
                .must((QueryBuilders.matchQuery(COUNTRY_EXPORT, CHINA)));

        //查询从中国进口的数量
        NativeSearchQuery chinaQuery = new NativeSearchQueryBuilder().withIndices(BILL_STANDARD_INDEX).withTypes(BILL_STANDARD_INDEX_TYPE)
                .withSearchType(DFS_QUERY_THEN_FETCH)
                .withPageable(EmptyPage.INSTANCE)
                //当前公司
                .withQuery(QueryBuilders.boolQuery().filter((china)))
                .build();
        logger.info(">>>>>>>>>>>>>公司在中国的采购数量查询语句"+chinaQuery.getQuery().toString());
        Long purchasesINChina = elasticsearchTemplate.query(chinaQuery, new ResultsExtractor<Long>() {
            @Override
            public Long extract(SearchResponse response) {
                logger.info(">>>>>>>>>>>>>公司在中国的进口金额查询结果"+response.toString());
                return response.getHits().getTotalHits();
            }
        });
        return purchasesINChina;
    }

    /**
     * 统计公司分
     * @param company
     * @param query
     * @return
     */
    public List<CompanyPartStatistics> companyPartStatistics(CompanyStatistics company, Aggregations query) {
        long startTime=0;
        List<CompanyPartStatistics> companyPartList = new ArrayList<>();
        String country = company.getCountry();
        CompanyPartStatistics companyPartStatistics;
//        NativeSearchQuery searchQuery = companyCondition(company.getCompanyName());
//        TermsAggregationBuilder code = AggregationBuilders.terms(code_key).field(HSCODE + ".keyword");
//        searchQuery.addAggregation(code);
//        startTime=System.currentTimeMillis();
//        logger.info(">>>>>>>>>>>>>统计公司分查询语句"+searchQuery.getQuery().toString());
//        Aggregations query = elasticsearchTemplate.query(searchQuery, SearchResponse::getAggregations);
//        calculateTime(startTime,company.getCompanyId() +"名称:"+company.getCompanyName()+"     查询分公司信息:" );
        ParsedStringTerms parsedTerms = query.get(code_key);
        List<? extends Terms.Bucket> buckets = parsedTerms.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            //编号
            String key = (String) bucket.getKey();
            companyPartStatistics = new CompanyPartStatistics();
            companyPartStatistics.setCompanyName(company.getCompanyName());
            companyPartStatistics.setCountry(country);
            companyPartStatistics.setCode(key);

            //次数
            Long purchases = bucket.getDocCount();
            companyPartStatistics.setPurchases(purchases);
            companyPartStatistics.setPurchasesRatio(0.0f);
            //计算采购比例
            Long totalPurchases = company.getTotalPurchases();
            if (purchases > 0&&totalPurchases>0) {
                float ratio = NumberUtil.div(purchases, totalPurchases, 2, HALF_UP).floatValue();
                companyPartStatistics.setPurchasesRatio(ratio);
            }else{
                logger.error("除数 {}  被除数{}   {}获取次数占比异常 （当前编码采购次数/该公司总次数）*100%",purchases,totalPurchases,companyPartStatistics.getCode());
            }

            //查询采购金额
            startTime=System.currentTimeMillis();
            ProductVlUsd ProductVlUsd = ProductVlUsd(key, company.getCompanyName());
            calculateTime(startTime,"公司分编码:"+companyPartStatistics.getCode()+"  公司分   查询采购金额信息:" );
            //设置当公司编号的销售金额
            companyPartStatistics.setProductVlUsdCif(ProductVlUsd.getProductVlUsdCif());
            companyPartStatistics.setProductVlUsdFob(ProductVlUsd.getProductVlUsdFob());
            companyPartStatistics.setProductVlUsd(ProductVlUsd.getProductVlUsdFob());
            //计算金额比例
            calculateAmountRatio(companyPartStatistics, company);
            //计算单价
            calculateUnitPrice(companyPartStatistics);
            //根据编号和公司查询中国采购次数
            startTime=System.currentTimeMillis();
            Long purchasesINChina = chinaPurchases(company, key);
            calculateTime(startTime,"公司分编码:"+companyPartStatistics.getCode()+"   chinaPurchases  公司分 根据编号和公司查询中国采购次数:" );
            if (purchasesINChina > 0) {
                companyPartStatistics.setInChina(true);
            }
            companyPartStatistics.setPurchasesINChina(purchasesINChina);
            companyPartStatistics.setPurchasesINChinaRatio(0.0f);
            //设置是否从中国的采购比例
            if (purchasesINChina > 0&&purchases>0) {
                float ratio = NumberUtil.div(purchasesINChina, purchases, 2, HALF_UP).floatValue();
                companyPartStatistics.setPurchasesINChinaRatio(ratio);
            }else{
                logger.error("除数 {}  被除数{}   {}获取从中国采购次数占比异常 （当前编码从中国采购件数/该编码总件数）*100%",purchasesINChina,purchases,companyPartStatistics.getCode());
            }
//            startTime=System.currentTimeMillis();
//            ProductVlUsd chinaProductVlUsd = chinaProductVlUsd(key, company.getCompanyName());
//            calculateTime(startTime,"公司分编码:"+companyPartStatistics.getCode()+" chinaProductVlUsd   公司分 根据编号和公司查询中国采购金额:" );
//            companyPartStatistics.setChinaProductVlUsdCif(chinaProductVlUsd.getProductVlUsdCif());
//            companyPartStatistics.setChinaProductVlUsd(chinaProductVlUsd.getProductVlUsdFob());
//            companyPartStatistics.setChinaProductVlUsdFob(chinaProductVlUsd.getProductVlUsdFob());
//            //计算中国
//            calculateChinaAmountRatio(companyPartStatistics, company);
            //计算供应商
//            startTime=System.currentTimeMillis();
//            Long totalSuppliers = suppliers(company.getCompanyName(), key);
//            calculateTime(startTime,"公司分编码:"+companyPartStatistics.getCode()+" suppliers   公司分 计算供应商:" );
//            companyPartStatistics.setTotalSuppliers(totalSuppliers);
//            startTime=System.currentTimeMillis();
//            Long chinaSuppliers = chinaSuppliers(company.getCompanyName(), key);
//            calculateTime(startTime,"公司分编码:"+companyPartStatistics.getCode()+" chinaSuppliers   公司分 计算中国供应商:" );
//            companyPartStatistics.setChinaSuppliers(chinaSuppliers);
//            companyPartStatistics.setChinaRatio(0.0f);
//            if (chinaSuppliers > 0) {
//                float chinaRatio = NumberUtil.div(chinaSuppliers, totalSuppliers, 2, HALF_UP).floatValue();
//                companyPartStatistics.setChinaRatio(chinaRatio);
//            }
//            //同行数
//            startTime=System.currentTimeMillis();
//            Long peer = peer(key, company.getCompanyName());
//            calculateTime(startTime,"公司分编码:"+companyPartStatistics.getCode()+" peer   公司分 计算同行数:" );
//            companyPartStatistics.setPeer(peer);
//            startTime=System.currentTimeMillis();
//            Long domesticPeers = domesticPeers(key, company.getCompanyName(), country);
//            calculateTime(startTime,"公司分编码:"+companyPartStatistics.getCode()+" domesticPeers   公司分 计算本国同行数:" );
//            companyPartStatistics.setDomesticPeers(domesticPeers);
//            companyPartStatistics.setDomesticPeersRatio(0.0f);
//            if (peer > 0) {
//                float peerRatio = NumberUtil.div(domesticPeers, peer, 2, HALF_UP).floatValue();
//                companyPartStatistics.setDomesticPeersRatio(peerRatio);
//            }
            companyPartList.add(companyPartStatistics);
        }

        return companyPartList;
    }

    /**
     * 公司的供应商
     *
     * @param company
     * @return
     */
    public Long companySuppliers(String company) {
        NativeSearchQuery suppliersQuery = companyCondition(company);
        TermsAggregationBuilder suppliers = AggregationBuilders.terms(suppliersCount).field(EXPORTER_ID + ".keyword");
        suppliersQuery.addAggregation(suppliers);

        Integer totalSuppliers = elasticsearchTemplate.query(suppliersQuery, response -> {
            //获取当前的聚合对象
            ParsedStringTerms suppliersTerms = response.getAggregations().get(suppliersCount);
            //获取当年的聚合数量
            return suppliersTerms.getBuckets().size();
        });
        return Long.valueOf(totalSuppliers);
    }

    /**
     * 公司中国的供应商数量
     *
     * @param company
     * @return
     */
    public Long companyChinaSuppliers(String company) {
        ProductVlUsd ProductVlUsdSum = new ProductVlUsd();
        BoolQueryBuilder china = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery(IMPORTER, company))
                .must((QueryBuilders.matchQuery(COUNTRY_EXPORT, CHINA)));

        //查询从中国进口的数量
        NativeSearchQuery chinaQuery = new NativeSearchQueryBuilder().withIndices(BILL_STANDARD_INDEX).withTypes(BILL_STANDARD_INDEX_TYPE)
                .withSearchType(DFS_QUERY_THEN_FETCH)
                .withPageable(EmptyPage.INSTANCE)
                //当前公司
                .withQuery(QueryBuilders.boolQuery().filter((china)))
                .build();
        TermsAggregationBuilder suppliers = AggregationBuilders.terms(suppliersCount).field(EXPORTER_ID + ".keyword");
        chinaQuery.addAggregation(suppliers);

        Integer totalSuppliers = elasticsearchTemplate.query(chinaQuery, response -> {
            //获取当前的聚合对象
            ParsedStringTerms suppliersTerms = response.getAggregations().get(suppliersCount);
            //获取当年的聚合数量
            return suppliersTerms.getBuckets().size();
        });
        return Long.valueOf(totalSuppliers);
    }

    /**
     * 根据公司和海关编号供应商数量
     *
     * @param company
     * @param hsCode
     * @return
     */
    public Long suppliers(String company, String hsCode) {
        BoolQueryBuilder suppliersBoolQueryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery(IMPORTER, company))
                .must(QueryBuilders.matchQuery(HSCODE, hsCode));

        NativeSearchQuery suppliersQuery = new NativeSearchQueryBuilder()
                .withSearchType(DFS_QUERY_THEN_FETCH)
                .withPageable(EmptyPage.INSTANCE)
                //当前公司
                .withQuery(QueryBuilders.boolQuery().filter((suppliersBoolQueryBuilder)))
                .build();

        TermsAggregationBuilder suppliers = AggregationBuilders.terms(suppliersCount).field(EXPORTER_ID + ".keyword");
        suppliersQuery.addAggregation(suppliers);

        Integer totalSuppliers = elasticsearchTemplate.query(suppliersQuery, response -> {
            //获取当前的聚合对象
            ParsedStringTerms suppliersTerms = response.getAggregations().get(suppliersCount);
            //获取当年的聚合数量
            return suppliersTerms.getBuckets().size();
        });
        return Long.valueOf(totalSuppliers);
    }

    /**
     * 根据中国和海关编号查中国的供应商数量
     *
     * @param company
     * @param hsCode
     * @return
     */
    public Long chinaSuppliers(String company, String hsCode) {
        BoolQueryBuilder china = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery(IMPORTER, company))
                .must((QueryBuilders.matchQuery(COUNTRY_EXPORT, CHINA)))
                .must(QueryBuilders.matchQuery(HSCODE, hsCode));
        TermsAggregationBuilder suppliers = AggregationBuilders.terms(suppliersCount).field(EXPORTER_ID + ".keyword");

        NativeSearchQuery chinaSuppliersQuery = new NativeSearchQueryBuilder()
                .withSearchType(DFS_QUERY_THEN_FETCH)
                .withPageable(EmptyPage.INSTANCE)
                //当前公司
                .withQuery(QueryBuilders.boolQuery().filter((china)))
                .build();
        chinaSuppliersQuery.addAggregation(suppliers);
        //中国供应商数量
        Integer chinaSuppliers = elasticsearchTemplate.query(chinaSuppliersQuery, response -> {
            //获取当前的聚合对象
            ParsedStringTerms suppliersTerms = response.getAggregations().get(suppliersCount);
            //获取当年的聚合数量
            return suppliersTerms.getBuckets().size();
        });
        return Long.valueOf(chinaSuppliers);
    }

    private void calculateChinaAmountRatio(CompanyPartStatistics companyPartStatistics, CompanyStatistics company) {
        BigDecimal ProductVlUsdCif = companyPartStatistics.getChinaProductVlUsdCif();
        BigDecimal ProductVlUsd = companyPartStatistics.getChinaProductVlUsd();
        BigDecimal ProductVlUsdFob = companyPartStatistics.getChinaProductVlUsdFob();
        companyPartStatistics.setChinaProductVlUsdCifRatio(0.0f);
        companyPartStatistics.setChinaProductVlUsdRatio(0.0f);
        companyPartStatistics.setChinaProductVlUsdFobRatio(0.0f);
        //设置三个采购金额比例
        if (BigDecimal.ZERO.compareTo(ProductVlUsdCif) < 0) {
            float ratio = NumberUtil.div(ProductVlUsdCif, companyPartStatistics.getProductVlUsdCif(), 2, HALF_UP).floatValue();
            companyPartStatistics.setChinaProductVlUsdCifRatio(ratio);
        }
        if (BigDecimal.ZERO.compareTo(ProductVlUsd) < 0) {
            float ratio = NumberUtil.div(ProductVlUsd, company.getProductVlUsd(), 2, HALF_UP).floatValue();
            companyPartStatistics.setChinaProductVlUsdRatio(ratio);
        }
        if (BigDecimal.ZERO.compareTo(ProductVlUsdFob) < 0) {
            float ratio = NumberUtil.div(ProductVlUsdFob, company.getChinaProductVlUsdFob(), 2, HALF_UP).floatValue();
            companyPartStatistics.setChinaProductVlUsdFobRatio(ratio);
        }

    }

    private Long chinaPurchases(CompanyStatistics company, String key) {
        BoolQueryBuilder china = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery(IMPORTER, company.getCompanyName())).must(QueryBuilders.matchQuery(HSCODE, key))
                .must((QueryBuilders.matchQuery(COUNTRY_EXPORT, CHINA)));
        //查询从中国进口的数量
        NativeSearchQuery chinaQuery = new NativeSearchQueryBuilder()
                .withSearchType(DFS_QUERY_THEN_FETCH)
                .withPageable(EmptyPage.INSTANCE)
                //当前公司
                .withQuery(QueryBuilders.boolQuery().filter((china)))
                .build();
        Long purchasesINChina = elasticsearchTemplate.query(chinaQuery, response -> response.getHits().getTotalHits());
        return purchasesINChina;
    }

    private void calculateUnitPrice(CompanyPartStatistics companyPartStatistics) {
        BigDecimal ProductVlUsdCif = companyPartStatistics.getProductVlUsdCif();
        BigDecimal ProductVlUsd = companyPartStatistics.getProductVlUsd();
        BigDecimal ProductVlUsdFob = companyPartStatistics.getProductVlUsdFob();
        Long purchases = companyPartStatistics.getPurchases();
        companyPartStatistics.setTraderPriceCif(BigDecimal.ZERO);
        companyPartStatistics.setTraderPriceUsd(BigDecimal.ZERO);
        companyPartStatistics.setTraderPriceFob(BigDecimal.ZERO);
        if (purchases > 0) {
            BigDecimal traderUnitPriceCif = NumberUtil.div(ProductVlUsdCif, purchases.doubleValue(), 2, HALF_UP);
            companyPartStatistics.setTraderPriceCif(traderUnitPriceCif);
            BigDecimal traderUnitPriceUsd = NumberUtil.div(ProductVlUsd, purchases.doubleValue(), 2, HALF_UP);
            companyPartStatistics.setTraderPriceUsd(traderUnitPriceUsd);
            BigDecimal traderUnitPriceFob = NumberUtil.div(ProductVlUsdFob, purchases.doubleValue(), 2, HALF_UP);
            companyPartStatistics.setTraderPriceFob(traderUnitPriceFob);
        }
    }

    private void calculateAmountRatio(CompanyPartStatistics companyPartStatistics, CompanyStatistics company) {
        BigDecimal ProductVlUsdCif = companyPartStatistics.getProductVlUsdCif();
        BigDecimal ProductVlUsd = companyPartStatistics.getProductVlUsd();
        BigDecimal ProductVlUsdFob = companyPartStatistics.getProductVlUsdFob();
        companyPartStatistics.setProductVlUsdCifRatio(0.0f);
        companyPartStatistics.setProductVlUsdRatio(0.0f);
        companyPartStatistics.setProductVlUsdFobRatio(0.0f);
        //设置三个采购金额比例
        if (BigDecimal.ZERO.compareTo(ProductVlUsdCif) < 0&&BigDecimal.ZERO.compareTo(company.getProductVlUsdCif()) < 0) {
            float ratio = NumberUtil.div(ProductVlUsdCif, company.getProductVlUsdCif(), 2, HALF_UP).floatValue();
            companyPartStatistics.setProductVlUsdCifRatio(ratio);
        }else{
            logger.error("除数 {}  被除数{}   {}获取ProductVlUsdCifRatio异常",ProductVlUsdCif,company.getProductVlUsdCif(),companyPartStatistics.getCode());
        }
        if (BigDecimal.ZERO.compareTo(ProductVlUsd) < 0&&BigDecimal.ZERO.compareTo(company.getProductVlUsd()) < 0) {
            float ratio = NumberUtil.div(ProductVlUsd, company.getProductVlUsd(), 2, HALF_UP).floatValue();
            companyPartStatistics.setProductVlUsdRatio(ratio);
        }else{
            logger.error("除数 {}  被除数{}   {}获取ProductVlUsdRatio异常 ",ProductVlUsd,company.getProductVlUsd(),companyPartStatistics.getCode());
        }
        if (BigDecimal.ZERO.compareTo(ProductVlUsdFob) < 0&&BigDecimal.ZERO.compareTo(company.getProductVlUsdFob()) < 0) {
            float ratio = NumberUtil.div(ProductVlUsdFob, company.getProductVlUsdFob(), 2, HALF_UP).floatValue();
            companyPartStatistics.setProductVlUsdFobRatio(ratio);
        }else{
            logger.error("除数 {}  被除数{}   {}获取ProductVlUsdFobRatio异常 ",ProductVlUsdFob,company.getProductVlUsdFob(),companyPartStatistics.getCode());
        }
    }

    /**
     * 根据公司和编号查询金额
     *
     * @param company
     * @return
     */
    public ProductVlUsd companyProductVlUsd(String company) {
        ProductVlUsd ProductVlUsdSum = new ProductVlUsd();
        NativeSearchQuery searchQuery = companyCondition(company);
        SumAggregationBuilder field = AggregationBuilders.sum(PRODUCT_VL_USD_CIF_SUM).field(PRODUCT_VL_USD_CIF);
        //查询采购金额
        searchQuery.addAggregation(field);
        SumAggregationBuilder field1 = AggregationBuilders.sum(PRODUCT_VL_USD_SUM).field(PRODUCT_VL_USD);
        searchQuery.addAggregation(field1);
        SumAggregationBuilder field2 = AggregationBuilders.sum(PRODUCT_VL_USD_FOB_SUM).field(PRODUCT_VL_USD_FOB);
        searchQuery.addAggregation(field2);
        Aggregations aggregations = elasticsearchTemplate.query(searchQuery, response -> response.getAggregations());
        ParsedSum ProductVlUsdCif;
        ParsedSum ProductVlUsd;
        ParsedSum ProductVlUsdFob;
        ProductVlUsdCif = aggregations.get(PRODUCT_VL_USD_CIF_SUM);
        ProductVlUsdSum.setProductVlUsdCif(BigDecimal.valueOf(ProductVlUsdCif.getValue()));
        ProductVlUsd = aggregations.get(PRODUCT_VL_USD_SUM);
        ProductVlUsdSum.setProductVlUsd(BigDecimal.valueOf(ProductVlUsd.getValue()));
        ProductVlUsdFob = aggregations.get(PRODUCT_VL_USD_FOB_SUM);
        ProductVlUsdSum.setProductVlUsdFob(BigDecimal.valueOf(ProductVlUsdFob.getValue()));
        return ProductVlUsdSum;
    }

    /**
     * 根据公司和编号查询金额
     *
     * @param code
     * @param company
     * @return
     */
    public ProductVlUsd ProductVlUsd(String code, String company) {
        ProductVlUsd ProductVlUsdSum = new ProductVlUsd();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery(HSCODE, code))
                .must(QueryBuilders.matchQuery(IMPORTER, company));
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices(BILL_STANDARD_INDEX).withTypes(BILL_STANDARD_INDEX_TYPE)
                .withPageable(EmptyPage.INSTANCE)
                .withQuery(QueryBuilders.boolQuery().filter((boolQueryBuilder)))
                .build();
        SumAggregationBuilder field = AggregationBuilders.sum(PRODUCT_VL_USD_CIF_SUM).field(PRODUCT_VL_USD_CIF);
        //查询采购金额
        searchQuery.addAggregation(field);
        SumAggregationBuilder field1 = AggregationBuilders.sum(PRODUCT_VL_USD_SUM).field(PRODUCT_VL_USD);
        searchQuery.addAggregation(field1);
        SumAggregationBuilder field2 = AggregationBuilders.sum(PRODUCT_VL_USD_FOB_SUM).field(PRODUCT_VL_USD_FOB);
        searchQuery.addAggregation(field2);
        Aggregations aggregations = elasticsearchTemplate.query(searchQuery, response -> response.getAggregations());
        ParsedSum ProductVlUsdCif;
        ParsedSum ProductVlUsd;
        ParsedSum ProductVlUsdFob;
        ProductVlUsdCif = aggregations.get(PRODUCT_VL_USD_CIF_SUM);
        ProductVlUsdSum.setProductVlUsdCif(BigDecimal.valueOf(ProductVlUsdCif.getValue()));
        ProductVlUsd = aggregations.get(PRODUCT_VL_USD_SUM);
        ProductVlUsdSum.setProductVlUsd(BigDecimal.valueOf(ProductVlUsd.getValue()));
        ProductVlUsdFob = aggregations.get(PRODUCT_VL_USD_FOB_SUM);
        ProductVlUsdSum.setProductVlUsdFob(BigDecimal.valueOf(ProductVlUsdFob.getValue()));
        return ProductVlUsdSum;
    }

    /**
     * 根据公司和编号查询金额
     *
     * @param code
     * @param company
     * @return
     */
    public ProductVlUsd chinaProductVlUsd(String code, String company) {
        ProductVlUsd ProductVlUsdSum = new ProductVlUsd();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery(HSCODE, code))
                .must(QueryBuilders.matchQuery(IMPORTER, company))
                .must((QueryBuilders.matchQuery(COUNTRY_EXPORT, CHINA)));
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withPageable(EmptyPage.INSTANCE)
                .withQuery(QueryBuilders.boolQuery().filter((boolQueryBuilder)))
                .build();
        SumAggregationBuilder field = AggregationBuilders.sum(PRODUCT_VL_USD_CIF_SUM).field(PRODUCT_VL_USD_CIF);
        //查询采购金额
        searchQuery.addAggregation(field);
        SumAggregationBuilder field1 = AggregationBuilders.sum(PRODUCT_VL_USD_SUM).field(PRODUCT_VL_USD);
        searchQuery.addAggregation(field1);
        SumAggregationBuilder field2 = AggregationBuilders.sum(PRODUCT_VL_USD_FOB_SUM).field(PRODUCT_VL_USD_FOB);
        searchQuery.addAggregation(field2);
        Aggregations aggregations = elasticsearchTemplate.query(searchQuery, response -> response.getAggregations());
        ParsedSum ProductVlUsdCif;
        ParsedSum ProductVlUsd;
        ParsedSum ProductVlUsdFob;
        ProductVlUsdCif = aggregations.get(PRODUCT_VL_USD_CIF_SUM);
        ProductVlUsdSum.setProductVlUsdCif(BigDecimal.valueOf(ProductVlUsdCif.getValue()));
        ProductVlUsd = aggregations.get(PRODUCT_VL_USD_SUM);
        ProductVlUsdSum.setProductVlUsd(BigDecimal.valueOf(ProductVlUsd.getValue()));
        ProductVlUsdFob = aggregations.get(PRODUCT_VL_USD_FOB_SUM);
        ProductVlUsdSum.setProductVlUsdFob(BigDecimal.valueOf(ProductVlUsdFob.getValue()));
        return ProductVlUsdSum;
    }

    /**
     * 根据编号和公司名称返回同行数
     *
     * @param code    海关编号
     * @param company 公司名称
     * @return
     */
    public Long peer(String code, String company) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery(HSCODE, code))
                .mustNot(QueryBuilders.matchQuery(IMPORTER, company));
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withPageable(EmptyPage.INSTANCE)
                .withQuery(QueryBuilders.boolQuery().filter((boolQueryBuilder)))
                .build();
        Long peer = elasticsearchTemplate.query(searchQuery, response -> response.getHits().getTotalHits());
        return peer;
    }

    /**
     * 根据编号和公司名称和国家返回同行数
     *
     * @param code    海关编号
     * @param company 公司名称
     * @param country 国家
     * @return
     */
    public Long domesticPeers(String code, String company, String country) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery(HSCODE, code))
                .mustNot(QueryBuilders.matchQuery(IMPORTER, company))
                .must(QueryBuilders.matchQuery(COUNTRY, country));
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withPageable(EmptyPage.INSTANCE)
                .withQuery(QueryBuilders.boolQuery().filter((boolQueryBuilder)))
                .build();
        Long domesticPeers = elasticsearchTemplate.query(searchQuery, response -> response.getHits().getTotalHits());
        return domesticPeers;
    }

    /**
     * 公司条件
     * @param companyName
     * @return
     */
    public NativeSearchQuery companyCondition(String companyName) {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices(BILL_STANDARD_INDEX).withTypes(BILL_STANDARD_INDEX_TYPE)
                .withPageable(EmptyPage.INSTANCE)
                //当前公司
                .withQuery(QueryBuilders.boolQuery().filter(QueryBuilders.matchQuery(IMPORTER, companyName)))
                .build();
        return searchQuery;
    }

    /**
     * 创建国家统计
     */
    public void creatCountryStatistics(){
        List<String> strings = allCountry();
        for (String string : strings) {
            countryStatistics(country_name);
        }
    }

    /**
     * 生成对应的国家信息
     * @param countryName
     */
    public void countryStatistics(String countryName) {
        //编号
        TermsAggregationBuilder code = AggregationBuilders.terms(code_key).field(HSCODE + ".keyword");
        NativeSearchQuery countryHsCodeQuery = new NativeSearchQueryBuilder()
                .withSearchType(DFS_QUERY_THEN_FETCH)
                .addAggregation(code)
                .withPageable(EmptyPage.INSTANCE).build();
        ParsedStringTerms HsCode = elasticsearchTemplate.query(countryHsCodeQuery, response -> {
            return response.getAggregations().get(code_key);
        });
        List<? extends Terms.Bucket> buckets = HsCode.getBuckets();
        IndexQuery build;
        List<IndexQuery> indexQueries=new ArrayList<>(buckets.size());
        for (Terms.Bucket bucket : buckets) {
            CountryStatistics countryStatistics = new CountryStatistics();
            countryStatistics.setCountry(countryName);
            String key = (String) bucket.getKey();
            countryStatistics.setCode(key);
            //去重的采购商数量
            Long buyers = CountryHscodeBuyers(countryName, key);
            countryStatistics.setBuyers(buyers);
            Long suppliers = countryHscodeSuppliers(countryName, key);
            countryStatistics.setSuppliers(suppliers);
             build = new IndexQueryBuilder().withType(COUNTRY_TYPE).withIndexName(COMPANY_INDEX).withObject(countryStatistics).build();
             indexQueries.add(build);
        }
        elasticsearchTemplate.bulkIndex(indexQueries,BulkOptions.defaultOptions());
    }

    /**
     * 根据国家和编号查找采购商
     * @param countryName
     * @param HsCode
     * @return
     */
    public Long CountryHscodeBuyers(String countryName, String HsCode) {
        BoolQueryBuilder must = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery(COUNTRY, countryName)).must(QueryBuilders.matchQuery(HSCODE, HsCode));
        QueryBuilders.boolQuery().filter(must);
        TermsAggregationBuilder buyers = AggregationBuilders.terms(buyersCount).field(IMPORTER + ".keyword");
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withPageable(EmptyPage.INSTANCE)
                //当前公司
                .withQuery(must)
                .addAggregation(buyers).build();
        Integer query = elasticsearchTemplate.query(searchQuery, response -> {
            ParsedStringTerms parsedStringTerms = response.getAggregations().get(buyersCount);
            return parsedStringTerms.getBuckets().size();
        });
        return Long.valueOf(query);
    }

    /**
     * 根据国家和hash查找提供商的数量
     * @param countryName
     * @param HsCode
     * @return
     */
    public Long countryHscodeSuppliers(String countryName, String HsCode) {
        BoolQueryBuilder must = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery(COUNTRY, countryName)).must(QueryBuilders.matchQuery(HSCODE, HsCode));
        QueryBuilders.boolQuery().filter(must);
        TermsAggregationBuilder suppliers = AggregationBuilders.terms(suppliersCount).field(EXPORTER_ID + ".keyword");
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withPageable(EmptyPage.INSTANCE)
                //当前公司
                .withQuery(must)
                .addAggregation(suppliers).build();
        Integer query = elasticsearchTemplate.query(searchQuery, response -> {
            ParsedStringTerms parsedStringTerms = response.getAggregations().get(suppliersCount);
            return parsedStringTerms.getBuckets().size();
        });
        return Long.valueOf(query);
    }
    public void calculateTime(long startTime, String query) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:S");
        Date startDate = new Date(startTime);
        Object object = null;
        try {
            logger.info("-------------------------{}开始日志:{}-------------------------",query,simpleDateFormat.format(startDate));
            logger.info("-------------------------{}结束日志:{}------------处理时长:{}-------------",query,simpleDateFormat.format(new Date()),System.currentTimeMillis() - startTime);
        } catch (Throwable throwable) {
            logger.error(throwable.getMessage(), throwable);
        }
    }

//    public void companyStatistics(String companyName){
//
//        new NativeSearchQueryBuilder()
//                //当前公司
//                .withQuery(QueryBuilders.boolQuery().filter((QueryBuilders.matchQuery(IMPORTER.name(),companyName))
//                //总采购
//                .addAggregation(AggregationBuilders.count(IMPORTER.name()))
//                //总采购金额(USD) 商品价值（美元）
//
//                .addAggregation(AggregationBuilders.sum(OFFIHeaders.PRODUCT_VL_USD.name()))
//                .addAggregation()
//        elasticsearchTemplate.query()
//
//    }
}

package com.meorient.mebuyerdiscovery.elasticsearch.compent;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.meorient.mebuyerdiscovery.elasticsearch.doc.LadingBillSimpleDocument;
import com.meorient.mebuyerdiscovery.elasticsearch.doc.OneRecordDocument;
import com.meorient.mebuyerdiscovery.elasticsearch.utils.CollectionUtil;
import com.meorient.mebuyerdiscovery.elasticsearch.utils.EsUtils;
import com.meorient.mebuyerdiscovery.elasticsearch.utils.SnowFlake;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.ScrolledPage;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.validation.constraints.Null;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static com.meorient.mebuyerdiscovery.elasticsearch.constant.ElasticSearchConstant.*;


@Component
public class PageImport {

    Logger log = LoggerFactory.getLogger(PageImport.class);
    /**
     * scroll游标快照超时时间，单位ms
     */
    private static final long SCROLL_TIMEOUT = 90000;
    private volatile Boolean flag = true;
    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;

    ThreadFactory build = new ThreadFactoryBuilder().setNameFormat("importEs-ThreadPool-%d").setUncaughtExceptionHandler((t, e) -> log.error(t.getName(), e)).build();

    TimeValue timeValue = TimeValue.timeValueMinutes(5);
    BulkOptions bulkOptions = BulkOptions.builder().withTimeout(timeValue).build();

    //这Queue控制中间存储 防止线程池任务队列过多
    private LinkedBlockingQueue<Map<Integer, List<OneRecordDocument>>> queue = new LinkedBlockingQueue<>(10);
    private LinkedBlockingQueue<Map<Integer, List<LadingBillSimpleDocument>>> updatequeue = new LinkedBlockingQueue<>(10);
    private LinkedBlockingQueue<List<LadingBillSimpleDocument>> addColumqueue = new LinkedBlockingQueue<>(10);
    private static ExecutorService execute = Executors.newFixedThreadPool(8);

    /**
     * 用于将Scroll获取到的结果，处理成dto列表，做复杂映射
     */
//    private final SearchResultMapper searchResultMapper = new SearchResultMapper() {
//        @Override
//        public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> aClass, Pageable pageable) {
//            List<OneRecordDocument> result = new ArrayList<>();
//            for (SearchHit hit : response.getHits()) {
//                if (response.getHits().getHits().length <= 0) {
//                    return new AggregatedPageImpl<T>(Collections.EMPTY_LIST, pageable, response.getHits().getTotalHits(), response.getScrollId());
//                }
//                //可以做更复杂的映射逻辑
//                Object obj = hit.getSourceAsMap().get("userId");
//                OneRecordDocument oneRecordDocument=new OneRecordDocument();
//                BeanUtils.copyProperties(obj,oneRecordDocument);
//                result.add(oneRecordDocument);
//                oneRecordDocument=null;
//            }
//            if (result.isEmpty()) {
//                return new AggregatedPageImpl<T>(Collections.EMPTY_LIST, pageable, response.getHits().getTotalHits(), response.getScrollId());
//            }
//            return new AggregatedPageImpl<T>((List<T>) result, pageable, response.getHits().getTotalHits(), response.getScrollId());
//        }
//
//        @Override
//        public <T> T mapSearchHit(SearchHit searchHit, Class<T> type) {
//            return null;
//        }
//    };

    /**
     * 从es导入es
     *
     * @param startId
     * @param endId
     * @param integer
     */
    public void pageImport(Integer inputPage, Integer step) throws InterruptedException {
        for (int i = 0; i < 8; i++) {
            consum(step);
        }
        try {
            SearchQuery searchQuery = new NativeSearchQueryBuilder()
                    .withIndices(ALL_COUNTRY_BALLING_INDEX)//索引名
                    .withTypes(ALL_COUNTRY_BALLING_TYPE)//类型名
                    .withQuery(QueryBuilders.matchAllQuery())//查询条件，这里简单使用term查询
                    .withPageable(PageRequest.of(0, step))//从0页开始查，每页10个结果
                    .build();

            ScrolledPage<OneRecordDocument> scroll = elasticsearchTemplate.startScroll(SCROLL_TIMEOUT, searchQuery, OneRecordDocument.class);
            log.info("查询总命中数：{}", scroll.getTotalElements());
            int i = 0;
            while (scroll.hasContent()) {
                try {
                    i++;
                    long l = System.currentTimeMillis();
                    List<OneRecordDocument> list = scroll.getContent();
                    log.info("当前i值：{},耗时：{},数据长度：{}", i, System.currentTimeMillis() - l, list.size());
                    log.info("查询总命中数：" + scroll.getTotalElements() + "查询出数量：" + list.size() + "  页数" +
                            i + "当前页首个id" + list.get(0).getTheId());
                    Map<Integer, List<OneRecordDocument>> scrollmap = new HashMap<>();
                    scrollmap.put(i, scroll.getContent());
                    if (i < inputPage) {
                        queue.put(scrollmap);
                    }
                    //取下一页，scrollId在es服务器上可能会发生变化，需要用最新的。发起continueScroll请求会重新刷新快照保留时间
                    scroll = elasticsearchTemplate.continueScroll
                            (scroll.getScrollId(), SCROLL_TIMEOUT, OneRecordDocument.class);
                } catch (Exception e) {
                    log.error("查询es数据失败", e);
                    log.info("最后失败的i值：{}", i);
                    break;
                }

            }
            do {
                log.info("当前队列剩余:{}", queue.size());
                Thread.sleep(1000);
            } while (queue.size() > 0);

            //及时释放es服务器资源
            elasticsearchTemplate.clearScroll(scroll.getScrollId());
            log.info("查询结束  滚动次数：" + i);
        } catch (Exception e) {
            log.error("put has error" + e);
        }


    }

    /**
     * @param step
     */
    public void consum(Integer step) {
        execute.execute(() -> {
            Integer page = 0;
            Map<Integer, List<OneRecordDocument>> take = null;
            do {
                try {
                    take = queue.take();
                    List<OneRecordDocument> list = new ArrayList<OneRecordDocument>();
                    PageBlock newPageBlock = new PageBlock();
                    for (Integer key : take.keySet()) {
                        newPageBlock.setPage(key);
                        page = key;
                        newPageBlock.setSize(step);
                        list = take.get(key);
                    }
                    importRecord(newPageBlock, list);

                } catch (Exception e) {
                    try {
                        queue.put(take);
                        log.info("消费者消费失败重入队列 页数:" + page);
                        Thread.sleep(5 * 60 * 1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    log.error("moveEsDataException >>>>>>>>>>>", e);
                }
            } while (true);
        });
    }

    /**
     * 导入es
     *
     * @param pageBlock
     */
    public void importRecord(PageBlock pageBlock, List<OneRecordDocument> list) {
        Integer page = pageBlock.getPage();
        Integer size = pageBlock.getSize();
        moveEsData(page, size, list);
        if (pageBlock.isEnd) {
            return;
        }
    }

    public void moveEsData(Integer page, Integer size, List<OneRecordDocument> list) {
//        String uuid = String.valueOf(UUID.randomUUID());
        long startTime = System.currentTimeMillis();
        List<LadingBillSimpleDocument> ladingBillList = new ArrayList<LadingBillSimpleDocument>();
        List<IndexQuery> collect = list.stream().map(one -> {
            long snowFlakeid = SnowFlake.getInstance(5, 8).nextId();
            LadingBillSimpleDocument ladingBill = new LadingBillSimpleDocument();
            BeanUtils.copyProperties(one, ladingBill);
            ladingBill.setId(String.valueOf(snowFlakeid));
            if (!StringUtils.isEmpty(ladingBill.getCountry()) && (ladingBill.getCountry().replace("\"", "").equals("usa") || ladingBill.getCountry().replace("\"", "").equals("USA"))) {
                ladingBill.setCountry("United States of America");
            } else if (!StringUtils.isEmpty(ladingBill.getCountry()) && (ladingBill.getCountry().replace("\"", "").equals("russia") || ladingBill.getCountry().replace("\"", "").equals("RUSSIA"))) {
                ladingBill.setCountry("Russian Federation");
            } else if (!StringUtils.isEmpty(ladingBill.getCountry()) && (ladingBill.getCountry().replace("\"", "").equals("srilanka") || ladingBill.getCountry().replace("\"", "").equals("SRILANKA"))) {
                ladingBill.setCountry("Sri Lanka");
            }
            if (!StringUtils.isEmpty(ladingBill.getByDate())) {
                String[] timearray = ladingBill.getByDate().split("-");
                if (timearray[0].length() != 4 || timearray[1].length() != 2 || timearray[2].length() != 2) {
                    log.info(" EsData TheId:" + one.getTheId());
                    log.error("DateOfSubmission parsing_exception  :" + ladingBill.getByDate());
                    ladingBill.setByDate(null);
                }
            }
            return new IndexQueryBuilder().withObject(ladingBill).withIndexName(BILL_STANDARD_INDEX).withType(BILL_STANDARD_INDEX_TYPE).withId(ladingBill.getTheId()).build();
        }).collect(Collectors.toList());
        log.info(" >>>>>>>>>>>moveEsData  pagesize:" + String.valueOf(collect.size()) + "   page:" + page);

        elasticsearchTemplate.bulkIndex(collect, BulkOptions.defaultOptions());
        list = null;
        //calculateTime(startTime, uuid + " moveEsData page:" + page + " size:" + size);


    }

    public void calculateTime(long startTime, String query) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:S");
        Date startDate = new Date(startTime);
        Object object = null;
        try {
            log.info("-------------------------{}开始日志:{}-------------------------", query, simpleDateFormat.format(startDate));
            log.info("-------------------------{}结束日志:{}------------处理时长:{}-------------", query, simpleDateFormat.format(new Date()), System.currentTimeMillis() - startTime);
        } catch (Throwable throwable) {
            log.error(throwable.getMessage(), throwable);
        }
    }


    public void updateIndex(String orginCountry, String updateCountry) {
        Integer step = 10000;
        try {
            SearchQuery searchQuery = new NativeSearchQueryBuilder()
                    .withIndices(BILL_STANDARD_INDEX)//索引名
                    .withTypes(BILL_STANDARD_INDEX_TYPE)//类型名
                    .withQuery(QueryBuilders.termQuery("country", orginCountry))//查询条件，这里简单使用term查询
                    .withPageable(PageRequest.of(0, step))//从0页开始查，每页10个结果
                    .build();

            ScrolledPage<LadingBillSimpleDocument> scroll = elasticsearchTemplate.startScroll(SCROLL_TIMEOUT, searchQuery, LadingBillSimpleDocument.class);
            log.info("查询总命中数：{}", scroll.getTotalElements());
            int i = 0;
            while (scroll.hasContent()) {
                try {
                    i++;
                    long l = System.currentTimeMillis();
                    List<LadingBillSimpleDocument> list = scroll.getContent();
                    log.info("当前i值：{},耗时：{},数据长度：{}", i, System.currentTimeMillis() - l, list.size());
                    log.info("查询总命中数：" + scroll.getTotalElements() + "查询出数量：" + list.size() + "  页数" +
                            i + "当前页首个id" + list.get(0).getTheId());
                    List<IndexQuery> updateList = list.stream().map(one -> {
                        one.setCountry(updateCountry);
                        return new IndexQueryBuilder().withObject(one).withIndexName(BILL_STANDARD_INDEX).withType(BILL_STANDARD_INDEX_TYPE).withId(one.getTheId()).build();
                    }).collect(Collectors.toList());
                    elasticsearchTemplate.bulkIndex(updateList, BulkOptions.defaultOptions());
                    //取下一页，scrollId在es服务器上可能会发生变化，需要用最新的。发起continueScroll请求会重新刷新快照保留时间
                    scroll = elasticsearchTemplate.continueScroll
                            (scroll.getScrollId(), SCROLL_TIMEOUT, LadingBillSimpleDocument.class);
                } catch (Exception e) {
                    log.error("查询es数据失败", e);
                    log.info("最后失败的i值：{}", i);
                    break;
                }

            }
            do {
                log.info("当前队列剩余:{}", updatequeue.size());
                Thread.sleep(1000);
            } while (updatequeue.size() > 0);

            //及时释放es服务器资源
            elasticsearchTemplate.clearScroll(scroll.getScrollId());
            log.info("查询结束  滚动次数：" + i);
        } catch (Exception e) {
            log.error("put has error" + e);
        }
    }

    /**
     * 从es增加字段
     *
     * @param startId
     * @param endId
     * @param integer
     */
    public void addColumn(Integer inputPage, Integer step) throws InterruptedException {
        for (int i = 0; i < 8; i++) {
            consumAddColumn();
        }
        try {
            SearchQuery searchQuery = new NativeSearchQueryBuilder()
                    .withIndices(BILL_STANDARD_INDEX)//索引名
                    .withTypes(BILL_STANDARD_INDEX_TYPE)//类型名
                    .withQuery(QueryBuilders.matchAllQuery())//查询条件，这里简单使用term查询
                    .withPageable(PageRequest.of(0, step))//从0页开始查，每页10个结果
                    .build();

            ScrolledPage<LadingBillSimpleDocument> scroll = elasticsearchTemplate.startScroll(SCROLL_TIMEOUT, searchQuery, LadingBillSimpleDocument.class);
            log.info("查询总命中数：{}", scroll.getTotalElements());
            int i = 0;
            while (scroll.hasContent()) {
                try {
                    i++;
                    long l = System.currentTimeMillis();
                    List<LadingBillSimpleDocument> list = scroll.getContent();
                    log.info("当前i值：{},耗时：{},数据长度：{}", i, System.currentTimeMillis() - l, list.size());
                    log.info("查询总命中数：" + scroll.getTotalElements() + "查询出数量：" + list.size() + "  页数" +
                            i + "当前页首个id" + list.get(0).getTheId());
                    if(i>inputPage){
                        if (!CollectionUtils.isEmpty(list)) {
                            addColumqueue.put(list);
                        }
                    }
                    //取下一页，scrollId在es服务器上可能会发生变化，需要用最新的。发起continueScroll请求会重新刷新快照保留时间
                    scroll = elasticsearchTemplate.continueScroll
                            (scroll.getScrollId(), SCROLL_TIMEOUT, LadingBillSimpleDocument.class);
                } catch (Exception e) {
                    log.error("查询es数据失败", e);
                    log.info("最后失败的i值：{}", i);
                    break;
                }

            }
            do {
                log.info("当前队列剩余:{}", addColumqueue.size());
                Thread.sleep(1000);
            } while (addColumqueue.size() > 0);

            //及时释放es服务器资源
            elasticsearchTemplate.clearScroll(scroll.getScrollId());
            log.info("查询结束  滚动次数：" + i);
        } catch (Exception e) {
            log.error("put has error" + e);
        }


    }

    /**
     * @param step
     */
    public void consumAddColumn() {
        execute.execute(() -> {
            do {
                List<LadingBillSimpleDocument> take = new ArrayList<>();
                try {
                    take = addColumqueue.take();
                    if (CollectionUtil.isNotEmpty(take)) {
                        List<IndexQuery> updateList = take.stream().map(one -> {
                            Double productVlUsd = Double.valueOf(0);
                            Double productVlUsdCif = Double.valueOf(0);
                            Double productVlUsdFob = Double.valueOf(0);
                            if (null != one && !StringUtils.isEmpty(one.getProductVlUsd())) {
                                productVlUsd = Double.valueOf(one.getProductVlUsd());
                            }
                            if (null != one && !StringUtils.isEmpty(one.getProductVlUsd())) {
                                productVlUsdCif = Double.valueOf(one.getProductVlUsd());
                            }
                            if (null != one && !StringUtils.isEmpty(one.getProductVlUsd())) {
                                productVlUsdFob = Double.valueOf(one.getProductVlUsd());
                            }
                            Double productVlUsdPrice = getThreeMax(productVlUsd, productVlUsdCif, productVlUsdFob);
                            one.setProductVlUsdPrice(productVlUsdPrice);
                            return new IndexQueryBuilder().withObject(one).withIndexName(BILL_STANDARD_INDEX).withType(BILL_STANDARD_INDEX_TYPE).withId(one.getTheId()).build();
                        }).collect(Collectors.toList());
                        elasticsearchTemplate.bulkIndex(updateList, BulkOptions.defaultOptions());
                        log.info("消费者消费成功 ");
                    }

                } catch (Exception e) {
                    try {
                        addColumqueue.put(take);
                        log.info("消费者消费失败重入队列 ", e);
                        Thread.sleep(5 * 60 * 1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            } while (true);
        });
    }

    static double getThreeMax(double a, double b, double c) {
        double max = a > b ? a : b;
        max = max > c ? max : c;
        return max;
    }
}

package com.meorient.mebuyerdiscovery.elasticsearch.controller;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.meorient.mebuyerdiscovery.elasticsearch.compent.PageImport;
import com.meorient.mebuyerdiscovery.elasticsearch.doc.OneRecordDocument;
import com.meorient.mebuyerdiscovery.elasticsearch.statistics.StatisticsService;
import org.apache.tomcat.util.http.fileupload.impl.SizeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: zjt
 * @time: 2020/5/19 11:18
 */
@RestController
public class EsController {
    Logger log = LoggerFactory.getLogger(EsController.class);
    @Autowired
    PageImport pageImport;
    @Autowired
    private StatisticsService statisticsService;
//    ThreadFactory providerbuild = new ThreadFactoryBuilder().setNameFormat("providerimportEs-ThreadPool-%d").setUncaughtExceptionHandler((t, e) -> log.error(t.getName(), e)).build();
//    private ThreadPoolExecutor providerthreadPoolExecutor = new ThreadPoolExecutor(9, 9,
//            60000L, TimeUnit.MILLISECONDS,
//            new LinkedBlockingQueue<>(), providerbuild);
    @GetMapping("/moveEs")
    public void moveEs(@RequestParam("step") Integer step,@RequestParam("inputpage") Integer inputpage
//            @RequestParam("startId") Integer startId,
//            @RequestParam("endId") Integer endId,

            ) throws InterruptedException {
          pageImport.pageImport(step,inputpage);
//        providerthreadPoolExecutor.execute(() -> {
//                    try {
//                        pageImport.pageImport(0, 450000000,step);
////                        Thread.currentThread().sleep(1000);
//                    } catch (Exception e) {
//                        log.error("1put has error", e);
//                    }
//                }
//
//        );
//        providerthreadPoolExecutor.execute(() -> {
//                    try {
//                        pageImport.pageImport(90000001, 180000000,step);
////                        Thread.currentThread().sleep(1000);
//                    } catch (Exception e) {
//                        log.error("2put has error", e);
//                    }
//                }
//
//        );
//        providerthreadPoolExecutor.execute(() -> {
//                    try {
//                        pageImport.pageImport(180000001, 270000000,step);
////                        Thread.currentThread().sleep(1000);
//                    } catch (Exception e) {
//                        log.error("3put has error", e);
//                    }
//                }
//
//        );
//        providerthreadPoolExecutor.execute(() -> {
//                    try {
//                        pageImport.pageImport(270000001, 36000000,step);
////                        Thread.currentThread().sleep(1000);
//                    } catch (Exception e) {
//                        log.error("4put has error", e);
//                    }
//                }
//
//        );
//        providerthreadPoolExecutor.execute(() -> {
//                    try {
//                        pageImport.pageImport(36000001, 450000000,step);
////                        Thread.currentThread().sleep(1000);
//                    } catch (Exception e) {
//                        log.error("5put has error", e);
//                    }
//                }
//
//        );
//        providerthreadPoolExecutor.execute(() -> {
//                    try {
//                        pageImport.pageImport(450000001, 540000000,step);
////                        Thread.currentThread().sleep(1000);
//                    } catch (Exception e) {
//                        log.error("6put has error", e);
//                    }
//                }
//
//        );
//        providerthreadPoolExecutor.execute(() -> {
//                    try {
//                        pageImport.pageImport(540000001, 630000000,step);
////                        Thread.currentThread().sleep(1000);
//                    } catch (Exception e) {
//                        log.error("7put has error", e);
//                    }
//                }
//
//        );
//        providerthreadPoolExecutor.execute(() -> {
//                    try {
//                        pageImport.pageImport(630000001, 720000000,step);
////                        Thread.currentThread().sleep(1000);
//                    } catch (Exception e) {
//                        log.error("8put has error", e);
//                    }
//                }
//
//        );
//        providerthreadPoolExecutor.execute(() -> {
//                    try {
//                        pageImport.pageImport(450000001, endId,step);
////                        Thread.currentThread().sleep(1000);
//                    } catch (Exception e) {
//                        log.error("9put has error", e);
//                    }
//                }
//
//        );

    }
    @GetMapping("/statisticsCompany")
    public void statisticsCompany(
            @RequestParam("startId") Integer startId,
            @RequestParam("endId") Integer endId,
            @RequestParam("threadcount") Integer threadcount) {
        try {
            statisticsService.statisticsCompany(startId,endId,threadcount);
            TimeUnit.DAYS.sleep(365);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @GetMapping("/updateIndex")
    public void updateIndex(@RequestParam("orginCountry")String orginCountry,@RequestParam("updateCountry") String updateCountry) {
        try {
            pageImport.updateIndex(orginCountry,updateCountry);
            TimeUnit.DAYS.sleep(365);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/addColumn")
    public void addColumn(@RequestParam("step") Integer step,@RequestParam("inputpage") Integer inputpage) throws InterruptedException {
        pageImport.addColumn(inputpage,step);
    }

}

package com.meorient.mebuyerdiscovery.elasticsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class MeorientdfServicesElasticsearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeorientdfServicesElasticsearchApplication.class, args);
    }
//    public static void pageImport(Integer startId, Integer endId, Integer step) {
//        Integer pagesize=(endId-startId)/step;
//        System.out.println(pagesize);
//    }
//
//    public static void main(String[] args) {
//        pageImport(0,10,3);
//    }
}

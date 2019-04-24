package com.zjt.controller;

import com.zjt.inter.FeignTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: springcloud-zjt *
 * @description: *
 * @author: zhangjitong *
 * @create: 2019-04-24 16:33
 **/
@RestController
public class HelloControler {

    @Autowired
    FeignTest feign;

    @GetMapping(value = "/hi")
    public String hi(@RequestParam String name) {
        System.out.println("Feign--------------------------");
        return feign.sayHiFromClientOne( name );
    }
}
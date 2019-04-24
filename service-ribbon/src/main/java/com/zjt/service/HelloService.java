package com.zjt.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @program: springcloud-zjt *
 * @description: *
 * @author: zhangjitong *
 * @create: 2019-04-24 16:29
 **/
@Service
public class HelloService {

    @Autowired
    RestTemplate restTemplate;
    @HystrixCommand(fallbackMethod = "hiError")
    public String hiService(String name) {
        System.out.println("ribbon--------------------------");
        return restTemplate.getForObject("http://SERVICE-ZJT/demo/config/getName?name="+name,String.class);
    }
    public String hiError(String name) {
        return "hi,"+name+",sorry,error!";
    }

}
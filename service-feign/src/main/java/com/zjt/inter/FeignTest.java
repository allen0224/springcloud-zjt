package com.zjt.inter;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "service-zjt")
@Service
public interface FeignTest {
    @RequestMapping(value = "/demo/config/getName",method = RequestMethod.GET)
    String sayHiFromClientOne(@RequestParam(value = "name") String name);
}
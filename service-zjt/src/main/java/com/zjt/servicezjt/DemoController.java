package com.zjt.servicezjt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//支持配置中心属性热加载
@RefreshScope
@RestController
@RequestMapping("/demo/config")
public class DemoController {
    //获取配置中心的属性
    @Value("${profile}")
    private String profile;

    @Value("${server.port}")
    String port;
    @GetMapping("/getHost")
    public String getHost(){

        return this.profile;
    }
    @GetMapping("/getName")
    public String getHost(@RequestParam String name){

        return name+ " ,i am from port:" + port;
    }
}
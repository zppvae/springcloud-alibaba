package org.springcloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springcloud.feign.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@Slf4j
@RestController
public class TestController {

    @Value("${alibaba.zh-cn:zh}")
    private String zhCn;

    @Autowired
    private Client client;

    @GetMapping("/test")
    public String test(@RequestParam String name) {
        return zhCn + " " + client.test(name);
    }
}
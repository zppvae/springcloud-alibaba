package org.springcloud.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("alibaba-server")
public interface Client {

    @GetMapping("/test")
    String test(@RequestParam(name = "name") String name);

}
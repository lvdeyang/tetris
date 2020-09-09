package com.sumavision.bvc.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;



/**
 * 
 */

@FeignClient(name = "bvc-user-service")
public interface LoginServiceClient {
    @GetMapping(value = "/user/login")
    public String login(@RequestParam("username") String username , @RequestParam("password") String password);
}




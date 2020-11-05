package com.sumavision.tetris.scl.service;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.sumavision.tetris.config.feign.FeignConfiguration;

@FeignClient(name = "tetris-scl", configuration = FeignConfiguration.class)
public interface CapacityFeign {

}

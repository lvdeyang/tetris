package com.sumavision.tetris.scl.service;

import org.springframework.cloud.openfeign.FeignClient;

import com.sumavision.tetris.config.feign.FeignConfiguration;

@FeignClient(name = "tetris-scl", configuration = FeignConfiguration.class)
public interface CapacityFeign {

}

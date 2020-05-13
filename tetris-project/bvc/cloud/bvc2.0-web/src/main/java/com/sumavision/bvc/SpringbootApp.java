package com.sumavision.bvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;

import com.sumavision.tetris.commons.context.SpringContext;
import com.sumavision.tetris.config.feign.FeignConfiguration;

@Import(SpringContext.class)
@ComponentScan(excludeFilters = {@ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE, classes=FeignConfiguration.class)},
			   basePackages = {"com.sumavision.bvc", "com.sumavision.tetris", "com.suma.venus.resource", "com.suma.venus.message"})
@ServletComponentScan(basePackages = {"com.sumavision.bvc.listener", "com.sumavision.bvc.control.device.monitor.record"})
@EnableAutoConfiguration(exclude = {MultipartAutoConfiguration.class})
@EnableCaching
@EnableFeignClients(basePackages={"com.sumavision.bvc", "com.suma.venus.resource", "com.sumavision.tetris"})
@EnableEurekaClient
@SpringBootApplication
public class SpringbootApp extends SpringBootServletInitializer {

	public static void main(String[] args) {
		try{
			SpringApplication.run(SpringbootApp.class, args);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(SpringbootApp.class);
	}

}

package com.suma.venus.resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.sumavision.tetris.config.feign.FeignConfiguration;

@SpringBootApplication
@ComponentScan(excludeFilters = {@ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE, classes=FeignConfiguration.class)},
			   basePackages={"com.suma.venus", "com.sumavision.tetris"})
@ServletComponentScan(basePackages={"com.suma.venus.resource.listener", "com.suma.venus", "com.sumavision.tetris"})
@EnableFeignClients(basePackages={"com.suma.venus", "com.sumavision.tetris"})
@EnableEurekaClient
@EnableScheduling
public class BootApplication extends SpringBootServletInitializer {

    @Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(BootApplication.class);
	}

	public static void main(String[] args) {
        SpringApplication.run(BootApplication.class, args);
    }

}
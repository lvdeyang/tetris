package com.sumavision.tetris.menu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.FilterType;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

import com.sumavision.tetris.commons.context.SpringContext;
import com.sumavision.tetris.config.feign.FeignConfiguration;
import com.sumavision.tetris.config.feign.MultipartSupportConfig;

@ComponentScan(excludeFilters = {@ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE, classes=FeignConfiguration.class)}, 
			   basePackages = {"com.sumavision.tetris"})
@ServletComponentScan(basePackages = {"com.sumavision.tetris"})
@EnableAutoConfiguration(exclude = {MultipartAutoConfiguration.class})
@EnableCaching
@EnableFeignClients("com.sumavision.tetris")
@EnableEurekaClient
@SpringBootApplication
public class TetrisMenuApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(TetrisMenuApplication.class, args);
		SpringContext.asynchronizedDone();
	}
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(TetrisMenuApplication.class);
    }
	
}
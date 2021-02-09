package com.sumavision.tetris.record;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = { "com.sumavision.tetris" })
@ServletComponentScan(basePackages = { "com.sumavision.tetris" })
@EnableAutoConfiguration(exclude = { MultipartAutoConfiguration.class })
@SpringBootApplication
@EnableCaching
@EnableFeignClients("com.sumavision.tetris")
@EnableEurekaClient
public class TetrisRecordApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(TetrisRecordApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(TetrisRecordApplication.class);
	}

}
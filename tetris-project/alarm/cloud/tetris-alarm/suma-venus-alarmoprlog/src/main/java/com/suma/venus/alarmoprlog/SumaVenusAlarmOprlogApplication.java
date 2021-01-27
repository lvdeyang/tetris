package com.suma.venus.alarmoprlog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@ComponentScan(basePackages = { "com.suma.venus","com.sumavision.tetris" })
@ServletComponentScan(basePackages = { "com.suma.venus", "com.sumavision.tetris.auth.filter" })
@EnableFeignClients(basePackages={"com.suma.venus","com.sumavision.tetris"})
// @EnableAutoConfiguration(exclude = {MultipartAutoConfiguration.class})
@EnableEurekaClient
public class SumaVenusAlarmOprlogApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SumaVenusAlarmOprlogApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SumaVenusAlarmOprlogApplication.class);
	}

}

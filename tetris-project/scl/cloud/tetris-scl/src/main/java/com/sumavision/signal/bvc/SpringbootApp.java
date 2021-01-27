package com.sumavision.signal.bvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.sumavision.signal.bvc", "com.sumavision.tetris", "com.suma.venus.resource", "com.suma.venus.message"})
@ServletComponentScan(basePackages = {"com.sumavision.signal.bvc.listener"})
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages={"com.sumavision.signal.bvc", "com.suma.venus.resource", "com.sumavision.tetris"})
public class SpringbootApp extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(SpringbootApp.class);
	}
	public static void main(String[] args) {
		try{
			System.setProperty("spring.devtools.restart.enabled", "false");
			SpringApplication.run(SpringbootApp.class, args);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}

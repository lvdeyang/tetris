package com.sumavision.bvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import com.sumavision.tetris.commons.context.SpringContext;

@ComponentScan(basePackages = {"com.sumavision.bvc", "com.sumavision.tetris", "com.suma.venus.resource", "com.suma.venus.message"})
@ServletComponentScan(basePackages = {"com.sumavision.bvc.listener", "com.sumavision.bvc.control.device.monitor.record"})
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages={"com.sumavision.bvc", "com.suma.venus.resource", "com.sumavision.tetris"})
@Import(SpringContext.class)
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

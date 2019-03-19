package com.sumavision.tetris.spring.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.sumavision.tetris.spring.zuul.auth.filter.ApiFilter;
import com.sumavision.tetris.spring.zuul.auth.filter.MobileLoginFilter;

@ComponentScan(basePackages = {"com.sumavision.tetris"})
@ServletComponentScan(basePackages = {"com.sumavision.tetris"})
@EnableZuulProxy
@EnableEurekaClient
@EnableDiscoveryClient
@SpringBootApplication
public class TetrisSpringZuulApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(TetrisSpringZuulApplication.class, args);
	}
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(TetrisSpringZuulApplication.class);
    }
	
	@Bean
	public MobileLoginFilter mobileLoginFilter() {
		return new MobileLoginFilter();
	}
	
	@Bean
	public ApiFilter apiFilter(){
		return new ApiFilter();
	}
	
}

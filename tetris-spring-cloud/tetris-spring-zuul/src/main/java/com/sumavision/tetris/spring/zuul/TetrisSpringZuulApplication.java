package com.sumavision.tetris.spring.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.sumavision.tetris.spring.zuul.auth.filter.ApiFilter;
import com.sumavision.tetris.spring.zuul.auth.filter.capture.ApiCaptureLoginFilter;
import com.sumavision.tetris.spring.zuul.auth.filter.qt.ApiQtLoginFilter;
import com.sumavision.tetris.spring.zuul.auth.filter.server.ApiServerLoginFilter;
import com.sumavision.tetris.spring.zuul.auth.filter.terminal.ApiTerminalLoginFilter;

@ComponentScan(basePackages = {"com.sumavision.tetris"})
@ServletComponentScan(basePackages = {"com.sumavision.tetris"})
@EnableAutoConfiguration(exclude = {MultipartAutoConfiguration.class})
@EnableZuulProxy
@EnableEurekaClient
@EnableFeignClients("com.sumavision.tetris")
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
	public ApiCaptureLoginFilter apiCaptureLoginFilter(){
		return new ApiCaptureLoginFilter();
	}
	
	@Bean
	public ApiQtLoginFilter apiQtLoginFilter(){
		return new ApiQtLoginFilter();
	}
	
	@Bean
	public ApiServerLoginFilter apiServerLoginFilter(){
		return new ApiServerLoginFilter();
	}
	
	@Bean
	public ApiTerminalLoginFilter apiTerminalLoginFilter() {
		return new ApiTerminalLoginFilter();
	}
	
	@Bean
	public ApiFilter apiFilter(){
		return new ApiFilter();
	}
	
}

package com.sumavision.tetris.mims;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.sumavision.tetris"})
@ServletComponentScan(basePackages = {"com.sumavision.tetris.mvc.listener"})
@EnableAutoConfiguration(exclude = {MultipartAutoConfiguration.class})
@EnableCaching
@SpringBootApplication
public class TetrisMimsApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(TetrisMimsApplication.class, args);
	}
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(TetrisMimsApplication.class);
    }
	
}

package com.sumavision.tetris.mims;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.ErrorPageFilter;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import com.sumavision.tetris.config.feign.FeignConfiguration;

@ComponentScan(excludeFilters = {@ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE, classes=FeignConfiguration.class)},
			   basePackages = {"com.sumavision.tetris"})
@ServletComponentScan(basePackages = {"com.sumavision.tetris"})
@EnableAutoConfiguration(exclude = {MultipartAutoConfiguration.class})
@EnableCaching
@EnableFeignClients("com.sumavision.tetris")
@EnableEurekaClient
@SpringBootApplication
public class TetrisMimsApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(TetrisMimsApplication.class, args);
	}
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(TetrisMimsApplication.class);
    }
	
	/**
	 * 解决服务端报错问题<br/>
	 * <p>
	 * 	Cannot forward to error page for request[/xxx]<br/>
	 * 	as the response has already been committed. As a result, <br/>
	 * 	the response may have the wrong status code. <br/>
	 * 	If your application is running on WebSphere Application <br/>
	 * 	Server you may be able to resolve this problem by setting <br/>
	 * 	com.ibm.ws.webcontainer.invokeFlushAfterService to false<br/>
	 * </p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月5日 下午3:13:30
	 */
	@Bean
	public ErrorPageFilter errorPageFilter() {
	    return new ErrorPageFilter();
	}

	/**
	 * 同上<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月5日 下午3:15:10
	 */
	@Bean
	public FilterRegistrationBean disableSpringBootErrorFilter(ErrorPageFilter filter) {
	    FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
	    filterRegistrationBean.setFilter(filter);
	    filterRegistrationBean.setEnabled(false);
	    return filterRegistrationBean;
	}
	
}

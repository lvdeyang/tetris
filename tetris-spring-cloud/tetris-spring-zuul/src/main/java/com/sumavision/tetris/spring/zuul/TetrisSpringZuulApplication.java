package com.sumavision.tetris.spring.zuul;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.pre.ServletDetectionFilter;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.EnableRetry;

import com.sumavision.tetris.spring.zuul.auth.filter.ApiFilter;
import com.sumavision.tetris.spring.zuul.auth.filter.MultipartFilter;
import com.sumavision.tetris.spring.zuul.auth.filter.android.ApiAndroidLoginFilter;
import com.sumavision.tetris.spring.zuul.auth.filter.g01.web.ApiG01WebLoginFilter;
import com.sumavision.tetris.spring.zuul.auth.filter.process.ApiProcessLoginFilter;
import com.sumavision.tetris.spring.zuul.auth.filter.qt.ApiQtLoginFilter;
import com.sumavision.tetris.spring.zuul.auth.filter.qt.ApiQtPollingLoginFilter;
import com.sumavision.tetris.spring.zuul.auth.filter.qt.ApiQtScheduleLoginFilter;
import com.sumavision.tetris.spring.zuul.auth.filter.qt.ApiQtZkLoginFilter;
import com.sumavision.tetris.spring.zuul.auth.filter.screen.web.ApiScreenWebLoginFilter;
import com.sumavision.tetris.spring.zuul.auth.filter.server.ApiServerLoginFilter;
import com.sumavision.tetris.spring.zuul.auth.filter.terminal.ApiTerminalLoginFilter;
import com.sumavision.tetris.spring.zuul.auth.filter.thirdpart.ApiThirdpartLoginFilter;
import com.sumavision.tetris.spring.zuul.auth.filter.tvos.ApiTvosLoginFilter;
import com.sumavision.tetris.spring.zuul.auth.filter.zoom.ApiZoomAndroidLoginFilter;
import com.sumavision.tetris.spring.zuul.auth.filter.zoom.ApiZoomQtLoginFilter;

@ComponentScan(basePackages = {"com.sumavision.tetris"})
//这个地方不能扫描user-feign中的filter
@ServletComponentScan(basePackages = {"com.sumavision.tetris.mvc.listener", "com.sumavision.tetris.spring.zuul"})
@EnableAutoConfiguration(exclude = {MultipartAutoConfiguration.class})
@EnableZuulProxy
@EnableEurekaClient
@EnableFeignClients("com.sumavision.tetris")
@EnableDiscoveryClient
@EnableRetry
@SpringBootApplication
public class TetrisSpringZuulApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(TetrisSpringZuulApplication.class, args);
	}
	
	/**
	 * 解决上传文件重置问题<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月31日 上午11:51:29
	 */
	@Bean
    public TomcatServletWebServerFactory containerFactory() {
        return new TomcatServletWebServerFactory() {
            protected void customizeConnector(Connector connector) {
            	super.customizeConnector(connector);
            	if(connector.getProtocolHandler() instanceof AbstractHttp11Protocol<?>){
	                ((AbstractHttp11Protocol<?>)connector.getProtocolHandler()).setMaxSwallowSize(-1);
	            }
                /*int maxSize = 50000000;
                super.customizeConnector(connector);
                connector.setMaxPostSize(maxSize);
                connector.setMaxSavePostSize(maxSize);
                if (connector.getProtocolHandler() instanceof AbstractHttp11Protocol) {

                    ((AbstractHttp11Protocol <?>) connector.getProtocolHandler()).setMaxSwallowSize(maxSize);
                    logger.info("Set MaxSwallowSize "+ maxSize);
                }*/
            }
        };

    }
	
	/*2.x中被换了
	@Bean
    public TomcatEmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory(){
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
        factory.addConnectorCustomizers(new TomcatConnectorCustomizer(){
			@Override
			public void customize(Connector connector){
				if(connector.getProtocolHandler() instanceof AbstractHttp11Protocol<?>){
	                ((AbstractHttp11Protocol<?>)connector.getProtocolHandler()).setMaxSwallowSize(-1);
	            }
			}
        });
        return factory;
    }*/
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(TetrisSpringZuulApplication.class);
    }
	
	@Bean
	public ApiQtZkLoginFilter apiQtZkLoginFilter(){
		return new ApiQtZkLoginFilter();
	}
	
	@Bean
	public ApiTvosLoginFilter apiTvosLoginFilter(){
		return new ApiTvosLoginFilter();
	}
	
	@Bean
	public ApiAndroidLoginFilter apiAndroidLoginFilter(){
		return new ApiAndroidLoginFilter();
	}
	
	@Bean
	public ApiProcessLoginFilter apiProcessLoginFilter(){
		return new ApiProcessLoginFilter();
	}
	
	@Bean
	public ApiQtLoginFilter apiQtLoginFilter(){
		return new ApiQtLoginFilter();
	}
	
	@Bean
	public ApiQtScheduleLoginFilter apiQtScheduleLoginFilter(){
		return new ApiQtScheduleLoginFilter();
	}
	
	@Bean
	public ApiQtPollingLoginFilter apiQtPollingLoginFilter(){
		return new ApiQtPollingLoginFilter();
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
	public ApiThirdpartLoginFilter apiThirdpartLoginFilter() {
		return new ApiThirdpartLoginFilter();
	}
	
	@Bean
	public ServletDetectionFilter servletDetectionFilter(){
		return new MultipartFilter();
	}
	
	
	@Bean
	public ApiZoomAndroidLoginFilter apiZoomAndroidLoginFilter(){
		return new ApiZoomAndroidLoginFilter();
	}
	
	@Bean
	public ApiZoomQtLoginFilter apiZoomQtLoginFilter(){
		return new ApiZoomQtLoginFilter();
	}
	
	@Bean
	public ApiFilter apiFilter(){
		return new ApiFilter();
	}
	
	@Bean
	public ApiG01WebLoginFilter apiG01WebLoginFilter() {
		return new ApiG01WebLoginFilter();
	}
	
	@Bean
	public ApiScreenWebLoginFilter apiScreenWebLoginFilter() {
		return new ApiScreenWebLoginFilter();
	}
}

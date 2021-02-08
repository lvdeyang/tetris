package com.sumavision.tetris.spring.zuul;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryPolicyFactory;
import org.springframework.cloud.netflix.ribbon.ServerIntrospector;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.netflix.ribbon.apache.RetryableRibbonLoadBalancingHttpClient;
import org.springframework.cloud.netflix.ribbon.apache.RibbonLoadBalancingHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netflix.client.AbstractLoadBalancerAwareClient;
import com.netflix.client.RetryHandler;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.servo.monitor.Monitors;

@Configuration
public class CustomRibbonConfiguration {
	
//	@Value("${ribbon.client.name}")
//	private String name = "client";
	
	
	@Bean
	public LoadBalancedRetryPolicyFactory loadBalancedRetryPolicyFactory(SpringClientFactory clientFactory){
		return new MyRibbonLoadBalacedRetryPolicyFactory(clientFactory);
	}
	
	/*@Configuration	
	protected static class MyHttpClientRibbonConfiguration {
//		@Value("${ribbon.client.name}")
//		private String name = "client";
		

		@Bean	
		@ConditionalOnClass(name = "org.springframework.retry.support.RetryTemplate")
		public RetryableRibbonLoadBalancingHttpClient retryableRibbonLoadBalancingHttpClient(
				IClientConfig config, ServerIntrospector serverIntrospector,
				ILoadBalancer loadBalancer, RetryHandler retryHandler,
				LoadBalancedRetryPolicyFactory loadBalancedRetryPolicyFactory) {
			MyRibbonHttpClient client = new MyRibbonHttpClient(
					config, serverIntrospector, loadBalancedRetryPolicyFactory);
			client.setLoadBalancer(loadBalancer);
			client.setRetryHandler(retryHandler);
			String name = config.getClientName();
			Monitors.registerObject("Client_" + name, client);
			return client;
		}
	}*/
	
	
	

}

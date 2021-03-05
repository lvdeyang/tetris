package com.sumavision.tetris.spring.zuul;

import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

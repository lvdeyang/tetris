package com.sumavision.tetris.spring.zuul;

import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryPolicy;
import org.springframework.cloud.client.loadbalancer.ServiceInstanceChooser;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancedRetryPolicy;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancedRetryPolicyFactory;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerContext;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;

public class MyRibbonLoadBalacedRetryPolicyFactory extends RibbonLoadBalancedRetryPolicyFactory{

		private SpringClientFactory clientFactory;
		
		public MyRibbonLoadBalacedRetryPolicyFactory(SpringClientFactory clientFactory) {
			super(clientFactory);
			this.clientFactory = clientFactory; 
		}
		
		@Override
	    public LoadBalancedRetryPolicy create(String serviceId, ServiceInstanceChooser loadBalanceChooser) {
	        RibbonLoadBalancerContext lbContext = this.clientFactory
	                .getLoadBalancerContext(serviceId);
	        if(!"eb-insert-web".equals(serviceId)){
	        	return new RibbonLoadBalancedRetryPolicy(serviceId, lbContext, loadBalanceChooser, clientFactory.getClientConfig(serviceId));
	        }else{	        	
	        	return new MyRibbonLoadBalacedRetryPolicy(serviceId, lbContext, loadBalanceChooser, clientFactory.getClientConfig(serviceId));
	        }
	    }
	}
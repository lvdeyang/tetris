package com.sumavision.tetris.spring.zuul;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryContext;
import org.springframework.cloud.client.loadbalancer.ServiceInstanceChooser;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancedRetryPolicy;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerContext;
import org.springframework.http.HttpMethod;

import com.netflix.client.config.IClientConfig;

public class MyRibbonLoadBalacedRetryPolicy extends RibbonLoadBalancedRetryPolicy{

		 private int sameServerCount = 0;
		    private int nextServerCount = 0;
		    private String serviceId;
		    private RibbonLoadBalancerContext lbContext;
		    private ServiceInstanceChooser loadBalanceChooser;
		    List<Integer> retryableStatusCodes = new ArrayList<>();
		    private Throwable lastThrowable;

		    public MyRibbonLoadBalacedRetryPolicy(String serviceId, RibbonLoadBalancerContext context, ServiceInstanceChooser loadBalanceChooser) {
		        super(serviceId, context, loadBalanceChooser);
		    	this.serviceId = serviceId;
		        this.lbContext = context;
		        this.loadBalanceChooser = loadBalanceChooser;
		    }

		    public MyRibbonLoadBalacedRetryPolicy(String serviceId, RibbonLoadBalancerContext context, ServiceInstanceChooser loadBalanceChooser,
		                                         IClientConfig clientConfig) {
		    	super(serviceId, context, loadBalanceChooser);
		        this.serviceId = serviceId;
		        this.lbContext = context;
		        this.loadBalanceChooser = loadBalanceChooser;
		        String retryableStatusCodesProp = clientConfig.getPropertyAsString(RETRYABLE_STATUS_CODES, "");
		        String[] retryableStatusCodesArray = retryableStatusCodesProp.split(",");
		        for(String code : retryableStatusCodesArray) {
		            if(!StringUtils.isEmpty(code)) {
		                try {
		                    retryableStatusCodes.add(Integer.valueOf(code));
		                } catch (NumberFormatException e) {
		                    //TODO log
		                }
		            }
		        }
		    }

		    public boolean canRetry(LoadBalancedRetryContext context) {
		        HttpMethod method = context.getRequest().getMethod();
		        if(lastThrowable == null){
		        	return HttpMethod.GET == method || lbContext.isOkToRetryOnAllOperations();			        	
		        }else{
		        	return HttpMethod.GET == method || lbContext.isOkToRetryOnAllOperations() 
			        		|| lastThrowable instanceof HttpHostConnectException || lastThrowable instanceof ConnectTimeoutException;
		        }
		        
		    }
		    
		    public boolean canRetrySameServer(LoadBalancedRetryContext context) {
		        return sameServerCount < lbContext.getRetryHandler().getMaxRetriesOnSameServer() && canRetry(context);
		    }
		    
		    public boolean canRetryNextServer(LoadBalancedRetryContext context) {
		        //this will be called after a failure occurs and we increment the counter
		        //so we check that the count is less than or equals to too make sure
		        //we try the next server the right number of times
		        return nextServerCount <= lbContext.getRetryHandler().getMaxRetriesOnNextServer() && canRetry(context);
		    }

		    @Override
		    public void close(LoadBalancedRetryContext context) {

		    }

		    @Override
		    public void registerThrowable(LoadBalancedRetryContext context, Throwable throwable) {
		        //Check if we need to ask the load balancer for a new server.
		        //Do this before we increment the counters because the first call to this method
		        //is not a retry it is just an initial failure.
		    	lastThrowable = throwable;
		        if(!canRetrySameServer(context)  && canRetryNextServer(context)) {
		            context.setServiceInstance(loadBalanceChooser.choose(serviceId));
		        }
		        //This method is called regardless of whether we are retrying or making the first request.
		        //Since we do not count the initial request in the retry count we don't reset the counter
		        //until we actually equal the same server count limit.  This will allow us to make the initial
		        //request plus the right number of retries.
		        if(sameServerCount >= lbContext.getRetryHandler().getMaxRetriesOnSameServer() && canRetry(context)) {
		            //reset same server since we are moving to a new server
		            sameServerCount = 0;
		            nextServerCount++;
		            if(!canRetryNextServer(context)) {
		                context.setExhaustedOnly();
		            }
		        } else {
		            sameServerCount++;
		        }

		    }

		    @Override
		    public boolean retryableStatusCode(int statusCode) {
		        return retryableStatusCodes.contains(statusCode);
		    }
		
	}
package com.sumavision.tetris.spring.zuul;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.feign.FeignAutoConfiguration;
import org.springframework.cloud.netflix.feign.FeignClientsConfiguration;
import org.springframework.cloud.netflix.feign.ribbon.FeignRibbonClientAutoConfiguration;
import org.springframework.cloud.netflix.feign.ribbon.LoadBalancerFeignClient;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonClientConfiguration;
import org.springframework.cloud.netflix.ribbon.apache.RetryableRibbonLoadBalancingHttpClient;
import org.springframework.cloud.netflix.zuul.ZuulProxyConfiguration;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.pre.PreDecorationFilter;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonRoutingFilter;
import org.springframework.retry.backoff.BackOffInterruptedException;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.PingUrl;
import com.netflix.loadbalancer.RoundRobinRule;
import com.netflix.loadbalancer.Server;
import com.netflix.zuul.context.RequestContext;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;


/**
 * 针对接入模块特定接口指定的服务提供者
 * @author Administrator
 *
 */
public class InsertIndexRule extends RoundRobinRule{

	private static final Logger log = LoggerFactory.getLogger(InsertIndexRule.class);
	
   	private static DistributedLockHandler lockHandler;   	   	
   	
   	private static final String redisKey = "com.suma.venus.insert.thread.MasterExamineTask";
   	
   	private static FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
   	
	@Override
	public Server choose(Object key) {
		// TODO Auto-generated method stub		
		return chooseProper(getLoadBalancer(), key);
	}

	@Override
	public void initWithNiwsConfig(IClientConfig config) {
		// TODO Auto-generated method stub
//		LoadBalancerFeignClient
//		RibbonRoutingFilter
//		PreDecorationFilter
//		RibbonClientConfiguration
//		ZuulProxyConfiguration
//		RibbonAutoConfiguration
//		PingUrl
//		FeignAutoConfiguration
//		FeignRibbonClientAutoConfiguration
//		FeignClientsConfiguration
		
	}
	
	private Server chooseProper(ILoadBalancer lb, Object key){
		if(lb == null){
			log.warn("no loadbalancer");
			return null;
		}
		
		if(lockHandler == null){
			lockHandler = SpringContextUtil.getBean(DistributedLockHandler.class);
		}				
				
		List<Server> reachables = lb.getReachableServers();
		if(reachables != null && reachables.size() > 0){	
			if(reachables.size() == 1){
				return reachables.get(0);
			}
			RequestContext ctx = RequestContext.getCurrentContext();
			HttpServletRequest request = ctx.getRequest();
			String requestUri = request.getRequestURI();
			requestUri = requestUri.replaceFirst(new StringBufferWrapper().append("/").append(requestUri.split("/")[1]).toString(), "");
			if(!requestUri.startsWith("/eb-insert-web/sarftPlat/downReq")){
				return super.choose(key);
			}
			for(Server serv:reachables){					
				String identity = lockHandler.getLockValue(redisKey);
				if(identity != null && identity.contains(serv.getHostPort())){
					if(key != null){
						List<Server> tmpList = new ArrayList<Server>();
						for(Server tmp:reachables){
							if(tmp != serv){
								tmpList.add(tmp);
							}
						}
						int index = new Random().nextInt(tmpList.size());
						log.info("retry to " + key.toString() + ":" + tmpList.get(index).getHostPort());
						boolean backOff = treatBackOff();
						if(!backOff){
							return null;
						}
						return tmpList.get(index);
					}					
					return serv;
				}
			}
			boolean backOff = treatBackOff();
			if(!backOff){
				return null;
			}
			return super.choose(key);
		}
		
		return null;
		
	}
	

	private synchronized boolean treatBackOff(){
		try {
			backOffPolicy.backOff(null);
		}
		catch (BackOffInterruptedException ex) {							
			return false;
		}
		return true;
	}
}

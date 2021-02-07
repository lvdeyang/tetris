package com.sumavision.tetris.spring.zuul;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.RoundRobinRule;
import com.netflix.loadbalancer.Server;
import com.netflix.zuul.context.RequestContext;


/**
 * 选取作为主服务的服务提供者
 * @author Administrator
 *
 */
public class MasterIndexRule extends RoundRobinRule{

	private static final Logger log = LoggerFactory.getLogger(MasterIndexRule.class);
	
   	private static DistributedLockHandler lockHandler;   	   	
   	
   	private static final String redisKey = "com.suma.venus.screen.listener.MasterExamineTask";
   	
	@Override
	public Server choose(Object key) {
		// TODO Auto-generated method stub
		return chooseProper(getLoadBalancer(), key);
	}

	@Override
	public void initWithNiwsConfig(IClientConfig config) {
		// TODO Auto-generated method stub
		
	}
	
	public Server chooseProper(ILoadBalancer lb, Object key){
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
						return tmpList.get(index);
					}	
					return serv;
				}
			}
			RequestContext ctx = RequestContext.getCurrentContext();
			if(!ctx.getRequest().getMethod().equalsIgnoreCase("GET")){
				return null;
			}
			return super.choose(key);
		}
		
		return null;
		
	}
	
	

}

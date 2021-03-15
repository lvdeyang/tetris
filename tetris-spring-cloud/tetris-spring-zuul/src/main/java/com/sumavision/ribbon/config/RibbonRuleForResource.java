package com.sumavision.ribbon.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancer;
import com.netflix.loadbalancer.ClientConfigEnabledRoundRobinRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.LoadBalancerStats;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerStats;

public class RibbonRuleForResource extends ClientConfigEnabledRoundRobinRule {

	private static final Logger log = LoggerFactory.getLogger(RibbonRuleForResource.class);

	private String chosenServerIp = "";

	private LoadBalancerStats loadBalancerStats;

	@Override
	public Server choose(Object key) {

		if (key != null && key.toString().contains("resource-heartbeat")) {
			log.info("RibbonRuleForResource in, key=" + JSONObject.toJSONString(key));
			return choose(getLoadBalancer(), key);
		}

		log.info("RoundRobinRule in");
		return super.choose(null);
	}

	public Server choose(ILoadBalancer lb, Object key) {

		// 转换心跳的连接会匹配这个选择策略

		if (lb == null) {
			return null;
		}

		Server server = null;

		List<Server> reachableServers = lb.getReachableServers();
		List<Server> allServers = lb.getAllServers();

		if ((reachableServers.size() == 0) || (allServers.size() == 0)) {
			log.warn("No up servers available from load balancer: " + lb);
			return null;
		}

		// 策略实现
		Server oldChosenServer = getOldChosenServerInServerList(chosenServerIp, reachableServers);

		if (StringUtils.isEmpty(chosenServerIp) || oldChosenServer == null) {

			server = super.choose(null);
			log.info("new choose server");

		} else {
			ServerStats serverStats = null;
			long currentTime = System.currentTimeMillis();

			if (loadBalancerStats != null) {
				serverStats = loadBalancerStats.getSingleServerStat(oldChosenServer);
			} else {
				log.warn("loadBalancerStats null");
			}

			if (serverStats != null && !serverStats.isCircuitBreakerTripped(currentTime) && oldChosenServer.isAlive()
					&& (oldChosenServer.isReadyToServe())) {
				server = oldChosenServer;
				log.info("use old chosen server");

			} else {
				server = super.choose(null);
				log.info("old chosen server isCircuitBreakerTripped, new choose server");
			}
		}

		if (server != null) {
			chosenServerIp = server.getHost();
		} else {
			chosenServerIp = null;
		}

		log.info("finally choose=" + chosenServerIp);

		return server;

	}

	@Override
	public void setLoadBalancer(ILoadBalancer lb) {
		super.setLoadBalancer(lb);
		if (lb instanceof AbstractLoadBalancer) {
			loadBalancerStats = ((AbstractLoadBalancer) lb).getLoadBalancerStats();
		}
	}

	@Override
	public void initWithNiwsConfig(IClientConfig clientConfig) {

		// TODO Auto-generated method stub
	}

	/**
	 * 
	 * 
	 * @param ip
	 * @param serverList
	 * @return
	 */
	private Server getOldChosenServerInServerList(String ip, List<Server> serverList) {

		if (StringUtils.isEmpty(ip)) {
			return null;
		}

		for (Server server : serverList) {

			if (server.getHost().equals(ip)) {
				return server;
			}
		}

		return null;
	}

}

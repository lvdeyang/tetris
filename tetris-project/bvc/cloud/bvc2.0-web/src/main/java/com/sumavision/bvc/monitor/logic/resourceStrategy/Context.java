package com.sumavision.bvc.monitor.logic.resourceStrategy;

import java.util.List;

import com.suma.venus.resource.pojo.ChannelSchemePO;
import com.sumavision.bvc.BO.ChannelSchemeBO;
import com.sumavision.bvc.BO.LockResourceBO;
import com.sumavision.bvc.DO.ResourceDO;

//使用资源算法的角色，维持一个对AbstractResourceStragy的引用，用于定义采用的策略
public class Context {
	// 资源处理策略
	AbstractResourceStrategy resourceStrategy;
	// 资源对象BO

	public void setResourceStrategy(AbstractResourceStrategy resourceStrategy) {
		this.resourceStrategy = resourceStrategy;
	}

	// 调用资源策略类中的算法
	public ChannelSchemeBO analysisResource(List<ChannelSchemeBO> channelSchemes, Object DO) {
		return resourceStrategy.analysisResource(channelSchemes, DO);
	}
}

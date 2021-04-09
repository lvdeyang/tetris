package com.sumavision.bvc.monitor.logic.resourceStrategy;

import java.util.List;

import com.sumavision.bvc.BO.ChannelSchemeBO;


//资源处理策略的抽象类，
public abstract class AbstractResourceStrategy {
	public abstract ChannelSchemeBO analysisResource(List<ChannelSchemeBO> channelSchemeBOs, Object DO);
}

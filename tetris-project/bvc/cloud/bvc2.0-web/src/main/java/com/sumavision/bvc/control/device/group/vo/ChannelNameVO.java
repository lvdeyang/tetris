package com.sumavision.bvc.control.device.group.vo;

import com.sumavision.bvc.device.group.enumeration.ChannelType;

public class ChannelNameVO {

	private ChannelType channelType;
	
	private String name;

	public ChannelType getChannelType() {
		return channelType;
	}

	public ChannelNameVO setChannelType(ChannelType channelType) {
		this.channelType = channelType;
		return this;
	}

	public String getName() {
		return name;
	}

	public ChannelNameVO setName(String name) {
		this.name = name;
		return this;
	}
	
	
}

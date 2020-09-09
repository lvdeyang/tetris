package com.sumavision.tetris.cs.channel.broad;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum ChannelServerType {
	CS_LOCAL_SERVER("平台微服务"),
	ABILITY_STREAM("推流能力"),
	PUSH_TERMINAL("push播发端");
	
	private String name;
	
	public String getName() {
		return name;
	}

	private ChannelServerType(String name) {
		this.name = name;
	}
	
	public static ChannelServerType fromName(String name) throws Exception{
		ChannelServerType[] values = ChannelServerType.values();
		for (ChannelServerType channelServerType : values) {
			if (channelServerType.getName().equals(name)) {
				return channelServerType;
			}
		}
		throw new ErrorTypeException("ChannelServerType", name);
	}
}

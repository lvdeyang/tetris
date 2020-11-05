package com.sumavision.bvc.control.device.monitor.device;

import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;

/**
 * 设备通道<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年4月25日 下午4:41:40
 */
public class ChannelVO {

	/** 通道id */
	private String channelId;
	
	/** 通道名称 */
	private String name;
	
	/** 通道类型 */
	private String baseType;

	public String getChannelId() {
		return channelId;
	}

	public ChannelVO setChannelId(String channelId) {
		this.channelId = channelId;
		return this;
	}

	public String getName() {
		return name;
	}

	public ChannelVO setName(String name) {
		this.name = name;
		return this;
	}
	
	public String getBaseType() {
		return baseType;
	}

	public ChannelVO setBaseType(String baseType) {
		this.baseType = baseType;
		return this;
	}
	
	public ChannelVO set(ChannelSchemeDTO entity) throws Exception{
		this.setChannelId(entity.getChannelId())
        	.setName(ChannelType.fromChannelType(entity.getChannelId().replace("_", "-")).getName())
        	.setBaseType(entity.getBaseType());
		return this;
	}
}

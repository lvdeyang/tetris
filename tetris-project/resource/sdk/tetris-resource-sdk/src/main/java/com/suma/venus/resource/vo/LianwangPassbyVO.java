package com.suma.venus.resource.vo;

import com.suma.venus.resource.pojo.LianwangPassbyPO;

public class LianwangPassbyVO {
	
	/** 联网id */
	private String layerId;

	/** passby协议 */
	private String protocol;
	
	/** 业务类型 */
	private String type;

	public String getLayerId() {
		return layerId;
	}

	public LianwangPassbyVO setLayerId(String layerId) {
		this.layerId = layerId;
		return this;
	}

	public String getProtocol() {
		return protocol;
	}

	public LianwangPassbyVO setProtocol(String protocol) {
		this.protocol = protocol;
		return this;
	}

	public String getType() {
		return type;
	}

	public LianwangPassbyVO setType(String type) {
		this.type = type;
		return this;
	}
	
	public LianwangPassbyVO set(LianwangPassbyPO entity)throws Exception{
		this.setLayerId(entity.getLayerId())
		.setProtocol(entity.getProtocol())
		.setType(entity.getType());
		return this;
	}
}

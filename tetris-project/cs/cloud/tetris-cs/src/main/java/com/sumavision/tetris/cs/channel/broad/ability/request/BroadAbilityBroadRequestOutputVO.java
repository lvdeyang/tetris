package com.sumavision.tetris.cs.channel.broad.ability.request;

public class BroadAbilityBroadRequestOutputVO {
	/** 输出地址 */
	private String url;
	
	private String localIp;
	
	/** 输出封装 */
	private String type;

	public String getUrl() {
		return url;
	}

	public BroadAbilityBroadRequestOutputVO setUrl(String url) {
		this.url = url;
		return this;
	}

	public String getType() {
		return type;
	}

	public BroadAbilityBroadRequestOutputVO setType(String type) {
		this.type = type;
		return this;
	}

	public String getLocalIp() {
		return localIp;
	}

	public BroadAbilityBroadRequestOutputVO setLocalIp(String localIp) {
		this.localIp = localIp;
		return this;
	}
}

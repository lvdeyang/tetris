package com.sumavision.tetris.cs.channel.broad.ability.request;

public class BroadAbilityBroadRequestOutputVO {
	/** 输出地址 */
	private String url;
	
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
}

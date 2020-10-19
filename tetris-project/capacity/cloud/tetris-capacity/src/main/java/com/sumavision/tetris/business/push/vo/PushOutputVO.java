package com.sumavision.tetris.business.push.vo;

public class PushOutputVO {

	private String localIp;

	/** 输出地址 */
	private String url;
	
	/** 输出封装格式 */
	private String type;

	public String getLocalIp() {
		return localIp;
	}

	public void setLocalIp(String localIp) {
		this.localIp = localIp;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}

package com.sumavision.tetris.business.director.vo;

/**
 * 源参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年2月14日 下午3:06:52
 */
public class SourceVO {
	
	/** 源bundleId */
	private String bundleId;
	
	/** 类型，区分srt,udp等源 */
	private String type;

	/** 源IP */
	private String ip;
	
	/** 源端口 */
	private String port;

	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}
	
}

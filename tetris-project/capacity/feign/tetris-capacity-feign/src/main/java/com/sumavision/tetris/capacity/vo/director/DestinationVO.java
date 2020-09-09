package com.sumavision.tetris.capacity.vo.director;

/**
 * 目的参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年2月17日 上午8:19:15
 */
public class DestinationVO {

	/** 目的bundleId */
	private String bundleId;
	
	/** 类型，区分udp等源 */
	private String type;

	/** 源目的IP */
	private String ip;
	
	/** 源目的端口 */
	private String port;
	
	/** 输出码率 */
	private Long bitrate;

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

	public Long getBitrate() {
		return bitrate;
	}

	public void setBitrate(Long bitrate) {
		this.bitrate = bitrate;
	}
	
}

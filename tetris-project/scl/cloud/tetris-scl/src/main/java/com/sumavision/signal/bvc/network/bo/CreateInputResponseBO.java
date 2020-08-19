package com.sumavision.signal.bvc.network.bo;

/**
 * 创建输入返回<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年3月16日 上午11:35:55
 */
public class CreateInputResponseBO {

	private Integer sid;
	
	private String ip;
	
	private String port;

	public Integer getSid() {
		return sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
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

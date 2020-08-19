package com.sumavision.tetris.sts.task.taskParamOutput;

import java.io.Serializable;
//udp_ts_passby,rtp_ts_passby
public class PassbyCommonBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6235624034203128694L;

	private String ip;
	private Integer port;
	private String local_ip;
	private OutputMediaBO media;
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getLocal_ip() {
		return local_ip;
	}
	public void setLocal_ip(String local_ip) {
		this.local_ip = local_ip;
	}
	public OutputMediaBO getMedia() {
		return media;
	}
	public void setMedia(OutputMediaBO media) {
		this.media = media;
	}
	
}

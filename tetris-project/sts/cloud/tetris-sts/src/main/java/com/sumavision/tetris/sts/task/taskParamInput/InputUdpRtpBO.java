package com.sumavision.tetris.sts.task.taskParamInput;

import java.io.Serializable;


/**
 * udp_ts  rtp_ts
 */
public class InputUdpRtpBO implements InputCommon,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2630289412252269237L;
	private String source_ip;
	private Integer source_port;
	private String local_ip;
	private Igmpv3 igmpv3;
	public InputUdpRtpBO(String source_ip, Integer source_port,String local_ip,Igmpv3 igmpv3){
		this.source_ip = source_ip;
		this.source_port = source_port;
		this.local_ip = local_ip;
		this.igmpv3 = igmpv3;
	}
	
	public String getSource_ip() {
		return source_ip;
	}
	public void setSource_ip(String source_ip) {
		this.source_ip = source_ip;
	}
	public Integer getSource_port() {
		return source_port;
	}
	public void setSource_port(Integer source_port) {
		this.source_port = source_port;
	}
	public String getLocal_ip() {
		return local_ip;
	}
	public void setLocal_ip(String local_ip) {
		this.local_ip = local_ip;
	}
	public Igmpv3 getIgmpv3() {
		return igmpv3;
	}
	public void setIgmpv3(Igmpv3 igmpv3) {
		this.igmpv3 = igmpv3;
	}

}

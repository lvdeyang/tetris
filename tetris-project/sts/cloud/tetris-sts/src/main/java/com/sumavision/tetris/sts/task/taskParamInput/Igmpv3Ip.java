package com.sumavision.tetris.sts.task.taskParamInput;

import java.io.Serializable;

public class Igmpv3Ip implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4668887221499811753L;
	private String ip;
	public Igmpv3Ip(String ip){
		this.ip = ip;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	

}

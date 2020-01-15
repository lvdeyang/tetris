package com.sumavision.tetris.sts.task.taskParamInput;

import java.io.Serializable;

public class InputRtpEsBO implements InputCommon, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3886596767426332110L;
	
	private Integer local_port;
	private String type;
	private String codec;
	public Integer getLocal_port() {
		return local_port;
	}
	public void setLocal_port(Integer local_port) {
		this.local_port = local_port;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCodec() {
		return codec;
	}
	public void setCodec(String codec) {
		this.codec = codec;
	}
	
}

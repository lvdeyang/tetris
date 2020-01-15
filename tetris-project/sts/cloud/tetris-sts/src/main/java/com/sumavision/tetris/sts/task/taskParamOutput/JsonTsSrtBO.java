package com.sumavision.tetris.sts.task.taskParamOutput;

import java.io.Serializable;
import java.util.ArrayList;

public class JsonTsSrtBO extends JsonTsCommonBO implements OutputCommon, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6549605781527728105L;
	
	private String mode;
	private Integer latency;
	private Integer connect_timeout;
	private Integer send_timeout;
	private Integer maxbw;
	private Integer send_buffsize;
	private String passphrase;
	private String key_len;
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public Integer getLatency() {
		return latency;
	}
	public void setLatency(Integer latency) {
		this.latency = latency;
	}
	public Integer getConnect_timeout() {
		return connect_timeout;
	}
	public void setConnect_timeout(Integer connect_timeout) {
		this.connect_timeout = connect_timeout;
	}
	public Integer getSend_timeout() {
		return send_timeout;
	}
	public void setSend_timeout(Integer send_timeout) {
		this.send_timeout = send_timeout;
	}
	public Integer getMaxbw() {
		return maxbw;
	}
	public void setMaxbw(Integer maxbw) {
		this.maxbw = maxbw;
	}
	public Integer getSend_buffsize() {
		return send_buffsize;
	}
	public void setSend_buffsize(Integer send_buffsize) {
		this.send_buffsize = send_buffsize;
	}
	public String getPassphrase() {
		return passphrase;
	}
	public void setPassphrase(String passphrase) {
		this.passphrase = passphrase;
	}
	public String getKey_len() {
		return key_len;
	}
	public void setKey_len(String key_len) {
		this.key_len = key_len;
	}
	

}

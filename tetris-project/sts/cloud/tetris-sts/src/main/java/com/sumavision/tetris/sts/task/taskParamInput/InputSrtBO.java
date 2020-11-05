package com.sumavision.tetris.sts.task.taskParamInput;

import java.io.Serializable;

public class InputSrtBO implements InputCommon,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -170674235916407349L;

	private String source_ip;
	private Integer source_port;
	private String mode;
	private Integer latency;
	private Integer connect_timeout;
	private Integer recv_timeout;
	private Integer maxbw;
	private Integer recv_buffsize;
	private String passphrase;
	private String key_len;
	public InputSrtBO(String source_ip,Integer source_port,String mode,Integer latency){
		this.source_ip = source_ip;
		this.source_port = source_port;
		this.mode = mode;
		this.latency = latency;
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
	public Integer getMaxbw() {
		return maxbw;
	}
	public void setMaxbw(Integer maxbw) {
		this.maxbw = maxbw;
	}
	public String getPassphrase() {
		return passphrase;
	}
	public void setPassphrase(String passphrase) {
		this.passphrase = passphrase;
	}
	public Integer getRecv_timeout() {
		return recv_timeout;
	}
	public void setRecv_timeout(Integer recv_timeout) {
		this.recv_timeout = recv_timeout;
	}
	public Integer getRecv_buffsize() {
		return recv_buffsize;
	}
	public void setRecv_buffsize(Integer recv_buffsize) {
		this.recv_buffsize = recv_buffsize;
	}
	public String getKey_len() {
		return key_len;
	}
	public void setKey_len(String key_len) {
		this.key_len = key_len;
	}
	
	
}

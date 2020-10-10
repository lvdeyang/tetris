package com.sumavision.tetris.capacity.bo.output;

import com.sumavision.tetris.business.common.Util.IpV4Util;

/**
 * srt_ts_passby<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年11月4日 上午10:26:54
 */
public class OutputSrtTsPassbyBO extends OutputBaseMediaBO<OutputSrtTsPassbyBO>{

	/** caller/listener/rendezvous */
	private String mode;
	
	private Integer latency;
	
	private Integer connect_timeout;
	
	private Integer send_timeout;
	
	private Integer maxbw;
	
	private Integer send_buffsize;
	
	private String passphrase;
	
	private String key_len;

	private String ip;

	private Integer port;

	private String local_ip;

	private BaseMediaBO media;

	public String getMode() {
		return mode;
	}

	public OutputSrtTsPassbyBO setMode(String mode) {
		this.mode = mode;
		return this;
	}

	public Integer getLatency() {
		return latency;
	}

	public OutputSrtTsPassbyBO setLatency(Integer latency) {
		this.latency = latency;
		return this;
	}

	public Integer getConnect_timeout() {
		return connect_timeout;
	}

	public OutputSrtTsPassbyBO setConnect_timeout(Integer connect_timeout) {
		this.connect_timeout = connect_timeout;
		return this;
	}

	public Integer getSend_timeout() {
		return send_timeout;
	}

	public OutputSrtTsPassbyBO setSend_timeout(Integer send_timeout) {
		this.send_timeout = send_timeout;
		return this;
	}

	public Integer getMaxbw() {
		return maxbw;
	}

	public OutputSrtTsPassbyBO setMaxbw(Integer maxbw) {
		this.maxbw = maxbw;
		return this;
	}

	public Integer getSend_buffsize() {
		return send_buffsize;
	}

	public OutputSrtTsPassbyBO setSend_buffsize(Integer send_buffsize) {
		this.send_buffsize = send_buffsize;
		return this;
	}

	public String getPassphrase() {
		return passphrase;
	}

	public OutputSrtTsPassbyBO setPassphrase(String passphrase) {
		this.passphrase = passphrase;
		return this;
	}

	public String getKey_len() {
		return key_len;
	}

	public OutputSrtTsPassbyBO setKey_len(String key_len) {
		this.key_len = key_len;
		return this;
	}

	public String getIp() {
		return ip;
	}

	public OutputSrtTsPassbyBO setIp(String ip) {
		this.ip = ip;
		return this;
	}

	public Integer getPort() {
		return port;
	}

	public OutputSrtTsPassbyBO setPort(Integer port) {
		this.port = port;
		return this;
	}

	public String getLocal_ip() {
		return local_ip;
	}

	public OutputSrtTsPassbyBO setLocal_ip(String local_ip) {
		this.local_ip = local_ip;
		return this;
	}

	public BaseMediaBO getMedia() {
		return media;
	}

	public OutputSrtTsPassbyBO setMedia(BaseMediaBO media) {
		this.media = media;
		return this;
	}

	public OutputSrtTsPassbyBO(String url, String local_ip) {
		this.ip = IpV4Util.getIpFromUrl(url);
		this.port = IpV4Util.getPortFromUrl(url);
		this.local_ip = local_ip;
	}

}

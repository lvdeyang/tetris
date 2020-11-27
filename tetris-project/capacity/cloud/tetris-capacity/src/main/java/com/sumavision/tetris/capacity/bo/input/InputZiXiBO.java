package com.sumavision.tetris.capacity.bo.input;

import com.sumavision.tetris.application.template.SourceVO;

/**
 * 紫熙参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年4月26日 下午2:09:13
 */
public class InputZiXiBO {

	private Integer mode;
	
	private String host;
	
	private Integer port;
	
	private String url;
	
	private String guid;
	
	private String session;
	
	private Integer max_latency;
	
	private String latency_mode;
	
	private String fec_mode;
	
	private Integer fec_overhead;
	
	private Integer fec_block_ms;
	
	private Integer low_latency;
	
	private Integer ignore_dtls_cert_error;
	
	private String dec_type;
	
	private String dec_key;

	public Integer getMode() {
		return mode;
	}

	public void setMode(Integer mode) {
		this.mode = mode;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public Integer getMax_latency() {
		return max_latency;
	}

	public void setMax_latency(Integer max_latency) {
		this.max_latency = max_latency;
	}

	public String getLatency_mode() {
		return latency_mode;
	}

	public void setLatency_mode(String latency_mode) {
		this.latency_mode = latency_mode;
	}

	public Integer getFec_overhead() {
		return fec_overhead;
	}

	public void setFec_overhead(Integer fec_overhead) {
		this.fec_overhead = fec_overhead;
	}

	public Integer getFec_block_ms() {
		return fec_block_ms;
	}

	public void setFec_block_ms(Integer fec_block_ms) {
		this.fec_block_ms = fec_block_ms;
	}

	public String getFec_mode() {
		return fec_mode;
	}

	public void setFec_mode(String fec_mode) {
		this.fec_mode = fec_mode;
	}

	public Integer getLow_latency() {
		return low_latency;
	}

	public void setLow_latency(Integer low_latency) {
		this.low_latency = low_latency;
	}

	public Integer getIgnore_dtls_cert_error() {
		return ignore_dtls_cert_error;
	}

	public void setIgnore_dtls_cert_error(Integer ignore_dtls_cert_error) {
		this.ignore_dtls_cert_error = ignore_dtls_cert_error;
	}

	public String getDec_type() {
		return dec_type;
	}

	public void setDec_type(String dec_type) {
		this.dec_type = dec_type;
	}

	public String getDec_key() {
		return dec_key;
	}

	public void setDec_key(String dec_key) {
		this.dec_key = dec_key;
	}

	public InputZiXiBO() {
	}

	public InputZiXiBO(SourceVO sourceVO) {
		this.mode = Integer.parseInt(sourceVO.getMode());
		this.host = sourceVO.getHost();
		this.port = sourceVO.getPort();
		this.url = sourceVO.getUrl();
		this.guid = sourceVO.getGuid();
		this.session = sourceVO.getSession();
		this.max_latency = sourceVO.getMax_latency();
		this.latency_mode = sourceVO.getLatency_mode();
		this.fec_mode = sourceVO.getFec_mode();
		this.fec_overhead = sourceVO.getFec_overhead();
		this.fec_block_ms = sourceVO.getFec_block_ms();
		this.low_latency = sourceVO.getLow_latency();
		this.ignore_dtls_cert_error = sourceVO.getIgnore_dtls_cert_error();
		this.dec_key = sourceVO.getDec_key();
		this.dec_type = sourceVO.getDec_type();
	}
}

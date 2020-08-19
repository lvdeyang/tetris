package com.sumavision.tetris.capacity.bo.input;

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
	
	private Integer latency_mode;
	
	private Integer fec_mode;
	
	private Integer fec_overhead;
	
	private Integer fec_block_ms;
	
	private boolean fec_content_aware;
	
	private boolean low_latency;
	
	private boolean ignore_dtls_cert_error;
	
	private Integer decrypt_type;
	
	private String key;

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

	public Integer getLatency_mode() {
		return latency_mode;
	}

	public void setLatency_mode(Integer latency_mode) {
		this.latency_mode = latency_mode;
	}

	public Integer getFec_mode() {
		return fec_mode;
	}

	public void setFec_mode(Integer fec_mode) {
		this.fec_mode = fec_mode;
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

	public boolean isFec_content_aware() {
		return fec_content_aware;
	}

	public void setFec_content_aware(boolean fec_content_aware) {
		this.fec_content_aware = fec_content_aware;
	}

	public boolean isLow_latency() {
		return low_latency;
	}

	public void setLow_latency(boolean low_latency) {
		this.low_latency = low_latency;
	}

	public boolean isIgnore_dtls_cert_error() {
		return ignore_dtls_cert_error;
	}

	public void setIgnore_dtls_cert_error(boolean ignore_dtls_cert_error) {
		this.ignore_dtls_cert_error = ignore_dtls_cert_error;
	}

	public Integer getDecrypt_type() {
		return decrypt_type;
	}

	public void setDecrypt_type(Integer decrypt_type) {
		this.decrypt_type = decrypt_type;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
}

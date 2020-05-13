package com.sumavision.tetris.capacity.bo.output;

/**
 * 紫熙参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年4月26日 下午2:26:40
 */
public class OutputZiXiBO {

	private String stream_id;
	
	private String host;
	
	private Integer port;
	
	private Integer max_latency_ms;
	
	private Integer max_bitrate;
	
	private boolean rtp;
	
	private Integer limited;
	
	private boolean stop_on_drop;
	
	private boolean reconnect;
	
	private Integer fec_overhead;
	
	private Integer fec_block_ms;
	
	private boolean content_aware_fec;
	
	private Integer enc_type;
	
	private Integer enc_key;
	
	private boolean use_compression;
	
	private boolean fast_connect;
	
	private Integer smoothing_latency;
	
	private Integer timeout;
	
	private String local_ip;
	
	private Integer enforce_bitrate;
	
	private Integer force_padding;
	
	private Integer mmt;
	
	private Integer elementary_streams;
	
	private Integer elementary_streams_max_va_diff_ms;
	
	private boolean expect_high_jitter;
	
	private boolean ignore_dtls_cert_error;
	
	private boolean replaceable;
	
	private Integer protocol;

	public String getStream_id() {
		return stream_id;
	}

	public void setStream_id(String stream_id) {
		this.stream_id = stream_id;
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

	public Integer getMax_latency_ms() {
		return max_latency_ms;
	}

	public void setMax_latency_ms(Integer max_latency_ms) {
		this.max_latency_ms = max_latency_ms;
	}

	public Integer getMax_bitrate() {
		return max_bitrate;
	}

	public void setMax_bitrate(Integer max_bitrate) {
		this.max_bitrate = max_bitrate;
	}

	public boolean isRtp() {
		return rtp;
	}

	public void setRtp(boolean rtp) {
		this.rtp = rtp;
	}

	public Integer getLimited() {
		return limited;
	}

	public void setLimited(Integer limited) {
		this.limited = limited;
	}

	public boolean isStop_on_drop() {
		return stop_on_drop;
	}

	public void setStop_on_drop(boolean stop_on_drop) {
		this.stop_on_drop = stop_on_drop;
	}

	public boolean isReconnect() {
		return reconnect;
	}

	public void setReconnect(boolean reconnect) {
		this.reconnect = reconnect;
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

	public boolean isContent_aware_fec() {
		return content_aware_fec;
	}

	public void setContent_aware_fec(boolean content_aware_fec) {
		this.content_aware_fec = content_aware_fec;
	}

	public Integer getEnc_type() {
		return enc_type;
	}

	public void setEnc_type(Integer enc_type) {
		this.enc_type = enc_type;
	}

	public Integer getEnc_key() {
		return enc_key;
	}

	public void setEnc_key(Integer enc_key) {
		this.enc_key = enc_key;
	}

	public boolean isUse_compression() {
		return use_compression;
	}

	public void setUse_compression(boolean use_compression) {
		this.use_compression = use_compression;
	}

	public boolean isFast_connect() {
		return fast_connect;
	}

	public void setFast_connect(boolean fast_connect) {
		this.fast_connect = fast_connect;
	}

	public Integer getSmoothing_latency() {
		return smoothing_latency;
	}

	public void setSmoothing_latency(Integer smoothing_latency) {
		this.smoothing_latency = smoothing_latency;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	public String getLocal_ip() {
		return local_ip;
	}

	public void setLocal_ip(String local_ip) {
		this.local_ip = local_ip;
	}

	public Integer getEnforce_bitrate() {
		return enforce_bitrate;
	}

	public void setEnforce_bitrate(Integer enforce_bitrate) {
		this.enforce_bitrate = enforce_bitrate;
	}

	public Integer getForce_padding() {
		return force_padding;
	}

	public void setForce_padding(Integer force_padding) {
		this.force_padding = force_padding;
	}

	public Integer getMmt() {
		return mmt;
	}

	public void setMmt(Integer mmt) {
		this.mmt = mmt;
	}

	public Integer getElementary_streams() {
		return elementary_streams;
	}

	public void setElementary_streams(Integer elementary_streams) {
		this.elementary_streams = elementary_streams;
	}

	public Integer getElementary_streams_max_va_diff_ms() {
		return elementary_streams_max_va_diff_ms;
	}

	public void setElementary_streams_max_va_diff_ms(Integer elementary_streams_max_va_diff_ms) {
		this.elementary_streams_max_va_diff_ms = elementary_streams_max_va_diff_ms;
	}

	public boolean isExpect_high_jitter() {
		return expect_high_jitter;
	}

	public void setExpect_high_jitter(boolean expect_high_jitter) {
		this.expect_high_jitter = expect_high_jitter;
	}

	public boolean isIgnore_dtls_cert_error() {
		return ignore_dtls_cert_error;
	}

	public void setIgnore_dtls_cert_error(boolean ignore_dtls_cert_error) {
		this.ignore_dtls_cert_error = ignore_dtls_cert_error;
	}

	public boolean isReplaceable() {
		return replaceable;
	}

	public void setReplaceable(boolean replaceable) {
		this.replaceable = replaceable;
	}

	public Integer getProtocol() {
		return protocol;
	}

	public void setProtocol(Integer protocol) {
		this.protocol = protocol;
	}
	
}

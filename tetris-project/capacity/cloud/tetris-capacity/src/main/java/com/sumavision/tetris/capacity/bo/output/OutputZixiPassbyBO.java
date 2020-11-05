package com.sumavision.tetris.capacity.bo.output;

import com.sumavision.tetris.business.common.Util.IpV4Util;

/**
 * srt_ts_passby<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年11月4日 上午10:26:54
 */
public class OutputZixiPassbyBO extends OutputBaseMediaBO<OutputZixiPassbyBO>{

	/** caller/listener/rendezvous */
	private String stream_id;
	private String host;
	private Integer port;
	private Integer max_latency_ms;
	private Integer max_bitrate;
	private Boolean rtp;
	private Integer limited;
	private Boolean stop_on_drop;
	private Boolean reconnect;
	private Integer fec_overhead;
	private Integer fec_block_ms;
	private Boolean content_aware_fec;
	private Integer enc_type;
	private String enc_key;
	private Boolean use_compression;
	private Boolean fast_connect;
	private Integer smoothing_latency;
	private Integer timeout;
	private String local_ip;
	private Integer enforce_bitrate;
	private Integer force_padding;
	private Integer mmt;
	private Integer elementary_streams;
	private Integer elementary_streams_max_va_diff_ms;
	private Boolean expect_high_jitter;
	private Boolean ignore_dtls_cert_error;
	private Boolean replaceable;
	private Integer protocol;
	private BaseMediaBO media;

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

	public Boolean getRtp() {
		return rtp;
	}

	public void setRtp(Boolean rtp) {
		this.rtp = rtp;
	}

	public Integer getLimited() {
		return limited;
	}

	public void setLimited(Integer limited) {
		this.limited = limited;
	}

	public Boolean getStop_on_drop() {
		return stop_on_drop;
	}

	public void setStop_on_drop(Boolean stop_on_drop) {
		this.stop_on_drop = stop_on_drop;
	}

	public Boolean getReconnect() {
		return reconnect;
	}

	public void setReconnect(Boolean reconnect) {
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

	public Boolean getContent_aware_fec() {
		return content_aware_fec;
	}

	public void setContent_aware_fec(Boolean content_aware_fec) {
		this.content_aware_fec = content_aware_fec;
	}

	public Integer getEnc_type() {
		return enc_type;
	}

	public void setEnc_type(Integer enc_type) {
		this.enc_type = enc_type;
	}

	public String getEnc_key() {
		return enc_key;
	}

	public void setEnc_key(String enc_key) {
		this.enc_key = enc_key;
	}

	public Boolean getUse_compression() {
		return use_compression;
	}

	public void setUse_compression(Boolean use_compression) {
		this.use_compression = use_compression;
	}

	public Boolean getFast_connect() {
		return fast_connect;
	}

	public void setFast_connect(Boolean fast_connect) {
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

	public Boolean getExpect_high_jitter() {
		return expect_high_jitter;
	}

	public void setExpect_high_jitter(Boolean expect_high_jitter) {
		this.expect_high_jitter = expect_high_jitter;
	}

	public Boolean getIgnore_dtls_cert_error() {
		return ignore_dtls_cert_error;
	}

	public void setIgnore_dtls_cert_error(Boolean ignore_dtls_cert_error) {
		this.ignore_dtls_cert_error = ignore_dtls_cert_error;
	}

	public Boolean getReplaceable() {
		return replaceable;
	}

	public void setReplaceable(Boolean replaceable) {
		this.replaceable = replaceable;
	}

	public Integer getProtocol() {
		return protocol;
	}

	public void setProtocol(Integer protocol) {
		this.protocol = protocol;
	}

	public BaseMediaBO getMedia() {
		return media;
	}

	public void setMedia(BaseMediaBO media) {
		this.media = media;
	}
}

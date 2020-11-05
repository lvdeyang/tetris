package com.sumavision.signal.bvc.capacity.bo.output;

import java.util.List;

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
	
	private Integer pcr_int;
	
	private Integer pat_int;
	
	private Integer pmt_int;
	
	private Integer sdt_int;
	
	private Integer tsid_pid;
	
	private String rate_ctrl;
	
	private Integer bitrate;
	
	private String av_mode;
	
	private Integer buf_init;
	
	private String ts_mode;
	
	private Integer interlace_depth;
	
	private String ajust_mode;
	
	private Integer ts_send_cnt;
	
	private Integer target_cnt;
	
	private Integer pad_pid;
	
	private Integer pat_version;
	
	private Integer cut_allow;
	
	private String const_output;
	
	private String send_control;
	
	private Integer send_gap_min;
	
	private List<OutputZiXiMediaBO> media_array;
	
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

	public Integer getPcr_int() {
		return pcr_int;
	}

	public void setPcr_int(Integer pcr_int) {
		this.pcr_int = pcr_int;
	}

	public Integer getPat_int() {
		return pat_int;
	}

	public void setPat_int(Integer pat_int) {
		this.pat_int = pat_int;
	}

	public Integer getPmt_int() {
		return pmt_int;
	}

	public void setPmt_int(Integer pmt_int) {
		this.pmt_int = pmt_int;
	}

	public Integer getSdt_int() {
		return sdt_int;
	}

	public void setSdt_int(Integer sdt_int) {
		this.sdt_int = sdt_int;
	}

	public Integer getTsid_pid() {
		return tsid_pid;
	}

	public void setTsid_pid(Integer tsid_pid) {
		this.tsid_pid = tsid_pid;
	}

	public String getRate_ctrl() {
		return rate_ctrl;
	}

	public void setRate_ctrl(String rate_ctrl) {
		this.rate_ctrl = rate_ctrl;
	}

	public Integer getBitrate() {
		return bitrate;
	}

	public void setBitrate(Integer bitrate) {
		this.bitrate = bitrate;
	}

	public String getAv_mode() {
		return av_mode;
	}

	public void setAv_mode(String av_mode) {
		this.av_mode = av_mode;
	}

	public Integer getBuf_init() {
		return buf_init;
	}

	public void setBuf_init(Integer buf_init) {
		this.buf_init = buf_init;
	}

	public String getTs_mode() {
		return ts_mode;
	}

	public void setTs_mode(String ts_mode) {
		this.ts_mode = ts_mode;
	}

	public Integer getInterlace_depth() {
		return interlace_depth;
	}

	public void setInterlace_depth(Integer interlace_depth) {
		this.interlace_depth = interlace_depth;
	}

	public String getAjust_mode() {
		return ajust_mode;
	}

	public void setAjust_mode(String ajust_mode) {
		this.ajust_mode = ajust_mode;
	}

	public Integer getTs_send_cnt() {
		return ts_send_cnt;
	}

	public void setTs_send_cnt(Integer ts_send_cnt) {
		this.ts_send_cnt = ts_send_cnt;
	}

	public Integer getTarget_cnt() {
		return target_cnt;
	}

	public void setTarget_cnt(Integer target_cnt) {
		this.target_cnt = target_cnt;
	}

	public Integer getPad_pid() {
		return pad_pid;
	}

	public void setPad_pid(Integer pad_pid) {
		this.pad_pid = pad_pid;
	}

	public Integer getPat_version() {
		return pat_version;
	}

	public void setPat_version(Integer pat_version) {
		this.pat_version = pat_version;
	}

	public Integer getCut_allow() {
		return cut_allow;
	}

	public void setCut_allow(Integer cut_allow) {
		this.cut_allow = cut_allow;
	}

	public String getConst_output() {
		return const_output;
	}

	public void setConst_output(String const_output) {
		this.const_output = const_output;
	}

	public String getSend_control() {
		return send_control;
	}

	public void setSend_control(String send_control) {
		this.send_control = send_control;
	}

	public Integer getSend_gap_min() {
		return send_gap_min;
	}

	public void setSend_gap_min(Integer send_gap_min) {
		this.send_gap_min = send_gap_min;
	}

	public List<OutputZiXiMediaBO> getMedia_array() {
		return media_array;
	}

	public void setMedia_array(List<OutputZiXiMediaBO> media_array) {
		this.media_array = media_array;
	}
	
}

package com.sumavision.tetris.application.template;/**
 * Created by Poemafar on 2020/10/27 14:35
 */

import com.alibaba.fastjson.JSONArray;
import com.sumavision.tetris.business.common.enumeration.ProtocolType;

import java.security.PrivateKey;

/**
 * @ClassName: SourceVO
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/10/27 14:35
 */
public class SourceVO {

    private Integer index;

    private String type;

    /**
     * 文件数组，结构体里有 url,loop_count,start_ms,duration见转换协议文档,,,约定
     */
    private String file_array;

    private String url;

    private String local_ip;

    private String mediaType;

    private String mode;

    //-------zixi----
    private String host;

    private Integer port;

    private String guid;

    private String session;

    private Integer max_latency;

    private String latency_mode;

    private String fec_mode;

    private Integer fec_overhead;

    private Integer fec_block_ms;

    private Integer content_aware_fc;

    private Integer low_latency;

    private Integer ignore_dtls_cert_error;

    private String dec_type;

    private String dec_key;

    //----------udppcm-----
    private Integer sample_rate;

    private String sample_fmt;

    private String channel_layout;

    //----------schedule-----
    private String stream_type;

    private Integer prev;

    private Integer next;

    //------input program结构----
    private JSONArray program_array;
    private String output_program;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLocal_ip() {
        return local_ip;
    }

    public void setLocal_ip(String local_ip) {
        this.local_ip = local_ip;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getFile_array() {
        return file_array;
    }

    public void setFile_array(String file_array) {
        this.file_array = file_array;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
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

    public String getFec_mode() {
        return fec_mode;
    }

    public void setFec_mode(String fec_mode) {
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

    public Integer getContent_aware_fc() {
        return content_aware_fc;
    }

    public void setContent_aware_fc(Integer content_aware_fc) {
        this.content_aware_fc = content_aware_fc;
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

    public Integer getSample_rate() {
        return sample_rate;
    }

    public void setSample_rate(Integer sample_rate) {
        this.sample_rate = sample_rate;
    }

    public String getSample_fmt() {
        return sample_fmt;
    }

    public void setSample_fmt(String sample_fmt) {
        this.sample_fmt = sample_fmt;
    }

    public String getChannel_layout() {
        return channel_layout;
    }

    public void setChannel_layout(String channel_layout) {
        this.channel_layout = channel_layout;
    }

    public Integer getPrev() {
        return prev;
    }

    public void setPrev(Integer prev) {
        this.prev = prev;
    }

    public Integer getNext() {
        return next;
    }

    public void setNext(Integer next) {
        this.next = next;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public JSONArray getProgram_array() {
        return program_array;
    }

    public void setProgram_array(JSONArray program_array) {
        this.program_array = program_array;
    }

    public String getOutput_program() {
        return output_program;
    }

    public void setOutput_program(String output_program) {
        this.output_program = output_program;
    }

    public String getStream_type() {
        return stream_type;
    }

    public void setStream_type(String stream_type) {
        this.stream_type = stream_type;
    }
}

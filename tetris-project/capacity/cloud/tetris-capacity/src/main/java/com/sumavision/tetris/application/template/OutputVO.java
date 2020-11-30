package com.sumavision.tetris.application.template;/**
 * Created by Poemafar on 2020/10/27 14:35
 */

import com.sumavision.tetris.business.common.enumeration.ProtocolType;

import java.util.List;

/**
 * @ClassName: OutputVO
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/10/27 14:35
 */
public class OutputVO {


    private String url;

    private String local_ip;

    private String type;

    //---------------通用参数-------------------
    private List<ProgramVO> programs;

    private List<MediaVO> medias;

    //--------------------------
    private Integer tsid_pid;

    private Integer bitrate;

    private String  av_mode;

    private String  rate_ctrl;

    private Integer  pmt_int;

    private Integer  pcr_int;

    private Integer  sdt_int;

    private Integer  buf_init;

    private Integer  pat_int;

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

    //-------------------HTTP_TS--------------------------
    /**
     * 发布名称；hlsrecord就是目录名
     */
    private String name;

    //-------------------SRT_TS-----------------------------
    private String mode;

    private Integer latency;

    private Integer connect_timeout;

    private Integer send_timeout;

    private Integer max_bitrate;

    private String passphrase;

    private String key_len;

    //-------------------HLS------------------------------
    private String playlist_name;

    private Integer playlist_seg_count;

    private Integer total_seg_count;

    private Integer max_seg_duration;

    private Boolean i_frames_only;


    //-------dash--------
    private String dir_name;

    private Boolean can_del;

    //-------rtsp----------
    private String av_sync;


    //------------rtp_es---------
    private Integer mtu;

    private Integer time_scale;

    //-------------http_flv和rtmp----------------
    private String pub_user;

    private String pub_password;

    private Boolean vid_exist;

    private Boolean aud_exist;


    //-------------hls_record-----------------
    private Integer cycle_time_seconds;

    private Integer split_folder_seconds;

    //-----------------zixi_ts--------------------
    private String stream_id;

    private Integer max_latency_ms;

    private Integer rtp;

    private String limited;

    private Integer stop_on_drop;

    private Integer reconnect;

    private Integer fec_overhead;

    private Integer fec_block_ms;

    private Integer content_aware_fec;

    private String enc_type;

    private Integer use_compression;

    private Integer fast_connect;

    private Integer smoothing_latency;

    private Integer timeout;

    private Integer force_bonding;

    private Integer auto_nics_update_interval;

    private Integer enforce_bitrate;

    private Integer force_padding;

    private Integer expect_high_jitter;

    private Integer ignore_dtls_cert_error;

    private Integer replaceable;

    private String protocol;

    private String pcr_clock;

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

    public Integer getTsid_pid() {
        return tsid_pid;
    }

    public void setTsid_pid(Integer tsid_pid) {
        this.tsid_pid = tsid_pid;
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

    public String getRate_ctrl() {
        return rate_ctrl;
    }

    public void setRate_ctrl(String rate_ctrl) {
        this.rate_ctrl = rate_ctrl;
    }

    public Integer getPmt_int() {
        return pmt_int;
    }

    public void setPmt_int(Integer pmt_int) {
        this.pmt_int = pmt_int;
    }

    public Integer getPcr_int() {
        return pcr_int;
    }

    public void setPcr_int(Integer pcr_int) {
        this.pcr_int = pcr_int;
    }

    public Integer getSdt_int() {
        return sdt_int;
    }

    public void setSdt_int(Integer sdt_int) {
        this.sdt_int = sdt_int;
    }

    public Integer getBuf_init() {
        return buf_init;
    }

    public void setBuf_init(Integer buf_init) {
        this.buf_init = buf_init;
    }

    public Integer getPat_int() {
        return pat_int;
    }

    public void setPat_int(Integer pat_int) {
        this.pat_int = pat_int;
    }

    public List<ProgramVO> getPrograms() {
        return programs;
    }

    public void setPrograms(List<ProgramVO> programs) {
        this.programs = programs;
    }

    public List<MediaVO> getMedias() {
        return medias;
    }

    public void setMedias(List<MediaVO> medias) {
        this.medias = medias;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getSend_timeout() {
        return send_timeout;
    }

    public void setSend_timeout(Integer send_timeout) {
        this.send_timeout = send_timeout;
    }

    public Integer getMax_bitrate() {
        return max_bitrate;
    }

    public void setMax_bitrate(Integer max_bitrate) {
        this.max_bitrate = max_bitrate;
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

    public String getPlaylist_name() {
        return playlist_name;
    }

    public void setPlaylist_name(String playlist_name) {
        this.playlist_name = playlist_name;
    }

    public Integer getPlaylist_seg_count() {
        return playlist_seg_count;
    }

    public void setPlaylist_seg_count(Integer playlist_seg_count) {
        this.playlist_seg_count = playlist_seg_count;
    }

    public Integer getTotal_seg_count() {
        return total_seg_count;
    }

    public void setTotal_seg_count(Integer total_seg_count) {
        this.total_seg_count = total_seg_count;
    }

    public Integer getMax_seg_duration() {
        return max_seg_duration;
    }

    public void setMax_seg_duration(Integer max_seg_duration) {
        this.max_seg_duration = max_seg_duration;
    }

    public Boolean getI_frames_only() {
        return i_frames_only;
    }

    public void setI_frames_only(Boolean i_frames_only) {
        this.i_frames_only = i_frames_only;
    }

    public Integer getCycle_time_seconds() {
        return cycle_time_seconds;
    }

    public void setCycle_time_seconds(Integer cycle_time_seconds) {
        this.cycle_time_seconds = cycle_time_seconds;
    }

    public Integer getSplit_folder_seconds() {
        return split_folder_seconds;
    }

    public void setSplit_folder_seconds(Integer split_folder_seconds) {
        this.split_folder_seconds = split_folder_seconds;
    }

    public String getPub_user() {
        return pub_user;
    }

    public void setPub_user(String pub_user) {
        this.pub_user = pub_user;
    }

    public String getPub_password() {
        return pub_password;
    }

    public void setPub_password(String pub_password) {
        this.pub_password = pub_password;
    }

    public Boolean getVid_exist() {
        return vid_exist;
    }

    public void setVid_exist(Boolean vid_exist) {
        this.vid_exist = vid_exist;
    }

    public Boolean getAud_exist() {
        return aud_exist;
    }

    public void setAud_exist(Boolean aud_exist) {
        this.aud_exist = aud_exist;
    }

    public Integer getMtu() {
        return mtu;
    }

    public void setMtu(Integer mtu) {
        this.mtu = mtu;
    }

    public Integer getTime_scale() {
        return time_scale;
    }

    public void setTime_scale(Integer time_scale) {
        this.time_scale = time_scale;
    }

    public String getAv_sync() {
        return av_sync;
    }

    public void setAv_sync(String av_sync) {
        this.av_sync = av_sync;
    }

    public String getDir_name() {
        return dir_name;
    }

    public void setDir_name(String dir_name) {
        this.dir_name = dir_name;
    }

    public Boolean getCan_del() {
        return can_del;
    }

    public void setCan_del(Boolean can_del) {
        this.can_del = can_del;
    }


    public String getStream_id() {
        return stream_id;
    }

    public void setStream_id(String stream_id) {
        this.stream_id = stream_id;
    }

    public Integer getMax_latency_ms() {
        return max_latency_ms;
    }

    public void setMax_latency_ms(Integer max_latency_ms) {
        this.max_latency_ms = max_latency_ms;
    }

    public Integer getRtp() {
        return rtp;
    }

    public void setRtp(Integer rtp) {
        this.rtp = rtp;
    }

    public String getLimited() {
        return limited;
    }

    public void setLimited(String limited) {
        this.limited = limited;
    }

    public Integer getStop_on_drop() {
        return stop_on_drop;
    }

    public void setStop_on_drop(Integer stop_on_drop) {
        this.stop_on_drop = stop_on_drop;
    }

    public Integer getReconnect() {
        return reconnect;
    }

    public void setReconnect(Integer reconnect) {
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

    public Integer getContent_aware_fec() {
        return content_aware_fec;
    }

    public void setContent_aware_fec(Integer content_aware_fec) {
        this.content_aware_fec = content_aware_fec;
    }

    public String getEnc_type() {
        return enc_type;
    }

    public void setEnc_type(String enc_type) {
        this.enc_type = enc_type;
    }

    public Integer getUse_compression() {
        return use_compression;
    }

    public void setUse_compression(Integer use_compression) {
        this.use_compression = use_compression;
    }

    public Integer getFast_connect() {
        return fast_connect;
    }

    public void setFast_connect(Integer fast_connect) {
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

    public Integer getForce_bonding() {
        return force_bonding;
    }

    public void setForce_bonding(Integer force_bonding) {
        this.force_bonding = force_bonding;
    }

    public Integer getAuto_nics_update_interval() {
        return auto_nics_update_interval;
    }

    public void setAuto_nics_update_interval(Integer auto_nics_update_interval) {
        this.auto_nics_update_interval = auto_nics_update_interval;
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

    public Integer getExpect_high_jitter() {
        return expect_high_jitter;
    }

    public void setExpect_high_jitter(Integer expect_high_jitter) {
        this.expect_high_jitter = expect_high_jitter;
    }

    public Integer getIgnore_dtls_cert_error() {
        return ignore_dtls_cert_error;
    }

    public void setIgnore_dtls_cert_error(Integer ignore_dtls_cert_error) {
        this.ignore_dtls_cert_error = ignore_dtls_cert_error;
    }

    public Integer getReplaceable() {
        return replaceable;
    }

    public void setReplaceable(Integer replaceable) {
        this.replaceable = replaceable;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getPcr_clock() {
        return pcr_clock;
    }

    public void setPcr_clock(String pcr_clock) {
        this.pcr_clock = pcr_clock;
    }
}

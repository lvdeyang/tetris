package com.sumavision.signal.bvc.fifthg.bo.http;/**
 * Created by Poemafar on 2020/8/31 8:37
 */

import java.util.List;

/**
 * @ClassName: SuiBusParam
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/8/31 8:37
 */
public class SuiBusParam {

    /**
     * 视频开关
     * 0关 1开
     */
    private Integer onoff;
    /**
     * 0：彩条；Color Bar
     * 1：4x3G SDI；（默认）
     * 2：12G SDI；
     * 3：UHD；
     * 4：IP信源；IP Source；
     */
    private Long vidCapSel;

    /**
     * 编码分辨率及帧率
     * 1：3840x2160P59.94
     * 2：3840x2160P60
     * 3：3840x2160P50
     * 4：3840x2160P29.97
     * 5：3840x2160P30
     * 6：3840x2160P25（默认）
     * 7：3840x2160P23.98
     * 8：3840x2160P24
     * 16：1920x1080P59.94
     * 17：1920x1080P60
     * 18：1920x1080P50
     * 19：1920x1080P29.97
     * 20：1920x1080P30
     * 21：1920x1080P23.98
     * 22：1920x1080P24
     * 23：1920x1080P25
     * 32：1920x1080I59.94
     * 33：1920x1080I60
     * 34：1920x1080I50
     * 35：1920x1080I47.96
     * 36：1920x1080I48
     * 48：1280x720P59.94
     * 49：1280x720P60
     * 50：1280x720P50
     * 51：1280x720P29.97
     * 52：1280x720P30
     * 53：1280x720P25
     * 54：1280x720P23.98
     * 55：1280x720P24
     * 64：720x576I50
     * 65：720x480I59.94
     */
    private Long vidEncRes = 6L;

    /**
     * 0：交织；2-sample Interleave division（默认）
     * 1：分割；Square Division
     */
    private Integer vidInputFmt = 0;

    /**
     * 视频编码格式
     * 0：H.265（默认）
     * 1：H.264
     */
    private Long vidEncStd;

    /**
     * 0：8位；8-bit（默认）
     * 1：10位；10-bit
     */
    private Integer vidBitDepth;

    /**
     * 编码码率
     */
    private Long vidEncBR;

    /**
     * 系统码率
     */
    private Long vidSysBR;

    /**
     * HDR模式
     */
    private Integer vidHdrMode;

    /**
     * 音频编码参数
     */
    private List<AudioEncParam> audEncPara;
    /**
     * 节目名
     */
    private String servName;
    /**
     * 节目提供商
     */
    private String servProv;
    /**
     * 传输协议
     * 0:UDP 1:RTP 2:SRT 3:RTMP 4:RIST
     */
    private Integer protocol;

    /**
     * 0关闭，1打开
     */
    private Integer udp_send_enable;
    /**
     * 网口选择
     * 0：CTRL
     * 1-2：SFP1-SFP2
     * 3：GBE
     * 4-6:5G1-5G3
     * 7-10：USB1-USB4
     */
    private Long udp_net_select = 0L;

    private String udp_send_ip;

    private Long udp_send_port;

    /**
     * srt开关
     */
    private Integer srt_send_enable;

    private Long srt_net_select;

    private String srt_send_ip;
    private Integer srt_send_port;
    /**
     * 加密
     */
    private Integer srt_encrypt;
    /**
     * 密码
     */
    private String srt_passwd;
    private Integer srt_pbkey_len;
    private Integer srt_recv_latency;
    private Integer srt_max_bandwidth;
    private Integer srt_bw_overhead;
    private Integer srt_timeout;
    private Integer srt_clear;

    /**
     * rist 开关
     */
    private Integer rist_send_enable;
    private Long rist_net_select0;
    private Integer rist_multi_send_onoff0;
    private Integer rist_multi_send_weight0;
    private String rist_multi_send_addr0;
    private Integer rist_multi_send_port0;
    private Long rist_net_select1;
    private Integer rist_multi_send_onoff1;
    private Integer rist_multi_send_weight1;
    private String rist_multi_send_addr1;
    private Integer rist_multi_send_port1;
    private Integer rist_backup_enable;

    /**
     * rtmp 开关
     */
    private Integer rtmp_enable;
    private String rtmp_appName;
    private String rtmp_streamName;
    private String rtmp_serverIp;
    private Integer rtmp_serverPort;
    private Long rtmp_net_select;


    public Integer getOnoff() {
        return onoff;
    }

    public void setOnoff(Integer onoff) {
        this.onoff = onoff;
    }

    public Long getVidCapSel() {
        return vidCapSel;
    }

    public void setVidCapSel(Long vidCapSel) {
        this.vidCapSel = vidCapSel;
    }

    public Long getVidEncRes() {
        return vidEncRes;
    }

    public void setVidEncRes(Long vidEncRes) {
        this.vidEncRes = vidEncRes;
    }

    public Integer getVidInputFmt() {
        return vidInputFmt;
    }

    public void setVidInputFmt(Integer vidInputFmt) {
        this.vidInputFmt = vidInputFmt;
    }

    public Long getVidEncStd() {
        return vidEncStd;
    }

    public void setVidEncStd(Long vidEncStd) {
        this.vidEncStd = vidEncStd;
    }

    public Integer getVidBitDepth() {
        return vidBitDepth;
    }

    public void setVidBitDepth(Integer vidBitDepth) {
        this.vidBitDepth = vidBitDepth;
    }

    public Long getVidEncBR() {
        return vidEncBR;
    }

    public void setVidEncBR(Long vidEncBR) {
        this.vidEncBR = vidEncBR;
    }

    public Long getVidSysBR() {
        return vidSysBR;
    }

    public void setVidSysBR(Long vidSysBR) {
        this.vidSysBR = vidSysBR;
    }

    public Integer getVidHdrMode() {
        return vidHdrMode;
    }

    public void setVidHdrMode(Integer vidHdrMode) {
        this.vidHdrMode = vidHdrMode;
    }

    public List<AudioEncParam> getAudEncPara() {
        return audEncPara;
    }

    public void setAudEncPara(List<AudioEncParam> audEncPara) {
        this.audEncPara = audEncPara;
    }

    public String getServName() {
        return servName;
    }

    public void setServName(String servName) {
        this.servName = servName;
    }

    public String getServProv() {
        return servProv;
    }

    public void setServProv(String servProv) {
        this.servProv = servProv;
    }

    public Integer getProtocol() {
        return protocol;
    }

    public void setProtocol(Integer protocol) {
        this.protocol = protocol;
    }

    public Integer getUdp_send_enable() {
        return udp_send_enable;
    }

    public void setUdp_send_enable(Integer udp_send_enable) {
        this.udp_send_enable = udp_send_enable;
    }


    public String getUdp_send_ip() {
        return udp_send_ip;
    }

    public void setUdp_send_ip(String udp_send_ip) {
        this.udp_send_ip = udp_send_ip;
    }

    public Long getUdp_send_port() {
        return udp_send_port;
    }

    public void setUdp_send_port(Long udp_send_port) {
        this.udp_send_port = udp_send_port;
    }

    public Integer getSrt_send_enable() {
        return srt_send_enable;
    }

    public void setSrt_send_enable(Integer srt_send_enable) {
        this.srt_send_enable = srt_send_enable;
    }


    public String getSrt_send_ip() {
        return srt_send_ip;
    }

    public void setSrt_send_ip(String srt_send_ip) {
        this.srt_send_ip = srt_send_ip;
    }

    public Integer getSrt_send_port() {
        return srt_send_port;
    }

    public void setSrt_send_port(Integer srt_send_port) {
        this.srt_send_port = srt_send_port;
    }

    public Integer getSrt_encrypt() {
        return srt_encrypt;
    }

    public void setSrt_encrypt(Integer srt_encrypt) {
        this.srt_encrypt = srt_encrypt;
    }

    public String getSrt_passwd() {
        return srt_passwd;
    }

    public void setSrt_passwd(String srt_passwd) {
        this.srt_passwd = srt_passwd;
    }

    public Integer getSrt_pbkey_len() {
        return srt_pbkey_len;
    }

    public void setSrt_pbkey_len(Integer srt_pbkey_len) {
        this.srt_pbkey_len = srt_pbkey_len;
    }

    public Integer getSrt_recv_latency() {
        return srt_recv_latency;
    }

    public void setSrt_recv_latency(Integer srt_recv_latency) {
        this.srt_recv_latency = srt_recv_latency;
    }

    public Integer getSrt_max_bandwidth() {
        return srt_max_bandwidth;
    }

    public void setSrt_max_bandwidth(Integer srt_max_bandwidth) {
        this.srt_max_bandwidth = srt_max_bandwidth;
    }

    public Integer getSrt_bw_overhead() {
        return srt_bw_overhead;
    }

    public void setSrt_bw_overhead(Integer srt_bw_overhead) {
        this.srt_bw_overhead = srt_bw_overhead;
    }

    public Integer getSrt_timeout() {
        return srt_timeout;
    }

    public void setSrt_timeout(Integer srt_timeout) {
        this.srt_timeout = srt_timeout;
    }

    public Integer getSrt_clear() {
        return srt_clear;
    }

    public void setSrt_clear(Integer srt_clear) {
        this.srt_clear = srt_clear;
    }

    public Integer getRist_send_enable() {
        return rist_send_enable;
    }

    public void setRist_send_enable(Integer rist_send_enable) {
        this.rist_send_enable = rist_send_enable;
    }


    public Integer getRist_multi_send_onoff0() {
        return rist_multi_send_onoff0;
    }

    public void setRist_multi_send_onoff0(Integer rist_multi_send_onoff0) {
        this.rist_multi_send_onoff0 = rist_multi_send_onoff0;
    }

    public Integer getRist_multi_send_weight0() {
        return rist_multi_send_weight0;
    }

    public void setRist_multi_send_weight0(Integer rist_multi_send_weight0) {
        this.rist_multi_send_weight0 = rist_multi_send_weight0;
    }

    public String getRist_multi_send_addr0() {
        return rist_multi_send_addr0;
    }

    public void setRist_multi_send_addr0(String rist_multi_send_addr0) {
        this.rist_multi_send_addr0 = rist_multi_send_addr0;
    }

    public Integer getRist_multi_send_port0() {
        return rist_multi_send_port0;
    }

    public void setRist_multi_send_port0(Integer rist_multi_send_port0) {
        this.rist_multi_send_port0 = rist_multi_send_port0;
    }


    public Integer getRist_multi_send_onoff1() {
        return rist_multi_send_onoff1;
    }

    public void setRist_multi_send_onoff1(Integer rist_multi_send_onoff1) {
        this.rist_multi_send_onoff1 = rist_multi_send_onoff1;
    }

    public Integer getRist_multi_send_weight1() {
        return rist_multi_send_weight1;
    }

    public void setRist_multi_send_weight1(Integer rist_multi_send_weight1) {
        this.rist_multi_send_weight1 = rist_multi_send_weight1;
    }

    public String getRist_multi_send_addr1() {
        return rist_multi_send_addr1;
    }

    public void setRist_multi_send_addr1(String rist_multi_send_addr1) {
        this.rist_multi_send_addr1 = rist_multi_send_addr1;
    }

    public Integer getRist_multi_send_port1() {
        return rist_multi_send_port1;
    }

    public void setRist_multi_send_port1(Integer rist_multi_send_port1) {
        this.rist_multi_send_port1 = rist_multi_send_port1;
    }

    public Integer getRist_backup_enable() {
        return rist_backup_enable;
    }

    public void setRist_backup_enable(Integer rist_backup_enable) {
        this.rist_backup_enable = rist_backup_enable;
    }

    public Integer getRtmp_enable() {
        return rtmp_enable;
    }

    public void setRtmp_enable(Integer rtmp_enable) {
        this.rtmp_enable = rtmp_enable;
    }

    public String getRtmp_appName() {
        return rtmp_appName;
    }

    public void setRtmp_appName(String rtmp_appName) {
        this.rtmp_appName = rtmp_appName;
    }

    public String getRtmp_streamName() {
        return rtmp_streamName;
    }

    public void setRtmp_streamName(String rtmp_streamName) {
        this.rtmp_streamName = rtmp_streamName;
    }

    public String getRtmp_serverIp() {
        return rtmp_serverIp;
    }

    public void setRtmp_serverIp(String rtmp_serverIp) {
        this.rtmp_serverIp = rtmp_serverIp;
    }

    public Integer getRtmp_serverPort() {
        return rtmp_serverPort;
    }

    public void setRtmp_serverPort(Integer rtmp_serverPort) {
        this.rtmp_serverPort = rtmp_serverPort;
    }

    public Long getUdp_net_select() {
        return udp_net_select;
    }

    public void setUdp_net_select(Long udp_net_select) {
        this.udp_net_select = udp_net_select;
    }

    public Long getSrt_net_select() {
        return srt_net_select;
    }

    public void setSrt_net_select(Long srt_net_select) {
        this.srt_net_select = srt_net_select;
    }

    public Long getRist_net_select0() {
        return rist_net_select0;
    }

    public void setRist_net_select0(Long rist_net_select0) {
        this.rist_net_select0 = rist_net_select0;
    }

    public Long getRist_net_select1() {
        return rist_net_select1;
    }

    public void setRist_net_select1(Long rist_net_select1) {
        this.rist_net_select1 = rist_net_select1;
    }

    public Long getRtmp_net_select() {
        return rtmp_net_select;
    }

    public void setRtmp_net_select(Long rtmp_net_select) {
        this.rtmp_net_select = rtmp_net_select;
    }
}

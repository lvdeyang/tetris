package com.sumavision.tetris.sts.device;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

public class EncapsulateAuth implements Authorization {


    /**
	 * 
	 */
	private static final long serialVersionUID = -6611933020093094677L;

	private String cmmb;

    private String mpts;

    private String drm;

    private String pip;

    private String ts_udp;

    private String ts_rtp;
    
    private String ts_srt;

    private String ts_http;

    private String ssm;

    private String rtmp_flv;

    private String rtsp;

    private String asf;

    private String hls;

    private String http_flv;

    private String hds;

    private String dash;

    private String rtmpdynamic;

    private Integer input_num;

    private Integer output_num;

    public String getCmmb() {
        return cmmb;
    }

    public void setCmmb(String cmmb) {
        this.cmmb = cmmb;
    }

    public String getMpts() {
        return mpts;
    }

    public void setMpts(String mpts) {
        this.mpts = mpts;
    }

    public String getDrm() {
        return drm;
    }

    public void setDrm(String drm) {
        this.drm = drm;
    }

    public String getPip() {
        return pip;
    }

    public void setPip(String pip) {
        this.pip = pip;
    }

    public String getTs_udp() {
        return ts_udp;
    }

    public void setTs_udp(String ts_udp) {
        this.ts_udp = ts_udp;
    }

    @XmlAttribute(name = "ts-rtp")
    public String getTs_rtp() {
        return ts_rtp;
    }

    public void setTs_rtp(String ts_rtp) {
        this.ts_rtp = ts_rtp;
    }

    public String getTs_http() {
        return ts_http;
    }

    public void setTs_http(String ts_http) {
        this.ts_http = ts_http;
    }

    public String getSsm() {
        return ssm;
    }

    public void setSsm(String ssm) {
        this.ssm = ssm;
    }

    public String getRtmp_flv() {
        return rtmp_flv;
    }

    public void setRtmp_flv(String rtmp_flv) {
        this.rtmp_flv = rtmp_flv;
    }

    public String getRtsp() {
        return rtsp;
    }

    public void setRtsp(String rtsp) {
        this.rtsp = rtsp;
    }

    public String getAsf() {
        return asf;
    }

    public void setAsf(String asf) {
        this.asf = asf;
    }

    public String getHls() {
        return hls;
    }

    public void setHls(String hls) {
        this.hls = hls;
    }

    public String getHttp_flv() {
        return http_flv;
    }

    public void setHttp_flv(String http_flv) {
        this.http_flv = http_flv;
    }

    public String getHds() {
        return hds;
    }

    public void setHds(String hds) {
        this.hds = hds;
    }

    public String getDash() {
        return dash;
    }

    public void setDash(String dash) {
        this.dash = dash;
    }

    public String getRtmpdynamic() {
        return rtmpdynamic;
    }

    public void setRtmpdynamic(String rtmpdynamic) {
        this.rtmpdynamic = rtmpdynamic;
    }

    public Integer getInput_num() {
        return input_num;
    }

    public void setInput_num(Integer input_num) {
        this.input_num = input_num;
    }

    public Integer getOutput_num() {
        return output_num;
    }

    public void setOutput_num(Integer output_num) {
        this.output_num = output_num;
    }

	public String getTs_srt() {
		return ts_srt;
	}

	public void setTs_srt(String ts_srt) {
		this.ts_srt = ts_srt;
	}
}

package com.sumavision.signal.bvc.director.bo;

import io.swagger.models.auth.In;

public class RequestTaskBo {
    private Integer index;
    private String codec;
    private String resolution;
    private String fps;
    private String rc_mode;
    private Long vbitrate;//kbs
    private Long abitrate;
    private Integer sample_rate;

    public Integer getIndex() {
        return index;
    }

    public String getCodec() {
        return codec;
    }

    public String getResolution() {
        return resolution;
    }

    public String getFps() {
        return fps;
    }

    public String getRc_mode() {
        return rc_mode;
    }

    public Long getVbitrate() {
        return vbitrate;
    }

    public Long getAbitrate() {
        return abitrate;
    }

    public Integer getSample_rate() {
        return sample_rate;
    }

    public RequestTaskBo setIndex(Integer index) {
        this.index = index;
        return this;
    }

    public RequestTaskBo setCodec(String codec) {
        this.codec = codec;
        return this;
    }

    public RequestTaskBo setResolution(String resolution) {
        this.resolution = resolution;
        return this;
    }

    public RequestTaskBo setFps(String fps) {
        this.fps = fps;
        return this;
    }

    public RequestTaskBo setRc_mode(String rc_mode) {
        this.rc_mode = rc_mode;
        return this;
    }

    public RequestTaskBo setVbitrate(Long vbitrate) {
        this.vbitrate = vbitrate;
        return this;
    }

    public RequestTaskBo setAbitrate(Long abitrate) {
        this.abitrate = abitrate;
        return this;
    }

    public RequestTaskBo setSample_rate(Integer sample_rate) {
        this.sample_rate = sample_rate;
        return this;
    }
}

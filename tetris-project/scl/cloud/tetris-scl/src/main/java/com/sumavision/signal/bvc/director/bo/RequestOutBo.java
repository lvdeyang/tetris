package com.sumavision.signal.bvc.director.bo;

public class RequestOutBo {
    private Integer index;
    private String type;
    private String url;
    private String local_ip;
    private String rate_ctrl;
    private Long bitrate;
    private String scramble_mode;//none AES-128
    private String scramble_key;
    private String ts_mode;//188 192 204

    public Integer getIndex() {
        return index;
    }

    public RequestOutBo setIndex(Integer index) {
        this.index = index;
        return this;
    }

    public String getType() {
        return type;
    }

    public RequestOutBo setType(String type) {
        this.type = type;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public RequestOutBo setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getLocal_ip() {
        return local_ip;
    }

    public RequestOutBo setLocal_ip(String local_ip) {
        this.local_ip = local_ip;
        return this;
    }

    public String getRate_ctrl() {
        return rate_ctrl;
    }

    public RequestOutBo setRate_ctrl(String rate_ctrl) {
        this.rate_ctrl = rate_ctrl;
        return this;
    }

    public Long getBitrate() {
        return bitrate;
    }

    public RequestOutBo setBitrate(Long bitrate) {
        this.bitrate = bitrate;
        return this;
    }

    public String getScramble_mode() {
        return scramble_mode;
    }

    public RequestOutBo setScramble_mode(String scramble_mode) {
        this.scramble_mode = scramble_mode;
        return this;
    }

    public String getScramble_key() {
        return scramble_key;
    }

    public RequestOutBo setScramble_key(String scramble_key) {
        this.scramble_key = scramble_key;
        return this;
    }

    public String getTs_mode() {
        return ts_mode;
    }

    public RequestOutBo setTs_mode(String ts_mode) {
        this.ts_mode = ts_mode;
        return this;
    }
}

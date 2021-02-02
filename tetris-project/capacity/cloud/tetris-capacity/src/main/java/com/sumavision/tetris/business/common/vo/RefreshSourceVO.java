package com.sumavision.tetris.business.common.vo;/**
 * Created by Poemafar on 2021/2/1 13:58
 */

/**
 * @ClassName: RefreshSourceVO
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2021/2/1 13:58
 */
public class RefreshSourceVO {

    private String deviceIp;

    private String type;

    private String url;

    private String localIp;

    private String srtMode;

    private Integer latency;

    public String getDeviceIp() {
        return deviceIp;
    }

    public RefreshSourceVO setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
        return this;
    }

    public String getType() {
        return type;
    }

    public RefreshSourceVO setType(String type) {
        this.type = type;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public RefreshSourceVO setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getLocalIp() {
        return localIp;
    }

    public RefreshSourceVO setLocalIp(String localIp) {
        this.localIp = localIp;
        return this;
    }

    public String getSrtMode() {
        return srtMode;
    }

    public RefreshSourceVO setSrtMode(String srtMode) {
        this.srtMode = srtMode;
        return this;
    }

    public Integer getLatency() {
        return latency;
    }

    public RefreshSourceVO setLatency(Integer latency) {
        this.latency = latency;
        return this;
    }
}

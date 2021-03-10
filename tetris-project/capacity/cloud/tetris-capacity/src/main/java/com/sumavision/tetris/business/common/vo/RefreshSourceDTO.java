package com.sumavision.tetris.business.common.vo;/**
 * Created by Poemafar on 2021/2/1 13:58
 */

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.capacity.bo.input.Igmpv3BO;

import java.util.List;

/**
 * @ClassName: RefreshSourceDTO
 * @Description 能力服务传输的刷源参数
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2021/2/1 13:58
 */
public class RefreshSourceDTO {

    /**
     * 刷源设备
     */
    private String deviceIp;

    /**
     * 源类型，必填
     */
    private String type;

    /**
     * 源地址，必填
     */
    private String url;

    /**
     * 刷源网口
     */
    private String localIp;

    /**
     * srt 模式
     */
    private String srtMode;

    /**
     * 加密密码
     */
    private String passphrase;

    /**
     * 密钥长度
     */
    private String keyLen;

    /**
     * 接收延迟
     */
    private Integer latency;

    /**
     * 文件循环次数
     */
    private Integer loopCount;

    /**
     * SDI卡类型
     */
    private String cardType;

    /**
     * IGMPV3开关
     */
    private Boolean beIgmpv3;

    /**
     * IGMPV3模式，取值include,exclude
     */
    private String igmpv3Mode;

    /**
     * 控制IP列表 [ip1,ip2]
     */
    private List<String> igmpv3IpList;

    public String getDeviceIp() {
        return deviceIp;
    }

    public RefreshSourceDTO setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
        return this;
    }

    public String getType() {
        return type;
    }

    public RefreshSourceDTO setType(String type) {
        this.type = type;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public RefreshSourceDTO setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getLocalIp() {
        return localIp;
    }

    public RefreshSourceDTO setLocalIp(String localIp) {
        this.localIp = localIp;
        return this;
    }

    public String getSrtMode() {
        return srtMode;
    }

    public RefreshSourceDTO setSrtMode(String srtMode) {
        this.srtMode = srtMode;
        return this;
    }

    public Integer getLatency() {
        return latency;
    }

    public RefreshSourceDTO setLatency(Integer latency) {
        this.latency = latency;
        return this;
    }

    public Integer getLoopCount() {
        return loopCount;
    }

    public RefreshSourceDTO setLoopCount(Integer loopCount) {
        this.loopCount = loopCount;
        return this;
    }

    public String getCardType() {
        return cardType;
    }

    public RefreshSourceDTO setCardType(String cardType) {
        this.cardType = cardType;
        return this;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public RefreshSourceDTO setPassphrase(String passphrase) {
        this.passphrase = passphrase;
        return this;
    }

    public String getKeyLen() {
        return keyLen;
    }

    public RefreshSourceDTO setKeyLen(String keyLen) {
        this.keyLen = keyLen;
        return this;
    }

    public Boolean getBeIgmpv3() {
        return beIgmpv3;
    }

    public RefreshSourceDTO setBeIgmpv3(Boolean beIgmpv3) {
        this.beIgmpv3 = beIgmpv3;
        return this;
    }

    public String getIgmpv3Mode() {
        return igmpv3Mode;
    }

    public RefreshSourceDTO setIgmpv3Mode(String igmpv3Mode) {
        this.igmpv3Mode = igmpv3Mode;
        return this;
    }

    public List<String> getIgmpv3IpList() {
        return igmpv3IpList;
    }

    public RefreshSourceDTO setIgmpv3IpList(List<String> igmpv3IpList) {
        this.igmpv3IpList = igmpv3IpList;
        return this;
    }
}

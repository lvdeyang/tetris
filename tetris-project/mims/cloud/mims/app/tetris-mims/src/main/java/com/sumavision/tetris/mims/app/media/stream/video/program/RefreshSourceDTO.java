package com.sumavision.tetris.mims.app.media.stream.video.program;/**
 * Created by Poemafar on 2021/2/25 19:01
 */

/**
 * @ClassName: RefreshSourceDTO
 * @Description 能力服务传输的刷源参数，参数映射不区分大小写，下划线
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2021/2/25 19:01
 */
public class RefreshSourceDTO {

    private String deviceIp;

    private String type;

    private String url;

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
     * 文件循环次数，默认无限
     */
    private Integer loopCount;

    /**
     * SDI卡类型
     */
    String cardType;

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
}

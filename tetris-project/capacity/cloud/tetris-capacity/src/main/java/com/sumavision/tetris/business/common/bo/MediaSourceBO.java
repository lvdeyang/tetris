package com.sumavision.tetris.business.common.bo;/**
 * Created by Poemafar on 2020/9/23 10:10
 */


import com.sumavision.tetris.business.common.enumeration.ProtocolType;
import com.sumavision.tetris.business.common.vo.RefreshSourceVO;
import com.sumavision.tetris.commons.exception.BaseException;

/**
 * @ClassName: MediaSourceBO
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/9/23 10:10
 */
public class MediaSourceBO {

    String url;

    String localIp;

    ProtocolType protocolType;

    //srt额外参数
    String mode;

    Integer latency;

    Integer connect_timeout;

    Integer recv_buffsize;

    String maxbw; //bps

    Integer recv_timeout;

    String passphrase;

    String key_len;

    public String getUrl() {
        return url;
    }

    public MediaSourceBO setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getLocalIp() {
        return localIp;
    }

    public MediaSourceBO setLocalIp(String localIp) {
        this.localIp = localIp;
        return this;
    }

    public ProtocolType getProtocolType() {
        return protocolType;
    }

    public MediaSourceBO setProtocolType(ProtocolType protocolType) {
        this.protocolType = protocolType;
        return this;
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

    public Integer getRecv_buffsize() {
        return recv_buffsize;
    }

    public void setRecv_buffsize(Integer recv_buffsize) {
        this.recv_buffsize = recv_buffsize;
    }

    public String getMaxbw() {
        return maxbw;
    }

    public void setMaxbw(String maxbw) {
        this.maxbw = maxbw;
    }

    public Integer getRecv_timeout() {
        return recv_timeout;
    }

    public void setRecv_timeout(Integer recv_timeout) {
        this.recv_timeout = recv_timeout;
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

    public MediaSourceBO(){}

    public MediaSourceBO(RefreshSourceVO refreshSourceVO,String localIp) throws BaseException {
        this.url = refreshSourceVO.getUrl();
        this.localIp = localIp;
        this.protocolType = ProtocolType.getProtocolType(refreshSourceVO.getType());
        this.mode = refreshSourceVO.getSrtMode();
        this.latency = refreshSourceVO.getLatency();
    }
}

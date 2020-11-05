package com.sumavision.signal.bvc.capacity.bo.source;/**
 * Created by Poemafar on 2020/9/23 10:10
 */

import com.sumavision.signal.bvc.common.enumeration.CommonConstants.*;
import com.sumavision.signal.bvc.entity.enumeration.OneOneFiveMParam;
import com.sumavision.signal.bvc.entity.po.SourcePO;
import com.sumavision.signal.bvc.fifthg.bo.http.SuiBusParam;
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

    public MediaSourceBO(SuiBusParam suiBusParam) throws BaseException {
        ProtocolType type = ProtocolType.getProtocolTypeByPackage(suiBusParam.getProtocol());
        this.protocolType = type;
        if (type.equals(ProtocolType.RTMP)){
            StringBuilder sb = new StringBuilder().append("rtmp://")
                    .append(suiBusParam.getRtmp_serverIp())
                    .append(":")
                    .append(suiBusParam.getRtmp_serverPort())
                    .append("/")
                    .append(suiBusParam.getRtmp_appName())
                    .append("/")
                    .append(suiBusParam.getRtmp_streamName());
            this.url = sb.toString();
        }else if (type.equals(ProtocolType.SRT_TS)){
            StringBuilder sbSrt = new StringBuilder().append("srt://")
                    .append(suiBusParam.getSrt_send_ip())
                    .append(":")
                    .append(suiBusParam.getSrt_send_port());
            this.url= sbSrt.toString();
            this.mode="listener";
        }

    }

    public MediaSourceBO(SourcePO sourcePO) {
        this.url = sourcePO.getUrl();
        this.protocolType = sourcePO.getProtocolType();
        if (sourcePO.getSrtMode()!=null) {
            this.mode = sourcePO.getSrtMode().equals("caller")?"listener":"caller";
        }
    }

    public MediaSourceBO(SourcePO sourcePO,String localIp){
        this.url = sourcePO.getUrl();
        this.protocolType = sourcePO.getProtocolType();
        if (sourcePO.getSrtMode()!=null) {
            this.mode = sourcePO.getSrtMode().equals("caller")?"listener":"caller";
        }
        this.localIp = localIp;
    }
}

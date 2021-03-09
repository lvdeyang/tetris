package com.sumavision.tetris.business.common.bo;/**
 * Created by Poemafar on 2020/9/23 10:10
 */


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.business.common.enumeration.IgmpV3Mode;
import com.sumavision.tetris.business.common.enumeration.ProtocolType;
import com.sumavision.tetris.business.common.vo.RefreshSourceDTO;
import com.sumavision.tetris.capacity.bo.input.Igmpv3BO;
import com.sumavision.tetris.commons.exception.BaseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    /**
     * 文件循环次数
     */
    Integer loop_count=-1;

    /**
     * 卡类型
     */
    String card_type="blackmagic";

    Igmpv3BO igmpv3;

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

    public Integer getLoop_count() {
        return loop_count;
    }

    public MediaSourceBO setLoop_count(Integer loop_count) {
        this.loop_count = loop_count;
        return this;
    }

    public String getCard_type() {
        return card_type;
    }

    public MediaSourceBO setCard_type(String card_type) {
        this.card_type = card_type;
        return this;
    }

    public Igmpv3BO getIgmpv3() {
        return igmpv3;
    }

    public MediaSourceBO setIgmpv3(Igmpv3BO igmpv3) {
        this.igmpv3 = igmpv3;
        return this;
    }

    public MediaSourceBO(){}

    public MediaSourceBO(RefreshSourceDTO refreshSourceDTO, String localIp) throws BaseException {
        this.url = refreshSourceDTO.getUrl();
        this.localIp = localIp;
        this.protocolType = ProtocolType.getProtocolType(refreshSourceDTO.getType());
        this.mode = refreshSourceDTO.getSrtMode();
        this.latency = refreshSourceDTO.getLatency();
        this.key_len = refreshSourceDTO.getKeyLen();
        this.passphrase = refreshSourceDTO.getPassphrase();
        if (refreshSourceDTO.getLoopCount() != null) {
            this.loop_count = refreshSourceDTO.getLoopCount();
        }
        if (refreshSourceDTO.getCardType() != null) {
            this.card_type= refreshSourceDTO.getCardType();
        }
        if (Boolean.TRUE.equals(refreshSourceDTO.getBeIgmpv3())) {
            Igmpv3BO igmpv3BO = new Igmpv3BO();
            IgmpV3Mode mode = IgmpV3Mode.getIgmpV3Mode(refreshSourceDTO.getIgmpv3Mode());
            igmpv3BO.setMode(mode.name().toLowerCase(Locale.ENGLISH));
            JSONArray ipArray = new JSONArray();
            for (int i = 0; i < refreshSourceDTO.getIgmpv3IpList().size(); i++) {
                String ip = refreshSourceDTO.getIgmpv3IpList().get(i);
                JSONObject ipObj = new JSONObject();
                ipObj.put("ip",ip);
                ipArray.add(ipObj);
            }
            igmpv3BO.setIp_array(ipArray);
            this.igmpv3=igmpv3BO;
        }
    }


}

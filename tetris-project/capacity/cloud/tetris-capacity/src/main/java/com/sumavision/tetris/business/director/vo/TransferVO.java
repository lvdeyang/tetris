package com.sumavision.tetris.business.director.vo;/**
 * Created by Poemafar on 2020/9/24 17:47
 */

/**
 * @ClassName: TransferVO
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/9/24 17:47
 */
public class TransferVO {

    public String device_ip;

    public Integer device_port;

    public String mission_id;

    public String inType;

    private String inUrl;

    private String outType;

    private String outUrl;

    private String srtMode;

    public String getDevice_ip() {
        return device_ip;
    }

    public void setDevice_ip(String device_ip) {
        this.device_ip = device_ip;
    }

    public String getInType() {
        return inType;
    }

    public void setInType(String inType) {
        this.inType = inType;
    }

    public String getInUrl() {
        return inUrl;
    }

    public void setInUrl(String inUrl) {
        this.inUrl = inUrl;
    }

    public String getOutType() {
        return outType;
    }

    public void setOutType(String outType) {
        this.outType = outType;
    }

    public String getOutUrl() {
        return outUrl;
    }

    public void setOutUrl(String outUrl) {
        this.outUrl = outUrl;
    }

    public String getMission_id() {
        return mission_id;
    }

    public void setMission_id(String mission_id) {
        this.mission_id = mission_id;
    }

    public String getSrtMode() {
        return srtMode;
    }

    public void setSrtMode(String srtMode) {
        this.srtMode = srtMode;
    }

    public Integer getDevice_port() {
        return device_port;
    }

    public TransferVO setDevice_port(Integer device_port) {
        this.device_port = device_port;
        return this;
    }
}

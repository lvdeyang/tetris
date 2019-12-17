package com.sumavision.tetris.sts.task.tasklink;

import java.io.Serializable;

import javax.persistence.*;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.sts.common.CommonPO;
import com.sumavision.tetris.sts.device.Authorization;
import com.sumavision.tetris.sts.device.StreamMediaAuth;
import com.sumavision.tetris.sts.device.StreamMediaCfg;

@Entity
@Table
public class StreamMediaPO extends CommonPO<StreamMediaPO> implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -1266412045852453266L;

	/**
     * 单元版本信息
     */
    private String unitVersion;

    private String socketAddress;

    private String cfg;

    private Integer clientNum;

    private String streamMediaAuthJson;

    private Long groupId;

    @Column
    public String getUnitVersion() {
        return unitVersion;
    }
    public void setUnitVersion(String unitVersion) {
        this.unitVersion = unitVersion;
    }
    @Column
    public String getSocketAddress() {
        return socketAddress;
    }
    public void setSocketAddress(String socketAddress) {
        this.socketAddress = socketAddress;
    }
    @Column(length = 1024)
    public String getCfg() {
        return cfg;
    }

    public void setCfg(String cfg) {
        this.cfg = cfg;
    }

    @Column
    public String getStreamMediaAuthJson() {
        return streamMediaAuthJson;
    }

    public void setStreamMediaAuthJson(String streamMediaAuthJson) {
        this.streamMediaAuthJson = streamMediaAuthJson;
    }

    @Column
    public Integer getClientNum() {
        return clientNum;
    }

    public void setClientNum(Integer clientNum) {
        this.clientNum = clientNum;
    }

    @Column
    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    @Transient
    public StreamMediaCfg getStreamMediaCfg() {
        return JSON.parseObject(cfg , StreamMediaCfg.class);
    }

    public void setStreamMediaCfg(StreamMediaCfg streamMediaCfg) {
        this.cfg = JSON.toJSONString(streamMediaCfg);
    }

    @Transient
    public StreamMediaAuth getStreamMediaAuth() {
        return JSON.parseObject(streamMediaAuthJson , StreamMediaAuth.class);
    }

    private void setStreamMediaAuth(StreamMediaAuth auth) {
        this.clientNum = auth.getClientNum();
        this.streamMediaAuthJson = JSON.toJSONString(auth);
    }

    public void setAuth(Authorization auth) {
        if (auth instanceof StreamMediaAuth)
            setStreamMediaAuth((StreamMediaAuth)auth);
    }
}

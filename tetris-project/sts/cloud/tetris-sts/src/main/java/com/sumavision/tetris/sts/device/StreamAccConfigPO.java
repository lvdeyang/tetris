package com.sumavision.tetris.sts.device;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.sts.common.CommonConstants.ProtoType;
import com.sumavision.tetris.sts.common.CommonPO;
import com.sumavision.tetris.sts.task.source.FilterIpSegment;

import javax.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lost on 2017/8/4.
 */
@Entity
@Table
public class StreamAccConfigPO extends CommonPO<StreamAccConfigPO> implements Serializable {

    private static final long serialVersionUID = -8234552121389360325L;

    private Long streamMediaId;

    private String pubName;

    private ProtoType protoType;

    /*
    * none , white , black
    * */
    private String accType;

    private List<FilterIpSegment> filterIpSegments;

    private String filterJson;

    @Column
    public Long getStreamMediaId() {
        return streamMediaId;
    }

    public void setStreamMediaId(Long streamMediaId) {
        this.streamMediaId = streamMediaId;
    }

    @Column
    public String getPubName() {
        return pubName;
    }

    public void setPubName(String pubName) {
        this.pubName = pubName;
    }

    @Column
    @Enumerated(EnumType.STRING)
    public ProtoType getProtoType() {
        return protoType;
    }

    public void setProtoType(ProtoType protoType) {
        this.protoType = protoType;
    }

    @Transient
    public List<FilterIpSegment> getFilterIpSegments() {
        if (null == this.filterJson)
            return new ArrayList<>();
        return JSON.parseArray(this.filterJson , FilterIpSegment.class);
    }

    public void setFilterIpSegments(List<FilterIpSegment> filterIpSegments) {
        this.filterJson = JSON.toJSONString(filterIpSegments);
    }

    @Column
    public String getFilterJson() {
        return filterJson;
    }

    public void setFilterJson(String filterJson) {
        this.filterJson = filterJson;
    }

    @Column
    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }
}

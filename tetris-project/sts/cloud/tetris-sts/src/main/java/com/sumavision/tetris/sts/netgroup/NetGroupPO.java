package com.sumavision.tetris.sts.netgroup;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.sumavision.tetris.sts.common.CommonPO;

import javax.persistence.*;
import java.io.Serializable;
import com.sumavision.tetris.sts.common.CommonConstants.NetGroupType;
/**
 * Created by Poemafar on 2019/12/16 13:50
 */
@Entity
@Table(name = "net_group")
public class NetGroupPO extends CommonPO<NetGroupPO> implements Serializable {

    private static final long serialVersionUID = -9164943864838791161L;

    private String netName;

    private Integer groupRole;

    private String info;

    private NetGroupType netType;

    private String netIp;

    private String  netMask;

    public String getNetName() {
        return netName;
    }

    public void setNetName(String netName) {
        this.netName = netName;
    }

    public Integer getGroupRole() {
        return groupRole;
    }

    public void setGroupRole(Integer groupRole) {
        this.groupRole = groupRole;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Column
    @Enumerated(EnumType.STRING)
    public NetGroupType getNetType() {
        return netType;
    }

    public void setNetType(NetGroupType netType) {
        this.netType = netType;
    }

    public String getNetIp() {
        return netIp;
    }

    public void setNetIp(String netIp) {
        this.netIp = netIp;
    }

    public String getNetMask() {
        return netMask;
    }

    public void setNetMask(String netMask) {
        this.netMask = netMask;
    }
}

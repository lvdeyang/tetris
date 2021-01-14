package com.sumavision.tetris.device.netgroup;

import com.sumavision.tetris.business.common.enumeration.NetGroupType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

import javax.persistence.*;

/**
 * Created by Poemafar on 2019/12/16 13:50
 */
@Entity
@Table(name = "TETRIS_CAPACITY_NET_GROUP")
public class NetGroupPO extends AbstractBasePO {


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

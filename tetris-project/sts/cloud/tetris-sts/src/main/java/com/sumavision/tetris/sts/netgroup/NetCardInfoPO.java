package com.sumavision.tetris.sts.netgroup;


import javax.persistence.*;

import org.hibernate.annotations.Type;

import com.sumavision.tetris.sts.common.CommonConstants.BondType;
import com.sumavision.tetris.sts.common.CommonConstants.NetCardType;
import com.sumavision.tetris.sts.common.CommonPO;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="net_card_info")
public class NetCardInfoPO extends CommonPO<NetCardInfoPO> implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7054763532518079754L;
	public static final Integer LINK_STATUS_NORMAL = 1;
	public static final Integer LINK_STATUS_ABNORMAL = 0;

	public NetCardInfoPO() {
        this.type = NetCardType.DEFAULT;
    }

    /**
     * 网卡类型
     * 0：普通
     * 1：数据
     * 2：输入
     * 3：输出
     */
    private NetCardType type;

    private Integer indexNum;

    private String name;

    /**
     * deviceId指的是deviceNodeId设备节点id
     */
    private Long deviceId;
    
    /**
     * 所属网络分组的id
     */
    private Long netGroupId;
    
    /**
     * ipv4地址
     */
    private String ipv4;
    
    private String virtualIpv4;
    
    /**
     * ipv4网关
     */
    private String ipv4Gate;
    
    /**
     * ipv4子网掩码
     */
    private String ipv4Mask;
    
    /**
     * ipv6地址
     */
    private String ipv6;
    
    /**
     * ipv6网关
     */
    private String ipv6Gate;
    
    /**
     * ipv6子网掩码
     */
    private String ipv6Mask;
    
    /**
     * Mac地址
     */
    private String mac;


    /**
     * 网卡连接状态
     * 0：异常
     * 1：正常
     */
    private Integer status;

    private Integer enable;

    private Integer linked;

    private String bondName;

    private BondType bondType;

    private String primaryName;

    private String vlanName;

    private List<NetCardInfoPO> netCardInfoPOS;

    private String routes;
    
    /**是否为控制网卡*/
    private Boolean beCtrl = false;

    @Column
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column
    @Enumerated(EnumType.STRING)
    public NetCardType getType() {
        return type;
    }

    public void setType(NetCardType type) {
        this.type = type;
    }

    @Column
    public String getIpv4() {
        return ipv4;
    }

    public void setIpv4(String ipv4) {
        this.ipv4 = ipv4;
    }

    @Column
    public String getVirtualIpv4() {
        return virtualIpv4;
    }

    public void setVirtualIpv4(String virtualIpv4) {
        this.virtualIpv4 = virtualIpv4;
    }

    @Column
    public String getIpv4Gate() {
        return ipv4Gate;
    }

    public void setIpv4Gate(String ipv4Gate) {
        this.ipv4Gate = ipv4Gate;
    }

    @Column
    public String getIpv4Mask() {
        return ipv4Mask;
    }

    public void setIpv4Mask(String ipv4Mask) {
        this.ipv4Mask = ipv4Mask;
    }

    @Column
    public String getIpv6() {
        return ipv6;
    }

    public void setIpv6(String ipv6) {
        this.ipv6 = ipv6;
    }

    @Column
    public String getIpv6Gate() {
        return ipv6Gate;
    }

    public void setIpv6Gate(String ipv6Gate) {
        this.ipv6Gate = ipv6Gate;
    }

    @Column
    public String getIpv6Mask() {
        return ipv6Mask;
    }

    public void setIpv6Mask(String ipv6Mask) {
        this.ipv6Mask = ipv6Mask;
    }

    @Column
    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    @Column
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column
    public Long getNetGroupId() {
        return netGroupId;
    }

    public void setNetGroupId(Long netGroupId) {
        this.netGroupId = netGroupId;
    }

    @Column
    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    @Column
    public String getBondName() {
        return bondName;
    }

    public void setBondName(String bondName) {
        this.bondName = bondName;
    }


    @Column
    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    @Column
    @Enumerated(EnumType.STRING)
    public BondType getBondType() {
        return bondType;
    }

    public void setBondType(BondType bondType) {
        this.bondType = bondType;
    }

    @Column
    public String getPrimaryName() {
        return primaryName;
    }

    public void setPrimaryName(String primaryName) {
        this.primaryName = primaryName;
    }

    @Transient
    public List<NetCardInfoPO> getNetCardInfoPOS() {
        return netCardInfoPOS;
    }

    public void setNetCardInfoPOS(List<NetCardInfoPO> netCardInfoPOS) {
        this.netCardInfoPOS = netCardInfoPOS;
    }

    @Column
    public String getVlanName() {
        return vlanName;
    }

    public void setVlanName(String vlanName) {
        this.vlanName = vlanName;
    }

    @Column
    public String getRoutes() {
        return routes;
    }

    public void setRoutes(String routes) {
        this.routes = routes;
    }

    @Column
    public Integer getIndexNum() {
        return indexNum;
    }

    public void setIndexNum(Integer indexNum) {
        this.indexNum = indexNum;
    }

    @Column
    public Integer getLinked() {
        return linked;
    }

    public void setLinked(Integer linked) {
        this.linked = linked;
    }

    @Column
    @Type(type = "yes_no")
	public Boolean getBeCtrl() {
		return beCtrl;
	}

	public void setBeCtrl(Boolean beCtrl) {
		this.beCtrl = beCtrl;
	}
}

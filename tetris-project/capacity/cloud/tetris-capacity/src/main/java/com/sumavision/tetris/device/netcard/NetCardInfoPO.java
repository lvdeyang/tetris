package com.sumavision.tetris.device.netcard;/**
 * Created by Poemafar on 2020/9/25 16:26
 */

import com.sumavision.tetris.business.common.enumeration.BondType;
import com.sumavision.tetris.business.common.enumeration.BusinessType;
import com.sumavision.tetris.business.common.enumeration.TaskType;
import com.sumavision.tetris.orm.po.AbstractBasePO;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * @ClassName: TemplatePO
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/9/25 16:26
 */
@Entity
@Table(name = "TETRIS_CAPACITY_DEVICE_NETCARD")
public class NetCardInfoPO extends AbstractBasePO {

    private String name;

    /**
     * 网卡序号
     */
    private Integer cardNum;

    /**
     *
     * 是否是控制口
     */
    private Boolean beCtrl=false;
    /**
     * ipv4地址
     */
    private String ipv4;
    private String virtualIpv4;
    private String ipv4Dns;
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
    private String ipv6Dns;
    /**
     * ipv6网关
     */
    private String ipv6Gate;
    /**
     * ipv6子网掩码
     */
    private String ipv6Mask;
    /**
     * MAC地址
     */
    private String mac;
    /**
     * 网卡连接状态，1正常，0异常
     */
    private Integer status;

    private BondType bondType;
    /**
     * 是否匹配网络组
     */
    private Integer matchNetGroup;

    private String prefix;

    private String vlanName;

    private Integer enable;//启用禁用

    private Integer linked;

    private String routes;

    private String primaryName;

    private Long deviceId;
    /**
     * 所属网络分组的id
     */
    private Long inputNetGroupId;
    private Long outputNetGroupId;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCardNum(Integer cardNum) {
        this.cardNum = cardNum;
    }

    public Integer getCardNum() {
        return cardNum;
    }

    public void setIpv4(String ipv4) {
        this.ipv4 = ipv4;
    }

    public String getIpv4() {
        return ipv4;
    }

    public void setIpv4Dns(String ipv4Dns) {
        this.ipv4Dns = ipv4Dns;
    }

    public String getIpv4Dns() {
        return ipv4Dns;
    }

    public void setIpv4Gate(String ipv4Gate) {
        this.ipv4Gate = ipv4Gate;
    }

    public String getIpv4Gate() {
        return ipv4Gate;
    }

    public void setIpv4Mask(String ipv4Mask) {
        this.ipv4Mask = ipv4Mask;
    }

    public String getIpv4Mask() {
        return ipv4Mask;
    }

    public void setIpv6(String ipv6) {
        this.ipv6 = ipv6;
    }

    public String getIpv6() {
        return ipv6;
    }

    public void setIpv6Dns(String ipv6Dns) {
        this.ipv6Dns = ipv6Dns;
    }

    public String getIpv6Dns() {
        return ipv6Dns;
    }

    public void setIpv6Gate(String ipv6Gate) {
        this.ipv6Gate = ipv6Gate;
    }

    public String getIpv6Gate() {
        return ipv6Gate;
    }

    public void setIpv6Mask(String ipv6Mask) {
        this.ipv6Mask = ipv6Mask;
    }

    public String getIpv6Mask() {
        return ipv6Mask;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getMac() {
        return mac;
    }


    public String getRoutes() {
        return routes;
    }

    public void setRoutes(String routes) {
        this.routes = routes;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getLinked() {
        return linked;
    }

    public void setLinked(Integer linked) {
        this.linked = linked;
    }

    public Integer getStatus() {
        return status;
    }

    public String getVlanName() {
        return vlanName;
    }

    public void setVlanName(String vlanName) {
        this.vlanName = vlanName;
    }

    public String getPrimaryName() {
        return primaryName;
    }

    public void setPrimaryName(String primaryName) {
        this.primaryName = primaryName;
    }

    @Type(type = "yes_no")
    public Boolean getBeCtrl() {
        return beCtrl;
    }

    public void setBeCtrl(Boolean beCtrl) {
        this.beCtrl = beCtrl;
    }

    public void setMatchNetGroup(Integer matchNetGroup) {
        this.matchNetGroup = matchNetGroup;
    }

    public Integer getMatchNetGroup() {
        return matchNetGroup;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public BondType getBondType() {
        return bondType;
    }

    public void setBondType(BondType bondType) {
        this.bondType = bondType;
    }

    public String getVirtualIpv4() {
        return virtualIpv4;
    }

    public void setVirtualIpv4(String virtualIpv4) {
        this.virtualIpv4 = virtualIpv4;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public Long getInputNetGroupId() {
        return inputNetGroupId;
    }

    public void setInputNetGroupId(Long inputNetGroupId) {
        this.inputNetGroupId = inputNetGroupId;
    }

    public Long getOutputNetGroupId() {
        return outputNetGroupId;
    }

    public void setOutputNetGroupId(Long outputNetGroupId) {
        this.outputNetGroupId = outputNetGroupId;
    }
}

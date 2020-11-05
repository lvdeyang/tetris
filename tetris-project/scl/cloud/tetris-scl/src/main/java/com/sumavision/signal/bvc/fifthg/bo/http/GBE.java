package com.sumavision.signal.bvc.fifthg.bo.http;/**
 * Created by Poemafar on 2020/8/31 9:29
 */

/**
 * @ClassName: GBE
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/8/31 9:29
 */
public class GBE {

    private String netName;
    private Integer dhcpRadiosV4;
    private String ipAddressV4;
    private String subnetV4;
    private String gatewayV4;

    public String getNetName() {
        return netName;
    }

    public void setNetName(String netName) {
        this.netName = netName;
    }

    public Integer getDhcpRadiosV4() {
        return dhcpRadiosV4;
    }

    public void setDhcpRadiosV4(Integer dhcpRadiosV4) {
        this.dhcpRadiosV4 = dhcpRadiosV4;
    }

    public String getIpAddressV4() {
        return ipAddressV4;
    }

    public void setIpAddressV4(String ipAddressV4) {
        this.ipAddressV4 = ipAddressV4;
    }

    public String getSubnetV4() {
        return subnetV4;
    }

    public void setSubnetV4(String subnetV4) {
        this.subnetV4 = subnetV4;
    }

    public String getGatewayV4() {
        return gatewayV4;
    }

    public void setGatewayV4(String gatewayV4) {
        this.gatewayV4 = gatewayV4;
    }
}

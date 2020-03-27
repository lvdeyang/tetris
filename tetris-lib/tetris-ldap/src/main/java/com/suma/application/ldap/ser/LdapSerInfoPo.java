package com.suma.application.ldap.ser;

import org.springframework.ldap.core.DistinguishedName;

//ldap服务设备节点
public class LdapSerInfoPo {
	private DistinguishedName dn;
	private String serUuid;
	private String serNo;
	private String serName;
	private String serAddr;
	private Integer serPort;
	private String serPwd;
	private Integer serType;
	private Integer serPro;
	private String serNode;
	private String serFactInfo;
	public DistinguishedName getDn() {
		return dn;
	}
	public void setDn(DistinguishedName dn) {
		this.dn = dn;
	}
	public String getSerUuid() {
		return serUuid;
	}
	public LdapSerInfoPo setSerUuid(String serUuid) {
		this.serUuid = serUuid;
		return this;
	}
	public String getSerNo() {
		return serNo;
	}
	public LdapSerInfoPo setSerNo(String serNo) {
		this.serNo = serNo;
		return this;
	}
	public String getSerName() {
		return serName;
	}
	public LdapSerInfoPo setSerName(String serName) {
		this.serName = serName;
		return this;
	}
	public String getSerAddr() {
		return serAddr;
	}
	public LdapSerInfoPo setSerAddr(String serAddr) {
		this.serAddr = serAddr;
		return this;
	}
	public Integer getSerPort() {
		return serPort;
	}
	public LdapSerInfoPo setSerPort(Integer serPort) {
		this.serPort = serPort;
		return this;
	}
	public String getSerPwd() {
		return serPwd;
	}
	public LdapSerInfoPo setSerPwd(String serPwd) {
		this.serPwd = serPwd;
		return this;
	}
	public Integer getSerType() {
		return serType;
	}
	public LdapSerInfoPo setSerType(Integer serType) {
		this.serType = serType;
		return this;
	}
	public Integer getSerPro() {
		return serPro;
	}
	public LdapSerInfoPo setSerPro(Integer serPro) {
		this.serPro = serPro;
		return this;
	}
	public String getSerNode() {
		return serNode;
	}
	public LdapSerInfoPo setSerNode(String serNode) {
		this.serNode = serNode;
		return this;
	}
	public String getSerFactInfo() {
		return serFactInfo;
	}
	public LdapSerInfoPo setSerFactInfo(String serFactInfo) {
		this.serFactInfo = serFactInfo;
		return this;
	}
	
}

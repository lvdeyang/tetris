package com.suma.application.ldap.equip.po;

import org.springframework.ldap.core.DistinguishedName;

public class LdapEquipPo {
	
	private DistinguishedName dn;
	private String equipUuid;
	private String equipNo;
	private String equipName;
	private String equipAddr;
	private Integer equipPort;
	private Integer equipType;
	private String equipPwd;
	private String equipOrg;
	private String equipNode;
	private String equipFactInfo;
	
	public DistinguishedName getDn() {
		return dn;
	}
	public void setDn(DistinguishedName dn) {
		this.dn = dn;
	}
	public String getEquipNo() {
		return equipNo;
	}
	public LdapEquipPo setEquipNo(String equipNo) {
		this.equipNo = equipNo;
		return this;
	}
	public String getEquipAddr() {
		return equipAddr;
	}
	public LdapEquipPo setEquipAddr(String equipAddr) {
		this.equipAddr = equipAddr;
		return this;
	}
	public Integer getEquipType() {
		return equipType;
	}
	public LdapEquipPo setEquipType(Integer equipType) {
		this.equipType = equipType;
		return this;
	}
	public String getEquipOrg() {
		return equipOrg;
	}
	public LdapEquipPo setEquipOrg(String equipOrg) {
		this.equipOrg = equipOrg;
		return this;
	}
	public String getEquipNode() {
		return equipNode;
	}
	public LdapEquipPo setEquipNode(String equipNode) {
		this.equipNode = equipNode;
		return this;
	}
	public String getEquipFactInfo() {
		return equipFactInfo;
	}
	public LdapEquipPo setEquipFactInfo(String equipFactInfo) {
		this.equipFactInfo = equipFactInfo;
		return this;
	}
	public String getEquipUuid() {
		return equipUuid;
	}
	public LdapEquipPo setEquipUuid(String equipUuid) {
		this.equipUuid = equipUuid;
		return this;
	}
	public String getEquipName() {
		return equipName;
	}
	public LdapEquipPo setEquipName(String equipName) {
		this.equipName = equipName;
		return this;
	}
	public Integer getEquipPort() {
		return equipPort;
	}
	public LdapEquipPo setEquipPort(Integer equipPort) {
		this.equipPort = equipPort;
		return this;
	}
	public String getEquipPwd() {
		return equipPwd;
	}
	public LdapEquipPo setEquipPwd(String equipPwd) {
		this.equipPwd = equipPwd;
		return this;
	}

}

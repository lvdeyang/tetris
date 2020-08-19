package com.suma.application.ldap.user.po;

import org.springframework.ldap.core.DistinguishedName;

public class LdapUserPo {

	private DistinguishedName dn;
	private String userUuid;
	private String userNo;
	private String userName;
	private String userAccount;
	private String userPwd;
	private Integer userLevel;
	private Integer userType;
	private String userOrg;
	private String userNode;
	private String userFactInfo;
	
	public DistinguishedName getDn() {
		return dn;
	}
	public void setDn(DistinguishedName dn) {
		this.dn = dn;
	}
	public String getUserUuid() {
		return userUuid;
	}
	public LdapUserPo setUserUuid(String userUuid) {
		this.userUuid = userUuid;
		return this;
	}
	public String getUserNo() {
		return userNo;
	}
	public LdapUserPo setUserNo(String userNo) {
		this.userNo = userNo;
		return this;
	}
	public Integer getUserType() {
		return userType;
	}
	public LdapUserPo setUserType(Integer userType) {
		this.userType = userType;
		return this;
	}
	public String getUserNode() {
		return userNode;
	}
	public LdapUserPo setUserNode(String userNode) {
		this.userNode = userNode;
		return this;
	}
	public String getUserOrg() {
		return userOrg;
	}
	public LdapUserPo setUserOrg(String userOrg) {
		this.userOrg = userOrg;
		return this;
	}
	public String getUserName() {
		return userName;
	}
	public LdapUserPo setUserName(String userName) {
		this.userName = userName;
		return this;
	}
	public String getUserAccount() {
		return userAccount;
	}
	public LdapUserPo setUserAccount(String userAccount) {
		this.userAccount = userAccount;
		return this;
	}
	public String getUserPwd() {
		return userPwd;
	}
	public LdapUserPo setUserPwd(String userPwd) {
		this.userPwd = userPwd;
		return this;
	}
	public Integer getUserLevel() {
		return userLevel;
	}
	public LdapUserPo setUserLevel(Integer userLevel) {
		this.userLevel = userLevel;
		return this;
	}
	public String getUserFactInfo() {
		return userFactInfo;
	}
	public LdapUserPo setUserFactInfo(String userFactInfo) {
		this.userFactInfo = userFactInfo;
		return this;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userUuid == null) ? 0 : userUuid.hashCode());
		result = prime * result + ((userOrg == null) ? 0 : userOrg.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LdapUserPo other = (LdapUserPo) obj;
		if (userUuid == null) {
			if (other.userUuid != null)
				return false;
		} else if (!userUuid.equals(other.userUuid))
			return false;
		if (userOrg == null) {
			if (other.userOrg != null)
				return false;
		} else if (!userOrg.equals(other.userOrg))
			return false;
		return true;
	}
}

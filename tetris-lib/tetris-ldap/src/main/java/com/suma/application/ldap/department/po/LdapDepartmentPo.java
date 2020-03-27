package com.suma.application.ldap.department.po;

import org.springframework.ldap.core.DistinguishedName;

public class LdapDepartmentPo {
	private DistinguishedName dn;
	private String orgUuid;
	private String orgName;
	private String orgRelation;
	private String orgCmdRelation;
	private String orgFactInfo;
	
	public DistinguishedName getDn() {
		return dn;
	}
	public LdapDepartmentPo setDn(DistinguishedName dn) {
		this.dn = dn;
		return this;
	}
	public String getOrgUuid() {
		return orgUuid;
	}
	public LdapDepartmentPo setOrgUuid(String orgUuid) {
		this.orgUuid = orgUuid;
		return this;
	}
	public String getOrgName() {
		return orgName;
	}
	public LdapDepartmentPo setOrgName(String orgName) {
		this.orgName = orgName;
		return this;
	}
	public String getOrgRelation() {
		return orgRelation;
	}
	public LdapDepartmentPo setOrgRelation(String orgRelation) {
		this.orgRelation = orgRelation;
		return this;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof LdapDepartmentPo){
			LdapDepartmentPo depart = (LdapDepartmentPo)obj;
			if(this.getOrgUuid() != null){
				if(this.getOrgUuid().equals(depart.getOrgUuid())){
					return true;
				}
			}
		}
		return false;
	}
	public String getOrgCmdRelation() {
		return orgCmdRelation;
	}
	public LdapDepartmentPo setOrgCmdRelation(String orgCmdRelation) {
		this.orgCmdRelation = orgCmdRelation;
		return this;
	}
	public String getOrgFactInfo() {
		return orgFactInfo;
	}
	public LdapDepartmentPo setOrgFactInfo(String orgFactInfo) {
		this.orgFactInfo = orgFactInfo;
		return this;
	}
}

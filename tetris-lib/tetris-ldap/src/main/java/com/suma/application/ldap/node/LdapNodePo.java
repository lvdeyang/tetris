package com.suma.application.ldap.node;

import org.springframework.ldap.core.DistinguishedName;

public class LdapNodePo {
	
	private DistinguishedName dn;
	private String nodeUuid;
	private String nodeName;
	private String nodeFather;
	private String nodeRelations;
	private String nodeFactInfo;
	
	public DistinguishedName getDn() {
		return dn;
	}
	public void setDn(DistinguishedName dn) {
		this.dn = dn;
	}
	public String getNodeUuid() {
		return nodeUuid;
	}
	public LdapNodePo setNodeUuid(String nodeUuid) {
		this.nodeUuid = nodeUuid;
		return this;
	}
	public String getNodeName() {
		return nodeName;
	}
	public LdapNodePo setNodeName(String nodeName) {
		this.nodeName = nodeName;
		return this;
	}
	public String getNodeFather() {
		return nodeFather;
	}
	public LdapNodePo setNodeFather(String nodeFather) {
		this.nodeFather = nodeFather;
		return this;
	}
	public String getNodeRelations() {
		return nodeRelations;
	}
	public LdapNodePo setNodeRelations(String nodeRelations) {
		this.nodeRelations = nodeRelations;
		return this;
	}
	public String getNodeFactInfo() {
		return nodeFactInfo;
	}
	public LdapNodePo setNodeFactInfo(String nodeFactInfo) {
		this.nodeFactInfo = nodeFactInfo;
		return this;
	}

}

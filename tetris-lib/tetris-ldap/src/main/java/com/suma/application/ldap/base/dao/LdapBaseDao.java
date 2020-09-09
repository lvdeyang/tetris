package com.suma.application.ldap.base.dao;

import java.util.LinkedList;

import org.springframework.ldap.core.DistinguishedName;

import com.suma.application.ldap.contants.LdapContants;

public class LdapBaseDao {

	protected DistinguishedName getBaseDN(){
		DistinguishedName baseDN = new DistinguishedName("");
		baseDN.add("ou", "org");
		return baseDN;
	}
	
	protected DistinguishedName getBaseDN(String key,String value){
		DistinguishedName baseDN = new DistinguishedName("");
		baseDN.add(key, value);
		return baseDN;
	}
	
	protected DistinguishedName getDepartmentDN(String myDN, LinkedList<String> parentDNs){
		DistinguishedName departmentDN= getBaseDN();
		for(String orgNo : parentDNs){
			if(!LdapContants.DEPARTROOTRELATION.equals(orgNo)){
				departmentDN.add("orgNo", orgNo);
			}
		}
		departmentDN.add("orgNo", myDN);
		return departmentDN;
	}
}

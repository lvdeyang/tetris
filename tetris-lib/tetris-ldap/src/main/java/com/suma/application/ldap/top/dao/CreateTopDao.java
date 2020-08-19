/**  
 * @Title: CreateTopDao.java
 * @Package com.suma.application.ldap.top.dao
 * @Description: TODO
 * @author yokoboy
 * @date 2016年3月9日
 */
package com.suma.application.ldap.top.dao;

import java.util.List;

import javax.naming.Name;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Repository;

import com.suma.application.ldap.base.dao.LdapBaseDao;
import com.suma.application.ldap.department.po.LdapDepartmentPo;

/**
 * ClassName: CreateTopDao 
 * @Description: TODO
 * @date 2016年3月9日
 */
@Repository("com.suma.application.ldap.top.dao.CreateTopDao")
public class CreateTopDao extends LdapBaseDao{
	@Autowired
	private LdapTemplate ldapTemplate;
	@SuppressWarnings("unchecked")
	public void createTopDepartNode(){
		//当前是否存在ou = org 节点，如果不存在则添加
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", "top"));
		filter.and(new EqualsFilter("ou", "org"));
		List<Name> dNames = ldapTemplate.search("", filter.encode(), new ContextMapper(){
			@Override
			public Object mapFromContext(Object ctx) {
				DirContextAdapter context = (DirContextAdapter)ctx;
				
				return context.getDn();
			}
		});
		if(dNames.size()<=0){
			BasicAttribute ocattr = new BasicAttribute("objectClass");
			ocattr.add("top");
			ocattr.add("organizationalUnit");
			Attributes attrs = new BasicAttributes();
			attrs.put(ocattr);
			ldapTemplate.bind(this.getBaseDN("ou","org"), null, attrs);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void createTopUserNode(){
		//当前是否存在ou = user 节点，如果不存在则添加
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", "top"));
		filter.and(new EqualsFilter("ou", "user"));
		List<Name> dNames = ldapTemplate.search("", filter.encode(), new ContextMapper(){
			@Override
			public Object mapFromContext(Object ctx) {
				DirContextAdapter context = (DirContextAdapter)ctx;
				
				return context.getDn();
			}
		});
		if(dNames.size()<=0){
			BasicAttribute ocattr = new BasicAttribute("objectclass");
			ocattr.add("top");
			ocattr.add("organizationalUnit");
			Attributes attrs = new BasicAttributes();
			attrs.put(ocattr);
			ldapTemplate.bind(this.getBaseDN("ou", "user"), null, attrs);
		}
	
	}
	@SuppressWarnings("unchecked")
	public void createTopEquipeNode(){
		//当前是否存在ou = user 节点，如果不存在则添加
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectClass", "top"));
		filter.and(new EqualsFilter("ou", "equip"));
		List<Name> dNames = ldapTemplate.search("", filter.encode(), new ContextMapper(){
				@Override
				public Object mapFromContext(Object ctx) {
					DirContextAdapter context = (DirContextAdapter)ctx;
					return context.getDn();
				}
		});
		if(dNames.size()<=0){
			BasicAttribute ocattr = new BasicAttribute("objectClass");
			ocattr.add("top");
			ocattr.add("organizationalUnit");
			Attributes attrs = new BasicAttributes();
			attrs.put(ocattr);
			ldapTemplate.bind(this.getBaseDN("ou", "equip"), null, attrs);
		 }
	}

}

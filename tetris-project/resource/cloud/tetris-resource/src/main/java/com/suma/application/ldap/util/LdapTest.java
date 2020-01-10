package com.suma.application.ldap.util;

import java.util.List;

import javax.naming.Name;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;

import com.suma.application.ldap.department.po.LdapDepartmentPo;
import com.suma.application.ldap.user.dao.LdapUserDao;
import com.suma.application.ldap.user.po.LdapUserPo;

public class LdapTest {

	public static void main(String[] args) {

		System.out.println("in main");

		@SuppressWarnings("resource")
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring/ldap.xml");
		BeanFactory factory = (BeanFactory) ac;
		
		LdapTemplate ldapTemplate = factory.getBean(LdapTemplate.class);

		LdapUserDao ldapUserDao = factory.getBean(LdapUserDao.class);

		List<LdapUserPo> ldapUserPoList = ldapUserDao.queryAllUsers();

		for (LdapUserPo ldapUserPo : ldapUserPoList) {
			System.out.println(ldapUserPo.toString());
		}

		// 测试添加一个ou
		// 当前是否存在ou = departinfo 节点，如果不存在则添加
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", "top"));
		filter.and(new EqualsFilter("ou", "departInfo"));
		List<Name> dNames = ldapTemplate.search("", filter.encode(), new ContextMapper() {
			@Override
			public Object mapFromContext(Object ctx) {
				DirContextAdapter context = (DirContextAdapter) ctx;

				return context.getDn();
			}
		});

		if (dNames.size() <= 0) {
			BasicAttribute ocattr = new BasicAttribute("objectclass");
			ocattr.add("top");
			ocattr.add("organizationalUnit");
			Attributes attrs = new BasicAttributes();
			attrs.put(ocattr);
			//
			DistinguishedName departInfoBaseDN = new DistinguishedName("");
			departInfoBaseDN.add("ou", "departInfo");

			ldapTemplate.bind(departInfoBaseDN, null, attrs);
		}
		
		LdapDepartmentPo ldapDepart = new LdapDepartmentPo();
		ldapDepart.setOrgUuid("aaaaa-cccc");
		ldapDepart.setOrgName("组织2");
		ldapDepart.setOrgRelation("1111");
		ldapDepart.setOrgCmdRelation("22222");
		ldapDepart.setOrgFactInfo("null");
		DistinguishedName baseDN2 = new DistinguishedName("");
		baseDN2.add("ou", "departInfo");
		baseDN2.add("orgUuid", ldapDepart.getOrgUuid());
		Attributes attrs = getAttributesByDepartment(ldapDepart);
		ldapTemplate.bind(baseDN2, null, attrs);
		

		// 测试添加testou下的一条数据
//		LdapUserPo userPo = new LdapUserPo();
//		userPo.setSumaUserId("21");
//		userPo.setUserAccount("22");
//		userPo.setUserNO("23");
//		userPo.setUserNode("24");
//		userPo.setUserLevel("25");
//		userPo.setUserOrg("16");
//		userPo.setUserType("17");
//		userPo.setUserPassword("28");
//		@SuppressWarnings("deprecation")
//		DistinguishedName baseDN2 = new DistinguishedName("");
//		baseDN2.add("ou", "testou");
//		baseDN2.add("sumaUserId", userPo.getSumaUserId());
//		Attributes attrs = getUserAttributes(userPo);
//		ldapTemplate.bind(baseDN2, null, attrs);

	}

//	private static Attributes getUserAttributes(LdapUserPo user) {
//		BasicAttribute ocattr = new BasicAttribute("objectClass");
//		ocattr.add("top");
//		ocattr.add("userInfo");
//		Attributes attrs = new BasicAttributes();
//		attrs.put(ocattr);
//		attrs.put("userNo", user.getUserNO());
//		attrs.put("userAccount", user.getUserAccount());
//		attrs.put("userLevel", user.getUserLevel());
//		attrs.put("userType", user.getUserType());
//		attrs.put("userPassword", "7");
//		attrs.put("userOrg", user.getUserOrg());
//		attrs.put("userNode", user.getUserNode());
//		attrs.put("sumaUserId", user.getSumaUserId());
//		return attrs;
//	}

	protected static DistinguishedName getBaseDN(String key, String value) {
		DistinguishedName baseDN = new DistinguishedName("");
		baseDN.add(key, value);
		return baseDN;
	}

	private static Attributes getAttributesByDepartment(LdapDepartmentPo department){
		BasicAttribute ocattr = new BasicAttribute("objectClass");
		ocattr.add("departInfo");
		Attributes attrs = new BasicAttributes();
		attrs.put(ocattr);
		attrs.put("orgUuid",department.getOrgUuid());
		attrs.put("orgName", department.getOrgName());
		attrs.put("orgRelation", department.getOrgRelation());
		attrs.put("orgCmdRelation", department.getOrgCmdRelation());
		attrs.put("orgFactInfo", department.getOrgFactInfo());
		return attrs;
	}
}

package com.suma.application.ldap.user.dao;

import java.util.List;

import javax.naming.Name;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Service;

import com.suma.application.ldap.base.dao.LdapBaseDao;
import com.suma.application.ldap.user.po.LdapUserPo;

@Service
public class LdapUserDao extends LdapBaseDao {

	@Autowired
	private LdapTemplate ldapTemplate;

	// 查询所有的帐号
	@SuppressWarnings("unchecked")
	public List<LdapUserPo> queryAllUsers() {
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", "userInfo"));
		return ldapTemplate.search(getBaseDN("ou", "userInfo"), filter.encode(), new ContextMapper() {
			@Override
			public Object mapFromContext(Object ctx) {
				DirContextAdapter context = (DirContextAdapter) ctx;
				return getLdapUserPO(context);
			}

		});
	}

	public void save(LdapUserPo ldapUser) {
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", "top"));
		filter.and(new EqualsFilter("ou", "userInfo"));
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
			DistinguishedName departInfoBaseDN = getBaseDN("ou", "userInfo");
			ldapTemplate.bind(departInfoBaseDN, null, attrs);
		}

		DistinguishedName baseDN2 = getBaseDN("ou", "userInfo");
		baseDN2.add("userUuid", ldapUser.getUserUuid());
		Attributes attrs = getUserAttributes(ldapUser);
		ldapTemplate.bind(baseDN2, null, attrs);
	}

	// 根据id获取一个帐号
	@SuppressWarnings("unchecked")
	public List<LdapUserPo> getUserByUuid(String userUuid) {
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", "userInfo"));
		filter.and(new EqualsFilter("userUuid", userUuid));
		return ldapTemplate.search(getBaseDN("ou", "userInfo"), filter.encode(), new ContextMapper() {
			@Override
			public Object mapFromContext(Object ctx) {
				DirContextAdapter context = (DirContextAdapter) ctx;
				LdapUserPo ldapUserPo = getLdapUserPO(context);
				return ldapUserPo;
			}
		});
	}
	
	// 根据服务类型批量查询
	@SuppressWarnings("unchecked")
	public List<LdapUserPo> getUserByFactInfo(String factInfo) {
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", "userInfo"));
		filter.and(new EqualsFilter("userFactInfo", factInfo));
		return ldapTemplate.search(getBaseDN("ou", "userInfo"), filter.encode(), new ContextMapper() {
			@Override
			public Object mapFromContext(Object ctx) {
				DirContextAdapter context = (DirContextAdapter) ctx;
				LdapUserPo ldapUserPo = getLdapUserPO(context);
				return ldapUserPo;
			}
		});
	}

	// 删除一个账户 返回删除的user
	public LdapUserPo remove(LdapUserPo user) {
		List<LdapUserPo> oldUserList = getUserByUuid(user.getUserUuid());
		if (oldUserList == null || oldUserList.size() <= 0) {
			return null;
		} else {
			user = oldUserList.get(0);
			ldapTemplate.unbind(user.getDn());
			return user;
		}
	}
	
	//批量删除
	public void removeAll(List<LdapUserPo> users){
		for(LdapUserPo user: users){
			ldapTemplate.unbind(user.getDn());
		}
	}

	// 不移动节点
	public void update(LdapUserPo user) {
		LdapUserPo oldser = getUserByUuid(user.getUserUuid()).get(0);
		ModificationItem[] mods = null;
		if (user.getUserOrg() != null) {
			mods = new ModificationItem[8];
			mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
					new BasicAttribute("userNo", user.getUserNo()));
			mods[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
					new BasicAttribute("userName", user.getUserName()));
			mods[2] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
					new BasicAttribute("userAccount", user.getUserAccount()));
			mods[3] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
					new BasicAttribute("userPwd", user.getUserPwd()));
			mods[4] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
					new BasicAttribute("userLevel", user.getUserLevel().toString()));
			mods[5] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
					new BasicAttribute("userType", user.getUserType().toString()));
			mods[6] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
					new BasicAttribute("userOrg", user.getUserOrg()));
			mods[7] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
					new BasicAttribute("userNode", user.getUserNode()));

		} else {
			mods = new ModificationItem[7];
			mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
					new BasicAttribute("userNo", user.getUserNo()));
			mods[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
					new BasicAttribute("userName", user.getUserName()));
			mods[2] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
					new BasicAttribute("userAccount", user.getUserAccount()));
			mods[3] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
					new BasicAttribute("userPwd", user.getUserPwd()));
			mods[4] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
					new BasicAttribute("userLevel", user.getUserLevel().toString()));
			mods[5] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
					new BasicAttribute("userType", user.getUserType().toString()));
			mods[6] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
					new BasicAttribute("userNode", user.getUserNode()));
		}
		ldapTemplate.modifyAttributes(oldser.getDn(), mods);
	}

	// 查询某一组织下的所有帐号
//	@SuppressWarnings("unchecked")
//	public List<LdapUserPo> getDepartChildrenUser(String departOrgNo) {
//		AndFilter filter = new AndFilter();
//		filter.and(new EqualsFilter("objectclass", "userInfo"));
//		filter.and(new EqualsFilter("userOrg", departOrgNo));
//		return ldapTemplate.search(getBaseDN("ou", "user"), filter.encode(),new ContextMapper(){
//			@Override
//			public Object mapFromContext(Object ctx) {
//				DirContextAdapter context = (DirContextAdapter)ctx;
//				LdapUserPo userPO = new LdapUserPo();
//				userPO.setDn(new DistinguishedName(context.getDn()));
//				userPO.setUserAccount(context.getStringAttribute("userAccount"));
//				userPO.setUserLevel(context.getStringAttribute("userLevel"));
//				userPO.setSumaUserId(context.getStringAttribute("sumaUserId"));
//				userPO.setUserNO(context.getStringAttribute("userNO"));
//				userPO.setUserNode(context.getStringAttribute("userNode"));
//				userPO.setUserOrg(context.getStringAttribute("userOrg"));
//				userPO.setUserPassword(new String((byte[])context.getObjectAttribute("userPassword")));
//				userPO.setUserType(context.getStringAttribute("userType"));
//				return userPO;
//			}
//		});
//	}

	// 根据用户名和密码获取一个账号
//	@SuppressWarnings("unchecked")
//	public List<LdapUserPo> getUserByNameAndPwd(String userName, String password){
//		AndFilter filter = new AndFilter();
//		filter.and(new EqualsFilter("objectclass", "userInfo"));
//		filter.and(new EqualsFilter("userAccount", userName));
//		filter.and(new EqualsFilter("userPassword", Md5Utils.getOpenLDAPMD5(password)));
//		return ldapTemplate.search(getBaseDN("ou", "user"), filter.encode(),new ContextMapper(){
//			@Override
//			public Object mapFromContext(Object ctx) {
//				DirContextAdapter context = (DirContextAdapter)ctx;
//				LdapUserPo userPO = new LdapUserPo();
//				userPO.setDn(new DistinguishedName(context.getDn()));
//				userPO.setUserAccount(context.getStringAttribute("userAccount"));
//				userPO.setUserLevel(context.getStringAttribute("userLevel"));
//				userPO.setSumaUserId(context.getStringAttribute("sumaUserId"));
//				userPO.setUserNO(context.getStringAttribute("userNO"));
//				userPO.setUserNode(context.getStringAttribute("userNode"));
//				userPO.setUserOrg(context.getStringAttribute("userOrg"));
//				userPO.setUserPassword(new String((byte[])context.getObjectAttribute("userPassword")));
//				userPO.setUserType(context.getStringAttribute("userType"));
//				return userPO;
//			}
//		});
//	}

	// 根据id获取一个帐号
//	@SuppressWarnings("unchecked")
//	public List<LdapUserPo> getUserById(String userId){
//		AndFilter filter = new AndFilter();
//		filter.and(new EqualsFilter("objectclass", "userInfo"));
//		filter.and(new EqualsFilter("sumaUserId", userId));
//		return ldapTemplate.search(getBaseDN("ou", "user"), filter.encode(),new ContextMapper(){
//			@Override
//			public Object mapFromContext(Object ctx) {
//				DirContextAdapter context = (DirContextAdapter)ctx;
//				LdapUserPo userPO = new LdapUserPo();
//				userPO.setDn(new DistinguishedName(context.getDn()));
//				userPO.setUserAccount(context.getStringAttribute("userAccount"));
//				userPO.setUserLevel(context.getStringAttribute("userLevel"));
//				userPO.setSumaUserId(context.getStringAttribute("sumaUserId"));
//				userPO.setUserNO(context.getStringAttribute("userNO"));
//				userPO.setUserNode(context.getStringAttribute("userNode"));
//				userPO.setUserOrg(context.getStringAttribute("userOrg"));
//				userPO.setUserPassword(new String((byte[])context.getObjectAttribute("userPassword")));
//				userPO.setUserType(context.getStringAttribute("userType"));
//				return userPO;
//			}
//		});
//	}

	// 插入一个用户
//	public void save(LdapUserPo user,DistinguishedName newParentDN){
//		newParentDN.add("sumaUserId",user.getSumaUserId());
//		Attributes attrs = getUserAttributes(user);
//		ldapTemplate.bind(newParentDN, null, attrs);
//	}

	// 删除一个账户 返回删除的user
//	public LdapUserPo remove(LdapUserPo user){
//		List<LdapUserPo> oldUserList = getUserById(user.getSumaUserId());
//		if(oldUserList==null || oldUserList.size()<=0){
//			return null;
//		}else{
//			user = oldUserList.get(0);
//			ldapTemplate.unbind(user.getDn());
//			return user;
//		}
//	}

	// 修改用户密码
//	public LdapUserPo changeUserPassword(LdapUserPo user){
//		LdapUserPo oldser = getUserById(user.getSumaUserId()).get(0);
//		ModificationItem[] mods = new ModificationItem[1];
//		mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,new BasicAttribute("userPassword",Md5Utils.getOpenLDAPMD5(user.getUserPassword())));
//		ldapTemplate.modifyAttributes(oldser.getDn(), mods);
//		return getUserById(user.getSumaUserId()).get(0);
//	}

	// 不移动节点
//	public void update(LdapUserPo user){
//		LdapUserPo oldser = getUserById(user.getSumaUserId()).get(0);
//		ModificationItem[] mods = null;
//		if(user.getUserOrg() != null){
//			mods = new ModificationItem[6];
//			mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,new BasicAttribute("userNo",user.getUserNO()));
//			mods[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,new BasicAttribute("userAccount",user.getUserAccount()));
//			mods[2] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,new BasicAttribute("userType",user.getUserType()));
//			mods[3] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,new BasicAttribute("userLevel",user.getUserLevel()));
//			mods[4] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,new BasicAttribute("userNode",user.getUserNode()));
//			mods[5] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,new BasicAttribute("userOrg",user.getUserOrg()));
//		}else{
//			mods = new ModificationItem[5];
//			mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,new BasicAttribute("userNo",user.getUserNO()));
//			mods[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,new BasicAttribute("userAccount",user.getUserAccount()));
//			mods[2] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,new BasicAttribute("userType",user.getUserType()));
//			mods[3] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,new BasicAttribute("userLevel",user.getUserLevel()));
//			mods[4] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,new BasicAttribute("userNode",user.getUserNode()));
//		}
//		ldapTemplate.modifyAttributes(oldser.getDn(), mods);
//	}

	// 移动节点 newParent:新的父节点
//	public void update(LdapUserPo user,DistinguishedName newParentDN){
//		LdapUserPo oldUser = new LdapUserPo();
//		oldUser.setSumaUserId(user.getSumaUserId());
//		oldUser = remove(oldUser);
//		user.setUserPassword(oldUser.getUserPassword());
//		save(user,newParentDN);
//	}

	/************************************
	 * 私有方法
	 ************************************/
	// 创建user的属性
	private Attributes getUserAttributes(LdapUserPo user) {
		BasicAttribute ocattr = new BasicAttribute("objectClass");
		ocattr.add("userInfo");
		Attributes attrs = new BasicAttributes();
		attrs.put(ocattr);
		attrs.put("userUuid", user.getUserUuid());
		attrs.put("userNo", user.getUserNo());
		attrs.put("userName", user.getUserName());
		attrs.put("userAccount", user.getUserAccount());
//		attrs.put("userPwd", Md5Utils.getOpenLDAPMD5(user.getUserPwd()));
		attrs.put("userPwd", user.getUserPwd());
		attrs.put("userLevel", String.valueOf(user.getUserLevel()));
		attrs.put("userType", String.valueOf(user.getUserType()));
		attrs.put("userOrg", user.getUserOrg());
		attrs.put("userNode", user.getUserNode());
		attrs.put("userFactInfo", user.getUserFactInfo());
		return attrs;
	}

	private LdapUserPo getLdapUserPO(DirContextAdapter context) {
		LdapUserPo userPO = new LdapUserPo();
		userPO.setDn(new DistinguishedName(context.getDn()));
		userPO.setUserUuid((context.getStringAttribute("userUuid")));
		userPO.setUserNo(context.getStringAttribute("userNo"));
		userPO.setUserName(context.getStringAttribute("userName"));
		userPO.setUserAccount(context.getStringAttribute("userAccount"));
		userPO.setUserPwd(context.getStringAttribute("userPwd"));
//		userPO.setUserPwd(new String((byte[])context.getObjectAttribute("userPwd")));
		userPO.setUserLevel(Integer.valueOf(context.getStringAttribute("userLevel")));
		userPO.setUserType(Integer.valueOf(context.getStringAttribute("userType")));
		userPO.setUserOrg(context.getStringAttribute("userOrg"));
		userPO.setUserNode(context.getStringAttribute("userNode"));
		userPO.setUserFactInfo(context.getStringAttribute("userFactInfo"));
		return userPO;
	}
}

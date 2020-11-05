package com.suma.application.ldap.ser;

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
import com.suma.application.ldap.department.po.LdapDepartmentPo;
import com.suma.application.ldap.user.po.LdapUserPo;

@Service
public class LdapSerInfoDao extends LdapBaseDao {

	@Autowired
	private LdapTemplate ldapTemplate;

	// 查询所有的帐号
	@SuppressWarnings("unchecked")
	public List<LdapSerInfoPo> queryAll() {
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", "serInfo"));
		return ldapTemplate.search(getBaseDN("ou", "serInfo"), filter.encode(), new ContextMapper() {
			@Override
			public Object mapFromContext(Object ctx) {
				DirContextAdapter context = (DirContextAdapter) ctx;
				return getLdapSerInfoPO(context);
			}

		});
	}

	public void save(LdapSerInfoPo ldapSerInfo) {
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", "top"));
		filter.and(new EqualsFilter("ou", "serInfo"));
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
			DistinguishedName departInfoBaseDN = getBaseDN("ou", "serInfo");
			ldapTemplate.bind(departInfoBaseDN, null, attrs);
		}

		DistinguishedName baseDN2 = getBaseDN("ou", "serInfo");
		baseDN2.add("serUuid ", ldapSerInfo.getSerUuid());
		Attributes attrs = getAttributes(ldapSerInfo);
		ldapTemplate.bind(baseDN2, null, attrs);
	}

	// 修改SerInfo信息
	public void update(LdapSerInfoPo ldapSerInfoPo) {

		LdapSerInfoPo oldSerInfo = getSerInfoByUuid(ldapSerInfoPo.getSerUuid()).get(0);
		ModificationItem[] mods = null;
		mods = new ModificationItem[9];
		mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("serNo", ldapSerInfoPo.getSerNo()));
		mods[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("serName", ldapSerInfoPo.getSerName()));
		mods[2] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("serAddr", ldapSerInfoPo.getSerAddr()));
		mods[3] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("serPort", String.valueOf(ldapSerInfoPo.getSerPort())));
		mods[4] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("serPwd", ldapSerInfoPo.getSerPwd()));
		mods[5] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("serType",String.valueOf(ldapSerInfoPo.getSerType())));
		mods[6] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("serPro", String.valueOf(ldapSerInfoPo.getSerPro())));
		mods[7] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("serNode", ldapSerInfoPo.getSerNode()));
		mods[8] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("serFactInfo", ldapSerInfoPo.getSerFactInfo()));
		ldapTemplate.modifyAttributes(oldSerInfo.getDn(), mods);
	}

	// 根据uuid获取一个SerInfo
	@SuppressWarnings("unchecked")
	public List<LdapSerInfoPo> getSerInfoByUuid(String uuid) {
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", "serInfo"));
		filter.and(new EqualsFilter("serUuid", uuid));
		return ldapTemplate.search(getBaseDN("ou", "serInfo"), filter.encode(), new ContextMapper() {
			@Override
			public Object mapFromContext(Object ctx) {
				DirContextAdapter context = (DirContextAdapter) ctx;

				LdapSerInfoPo ldapSerInfoPo = new LdapSerInfoPo();
				ldapSerInfoPo.setDn(new DistinguishedName(context.getDn()));
				ldapSerInfoPo.setSerUuid(context.getStringAttribute("serUuid"));
				ldapSerInfoPo.setSerNo(context.getStringAttribute("serNo"));
				ldapSerInfoPo.setSerName(context.getStringAttribute("serName"));
				ldapSerInfoPo.setSerAddr(context.getStringAttribute("serAddr"));
				ldapSerInfoPo.setSerPort(Integer.valueOf(context.getStringAttribute("serPort")));
				ldapSerInfoPo.setSerPwd(context.getStringAttribute("serPwd"));
				ldapSerInfoPo.setSerType(Integer.valueOf(context.getStringAttribute("serType")));
				ldapSerInfoPo.setSerPro(Integer.valueOf(context.getStringAttribute("serPro")));
				ldapSerInfoPo.setSerNode(context.getStringAttribute("serNode"));
				ldapSerInfoPo.setSerFactInfo(context.getStringAttribute("serFactInfo"));
				return ldapSerInfoPo;
			}
		});
	}

	// 删除一个serInfo 返回删除的serInfo
	public LdapSerInfoPo remove(LdapSerInfoPo ldapSerInfoPo) {
		List<LdapSerInfoPo> oldSerInfoList = getSerInfoByUuid(ldapSerInfoPo.getSerUuid());
		if (oldSerInfoList == null || oldSerInfoList.size() <= 0) {
			return null;
		} else {
			ldapSerInfoPo = oldSerInfoList.get(0);
			ldapTemplate.unbind(ldapSerInfoPo.getDn());
			return ldapSerInfoPo;
		}
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
	// 创建属性
	private Attributes getAttributes(LdapSerInfoPo ser) {
		BasicAttribute ocattr = new BasicAttribute("objectClass");
		ocattr.add("serInfo");
		Attributes attrs = new BasicAttributes();
		attrs.put(ocattr);
		attrs.put("serUuid", ser.getSerUuid());
		attrs.put("serNo", ser.getSerNo());
		attrs.put("serName", ser.getSerName());
		attrs.put("serAddr", ser.getSerAddr());
		attrs.put("serPort", String.valueOf(ser.getSerPort()));
		attrs.put("serPwd", ser.getSerPwd());
		attrs.put("serType", String.valueOf(ser.getSerType()));
		attrs.put("serPro", String.valueOf(ser.getSerPro()));
		attrs.put("serNode", ser.getSerNode());
		attrs.put("serFactInfo", ser.getSerFactInfo());

		return attrs;
	}

	private LdapSerInfoPo getLdapSerInfoPO(DirContextAdapter context) {
		LdapSerInfoPo serInfo = new LdapSerInfoPo();
		serInfo.setDn(new DistinguishedName(context.getDn()));
		serInfo.setSerUuid(context.getStringAttribute("serUuid"));
		serInfo.setSerNo(context.getStringAttribute("serNo"));
		serInfo.setSerName(context.getStringAttribute("serName"));
		serInfo.setSerAddr(context.getStringAttribute("serAddr"));
		serInfo.setSerPort(Integer.valueOf(context.getStringAttribute("serPort")));
		serInfo.setSerPwd(context.getStringAttribute("serPwd"));
		serInfo.setSerType(Integer.valueOf(context.getStringAttribute("serType")));
		serInfo.setSerPro(Integer.valueOf(context.getStringAttribute("serPro")));
		serInfo.setSerNode(context.getStringAttribute("serNode"));
		serInfo.setSerFactInfo(context.getStringAttribute("serFactInfo"));
		return serInfo;
	}
}

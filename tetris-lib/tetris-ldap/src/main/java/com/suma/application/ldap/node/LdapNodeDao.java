package com.suma.application.ldap.node;

import java.util.List;

import javax.naming.Name;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.control.PagedResult;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Service;

import com.suma.application.ldap.base.dao.LdapBaseDao;

@Service
public class LdapNodeDao extends LdapBaseDao {

	@Autowired
	private LdapTemplate ldapTemplate;

	// 查询所有的帐号
	@SuppressWarnings("unchecked")
	public List<LdapNodePo> queryAll() {
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", "nodeInfo"));
		return ldapTemplate.search(getBaseDN("ou", "nodeInfo"), filter.encode(), new ContextMapper() {
			@Override
			public Object mapFromContext(Object ctx) {
				DirContextAdapter context = (DirContextAdapter) ctx;
				return getLdapNodePO(context);
			}

		});
	}

	public void save(LdapNodePo ldapNode) {
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", "top"));
		filter.and(new EqualsFilter("ou", "nodeInfo"));
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
			DistinguishedName departInfoBaseDN = getBaseDN("ou", "nodeInfo");
			ldapTemplate.bind(departInfoBaseDN, null, attrs);
		}

		DistinguishedName baseDN2 = getBaseDN("ou", "nodeInfo");
		baseDN2.add("nodeUuid ", ldapNode.getNodeUuid());
		Attributes attrs = getAttributes(ldapNode);
		ldapTemplate.bind(baseDN2, null, attrs);
	}

	// 根据uuid获取一个Node
	@SuppressWarnings("unchecked")
	public List<LdapNodePo> getNodeInfoByUuid(String uuid) {
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", "nodeInfo"));
		filter.and(new EqualsFilter("nodeUuid", uuid));
		return ldapTemplate.search(getBaseDN("ou", "nodeInfo"), filter.encode(), new ContextMapper() {
			@Override
			public Object mapFromContext(Object ctx) {
				DirContextAdapter context = (DirContextAdapter) ctx;

				LdapNodePo ldapNodePo = new LdapNodePo();
				ldapNodePo.setDn(new DistinguishedName(context.getDn()));
				ldapNodePo.setNodeUuid(context.getStringAttribute("nodeUuid"));
				ldapNodePo.setNodeName(context.getStringAttribute("nodeName"));
				ldapNodePo.setNodeFather(context.getStringAttribute("nodeFather"));
				ldapNodePo.setNodeRelations(context.getStringAttribute("nodeRelations"));
				ldapNodePo.setNodeFactInfo(context.getStringAttribute("nodeFactInfo"));

				return ldapNodePo;
			}
		});
	}

	// 删除一个nodeInfo 返回删除的nodeInfo
	public LdapNodePo remove(LdapNodePo ldapNodePo) {
		List<LdapNodePo> oldNodeList = getNodeInfoByUuid(ldapNodePo.getNodeUuid());
		if (oldNodeList == null || oldNodeList.size() <= 0) {
			return null;
		} else {
			ldapNodePo = oldNodeList.get(0);
			ldapTemplate.unbind(ldapNodePo.getDn());
			return ldapNodePo;
		}
	}

	// 不移动节点
	public void update(LdapNodePo ldapNodePo) {
		LdapNodePo oldNodePo = getNodeInfoByUuid(ldapNodePo.getNodeUuid()).get(0);
		ModificationItem[] mods = null;
		mods = new ModificationItem[2];
		mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("nodeName", ldapNodePo.getNodeName()));
		mods[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("nodeFather", ldapNodePo.getNodeFather()));
		ldapTemplate.modifyAttributes(oldNodePo.getDn(), mods);
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
	private Attributes getAttributes(LdapNodePo node) {
		BasicAttribute ocattr = new BasicAttribute("objectClass");
		ocattr.add("nodeInfo");
		Attributes attrs = new BasicAttributes();
		attrs.put(ocattr);
		attrs.put("nodeUuid", node.getNodeUuid());
		attrs.put("nodeName", node.getNodeName());
		attrs.put("nodeFather", node.getNodeFather());
		attrs.put("nodeRelations", node.getNodeRelations());
		attrs.put("nodeFactInfo", node.getNodeFactInfo());
		return attrs;
	}

	private LdapNodePo getLdapNodePO(DirContextAdapter context) {
		LdapNodePo nodePo = new LdapNodePo();
		nodePo.setDn(new DistinguishedName(context.getDn()));
		nodePo.setNodeUuid((context.getStringAttribute("nodeUuid")));
		nodePo.setNodeName(context.getStringAttribute("nodeName"));
		nodePo.setNodeRelations(context.getStringAttribute("nodeRelations"));
		nodePo.setNodeFather(context.getStringAttribute("nodeFather"));
		nodePo.setNodeFactInfo(context.getStringAttribute("nodeFactInfo"));
		return nodePo;
	}
}

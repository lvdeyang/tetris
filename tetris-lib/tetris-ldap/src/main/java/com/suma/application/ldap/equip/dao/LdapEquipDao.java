package com.suma.application.ldap.equip.dao;

import java.util.ArrayList;
import java.util.List;

import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Service;

import com.suma.application.ldap.base.dao.LdapBaseDao;
import com.suma.application.ldap.equip.po.LdapEquipPo;
import com.suma.application.ldap.user.po.LdapUserPo;

@Service
public class LdapEquipDao extends LdapBaseDao {

	@Autowired
	private LdapTemplate ldapTemplate;

	// 查询所有的设备
	@SuppressWarnings("unchecked")
	public List<LdapEquipPo> queryAllEquips() {
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", "equipInfo"));
		return ldapTemplate.search(getBaseDN("ou", "equipInfo"), filter.encode(), new ContextMapper() {
			@Override
			public Object mapFromContext(Object ctx) {
				DirContextAdapter context = (DirContextAdapter) ctx;
				return getLdapEquipPO(context);
			}

		});
	}

	public void save(LdapEquipPo ldapEquip) {
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", "top"));
		filter.and(new EqualsFilter("ou", "equipInfo"));
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
			DistinguishedName departInfoBaseDN = getBaseDN("ou", "equipInfo");
			ldapTemplate.bind(departInfoBaseDN, null, attrs);
		}

		DistinguishedName baseDN2 = getBaseDN("ou", "equipInfo");
		baseDN2.add("equipUuid", ldapEquip.getEquipUuid());
		Attributes attrs = getAttributes(ldapEquip);
		ldapTemplate.bind(baseDN2, null, attrs);
	}

	// 根据id获取一个设备
	@SuppressWarnings("unchecked")
	public List<LdapEquipPo> getEquipByIUuid(String equipUuid) {
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", "equipInfo"));
		filter.and(new EqualsFilter("equipUuid", equipUuid));
		return ldapTemplate.search(getBaseDN("ou", "equipInfo"), filter.encode(), new ContextMapper() {
			@Override
			public Object mapFromContext(Object ctx) {
				DirContextAdapter context = (DirContextAdapter) ctx;
				LdapEquipPo equipPo = getLdapEquipPO(context);
				return equipPo;
			}
		});
	}

	// 删除一个设备
	public LdapEquipPo remove(LdapEquipPo equip) {
		List<LdapEquipPo> oldEquipList = getEquipByIUuid(equip.getEquipUuid());
		if (oldEquipList == null || oldEquipList.size() <= 0) {
			return null;
		} else {
			LdapEquipPo oldEquip = oldEquipList.get(0);
			ldapTemplate.unbind(oldEquip.getDn());
			return oldEquip;
		}
	}

	public void update(LdapEquipPo equip) {
		LdapEquipPo oldequip = getEquipByIUuid(equip.getEquipUuid()).get(0);
		ModificationItem[] mods = new ModificationItem[9];
		mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("equipNo", equip.getEquipNo()));
		mods[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("equipName", equip.getEquipName()));
		mods[2] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("equipAddr", equip.getEquipAddr()));
		mods[3] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("equipPort", equip.getEquipPort().toString()));
		mods[4] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("equipType", equip.getEquipType().toString()));
		mods[5] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("equipPwd", equip.getEquipPwd()));
		mods[6] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("equipOrg", equip.getEquipOrg()));
		mods[7] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("equipNode", equip.getEquipNode()));
		mods[8] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("equipFactInfo", equip.getEquipFactInfo()));

		ldapTemplate.modifyAttributes(oldequip.getDn(), mods);
	}
	
	// 查询某一组织下的所有设备
//	@SuppressWarnings("unchecked")
//	public List<LdapEquipPo> getDepartChildrenEquip(String departOrgNo){
//		AndFilter filter = new AndFilter();
//		filter.and(new EqualsFilter("objectclass", "equipInfo"));
//		filter.and(new EqualsFilter("equipOrg", departOrgNo));
//		return ldapTemplate.search(getBaseDN("ou", "equip"), filter.encode(),new ContextMapper(){
//			@Override
//			public Object mapFromContext(Object ctx) {
//				DirContextAdapter context = (DirContextAdapter)ctx;
//				LdapEquipPo equipPo = new LdapEquipPo();
//				equipPo.setDn(new DistinguishedName(context.getDn()));
//				equipPo.setEquipId(context.getStringAttribute("equipId"));
//				equipPo.setEquipAddr(context.getStringAttribute("equipAddr"));
//				equipPo.setEquipFactInfo(context.getStringAttribute("equipFactInfo"));
//				equipPo.setEquipNo(context.getStringAttribute("equipNo"));
//				equipPo.setEquipNode(context.getStringAttribute("equipNode"));
//				equipPo.setEquipOrg(context.getStringAttribute("equipOrg"));
//				equipPo.setEquipType(context.getStringAttribute("equipType"));
//				return equipPo;
//			}
//		});
//	}

	// 根据id获取一个设备
//	@SuppressWarnings("unchecked")
//	public List<LdapEquipPo> getEquipById(String equipId){
//		AndFilter filter = new AndFilter();
//		filter.and(new EqualsFilter("objectclass", "equipInfo"));
//		filter.and(new EqualsFilter("equipId", equipId));
//		return ldapTemplate.search(getBaseDN("ou", "equip"), filter.encode(),new ContextMapper(){
//			@Override
//			public Object mapFromContext(Object ctx) {
//				DirContextAdapter context = (DirContextAdapter)ctx;
//				LdapEquipPo equipPo = new LdapEquipPo();
//				equipPo.setDn(new DistinguishedName(context.getDn()));
//				equipPo.setEquipId(context.getStringAttribute("equipId"));
//				equipPo.setEquipAddr(context.getStringAttribute("equipAddr"));
//				equipPo.setEquipFactInfo(context.getStringAttribute("equipFactInfo"));
//				equipPo.setEquipNo(context.getStringAttribute("equipNo"));
//				equipPo.setEquipNode(context.getStringAttribute("equipNode"));
//				equipPo.setEquipOrg(context.getStringAttribute("equipOrg"));
//				equipPo.setEquipType(context.getStringAttribute("equipType"));
//				return equipPo;
//			}
//		});
//	}

	// 根据地址获得设备
//	@SuppressWarnings("unchecked")
//	public List<LdapEquipPo> getEquipByAddr(String equipAddr) {
//		AndFilter filter = new AndFilter();
//		filter.and(new EqualsFilter("objectClass", "equipInfo"));
//		filter.and(new EqualsFilter("equipAddr", equipAddr));
//		return ldapTemplate.search(getBaseDN("ou", "equip"), filter.encode(), new ContextMapper() {
//			@Override
//			public Object mapFromContext(Object ctx) {
//				DirContextAdapter context = (DirContextAdapter)ctx;
//				LdapEquipPo equipPo = new LdapEquipPo();
//				equipPo.setDn(new DistinguishedName(context.getDn()));
//				equipPo.setEquipId(context.getStringAttribute("equipId"));
//				equipPo.setEquipAddr(context.getStringAttribute("equipAddr"));
//				equipPo.setEquipFactInfo(context.getStringAttribute("equipFactInfo"));
//				equipPo.setEquipNo(context.getStringAttribute("equipNo"));
//				equipPo.setEquipNode(context.getStringAttribute("equipNode"));
//				equipPo.setEquipOrg(context.getStringAttribute("equipOrg"));
//				equipPo.setEquipType(context.getStringAttribute("equipType"));
//				return equipPo;
//			}
//		});
//	}

	// 添加一个设备
//	public void save(LdapEquipPo equip,DistinguishedName newParentDN){
//		newParentDN.add("equipId",equip.getEquipId());
//		Attributes attrs = getAttributes(equip);
//		ldapTemplate.bind(newParentDN, null, attrs);
//	}

	// 删除一个设备
//	public LdapEquipPo remove(LdapEquipPo equip){
//		List<LdapEquipPo> oldEquipList = getEquipById(equip.getEquipId());
//		if(oldEquipList==null || oldEquipList.size()<=0){
//			return null;
//		}else{
//			LdapEquipPo oldEquip = oldEquipList.get(0);
//			ldapTemplate.unbind(oldEquip.getDn());
//			return oldEquip;
//		}
//	}

	// 修改一个设备 节点不移动
//	public void update(LdapEquipPo equip){
//		LdapEquipPo oldEquip = getEquipById(equip.getEquipId()).get(0);
//		ModificationItem[] mods = null;
//		if(equip.getEquipOrg() != null){
//			mods = new ModificationItem[6];
//			mods[0] = new ModificationItem(DirContextAdapter.REPLACE_ATTRIBUTE,new BasicAttribute("equipNo",equip.getEquipNo()));
//			mods[1] = new ModificationItem(DirContextAdapter.REPLACE_ATTRIBUTE,new BasicAttribute("equipAddr",equip.getEquipAddr()));
//			mods[2] = new ModificationItem(DirContextAdapter.REPLACE_ATTRIBUTE,new BasicAttribute("equipFactInfo",equip.getEquipFactInfo()));
//			mods[3] = new ModificationItem(DirContextAdapter.REPLACE_ATTRIBUTE,new BasicAttribute("equipType",equip.getEquipType()));
//			mods[4] = new ModificationItem(DirContextAdapter.REPLACE_ATTRIBUTE,new BasicAttribute("equipNode",equip.getEquipNode()));
//			mods[5] = new ModificationItem(DirContextAdapter.REPLACE_ATTRIBUTE,new BasicAttribute("equipOrg",equip.getEquipOrg()));
//		}else{
//			mods = new ModificationItem[5];
//			mods[0] = new ModificationItem(DirContextAdapter.REPLACE_ATTRIBUTE,new BasicAttribute("equipNo",equip.getEquipNo()));
//			mods[1] = new ModificationItem(DirContextAdapter.REPLACE_ATTRIBUTE,new BasicAttribute("equipAddr",equip.getEquipAddr()));
//			mods[2] = new ModificationItem(DirContextAdapter.REPLACE_ATTRIBUTE,new BasicAttribute("equipFactInfo",equip.getEquipFactInfo()));
//			mods[3] = new ModificationItem(DirContextAdapter.REPLACE_ATTRIBUTE,new BasicAttribute("equipType",equip.getEquipType()));
//			mods[4] = new ModificationItem(DirContextAdapter.REPLACE_ATTRIBUTE,new BasicAttribute("equipNode",equip.getEquipNode()));
//		}
//		ldapTemplate.modifyAttributes(oldEquip.getDn(),mods);
//	}

	// 修改一个设备 节点移动
//	public void update(LdapEquipPo equip,DistinguishedName newParentDn){
//		LdapEquipPo oldEquip = new LdapEquipPo();
//		oldEquip.setEquipId(equip.getEquipId());
//		oldEquip = remove(oldEquip);
//		save(equip,newParentDn);
//	}

	/********************************************
	 * 私有方法
	 ********************************************/
	private LdapEquipPo getLdapEquipPO(DirContextAdapter context) {
		LdapEquipPo equipPo = new LdapEquipPo();
		equipPo.setDn(new DistinguishedName(context.getDn()));
		equipPo.setEquipUuid(context.getStringAttribute("equipUuid"));
		equipPo.setEquipNo(context.getStringAttribute("equipNo"));
		equipPo.setEquipName(context.getStringAttribute("equipName"));
		equipPo.setEquipAddr(context.getStringAttribute("equipAddr"));
		equipPo.setEquipPort(Integer.valueOf(context.getStringAttribute("equipPort")));
		equipPo.setEquipType(Integer.valueOf(context.getStringAttribute("equipType")));
		equipPo.setEquipPwd(context.getStringAttribute("equipPwd"));
		equipPo.setEquipOrg(context.getStringAttribute("equipOrg"));
		equipPo.setEquipNode(context.getStringAttribute("equipNode"));
		equipPo.setEquipFactInfo(context.getStringAttribute("equipFactInfo"));

		return equipPo;
	}

	// 创建的属性
	private Attributes getAttributes(LdapEquipPo equip) {
		BasicAttribute ocattr = new BasicAttribute("objectClass");
		ocattr.add("equipInfo");
		Attributes attrs = new BasicAttributes();
		attrs.put(ocattr);
		attrs.put("equipUuid", equip.getEquipUuid());
		attrs.put("equipNo", equip.getEquipNo());
		attrs.put("equipName", equip.getEquipName());
		attrs.put("equipAddr", equip.getEquipAddr());
		attrs.put("equipPort", String.valueOf(equip.getEquipPort()));
		attrs.put("equipType", String.valueOf(equip.getEquipType()));
		attrs.put("equipPwd", equip.getEquipPwd());
		attrs.put("equipOrg", equip.getEquipOrg());
		attrs.put("equipNode", equip.getEquipNode());
		attrs.put("equipFactInfo", equip.getEquipFactInfo());
		return attrs;
	}
}

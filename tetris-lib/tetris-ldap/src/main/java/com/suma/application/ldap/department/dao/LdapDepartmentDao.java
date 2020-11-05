package com.suma.application.ldap.department.dao;

import java.util.ArrayList;
import java.util.List;

import javax.naming.Name;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.control.PagedResult;
import org.springframework.ldap.control.PagedResultsCookie;
import org.springframework.ldap.control.PagedResultsRequestControl;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Service;

import com.suma.application.ldap.base.contants.ZKCommContants;
import com.suma.application.ldap.base.dao.LdapBaseDao;
import com.suma.application.ldap.contants.LdapContants;
import com.suma.application.ldap.department.po.LdapDepartmentPo;
import com.suma.application.ldap.ser.LdapSerInfoPo;

@SuppressWarnings("deprecation")
@Service
public class LdapDepartmentDao extends LdapBaseDao{

	@Autowired
	private LdapTemplate ldapTemplate;
	
	//查询所有的组织
	@SuppressWarnings("unchecked")
	public List<LdapDepartmentPo> queryAllDepartment() {
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", "departInfo"));
		return ldapTemplate.search(getBaseDN("ou", "departInfo"), filter.encode(), new ContextMapper(){
			@Override
			public Object mapFromContext(Object ctx) {
				DirContextAdapter context = (DirContextAdapter)ctx;
				return getLdapDepartPO(context);
			}

		});
	}
	
	public void save(LdapDepartmentPo ldapDepart){
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
			DistinguishedName departInfoBaseDN = getBaseDN("ou", "departInfo");
			ldapTemplate.bind(departInfoBaseDN, null, attrs);
		}
		
		DistinguishedName baseDN2 = getBaseDN("ou", "departInfo");
		baseDN2.add("orgUuid", ldapDepart.getOrgUuid());
		Attributes attrs = getAttributesByDepartment(ldapDepart);
		ldapTemplate.bind(baseDN2, null, attrs);
	}
	
	// 修改Department信息
	public void update(LdapDepartmentPo ldapDepartPo) {
		LdapDepartmentPo oldDepart = getDepartByUuid(ldapDepartPo.getOrgUuid()).get(0);
		ModificationItem[] mods = null;
		mods = new ModificationItem[4];
		mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("orgName", ldapDepartPo.getOrgName()));
		mods[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("orgRelation", ldapDepartPo.getOrgRelation()));
		mods[2] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("orgCmdRelation", ldapDepartPo.getOrgCmdRelation()));
		mods[3] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("orgFactInfo", ldapDepartPo.getOrgFactInfo()));
		ldapTemplate.modifyAttributes(oldDepart.getDn(), mods);
	}
	
	@SuppressWarnings("unchecked")
	public List<LdapDepartmentPo> getDepartByUuid(String uuid){
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", "departInfo"));
		filter.and(new EqualsFilter("orgUuid", uuid));
		return ldapTemplate.search(getBaseDN("ou", "departInfo"), filter.encode(), new ContextMapper() {
			@Override
			public Object mapFromContext(Object ctx) {
				DirContextAdapter context = (DirContextAdapter) ctx;
				LdapDepartmentPo ldapDepartmentPo = new LdapDepartmentPo();
				ldapDepartmentPo.setDn(new DistinguishedName(context.getDn()));
				ldapDepartmentPo.setOrgUuid(context.getStringAttribute("orgUuid"));
				ldapDepartmentPo.setOrgName(context.getStringAttribute("orgName"));
				ldapDepartmentPo.setOrgRelation(context.getStringAttribute("orgRelation"));
				ldapDepartmentPo.setOrgCmdRelation(context.getStringAttribute("orgCmdRelation"));
				ldapDepartmentPo.setOrgFactInfo(context.getStringAttribute("orgFactInfo"));
				return ldapDepartmentPo;
			}
		});
	}
	
	// 删除一个departmentPo 返回删除的departmentPo
	public LdapDepartmentPo remove(LdapDepartmentPo ldapDepartmentPo) {
		List<LdapDepartmentPo> oldList = getDepartByUuid(ldapDepartmentPo.getOrgUuid());
		if (oldList == null || oldList.size() <= 0) {
			return null;
		} else {
			ldapDepartmentPo = oldList.get(0);
			ldapTemplate.unbind(ldapDepartmentPo.getDn());
			return ldapDepartmentPo;
		}
	}
	
	//查询一个组织的直接下级分页效果后来再做
//	@SuppressWarnings("unchecked")
//	public List<LdapDepartmentPo> getDepartChildrenDepart(String departOrgNo){
//		
//		AndFilter filter = new AndFilter();
//		filter.and(new EqualsFilter("objectclass", "departInfo"));
//		filter.and(new EqualsFilter("orgRelation", departOrgNo));
//		return ldapTemplate.search(getBaseDN("ou","org"), filter.encode(), new ContextMapper(){
//			@Override
//			public Object mapFromContext(Object ctx) {
//				DirContextAdapter context = (DirContextAdapter)ctx;
//				LdapDepartmentPo departmentPO = new LdapDepartmentPo();
//				departmentPO.setDn(new DistinguishedName(context.getDn()));
//				departmentPO.setOrgId(context.getStringAttribute("orgId"));
//				departmentPO.setOrgNo(context.getStringAttribute("orgNo"));
//				departmentPO = getDepartmentByOrgNo(departmentPO.getOrgNo()).get(0);
//				departmentPO.setOrgName(context.getStringAttribute("orgName"));
//				//departmentPO.setOrgRelation(context.getStringAttribute("orgRelation"));
//				return departmentPO;
//			}
//			
//		});
//	}
	//通过orgNo查找下级组织数量
	public int getAccountById(String departOrgNo){
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", "departInfo"));
		filter.and(new EqualsFilter("orgRelation", departOrgNo));
		return ldapTemplate.search(getBaseDN("ou","org"), filter.encode(), new ContextMapper(){
			public Object mapFromContext(Object arg0) {
				return 1;
			}
		}).size();
	}
	//查询一个组织的上级或平级组织
//	public List<LdapDepartmentPo> getSuperOrLevelDepart(LdapDepartmentPo department,String departId){
//		List<LdapDepartmentPo> total = queryLowLevDepartment(departId);
//		List<LdapDepartmentPo> result = new ArrayList<LdapDepartmentPo>();
//		result.add(department);
//		getChildrenList(department,total,result);
//		List<LdapDepartmentPo> superOrLevelList = new ArrayList<LdapDepartmentPo>();
//		for(int i=0;i<total.size();i++){
//			boolean flag = true;
//			for(int j=0;j<result.size();j++){
//				if(total.get(i).equals(result.get(j))){
//					flag = false;
//				}
//			}
//			if(flag){
//				superOrLevelList.add(total.get(i));
//			}
//		}
//		return superOrLevelList;
//	}
	
	//通过组织id查找组织
//	@SuppressWarnings("unchecked")
//	public List<LdapDepartmentPo> getDepartmentById(String departId){
//		AndFilter filter = new AndFilter();
//		filter.and(new EqualsFilter("objectclass", "departInfo"));
//		filter.and(new EqualsFilter("orgId", departId));
//		return ldapTemplate.search(getBaseDN("ou","org"), filter.encode(), new ContextMapper(){
//			@Override
//			public Object mapFromContext(Object ctx) {
//				DirContextAdapter context = (DirContextAdapter)ctx;
//				LdapDepartmentPo departmentPO = new LdapDepartmentPo();
//				departmentPO.setDn(new DistinguishedName(context.getDn()));
//				departmentPO.setOrgId(context.getStringAttribute("orgId"));
//				departmentPO.setOrgNo(context.getStringAttribute("orgNo"));
//				departmentPO.setOrgName(context.getStringAttribute("orgName"));
//				departmentPO.setOrgRelation(context.getStringAttribute("orgRelation"));
//				return departmentPO;
//			}
//		});
//	}
	
	//通过orgNo查询组织
//	@SuppressWarnings("unchecked")
//	public List<LdapDepartmentPo> getDepartmentByOrgNo(String orgNo){
//		AndFilter filter = new AndFilter();
//		filter.and(new EqualsFilter("objectclass", "departInfo"));
//		filter.and(new EqualsFilter("orgNo", orgNo));
//		return ldapTemplate.search(getBaseDN("ou","org"), filter.encode(), new ContextMapper(){
//			@Override
//			public Object mapFromContext(Object ctx) {
//				DirContextAdapter context = (DirContextAdapter)ctx;
//				LdapDepartmentPo departmentPO = new LdapDepartmentPo();
//				departmentPO.setDn(new DistinguishedName(context.getDn()));
//				departmentPO.setOrgId(context.getStringAttribute("orgId"));
//				departmentPO.setOrgNo(context.getStringAttribute("orgNo"));
//				departmentPO.setOrgName(context.getStringAttribute("orgName"));
//				departmentPO.setOrgRelation(context.getStringAttribute("orgRelation"));
//				return departmentPO;
//			}
//		});
//	}
	
//	//查询用户节点组织目录
//	@SuppressWarnings("unchecked")
//	public LdapDepartmentPo getUserDepartNode(String departId){
//		AndFilter filter = new AndFilter();
//		filter.and(new EqualsFilter("objectclass", "departInfo"));
//		filter.and(new EqualsFilter("orgId", departId));
//		List<LdapDepartmentPo> departs = ldapTemplate.search(getBaseDN("ou","user"), filter.encode(), new ContextMapper(){
//			@Override
//			public Object mapFromContext(Object ctx) {
//				DirContextAdapter context = (DirContextAdapter)ctx;
//				LdapDepartmentPo departmentPO = new LdapDepartmentPo();
//				departmentPO.setDn(new DistinguishedName(context.getDn()));
//				departmentPO.setOrgId(context.getStringAttribute("orgId"));
//				departmentPO.setOrgNo(context.getStringAttribute("orgNo"));
//				departmentPO.setOrgName(context.getStringAttribute("orgName"));
//				departmentPO.setOrgRelation(context.getStringAttribute("orgRelation"));
//				return departmentPO;
//			}
//		});
//		if(departs==null || departs.size()<=0){
//			return null;
//		}else{
//			return departs.get(0);
//		}
//	} 
	
	//创建equip树的组织目录
//	public int createEquipNode(LdapDepartmentPo department){
//		try{
//			List<LdapDepartmentPo> equipNode = getEquipNode(department.getOrgId());
//			if(equipNode==null || equipNode.size()<=0){
//				DistinguishedName departmentDN = this.getBaseDN("ou", "equip");
//				departmentDN.add("orgId", department.getOrgId());
//				Attributes attrs = getAttributesByDepartment(department);
//				ldapTemplate.bind(departmentDN, null, attrs);	
//			}
//			return 1;
//		}catch(Exception e){
//			e.printStackTrace();
//			return 0;
//		}
//	}
	
	//TODO 查询设备节点组织目录	要根据id查   不根据no查
//	@SuppressWarnings("unchecked")
//	public LdapDepartmentPo getEquipDepartNode(String departOrgId){
//		AndFilter filter = new AndFilter();
//		filter.and(new EqualsFilter("objectclass", "departInfo"));
//		filter.and(new EqualsFilter("orgId", departOrgId));
//		List<LdapDepartmentPo> departs = ldapTemplate.search(getBaseDN("ou","equip"), filter.encode(), new ContextMapper(){
//			@Override
//			public Object mapFromContext(Object ctx) {
//				DirContextAdapter context = (DirContextAdapter)ctx;
//				LdapDepartmentPo departmentPO = new LdapDepartmentPo();
//				departmentPO.setDn(new DistinguishedName(context.getDn()));
//				departmentPO.setOrgNo(context.getStringAttribute("orgId"));
//				departmentPO.setOrgNo(context.getStringAttribute("orgNo"));
//				departmentPO.setOrgName(context.getStringAttribute("orgName"));
//				departmentPO.setOrgRelation(context.getStringAttribute("orgRelation"));
//				return departmentPO;
//			}
//		});
//		if(departs==null && departs.size()<=0){
//			return null;
//		}else{
//			return departs.get(0);
//		}
//	}
	
	//添加一个组织
//	public String save(LdapDepartmentPo department){
//		int successFlag = createUserNode(department);
//		if(successFlag == 0){
//			return "用户根目录节点创建失败";
//		}else{
//			successFlag = createEquipNode(department);
//			if(successFlag == 0){
//				return "设备根目录节点创建失败";
//			}
//		}
//		DistinguishedName dn = null;
//		if(!ZKCommContants.SUPER_INST_ORG.equals(department.getOrgRelation())){
//			dn = getDepartmentByOrgNo(department.getOrgRelation()).get(0).getDn();
//		}else{
//			dn = this.getBaseDN("ou", "org");
//		}
//		dn.add("orgId",department.getOrgId());
//		Attributes attrs = getAttributesByDepartment(department);
//		ldapTemplate.bind(dn, null, attrs);	
//		return "yes";
//	}
	
	//根据id删除一个组织
//	public String remove(LdapDepartmentPo department){
//		List<LdapDepartmentPo> departmentTotal = queryAllDepartment();
//		List<LdapDepartmentPo> departmentList = new ArrayList<LdapDepartmentPo>();
//		departmentList.add(department);
//		getChildrenList(department,departmentTotal,departmentList);
//		
//		int successFlag = 1;
//		for(LdapDepartmentPo node:departmentList){
//			successFlag = delUserNode(node);
//			if(successFlag == 0){
//				return "用户根目录节点删除失败";
//			}
//		}
//		for(LdapDepartmentPo node:departmentList){
//			successFlag = delEquipNode(node);
//			if(successFlag == 0){
//				return "设备根目录节点删除失败";
//			}
//		}
//		department = this.getDepartmentById(department.getOrgId()).get(0);
//		ldapTemplate.unbind(department.getDn(), true);
//		return ZKCommContants.STRING_BOOLEAN_YES;
//	}
	
	//根据id修改一个组织
//	public void update(LdapDepartmentPo department){
//		if(department.getOrgRelation() == null){
//			//DN没有变化
//			DistinguishedName dn = getDepartmentById(department.getOrgId()).get(0).getDn();
//			ModificationItem[] mods = null;
//			if(department.getOrgRelation() != null){
//				mods = new ModificationItem[3];
//				mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,new BasicAttribute("orgNo",department.getOrgNo()));
//				mods[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,new BasicAttribute("orgName",department.getOrgName()));
//				mods[2] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,new BasicAttribute("orgRelation",department.getOrgRelation()));
//			}else{
//				mods = new ModificationItem[2];
//				mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,new BasicAttribute("orgNo",department.getOrgNo()));
//				mods[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,new BasicAttribute("orgName",department.getOrgName()));
//			}
//			ldapTemplate.modifyAttributes(dn, mods);
//		}else{
//			//先获取子元素
//			List<LdapDepartmentPo> total = queryAllDepartment();
//			//不包含父节点
//			List<LdapDepartmentPo> childrenList = new ArrayList<LdapDepartmentPo>();
//			getChildrenList(department, total, childrenList);
//			//递归删除     不删除ou=user和ou=equip的目录
//			ldapTemplate.unbind(this.getDepartmentById(department.getOrgId()).get(0).getDn(),true);
//			
//			//父节点插入才能插入子节点
//			save(department);
//			//将旧的子节点的orgrelation跟换为新的,最后插入子节点
//			for(LdapDepartmentPo children:childrenList){
//				if(children.getOrgRelation().equals(department.getOrgNo())){
//					children.setOrgRelation(department.getOrgNo());
//				}
//				save(children);
//			}
// 		}
//	}
	
	//获取一个组织的所有下级组织
//	public void getChildrenList(LdapDepartmentPo root,List<LdapDepartmentPo> total,List<LdapDepartmentPo> result){
//		for(int i=0;i<total.size();i++){
//			if(total.get(i).getOrgRelation().equals(root.getOrgNo())){
//				result.add(total.get(i));
//				getChildrenList(total.get(i),total,result);
//			}else{
//				if(i >= (total.size()-1)){
//					return;
//				}
//			}
//		}
//	}

	/**
	 * @Description:获取当前组织和其所有的下级组织
	 * @param @param departId
	 * @param @return   
	 * @return List<LdapDepartmentPo>  
	 * @throws
	 * @date 2016年3月4日
	 */
//	public List<LdapDepartmentPo> queryLowLevDepartment(String departId) {
//		List<LdapDepartmentPo> total = queryAllDepartment();
//		List<LdapDepartmentPo> result = new ArrayList<LdapDepartmentPo>();
//		List<LdapDepartmentPo> departList = getDepartmentById(departId);
//		if(departList==null || departList.size()<=0){
//			return result;
//		}
//		result.add(departList.get(0));
//		getChildrenList(departList.get(0),total,result);
//		return result;
//	}
	
	/**************************************
	 *               私有方法
	 **************************************/
	private Attributes getAttributesByDepartment(LdapDepartmentPo department){
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
	
	private Object getLdapDepartPO(DirContextAdapter context) {
		LdapDepartmentPo departmentPO = new LdapDepartmentPo();
		departmentPO.setDn(new DistinguishedName(context.getDn()));
		departmentPO.setOrgUuid(context.getStringAttribute("orgUuid"));
		departmentPO.setOrgName(context.getStringAttribute("orgName"));
		departmentPO.setOrgRelation(context.getStringAttribute("orgRelation"));
		departmentPO.setOrgCmdRelation(context.getStringAttribute("orgCmdRelation"));
		departmentPO.setOrgFactInfo(context.getStringAttribute("orgFactInfo"));
		return departmentPO;
	}
}

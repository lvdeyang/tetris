package com.sumavision.bvc.system.dao;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.system.enumeration.BusinessRoleSpecial;
import com.sumavision.bvc.system.enumeration.BusinessRoleType;
import com.sumavision.bvc.system.po.BusinessRolePO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = BusinessRolePO.class, idClass = long.class)
public interface BusinessRoleDAO extends MetBaseDAO<BusinessRolePO>{

	/**
	 * @Title: 根据属性查询角色 
	 * @param special 角色属性
	 * @param type 角色业务
	 * @return List<BusinessRolePO> 角色列表
	 */
	public List<BusinessRolePO> findBySpecialAndType(BusinessRoleSpecial special, BusinessRoleType type);
	
	public List<BusinessRolePO> findByType(BusinessRoleType type);
	
	public List<BusinessRolePO> findByName(String name);
}

package com.sumavision.bvc.basic.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sumavision.bvc.basic.bo.BusinessRoleBO;

@Service
public class BasicQueryUtil {

	/**
	 * 根据角色id查询业务角色信息<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月14日 下午4:40:55
	 * @param List<BusinessRoleBO> roles 业务角色信息
	 * @param Long roleId 角色id
	 * @return BusinessRoleBO
	 */
	public BusinessRoleBO queryBusinessRole(List<BusinessRoleBO> roles, Long roleId){
		
		if(roleId != null){
			for(BusinessRoleBO role: roles){
				if(roleId.equals(role.getRoleId())){
					return role;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * 根据角色id查询业务角色信息<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月14日 下午4:40:55
	 * @param List<BusinessRoleBO> roles 业务角色信息
	 * @param Long roleId 角色id
	 * @return BusinessRoleBO
	 */
	public List<BusinessRoleBO> queryBusinessRole(List<BusinessRoleBO> roles, List<Long> roleIds){
		
		List<BusinessRoleBO> businessRoles = new ArrayList<BusinessRoleBO>();
 		
		if(roleIds != null && roleIds.size() > 0){
			for(Long roleId: roleIds){
				for(BusinessRoleBO role: roles){
					if(roleId.equals(role.getRoleId())){
						businessRoles.add(role);
						break;
					}
				}
			}
		}
		
		return businessRoles;
	}
	
}

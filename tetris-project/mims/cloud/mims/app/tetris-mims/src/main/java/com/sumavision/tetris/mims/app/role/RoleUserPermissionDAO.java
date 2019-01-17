package com.sumavision.tetris.mims.app.role;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = RoleUserPermissionPO.class, idClass = Long.class)
public interface RoleUserPermissionDAO extends BaseDAO<RoleUserPermissionPO>{

	/**
	 * 根据用户获取用户角色映射关系<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月21日 下午3:34:09
	 * @param Collection<String> userIds 用户id列表
	 * @return List<RoleUserPermissionPO> 用户角色映射关系
	 */
	public List<RoleUserPermissionPO> findByUserIdIn(Collection<String> userIds);
	
	/**
	 * 获取角色的映射关系<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月8日 下午7:18:19
	 * @param Long roleId 角色id
	 * @return List<RoleUserPermissionPO> 用户角色映射关系
	 */
	public List<RoleUserPermissionPO> findByRoleId(Long roleId);
	
	/**
	 * 根据角色和用户获取权限<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月10日 上午10:50:15
	 * @param Long roleId 角色id
	 * @param String userId 用户id
	 * @return List<RoleUserPermissionPO> 权限
	 */
	public List<RoleUserPermissionPO> findByRoleIdAndUserId(Long roleId, String userId);
	
}

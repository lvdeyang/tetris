package com.sumavision.tetris.system.role;

import java.util.List;
import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = UserSystemRolePermissionPO.class, idClass = Long.class)
public interface UserSystemRolePermissionDAO extends BaseDAO<UserSystemRolePermissionPO>{

	/**
	 * 获取用户的系统角色权限绑定<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月19日 下午4:34:45
	 * @param Long userId 用户id
	 * @return List<UserSystemRolePermissionPO> 权限列表
	 */
	public List<UserSystemRolePermissionPO> findByUserId(Long userId);
	
}

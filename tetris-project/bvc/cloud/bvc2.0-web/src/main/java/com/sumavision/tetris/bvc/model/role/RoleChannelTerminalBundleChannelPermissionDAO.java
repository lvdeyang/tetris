package com.sumavision.tetris.bvc.model.role;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = RoleChannelTerminalBundleChannelPermissionPO.class, idClass = Long.class)
public interface RoleChannelTerminalBundleChannelPermissionDAO extends BaseDAO<RoleChannelTerminalBundleChannelPermissionPO>{

	/**
	 * 查询角色通道的关联<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月29日 下午1:46:14
	 * @param Long roleChannelId 角色通道id
	 * @return List<RoleChannelTerminalBundleChannelPermissionPO> 关联列表
	 */
	public List<RoleChannelTerminalBundleChannelPermissionPO> findByRoleChannelId(Long roleChannelId);
	
}

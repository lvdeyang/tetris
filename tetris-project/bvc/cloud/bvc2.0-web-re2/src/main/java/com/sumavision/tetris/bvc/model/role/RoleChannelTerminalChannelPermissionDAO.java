package com.sumavision.tetris.bvc.model.role;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = RoleChannelTerminalChannelPermissionPO.class, idClass = Long.class)
public interface RoleChannelTerminalChannelPermissionDAO extends BaseDAO<RoleChannelTerminalChannelPermissionPO>{

	/**
	 * 查询角色通道的关联<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月29日 下午1:46:14
	 * @param Long roleChannelId 角色通道id
	 * @return List<RoleChannelTerminalBundleChannelPermissionPO> 关联列表
	 */
	public List<RoleChannelTerminalChannelPermissionPO> findByRoleChannelId(Long roleChannelId);
	
	/**
	 * 根据角色通道查询终端通道关联<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月21日 下午2:29:48
	 * @param Collection<Long> roleChannelIds 角色通道id列表
	 * @param Long terminalId 终端id
	 * @return List<RoleChannelTerminalChannelPermissionPO> 通道关联列表
	 */
	public List<RoleChannelTerminalChannelPermissionPO> findByRoleChannelIdInAndTerminalId(Collection<Long> roleChannelIds, Long terminalId);
	
	public RoleChannelTerminalChannelPermissionPO findByTerminalIdAndRoleChannelId(Long terminalId, Long roleChannelId);
	
	/**
	 * 根据终端通道id查询角色终端通道映射列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月8日 下午1:11:21
	 * @param Collection<Long> terminalChannelIds 终端通道id列表
	 * @return List<RoleChannelTerminalChannelPermissionPO> 映射列表
	 */
	public List<RoleChannelTerminalChannelPermissionPO> findByTerminalChannelIdIn(Collection<Long> terminalChannelIds);
	
}

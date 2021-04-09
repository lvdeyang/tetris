package com.sumavision.tetris.bvc.model.role;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = RoleChannelPO.class, idClass = Long.class)
public interface RoleChannelDAO extends BaseDAO<RoleChannelPO>{

	/**
	 * 查询角色下通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 下午3:54:57
	 * @param Long roleId 角色id
	 * @return List<RoleChannelPO> 通道列表
	 */
	public List<RoleChannelPO> findByRoleId(Long roleId);
	public List<RoleChannelPO> findByRoleIdIn(Collection<Long> roleId);
	
	/**
	 * 查询角色特定类型的通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月6日 上午10:52:07
	 * @param Collection<Long> roleIds 角色id列表
	 * @param Collection<RoleChannelType> types 通道类型列表
	 * @return List<RoleChannelPO> 通道列表
	 */
	public List<RoleChannelPO> findByRoleIdInAndTypeIn(Collection<Long> roleIds, Collection<RoleChannelType> types);
	
	/**
	 * 根据角色通道id查询带有角色信息的角色通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月20日 上午10:38:42
	 * @param Collection<Long> ids 角色通道id列表
	 * @return List<RoleChannelWithRolePermissionDTO> 角色通道列表
	 */
	@Query(value = "select new com.sumavision.tetris.bvc.model.role.RoleChannelWithRolePermissionDTO("
					+ "channel.id, "
					+ "channel.name, "
					+ "channel.type, "
					+ "role.id, "
					+ "role.name, "
					+ "role.internalRoleType"
				+ ") "
				+ "from com.sumavision.tetris.bvc.model.role.RoleChannelPO channel, "
				+ "com.sumavision.tetris.bvc.model.role.RolePO role "
				+ "where channel.id in ?1 "
				+ "and channel.roleId=role.id")
	public List<RoleChannelWithRolePermissionDTO> findRoleChannelWithRolePermissionByIdIn(Collection<Long> ids);
	
	/**
	 * 根据角色id列表查询角色通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月20日 下午4:26:39
	 * @param Collection<Long> roleIds 角色id列表
	 * @param RoleChannelType type 角色通道类型
	 * @return List<RoleChannelPO> 角色通道列表
	 */
	public List<RoleChannelPO> findByRoleIdInAndType(Collection<Long> roleIds, RoleChannelType type);
	
	/**
	 * 根据角色查询通道列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月26日 上午10:48:22
	 * @param Collection<Long> roleIds 角色id列表
	 * @return List<RoleChannelPO> 角色通道列表
	 */
	public List<RoleChannelPO> findByRoleIdInOrderByTypeDesc(Collection<Long> roleIds);
}

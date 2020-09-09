package com.sumavision.tetris.bvc.model.role;

import java.util.Collection;
import java.util.List;

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
}

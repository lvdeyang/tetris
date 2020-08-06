package com.sumavision.tetris.bvc.business.terminal.hall;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ConferenceHallRolePermissionPO.class, idClass = Long.class)
public interface ConferenceHallRolePermissionDAO extends BaseDAO<ConferenceHallRolePermissionPO>{

	/**
	 * 查询授权<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月5日 下午3:24:36
	 * @param Long roleId 角色id
	 * @param Collection<Long> conferenceHallIds 会场id列表 
	 * @return List<ConferenceHallRolePermissionPO> 授权列表
	 */
	public List<ConferenceHallRolePermissionPO> findByRoleIdAndConferenceHallIdIn(Long roleId, Collection<Long> conferenceHallIds);
	
	/**
	 * 查询具体授权<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月5日 下午4:42:17
	 * @param Long roleId 角色id
	 * @param Long conferenceHallId 会场id
	 * @param PrivilegeType privilegeType 权限类型
	 * @return ConferenceHallRolePermissionPO 授权信息
	 */ 
	public ConferenceHallRolePermissionPO findByRoleIdAndConferenceHallIdAndPrivilegeType(Long roleId, Long conferenceHallId, PrivilegeType privilegeType);
	
}

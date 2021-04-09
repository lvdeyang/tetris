package com.sumavision.tetris.bvc.model.role;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = RolePO.class, idClass = Long.class)
public interface RoleDAO extends BaseDAO<RolePO>{

	/**
	 * 查询内置角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 下午1:51:12
	 * @return List<RolePO> 角色列表
	 */
	public List<RolePO> findByBusinessIdIsNullAndUserIdIsNull();
	
	/**
	 * 分页查询内置角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 下午1:51:12
	 * @param Pageable page 分页信息
	 * @return List<RolePO> 角色列表
	 */
	public Page<RolePO> findByBusinessIdIsNullAndUserIdIsNull(Pageable page);
	
	/**
	 * 统计内置角色数量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 下午1:51:50
	 * @return Long 角色数量
	 */
	public Long countByBusinessIdIsNullAndUserIdIsNull();
	
	/**
	 * 根据内置角色类型查询角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月20日 上午10:36:36
	 * @param InternalRoleType internalRoleType 角色类型
	 * @return RolePO 角色
	 */
	public RolePO findByInternalRoleType(InternalRoleType internalRoleType);
	
	/**
	 * 根据业务查询角色列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月26日 上午9:41:58
	 * @param Long businessId 业务id
	 * @return List<RolePO> 角色列表
	 */
	public List<RolePO> findByBusinessId(Long businessId);
	
	
	/**
	 * 根据id查询<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月22日 下午4:43:37
	 * @param ids
	 * @return
	 */
	public List<RolePO> findByIdIn(Collection<Long> ids);

	/**
	 * 通过会议组id以及角色成员关系查询<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月30日 上午11:56:52
	 * @param Long businessId 会议组id
	 * @param RoleUserMappingType type 1：1或者1：n角色成员关系
	 * @return
	 */
	public List<RolePO> findByBusinessIdAndRoleUserMappingType(Long businessId, RoleUserMappingType type);

	/**
	 * 按照关联业务删除角色
	 * @param businessId
	 */

	public void deleteByBusinessId(long businessId);

	/**
	 * 通过角色成员关系以及内置类型查询<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月30日 上午11:54:21
	 * @param RoleUserMappingType type 1：1或者1：n角色成员关系
	 * @param List<InternalRoleType> internalRoleTypeList 内置角色集合
	 */
	public List<RolePO> findByRoleUserMappingTypeAndInternalRoleTypeIn(RoleUserMappingType type, List<InternalRoleType> internalRoleTypeList);
}

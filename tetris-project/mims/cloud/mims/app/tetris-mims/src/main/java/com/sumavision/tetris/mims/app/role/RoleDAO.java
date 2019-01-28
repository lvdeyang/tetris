package com.sumavision.tetris.mims.app.role;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = RolePO.class, idClass = Long.class)
public interface RoleDAO extends BaseDAO<RolePO>{

	/**
	 * 获取内置公司管理员角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月28日 下午1:15:31
	 * @param String companyId 公司id
	 * @return RolePO 角色
	 */
	@Query(value = "SELECT role.* FROM mims_role role WHERE role.group_id=?1 AND role.classify='INTERNAL_COMPANY_ADMIN_ROLE'", nativeQuery = true)
	public RolePO findInternalCompanyAdminRole(String companyId);
	
	/**
	 * 查询组下的角色列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月21日 下午3:00:30
	 * @param String groupId 用户组id
	 * @return List<RolePO> 角色列表
	 */
	public List<RolePO> findByGroupIdOrderBySerialAsc(String groupId);
	
	/**
	 * 查询组下的角色列表（带有例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月11日 下午2:39:59
	 * @param String groupId 用户组id
	 * @param Collection<Long> ids 例外角色id列表
	 * @return List<RolePO> 角色列表
	 */
	public List<RolePO> findByGroupIdAndIdNotInOrderBySerialAsc(String groupId, Collection<Long> ids);
	
	/**
	 * 获取企业内权限<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月11日 下午4:02:29
	 * @param String groupId 企业id
	 * @param Collection<Long> ids 权限id
	 * @return List<RolePO> 权限数据
	 */
	public List<RolePO> findByGroupIdAndIdIn(String groupId, Collection<Long> ids);
	
	
	
}

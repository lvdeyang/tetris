package com.sumavision.tetris.mims.app.organization;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = OrganizationUserPermissionPO.class, idClass = Long.class)
public interface OrganizationUserPermissionDAO extends BaseDAO<OrganizationUserPermissionPO>{

	/**
	 * 根据用户获取用户组织机构映射关系<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月21日 下午3:34:09
	 * @param Collection<String> userIds 用户id列表
	 * @return List<OrganizationUserPermissionPO> 用户组织机构映射关系
	 */
	public List<OrganizationUserPermissionPO> findByUserIdIn(Collection<String> userIds);
	
	/**
	 * 根据部门id查询权限<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月7日 下午3:45:38
	 * @param Long organizationId 部门id
	 */
	public List<OrganizationUserPermissionPO> findByOrganizationId(Long organizationId);
	
	/**
	 * 获取已经绑定的用户id列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月7日 下午6:37:42
	 * @param String groupId 企业id
	 * @return List<String> 用户id列表
	 */
	@Query(value = "SELECT permission.user_id FROM mims_organization_user_permission permission LEFT JOIN mims_organization organization ON permission.organization_id=organization.id WHERE organization.group_id=?1", nativeQuery = true)
	public List<String> findPermissionedUser(String groupId);
	
	/**
	 * 根据用户和部门获取权限<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月8日 上午9:15:06
	 * @param Long organizationId 部门id
	 * @param String userId 用户id
	 * @return List<OrganizationUserPermissionPO> 权限数据
	 */
	public List<OrganizationUserPermissionPO> findByOrganizationIdAndUserId(Long organizationId, String userId);
	
}

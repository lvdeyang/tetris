package com.sumavision.tetris.organization;

import java.util.List;
import java.util.Map;

import org.neo4j.cypher.internal.compiler.v2_1.docbuilders.internalDocBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * 部门用户映射查询操作<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月24日 下午6:25:56
 */
@Component
public class OrganizationUserPermissionQuery {

	@Autowired
	private OrganizationUserPermissionDAO organizationUserPermissionDao;
	
	public Map<String, Object> listByOrganizationId(Long organizationId, int currentPage, int pageSize) throws Exception{
		
		int total = organizationUserPermissionDao.countByOrganization(organizationId);
		
		
		
	}
	
	/**
	 * 分页查询部门下的用户列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月24日 下午6:28:39
	 * @param Long organizationId 部门id
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return List<OrganizationUserPermissionPO> 权限列表
	 */
	public List<OrganizationUserPermissionPO> findByOrganizationId(Long organizationId, int currentPage, int pageSize){
		Pageable page = new PageRequest(currentPage -1, pageSize);
		Page<OrganizationUserPermissionPO> permissions = organizationUserPermissionDao.findByOrganizationId(organizationId, page);
		return permissions.getContent();
	}
	
}

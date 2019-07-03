package com.sumavision.tetris.subordinate.role;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = SubordinateRolePO.class, idClass = Long.class)
public interface SubordinateRoleDAO extends BaseDAO<SubordinateRolePO>{
	/**
	 * 隶属角色查询操作(过滤管理员角色)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月3日 上午11:34:06
	 * @param companyId 公司id
	 * @return List<SubordinateRolePO> 角色列表
	 */
	@Query(value = "select * from TETRIS_SUBORDINATE_ROLE where company_id = ?1 AND classify <> 'INTERNAL_COMPANY_ADMIN_ROLE'", nativeQuery = true)
	public List<SubordinateRolePO> findByCompanyId(Long companyId);
	
	public List<SubordinateRolePO> findByCompanyIdAndClassify(Long companyId, SubordinateRoleClassify classify);
	
	public SubordinateRolePO findById(Long id);
}

package com.sumavision.tetris.subordinate.role;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = SubordinateRolePO.class, idClass = Long.class)
public interface SubordinateRoleDAO extends BaseDAO<SubordinateRolePO>{
	
	/**
	 * 查询公司下的业务角色(过滤管理员角色)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月3日 上午11:34:06
	 * @param companyId 公司id
	 * @return List<SubordinateRolePO> 角色列表
	 */
	@Query(value = "SELECT * from TETRIS_SUBORDINATE_ROLE WHERE company_id = ?1 AND classify <> 'INTERNAL_COMPANY_ADMIN_ROLE'", nativeQuery = true)
	public List<SubordinateRolePO> findByCompanyId(Long companyId);
	
	/**
	 * 查询公司下的业务角色（带例外且过滤管理员角色）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月16日 上午8:45:09
	 * @param Long companyId 公司id
	 * @param Collection<Long> ids 例外角色id列表
	 * @return List<SubordinateRolePO> 角色列表
	 */
	@Query(value = "SELECT * from TETRIS_SUBORDINATE_ROLE WHERE company_id = ?1 AND classify <> 'INTERNAL_COMPANY_ADMIN_ROLE' AND id NOT IN ?2", nativeQuery = true)
	public List<SubordinateRolePO> findByCompanyIdWithExcept(Long companyId, Collection<Long> ids);
	
	/**
	 * 查询公司和分类查询角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月12日 下午4:53:10
	 * @param Long companyId 公司id
	 * @param SubordinateRoleClassify classify 分类
	 * @return List<SubordinateRolePO> 角色列表
	 */
	public List<SubordinateRolePO> findByCompanyIdAndClassify(Long companyId, SubordinateRoleClassify classify);
	
	/**
	 * 根据id查询角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月12日 下午4:53:02
	 * @param Collection<Long> ids 角色id列表
	 * @return List<SubordinateRolePO> 角色列表
	 */
	public List<SubordinateRolePO> findByIdIn(Collection<Long> ids);
}

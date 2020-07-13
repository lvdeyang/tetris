package com.sumavision.tetris.bvc.model.role;

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
	
}

package com.sumavision.tetris.system.role;

import java.util.Collection;
import java.util.List;
import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = SystemRolePO.class, idClass = Long.class)
public interface SystemRoleDAO extends BaseDAO<SystemRolePO>{

	/**
	 * 查询所有的系统角色（带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月17日 上午11:27:24
	 * @param Collection<Long> ids 例外角色id列表
	 * @return List<SystemRolePO> 系统角色列表
	 */
	public List<SystemRolePO> findByIdNotIn(Collection<Long> ids);
	
}

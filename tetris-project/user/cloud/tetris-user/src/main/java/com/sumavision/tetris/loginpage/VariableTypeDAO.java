/**
 * 
 */
package com.sumavision.tetris.loginpage;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

/**
 * 类型概述<br/>
 * <b>作者:</b>zhouaining<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2021年1月14日 下午1:23:07
 */
@RepositoryDefinition(domainClass = VariableTypePO.class, idClass = Long.class)
public interface VariableTypeDAO extends BaseDAO<VariableTypePO>{
	
	public List<VariableTypePO> findByIdIn(Collection<Long> ids);

}

/**
 * 
 */
package com.sumavision.tetris.loginpage;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

/**
 * 类型概述<br/>
 * <b>作者:</b>zhouaining<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2021年1月14日 下午1:23:53
 */
@RepositoryDefinition(domainClass = VariablePO.class, idClass = Long.class)
public interface VariableDAO extends BaseDAO<VariablePO>{
	
}

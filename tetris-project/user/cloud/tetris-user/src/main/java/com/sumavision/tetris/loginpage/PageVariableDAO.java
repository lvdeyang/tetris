/**
 * 
 */
package com.sumavision.tetris.loginpage;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

import antlr.collections.List;

/**
 * 类型概述<br/>
 * <b>作者:</b>zhouaining<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2021年1月14日 下午1:22:35
 */
@RepositoryDefinition(domainClass = PageVariablePO.class, idClass = Long.class)
public interface PageVariableDAO extends BaseDAO<PageVariablePO>{
	
	
}

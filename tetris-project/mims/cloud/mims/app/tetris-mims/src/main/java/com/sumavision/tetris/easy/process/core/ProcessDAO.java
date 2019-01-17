package com.sumavision.tetris.easy.process.core;

import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ProcessPO.class, idClass = Long.class)
public interface ProcessDAO extends BaseDAO<ProcessPO>{

	/**
	 * 根据自定义流程id查询流程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月24日 下午5:38:11
	 * @param String processId 流程id
	 * @return ProcessPO 流程数据
	 */
	public ProcessPO findByProcessId(String processId);
	
}

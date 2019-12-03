package com.sumavision.tetris.business.common.dao;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.business.common.po.TaskOutputPO;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = TaskOutputPO.class, idClass = Long.class)
public interface TaskOutputDAO extends BaseDAO<TaskOutputPO>{

	public TaskOutputPO findByTaskUuid(String taskUuid);
	
	public List<TaskOutputPO> findByInputId(Long id);
	
}

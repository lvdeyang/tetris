package com.sumavision.tetris.business.common.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.business.common.po.TaskInputPO;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = TaskInputPO.class, idClass = Long.class)
public interface TaskInputDAO extends BaseDAO<TaskInputPO>{

	public TaskInputPO findByTaskUuid(String taskUuid);
	
	public TaskInputPO findByUniq(String uniq);
	
	public List<TaskInputPO> findByCount(Integer count);
	
	@Modifying
	@Query("update TaskInputPO input set input.version = ?2 + 1, input.count = ?3 where input.taskUuid = ?1 and input.version = ?2")
	public int update(String taskUuid, Integer version, Integer count);
	
}

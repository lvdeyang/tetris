package com.sumavision.tetris.sts.task;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.sts.common.CommonDao;


@RepositoryDefinition(domainClass = TaskGroupPO.class, idClass = Long.class)
public interface TaskGroupDao extends CommonDao<TaskGroupPO>{

	@Query(value = "select id from TaskGroupPO")
	public List<Long> findAllId();
	
	@Query(value = "select id from TaskGroupPO t where t.groupName = ?1  ")
	public Long findIdByGroupName(String groupName);
	
	public List<TaskGroupPO> findByIdIn(List<Long> ids);
	
	public TaskGroupPO findTopByGroupName(String groupName);
}

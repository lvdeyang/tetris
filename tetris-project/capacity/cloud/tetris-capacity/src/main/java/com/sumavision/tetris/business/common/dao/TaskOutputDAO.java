package com.sumavision.tetris.business.common.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.business.common.enumeration.BusinessType;
import com.sumavision.tetris.business.common.po.TaskOutputPO;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = TaskOutputPO.class, idClass = Long.class)
public interface TaskOutputDAO extends BaseDAO<TaskOutputPO>{

	public TaskOutputPO findByTaskUuidAndType(String taskUuid, BusinessType type);
	public TaskOutputPO findByCapacityIpAndTaskUuidAndType(String deviceIp,String taskUuid, BusinessType type);

	public List<TaskOutputPO> findByTaskUuidInAndType(Collection<String> taskUuids, BusinessType type);
	public List<TaskOutputPO> findByType(BusinessType type);
	public List<TaskOutputPO> findByCapacityIpAndType(String deviceIp,BusinessType type);

	public List<TaskOutputPO> findByInputId(Long id);
	
	public List<TaskOutputPO> findByCapacityIp(String capacityIp);

	public List<TaskOutputPO> findByTaskUuidNotAndTaskUuidNotNullAndOutputNotNullAndTaskNotNull(String taskId);
	public Integer countDistinctByInputIdAndTaskUuidNotAndTaskUuidNotNullAndOutputNotNullAndTaskNotNull(Long inputId,String taskId);
}

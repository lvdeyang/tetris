package com.sumavision.tetris.business.common.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.business.common.enumeration.BusinessType;
import com.sumavision.tetris.business.common.po.TaskOutputPO;
import com.sumavision.tetris.orm.dao.BaseDAO;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;

@RepositoryDefinition(domainClass = TaskOutputPO.class, idClass = Long.class)
public interface TaskOutputDAO extends BaseDAO<TaskOutputPO>{

	public List<TaskOutputPO> findByTaskUuid(String taskUuid);
	public TaskOutputPO findByTaskUuidAndType(String taskUuid, BusinessType type);
	public TaskOutputPO findByCapacityIpAndTaskUuidAndType(String deviceIp,String taskUuid, BusinessType type);

	public List<TaskOutputPO> findByTaskUuidInAndType(Collection<String> taskUuids, BusinessType type);
	public List<TaskOutputPO> findByType(BusinessType type);
	public List<TaskOutputPO> findByCapacityIpAndType(String deviceIp,BusinessType type);

	public List<TaskOutputPO> findByInputId(Long id);
	
	public List<TaskOutputPO> findByCapacityIp(String capacityIp);
	public Integer countDistinctByCapacityIpAndOutputNotNullAndTaskNotNull(String capacityIp);
	public List<TaskOutputPO> findByCapacityIpAndSyncStatus(String capacityIp, Integer syncStatus);

	public List<TaskOutputPO> findByTaskUuidNotAndTaskUuidNotNullAndOutputNotNullAndTaskNotNull(String taskId);
	public List<TaskOutputPO> findByTaskUuidNotNullAndOutputNotNullAndTaskNotNull();

	public Integer countDistinctByInputIdAndTaskUuidNotAndTaskUuidNotNullAndOutputNotNullAndTaskNotNull(Long inputId,String taskId);

	@Transactional(propagation = Propagation.REQUIRED)
	@Modifying(clearAutomatically = true)
	@Query("update TaskOutputPO output set output.syncStatus = ?2 where output.id = ?1")
	public void updateSyncStatusById(Long id, Integer syncStatus);

	@Transactional(propagation = Propagation.REQUIRED)
	@Modifying(clearAutomatically = true)
	@Query("update TaskOutputPO output set output.capacityIp = ?2 where output.capacityIp = ?1")
	public void updateCapacityIpByIp(String srcIp, String tgtIp);


}

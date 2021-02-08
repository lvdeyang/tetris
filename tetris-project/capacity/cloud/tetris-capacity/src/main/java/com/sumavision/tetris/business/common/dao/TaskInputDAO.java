package com.sumavision.tetris.business.common.dao;

import java.util.Collection;
import java.util.List;

import org.hibernate.annotations.Cache;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.business.common.enumeration.BusinessType;
import com.sumavision.tetris.business.common.po.TaskInputPO;
import com.sumavision.tetris.orm.dao.BaseDAO;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RepositoryDefinition(domainClass = TaskInputPO.class, idClass = Long.class)
public interface TaskInputDAO extends BaseDAO<TaskInputPO>{

	public List<TaskInputPO> findByTaskUuidAndType(String taskUuid, BusinessType type);

	public TaskInputPO findByUniq(String uniq);
	
	public TaskInputPO findTopByUniq(String uniq);
	
//	@Query(value = "select * from TETRIS_CAPACITY_TASK_INPUT where uniq=?1", nativeQuery=true)
//	public List<TaskInputPO> findTopByUniq(String uniq);
	
	//悲观
	@Query(value = "SELECT input.* FROM TETRIS_CAPACITY_TASK_INPUT input WHERE input.uniq = ?1 for update", nativeQuery = true)
	public TaskInputPO selectByUniq(String uniq);
	
	public List<TaskInputPO> findByCount(Integer count);
	
	public List<TaskInputPO> findByIdIn(Collection<Long> ids);

	public List<TaskInputPO> findByUrlNotNull();

	//乐观
	@Modifying
	@Query("update TaskInputPO input set input.version = ?2 + 1, input.count = ?3 where input.taskUuid = ?1 and input.version = ?2")
	public int update(String taskUuid, Integer version, Integer count);
	
	public TaskInputPO findByTypeAndTaskUuid(BusinessType type, String taskUuid);

	public List<TaskInputPO> findByType(BusinessType type);

	public List<TaskInputPO> findByTypeAndCapacityIp(BusinessType type, String capacityIp);

	@Transactional(propagation = Propagation.REQUIRED)
	@Modifying(clearAutomatically = true)
	@Query("update TaskInputPO input set input.syncStatus = ?2 where input.id = ?1")
	public void updateSyncStatusById(Long id, Integer syncStatus);

	@Transactional(propagation = Propagation.REQUIRED)
	@Modifying(clearAutomatically = true)
	@Query("update TaskInputPO input set input.analysis = ?2 where input.id = ?1")
	public void updateAnalysisById(Long id, Integer syncStatus);

	@Transactional(propagation = Propagation.REQUIRED)
	@Modifying(clearAutomatically = true)
	@Query("update TaskInputPO input set input.count= ?2, input.syncStatus = ?3 where input.id = ?1")
	public void updateCountAndSyncStatusById(Long id, Integer count, Integer syncStatus);


	@Transactional(propagation = Propagation.REQUIRED)
	@Modifying(clearAutomatically = true)
	@Query("update TaskInputPO input set input.capacityIp = ?2 where input.capacityIp = ?1")
	public void updateCapacityIpByIp(String srcIp, String tgtIp);
}

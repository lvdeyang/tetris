package com.sumavision.tetris.sts.task.tasklink;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.sts.common.CommonDao;
import com.sumavision.tetris.sts.task.tasklink.TaskLinkPO.TaskLinkStatus;


@RepositoryDefinition(domainClass = TaskLinkPO.class, idClass = Long.class)
public interface TaskLinkDao extends CommonDao<TaskLinkPO>{
	
	public List<TaskLinkPO> findBySourceId(Long sourceId);
	
	public List<TaskLinkPO> findBySourceIdAndProgramNum(Long sourceId,Integer programNum);
	
	public List<TaskLinkPO> findByTaskGroupId(Long taskGroupId);
	
	public List<TaskLinkPO> findByCurDeviceNodeId(Long deviceNodeId);
	
	public List<TaskLinkPO> findByDeviceGroupId(Long deviceGroupId);
	
	public List<TaskLinkPO> findByLinkName(String linkName);
	
	public List<TaskLinkPO> findByLinkStatus(TaskLinkStatus linkStatus);
	
	public List<TaskLinkPO> findByPreview(Integer preview);
	
	public TaskLinkPO findTopBySourceIdAndProgramNumAndPreview(Long sourceId,Integer programNum,Integer preview);

	public List<TaskLinkPO> findByLinkStatusAndCurDeviceNodeIdAndUseInpub(TaskLinkStatus linkStatus,Long curDeviceNodeId,Boolean useInPub);

	@Query(value = "select max(id) from TaskLinkPO")
	public Long findMaxId();
	
	@Query(value = "select id from TaskLinkPO t where t.curDeviceNodeId = ?1")
	public List<Long> findIdsByCurDeviceNodeId(Long deviceNodeId);

	@Query(value = "select id from TaskLinkPO t where t.curDeviceNodeId = ?1 and t.linkStatus = ?2")
	public List<Long> findIdsByCurDeviceNodeIdAndLinkStatus(Long deviceNodeId,TaskLinkStatus linkStatus);
	
	@Query(value = "select distinct t from TaskLinkPO t where preview = 0 and linkName like %?1% or sourceName like %?1% or programName like %?1%")
	public Page<TaskLinkPO> findByKeyword(String keyword,Pageable pageRequest);
	
	@Query(value = "select distinct t from TaskLinkPO t where preview = 0 and (linkName like %?1% or sourceName like %?1% or programName like %?1%) and taskGroupId in ?2 and curDeviceNodeId in ?3 and deviceGroupId in ?4")
	public Page<TaskLinkPO> findByKeyword(String keyword,List<Long> groupIds,List<Long> deviceNodeIdList,Pageable pageRequest,List<Long> deviceGroupIds);
	
	@Query(value = "select distinct t from TaskLinkPO t where preview = 0 and (linkName like %?1% or sourceName like %?1% or programName like %?1%) and  taskGroupId in ?2 and curDeviceNodeId=?3 and deviceGroupId in ?4")
	public Page<TaskLinkPO> findByKeyword(String keyword,List<Long> groupIds,Long deviceNodeId,Pageable pageRequest,List<Long> deviceGroupIds);
	
	@Query(value = "select distinct t from TaskLinkPO t where preview = 0 and (linkName like %?1% or sourceName like %?1% or programName like %?1%) and taskGroupId in ?2 and curDeviceNodeId in ?3 and id in ?4")
	public Page<TaskLinkPO> findByKeyword(String keyword,List<Long> groupIds,List<Long> deviceNodeIdList,List<Long> taskLinkIds,Pageable pageRequest);
	
	@Query(value = "select distinct t from TaskLinkPO t where preview = 0 and (linkName like %?1% or sourceName like %?1% or programName like %?1%) and taskGroupId in ?2 and deviceGroupId in ?3 and curDeviceNodeId in ?4 and alarmFlag = ?5 and linkStatus = ?6")
	public Page<TaskLinkPO> findByKeyword(String keyword,List<Long> groupIds,List<Long> deviceGroupIds,List<Long> deviceNodeIdList,Boolean alarmFlag,TaskLinkStatus taskLinkStatus,Pageable pageRequest);

	@Query(value = "select distinct t from TaskLinkPO t where preview = 0 and (linkName like %?1% or sourceName like %?1% or programName like %?1%) and taskGroupId in ?2 and curDeviceNodeId in ?3 and alarmFlag = ?4 and linkStatus = ?5 and id in ?6")
	public Page<TaskLinkPO> findByKeyword(String keyword,List<Long> groupIds,List<Long> deviceNodeIdList,Boolean alarmFlag,TaskLinkStatus taskLinkStatus,List<Long> taskLinkIds,Pageable pageRequest);

	@Query(value = "select count(t) from TaskLinkPO t where curDeviceNodeId in ?1 and linkStatus = ?2 and alarmFlag = ?3")
	public Integer findCountByDeviceNodeIdAndLinkStatusAndAlarmFlag(List<Long> deviceNodeList, TaskLinkStatus linkStatus, Boolean alarmFlag);
	
	@Query(value = "select count(t) from TaskLinkPO t where deviceGroupId in ?1 and linkStatus = ?2")
	public Integer findCountBySdmListAndLinkStatus(List<Long> sdmIdList, TaskLinkStatus linkStatus);

	@Query(value = "select count(t) from TaskLinkPO t where deviceGroupId in ?1 and linkStatus = ?2 and alarmFlag = ?3")
	public Integer findCountBySdmListAndLinkStatusAndAlarmFlag(List<Long> sdmIdList, TaskLinkStatus linkStatus,Boolean alarmFlag);

	@Query(value = "select count(t) from TaskLinkPO t where deviceGroupId = ?1 and linkStatus = ?2")
	public Integer findCountBysdmGroupAndLinkStatus(Long deviceGroupId, TaskLinkStatus linkStatus);
	
	@Query(value = "select count(t) from TaskLinkPO t where deviceGroupId = ?1 and linkStatus = ?2 and alarmFlag = ?3")
	public Integer findCountBysdmGroupAndLinkStatusAndAlarmFlag(Long deviceGroupId,TaskLinkStatus linkStatus,Boolean alarmFlag);
	
	@Transactional(propagation = Propagation.REQUIRED)
    @Modifying(clearAutomatically = true)
	@Query(value = "update TaskLinkPO t set t.linkStatus = ?1 where t.id = ?2")
	public Integer updateLinkStatusById(TaskLinkStatus linkStatus, Long taskLinkId);

	@Transactional(propagation = Propagation.REQUIRED)
    @Modifying(clearAutomatically = true)
	@Query(value = "update TaskLinkPO t set t.linkStatus = ?1")
	public Integer updateAllLinkStatus(TaskLinkStatus linkStatus);
	
	@Transactional(propagation = Propagation.REQUIRED)
    @Modifying(clearAutomatically = true)
	@Query(value = "update TaskLinkPO t set t.linkName = ?1 ,t.taskGroupId = ?2 where t.id = ?3")
	public Integer updateLinkNameAndTaskGroupIdById(String linkName,Long taskGroupId, Long taskLinkId);
	
	@Transactional(propagation = Propagation.REQUIRED)
    @Modifying(clearAutomatically = true)
	@Query(value = "update TaskLinkPO t set t.alarmFlag = ?1 where t.id = ?2")
	public Integer updateAlarmFlagById(Boolean alarmFlag,Long taskLinkId);
	
	/*@Transactional(propagation = Propagation.REQUIRED)
	@Modifying(clearAutomatically = true)
	@Query(value = "update TaskLinkPO t set t.linkStatus = ?1 where t.sdmDeviceId = ?2")
	public Integer updateLinkStatusBySdmDeviceId(TaskLinkStatus linkStatus,Long sdmDeviceId );*/
	
	
	@Transactional(propagation = Propagation.REQUIRED)
    @Modifying(clearAutomatically = true)
	@Query(value = "update TaskLinkPO t set t.workSourceId = ?1,t.workSourceName = ?2,t.workProgramNum = ?3,t.workProgramName = ?4 where t.id = ?5")
	public Integer updateWorkSourceById(Long workSourceId,String workSourceName,Integer workProgramNum,String workProgramName, Long taskLinkId);
	
	@Transactional(propagation = Propagation.REQUIRED)
    @Modifying(clearAutomatically = true)
	@Query(value = "update TaskLinkPO t set t.errInfo = ?2 where t.id = ?1")
	public Integer updateErrInfoById(Long taskLinkId,String errInfo);
	
	@Transactional(propagation = Propagation.REQUIRED)
    @Modifying(clearAutomatically = true)
	@Query(value = "update TaskLinkPO t set t.taskParamDetail = ?2 where t.id = ?1")
	public Integer updateTaskParamDetailById(Long taskLinkId,String taskParamDetail);
	
	@Transactional(propagation = Propagation.REQUIRED)
    @Modifying(clearAutomatically = true)
	@Query(value = "update TaskLinkPO t set t.curDeviceNodeId = ?2 where t.id = ?1")
	public Integer updateTaskCurDeviceNodeIdById(Long taskLinkId,Long curDeviceNodeId);
	
	@Transactional(propagation = Propagation.REQUIRED)
    @Modifying(clearAutomatically = true)
	@Query(value = "update TaskLinkPO t set t.curDeviceNodeId = ?2,t.chooseDeviceNodeId = ?2 where t.id in ?1")
	public Integer updateTaskCurDeviceNodeIdInIds(Set<Long> taskLinkIds,Long curDeviceNodeId);

	@Transactional(propagation = Propagation.REQUIRED)
	@Modifying(clearAutomatically = true)
	@Query(value = "update TaskLinkPO t set t.curDeviceNodeId = 0,t.chooseDeviceNodeId = 0 where t.curDeviceNodeId = ?1")
	public Integer cleanTaskCurDeviceNodeId(Long curDeviceNodeId);

	@Query(value = "select linkStatus from TaskLinkPO t where t.id = ?1")
	public TaskLinkStatus findTasklinkStatusByTaskLinkId(Long taskLinkId);

	/**
     * 任务统计：RUN、CREATE_ERROR、STOP
     * @return count
     */
	@Query(value = "select count(*) from TaskLinkPO t where t.preview = 0")
	public int findTaskLinkCount();
	@Query(value = "select count(*) from TaskLinkPO t where t.preview = 0 and t.linkStatus = 'RUN' ")
	public int findTaskLinkRunCount();
	@Query(value = "select count(*) from TaskLinkPO t where t.preview = 0 and t.linkStatus = 'CREATE_ERROR' ")
	public int findTaskLinkCreatErrorCount();
	@Query(value = "select count(*) from TaskLinkPO t where t.preview = 0 and t.linkStatus = 'STOP' ")
	public int findTaskLinkStopCount();

	@Transactional(propagation = Propagation.REQUIRED)
	@Modifying(clearAutomatically = true)
	@Query(value = "update TaskLinkPO t set t.alarmFlag = ?1")
	public Integer updateAllAlarmFlag(Boolean alarmFlag);
}

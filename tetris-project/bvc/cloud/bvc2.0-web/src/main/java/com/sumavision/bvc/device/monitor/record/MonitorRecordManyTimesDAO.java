package com.sumavision.bvc.device.monitor.record;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass=MonitorRecordManyTimesPO.class, idClass=Long.class)
public interface MonitorRecordManyTimesDAO extends MetBaseDAO<MonitorRecordManyTimesPO> {

	@Query("from com.sumavision.bvc.device.monitor.record.MonitorRecordManyTimesPO record where record.status='RUN' and record.relationId in ?1")
	public List<MonitorRecordManyTimesPO> findNeedStop(Collection<Long> relationIds);
	
	public List<MonitorRecordManyTimesPO> findByRelationId(Long relationId);
	
	@Query(value="select * from BVC_MONITOR_RECORD_MANY_TIMES where relationId=?1 \n#pageable\n",
			countQuery="select COUNT(ID) from BVC_MONITOR_RECORD_MANY_TIMES where relationId=?1",
			nativeQuery=true)
	public Page<MonitorRecordManyTimesPO> findByRelation(Long relationId, Pageable page);
	
	/**
	 * 根据条件查询录制<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月17日 下午7:11:00
	 * @param String videoBundleId 录制设备id
	 * @param Date startTime 开始时间下限
	 * @param Date endTime 开始时间上限
	 * @param String userId 执行业务用户id
	 * @param String status 录制执行状态
	 * @param Pageable pageable 分页信息
	 * @return List<MonitorRecordPO> 录制列表
	 */
	@Query(
		value = "SELECT * FROM BVC_MONITOR_RECORD WHERE " + 
				"mode=?1 " + 
				"AND IF(?2 IS NULL OR ?2='', TRUE, VIDEO_BUNDLE_ID=?2) " + 
				"AND IF(?3 IS NULL OR ?3='', TRUE, START_TIME>=?3)" + 
				"AND IF(?4 IS NULL OR ?4='', TRUE, START_TIME<=?4)" + 
				"AND IF(?5 IS NULL OR ?5='', TRUE, USER_ID=?5) " +
				"AND IF(?7 IS NULL OR ?7='', TRUE, RECORD_USER_ID=?7) " +
				"AND IF(?8 IS NULL OR ?8='', TRUE, FILE_NAME like ?8) " +
				"AND STATUS<>?6 \n#pageable\n",
		countQuery = "SELECT COUNT(ID) FROM BVC_MONITOR_RECORD WHERE " + 
				"mode=?1 " + 
				"AND IF(?2 IS NULL OR ?2='', TRUE, VIDEO_BUNDLE_ID=?2) " + 
				"AND IF(?3 IS NULL OR ?3='', TRUE, START_TIME>=?3)" + 
				"AND IF(?4 IS NULL OR ?4='', TRUE, START_TIME<=?4)" + 
				"AND IF(?5 IS NULL OR ?5='', TRUE, USER_ID=?5) " +
				"AND IF(?7 IS NULL OR ?7='', TRUE, RECORD_USER_ID=?7) " +
				"AND IF(?8 IS NULL OR ?8='', TRUE, FILE_NAME like ?8) " +
				"AND STATUS<>?6",
		nativeQuery = true
	)
	public Page<MonitorRecordPO> findByConditions(
			String mode,
			String videoBundleId,
			Date startTime,
			Date endTime,
			Long userId,
			String status,
			Long recordUserId,
			String fileNameReg,
			Pageable pageable);
}

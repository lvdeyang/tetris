package com.sumavision.bvc.device.monitor.record;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = MonitorRecordPO.class, idClass = Long.class)
public interface MonitorRecordDAO extends MetBaseDAO<MonitorRecordPO>{

	/**
	 * 查询需要开始的定时录制<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月9日 上午8:54:45
	 * @param Date time 时间节点
	 * @return List<MonitorRecordPO> 录制任务列表
	 */
	@Query("from com.sumavision.bvc.device.monitor.record.MonitorRecordPO record where record.mode='SCHEDULING' and record.status='WAITING' and record.startTime<=?1 ")
	public List<MonitorRecordPO> findNeedStartSchedulingRecord(Date time);
	
	/**
	 * 查询需要开始的排期录制<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月17日 下午6:50:50
	 * @param time
	 * @return List<MonitorRecordPO> 录制任务id
	 */
	@Query("from com.sumavision.bvc.device.monitor.record.MonitorRecordPO record where record.mode='TIMESEGMENT' and record.status='WAITING' ")
	public List<MonitorRecordPO> findNeedStartSheduleRecordId();
	
	/**
	 * 查询需要停止的排期录制<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月9日 上午8:56:03
	 * @param Date time 时间节点
	 * @return List<MonitorRecordPO> 录制任务列表
	 */
	@Query("from com.sumavision.bvc.device.monitor.record.MonitorRecordPO record where record.mode='SCHEDULING' and record.status='RUN' and record.endTime<=?1")
	public List<MonitorRecordPO> findNeedStopSchedulingRecord(Date time);
	
	/**
	 * 根据录制时间区间以及设备id查询文件(过滤不能看的文件)<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月8日 下午2:38:08
	 * @param Collection<String> bundleIds 设备id列表
	 * @param Date timeLowerLimit 录制时间下限
	 * @param Date timeUpLimit 录制时间上限
	 * @return List<MonitorRecordPlaybackTaskPO> 文件列表
	 */
	public List<MonitorRecordPO> findByVideoBundleIdInAndStartTimeGreaterThanEqualAndStartTimeLessThanEqualAndStoreLayerIdIsNotNull(Collection<String> bundleIds, Date timeLowerLimit, Date timeUpLimit);
	
	/**
	 * 根据录制时间下限以及设备id查询文件(过滤不能看的文件)<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月8日 下午2:38:08
	 * @param Collection<String> bundleIds 设备id列表
	 * @param Date timeLowerLimit 录制时间下限
	 * @return List<MonitorRecordPlaybackTaskPO> 文件列表
	 */
	public List<MonitorRecordPO> findByVideoBundleIdInAndStartTimeGreaterThanEqualAndStoreLayerIdIsNotNull(Collection<String> bundleIds, Date timeLowerLimit);
	
	/**
	 * 根据录制时间上以及设备id查询文件(过滤不能看的文件)<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月8日 下午2:38:08
	 * @param Collection<String> bundleIds 设备id列表
	 * @param Date timeUpLimit 录制时间上
	 * @return List<MonitorRecordPlaybackTaskPO> 文件列表
	 */
	public List<MonitorRecordPO> findByVideoBundleIdInAndStartTimeLessThanEqualAndStoreLayerIdIsNotNull(Collection<String> bundleIds, Date timeUpLimit);
	
	/**
	 * 根据设备id列表查询文件(过滤不能看的文件)<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月8日 下午2:37:33
	 * @param Collection<String> bundleIds 设备id列表
	 * @return List<MonitorRecordPlaybackTaskPO> 文件列表
	 */
	public List<MonitorRecordPO> findByVideoBundleIdInAndStoreLayerIdIsNotNull(Collection<String> bundleIds);
	
	/**
	 * 根据文件名以及设备查询录制文件（不能调阅的都过滤了）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月8日 下午1:47:15
	 * @param Collection<String> bundleIds 设备列表
	 * @param String fileNameExpression 文件名表达式
	 * @return List<MonitorRecordPlaybackTaskPO> 文件列表
	 */
	public List<MonitorRecordPO> findByVideoBundleIdInAndFileNameLikeAndStoreLayerIdIsNotNull(Collection<String> bundleIds, String fileNameExpression);
	
	/**
	 * 根据文件名以及设备查询录制文件<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月8日 下午1:47:15
	 * @param Collection<String> bundleIds 设备列表
	 * @param String fileNameExpression 文件名表达式
	 * @param Pageable page 分页信息
	 * @return List<MonitorRecordPlaybackTaskPO> 文件列表
	 */
	public Page<MonitorRecordPO> findByVideoBundleIdInAndFileNameLike(Collection<String> bundleIds, String fileNameExpression, Pageable page);
	
	/**
	 * 根据文件名以及设备统计录制文件数量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月8日 下午1:49:14
	 * @param Collection<String> bundleIds 设备列表
	 * @param String fileNameExpression 文件名表达式
	 * @return int 文件数量
	 */
	public int countByVideoBundleIdInAndFileNameLike(Collection<String> bundleIds, String fileNameExpression);
	
	/**
	 * 根据uuid查询录制文件<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月5日 上午11:12:05
	 * @param String uuid 文件uuid
	 * @return MonitorRecordPO 录制文件
	 */
	public MonitorRecordPO findByUuid(String uuid);
	
	/**
	 * 根据预览地址查询录制文件<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月5日 上午11:12:05
	 * @param String previewUrl 预览地址
	 * @return MonitorRecordPO 录制文件
	 */
	public MonitorRecordPO findByPreviewUrl(String previewUrl);
	
	/**
	 * 查询设备录制的文件<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月14日 下午2:41:12
	 * @param String videoBundleId 设备id
	 * @return List<MonitorRecordPO> 录制文件列表
	 */
	public List<MonitorRecordPO> findByVideoBundleId(String videoBundleId);
	
	/**
	 * 分页查询设备录制的文件<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月24日 下午2:20:46
	 * @param String videoBundleId 设备id
	 * @param MonitorRecordStatus status 除去WAITING状态
	 * @param Pageable pageable 分页信息
	 * @return Page<MonitorRecordPO> 录制文件列表
	 */
	public Page<MonitorRecordPO> findByVideoBundleIdAndStatusNot(String videoBundleId, MonitorRecordStatus status, Pageable pageable);
	
	/**
	 * 统计设备录制文件的数量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月24日 下午2:29:08
	 * @param String videoBundleId 设备id
	 * @param MonitorRecordStatus status 除去WAITING状态
	 * @return int 统计结果
	 */
	public int countByVideoBundleIdAndStatusNot(String videoBundleId, MonitorRecordStatus status);
	
	public Page<MonitorRecordPO> findByVideoBundleIdAndStatusNotAndStartTimeGreaterThanEqual(String videoBundleId, MonitorRecordStatus status, Date scopeStartTime, Pageable pageable);
	
	public int countByVideoBundleIdAndStatusNotAndStartTimeGreaterThanEqual(String videoBundleId, MonitorRecordStatus status, Date scopeStartTime);
	
	public Page<MonitorRecordPO> findByVideoBundleIdAndStatusNotAndStartTimeLessThanEqual(String videoBundleId, MonitorRecordStatus status, Date scopeEndTime, Pageable pageable);
	
	public int countByVideoBundleIdAndStatusNotAndStartTimeLessThanEqual(String videoBundleId, MonitorRecordStatus status, Date scopeEndTime);
	
	public Page<MonitorRecordPO> findByVideoBundleIdAndStatusNotAndStartTimeBetween(String videoBundleId, MonitorRecordStatus status, Date scopeStartTime, Date scopeEndTime, Pageable pageable);
	
	public int countByVideoBundleIdAndStatusNotAndStartTimeBetween(String videoBundleId, MonitorRecordStatus status, Date scopeStartTime, Date scopeEndTime);
	
	/**
	 * 分页查询xt设备录制文件<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月1日 下午4:56:11
	 * @param String videoBundleIdExpression bundleId_%
	 * @param MonitorRecordStatus status 除去WAITING状态
	 * @return Page<MonitorRecordPO> 录制文件列表
	 */
	public Page<MonitorRecordPO> findByVideoBundleIdLikeAndStatusNot(String videoBundleIdExpression, MonitorRecordStatus status, Pageable pageable);
	
	/**
	 * 统计xt设备录制文件数量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月1日 下午4:57:04
	 * @param String videoBundleIdExpression bundleId_%
	 * @param MonitorRecordStatus status 除去WAITING状态
	 * @return int 统计结果
	 */
	public int countByVideoBundleIdLikeAndStatusNot(String videoBundleIdExpression, MonitorRecordStatus status);
	
	public Page<MonitorRecordPO> findByVideoBundleIdLikeAndStatusNotAndStartTimeGreaterThanEqual(String videoBundleIdExpression, MonitorRecordStatus status, Date scopeStartTime, Pageable pageable);
	
	public int countByVideoBundleIdLikeAndStatusNotAndStartTimeGreaterThanEqual(String videoBundleIdExpression, MonitorRecordStatus status, Date scopeStartTime);
	
	public Page<MonitorRecordPO> findByVideoBundleIdLikeAndStatusNotAndStartTimeLessThanEqual(String videoBundleIdExpression, MonitorRecordStatus status, Date scopeEndTime, Pageable pageable);
	
	public int countByVideoBundleIdLikeAndStatusNotAndStartTimeLessThanEqual(String videoBundleIdExpression, MonitorRecordStatus status, Date scopeEndTime);
	
	public Page<MonitorRecordPO> findByVideoBundleIdLikeAndStatusNotAndStartTimeBetween(String videoBundleIdExpression, MonitorRecordStatus status, Date scopeStartTime, Date scopeEndTime, Pageable pageable);
	
	public int countByVideoBundleIdLikeAndStatusNotAndStartTimeBetween(String videoBundleIdExpression, MonitorRecordStatus status, Date scopeStartTime, Date scopeEndTime);
	
	/**
	 * 分页查询用户录制文件<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月1日 下午4:59:24
	 * @param Long userId 用户id
	 * @param MonitorRecordStatus status 除去WAITING状态
	 * @return Page<MonitorRecordPO> 录制文件列表
	 */
	public Page<MonitorRecordPO> findByRecordUserIdAndStatusNot(Long userId, MonitorRecordStatus status, Pageable pageable);
	
	/**
	 * 统计用户录制文件数量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月1日 下午5:00:11
	 * @param Long userId 用户id
	 * @param MonitorRecordStatus status 除去WAITING状态
	 * @return int 统计结果
	 */
	public int countByRecordUserIdAndStatusNot(Long userId, MonitorRecordStatus status);
	
	public Page<MonitorRecordPO> findByRecordUserIdAndStatusNotAndStartTimeGreaterThanEqual(Long userId, MonitorRecordStatus status, Date scopeStartTime, Pageable pageable);
	
	public int countByRecordUserIdAndStatusNotAndStartTimeGreaterThanEqual(Long userId, MonitorRecordStatus status, Date scopeStartTime);
	
	public Page<MonitorRecordPO> findByRecordUserIdAndStatusNotAndStartTimeLessThanEqual(Long userId, MonitorRecordStatus status, Date scopeEndTime, Pageable pageable);
	
	public int countByRecordUserIdAndStatusNotAndStartTimeLessThanEqual(Long userId, MonitorRecordStatus status, Date scopeEndTime);
	
	public Page<MonitorRecordPO> findByRecordUserIdAndStatusNotAndStartTimeBetween(Long userId, MonitorRecordStatus status, Date scopeStartTime, Date scopeEndTime, Pageable pageable);
	
	public int countByRecordUserIdAndStatusNotAndStartTimeBetween(Long userId, MonitorRecordStatus status, Date scopeStartTime, Date scopeEndTime);
	
	/**
	 * 根据条件查询正在运行的录制<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月17日 下午7:11:00
	 * @param String mode 录制模式
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
	
	/**
	 * 根据条件查询所有未完成的录制（status决定）<br/>
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
				"mode IS NOT NULL " + 
				"AND IF(?1 IS NULL OR ?1='', TRUE, FILE_NAME like ?1) " +
				"AND IF(?2 IS NULL OR ?2='', TRUE, VIDEO_BUNDLE_ID=?2) " + 
				"AND IF(?3 IS NULL OR ?3='', TRUE, START_TIME>=?3)" + 
				"AND IF(?4 IS NULL OR ?4='', TRUE, START_TIME<=?4)" + 
				"AND IF(?5 IS NULL OR ?5='', TRUE, USER_ID=?5) " +
				"AND IF(?7 IS NULL OR ?7='', TRUE, RECORD_USER_ID=?7) " + 
				"AND STATUS<>?6 \n#pageable\n",
		countQuery = "SELECT COUNT(ID) FROM BVC_MONITOR_RECORD WHERE " + 
				"mode IS NOT NULL " + 
				"AND IF(?1 IS NULL OR ?1='', TRUE, FILE_NAME like ?1) "+
				"AND IF(?2 IS NULL OR ?2='', TRUE, VIDEO_BUNDLE_ID=?2) " + 
				"AND IF(?3 IS NULL OR ?3='', TRUE, START_TIME>=?3)" + 
				"AND IF(?4 IS NULL OR ?4='', TRUE, START_TIME<=?4)" + 
				"AND IF(?5 IS NULL OR ?5='', TRUE, USER_ID=?5) " +
				"AND IF(?7 IS NULL OR ?7='', TRUE, RECORD_USER_ID=?7) " +
				"AND STATUS<>?6",
		nativeQuery = true
	)
	public Page<MonitorRecordPO> findAllByConditions(
//			String mode,
			String fileNameReg,
			String videoBundleId,
			Date startTime,
			Date endTime,
			Long userId,
			String status,
			Long recordUserId,
			Pageable pageable);
	
	/**
	 * 根据条件查询mode对应所有状态录制任务<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月17日 下午7:11:00
	 * @param String mode 录制模式
	 * @param String videoBundleId 录制设备id
	 * @param Date startTime 开始时间下限
	 * @param Date endTime 开始时间上限
	 * @param String userId 执行业务用户id
	 * @param String status 录制执行状态
	 * @param String fileNameReg 录制任务名
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
				"AND IF(?6 IS NULL OR ?6='', TRUE, STATUS =?6)" +
				"AND IF(?7 IS NULL OR ?7='', TRUE, RECORD_USER_ID=?7) " +
				"AND IF(?8 IS NULL OR ?8='', TRUE, FILE_NAME like ?8) " +
				"\n#pageable\n",
		countQuery = "SELECT COUNT(ID) FROM BVC_MONITOR_RECORD WHERE " + 
				"mode=?1 " + 
				"AND IF(?2 IS NULL OR ?2='', TRUE, VIDEO_BUNDLE_ID=?2) " + 
				"AND IF(?3 IS NULL OR ?3='', TRUE, START_TIME>=?3)" + 
				"AND IF(?4 IS NULL OR ?4='', TRUE, START_TIME<=?4)" + 
				"AND IF(?5 IS NULL OR ?5='', TRUE, USER_ID=?5) " +
				"AND IF(?6 IS NULL OR ?6='', TRUE, STATUS =?6)" +
				"AND IF(?7 IS NULL OR ?7='', TRUE, RECORD_USER_ID=?7) " +
				"AND IF(?8 IS NULL OR ?8='', TRUE, FILE_NAME like ?8) " ,
		nativeQuery = true
	)
	public Page<MonitorRecordPO> findByConditionsAndStatus(
			String mode,
			String videoBundleId,
			Date startTime,
			Date endTime,
			Long userId,
			String status,
			Long recordUserId,
			String fileNameReg,
			Pageable pageable);
	
	/**
	 * 根据条件查询所有mode的所有状态录制任务<br/>
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
				"mode IS NOT NULL " + 
				"AND IF(?1 IS NULL OR ?1='', TRUE, FILE_NAME like ?1) " +
				"AND IF(?2 IS NULL OR ?2='', TRUE, VIDEO_BUNDLE_ID=?2) " + 
				"AND IF(?3 IS NULL OR ?3='', TRUE, START_TIME>=?3)" + 
				"AND IF(?4 IS NULL OR ?4='', TRUE, START_TIME<=?4)" + 
				"AND IF(?5 IS NULL OR ?5='', TRUE, USER_ID=?5) " +
				"AND IF(?6 IS NULL OR ?6='', TRUE, STATUS =?6) " +
				"AND IF(?7 IS NULL OR ?7='', TRUE, RECORD_USER_ID=?7) " + 
				"\n#pageable\n",
		countQuery = "SELECT COUNT(ID) FROM BVC_MONITOR_RECORD WHERE " + 
				"mode IS NOT NULL " + 
				"AND IF(?1 IS NULL OR ?1='', TRUE, FILE_NAME like ?1) "+
				"AND IF(?2 IS NULL OR ?2='', TRUE, VIDEO_BUNDLE_ID=?2) " + 
				"AND IF(?3 IS NULL OR ?3='', TRUE, START_TIME>=?3)" + 
				"AND IF(?4 IS NULL OR ?4='', TRUE, START_TIME<=?4)" + 
				"AND IF(?5 IS NULL OR ?5='', TRUE, USER_ID=?5) " +
				"AND IF(?6 IS NULL OR ?6='', TRUE, STATUS =?6) " +
				"AND IF(?7 IS NULL OR ?7='', TRUE, RECORD_USER_ID=?7) " ,
		nativeQuery = true
	)
	public Page<MonitorRecordPO> findAllByConditionsAndStatus(
			String fileNameReg,
			String videoBundleId,
			Date startTime,
			Date endTime,
			Long userId,
			String status,
			Long recordUserId,
			Pageable pageable);
	
	/**
	 * 根据条件查询mode对应所有状态录制任务<br/>//注释都要修改
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月17日 下午7:11:00
	 * @param String mode 录制模式
	 * @param String videoBundleId 录制设备id
	 * @param Date startTime 开始时间下限
	 * @param Date endTime 开始时间上限
	 * @param Set<String> bundleIdSet bundleid集合
	 * @param String status 录制执行状态
	 * @param String fileNameReg 录制任务名
	 * @param Pageable pageable 分页信息
	 * @return List<MonitorRecordPO> 录制列表
	 */
	@Query(
		value = "SELECT * FROM BVC_MONITOR_RECORD WHERE " + 
				"1=1 "+
				"AND IF(?1 IS NULL OR ?1='' ,TRUE, MODE=?1) " +
				"AND IF(?2 IS NULL OR ?2='', TRUE, VIDEO_BUNDLE_ID=?2) " + 
				"AND IF(?3 IS NULL OR ?3='', TRUE, START_TIME>=?3) " + 
				"AND IF(?4 IS NULL OR ?4='', TRUE, END_TIME<=?4) " + 
				"AND (coalesce (?5 , null) is null or VIDEO_BUNDLE_ID in ?5) " +
				"AND IF(?6 IS NULL OR ?6='', TRUE, STATUS =?6) " +
				"AND IF(?7 IS NULL OR ?7='', TRUE, RECORD_USER_ID=?7) " +
				"AND IF(?8 IS NULL OR ?8='', TRUE, FILE_NAME like ?8) " +
				"AND IF(?9 IS NULL OR ?9='', TRUE, TASK_NAME=?9) " +
				"\n#pageable\n",
		countQuery = "SELECT COUNT(ID) FROM BVC_MONITOR_RECORD WHERE " +
				"1=1 "+
				"AND IF(?1 IS NULL OR ?1='' ,TRUE, MODE=?1) " + 
				"AND IF(?2 IS NULL OR ?2='', TRUE, VIDEO_BUNDLE_ID=?2) " + 
				"AND IF(?3 IS NULL OR ?3='', TRUE, START_TIME>=?3) " + 
				"AND IF(?4 IS NULL OR ?4='', TRUE, END_TIME<=?4) " + 
				"AND (coalesce (?5 , null) is null or VIDEO_BUNDLE_ID in ?5) " +
				"AND IF(?6 IS NULL OR ?6='', TRUE, STATUS =?6) " +
				"AND IF(?7 IS NULL OR ?7='', TRUE, RECORD_USER_ID=?7) " +
				"AND IF(?8 IS NULL OR ?8='', TRUE, FILE_NAME like ?8) " +
				"AND IF(?9 IS NULL OR ?9='', TRUE, TASK_NAME=?9) " ,
		nativeQuery = true
	)
	public Page<MonitorRecordPO> findByConditionsAndStatusAndBundleIdIn(
			String mode,
			String videoBundleId,
			Date startTime,
			Date endTime,
			Set<String> bundleIdSet,
			String status,
			Long recordUserId,
			String fileNameReg,
			String taskName,
			Pageable pageable);
	
	
	
	@Query(
			value = "SELECT * FROM BVC_MONITOR_RECORD WHERE " + 
					"1=1 "+
					"AND IF(?1 IS NULL OR ?1='' ,TRUE, MODE=?1) " +
					"AND IF(?2 IS NULL OR ?2='', TRUE, VIDEO_BUNDLE_ID=?2) " + 
					"AND IF(?3 IS NULL OR ?3='', TRUE, START_TIME>=?3) " + 
					"AND IF(?4 IS NULL OR ?4='', TRUE, END_TIME<=?4) " + 
					"AND IF(?5 IS NULL OR ?5='', TRUE, FILE_NAME like ?5) " +
					"AND IF(?6 IS NULL OR ?6='', TRUE, STATUS =?6) " +
					"AND IF(?7 IS NULL OR ?7='', TRUE, RECORD_USER_ID=?7) " +
					"AND IF(?8 IS NULL OR ?8='', TRUE, TASK_NAME=?8)" +
					"\n#pageable\n ",
			countQuery = "SELECT COUNT(ID) FROM BVC_MONITOR_RECORD WHERE " +
					"1=1 "+
					"AND IF(?1 IS NULL OR ?1='' ,TRUE, MODE=?1) " + 
					"AND IF(?2 IS NULL OR ?2='', TRUE, VIDEO_BUNDLE_ID=?2) " + 
					"AND IF(?3 IS NULL OR ?3='', TRUE, START_TIME>=?3) " + 
					"AND IF(?4 IS NULL OR ?4='', TRUE, END_TIME<=?4) " + 
					"AND IF(?5 IS NULL OR ?5='', TRUE, FILE_NAME like ?5) "+
					"AND IF(?6 IS NULL OR ?6='', TRUE, STATUS =?6) " +
					"AND IF(?7 IS NULL OR ?7='', TRUE, RECORD_USER_ID=?7) " +
					"AND IF(?8 IS NULL OR ?8='', TRUE, TASK_NAME=?8) ",
			nativeQuery = true
		)
		public Page<MonitorRecordPO> findAllByConditionsAndStatusAndBundleIdIn(
				String mode,
				String videoBundleId,
				Date startTime,
				Date endTime,
				String fileNameReg,
				String status,
				Long recordUserId,
				String taskName,
				Pageable pageable);
	
	public List<MonitorRecordPO> findByIdIn(Collection<Long> ids);
	
}

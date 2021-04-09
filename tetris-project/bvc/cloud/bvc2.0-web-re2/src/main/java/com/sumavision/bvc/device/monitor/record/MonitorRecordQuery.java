package com.sumavision.bvc.device.monitor.record;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

/**
 * 录制查询业务<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年4月16日 下午8:26:17
 */
@Component
public class MonitorRecordQuery {

	@Autowired
	private MonitorRecordDAO monitorRecordDao;
	
	/**
	 * 根据文件名以及设备查询录制文件(过滤不能调阅的文件)<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月8日 下午1:47:15
	 * @param Collection<String> bundleIds 设备列表
	 * @param String fileName 文件名表达式
	 * @return List<MonitorRecordPO> 文件列表
	 */
	public List<MonitorRecordPO> findByVideoBundleIdInAndFileNameLike(
			Collection<String> bundleIds, 
			String fileName) throws Exception{
		
		String fileNameExpression = new StringBufferWrapper().append("%")
				 .append(fileName)
				 .append("%")
				 .toString();
		return monitorRecordDao.findByVideoBundleIdInAndFileNameLikeAndStoreLayerIdIsNotNull(bundleIds, fileNameExpression);
	}
	
	/**
	 * 根据文件名以及设备查询录制文件<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月8日 下午1:47:15
	 * @param Collection<String> bundleIds 设备列表
	 * @param String fileName 文件名表达式
	 * @param int currengPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return List<MonitorRecordPO> 文件列表
	 */
	public List<MonitorRecordPO> findByVideoBundleIdInAndFileNameLike(
			Collection<String> bundleIds, 
			String fileName, 
			int currentPage, 
			int pageSize) throws Exception{
		
		Pageable page = new PageRequest(currentPage-1, pageSize);
		String fileNameExpression = new StringBufferWrapper().append("%")
															 .append(fileName)
															 .append("%")
															 .toString();
		Page<MonitorRecordPO> pagedEntities = monitorRecordDao.findByVideoBundleIdInAndFileNameLike(bundleIds, fileNameExpression, page);
		
		return pagedEntities.getContent();
	}
	
	/**
	 * 根据文件名以及设备统计录制文件数量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月8日 下午1:49:14
	 * @param Collection<String> bundleIds 设备列表
	 * @param String fileName 文件名表达式
	 * @return int 文件数量
	 */
	public int countByVideoBundleIdInAndFileNameLike(
			Collection<String> bundleIds, 
			String fileName) throws Exception{
		String fileNameExpression = new StringBufferWrapper().append("%")
															 .append(fileName)
															 .append("%")
															 .toString();
		return monitorRecordDao.countByVideoBundleIdInAndFileNameLike(bundleIds, fileNameExpression);
	}
	
	/**
	 * 分页查询设备录制的文件<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月24日 下午2:20:46
	 * @param String videoBundleId 设备id
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return List<MonitorRecordPO> 录制文件列表
	 */
	public List<MonitorRecordPO> findByVideoBundleId(String videoBundleId, int currentPage, int pageSize){
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<MonitorRecordPO> monitorRecords = monitorRecordDao.findByVideoBundleIdAndStatusNot(videoBundleId, MonitorRecordStatus.WAITING, page);
		return monitorRecords.getContent();
	}
	
	public List<MonitorRecordPO> findByVideoBundleIdAndStartTimeGreaterThanEqual(String videoBundleId, Date scopeStartTime, int currentPage, int pageSize){
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<MonitorRecordPO> monitorRecords = monitorRecordDao.findByVideoBundleIdAndStatusNotAndStartTimeGreaterThanEqual(videoBundleId, MonitorRecordStatus.WAITING, scopeStartTime, page);
		return monitorRecords.getContent();
	}
	
	public List<MonitorRecordPO> findByVideoBundleIdAndStartTimeLessThanEqual(String videoBundleId, Date scopeEndTime, int currentPage, int pageSize){
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<MonitorRecordPO> monitorRecords = monitorRecordDao.findByVideoBundleIdAndStatusNotAndStartTimeLessThanEqual(videoBundleId, MonitorRecordStatus.WAITING, scopeEndTime, page);
		return monitorRecords.getContent();
	}
	
	public List<MonitorRecordPO> findByVideoBundleIdAndStartTimeBetween(String videoBundleId, Date scopeStartTime, Date scopeEndTime, int currentPage, int pageSize){
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<MonitorRecordPO> monitorRecords = monitorRecordDao.findByVideoBundleIdAndStatusNotAndStartTimeBetween(videoBundleId, MonitorRecordStatus.WAITING, scopeStartTime, scopeEndTime, page);
		return monitorRecords.getContent();
	}
	
	/**
	 * 分页查询xt设备录制文件<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月1日 下午5:05:02
	 * @param String videoBundleId 视频设备id
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return List<MonitorRecordPO> 录制文件列表
	 */
	public List<MonitorRecordPO> findByVideoBundleIdLike(String videoBundleId, int currentPage, int pageSize){
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<MonitorRecordPO> monitorRecords = monitorRecordDao.findByVideoBundleIdLikeAndStatusNot(new StringBufferWrapper().append(videoBundleId).append("_%").toString(), MonitorRecordStatus.WAITING, page);
		return monitorRecords.getContent();
	}
	
	public List<MonitorRecordPO> findByVideoBundleIdLikeAndStartTimeGreaterThanEqual(String videoBundleId, Date scopeStartTime, int currentPage, int pageSize){
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<MonitorRecordPO> monitorRecords = monitorRecordDao.findByVideoBundleIdLikeAndStatusNotAndStartTimeGreaterThanEqual(new StringBufferWrapper().append(videoBundleId).append("_%").toString(), MonitorRecordStatus.WAITING, scopeStartTime, page);
		return monitorRecords.getContent();
	}
	
	public List<MonitorRecordPO> findByVideoBundleIdLikeAndStartTimeLessThanEqual(String videoBundleId, Date scopeEndTime, int currentPage, int pageSize){
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<MonitorRecordPO> monitorRecords = monitorRecordDao.findByVideoBundleIdLikeAndStatusNotAndStartTimeLessThanEqual(new StringBufferWrapper().append(videoBundleId).append("_%").toString(), MonitorRecordStatus.WAITING, scopeEndTime, page);
		return monitorRecords.getContent();
	}
	
	public List<MonitorRecordPO> findByVideoBundleIdLikeAndStartTimeBetween(String videoBundleId, Date scopeStartTime, Date scopeEndTime, int currentPage, int pageSize){
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<MonitorRecordPO> monitorRecords = monitorRecordDao.findByVideoBundleIdLikeAndStatusNotAndStartTimeBetween(new StringBufferWrapper().append(videoBundleId).append("_%").toString(), MonitorRecordStatus.WAITING, scopeStartTime, scopeEndTime, page);
		return monitorRecords.getContent();
	}
	
	/**
	 * 分页查询用户录制文件<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月1日 下午5:06:44
	 * @param Long recordUserId 录制用户id
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return List<MonitorRecordPO> 录制文件列表
	 */
	public List<MonitorRecordPO> findByRecordUserId(Long recordUserId, int currentPage, int pageSize){
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<MonitorRecordPO> monitorRecords = monitorRecordDao.findByRecordUserIdAndStatusNot(recordUserId, MonitorRecordStatus.WAITING, page);
		return monitorRecords.getContent();
	}
	
	public List<MonitorRecordPO> findByRecordUserIdAndStartTimeGreaterThanEqual(Long recordUserId, Date scopeStartTime, int currentPage, int pageSize){
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<MonitorRecordPO> monitorRecords = monitorRecordDao.findByRecordUserIdAndStatusNotAndStartTimeGreaterThanEqual(recordUserId, MonitorRecordStatus.WAITING, scopeStartTime, page);
		return monitorRecords.getContent();
	}
	
	public List<MonitorRecordPO> findByRecordUserIdAndStartTimeLessThanEqual(Long recordUserId, Date scopeEndTime, int currentPage, int pageSize){
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<MonitorRecordPO> monitorRecords = monitorRecordDao.findByRecordUserIdAndStatusNotAndStartTimeLessThanEqual(recordUserId, MonitorRecordStatus.WAITING, scopeEndTime, page);
		return monitorRecords.getContent();
	}
	
	public List<MonitorRecordPO> findByRecordUserIdAndStartTimeBetween(Long recordUserId, Date scopeStartTime, Date scopeEndTime, int currentPage, int pageSize){
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<MonitorRecordPO> monitorRecords = monitorRecordDao.findByRecordUserIdAndStatusNotAndStartTimeBetween(recordUserId, MonitorRecordStatus.WAITING, scopeStartTime, scopeEndTime, page);
		return monitorRecords.getContent();
	}
	
}

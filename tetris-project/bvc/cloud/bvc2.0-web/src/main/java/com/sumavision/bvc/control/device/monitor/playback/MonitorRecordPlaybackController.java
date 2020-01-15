package com.sumavision.bvc.control.device.monitor.playback;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.suma.venus.resource.base.bo.AccessNodeBO;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.control.device.monitor.osd.MonitorOsdVO;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.device.monitor.osd.MonitorOsdDAO;
import com.sumavision.bvc.device.monitor.osd.MonitorOsdPO;
import com.sumavision.bvc.device.monitor.playback.MonitorRecordPlaybackTaskDAO;
import com.sumavision.bvc.device.monitor.playback.MonitorRecordPlaybackTaskPO;
import com.sumavision.bvc.device.monitor.playback.MonitorRecordPlaybackTaskQuery;
import com.sumavision.bvc.device.monitor.playback.MonitorRecordPlaybackTaskService;
import com.sumavision.bvc.device.monitor.record.MonitorRecordDAO;
import com.sumavision.bvc.device.monitor.record.MonitorRecordPO;
import com.sumavision.bvc.device.monitor.record.MonitorRecordQuery;
import com.sumavision.bvc.device.monitor.record.MonitorRecordStatus;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/monitor/record/playback")
public class MonitorRecordPlaybackController {
	
	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private MonitorRecordDAO monitorRecordDao;
	
	@Autowired
	private MonitorRecordQuery monitorRecordQuery;
	
	@Autowired
	private MonitorRecordPlaybackTaskService monitorRecordPlaybackTaskService;
	
	@Autowired
	private MonitorRecordPlaybackTaskQuery monitorRecordPlaybackTaskQuery;
	
	@Autowired
	private MonitorRecordPlaybackTaskDAO monitorRecordPlaybackTaskDao;
	
	@Autowired
	private MonitorOsdDAO monitorOsdDao;
	
	@Autowired
	private ResourceService resourceService;
	
	@RequestMapping(value = "/index")
	public ModelAndView index(String token){
		
		ModelAndView mv = new ModelAndView("web/bvc/monitor/playback/playback");
		mv.addObject("token", token);
		
		return mv;
	}
	
	@RequestMapping(value = "/list/index")
	public ModelAndView listIndex(String token){
		
		ModelAndView mv = new ModelAndView("web/bvc/monitor/playback-list/playback-list");
		mv.addObject("token", token);
		
		return mv;
	}
	
	/**
	 * 分页查询调阅任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月4日 下午2:42:23
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return total int 总数据量
	 * @return rows List<MonitorRecordPlaybackTaskVO> 调阅任务列表 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		int total = monitorRecordPlaybackTaskDao.countByUserId(userId.toString());
		
		List<MonitorRecordPlaybackTaskPO> entities = monitorRecordPlaybackTaskQuery.findByUserId(userId.toString(), currentPage, pageSize);
		
		List<MonitorRecordPlaybackTaskVO> rows = MonitorRecordPlaybackTaskVO.getConverter(MonitorRecordPlaybackTaskVO.class).convert(entities, MonitorRecordPlaybackTaskVO.class);
	
		if(rows!=null && rows.size()>0){
			Set<Long> osdIds = new HashSet<Long>();
			for(MonitorRecordPlaybackTaskVO row:rows){
				osdIds.add(row.getOsdId()==null?0l:row.getOsdId());
			}
			List<MonitorOsdPO> osdEntities = monitorOsdDao.findAll(osdIds);
			if(osdEntities!=null && osdEntities.size()>0){
				for(MonitorRecordPlaybackTaskVO row:rows){
					for(MonitorOsdPO osdEntity:osdEntities){
						if(osdEntity.getId().equals(row.getOsdId())){
							row.setOsdName(osdEntity.getName())
							   .setOsdUsername(osdEntity.getUsername());
							break;
						}
					}
				}
			}
		}
		
		return new HashMapWrapper<String, Object>().put("total", total)
												   .put("rows", rows)
												   .getMap();
	}
	
	/**
	 * 根据设备id查询设备录制的文件<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月24日 下午2:17:56
	 * @param String type "BUNDLE||USER"
	 * @param String bundleRealType "XT||NULL"
	 * @param String bundleId 设备id
	 * @param Long recordUserId 录制的用户id
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return long total 总数据量
	 * @return List<MonitorRecordVO> rows 录制文件列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/by/condition")
	public Object findByCondition(
			String type,
			String bundleRealType,
			String bundleId,
			Long recordUserId,
			String timeLowerLimit,
			String timeUpperLimit,
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		Date scopeStartTime = (timeUpperLimit==null?null:DateUtil.parse(timeLowerLimit, DateUtil.dateTimePattern));
		Date scopeEndTime = (timeUpperLimit==null?null:DateUtil.parse(timeUpperLimit, DateUtil.dateTimePattern));
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		int total = 0;
		List<MonitorRecordPO> entities = null;
		
		if("BUNDLE".equals(type)){
			if("XT".equals(bundleRealType)){
				if(scopeStartTime==null && scopeEndTime==null){
					total = monitorRecordDao.countByVideoBundleIdLikeAndStatusNot(bundleId, MonitorRecordStatus.WAITING);
					entities = monitorRecordQuery.findByVideoBundleIdLike(bundleId, currentPage, pageSize);
				}else if(scopeStartTime!=null && scopeEndTime==null){
					total = monitorRecordDao.countByVideoBundleIdLikeAndStatusNotAndStartTimeGreaterThanEqual(bundleId, MonitorRecordStatus.WAITING, scopeStartTime);
					entities = monitorRecordQuery.findByVideoBundleIdLikeAndStartTimeGreaterThanEqual(bundleId, scopeStartTime, currentPage, pageSize);
				}else if(scopeStartTime==null && scopeEndTime!=null){
					total = monitorRecordDao.countByVideoBundleIdLikeAndStatusNotAndStartTimeLessThanEqual(bundleId, MonitorRecordStatus.WAITING, scopeEndTime);
					entities = monitorRecordQuery.findByVideoBundleIdLikeAndStartTimeLessThanEqual(bundleId, scopeEndTime, currentPage, pageSize);
				}else if(scopeStartTime!=null && scopeEndTime!=null){
					total = monitorRecordDao.countByVideoBundleIdLikeAndStatusNotAndStartTimeBetween(bundleId, MonitorRecordStatus.WAITING, scopeStartTime, scopeEndTime);
					entities = monitorRecordQuery.findByVideoBundleIdLikeAndStartTimeBetween(bundleId, scopeStartTime, scopeEndTime, currentPage, pageSize);
				}
			}else{
				if(scopeStartTime==null && scopeEndTime==null){
					total = monitorRecordDao.countByVideoBundleIdAndStatusNot(bundleId, MonitorRecordStatus.WAITING);
					entities = monitorRecordQuery.findByVideoBundleId(bundleId, currentPage, pageSize);
				}else if(scopeStartTime!=null && scopeEndTime==null){
					total = monitorRecordDao.countByVideoBundleIdAndStatusNotAndStartTimeGreaterThanEqual(bundleId, MonitorRecordStatus.WAITING, scopeStartTime);
					entities = monitorRecordQuery.findByVideoBundleIdAndStartTimeGreaterThanEqual(bundleId, scopeStartTime, currentPage, pageSize);
				}else if(scopeStartTime==null && scopeEndTime!=null){
					total = monitorRecordDao.countByVideoBundleIdAndStatusNotAndStartTimeLessThanEqual(bundleId, MonitorRecordStatus.WAITING, scopeEndTime);
					entities = monitorRecordQuery.findByVideoBundleIdAndStartTimeLessThanEqual(bundleId, scopeEndTime, currentPage, pageSize);
				}else if(scopeStartTime!=null && scopeEndTime!=null){
					total = monitorRecordDao.countByVideoBundleIdAndStatusNotAndStartTimeBetween(bundleId, MonitorRecordStatus.WAITING, scopeStartTime, scopeEndTime);
					entities = monitorRecordQuery.findByVideoBundleIdAndStartTimeBetween(bundleId, scopeStartTime, scopeEndTime, currentPage, pageSize);
				}
			}
		}else if("USER".equals(type)){
			if(scopeStartTime==null && scopeEndTime==null){
				total = monitorRecordDao.countByRecordUserIdAndStatusNot(recordUserId, MonitorRecordStatus.WAITING);
				entities = monitorRecordQuery.findByRecordUserId(recordUserId, currentPage, pageSize);
			}else if(scopeStartTime!=null && scopeEndTime==null){
				total = monitorRecordDao.countByRecordUserIdAndStatusNotAndStartTimeGreaterThanEqual(recordUserId, MonitorRecordStatus.WAITING, scopeStartTime);
				entities = monitorRecordQuery.findByRecordUserIdAndStartTimeGreaterThanEqual(recordUserId, scopeStartTime, currentPage, pageSize);
			}else if(scopeStartTime==null && scopeEndTime!=null){
				total = monitorRecordDao.countByRecordUserIdAndStatusNotAndStartTimeLessThanEqual(recordUserId, MonitorRecordStatus.WAITING, scopeEndTime);
				entities = monitorRecordQuery.findByRecordUserIdAndStartTimeLessThanEqual(recordUserId, scopeEndTime, currentPage, pageSize);
			}else if(scopeStartTime!=null && scopeEndTime!=null){
				total = monitorRecordDao.countByRecordUserIdAndStatusNotAndStartTimeBetween(recordUserId, MonitorRecordStatus.WAITING, scopeStartTime, scopeEndTime);
				entities = monitorRecordQuery.findByRecordUserIdAndStartTimeBetween(recordUserId, scopeStartTime, scopeEndTime, currentPage, pageSize);
			}
		}
		
		List<RecordFileVO> rows = new ArrayList<RecordFileVO>();
		if(entities!=null && entities.size()>0){
			Set<String> layerIds = new HashSet<String>();
			for(MonitorRecordPO entity:entities){
				if(entity.getStoreLayerId()!=null) layerIds.add(entity.getStoreLayerId());
			}
			List<AccessNodeBO> layers = resourceService.queryAccessNodeByNodeUids(layerIds);
			for(MonitorRecordPO entity:entities){
				AccessNodeBO targetLayer = null;
				for(AccessNodeBO layer:layers){
					if(layer.getNodeUid().equals(entity.getStoreLayerId())){
						targetLayer = layer;
						break;
					}
				}
				rows.add(new RecordFileVO().set(entity, userId, targetLayer));
			}
		}
		
		return new HashMapWrapper<String, Object>().put("total", total)
												   .put("rows", rows)
												   .getMap();
	}
	
	/**
	 * 添加调阅任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月24日 下午6:08:59
	 * @param Long fileId 文件id
	 * @param Long osdId osd id
	 * @param String dstVideoBundleId 目的视频设备id
	 * @param String dstVideoBundleName 目的视频设备名称
	 * @param String dstVideoBundleType 目的视频设备类型
	 * @param String dstVideoLayerId 目的视频设备接入层
	 * @param String dstVideoChannelId 目的视频通道id
	 * @param String dstVideoBaseType 目的视频通道类型
	 * @param String dstAudioBundleId 目的音频设备id
	 * @param String dstAudioBundleName 目的音频设备名称
	 * @param String dstAudioBundleType 目的音频设备类型
	 * @param String dstAudioLayerId 目的音频设备接入层
	 * @param String dstAudioChannelId 目的音频通道id
	 * @param String dstAudioBaseType 目的音频通道类型
	 * @param long userId 操作业务用户
	 * @return MonitorRecordPlaybackTaskVO 调阅任务
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/task")
	public Object addTask(
			Long fileId,
			Long osdId,
			String dstVideoBundleId,
			String dstVideoBundleName,
			String dstVideoBundleType,
			String dstVideoLayerId,
			String dstVideoChannelId,
			String dstVideoBaseType,
			String dstAudioBundleId,
			String dstAudioBundleName,
			String dstAudioBundleType,
			String dstAudioLayerId,
			String dstAudioChannelId,
			String dstAudioBaseType,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		MonitorRecordPlaybackTaskPO entity = monitorRecordPlaybackTaskService.addTask(fileId, osdId,
				dstVideoBundleId, dstVideoBundleName, dstVideoBundleType, dstVideoLayerId, dstVideoChannelId, dstVideoBaseType,
				dstAudioBundleId, dstAudioBundleName, dstAudioBundleType, dstAudioLayerId, dstAudioChannelId, dstAudioBaseType, userId);
		
		return new MonitorRecordPlaybackTaskVO().set(entity);
	}
	
	/**
	 * 停止调阅任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月28日 下午4:57:52
	 * @param @PathVariable id 调阅任务id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/{id}")
	public Object remove(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		monitorRecordPlaybackTaskService.remove(id, userId);
		
		return null;
		
	}
	
	/**
	 * 调阅任务修改字幕<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月28日 下午9:05:22
	 * @param Long taskId 任务id
	 * @param Long osdId osd id
	 * @return MonitorOsdVO 修改的字幕
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/change/osd")
	public Object changeOsd(
			Long taskId,
			Long osdId,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		MonitorOsdPO osd = monitorRecordPlaybackTaskService.changeOsd(taskId, osdId, userId);
		
		return new MonitorOsdVO().set(osd);
	}
	
}

package com.sumavision.bvc.control.device.monitor.record;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.suma.venus.resource.base.bo.AccessNodeBO;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.constant.BusinessConstants;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.control.device.monitor.exception.UserHasNoPermissionForBusinessException;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.control.welcome.UserVO;
import com.sumavision.bvc.device.monitor.record.MonitorRecordDAO;
import com.sumavision.bvc.device.monitor.record.MonitorRecordPO;
import com.sumavision.bvc.device.monitor.record.MonitorRecordService;
import com.sumavision.bvc.device.monitor.record.MonitorRecordStatus;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/monitor/record")
public class MonitorRecordController {

	@Autowired
	private MonitorRecordDAO monitorRecordDao;
	
	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private MonitorRecordService monitorRecordService;
	
	@Autowired
	private ResourceService resourceService;
	
	@RequestMapping(value = "/index")
	public ModelAndView index(String token){
		
		ModelAndView mv = new ModelAndView("web/bvc/monitor/record/record");
		mv.addObject("token", token);
		
		return mv;
	}
	
	/**
	 * 获取下载地址<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月15日 上午9:04:10
	 * @param @PathVariable Long id 录制文件id
	 * @return String 下载地址
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/download/url/{id}")
	public Object downloadUrl(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		MonitorRecordPO file = monitorRecordDao.findOne(id);
		if(file == null) return null;
		List<AccessNodeBO> layers =resourceService.queryAccessNodeByNodeUids(new ArrayListWrapper<String>().add(file.getStoreLayerId()).getList());
		if(layers==null || layers.size()<=0) return null;
		AccessNodeBO targetLayer = layers.get(0);
		
		
		StringBufferWrapper downloadUrl = new StringBufferWrapper();
		downloadUrl.append("http://")
				   .append(targetLayer.getIp())
				   .append(":")
				   .append(targetLayer.getDownloadPort())
				   .append("/action/download?file=")
				   .append(file.getPreviewUrl());
		
		Date startTime = file.getStartTime();
		Date endTime = file.getEndTime()==null?new Date():file.getEndTime();
		
		long duration = (endTime.getTime()-startTime.getTime())/1000;
		
		return new HashMapWrapper<String, Object>().put("downloadUrl", downloadUrl.toString())
											       .put("duration", duration)
											       .getMap();
	}
	
	/**
	 * 根据条件分页查询录制任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月17日 下午7:35:49
	 * @param String mode 录制模式
	 * @param String device 设备id
	 * @param String startTime 开始录制时间下限
	 * @param String endTime 开始录制时间上限
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return total long 总数据量
	 * @return rows List<MonitorRecordTaskVO> 任务列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			String mode,
			String device,
			String startTime,
			String endTime,
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		//获取userId
		Long userId = userUtils.getUserIdFromSession(request);
		
		Date parsedStartTime = null;
		if(startTime != null){
			parsedStartTime = DateUtil.parse(startTime, DateUtil.dateTimePattern);
		}
		
		Date parsedEndTime = null;
		if(endTime != null){
			parsedEndTime = DateUtil.parse(endTime, DateUtil.dateTimePattern);
		}
		
		long total = 0;
		List<MonitorRecordPO> entities = null;
		Pageable page = new PageRequest(currentPage-1, pageSize);
		
		if(userId.longValue() == 1l){
			Page<MonitorRecordPO> pagedEntities = monitorRecordDao.findByConditions(
												mode, device, parsedStartTime, parsedEndTime, 
												null, MonitorRecordStatus.STOP.toString(), page);
			total = pagedEntities.getTotalElements();
			entities = pagedEntities.getContent();
		}else{
			Page<MonitorRecordPO> pagedEntities = monitorRecordDao.findByConditions(
												mode, device, parsedStartTime, parsedEndTime, 
												userId, MonitorRecordStatus.STOP.toString(), page);
			total = pagedEntities.getTotalElements();
			entities = pagedEntities.getContent();
		}
		
		List<MonitorRecordTaskVO> rows = MonitorRecordTaskVO.getConverter(MonitorRecordTaskVO.class).convert(entities, MonitorRecordTaskVO.class);
		return new HashMapWrapper<String, Object>().put("total", total)
												   .put("rows", rows)
												   .getMap();
	
	}
	
	/**
	 * 添加录制本地设备任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月18日 上午9:21:49
	 * @param String mode 录制模式
	 * @param String fileName 文件名
	 * @param String startTime 开始录制时间
	 * @param String endTime 结束录制时间
	 * @param String videoBundleId 视频设备id
	 * @param String videoBundleName 视频设备名称
	 * @param String videoBundleType 视频设备类型
	 * @param String videoLayerId 视频设备接入层
	 * @param String videoChannelId 视频通道id
	 * @param String videoBaseType 视频通道类型
	 * @param String videoChannelName 视频通道名称
	 * @param String audioBundleId 音频设备id
	 * @param String audioBundleName 音频设备名称
	 * @param String audioBundleType 音频设备类型
	 * @param String audioLayerId 音频设备接入层
	 * @param String audioChannelId 音频通道id
	 * @param String audioBaseType 音频通道类型
	 * @param String audioChannelName 音频通道名称
	 * @return MonitorRecordTaskVO 录制任务
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			String mode, 
			String fileName, 
			String startTime, 
			String endTime,
			String videoBundleId,
			String videoBundleName,
			String videoBundleType,
			String videoLayerId,
			String videoChannelId,
			String videoBaseType,
			String videoChannelName,
			String audioBundleId,
			String audioBundleName,
			String audioBundleType,
			String audioLayerId,
			String audioChannelId,
			String audioBaseType,
			String audioChannelName,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		boolean result = resourceService.hasPrivilegeOfBundle(user.getId(), videoBundleId, BusinessConstants.BUSINESS_OPR_TYPE.RECORD);
		if(!result){
			throw new UserHasNoPermissionForBusinessException(BusinessConstants.BUSINESS_OPR_TYPE.RECORD, 0);
		}
		if(audioBundleId!=null && !audioBundleId.equals(videoBundleId)){
			result = resourceService.hasPrivilegeOfBundle(user.getId(), audioBundleId, BusinessConstants.BUSINESS_OPR_TYPE.RECORD);
			if(!result){
				throw new UserHasNoPermissionForBusinessException(BusinessConstants.BUSINESS_OPR_TYPE.RECORD, 0);
			}
		}
		
		MonitorRecordPO task = monitorRecordService.addLocalDevice(
				mode, fileName, startTime, endTime, 
				videoBundleId, videoBundleName, videoBundleType, videoLayerId, videoChannelId, videoBaseType, videoChannelName, 
				audioBundleId, audioBundleName, audioBundleType, audioLayerId, audioChannelId, audioBaseType, audioChannelName, 
				user.getId(), user.getUserno());
		
		return new MonitorRecordTaskVO().set(task);
	}
	
	/**
	 * 添加录制xt设备任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月1日 下午3:19:58
	 * @param String mode 录制模式
	 * @param String fileName 文件名
	 * @param String startTime 开始录制时间
	 * @param String endTime 结束录制时间
	 * @param String bundleId xt设备id
	 * @return MonitorRecordTaskVO 录制任务
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/xt/device")
	public Object addXtDevice(
			String mode, 
			String fileName, 
			String startTime, 
			String endTime,
			String bundleId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		boolean result = resourceService.hasPrivilegeOfBundle(user.getId(), bundleId, BusinessConstants.BUSINESS_OPR_TYPE.RECORD);
		if(!result){
			throw new UserHasNoPermissionForBusinessException(BusinessConstants.BUSINESS_OPR_TYPE.RECORD, 0);
		}
		
		MonitorRecordPO task = monitorRecordService.addXtDevice(mode, fileName, startTime, endTime, bundleId, user.getId(), user.getUserno());
		
		return new MonitorRecordTaskVO().set(task);
	}
	
	/**
	 * 添加录制用户任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月4日 下午1:32:25
	 * @param Long userId 录制的用户id
	 * @param String mode 录制模式
	 * @param String fileName 录制文件名
	 * @param String startTime 排期录制开始时间
	 * @param String endTime 排期录制结束时间
	 * @return MonitorRecordTaskVO 录制任务
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/record/user/task")
	public Object addRecordUserTask(
			Long targetUserId,
			String mode, 
			String fileName, 
			String startTime, 
			String endTime,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		boolean result = resourceService.hasPrivilegeOfUser(user.getId(), targetUserId, BusinessConstants.BUSINESS_OPR_TYPE.RECORD);
		if(!result){
			throw new UserHasNoPermissionForBusinessException(BusinessConstants.BUSINESS_OPR_TYPE.RECORD, 1);
		}
		
		UserBO targetUser = userUtils.queryUserById(targetUserId);
		
		MonitorRecordPO task = null;
		if("ldap".equals(targetUser.getCreater())){
			//录制xt用户
			task = monitorRecordService.addXtUser(mode, fileName, startTime, endTime, targetUser, user.getId(), user.getUserno());
		}else{
			//录制本地用户
			task = monitorRecordService.addLocalUser(mode, fileName, startTime, endTime, targetUser, user.getId(), user.getUserno());
		}
		
		return new MonitorRecordTaskVO().set(task);
	}
	
	
	/**
	 * 停止录制<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月18日 下午2:33:05
	 * @param @PathVariable Long id 录制任务id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/stop/{id}")
	public Object stop(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		monitorRecordService.stop(id, userId);
		
		return null;
	}
	
	/**
	 * 删除录制文件<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月25日 上午10:10:55
	 * @param @PathVariable Long id 文件id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/file/{id}")
	public Object removeFile(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		monitorRecordService.removeFile(id, userId);
	
		return null;
	}
	
}

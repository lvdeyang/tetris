package com.sumavision.bvc.control.device.monitor.record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
import com.suma.venus.resource.base.bo.ResourceIdListBO;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.constant.BusinessConstants;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.service.BundleService;
import com.suma.venus.resource.service.ResourceService;
import com.suma.venus.resource.service.UserQueryService;
import com.sumavision.bvc.control.device.monitor.device.ChannelVO;
import com.sumavision.bvc.control.device.monitor.exception.UserHasNoPermissionForBusinessException;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.control.welcome.UserVO;
import com.sumavision.bvc.device.group.service.util.ResourceQueryUtil;
import com.sumavision.bvc.device.monitor.playback.exception.AccessNodeIpMissingException;
import com.sumavision.bvc.device.monitor.playback.exception.AccessNodeNotExistException;
import com.sumavision.bvc.device.monitor.playback.exception.AccessNodePortMissionException;
import com.sumavision.bvc.device.monitor.record.MonitorRecordDAO;
import com.sumavision.bvc.device.monitor.record.MonitorRecordManyTimesDAO;
import com.sumavision.bvc.device.monitor.record.MonitorRecordManyTimesPO;
import com.sumavision.bvc.device.monitor.record.MonitorRecordManyTimesRelationDAO;
import com.sumavision.bvc.device.monitor.record.MonitorRecordManyTimesRelationPO;
import com.sumavision.bvc.device.monitor.record.MonitorRecordManyTimesVO;
import com.sumavision.bvc.device.monitor.record.MonitorRecordMode;
import com.sumavision.bvc.device.monitor.record.MonitorRecordPO;
import com.sumavision.bvc.device.monitor.record.MonitorRecordService;
import com.sumavision.bvc.device.monitor.record.MonitorRecordStatus;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.tetris.auth.token.TerminalType;
import com.sumavision.tetris.bvc.system.dao.SystemConfigurationDAO;
import com.sumavision.tetris.bvc.system.po.SystemConfigurationPO;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;

@Controller
@RequestMapping(value = "/monitor/record")
public class MonitorRecordController {

	@Autowired
	private MonitorRecordDAO monitorRecordDao;
	
	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private MonitorRecordService monitorRecordService;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private BundleService bundleService;
	
	@Autowired
	private ResourceQueryUtil resourceQueryUtil;
	
	@Autowired
	private MonitorRecordManyTimesRelationDAO monitorRecordManyTimesRelationDao;
	
	@Autowired
	private MonitorRecordManyTimesDAO monitorRecordManyTimesDao;
	
	@Autowired
	private SystemConfigurationDAO systemConfigurationDao;
	
	@Autowired
	private UserQueryService userQueryService;
	
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
	 * @return String downloadUrl 下载地址
	 * @return long duration 文件时长 单位：秒
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
		
		
//		.setPreviewUrl(new StringBufferWrapper().append("http://").append(layer==null?"0.0.0.0":layer.getIp()).append(":").append(layer==null?"0":layer.getPort()).append("/").append(entity.getPreviewUrl()).toString())
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
	 * @param String fileName 文件名称
	 * @param String deviceType 如果为user device为用户id
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
			String fileName,
			String deviceType,
			String device,
			String startTime,
			String endTime,
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		if("".equals(device)) device = null;
		
		String fileNameReg = null;
		if(fileName!=null && !"".equals(fileName)){
			fileNameReg = new StringBufferWrapper().append("%").append(fileName).append("%").toString();
		}
		
		com.sumavision.tetris.user.UserVO user = userQuery.current();
		
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
		
		if("user".equals(deviceType)){
			if("ALL".equals(mode)){
				//对所有类型进行筛选
				if(userId.longValue()==1l || user.getIsGroupCreator()){
					Page<MonitorRecordPO> pagedEntities = monitorRecordDao.findAllByConditions(
							fileNameReg, null, parsedStartTime, parsedEndTime, 
														null, MonitorRecordStatus.STOP.toString(), Long.valueOf(device), page);
					total = pagedEntities.getTotalElements();
					entities = pagedEntities.getContent();
				}else{
					Page<MonitorRecordPO> pagedEntities = monitorRecordDao.findAllByConditions(
							fileNameReg, null, parsedStartTime, parsedEndTime, 
														userId, MonitorRecordStatus.STOP.toString(), Long.valueOf(device), page);
					total = pagedEntities.getTotalElements();
					entities = pagedEntities.getContent();
				}
				
			}else{
				if(userId.longValue()==1l || user.getIsGroupCreator()){
					Page<MonitorRecordPO> pagedEntities = monitorRecordDao.findByConditions(
														mode, null, parsedStartTime, parsedEndTime, 
														null, MonitorRecordStatus.STOP.toString(), Long.valueOf(device), fileNameReg, page);
					total = pagedEntities.getTotalElements();
					entities = pagedEntities.getContent();
				}else{
					Page<MonitorRecordPO> pagedEntities = monitorRecordDao.findByConditions(
														mode, null, parsedStartTime, parsedEndTime, 
														userId, MonitorRecordStatus.STOP.toString(), Long.valueOf(device), fileNameReg, page);
					total = pagedEntities.getTotalElements();
					entities = pagedEntities.getContent();
				}
			}
		}else{
			if("ALL".equals(mode)){
				if(userId.longValue()==1l || user.getIsGroupCreator()){
					Page<MonitorRecordPO> pagedEntities = monitorRecordDao.findAllByConditions(
							fileNameReg, device, parsedStartTime, parsedEndTime, 
														null, MonitorRecordStatus.STOP.toString(), null, page);
					total = pagedEntities.getTotalElements();
					entities = pagedEntities.getContent();
				}else{
					Page<MonitorRecordPO> pagedEntities = monitorRecordDao.findAllByConditions(
							fileNameReg, device, parsedStartTime, parsedEndTime, 
														userId, MonitorRecordStatus.STOP.toString(), null, page);
					total = pagedEntities.getTotalElements();
					entities = pagedEntities.getContent();
				}
			}else{
				if(userId.longValue()==1l || user.getIsGroupCreator()){
					Page<MonitorRecordPO> pagedEntities = monitorRecordDao.findByConditions(
														mode, device, parsedStartTime, parsedEndTime, 
														null, MonitorRecordStatus.STOP.toString(), null, fileNameReg, page);
					total = pagedEntities.getTotalElements();
					entities = pagedEntities.getContent();
				}else{
					Page<MonitorRecordPO> pagedEntities = monitorRecordDao.findByConditions(
														mode, device, parsedStartTime, parsedEndTime, 
														userId, MonitorRecordStatus.STOP.toString(), null, fileNameReg, page);
					total = pagedEntities.getTotalElements();
					entities = pagedEntities.getContent();
				}
			}
		}
		
		
		List<MonitorRecordTaskVO> rows = MonitorRecordTaskVO.getConverter(MonitorRecordTaskVO.class).convert(entities, MonitorRecordTaskVO.class);
		return new HashMapWrapper<String, Object>().put("total", total)
												   .put("rows", rows)
												   .put("totalSizeMb", systemConfigurationDao.findByTotalSizeMbNotNull().getTotalSizeMb())
												   .getMap();
	
	}
	
	/**
	 * 根据条件分页查询所有状态录制任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月17日 下午7:35:49
	 * @param String mode 录制模式
	 * @param String fileName 文件名称
	 * @param String deviceType 如果为user device为用户id
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
	@RequestMapping(value = "/load/all/status/record")
	public Object loadAllStatusRecord(
			String mode,
			String fileName,
			String deviceType,
			String device,
			String startTime,
			String endTime,
			int currentPage,
			int pageSize,
			String status,
			HttpServletRequest request) throws Exception{
		
		if("".equals(device)) device = null;
		
		String fileNameReg = null;
		if(fileName!=null && !"".equals(fileName)){
			fileNameReg = new StringBufferWrapper().append("%").append(fileName).append("%").toString();
		}
		
		com.sumavision.tetris.user.UserVO user = userQuery.current();
		
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
		
		if("user".equals(deviceType)){
			if("ALL".equals(mode)){
				//对所有类型进行筛选
				if(userId.longValue()==1l || user.getIsGroupCreator()){
					Page<MonitorRecordPO> pagedEntities = monitorRecordDao.findAllByConditionsAndStatus(
							fileNameReg, null, parsedStartTime, parsedEndTime, 
														null, status, Long.valueOf(device), page);
					total = pagedEntities.getTotalElements();
					entities = pagedEntities.getContent();
				}else{
					Page<MonitorRecordPO> pagedEntities = monitorRecordDao.findAllByConditionsAndStatus(
							fileNameReg, null, parsedStartTime, parsedEndTime, 
														userId, status, Long.valueOf(device), page);
					total = pagedEntities.getTotalElements();
					entities = pagedEntities.getContent();
				}
				
			}else{
				if(userId.longValue()==1l || user.getIsGroupCreator()){
					Page<MonitorRecordPO> pagedEntities = monitorRecordDao.findByConditionsAndStatus(
														mode, null, parsedStartTime, parsedEndTime, 
														null, status, Long.valueOf(device), fileNameReg, page);
					total = pagedEntities.getTotalElements();
					entities = pagedEntities.getContent();
				}else{
					Page<MonitorRecordPO> pagedEntities = monitorRecordDao.findByConditionsAndStatus(
														mode, null, parsedStartTime, parsedEndTime, 
														userId, status, Long.valueOf(device), fileNameReg, page);
					total = pagedEntities.getTotalElements();
					entities = pagedEntities.getContent();
				}
			}
		}else{
			if("ALL".equals(mode)){
				if(userId.longValue()==1l || user.getIsGroupCreator()){
					Page<MonitorRecordPO> pagedEntities = monitorRecordDao.findAllByConditionsAndStatus(
							fileNameReg, device, parsedStartTime, parsedEndTime, 
														null, status, null, page);
					total = pagedEntities.getTotalElements();
					entities = pagedEntities.getContent();
				}else{
					Page<MonitorRecordPO> pagedEntities = monitorRecordDao.findAllByConditionsAndStatus(
							fileNameReg, device, parsedStartTime, parsedEndTime, 
														userId, status, null, page);
					total = pagedEntities.getTotalElements();
					entities = pagedEntities.getContent();
				}
			}else{
				if(userId.longValue()==1l || user.getIsGroupCreator()){
					Page<MonitorRecordPO> pagedEntities = monitorRecordDao.findByConditionsAndStatus(
														mode, device, parsedStartTime, parsedEndTime, 
														null,status, null, fileNameReg, page);
					total = pagedEntities.getTotalElements();
					entities = pagedEntities.getContent();
				}else{
					Page<MonitorRecordPO> pagedEntities = monitorRecordDao.findByConditionsAndStatus(
														mode, device, parsedStartTime, parsedEndTime, 
														userId, status, null, fileNameReg, page);
					total = pagedEntities.getTotalElements();
					entities = pagedEntities.getContent();
				}
			}
		}
		
		
//		List<MonitorRecordTaskVO> rows = MonitorRecordTaskVO.getConverter(MonitorRecordTaskVO.class).convert(entities, MonitorRecordTaskVO.class);
		ResourceIdListBO bo = userQueryService.queryUserPrivilege(userId);
		List<MonitorRecordTaskVO> rows = new ArrayList<MonitorRecordTaskVO>();
		for(MonitorRecordPO recordPo:entities){
			MonitorRecordTaskVO recordTaskVo = new MonitorRecordTaskVO();
			rows.add(recordTaskVo.set(recordPo, bo));
		}
		
		Integer totalSizeMb=(systemConfigurationDao.findByTotalSizeMbNotNull()==null?null:systemConfigurationDao.findByTotalSizeMbNotNull().getTotalSizeMb());
		
		return new HashMapWrapper<String, Object>().put("total", total)
												   .put("rows", rows)
												   .put("totalSizeMb", totalSizeMb)
												   .getMap();
	
	}
	
	/**
	 * 根据条件分页查询录制任务<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月22日 上午9:45:40
	 * @param Long id monitorRecord的主键
	 * @param String mode 录制模式
	 * @param String fileName 文件名称
	 * @param String deviceType 如果为user device为用户id
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
	@RequestMapping(value = "/load/many/times/record")
	public Object loadManyTimesRecord(
			Long id,
			String mode,
			String fileName,
			String deviceType,
			String device,
			String startTime,
			String endTime,
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		//查询
		Long total = 0L;
		List<MonitorRecordManyTimesPO> entities = null;
		Pageable page = new PageRequest(currentPage-1, pageSize);
		
		MonitorRecordPO record=monitorRecordDao.findOne(id);
		
		if(record==null||!MonitorRecordMode.TIMESEGMENT.equals(record.getMode())){
			return null;
		}
		
		MonitorRecordManyTimesRelationPO relation=monitorRecordManyTimesRelationDao.findByBusinessId(id);
		
		Page<MonitorRecordManyTimesPO> pagedEntities=monitorRecordManyTimesDao.findByRelation(relation.getId(),page);
		
		entities=pagedEntities.getContent();
		
		total=pagedEntities.getTotalElements();
		
		if(entities!=null && entities.size()>0){
			List<AccessNodeBO> layers =resourceService.queryAccessNodeByNodeUids(new ArrayListWrapper<String>().add(record.getStoreLayerId()).getList());
			if(layers==null || layers.size()<=0){
				throw new AccessNodeNotExistException(record.getStoreLayerId());
			}
			AccessNodeBO targetLayer = layers.get(0);
			if(targetLayer.getIp() == null){
				throw new AccessNodeIpMissingException(record.getStoreLayerId());
			}
			if(targetLayer.getPort() == null){
				throw new AccessNodePortMissionException(record.getStoreLayerId());
			}
			
			//还能优化么？
			List <MonitorRecordManyTimesVO> rows=entities.stream().map(entity->{
				String downLoadPath=new StringBufferWrapper().append("http://")
				   .append(targetLayer.getIp())
				   .append(":")
				   .append(targetLayer.getDownloadPort())
				   .append("/action/download?file=")
				   .append(record.getPreviewUrl().replace("/", "/"+entity.getIndexNumber()+"/")).toString();
				String previewPath=new StringBufferWrapper().append("http://")
						.append(targetLayer==null?"0.0.0.0":targetLayer.getIp())
						.append(":")
						.append(targetLayer==null?"0":targetLayer.getPort())
						.append("/")
						.append(record.getPreviewUrl().replace("/", "/"+entity.getIndexNumber()+"/")).toString();
				return new MonitorRecordManyTimesVO().set(entity,record.getFileName(),previewPath,downLoadPath);
			}).collect(Collectors.toList());
			
			return new HashMapWrapper<String, Object>().put("total", total)
													   .put("rows", rows)
													   .getMap();
		}
		
		return null;
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
	 * @param String bundleId 传channel信息时有这个字段，代表录制设备（当前设备只有一个音频编码和一个视频编码）
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
			String bundleId,
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
			String storeMode,
			String timeQuantum,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		if(bundleId != null){
			//不选通道直接录制设备（新增）
			boolean result = resourceService.hasPrivilegeOfBundle(user.getId(), bundleId, BusinessConstants.BUSINESS_OPR_TYPE.RECORD);
			if(!result){
				throw new UserHasNoPermissionForBusinessException(BusinessConstants.BUSINESS_OPR_TYPE.RECORD, 0);
			}
		}else{
			//选通道录制（原有方式）
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
		}
		
		//兼容不选通道直接录制设备
		if(bundleId != null){
			BundlePO bundle = bundleService.findByBundleId(bundleId);
			List<ChannelSchemeDTO> queryChannels = resourceQueryUtil.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(bundleId).getList(), 0);
			ChannelVO encodeVideo = null;
			ChannelVO encodeAudio = null;
			if(queryChannels!=null && queryChannels.size()>0){
				for(ChannelSchemeDTO channel:queryChannels){
					if("VenusVideoIn".equals(channel.getBaseType())){
						encodeVideo = new ChannelVO().set(channel);
					}else if("VenusAudioIn".equals(channel.getBaseType())){
						encodeAudio = new ChannelVO().set(channel);
					}
				}
			}
			videoBundleId = bundle.getBundleId();
			videoBundleName = bundle.getBundleName();
			videoBundleType = bundle.getBundleType();
			videoLayerId = bundle.getAccessNodeUid();
			videoChannelId = encodeVideo.getChannelId();
			videoBaseType = encodeVideo.getBaseType();
			videoChannelName = encodeVideo.getName();
			audioBundleId = bundle.getBundleId();
			audioBundleName = bundle.getBundleName();
			audioBundleType = bundle.getBundleType();
			audioLayerId = bundle.getAccessNodeUid();
			audioChannelId = encodeAudio.getChannelId();
			audioBaseType = encodeAudio.getBaseType();
			audioChannelName = encodeAudio.getName();
		}
		
		if(fileName==null || "".equals(fileName)) throw new BaseException(StatusCode.FORBIDDEN, "文件名不能为空！");
		
		if(MonitorRecordMode.TIMESEGMENT.equals(MonitorRecordMode.valueOf(mode))){
			
			SystemConfigurationPO configuration=systemConfigurationDao.findByTotalSizeMbNotNull();
			if(configuration==null){
				throw new BaseException(StatusCode.FORBIDDEN, "还没有设置磁盘大小");
			}
			Integer totalSizeMb =configuration.getTotalSizeMb();
			
			MonitorRecordPO task = monitorRecordService.addLocalDevice(
					mode, fileName, startTime, endTime, 
					videoBundleId, videoBundleName, videoBundleType, videoLayerId, videoChannelId, videoBaseType, videoChannelName, 
					audioBundleId, audioBundleName, audioBundleType, audioLayerId, audioChannelId, audioBaseType, audioChannelName, 
					user.getId(), user.getUserno(), user.getName(), storeMode,
					timeQuantum, totalSizeMb);
			return new MonitorRecordTaskVO().set(task);
		}else{
			MonitorRecordPO task = monitorRecordService.addLocalDevice(
					mode, fileName, startTime, endTime, 
					videoBundleId, videoBundleName, videoBundleType, videoLayerId, videoChannelId, videoBaseType, videoChannelName, 
					audioBundleId, audioBundleName, audioBundleType, audioLayerId, audioChannelId, audioBaseType, audioChannelName, 
					user.getId(), user.getUserno(), user.getName());
			return new MonitorRecordTaskVO().set(task);
		}
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
		
		if(fileName==null || "".equals(fileName)) throw new BaseException(StatusCode.FORBIDDEN, "文件名不能为空！");
		
		MonitorRecordPO task = monitorRecordService.addXtDevice(mode, fileName, startTime, endTime, bundleId, user.getId(), user.getUserno(), user.getName());
		
		return new MonitorRecordTaskVO().set(task);
	}
	
	/**
	 * 添加录制用户任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月4日 下午1:32:25
	 * @param Long targetUserId 录制的用户id
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
		
		if(fileName==null || "".equals(fileName)) throw new BaseException(StatusCode.FORBIDDEN, "文件名不能为空！");
		
		UserBO targetUser = userUtils.queryUserById(targetUserId, TerminalType.PC_PLATFORM);
		
		MonitorRecordPO task = null;
		if("ldap".equals(targetUser.getCreater())){
			//录制xt用户
			task = monitorRecordService.addXtUser(mode, fileName, startTime, endTime, targetUser, user.getId(), user.getUserno(), user.getName());
		}else{
			//录制本地用户
			task = monitorRecordService.addLocalUser(mode, fileName, startTime, endTime, targetUser, user.getId(), user.getUserno(), user.getName());
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
	
	/**
	 * 删除录制文件<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月25日 上午10:10:55
	 * @param @PathVariable Long id 文件id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/file/by/id/timeSegmentId")
	public Object removeFileById(
			Long id,
			Long timeSegmentId,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		monitorRecordService.removeFileById(id,timeSegmentId, userId);
	
		return null;
	}
	
	/**
	 * 设置磁盘大小<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月22日 下午4:41:40
	 * @param total_size_mb磁盘大小
	 * @return
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/total/size")
	public Object totalSize(Integer totalSizeMb){
		
		monitorRecordService.setTotalSize(totalSizeMb);
		
		return null;
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/detail/message/of/timesegment")
	public Object detailMessageOfTimeSegment(Long id) throws Exception{
		
		return  monitorRecordService.getRecordRelation(id);
	}
	
	/*@JsonBody
	@ResponseBody
	@RequestMapping(value="/many/times/load")
	public Object manyTimesLoad(
			int id,
			String fileName,
			String deviceType,
			String device,
			String startTime,
			String endTime,
			int currentPage,
			int pageSize,
			HttpServletRequest request){
		if("".equals(device)) device = null;
		
		String fileNameReg = null;
		if(fileName!=null && !"".equals(fileName)){
			fileNameReg = new StringBufferWrapper().append("%").append(fileName).append("%").toString();
		}
		
		com.sumavision.tetris.user.UserVO user = userQuery.current();
		
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
		List<MOnitorRrecordManyTimesPO> entities = null;
		Pageable page = new PageRequest(currentPage-1, pageSize);
		
		if("user".equals(deviceType)){
			if(userId.longValue()==1l || user.getIsGroupCreator()){
				Page<MonitorRecordPO> pagedEntities = monitorRecordDao.findByConditions(
													mode, null, parsedStartTime, parsedEndTime, 
													null, MonitorRecordStatus.STOP.toString(), Long.valueOf(device), fileNameReg, page);
				total = pagedEntities.getTotalElements();
				entities = pagedEntities.getContent();
			}else{
				Page<MonitorRecordPO> pagedEntities = monitorRecordDao.findByConditions(
													mode, null, parsedStartTime, parsedEndTime, 
													userId, MonitorRecordStatus.STOP.toString(), Long.valueOf(device), fileNameReg, page);
				total = pagedEntities.getTotalElements();
				entities = pagedEntities.getContent();
			}
		}else{
			if(userId.longValue()==1l || user.getIsGroupCreator()){
				Page<MonitorRecordPO> pagedEntities = monitorRecordDao.findByConditions(
													mode, device, parsedStartTime, parsedEndTime, 
													null, MonitorRecordStatus.STOP.toString(), null, fileNameReg, page);
				total = pagedEntities.getTotalElements();
				entities = pagedEntities.getContent();
			}else{
				Page<MonitorRecordPO> pagedEntities = monitorRecordDao.findByConditions(
													mode, device, parsedStartTime, parsedEndTime, 
													userId, MonitorRecordStatus.STOP.toString(), null, fileNameReg, page);
				total = pagedEntities.getTotalElements();
				entities = pagedEntities.getContent();
			}
		}
		
		
		List<MonitorRecordTaskVO> rows = MonitorRecordTaskVO.getConverter(MonitorRecordTaskVO.class).convert(entities, MonitorRecordTaskVO.class);
		return new HashMapWrapper<String, Object>().put("total", total)
												   .put("rows", rows)
												   .getMap();
	
	}*/

}

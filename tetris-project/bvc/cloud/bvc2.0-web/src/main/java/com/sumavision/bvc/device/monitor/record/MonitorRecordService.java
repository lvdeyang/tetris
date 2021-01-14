package com.sumavision.bvc.device.monitor.record;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.service.BundleService;
import com.suma.venus.resource.service.ResourceRemoteService;
import com.sumavision.bvc.command.system.dao.CommandSystemTitleDAO;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.device.group.bo.AapAlarmBO;
import com.sumavision.bvc.device.group.bo.AlarmBO;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.ConnectBO;
import com.sumavision.bvc.device.group.bo.ConnectBundleBO;
import com.sumavision.bvc.device.group.bo.CycleBO;
import com.sumavision.bvc.device.group.bo.DisconnectBundleBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.PassByBO;
import com.sumavision.bvc.device.group.bo.RecordDateTimeBO;
import com.sumavision.bvc.device.group.bo.RecordSetBO;
import com.sumavision.bvc.device.group.bo.RecordSourceBO;
import com.sumavision.bvc.device.group.bo.RecordTimeSegmentBO;
import com.sumavision.bvc.device.group.bo.XtBusinessPassByContentBO;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplPO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.CommonQueryUtil;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.device.monitor.exception.AvtplNotFoundException;
import com.sumavision.bvc.device.monitor.exception.UserHashNoPermissionToStopMonitorRecordException;
import com.sumavision.bvc.device.monitor.live.MonitorLiveCommons;
import com.sumavision.bvc.device.monitor.record.exception.ErrorRecordModeException;
import com.sumavision.bvc.device.monitor.record.exception.MonitorRecordSourceVideoCannotBeNullException;
import com.sumavision.bvc.feign.ResourceServiceClient;
import com.sumavision.bvc.log.OperationLogService;
import com.sumavision.bvc.meeting.logic.ExecuteBusinessReturnBO;
import com.sumavision.bvc.meeting.logic.ExecuteBusinessReturnBO.ResultDstBO;
import com.sumavision.bvc.resource.dao.ResourceBundleDAO;
import com.sumavision.bvc.resource.dao.ResourceChannelDAO;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.bvc.system.dao.AVtplGearsDAO;
import com.sumavision.bvc.system.dao.AvtplDAO;
import com.sumavision.bvc.system.enumeration.AvtplUsageType;
import com.sumavision.bvc.system.po.AvtplGearsPO;
import com.sumavision.bvc.system.po.AvtplPO;
import com.sumavision.tetris.bvc.business.common.MulticastService;
import com.sumavision.tetris.bvc.business.group.TransmissionMode;
import com.sumavision.tetris.bvc.system.dao.SystemConfigurationDAO;
import com.sumavision.tetris.bvc.system.po.SystemConfigurationPO;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

/**
 * 录制增删改业务<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年4月16日 下午8:27:42
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MonitorRecordService {

	@Autowired
	private MonitorRecordDAO monitorRecordDao;

	@Autowired
	private AvtplDAO avtplDao;

	@Autowired
	private AVtplGearsDAO avtplGearsDao;

	@Autowired
	private BundleDao bundleDao;

	@Autowired
	private ResourceBundleDAO resourceBundleDao;

	@Autowired
	private ResourceChannelDAO resourceChannelDao;

	@Autowired
	private CommonQueryUtil commonQueryUtil;

	@Autowired
	private MonitorLiveCommons commons;

	@Autowired
	private ExecuteBusinessProxy executeBusiness;

	@Autowired
	private UserQuery userQuery;

	@Autowired
	private QueryUtil queryUtil;
	
	@Autowired
	private MonitorRecordManyTimesDAO monitorRecordManyTimesDao;
	
	@Autowired
	private MonitorRecordManyTimesRelationDAO monitorRecordManyTimesRelationDao;
	
	@Autowired
	private MonitorRecordManyTimesRelationService monitorRecordManyTimesRelationService;
	
	@Autowired
	private SystemConfigurationDAO systemConfigurationDao;
	
	@Autowired
	private OperationLogService operationLogService;
	
	@Autowired
	private ResourceRemoteService resourceRemoteService;
	
	@Autowired
	private BundleService bundleService;
	
	@Autowired
	private MulticastService multicastService;
	
	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private ResourceServiceClient resourceServiceClient;
	
	@Autowired
	private CommandSystemTitleDAO commandSystemTitleDao;
	
	/**
	 * 录制设备（支持本系统和外部系统）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月1日 上午10:18:01
	 * 
	 * @param String
	 *            mode 录制模式
	 * @param String
	 *            fileName 文件名称
	 * @param String
	 *            startTime 录制开始时间
	 * @param String
	 *            endTime 录制结束时间
	 * @param String
	 *            videoBundleId 视频设备id
	 * @param String
	 *            videoBundleName 视频设备名称
	 * @param String
	 *            videoBundleType 视频设备类型
	 * @param String
	 *            videoLayerId 视频设备接入层id
	 * @param String
	 *            videoChannelId 视频设备通道id
	 * @param String
	 *            videoBaseType 视频设备通道类型
	 * @param String
	 *            videoChannelName 视频设备通道名称
	 * @param String
	 *            audioBundleId 音频设备id
	 * @param String
	 *            audioBundleName 音频设备名称
	 * @param String
	 *            audioBundleType 音频设备类型
	 * @param String
	 *            audioLayerId 音频设备接入层id
	 * @param String
	 *            audioChannelId 音频通道id
	 * @param String
	 *            audioBaseType 音频通道类型
	 * @param String
	 *            audioChannelName 音频通道名称
	 * @param Long
	 *            userId 操作业务用户id
	 * @param String
	 *            userno 操作业务用户名称
	 * @param String
	 *            nickname 操作业务用户昵称
	 * @param Long total_size_mb 此任务可占用的磁盘大小，单位兆，0代表无限大。
	 * @param Long time_duration 此任务可录制的时长，单位：小时，0代表物无限长
	 * @return MonitorRecordPO 录制任务
	 */
	public MonitorRecordPO addDevice(String mode, String fileName, String startTime, String endTime,

			String videoBundleId, String videoBundleName, String videoBundleType, String videoLayerId,
			String videoChannelId, String videoBaseType, String videoChannelName,

			String audioBundleId, String audioBundleName, String audioBundleType, String audioLayerId,
			String audioChannelId, String audioBaseType, String audioChannelName, Long userId, String userno,
			String nickname, Long total_size_mb, Long time_duration,Long alarm_size_mb 
	) throws Exception {
		
		BundlePO bundle = bundleDao.findByBundleId(videoBundleId);
		boolean bLdap = queryUtil.isLdapBundle(bundle);
		if(bLdap){
			//使用原本的bundleId
//			videoBundleId = new StringBufferWrapper().append(videoBundleId).append("_")
//					.append(UUID.randomUUID().toString().replace("-", "")).toString();
			videoBundleName = bundle.getBundleName();
			videoBundleType = bundle.getBundleType();
			videoLayerId = resourceRemoteService.queryLocalLayerId();
			videoChannelId = ChannelType.VIDEOENCODE1.getChannelId();
			//videoBaseType
			videoChannelName = ChannelType.VIDEOENCODE1.getName();
			audioBundleId = videoBundleId;
			audioBundleName = bundle.getBundleName();
			audioBundleType = bundle.getBundleType();
			audioLayerId = videoLayerId;
			audioChannelId = ChannelType.AUDIOENCODE1.getChannelId();
			//audioBaseType
			audioChannelName = ChannelType.AUDIOENCODE1.getName();			
		}

		// 参数模板
		Map<String, Object> result = commons.queryDefaultAvCodec();
		AvtplPO targetAvtpl = (AvtplPO) result.get("avtpl");
		AvtplGearsPO targetGear = (AvtplGearsPO) result.get("gear");
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl),
				new DeviceGroupAvtplGearsPO().set(targetGear));

		MonitorRecordMode parsedMode = MonitorRecordMode.valueOf(mode);
		Date parsedStartTime = startTime == null ? new Date() : DateUtil.parse(startTime, DateUtil.dateTimePattern);
		Date parsedEndTime = endTime == null ? null : DateUtil.parse(endTime, DateUtil.dateTimePattern);

		MonitorRecordPO task = new MonitorRecordPO();
		task.setMode(parsedMode);
		task.setFileName(fileName);

		task.setStartTime(parsedStartTime);
		task.setEndTime(parsedEndTime);
		task.setVideoType(MonitorRecordSourceType.DEVICE);
		task.setVideoBundleId(videoBundleId);
		task.setVideoBundleName(videoBundleName);
		task.setVideoBundleType(videoBundleType);
		task.setVideoLayerId(videoLayerId);
		task.setVideoChannelId(videoChannelId);
		task.setVideoBaseType(videoBaseType);
		task.setVideoChannelName(videoChannelName);
		task.setAudioType(MonitorRecordSourceType.DEVICE);
		task.setAudioBundleId(audioBundleId);
		task.setAudioBundleName(audioBundleName);
		task.setAudioBundleType(audioBundleType);
		task.setAudioLayerId(audioLayerId);
		task.setAudioChannelId(audioChannelId);
		task.setAudioBaseType(audioBaseType);
		task.setAudioChannelName(audioChannelName);
		task.setTotal_size_mb(total_size_mb);
		task.setTime_duration(time_duration);
		task.setAlarm_size_mb(alarm_size_mb);
		
		//设置所属任务
		Optional.ofNullable(commandSystemTitleDao.findByCurrentTaskEquals(true)).map(titleTask->{
			task.setTaskId(titleTask.getId()==null?null:titleTask.getId())
				.setTaskName(titleTask.getTitleName()==null?"":titleTask.getTitleName());
			return true;
		});
		
		
		
		MonitorRecordStatus status = null;

		// 录制模式选择
		if (MonitorRecordMode.MANUAL.equals(parsedMode)) {
			status = MonitorRecordStatus.RUN;
		} else if (MonitorRecordMode.SCHEDULING.equals(parsedMode)) {
			status = MonitorRecordStatus.WAITING;
		} else {
			throw new ErrorRecordModeException(mode);
		}

		task.setStatus(status);
		task.setType(bLdap?MonitorRecordType.XT_DEVICE:MonitorRecordType.LOCAL_DEVICE);
		task.setUserId(userId);
		task.setUserno(userno);
		task.setNickname(nickname);
		task.setAvTplId(targetAvtpl.getId());
		task.setGearId(targetGear.getId());
		
		monitorRecordDao.save(task);

		// 拼预览地址
		String urlIndex = task.getId() + "-" + Integer.toString(task.getUuid().hashCode()).replace("-", "m") + "/video";
		String previewUrl = urlIndex + ".m3u8";
		task.setPreviewUrl(previewUrl);
		monitorRecordDao.save(task);

		// 处理定时录制   直接下命令
		if (MonitorRecordMode.SCHEDULING.equals(parsedMode)) {
			Date now = new Date();
			now = DateUtil.addMilliSecond(now, MonitorRecordPO.RECORD_OFFSET);
			if (task.getStartTime().after(now)) {
//				return task;
			} else {
				task.setStatus(MonitorRecordStatus.RUN);
			}
		}

		LogicBO logic = bLdap ? startDevicePassby(task, codec, bundle) : openBundle(task, codec);
		logic.merge(startRecord(task, codec));
		sendProtocol(task, logic, bLdap?"点播系统：开始录制外部系统设备":"点播系统：开始录制本地设备");
		
		operationLogService.send(userUtils.queryUserById(userId).getName(), "添加录制", "添加录制任务文件名称："+fileName);

		return task;
	}

	/**
	 * 排期录制设备（支持本系统和外部系统）<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月1日 上午10:18:01
	 * @param String mode 录制模式
	 * @param String fileName 文件名称
	 * @param String startTime 录制开始时间
	 * @param String endTime 录制结束时间
	 * @param String videoBundleId 视频设备id
	 * @param String videoBundleName 视频设备名称
	 * @param String videoBundleType 视频设备类型
	 * @param String videoLayerId 视频设备接入层id
	 * @param String videoChannelId 视频设备通道id
	 * @param String videoBaseType 视频设备通道类型
	 * @param String videoChannelName 视频设备通道名称
	 * @param String audioBundleId 音频设备id
	 * @param String audioBundleName 音频设备名称
	 * @param String audioBundleType 音频设备类型
	 * @param String audioLayerId 音频设备接入层id
	 * @param String audioChannelId 音频通道id
	 * @param String audioBaseType 音频通道类型
	 * @param String audioChannelName 音频通道名称
	 * @param Long userId 操作业务用户id
	 * @param String userno 操作业务用户名称
	 * @param String nickname 操作业务用户昵称
	 * @param String storeMode 排期存储模式//day、week、year
	 * @param String timeQuantum 不同排期模式对应的时间选择
	 * @param Long total_size_mb 此任务可占用的磁盘大小，单位兆，0代表无限大。
	 * @param Long time_duration 此任务可录制的时长，单位：小时，0代表物无限长
	 * @return MonitorRecordPO 录制任务
	 */
	public MonitorRecordPO addDeviceTimeSegment(
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
			Long userId,
			String userno,
			String nickname,
			String storeMode,
			String timeQuantum,
			Long total_size_mb, 
			Long time_duration,
			Long alarm_size_mb
			) throws Exception{

		BundlePO bundle = bundleDao.findByBundleId(videoBundleId);
		boolean bLdap = queryUtil.isLdapBundle(bundle);
		if(bLdap){
			videoBundleName = bundle.getBundleName();
			videoBundleType = bundle.getBundleType();
			videoLayerId = resourceRemoteService.queryLocalLayerId();
			videoChannelId = ChannelType.VIDEOENCODE1.getChannelId();
			//videoBaseType
			videoChannelName = ChannelType.VIDEOENCODE1.getName();
			audioBundleName = bundle.getBundleName();
			audioBundleType = bundle.getBundleType();
			audioLayerId = videoLayerId;
			audioChannelId = ChannelType.AUDIOENCODE1.getChannelId();
			//audioBaseType
			audioChannelName = ChannelType.AUDIOENCODE1.getName();			
		}
		
		//参数模板
		Map<String, Object> result = commons.queryDefaultAvCodec();
		AvtplPO targetAvtpl = (AvtplPO)result.get("avtpl");
		AvtplGearsPO targetGear = (AvtplGearsPO)result.get("gear");
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
		
		MonitorRecordMode parsedMode = MonitorRecordMode.valueOf(mode);
//		Date parsedStartTime = startTime==null?new Date():DateUtil.parse(startTime, DateUtil.dateTimePattern);
//		Date parsedEndTime = endTime==null?null:DateUtil.parse(endTime, DateUtil.dateTimePattern);
		
		MonitorRecordPO task = new MonitorRecordPO();
		task.setMode(parsedMode);
		task.setFileName(fileName);
		
		task.setStartTime(new Date());
//		task.setEndTime(parsedEndTime); //不需要结束时间
		task.setVideoType(MonitorRecordSourceType.DEVICE);
		task.setVideoBundleId(videoBundleId);
		task.setVideoBundleName(videoBundleName);
		task.setVideoBundleType(videoBundleType);
		task.setVideoLayerId(videoLayerId);
		task.setVideoChannelId(videoChannelId);
		task.setVideoBaseType(videoBaseType);
		task.setVideoChannelName(videoChannelName);
		task.setAudioType(MonitorRecordSourceType.DEVICE);
		task.setAudioBundleId(audioBundleId);
		task.setAudioBundleName(audioBundleName);
		task.setAudioBundleType(audioBundleType);
		task.setAudioLayerId(audioLayerId);
		task.setAudioChannelId(audioChannelId);
		task.setAudioBaseType(audioBaseType);
		task.setAudioChannelName(audioChannelName);
		
		task.setStatus(MonitorRecordStatus.RUN);
		
		task.setType(bLdap?MonitorRecordType.XT_DEVICE:MonitorRecordType.LOCAL_DEVICE);
		task.setUserId(userId);
		task.setUserno(userno);
		task.setNickname(nickname);
		task.setAvTplId(targetAvtpl.getId());
		task.setGearId(targetGear.getId());
		task.setTotal_size_mb(total_size_mb);
		task.setTime_duration(time_duration);
		task.setAlarm_size_mb(alarm_size_mb);
		
		//设置所属任务
		Optional.ofNullable(commandSystemTitleDao.findByCurrentTaskEquals(true)).map(titleTask->{
			task.setTaskId(titleTask.getId()==null?null:titleTask.getId())
				.setTaskName(titleTask.getTitleName()==null?"":titleTask.getTitleName());
			return true;
		});
		monitorRecordDao.save(task);
		
		//拼预览地址（这里要查找对应的规则表去取到index）
		String urlIndex = task.getId() + "-" + Integer.toString(task.getUuid().hashCode()).replace("-", "m") + "/video";
		String previewUrl = urlIndex + ".m3u8";
		task.setPreviewUrl(previewUrl);
		monitorRecordDao.save(task);
		
		//录制规则表以及关联实例表
		String dayStart="";
		String dayEnd="";
		String weekStart="";
		String weekEnd="";
		String dayOfMonthStart="";
		String dayOfMonthEnd="";
		Date nextStartTime;
		Date nextEndTime;
		
		boolean shouldRecord=false;
		Date shouldStart=new Date();
		Date shouldEnd=new Date();
		
		MonitorRecordManyTimesRelationPO relation = new MonitorRecordManyTimesRelationPO();
		MonitorRecordManyTimesPO monitorRecordManyTimes = new MonitorRecordManyTimesPO();
		
		if(timeQuantum!=null){
			
			//当天的日期
			String todayDate=DateUtil.format(new Date(),DateUtil.defaultDatePattern);
			
			//整理判断是否在指定时间 
			if(MonitorRecordManyTimesMode.DAY.equals(MonitorRecordManyTimesMode.forName(storeMode))){
				
				dayStart=timeQuantum.split(",")[0];
				dayEnd=timeQuantum.split(",")[1];
				
				//算出开始录制时间以及结束时间
				shouldStart=DateUtil.parse(todayDate+" "+dayStart);
				shouldEnd=DateUtil.parse(todayDate+" "+dayEnd);
				if(DateUtil.compare(shouldStart, shouldEnd)){
					shouldEnd=DateUtil.addDay(shouldEnd, 1);
				}
				
				//
				if(new Date().after(shouldStart) && new Date().before(shouldEnd)){
					shouldRecord=true;
					nextStartTime=DateUtil.addDay(shouldStart, 1);
					nextEndTime=DateUtil.addDay(shouldEnd, 1);
					monitorRecordManyTimes.setEndTime(shouldEnd).setStartTime(new Date());
				}else if(new Date().after(shouldStart) && new Date().after(shouldEnd)){
					nextStartTime=DateUtil.addDay(shouldStart, 1);
					nextEndTime=DateUtil.addDay(shouldEnd, 1);
				}else{
					nextStartTime=shouldStart;
					nextEndTime=shouldEnd;
				}
				
				relation.setNextStartTime(nextStartTime);
				relation.setNextEndTime(nextEndTime);
				
			}else if(MonitorRecordManyTimesMode.WEEK.equals(MonitorRecordManyTimesMode.forName(storeMode))){
				dayStart=timeQuantum.split(",")[0].split("-")[1];
				dayEnd=timeQuantum.split(",")[1].split("-")[1];
				weekStart=timeQuantum.split(",")[0].split("-")[0];
				weekEnd=timeQuantum.split(",")[1].split("-")[0];
				
				//算出本周打开始录制时间
				String weekStartDate = getDayOfWeek(Integer.valueOf(weekStart));
				String weekEndDate=getDayOfWeek(Integer.valueOf(weekEnd));
				 shouldStart=DateUtil.parse(weekStartDate+" "+dayStart);
				 shouldEnd=DateUtil.parse(weekEndDate+" "+dayEnd);
				if(shouldStart.after(shouldEnd)){
					shouldEnd=DateUtil.addDay(shouldEnd, 7);
				}
				
				if(new Date().after(shouldStart) && new Date().before(shouldEnd)){
					shouldRecord=true;
					nextStartTime=DateUtil.addDay(shouldStart, 7);
					nextEndTime=DateUtil.addDay(shouldEnd, 7);
					monitorRecordManyTimes.setEndTime(shouldEnd).setStartTime(new Date());
				}else if(new Date().after(shouldStart) && new Date().after(shouldEnd)){
					nextStartTime=DateUtil.addDay(shouldStart, 7);
					nextEndTime=DateUtil.addDay(shouldEnd, 7);
				}else{
					nextStartTime=shouldStart;
					nextEndTime=shouldEnd;
				}
				
				relation.setNextStartTime(nextStartTime);
				relation.setNextEndTime(nextEndTime);
				
			}else if(MonitorRecordManyTimesMode.MONTH.equals(MonitorRecordManyTimesMode.forName(storeMode))){
				dayStart=timeQuantum.split(",")[0].split("-")[1];
				dayEnd=timeQuantum.split(",")[1].split("-")[1];
				dayOfMonthStart=timeQuantum.split(",")[0].split("-")[0];
				dayOfMonthEnd=timeQuantum.split(",")[1].split("-")[0];
				
				//算出本月的开始录制时间
				shouldStart=getDayOfMonth(Integer.valueOf(dayOfMonthStart),dayStart);
				shouldEnd=getDayOfMonth(Integer.valueOf(dayOfMonthEnd),dayEnd);
				if(shouldStart.after(shouldEnd)){
					shouldEnd=DateUtil.addMonth(shouldEnd, 1);
				}
				
				if(new Date().after(shouldStart)&&new Date().before(shouldEnd)){
					shouldRecord=true;
					nextStartTime=DateUtil.addMonth(shouldStart, 1);
					nextEndTime=DateUtil.addMonth(shouldEnd, 1);
					monitorRecordManyTimes.setEndTime(shouldEnd).setStartTime(new Date());
				}else if(new Date().after(shouldStart) && new Date().after(shouldEnd)){
					nextStartTime=DateUtil.addMonth(shouldStart, 1);
					nextEndTime=DateUtil.addMonth(shouldEnd, 1);
				}else{
					nextStartTime=shouldStart;
					nextEndTime=shouldEnd;
				}
				
				relation.setNextStartTime(nextStartTime);
				relation.setNextEndTime(nextEndTime);
			}
			
			relation.setBusinessId(task.getId())
			.setDayStart(dayStart)
			.setDayEnd(dayEnd)
			.setWeekStart(weekStart)
			.setWeekEnd(weekEnd)
			.setDayOfMonthStart(dayOfMonthStart)
			.setDayOfMonthEnd(dayOfMonthEnd)
			.setIndexNumber(1)
			.setMode(MonitorRecordManyTimesMode.forName(storeMode))
			.setStatus(MonitorRecordStatus.WAITING);
			
			monitorRecordManyTimesRelationDao.save(relation);
			
			if(shouldRecord){
				monitorRecordManyTimes.setStatus(MonitorRecordStatus.RUN)
									.setRelationId(relation.getId())
									.setIndexNumber(relation.getIndexNumber().intValue());
				monitorRecordManyTimesDao.save(monitorRecordManyTimes);
				
				relation.setIndexNumber(relation.getIndexNumber()+1);
				relation.setManyTimeId(monitorRecordManyTimes.getId());
				monitorRecordManyTimesRelationDao.save(relation);
			}
		}
		//录制规则表以及关联实例表结束
		
		//下命令
//		LogicBO logic = openBundle(task, codec);
		LogicBO logic = bLdap ? startDevicePassby(task, codec, bundle) : openBundle(task, codec);
		logic.merge(startManyTimesRecord(task,relation,codec));
		sendProtocol(task, logic, bLdap?"点播系统：开始排期录制外部系统设备":"点播系统：开始排期录制本地设备");
		
		operationLogService.send(userUtils.queryUserById(userId).getName(), "添加排期录制", "添加排期录制任务文件名称："+fileName);
		
		return task;
	}
	
	public String getDayOfWeek(int dayWeek) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		dayWeek=dayWeek-1;
		cal.add(Calendar.DATE, dayWeek - cal.getFirstDayOfWeek());
		Date mondayDate = cal.getTime();
		String dayOfWeek = sdf.format(mondayDate);
		return dayOfWeek;
	}
	
	public Date getDayOfMonth(int dayNumber,String time) throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, dayNumber);
		Date date = cal.getTime();
		String dateStr=DateUtil.format(date, DateUtil.defaultDatePattern);
		dateStr=dateStr+" "+time;
		return DateUtil.parse(dateStr, DateUtil.dateTimePattern);
	}

	/**
	 * 录制xt设备<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月1日 上午10:18:01
	 * 
	 * @param String
	 *            mode 录制模式
	 * @param String
	 *            fileName 文件名称
	 * @param String
	 *            startTime 录制开始时间
	 * @param String
	 *            endTime 录制结束时间
	 * @param UserBO
	 *            user 录制的用户
	 * @param Long
	 *            userId 操作业务用户id
	 * @param String
	 *            userno 操作业务用户名称
	 * @param String
	 *            nickname 录制人昵称
	 * @return MonitorRecordPO 录制任务
	 */
	public MonitorRecordPO addXtDevice(String mode, String fileName, String startTime, String endTime, String bundleId,
			Long userId, String userno, String nickname) throws Exception {

		BundlePO bundle = bundleDao.findByBundleId(bundleId);

		// 虚拟设备id（使用原本的bundleId）
//		String videoBundleId = new StringBufferWrapper().append(bundleId).append("_")
//				.append(UUID.randomUUID().toString().replace("-", "")).toString();
		String videoBundleId = bundleId;
		String videoLayerId = commons.queryNetworkLayerId();
		String audioBundleId = videoBundleId;
		String audioLayerId = videoLayerId;

		// 参数模板
		Map<String, Object> result = commons.queryDefaultAvCodec();
		AvtplPO targetAvtpl = (AvtplPO) result.get("avtpl");
		AvtplGearsPO targetGear = (AvtplGearsPO) result.get("gear");
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl),
				new DeviceGroupAvtplGearsPO().set(targetGear));

		MonitorRecordMode parsedMode = MonitorRecordMode.valueOf(mode);
		Date parsedStartTime = startTime == null ? new Date() : DateUtil.parse(startTime, DateUtil.dateTimePattern);
		Date parsedEndTime = endTime == null ? null : DateUtil.parse(endTime, DateUtil.dateTimePattern);

		MonitorRecordPO task = new MonitorRecordPO();
		task.setMode(parsedMode);
		task.setFileName(fileName);

		task.setStartTime(parsedStartTime);
		task.setEndTime(parsedEndTime);
		task.setVideoType(MonitorRecordSourceType.DEVICE);
		task.setVideoBundleId(videoBundleId);
		task.setVideoBundleName(bundle.getBundleName());
		task.setVideoBundleType(bundle.getBundleType());
		task.setVideoLayerId(videoLayerId);
		task.setVideoChannelId(ChannelType.VIDEOENCODE1.getChannelId());
		task.setVideoChannelName(ChannelType.VIDEOENCODE1.getName());
		task.setAudioType(MonitorRecordSourceType.DEVICE);
		task.setAudioBundleId(audioBundleId);
		task.setAudioBundleName(bundle.getBundleName());
		task.setAudioBundleType(bundle.getBundleType());
		task.setAudioLayerId(audioLayerId);
		task.setAudioChannelId(ChannelType.AUDIOENCODE1.getChannelId());
		task.setAudioChannelName(ChannelType.AUDIOENCODE1.getName());

		MonitorRecordStatus status = null;
		if (MonitorRecordMode.MANUAL.equals(parsedMode)) {
			status = MonitorRecordStatus.RUN;
		} else if (MonitorRecordMode.SCHEDULING.equals(parsedMode)) {
			status = MonitorRecordStatus.WAITING;
		} else {
			throw new ErrorRecordModeException(mode);
		}

		task.setStatus(status);
		task.setType(MonitorRecordType.XT_DEVICE);
		task.setUserId(userId);
		task.setUserno(userno);
		task.setNickname(nickname);
		task.setAvTplId(targetAvtpl.getId());
		task.setGearId(targetGear.getId());
		monitorRecordDao.save(task);

		// 拼预览地址
		String urlIndex = task.getId() + "-" + Integer.toString(task.getUuid().hashCode()).replace("-", "m") + "/video";
		String previewUrl = urlIndex + ".m3u8";
		task.setPreviewUrl(previewUrl);
		monitorRecordDao.save(task);

		// 处理排期录制
		if (MonitorRecordMode.SCHEDULING.equals(parsedMode)) {
			Date now = new Date();
			DateUtil.addMilliSecond(now, MonitorRecordPO.RECORD_OFFSET);
			if (task.getStartTime().after(now)) {
				return task;
			} else {
				task.setStatus(MonitorRecordStatus.RUN);
			}
		}

		LogicBO logic = startDevicePassby(task, codec, bundle);
		logic.merge(startRecord(task, codec));
		sendProtocol(task, logic, "点播系统：开始录制xt设备");

		return task;
	}

	/**
	 * 录制本地用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月1日 上午11:43:50
	 * 
	 * @param String
	 *            mode 录制模式
	 * @param String
	 *            fileName 文件名
	 * @param String
	 *            startTime 开始录制时间
	 * @param String
	 *            endTime 停止录制时间
	 * @param UserBO
	 *            user 录制的本地用户
	 * @param Long
	 *            userId 操作业务的用户id
	 * @param String
	 *            userno 操作业务的用户号码
	 * @param String
	 *            nickname 操作业务的用户昵称
	 * @return MonitorRecordPO 录制任务
	 */
	public MonitorRecordPO addLocalUser(String mode, String fileName, String startTime, String endTime, UserBO user,
			Long userId, String userno, String nickname) throws Exception {

		// 用户绑定的编码器
		List<BundlePO> srcBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>()
				.add(commonQueryUtil.queryExternalOrLocalEncoderIdFromUserBO(user)).getList());
		BundlePO srcBundleEntity = srcBundleEntities.get(0);

		List<ChannelSchemeDTO> srcVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(
				new ArrayListWrapper<String>().add(srcBundleEntity.getBundleId()).getList(),
				ResourceChannelDAO.ENCODE_VIDEO);
		ChannelSchemeDTO srcVideoChannel = srcVideoChannels.get(0);

		List<ChannelSchemeDTO> srcAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(
				new ArrayListWrapper<String>().add(srcBundleEntity.getBundleId()).getList(),
				ResourceChannelDAO.ENCODE_AUDIO);
		ChannelSchemeDTO srcAudioChannel = (srcAudioChannels == null || srcAudioChannels.size() <= 0) ? null
				: srcAudioChannels.get(0);

		// 参数模板
		Map<String, Object> result = commons.queryDefaultAvCodec();
		AvtplPO targetAvtpl = (AvtplPO) result.get("avtpl");
		AvtplGearsPO targetGear = (AvtplGearsPO) result.get("gear");
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl),
				new DeviceGroupAvtplGearsPO().set(targetGear));

		MonitorRecordMode parsedMode = MonitorRecordMode.valueOf(mode);
		Date parsedStartTime = startTime == null ? new Date() : DateUtil.parse(startTime, DateUtil.dateTimePattern);
		Date parsedEndTime = endTime == null ? null : DateUtil.parse(endTime, DateUtil.dateTimePattern);

		MonitorRecordPO task = new MonitorRecordPO();
		task.setMode(parsedMode);
		task.setFileName(fileName);

		task.setStartTime(parsedStartTime);
		task.setEndTime(parsedEndTime);
		task.setVideoType(MonitorRecordSourceType.DEVICE);
		task.setVideoBundleId(srcBundleEntity.getBundleId());
		task.setVideoBundleName(srcBundleEntity.getBundleName());
		task.setVideoBundleType(srcBundleEntity.getBundleType());
		task.setVideoLayerId(srcBundleEntity.getAccessNodeUid());
		task.setVideoChannelId(srcVideoChannel.getChannelId());
		task.setVideoBaseType(srcVideoChannel.getBaseType());
		task.setVideoChannelName(ChannelType.transChannelName(srcVideoChannel.getChannelId()));
		task.setAudioType(MonitorRecordSourceType.DEVICE);
		task.setAudioBundleId(srcAudioChannel == null ? null : srcBundleEntity.getBundleId());
		task.setAudioBundleName(srcAudioChannel == null ? null : srcBundleEntity.getBundleName());
		task.setAudioBundleType(srcAudioChannel == null ? null : srcBundleEntity.getBundleType());
		task.setAudioLayerId(srcAudioChannel == null ? null : srcBundleEntity.getAccessNodeUid());
		task.setAudioChannelId(srcAudioChannel == null ? null : srcAudioChannel.getChannelId());
		task.setAudioBaseType(srcAudioChannel == null ? null : srcAudioChannel.getBaseType());
		task.setAudioChannelName(srcAudioChannel == null ? null : srcAudioChannel.getChannelName());

		MonitorRecordStatus status = null;
		if (MonitorRecordMode.MANUAL.equals(parsedMode)) {
			status = MonitorRecordStatus.RUN;
		} else if (MonitorRecordMode.SCHEDULING.equals(parsedMode)) {
			status = MonitorRecordStatus.WAITING;
		} else {
			throw new ErrorRecordModeException(mode);
		}

		task.setStatus(status);
		task.setType(MonitorRecordType.LOCAL_USER);
		task.setRecordUserId(user.getId());
		task.setRecordUsername(user.getName());
		task.setRecordUserno(user.getUserNo());
		task.setUserId(userId);
		task.setUserno(userno);
		task.setNickname(nickname);
		task.setAvTplId(targetAvtpl.getId());
		task.setGearId(targetGear.getId());
		monitorRecordDao.save(task);

		// 拼预览地址
		String urlIndex = task.getId() + "-" + Integer.toString(task.getUuid().hashCode()).replace("-", "m") + "/video";
		String previewUrl = urlIndex + ".m3u8";
		task.setPreviewUrl(previewUrl);
		monitorRecordDao.save(task);

		// 处理排期录制
		if (MonitorRecordMode.SCHEDULING.equals(parsedMode)) {
			Date now = new Date();
			DateUtil.addMilliSecond(now, MonitorRecordPO.RECORD_OFFSET);
			if (task.getStartTime().after(now)) {
				return task;
			} else {
				task.setStatus(MonitorRecordStatus.RUN);
			}
		}

		LogicBO logic = openBundle(task, codec);
		logic.merge(startRecord(task, codec));
		sendProtocol(task, logic, "点播系统：开始录制本地用户");

		return task;

	}

	/**
	 * 录制xt用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月1日 下午1:08:18
	 * 
	 * @param String
	 *            mode 录制模式
	 * @param String
	 *            fileName 文件名
	 * @param String
	 *            startTime 开始录制时间
	 * @param String
	 *            endTime 结束录制时间
	 * @param UserBO
	 *            user xt用户
	 * @param Long
	 *            userId 操作业务用户id
	 * @param String
	 *            userno 操作业务用户号码
	 * @param String
	 *            nickname 操作业务用户昵称
	 * @return MonitorRecordPO 录制任务
	 */
	public MonitorRecordPO addXtUser(String mode, String fileName, String startTime, String endTime, UserBO user,
			Long userId, String userno, String nickname) throws Exception {

		// 联网id
		String networkLayerId = commons.queryNetworkLayerId();

		// xt用户虚拟编码器
		String bundleId = UUID.randomUUID().toString().replaceAll("-", "");

		// 参数模板
		Map<String, Object> result = commons.queryDefaultAvCodec();
		AvtplPO targetAvtpl = (AvtplPO) result.get("avtpl");
		AvtplGearsPO targetGear = (AvtplGearsPO) result.get("gear");
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl),
				new DeviceGroupAvtplGearsPO().set(targetGear));

		MonitorRecordMode parsedMode = MonitorRecordMode.valueOf(mode);
		Date parsedStartTime = startTime == null ? new Date() : DateUtil.parse(startTime, DateUtil.dateTimePattern);
		Date parsedEndTime = endTime == null ? null : DateUtil.parse(endTime, DateUtil.dateTimePattern);

		MonitorRecordPO task = new MonitorRecordPO();
		task.setMode(parsedMode);
		task.setFileName(fileName);

		task.setStartTime(parsedStartTime);
		task.setEndTime(parsedEndTime);
		task.setVideoType(MonitorRecordSourceType.DEVICE);
		task.setVideoBundleId(bundleId);
		task.setVideoBundleName(user.getName() + "用户视频");
		task.setVideoLayerId(networkLayerId);
		task.setVideoChannelId(ChannelType.VIDEOENCODE1.getChannelId());
		task.setVideoChannelName(ChannelType.VIDEOENCODE1.getName());
		task.setAudioType(MonitorRecordSourceType.DEVICE);
		task.setAudioBundleId(bundleId);
		task.setAudioBundleName(user.getName() + "用户音频");
		task.setAudioLayerId(networkLayerId);
		task.setAudioChannelId(ChannelType.AUDIOENCODE1.getChannelId());
		task.setAudioChannelName(ChannelType.AUDIOENCODE1.getName());

		MonitorRecordStatus status = null;
		if (MonitorRecordMode.MANUAL.equals(parsedMode)) {
			status = MonitorRecordStatus.RUN;
		} else if (MonitorRecordMode.SCHEDULING.equals(parsedMode)) {
			status = MonitorRecordStatus.WAITING;
		} else {
			throw new ErrorRecordModeException(mode);
		}

		task.setStatus(status);
		task.setType(MonitorRecordType.XT_USER);
		task.setRecordUserId(user.getId());
		task.setRecordUsername(user.getName());
		task.setRecordUserno(user.getUserNo());
		task.setUserId(userId);
		task.setUserno(userno);
		task.setNickname(nickname);
		task.setAvTplId(targetAvtpl.getId());
		task.setGearId(targetGear.getId());
		monitorRecordDao.save(task);

		// 拼预览地址
		String urlIndex = task.getId() + "-" + Integer.toString(task.getUuid().hashCode()).replace("-", "m") + "/video";
		String previewUrl = urlIndex + ".m3u8";
		task.setPreviewUrl(previewUrl);
		monitorRecordDao.save(task);

		// 处理排期录制
		if (MonitorRecordMode.SCHEDULING.equals(parsedMode)) {
			Date now = new Date();
			DateUtil.addMilliSecond(now, MonitorRecordPO.RECORD_OFFSET);
			if (task.getStartTime().after(now)) {
				return task;
			} else {
				task.setStatus(MonitorRecordStatus.RUN);
			}
		}

		LogicBO logic = startUserPassby(task, codec);
		logic.merge(startRecord(task, codec));
		sendProtocol(task, logic, "点播系统：开始录制xt用户");

		return task;
	}

	/**
	 * 停止录制任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月1日 下午1:16:58
	 * 
	 * @param Long
	 *            id 录制任务id
	 * @param Long
	 *            userId 操作业务用户id
	 */
	public void stop(Long id, Long userId) throws Exception {

		UserVO user = userQuery.current();

		MonitorRecordPO task = monitorRecordDao.findOne(id);
		if (task == null)
			return;

		if (userId.longValue() == 1 || user.getIsGroupCreator()) {
			userId = task.getUserId();
		}

		if (!userId.equals(task.getUserId())) {
			throw new UserHashNoPermissionToStopMonitorRecordException(id, userId);
		}

		task.setStatus(MonitorRecordStatus.STOP);
		if (task.getEndTime() == null) {
			task.setEndTime(new Date());
		}
		
		//如果是排期，做一些其他处理
		if(MonitorRecordMode.TIMESEGMENT.equals(task.getMode())){
			stopManyTimesRecord(task);
		}
		
		monitorRecordDao.save(task);

		if (MonitorRecordType.LOCAL_DEVICE.equals(task.getType())) {
			stopLocalDevice(task);
		} else if (MonitorRecordType.XT_DEVICE.equals(task.getType())) {
			stopXtDevice(task);
		} else if (MonitorRecordType.LOCAL_USER.equals(task.getType())) {
			stopLocalUser(task);
		} else if (MonitorRecordType.XT_USER.equals(task.getType())) {
			stopXtUser(task);
		}
		
		operationLogService.send(user.getUsername(), "停止录制任务", user.getUsername()+"停止录制任务：" +task.getFileName());
	}
	
	/**
	 * 排期的停止<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月22日 下午2:42:07
	 * @param task 录制任务
	 */
	public void stopManyTimesRecord(MonitorRecordPO task){
		
		if(!MonitorRecordMode.TIMESEGMENT.equals(task.getMode())){
			return;
		}
		
		MonitorRecordManyTimesRelationPO relation=monitorRecordManyTimesRelationDao.findByBusinessId(task.getId());
		
		if(relation==null){
			return;
		}
		
		relation.setStatus(MonitorRecordStatus.STOP);
		monitorRecordManyTimesRelationDao.save(relation);
		
		MonitorRecordManyTimesPO manyTimesRecord=monitorRecordManyTimesDao.findByRelationIdAndStatusNot(relation.getId(),MonitorRecordStatus.STOP);
		
		if(manyTimesRecord==null){
			return;
		}
		
		manyTimesRecord.setEndTime(new Date());
		manyTimesRecord.setStatus(MonitorRecordStatus.STOP);
		monitorRecordManyTimesDao.save(manyTimesRecord);
		
	}

	/**
	 * 停止录制本地设备<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月1日 下午1:26:09
	 * 
	 * @param MonitorRecordPO
	 *            task 录制任务
	 */
	public void stopLocalDevice(MonitorRecordPO task) throws Exception {
		LogicBO logic = closeBundle(task);
		logic.merge(stopRecord(task));
		executeBusiness.execute(logic, "点播系统：停止录制本地设备");
		saveOrRemoveLianwangPassbyToResource(logic);
	}

	/**
	 * 停止录制xt设备<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月1日 下午1:31:08
	 * 
	 * @param MonitorRecordPO
	 *            task 录制任务
	 */
	public void stopXtDevice(MonitorRecordPO task) throws Exception {

		BundlePO bundle = bundleDao.findByBundleId(task.getVideoBundleId().split("_")[0]);

		AvtplPO targetAvtpl = avtplDao.findOne(task.getAvTplId());
		AvtplGearsPO targetGear = avtplGearsDao.findOne(task.getGearId());
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl),
				new DeviceGroupAvtplGearsPO().set(targetGear));

		LogicBO logic = stopRecord(task);
		logic.merge(stopDevicePassby(task, codec, bundle));
		executeBusiness.execute(logic, "点播系统：停止录制xt设备");
		saveOrRemoveLianwangPassbyToResource(logic);
	}

	/**
	 * 停止录制本地用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月1日 下午1:27:07
	 * 
	 * @param MonitorRecordPO
	 *            task 录制任务
	 */
	public void stopLocalUser(MonitorRecordPO task) throws Exception {
		LogicBO logic = closeBundle(task);
		logic.merge(stopRecord(task));
		executeBusiness.execute(logic, "点播系统：停止录制本地用户");
		saveOrRemoveLianwangPassbyToResource(logic);
	}

	/**
	 * 停止录制xt用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月1日 下午1:36:05
	 * 
	 * @param MonitorRecordPO
	 *            task 录制任务
	 */
	public void stopXtUser(MonitorRecordPO task) throws Exception {

		AvtplPO targetAvtpl = avtplDao.findOne(task.getAvTplId());
		AvtplGearsPO targetGear = avtplGearsDao.findOne(task.getGearId());
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl),
				new DeviceGroupAvtplGearsPO().set(targetGear));

		LogicBO logic = stopRecord(task);
		logic.merge(stopUserPassby(task, codec));
		executeBusiness.execute(logic, "点播系统：停止录制xt用户");
		saveOrRemoveLianwangPassbyToResource(logic);
	}

	/**
	 * 处理openbundle协议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月1日 上午10:07:22
	 * 
	 * @param MonitorRecordPO
	 *            task 录制任务
	 * @param CodecParamBO
	 *            codec 参数模板
	 * @param Long
	 *            userId 用户id
	 * @return LogicBO 协议
	 */
	private LogicBO openBundle(MonitorRecordPO task, CodecParamBO codec) throws Exception {

		LogicBO logic = new LogicBO().setUserId(task.getUserId().toString())
				.setConnectBundle(new ArrayList<ConnectBundleBO>());
		
		BundlePO videoBundle = bundleService.findByBundleId(task.getVideoBundleId());

		ConnectBundleBO connectVideoBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
				.setOperateType(ConnectBundleBO.OPERATE_TYPE).setLock_type("write").setBundleId(task.getVideoBundleId())
				.setLayerId(task.getVideoLayerId()).setBundle_type(task.getVideoBundleType());
		ConnectBO connectVideoChannel = new ConnectBO().setChannelId(task.getVideoChannelId()).setChannel_status("Open")
				.setBase_type(task.getVideoBaseType()).setCodec_param(codec);
		//发组播视频
		if(Boolean.TRUE.equals(videoBundle.getMulticastEncode())){
			String videoAddr = multicastService.addrAddPort(videoBundle.getMulticastEncodeAddr(), 0);
			connectVideoChannel.setMode(TransmissionMode.MULTICAST.getCode())
								.setMulti_addr(videoAddr)
								.setSrc_multi_ip(videoBundle.getMulticastSourceIp());
		}
		connectVideoBundle.setChannels(new ArrayListWrapper<ConnectBO>().add(connectVideoChannel).getList());
		logic.getConnectBundle().add(connectVideoBundle);

		if (task.getAudioBundleId() != null) {
			if (task.getVideoBundleId().equals(task.getAudioBundleId())) {
				ConnectBO connectAudioChannel = new ConnectBO().setChannelId(task.getAudioChannelId())
						.setChannel_status("Open").setBase_type(task.getAudioBaseType()).setCodec_param(codec);
				//发组播音频
				if(Boolean.TRUE.equals(videoBundle.getMulticastEncode())){
					String audioAddr = multicastService.addrAddPort(videoBundle.getMulticastEncodeAddr(), 2);
					connectAudioChannel.setMode(TransmissionMode.MULTICAST.getCode())
										.setMulti_addr(audioAddr)
										.setSrc_multi_ip(videoBundle.getMulticastSourceIp());
				}
				connectVideoBundle.getChannels().add(connectAudioChannel);
			} else {
				ConnectBundleBO connectAudioBundle = new ConnectBundleBO()
						.setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD).setOperateType(ConnectBundleBO.OPERATE_TYPE)
						.setLock_type("write").setBundleId(task.getAudioBundleId()).setLayerId(task.getAudioLayerId())
						.setBundle_type(task.getAudioBundleType());
				ConnectBO connectAudioChannel = new ConnectBO().setChannelId(task.getAudioChannelId())
						.setChannel_status("Open").setBase_type(task.getAudioBaseType()).setCodec_param(codec);
				connectAudioBundle.setChannels(new ArrayListWrapper<ConnectBO>().add(connectAudioChannel).getList());
				logic.getConnectBundle().add(connectAudioBundle);
			}
		}

		return logic;
	}

	/**
	 * 处理xt设备passby<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月1日 下午1:56:40
	 * 
	 * @param MonitorRecordPO
	 *            task 录制任务
	 * @param CodecParamBO
	 *            codec 参数模板
	 * @param TerminalBundlePO
	 *            bundle xt设备
	 * @return LogicBO 协议
	 */
	public LogicBO startDevicePassby(MonitorRecordPO task, CodecParamBO codec, BundlePO bundle) throws Exception {

		LogicBO logic = new LogicBO().setUserId(task.getUserId().toString()).setPass_by(new ArrayList<PassByBO>());

		XtBusinessPassByContentBO passByContent = new XtBusinessPassByContentBO()
				.setCmd(XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_ENCODER)
				.setOperate(XtBusinessPassByContentBO.OPERATE_START)
				.setUuid(task.getUuid())
				.setSrc_user(task.getUserno())
				.setXt_encoder(new HashMapWrapper<String, String>().put("layerid", task.getVideoLayerId())
						.put("bundleid", task.getVideoBundleId()).put("video_channelid", task.getVideoChannelId())
						.put("audio_channelid", task.getAudioChannelId()).getMap())
				.setDst_number(bundle.getUsername()).setVparam(codec);

		PassByBO passby = new PassByBO().setLayer_id(task.getVideoLayerId())
				.setType(XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_ENCODER).setPass_by_content(passByContent);
		logic.getPass_by().add(passby);

		return logic;
	}

	/**
	 * 处理xt用户passby<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月1日 下午2:00:39
	 * 
	 * @param MonitorRecordPO
	 *            task 录制任务
	 * @param CodecParamBO
	 *            codec 参数模板
	 * @return LogicBO 协议
	 */
	public LogicBO startUserPassby(MonitorRecordPO task, CodecParamBO codec) throws Exception {

		LogicBO logic = new LogicBO().setUserId(task.getUserId().toString()).setPass_by(new ArrayList<PassByBO>());

		XtBusinessPassByContentBO passByContent = new XtBusinessPassByContentBO()
				.setCmd(XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_USER)
				.setOperate(XtBusinessPassByContentBO.OPERATE_START).setUuid(task.getUuid())
				.setSrc_user(task.getUserno())
				.setXt_encoder(new HashMapWrapper<String, String>().put("layerid", task.getVideoLayerId())
						.put("bundleid", task.getVideoBundleId()).put("video_channelid", task.getVideoChannelId())
						.put("audio_channelid", task.getAudioChannelId()).getMap())
				.setDst_number(task.getRecordUserno()).setVparam(codec);

		PassByBO passby = new PassByBO().setLayer_id(task.getVideoLayerId())
				.setType(XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_USER).setPass_by_content(passByContent);

		logic.getPass_by().add(passby);

		return logic;
	}

	/**
	 * 处理开始录制协议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月1日 上午10:11:45
	 * 
	 * @param MonitorRecordPO
	 *            task 录制任务
	 * @param CodecParamBO
	 *            codec 参数模板
	 * @return LogicBO 协议数据
	 */
	private LogicBO startRecord(MonitorRecordPO task, CodecParamBO codec) throws Exception {

		LogicBO logic = new LogicBO().setUserId(task.getUserId().toString());

		RecordSetBO recordSet = new RecordSetBO().setGroupUuid(task.getUuid()).setUuid(task.getUuid()).setVideoType("2")
				.setVideoName(task.getFileName()).setUrl(task.getPreviewUrl().replace(".m3u8", ""))
				.setPlayUrl(task.getPreviewUrl()).setCodec_param(codec);
		RecordSourceBO videoSource = new RecordSourceBO().setType("channel").setBundle_id(task.getVideoBundleId())
				.setLayer_id(task.getVideoLayerId()).setChannel_id(task.getVideoChannelId());
		recordSet.setVideo_source(videoSource);
		
		//额外添加
		recordSet.setCycle(new CycleBO().setTime_duration(task.getTime_duration()).setTotal_size_mb(task.getTotal_size_mb()))
				 .setAlarm(new AlarmBO().setCap_alarm(new AapAlarmBO().setSize_mb(task.getAlarm_size_mb())))
				 .setStore_mode(task.getMode().getCode());
		if(MonitorRecordMode.SCHEDULING.equals(task.getMode())){
			recordSet.setDatetime(new RecordDateTimeBO().setStart(DateUtil.format(task.getStartTime(), DateUtil.dateTimePattenWithoutSecind))
														.setEnd(DateUtil.format(task.getEndTime(), DateUtil.dateTimePattenWithoutSecind)));
		}
		//额外添加结束
		
		//收组播视频
		BundlePO videoBundle = bundleService.findByBundleId(task.getVideoBundleId());
		if(Boolean.TRUE.equals(videoBundle.getMulticastEncode())){
			String videoAddr = multicastService.addrAddPort(videoBundle.getMulticastEncodeAddr(), 0);
			recordSet.setMode(TransmissionMode.MULTICAST.getCode())
								.setMulti_video_addr(videoAddr)
								.setSrc_multi_ip(videoBundle.getMulticastSourceIp());
		}
		
		if (task.getAudioBundleId() != null) {
			RecordSourceBO audioSource = new RecordSourceBO().setType("channel").setBundle_id(task.getAudioBundleId())
					.setLayer_id(task.getAudioLayerId()).setChannel_id(task.getAudioChannelId());
			recordSet.setAudio_source(audioSource);
			//收组播音频
			if(Boolean.TRUE.equals(videoBundle.getMulticastEncode())){
				String audioAddr = multicastService.addrAddPort(videoBundle.getMulticastEncodeAddr(), 2);
				recordSet.setMode(TransmissionMode.MULTICAST.getCode())
									.setMulti_audio_addr(audioAddr)
									.setSrc_multi_ip(videoBundle.getMulticastSourceIp());
			}
		}
		logic.setRecordSet(new ArrayListWrapper<RecordSetBO>().add(recordSet).getList());

		return logic;
	}
	
	/**
	 * 处理开始排期录制协议<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月18日 上午11:29:59
	 * @param task
	 * @param codec
	 * @return
	 * @throws Exception
	 */
	private LogicBO startManyTimesRecord(MonitorRecordPO task,MonitorRecordManyTimesRelationPO relation, CodecParamBO codec) throws Exception {

		LogicBO logic = new LogicBO().setUserId(task.getUserId().toString());

		RecordSetBO recordSet = new RecordSetBO().setGroupUuid(task.getUuid()).setUuid(task.getUuid()).setVideoType("2")
				.setVideoName(task.getFileName()).setUrl(task.getPreviewUrl().replace(".m3u8", ""))
				.setPlayUrl(task.getPreviewUrl()).setCodec_param(codec);
		RecordSourceBO videoSource = new RecordSourceBO().setType("channel").setBundle_id(task.getVideoBundleId())
				.setLayer_id(task.getVideoLayerId()).setChannel_id(task.getVideoChannelId());
		recordSet.setVideo_source(videoSource);
		
		//处理排期额外添加开始
		RecordTimeSegmentBO timeSegment=new RecordTimeSegmentBO().set(relation);
		recordSet.setCycle(new CycleBO().setTime_duration(task.getTime_duration()).setTotal_size_mb(task.getTotal_size_mb()))
				 .setAlarm(new AlarmBO().setCap_alarm(new AapAlarmBO().setSize_mb(task.getAlarm_size_mb())))
				 .setStore_mode(task.getMode().getCode())
				 .setTime_segment(timeSegment);
		//添加结束
		
		//收组播视频
		BundlePO videoBundle = bundleService.findByBundleId(task.getVideoBundleId());
		if(Boolean.TRUE.equals(videoBundle.getMulticastEncode())){
			String videoAddr = multicastService.addrAddPort(videoBundle.getMulticastEncodeAddr(), 0);
			recordSet.setMode(TransmissionMode.MULTICAST.getCode())
								.setMulti_video_addr(videoAddr)
								.setSrc_multi_ip(videoBundle.getMulticastSourceIp());
		}
		
		if (task.getAudioBundleId() != null) {
			RecordSourceBO audioSource = new RecordSourceBO().setType("channel").setBundle_id(task.getAudioBundleId())
					.setLayer_id(task.getAudioLayerId()).setChannel_id(task.getAudioChannelId());
			recordSet.setAudio_source(audioSource);
			//收组播音频
			if(Boolean.TRUE.equals(videoBundle.getMulticastEncode())){
				String audioAddr = multicastService.addrAddPort(videoBundle.getMulticastEncodeAddr(), 2);
				recordSet.setMode(TransmissionMode.MULTICAST.getCode())
									.setMulti_audio_addr(audioAddr)
									.setSrc_multi_ip(videoBundle.getMulticastSourceIp());
			}
		}
		logic.setRecordSet(new ArrayListWrapper<RecordSetBO>().add(recordSet).getList());

		return logic;
	}

	/**
	 * 录制任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月1日 上午10:16:19
	 * 
	 * @param MonitorRecordPO
	 *            task 录制任务
	 * @param DispatchBO
	 *            logic 协议
	 * @param DispatchBO
	 *            log 打印日志
	 */
	private void sendProtocol(MonitorRecordPO task, LogicBO logic, String log) throws Exception {

		ExecuteBusinessReturnBO response = executeBusiness.execute(logic, log);

		task.setStoreLayerId(response.getOutConnMediaMuxSet().get(0).getLayerId());
		monitorRecordDao.save(task);
		
		saveOrRemoveLianwangPassbyToResource(logic);
	}

	/**
	 * 处理closeBundle协议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月1日 下午1:20:22
	 * 
	 * @param MonitorRecordPO
	 *            task 录制任务
	 * @return LogicBO 协议
	 */
	public LogicBO closeBundle(MonitorRecordPO task) throws Exception {
		LogicBO logic = new LogicBO().setUserId(task.getUserId().toString())
				.setDisconnectBundle(new ArrayList<DisconnectBundleBO>());

		DisconnectBundleBO disconnectVideoBundle = new DisconnectBundleBO()
				.setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD).setOperateType(DisconnectBundleBO.OPERATE_TYPE)
				.setBundleId(task.getVideoBundleId()).setBundle_type(task.getVideoBundleType())
				.setLayerId(task.getVideoLayerId());
		logic.setDisconnectBundle(new ArrayListWrapper<DisconnectBundleBO>().add(disconnectVideoBundle).getList());

		if (task.getAudioBundleId() != null && !task.getVideoBundleId().equals(task.getAudioBundleId())) {
			DisconnectBundleBO disconnectAudioBundle = new DisconnectBundleBO()
					.setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
					.setOperateType(DisconnectBundleBO.OPERATE_TYPE).setBundleId(task.getAudioBundleId())
					.setBundle_type(task.getAudioBundleType()).setLayerId(task.getAudioLayerId());
			logic.setDisconnectBundle(new ArrayListWrapper<DisconnectBundleBO>().add(disconnectAudioBundle).getList());
		}
		return logic;
	}

	/**
	 * 处理停止xt设备passby<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月1日 下午2:05:30
	 * 
	 * @param MonitorRecordPO
	 *            task 录制任务
	 * @param CodecParamBO
	 *            codec 参数模板
	 * @param TerminalBundlePO
	 *            bundle 设备
	 * @return LogicBO 协议
	 */
	public LogicBO stopDevicePassby(MonitorRecordPO task, CodecParamBO codec, BundlePO bundle) throws Exception {

		LogicBO logic = new LogicBO().setUserId(task.getUserId().toString()).setPass_by(new ArrayList<PassByBO>());

		XtBusinessPassByContentBO passByContent = new XtBusinessPassByContentBO()
				.setCmd(XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_ENCODER)
				.setOperate(XtBusinessPassByContentBO.OPERATE_STOP)
				.setUuid(task.getUuid())
				.setSrc_user(task.getUserno())
				.setXt_encoder(new HashMapWrapper<String, String>().put("layerid", task.getVideoLayerId())
						.put("bundleid", task.getVideoBundleId()).put("video_channelid", task.getVideoChannelId())
						.put("audio_channelid", task.getAudioChannelId()).getMap())
				.setDst_number(bundle.getUsername()).setVparam(codec);

		PassByBO passby = new PassByBO().setLayer_id(task.getVideoLayerId())
				.setType(XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_ENCODER).setPass_by_content(passByContent);
		logic.getPass_by().add(passby);

		return logic;
	}

	/**
	 * 处理停止xt用户passby<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月1日 下午2:09:34
	 * 
	 * @param MonitorRecordPO
	 *            task 录制任务
	 * @param CodecParamBO
	 *            codec 参数模板
	 * @return LogicBO 协议
	 */
	public LogicBO stopUserPassby(MonitorRecordPO task, CodecParamBO codec) throws Exception {

		LogicBO logic = new LogicBO().setUserId(task.getUserId().toString()).setPass_by(new ArrayList<PassByBO>());

		XtBusinessPassByContentBO passByContent = new XtBusinessPassByContentBO()
				.setCmd(XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_USER)
				.setOperate(XtBusinessPassByContentBO.OPERATE_STOP).setUuid(task.getUuid())
				.setSrc_user(task.getUserno())
				.setXt_encoder(new HashMapWrapper<String, String>().put("layerid", task.getVideoLayerId())
						.put("bundleid", task.getVideoBundleId()).put("video_channelid", task.getVideoChannelId())
						.put("audio_channelid", task.getAudioChannelId()).getMap())
				.setDst_number(task.getRecordUserno()).setVparam(codec);

		PassByBO passby = new PassByBO().setLayer_id(task.getVideoLayerId())
				.setType(XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_USER).setPass_by_content(passByContent);

		logic.getPass_by().add(passby);

		return logic;
	}

	/**
	 * 处理停止录制协议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月1日 下午1:22:42
	 * 
	 * @param MonitorRecordPO
	 *            task 录制任务
	 * @return LogicBO 协议
	 */
	public LogicBO stopRecord(MonitorRecordPO task) throws Exception {
		LogicBO logic = new LogicBO().setUserId(task.getUserId().toString());
		RecordSetBO recordDel = new RecordSetBO().setUuid(task.getUuid());
		logic.setRecordDel(new ArrayListWrapper<RecordSetBO>().add(recordDel).getList());
		return logic;
	}

	/**
	 * 添加一个录制任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月18日 上午9:11:40
	 * 
	 * @param String
	 *            mode 录制模式
	 * @param String
	 *            fileName 文件名称
	 * @param String
	 *            startTime 录制开始时间
	 * @param String
	 *            endTime 录制结束时间
	 * @param String
	 *            videoType 视频源类型
	 * @param String
	 *            combineVideoUuid 合屏uuid
	 * @param String
	 *            combineVideoName 合屏名称
	 * @param String
	 *            videoBundleId 视频设备id
	 * @param String
	 *            videoBundleName 视频设备名称
	 * @param String
	 *            videoBundleType 视频设备类型
	 * @param String
	 *            videoChannelId 视频通道id
	 * @param String
	 *            videoBaseType 视频通道类型
	 * @param String
	 *            videoChannelName 视频通道名称
	 * @param String
	 *            audioType 音频源类型
	 * @param String
	 *            combineAudioUuid 混音uuid
	 * @param String
	 *            combineAudioName 混音名称
	 * @param String
	 *            audioBundleId 音频设备id
	 * @param String
	 *            audioBundleName 音频设备名称
	 * @param String
	 *            audioBundleType 音频设备类型
	 * @param String
	 *            audioChannelId 音频通道id
	 * @param String
	 *            audioBaseType 音频通道类型
	 * @param String
	 *            audioChannelName 音频通道名称
	 * @param MonitorRecordType
	 *            type 录制类型
	 * @param Long
	 *            recordUserId 录制用户id
	 * @param String
	 *            recordUsername 录制用户名称
	 * @param String
	 *            recordUserno 录制用户号码
	 * @param Long
	 *            userId 业务用户id
	 * @return MonitorRecordPO 录制任务
	 */
	@Deprecated
	public MonitorRecordPO add(String mode, String fileName, String startTime, String endTime, String videoType,
			String combineVideoUuid, String combineVideoName, String videoBundleId, String videoBundleName,
			String videoBundleType, String videoLayerId, String videoChannelId, String videoBaseType,
			String videoChannelName, String audioType, String combineAudioUuid, String combineAudioName,
			String audioBundleId, String audioBundleName, String audioBundleType, String audioLayerId,
			String audioChannelId, String audioBaseType, String audioChannelName, MonitorRecordType type,
			Long recordUserId, String recordUsername, String recordUserno, Long userId) throws Exception {

		if ((videoBundleId == null || "".equals(videoBundleId))
				&& (combineVideoUuid == null || "".equals(combineVideoUuid))) {
			throw new MonitorRecordSourceVideoCannotBeNullException();
		}

		// 查询codec参数
		List<AvtplPO> avTpls = avtplDao.findByUsageType(AvtplUsageType.VOD);
		if (avTpls == null || avTpls.size() <= 0) {
			throw new AvtplNotFoundException("系统缺少点播系统参数模板！");
		}
		AvtplPO targetAvtpl = avTpls.get(0);

		// 查询codec模板档位
		AvtplGearsPO targetGear = null;
		Set<AvtplGearsPO> gears = targetAvtpl.getGears();
		for (AvtplGearsPO gear : gears) {
			targetGear = gear;
			break;
		}

		MonitorRecordMode parsedMode = MonitorRecordMode.valueOf(mode);

		Date parsedStartTime = null;
		if (startTime == null) {
			parsedStartTime = new Date();
		} else {
			parsedStartTime = DateUtil.parse(startTime, DateUtil.dateTimePattern);
		}

		Date parsedEndTime = null;
		if (endTime != null) {
			parsedEndTime = DateUtil.parse(endTime, DateUtil.dateTimePattern);
		}

		MonitorRecordSourceType parsedVideoType = MonitorRecordSourceType.valueOf(videoType);
		MonitorRecordSourceType parsedAudioType = MonitorRecordSourceType.valueOf(audioType);

		MonitorRecordPO task = new MonitorRecordPO();
		task.setMode(parsedMode);
		task.setFileName(fileName);

		task.setStartTime(parsedStartTime);
		task.setEndTime(parsedEndTime);
		task.setVideoType(parsedVideoType);
		task.setCombineVideoUuid(combineVideoUuid);
		task.setCombineVideoName(combineVideoName);
		task.setVideoBundleId(videoBundleId);
		task.setVideoBundleName(videoBundleName);
		task.setVideoBundleType(videoBundleType);
		task.setVideoLayerId(videoLayerId);
		task.setVideoChannelId(videoChannelId);
		task.setVideoBaseType(videoBaseType);
		task.setVideoChannelName(videoChannelName);
		task.setAudioType(parsedAudioType);
		task.setCombineAudioUuid(combineAudioUuid);
		task.setCombineAudioName(combineAudioName);
		task.setAudioBundleId(audioBundleId);
		task.setAudioBundleName(audioBundleName);
		task.setAudioBundleType(audioBundleType);
		task.setAudioLayerId(audioLayerId);
		task.setAudioChannelId(audioChannelId);
		task.setAudioBaseType(audioBaseType);
		task.setAudioChannelName(audioChannelName);

		MonitorRecordStatus status = null;
		if (MonitorRecordMode.MANUAL.equals(parsedMode)) {
			status = MonitorRecordStatus.RUN;
		} else if (MonitorRecordMode.SCHEDULING.equals(parsedMode)) {
			status = MonitorRecordStatus.WAITING;
		} else {
			throw new ErrorRecordModeException(mode);
		}

		task.setStatus(status);
		task.setType(type);
		task.setRecordUserId(recordUserId == null ? null : recordUserId);
		task.setRecordUsername(recordUsername);
		task.setRecordUserno(recordUserno);
		task.setUserId(userId);
		task.setAvTplId(targetAvtpl.getId());
		task.setGearId(targetGear.getId());
		monitorRecordDao.save(task);

		// 拼预览地址
		String urlIndex = task.getId() + "-" + Integer.toString(task.getUuid().hashCode()).replace("-", "m") + "/video";
		String previewUrl = urlIndex + ".m3u8";
		task.setPreviewUrl(previewUrl);
		monitorRecordDao.save(task);

		// 处理排期录制
		if (MonitorRecordMode.SCHEDULING.equals(parsedMode)) {
			Date now = new Date();
			DateUtil.addMilliSecond(now, MonitorRecordPO.RECORD_OFFSET);
			if (task.getStartTime().after(now)) {
				return task;
			} else {
				task.setStatus(MonitorRecordStatus.RUN);
			}
		}

		LogicBO logic = new LogicBO().setUserId(userId.toString()).setConnectBundle(new ArrayList<ConnectBundleBO>());

		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl),
				new DeviceGroupAvtplGearsPO().set(targetGear));

		// 呼叫设备
		ConnectBundleBO connectVideoBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
				.setOperateType(ConnectBundleBO.OPERATE_TYPE).setLock_type("write").setBundleId(task.getVideoBundleId())
				.setLayerId(task.getVideoLayerId()).setBundle_type(task.getVideoBundleType());
		ConnectBO connectVideoChannel = new ConnectBO().setChannelId(task.getVideoChannelId()).setChannel_status("Open")
				.setBase_type(task.getVideoBaseType()).setCodec_param(codec);
		connectVideoBundle.setChannels(new ArrayListWrapper<ConnectBO>().add(connectVideoChannel).getList());
		logic.getConnectBundle().add(connectVideoBundle);

		if (task.getAudioBundleId() != null) {
			if (task.getVideoBundleId().equals(task.getAudioBundleId())) {
				ConnectBO connectAudioChannel = new ConnectBO().setChannelId(task.getAudioChannelId())
						.setChannel_status("Open").setBase_type(task.getAudioBaseType()).setCodec_param(codec);
				connectVideoBundle.getChannels().add(connectAudioChannel);
			} else {
				ConnectBundleBO connectAudioBundle = new ConnectBundleBO()
						.setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD).setOperateType(ConnectBundleBO.OPERATE_TYPE)
						.setLock_type("write").setBundleId(task.getAudioBundleId()).setLayerId(task.getAudioLayerId())
						.setBundle_type(task.getAudioBundleType());
				ConnectBO connectAudioChannel = new ConnectBO().setChannelId(task.getAudioChannelId())
						.setChannel_status("Open").setBase_type(task.getAudioBaseType()).setCodec_param(codec);
				connectAudioBundle.setChannels(new ArrayListWrapper<ConnectBO>().add(connectAudioChannel).getList());
				logic.getConnectBundle().add(connectAudioBundle);
			}
		}

		// 录制命令
		RecordSetBO recordSet = new RecordSetBO().setGroupUuid(task.getUuid()).setUuid(task.getUuid()).setVideoType("2")
				.setVideoName(task.getFileName()).setUrl(urlIndex).setPlayUrl(previewUrl).setCodec_param(codec);
		RecordSourceBO videoSource = new RecordSourceBO().setType("channel").setBundle_id(task.getVideoBundleId())
				.setLayer_id(task.getVideoLayerId()).setChannel_id(task.getVideoChannelId());
		recordSet.setVideo_source(videoSource);
		if (task.getAudioBundleId() != null) {
			RecordSourceBO audioSource = new RecordSourceBO().setType("channel").setBundle_id(task.getAudioBundleId())
					.setLayer_id(task.getAudioLayerId()).setChannel_id(task.getAudioChannelId());
			recordSet.setAudio_source(audioSource);
		}
		logic.setRecordSet(new ArrayListWrapper<RecordSetBO>().add(recordSet).getList());

		ExecuteBusinessReturnBO response = executeBusiness.execute(logic, "点播系统，开始录制：");

		task.setStoreLayerId(response.getOutConnMediaMuxSet().get(0).getLayerId());
		monitorRecordDao.save(task);

		return task;
	}

	/**
	 * 停止录制任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月18日 下午2:31:01
	 * 
	 * @param Long
	 *            id 录制任务id
	 */
	@Deprecated
	public void stop(Long id) throws Exception {

		MonitorRecordPO task = monitorRecordDao.findOne(id);

		if (task != null) {
			task.setStatus(MonitorRecordStatus.STOP);
			if (task.getEndTime() == null) {
				task.setEndTime(new Date());
			}
			monitorRecordDao.save(task);

			LogicBO logic = new LogicBO().setUserId(task.getUserId().toString());

			RecordSetBO recordDel = new RecordSetBO().setUuid(task.getUuid());
			logic.setRecordDel(new ArrayListWrapper<RecordSetBO>().add(recordDel).getList());

			DisconnectBundleBO disconnectVideoBundle = new DisconnectBundleBO()
					.setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
					.setOperateType(DisconnectBundleBO.OPERATE_TYPE).setBundleId(task.getVideoBundleId())
					.setBundle_type(task.getVideoBundleType()).setLayerId(task.getVideoLayerId());
			logic.setDisconnectBundle(new ArrayListWrapper<DisconnectBundleBO>().add(disconnectVideoBundle).getList());

			if (task.getAudioBundleId() != null && !task.getVideoBundleId().equals(task.getAudioBundleId())) {
				DisconnectBundleBO disconnectAudioBundle = new DisconnectBundleBO()
						.setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
						.setOperateType(DisconnectBundleBO.OPERATE_TYPE).setBundleId(task.getAudioBundleId())
						.setBundle_type(task.getAudioBundleType()).setLayerId(task.getAudioLayerId());
				logic.setDisconnectBundle(
						new ArrayListWrapper<DisconnectBundleBO>().add(disconnectAudioBundle).getList());
			}

			executeBusiness.execute(logic, "点播系统，停止录制：");
		}

	}

	/**
	 * 定时录制任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月8日 下午8:20:13
	 */
	public void doScheduling() throws Exception {
		Date now = new Date();

		Map<String, LogicBO> logics = new HashMap<String, LogicBO>();

		// 早录一会
		List<MonitorRecordPO> needStartRecords = monitorRecordDao
				.findNeedStartSchedulingRecord(DateUtil.addMilliSecond(now, MonitorRecordPO.RECORD_OFFSET));
		if (needStartRecords != null && needStartRecords.size() > 0) {
			Set<Long> avtplIds = new HashSet<Long>();
			Set<Long> gearIds = new HashSet<Long>();
			for (MonitorRecordPO record : needStartRecords) {
				avtplIds.add(record.getAvTplId());
				gearIds.add(record.getGearId());
			}
			List<AvtplPO> avtpls = avtplDao.findAll(avtplIds);
			List<AvtplGearsPO> gears = avtplGearsDao.findAll(gearIds);
			for (MonitorRecordPO record : needStartRecords) {

				// logic的添加。注：开始录制设备的命令在创建任务时就已经下发，所以这里不下发；开始录制用户的命令目前还需要下发
				LogicBO targetLogic = logics.get(record.getUserId().toString());
				if (targetLogic == null) {
					targetLogic = new LogicBO().setUserId(record.getUserId().toString())
							.setConnectBundle(new ArrayList<ConnectBundleBO>())
							.setDisconnectBundle(new ArrayList<DisconnectBundleBO>())
							.setRecordSet(new ArrayList<RecordSetBO>())
							.setRecordDel(new ArrayList<RecordSetBO>());
					logics.put(record.getUserId().toString(), targetLogic);
				}

				// 参数模板
				AvtplPO targetAvtpl = null;
				for (AvtplPO avtpl : avtpls) {
					if (avtpl.getId().equals(record.getAvTplId())) {
						targetAvtpl = avtpl;
						break;
					}
				}
				AvtplGearsPO targetGear = null;
				for (AvtplGearsPO gear : gears) {
					if (gear.getId().equals(record.getGearId())) {
						targetGear = gear;
						break;
					}
				}
				CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl),
						new DeviceGroupAvtplGearsPO().set(targetGear));
				// 参数模板结束

				if (MonitorRecordType.LOCAL_DEVICE.equals(record.getType())) {
					/*targetLogic.merge(openBundle(record, codec));
					targetLogic.merge(startRecord(record, codec));*/
				} else if (MonitorRecordType.XT_DEVICE.equals(record.getType())) {
					/*BundlePO bundle = bundleDao.findByBundleId(record.getVideoBundleId());
					targetLogic.merge(startDevicePassby(record, codec, bundle));
					targetLogic.merge(startRecord(record, codec));*/
				} else if (MonitorRecordType.LOCAL_USER.equals(record.getType())) {
					targetLogic.merge(openBundle(record, codec));
					targetLogic.merge(startRecord(record, codec));
				} else if (MonitorRecordType.XT_USER.equals(record.getType())) {
					targetLogic.merge(startUserPassby(record, codec));
					targetLogic.merge(startRecord(record, codec));
				}
				// logic结束

				// 修改任务状态
				record.setStatus(MonitorRecordStatus.RUN);
			}
		}

		// 晚停一会
		List<MonitorRecordPO> needStopRecords = monitorRecordDao
				.findNeedStopSchedulingRecord(DateUtil.addMilliSecond(now, -MonitorRecordPO.RECORD_OFFSET));
		if (needStopRecords != null && needStopRecords.size() > 0) {
			for (MonitorRecordPO record : needStopRecords) {

				// logic协议
				LogicBO targetLogic = logics.get(record.getUserId().toString());
				if (targetLogic == null) {
					targetLogic = new LogicBO().setUserId(record.getUserId().toString())
							.setConnectBundle(new ArrayList<ConnectBundleBO>())
							.setDisconnectBundle(new ArrayList<DisconnectBundleBO>())
							.setRecordSet(new ArrayList<RecordSetBO>())
							.setRecordDel(new ArrayList<RecordSetBO>());
					logics.put(record.getUserId().toString(), targetLogic);
				}

				if (MonitorRecordType.LOCAL_DEVICE.equals(record.getType())) {
					targetLogic.merge(closeBundle(record));
					targetLogic.merge(stopRecord(record));
				} else if (MonitorRecordType.XT_DEVICE.equals(record.getType())) {
					BundlePO bundle = bundleDao.findByBundleId(record.getVideoBundleId().split("_")[0]);
					AvtplPO targetAvtpl = avtplDao.findOne(record.getAvTplId());
					AvtplGearsPO targetGear = avtplGearsDao.findOne(record.getGearId());
					CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl),
							new DeviceGroupAvtplGearsPO().set(targetGear));
					targetLogic.merge(stopRecord(record));
					targetLogic.merge(stopDevicePassby(record, codec, bundle));
				} else if (MonitorRecordType.LOCAL_USER.equals(record.getType())) {
					targetLogic.merge(closeBundle(record));
					targetLogic.merge(stopRecord(record));
				} else if (MonitorRecordType.XT_USER.equals(record.getType())) {
					AvtplPO targetAvtpl = avtplDao.findOne(record.getAvTplId());
					AvtplGearsPO targetGear = avtplGearsDao.findOne(record.getGearId());
					CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl),
							new DeviceGroupAvtplGearsPO().set(targetGear));
					targetLogic.merge(stopRecord(record));
					targetLogic.merge(stopUserPassby(record, codec));
				}
				// logic结束

				// 修改任务状态
				record.setStatus(MonitorRecordStatus.STOP);
			}
		}

		if (needStartRecords != null && needStartRecords.size() > 0) {
			monitorRecordDao.save(needStartRecords);
		}

		if (needStopRecords != null && needStopRecords.size() > 0) {
			monitorRecordDao.save(needStopRecords);
		}

		if (logics.size() > 0) {
			List<ResultDstBO> responses = new ArrayList<ExecuteBusinessReturnBO.ResultDstBO>();
			Collection<LogicBO> protocols = logics.values();
			for (LogicBO protocol : protocols) {
				ExecuteBusinessReturnBO response = executeBusiness.execute(protocol, "点播系统，排期任务线程：");
				if (response.getOutConnMediaMuxSet() != null && response.getOutConnMediaMuxSet().size() > 0) {
					responses.addAll(response.getOutConnMediaMuxSet());
				}
			}
			// 回写录制文件接入层信息
			if (needStartRecords != null && needStartRecords.size() > 0) {
				for (MonitorRecordPO record : needStartRecords) {
					for (ResultDstBO response : responses) {
						if (response.getUuid().equals(record.getUuid())) {
							record.setStoreLayerId(response.getLayerId());
							break;
						}
					}
				}
				monitorRecordDao.save(needStartRecords);
			}
		}
	}
	
	/**
	 * 执行排期录制中的待执行任务<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月17日 下午6:45:33
	 */
	public void scheduling() throws Exception{
		
		Date now = new Date();
		
		//找出需要停止录制的规则表   晚停一会。需要先停止否则会将结束时间更新。
		List<MonitorRecordManyTimesPO> stopRecords = monitorRecordManyTimesDao.findNeedStopRecord(DateUtil.addMilliSecond(now, -MonitorRecordPO.RECORD_OFFSET));
		
		for(MonitorRecordManyTimesPO record : stopRecords){
			record.setStatus(MonitorRecordStatus.STOP);
		}
		
		monitorRecordManyTimesDao.save(stopRecords);
		
		//找出需要录制但是错过时间没录制的：先将需要停止的做停止，再判断是否需要立即录制还是等待录制。
		List<MonitorRecordManyTimesRelationPO> missedRelations = monitorRecordManyTimesRelationDao.findMissedRelations(DateUtil.addMilliSecond(now, -MonitorRecordPO.RECORD_OFFSET));
		
		if(missedRelations.size()>0){
			
			List<Long> missedRecordIds = missedRelations.stream().map(MonitorRecordManyTimesRelationPO::getManyTimeId).filter(id -> id!=null).collect(Collectors.toList());
			List<MonitorRecordManyTimesPO> missedNeedToStopRecords = new ArrayList<MonitorRecordManyTimesPO>();
			if(missedRecordIds.size() > 0){
				missedNeedToStopRecords = monitorRecordManyTimesDao.findNeedStop(missedRecordIds);
			}
			if(missedNeedToStopRecords.size() > 0){
				for(MonitorRecordManyTimesPO missedNeedToStopRecord : missedNeedToStopRecords){
					missedNeedToStopRecord.setStatus(MonitorRecordStatus.STOP);
				}
			}
			
			for(MonitorRecordManyTimesRelationPO missedRelation :missedRelations){
				MonitorRecordManyTimesPO monitorRecordManyTimes = new MonitorRecordManyTimesPO();
				setStartAndEndTime(missedRelation.getMode().getName(), now, missedRelation, monitorRecordManyTimes);
			}
		}
		
		//找出需要录制的规则表 早录一会
		List<MonitorRecordManyTimesRelationPO> startRelations = monitorRecordManyTimesRelationDao.findNeedUpdateTime(DateUtil.addMilliSecond(now, MonitorRecordPO.RECORD_OFFSET));
		List<Long> monitorRecordStartIds=new ArrayList<Long>();
		
		for(MonitorRecordManyTimesRelationPO relation:startRelations){
			monitorRecordStartIds.add(relation.getBusinessId());
		}
		List<MonitorRecordPO> needStartRecords = monitorRecordDao.findByIdIn(monitorRecordStartIds);
		if (needStartRecords != null && needStartRecords.size() > 0) {
			for (MonitorRecordPO record : needStartRecords) {
				record.setStatus(MonitorRecordStatus.RUN);
			}
			
			//更新录制规则表中的下次开始停止时间,MonitorRecordManyTimesPO中添加一些数据
//			List<MonitorRecordManyTimesPO> monitorRecordStartManyTimes=new ArrayList<MonitorRecordManyTimesPO>();
			
			for(MonitorRecordManyTimesRelationPO relation:startRelations){
				MonitorRecordManyTimesPO monitorRecordManyTimesPO=new MonitorRecordManyTimesPO();
				monitorRecordManyTimesPO.setStartTime(relation.getNextStartTime())
										.setEndTime(relation.getNextEndTime())
										.setIndexNumber(relation.getIndexNumber().intValue())
										.setStatus(MonitorRecordStatus.RUN)
										.setRelationId(relation.getId());
//				monitorRecordStartManyTimes.add(monitorRecordManyTimesPO);
				monitorRecordManyTimesDao.save(monitorRecordManyTimesPO);
				relation.setManyTimeId(monitorRecordManyTimesPO.getId());
				monitorRecordManyTimesRelationService.updateNextTime(relation);
			}
			
//			monitorRecordManyTimesDao.save(monitorRecordStartManyTimes);
			//更新添加结束
		}
		
		if (needStartRecords != null && needStartRecords.size() > 0) {
			monitorRecordDao.save(needStartRecords);
		}

	}
	
	public void setStartAndEndTime(
			String storeMode,
			Date now,
			MonitorRecordManyTimesRelationPO relation,
			MonitorRecordManyTimesPO monitorRecordManyTimes 
			) throws Exception{
		
		String dayStart=relation.getDayStart();
		String dayEnd=relation.getDayEnd();
		String weekStart=relation.getWeekStart();
		String weekEnd=relation.getWeekEnd();
		String dayOfMonthStart=relation.getDayOfMonthStart();
		String dayOfMonthEnd=relation.getDayOfMonthEnd();
		Date nextStartTime;
		Date nextEndTime;
		
		boolean shouldRecord=false;
		Date shouldStart=new Date();
		Date shouldEnd=new Date();
		
		String todayDate=DateUtil.format(now,DateUtil.defaultDatePattern);
		
		if(MonitorRecordManyTimesMode.DAY.equals(MonitorRecordManyTimesMode.forName(storeMode))){
			
			//算出开始录制时间以及结束时间
			shouldStart=DateUtil.parse(todayDate+" "+dayStart);
			shouldEnd=DateUtil.parse(todayDate+" "+dayEnd);
			if(DateUtil.compare(shouldStart, shouldEnd)){
				shouldEnd=DateUtil.addDay(shouldEnd, 1);
			}
			
			//
			if(new Date().after(shouldStart) && new Date().before(shouldEnd)){
				shouldRecord=true;
				nextStartTime=DateUtil.addDay(shouldStart, 1);
				nextEndTime=DateUtil.addDay(shouldEnd, 1);
				monitorRecordManyTimes.setEndTime(shouldEnd).setStartTime(new Date());
			}else if(new Date().after(shouldStart) && new Date().after(shouldEnd)){
				nextStartTime=DateUtil.addDay(shouldStart, 1);
				nextEndTime=DateUtil.addDay(shouldEnd, 1);
			}else{
				nextStartTime=shouldStart;
				nextEndTime=shouldEnd;
			}
			
			relation.setNextStartTime(nextStartTime);
			relation.setNextEndTime(nextEndTime);
			
		}else if(MonitorRecordManyTimesMode.WEEK.equals(MonitorRecordManyTimesMode.forName(storeMode))){
			
			//算出本周打开始录制时间
			String weekStartDate = getDayOfWeek(Integer.valueOf(weekStart));
			String weekEndDate=getDayOfWeek(Integer.valueOf(weekEnd));
			 shouldStart=DateUtil.parse(weekStartDate+" "+dayStart);
			 shouldEnd=DateUtil.parse(weekEndDate+" "+dayEnd);
			if(shouldStart.after(shouldEnd)){
				shouldEnd=DateUtil.addDay(shouldEnd, 7);
			}
			
			if(new Date().after(shouldStart) && new Date().before(shouldEnd)){
				shouldRecord=true;
				nextStartTime=DateUtil.addDay(shouldStart, 7);
				nextEndTime=DateUtil.addDay(shouldEnd, 7);
				monitorRecordManyTimes.setEndTime(shouldEnd).setStartTime(new Date());
			}else if(new Date().after(shouldStart) && new Date().after(shouldEnd)){
				nextStartTime=DateUtil.addDay(shouldStart, 7);
				nextEndTime=DateUtil.addDay(shouldEnd, 7);
			}else{
				nextStartTime=shouldStart;
				nextEndTime=shouldEnd;
			}
			
			relation.setNextStartTime(nextStartTime);
			relation.setNextEndTime(nextEndTime);
			
		}else if(MonitorRecordManyTimesMode.MONTH.equals(MonitorRecordManyTimesMode.forName(storeMode))){
			
			//算出本月的开始录制时间
			shouldStart=getDayOfMonth(Integer.valueOf(dayOfMonthStart),dayStart);
			shouldEnd=getDayOfMonth(Integer.valueOf(dayOfMonthEnd),dayEnd);
			if(shouldStart.after(shouldEnd)){
				shouldEnd=DateUtil.addMonth(shouldEnd, 1);
			}
			
			if(new Date().after(shouldStart)&&new Date().before(shouldEnd)){
				shouldRecord=true;
				nextStartTime=DateUtil.addMonth(shouldStart, 1);
				nextEndTime=DateUtil.addMonth(shouldEnd, 1);
				monitorRecordManyTimes.setEndTime(shouldEnd).setStartTime(new Date());
			}else if(new Date().after(shouldStart) && new Date().after(shouldEnd)){
				nextStartTime=DateUtil.addMonth(shouldStart, 1);
				nextEndTime=DateUtil.addMonth(shouldEnd, 1);
			}else{
				nextStartTime=shouldStart;
				nextEndTime=shouldEnd;
			}
			
			relation.setNextStartTime(nextStartTime);
			relation.setNextEndTime(nextEndTime);
		}
		
		if(shouldRecord){
			monitorRecordManyTimes.setStatus(MonitorRecordStatus.RUN)
			  .setRelationId(relation.getId())
			  .setIndexNumber(relation.getIndexNumber().intValue());
			monitorRecordManyTimesDao.save(monitorRecordManyTimes);

			relation.setManyTimeId(monitorRecordManyTimes.getId()).setIndexNumber(relation.getIndexNumber()+1);
			monitorRecordManyTimesRelationDao.save(relation);
		}
	}

	/**
	 * 删除文件<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月25日 上午10:09:38
	 * 
	 * @param Long
	 *            id 文件id
	 * @param Long
	 *            userId 当前业务用户
	 * @throws Exception
	 *//*
	public void removeFile(Long id, Long userId) throws Exception {
		MonitorRecordPO file = monitorRecordDao.findOne(id);
		if (file.getUserId().toString().equals(userId.toString())
				&& MonitorRecordStatus.STOP.equals(file.getStatus())) {

			// 发送删除文件命令
			LogicBO logic = new LogicBO().setUserId("-1").setPass_by(new ArrayList<PassByBO>());
			List<String> files = new ArrayList<String>();
			String fileName = null;
			if (file.getPreviewUrl() != null) {
				fileName = file.getPreviewUrl().split("/")[0];
			}
			if (fileName != null && !fileName.equals("")) {
				files.add(fileName);
				PassByBO passByBO = new PassByBO().setBundle_id("").setLayer_id(file.getStoreLayerId())
						.setType("delete_record_file")
						.setPass_by_content(new HashMapWrapper<String, Object>().put("files", files).getMap());
				logic.getPass_by().add(passByBO);
				executeBusiness.execute(logic, "点播系统：删除录制及文件：" + file.getFileName());
			}

			monitorRecordDao.delete(file);
		}
	}*/
	
	/**
	 * 删除文件<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月25日 上午10:09:38
	 * 
	 * @param Long
	 *            id 文件id
	 * @param Long
	 *            userId 当前业务用户
	 * @throws Exception
	 */
	public void removeFile(Long id, Long userId) throws Exception {
		MonitorRecordPO file = monitorRecordDao.findOne(id);
		
		if (file.getUserId().toString().equals(userId.toString())
				&& MonitorRecordStatus.STOP.equals(file.getStatus())) {

			// 发送删除文件命令
			LogicBO logic = new LogicBO().setUserId("-1").setPass_by(new ArrayList<PassByBO>());
			List<String> files = new ArrayList<String>();
			String fileName = null;
			if (file.getPreviewUrl() != null) {
				fileName = file.getPreviewUrl().split("/")[0];
			}
			if (fileName != null && !fileName.equals("")) {
				files.add(fileName);
				PassByBO passByBO = new PassByBO().setBundle_id("").setLayer_id(file.getStoreLayerId())
						.setType("delete_record_file")
						.setPass_by_content(new HashMapWrapper<String, Object>().put("files", files).getMap());
				logic.getPass_by().add(passByBO);
				executeBusiness.execute(logic, "点播系统：删除录制及文件：" + file.getFileName());
				
				operationLogService.send(userUtils.queryUserById(userId).getName(), "删除录制", "删除录制任务文件名称："+file.getFileName());
			}

			monitorRecordDao.delete(file);
		}
	}

	/**
	 * 删除文件，兼容老的删除<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月29日 上午9:49:02
	 * @param id id 文件id
	 * @param timeSegmentId 排期任务的子id
	 * @param userId userId 当前业务用户
	 * @throws Exception
	 */
	public void removeFileById(Long id,Long timeSegmentId, Long userId) throws Exception {
		
		MonitorRecordPO file = monitorRecordDao.findOne(id);
		
		UserVO user = userQuery.current();
		
		if (userId.longValue() == 1 || user.getIsGroupCreator()) {
			userId = file.getUserId();
		}
		
		if(timeSegmentId!=null){
			if (file.getUserId().toString().equals(userId.toString())){
				
				MonitorRecordManyTimesPO record=monitorRecordManyTimesDao.findOne(timeSegmentId);
				
				if(!MonitorRecordStatus.STOP.equals(record.getStatus())){
					throw new BaseException(StatusCode.FORBIDDEN,"录制任务还未结束");
				}
				
				LogicBO logic = new LogicBO().setUserId("-1").setPass_by(new ArrayList<PassByBO>());
				List<String> files = new ArrayList<String>();
				String fileName = null;
				
				if (file.getPreviewUrl() != null&& record.getIndexNumber() != null ) {
					fileName = file.getPreviewUrl().split("/")[0]+record.getIndexNumber();
				}
				
				if (fileName != null && !fileName.equals("")) {
					
					PassByBO passByBO = new PassByBO().setBundle_id("").setLayer_id(file.getStoreLayerId())
							.setType("delete_record_file")
							.setPass_by_content(new HashMapWrapper<String, Object>().put("files", files).getMap());
					logic.getPass_by().add(passByBO);
					
					executeBusiness.execute(logic, "点播系统：删除录制及文件：" + file.getFileName());
					
					operationLogService.send(userUtils.queryUserById(userId).getName(), "删除录制", "删除排期录制任务id："+record.getId());
				}
				
				monitorRecordManyTimesDao.delete(record);
				
			}else if(!file.getUserId().toString().equals(userId.toString())){
				throw new BaseException(StatusCode.FORBIDDEN,"没有权限删除");
			}
		}else{
			if (file.getUserId().toString().equals(userId.toString())
					&& (MonitorRecordStatus.STOP.equals(file.getStatus()))) {

				// 发送删除文件命令
				LogicBO logic = new LogicBO().setUserId("-1").setPass_by(new ArrayList<PassByBO>());
				List<String> files = new ArrayList<String>();
				String fileName = null;
				
				if (file.getPreviewUrl() != null) {
					fileName = file.getPreviewUrl().split("/")[0];
				}
				
				if (fileName != null && !fileName.equals("")) {
					if(MonitorRecordMode.TIMESEGMENT.equals(file.getMode())){
						Optional<List<MonitorRecordManyTimesPO>> records=Optional.ofNullable(monitorRecordManyTimesRelationDao.findByBusinessId(file.getId())).map(relation->{
							monitorRecordManyTimesRelationDao.delete(relation);
							return relation.getId();
						}).map(relationId->{
							return monitorRecordManyTimesDao.findByRelationId(relationId);
						});
						
						if(records.isPresent()){
							List<Long> ids=new ArrayList<Long>();
							
							for(MonitorRecordManyTimesPO record:records.get()){
//								files.add(fileName+"/"+record.getIndexNumber());
								ids.add(record.getId());
							}
							
							monitorRecordManyTimesDao.deleteByIdIn(ids);
						}
						files.add(fileName);//删除整个排期的文件，对整个目录添加一次即可
					}else{
						files.add(fileName);
					}
					
					PassByBO passByBO = new PassByBO().setBundle_id("").setLayer_id(file.getStoreLayerId())
							.setType("delete_record_file")
							.setPass_by_content(new HashMapWrapper<String, Object>().put("files", files).getMap());
					logic.getPass_by().add(passByBO);
					
					executeBusiness.execute(logic, "点播系统：删除录制及文件：" + file.getFileName());
					
					operationLogService.send(userUtils.queryUserById(userId).getName(), "删除录制", "删除录制任务文件名称："+file.getFileName());

				}
				
				monitorRecordDao.delete(file);
					
			}else if(!MonitorRecordStatus.STOP.equals(file.getStatus())){
				throw new BaseException(StatusCode.FORBIDDEN,"录制任务还未结束");
			}
			else if(!file.getUserId().toString().equals(userId.toString())){
				throw new BaseException(StatusCode.FORBIDDEN,"没有权限删除");
			}
		}
		
	}

	/**
	 * 设置全局磁盘大小<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月22日 下午5:13:02
	 * @param totalSizeMb 磁盘大小
	 */
	public void setTotalSize(Integer totalSizeMb){
		
		SystemConfigurationPO congfiguration = systemConfigurationDao.findByTotalSizeMbNotNull();
		if(congfiguration==null){
			systemConfigurationDao.save(new SystemConfigurationPO().setTotalSizeMb(totalSizeMb));
			return;
		}
		congfiguration.setTotalSizeMb(totalSizeMb);
		systemConfigurationDao.save(congfiguration);
		
	}
	
	/**
	 * 返回排期录制对应关系的VO<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月28日 下午3:41:31
	 * @param business monitorrecord的id
	 * @return
	 * @throws Exception
	 */
	public MonitorRecordManyTimesRelationVO getRecordRelation(Long business) throws Exception{
		MonitorRecordManyTimesRelationPO relationPo = monitorRecordManyTimesRelationDao.findByBusinessId(business);
		if(relationPo==null){
			return null;
		}
		MonitorRecordManyTimesRelationVO relationVo = new MonitorRecordManyTimesRelationVO().set(relationPo);
		return relationVo;
	}
	
	/** 把联网的passby存储或从资源层删除 */
	public void saveOrRemoveLianwangPassbyToResource(LogicBO logic){
		List<PassByBO> passbys = logic.getPass_by();
		if(passbys == null) return;
		for(PassByBO passby : passbys){
			String type = passby.getType();
			if(XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_ENCODER.equals(type)
					|| XtBusinessPassByContentBO.CMD_XT_SEE_LOCAL_ENCODER.equals(type)
					|| XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_USER.equals(type)
					|| XtBusinessPassByContentBO.CMD_XT_SEE_LOCAL_USER.equals(type)
					|| XtBusinessPassByContentBO.CMD_LOCAL_CALL_XT_USER.equals(type)
					|| XtBusinessPassByContentBO.CMD_XT_CALL_LOCAL_USER.equals(type)){
				try{
					String uuid = ((XtBusinessPassByContentBO)passby.getPass_by_content()).getUuid();
					String operate = ((XtBusinessPassByContentBO)passby.getPass_by_content()).getOperate();
					if(XtBusinessPassByContentBO.OPERATE_START.equals(operate)){
						resourceServiceClient.coverLianwangPassby(
								uuid, 
								passby.getLayer_id(), 
								type, 
								JSON.toJSONString(passby));
					}else if(XtBusinessPassByContentBO.OPERATE_STOP.equals(operate)){
						resourceServiceClient.removeLianwangPassby(uuid);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}

}

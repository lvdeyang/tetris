package com.sumavision.bvc.device.monitor.live.device;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.constant.BusinessConstants.BUSINESS_OPR_TYPE;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.ExtraInfoPO;
import com.suma.venus.resource.pojo.VedioCapacityPO;
import com.suma.venus.resource.service.ExtraInfoService;
import com.suma.venus.resource.service.ResourceService;
import com.suma.venus.resource.service.VedioCapacityService;
import com.sumavision.bvc.command.system.service.CommandSystemQueryService;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.ConnectBO;
import com.sumavision.bvc.device.group.bo.ConnectBundleBO;
import com.sumavision.bvc.device.group.bo.DisconnectBundleBO;
import com.sumavision.bvc.device.group.bo.ForwardSetSrcBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.PassByBO;
import com.sumavision.bvc.device.group.bo.VideoParamBO;
import com.sumavision.bvc.device.group.bo.XtBusinessPassByContentBO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplPO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.ResourceQueryUtil;
import com.sumavision.bvc.device.monitor.exception.UserHasNoPermissionToRemoveLiveDeviceException;
import com.sumavision.bvc.device.monitor.live.DstDeviceType;
import com.sumavision.bvc.device.monitor.live.LiveType;
import com.sumavision.bvc.device.monitor.live.MonitorLiveCommons;
import com.sumavision.bvc.device.monitor.live.exception.MonitorLiveDstVideoBundleCannotBeNullException;
import com.sumavision.bvc.device.monitor.live.exception.MonitorLiveNotExistException;
import com.sumavision.bvc.device.monitor.live.exception.MonitorLiveSourceVideoBundleCannotBeNullException;
import com.sumavision.bvc.device.monitor.live.exception.UserHasNoPermissionForBusinessException;
import com.sumavision.bvc.device.monitor.live.user.MonitorLiveUserDAO;
import com.sumavision.bvc.device.monitor.live.user.MonitorLiveUserPO;
import com.sumavision.bvc.device.monitor.live.user.MonitorLiveUserService;
import com.sumavision.bvc.device.monitor.osd.MonitorOsdDAO;
import com.sumavision.bvc.device.monitor.osd.MonitorOsdPO;
import com.sumavision.bvc.device.monitor.osd.MonitorOsdService;
import com.sumavision.bvc.device.monitor.osd.exception.MonitorOsdNotExistException;
import com.sumavision.bvc.device.monitor.playback.MonitorRecordPlaybackTaskDAO;
import com.sumavision.bvc.device.monitor.playback.MonitorRecordPlaybackTaskPO;
import com.sumavision.bvc.device.monitor.playback.MonitorRecordPlaybackTaskService;
import com.sumavision.bvc.device.monitor.record.MonitorRecordStatus;
import com.sumavision.bvc.feign.ResourceServiceClient;
import com.sumavision.bvc.log.OperationLogService;
import com.sumavision.bvc.system.dao.AVtplGearsDAO;
import com.sumavision.bvc.system.dao.AvtplDAO;
import com.sumavision.bvc.system.po.AvtplGearsPO;
import com.sumavision.bvc.system.po.AvtplPO;
import com.sumavision.tetris.auth.token.TerminalType;
import com.sumavision.tetris.bvc.business.common.MulticastService;
import com.sumavision.tetris.bvc.business.group.TransmissionMode;
import com.sumavision.tetris.bvc.business.location.LocationExecuteStatus;
import com.sumavision.tetris.bvc.business.location.LocationOfScreenWallDAO;
import com.sumavision.tetris.bvc.business.location.LocationOfScreenWallPO;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

/**
 * 点播设备业务<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年6月19日 下午3:32:02
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MonitorLiveDeviceService {
	
	private String lockMonitorLiveDevice = "monitor-live-device";
	
	private boolean isLock = true;
	
	@Autowired
	private MonitorLiveDeviceDAO monitorLiveDeviceDao;
	
	@Autowired
	private MonitorLiveUserDAO monitorLiveUserDao;
	
	@Autowired
	private MonitorLiveUserService monitorLiveUserService;
	
	@Autowired
	private MonitorRecordPlaybackTaskDAO monitorRecordPlaybackTaskDao;
	
	@Autowired
	private MonitorRecordPlaybackTaskService monitorRecordPlaybackTaskService;
	
	@Autowired
	private MonitorOsdDAO monitorOsdDao;
	
	@Autowired
	private MonitorOsdService monitorOsdService;
	
	@Autowired
	private ExtraInfoService extraInfoService;
	
	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private AvtplDAO avtplDao;
	
	@Autowired
	private AVtplGearsDAO avtplGearsDao;
	
	@Autowired
	private MonitorLiveCommons commons;
	
	@Autowired
	private MulticastService multicastService;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	@Autowired
	private ResourceServiceClient resourceServiceClient;
	
	@Autowired
	private OperationLogService operationLogService;
	
	@Autowired
	private ResourceQueryUtil resourceQueryUtil;
	
	@Autowired
	private LocationOfScreenWallDAO locationOfScreenWallDao;
	
	@Autowired
	private VedioCapacityService vedioCapacityService;
	
	@Autowired
	private CommandSystemQueryService commandSystemQueryService;
	/**
	 * xt看本地设备<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 下午2:24:05
	 * @param Long osdId 字幕id
	 * @param String uuid 既定uuid
	 * @param String videoBundleId 视频源设备id
	 * @param String videoBundleName 视频源设备名称
	 * @parma String videoBundleType 视频源设备类型
	 * @param String videoLayerId 视频源设备接入层
	 * @param String videoChannelId 视频源通道id
	 * @param String videoBaseType 视频源通道类型
	 * @param String audioBundleId 音频源设备id
	 * @param String audioBundleName 音频源设备名称
	 * @param String audioBundleType 音频源设备类型
	 * @param String audioLayerId 音频源设备接入层id
	 * @param String audioChannelId 音频源通道id
	 * @param String audioBaseType 音频源通道类型
	 * @param Long userId 发起业务用户id
	 * @param String userno 发起业务用户号码
	 * @return MonitorLiveDevicePO 点播任务
	 */
	public MonitorLiveDevicePO startXtSeeLocal(
			Long osdId,
			String uuid,
			String videoBundleId,
			String videoBundleName,
			String videoBundleType,
			String videoLayerId,
			String videoChannelId,
			String videoBaseType,
			String audioBundleId,
			String audioBundleName,
			String audioBundleType,
			String audioLayerId,
			String audioChannelId,
			String audioBaseType,
			Long userId,
			String userno) throws Exception{
		
		if(isLock){
			synchronized (lockMonitorLiveDevice.intern()) {
				List<VedioCapacityPO> videoCapacityList = vedioCapacityService.findAll();
				if(videoCapacityList == null || videoCapacityList.size()==0 || videoCapacityList.get(0).getTurnCapacity() == null){
					throw new BaseException(StatusCode.FORBIDDEN, "没有查到转发容量");
				}
				
				Long TurnCapacity = videoCapacityList.get(0).getTurnCapacity();
				Long forwardCount = commandSystemQueryService.queryCountOfTransmit();
				if(TurnCapacity <= forwardCount){
					throw new BaseException(StatusCode.FORBIDDEN, "转发路数已经达到上限");
				}
				
				//参数校验
				isVideoBundleNotNull(videoBundleId, "internal");
				regularizedAudioParams(audioBundleId, audioChannelId, "internal", "internal");
//				authorize(videoBundleId, audioBundleId, userId);//TODO: 暂时注释
				
				//本地编码器
				BundlePO localEncoder = bundleDao.findByBundleId(videoBundleId);
				
				//参数模板
				Map<String, Object> result = commons.queryDefaultAvCodec();
				AvtplPO targetAvtpl = (AvtplPO)result.get("avtpl");
				AvtplGearsPO targetGear = (AvtplGearsPO)result.get("gear");
				CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
				codec = audioChannelId == null?codec.setAudio_param(null):codec;
				
				//获取联网id
				String networkLayerId = commons.queryNetworkLayerId();
				
				//字幕
				MonitorOsdPO osd = monitorOsdDao.findOne(osdId==null?0:osdId);
				
				MonitorLiveDevicePO live = new MonitorLiveDevicePO(
						videoBundleId, videoBundleName, videoBundleType, videoLayerId, videoChannelId, videoBaseType,
						audioBundleId, audioBundleName, audioBundleType, audioLayerId, audioChannelId, audioBaseType,
						null, null, null, null, null, null,
						null, null, null, null, null, null,
						userId,
						targetAvtpl.getId(),
						targetGear.getId(),
						null,
						LiveType.XT_LOCAL,
						(osd==null?null:osd.getId()),
						(osd==null?null:osd.getUsername()));
				
				if(uuid != null) live.setUuid(uuid);
				
				monitorLiveDeviceDao.save(live);
				
				LogicBO logic = openBundle(live, codec, osd, localEncoder, null, userId);
				
				logic.setPass_by(new ArrayList<PassByBO>());
				
				XtBusinessPassByContentBO passByContent = new XtBusinessPassByContentBO().setCmd(XtBusinessPassByContentBO.CMD_XT_SEE_LOCAL_ENCODER)
																						 .setOperate(XtBusinessPassByContentBO.OPERATE_START)
																						 .setUuid(live.getUuid())
																						 .setSrc_user(userno)
																						 .setLocal_encoder(new HashMapWrapper<String, String>().put("layerid", videoLayerId)
																								 											   .put("bundleid", videoBundleId)	
																								 											   .put("video_channelid", videoChannelId)
																								 											   .put("audio_channelid", audioChannelId)
																								 											   .getMap())
																						 .setDst_number(localEncoder.getUsername())
																						 .setVparam(codec);
				
				PassByBO passby = new PassByBO().setLayer_id(networkLayerId)
												.setType(XtBusinessPassByContentBO.CMD_XT_SEE_LOCAL_ENCODER)
												.setPass_by_content(passByContent);
				
				logic.getPass_by().add(passby);
				
				resourceServiceClient.coverLianwangPassby(
						live.getUuid(), 
						networkLayerId, 
						XtBusinessPassByContentBO.CMD_XT_SEE_LOCAL_ENCODER, 
						JSON.toJSONString(passby));
				
				executeBusiness.execute(logic, "点播系统：xt点播本地设备 " + videoBundleName);
				
				return live;
			}
		}else{
			//参数校验
			isVideoBundleNotNull(videoBundleId, "internal");
			regularizedAudioParams(audioBundleId, audioChannelId, "internal", "internal");
//			authorize(videoBundleId, audioBundleId, userId);//TODO: 暂时注释
			
			//本地编码器
			BundlePO localEncoder = bundleDao.findByBundleId(videoBundleId);
			
			//参数模板
			Map<String, Object> result = commons.queryDefaultAvCodec();
			AvtplPO targetAvtpl = (AvtplPO)result.get("avtpl");
			AvtplGearsPO targetGear = (AvtplGearsPO)result.get("gear");
			CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
			codec = audioChannelId == null?codec.setAudio_param(null):codec;
			
			//获取联网id
			String networkLayerId = commons.queryNetworkLayerId();
			
			//字幕
			MonitorOsdPO osd = monitorOsdDao.findOne(osdId==null?0:osdId);
			
			MonitorLiveDevicePO live = new MonitorLiveDevicePO(
					videoBundleId, videoBundleName, videoBundleType, videoLayerId, videoChannelId, videoBaseType,
					audioBundleId, audioBundleName, audioBundleType, audioLayerId, audioChannelId, audioBaseType,
					null, null, null, null, null, null,
					null, null, null, null, null, null,
					userId,
					targetAvtpl.getId(),
					targetGear.getId(),
					null,
					LiveType.XT_LOCAL,
					(osd==null?null:osd.getId()),
					(osd==null?null:osd.getUsername()));
			
			if(uuid != null) live.setUuid(uuid);
			
			monitorLiveDeviceDao.save(live);
			
			LogicBO logic = openBundle(live, codec, osd, localEncoder, null, userId);
			
			logic.setPass_by(new ArrayList<PassByBO>());
			
			XtBusinessPassByContentBO passByContent = new XtBusinessPassByContentBO().setCmd(XtBusinessPassByContentBO.CMD_XT_SEE_LOCAL_ENCODER)
																					 .setOperate(XtBusinessPassByContentBO.OPERATE_START)
																					 .setUuid(live.getUuid())
																					 .setSrc_user(userno)
																					 .setLocal_encoder(new HashMapWrapper<String, String>().put("layerid", videoLayerId)
																							 											   .put("bundleid", videoBundleId)	
																							 											   .put("video_channelid", videoChannelId)
																							 											   .put("audio_channelid", audioChannelId)
																							 											   .getMap())
																					 .setDst_number(localEncoder.getUsername())
																					 .setVparam(codec);
			
			PassByBO passby = new PassByBO().setLayer_id(networkLayerId)
											.setType(XtBusinessPassByContentBO.CMD_XT_SEE_LOCAL_ENCODER)
											.setPass_by_content(passByContent);
			
			logic.getPass_by().add(passby);
			
			resourceServiceClient.coverLianwangPassby(
					live.getUuid(), 
					networkLayerId, 
					XtBusinessPassByContentBO.CMD_XT_SEE_LOCAL_ENCODER, 
					JSON.toJSONString(passby));
			
			executeBusiness.execute(logic, "点播系统：xt点播本地设备 " + videoBundleName);
			
			return live;
		}
		
		
	}
	
	/**
	 * 本地设备点播本地设备<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 下午1:17:41
	 * @param Long osdId 字幕id
	 * @param String videoBundleId 视频源设备id
	 * @param String videoBundleName 视频源设备名称
	 * @parma String videoBundleType 视频源设备类型
	 * @param String videoLayerId 视频源设备接入层
	 * @param String videoChannelId 视频源通道id
	 * @param String videoBaseType 视频源通道类型
	 * @param String audioBundleId 音频源设备id
	 * @param String audioBundleName 音频源设备名称
	 * @param String audioBundleType 音频源设备类型
	 * @param String audioLayerId 音频源设备接入层id
	 * @param String audioChannelId 音频源通道id
	 * @param String audioBaseType 音频源通道类型
	 * @param String dstVideoBundleId 目标视频设备id
	 * @param String dstVideoBundleName 目标视频设备名称
	 * @param String dstVideoBundleType 目标视频设备类型
	 * @param String dstVideoLayerId 目标视频设备接入层
	 * @param String dstVideoChannelId 目标视频设备通道id
	 * @param String dstVideoBaseType 目标视频设备通道类型
	 * @param String dstAudioBundleId 目标音频设备id
	 * @param String dstAudioBundleName 目标音频设备名称
	 * @param String dstAudioBundleType 目标音频设备类型
	 * @param String dstAudioLayerId 目标音频设备接入层
	 * @param String dstAudioChannelId 目标音频设备通道id
	 * @param String dstAudioBaseType 目标音频设备通道类型
	 * @param String dstDeviceType 业务类型 WEBSITE_PLAYER, DEVICE
	 * @param Long userId 操作业务用户
	 * @param String userno 用户号码
	 * @param boolean transcord 是否进行转码
	 * @param String udpUrl 转码时的udp播放地址
	 * @return MonitorLiveDevicePO 点播任务
	 */
	@Transactional(rollbackFor = Exception.class)
	public MonitorLiveDevicePO startLocalSeeLocal(
			Long osdId,
			String videoBundleId,
			String videoBundleName,
			String videoBundleType,
			String videoLayerId,
			String videoChannelId,
			String videoBaseType,
			String audioBundleId,
			String audioBundleName,
			String audioBundleType,
			String audioLayerId,
			String audioChannelId,
			String audioBaseType,
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
			String dstDeviceType,
			Long userId,
			String userno,
			boolean transcord,
			String udpUrl) throws Exception{
		
		if(isLock){
			synchronized (lockMonitorLiveDevice.intern()) {
				List<VedioCapacityPO> videoCapacityList = vedioCapacityService.findAll();
				if(videoCapacityList == null || videoCapacityList.size()==0 || videoCapacityList.get(0).getTurnCapacity() == null){
					throw new BaseException(StatusCode.FORBIDDEN, "没有查到转发容量");
				}
				
				Long TurnCapacity = videoCapacityList.get(0).getTurnCapacity();
				Long forwardCount = commandSystemQueryService.queryCountOfTransmit();
				if(TurnCapacity <= forwardCount){
					throw new BaseException(StatusCode.FORBIDDEN, "转发路数已经达到上限");
				}
		
				//参数校验
				isVideoBundleNotNull(videoBundleId, dstVideoBundleId);
				regularizedAudioParams(audioBundleId, audioChannelId, dstAudioBundleId, dstAudioChannelId);
				isSrcAndDstBeTheSame(videoBundleId, dstVideoBundleId, audioBundleId, dstAudioBundleId);
				authorize(videoBundleId, audioBundleId, userId);
				
				//参数模板
				Map<String, Object> result = commons.queryDefaultAvCodec();
				AvtplPO targetAvtpl = (AvtplPO)result.get("avtpl");
				AvtplGearsPO targetGear = (AvtplGearsPO)result.get("gear");
				CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
				
				//用于播放器的参数模板
				CodecParamBO playerCodec;
				if(transcord){
					Map<String, Object> resultForPlayer = commons.queryDefaultPlayerAvCodec();
					AvtplPO targetPlayerAvtpl = (AvtplPO)resultForPlayer.get("avtpl");
					AvtplGearsPO targetPlayerGear = (AvtplGearsPO)resultForPlayer.get("gear");
					playerCodec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetPlayerAvtpl), new DeviceGroupAvtplGearsPO().set(targetPlayerGear));			
				}else{
					playerCodec = codec;
				}
						
				//字幕
				MonitorOsdPO osd = monitorOsdDao.findOne(osdId==null?0:osdId);
				
				//处理业务覆盖
				coverBusiness(dstVideoBundleId, dstVideoChannelId, dstAudioBundleId, dstAudioChannelId, userId, userno);
				
				//视频源和目的
				BundlePO videoBundle = bundleDao.findByBundleId(videoBundleId);
				BundlePO dstVideoBundle = bundleDao.findByBundleId(dstVideoBundleId);
				
			//		//先做停止操作转发并删除操作(处理逻辑放在coverBusiness()方法中)
			//		MonitorLiveDevicePO oldLIve = monitorLiveDeviceDao.findByDstVideoBundleId(dstVideoBundleId);
			//		if(oldLIve != null){
			//			if(MonitorRecordStatus.RUN.equals(oldLIve.getStatus())){
			//				stop(oldLIve.getId(), userId, userno, null);
			//			}else{
			//				stop(oldLIve.getId(), userId, userno, Boolean.FALSE);
			//			}
			//		}
				
				MonitorLiveDevicePO live = new MonitorLiveDevicePO(
						videoBundleId, videoBundleName, videoBundleType, videoLayerId, videoChannelId, videoBaseType,
						audioBundleId, audioBundleName, audioBundleType, audioLayerId, audioChannelId, audioBaseType,
						dstVideoBundleId, dstVideoBundleName, dstVideoBundleType, dstVideoLayerId, dstVideoChannelId, dstVideoBaseType,
						dstAudioBundleId, dstAudioBundleName, dstAudioBundleType, dstAudioLayerId, dstAudioChannelId, dstAudioBaseType,
						userId,
						targetAvtpl.getId(),
						targetGear.getId(),
						DstDeviceType.valueOf(dstDeviceType),
						LiveType.LOCAL_LOCAL,
						(osd==null?null:osd.getId()),
						(osd==null?null:osd.getUsername()));
				
				//新加的参数，就不改MonitorLiveDevicePO(xxx)方法了
				live.setUdpUrl(udpUrl);
				live.setVideoDeviceModel(videoBundle.getDeviceModel());
				live.setAudioDeviceModel(videoBundle.getDeviceModel());
				live.setDstVideoDeviceModel(dstVideoBundle.getDeviceModel());
				live.setDstAudioDeviceModel(dstVideoBundle.getDeviceModel());
				
				monitorLiveDeviceDao.save(live);
				
				
			//		LogicBO logic = openBundle(live, codec, playerCodec, osd, videoBundle, dstVideoBundle, userId, transcord, udpUrl);
				
			//		executeBusiness.execute(logic, "点播系统：本地设备点播本地设备");
				
				return live;
			}
		}else{
			//参数校验
			isVideoBundleNotNull(videoBundleId, dstVideoBundleId);
			regularizedAudioParams(audioBundleId, audioChannelId, dstAudioBundleId, dstAudioChannelId);
			isSrcAndDstBeTheSame(videoBundleId, dstVideoBundleId, audioBundleId, dstAudioBundleId);
			authorize(videoBundleId, audioBundleId, userId);
			
			//参数模板
			Map<String, Object> result = commons.queryDefaultAvCodec();
			AvtplPO targetAvtpl = (AvtplPO)result.get("avtpl");
			AvtplGearsPO targetGear = (AvtplGearsPO)result.get("gear");
			CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
			
			//用于播放器的参数模板
			CodecParamBO playerCodec;
			if(transcord){
				Map<String, Object> resultForPlayer = commons.queryDefaultPlayerAvCodec();
				AvtplPO targetPlayerAvtpl = (AvtplPO)resultForPlayer.get("avtpl");
				AvtplGearsPO targetPlayerGear = (AvtplGearsPO)resultForPlayer.get("gear");
				playerCodec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetPlayerAvtpl), new DeviceGroupAvtplGearsPO().set(targetPlayerGear));			
			}else{
				playerCodec = codec;
			}
					
			//字幕
			MonitorOsdPO osd = monitorOsdDao.findOne(osdId==null?0:osdId);
			
			//处理业务覆盖
			coverBusiness(dstVideoBundleId, dstVideoChannelId, dstAudioBundleId, dstAudioChannelId, userId, userno);
			
			//视频源和目的
			BundlePO videoBundle = bundleDao.findByBundleId(videoBundleId);
			BundlePO dstVideoBundle = bundleDao.findByBundleId(dstVideoBundleId);
			
		//		//先做停止操作转发并删除操作(处理逻辑放在coverBusiness()方法中)
		//		MonitorLiveDevicePO oldLIve = monitorLiveDeviceDao.findByDstVideoBundleId(dstVideoBundleId);
		//		if(oldLIve != null){
		//			if(MonitorRecordStatus.RUN.equals(oldLIve.getStatus())){
		//				stop(oldLIve.getId(), userId, userno, null);
		//			}else{
		//				stop(oldLIve.getId(), userId, userno, Boolean.FALSE);
		//			}
		//		}
			
			MonitorLiveDevicePO live = new MonitorLiveDevicePO(
					videoBundleId, videoBundleName, videoBundleType, videoLayerId, videoChannelId, videoBaseType,
					audioBundleId, audioBundleName, audioBundleType, audioLayerId, audioChannelId, audioBaseType,
					dstVideoBundleId, dstVideoBundleName, dstVideoBundleType, dstVideoLayerId, dstVideoChannelId, dstVideoBaseType,
					dstAudioBundleId, dstAudioBundleName, dstAudioBundleType, dstAudioLayerId, dstAudioChannelId, dstAudioBaseType,
					userId,
					targetAvtpl.getId(),
					targetGear.getId(),
					DstDeviceType.valueOf(dstDeviceType),
					LiveType.LOCAL_LOCAL,
					(osd==null?null:osd.getId()),
					(osd==null?null:osd.getUsername()));
			
			//新加的参数，就不改MonitorLiveDevicePO(xxx)方法了
			live.setUdpUrl(udpUrl);
			live.setVideoDeviceModel(videoBundle.getDeviceModel());
			live.setAudioDeviceModel(videoBundle.getDeviceModel());
			live.setDstVideoDeviceModel(dstVideoBundle.getDeviceModel());
			live.setDstAudioDeviceModel(dstVideoBundle.getDeviceModel());
			
			monitorLiveDeviceDao.save(live);
			
			
		//		LogicBO logic = openBundle(live, codec, playerCodec, osd, videoBundle, dstVideoBundle, userId, transcord, udpUrl);
			
		//		executeBusiness.execute(logic, "点播系统：本地设备点播本地设备");
			
			return live;
		}
	}
	
	/**
	 * 本地看xt设备<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 下午2:27:59
	 * @param Long osdId 字幕id
	 * @param String videoBundleId 视频源设备id
	 * @param String videoBundleName 视频源设备名称
	 * @parma String videoBundleType 视频源设备类型
	 * @param String videoLayerId 视频源设备接入层
	 * @param String videoChannelId 视频源通道id
	 * @param String videoBaseType 视频源通道类型
	 * @param String audioBundleId 音频源设备id
	 * @param String audioBundleName 音频源设备名称
	 * @param String audioBundleType 音频源设备类型
	 * @param String audioLayerId 音频源设备接入层id
	 * @param String audioChannelId 音频源通道id
	 * @param String audioBaseType 音频源通道类型
	 * @param String dstVideoBundleId 目标视频设备id
	 * @param String dstVideoBundleName 目标视频设备名称
	 * @param String dstVideoBundleType 目标视频设备类型
	 * @param String dstVideoLayerId 目标视频设备接入层
	 * @param String dstVideoChannelId 目标视频设备通道id
	 * @param String dstVideoBaseType 目标视频设备通道类型
	 * @param String dstAudioBundleId 目标音频设备id
	 * @param String dstAudioBundleName 目标音频设备名称
	 * @param String dstAudioBundleType 目标音频设备类型
	 * @param String dstAudioLayerId 目标音频设备接入层
	 * @param String dstAudioChannelId 目标音频设备通道id
	 * @param String dstAudioBaseType 目标音频设备通道类型
	 * @param String dstDeviceType 业务类型 WEBSITE_PLAYER, DEVICE
	 * @param Long userId 发起业务用户id
	 * @param String userno 发起业务用户号码
	 * @return MonitorLiveDevicePO 点播任务
	 */
	public MonitorLiveDevicePO startLocalSeeXt(
			Long osdId,
			String videoBundleId,
			String videoBundleName,
			String videoBundleType,
			String videoLayerId,
			String videoChannelId,
			String videoBaseType,
			String audioBundleId,
			String audioBundleName,
			String audioBundleType,
			String audioLayerId,
			String audioChannelId,
			String audioBaseType,
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
			String dstDeviceType,
			Long userId,
			String userno) throws Exception{
		
		if(isLock){
			synchronized (lockMonitorLiveDevice.intern()) {
				List<VedioCapacityPO> videoCapacityList = vedioCapacityService.findAll();
				if(videoCapacityList == null || videoCapacityList.size()==0 || videoCapacityList.get(0).getTurnCapacity() == null){
					throw new BaseException(StatusCode.FORBIDDEN, "没有查到转发容量");
				}
				
				Long TurnCapacity = videoCapacityList.get(0).getTurnCapacity();
				Long forwardCount = commandSystemQueryService.queryCountOfTransmit();
				if(TurnCapacity <= forwardCount){
					throw new BaseException(StatusCode.FORBIDDEN, "转发路数已经达到上限");
				}
				
				//参数校验
				isVideoBundleNotNull(videoBundleId, dstVideoBundleId);
				regularizedAudioParams(audioBundleId, audioChannelId, dstAudioBundleId, dstAudioChannelId);
				authorize(videoBundleId, audioBundleId, userId);
				
				//xt编码器
				BundlePO xtEncoder = bundleDao.findByBundleId(videoBundleId);
				
				//虚拟xt编码器--这个不取原来的号了
				videoBundleId = UUID.randomUUID().toString().replace("-", "");
				videoBundleId = new StringBufferWrapper().append(xtEncoder.getBundleId()).append("_").append(videoBundleId).toString();
				audioBundleId = videoBundleId;
				
				//参数模板
				Map<String, Object> result = commons.queryDefaultAvCodec();
				AvtplPO targetAvtpl = (AvtplPO)result.get("avtpl");
				AvtplGearsPO targetGear = (AvtplGearsPO)result.get("gear");
				CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
				
				//获取联网id
				String networkLayerId = commons.queryNetworkLayerId();
				videoLayerId = audioLayerId = networkLayerId;
				
				//字幕
				MonitorOsdPO osd = monitorOsdDao.findOne(osdId==null?0:osdId);
				
				//处理业务覆盖
				coverBusiness(dstVideoBundleId, dstVideoChannelId, dstAudioBundleId, dstAudioChannelId, userId, userno);
				
				//视频目的
				BundlePO dstVideoBundle = bundleDao.findByBundleId(dstVideoBundleId);
				
				MonitorLiveDevicePO live = new MonitorLiveDevicePO(
						videoBundleId, videoBundleName, videoBundleType, videoLayerId, videoChannelId, videoBaseType,
						audioBundleId, audioBundleName, audioBundleType, audioLayerId, audioChannelId, audioBaseType,
						dstVideoBundleId, dstVideoBundleName, dstVideoBundleType, dstVideoLayerId, dstVideoChannelId, dstVideoBaseType,
						dstAudioBundleId, dstAudioBundleName, dstAudioBundleType, dstAudioLayerId, dstAudioChannelId, dstAudioBaseType,
						userId,
						targetAvtpl.getId(),
						targetGear.getId(),
						DstDeviceType.valueOf(dstDeviceType),
						LiveType.LOCAL_XT,
						(osd==null?null:osd.getId()),
						(osd==null?null:osd.getUsername()));
				
				live.setDstVideoDeviceModel(dstVideoBundle.getDeviceModel());
				live.setDstAudioDeviceModel(dstVideoBundle.getDeviceModel());
				
				monitorLiveDeviceDao.save(live);
				LogicBO logic = openBundle(live, codec, osd, xtEncoder, dstVideoBundle, userId);
				
				logic.setPass_by(new ArrayList<PassByBO>());
				
				XtBusinessPassByContentBO passByContent = new XtBusinessPassByContentBO().setCmd(XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_ENCODER)
																						 .setOperate(XtBusinessPassByContentBO.OPERATE_START)
																						 .setUuid(live.getUuid())
																						 .setSrc_user(userno)
																						 .setXt_encoder(new HashMapWrapper<String, String>().put("layerid", networkLayerId)
																								 											.put("bundleid", videoBundleId)
																								 											.put("video_channelid", videoChannelId)
																								 											.put("audio_channelid", audioChannelId)
																								 											.getMap())
																						 .setDst_number(xtEncoder.getUsername())
																						 .setVparam(codec);
				
				PassByBO passby = new PassByBO().setLayer_id(networkLayerId)
												.setType(XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_ENCODER)
												.setPass_by_content(passByContent);
				
				logic.getPass_by().add(passby);
				
				resourceServiceClient.coverLianwangPassby(
						live.getUuid(), 
						networkLayerId, 
						XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_ENCODER, 
						JSON.toJSONString(passby));
				
				executeBusiness.execute(logic, "点播系统：本地点播xt设备");
				
				return live;
			}
		}else{
			//参数校验
			isVideoBundleNotNull(videoBundleId, dstVideoBundleId);
			regularizedAudioParams(audioBundleId, audioChannelId, dstAudioBundleId, dstAudioChannelId);
			authorize(videoBundleId, audioBundleId, userId);
			
			//xt编码器
			BundlePO xtEncoder = bundleDao.findByBundleId(videoBundleId);
			
			//虚拟xt编码器--这个不取原来的号了
			videoBundleId = UUID.randomUUID().toString().replace("-", "");
			videoBundleId = new StringBufferWrapper().append(xtEncoder.getBundleId()).append("_").append(videoBundleId).toString();
			audioBundleId = videoBundleId;
			
			//参数模板
			Map<String, Object> result = commons.queryDefaultAvCodec();
			AvtplPO targetAvtpl = (AvtplPO)result.get("avtpl");
			AvtplGearsPO targetGear = (AvtplGearsPO)result.get("gear");
			CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
			
			//获取联网id
			String networkLayerId = commons.queryNetworkLayerId();
			videoLayerId = audioLayerId = networkLayerId;
			
			//字幕
			MonitorOsdPO osd = monitorOsdDao.findOne(osdId==null?0:osdId);
			
			//处理业务覆盖
			coverBusiness(dstVideoBundleId, dstVideoChannelId, dstAudioBundleId, dstAudioChannelId, userId, userno);
			
			//视频目的
			BundlePO dstVideoBundle = bundleDao.findByBundleId(dstVideoBundleId);
			
			MonitorLiveDevicePO live = new MonitorLiveDevicePO(
					videoBundleId, videoBundleName, videoBundleType, videoLayerId, videoChannelId, videoBaseType,
					audioBundleId, audioBundleName, audioBundleType, audioLayerId, audioChannelId, audioBaseType,
					dstVideoBundleId, dstVideoBundleName, dstVideoBundleType, dstVideoLayerId, dstVideoChannelId, dstVideoBaseType,
					dstAudioBundleId, dstAudioBundleName, dstAudioBundleType, dstAudioLayerId, dstAudioChannelId, dstAudioBaseType,
					userId,
					targetAvtpl.getId(),
					targetGear.getId(),
					DstDeviceType.valueOf(dstDeviceType),
					LiveType.LOCAL_XT,
					(osd==null?null:osd.getId()),
					(osd==null?null:osd.getUsername()));
			
			live.setDstVideoDeviceModel(dstVideoBundle.getDeviceModel());
			live.setDstAudioDeviceModel(dstVideoBundle.getDeviceModel());
			
			monitorLiveDeviceDao.save(live);
			LogicBO logic = openBundle(live, codec, osd, xtEncoder, dstVideoBundle, userId);
			
			logic.setPass_by(new ArrayList<PassByBO>());
			
			XtBusinessPassByContentBO passByContent = new XtBusinessPassByContentBO().setCmd(XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_ENCODER)
																					 .setOperate(XtBusinessPassByContentBO.OPERATE_START)
																					 .setUuid(live.getUuid())
																					 .setSrc_user(userno)
																					 .setXt_encoder(new HashMapWrapper<String, String>().put("layerid", networkLayerId)
																							 											.put("bundleid", videoBundleId)
																							 											.put("video_channelid", videoChannelId)
																							 											.put("audio_channelid", audioChannelId)
																							 											.getMap())
																					 .setDst_number(xtEncoder.getUsername())
																					 .setVparam(codec);
			
			PassByBO passby = new PassByBO().setLayer_id(networkLayerId)
											.setType(XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_ENCODER)
											.setPass_by_content(passByContent);
			
			logic.getPass_by().add(passby);
			
			resourceServiceClient.coverLianwangPassby(
					live.getUuid(), 
					networkLayerId, 
					XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_ENCODER, 
					JSON.toJSONString(passby));
			
			executeBusiness.execute(logic, "点播系统：本地点播xt设备");
			
			return live;
		}
	}
	
	/**
	 * xt看xt设备，这个暂时不用实现了<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 下午2:25:45
	 * @return MonitorLiveDevicePO 点播任务
	 */
	public MonitorLiveDevicePO startXtSeeXt() throws Exception{
		return null;
	}
	
	/**
	 * 停止当前用户的播放器点播设备任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月24日 下午1:38:14
	 * @param Collection<String> webPlayerBundleIds 播放器设备id列表
	 * @param Long userId 当前业务用户id
	 * @param String userno 当前业务用户号码
	 */
	public void stopWebPlayerLives(Collection<String> webPlayerBundleIds, Long userId, String userno) throws Exception{
		List<MonitorLiveDevicePO> lives = monitorLiveDeviceDao.findByDstVideoBundleIdIn(webPlayerBundleIds);
		if(lives!=null && lives.size()>0){
			for(MonitorLiveDevicePO live:lives){
				stop(live.getId(), userId, userno);
			}
		}
	}
	
	/**
	 * 停止点播设备<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 下午2:46:51
	 * @param Long liveId 点播任务id
	 * @param Long userId 发起用户id
	 * @param String userno 发起用户号码
	 */
	public void stop(Long liveId, Long userId, String userno) throws Exception{
		MonitorLiveDevicePO live = monitorLiveDeviceDao.findOne(liveId);
		if(live == null) return;
		UserVO userVO = userQuery.current();
		if(userId.longValue() == 1l || userVO.getIsGroupCreator()){
			//admin操作转换
			userId = live.getUserId();
			UserBO user = resourceService.queryUserById(userId, TerminalType.PC_PLATFORM);
			userno = user.getUserNo();
		}
		if(!live.getUserId().equals(userId)){
			throw new UserHasNoPermissionToRemoveLiveDeviceException(userId, liveId);
		}
		if(LiveType.XT_LOCAL.equals(live.getType())){
			stopXtSeeLocal(live, userId, userno, null);
		}else if(LiveType.LOCAL_XT.equals(live.getType())){
			stopLocalSeeXt(live, userId, userno, null);
		}else if(LiveType.LOCAL_LOCAL.equals(live.getType())){
			stopLocalSeeLocal(live, userId, userno, null);
		}else if(LiveType.XT_XT.equals(live.getType())){
			stopXtSeeXt(live, userId, userno, null);
		}
		operationLogService.send(userVO.getNickname(), "停止转发", live.getVideoBundleName() + " 停止转发给 " + live.getDstVideoBundleName());
	}
	
	/**
	 * 停止点播设备<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 下午2:46:51
	 * @param Long liveId 点播任务id
	 * @param Long userId 发起用户id
	 * @param String userno 发起用户号码
	 * @param Boolean stopAndDelete TRUE停止但不删除、FALSE删除、null停止且删除
	 */
	public void stop(Long liveId, Long userId, String userno, Boolean stopAndDelete) throws Exception{
		
		MonitorLiveDevicePO live = monitorLiveDeviceDao.findOne(liveId);
		if(live == null) return;
		UserVO userVO = userQuery.current();
		if(userId.longValue() == 1l || userVO.getIsGroupCreator()){
			//admin操作转换
			userId = live.getUserId();
			UserBO user = resourceService.queryUserById(userId, TerminalType.PC_PLATFORM);
			userno = user.getUserNo();
		}
		if(!live.getUserId().equals(userId)){
			throw new UserHasNoPermissionToRemoveLiveDeviceException(userId, liveId);
		}
		
		if(stopAndDelete == null){
			if(LiveType.XT_LOCAL.equals(live.getType())){
				stopXtSeeLocal(live, userId, userno, stopAndDelete);
			}else if(LiveType.LOCAL_XT.equals(live.getType())){
				stopLocalSeeXt(live, userId, userno, stopAndDelete);
			}else if(LiveType.LOCAL_LOCAL.equals(live.getType())){
				stopLocalSeeLocal(live, userId, userno, stopAndDelete);
			}else if(LiveType.XT_XT.equals(live.getType())){
				stopXtSeeXt(live, userId, userno, stopAndDelete);
			}
			
			operationLogService.send(userVO.getUsername(), "停止且删除转发", userVO.getUsername() + "停止且删除转发：" + live.getVideoBundleName() + " 转发给 " + live.getDstVideoBundleName());
		}else if(Boolean.TRUE.equals(stopAndDelete)){
			
			if(LiveType.XT_LOCAL.equals(live.getType())){
				stopXtSeeLocal(live, userId, userno, stopAndDelete);
			}else if(LiveType.LOCAL_XT.equals(live.getType())){
				stopLocalSeeXt(live, userId, userno, stopAndDelete);
			}else if(LiveType.LOCAL_LOCAL.equals(live.getType())){
				stopLocalSeeLocal(live, userId, userno, stopAndDelete);
			}else if(LiveType.XT_XT.equals(live.getType())){
				stopXtSeeXt(live, userId, userno, stopAndDelete);
			}
			
			operationLogService.send(userVO.getUsername(), "停止但不删除转发", userVO.getUsername() + "停止但不删除转发：" + live.getVideoBundleName() + " 转发给 " + live.getDstVideoBundleName());
		}else{
			monitorLiveDeviceDao.delete(live);
			operationLogService.send(userVO.getUsername(), "删除转发", userVO.getUsername() + "删除转发：" + live.getVideoBundleName() + " 转发给 " + live.getDstVideoBundleName());
		}
		
		
		
	}

	/**
	 * 批量停止点播设备<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月9日 上午10:08:53
	 * @param List<Long> liveIdList 点播任务id集合
	 * @param Long userId 发起用户id
	 * @param String userno 发起用户号码
	 * @param Boolean stopAndDelete TRUE停止但不删除、FALSE删除、null停止且删除
	 */
	public void stop(List<Long> liveIdList, Long userId, String userno, Boolean stopAndDelete) throws Exception{
		
		List<MonitorLiveDevicePO> liveList = monitorLiveDeviceDao.findAll(liveIdList);
		for(MonitorLiveDevicePO live:liveList){
			try {
				if(live == null) continue;
				UserVO userVO = userQuery.current();
				if(userId.longValue() == 1l || userVO.getIsGroupCreator()){
					//admin操作转换
					userId = live.getUserId();
					UserBO user = resourceService.queryUserById(userId, TerminalType.PC_PLATFORM);
					userno = user.getUserNo();
				}
				if(!live.getUserId().equals(userId)){
					throw new UserHasNoPermissionToRemoveLiveDeviceException(userId, live.getId());
				}
				
				if(stopAndDelete == null){
					if(LiveType.XT_LOCAL.equals(live.getType())){
						stopXtSeeLocal(live, userId, userno, stopAndDelete);
					}else if(LiveType.LOCAL_XT.equals(live.getType())){
						stopLocalSeeXt(live, userId, userno, stopAndDelete);
					}else if(LiveType.LOCAL_LOCAL.equals(live.getType())){
						stopLocalSeeLocal(live, userId, userno, stopAndDelete);
					}else if(LiveType.XT_XT.equals(live.getType())){
						stopXtSeeXt(live, userId, userno, stopAndDelete);
					}
					
					operationLogService.send(userVO.getNickname(), "停止转发", live.getVideoBundleName() + " 停止转发给 " + live.getDstVideoBundleName());
				}else if(Boolean.TRUE.equals(stopAndDelete)){
					
					if(LiveType.XT_LOCAL.equals(live.getType())){
						stopXtSeeLocal(live, userId, userno, stopAndDelete);
					}else if(LiveType.LOCAL_XT.equals(live.getType())){
						stopLocalSeeXt(live, userId, userno, stopAndDelete);
					}else if(LiveType.LOCAL_LOCAL.equals(live.getType())){
						stopLocalSeeLocal(live, userId, userno, stopAndDelete);
					}else if(LiveType.XT_XT.equals(live.getType())){
						stopXtSeeXt(live, userId, userno, stopAndDelete);
					}
				}else{
					monitorLiveDeviceDao.delete(live);
				}
			} catch (Exception e) {
				System.out.println("批量停止设备报错:");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 停止点播设备<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 下午2:47:34
	 * @param String liveUuid 点播任务uuid
	 * @param Long userId 发起用户id
	 * @param String userno 发起用户号码
	 */
	public void stop(String liveUuid, Long userId, String userno) throws Exception{
		MonitorLiveDevicePO live = monitorLiveDeviceDao.findByUuid(liveUuid);
		if(live == null) return;
		if(userId.longValue() == 1l){
			//admin操作转换
			userId = live.getUserId();
			UserBO user = resourceService.queryUserById(userId, TerminalType.PC_PLATFORM);
			userno = user.getUserNo();
		}
		if(!live.getUserId().equals(userId)){
			throw new UserHasNoPermissionToRemoveLiveDeviceException(userId, liveUuid);
		}
		if(LiveType.XT_LOCAL.equals(live.getType())){
			stopXtSeeLocal(live, userId, userno, null);
		}else if(LiveType.LOCAL_XT.equals(live.getType())){
			stopLocalSeeXt(live, userId, userno, null);
		}else if(LiveType.LOCAL_LOCAL.equals(live.getType())){
			stopLocalSeeLocal(live, userId, userno, null);
		}else if(LiveType.XT_XT.equals(live.getType())){
			stopXtSeeXt(live, userId, userno, null);
		}
	}
	
	/**
	 * 停止xt点播本地设备任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 下午3:09:28
	 * @param MonitorLiveDevicePO live xt点播本地设备任务
	 * @param Long userId 发起用户id
	 * @param String userno 发起用户号码
	 */
	public void stopXtSeeLocal(MonitorLiveDevicePO live, Long userId, String userno, Boolean stopAndDelete) throws Exception{
		
		//本地编码器
		BundlePO localEncoder = bundleDao.findByBundleId(live.getVideoBundleId());
		
		AvtplPO targetAvtpl = avtplDao.findOne(live.getAvTplId());
		AvtplGearsPO targetGear = avtplGearsDao.findOne(live.getGearId());
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
		codec = live.getAudioChannelId() == null?codec.setAudio_param(null):codec;
		
		String networkLayerId = commons.queryNetworkLayerId();
		
		LogicBO logic = closeBundle(live);
		
		logic.setPass_by(new ArrayList<PassByBO>());
		
		XtBusinessPassByContentBO passByContent = new XtBusinessPassByContentBO().setCmd(XtBusinessPassByContentBO.CMD_XT_SEE_LOCAL_ENCODER)
																				 .setOperate(XtBusinessPassByContentBO.OPERATE_STOP)
																				 .setUuid(live.getUuid())
																				 .setSrc_user(userno)
																				 .setLocal_encoder(new HashMapWrapper<String, String>().put("layerid", live.getVideoLayerId())
																						 											   .put("bundleid", live.getVideoBundleId())	
																						 											   .put("video_channelid", live.getVideoChannelId())
																						 											   .put("audio_channelid", live.getAudioChannelId())
																						 											   .getMap())
																				 .setDst_number(localEncoder.getUsername())
																				 .setVparam(codec);
		
		PassByBO passby = new PassByBO().setLayer_id(networkLayerId)
										.setType(XtBusinessPassByContentBO.CMD_XT_SEE_LOCAL_ENCODER)
										.setPass_by_content(passByContent);
		
		logic.getPass_by().add(passby);
		
		if(stopAndDelete == null){
			monitorLiveDeviceDao.delete(live);
		}if(Boolean.TRUE.equals(stopAndDelete)){
			live.setStatus(MonitorRecordStatus.STOP);
			monitorLiveDeviceDao.save(live);
		}
		
		resourceServiceClient.removeLianwangPassby(live.getUuid());
		
		executeBusiness.execute(logic, "点播系统：停止xt点播本地设备 " + live.getVideoBundleName());
		
	}
	
	/**
	 * BQ项目给按bundleId停止外域看本地<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月24日 下午3:16:53
	 * @param liveList 点播任务集合
	 * @param stopAndDelete 默认true
	 */
	public void stopXtSeeLocal(List<MonitorLiveDevicePO> liveList, Boolean stopAndDelete) throws Exception{
		
		//本地编码器
		for(MonitorLiveDevicePO live : liveList){
			BundlePO localEncoder = bundleDao.findByBundleId(live.getVideoBundleId());
			
			AvtplPO targetAvtpl = avtplDao.findOne(live.getAvTplId());
			AvtplGearsPO targetGear = avtplGearsDao.findOne(live.getGearId());
			CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
			codec = live.getAudioChannelId() == null?codec.setAudio_param(null):codec;
			
			String networkLayerId = commons.queryNetworkLayerId();
			
			LogicBO logic = closeBundle(live);
			
			logic.setPass_by(new ArrayList<PassByBO>());
			
			XtBusinessPassByContentBO passByContent = new XtBusinessPassByContentBO().setCmd(XtBusinessPassByContentBO.CMD_XT_SEE_LOCAL_ENCODER)
																					 .setOperate(XtBusinessPassByContentBO.OPERATE_STOP)
																					 .setUuid(live.getUuid())
																					 .setLocal_encoder(new HashMapWrapper<String, String>().put("layerid", live.getVideoLayerId())
																							 											   .put("bundleid", live.getVideoBundleId())	
																							 											   .put("video_channelid", live.getVideoChannelId())
																							 											   .put("audio_channelid", live.getAudioChannelId())
																							 											   .getMap())
																					 .setDst_number(localEncoder.getUsername())
																					 .setVparam(codec);
			
			PassByBO passby = new PassByBO().setLayer_id(networkLayerId)
											.setType(XtBusinessPassByContentBO.CMD_XT_SEE_LOCAL_ENCODER)
											.setPass_by_content(passByContent);
			
			logic.getPass_by().add(passby);
			
			if(stopAndDelete == null){
				monitorLiveDeviceDao.delete(live);
			}if(Boolean.TRUE.equals(stopAndDelete)){
				live.setStatus(MonitorRecordStatus.STOP);
				monitorLiveDeviceDao.delete(live);
			}
			
			resourceServiceClient.removeLianwangPassby(live.getUuid());
			
			executeBusiness.execute(logic, "点播系统：停止xt点播本地设备 " + live.getVideoBundleName());
		}
	}
	
	/**
	 * 停止本地点播本地设备任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 下午3:09:28
	 * @param MonitorLiveDevicePO live 本地点播本地设备任务
	 * @param Long userId 发起用户id
	 * @param String userno 发起用户号码
	 */
	public void stopLocalSeeLocal(MonitorLiveDevicePO live, Long userId, String userno, Boolean stopAndDelete) throws Exception{
		
		AvtplPO targetAvtpl = avtplDao.findOne(live.getAvTplId());
		AvtplGearsPO targetGear = avtplGearsDao.findOne(live.getGearId());
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
		
		if(live.getUdpUrl() == null){
			clearOsd(live, codec);
		}
		
		LogicBO logic = closeBundle(live);
		
		if(stopAndDelete == null){
			monitorLiveDeviceDao.delete(live);
		}if(Boolean.TRUE.equals(stopAndDelete)){
			live.setStatus(MonitorRecordStatus.STOP);
			monitorLiveDeviceDao.save(live);
		}
		
		executeBusiness.execute(logic, "点播系统：停止本地点播本地设备任务");
	}
	
	/**
	 * 停止本地点播xt设备任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 下午3:09:28
	 * @param MonitorLiveDevicePO live 本地点播xt设备任务
	 * @param Long userId 发起用户id
	 * @param String userno 发起用户号码
	 */
	public void stopLocalSeeXt(MonitorLiveDevicePO live, Long userId, String userno, Boolean stopAndDelete) throws Exception{
		
		//xt编码器
		BundlePO xtEncoder = bundleDao.findByBundleId(live.getVideoBundleId().indexOf("_")>=0?live.getVideoBundleId().split("_")[0]:live.getVideoBundleId());
		
		AvtplPO targetAvtpl = avtplDao.findOne(live.getAvTplId());
		AvtplGearsPO targetGear = avtplGearsDao.findOne(live.getGearId());
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
		
		String networkLayerId = commons.queryNetworkLayerId();
		
		clearOsd(live, codec);
		
		LogicBO logic = closeBundle(live);
		
		logic.setPass_by(new ArrayList<PassByBO>());
		
		XtBusinessPassByContentBO passByContent = new XtBusinessPassByContentBO().setCmd(XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_ENCODER)
																				 .setOperate(XtBusinessPassByContentBO.OPERATE_STOP)
																				 .setUuid(live.getUuid())
																				 .setSrc_user(userno)
																				 .setXt_encoder(new HashMapWrapper<String, String>().put("layerid", networkLayerId)
																						 											.put("bundleid", live.getVideoBundleId())
																						 											.put("video_channelid", live.getVideoChannelId())
																						 											.put("audio_channelid", live.getAudioChannelId())
																						 											.getMap())
																				 .setDst_number(xtEncoder.getUsername())
																				 .setVparam(codec);
		
		PassByBO passby = new PassByBO().setLayer_id(networkLayerId)
										.setType(XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_ENCODER)
										.setPass_by_content(passByContent);
		
		logic.getPass_by().add(passby);
		
		if(stopAndDelete == null){
			monitorLiveDeviceDao.delete(live);
		}if(Boolean.TRUE.equals(stopAndDelete)){
			live.setStatus(MonitorRecordStatus.STOP);
			monitorLiveDeviceDao.save(live);
		}
		
		resourceServiceClient.removeLianwangPassby(live.getUuid());
		
		executeBusiness.execute(logic, "点播系统：停止本地点播xt设备");
		
	}
	
	/**
	 * 停止xt看xt设备，这个暂时不实现<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 下午2:40:01
	 */
	public void stopXtSeeXt(MonitorLiveDevicePO live, Long userId, String userno, Boolean stopAndDelete) throws Exception{
		
	}
	
	/**
	 * 修改osd模板<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月28日 下午5:20:59
	 * @param Long liveId 直播任务id
	 * @param Long osdId 字幕id
	 * @param Long userId 业务用户id
	 */
	public MonitorOsdPO changeOsd(Long liveId, Long osdId, Long userId) throws Exception{
		
		MonitorLiveDevicePO live = monitorLiveDeviceDao.findOne(liveId);
		if(live == null){
			throw new MonitorLiveNotExistException(liveId);
		}
		if(!live.getUserId().equals(userId)){
			throw new UserHasNoPermissionToRemoveLiveDeviceException(liveId, userId);
		}
		
		MonitorOsdPO osd = monitorOsdDao.findOne(osdId);
		if(osd == null){
			throw new MonitorOsdNotExistException(osdId);
		}
		
		live.setOsdId(osd.getId());
		live.setOsdUsername(osd.getUsername());
		monitorLiveDeviceDao.save(live);
		
		AvtplPO targetAvtpl = null;
		AvtplGearsPO targetGear = null;
		targetAvtpl = avtplDao.findOne(live.getAvTplId());
		targetGear = avtplGearsDao.findOne(live.getGearId());
		
		LogicBO logic = new LogicBO().setUserId(userId.toString())
				 			 .setConnectBundle(new ArrayList<ConnectBundleBO>());
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
		
		ConnectBundleBO connectDstVideoBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
																	 .setLock_type("write")
																     .setBundleId(live.getDstVideoBundleId())
																     .setLayerId(live.getDstVideoLayerId())
																     .setBundle_type(live.getDstVideoBundleType());
		ConnectBO connectDstVideoChannel = new ConnectBO().setChannelId(live.getDstVideoChannelId())
													      .setChannel_status("Open")
													      .setBase_type(live.getDstVideoBaseType())
													      .setCodec_param(codec);
		/*ForwardSetSrcBO videoForwardSetSrc = new ForwardSetSrcBO().setType("channel")
															 	  .setBundleId(live.getVideoBundleId())
															 	  .setLayerId(live.getVideoLayerId())
															 	  .setChannelId(live.getVideoChannelId());
		
		connectDstVideoChannel.setSource_param(videoForwardSetSrc);*/
		connectDstVideoBundle.setChannels(new ArrayListWrapper<ConnectBO>().add(connectDstVideoChannel).getList());
		logic.getConnectBundle().add(connectDstVideoBundle);
		
		BundlePO videoBundle = bundleDao.findByBundleId(live.getVideoBundleId());
		
		//先发清除
		connectDstVideoChannel.setOsds(monitorOsdService.clearProtocol(videoBundle.getUsername(), live.getVideoBundleName()));
		executeBusiness.execute(logic, "点播系统：清除字幕");
		
		//后发设置
		connectDstVideoChannel.setOsds(monitorOsdService.protocol(osd, videoBundle.getUsername(), live.getVideoBundleName()));
		executeBusiness.execute(logic, "点播系统：重设字幕");
		
		return osd;
	}
	
	/**
	 * 点播任务视频设备不能为空<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 上午9:34:56
	 * @param String videoBundleId 源视频设备id
	 * @param String dstVideoBundleId 目标视频设备id
	 */
	private void isVideoBundleNotNull(String videoBundleId, String dstVideoBundleId) throws Exception{
		if(videoBundleId==null || "".equals(videoBundleId)){
			throw new MonitorLiveSourceVideoBundleCannotBeNullException();
		}
		if(dstVideoBundleId==null || "".equals(dstVideoBundleId)){
			throw new MonitorLiveDstVideoBundleCannotBeNullException();
		}
	}
	
	/**
	 * 点播任务的源和目的不能是同一设备<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月9日 上午9:34:56
	 * @param String videoBundleId 源视频设备id
	 * @param String dstVideoBundleId 目标视频设备id
	 * @param String audioBundleId 源音频设备id
	 * @param String dstAudioBundleId 目标音频设备id
	 */
	private void isSrcAndDstBeTheSame(String videoBundleId, String dstVideoBundleId, String audioBundleId, String dstAudioBundleId) throws Exception{
		if(videoBundleId!=null && !"".equals(videoBundleId) 
				&& (videoBundleId.equals(dstVideoBundleId) || videoBundleId.equals(dstAudioBundleId))){
			throw new MonitorLiveDstVideoBundleCannotBeNullException();
		}
		if(audioBundleId!=null && !"".equals(audioBundleId) 
				&& (audioBundleId.equals(dstVideoBundleId) || audioBundleId.equals(dstAudioBundleId))){
			throw new MonitorLiveDstVideoBundleCannotBeNullException();
		}
	}
	
	/**
	 * 规范化音频参数<br/>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 上午9:39:32
	 * @param String audioBundleId 源音频设备id
	 * @param String audioChannelId 源音频通道id
	 * @param String dstAudioBundleId 目的音频设备id
	 * @param String dstAudioChannelId 目的音频通道id
	 */
	private void regularizedAudioParams(
			String audioBundleId, 
			String audioChannelId, 
			String dstAudioBundleId, 
			String dstAudioChannelId){
		audioBundleId = "".equals(audioBundleId)?null:audioBundleId;
		audioChannelId = "".equals(dstAudioChannelId)?null:audioChannelId;
		dstAudioBundleId = "".equals(dstAudioBundleId)?null:dstAudioBundleId;
		dstAudioChannelId = "".equals(dstAudioChannelId)?null:dstAudioChannelId;
		if(audioBundleId!=null && dstAudioBundleId==null){
			audioBundleId = null;
			dstAudioBundleId = null;
		}
		if(audioBundleId==null && dstAudioBundleId!=null){
			audioBundleId = null;
			dstAudioBundleId = null;
		}
	}
	
	/**
	 * 用户对设备的权限校验<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月27日 下午2:15:11
	 * @param String videoBundleId 视频设备id
	 * @param String audioBundleId 音频设备id
	 * @param Long userId 业务用户id
	 */
	private void authorize(String videoBundleId, String audioBundleId, Long userId) throws Exception{
		boolean authorized = resourceService.hasPrivilegeOfBundle(userId, videoBundleId, BUSINESS_OPR_TYPE.DIANBO);
		if(!authorized){
			throw new UserHasNoPermissionForBusinessException(BUSINESS_OPR_TYPE.DIANBO, 0);
		}else{
			if(audioBundleId!=null && audioBundleId.equals(videoBundleId)){
				authorized = resourceService.hasPrivilegeOfBundle(userId, audioBundleId, BUSINESS_OPR_TYPE.DIANBO);
				if(!authorized){
					throw new UserHasNoPermissionForBusinessException(BUSINESS_OPR_TYPE.DIANBO, 0);
				}
			}
		}
	}
	
	/**
	 * 覆盖业务<br/>
	 * <p>
	 * 	1.当前用户的设备点播任务，停止
	 *  2.当前用户的用户点播任务，停止
	 *  3.当前用户的呼叫任务--
	 * </p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 下午3:45:00
	 * @param String dstVideoBundleId 目标视频设备id
	 * @param String dstVideoChannelId 目标视频通道id
	 * @param String dstAudioBundleId 目标音频设备id
	 * @param String dstAudioChannelId 目标音频通道id
	 * @param Long userId 发起业务用户id
	 */
	private void coverBusiness(
			String dstVideoBundleId, 
			String dstVideoChannelId, 
			String dstAudioBundleId, 
			String dstAudioChannelId, 
			Long userId,
			String userno) throws Exception{
		
		List<MonitorLiveDevicePO> deviceLives = monitorLiveDeviceDao.findByDstVideoBundleIdAndDstVideoChannelIdAndDstAudioBundleIdAndDstAudioChannelId(dstVideoBundleId, dstVideoChannelId, dstAudioBundleId, dstAudioChannelId);
		if(deviceLives!=null && deviceLives.size()>0){
			for(MonitorLiveDevicePO deviceLive:deviceLives){
				if(MonitorRecordStatus.STOP.equals(deviceLive.getStatus())){
					
				}else{
					stop(deviceLive.getId(), userId, userno);
				}
			}
			
			monitorLiveDeviceDao.deleteInBatch(deviceLives);
		}
		
		List<MonitorLiveUserPO> userLives = monitorLiveUserDao.findByDstVideoBundleIdAndDstVideoChannelIdAndDstAudioBundleIdAndDstAudioChannelId(dstVideoBundleId, dstVideoChannelId, dstAudioBundleId, dstAudioChannelId);
		if(userLives!=null && userLives.size()>0){
			for(MonitorLiveUserPO userLive:userLives){
				monitorLiveUserService.stop(userLive.getId(), userId, userno);
			}
		}
		
		List<MonitorRecordPlaybackTaskPO> playbackLives = monitorRecordPlaybackTaskDao.findByDstVideoBundleIdAndDstVideoChannelIdAndDstAudioBundleIdAndDstAudioChannelId(dstVideoBundleId, dstVideoChannelId, dstAudioBundleId, dstAudioChannelId);
		if(playbackLives!=null && playbackLives.size()>0){
			for(MonitorRecordPlaybackTaskPO playbackLive:playbackLives){
				monitorRecordPlaybackTaskService.remove(playbackLive.getId(), userId);
			}
		}
	}
	
	/**
	 * 处理openbundle协议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 上午10:24:26
	 * @param MonitorLiveDevicePO live 点播设备任务
	 * @param CodecParamBO codec 参数模板
	 * @param MonitorOsdPO osd 字幕
	 * @param TerminalBundlePO videoBundle 视频源设备
	 * @param Long userId 业务用户
	 * @return LogicBO 协议数据
	 */
	private LogicBO openBundle(
			MonitorLiveDevicePO live,
			CodecParamBO codec,
			MonitorOsdPO osd,
			BundlePO videoBundle,
			BundlePO dstVideoBundle,
			Long userId) throws Exception{
		
		return openBundle(
			live,
			codec,
			codec,
			osd,
			videoBundle,
			dstVideoBundle,
			userId,
			false,
			null);
	}
	
	/**
	 * 处理openbundle协议<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月18日 上午9:31:30
	 * @param live 点播设备任务
	 * @param encodeCodec 编码端的参数模板
	 * @param decodeCodec 解码端的参数模板
	 * @param osd 字幕
	 * @param videoBundle 视频源设备
	 * @param userId 业务用户
	 * @param transcord 是否转码
	 * @param udpUrl udp地址
	 * @return
	 * @throws Exception
	 */
	private LogicBO openBundle(
			MonitorLiveDevicePO live,
			CodecParamBO encodeCodec,
			CodecParamBO decodeCodec,
			MonitorOsdPO osd,
			BundlePO videoBundle,
			BundlePO dstVideoBundle,
			Long userId,
			boolean transcord,
			String udpUrl) throws Exception{
		
		//呼叫设备
		LogicBO logic = new LogicBO().setUserId(userId.toString())
				 			 		 .setConnectBundle(new ArrayList<ConnectBundleBO>());
		
		if(LiveType.XT_LOCAL.equals(live.getType()) || LiveType.LOCAL_LOCAL.equals(live.getType())){
			//呼叫源--这里面先认为源和目的设备不一样
			ConnectBundleBO connectVideoBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
															          .setOperateType(ConnectBundleBO.OPERATE_TYPE)
																	  .setLock_type("write")
																	  .setBundleId(live.getVideoBundleId())
																	  .setLayerId(live.getVideoLayerId())
																	  .setBundle_type(live.getVideoBundleType())
																	  .setDevice_model(videoBundle.getDeviceModel());
			ConnectBO connectVideoChannel = new ConnectBO().setChannelId(live.getVideoChannelId())
														   .setChannel_status("Open")
														   .setBase_type(live.getVideoBaseType())
														   .setCodec_param(encodeCodec);
			//发组播视频
			if(Boolean.TRUE.equals(videoBundle.getMulticastEncode())){
				String videoAddr = multicastService.addrAddPort(videoBundle.getMulticastEncodeAddr(), 0);
				connectVideoChannel.setMode(TransmissionMode.MULTICAST.getCode())
									.setMulti_addr(videoAddr)
									.setSrc_multi_ip(videoBundle.getMulticastSourceIp());
			}
			connectVideoBundle.setChannels(new ArrayListWrapper<ConnectBO>().add(connectVideoChannel).getList());
			
			logic.getConnectBundle().add(connectVideoBundle);
			
			if(live.getAudioBundleId() != null){
				if(live.getVideoBundleId().equals(live.getAudioBundleId())){
					ConnectBO connectAudioChannel = new ConnectBO().setChannelId(live.getAudioChannelId())
																   .setChannel_status("Open")
																   .setBase_type(live.getAudioBaseType())
																   .setCodec_param(encodeCodec);
					//发组播音频
					if(Boolean.TRUE.equals(videoBundle.getMulticastEncode())){
						String audioAddr = multicastService.addrAddPort(videoBundle.getMulticastEncodeAddr(), 4);
						connectAudioChannel.setMode(TransmissionMode.MULTICAST.getCode())
											.setMulti_addr(audioAddr)
											.setSrc_multi_ip(videoBundle.getMulticastSourceIp());
					}
					connectVideoBundle.getChannels().add(connectAudioChannel);
				}else{
					ConnectBundleBO connectAudioBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
					          												  .setOperateType(ConnectBundleBO.OPERATE_TYPE)
																			  .setLock_type("write")
																			  .setBundleId(live.getAudioBundleId())
																			  .setLayerId(live.getAudioLayerId())
																			  .setBundle_type(live.getAudioBundleType())
																			  .setDevice_model(videoBundle.getDeviceModel());
								
					ConnectBO connectAudioChannel = new ConnectBO().setChannelId(live.getAudioChannelId())
																   .setChannel_status("Open")
																   .setBase_type(live.getAudioBaseType())
																   .setCodec_param(encodeCodec);
					connectAudioBundle.setChannels(new ArrayListWrapper<ConnectBO>().add(connectAudioChannel).getList());
					logic.getConnectBundle().add(connectAudioBundle);
				}
			}
		}
		
		if(LiveType.LOCAL_XT.equals(live.getType()) || LiveType.LOCAL_LOCAL.equals(live.getType())){
			//呼叫目的
			ConnectBundleBO connectDstVideoBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
					  													 .setOperateType(ConnectBundleBO.OPERATE_TYPE)
																		 .setLock_type("write")
																	     .setBundleId(live.getDstVideoBundleId())
																	     .setLayerId(live.getDstVideoLayerId())
																	     .setBundle_type(live.getDstVideoBundleType())
																	     .setDevice_model(live.getDstVideoDeviceModel());
			//接收转码
			CodecParamBO thisCodec = null;
			if(Boolean.TRUE.equals(dstVideoBundle.getTranscod())){
				List<ExtraInfoPO> extraInfos = extraInfoService.findByBundleId(dstVideoBundle.getBundleId());
				String dst_codec = extraInfoService.queryExtraInfoValueByName(extraInfos, "dst_codec");
				String resolution = extraInfoService.queryExtraInfoValueByName(extraInfos, "resolution");
				String fps = extraInfoService.queryExtraInfoValueByName(extraInfos, "fps");
				String gop_size = extraInfoService.queryExtraInfoValueByName(extraInfos, "gop_size");
				String video_bitrate = extraInfoService.queryExtraInfoValueByName(extraInfos, "video_bitrate");
				thisCodec = new CodecParamBO().copy(decodeCodec);
				VideoParamBO videoParam = thisCodec.getVideo_param();
				videoParam.setTranscode(true);
				if(dst_codec != null) videoParam.setCodec(dst_codec);
				if(resolution != null) videoParam.setResolution(resolution);
				if(fps != null) videoParam.setFps(fps);
				if(gop_size != null) videoParam.setGop_size(gop_size);
				if(video_bitrate != null) videoParam.setBitrate(video_bitrate);
			}
			if(thisCodec == null) thisCodec = decodeCodec;
			
			//对转码预览任务，添加udp_decode类型的passby。对210接入层不能带passby，否则会异常
			if(transcord){
				JSONObject pass_by_content = new JSONObject();
				JSONObject source = new JSONObject();
				source.put("bundle_id", live.getVideoBundleId());
				source.put("layer_id", live.getVideoLayerId());
				pass_by_content.put("source", source);
				pass_by_content.put("udpUrl", udpUrl);
				pass_by_content.put("video_param", decodeCodec.getVideo_param());//音频透传所以不写音频参数
				PassByBO passBy = new PassByBO()
						.setBundle_id(live.getDstVideoBundleId())
						.setLayer_id(live.getDstVideoLayerId())
						.setType("udp_decode")
						.setPass_by_content(pass_by_content);
				connectDstVideoBundle.setPass_by_str(passBy);
			}
			ConnectBO connectDstVideoChannel = new ConnectBO().setChannelId(live.getDstVideoChannelId())
														      .setChannel_status("Open")
														      .setBase_type(live.getDstVideoBaseType())
														      .setCodec_param(thisCodec);
			//收组播视频
			if(Boolean.TRUE.equals(videoBundle.getMulticastEncode())){
				String videoAddr = multicastService.addrAddPort(videoBundle.getMulticastEncodeAddr(), 2);
				connectDstVideoChannel.setMode(TransmissionMode.MULTICAST.getCode())
									.setMulti_addr(videoAddr)
									.setSrc_multi_ip(videoBundle.getMulticastSourceIp());
			}
			ForwardSetSrcBO videoForwardSetSrc = new ForwardSetSrcBO().setType("channel")
																 	  .setBundleId(live.getVideoBundleId())
																 	  .setLayerId(live.getVideoLayerId())
																 	  .setChannelId(live.getVideoChannelId());
			//设置osd内容
			//BundlePO videoBundle = bundleDao.findByBundleId(live.getVideoBundleId());
			connectDstVideoChannel.setOsds(monitorOsdService.protocol(osd, videoBundle.getUsername(), live.getVideoBundleName()));
			connectDstVideoChannel.setSource_param(videoForwardSetSrc);
			connectDstVideoBundle.setChannels(new ArrayListWrapper<ConnectBO>().add(connectDstVideoChannel).getList());
			logic.getConnectBundle().add(connectDstVideoBundle);
			
			if(live.getAudioChannelId()!=null && live.getDstAudioBundleId()!=null){
				if(live.getDstVideoBundleId().equals(live.getDstAudioBundleId())){
					ConnectBO connectDstAudioChannel = new ConnectBO().setChannelId(live.getDstAudioChannelId())
																      .setChannel_status("Open")
																      .setBase_type(live.getDstAudioBaseType())
																      .setCodec_param(thisCodec);
					//收组播音频
					if(Boolean.TRUE.equals(videoBundle.getMulticastEncode())){
						String audioAddr = multicastService.addrAddPort(videoBundle.getMulticastEncodeAddr(), 4);
						connectDstAudioChannel.setMode(TransmissionMode.MULTICAST.getCode())
											.setMulti_addr(audioAddr)
											.setSrc_multi_ip(videoBundle.getMulticastSourceIp());
					}
					ForwardSetSrcBO audioForwardSetSrc = new ForwardSetSrcBO().setType("channel")
						 	  												  .setBundleId(live.getAudioBundleId())
						 	  												  .setLayerId(live.getAudioLayerId())
						 	  												  .setChannelId(live.getAudioChannelId());
					connectDstAudioChannel.setSource_param(audioForwardSetSrc);
					connectDstVideoBundle.getChannels().add(connectDstAudioChannel);
					
				}else{
					ConnectBundleBO connectDstAudioBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
							  													 .setOperateType(ConnectBundleBO.OPERATE_TYPE)
																				 .setLock_type("write")
																			     .setBundleId(live.getDstAudioBundleId())
																			     .setLayerId(live.getDstAudioLayerId())
																			     .setBundle_type(live.getDstAudioBundleType())
																				 .setDevice_model(live.getDstAudioDeviceModel());
					ConnectBO connectDstAudioChannel = new ConnectBO().setChannelId(live.getDstAudioChannelId())
																      .setChannel_status("Open")
																      .setBase_type(live.getDstAudioBaseType())
																      .setCodec_param(encodeCodec);
					ForwardSetSrcBO audioForwardSetSrc = new ForwardSetSrcBO().setType("channel")
																			  .setBundleId(live.getAudioBundleId())
																			  .setLayerId(live.getAudioLayerId())
																			  .setChannelId(live.getAudioChannelId());
					connectDstAudioChannel.setSource_param(audioForwardSetSrc);
					connectDstAudioBundle.setChannels(new ArrayListWrapper<ConnectBO>().add(connectDstAudioChannel).getList());
					logic.getConnectBundle().add(connectDstAudioBundle);
				}
			}
		}
		
		return logic;
	}
	
	/**
	 * 清除字幕<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 下午3:08:15
	 * @param MonitorLiveDevicePO live 点播设备任务
	 * @return LogicBO 协议
	 */
	private LogicBO clearOsd(MonitorLiveDevicePO live, CodecParamBO codec) throws Exception{
		
		LogicBO logic = new LogicBO().setUserId(live.getUserId().toString())
				 		 			 .setConnectBundle(new ArrayList<ConnectBundleBO>());
		
		ConnectBundleBO connectDstVideoBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
														 .setLock_type("write")
													     .setBundleId(live.getDstVideoBundleId())
													     .setLayerId(live.getDstVideoLayerId())
													     .setBundle_type(live.getDstVideoBundleType());
		ConnectBO connectDstVideoChannel = new ConnectBO().setChannelId(live.getDstVideoChannelId())
													      .setChannel_status("Open")
													      .setBase_type(live.getDstVideoBaseType())
													      .setCodec_param(codec);
		/*ForwardSetSrcBO videoForwardSetSrc = new ForwardSetSrcBO().setType("channel")
												 	  .setBundleId(live.getVideoBundleId())
												 	  .setLayerId(live.getVideoLayerId())
												 	  .setChannelId(live.getVideoChannelId());
		
		connectDstVideoChannel.setSource_param(videoForwardSetSrc);*/
		connectDstVideoBundle.setChannels(new ArrayListWrapper<ConnectBO>().add(connectDstVideoChannel).getList());
		logic.getConnectBundle().add(connectDstVideoBundle);
		
		BundlePO videoBundle = bundleDao.findByBundleId(live.getVideoBundleId().indexOf("_")>=0?live.getVideoBundleId().split("_")[0]:live.getVideoBundleId());
		
		//先发清除字幕
		try{
				connectDstVideoChannel.setOsds(monitorOsdService.clearProtocol(videoBundle.getUsername(), live.getVideoBundleName()));
				executeBusiness.execute(logic, "点播系统：清除字幕");
		}catch (Exception e) {
			System.out.println();
		}
		return logic;
	}
	
	/**
	 * 处理closebundle协议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 下午3:13:37
	 * @param MonitorLiveDevicePO live 设备点播任务
	 * @param LiveType type 业务类型
	 * @return LogicBO 协议
	 */
	private LogicBO closeBundle(MonitorLiveDevicePO live) throws Exception{
		LogicBO logic = new LogicBO().setUserId(live.getUserId().toString())
									 .setDisconnectBundle(new ArrayList<DisconnectBundleBO>());
		if(LiveType.XT_LOCAL.equals(live.getType()) || LiveType.LOCAL_LOCAL.equals(live.getType())){
			DisconnectBundleBO disconnectVideoBundle = new DisconnectBundleBO().setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
																		       .setOperateType(DisconnectBundleBO.OPERATE_TYPE)
																			   .setBundleId(live.getVideoBundleId())
																			   .setBundle_type(live.getVideoBundleType())
																			   .setDevice_model(live.getVideoDeviceModel())
																			   .setLayerId(live.getVideoLayerId());
			
			logic.getDisconnectBundle().add(disconnectVideoBundle);
			
			if(live.getAudioBundleId()!=null && !live.getVideoBundleId().equals(live.getAudioBundleId())){
				DisconnectBundleBO disconnectAudioBundle = new DisconnectBundleBO().setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
																				   .setOperateType(DisconnectBundleBO.OPERATE_TYPE)
																				   .setBundleId(live.getAudioBundleId())
																				   .setBundle_type(live.getAudioBundleType())
																				   .setDevice_model(live.getAudioDeviceModel())
																				   .setLayerId(live.getAudioLayerId());
				logic.setDisconnectBundle(new ArrayListWrapper<DisconnectBundleBO>().add(disconnectAudioBundle).getList());
			}
		}
		
		if(LiveType.LOCAL_XT.equals(live.getType()) || LiveType.LOCAL_LOCAL.equals(live.getType())){
			DisconnectBundleBO disconnectDstVideoBundle = new DisconnectBundleBO().setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
																				  .setOperateType(DisconnectBundleBO.OPERATE_TYPE)
																				  .setBundleId(live.getDstVideoBundleId())
																			      .setBundle_type(live.getDstVideoBundleType())
																				  .setDevice_model(live.getDstVideoDeviceModel())
																			      .setLayerId(live.getDstVideoLayerId());
			//暂时不需要带passby
//			if(live.getUdpUrl() != null){
//				JSONObject pass_by_content = new JSONObject();
//				pass_by_content.put("udpUrl", live.getUdpUrl());
//				PassByBO passBy = new PassByBO()
//						.setBundle_id(live.getVideoBundleId())
//						.setLayer_id(live.getVideoLayerId())
//						.setType("udp_decode")
//						.setPass_by_content(pass_by_content);
//				disconnectDstVideoBundle.setPass_by_str(passBy);
//			}
			logic.getDisconnectBundle().add(disconnectDstVideoBundle);
			
			if(live.getAudioBundleId()!=null && live.getDstAudioBundleId()!=null && !live.getDstVideoBundleId().equals(live.getDstAudioBundleId())){
				DisconnectBundleBO disconnectDstAudioBundle = new DisconnectBundleBO().setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
																					  .setOperateType(DisconnectBundleBO.OPERATE_TYPE)
																					  .setBundleId(live.getDstAudioBundleId())
																				      .setBundle_type(live.getDstAudioBundleType())
																					  .setDevice_model(live.getDstAudioDeviceModel())
																				      .setLayerId(live.getDstAudioLayerId());
				logic.setDisconnectBundle(new ArrayListWrapper<DisconnectBundleBO>().add(disconnectDstAudioBundle).getList());
			}
		}
		return logic;
	}
	
	/**
	 * 停止转发重新开始<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月28日 上午11:37:29
	 * @param List<Long> idList 点播监控设备任务MonitorLiveDevicePO的主键集合
	 * @param userId 业务用户
	 * @throws Exception 
	 */
	public void stopToRestart(List<Long> idList, Long userId) throws Exception{
		
		if(isLock){
			synchronized (lockMonitorLiveDevice.intern()) {
				List<VedioCapacityPO> videoCapacityList = vedioCapacityService.findAll();
				if(videoCapacityList == null || videoCapacityList.size()==0 || videoCapacityList.get(0).getTurnCapacity() == null){
					throw new BaseException(StatusCode.FORBIDDEN, "没有查到转发容量");
				}
				
				Long TurnCapacity = videoCapacityList.get(0).getTurnCapacity();
				Long forwardCount = commandSystemQueryService.queryCountOfTransmit();
				Long idSize = (long) idList.size();
				if(TurnCapacity <= (forwardCount + idSize)){
					throw new BaseException(StatusCode.FORBIDDEN, "转发路数已经达到上限");
				}
				List<MonitorLiveDevicePO> liveList = monitorLiveDeviceDao.findAll(idList);
				
				String userName = userQuery.current().getUsername();
				for(MonitorLiveDevicePO live:liveList){
					try {
						if(live == null) continue;
						
						if(LiveType.LOCAL_XT.equals(live.getType())){
							authorize(live.getVideoBundleId().substring(0, live.getVideoBundleId().lastIndexOf("_")), 
									  live.getAudioBundleId().substring(0, live.getAudioBundleId().lastIndexOf("_")), 
									  userId);
						}else{
							authorize(live.getVideoBundleId(), live.getAudioBundleId(), userId);
						}
						
						//参数模板
						Map<String, Object> result = commons.queryDefaultAvCodec();
						AvtplPO targetAvtpl = (AvtplPO)result.get("avtpl");
						AvtplGearsPO targetGear = (AvtplGearsPO)result.get("gear");
						CodecParamBO playerCodec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
						CodecParamBO codec = playerCodec;
						
						//字幕
						MonitorOsdPO osd = monitorOsdDao.findOne(live.getOsdId()==null?0:live.getOsdId());
						
						//处理业务覆盖
//						coverBusiness(live.getDstVideoBundleId(), live.getDstVideoChannelId(), live.getDstAudioBundleId(), live.getDstAudioChannelId(), user.getId(), user.getUserno());
						
						//视频源和目的
						BundlePO videoBundle = bundleDao.findByBundleId(live.getVideoBundleId());
						BundlePO dstVideoBundle = bundleDao.findByBundleId(live.getDstVideoBundleId());
						if(LiveType.LOCAL_XT.equals(live.getType())){
							videoBundle = bundleDao.findByBundleId(live.getVideoBundleId().substring(0, live.getVideoBundleId().lastIndexOf("_")));
							dstVideoBundle = bundleDao.findByBundleId(live.getDstVideoBundleId());
						}
						
						live.setStatus(MonitorRecordStatus.RUN);
						
						monitorLiveDeviceDao.save(live);
						
						LogicBO logic = openBundle(live, codec, playerCodec, osd, videoBundle, dstVideoBundle, userId, false, live.getUdpUrl());
						
						executeBusiness.execute(logic, "点播系统：重新开始点播设备");
						
						operationLogService.send(userName, "开始转发转发", userName + "开始转发。" + live.getVideoBundleName() + " 转发给 " + live.getDstVideoBundleName());
					} catch (Exception e) {
						System.out.println("停止转发重新开始报错:");
						e.printStackTrace();
					}
				}
			}
		}else{
			List<MonitorLiveDevicePO> liveList = monitorLiveDeviceDao.findAll(idList);
			
			String userName = userQuery.current().getUsername();
			for(MonitorLiveDevicePO live:liveList){
				try {
					if(live == null) continue;
					
					if(LiveType.LOCAL_XT.equals(live.getType())){
						authorize(live.getVideoBundleId().substring(0, live.getVideoBundleId().lastIndexOf("_")), 
								  live.getAudioBundleId().substring(0, live.getAudioBundleId().lastIndexOf("_")), 
								  userId);
					}else{
						authorize(live.getVideoBundleId(), live.getAudioBundleId(), userId);
					}
					
					//参数模板
					Map<String, Object> result = commons.queryDefaultAvCodec();
					AvtplPO targetAvtpl = (AvtplPO)result.get("avtpl");
					AvtplGearsPO targetGear = (AvtplGearsPO)result.get("gear");
					CodecParamBO playerCodec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
					CodecParamBO codec = playerCodec;
					
					//字幕
					MonitorOsdPO osd = monitorOsdDao.findOne(live.getOsdId()==null?0:live.getOsdId());
					
					//处理业务覆盖
//					coverBusiness(live.getDstVideoBundleId(), live.getDstVideoChannelId(), live.getDstAudioBundleId(), live.getDstAudioChannelId(), user.getId(), user.getUserno());
					
					//视频源和目的
					BundlePO videoBundle = bundleDao.findByBundleId(live.getVideoBundleId());
					BundlePO dstVideoBundle = bundleDao.findByBundleId(live.getDstVideoBundleId());
					if(LiveType.LOCAL_XT.equals(live.getType())){
						videoBundle = bundleDao.findByBundleId(live.getVideoBundleId().substring(0, live.getVideoBundleId().lastIndexOf("_")));
						dstVideoBundle = bundleDao.findByBundleId(live.getDstVideoBundleId());
					}
					
					live.setStatus(MonitorRecordStatus.RUN);
					
					monitorLiveDeviceDao.save(live);
					
					LogicBO logic = openBundle(live, codec, playerCodec, osd, videoBundle, dstVideoBundle, userId, false, live.getUdpUrl());
					
					executeBusiness.execute(logic, "点播系统：重新开始点播设备");
					
					operationLogService.send(userName, "开始转发转发", userName + "开始转发。" + live.getVideoBundleName() + " 转发给 " + live.getDstVideoBundleName());
				} catch (Exception e) {
					System.out.println("停止转发重新开始报错:");
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 失去权限停止转发<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月12日 下午3:58:06
	 * @param userBundleBo
	 */
	@Transactional(rollbackFor = Exception.class)
	public void stopLiveByLosePrivilege(
			List<UserBundleBO> userBundleBoList,
			Long userId,
			String userNo) throws Exception {
		
		//这里可以再优化，但是会写的很复杂 //这里可以加一个功能，那些用户的哪些设备没有点播权限
		for(UserBundleBO userBundleBo : userBundleBoList){
			
			Map<String,String> bundleIdMap = resourceQueryUtil.queryUseableBundleIds(userBundleBo.getUserId(), new ArrayListWrapper<String>().add("DIANBO").getList(),Boolean.FALSE)
					 .stream().collect(Collectors.toMap(String::toString, Function.identity()));
			
			List<MonitorLiveDevicePO> monitorLiveDeviceList = monitorLiveDeviceDao.findByUserId(userBundleBo.getUserId());
			
			List<Long> needStopMonitorLiveDeviceIds = new ArrayList<Long>();
			
			List<Long> needDeleteMonitorLiveDeviceIds= monitorLiveDeviceList.stream().filter(monitorLiveDevice->{
				return bundleIdMap.get(monitorLiveDevice.getAudioBundleId()) == null ? true : false;
			}).map(monitorLiveDevice->{
				if(MonitorRecordStatus.RUN.equals(monitorLiveDevice.getStatus())){
					needStopMonitorLiveDeviceIds.add(monitorLiveDevice.getId());
				}
				return monitorLiveDevice;
			}).map(MonitorLiveDevicePO::getId).collect(Collectors.toList());
			
			if(needDeleteMonitorLiveDeviceIds.size()<=0){
				continue;
			}
			
			//处理屏幕墙
			List<LocationOfScreenWallPO> locationOfScreenWallPOList = locationOfScreenWallDao.findByMonitorLiveDeviceIdIn(needStopMonitorLiveDeviceIds);
			locationOfScreenWallPOList.stream().forEach(screenWall->{
			screenWall.setEncoderBundleId("");
			screenWall.setEncoderBundleName("");
			screenWall.setStatus(LocationExecuteStatus.STOP);
			});
			locationOfScreenWallDao.save(locationOfScreenWallPOList);
			
			stop(needStopMonitorLiveDeviceIds, userId, userNo, null);
			
			monitorLiveDeviceDao.deleteByIdIn(needDeleteMonitorLiveDeviceIds);
		}
		
	}

	/**
	 * 重置设备<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月12日 下午7:15:15
	 * @param bundleIds 设备bundleIds集合
	 */
	public void resetBundles(List<String> bundleIds, Long userId) throws Exception {
		
		List<DisconnectBundleBO> disconnectBundleBoList=bundleDao.findByBundleIdIn(bundleIds).stream().map(bundle->{
			DisconnectBundleBO disconnectVideoBundle = new DisconnectBundleBO().setBundleId(bundle.getBundleId())
																			   .setBundle_type(bundle.getBundleType())
																			   .setDevice_model(bundle.getDeviceModel())
																			   .setLayerId(bundle.getAccessNodeUid());
			return disconnectVideoBundle;
		}).collect(Collectors.toList());
		
		LogicBO logic = new LogicBO().setUserId(userId.toString())
				 .setDisconnectBundle(disconnectBundleBoList);
		
		executeBusiness.execute(logic, "重置设备");
	}
	
	/**
	 * 删除设备停止点播设备<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月19日 上午11:55:22
	 * @param bundleIdList 要删除的设备id
	 * @param userId 业务人员id
	 * @param userNo 业务人员no
	 * @throws Exception 
	 */
	@Transactional(rollbackFor = Exception.class )
	public void stopLiveDeviceByDeleteDevice(
			List<String> bundleIdList,
			Long userId,
			String userNo) throws Exception{
		
		//找到转发
		List<MonitorLiveDevicePO> monitorLiveDeviceList = monitorLiveDeviceDao.findByVideoBundleIdInOrDstVideoBundleIdIn(bundleIdList, bundleIdList);
		List<Long> needStopMonitorLiveDeviceIds = new ArrayList<Long>();
		//如果删除的设备是解码器还需要删除屏幕墙
		List<Long> needDeleteScreenWall = new ArrayList<Long>();
		
		List<Long> needDeleteMonitorLiveDeviceIds= monitorLiveDeviceList.stream().map(monitorLiveDevice->{
			if(MonitorRecordStatus.RUN.equals(monitorLiveDevice.getStatus())){
				needStopMonitorLiveDeviceIds.add(monitorLiveDevice.getId());
			}if(bundleIdList.contains(monitorLiveDevice.getDstVideoBundleId())){
				needDeleteScreenWall.add(monitorLiveDevice.getId());
			}
			return monitorLiveDevice;
		}).map(MonitorLiveDevicePO::getId).collect(Collectors.toList());
		
		if(needDeleteMonitorLiveDeviceIds.size()<=0){
			return;
		}
		
		//处理屏幕墙
		List<LocationOfScreenWallPO> locationOfScreenWallPOList = locationOfScreenWallDao.findByMonitorLiveDeviceIdIn(needStopMonitorLiveDeviceIds);
		locationOfScreenWallPOList.stream().forEach(screenWall->{
		screenWall.setEncoderBundleId("");
		screenWall.setEncoderBundleName("");
		screenWall.setStatus(LocationExecuteStatus.STOP);
		});
		locationOfScreenWallDao.save(locationOfScreenWallPOList);
		
		stop(needStopMonitorLiveDeviceIds, userId, userNo, null);
		
		monitorLiveDeviceDao.deleteByIdIn(needDeleteMonitorLiveDeviceIds);
		locationOfScreenWallDao.deleteByMonitorLiveDeviceIdIn(needDeleteScreenWall);
	}

	/**
	 * 为了测试需要批量停止或者开始<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月12日 下午7:23:10
	 * @param allStart true将所有的转发开始，stop将所有转发停止
	 */
	public void allStartOrStop(Boolean allStart, Long userId, String userno) throws Exception{
		
		if(allStart != null && allStart){
			List<Long> liveIds = monitorLiveDeviceDao.findByStatus(MonitorRecordStatus.STOP).stream().map(MonitorLiveDevicePO::getId).collect(Collectors.toList());
			stopToRestart(liveIds, userId);
		}else if(allStart != null && !allStart){
			List<Long> liveIds = monitorLiveDeviceDao.findByStatus(MonitorRecordStatus.RUN).stream().map(MonitorLiveDevicePO::getId).collect(Collectors.toList());
			stop(liveIds, userId, userno, Boolean.TRUE);
		}
	}
}

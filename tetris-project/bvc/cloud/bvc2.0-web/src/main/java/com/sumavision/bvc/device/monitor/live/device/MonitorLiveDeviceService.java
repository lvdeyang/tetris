package com.sumavision.bvc.device.monitor.live.device;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.constant.BusinessConstants.BUSINESS_OPR_TYPE;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.ConnectBO;
import com.sumavision.bvc.device.group.bo.ConnectBundleBO;
import com.sumavision.bvc.device.group.bo.DisconnectBundleBO;
import com.sumavision.bvc.device.group.bo.ForwardSetSrcBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.PassByBO;
import com.sumavision.bvc.device.group.bo.XtBusinessPassByContentBO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplPO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
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
import com.sumavision.bvc.system.dao.AVtplGearsDAO;
import com.sumavision.bvc.system.dao.AvtplDAO;
import com.sumavision.bvc.system.po.AvtplGearsPO;
import com.sumavision.bvc.system.po.AvtplPO;
import com.sumavision.tetris.auth.token.TerminalType;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

/**
 * 点播设备业务<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年6月19日 下午3:32:02
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MonitorLiveDeviceService {
	
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
	private BundleDao bundleDao;
	
	@Autowired
	private AvtplDAO avtplDao;
	
	@Autowired
	private AVtplGearsDAO avtplGearsDao;
	
	@Autowired
	private MonitorLiveCommons commons;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;

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
		
		//参数校验
		isVideoBundleNotNull(videoBundleId, "internal");
		regularizedAudioParams(audioBundleId, audioChannelId, "internal", "internal");
		authorize(videoBundleId, audioBundleId, userId);
		
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
		
		LogicBO logic = openBundle(live, codec, osd, null, null, userId);
		
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
		
		executeBusiness.execute(logic, "点播系统：xt点播本地设备");
		
		return live;
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
		
		
		LogicBO logic = openBundle(live, codec, playerCodec, osd, videoBundle, dstVideoBundle, userId, transcord, udpUrl);
		
		executeBusiness.execute(logic, "点播系统：本地设备点播本地设备");
		
		return live;
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
		
		executeBusiness.execute(logic, "点播系统：本地点播xt设备");
		
		return live;
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
		if(userId.longValue() == 1l){
			//admin操作转换
			userId = live.getUserId();
			UserBO user = resourceService.queryUserById(userId, TerminalType.PC_PLATFORM);
			userno = user.getUserNo();
		}
		if(!live.getUserId().equals(userId)){
			throw new UserHasNoPermissionToRemoveLiveDeviceException(userId, liveId);
		}
		if(LiveType.XT_LOCAL.equals(live.getType())){
			stopXtSeeLocal(live, userId, userno);
		}else if(LiveType.LOCAL_XT.equals(live.getType())){
			stopLocalSeeXt(live, userId, userno);
		}else if(LiveType.LOCAL_LOCAL.equals(live.getType())){
			stopLocalSeeLocal(live, userId, userno);
		}else if(LiveType.XT_XT.equals(live.getType())){
			stopXtSeeXt(live, userId, userno);
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
			stopXtSeeLocal(live, userId, userno);
		}else if(LiveType.LOCAL_XT.equals(live.getType())){
			stopLocalSeeXt(live, userId, userno);
		}else if(LiveType.LOCAL_LOCAL.equals(live.getType())){
			stopLocalSeeLocal(live, userId, userno);
		}else if(LiveType.XT_XT.equals(live.getType())){
			stopXtSeeXt(live, userId, userno);
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
	public void stopXtSeeLocal(MonitorLiveDevicePO live, Long userId, String userno) throws Exception{
		
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
		
		monitorLiveDeviceDao.delete(live);
		
		executeBusiness.execute(logic, "点播系统：停止xt点播本地设备任务");
		
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
	public void stopLocalSeeLocal(MonitorLiveDevicePO live, Long userId, String userno) throws Exception{
		
		AvtplPO targetAvtpl = avtplDao.findOne(live.getAvTplId());
		AvtplGearsPO targetGear = avtplGearsDao.findOne(live.getGearId());
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
		
		if(live.getUdpUrl() == null){
			clearOsd(live, codec);
		}
		
		LogicBO logic = closeBundle(live);
		
		monitorLiveDeviceDao.delete(live);
		
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
	public void stopLocalSeeXt(MonitorLiveDevicePO live, Long userId, String userno) throws Exception{
		
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
		
		monitorLiveDeviceDao.delete(live);
		
		executeBusiness.execute(logic, "点播系统：停止本地点播xt设备");
		
	}
	
	/**
	 * 停止xt看xt设备，这个暂时不实现<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 下午2:40:01
	 */
	public void stopXtSeeXt(MonitorLiveDevicePO live, Long userId, String userno) throws Exception{
		
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
				stop(deviceLive.getId(), userId, userno);
			}
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
	 * @param BundlePO videoBundle 视频源设备
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
			connectVideoBundle.setChannels(new ArrayListWrapper<ConnectBO>().add(connectVideoChannel).getList());
			
			logic.getConnectBundle().add(connectVideoBundle);
			
			if(live.getAudioBundleId() != null){
				if(live.getVideoBundleId().equals(live.getAudioBundleId())){
					ConnectBO connectAudioChannel = new ConnectBO().setChannelId(live.getAudioChannelId())
																   .setChannel_status("Open")
																   .setBase_type(live.getAudioBaseType())
																   .setCodec_param(encodeCodec);
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
														      .setCodec_param(decodeCodec);
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
																      .setCodec_param(decodeCodec);
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
		connectDstVideoChannel.setOsds(monitorOsdService.clearProtocol(videoBundle.getUsername(), live.getVideoBundleName()));
		executeBusiness.execute(logic, "点播系统：清除字幕");
		
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
	
}

package com.sumavision.bvc.device.monitor.live.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.constant.BusinessConstants.BUSINESS_OPR_TYPE;
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
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplPO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.CommonQueryUtil;
import com.sumavision.bvc.device.monitor.exception.UserHasNoPermissionToRemoveLiveUserException;
import com.sumavision.bvc.device.monitor.live.DstDeviceType;
import com.sumavision.bvc.device.monitor.live.LiveType;
import com.sumavision.bvc.device.monitor.live.MonitorLiveCommons;
import com.sumavision.bvc.device.monitor.live.device.MonitorLiveDeviceDAO;
import com.sumavision.bvc.device.monitor.live.device.MonitorLiveDevicePO;
import com.sumavision.bvc.device.monitor.live.device.MonitorLiveDeviceService;
import com.sumavision.bvc.device.monitor.live.exception.UserCannotBeFoundException;
import com.sumavision.bvc.device.monitor.live.exception.UserEncoderCannotBeFoundException;
import com.sumavision.bvc.device.monitor.live.exception.UserHasNoPermissionForBusinessException;
import com.sumavision.bvc.device.monitor.playback.MonitorRecordPlaybackTaskDAO;
import com.sumavision.bvc.device.monitor.playback.MonitorRecordPlaybackTaskPO;
import com.sumavision.bvc.device.monitor.playback.MonitorRecordPlaybackTaskService;
import com.sumavision.bvc.feign.ResourceServiceClient;
import com.sumavision.bvc.resource.dao.ResourceBundleDAO;
import com.sumavision.bvc.resource.dao.ResourceChannelDAO;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.bvc.system.dao.AVtplGearsDAO;
import com.sumavision.bvc.system.dao.AvtplDAO;
import com.sumavision.bvc.system.po.AvtplGearsPO;
import com.sumavision.bvc.system.po.AvtplPO;
import com.sumavision.tetris.auth.token.TerminalType;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;

/**
 * 点播用户业务<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年6月19日 下午4:05:00
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MonitorLiveUserService {

	@Autowired
	private AvtplDAO avtplDao;
	
	@Autowired
	private AVtplGearsDAO avtplGearsDao;
	
	@Autowired
	private MonitorLiveUserDAO monitorLiveUserDao;
	
	@Autowired
	private MonitorLiveDeviceDAO monitorLiveDeviceDao;
	
	@Autowired
	private MonitorLiveDeviceService monitorLiveDeviceService;
	
	@Autowired
	private MonitorRecordPlaybackTaskDAO monitorRecordPlaybackTaskDao;
	
	@Autowired
	private MonitorRecordPlaybackTaskService monitorRecordPlaybackTaskService;
	
	@Autowired
	private ResourceBundleDAO resourceBundleDao;
	
	@Autowired
	private ResourceChannelDAO resourceChannelDao;
	
	@Autowired
	private MonitorLiveCommons commons;
	
	@Autowired
	private CommonQueryUtil commonQueryUtil;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	@Autowired
	private ResourceServiceClient resourceServiceClient;
	
	/**
	 * xt点播本地用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 下午4:37:25
	 * @param String uuid 既定uuid
	 * @param UserBO user 本地用户
	 * @param Long userId 当前操作业务用户
	 * @param String username 业务用户名
	 * @param String userno 业务用户号码
	 * @return MonitorLiveUserPO 点播用户任务
	 */
	public MonitorLiveUserPO startXtSeeLocal(
			String uuid,
			UserBO user,
			Long userId,
			String username,
			String userno) throws Exception{
		
		if(user == null) throw new UserCannotBeFoundException();
//		String encoderId = resourceQueryUtil.queryEncodeBundleIdByUserId(user.getId());
		String encoderId = commonQueryUtil.queryExternalOrLocalEncoderIdFromUserBO(user);
		if(encoderId == null) throw new UserEncoderCannotBeFoundException();
//		authorize(user.getId(), userId);//TODO: 暂时注释
		
		//参数模板
		Map<String, Object> result = commons.queryDefaultAvCodec();
		AvtplPO targetAvtpl = (AvtplPO)result.get("avtpl");
		AvtplGearsPO targetGear = (AvtplGearsPO)result.get("gear");
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
				
		//联网layerId
		String networkLayerId = commons.queryNetworkLayerId();
		
		//本地用户绑定编码器
		List<BundlePO> srcBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(encoderId).getList());
		BundlePO srcBundleEntity = srcBundleEntities.get(0);
		
		List<ChannelSchemeDTO> srcVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(srcBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_VIDEO);
		ChannelSchemeDTO srcVideoChannel = srcVideoChannels.get(0);
		
		List<ChannelSchemeDTO> srcAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(srcBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_AUDIO);
		ChannelSchemeDTO srcAudioChannel = srcAudioChannels.get(0);
		
		MonitorLiveUserPO live = new MonitorLiveUserPO(
				user.getUserNo(), user.getName(),
				srcBundleEntity.getBundleId(), srcBundleEntity.getBundleName(), srcBundleEntity.getBundleType(), srcBundleEntity.getAccessNodeUid(),
				srcVideoChannel.getChannelId(), srcVideoChannel.getBaseType(), srcAudioChannel.getChannelId(), srcAudioChannel.getBaseType(),
				null, null, null, null, null, null,
				null, null, null, null, null, null,
				userId, username,
				targetAvtpl.getId(),
				targetGear.getId(),
				null,
				LiveType.XT_LOCAL);
		
		if(uuid != null) live.setUuid(uuid);
		
		monitorLiveUserDao.save(live);
		
		LogicBO logic = openBundle(live, codec, userId);
		
		logic.setPass_by(new ArrayList<PassByBO>());
		
		XtBusinessPassByContentBO passByContent = new XtBusinessPassByContentBO().setCmd(XtBusinessPassByContentBO.CMD_XT_SEE_LOCAL_USER)
																				 .setOperate(XtBusinessPassByContentBO.OPERATE_START)
																				 .setUuid(live.getUuid())
																				 .setSrc_user(userno)
																				 .setLocal_encoder(new HashMapWrapper<String, String>().put("layerid", live.getLayerId())
																						 											   .put("bundleid", live.getBundleId())
																						 											   .put("video_channelid", live.getVideoChannelId())
																						 											   .put("audio_channelid", live.getAudioChannelId())
																						 											   .getMap())
																				 .setDst_number(user.getUserNo())
																				 .setVparam(codec);
		
		PassByBO passby = new PassByBO().setLayer_id(networkLayerId)
										.setType(XtBusinessPassByContentBO.CMD_XT_SEE_LOCAL_USER)
										.setPass_by_content(passByContent);
		
		logic.getPass_by().add(passby);
		
		resourceServiceClient.coverLianwangPassby(
				live.getUuid(), 
				networkLayerId, 
				XtBusinessPassByContentBO.CMD_XT_SEE_LOCAL_USER, 
				JSON.toJSONString(passby));
		
		executeBusiness.execute(logic, "点播系统：xt点播本地用户 " + live.getSrcUsername());
		
		return live;
	}
	
	/**
	 * 本地点播本地用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 下午4:37:25
	 * @param UserBO user 本地用户
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
	 * @param Long userId 当前操作业务用户
	 * @param String username 业务用户名
	 * @param String userno 业务用户号码
	 * @return MonitorLiveUserPO 点播用户任务
	 */
	public MonitorLiveUserPO startLocalSeeLocal(
			UserBO user,
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
			String username,
			String userno) throws Exception{
		
		if(user == null) throw new UserCannotBeFoundException();
//		String encoderId = resourceQueryUtil.queryEncodeBundleIdByUserId(user.getId());
		String encoderId = commonQueryUtil.queryExternalOrLocalEncoderIdFromUserBO(user);
		if(encoderId == null) throw new UserEncoderCannotBeFoundException();
		authorize(user.getId(), userId);
		
		//参数模板
		Map<String, Object> result = commons.queryDefaultAvCodec();
		AvtplPO targetAvtpl = (AvtplPO)result.get("avtpl");
		AvtplGearsPO targetGear = (AvtplGearsPO)result.get("gear");
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
		
		//本地用户绑定编码器
		List<BundlePO> srcBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(encoderId).getList());
		BundlePO srcBundleEntity = srcBundleEntities.get(0);
		
		List<ChannelSchemeDTO> srcVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(srcBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_VIDEO);
		ChannelSchemeDTO srcVideoChannel = srcVideoChannels.get(0);
		
		List<ChannelSchemeDTO> srcAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(srcBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_AUDIO);
		ChannelSchemeDTO srcAudioChannel = srcAudioChannels.get(0);
		
		//处理覆盖业务
		coverBusiness(dstVideoBundleId, dstVideoChannelId, dstAudioBundleId, dstAudioChannelId, userId, userno);
		
		MonitorLiveUserPO live = new MonitorLiveUserPO(
				user.getUserNo(), user.getName(),
				srcBundleEntity.getBundleId(), srcBundleEntity.getBundleName(), srcBundleEntity.getBundleType(), srcBundleEntity.getAccessNodeUid(),
				srcVideoChannel.getChannelId(), srcVideoChannel.getBaseType(), srcAudioChannel.getChannelId(), srcAudioChannel.getBaseType(),
				dstVideoBundleId, dstVideoBundleName, dstVideoBundleType, dstVideoLayerId, dstVideoChannelId, dstVideoBaseType,
				dstAudioBundleId, dstAudioBundleName, dstAudioBundleType, dstAudioLayerId, dstAudioChannelId, dstAudioBaseType,
				userId, username,
				targetAvtpl.getId(),
				targetGear.getId(),
				DstDeviceType.valueOf(dstDeviceType),
				LiveType.LOCAL_LOCAL);
		
		monitorLiveUserDao.save(live);
		
		LogicBO logic = openBundle(live, codec, userId);
		
		executeBusiness.execute(logic, "点播系统：本地点播本地用户");
		
		return live;
	}

	/**
	 * 本地点播xt用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 下午4:37:25
	 * @param UserBO user xt用户
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
	 * @param Long userId 当前操作业务用户
	 * @param String username 业务用户名
	 * @param String userno 业务用户号码
	 * @return MonitorLiveUserPO 点播用户任务
	 */
	public MonitorLiveUserPO startLocalSeeXt(
			UserBO user,
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
			String username,
			String userno) throws Exception{
		
		if(user == null) throw new UserCannotBeFoundException();
		//if(user.getEncoderId() == null) throw new UserEncoderCannotBeFoundException();
		authorize(user.getId(), userId);
		
		//参数模板
		Map<String, Object> result = commons.queryDefaultAvCodec();
		AvtplPO targetAvtpl = (AvtplPO)result.get("avtpl");
		AvtplGearsPO targetGear = (AvtplGearsPO)result.get("gear");
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
				
		//联网layerId
		String networkLayerId = commons.queryNetworkLayerId();
		
		//xt用户绑定编码器
		/*List<BundlePO> srcBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(user.getEncoderId()).getList());
		BundlePO srcBundleEntity = srcBundleEntities.get(0);
		
		List<ChannelSchemeDTO> srcVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(srcBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_VIDEO);
		ChannelSchemeDTO srcVideoChannel = srcVideoChannels.get(0);
		
		List<ChannelSchemeDTO> srcAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(srcBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_AUDIO);
		ChannelSchemeDTO srcAudioChannel = srcAudioChannels.get(0);*/
		
		//xt用户虚拟编码器
		String bundleId = UUID.randomUUID().toString().replaceAll("-", "");
		String videoChannelId = ChannelType.VIDEOENCODE1.getChannelId();
		String audioChannelId = ChannelType.AUDIOENCODE1.getChannelId();
		
		//处理覆盖业务
		coverBusiness(dstVideoBundleId, dstVideoChannelId, dstAudioBundleId, dstAudioChannelId, userId, userno);
		
		/*MonitorLiveUserPO live = new MonitorLiveUserPO(
				user.getUserNo(), user.getName(),
				srcBundleEntity.getBundleId(), srcBundleEntity.getBundleName(), srcBundleEntity.getBundleType(), networkLayerId,
				srcVideoChannel.getChannelId(), srcVideoChannel.getBaseType(), srcAudioChannel.getChannelId(), srcAudioChannel.getBaseType(),
				dstVideoBundleId, dstVideoBundleName, dstVideoBundleType, dstVideoLayerId, dstVideoChannelId, dstVideoBaseType,
				dstAudioBundleId, dstAudioBundleName, dstAudioBundleType, dstAudioLayerId, dstAudioChannelId, dstAudioBaseType,
				userId, username,
				targetAvtpl.getId(),
				targetGear.getId(),
				DstDeviceType.valueOf(dstDeviceType),
				LiveType.LOCAL_XT);*/
		
		MonitorLiveUserPO live = new MonitorLiveUserPO(
				user.getUserNo(), user.getName(),
				bundleId, "虚拟xt编码设备", null, networkLayerId,
				videoChannelId, null, audioChannelId, null,
				dstVideoBundleId, dstVideoBundleName, dstVideoBundleType, dstVideoLayerId, dstVideoChannelId, dstVideoBaseType,
				dstAudioBundleId, dstAudioBundleName, dstAudioBundleType, dstAudioLayerId, dstAudioChannelId, dstAudioBaseType,
				userId, username,
				targetAvtpl.getId(),
				targetGear.getId(),
				DstDeviceType.valueOf(dstDeviceType),
				LiveType.LOCAL_XT);
		
		monitorLiveUserDao.save(live);
		
		LogicBO logic = openBundle(live, codec, userId);
		
		logic.setPass_by(new ArrayList<PassByBO>());
		
		XtBusinessPassByContentBO passByContent = new XtBusinessPassByContentBO().setCmd(XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_USER)
																				 .setOperate(XtBusinessPassByContentBO.OPERATE_START)
																				 .setUuid(live.getUuid())
																				 .setSrc_user(userno)
																				 .setXt_encoder(new HashMapWrapper<String, String>().put("layerid", networkLayerId)
																						 											.put("bundleid", bundleId)
																						 											.put("video_channelid", videoChannelId)
																						 											.put("audio_channelid", audioChannelId)
																						 											.getMap())
																				 .setDst_number(user.getUserNo())
																				 .setVparam(codec);
		
		PassByBO passby = new PassByBO().setLayer_id(networkLayerId)
										.setType(XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_USER)
										.setPass_by_content(passByContent);
		
		logic.getPass_by().add(passby);
		
		resourceServiceClient.coverLianwangPassby(
				live.getUuid(), 
				networkLayerId, 
				XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_USER, 
				JSON.toJSONString(passby));
		
		executeBusiness.execute(logic, "点播系统：本地点播xt用户");
		
		return live;
		
	}
	
	/**
	 * xt点播xt用户，暂时不实现<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 下午5:50:57
	 * @return MonitorLiveUserPO 点播用户任务
	 */
	public MonitorLiveUserPO startXtSeeXt() throws Exception{
		return null;
	}
	
	/**
	 * 停止当前用户的播放器点播用户任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月24日 下午1:45:03
	 * @param Collection<String> webPlayerBundleIds 播放器设备id列表
	 * @param Long userId 当前业务用户id
	 * @param String userno 当前业务用户号码
	 */
	public void stopWebPlayerLives(Collection<String> webPlayerBundleIds, Long userId, String userno) throws Exception{
		List<MonitorLiveUserPO> lives = monitorLiveUserDao.findByDstVideoBundleIdIn(webPlayerBundleIds);
		if(lives!=null && lives.size()>0){
			for(MonitorLiveUserPO live:lives){
				stop(live.getId(), userId, userno);
			}
		}
	}
	
	/**
	 * 停止点播用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月20日 上午8:44:10
	 * @param Long liveId 点播任务id
	 * @param Long userId 发起业务用户id
	 * @param String userno 发起业务用户号码
	 */
	public void stop(Long liveId, Long userId, String userno) throws Exception{
		MonitorLiveUserPO live = monitorLiveUserDao.findOne(liveId);
		if(live == null) return;
		if(userId.longValue() == 1l){
			//admin操作转换
			userId = live.getUserId();
			UserBO user = resourceService.queryUserById(userId, TerminalType.PC_PLATFORM);
			userno = user.getUserNo();
		}
		if(!userId.equals(live.getUserId())){
			throw new UserHasNoPermissionToRemoveLiveUserException(userId, liveId);
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
	 * 停止点播用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月20日 上午8:46:53
	 * @param String liveUuid 点播任务uuid
	 * @param Long userId 发起业务用户id
	 * @param String userno 发起业务用户号码
	 */
	public void stop(String liveUuid, Long userId, String userno) throws Exception{
		MonitorLiveUserPO live = monitorLiveUserDao.findByUuid(liveUuid);
		if(live == null) return;
		if(userId.longValue() == 1l){
			//admin操作转换
			userId = live.getUserId();
			UserBO user = resourceService.queryUserById(userId, TerminalType.PC_PLATFORM);
			userno = user.getUserNo();
		}
		if(!userId.equals(live.getUserId())){
			throw new UserHasNoPermissionToRemoveLiveUserException(userId, liveUuid);
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
	 * 停止xt点播本地用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月20日 上午8:57:51
	 * @param MonitorLiveUserPO live xt点播本地用户任务
	 * @param Long userId 发起任务用户id
	 * @param String userno 发起任务用户号码
	 */
	public void stopXtSeeLocal(MonitorLiveUserPO live, Long userId, String userno) throws Exception{
		
		AvtplPO targetAvtpl = avtplDao.findOne(live.getAvTplId());
		AvtplGearsPO targetGear = avtplGearsDao.findOne(live.getGearId());
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
		
		String networkLayerId = commons.queryNetworkLayerId();
		
		LogicBO logic = closeBundle(live);
		
		logic.setPass_by(new ArrayList<PassByBO>());
		
		XtBusinessPassByContentBO passByContent = new XtBusinessPassByContentBO().setCmd(XtBusinessPassByContentBO.CMD_XT_SEE_LOCAL_USER)
																				 .setOperate(XtBusinessPassByContentBO.OPERATE_STOP)
																				 .setUuid(live.getUuid())
																				 .setSrc_user(userno)
																				 .setLocal_encoder(new HashMapWrapper<String, String>().put("layerid", live.getLayerId())
																						 											   .put("bundleid", live.getBundleId())
																						 											   .put("video_channelid", live.getVideoChannelId())
																						 											   .put("audio_channelid", live.getAudioChannelId())
																						 											   .getMap())
																				 .setDst_number(live.getSrcUserno())
																				 .setVparam(codec);
		
		PassByBO passby = new PassByBO().setLayer_id(networkLayerId)
										.setType(XtBusinessPassByContentBO.CMD_XT_SEE_LOCAL_USER)
										.setPass_by_content(passByContent);
		
		logic.getPass_by().add(passby);
		
		monitorLiveUserDao.delete(live);
		
		resourceServiceClient.removeLianwangPassby(live.getUuid());
		
		executeBusiness.execute(logic, "点播系统：停止xt点播本地用户 " + live.getSrcUsername());
	}
	
	
	/**
	 * 停止本地点播本地用户任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月20日 上午9:01:19
	 * @param MonitorLiveUserPO live 本地点播本地用户任务
	 * @param Long userId 发起任务用户id
	 * @param String userno 发起任务用户号码
	 */
	public void stopLocalSeeLocal(MonitorLiveUserPO live, Long userId, String userno) throws Exception{
		
		LogicBO logic = closeBundle(live);
		
		monitorLiveUserDao.delete(live);
		
		executeBusiness.execute(logic, "点播系统：停止本地点播本地用户");
	}
	
	
	/**
	 * 停止本地点播xt用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月20日 上午9:05:48
	 * @param MonitorLiveUserPO live 本地点播xt用户
	 * @param Long userId 发起任务用户id
	 * @param String userno 发起任务用户号码
	 */
	public void stopLocalSeeXt(MonitorLiveUserPO live, Long userId, String userno) throws Exception{
		
		AvtplPO targetAvtpl = avtplDao.findOne(live.getAvTplId());
		AvtplGearsPO targetGear = avtplGearsDao.findOne(live.getGearId());
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
		
		String networkLayerId = commons.queryNetworkLayerId();
		
		LogicBO logic = closeBundle(live);
		
		logic.setPass_by(new ArrayList<PassByBO>());
		
		XtBusinessPassByContentBO passByContent = new XtBusinessPassByContentBO().setCmd(XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_USER)
																				 .setOperate(XtBusinessPassByContentBO.OPERATE_STOP)
																				 .setUuid(live.getUuid())
																				 .setSrc_user(userno)
																				 .setXt_encoder(new HashMapWrapper<String, String>().put("layerid", networkLayerId)
																						 											.put("bundleid", live.getBundleId())
																						 											.put("video_channelid", live.getVideoChannelId())
																						 											.put("audio_channelid", live.getAudioChannelId())
																						 											.getMap())
																				 .setDst_number(live.getSrcUserno())
																				 .setVparam(codec);
		
		PassByBO passby = new PassByBO().setLayer_id(networkLayerId)
										.setType(XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_USER)
										.setPass_by_content(passByContent);
		
		logic.getPass_by().add(passby);
		
		monitorLiveUserDao.delete(live);
		
		resourceServiceClient.removeLianwangPassby(live.getUuid());
		
		executeBusiness.execute(logic, "点播系统：停止本地点播xt用户");
	}
	
	
	/**
	 * 停止xt点播xt用户，暂时不实现<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 下午5:50:57
	 */
	public void stopXtSeeXt(MonitorLiveUserPO live, Long userId, String userno) throws Exception{
		
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
				monitorLiveDeviceService.stop(deviceLive.getId(), userId, userno);
			}
		}
		
		List<MonitorLiveUserPO> userLives = monitorLiveUserDao.findByDstVideoBundleIdAndDstVideoChannelIdAndDstAudioBundleIdAndDstAudioChannelId(dstVideoBundleId, dstVideoChannelId, dstAudioBundleId, dstAudioChannelId);
		if(userLives!=null && userLives.size()>0){
			for(MonitorLiveUserPO userLive:userLives){
				stop(userLive.getId(), userId, userno);
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
	 * @param MonitorLiveUserPO live 点播用户任务
	 * @param CodecParamBO codec 参数模板
	 * @param Long userId 业务用户
	 * @return LogicBO 协议数据
	 */
	private LogicBO openBundle(
			MonitorLiveUserPO live,
			CodecParamBO codec,
			Long userId) throws Exception{
		
		//呼叫设备
		LogicBO logic = new LogicBO().setUserId(userId.toString())
				 			 		 .setConnectBundle(new ArrayList<ConnectBundleBO>());
		
		if(LiveType.XT_LOCAL.equals(live.getType()) || LiveType.LOCAL_LOCAL.equals(live.getType())){
			//呼叫源--这里面先认为源和目的设备不一样
			ConnectBundleBO connectVideoBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
															          .setOperateType(ConnectBundleBO.OPERATE_TYPE)
																	  .setLock_type("write")
																	  .setBundleId(live.getBundleId())
																	  .setLayerId(live.getLayerId())
																	  .setBundle_type(live.getBundleType());
			ConnectBO connectVideoChannel = new ConnectBO().setChannelId(live.getVideoChannelId())
														   .setChannel_status("Open")
														   .setBase_type(live.getVideoBaseType())
														   .setCodec_param(codec);
			ConnectBO connectAudioChannel = new ConnectBO().setChannelId(live.getAudioChannelId())
														   .setChannel_status("Open")
														   .setBase_type(live.getAudioBaseType())
														   .setCodec_param(codec);
			connectVideoBundle.setChannels(new ArrayListWrapper<ConnectBO>().add(connectVideoChannel).add(connectAudioChannel).getList());
			logic.getConnectBundle().add(connectVideoBundle);
		}
		
		if(LiveType.LOCAL_XT.equals(live.getType()) || LiveType.LOCAL_LOCAL.equals(live.getType())){
			//呼叫目的
			ConnectBundleBO connectDstVideoBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
					  													 .setOperateType(ConnectBundleBO.OPERATE_TYPE)
																		 .setLock_type("write")
																	     .setBundleId(live.getDstVideoBundleId())
																	     .setLayerId(live.getDstVideoLayerId())
																	     .setBundle_type(live.getDstVideoBundleType());
			ConnectBO connectDstVideoChannel = new ConnectBO().setChannelId(live.getDstVideoChannelId())
														      .setChannel_status("Open")
														      .setBase_type(live.getDstVideoBaseType())
														      .setCodec_param(codec);
			ForwardSetSrcBO videoForwardSetSrc = new ForwardSetSrcBO().setType("channel")
																 	  .setBundleId(live.getBundleId())
																 	  .setLayerId(live.getLayerId())
																 	  .setChannelId(live.getVideoChannelId());
			connectDstVideoChannel.setSource_param(videoForwardSetSrc);
			connectDstVideoBundle.setChannels(new ArrayListWrapper<ConnectBO>().add(connectDstVideoChannel).getList());
			logic.getConnectBundle().add(connectDstVideoBundle);
			
			if(live.getAudioChannelId()!=null && live.getDstAudioBundleId()!=null){
				if(live.getDstVideoBundleId().equals(live.getDstAudioBundleId())){
					ConnectBO connectDstAudioChannel = new ConnectBO().setChannelId(live.getDstAudioChannelId())
																      .setChannel_status("Open")
																      .setBase_type(live.getDstAudioBaseType())
																      .setCodec_param(codec);
					ForwardSetSrcBO audioForwardSetSrc = new ForwardSetSrcBO().setType("channel")
						 	  												  .setBundleId(live.getBundleId())
						 	  												  .setLayerId(live.getLayerId())
						 	  												  .setChannelId(live.getAudioChannelId());
					connectDstAudioChannel.setSource_param(audioForwardSetSrc);
					connectDstVideoBundle.getChannels().add(connectDstAudioChannel);
					
				}else{
					ConnectBundleBO connectDstAudioBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
							  													 .setOperateType(ConnectBundleBO.OPERATE_TYPE)
																				 .setLock_type("write")
																			     .setBundleId(live.getDstAudioBundleId())
																			     .setLayerId(live.getDstAudioLayerId())
																			     .setBundle_type(live.getDstAudioBundleType());
					ConnectBO connectDstAudioChannel = new ConnectBO().setChannelId(live.getDstAudioChannelId())
																      .setChannel_status("Open")
																      .setBase_type(live.getDstAudioBaseType())
																      .setCodec_param(codec);
					ForwardSetSrcBO audioForwardSetSrc = new ForwardSetSrcBO().setType("channel")
																			  .setBundleId(live.getBundleId())
																			  .setLayerId(live.getLayerId())
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
	 * 处理closebundle协议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 下午3:13:37
	 * @param MonitorLiveDevicePO live 设备点播任务
	 * @return LogicBO 协议
	 */
	private LogicBO closeBundle(MonitorLiveUserPO live) throws Exception{
		LogicBO logic = new LogicBO().setUserId(live.getUserId().toString())
									 .setDisconnectBundle(new ArrayList<DisconnectBundleBO>());
		if(LiveType.XT_LOCAL.equals(live.getType()) || LiveType.LOCAL_LOCAL.equals(live.getType())){
			DisconnectBundleBO disconnectVideoBundle = new DisconnectBundleBO().setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
																		       .setOperateType(DisconnectBundleBO.OPERATE_TYPE)
																			   .setBundleId(live.getBundleId())
																			   .setBundle_type(live.getBundleType())
																			   .setLayerId(live.getLayerId());
			logic.getDisconnectBundle().add(disconnectVideoBundle);
		}
		
		if(LiveType.LOCAL_XT.equals(live.getType()) || LiveType.LOCAL_LOCAL.equals(live.getType())){
			DisconnectBundleBO disconnectDstVideoBundle = new DisconnectBundleBO().setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
																	  			  .setOperateType(DisconnectBundleBO.OPERATE_TYPE)
																				  .setBundleId(live.getDstVideoBundleId())
																			      .setBundle_type(live.getDstVideoBundleType())
																			      .setLayerId(live.getDstVideoLayerId());
			
			logic.getDisconnectBundle().add(disconnectDstVideoBundle);
			
			if(live.getDstAudioBundleId()!=null && !live.getDstVideoBundleId().equals(live.getDstAudioBundleId())){
				DisconnectBundleBO disconnectDstAudioBundle = new DisconnectBundleBO().setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
																					  .setOperateType(DisconnectBundleBO.OPERATE_TYPE)
																					  .setBundleId(live.getDstAudioBundleId())
																				      .setBundle_type(live.getDstAudioBundleType())
																				      .setLayerId(live.getDstAudioLayerId());
				logic.getDisconnectBundle().add(disconnectDstAudioBundle);
			}
		}
		
		return logic;
	}
	
	/**
	 * 权限校验<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月27日 下午2:20:34
	 * @param Long targetUserId 点播目标用户
	 * @param Long userId 操作业务用户
	 */
	private void authorize(Long targetUserId, Long userId) throws Exception{
		boolean authorized = resourceService.hasPrivilegeOfUser(userId, targetUserId, BUSINESS_OPR_TYPE.DIANBO);
		if(!authorized){
			throw new UserHasNoPermissionForBusinessException(BUSINESS_OPR_TYPE.DIANBO, 1);
		}
	}
	
}

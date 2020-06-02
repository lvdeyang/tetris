package com.sumavision.bvc.device.monitor.live.call;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.sumavision.bvc.device.group.service.util.ResourceQueryUtil;
import com.sumavision.bvc.device.monitor.exception.UserHasNoPermissionToRemoveLiveCallException;
import com.sumavision.bvc.device.monitor.live.LiveType;
import com.sumavision.bvc.device.monitor.live.MonitorLiveCommons;
import com.sumavision.bvc.device.monitor.live.exception.UserBusyException;
import com.sumavision.bvc.device.monitor.live.exception.UserHasNoPermissionForBusinessException;
import com.sumavision.bvc.device.monitor.live.user.MonitorLiveUserDAO;
import com.sumavision.bvc.device.monitor.live.user.MonitorLiveUserPO;
import com.sumavision.bvc.resource.dao.ResourceBundleDAO;
import com.sumavision.bvc.resource.dao.ResourceChannelDAO;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.bvc.system.dao.AVtplGearsDAO;
import com.sumavision.bvc.system.dao.AvtplDAO;
import com.sumavision.bvc.system.po.AvtplGearsPO;
import com.sumavision.bvc.system.po.AvtplPO;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class MonitorLiveCallService {
	
	@Autowired
	private AvtplDAO avtplDao;
	
	@Autowired
	private AVtplGearsDAO avtplGearsDao;
	
	@Autowired
	private MonitorLiveUserDAO monitorLiveUserDao;
	
	@Autowired
	private MonitorLiveCallDAO monitorLiveCallDao;
	
	@Autowired
	private ResourceBundleDAO resourceBundleDao;

	@Autowired
	private ResourceChannelDAO resourceChannelDao;

	@Autowired
	private CommonQueryUtil commonQueryUtil;
	
	@Autowired
	private ResourceQueryUtil resourceQueryUtil;
	
	@Autowired
	private MonitorLiveCommons commons;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	@Autowired
	private ResourceService resourceService;
	
	/**
	 * xt点播本地用户任务转xt呼叫本地用户任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月21日 下午5:22:34
	 * @param String uuid 点播任务uuid
	 * @param UserBO localUser 本地用户（被叫用户）
	 * @param UserBO xtUser xt用户（主叫用户）
	 * @return MonitorLiveCallPO xt呼叫本地用户任务
	 */
	public MonitorLiveCallPO transXtCallLocal(
			String uuid,
			UserBO localUser,
			UserBO xtUser) throws Exception{
		
		authorize(localUser.getId(), xtUser.getId());
		
		MonitorLiveUserPO liveUser = monitorLiveUserDao.findByUuid(uuid);
		monitorLiveUserDao.delete(liveUser);
		
		//参数模板
		Map<String, Object> result = commons.queryDefaultAvCodec();
		AvtplPO targetAvtpl = (AvtplPO)result.get("avtpl");
		AvtplGearsPO targetGear = (AvtplGearsPO)result.get("gear");
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
		
		//联网layerid
		String networkLayerId = commons.queryNetworkLayerId();
		
		//被叫用户设备
//		EncoderDecoderUserMap localUserMap = encoderDecoderUserMapDao.findByUserId(localUser.getId());
//		if(localUserMap == null) throw new UserHasNoAvailableEncoderException(localUser.getName());
		String encoderId = commonQueryUtil.queryExternalOrLocalEncoderIdFromUserBO(localUser);
		List<BundlePO> calledEncoderBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(encoderId).getList());
		BundlePO calledEncoderBundleEntity = calledEncoderBundleEntities.get(0);
		
		List<ChannelSchemeDTO> calledEncoderVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(calledEncoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_VIDEO);
		ChannelSchemeDTO calledEncoderVideoChannel = calledEncoderVideoChannels.get(0);
		
		List<ChannelSchemeDTO> calledEncoderAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(calledEncoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_AUDIO);
		ChannelSchemeDTO calledEncoderAudioChannel = calledEncoderAudioChannels.get(0);
		
//		List<BundlePO> calledDecoderBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(localUserMap.getDecodeBundleId()).getList());
//		BundlePO calledDecoderBundleEntity = calledDecoderBundleEntities.get(0);
		BundlePO calledDecoderBundleEntity = resourceQueryUtil.querySpecifiedPlayerBundlePO(localUser.getId());
		
		List<ChannelSchemeDTO> calledDecoderVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(calledDecoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.DECODE_VIDEO);
		ChannelSchemeDTO calledDecoderVideoChannel = calledDecoderVideoChannels.get(0);
		
		List<ChannelSchemeDTO> calledDecoderAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(calledDecoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.DECODE_AUDIO);
		ChannelSchemeDTO calledDecoderAudioChannel = calledDecoderAudioChannels.get(0);
		
		//主叫用户设备
		/*List<BundlePO> callEncoderBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(xtUser.getEncoderId()).getList());
		BundlePO callEncoderBundleEntity = callEncoderBundleEntities.get(0);
		
		List<ChannelSchemeDTO> callEncoderVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(callEncoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_VIDEO);
		ChannelSchemeDTO callEncoderVideoChannel = callEncoderVideoChannels.get(0);
		
		List<ChannelSchemeDTO> callEncoderAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(callEncoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_AUDIO);
		ChannelSchemeDTO callEncoderAudioChannel = callEncoderAudioChannels.get(0);*/
		
		String bundleId = UUID.randomUUID().toString().replaceAll("-", "");
		String videoChannelId = ChannelType.VIDEOENCODE1.getChannelId();
		String audioChannelId = ChannelType.AUDIOENCODE1.getChannelId();
		
		/*MonitorLiveCallPO liveCall = new MonitorLiveCallPO(
				localUser.getId(), localUser.getUserNo(), localUser.getName(),
				calledEncoderBundleEntity.getBundleId(), calledEncoderBundleEntity.getBundleName(), calledEncoderBundleEntity.getBundleType(), calledEncoderBundleEntity.getAccessNodeUid(),
				calledEncoderVideoChannel.getChannelId(), calledEncoderVideoChannel.getBaseType(),
				calledEncoderAudioChannel.getChannelId(), calledEncoderAudioChannel.getBaseType(),
				calledDecoderBundleEntity.getBundleId(), calledDecoderBundleEntity.getBundleName(), calledDecoderBundleEntity.getBundleType(), calledDecoderBundleEntity.getAccessNodeUid(),
				calledDecoderVideoChannel.getChannelId(), calledDecoderVideoChannel.getBaseType(),
				calledDecoderAudioChannel.getChannelId(), calledDecoderAudioChannel.getBaseType(),
				xtUser.getId(), xtUser.getUserNo(), xtUser.getName(),
				callEncoderBundleEntity.getBundleId(), callEncoderBundleEntity.getBundleName(), callEncoderBundleEntity.getBundleType(), callEncoderBundleEntity.getAccessNodeUid(),
				callEncoderVideoChannel.getChannelId(), callEncoderVideoChannel.getBaseType(),
				callEncoderAudioChannel.getChannelId(), callEncoderAudioChannel.getBaseType(),
				null, null, null, null,
				null, null,
				null, null,
				targetAvtpl.getId(),
				targetGear.getId(),
				LiveType.XT_LOCAL);*/
		
		MonitorLiveCallPO liveCall = new MonitorLiveCallPO(
				localUser.getId(), localUser.getUserNo(), localUser.getName(),
				calledEncoderBundleEntity.getBundleId(), calledEncoderBundleEntity.getBundleName(), calledEncoderBundleEntity.getBundleType(), calledEncoderBundleEntity.getAccessNodeUid(),
				calledEncoderVideoChannel.getChannelId(), calledEncoderVideoChannel.getBaseType(),
				calledEncoderAudioChannel.getChannelId(), calledEncoderAudioChannel.getBaseType(),
				calledDecoderBundleEntity.getBundleId(), calledDecoderBundleEntity.getBundleName(), calledDecoderBundleEntity.getBundleType(), calledDecoderBundleEntity.getAccessNodeUid(),
				calledDecoderVideoChannel.getChannelId(), calledDecoderVideoChannel.getBaseType(),
				calledDecoderAudioChannel.getChannelId(), calledDecoderAudioChannel.getBaseType(),
				xtUser.getId(), xtUser.getUserNo(), xtUser.getName(),
				bundleId, "xt虚拟设备", null, networkLayerId,
				videoChannelId, null,
				audioChannelId, null,
				null, null, null, null,
				null, null,
				null, null,
				targetAvtpl.getId(),
				targetGear.getId(),
				LiveType.XT_LOCAL);
		
		if(uuid != null) liveCall.setUuid(uuid);
		
		monitorLiveCallDao.save(liveCall);
		
		LogicBO logic = new LogicBO().setUserId(xtUser.getId().toString())
		 		 					 .setConnectBundle(new ArrayList<ConnectBundleBO>());
		
		//呼叫被叫解码
		ConnectBundleBO connectCalledDecoderBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
				  													      .setOperateType(ConnectBundleBO.OPERATE_TYPE)
																		  .setLock_type("write")
																	      .setBundleId(liveCall.getCalledDecoderBundleId())
																	      .setLayerId(liveCall.getCalledDecoderLayerId())
																	      .setBundle_type(liveCall.getCalledDecoderBundleType());
		ForwardSetSrcBO calledDecoderVideoForwardSet = new ForwardSetSrcBO().setType("channel")
																	 	    .setBundleId(liveCall.getCallEncoderBundleId())
																	 	    .setLayerId(liveCall.getCallEncoderLayerId())
																	 	    .setChannelId(liveCall.getCallEncoderVideoChannelId());
		ConnectBO connectCalledDecoderVideoChannel = new ConnectBO().setChannelId(liveCall.getCalledDecoderVideoChannelId())
															        .setChannel_status("Open")
															        .setBase_type(liveCall.getCalledDecoderVideoBaseType())
															        .setCodec_param(codec)
															        .setSource_param(calledDecoderVideoForwardSet);
		ForwardSetSrcBO calledDecoderAudioForwardSet = new ForwardSetSrcBO().setType("channel")
																	 	    .setBundleId(liveCall.getCallEncoderBundleId())
																	 	    .setLayerId(liveCall.getCallEncoderLayerId())
																	 	    .setChannelId(liveCall.getCallEncoderAudioChannelId());
		ConnectBO connectCalledDecoderAudioChannel = new ConnectBO().setChannelId(liveCall.getCalledDecoderAudioChannelId())
															        .setChannel_status("Open")
															        .setBase_type(liveCall.getCalledDecoderAudioBaseType())
															        .setCodec_param(codec)
															        .setSource_param(calledDecoderAudioForwardSet);
		
		connectCalledDecoderBundle.setChannels(new ArrayListWrapper<ConnectBO>().add(connectCalledDecoderVideoChannel).add(connectCalledDecoderAudioChannel).getList());
		logic.getConnectBundle().add(connectCalledDecoderBundle);
		
		//处理passBy协议
		logic.setPass_by(new ArrayList<PassByBO>());
		
		XtBusinessPassByContentBO passByContent = new XtBusinessPassByContentBO().setCmd(XtBusinessPassByContentBO.CMD_XT_CALL_LOCAL_USER)
																				 .setOperate(XtBusinessPassByContentBO.OPERATE_START)
																				 .setUuid(liveCall.getUuid())
																				 .setSrc_user(xtUser.getUserNo())
																				 .setLocal_encoder(new HashMapWrapper<String, String>().put("layerid", calledEncoderBundleEntity.getAccessNodeUid())
																						 											   .put("bundleid", calledEncoderBundleEntity.getBundleId())
																						 											   .put("video_channelid", calledEncoderVideoChannel.getChannelId())
																						 											   .put("audio_channelid", calledEncoderAudioChannel.getChannelId())
																						 											   .getMap())
																				 .setXt_encoder(new HashMapWrapper<String, String>().put("layerid", networkLayerId)
																						 											.put("bundleid", bundleId)
																						 											.put("video_channelid", videoChannelId)
																						 											.put("audio_channelid", audioChannelId)
																						                                            .getMap())
																				 .setDst_number(localUser.getUserNo())
																				 .setVparam(codec);
		
		PassByBO passby = new PassByBO().setLayer_id(networkLayerId)
										.setType(XtBusinessPassByContentBO.CMD_XT_CALL_LOCAL_USER)
										.setPass_by_content(passByContent);

		logic.getPass_by().add(passby);
		
		executeBusiness.execute(logic, "点播系统：xt用户点播本地用户转xt用户呼叫本地用户");
		
		return liveCall;
	}
	
	/**
	 * xt用户呼叫本地用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月20日 上午10:27:42
	 * @param String uuid 既定uuid
	 * @param UserBO localUser 本地用户
	 * @param UserBO xtUser xt用户
	 * @return MonitorLiveCallPO xt用户呼叫本地用户任务
	 */
	public MonitorLiveCallPO startXtCallLocal(
			String uuid,
			UserBO localUser,
			UserBO xtUser) throws Exception{
		
		authorize(localUser.getId(), xtUser.getId());
		
		//参数模板
		Map<String, Object> result = commons.queryDefaultAvCodec();
		AvtplPO targetAvtpl = (AvtplPO)result.get("avtpl");
		AvtplGearsPO targetGear = (AvtplGearsPO)result.get("gear");
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
		
		//联网layerid
		String networkLayerId = commons.queryNetworkLayerId();
		
		//被叫用户设备
//		EncoderDecoderUserMap localUserMap = encoderDecoderUserMapDao.findByUserId(localUser.getId());
//		if(localUserMap == null) throw new UserHasNoAvailableEncoderException(localUser.getName());
		String encoderId = commonQueryUtil.queryExternalOrLocalEncoderIdFromUserBO(localUser);
		List<BundlePO> calledEncoderBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(encoderId).getList());
		BundlePO calledEncoderBundleEntity = calledEncoderBundleEntities.get(0);
		
		List<ChannelSchemeDTO> calledEncoderVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(calledEncoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_VIDEO);
		ChannelSchemeDTO calledEncoderVideoChannel = calledEncoderVideoChannels.get(0);
		
		List<ChannelSchemeDTO> calledEncoderAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(calledEncoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_AUDIO);
		ChannelSchemeDTO calledEncoderAudioChannel = calledEncoderAudioChannels.get(0);
		
//		List<BundlePO> calledDecoderBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(localUserMap.getDecodeBundleId()).getList());
//		BundlePO calledDecoderBundleEntity = calledDecoderBundleEntities.get(0);
		BundlePO calledDecoderBundleEntity = resourceQueryUtil.querySpecifiedPlayerBundlePO(localUser.getId());
		
		List<ChannelSchemeDTO> calledDecoderVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(calledDecoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.DECODE_VIDEO);
		ChannelSchemeDTO calledDecoderVideoChannel = calledDecoderVideoChannels.get(0);
		
		List<ChannelSchemeDTO> calledDecoderAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(calledDecoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.DECODE_AUDIO);
		ChannelSchemeDTO calledDecoderAudioChannel = calledDecoderAudioChannels.get(0);
		
		//主叫用户设备
		/*List<BundlePO> callEncoderBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(xtUser.getEncoderId()).getList());
		BundlePO callEncoderBundleEntity = callEncoderBundleEntities.get(0);
		
		List<ChannelSchemeDTO> callEncoderVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(callEncoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_VIDEO);
		ChannelSchemeDTO callEncoderVideoChannel = callEncoderVideoChannels.get(0);
		
		List<ChannelSchemeDTO> callEncoderAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(callEncoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_AUDIO);
		ChannelSchemeDTO callEncoderAudioChannel = callEncoderAudioChannels.get(0);*/
		
		//xt虚拟编码器
		String bundleId = UUID.randomUUID().toString().replaceAll("-", "");
		String videoChannelId = ChannelType.VIDEOENCODE1.getChannelId();
		String audioChannelId = ChannelType.AUDIOENCODE1.getChannelId();
		
		/*MonitorLiveCallPO live = new MonitorLiveCallPO(
				localUser.getId(), localUser.getUserNo(), localUser.getName(),
				calledEncoderBundleEntity.getBundleId(), calledEncoderBundleEntity.getBundleName(), calledEncoderBundleEntity.getBundleType(), calledEncoderBundleEntity.getAccessNodeUid(),
				calledEncoderVideoChannel.getChannelId(), calledEncoderVideoChannel.getBaseType(),
				calledEncoderAudioChannel.getChannelId(), calledEncoderAudioChannel.getBaseType(),
				calledDecoderBundleEntity.getBundleId(), calledDecoderBundleEntity.getBundleName(), calledDecoderBundleEntity.getBundleType(), calledDecoderBundleEntity.getAccessNodeUid(),
				calledDecoderVideoChannel.getChannelId(), calledDecoderVideoChannel.getBaseType(),
				calledDecoderAudioChannel.getChannelId(), calledDecoderAudioChannel.getBaseType(),
				xtUser.getId(), xtUser.getUserNo(), xtUser.getName(),
				callEncoderBundleEntity.getBundleId(), callEncoderBundleEntity.getBundleName(), callEncoderBundleEntity.getBundleType(), callEncoderBundleEntity.getAccessNodeUid(),
				callEncoderVideoChannel.getChannelId(), callEncoderVideoChannel.getBaseType(),
				callEncoderAudioChannel.getChannelId(), callEncoderAudioChannel.getBaseType(),
				null, null, null, null,
				null, null,
				null, null,
				targetAvtpl.getId(),
				targetGear.getId(),
				LiveType.XT_LOCAL);*/
		
		MonitorLiveCallPO live = new MonitorLiveCallPO(
				localUser.getId(), localUser.getUserNo(), localUser.getName(),
				calledEncoderBundleEntity.getBundleId(), calledEncoderBundleEntity.getBundleName(), calledEncoderBundleEntity.getBundleType(), calledEncoderBundleEntity.getAccessNodeUid(),
				calledEncoderVideoChannel.getChannelId(), calledEncoderVideoChannel.getBaseType(),
				calledEncoderAudioChannel.getChannelId(), calledEncoderAudioChannel.getBaseType(),
				calledDecoderBundleEntity.getBundleId(), calledDecoderBundleEntity.getBundleName(), calledDecoderBundleEntity.getBundleType(), calledDecoderBundleEntity.getAccessNodeUid(),
				calledDecoderVideoChannel.getChannelId(), calledDecoderVideoChannel.getBaseType(),
				calledDecoderAudioChannel.getChannelId(), calledDecoderAudioChannel.getBaseType(),
				xtUser.getId(), xtUser.getUserNo(), xtUser.getName(),
				bundleId, "xt虚拟编码器", null, networkLayerId,
				videoChannelId, null,
				audioChannelId, null,
				null, null, null, null,
				null, null,
				null, null,
				targetAvtpl.getId(),
				targetGear.getId(),
				LiveType.XT_LOCAL);
		
		if(uuid != null) live.setUuid(uuid);
		
		monitorLiveCallDao.save(live);
		
		LogicBO logic = openBundle(live, codec, xtUser.getId());
		
		logic.setPass_by(new ArrayList<PassByBO>());
		
		XtBusinessPassByContentBO passByContent = new XtBusinessPassByContentBO().setCmd(XtBusinessPassByContentBO.CMD_XT_CALL_LOCAL_USER)
																				 .setOperate(XtBusinessPassByContentBO.OPERATE_START)
																				 .setUuid(live.getUuid())
																				 .setSrc_user(xtUser.getUserNo())
																				 .setLocal_encoder(new HashMapWrapper<String, String>().put("layerid", calledEncoderBundleEntity.getAccessNodeUid())
																						 											   .put("bundleid", calledEncoderBundleEntity.getBundleId())
																						 											   .put("video_channelid", calledEncoderVideoChannel.getChannelId())
																						 											   .put("audio_channelid", calledEncoderAudioChannel.getChannelId())
																						 											   .getMap())
																				 .setXt_encoder(new HashMapWrapper<String, String>().put("layerid", networkLayerId)
																						 											.put("bundleid", bundleId)
																						 											.put("video_channelid", videoChannelId)
																						 											.put("audio_channelid", audioChannelId)
																						                                            .getMap())
																				 .setDst_number(localUser.getUserNo())
																				 .setVparam(codec);
		
		PassByBO passby = new PassByBO().setLayer_id(networkLayerId)
										.setType(XtBusinessPassByContentBO.CMD_XT_CALL_LOCAL_USER)
										.setPass_by_content(passByContent);

		logic.getPass_by().add(passby);
		
		executeBusiness.execute(logic, "点播系统：xt用户呼叫本地用户");
		
		return live;
	}
	
	/**
	 * 本地用户呼叫本地用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月20日 上午10:29:51
	 * @param UserBO calledUser 被叫本地用户
	 * @param UserBO callUser 主叫本地用户
	 * @return MonitorLiveCallPO 本地用户呼叫本地用户任务
	 */
	public MonitorLiveCallPO startLocalCallLocal(
			UserBO calledUser,
			UserBO callUser) throws Exception{
		
		authorize(calledUser.getId(), callUser.getId());
		
		//业务覆盖
		coverBusiness(calledUser.getId(), callUser.getId(), true);
		
		//参数模板
		Map<String, Object> result = commons.queryDefaultAvCodec();
		AvtplPO targetAvtpl = (AvtplPO)result.get("avtpl");
		AvtplGearsPO targetGear = (AvtplGearsPO)result.get("gear");
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
		
		//被叫用户设备
//		EncoderDecoderUserMap calledUserMap = encoderDecoderUserMapDao.findByUserId(calledUser.getId());
//		if(calledUserMap == null) throw new UserHasNoAvailableEncoderException(calledUser.getName());
		String calledUserEncoderId = commonQueryUtil.queryExternalOrLocalEncoderIdFromUserBO(calledUser);
		List<BundlePO> calledEncoderBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(calledUserEncoderId).getList());
		BundlePO calledEncoderBundleEntity = calledEncoderBundleEntities.get(0);
		
		List<ChannelSchemeDTO> calledEncoderVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(calledEncoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_VIDEO);
		ChannelSchemeDTO calledEncoderVideoChannel = calledEncoderVideoChannels.get(0);
		
		List<ChannelSchemeDTO> calledEncoderAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(calledEncoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_AUDIO);
		ChannelSchemeDTO calledEncoderAudioChannel = calledEncoderAudioChannels.get(0);
		
//		List<BundlePO> calledDecoderBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(calledUserMap.getDecodeBundleId()).getList());
//		BundlePO calledDecoderBundleEntity = calledDecoderBundleEntities.get(0);
		BundlePO calledDecoderBundleEntity = resourceQueryUtil.querySpecifiedPlayerBundlePO(calledUser.getId());
		
		List<ChannelSchemeDTO> calledDecoderVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(calledDecoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.DECODE_VIDEO);
		ChannelSchemeDTO calledDecoderVideoChannel = calledDecoderVideoChannels.get(0);
		
		List<ChannelSchemeDTO> calledDecoderAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(calledDecoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.DECODE_AUDIO);
		ChannelSchemeDTO calledDecoderAudioChannel = calledDecoderAudioChannels.get(0);
		
		//主叫用户设备
//		EncoderDecoderUserMap callUserMap = encoderDecoderUserMapDao.findByUserId(callUser.getId());
		String callUserEncoderId = commonQueryUtil.queryExternalOrLocalEncoderIdFromUserBO(callUser);
		List<BundlePO> callEncoderBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(callUserEncoderId).getList());
		BundlePO callEncoderBundleEntity = callEncoderBundleEntities.get(0);
		
		List<ChannelSchemeDTO> callEncoderVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(callEncoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_VIDEO);
		ChannelSchemeDTO callEncoderVideoChannel = callEncoderVideoChannels.get(0);
		
		List<ChannelSchemeDTO> callEncoderAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(callEncoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_AUDIO);
		ChannelSchemeDTO callEncoderAudioChannel = callEncoderAudioChannels.get(0);
		
//		List<BundlePO> callDecoderBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(callUserMap.getDecodeBundleId()).getList());
//		BundlePO callDecoderBundleEntity = callDecoderBundleEntities.get(0);
		BundlePO callDecoderBundleEntity = resourceQueryUtil.querySpecifiedPlayerBundlePO(callUser.getId());
		
		List<ChannelSchemeDTO> callDecoderVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(callDecoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.DECODE_VIDEO);
		ChannelSchemeDTO callDecoderVideoChannel = callDecoderVideoChannels.get(0);
		
		List<ChannelSchemeDTO> callDecoderAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(callDecoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.DECODE_AUDIO);
		ChannelSchemeDTO callDecoderAudioChannel = callDecoderAudioChannels.get(0);
		
		MonitorLiveCallPO live = new MonitorLiveCallPO(
				calledUser.getId(), calledUser.getUserNo(), calledUser.getName(),
				calledEncoderBundleEntity.getBundleId(), calledEncoderBundleEntity.getBundleName(), calledEncoderBundleEntity.getBundleType(), calledEncoderBundleEntity.getAccessNodeUid(),
				calledEncoderVideoChannel.getChannelId(), calledEncoderVideoChannel.getBaseType(),
				calledEncoderAudioChannel.getChannelId(), calledEncoderAudioChannel.getBaseType(),
				calledDecoderBundleEntity.getBundleId(), calledDecoderBundleEntity.getBundleName(), calledDecoderBundleEntity.getBundleType(), calledDecoderBundleEntity.getAccessNodeUid(),
				calledDecoderVideoChannel.getChannelId(), calledDecoderVideoChannel.getBaseType(),
				calledDecoderAudioChannel.getChannelId(), calledDecoderAudioChannel.getBaseType(),
				callUser.getId(), callUser.getUserNo(), callUser.getName(),
				callEncoderBundleEntity.getBundleId(), callEncoderBundleEntity.getBundleName(), callEncoderBundleEntity.getBundleType(), callEncoderBundleEntity.getAccessNodeUid(),
				callEncoderVideoChannel.getChannelId(), callEncoderVideoChannel.getBaseType(),
				callEncoderAudioChannel.getChannelId(), callEncoderAudioChannel.getBaseType(),
				callDecoderBundleEntity.getBundleId(), callDecoderBundleEntity.getBundleName(), callDecoderBundleEntity.getBundleType(), callDecoderBundleEntity.getAccessNodeUid(),
				callDecoderVideoChannel.getChannelId(), callDecoderVideoChannel.getBaseType(),
				callDecoderAudioChannel.getChannelId(), callDecoderAudioChannel.getBaseType(),
				targetAvtpl.getId(),
				targetGear.getId(),
				LiveType.LOCAL_LOCAL);
		
		monitorLiveCallDao.save(live);
		
		LogicBO logic = openBundle(live, codec, callUser.getId());
		
		executeBusiness.execute(logic, "点播系统：本地用户呼叫本地用户");
		
		return live;
	}
	
	/**
	 * 本地用户呼叫xt用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月20日 上午10:33:15
	 * @param UserBO xtUser xt用户
	 * @param UserBO localUser 本地用户
	 * @return MonitorLiveCallPO 本地用户呼叫xt用户任务
	 */
	public MonitorLiveCallPO startLocalCallXt(
			UserBO xtUser,
			UserBO localUser) throws Exception{
		
		authorize(xtUser.getId(), localUser.getId());
		
		//业务覆盖
		coverBusiness(xtUser.getId(), localUser.getId(), true);
		
		//参数模板
		Map<String, Object> result = commons.queryDefaultAvCodec();
		AvtplPO targetAvtpl = (AvtplPO)result.get("avtpl");
		AvtplGearsPO targetGear = (AvtplGearsPO)result.get("gear");
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
		
		//联网layerid
		String networkLayerId = commons.queryNetworkLayerId();
		
		//被叫用户设备
		/*List<BundlePO> calledEncoderBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(xtUser.getEncoderId()).getList());
		BundlePO calledEncoderBundleEntity = calledEncoderBundleEntities.get(0);
		
		List<ChannelSchemeDTO> calledEncoderVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(calledEncoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_VIDEO);
		ChannelSchemeDTO calledEncoderVideoChannel = calledEncoderVideoChannels.get(0);
		
		List<ChannelSchemeDTO> calledEncoderAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(calledEncoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_AUDIO);
		ChannelSchemeDTO calledEncoderAudioChannel = calledEncoderAudioChannels.get(0);*/
		
		//xt用户虚拟编码器
		String bundleId = UUID.randomUUID().toString().replace("-", "");
		String videoChannelId = ChannelType.VIDEOENCODE1.getChannelId();
		String audioChannelId = ChannelType.AUDIOENCODE1.getChannelId();
		
		//主叫用户设备
//		EncoderDecoderUserMap localUserMap = encoderDecoderUserMapDao.findByUserId(localUser.getId());
//		if(localUserMap == null) throw new UserHasNoAvailableEncoderException(localUser.getName());
		String localUserEncoderId = commonQueryUtil.queryExternalOrLocalEncoderIdFromUserBO(localUser);
		List<BundlePO> callEncoderBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(localUserEncoderId).getList());
		BundlePO callEncoderBundleEntity = callEncoderBundleEntities.get(0);
		
		List<ChannelSchemeDTO> callEncoderVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(callEncoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_VIDEO);
		ChannelSchemeDTO callEncoderVideoChannel = callEncoderVideoChannels.get(0);
		
		List<ChannelSchemeDTO> callEncoderAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(callEncoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_AUDIO);
		ChannelSchemeDTO callEncoderAudioChannel = callEncoderAudioChannels.get(0);
		
//		List<BundlePO> callDecoderBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(localUserMap.getDecodeBundleId()).getList());
//		BundlePO callDecoderBundleEntity = callDecoderBundleEntities.get(0);
		BundlePO callDecoderBundleEntity = resourceQueryUtil.querySpecifiedPlayerBundlePO(localUser.getId());
		
		List<ChannelSchemeDTO> callDecoderVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(callDecoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.DECODE_VIDEO);
		ChannelSchemeDTO callDecoderVideoChannel = callDecoderVideoChannels.get(0);
		
		List<ChannelSchemeDTO> callDecoderAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(callDecoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.DECODE_AUDIO);
		ChannelSchemeDTO callDecoderAudioChannel = callDecoderAudioChannels.get(0);
		
		/*MonitorLiveCallPO live = new MonitorLiveCallPO(
				xtUser.getId(), xtUser.getUserNo(), xtUser.getName(),
				calledEncoderBundleEntity.getBundleId(), calledEncoderBundleEntity.getBundleName(), calledEncoderBundleEntity.getBundleType(), calledEncoderBundleEntity.getAccessNodeUid(),
				calledEncoderVideoChannel.getChannelId(), calledEncoderVideoChannel.getBaseType(),
				calledEncoderAudioChannel.getChannelId(), calledEncoderAudioChannel.getBaseType(),
				null, null, null, null,
				null, null,
				null, null,
				localUser.getId(), localUser.getUserNo(), localUser.getName(),
				callEncoderBundleEntity.getBundleId(), callEncoderBundleEntity.getBundleName(), callEncoderBundleEntity.getBundleType(), callEncoderBundleEntity.getAccessNodeUid(),
				callEncoderVideoChannel.getChannelId(), callEncoderVideoChannel.getBaseType(),
				callEncoderAudioChannel.getChannelId(), callEncoderAudioChannel.getBaseType(),
				callDecoderBundleEntity.getBundleId(), callDecoderBundleEntity.getBundleName(), callDecoderBundleEntity.getBundleType(), callDecoderBundleEntity.getAccessNodeUid(),
				callDecoderVideoChannel.getChannelId(), callDecoderVideoChannel.getBaseType(),
				callDecoderAudioChannel.getChannelId(), callDecoderAudioChannel.getBaseType(),
				targetAvtpl.getId(),
				targetGear.getId(),
				LiveType.LOCAL_XT);*/
		
		MonitorLiveCallPO live = new MonitorLiveCallPO(
				xtUser.getId(), xtUser.getUserNo(), xtUser.getName(),
				bundleId, "xt虚拟编码器", null, networkLayerId,
				videoChannelId, null,
				audioChannelId, null,
				null, null, null, null,
				null, null,
				null, null,
				localUser.getId(), localUser.getUserNo(), localUser.getName(),
				callEncoderBundleEntity.getBundleId(), callEncoderBundleEntity.getBundleName(), callEncoderBundleEntity.getBundleType(), callEncoderBundleEntity.getAccessNodeUid(),
				callEncoderVideoChannel.getChannelId(), callEncoderVideoChannel.getBaseType(),
				callEncoderAudioChannel.getChannelId(), callEncoderAudioChannel.getBaseType(),
				callDecoderBundleEntity.getBundleId(), callDecoderBundleEntity.getBundleName(), callDecoderBundleEntity.getBundleType(), callDecoderBundleEntity.getAccessNodeUid(),
				callDecoderVideoChannel.getChannelId(), callDecoderVideoChannel.getBaseType(),
				callDecoderAudioChannel.getChannelId(), callDecoderAudioChannel.getBaseType(),
				targetAvtpl.getId(),
				targetGear.getId(),
				LiveType.LOCAL_XT);
		
		monitorLiveCallDao.save(live);
		
		LogicBO logic = openBundle(live, codec, localUser.getId());

		logic.setPass_by(new ArrayList<PassByBO>());
		
		XtBusinessPassByContentBO passByContent = new XtBusinessPassByContentBO().setCmd(XtBusinessPassByContentBO.CMD_LOCAL_CALL_XT_USER)
																				 .setOperate(XtBusinessPassByContentBO.OPERATE_START)
																				 .setUuid(live.getUuid())
																				 .setSrc_user(localUser.getUserNo())
																				 .setLocal_encoder(new HashMapWrapper<String, String>().put("layerid", callEncoderBundleEntity.getAccessNodeUid())
																						 											   .put("bundleid", callEncoderBundleEntity.getBundleId())
																						                         					   .put("video_channelid", callEncoderVideoChannel.getChannelId())
																						                         					   .put("audio_channelid", callEncoderAudioChannel.getChannelId())
																						 											   .getMap())
																				 .setXt_encoder(new HashMapWrapper<String, String>().put("layerid", networkLayerId)
																						 											.put("bundleid", bundleId)
																						 											.put("video_channelid", videoChannelId)
																						 											.put("audio_channelid", audioChannelId)
																						 											.getMap())
																				 .setDst_number(xtUser.getUserNo())
																				 .setVparam(codec);
		
		PassByBO passby = new PassByBO().setLayer_id(networkLayerId)
										.setType(XtBusinessPassByContentBO.CMD_LOCAL_CALL_XT_USER)
										.setPass_by_content(passByContent);

		logic.getPass_by().add(passby);
		
		executeBusiness.execute(logic, "点播系统：本地用户呼叫xt用户");
		
		return live;
	}
	
	/**
	 * xt用户呼叫xt用户，暂时不实现<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月20日 上午10:21:44
	 */
	public MonitorLiveCallPO startXtCallXt() throws Exception{
		return null;
	}
	
	/**
	 * 停止呼叫用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月20日 下午3:54:17
	 * @param Long liveId 任务id
	 * @param Long userId 发起用户id
	 */
	public void stop(Long liveId, Long userId) throws Exception{
		MonitorLiveCallPO live = monitorLiveCallDao.findOne(liveId);
		if(live == null) return;
		if(!live.getCalledUserId().equals(userId) && !live.getCallUserId().equals(userId)){
			throw new UserHasNoPermissionToRemoveLiveCallException(userId, liveId);
		}
		if(LiveType.XT_LOCAL.equals(live.getType())){
			stopXtCallLocal(live, userId);
		}else if(LiveType.LOCAL_XT.equals(live.getType())){
			stopLocalCallXt(live, userId);
		}else if(LiveType.LOCAL_LOCAL.equals(live.getType())){
			stopLocalCallLocal(live, userId);
		}else if(LiveType.XT_XT.equals(live.getType())){
			stopXtCallXt(live, userId);
		}
	}
	
	/**
	 * 停止呼叫用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月20日 下午3:54:17
	 * @param Long liveUuid 任务uuid
	 * @param Long userId 发起用户id
	 */
	public void stop(String liveUuid, Long userId) throws Exception{
		MonitorLiveCallPO live = monitorLiveCallDao.findByUuid(liveUuid);
		if(live == null) return;
		if(!live.getCalledUserId().equals(userId) && !live.getCallUserId().equals(userId)){
			throw new UserHasNoPermissionToRemoveLiveCallException(userId, liveUuid);
		}
		if(LiveType.XT_LOCAL.equals(live.getType())){
			stopXtCallLocal(live, userId);
		}else if(LiveType.LOCAL_XT.equals(live.getType())){
			stopLocalCallXt(live, userId);
		}else if(LiveType.LOCAL_LOCAL.equals(live.getType())){
			stopLocalCallLocal(live, userId);
		}else if(LiveType.XT_XT.equals(live.getType())){
			stopXtCallXt(live, userId);
		}
	}
	
	/**
	 * 停止xt用户呼叫本地用户任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月20日 下午3:55:47
	 * @param MonitorLiveCallPO live xt用户点播本地用户任务
	 * @param Long userId 发起用户id
	 */
	public void stopXtCallLocal(MonitorLiveCallPO live, Long userId) throws Exception{
		
		//参数模板
		AvtplPO targetAvtpl = avtplDao.findOne(live.getAvTplId());
		AvtplGearsPO targetGear = avtplGearsDao.findOne(live.getGearId());
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
		
		String networkLayerId = commons.queryNetworkLayerId();
		
		LogicBO logic = closeBundle(live, userId);
		
		logic.setPass_by(new ArrayList<PassByBO>());
		
		XtBusinessPassByContentBO passByContent = new XtBusinessPassByContentBO().setCmd(XtBusinessPassByContentBO.CMD_XT_CALL_LOCAL_USER)
																				 .setOperate(XtBusinessPassByContentBO.OPERATE_STOP)
																				 .setUuid(live.getUuid())
																				 .setSrc_user(live.getCallUserno())
																				 .setLocal_encoder(new HashMapWrapper<String, String>().put("layerid", live.getCalledEncoderLayerId())
																						 											   .put("bundleid", live.getCalledEncoderBundleId())
																						 											   .put("video_channelid", live.getCalledEncoderVideoChannelId())
																						 											   .put("audio_channelid", live.getCalledEncoderAudioChannelId())
																						 											   .getMap())
																				 .setXt_encoder(new HashMapWrapper<String, String>().put("layerid", networkLayerId)
																						 											.put("bundleid", live.getCallEncoderBundleId())
																						 											.put("video_channelid", live.getCallEncoderVideoChannelId())
																						 											.put("audio_channelid", live.getCallEncoderAudioChannelId())
																						                                            .getMap())
																				 .setDst_number(live.getCalledUserno())
																				 .setVparam(codec);
		
		PassByBO passby = new PassByBO().setLayer_id(networkLayerId)
										.setType(XtBusinessPassByContentBO.CMD_XT_CALL_LOCAL_USER)
										.setPass_by_content(passByContent);

		logic.getPass_by().add(passby);
		
		monitorLiveCallDao.delete(live);
		
		executeBusiness.execute(logic, "点播系统：停止xt用户呼叫本地用户任务");
	}
	
	/**
	 * 停止本地用户呼叫本地用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月20日 下午3:56:44
	 * @param MonitorLiveCallPO live xt用户点播本地用户任务
	 * @param Long userId 发起用户id
	 */
	public void stopLocalCallLocal(MonitorLiveCallPO live, Long userId) throws Exception{
		
		LogicBO logic = closeBundle(live, userId);
		
		monitorLiveCallDao.delete(live);
		
		executeBusiness.execute(logic, "点播系统：停止本地用户呼叫本地用户任务");
	}
	
	/**
	 * 停止本地用户呼叫xt用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月20日 下午3:57:40
	 * @param MonitorLiveCallPO live xt用户点播本地用户任务
	 * @param Long userId 发起用户id
	 */
	public void stopLocalCallXt(MonitorLiveCallPO live, Long userId) throws Exception{
		
		//参数模板
		AvtplPO targetAvtpl = avtplDao.findOne(live.getAvTplId());
		AvtplGearsPO targetGear = avtplGearsDao.findOne(live.getGearId());
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
		
		String networkLayerId = commons.queryNetworkLayerId();
		
		LogicBO logic = closeBundle(live, userId);
		
		logic.setPass_by(new ArrayList<PassByBO>());
		
		XtBusinessPassByContentBO passByContent = new XtBusinessPassByContentBO().setCmd(XtBusinessPassByContentBO.CMD_LOCAL_CALL_XT_USER)
																				 .setOperate(XtBusinessPassByContentBO.OPERATE_STOP)
																				 .setUuid(live.getUuid())
																				 .setSrc_user(live.getCallUserno())
																				 .setLocal_encoder(new HashMapWrapper<String, String>().put("layerid", live.getCallEncoderLayerId())
																						 											   .put("bundleid", live.getCallEncoderBundleId())
																						                         					   .put("video_channelid", live.getCallEncoderVideoChannelId())
																						                         					   .put("audio_channelid", live.getCallEncoderAudioChannelId())
																						 											   .getMap())
																				 .setXt_encoder(new HashMapWrapper<String, String>().put("layerid", networkLayerId)
																						 											.put("bundleid", live.getCalledEncoderBundleId())
																						 											.put("video_channelid", live.getCalledEncoderVideoChannelId())
																						 											.put("audio_channelid", live.getCalledEncoderAudioChannelId())
																						 											.getMap())
																				 .setDst_number(live.getCalledUserno())
																				 .setVparam(codec);
		
		PassByBO passby = new PassByBO().setLayer_id(networkLayerId)
										.setType(XtBusinessPassByContentBO.CMD_LOCAL_CALL_XT_USER)
										.setPass_by_content(passByContent);

		logic.getPass_by().add(passby);
		
		monitorLiveCallDao.delete(live);
		
		executeBusiness.execute(logic, "点播系统：停止本地用户呼叫xt用户任务");
	}
	
	/**
	 * 停止xt用户呼叫xt用户，暂时不用实现<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月20日 下午3:58:20
	 * @param MonitorLiveCallPO live xt用户点播本地用户任务
	 * @param Long userId 发起用户id
	 */
	public void stopXtCallXt(MonitorLiveCallPO live, Long userId) throws Exception{
		
	}
	
	/**
	 * 覆盖业务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 下午3:45:00
	 * @param Long calledUserId 被叫用户id
	 * @param Long callUserId 主叫用户id
	 * @param boolean doCover 是否做业务覆盖
	 */
	public void coverBusiness(Long calledUserId, Long callUserId, boolean doCover) throws Exception{
		//被叫用户是否正忙
		List<MonitorLiveCallPO> calledLives = monitorLiveCallDao.findByCallUserIdOrCalledUserId(calledUserId);
		if(calledLives!=null && calledLives.size()>0){
			throw new UserBusyException(calledUserId);
		}
		
		if(doCover){
			List<MonitorLiveCallPO> callLives = monitorLiveCallDao.findByCallUserIdOrCalledUserId(callUserId);
			if(callLives!=null && callLives.size()>0){
				for(MonitorLiveCallPO callLive:callLives){
					stop(callLive.getId(), callUserId);
				}
			}
		}
	}
	
	/**
	 * 处理openbundle协议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 上午10:24:26
	 * @param MonitorLiveCallPO live 呼叫用户任务
	 * @param CodecParamBO codec 参数模板
	 * @param Long userId 业务用户
	 * @return LogicBO 协议数据
	 */
	public LogicBO openBundle(
			MonitorLiveCallPO live, 
			CodecParamBO codec,
			Long userId) throws Exception{
		
		//呼叫设备
		LogicBO logic = new LogicBO().setUserId(userId.toString())
				 			 		 .setConnectBundle(new ArrayList<ConnectBundleBO>());
		
		if(LiveType.XT_LOCAL.equals(live.getType()) || LiveType.LOCAL_LOCAL.equals(live.getType())){
			
			//主叫解码看被叫编码
			
			//呼叫被叫编码
			ConnectBundleBO connectCalledEncoderBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
																	          .setOperateType(ConnectBundleBO.OPERATE_TYPE)
																			  .setLock_type("write")
																			  .setBundleId(live.getCalledEncoderBundleId())
																			  .setLayerId(live.getCalledEncoderLayerId())
																			  .setBundle_type(live.getCalledEncoderBundleType());
			ConnectBO connectCalledEncoderVideoChannel = new ConnectBO().setChannelId(live.getCalledEncoderVideoChannelId())
																	    .setChannel_status("Open")
																	    .setBase_type(live.getCalledEncoderVideoBaseType())
																	    .setCodec_param(codec);
			ConnectBO connectCalledEncoderAudioChannel = new ConnectBO().setChannelId(live.getCalledEncoderAudioChannelId())
																	    .setChannel_status("Open")
																	    .setBase_type(live.getCalledEncoderAudioBaseType())
																	    .setCodec_param(codec);
			
			connectCalledEncoderBundle.setChannels(new ArrayListWrapper<ConnectBO>().add(connectCalledEncoderVideoChannel).add(connectCalledEncoderAudioChannel).getList());
			logic.getConnectBundle().add(connectCalledEncoderBundle);
			
			//被叫解码看主叫编码
			
			//呼叫被叫解码
			ConnectBundleBO connectCalledDecoderBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
					  													      .setOperateType(ConnectBundleBO.OPERATE_TYPE)
																			  .setLock_type("write")
																		      .setBundleId(live.getCalledDecoderBundleId())
																		      .setLayerId(live.getCalledDecoderLayerId())
																		      .setBundle_type(live.getCalledDecoderBundleType());
			ForwardSetSrcBO calledDecoderVideoForwardSet = new ForwardSetSrcBO().setType("channel")
																		 	    .setBundleId(live.getCallEncoderBundleId())
																		 	    .setLayerId(live.getCallEncoderLayerId())
																		 	    .setChannelId(live.getCallEncoderVideoChannelId());
			ConnectBO connectCalledDecoderVideoChannel = new ConnectBO().setChannelId(live.getCalledDecoderVideoChannelId())
																        .setChannel_status("Open")
																        .setBase_type(live.getCalledDecoderVideoBaseType())
																        .setCodec_param(codec)
																        .setSource_param(calledDecoderVideoForwardSet);
			ForwardSetSrcBO calledDecoderAudioForwardSet = new ForwardSetSrcBO().setType("channel")
																		 	    .setBundleId(live.getCallEncoderBundleId())
																		 	    .setLayerId(live.getCallEncoderLayerId())
																		 	    .setChannelId(live.getCallEncoderAudioChannelId());
			ConnectBO connectCalledDecoderAudioChannel = new ConnectBO().setChannelId(live.getCalledDecoderAudioChannelId())
																        .setChannel_status("Open")
																        .setBase_type(live.getCalledDecoderAudioBaseType())
																        .setCodec_param(codec)
																        .setSource_param(calledDecoderAudioForwardSet);
			
			connectCalledDecoderBundle.setChannels(new ArrayListWrapper<ConnectBO>().add(connectCalledDecoderVideoChannel).add(connectCalledDecoderAudioChannel).getList());
			logic.getConnectBundle().add(connectCalledDecoderBundle);
			
		}
		
		if(LiveType.LOCAL_XT.equals(live.getType()) || LiveType.LOCAL_LOCAL.equals(live.getType())){
			
			//主叫解码看被叫编码
			
			//呼叫主叫解码
			ConnectBundleBO connectCallDecoderBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
					  													    .setOperateType(ConnectBundleBO.OPERATE_TYPE)
																			.setLock_type("write")
																		    .setBundleId(live.getCallDecoderBundleId())
																		    .setLayerId(live.getCallDecoderLayerId())
																		    .setBundle_type(live.getCallDecoderBundleType());
			ForwardSetSrcBO callDecoderVideoForwardSet = new ForwardSetSrcBO().setType("channel")
																		 	  .setBundleId(live.getCalledEncoderBundleId())
																		 	  .setLayerId(live.getCalledEncoderLayerId())
																		 	  .setChannelId(live.getCalledEncoderVideoChannelId());
			ConnectBO connectCallDecoderVideoChannel = new ConnectBO().setChannelId(live.getCallDecoderVideoChannelId())
																      .setChannel_status("Open")
																      .setBase_type(live.getCallDecoderVideoBaseType())
																      .setCodec_param(codec)
																      .setSource_param(callDecoderVideoForwardSet);
			ForwardSetSrcBO callDecoderAudioForwardSet = new ForwardSetSrcBO().setType("channel")
																		 	  .setBundleId(live.getCalledEncoderBundleId())
																		 	  .setLayerId(live.getCalledEncoderLayerId())
																		 	  .setChannelId(live.getCalledEncoderAudioChannelId());
			ConnectBO connectCallDecoderAudioChannel = new ConnectBO().setChannelId(live.getCallDecoderAudioChannelId())
																      .setChannel_status("Open")
																      .setBase_type(live.getCallDecoderAudioBaseType())
																      .setCodec_param(codec)
																      .setSource_param(callDecoderAudioForwardSet);
			
			connectCallDecoderBundle.setChannels(new ArrayListWrapper<ConnectBO>().add(connectCallDecoderVideoChannel).add(connectCallDecoderAudioChannel).getList());
			logic.getConnectBundle().add(connectCallDecoderBundle);
			
			//被叫解码看主叫编码
			
			//呼叫主叫编码
			ConnectBundleBO connectCallEncoderBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
																            .setOperateType(ConnectBundleBO.OPERATE_TYPE)
																		    .setLock_type("write")
																		    .setBundleId(live.getCallEncoderBundleId())
																		    .setLayerId(live.getCallEncoderLayerId())
																		    .setBundle_type(live.getCallEncoderBundleType());
			ConnectBO connectCallEncoderVideoChannel = new ConnectBO().setChannelId(live.getCallEncoderVideoChannelId())
																	    .setChannel_status("Open")
																	    .setBase_type(live.getCallEncoderVideoBaseType())
																	    .setCodec_param(codec);
			ConnectBO connectCallEncoderAudioChannel = new ConnectBO().setChannelId(live.getCallEncoderAudioChannelId())
																	    .setChannel_status("Open")
																	    .setBase_type(live.getCallEncoderAudioBaseType())
																	    .setCodec_param(codec);
			
			connectCallEncoderBundle.setChannels(new ArrayListWrapper<ConnectBO>().add(connectCallEncoderVideoChannel).add(connectCallEncoderAudioChannel).getList());
			logic.getConnectBundle().add(connectCallEncoderBundle);
			
		}
		
		return logic;
	}
	
	/**
	 * 处理closebundle协议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 下午3:13:37
	 * @param MonitorLiveCallPO live 呼叫用户任务
	 * @return LogicBO 协议
	 */
	private LogicBO closeBundle(MonitorLiveCallPO live, Long userId) throws Exception{
		LogicBO logic = new LogicBO().setUserId(userId.toString())
									 .setDisconnectBundle(new ArrayList<DisconnectBundleBO>());
		
		if(LiveType.XT_LOCAL.equals(live.getType()) || LiveType.LOCAL_LOCAL.equals(live.getType())){
			//关闭被叫用户设备
			DisconnectBundleBO disconnectCalledEncoderBundle = new DisconnectBundleBO().setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
																			           .setOperateType(DisconnectBundleBO.OPERATE_TYPE)
																				       .setBundleId(live.getCalledEncoderBundleId())
																				       .setBundle_type(live.getCalledEncoderBundleType())
																				       .setLayerId(live.getCalledEncoderLayerId());
			DisconnectBundleBO disconnectCalledDecoderBundle = new DisconnectBundleBO().setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
																			           .setOperateType(DisconnectBundleBO.OPERATE_TYPE)
																				       .setBundleId(live.getCalledDecoderBundleId())
																				       .setBundle_type(live.getCalledDecoderBundleType())
																				       .setLayerId(live.getCalledDecoderLayerId());
			
			logic.getDisconnectBundle().add(disconnectCalledEncoderBundle);
			logic.getDisconnectBundle().add(disconnectCalledDecoderBundle);
		}
		
		if(LiveType.LOCAL_XT.equals(live.getType()) || LiveType.LOCAL_LOCAL.equals(live.getType())){
			//关闭主叫用户设备
			DisconnectBundleBO disconnectCallEncoderBundle = new DisconnectBundleBO().setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
																			         .setOperateType(DisconnectBundleBO.OPERATE_TYPE)
																				     .setBundleId(live.getCallEncoderBundleId())
																				     .setBundle_type(live.getCallEncoderBundleType())
																				     .setLayerId(live.getCallEncoderLayerId());
			DisconnectBundleBO disconnectCallDecoderBundle = new DisconnectBundleBO().setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
																			         .setOperateType(DisconnectBundleBO.OPERATE_TYPE)
																				     .setBundleId(live.getCallDecoderBundleId())
																				     .setBundle_type(live.getCallDecoderBundleType())
																				     .setLayerId(live.getCallDecoderLayerId());
			logic.getDisconnectBundle().add(disconnectCallEncoderBundle);
			logic.getDisconnectBundle().add(disconnectCallDecoderBundle);
		}
		
		return logic;
	}
	
	/**
	 * 权限校验<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月27日 下午2:20:34
	 * @param Long targetUserId 呼叫目标用户
	 * @param Long userId 操作业务用户
	 */
	private void authorize(Long targetUserId, Long userId) throws Exception{
		boolean authorized = resourceService.hasPrivilegeOfUser(userId, targetUserId, BUSINESS_OPR_TYPE.CALL);
		if(!authorized){
			throw new UserHasNoPermissionForBusinessException(BUSINESS_OPR_TYPE.CALL, 1);
		}
	}
	
}

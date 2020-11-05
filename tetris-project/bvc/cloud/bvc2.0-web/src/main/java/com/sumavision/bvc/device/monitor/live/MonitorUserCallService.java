package com.sumavision.bvc.device.monitor.live;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.PassByBO;
import com.sumavision.bvc.device.group.bo.UserActionPassbyContentBO;
import com.sumavision.bvc.device.group.bo.VenusSeeRelationPassByContentBO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplPO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.monitor.exception.AvtplNotFoundException;
import com.sumavision.bvc.device.monitor.live.exception.BindingDecoderNotFoundException;
import com.sumavision.bvc.device.monitor.live.exception.BindingEncoderNotFoundException;
import com.sumavision.bvc.device.monitor.live.exception.ParamErrorException;
import com.sumavision.bvc.resource.dao.ResourceBundleDAO;
import com.sumavision.bvc.resource.dao.ResourceChannelDAO;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.bvc.system.dao.AvtplDAO;
import com.sumavision.bvc.system.enumeration.AvtplUsageType;
import com.sumavision.bvc.system.po.AvtplGearsPO;
import com.sumavision.bvc.system.po.AvtplPO;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Deprecated
@Service
@Transactional(rollbackFor = Exception.class)
public class MonitorUserCallService {

	@Autowired
	private MonitorLiveService monitorLiveService;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	@Autowired
	private MonitorUserCallDAO monitorUserCallDao;
	
	@Autowired
	private ResourceChannelDAO resourceChannelDao;
	
	@Autowired
	private ResourceBundleDAO resourceBundleDao;
	
	@Autowired
	private AvtplDAO avtplDao;

	@Autowired
	private ResourceService resourceService;
	
	/**
	 * 停止用户双向通话<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 下午4:55:59
	 * @param String srcUser 主叫用户
	 * @param String dstUser 被叫用户
	 * @param String uuid passby透传的uuid
	 * @param Long userId 业务用户id
	 */
	public void stopUserTwoSideCall(String srcUser, String dstUser, String uuid, String type, Long userId) throws Exception{
		Set<Long> liveIds = new HashSet<Long>();
		Set<Long> taskIds = new HashSet<Long>();
		
		if("call".equals(type)){
			List<MonitorUserCallPO> callTasks = monitorUserCallDao.findBySrcUserAndDstUserAndType(srcUser, dstUser, "call");
			if(callTasks!=null && callTasks.size()>0){
				for(MonitorUserCallPO callTask:callTasks){
					if(callTask.getLiveId()!=null) liveIds.add(callTask.getLiveId());
					if(callTask.getReverseLiveId()!=null) liveIds.add(callTask.getReverseLiveId());
					taskIds.add(callTask.getId());
				}
			}
			callTasks = monitorUserCallDao.findBySrcUserAndDstUserAndType(dstUser, srcUser,"call");
			if(callTasks!=null && callTasks.size()>0){
				for(MonitorUserCallPO callTask:callTasks){
					if(callTask.getLiveId()!=null) liveIds.add(callTask.getLiveId());
					if(callTask.getReverseLiveId()!=null) liveIds.add(callTask.getReverseLiveId());
					taskIds.add(callTask.getId());
				}
			}
		}else if("play".equals(type)){
			MonitorUserCallPO existCallTask = monitorUserCallDao.findByUuidAndType(uuid, "play");
			if(existCallTask != null){
				if(existCallTask.getLiveId() != null) liveIds.add(existCallTask.getLiveId());
				if(existCallTask.getReverseLiveId() != null) liveIds.add(existCallTask.getReverseLiveId());
				taskIds.add(existCallTask.getId());
			}
		}
		
		if(liveIds.size()>0){
			for(Long liveId:liveIds){
				monitorLiveService.remove(liveId, userId);
			}
		}
		if(taskIds.size() > 0){
			monitorUserCallDao.deleteByIdIn(taskIds);
		}
	}
	
	/**
	 * 开始用户双向通话<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 下午4:55:59
	 * @param String srcUser 主叫用户
	 * @param String dstUser 被叫用户
	 * @param JSONArray forwards 转发关系
	 * @param String uuid passby透传的uuid
	 * @param Long userId 业务用户id
	 * @return MonitorUserCallPO 用户互通任务
	 */
	public MonitorUserCallPO startUserTwoSideCall(String srcUser, String dstUser, JSONArray forwards, String uuid, String type, Long userId) throws Exception{
		if(forwards == null){
			throw new ParamErrorException(new StringBufferWrapper().append("转发关系为空").toString());
		}
		if(forwards.size()!=2 && forwards.size()!=1){
			throw new ParamErrorException(new StringBufferWrapper().append("转发关系长度不为2也不为1：").append(forwards.size()).toString());
		}
		//清除主叫用户和被叫用户的通话
		Set<Long> liveIds = new HashSet<Long>();
		Set<Long> taskIds = new HashSet<Long>();
		if("call".equals(type)){
			List<MonitorUserCallPO> callTasks = monitorUserCallDao.findBySrcUserOrDstUserAndType(srcUser, "call");
			if(callTasks!=null && callTasks.size()>0){
				for(MonitorUserCallPO callTask:callTasks){
					if(callTask.getLiveId()!=null) liveIds.add(callTask.getLiveId());
					if(callTask.getReverseLiveId()!=null) liveIds.add(callTask.getReverseLiveId());
					taskIds.add(callTask.getId());
				}
			}
			callTasks = monitorUserCallDao.findBySrcUserOrDstUserAndType(dstUser, "call");
			if(callTasks!=null && callTasks.size()>0){
				for(MonitorUserCallPO callTask:callTasks){
					if(callTask.getLiveId()!=null) liveIds.add(callTask.getLiveId());
					if(callTask.getReverseLiveId()!=null) liveIds.add(callTask.getReverseLiveId());
					taskIds.add(callTask.getId());
				}
			}
		}else if("play".equals(type)){
			MonitorUserCallPO existCallTask = monitorUserCallDao.findByUuidAndType(uuid, "play");
			if(existCallTask != null){
				if(existCallTask.getLiveId() != null) liveIds.add(existCallTask.getLiveId());
				if(existCallTask.getReverseLiveId() != null) liveIds.add(existCallTask.getReverseLiveId());
				taskIds.add(existCallTask.getId());
			}
		}
		
		if(liveIds.size()>0){
			for(Long liveId:liveIds){
				monitorLiveService.remove(liveId, userId);
			}
		}
		if(taskIds.size() > 0){
			monitorUserCallDao.deleteByIdIn(taskIds);
		}
		
		MonitorUserCallPO callTask = new MonitorUserCallPO();
		callTask.setUuid(uuid);
		callTask.setSrcUser(srcUser);
		callTask.setDstUser(dstUser);
		callTask.setType(type);
		
		JSONObject forward = forwards.getJSONObject(0);
		String srcBundle = forward.getString("src_bundle");
		String dstBundle = forward.getString("dst_bundle");
		
		List<BundlePO> srcBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(srcBundle).getList());
		BundlePO srcBundleEntity = srcBundleEntities.get(0);
		
		List<ChannelSchemeDTO> srcVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(srcBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_VIDEO);
		ChannelSchemeDTO srcVideoChannel = srcVideoChannels.get(0);
		
		List<ChannelSchemeDTO> srcAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(srcBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_AUDIO);
		ChannelSchemeDTO srcAudioChannel = srcAudioChannels.get(0);
		
		List<BundlePO> dstBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(dstBundle).getList());
		BundlePO dstBundleEntity = dstBundleEntities.get(0);
		
		List<ChannelSchemeDTO> dstVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(dstBundleEntity.getBundleId()).getList(), ResourceChannelDAO.DECODE_VIDEO);
		ChannelSchemeDTO dstVideoChannel = dstVideoChannels.get(0);
		
		List<ChannelSchemeDTO> dstAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(dstBundleEntity.getBundleId()).getList(), ResourceChannelDAO.DECODE_AUDIO);
		ChannelSchemeDTO dstAudioChannel = dstAudioChannels.get(0);
		
		MonitorLivePO live = monitorLiveService.add(null,
			srcBundleEntity.getBundleId(), srcBundleEntity.getBundleName(), srcBundleEntity.getBundleType(), srcBundleEntity.getAccessNodeUid(), srcVideoChannel.getChannelId(), srcVideoChannel.getBaseType(), 
			srcBundleEntity.getBundleId(), srcBundleEntity.getBundleName(), srcBundleEntity.getBundleType(), srcBundleEntity.getAccessNodeUid(), srcAudioChannel.getChannelId(), srcAudioChannel.getBaseType(), 
			dstBundleEntity.getBundleId(), dstBundleEntity.getBundleName(), dstBundleEntity.getBundleType(), dstBundleEntity.getAccessNodeUid(), dstVideoChannel.getChannelId(), dstVideoChannel.getBaseType(), 
			dstBundleEntity.getBundleId(), dstBundleEntity.getBundleName(), dstBundleEntity.getBundleType(), dstBundleEntity.getAccessNodeUid(), dstAudioChannel.getChannelId(), dstAudioChannel.getBaseType(), 
			"USER_TWO_SIDE", userId);
		
		callTask.setLiveId(live.getId());
		
		if(forwards.size() == 2){
			forward = forwards.getJSONObject(1);
			srcBundle = forward.getString("src_bundle");
			dstBundle = forward.getString("dst_bundle");
			
			srcBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(srcBundle).getList());
			srcBundleEntity = srcBundleEntities.get(0);
			
			srcVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(srcBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_VIDEO);
			srcVideoChannel = srcVideoChannels.get(0);
			
			srcAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(srcBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_AUDIO);
			srcAudioChannel = srcAudioChannels.get(0);
			
			dstBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(dstBundle).getList());
			dstBundleEntity = dstBundleEntities.get(0);
			
			dstVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(dstBundleEntity.getBundleId()).getList(), ResourceChannelDAO.DECODE_VIDEO);
			dstVideoChannel = dstVideoChannels.get(0);
			
			dstAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(dstBundleEntity.getBundleId()).getList(), ResourceChannelDAO.DECODE_AUDIO);
			dstAudioChannel = dstAudioChannels.get(0);
			
			live = monitorLiveService.add(null,
					srcBundleEntity.getBundleId(), srcBundleEntity.getBundleName(), srcBundleEntity.getBundleType(), srcBundleEntity.getAccessNodeUid(), srcVideoChannel.getChannelId(), srcVideoChannel.getBaseType(), 
					srcBundleEntity.getBundleId(), srcBundleEntity.getBundleName(), srcBundleEntity.getBundleType(), srcBundleEntity.getAccessNodeUid(), srcAudioChannel.getChannelId(), srcAudioChannel.getBaseType(), 
					dstBundleEntity.getBundleId(), dstBundleEntity.getBundleName(), dstBundleEntity.getBundleType(), dstBundleEntity.getAccessNodeUid(), dstVideoChannel.getChannelId(), dstVideoChannel.getBaseType(), 
					dstBundleEntity.getBundleId(), dstBundleEntity.getBundleName(), dstBundleEntity.getBundleType(), dstBundleEntity.getAccessNodeUid(), dstAudioChannel.getChannelId(), dstAudioChannel.getBaseType(), 
					"USER_TWO_SIDE", userId);
				
			callTask.setReverseLiveId(live.getId());
		}
		
		monitorUserCallDao.save(callTask);
		
		return callTask;
	}
	
	/**
	 * 向接入层透传用户被叫后操作<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 下午8:00:13
	 * @param String layerId 用户绑定设备接入层id
	 * @param String ldapUser 主叫用户
	 * @param String localUser 被叫用户
	 * @param int action 操作结果:1接听, 2拒绝, 3没登录, 4没有这个用户, 5用户正忙, 6超时
	 * @param Long userId 业务用户id
	 */
	public void sendUserTwoSideCallActionWithPassBy(
			String layerId, 
			String ldapUser, 
			String localUser, 
			int action,
			Long userId) throws Exception{
		
		String[] actions = new String[]{"接听", "拒绝", "没登录", "没有这个用户", "用户正忙", "超时"};
		
		LogicBO logic = new LogicBO();
		logic.setUserId(userId.toString());
		logic.setPass_by(new ArrayList<PassByBO>());
		
		PassByBO passBy = new PassByBO();
		passBy.setLayer_id(layerId);
		passBy.setType("venus_invite_user_action");
		UserActionPassbyContentBO passbyContent = new UserActionPassbyContentBO();
		passbyContent.setLdap_user(ldapUser);
		passbyContent.setLocal_user(localUser);
		passbyContent.setAction(action);
		passBy.setPass_by_content(passbyContent);
		logic.getPass_by().add(passBy);
		
		executeBusiness.execute(logic, new StringBufferWrapper().append("点播系统：用户被叫后操作，")
															    .append(action)
															    .append("(").append(actions[action-1]).append(")")
															    .toString());
	}
	
	/**
	 * bvc系统发起用户双向通信主叫<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 下午8:24:27
	 * @param Long callUserId bvc用户id
	 * @param String callUsername bvc用户名
	 * @param String callUserno bvc用户号码
	 * @param UserBO receiveUser xt用户 （被叫）
	 */
	public void sendUserTwoSideCallStartWithPassBy(
			Long callUserId,
			String callUsername,
			String callUserno,
			UserBO receiveUser) throws Exception{
		AvtplPO targetAvtpl = null;
		AvtplGearsPO targetGear = null;
		//查询codec参数
		List<AvtplPO> avTpls = avtplDao.findByUsageType(AvtplUsageType.VOD);
		if(avTpls==null || avTpls.size()<=0){
			throw new AvtplNotFoundException("系统缺少点播系统参数模板！");
		}
		targetAvtpl = avTpls.get(0);
		//查询codec模板档位
		Set<AvtplGearsPO> gears = targetAvtpl.getGears();
		for(AvtplGearsPO gear:gears){
			targetGear = gear;
			break;
		}
		
		//本地编码
		BundlePO localEncoder = resourceService.queryEncoderByUsername(callUsername);
		if(localEncoder == null){
			throw new BindingEncoderNotFoundException(callUsername);
		}
		
		List<ChannelSchemeDTO> localEncodeVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(localEncoder.getBundleId()).getList(), ResourceChannelDAO.ENCODE_VIDEO);
		ChannelSchemeDTO localEncodeVideoChannel = localEncodeVideoChannels.get(0);
		
		List<ChannelSchemeDTO> localEncodeAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(localEncoder.getBundleId()).getList(), ResourceChannelDAO.ENCODE_AUDIO);
		ChannelSchemeDTO localEncodeAudioChannel = localEncodeAudioChannels.get(0);
		
		//本地解码
		BundlePO localDecoder = resourceService.queryDecoderByUsername(callUsername);
		if(localDecoder == null){
			throw new BindingDecoderNotFoundException(callUsername);
		}
		
		List<ChannelSchemeDTO> localDecodeVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(localDecoder.getBundleId()).getList(), ResourceChannelDAO.DECODE_VIDEO);
		ChannelSchemeDTO localDecodeVideoChannel = localDecodeVideoChannels.get(0);
		
		List<ChannelSchemeDTO> localDecodeAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(localDecoder.getBundleId()).getList(), ResourceChannelDAO.DECODE_AUDIO);
		ChannelSchemeDTO localDecodeAudioChannel = localDecodeAudioChannels.get(0);
		
		//xt编码
		BundlePO xtEncoder = resourceService.queryLdapEncoderByUserNo(receiveUser.getUserNo());
		if(xtEncoder == null){
			throw new BindingEncoderNotFoundException(receiveUser.getName());
		}
		
		List<ChannelSchemeDTO> xtEncodeVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(xtEncoder.getBundleId()).getList(), ResourceChannelDAO.ENCODE_VIDEO);
		ChannelSchemeDTO xtEncodeVideoChannel = xtEncodeVideoChannels.get(0);
		
		List<ChannelSchemeDTO> xtEncodeAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(xtEncoder.getBundleId()).getList(), ResourceChannelDAO.ENCODE_AUDIO);
		ChannelSchemeDTO xtEncodeAudioChannel = xtEncodeAudioChannels.get(0);
		
		//xt解码
		BundlePO xtDecoder = resourceService.queryLdapDecoderByUserNo(receiveUser.getUserNo());
		if(xtDecoder == null){
			throw new BindingDecoderNotFoundException(receiveUser.getName());
		}
		
		List<ChannelSchemeDTO> xtDecodeVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(xtDecoder.getBundleId()).getList(), ResourceChannelDAO.DECODE_VIDEO);
		ChannelSchemeDTO xtDecodeVideoChannel = xtDecodeVideoChannels.get(0);
		
		List<ChannelSchemeDTO> xtDecodeAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(xtDecoder.getBundleId()).getList(), ResourceChannelDAO.DECODE_AUDIO);
		ChannelSchemeDTO xtDecodeAudioChannel = xtDecodeAudioChannels.get(0);
		
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
		
		LogicBO logic = new LogicBO().setUserId(callUserId.toString())
				 					 .setPass_by(new ArrayList<PassByBO>());

		VenusSeeRelationPassByContentBO passByContent = new VenusSeeRelationPassByContentBO().setType("call")
																							 .setUuid(UUID.randomUUID().toString())
																							 .setOperate("start")
																							 .setVparam(codec);
		
		//本地用户
		JSONObject venus_user = new JSONObject();
		JSONObject venus_encoder = new JSONObject();
		JSONObject venus_encoder_vsource = new JSONObject();
		JSONObject venus_encoder_asource = new JSONObject();
		JSONObject venus_decoder = new JSONObject();
		JSONObject venus_decoder_vsource = new JSONObject();
		JSONObject venus_decoder_asource = new JSONObject();
		
		venus_user.put("username", callUsername);
		venus_user.put("userno", callUserno);
		
		venus_user.put("encoder", venus_encoder);
		venus_encoder.put("vsource", venus_encoder_vsource);
		venus_encoder.put("asource", venus_encoder_asource);
		
		venus_encoder_vsource.put("layerid", localEncoder.getAccessNodeUid());
		venus_encoder_vsource.put("bundleid", localEncoder.getBundleId());
		venus_encoder_vsource.put("channelid", localEncodeVideoChannel.getChannelId());
		
		venus_encoder_asource.put("layerid", localEncoder.getAccessNodeUid());
		venus_encoder_asource.put("bundleid", localEncoder.getBundleId());
		venus_encoder_asource.put("channelid", localEncodeAudioChannel.getChannelId());
		
		venus_user.put("decoder", venus_decoder);
		venus_decoder.put("vsource", venus_decoder_vsource);
		venus_decoder.put("asource", venus_decoder_asource);
		
		venus_decoder_vsource.put("layerid", localDecoder.getAccessNodeUid());
		venus_decoder_vsource.put("bundleid", localDecoder.getBundleId());
		venus_decoder_vsource.put("channelid", localDecodeVideoChannel.getChannelId());
		
		venus_decoder_asource.put("layerid", localDecoder.getAccessNodeUid());
		venus_decoder_asource.put("bundleid", localDecoder.getBundleId());
		venus_decoder_asource.put("channelid", localDecodeAudioChannel.getChannelId());
		
		//xt用户
		JSONObject xt_user = new JSONObject();
		JSONObject xt_encoder = new JSONObject();
		JSONObject xt_encoder_vsource = new JSONObject();
		JSONObject xt_encoder_asource = new JSONObject();
		JSONObject xt_decoder = new JSONObject();
		JSONObject xt_decoder_vsource = new JSONObject();
		JSONObject xt_decoder_asource = new JSONObject();
		
		xt_user.put("username", receiveUser.getName());
		xt_user.put("userno", receiveUser.getUserNo());
		
		xt_user.put("encoder", xt_encoder);
		xt_encoder.put("vsource", xt_encoder_vsource);
		xt_encoder.put("asource", xt_encoder_asource);
		
		xt_encoder_vsource.put("layerid", xtEncoder.getAccessNodeUid());
		xt_encoder_vsource.put("bundleid", xtEncoder.getBundleId());
		xt_encoder_vsource.put("channelid", xtEncodeVideoChannel.getChannelId());
		
		xt_encoder_asource.put("layerid", xtEncoder.getAccessNodeUid());
		xt_encoder_asource.put("bundleid", xtEncoder.getBundleId());
		xt_encoder_asource.put("channelid", xtEncodeAudioChannel.getChannelId());
		
		xt_user.put("decoder", xt_decoder);
		xt_decoder.put("vsource", xt_decoder_vsource);
		xt_decoder.put("asource", xt_decoder_asource);
		
		xt_decoder_vsource.put("layerid", xtDecoder.getAccessNodeUid());
		xt_decoder_vsource.put("bundleid", xtDecoder.getBundleId());
		xt_decoder_vsource.put("channelid", xtDecodeVideoChannel.getChannelId());
		
		xt_decoder_asource.put("layerid", xtDecoder.getAccessNodeUid());
		xt_decoder_asource.put("bundleid", xtDecoder.getBundleId());
		xt_decoder_asource.put("channelid", xtDecodeAudioChannel.getChannelId());
		
		passByContent.setVenus_user(venus_user)
		 			 .setRelation_user(xt_user);
		
		PassByBO passBy = new PassByBO().setLayer_id(xtEncoder.getAccessNodeUid())
									    .setType("venus_see_relation")
									    .setPass_by_content(passByContent);
		
		logic.getPass_by().add(passBy);
		
		executeBusiness.execute(logic, "点播系统：发起呼叫xt用户");
	}
	
	/**
	 * bvc系统停止用户双向通信主叫<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 下午8:24:27
	 * @param Long callUserId bvc用户id
	 * @param String callUsername bvc用户名
	 * @param String callUserno bvc用户号码
	 * @param UserBO receiveUser xt用户 （被叫）
	 */
	public void sendUserTwoSideCallStopWithPassBy(
			Long callUserId,
			String callUsername,
			String callUserno,
			UserBO receiveUser) throws Exception{
		
		AvtplPO targetAvtpl = null;
		AvtplGearsPO targetGear = null;
		//查询codec参数
		List<AvtplPO> avTpls = avtplDao.findByUsageType(AvtplUsageType.VOD);
		if(avTpls==null || avTpls.size()<=0){
			throw new AvtplNotFoundException("系统缺少点播系统参数模板！");
		}
		targetAvtpl = avTpls.get(0);
		//查询codec模板档位
		Set<AvtplGearsPO> gears = targetAvtpl.getGears();
		for(AvtplGearsPO gear:gears){
			targetGear = gear;
			break;
		}
		
		//本地编码
		BundlePO localEncoder = resourceService.queryEncoderByUsername(callUsername);
		if(localEncoder == null){
			throw new BindingEncoderNotFoundException(callUsername);
		}
		
		List<ChannelSchemeDTO> localEncodeVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(localEncoder.getBundleId()).getList(), ResourceChannelDAO.ENCODE_VIDEO);
		ChannelSchemeDTO localEncodeVideoChannel = localEncodeVideoChannels.get(0);
		
		List<ChannelSchemeDTO> localEncodeAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(localEncoder.getBundleId()).getList(), ResourceChannelDAO.ENCODE_AUDIO);
		ChannelSchemeDTO localEncodeAudioChannel = localEncodeAudioChannels.get(0);
		
		//本地解码
		BundlePO localDecoder = resourceService.queryDecoderByUsername(callUsername);
		if(localDecoder == null){
			throw new BindingDecoderNotFoundException(callUsername);
		}
		
		List<ChannelSchemeDTO> localDecodeVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(localDecoder.getBundleId()).getList(), ResourceChannelDAO.DECODE_VIDEO);
		ChannelSchemeDTO localDecodeVideoChannel = localDecodeVideoChannels.get(0);
		
		List<ChannelSchemeDTO> localDecodeAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(localDecoder.getBundleId()).getList(), ResourceChannelDAO.DECODE_AUDIO);
		ChannelSchemeDTO localDecodeAudioChannel = localDecodeAudioChannels.get(0);
		
		//xt编码
		BundlePO xtEncoder = resourceService.queryLdapEncoderByUserNo(receiveUser.getUserNo());
		if(xtEncoder == null){
			throw new BindingEncoderNotFoundException(receiveUser.getName());
		}
		
		List<ChannelSchemeDTO> xtEncodeVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(xtEncoder.getBundleId()).getList(), ResourceChannelDAO.ENCODE_VIDEO);
		ChannelSchemeDTO xtEncodeVideoChannel = xtEncodeVideoChannels.get(0);
		
		List<ChannelSchemeDTO> xtEncodeAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(xtEncoder.getBundleId()).getList(), ResourceChannelDAO.ENCODE_AUDIO);
		ChannelSchemeDTO xtEncodeAudioChannel = xtEncodeAudioChannels.get(0);
		
		//xt解码
		BundlePO xtDecoder = resourceService.queryLdapDecoderByUserNo(receiveUser.getUserNo());
		if(xtDecoder == null){
			throw new BindingDecoderNotFoundException(receiveUser.getName());
		}
		
		List<ChannelSchemeDTO> xtDecodeVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(xtDecoder.getBundleId()).getList(), ResourceChannelDAO.DECODE_VIDEO);
		ChannelSchemeDTO xtDecodeVideoChannel = xtDecodeVideoChannels.get(0);
		
		List<ChannelSchemeDTO> xtDecodeAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(xtDecoder.getBundleId()).getList(), ResourceChannelDAO.DECODE_AUDIO);
		ChannelSchemeDTO xtDecodeAudioChannel = xtDecodeAudioChannels.get(0);
		
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
		
		LogicBO logic = new LogicBO().setUserId(callUserId.toString())
				 					 .setPass_by(new ArrayList<PassByBO>());

		VenusSeeRelationPassByContentBO passByContent = new VenusSeeRelationPassByContentBO().setType("call")
																							 .setOperate("stop")
																							 .setVparam(codec);
		
		//本地用户
		JSONObject venus_user = new JSONObject();
		JSONObject venus_encoder = new JSONObject();
		JSONObject venus_encoder_vsource = new JSONObject();
		JSONObject venus_encoder_asource = new JSONObject();
		JSONObject venus_decoder = new JSONObject();
		JSONObject venus_decoder_vsource = new JSONObject();
		JSONObject venus_decoder_asource = new JSONObject();
		
		venus_user.put("username", callUsername);
		venus_user.put("userno", callUserno);
		
		venus_user.put("encoder", venus_encoder);
		venus_encoder.put("vsource", venus_encoder_vsource);
		venus_encoder.put("asource", venus_encoder_asource);
		
		venus_encoder_vsource.put("layerid", localEncoder.getAccessNodeUid());
		venus_encoder_vsource.put("bundleid", localEncoder.getBundleId());
		venus_encoder_vsource.put("channelid", localEncodeVideoChannel.getChannelId());
		
		venus_encoder_asource.put("layerid", localEncoder.getAccessNodeUid());
		venus_encoder_asource.put("bundleid", localEncoder.getBundleId());
		venus_encoder_asource.put("channelid", localEncodeAudioChannel.getChannelId());
		
		venus_user.put("decoder", venus_decoder);
		venus_decoder.put("vsource", venus_decoder_vsource);
		venus_decoder.put("asource", venus_decoder_asource);
		
		venus_decoder_vsource.put("layerid", localDecoder.getAccessNodeUid());
		venus_decoder_vsource.put("bundleid", localDecoder.getBundleId());
		venus_decoder_vsource.put("channelid", localDecodeVideoChannel.getChannelId());
		
		venus_decoder_asource.put("layerid", localDecoder.getAccessNodeUid());
		venus_decoder_asource.put("bundleid", localDecoder.getBundleId());
		venus_decoder_asource.put("channelid", localDecodeAudioChannel.getChannelId());
		
		//xt用户
		JSONObject xt_user = new JSONObject();
		JSONObject xt_encoder = new JSONObject();
		JSONObject xt_encoder_vsource = new JSONObject();
		JSONObject xt_encoder_asource = new JSONObject();
		JSONObject xt_decoder = new JSONObject();
		JSONObject xt_decoder_vsource = new JSONObject();
		JSONObject xt_decoder_asource = new JSONObject();
		
		xt_user.put("username", receiveUser.getName());
		xt_user.put("userno", receiveUser.getUserNo());
		
		xt_user.put("encoder", xt_encoder);
		xt_encoder.put("vsource", xt_encoder_vsource);
		xt_encoder.put("asource", xt_encoder_asource);
		
		xt_encoder_vsource.put("layerid", xtEncoder.getAccessNodeUid());
		xt_encoder_vsource.put("bundleid", xtEncoder.getBundleId());
		xt_encoder_vsource.put("channelid", xtEncodeVideoChannel.getChannelId());
		
		xt_encoder_asource.put("layerid", xtEncoder.getAccessNodeUid());
		xt_encoder_asource.put("bundleid", xtEncoder.getBundleId());
		xt_encoder_asource.put("channelid", xtEncodeAudioChannel.getChannelId());
		
		xt_user.put("decoder", xt_decoder);
		xt_decoder.put("vsource", xt_decoder_vsource);
		xt_decoder.put("asource", xt_decoder_asource);
		
		xt_decoder_vsource.put("layerid", xtDecoder.getAccessNodeUid());
		xt_decoder_vsource.put("bundleid", xtDecoder.getBundleId());
		xt_decoder_vsource.put("channelid", xtDecodeVideoChannel.getChannelId());
		
		xt_decoder_asource.put("layerid", xtDecoder.getAccessNodeUid());
		xt_decoder_asource.put("bundleid", xtDecoder.getBundleId());
		xt_decoder_asource.put("channelid", xtDecodeAudioChannel.getChannelId());
		
		passByContent.setVenus_user(venus_user)
		 			 .setRelation_user(xt_user);
		
		PassByBO passBy = new PassByBO().setLayer_id(xtEncoder.getAccessNodeUid())
									    .setType("venus_see_relation")
									    .setPass_by_content(passByContent);
		
		logic.getPass_by().add(passBy);
		
		executeBusiness.execute(logic, "点播系统：停止呼叫xt用户");
	}
	
}

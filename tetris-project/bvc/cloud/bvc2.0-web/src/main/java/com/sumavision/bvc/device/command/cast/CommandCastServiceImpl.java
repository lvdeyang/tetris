package com.sumavision.bvc.device.command.cast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.command.group.basic.CommandGroupAvtplGearsPO;
import com.sumavision.bvc.command.group.basic.CommandGroupMemberPO;
import com.sumavision.bvc.command.group.basic.CommandGroupPO;
import com.sumavision.bvc.command.group.dao.CommandGroupForwardDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupForwardDemandDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupUserPlayerDAO;
import com.sumavision.bvc.command.group.dao.CommandVodDAO;
import com.sumavision.bvc.command.group.dao.UserLiveCallDAO;
import com.sumavision.bvc.command.group.enumeration.CallStatus;
import com.sumavision.bvc.command.group.enumeration.CallType;
import com.sumavision.bvc.command.group.enumeration.ExecuteStatus;
import com.sumavision.bvc.command.group.enumeration.ForwardDemandBusinessType;
import com.sumavision.bvc.command.group.enumeration.ForwardDemandStatus;
import com.sumavision.bvc.command.group.enumeration.MediaType;
import com.sumavision.bvc.command.group.enumeration.VodType;
import com.sumavision.bvc.command.group.forward.CommandGroupForwardDemandPO;
import com.sumavision.bvc.command.group.forward.CommandGroupForwardPO;
import com.sumavision.bvc.command.group.user.CommandGroupUserInfoPO;
import com.sumavision.bvc.command.group.user.UserLiveCallPO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerCastDevicePO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.command.group.user.layout.player.PlayerBusinessType;
import com.sumavision.bvc.command.group.vod.CommandVodPO;
import com.sumavision.bvc.device.command.bo.PlayerInfoBO;
import com.sumavision.bvc.device.command.common.CommandCommonConstant;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.ConnectBundleBO;
import com.sumavision.bvc.device.group.bo.DisconnectBundleBO;
import com.sumavision.bvc.device.group.bo.ForwardDelBO;
import com.sumavision.bvc.device.group.bo.ForwardSetBO;
import com.sumavision.bvc.device.group.bo.ForwardSetSrcBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.MediaPushSetBO;
import com.sumavision.bvc.device.group.bo.OsdWrapperBO;
import com.sumavision.bvc.device.group.bo.PassByBO;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplPO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.monitor.osd.MonitorOsdDAO;
import com.sumavision.bvc.device.monitor.osd.MonitorOsdPO;
import com.sumavision.bvc.device.monitor.osd.MonitorOsdService;
import com.sumavision.bvc.resource.dao.ResourceBundleDAO;
import com.sumavision.bvc.resource.dao.ResourceChannelDAO;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.bvc.system.po.AvtplGearsPO;
import com.sumavision.bvc.system.po.AvtplPO;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashSetWrapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 
* @ClassName: CommandCastServiceImpl 
* @Description: 播放器关联上屏业务<br>
* mediaPushSet的uuid规则（没有空格）：PO的uuid @@ 上屏设备PO的id
* @author zsy
* @date 2019年11月22日 上午10:56:48 
*
 */
@Slf4j
@Transactional(rollbackFor = Exception.class)
@Service
public class CommandCastServiceImpl {
	
	@Autowired
	private CommandGroupUserPlayerDAO commandGroupUserPlayerDao;
	
	@Autowired
	private CommandVodDAO commandVodDao;
	
	@Autowired
	private UserLiveCallDAO userLiveCallDao;
	
	@Autowired
	private CommandGroupForwardDAO commandGroupForwardDao;
	
	@Autowired
	private CommandGroupForwardDemandDAO commandGroupForwardDemandDao;
	
	@Autowired
	private MonitorOsdDAO monitorOsdDao;
	
	@Autowired
	private ResourceBundleDAO resourceBundleDao;
	
	@Autowired
	private ResourceChannelDAO resourceChannelDao;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private CommandCommonServiceImpl commandCommonServiceImpl;
	
	@Autowired
	private MonitorOsdService monitorOsdService;
	
	@Autowired
	private CommandCommonUtil commandCommonUtil;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	/**
	 * 全量设置播放器的上屏设备<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月26日 上午10:32:54
	 * @param player
	 * @param bundleIds 全量
	 * @throws Exception
	 */
	public void setCastDevices(
			CommandGroupUserPlayerPO player,
			List<String> bundleIds) throws Exception{
		
		if(player.getCastDevices() == null) player.setCastDevices(new ArrayList<CommandGroupUserPlayerCastDevicePO>());
		List<CommandGroupUserPlayerCastDevicePO> castDevices = player.getCastDevices();
		CommandGroupUserInfoPO userInfo = player.getUserInfo();
		
		List<String> addBundleIds = new ArrayList<String>();
		List<CommandGroupUserPlayerCastDevicePO> addDevices = new ArrayList<CommandGroupUserPlayerCastDevicePO>();
		List<CommandGroupUserPlayerCastDevicePO> removeDevices = new ArrayList<CommandGroupUserPlayerCastDevicePO>();
		
		//查找需要删掉的设备
		for(CommandGroupUserPlayerCastDevicePO castDevice : castDevices){
			if(!bundleIds.contains(castDevice.getDstBundleId())){
				removeDevices.add(castDevice);
			}
		}
		
		//查找新的设备
		for(String bundleId : bundleIds){
			boolean has = false;
			for(CommandGroupUserPlayerCastDevicePO castDevice : castDevices){
				if(castDevice.getDstBundleId().equals(bundleId)){
					has = true;
					break;
				}
			}
			if(!has){
				addBundleIds.add(bundleId);
			}
		}
		
		//生成addDevices
		//从bundleId列表查询所有的bundlePO
		List<BundlePO> srcBundleEntities = resourceBundleDao.findByBundleIds(bundleIds);
		
		//从bundleId列表查询所有的视频编码1通道
		List<ChannelSchemeDTO> videoEncode1Channels = resourceChannelDao.findByBundleIdsAndChannelId(bundleIds, ChannelType.VIDEODECODE1.getChannelId());
		
		//从bundleId列表查询所有的音频编码1通道
		List<ChannelSchemeDTO> audioEncode1Channels = resourceChannelDao.findByBundleIdsAndChannelId(bundleIds, ChannelType.AUDIODECODE1.getChannelId());
		
		for(String addBundleId : addBundleIds){
			for(BundlePO bundle : srcBundleEntities){
				if(bundle.getBundleId().equals(addBundleId)){
					CommandGroupUserPlayerCastDevicePO device = new CommandGroupUserPlayerCastDevicePO();
					device.setUserId(userInfo.getUserId());
					device.setUserName(userInfo.getUserName());
					device.setFolderId(bundle.getFolderId());
					device.setDstBundleId(bundle.getBundleId());
					device.setDstLayerId(bundle.getAccessNodeUid());
					device.setDstBundleName(bundle.getBundleName());
					device.setDstBundleType(bundle.getDeviceModel());
					device.setDstVenusBundleType(bundle.getBundleType());
					//遍历视频通道
					for(ChannelSchemeDTO videoChannel : videoEncode1Channels){
						if(addBundleId.equals(videoChannel.getBundleId())){
							device.setDstVideoChannelId(videoChannel.getChannelId());
							break;
						}
					}					
					//遍历音频通道
					for(ChannelSchemeDTO audioChannel : audioEncode1Channels){
						if(addBundleId.equals(audioChannel.getBundleId())){
							device.setDstAudioChannelId(audioChannel.getChannelId());
							break;
						}
					}
					addDevices.add(device);
					break;
				}
			}
		}
		
		//持久化并下发logic协议
		changeCastDevices2(player, addDevices, removeDevices, true, false);
		
	}
	
	/**
	 * 编辑播放器的上屏设备<br/>
	 * <p>增加和删除</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月27日 下午2:47:54
	 * @param player
	 * @param addBundleIds
	 * @param removeBundleIds
	 * @throws Exception
	 */
	public void editCastDevices(
			CommandGroupUserPlayerPO player,
			List<String> addBundleIds,
			List<String> removeBundleIds) throws Exception{
		
		List<String> bundleIds = new ArrayList<String>();
		for(CommandGroupUserPlayerCastDevicePO device : player.getCastDevices()){
			bundleIds.add(device.getDstBundleId());
		}
		if(removeBundleIds != null) bundleIds.removeAll(removeBundleIds);
		if(addBundleIds != null) bundleIds.addAll(addBundleIds);
		setCastDevices(player, bundleIds);
	}

	/**
	 * 修改播放器的上屏设备<br/>
	 * <p>包含持久化和下发logic协议</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月26日 上午10:31:16
	 * @param player
	 * @param addDevices
	 * @param removeDevices 必须来自player.getCastDevices()
	 * @throws Exception
	 */
	/**@Deprecated
	private void changeCastDevices1(
			CommandGroupUserPlayerPO player,
			List<CommandGroupUserPlayerCastDevicePO> addDevices,
			List<CommandGroupUserPlayerCastDevicePO> removeDevices) throws Exception{
		
		if(player.getCastDevices() == null) player.setCastDevices(new ArrayList<CommandGroupUserPlayerCastDevicePO>());
		if(null == addDevices) addDevices = new ArrayList<CommandGroupUserPlayerCastDevicePO>();
		if(null == removeDevices) removeDevices = new ArrayList<CommandGroupUserPlayerCastDevicePO>();
		
		//考虑判重
		
		//保存获得id
		for(CommandGroupUserPlayerCastDevicePO addDevice : addDevices){
			addDevice.setPlayer(player);
		}
		player.getCastDevices().removeAll(removeDevices);
		player.getCastDevices().addAll(addDevices);
		commandGroupUserPlayerDao.save(player);
		
//		UserBO admin = resourceService.queryUserInfoByUsername(CommandCommonConstant.USER_NAME);
		UserBO admin = new UserBO(); admin.setId(-1L);
		
		LogicBO logic = new LogicBO().setUserId(admin.getId().toString())
		 		 .setConnectBundle(new ArrayList<ConnectBundleBO>())
		 		 .setForwardSet(new ArrayList<ForwardSetBO>())
		 		 .setForwardDel(new ArrayList<ForwardDelBO>())
		 		 .setMediaPushSet(new ArrayList<MediaPushSetBO>())
		 		 .setDisconnectBundle(new ArrayList<DisconnectBundleBO>())
		 		 .setMediaPushDel(new ArrayList<MediaPushSetBO>());
		
		String description = "";
		
		//查找给播放器的vod转发
		CommandVodPO vod = commandVodDao.findByDstBundleId(player.getBundleId());
		if(vod != null){
			
			description = "，有一个相关点播vod，id：" + vod.getId();
			
			Map<String, Object> result = commandCommonServiceImpl.queryDefaultAvCodec();
			AvtplPO targetAvtpl = (AvtplPO)result.get("avtpl");
			AvtplGearsPO targetGear = (AvtplGearsPO)result.get("gear");
			CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
			
			//给新增设备生成forwardSet
			LogicBO logicVodStart = openBundleCastDeviceWithRestrict2(null, null, null, null, new ArrayListWrapper<CommandVodPO>().add(vod).getList(), null, null, codec, admin.getId(), addDevices);
			logic.merge(logicVodStart);
			
			//给删除设备生成disconnectBundle
			LogicBO logicVodStop = closeBundleCastDeviceWithRestrict1(null, null, null, null, codec, admin.getId(), removeDevices);
			logic.merge(logicVodStop);
		}
		
		//待测：查找给播放器的call，从呼叫方与被叫方各查一遍，最多只会有一个
		UserLiveCallPO call = userLiveCallDao.findByCalledDecoderBundleId(player.getBundleId());
		if(call == null){
			call = userLiveCallDao.findByCallDecoderBundleId(player.getBundleId());
		}
		if(call != null){
			
			description = "，有一个相关呼叫call，id：" + call.getId();
			
			Map<String, Object> result = commandCommonServiceImpl.queryDefaultAvCodec();
			AvtplPO targetAvtpl = (AvtplPO)result.get("avtpl");
			AvtplGearsPO targetGear = (AvtplGearsPO)result.get("gear");
			CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
			
			//给新增设备生成forwardSet
			LogicBO logicCallStart = openBundleCastDeviceWithRestrict2(null, null, null, null, null, new ArrayListWrapper<UserLiveCallPO>().add(call).getList(), codec, admin.getId(), addDevices);
			logic.merge(logicCallStart);
			
			//给删除设备生成disconnectBundle
			LogicBO logicCallStop = closeBundleCastDeviceWithRestrict1(null, null, null, null, codec, admin.getId(), removeDevices);
			logic.merge(logicCallStop);
		}
		
		//查找给播放器的转发
		CommandGroupForwardPO forward = commandGroupForwardDao.findByDstVideoBundleIdAndExecuteStatus(player.getBundleId(), ExecuteStatus.DONE);
		if(forward != null){
			
			description = "，有一个相关指挥/会议转发forward，id：" + forward.getId();
			
			CommandGroupPO group = forward.getGroup();
			CommandGroupAvtplGearsPO currentGear = commandCommonUtil.queryCurrentGear(group);
			CodecParamBO codec = new CodecParamBO().set(group.getAvtpl(), currentGear);
			
			//给新增设备生成forwardSet
			LogicBO logic1 = openBundleCastDeviceWithRestrict2(null, null, new HashSetWrapper<CommandGroupForwardPO>().add(forward).getSet(), null, null, null, codec, admin.getId(), addDevices);
			logic.merge(logic1);
			
			//给删除设备生成disconnectBundle
			LogicBO logic2 = closeBundleCastDeviceWithRestrict1(null, null, null, null, codec, admin.getId(), removeDevices);
			logic.merge(logic2);
		}
		
		//查找给播放器的转发点播
		CommandGroupForwardDemandPO demand = commandGroupForwardDemandDao.findByDstVideoBundleIdAndExecuteStatus(player.getBundleId(), ForwardDemandStatus.DONE);
		if(demand != null){
			
			description = "，有一个相关指挥转发点播资源demand，id：" + demand.getId();
			
			CommandGroupPO group = demand.getGroup();
			CommandGroupAvtplGearsPO currentGear = commandCommonUtil.queryCurrentGear(group);
			CodecParamBO codec = new CodecParamBO().set(group.getAvtpl(), currentGear);

			//给新增设备生成forwardSet和mediaPushSet
			LogicBO logic3 = openBundleCastDeviceWithRestrict1(new ArrayListWrapper<CommandGroupForwardDemandPO>().add(demand).getList(), null, null, null, null, codec, admin.getId(), addDevices);
			logic.merge(logic3);
			
			//给删除设备生成mediaPushDel和disconnectBundle
			LogicBO logic4 = closeBundleCastDeviceWithRestrict1(new ArrayListWrapper<CommandGroupForwardDemandPO>().add(demand).getList(), null, null, null, codec, admin.getId(), removeDevices);
			logic.merge(logic4);
		}
		
		executeBusiness.execute(logic, "播放器修改上屏列表" + description);
	}*/
	
	/**
	 * 修改播放器的上屏设备<br/>
	 * <p>包含持久化和下发logic协议</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月26日 上午10:31:16
	 * @param player
	 * @param addDevices
	 * @param removeDevices 必须来自player.getCastDevices()
	 * @param doLogic 是否下logic协议，只查询播放器源的时候用false
	 * @param getPlayerSrcInfo 修改播放器的上屏设备时应使用false；true表示查找播放器源信息
	 * @throws Exception
	 */
	public PlayerInfoBO changeCastDevices2(
			CommandGroupUserPlayerPO player,
			List<CommandGroupUserPlayerCastDevicePO> addDevices,
			List<CommandGroupUserPlayerCastDevicePO> removeDevices,
			boolean doLogic,
			boolean getPlayerSrcInfo) throws Exception{
		
		if(player.getCastDevices() == null) player.setCastDevices(new ArrayList<CommandGroupUserPlayerCastDevicePO>());
		if(null == addDevices) addDevices = new ArrayList<CommandGroupUserPlayerCastDevicePO>();
		if(null == removeDevices) removeDevices = new ArrayList<CommandGroupUserPlayerCastDevicePO>();
		PlayerInfoBO playerInfoBO = new PlayerInfoBO();
		
		//TODO:判重
		
		//保存获得id
		for(CommandGroupUserPlayerCastDevicePO addDevice : addDevices){
			addDevice.setPlayer(player);
		}
		player.getCastDevices().removeAll(removeDevices);
		player.getCastDevices().addAll(addDevices);
		commandGroupUserPlayerDao.save(player);
		
//		UserBO admin = resourceService.queryUserInfoByUsername(CommandCommonConstant.USER_NAME);
		UserBO admin = new UserBO(); admin.setId(-1L);
		
		LogicBO logic = new LogicBO().setUserId(admin.getId().toString())
		 		 .setConnectBundle(new ArrayList<ConnectBundleBO>())
		 		 .setForwardSet(new ArrayList<ForwardSetBO>())
		 		 .setForwardDel(new ArrayList<ForwardDelBO>())
		 		 .setMediaPushSet(new ArrayList<MediaPushSetBO>())
		 		 .setDisconnectBundle(new ArrayList<DisconnectBundleBO>())
		 		 .setMediaPushDel(new ArrayList<MediaPushSetBO>())
		 		 .setPass_by(new ArrayList<PassByBO>());
		
		String description = "";
		
		//如果播放器在播放文件
		if(player.playingFile()){
			
			PlayerBusinessType businessType = player.getPlayerBusinessType();
			description = "，有一个相关文件播放，类型：" + businessType + "playerUrl: " + player.getPlayUrl();
			CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();
			
			//给新增设备生成forwardSet
			LogicBO logicPlayFileStart = openBundleCastDeviceWithRestrict2(null, new ArrayListWrapper<CommandGroupUserPlayerPO>().add(player).getList(), null, null, null, null, null, codec, admin.getId(), addDevices);
			logic.merge(logicPlayFileStart);
			
			//给删除设备生成disconnectBundle
			LogicBO logicPlayFileStop = closeBundleCastDeviceWithRestrict2(new ArrayListWrapper<CommandGroupUserPlayerPO>().add(player).getList(), null, null, null, codec, admin.getId(), removeDevices);
			logic.merge(logicPlayFileStop);
			
			if(getPlayerSrcInfo){
				return new PlayerInfoBO(true, player.getBusinessName(), "");
			}
		}
		
		//查找给播放器的vod转发
		CommandVodPO vod = commandVodDao.findByDstBundleId(player.getBundleId());
		if(vod != null){
			
			description = "，有一个相关点播vod，id：" + vod.getId();				
			CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();
			
			//给新增设备生成forwardSet
			LogicBO logicVodStart = openBundleCastDeviceWithRestrict2(null, null, null, null, new ArrayListWrapper<CommandVodPO>().add(vod).getList(), null, null, codec, admin.getId(), addDevices);
			logic.merge(logicVodStart);
			
			//给删除设备生成disconnectBundle
			LogicBO logicVodStop = closeBundleCastDeviceWithRestrict2(null, null, null, null, codec, admin.getId(), removeDevices);
			logic.merge(logicVodStop);
			
			if(getPlayerSrcInfo){
				String srcInfo = vod.getSourceUserName()==null?vod.getSourceBundleName():vod.getSourceUserName();
				return new PlayerInfoBO(true, srcInfo, vod.getSourceNo());
			}
		}
		
		//TODO:这里可能有问题，：查找给播放器的call，从呼叫方与被叫方各查一遍，最多只会有一个
		UserLiveCallPO call = userLiveCallDao.findByCallDecoderBundleId(player.getBundleId());
		UserLiveCallPO called = userLiveCallDao.findByCalledDecoderBundleId(player.getBundleId());
		if(call != null){
			
			description = "，作为主叫方，有一个相关呼叫call，id：" + call.getId();
			CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();
			
			//给新增设备生成forwardSet
			LogicBO logicCallStart = openBundleCastDeviceWithRestrict2(null, null, null, null, null, new ArrayListWrapper<UserLiveCallPO>().add(call).getList(), null, codec, admin.getId(), addDevices);
			logic.merge(logicCallStart);
			
			//给删除设备生成disconnectBundle
			LogicBO logicCallStop = closeBundleCastDeviceWithRestrict2(null, null, null, null, codec, admin.getId(), removeDevices);
			logic.merge(logicCallStop);
			
			//这是主叫的播放器
			if(getPlayerSrcInfo){
				return new PlayerInfoBO(true, call.getCalledUsername(), call.getCalledUserno());
			}
		}else if(called != null){
			
			description = "，作为被叫方，有一个相关呼叫called，id：" + called.getId();
			CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();
			
			//给新增设备生成forwardSet
			LogicBO logicCallStart = openBundleCastDeviceWithRestrict2(null, null, null, null, null, null, new ArrayListWrapper<UserLiveCallPO>().add(called).getList(), codec, admin.getId(), addDevices);
			logic.merge(logicCallStart);
			
			//给删除设备生成disconnectBundle
			LogicBO logicCallStop = closeBundleCastDeviceWithRestrict2(null, null, null, null, codec, admin.getId(), removeDevices);
			logic.merge(logicCallStop);
			
			//这是被叫的播放器
			if(getPlayerSrcInfo){
				return new PlayerInfoBO(true, called.getCallUsername(), called.getCallUserno());
			}
		}
		
		//查找给播放器的转发
		CommandGroupForwardPO forward = commandGroupForwardDao.findByDstVideoBundleIdAndExecuteStatus(player.getBundleId(), ExecuteStatus.DONE);
		if(forward != null){
			
			description = "，有一个相关指挥/会议转发forward，id：" + forward.getId();
			
			CommandGroupPO group = forward.getGroup();
			CommandGroupAvtplGearsPO currentGear = commandCommonUtil.queryCurrentGear(group);
			CodecParamBO codec = new CodecParamBO().set(group.getAvtpl(), currentGear);
			
			//给新增设备生成forwardSet
			LogicBO logic1 = openBundleCastDeviceWithRestrict2(null, null, new HashSetWrapper<CommandGroupForwardPO>().add(forward).getSet(), null, null, null, null, codec, admin.getId(), addDevices);
			logic.merge(logic1);
			
			//给删除设备生成disconnectBundle
			LogicBO logic2 = closeBundleCastDeviceWithRestrict2(null, null, null, null, codec, admin.getId(), removeDevices);
			logic.merge(logic2);
			
			if(getPlayerSrcInfo){
				List<CommandGroupMemberPO> members = forward.getGroup().getMembers();
				CommandGroupMemberPO srcMember = commandCommonUtil.queryMemberById(members, forward.getSrcMemberId());
				if(srcMember == null){
					log.warn("从转发关系查找源成员失败，源成员id: " + forward.getSrcMemberId());
					return playerInfoBO;
				}else{
					return new PlayerInfoBO(true, srcMember.getUserName(), srcMember.getUserNum());
				}
			}
		}
		
		//查找给播放器的转发点播
		CommandGroupForwardDemandPO demand = commandGroupForwardDemandDao.findByDstVideoBundleIdAndExecuteStatus(player.getBundleId(), ForwardDemandStatus.DONE);
		if(demand != null){
			
			description = "，有一个相关指挥转发点播资源demand，id：" + demand.getId();
			
			CommandGroupPO group = demand.getGroup();
			CommandGroupAvtplGearsPO currentGear = commandCommonUtil.queryCurrentGear(group);
			CodecParamBO codec = new CodecParamBO().set(group.getAvtpl(), currentGear);

			//给新增设备生成forwardSet和mediaPushSet
			LogicBO logic3 = openBundleCastDeviceWithRestrict2(new ArrayListWrapper<CommandGroupForwardDemandPO>().add(demand).getList(), null, null, null, null, null, null, codec, admin.getId(), addDevices);
			logic.merge(logic3);
			
			//给删除设备生成mediaPushDel和disconnectBundle
			LogicBO logic4 = closeBundleCastDeviceWithRestrict2(null, null, null, null, codec, admin.getId(), removeDevices);
			logic.merge(logic4);
			
			if(getPlayerSrcInfo){
				return new PlayerInfoBO(true, demand.getVideoBundleName(), demand.getSrcCode());
			}
		}
		
		//字幕
		if(player.getOsdId() != null){
			
			CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();
			PlayerInfoBO playerInfo = changeCastDevices2(player, null, null, false, true);
			
			//清除字幕协议
			OsdWrapperBO clearOsd = monitorOsdService.clearProtocol(playerInfo.getSrcCode(), playerInfo.getSrcInfo());
			clearOsd.setResolution(codec.getVideo_param().getResolution());
			for(CommandGroupUserPlayerCastDevicePO removeDevice : removeDevices){
				PassByBO passByBO = new PassByBO()
						.setBundle_id(removeDevice.getDstBundleId())
						.setLayer_id(removeDevice.getDstLayerId())
						.setType("osds")
						.setPass_by_content(clearOsd);
				logic.getPass_by().add(passByBO);
			}
			
			//下发字幕协议
			MonitorOsdPO osd = monitorOsdDao.findOne(player.getOsdId());
			if(osd != null){
				OsdWrapperBO setOsd = monitorOsdService.protocol(osd, playerInfo.getSrcCode(), playerInfo.getSrcInfo());
				setOsd.setResolution(codec.getVideo_param().getResolution());
				for(CommandGroupUserPlayerCastDevicePO addDevice : addDevices){
					PassByBO passByBO = new PassByBO()
							.setBundle_id(addDevice.getDstBundleId())
							.setLayer_id(addDevice.getDstLayerId())
							.setType("osds")
							.setPass_by_content(setOsd);
					logic.getPass_by().add(passByBO);
				}
			}			
		}		
		
		if(doLogic) executeBusiness.execute(logic, "播放器修改上屏列表" + description);
		return playerInfoBO;
	}

	/**
	 * 播放器关联上屏<br/>
	 * <p>通常可以在openBundle方法后边使用，把新增的业务都传进来，生成logic协议</p>
	 * <b>作者:</b>zsu<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月22日 下午3:22:49
	 * @param demandsForForward转发点播，用于生成转发forwardSet（不会处理转发文件，转发文件的请使用下一个参数playFilePlayers）//，对于转发文件，还会生成mediaPushSet
	 * @param playFilePlayers 正在播放文件的播放器，用于生成mediaPush
	 * @param forwards 转发，用于生成forwardSet
	 * @param delForwards 删除转发，用于生成forwardDel
	 * @param vods 点播
	 * @param calls 呼叫
	 * @param codec
	 * @param userId 暂时无用，会被替换为Admin的userId
	 * @return LogicBO
	 */
	public LogicBO openBundleCastDevice(
			List<CommandGroupForwardDemandPO> demandsForForward,
			List<CommandGroupUserPlayerPO> playFilePlayers,
			Set<CommandGroupForwardPO> forwards,
			Set<CommandGroupForwardPO> delForwards,
			List<CommandVodPO> vods,
			List<UserLiveCallPO> calls,//已完成，待测
			CodecParamBO codec,
			Long userId){
		return openBundleCastDeviceWithRestrict2(demandsForForward, playFilePlayers, forwards, delForwards, vods, calls, calls, codec, userId, null);
	}
	
	/** restrictCastDevices 是指定的上屏设备，为null时按照播放器player.getCastDevices()正常查找；
	 * 慎用：非null时，则强制使用restrictCastDevices作为上屏设备，即使它是不包含任何元素的空表 */
	@Deprecated
	private LogicBO openBundleCastDeviceWithRestrict1(
			List<CommandGroupForwardDemandPO> demandsForForwardAndVod,
			Set<CommandGroupForwardPO> forwards,
			Set<CommandGroupForwardPO> delForwards,
			List<CommandVodPO> vods,
			List<UserLiveCallPO> calls,//已完成，待测
			CodecParamBO codec,
			Long userId,
			List<CommandGroupUserPlayerCastDevicePO> restrictCastDevices){
		
//		UserBO admin = resourceService.queryUserInfoByUsername(CommandCommonConstant.USER_NAME);
		UserBO admin = new UserBO(); admin.setId(-1L);
		
		LogicBO logic = new LogicBO().setUserId(admin.getId().toString())
		 		 .setConnectBundle(new ArrayList<ConnectBundleBO>())
		 		 .setForwardSet(new ArrayList<ForwardSetBO>())
		 		 .setForwardDel(new ArrayList<ForwardDelBO>())
		 		 .setMediaPushSet(new ArrayList<MediaPushSetBO>());
		
		//vod点播
		if(null == vods) vods = new ArrayList<CommandVodPO>();
		for(CommandVodPO vod : vods){
			if(vod.getDstBundleId() != null){
				List<CommandGroupUserPlayerCastDevicePO> castDevices = null;
				if(restrictCastDevices == null){
					//这个查询会不会脏读？
					CommandGroupUserPlayerPO player = commandGroupUserPlayerDao.findByBundleId(vod.getDstBundleId());
					if(player != null){
						castDevices = player.getCastDevices();
					}
				}else{
					castDevices = restrictCastDevices;
				}
				if(castDevices != null){
					for(CommandGroupUserPlayerCastDevicePO castDevice : castDevices){
						if(vod.getVodType().equals(VodType.USER)
								|| vod.getVodType().equals(VodType.USER_ONESELF)
								|| vod.getVodType().equals(VodType.DEVICE)
								|| vod.getVodType().equals(VodType.LOCAL_SEE_OUTER_DEVICE)
								|| vod.getVodType().equals(VodType.LOCAL_SEE_OUTER_USER)){
							//只要不是点播文件，都可以
							ForwardSetBO forwardVideo = new ForwardSetBO().setBySrcVodAndDstCastDevice(vod, castDevice, codec, MediaType.VIDEO);
							ForwardSetBO forwardAudio = new ForwardSetBO().setBySrcVodAndDstCastDevice(vod, castDevice, codec, MediaType.AUDIO);
							logic.getForwardSet().add(forwardVideo);
							logic.getForwardSet().add(forwardAudio);
						}
					}
				}
			}
		}
		
		//呼叫 TODO: restrictCastDevices 不为null的时候可能有问题
		if(null == calls) calls = new ArrayList<UserLiveCallPO>();
		for(UserLiveCallPO call : calls){
			
			//呼叫方
			List<CommandGroupUserPlayerCastDevicePO> castDevices = null;
			if(restrictCastDevices == null){
				if(call.getCalledEncoderBundleId() != null){
					CommandGroupUserPlayerPO player = commandGroupUserPlayerDao.findByBundleId(call.getCalledDecoderBundleId());
					if(player != null){
						castDevices = player.getCastDevices();
					}
				}
			}else{
				castDevices = restrictCastDevices;
			}
			if(castDevices != null){
				for(CommandGroupUserPlayerCastDevicePO castDevice : castDevices){
					if(call.getCallType()==null
							|| call.getCallType().equals(CallType.LOCAL_LOCAL)
							|| call.getCallType().equals(CallType.LOCAL_OUTER)){
						
						ForwardSetBO forwardVideo = new ForwardSetBO().setBySrcCallAndDstCastDevice(call, castDevice, codec, MediaType.VIDEO);
						ForwardSetBO forwardAudio = new ForwardSetBO().setBySrcCallAndDstCastDevice(call, castDevice, codec, MediaType.AUDIO);
						logic.getForwardSet().add(forwardVideo);
						logic.getForwardSet().add(forwardAudio);
					}
				}
			}
			
			//被呼叫方
			List<CommandGroupUserPlayerCastDevicePO> castCalledDevices = null;
			if(restrictCastDevices == null){
				if(call.getCallEncoderBundleId() != null){
					CommandGroupUserPlayerPO player = commandGroupUserPlayerDao.findByBundleId(call.getCallDecoderBundleId());
					if(player != null){
						castCalledDevices = player.getCastDevices();
					}
				}
			}else{
				castCalledDevices = restrictCastDevices;
			}
			if(castCalledDevices != null){
				for(CommandGroupUserPlayerCastDevicePO castDevice : castCalledDevices){
					if(call.getCallType()==null
							|| call.getCallType().equals(CallType.LOCAL_LOCAL)
							|| call.getCallType().equals(CallType.OUTER_LOCAL)){
						
						ForwardSetBO forwardVideo = new ForwardSetBO().setBySrcCalledAndDstCastDevice(call, castDevice, codec, MediaType.VIDEO);
						ForwardSetBO forwardAudio = new ForwardSetBO().setBySrcCalledAndDstCastDevice(call, castDevice, codec, MediaType.AUDIO);
						logic.getForwardSet().add(forwardVideo);
						logic.getForwardSet().add(forwardAudio);
					}
				}
			}
		}
		
		//转发点播
		if(null == demandsForForwardAndVod) demandsForForwardAndVod = new ArrayList<CommandGroupForwardDemandPO>();
		for(CommandGroupForwardDemandPO demand : demandsForForwardAndVod){
			if(demand.getDstVideoBundleId() != null){
				List<CommandGroupUserPlayerCastDevicePO> castDevices = null;
				if(restrictCastDevices == null){
					//这个查询会不会脏读？
					CommandGroupUserPlayerPO player = commandGroupUserPlayerDao.findByBundleId(demand.getDstVideoBundleId());
					if(player != null){
						castDevices = player.getCastDevices();
					}
				}else{
					castDevices = restrictCastDevices;
				}
				if(castDevices != null){
					for(CommandGroupUserPlayerCastDevicePO castDevice : castDevices){
						if(demand.getDemandType().equals(ForwardDemandBusinessType.FORWARD_DEVICE)){
							//转发设备
							ForwardSetBO forwardVideo = new ForwardSetBO().setBySrcDemandAndDstCastDevice(demand, castDevice, codec, MediaType.VIDEO);
							ForwardSetBO forwardAudio = new ForwardSetBO().setBySrcDemandAndDstCastDevice(demand, castDevice, codec, MediaType.AUDIO);
							logic.getForwardSet().add(forwardVideo);
							logic.getForwardSet().add(forwardAudio);
						}else if(demand.getDemandType().equals(ForwardDemandBusinessType.FORWARD_FILE)){
							//转发文件，使用mediaPush（vod模块负责运行）
							String mediaPushUuid = demand.getUuid() + "@@" + castDevice.getId();
							
							ForwardSetSrcBO videoForwardSetSrc = new ForwardSetSrcBO()
									.setType("mediaPush")
									.setUuid(mediaPushUuid);
							ForwardSetBO forwardVideo = new ForwardSetBO()
									.setBySrcDemandAndDstCastDevice(demand, castDevice, codec, MediaType.VIDEO)
									.setSrc(videoForwardSetSrc);
							
							ForwardSetSrcBO audioForwardSetSrc = new ForwardSetSrcBO()
									.setType("mediaPush")
									.setUuid(mediaPushUuid);
							ForwardSetBO forwardAudio = new ForwardSetBO()
									.setBySrcDemandAndDstCastDevice(demand, castDevice, codec, MediaType.AUDIO)
									.setSrc(audioForwardSetSrc);
							
							logic.getForwardSet().add(forwardVideo);
							logic.getForwardSet().add(forwardAudio);
							
							//mediaPushSet协议
							MediaPushSetBO mediaPushSet = new MediaPushSetBO()
									.setUuid(mediaPushUuid)
									.setFile_source(demand.getPlayUrl())
									.setCodec_param(codec);
							logic.getMediaPushSet().add(mediaPushSet);
						}
					}
				}
			}
		}
		
		//转发
		if(null == forwards) forwards = new HashSet<CommandGroupForwardPO>();
		for(CommandGroupForwardPO forward : forwards){
			if(forward.getDstVideoBundleId() != null){
				List<CommandGroupUserPlayerCastDevicePO> castDevices = null;
				if(restrictCastDevices == null){
					//这个查询会不会脏读？
					CommandGroupUserPlayerPO player = commandGroupUserPlayerDao.findByBundleId(forward.getDstVideoBundleId());
					if(player != null){
						castDevices = player.getCastDevices();
					}
				}else{
					castDevices = restrictCastDevices;
				}
				if(castDevices != null){
					for(CommandGroupUserPlayerCastDevicePO castDevice : castDevices){
						ForwardSetBO forwardVideo = new ForwardSetBO().setBySrcForwardAndDstCastDevice(forward, castDevice, codec, MediaType.VIDEO);
						ForwardSetBO forwardAudio = new ForwardSetBO().setBySrcForwardAndDstCastDevice(forward, castDevice, codec, MediaType.AUDIO);
						logic.getForwardSet().add(forwardVideo);
						logic.getForwardSet().add(forwardAudio);
					}
				}
			}
		}
		
		//删除转发
		if(null == delForwards) delForwards = new HashSet<CommandGroupForwardPO>();
		for(CommandGroupForwardPO delForward : delForwards){
			if(delForward.getDstVideoBundleId() != null){
				List<CommandGroupUserPlayerCastDevicePO> castDevices = null;
				if(restrictCastDevices == null){
					//这个查询会不会脏读？
					CommandGroupUserPlayerPO player = commandGroupUserPlayerDao.findByBundleId(delForward.getDstVideoBundleId());
					if(player != null){
						castDevices = player.getCastDevices();
					}
				}else{
					castDevices = restrictCastDevices;
				}
				if(castDevices != null){
					for(CommandGroupUserPlayerCastDevicePO castDevice : castDevices){
						ForwardDelBO forwardVideo = new ForwardDelBO().setByDstCastDevice(castDevice, codec, MediaType.VIDEO);
						ForwardDelBO forwardAudio = new ForwardDelBO().setByDstCastDevice(castDevice, codec, MediaType.AUDIO);
						logic.getForwardDel().add(forwardVideo);
						logic.getForwardDel().add(forwardAudio);
					}
				}
			}
		}
		
		return logic;		
	}
	
	/**
	 * 该private方法主要用于修改播放器关联的解码器时，最后一个参数<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月28日 上午11:57:24
	 * @param demandsForForward
	 * @param playFilePlayers
	 * @param forwards
	 * @param delForwards
	 * @param vods
	 * @param calls 主叫播放器涉及到的呼叫
	 * @param calleds 被叫播放器涉及到的呼叫
	 * @param codec
	 * @param userId
	 * @param restrictCastDevices 指定的上屏设备，为null时按照播放器player.getCastDevices()正常查找；慎用：非null时，则强制使用restrictCastDevices作为上屏设备，即使它是不包含任何元素的空表，通常用于“给播放器添加了解码器”的时候
	 * 
	 * @return
	 */
	private LogicBO openBundleCastDeviceWithRestrict2(
			List<CommandGroupForwardDemandPO> demandsForForward,
			List<CommandGroupUserPlayerPO> playFilePlayers,
			Set<CommandGroupForwardPO> forwards,
			Set<CommandGroupForwardPO> delForwards,
			List<CommandVodPO> vods,
			List<UserLiveCallPO> calls,//已完成，待测
			List<UserLiveCallPO> calleds,//已完成，待测
			CodecParamBO codec,
			Long userId,
			List<CommandGroupUserPlayerCastDevicePO> restrictCastDevices){
		
		UserBO admin = new UserBO(); admin.setId(-1L);
		
		LogicBO logic = new LogicBO().setUserId(admin.getId().toString())
		 		 .setConnectBundle(new ArrayList<ConnectBundleBO>())
		 		 .setForwardSet(new ArrayList<ForwardSetBO>())
		 		 .setForwardDel(new ArrayList<ForwardDelBO>())
		 		 .setMediaPushSet(new ArrayList<MediaPushSetBO>());
		
		//播放文件的播放器
		if(null == playFilePlayers) playFilePlayers = new ArrayList<CommandGroupUserPlayerPO>();
		for(CommandGroupUserPlayerPO playFilePlayer : playFilePlayers){
			
			//校验是不是在播放文件
			if(playFilePlayer.playingFile()){
				
				String playUrl = playFilePlayer.getPlayUrl();
				
				//playUrl有效则开始生成logic
				if(playUrl!=null && !playUrl.equals("")){					
					List<CommandGroupUserPlayerCastDevicePO> castDevices = null;
					if(restrictCastDevices == null){
						castDevices = playFilePlayer.getCastDevices();
					}else{
						castDevices = restrictCastDevices;
					}
					if(castDevices != null){
						for(CommandGroupUserPlayerCastDevicePO castDevice : castDevices){
							
							//转发文件，使用mediaPush（vod模块负责运行）这个uuid要在关闭时一致
							String mediaPushUuid = castDevice.getUuid() + "@@" + castDevice.getId();
							
							ForwardSetBO forwardVideo = new ForwardSetBO()
									.setByMediapushAndDstCastDevice(mediaPushUuid, castDevice, codec, MediaType.VIDEO);
							
							ForwardSetBO forwardAudio = new ForwardSetBO()
									.setByMediapushAndDstCastDevice(mediaPushUuid, castDevice, codec, MediaType.AUDIO);
							
							logic.getForwardSet().add(forwardVideo);
							logic.getForwardSet().add(forwardAudio);
							
							//mediaPushSet协议
							MediaPushSetBO mediaPushSet = new MediaPushSetBO()
									.setUuid(mediaPushUuid)
									.setFile_source(playUrl)
									.setCodec_param(codec);
							logic.getMediaPushSet().add(mediaPushSet);
						}
					}
				}
			}
		}
		
		//vod点播
		if(null == vods) vods = new ArrayList<CommandVodPO>();
		for(CommandVodPO vod : vods){
			if(vod.getDstBundleId() != null){
				List<CommandGroupUserPlayerCastDevicePO> castDevices = null;
				if(restrictCastDevices == null){
					//这个查询会不会脏读？
					CommandGroupUserPlayerPO player = commandGroupUserPlayerDao.findByBundleId(vod.getDstBundleId());
					if(player != null){
						castDevices = player.getCastDevices();
					}
				}else{
					castDevices = restrictCastDevices;
				}
				if(castDevices != null){
					for(CommandGroupUserPlayerCastDevicePO castDevice : castDevices){
						if(vod.getVodType().equals(VodType.USER)
								|| vod.getVodType().equals(VodType.USER_ONESELF)
								|| vod.getVodType().equals(VodType.DEVICE)
								|| vod.getVodType().equals(VodType.LOCAL_SEE_OUTER_DEVICE)
								|| vod.getVodType().equals(VodType.LOCAL_SEE_OUTER_USER)){
							//只要不是点播文件，都可以
							ForwardSetBO forwardVideo = new ForwardSetBO().setBySrcVodAndDstCastDevice(vod, castDevice, codec, MediaType.VIDEO);
							ForwardSetBO forwardAudio = new ForwardSetBO().setBySrcVodAndDstCastDevice(vod, castDevice, codec, MediaType.AUDIO);
							logic.getForwardSet().add(forwardVideo);
							logic.getForwardSet().add(forwardAudio);
						}
					}
				}
			}
		}
		
		//呼叫：restrictCastDevices 不为null时可能有问题
		if(null == calls) calls = new ArrayList<UserLiveCallPO>();
		for(UserLiveCallPO call : calls){
			
			if(!CallStatus.ONGOING.equals(call.getStatus())){
				continue;
			}
			
			//呼叫方的播放器，看被叫方的源
			List<CommandGroupUserPlayerCastDevicePO> castCalledDevices = null;
			if(restrictCastDevices == null){
				if(call.getCallEncoderBundleId() != null){
					CommandGroupUserPlayerPO player = commandGroupUserPlayerDao.findByBundleId(call.getCallDecoderBundleId());
					if(player != null){
						castCalledDevices = player.getCastDevices();
					}
				}
			}else{
				castCalledDevices = restrictCastDevices;
			}
			if(castCalledDevices != null){
				for(CommandGroupUserPlayerCastDevicePO castDevice : castCalledDevices){
					if(call.getCallType()==null
							|| call.getCallType().equals(CallType.LOCAL_LOCAL)
							|| call.getCallType().equals(CallType.LOCAL_OUTER)){
						
						ForwardSetBO forwardVideo = new ForwardSetBO().setBySrcCallAndDstCastDevice(call, castDevice, codec, MediaType.VIDEO);
						ForwardSetBO forwardAudio = new ForwardSetBO().setBySrcCallAndDstCastDevice(call, castDevice, codec, MediaType.AUDIO);
						logic.getForwardSet().add(forwardVideo);
						logic.getForwardSet().add(forwardAudio);
					}
				}
			}
		}
		
		//呼叫：restrictCastDevices 不为null时可能有问题
		if(null == calleds) calleds = new ArrayList<UserLiveCallPO>();
		for(UserLiveCallPO called : calleds){
			
			if(!CallStatus.ONGOING.equals(called.getStatus())){
				continue;
			}
			
			//被叫方的播放器，看呼叫方的源
			List<CommandGroupUserPlayerCastDevicePO> castDevices = null;
			if(restrictCastDevices == null){
				if(called.getCalledEncoderBundleId() != null){
					CommandGroupUserPlayerPO player = commandGroupUserPlayerDao.findByBundleId(called.getCalledDecoderBundleId());
					if(player != null){
						castDevices = player.getCastDevices();
					}
				}
			}else{
				castDevices = restrictCastDevices;
			}
			if(castDevices != null){
				for(CommandGroupUserPlayerCastDevicePO castDevice : castDevices){
					if(called.getCallType()==null
							|| called.getCallType().equals(CallType.LOCAL_LOCAL)
							|| called.getCallType().equals(CallType.OUTER_LOCAL)){
						
						ForwardSetBO forwardVideo = new ForwardSetBO().setBySrcCalledAndDstCastDevice(called, castDevice, codec, MediaType.VIDEO);
						ForwardSetBO forwardAudio = new ForwardSetBO().setBySrcCalledAndDstCastDevice(called, castDevice, codec, MediaType.AUDIO);
						logic.getForwardSet().add(forwardVideo);
						logic.getForwardSet().add(forwardAudio);
					}
				}
			}
		}
		
		//转发点播
		if(null == demandsForForward) demandsForForward = new ArrayList<CommandGroupForwardDemandPO>();
		for(CommandGroupForwardDemandPO demand : demandsForForward){
			if(demand.getDstVideoBundleId() != null){
				List<CommandGroupUserPlayerCastDevicePO> castDevices = null;
				if(restrictCastDevices == null){
					//这个查询会不会脏读？
					CommandGroupUserPlayerPO player = commandGroupUserPlayerDao.findByBundleId(demand.getDstVideoBundleId());
					if(player != null){
						castDevices = player.getCastDevices();
					}
				}else{
					castDevices = restrictCastDevices;
				}
				if(castDevices != null){
					for(CommandGroupUserPlayerCastDevicePO castDevice : castDevices){
						if(demand.getDemandType().equals(ForwardDemandBusinessType.FORWARD_DEVICE)){
							//转发设备
							ForwardSetBO forwardVideo = new ForwardSetBO().setBySrcDemandAndDstCastDevice(demand, castDevice, codec, MediaType.VIDEO);
							ForwardSetBO forwardAudio = new ForwardSetBO().setBySrcDemandAndDstCastDevice(demand, castDevice, codec, MediaType.AUDIO);
							logic.getForwardSet().add(forwardVideo);
							logic.getForwardSet().add(forwardAudio);
						}
						//转发文件的，通过playFilePlayers处理
//						else if(demand.getDemandType().equals(ForwardDemandBusinessType.FORWARD_FILE)){
//							//转发文件，使用mediaPush（vod模块负责运行）
//							String mediaPushUuid = demand.getUuid() + "@@" + castDevice.getId();
//							
//							ForwardSetSrcBO videoForwardSetSrc = new ForwardSetSrcBO()
//									.setType("mediaPush")
//									.setUuid(mediaPushUuid);
//							ForwardSetBO forwardVideo = new ForwardSetBO()
//									.setBySrcDemandAndDstCastDevice(demand, castDevice, codec, MediaType.VIDEO)
//									.setSrc(videoForwardSetSrc);
//							
//							ForwardSetSrcBO audioForwardSetSrc = new ForwardSetSrcBO()
//									.setType("mediaPush")
//									.setUuid(mediaPushUuid);
//							ForwardSetBO forwardAudio = new ForwardSetBO()
//									.setBySrcDemandAndDstCastDevice(demand, castDevice, codec, MediaType.AUDIO)
//									.setSrc(audioForwardSetSrc);
//							
//							logic.getForwardSet().add(forwardVideo);
//							logic.getForwardSet().add(forwardAudio);
//							
//							//mediaPushSet协议
//							MediaPushSetBO mediaPushSet = new MediaPushSetBO()
//									.setUuid(mediaPushUuid)
//									.setFile_source(demand.getPlayUrl())
//									.setCodec_param(codec);
//							logic.getMediaPushSet().add(mediaPushSet);
//						}
					}
				}
			}
		}
		
		//转发
		if(null == forwards) forwards = new HashSet<CommandGroupForwardPO>();
		for(CommandGroupForwardPO forward : forwards){
			if(forward.getDstVideoBundleId() != null){
				List<CommandGroupUserPlayerCastDevicePO> castDevices = null;
				if(restrictCastDevices == null){
					//这个查询会不会脏读？
					CommandGroupUserPlayerPO player = commandGroupUserPlayerDao.findByBundleId(forward.getDstVideoBundleId());
					if(player != null){
						castDevices = player.getCastDevices();
					}
				}else{
					castDevices = restrictCastDevices;
				}
				if(castDevices != null){
					for(CommandGroupUserPlayerCastDevicePO castDevice : castDevices){
						ForwardSetBO forwardVideo = new ForwardSetBO().setBySrcForwardAndDstCastDevice(forward, castDevice, codec, MediaType.VIDEO);
						ForwardSetBO forwardAudio = new ForwardSetBO().setBySrcForwardAndDstCastDevice(forward, castDevice, codec, MediaType.AUDIO);
						logic.getForwardSet().add(forwardVideo);
						logic.getForwardSet().add(forwardAudio);
					}
				}
			}
		}
		
		//删除转发
		if(null == delForwards) delForwards = new HashSet<CommandGroupForwardPO>();
		for(CommandGroupForwardPO delForward : delForwards){
			if(delForward.getDstVideoBundleId() != null){
				List<CommandGroupUserPlayerCastDevicePO> castDevices = null;
				if(restrictCastDevices == null){
					//这个查询会不会脏读？
					CommandGroupUserPlayerPO player = commandGroupUserPlayerDao.findByBundleId(delForward.getDstVideoBundleId());
					if(player != null){
						castDevices = player.getCastDevices();
					}
				}else{
					castDevices = restrictCastDevices;
				}
				if(castDevices != null){
					for(CommandGroupUserPlayerCastDevicePO castDevice : castDevices){
						ForwardDelBO forwardVideo = new ForwardDelBO().setByDstCastDevice(castDevice, codec, MediaType.VIDEO);
						ForwardDelBO forwardAudio = new ForwardDelBO().setByDstCastDevice(castDevice, codec, MediaType.AUDIO);
						logic.getForwardDel().add(forwardVideo);
						logic.getForwardDel().add(forwardAudio);
					}
				}
			}
		}
		
		return logic;		
	}

	/**
	 * 播放器关联下屏<br/>
	 * <p>通常可以在closeBundle方法后边使用</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月22日 下午3:25:59
	 * @param playFilePlayers 用于关闭mediaPush【确认正在播放文件的播放器由于该参数此时已被setFree，所以无法再判断是否还在播放文件，必须在输入前确认】
	 * @param players 播放器列表
	 * @param codec
	 * @param userId 暂时无用，会被替换为Admin的userId
	 * @return LogicBO
	 */
	public LogicBO closeBundleCastDevice(
//			List<CommandGroupForwardDemandPO> demandsForVod,//用于关闭文件转发的mediaPushSet
			List<CommandGroupUserPlayerPO> playFilePlayers,
			List<CommandVodPO> vods,
			List<UserLiveCallPO> calls,//后续处理
			List<CommandGroupUserPlayerPO> players,
			CodecParamBO codec,
			Long userId){
		return closeBundleCastDeviceWithRestrict2(playFilePlayers, vods, calls, players, codec, userId, null);
	}
	
	/** restrictCastDevices 是指定的上屏设备，为null时按照播放器player.getCastDevices()正常查找；
	 * 慎用：非null时，则强制使用restrictCastDevices作为上屏设备，即使它是不包含任何元素的空表，此时players参数无效 */
	private LogicBO closeBundleCastDeviceWithRestrict1(
			List<CommandGroupForwardDemandPO> demandsForVod,//用于关闭文件转发的mediaPushSet
			List<CommandVodPO> vodsForVod,//后续处理：用于关闭文件转发的mediaPushSet
			List<UserLiveCallPO> calls,//后续处理
			List<CommandGroupUserPlayerPO> players,
			CodecParamBO codec,
			Long userId,
			List<CommandGroupUserPlayerCastDevicePO> restrictCastDevices){
		
//		UserBO admin = resourceService.queryUserInfoByUsername(CommandCommonConstant.USER_NAME);
		UserBO admin = new UserBO(); admin.setId(-1L);
		
		LogicBO logic = new LogicBO().setUserId(admin.getId().toString())
		 		 .setDisconnectBundle(new ArrayList<DisconnectBundleBO>())
		 		 .setMediaPushDel(new ArrayList<MediaPushSetBO>());
		
		//关闭文件转发的mediaPushSet
		if(null == demandsForVod) demandsForVod = new ArrayList<CommandGroupForwardDemandPO>();
		for(CommandGroupForwardDemandPO demand : demandsForVod){
			if(demand.getDstVideoBundleId() != null){
				List<CommandGroupUserPlayerCastDevicePO> castDevices = null;
				if(restrictCastDevices == null){
					//这个查询会不会脏读？
					CommandGroupUserPlayerPO player = commandGroupUserPlayerDao.findByBundleId(demand.getDstVideoBundleId());
					if(player != null){
						castDevices = player.getCastDevices();
					}
				}else{
					castDevices = restrictCastDevices;
				}
				if(castDevices != null){
					for(CommandGroupUserPlayerCastDevicePO castDevice : castDevices){
						if(demand.getDemandType().equals(ForwardDemandBusinessType.FORWARD_FILE)){
							//转发文件，删除mediaPush（vod模块负责运行）
							String mediaPushUuid = demand.getUuid() + "@@" + castDevice.getId();								
							logic.getMediaPushDel().add(new MediaPushSetBO().setUuid(mediaPushUuid));								
						}
					}
				}
			}
		}
		
		//挂断播放器关联的解码器
		List<CommandGroupUserPlayerCastDevicePO> closeCastDevices = new ArrayList<CommandGroupUserPlayerCastDevicePO>();
		if(restrictCastDevices == null){
			if(null == players) players = new ArrayList<CommandGroupUserPlayerPO>();
			for(CommandGroupUserPlayerPO player : players){
				List<CommandGroupUserPlayerCastDevicePO> castDevices = player.getCastDevices();
				if(castDevices != null){
					closeCastDevices.addAll(castDevices);
				}
			}
		}else{
			closeCastDevices = restrictCastDevices;
		}
		if(closeCastDevices != null){
			for(CommandGroupUserPlayerCastDevicePO castDevice : closeCastDevices){
				DisconnectBundleBO disconnectDstBundle = new DisconnectBundleBO()
						.setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
//								.setOperateType(DisconnectBundleBO.OPERATE_TYPE)
						.setBundleId(castDevice.getDstBundleId())
						.setBundle_type(castDevice.getDstBundleType())
						.setLayerId(castDevice.getDstLayerId());
				logic.getDisconnectBundle().add(disconnectDstBundle);
			}
		}
		
		return logic;
	}

	/** restrictCastDevices 是指定的上屏设备，为null时按照播放器player.getCastDevices()正常查找；
	 * 慎用：非null时，则强制使用restrictCastDevices作为上屏设备，即使它是不包含任何元素的空表，此时players参数无效 */
	private LogicBO closeBundleCastDeviceWithRestrict2(
			List<CommandGroupUserPlayerPO> playFilePlayers,
			List<CommandVodPO> vodsForVod,//后续处理：用于关闭文件转发的mediaPushSet
			List<UserLiveCallPO> calls,//后续处理
			List<CommandGroupUserPlayerPO> players,
			CodecParamBO codec,
			Long userId,
			List<CommandGroupUserPlayerCastDevicePO> restrictCastDevices){
		
		UserBO admin = new UserBO(); admin.setId(-1L);
		
		LogicBO logic = new LogicBO().setUserId(admin.getId().toString())
		 		 .setDisconnectBundle(new ArrayList<DisconnectBundleBO>())
		 		 .setMediaPushDel(new ArrayList<MediaPushSetBO>());
		
		if(null == playFilePlayers) playFilePlayers = new ArrayList<CommandGroupUserPlayerPO>();
		for(CommandGroupUserPlayerPO playFilePlayer : playFilePlayers){					
			List<CommandGroupUserPlayerCastDevicePO> castDevices = null;
			if(restrictCastDevices == null){
				castDevices = playFilePlayer.getCastDevices();
			}else{
				castDevices = restrictCastDevices;
			}
			if(castDevices != null){
				for(CommandGroupUserPlayerCastDevicePO castDevice : castDevices){
					//转发文件，删除mediaPush（vod模块负责运行）这个uuid要与打开时一致
					String mediaPushUuid = castDevice.getUuid() + "@@" + castDevice.getId();								
					logic.getMediaPushDel().add(new MediaPushSetBO().setUuid(mediaPushUuid));
				}
			}
		}
		
		//挂断播放器关联的解码器
		List<CommandGroupUserPlayerCastDevicePO> closeCastDevices = new ArrayList<CommandGroupUserPlayerCastDevicePO>();
		if(restrictCastDevices == null){
			if(null == players) players = new ArrayList<CommandGroupUserPlayerPO>();
			for(CommandGroupUserPlayerPO player : players){
				List<CommandGroupUserPlayerCastDevicePO> castDevices = player.getCastDevices();
				if(castDevices != null){
					closeCastDevices.addAll(castDevices);
				}
			}
		}else{
			closeCastDevices = restrictCastDevices;
		}
		if(closeCastDevices != null){
			for(CommandGroupUserPlayerCastDevicePO castDevice : closeCastDevices){
				DisconnectBundleBO disconnectDstBundle = new DisconnectBundleBO()
						.setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
//								.setOperateType(DisconnectBundleBO.OPERATE_TYPE)
						.setBundleId(castDevice.getDstBundleId())
						.setBundle_type(castDevice.getDstBundleType())
						.setLayerId(castDevice.getDstLayerId());
				logic.getDisconnectBundle().add(disconnectDstBundle);
			}
		}
		
		return logic;
	}
}

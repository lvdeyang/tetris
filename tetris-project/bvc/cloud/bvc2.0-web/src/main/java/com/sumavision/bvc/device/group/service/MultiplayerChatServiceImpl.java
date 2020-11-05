package com.sumavision.bvc.device.group.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.ScreenRectTemplatePO;
import com.suma.venus.resource.pojo.ScreenSchemePO;
import com.sumavision.bvc.device.group.bo.CallBO;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.ConnectBundleBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.PassByBO;
import com.sumavision.bvc.device.group.dao.ChannelForwardDAO;
import com.sumavision.bvc.device.group.dao.CombineAudioDAO;
import com.sumavision.bvc.device.group.dao.CombineVideoDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigVideoPositionDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupMemberDAO;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.enumeration.CodecParamType;
import com.sumavision.bvc.device.group.enumeration.ConfigType;
import com.sumavision.bvc.device.group.enumeration.ForwardDstType;
import com.sumavision.bvc.device.group.enumeration.GroupStatus;
import com.sumavision.bvc.device.group.enumeration.GroupType;
import com.sumavision.bvc.device.group.enumeration.MemberStatus;
import com.sumavision.bvc.device.group.enumeration.PictureType;
import com.sumavision.bvc.device.group.enumeration.PollingStatus;
import com.sumavision.bvc.device.group.enumeration.ScreenLayout;
import com.sumavision.bvc.device.group.enumeration.VideoOperationType;
import com.sumavision.bvc.device.group.exception.DeviceGroupHasNotStartedException;
import com.sumavision.bvc.device.group.exception.InitiatorNotAdministratorException;
import com.sumavision.bvc.device.group.po.ChannelForwardPO;
import com.sumavision.bvc.device.group.po.CombineAudioPO;
import com.sumavision.bvc.device.group.po.CombineVideoPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplPO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigAudioPO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigPO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoDstPO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoPO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoPositionPO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoSrcPO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberChannelPO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberPO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberScreenPO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberScreenRectPO;
import com.sumavision.bvc.device.group.po.DeviceGroupPO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.MeetingUtil;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.device.group.service.util.ResourceQueryUtil;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.bvc.system.dao.AvtplDAO;
import com.sumavision.bvc.system.enumeration.AudioFormat;
import com.sumavision.bvc.system.enumeration.GearsLevel;
import com.sumavision.bvc.system.enumeration.Resolution;
import com.sumavision.bvc.system.enumeration.VideoFormat;
import com.sumavision.bvc.system.po.AvtplGearsPO;
import com.sumavision.bvc.system.po.AvtplPO;

@Transactional(rollbackFor = Exception.class)
@Service
public class MultiplayerChatServiceImpl {
	
	@Autowired
	private QueryUtil queryUtil;
	
	@Autowired
	private ResourceQueryUtil resourceQueryUtil;
	
	@Autowired
	private MeetingUtil meetingUtil;
	
	@Autowired
	private AvtplDAO avtplDao;
	
	@Autowired
	private DeviceGroupDAO deviceGroupDao;
	
	@Autowired
	private DeviceGroupMemberDAO deviceGroupMemberDao;
	
	@Autowired
	private DeviceGroupConfigVideoPositionDAO deviceGroupConfigVideoPositionDao;
	
	@Autowired
	private CombineVideoDAO combineVideoDao;
	
	@Autowired
	private CombineAudioDAO combineAudioDao;
	
	@Autowired
	private ChannelForwardDAO channelForwardDao;
	
	@Autowired
	private VideoServiceImpl videoServiceImpl;
	
	@Autowired
	private AudioServiceImpl audioServiceImpl;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;

	/**
	 * 开始多人通话 
	 * @param name 设备组名称
	 * @param callType 通话类型（video/audio）
	 * @param bundleId 发起人设备id
	 * @param targetBundleIdList 被呼叫方的设备id数组
	 * @param userId 用户id
	 * @param userName 用户名
	 * @param websiteDraw 前端布局
	 * @param positions 位置信息
	 * @return DeviceGroupPO 多人通话
	 * @throws
	 */
	public DeviceGroupPO startChat(
			String name, 
			String callType, 
			String codecParamType,
			String codecParam,
			String bundleId, 
			List<String> targetBundleIdList,
			Long userId,
			String userName,
			String websiteDraw,
			List<JSONObject> positions) throws Exception{
		
		//建会
		DeviceGroupPO group = createChat(name, callType, codecParamType, codecParam, bundleId, targetBundleIdList, userId, userName, websiteDraw, positions);
		
		group.setStatus(GroupStatus.START);
		
		//处理参数模板
		DeviceGroupAvtplPO avtpl = group.getAvtpl();
		DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
		CodecParamBO codec = new CodecParamBO().set(avtpl, currentGear);
		
		//呼叫发起人
		connectBundle(group, bundleId, codec);
		
		List<String> bundleIds = new ArrayList<String>();
		bundleIds.add(bundleId);
		//合屏+混音+转发
		updateGroupMember(group, bundleIds, true, false, true);
		
		//呼叫被呼叫设备
		for(String targetBundleId: targetBundleIdList){
			connectBundle(group, targetBundleId, codec);				
		}
		
		return group;
	}
	
	/**
	 * 创建多人通话设备组（会议）<br/> 
	 * @param name 设备组名称
	 * @param callType 通话类型（video/audio）
	 * @param bundleId 发起人设备id
	 * @param targetBundleIdList 被呼叫方的设备id数组
	 * @param userId 用户id
	 * @param userName 用户名
	 * @param websiteDraw 前端布局
	 * @param positions 位置信息
	 * @return DeviceGroupPO 设备组信息 
	 * @throws
	 */
	public DeviceGroupPO createChat(
					String name, 
					String callType, 
					String codecParamType,
					String codecParam,
					String bundleId, 
					List<String> targetBundleIdList,
					Long userId,
					String userName,
					String websiteDraw,
					List<JSONObject> positions) throws Exception{
		
		DeviceGroupPO group = new DeviceGroupPO();
		group.setName(name);
		group.setUserId(userId);
		group.setUserName(userName);
		group.setCurrentGearLevel(GearsLevel.LEVEL_3);
		group.setType(GroupType.fromName(callType));
		
		//获取机顶盒模板(系统获取)	
		AvtplPO avtpl = meetingUtil.generateAvtpl(codecParamType, codecParam);

		DeviceGroupAvtplPO groupAvtpl = new DeviceGroupAvtplPO().set(avtpl);
		groupAvtpl.setGears(new HashSet<DeviceGroupAvtplGearsPO>());
		
		Set<AvtplGearsPO> avtplGears = avtpl.getGears();
		for(AvtplGearsPO avtplGear: avtplGears){
			DeviceGroupAvtplGearsPO _gear = new DeviceGroupAvtplGearsPO().set(avtplGear);
			_gear.setAvtpl(groupAvtpl);
			groupAvtpl.getGears().add(_gear);
		}
		
		groupAvtpl.setGroup(group);
		group.setAvtpl(groupAvtpl);
		
		group.setMembers(new HashSet<DeviceGroupMemberPO>());
		
		List<String> allBundleIdList = new ArrayList<String>();
		allBundleIdList.addAll(targetBundleIdList);
		allBundleIdList.add(bundleId);
		
		List<BundlePO> bundles = resourceQueryUtil.queryAllBundlesByBundleIds(allBundleIdList);
		List<ChannelSchemeDTO> channels = resourceQueryUtil.queryAllChannelsByBundleIds(allBundleIdList);
		List<ScreenSchemePO> screens = resourceQueryUtil.queryScreensByBundleIds(allBundleIdList);
		
		Set<String> screenIds = new HashSet<String>();
		for(ScreenSchemePO screen: screens){
			screenIds.add(screen.getScreenId());
		}
		List<ScreenRectTemplatePO> rects = resourceQueryUtil.queryRectsByScreenIds(screenIds);
		
		for(BundlePO bundle: bundles){
			DeviceGroupMemberPO member = new DeviceGroupMemberPO();
			member.setBundleId(bundle.getBundleId());
			member.setBundleName(bundle.getBundleName());
			member.setBundleType(bundle.getDeviceModel());
			member.setVenusBundleType(bundle.getBundleType());
			member.setLayerId(bundle.getAccessNodeUid());
			member.setUserId(userId);
			if(bundle.getBundleId().equals(bundleId)){
				member.setMemberStatus(MemberStatus.CONNECT);
				member.setAdministrator(true);
			}else{
				member.setMemberStatus(MemberStatus.CONNECTING);
				member.setAdministrator(false);
			}
				
			//通道
			member.setChannels(new HashSet<DeviceGroupMemberChannelPO>());
			for(ChannelSchemeDTO channel: channels){
				if(channel.getBundleId().equals(bundle.getBundleId())){
					DeviceGroupMemberChannelPO channelPO = new DeviceGroupMemberChannelPO();
					channelPO.setBundleId(bundle.getBundleId());
					channelPO.setBundleName(bundle.getBundleName());
					channelPO.setBundleType(bundle.getDeviceModel());
					channelPO.setVenusBundleType(bundle.getBundleType());
					channelPO.setChannelId(channel.getChannelId());
					channelPO.setChannelName(channel.getChannelName());
					channelPO.setChannelType(channel.getBaseType());
					if("VenusVideoOut".equals(channel.getBaseType())){
						channelPO.setType(ChannelType.VIDEODECODE1);
					}else if("VenusVideoIn".equals(channel.getBaseType())){
						channelPO.setType(ChannelType.VIDEOENCODE1);
					}else if("VenusAudioOut".equals(channel.getBaseType())){
						channelPO.setType(ChannelType.AUDIODECODE1);
					}else if("VenusAudioIn".equals(channel.getBaseType())){
						channelPO.setType(ChannelType.AUDIOENCODE1);
					}
					channelPO.setMember(member);	
					member.getChannels().add(channelPO);
				}		
			}
			
			//屏幕
			member.setScreens(new HashSet<DeviceGroupMemberScreenPO>());
			for(ScreenSchemePO screen: screens){
				if(screen.getBundleId().equals(bundle.getBundleId())){
					DeviceGroupMemberScreenPO screenPO = new DeviceGroupMemberScreenPO();
					screenPO.setBundleId(screen.getBundleId());
					screenPO.setScreenId(screen.getScreenId());
					screenPO.setMember(member);
					screenPO.setRests(new HashSet<DeviceGroupMemberScreenRectPO>());
					for(ScreenRectTemplatePO rect: rects){
						if(rect.getScreenId().equals(screen.getScreenId()) && rect.getDeviceModel().equals(screen.getDeviceModel())){
							DeviceGroupMemberScreenRectPO rectPO = new DeviceGroupMemberScreenRectPO();
							rectPO.setBundleId(screen.getBundleId());
							rectPO.setScreenId(rect.getScreenId());
							rectPO.setRectId(rect.getRectId());
							rectPO.setParam(rect.getParam());
							rectPO.setChannel(rect.getChannel());
							rectPO.setScreen(screenPO);
							screenPO.getRests().add(rectPO);
						}						
					}
					member.getScreens().add(screenPO);
				}
			}
			
			member.setGroup(group);
			group.getMembers().add(member);
		}
		
		deviceGroupDao.save(group);
		
		if(group.getType().equals(GroupType.MULTIPLAYERVIDEO)){
			//生成config包含默认一个video
			generateConfig(group, websiteDraw, positions, null, null);
		}
		
		return group;
	}
	
	/**
	 * @Title: 更新多人通话设备组（会议）<br/> 
	 * @param groupUuid 设备组uuid
	 * @param bundleId 添加方设备id
	 * @param targetBundleIdList 更新设备id数组
	 * @return DeviceGroupPO 设备组信息 
	 * @throws
	 */
	public DeviceGroupPO updateChat(DeviceGroupPO group, List<String> targetBundleIdList) throws Exception{
		
		Set<DeviceGroupMemberPO> members = group.getMembers();
		
		//根据设备id数组 查询 所有设备信息
		List<BundlePO> bundles = resourceQueryUtil.queryAllBundlesByBundleIds(targetBundleIdList);
		List<ChannelSchemeDTO> channels = resourceQueryUtil.queryAllChannelsByBundleIds(targetBundleIdList);
		List<ScreenSchemePO> screens = resourceQueryUtil.queryScreensByBundleIds(targetBundleIdList);
		
		Set<String> screenIds = new HashSet<String>();
		for(ScreenSchemePO screen: screens){
			screenIds.add(screen.getScreenId());
		}
		List<ScreenRectTemplatePO> rects = resourceQueryUtil.queryRectsByScreenIds(screenIds);
		
		for(BundlePO bundle: bundles){
			DeviceGroupMemberPO existMember = queryUtil.queryMemberPOByBundleId(members, bundle.getBundleId());
			
			if(existMember == null){
				DeviceGroupMemberPO member = new DeviceGroupMemberPO();
				member.setBundleId(bundle.getBundleId());
				member.setBundleName(bundle.getBundleName());
				member.setBundleType(bundle.getDeviceModel());
				member.setVenusBundleType(bundle.getBundleType());
				member.setLayerId(bundle.getAccessNodeUid());
				member.setMemberStatus(MemberStatus.CONNECTING);
				member.setUserId(group.getUserId());
					
				//通道
				member.setChannels(new HashSet<DeviceGroupMemberChannelPO>());
				for(ChannelSchemeDTO channel: channels){
					if(channel.getBundleId().equals(bundle.getBundleId())){
						DeviceGroupMemberChannelPO channelPO = new DeviceGroupMemberChannelPO();
						channelPO.setBundleId(bundle.getBundleId());
						channelPO.setBundleName(bundle.getBundleName());
						channelPO.setBundleType(bundle.getDeviceModel());
						channelPO.setVenusBundleType(bundle.getBundleType());
						channelPO.setChannelId(channel.getChannelId());
						channelPO.setChannelName(channel.getChannelName());
						channelPO.setChannelType(channel.getBaseType());
						if("VenusVideoOut".equals(channel.getBaseType())){
							channelPO.setType(ChannelType.VIDEODECODE1);
						}else if("VenusVideoIn".equals(channel.getBaseType())){
							channelPO.setType(ChannelType.VIDEOENCODE1);
						}else if("VenusAudioOut".equals(channel.getBaseType())){
							channelPO.setType(ChannelType.AUDIODECODE1);
						}else if("VenusAudioIn".equals(channel.getBaseType())){
							channelPO.setType(ChannelType.AUDIOENCODE1);
						}
						channelPO.setMember(member);	
						member.getChannels().add(channelPO);
					}		
				}
				
				//屏幕
				member.setScreens(new HashSet<DeviceGroupMemberScreenPO>());
				for(ScreenSchemePO screen: screens){
					if(screen.getBundleId().equals(bundle.getBundleId())){
						DeviceGroupMemberScreenPO screenPO = new DeviceGroupMemberScreenPO();
						screenPO.setBundleId(screen.getBundleId());
						screenPO.setScreenId(screen.getScreenId());
						screenPO.setMember(member);
						screenPO.setRests(new HashSet<DeviceGroupMemberScreenRectPO>());
						for(ScreenRectTemplatePO rect: rects){
							if(rect.getScreenId().equals(screen.getScreenId()) && rect.getDeviceModel().equals(screen.getDeviceModel())){
								DeviceGroupMemberScreenRectPO rectPO = new DeviceGroupMemberScreenRectPO();
								rectPO.setBundleId(screen.getBundleId());
								rectPO.setScreenId(rect.getScreenId());
								rectPO.setRectId(rect.getRectId());
								rectPO.setParam(rect.getParam());
								rectPO.setChannel(rect.getChannel());
								rectPO.setScreen(screenPO);
								screenPO.getRests().add(rectPO);
							}						
						}
						member.getScreens().add(screenPO);
					}
				}
				
				member.setGroup(group);
				group.getMembers().add(member);
			}		
		}
		
		deviceGroupDao.save(group);
		
		return group;
	}
	
	/**
	 * @Title: 呼叫多人通话设备
	 * @param group
	 * @param bundleId
	 * @param codec
	 * @return void
	 * @throws
	 */
	public void connectBundle(DeviceGroupPO group, String bundleId, CodecParamBO codec) throws Exception{

		LogicBO logic = new LogicBO(); 
		logic.setConnectBundle(new ArrayList<ConnectBundleBO>());
		logic.setUserId(group.getUserId().toString());
		
		Set<DeviceGroupMemberPO> members = group.getMembers();
		DeviceGroupMemberPO member = queryUtil.queryMemberPOByBundleId(members, bundleId);
		
		if(member.isAdministrator()){
			ConnectBundleBO connect = new ConnectBundleBO().set(group, member, codec);	
			logic.getConnectBundle().add(connect);
			executeBusiness.execute(logic, "呼叫"+member.getBundleName()+"设备：");
		}else{
			ConnectBundleBO connect = new ConnectBundleBO().set(group, member, codec);
			logic.getConnectBundle().add(connect);
			
			try {
				executeBusiness.execute(logic, "呼叫"+member.getBundleName()+"设备：");
				member.setMemberStatus(MemberStatus.CONNECTING);
			} catch (Exception e) {
				member.setMemberStatus(MemberStatus.DISCONNECT);
			}
		}
	}
	
	/**
	 * @Title: 管理员权限转移(被动)
	 * @param groupUuid 设备组uuid
	 * @param bundleId 转移人设备id
	 * @param targetBundleId 被转移人设备id
	 * @return void 
	 * @throws
	 */
	public LogicBO transferAdministrator(DeviceGroupMemberPO administrator, DeviceGroupPO group) throws Exception{
		
		LogicBO logic = new LogicBO();
		
		if(group == null) throw new Exception("该多人通话不存在！");
		
		Set<DeviceGroupMemberPO> members = group.getMembers();
		for(DeviceGroupMemberPO member: members){
			if(member.getMemberStatus().equals(MemberStatus.CONNECT) && member.getBundleType().equals("tvos")){
				logic = transferAdministrator(group.getUuid(), administrator.getBundleId(), member.getBundleId(), false);
			}
		}
		return logic;
	}
	
	/**
	 * @Title: 管理员权限转移(主动)
	 * @param groupUuid 设备组uuid
	 * @param bundleId 转移人设备id
	 * @param targetBundleId 被转移人设备id
	 * @return void 
	 * @throws
	 */
	public LogicBO transferAdministrator(String groupUuid, String bundleId, String targetBundleId, boolean doProtocal) throws Exception{
		
		DeviceGroupPO group = deviceGroupDao.findByUuid(groupUuid);
		
		if(group == null) throw new Exception("该多人通话不存在！");
		
		Set<DeviceGroupMemberPO> members = group.getMembers();
		DeviceGroupMemberPO administrator = queryUtil.queryMemberPOByBundleId(members, bundleId);
		if(!administrator.isAdministrator()){
			throw new InitiatorNotAdministratorException(administrator.getBundleName());
		}
		
		DeviceGroupMemberPO newAdministrator = queryUtil.queryMemberPOByBundleId(members, targetBundleId);
		administrator.setAdministrator(false);
		newAdministrator.setAdministrator(true);
		
		LogicBO logic = setAdministratorUpdate(group, newAdministrator);
		
		if(doProtocal) executeBusiness.execute(logic, "管理员权限转移：");
		
		return logic;
	}
	
	/**
	 * @Title: 锁设备 <br/> 
	 * @param bundleId 设备id
	 * @return void 
	 * @throws
	 */
	public void lockMember(String bundleId, DeviceGroupPO group) throws Exception{
		
		LogicBO logic = new LogicBO();
		
		Set<DeviceGroupMemberPO> members = group.getMembers();
		
		DeviceGroupMemberPO member = queryUtil.queryMemberPOByBundleId(members, bundleId);
		
		Set<DeviceGroupMemberChannelPO> channels = member.getChannels();
		
		logic.setLock(channels);
		
		executeBusiness.execute(logic, "锁定多人通话发起人：");
	}
	
	/**
	 * @Title: 被呼叫方消息推送 <br/> 
	 * @param targetBundleIdList 被呼叫设备id数组
	 * @param group 设备组
	 * @return void 
	 * @throws
	 */
	public void inComingCall(List<String> targetBundleIdList, DeviceGroupPO group) throws Exception{
		
		LogicBO logic = new LogicBO();
		
		String callType = "";
		if(group.getType().equals(GroupType.MULTIPLAYERVIDEO)){
			callType = "video";
		}else if(group.getType().equals(GroupType.MULTIPLAYERAUDIO)){
			callType = "audio";
		}
		
		logic.setPass_by(new ArrayList<PassByBO>());
		
		Set<DeviceGroupMemberPO> members = group.getMembers();
		
		for(String bundleId: targetBundleIdList){
			DeviceGroupMemberPO member = queryUtil.queryMemberPOByBundleId(members, bundleId);
			CallBO call = new CallBO().setIncomingCall(group, callType);
//			PassByContentBO passByContent = new PassByContentBO().setIncoming_call_request(call);
//			PassByBO passBy = new PassByBO().setPassBy(member, passByContent);
//			logic.getPass_by().add(passBy);
		}
		
		executeBusiness.execute(logic, "被呼叫人消息推送：");
	}
	
	/**
	 * 
	 * @Title: reenterMeeting 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param groupUuid
	 * @param @param bundleId
	 * @param @throws Exception    设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	public void reenterMeeting(String groupUuid, String bundleId) throws Exception{
		DeviceGroupPO group = deviceGroupDao.findByUuid(groupUuid);		
		if(group == null) throw new Exception("该会议不存在！");
		
		//是否会议成员	
		Set<DeviceGroupMemberPO> members = group.getMembers();
		DeviceGroupMemberPO _member = new DeviceGroupMemberPO();
		boolean flag = false;
		for(DeviceGroupMemberPO member: members){
			if(member.getBundleId().equals(bundleId)){
				//如果加入会议已经接听，则不处理
				if(member.getMemberStatus().equals(MemberStatus.CONNECT)) return;
				flag = true;
				_member = member;
				break;
			}
		}
		if(!flag) throw new Exception(bundleId + "不是会议成员!");	
		
		LogicBO logic = new LogicBO();
		
		List<String> bundleIds = new ArrayList<String>();
		bundleIds.add(bundleId);
		
		logic = updateGroupMember(group, bundleIds, true, false, false);

		executeBusiness.execute(logic, "设备：" + _member.getBundleName() + "重新入会：");	
	}
	
	/**
	 * @Title: 设备组成员更新逻辑处理  <br/>
	 * @Description: 因为需要锁，所以放到一个方法里面来处理 
	 * @param groupUuid 设备组标识
	 * @param bundleIds 成员标识
	 * @param accept 成员接收入会/不接受入会（离会）标识（true代表接受入会，false代表不接受入会（离会））
	 * @param activeEnter 是否主动入会标识，非主动入会要发incomingCall并处理会议状态
	 * @param activeExit 是否主动离会标识，非主动离会要发hangup并处理会议状态
	 * @throws
	 */
	public LogicBO updateGroupMember(DeviceGroupPO group, List<String> bundleIds, boolean accept, boolean activeExit, boolean doProtocol) throws Exception{
		
		LogicBO logic = new LogicBO();
		
		if(group == null) throw new Exception("该多人通话不存在！");
		
		Long lockId = group.getId();
		
		synchronized (lockId) {
			
			//保证同步
			group = deviceGroupDao.findOne(lockId);
			
			//处理参数模板
			DeviceGroupAvtplPO avtpl = group.getAvtpl();
			DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
			CodecParamBO codec = new CodecParamBO().set(avtpl, currentGear);
			
			//处理设备组成员
			Set<DeviceGroupMemberPO> members = group.getMembers();
			List<DeviceGroupMemberPO> _members = queryUtil.queryMembersByBundleIds(members, bundleIds);
			DeviceGroupConfigPO defaultConfig = queryUtil.queryDefaultConfig(group);
			
			DeviceGroupConfigVideoPO video = new DeviceGroupConfigVideoPO();
			Set<DeviceGroupConfigVideoPositionPO> positions = new HashSet<DeviceGroupConfigVideoPositionPO>();
			if(group.getType().equals(GroupType.MULTIPLAYERVIDEO)){
				//处理设备组config（唯一）
			    video = defaultConfig.getVideos().iterator().next();
			    positions = video.getPositions();
			}
			
			boolean isDeleteGroup = false;
			
			if(accept){
				
				//接受：成员状态变为连通；合屏源更新和转发目的更新
				for(DeviceGroupMemberPO member: _members){
					member.setMemberStatus(MemberStatus.CONNECT);
				}
					
				//更新video
				if(group.getType().equals(GroupType.MULTIPLAYERVIDEO)){
					updateVideo(video, members, false, true, true);
					logic.merge(videoServiceImpl.setCombineVideo(group, video, false, false, false));
				}
				
//				List<Long> connectMemberIds = queryConnectMemberIds(members);
				meetingUtil.addAudio(defaultConfig, _members);
				List<Long> audioMemberIds = queryUtil.queryConfigAudio(defaultConfig);
				
				//混音
				logic.merge(audioServiceImpl.setGroupAudio(group, audioMemberIds, false, false, false));
				
				//成员变更通知：
				logic.merge(setMemberUpdate(group));
				
			}else{
				
				//不接受：成员状态变为未连通；检查合屏布局需不需要更新
				for(DeviceGroupMemberPO member: _members){
					member.setMemberStatus(MemberStatus.DISCONNECT);
				}
				
//				List<Long> connectMemberIds = queryConnectMemberIds(members);
				List<Long> conAndConingIds = queryConnectAndConnectingMemberIds(members);
				
				//视频处理
				if(group.getType().equals(GroupType.MULTIPLAYERVIDEO)){
				
//					if((activeExit && connectMemberIds.size() == 1) || conAndConingIds.size() == 1){
					if(conAndConingIds.size() == 1){
						if(!isDeleteGroup){
							logic.merge(stopGroup(group));
							isDeleteGroup = true;
						}					
					}else{
						
						//清除视频源
						removePositionSrc(positions, _members);
						
						logic.merge(videoServiceImpl.setCombineVideo(group, video, false, false, false));

					}
				}	
				
				//音频处理：主动离会需要更新混音
				if(activeExit){						
					if(conAndConingIds.size() == 1){
						if(!isDeleteGroup){
							logic.merge(stopGroup(group));
							isDeleteGroup = true;
						}	
					}else{
						
						meetingUtil.removeAudio(defaultConfig, _members);	
						List<Long> audioMemberIds = queryUtil.queryConfigAudio(defaultConfig);
						
						//混音
						audioServiceImpl.setGroupAudio(group, audioMemberIds, false, false, false);
						
						//成员变更通知：
						logic.merge(setMemberUpdate(group));

					}				
				}
			}
			
			logic.setForward(group.getForwards(), codec);
			
			//标识delete还是save设备组
			if(isDeleteGroup){
				deviceGroupDao.delete(group);
			}else{
				deviceGroupDao.save(group);
			}
			
			if(doProtocol){
				if(accept){
					executeBusiness.execute(logic, "多人通话设备进入：");
				}else{
					executeBusiness.execute(logic, "多人通话设备退出：");
				}
			}
		}
		
		return logic;
	}
	
	/**
	 * @Title: 删除合屏中中某个源
	 * @param positions 合屏位置信息
	 * @param member 源
	 * @return void
	 * @throws
	 */
	public void removePositionSrc(Collection<DeviceGroupConfigVideoPositionPO> positions, List<DeviceGroupMemberPO> members){
		if(positions != null && positions.size()>0){
			if(members != null && members.size()>0){
				for(DeviceGroupConfigVideoPositionPO position: positions){
					List<DeviceGroupConfigVideoSrcPO> srcs = position.getSrcs();
					if(srcs != null && srcs.size()>0){
						DeviceGroupConfigVideoSrcPO src = srcs.iterator().next();
						for(DeviceGroupMemberPO member: members){
							if(src.getMemberId().equals(member.getId())){
								src.setPosition(null);
								srcs.remove(src);
								break;
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * @Title: 管理员踢人
	 * @param groupUuid 设备组uuid：唯一标识
	 * @param bundleId 管理员设备id：唯一标识
	 * @param targetBundleIdList 踢出设备id数组
	 * @throws
	 */
	public DeviceGroupPO removeMembers(DeviceGroupPO group, List<String> targetBundleIdList) throws Exception{
				
		Set<DeviceGroupMemberPO> members = group.getMembers();
		//处理参数模板
		DeviceGroupAvtplPO avtpl = group.getAvtpl();
		DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
		CodecParamBO codec = new CodecParamBO().set(avtpl, currentGear);
		
		List<DeviceGroupMemberPO> disconnectBundles = queryUtil.queryMembersByBundleIds(members, targetBundleIdList);
		
		LogicBO logic = updateGroupMember(group, targetBundleIdList, false, true, false);
		logic.setDisconnectBundle(group, disconnectBundles, codec);
		
		executeBusiness.execute(logic, "管理员踢人：");
		
		return group;
	}
	
	/**
	 * @Title: 成员离线
	 * @param bundleId 设备id：唯一标识
	 * @throws
	 */
	public void memberOffline(String bundleId) throws Exception{
		LogicBO logic = new LogicBO();
		List<DeviceGroupMemberPO> members = deviceGroupMemberDao.findByBundleId(bundleId);
		for(DeviceGroupMemberPO member: members){
			if(member.getMemberStatus().equals(MemberStatus.CONNECT) && (member.getGroup().getType().equals(GroupType.MULTIPLAYERAUDIO) || member.getGroup().getType().equals(GroupType.MULTIPLAYERAUDIO))){
				logic.merge(exitGroup(member.getGroup().getUuid(), bundleId));
			}
		}
		
		executeBusiness.execute(logic, "成员离线，成员设备id为：" + bundleId);
	}
	
	/**
	 * @Title: 成员主动挂断
	 * @param groupUuid 设备组uuid：唯一标识
	 * @param bundleId 设备id：唯一标识
	 * @throws
	 */
	public LogicBO exitGroup(String groupUuid, String bundleId) throws Exception{
		
		LogicBO logic = new LogicBO();
		
		DeviceGroupPO group = deviceGroupDao.findByUuid(groupUuid);
		Set<DeviceGroupMemberPO> members = group.getMembers();
		List<DeviceGroupMemberPO> disconnectBundles = new ArrayList<DeviceGroupMemberPO>();
		
		//处理参数模板
		DeviceGroupAvtplPO avtpl = group.getAvtpl();
		DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
		CodecParamBO codec = new CodecParamBO().set(avtpl, currentGear);
		DeviceGroupMemberPO member = queryUtil.queryMemberPOByBundleId(members, bundleId);	
		
		disconnectBundles.add(member);		
		List<String> bundleIds = new ArrayList<String>();
		bundleIds.add(bundleId);
		logic = updateGroupMember(group, bundleIds, false, true, false);
		logic.setDisconnectBundle(group, disconnectBundles, codec);
		
		executeBusiness.execute(logic, "设备：" + member.getBundleName() + "主动挂断：");	
		
		return logic;
	}
	
	/**
	 * @Title: 停会（多人通话）协议处理 <br/>
	 * @param group 设备组会议信息
	 * @return LogicBO
	 * @throws
	 */
	public LogicBO stopGroup(DeviceGroupPO group) throws Exception{
		
		LogicBO logic = new LogicBO();
		
		//处理参数模板
		DeviceGroupAvtplPO avtpl = group.getAvtpl();
		DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
		CodecParamBO codec = new CodecParamBO().set(avtpl, currentGear);
		
		//清除、挂断设备		
		List<DeviceGroupMemberPO> totalMembers = new ArrayList<DeviceGroupMemberPO>();
		Set<DeviceGroupMemberPO> members = group.getMembers();
		for(DeviceGroupMemberPO member: members){
			if(member.getMemberStatus().equals(MemberStatus.CONNECT) || member.getMemberStatus().equals(MemberStatus.CONNECTING)){
				totalMembers.add(member);
			}
			member.setMemberStatus(MemberStatus.DISCONNECT);
		}
		
		//清除合屏
		List<CombineVideoPO> combineVideos = queryUtil.queryCombineVideosAsNewPointer(group);
		if(combineVideos != null){
			for(CombineVideoPO video:combineVideos){
				video.setGroup(null);
			}
			group.getCombineVideos().removeAll(combineVideos);
			combineVideoDao.deleteInBatch(combineVideos);
		}
		
		//清除混音
		List<CombineAudioPO> combineAudios = queryUtil.queryCombineAudiosAsNewPointer(group);
		if(combineAudios != null){
			for(CombineAudioPO audio:combineAudios){
				audio.setGroup(null);
			}
			group.getCombineAudios().removeAll(combineAudios);
			combineAudioDao.deleteInBatch(combineAudios);
		}
		
		//清除转发
		Set<ChannelForwardPO> forwards = group.getForwards();
		if(forwards != null){
			for(ChannelForwardPO forward:forwards){
				forward.setGroup(null);
			}
			group.getForwards().removeAll(forwards);
			channelForwardDao.deleteInBatch(forwards);
		}
		
		//处理协议
		logic.setUserId(group.getUserId().toString())
			 .setDisconnectBundle(group, totalMembers, codec)
			 .setCombineVideoDel(combineVideos)
			 .setCombineAudioDel(combineAudios)
			 .deleteForward(forwards, codec);
		
		//修改会议状态
		group.setStatus(GroupStatus.STOP);
		
		return logic;
	}
	
	/**
	 * @Title: 变更成员通知 <br/>
	 * @param group 设备组
	 * @return LogicBO 
	 * @throws
	 */
	public LogicBO setMemberUpdate(DeviceGroupPO group) throws Exception{
		
		LogicBO logic = new LogicBO();
		logic.setPass_by(new ArrayList<PassByBO>());
		
		Set<DeviceGroupMemberPO> members = group.getMembers();
		
		List<DeviceGroupMemberPO> connectMembers = meetingUtil.queryConnectMembers(members);
		
		for(DeviceGroupMemberPO connectMember: connectMembers){
			PassByBO passBy = new PassByBO().setMemberUpdate(group, connectMember, connectMembers);
			
			logic.getPass_by().add(passBy);
		}
		
		return logic;
	}
	
	/**
	 * @Title: 管理员变更通知 <br/>
	 * @param group 设备组
	 * @return LogicBO 
	 * @throws
	 */
	public LogicBO setAdministratorUpdate(DeviceGroupPO group, DeviceGroupMemberPO member) throws Exception{
		
		LogicBO logic = new LogicBO();
		logic.setPass_by(new ArrayList<PassByBO>());

		PassByBO passBy = new PassByBO().setAdministratorUpdate(group, member);		
		logic.getPass_by().add(passBy);
		
		return logic;
	}
	

	/**
	 * @Title: 多人通话“会议”生成唯一config里面包含唯一video <br/> 
	 * @param group 该会议信息
	 * @return void 
	 * @throws
	 */
	public void generateConfig(DeviceGroupPO group, String websiteDraw, List<JSONObject> positions, List<JSONObject> dsts, List<String> audios) throws Exception{
		
		Set<DeviceGroupMemberPO> members = group.getMembers();
		
		DeviceGroupConfigVideoPO video = new DeviceGroupConfigVideoPO();
		
		video.setName(group.getName() + "合屏画面");
		video.setVideoOperation(VideoOperationType.COMBINE);
		video.setLayout(ScreenLayout.SINGLE);
		video.setWebsiteDraw(websiteDraw);
		
		video.setPositions(new HashSet<DeviceGroupConfigVideoPositionPO>());
		video.setDsts(new HashSet<DeviceGroupConfigVideoDstPO>());
		
		//video生成
		meetingUtil.generateVideo(group, video, members, positions, dsts, null);
		
		DeviceGroupConfigPO config = new DeviceGroupConfigPO();
		config.setVideos(new HashSet<DeviceGroupConfigVideoPO>());
		config.setAudios(new HashSet<DeviceGroupConfigAudioPO>());
		video.setConfig(config);
		config.setName(group.getName() + ConfigType.DEFAULT.getName());
		config.setType(ConfigType.DEFAULT);
		config.getVideos().add(video);
		
		//audio生成
		meetingUtil.generateAudio(config, members, audios);
		
		group.setConfigs(new HashSet<DeviceGroupConfigPO>());
		config.setGroup(group);
		group.getConfigs().add(config);
		
		deviceGroupDao.save(group);
		
	}
	
	/**
	 * @Title: 判断是否使用同一个合屏布局 <br/> 
	 * @param members 当前多人通话成员数:需要过滤disconnect
	 * @param positions 当前的屏幕布局
	 * @return boolean
	 * @throws
	 */
	public boolean isSameLayout(Set<DeviceGroupMemberPO> members, Set<DeviceGroupConfigVideoPositionPO> positions) throws Exception{
		
		//找出合屏源个数
		int num1 = countCombineMembers(members);
		
		//已存在合屏源数
		int num2 = positions.size();
		
		if(num1>0 && num1<=2 && num2>0 && num2<=2){
			return true;
		}else if(num1>2 && num1<=4 && num2>2 && num2<=4){
			return true;
		}else if(num1>4 && num1<=9 && num2>4 && num2<=9){
			return true;
		}else if(num1>9 && num1<=16 && num2>9 && num2<=16){
			return true;
		}
		
		return false;
	}

	/**
	 * @Title: 唯一配置视频video更新，调用之前根据逻辑确定是要更新位置还是更新源还是更新目的，更新位置则必须更新源 <br/>
	 * @param video 视频信息
	 * @param members 设备组成员
	 * @param updatePosition 是否更新布局位置
	 * @param updateSrcs 是否更新源
	 * @param updateDsts 是否更新目的
	 * @return void 
	 * @throws
	 */
	public void updateVideo(DeviceGroupConfigVideoPO video, Set<DeviceGroupMemberPO> members, boolean updatePosition, boolean updateSrcs, boolean updateDsts) throws Exception{
		
		//找出合屏源
		List<DeviceGroupMemberPO> combineMembers = meetingUtil.queryConnectMembers(members);
		
		//找出合屏源总个数
		int _num = countCombineMembers(members);
		
		//更新位置(加了选模板之后咩用了)
		if(updatePosition){
			
			if(_num>0 && _num<=2){
				generatePositions(1, 2, video);
			}else if(_num>2 && _num<=4){
				generatePositions(2, 2, video);
			}else if(_num>4 && _num<=9){
				generatePositions(3, 3, video);
			}else if(_num>9 && _num<=16){
				generatePositions(4, 4, video);
			}		
		}
		
		//更新源
		if(updateSrcs){
			updateSrcs(video, combineMembers);
		}
		
		//更新目的
		if(updateDsts){
			meetingUtil.updateDsts(video, combineMembers);
		}		
	}
	
	/**
	 * @Title: 更新视频合屏 
	 * @param video 视频信息
	 * @param members 成员信息
	 * @param websiteDraw 前端合屏布局
	 * @param  positions 合屏位置信息
	 * @throws
	 */
	public void updateVideoPositions(DeviceGroupConfigVideoPO video, Collection<DeviceGroupMemberPO>members, String websiteDraw, List<JSONObject> positions) throws Exception{
		video.setWebsiteDraw(websiteDraw);
		Set<DeviceGroupConfigVideoPositionPO> oldPositions = video.getPositions();
		for(DeviceGroupConfigVideoPositionPO oldPosition: oldPositions){
			oldPosition.setVideo(null);
		}
		video.getPositions().removeAll(oldPositions);
		deviceGroupConfigVideoPositionDao.deleteInBatch(oldPositions);
		
		for(JSONObject position: positions){	
			DeviceGroupConfigVideoPositionPO positionPO = new DeviceGroupConfigVideoPositionPO();
			positionPO.setSerialnum(position.getIntValue("serialNum"));
			positionPO.setPictureType(PictureType.STATIC);
			positionPO.setH(position.getString("h"));
			positionPO.setW(position.getString("w"));
			positionPO.setX(position.getString("x"));
			positionPO.setY(position.getString("y"));
			positionPO.setSrcs(new ArrayList<DeviceGroupConfigVideoSrcPO>());
			
			List<String> srcList = JSONArray.parseArray(position.getString("src"), String.class);
			if(srcList.size()>1){
				positionPO.setPictureType(PictureType.POLLING);
				positionPO.setPollingStatus(PollingStatus.RUN);
				positionPO.setPollingTime(position.getString("pollingTime"));
			}else{
				positionPO.setPictureType(PictureType.STATIC);
			}
			
			//合屏源(区分类型)
			for(String srcString: srcList){
				if(position.getString("srcType").equals("BUNDLE")){
					DeviceGroupMemberPO member = queryUtil.queryMemberPOByBundleId(members, srcString);
					DeviceGroupMemberChannelPO channel = meetingUtil.queryVideoEncode1(member); 
					DeviceGroupConfigVideoSrcPO src = meetingUtil.transferSrc(member, channel);
					src.setPosition(positionPO);
					positionPO.getSrcs().add(src);
				}else if(position.getString("srcType").equals("CHANNEL")){
					String _bundleId = srcString.split("@@")[0];
					String _channelId = srcString.split("@@")[1];
					
					DeviceGroupMemberPO member = queryUtil.queryMemberPOByBundleId(members, _bundleId);
					DeviceGroupMemberChannelPO channel = queryUtil.queryMemberChannel(member, _channelId);
					
					DeviceGroupConfigVideoSrcPO src = meetingUtil.transferSrc(member, channel);
					src.setPosition(positionPO);
					positionPO.getSrcs().add(src);
				}
			}
			
			positionPO.setVideo(video);
			video.getPositions().add(positionPO);					
		}		
	}
	
	/**
	 * @Title: 更新配置音频 
	 * @param config 配置信息
	 * @param members 成员信息
	 * @param audios 音频信息
	 * @throws
	 */
	public void updateConfigAudio(DeviceGroupConfigPO config, Collection<DeviceGroupMemberPO>members, List<JSONObject> audios) throws Exception{
		
		//TODO
	}
	
	/**
	 * @Title: 找出合屏源总个数（过滤成员状态为disconnect的设备） <br/> 
	 * @param members 设备组成员
	 * @return int 合屏源个数
	 * @throws
	 */
	public int countCombineMembers(Set<DeviceGroupMemberPO> members) throws Exception{
		
		//找出合屏源总个数
		int _num = 0;
		for(DeviceGroupMemberPO member: members){
			if(!member.getMemberStatus().equals(MemberStatus.DISCONNECT)){			
				_num ++;
			}
		}
		
		return _num;
	}
	
	/**
	 * @Title: 找出所有联通成员的memberId数组：用于生成混音用 <br/> 
	 * @param members 所有成员
	 * @return List<Long> 所有联通成员的memberId数组
	 * @throws
	 */
	public List<Long> queryConnectMemberIds(Set<DeviceGroupMemberPO> members) throws Exception{		
		List<Long> connectMemberIds = new ArrayList<Long>();
		for(DeviceGroupMemberPO member: members){
			if(member.getMemberStatus().equals(MemberStatus.CONNECT)){
				connectMemberIds.add(member.getId());			
			}
		}		
		return connectMemberIds;
	}
	
	/**
	 * @Title: 找出所有联通和连接中成员的memberId数组：用于生成混音用 <br/> 
	 * @param members 所有成员
	 * @return List<Long> 所有联通成员的memberId数组
	 * @throws
	 */
	public List<Long> queryConnectAndConnectingMemberIds(Set<DeviceGroupMemberPO> members) throws Exception{		
		List<Long> connectMemberIds = new ArrayList<Long>();
		for(DeviceGroupMemberPO member: members){
			if(!member.getMemberStatus().equals(MemberStatus.DISCONNECT)){
				connectMemberIds.add(member.getId());			}
		}		
		return connectMemberIds;
	}	
		
	/**
	 * @Title: 布局生成 ,合屏布局改变，生成新的布局 <br/>
	 * @param row 布局行数
	 * @param column 布局列数
	 * @param video 关联的video
	 * @return void 
	 * @throws
	 */
	public void generatePositions(int row, int column, DeviceGroupConfigVideoPO video) throws Exception{
		video.setWebsiteDraw("{'basic':{'column':" + column + ",'row':" + row + "},'cellspan':[]}");
		for(int i=0;i<row;i++){
			for(int j=0;j<column;j++){
				DeviceGroupConfigVideoPositionPO position = new DeviceGroupConfigVideoPositionPO();
				position.setSerialnum(i * column + (j+1));
				position.setPictureType(PictureType.STATIC);
				position.setH("1/"+row);
				position.setW("1/"+column);
				position.setX(j%column + "/" +column);
				position.setY(i%row + "/" +row);
				position.setVideo(video);
				video.getPositions().add(position);	
			}				
		}
	}
	

	/**
	 * @Title: 更新源信息 ，连通的源放入position中<br/>
	 * @param video video信息
	 * @param members 连通的成员
	 * @return void 
	 * @throws
	 */
	public void updateSrcs(DeviceGroupConfigVideoPO video, List<DeviceGroupMemberPO> members) throws Exception{
		Set<DeviceGroupConfigVideoPositionPO> positions = video.getPositions();
		for(DeviceGroupMemberPO member: members){
			if(!isPositionsContainsMember(positions, member)){
				for(int i=1;i<=positions.size();i++){
					boolean flag = false;
					for(DeviceGroupConfigVideoPositionPO position: positions){
						if(position.getSerialnum() == i && (position.getSrcs() == null || position.getSrcs().size() == 0)){
							if(position.getSrcs() == null) position.setSrcs(new ArrayList<DeviceGroupConfigVideoSrcPO>());							
							DeviceGroupMemberChannelPO channel = meetingUtil.queryVideoEncode1(member); 
							DeviceGroupConfigVideoSrcPO src = meetingUtil.transferSrc(member, channel);
							src.setPosition(position);
							position.getSrcs().add(src);
							flag = true;
							break;
						}
					}
					if(flag) break;
				}
			}
		}		
	}
	
	/**
	 * @Title: 判断某一成员是否已经加入position中 <br/>
	 * @param positions 位置信息
	 * @param member 成员
	 * @return boolean
	 * @throws
	 */
	public boolean isPositionsContainsMember(Set<DeviceGroupConfigVideoPositionPO> positions, DeviceGroupMemberPO member){
		if(positions != null && positions.size() > 0){
			for(DeviceGroupConfigVideoPositionPO position: positions){
				if(position.getSrcs() != null && position.getSrcs().size() > 0 ){
					if(position.getSrcs().iterator().next().getBundleId().equals(member.getBundleId())){
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	/**
	 * @Title: 多人通话更新合屏
	 * @param groupUuid 多人通话uuid
	 * @param bundleId 管理员bundleId
	 * @param websiteDraw 前端合屏布局
	 * @param positions 合屏位置信息
	 * @throws 
	 */
	public void updateCombineVideoAndAudio(
			String groupUuid, 
			String userId, 
			String userName,
			String websiteDraw, 
			List<JSONObject> positions,
			List<JSONObject> audios) throws Exception{
		
		DeviceGroupPO group = deviceGroupDao.findByUuid(groupUuid);
		
		meetingUtil.incorrectGroupUserIdHandle(group, Long.valueOf(userId), userName);
		
		DeviceGroupConfigPO defaultConfig = queryUtil.queryDefaultConfig(group);
		
		DeviceGroupConfigVideoPO video = defaultConfig.getVideos().iterator().next();
		Set<DeviceGroupMemberPO> members = group.getMembers();
		
		LogicBO logic = new LogicBO();
		
		if(group.getType().equals(GroupType.MULTIPLAYERVIDEO)){
			//更新合屏
			updateVideoPositions(video, members, websiteDraw, positions);
			logic = videoServiceImpl.setCombineVideo(group, video, false, true, false);
		}else{
			//TODO
		}		
		
		deviceGroupDao.save(group);
		
		executeBusiness.execute(logic, "多人通话更改合屏：");
	}
	
}

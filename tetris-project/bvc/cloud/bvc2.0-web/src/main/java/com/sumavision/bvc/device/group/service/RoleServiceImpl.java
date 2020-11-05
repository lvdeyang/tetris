package com.sumavision.bvc.device.group.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumavision.bvc.command.group.enumeration.MemberStatus;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.dao.ChannelForwardDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigVideoDstDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupDAO;
import com.sumavision.bvc.device.group.enumeration.ForwardDstType;
import com.sumavision.bvc.device.group.enumeration.ForwardMode;
import com.sumavision.bvc.device.group.enumeration.GroupStatus;
import com.sumavision.bvc.device.group.po.ChannelForwardPO;
import com.sumavision.bvc.device.group.po.CombineAudioPO;
import com.sumavision.bvc.device.group.po.CombineVideoPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplPO;
import com.sumavision.bvc.device.group.po.DeviceGroupBusinessRolePO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigPO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoDstPO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoPO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberChannelPO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberPO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberScreenPO;
import com.sumavision.bvc.device.group.po.DeviceGroupPO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.system.enumeration.BusinessRoleSpecial;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;

/**
 * 角色相关操作<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年10月17日 下午1:38:35
 */
@Service
public class RoleServiceImpl {
	
	@Autowired
	private QueryUtil queryUtil;
	
	@Autowired
	private VideoServiceImpl videoServiceImpl;
	
	@Autowired
	private AudioServiceImpl audioServiceImpl;
	
	@Autowired
	private RecordServiceImpl recordServiceImpl;
	
	@Autowired
	private DeviceGroupDAO deviceGroupDao;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	@Autowired
	private ChannelForwardDAO channelForwardDAO;
	
	@Autowired
	private DeviceGroupConfigVideoDstDAO deviceGroupConfigVideoDstDao;

	/**
	 * 设置发言人<br/>
	 * <p>
	 * 	1.设置当前成员为该发言人<br/>
	 * 		--修改所有配置该发言人并且生效的合屏<br/>
	 * 		--修改该发言人的观看<br/>
	 * 	2.设置上一个该发言人的成员为观众<br/>
	 * 		--设置观众观看<br/>
	 * </p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月17日 下午1:41:12
	 * @return LogicBO 协议
	 * @throws Exception
	 */
	public LogicBO setSpokenman(
			DeviceGroupPO group, 
			DeviceGroupBusinessRolePO role, 
			DeviceGroupMemberPO newMember, 
			DeviceGroupMemberPO oldMember,
			boolean doPersistence, 
			boolean doForward, 
			boolean doProtocal) throws Exception{
		
		List<DeviceGroupConfigVideoPO> configVideos = queryUtil.queryEffectiveConfigVideosSettedSpokesman(group, role);
		
		//协议
		LogicBO logic = new LogicBO();
		
		CodecParamBO codec = null;
		
		if(doForward){
			//参数模板
			DeviceGroupAvtplPO avtpl = group.getAvtpl();
			DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
			codec = new CodecParamBO().set(avtpl, currentGear);
		} 
		
		//查询会议中的所有拼接屏
		queryUtil.queryCombineJv230s(group);
		
		//预设置角色
		if(newMember != null){
			newMember.setRoleId(role.getId());
			newMember.setRoleName(role.getName());
		}
		
		if(oldMember != null){
			oldMember.setRoleId(null);
			oldMember.setRoleName(null);
		}
		
		//修改发言人的观看
		if(newMember != null){
			logic.merge(setRole(group, role, newMember, false, false, false));
		}
	
		//设置旧成员为观众
		if(oldMember != null){
			List<DeviceGroupBusinessRolePO> roles = queryUtil.queryRoleBySpecial(group, BusinessRoleSpecial.AUDIENCE);
			if(roles!=null && roles.size()>0){
				logic.merge(setRole(group, roles.get(0), oldMember, false, false, false));
			}
		}
		
		//修改合屏协议
		if(configVideos!=null && configVideos.size()>0){
			for(DeviceGroupConfigVideoPO configVideo:configVideos){
				logic.merge(videoServiceImpl.setCombineVideo(group, configVideo, false, false, false));
			}
		}	
		
		//修改混音协议
		Set<CombineAudioPO> combineAudios = group.getCombineAudios();
		if(combineAudios != null && combineAudios.size()>0){
			List<BusinessRoleSpecial> specials = new ArrayList<BusinessRoleSpecial>();
			specials.add(BusinessRoleSpecial.CHAIRMAN);
			specials.add(BusinessRoleSpecial.SPOKESMAN);
			List<DeviceGroupMemberPO> spokeMembers = queryUtil.queryRoleMembersBySpecials(group, specials);
			if(spokeMembers != null && spokeMembers.size()>0){
				List<Long> spokeMemberIds = new ArrayList<Long>();
				for(DeviceGroupMemberPO spokeMember: spokeMembers){
					//过滤ipc
					if(!spokeMember.getBundleType().equals("ipc")){
						spokeMemberIds.add(spokeMember.getId());
					}
				}
				logic.merge(audioServiceImpl.setGroupAudio(group, spokeMemberIds, false, false, false));
			}
		}
		
		//处理视频录制联动
		if(group.isRecord() || group.hasRunningPublishStream()){
			if(configVideos!=null && configVideos.size()>0){
				for(DeviceGroupConfigVideoPO video:configVideos){
					logic.merge(recordServiceImpl.updateRecordWhenVideoRun(group, video, false, false));
				}
			}
		}
		
		if(doPersistence) deviceGroupDao.save(group);
		
		if(doForward) logic.merge(new LogicBO().setForward(group.getForwards(), codec));

		if(doProtocal) executeBusiness.execute(logic, "修改发言人：");
		
		return logic;
	}
	
	/**
	 * 设置角色<br/>
	 * <p>
	 * 	1.设置成员角色<br/>
	 * 		--设置角色观看
	 * </p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月17日 下午1:55:05
	 * @return LogicBO 协议
	 * @throws Exception
	 */
	public LogicBO setRole(
			DeviceGroupPO group, 
			DeviceGroupBusinessRolePO role, 
			DeviceGroupMemberPO member,
			boolean doPersistence, 
			boolean doForward,
			boolean doProtocal) throws Exception{
		
		//协议
		LogicBO logic = new LogicBO();
		
		//参数模板
		DeviceGroupAvtplPO avtpl = group.getAvtpl();
		DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
		CodecParamBO codec = new CodecParamBO().set(avtpl, currentGear);
		
		//更新成员角色
		member.setRoleId(role.getId());
		member.setRoleName(role.getName());
		
		//成员存在的video、dst删掉
		Set<DeviceGroupConfigPO> configs = group.getConfigs();
		if(configs != null && configs.size() > 0){
			for(DeviceGroupConfigPO config: configs){
				Set<DeviceGroupConfigVideoPO> videos = config.getVideos();
				if(videos != null && videos.size() > 0){
					for(DeviceGroupConfigVideoPO video: videos){
						Set<DeviceGroupConfigVideoDstPO> dsts = video.getDsts();
						List<DeviceGroupConfigVideoDstPO> _dsts = new ArrayList<DeviceGroupConfigVideoDstPO>();
						if(dsts != null && dsts.size() > 0){
							for(DeviceGroupConfigVideoDstPO dst: dsts){
								if((dst.getType().equals(ForwardDstType.SCREEN) && dst.getBundleId().equals(member.getBundleId()))){
									_dsts.add(dst);
								}
							}
						}
						if(_dsts.size() > 0) video.getDsts().removeAll(_dsts);
					}
				}
			}
		}
		
		//区分主席、发言人/观众、自定义角色观看
		if(role.getSpecial().equals(BusinessRoleSpecial.SPOKESMAN) || role.getSpecial().equals(BusinessRoleSpecial.CHAIRMAN) || group.getForwardMode().equals(ForwardMode.DEVICE)){
			
			//获取配置当前角色的成员
			List<DeviceGroupMemberPO> roleMembers = queryUtil.queryMemberByRole(group, role.getId());
			
			if(roleMembers==null || roleMembers.size()<=0){
				if(doPersistence) deviceGroupDao.save(group);
				return logic;
			}
			
			Set<DeviceGroupMemberScreenPO> memberScreens = member.getScreens();
			if(memberScreens==null || memberScreens.size()<=0){
				if(doPersistence) deviceGroupDao.save(group);
				return logic;
			}
			
			//获取成员屏幕id类型
			Map<String, String> ScreenIdMap = queryUtil.queryMemberUnionScreenId(member);
			if(ScreenIdMap!=null && ScreenIdMap.size()>0){
				Set<String> screenIds = ScreenIdMap.keySet();
				for(String screenId:screenIds){			
					//查找角色对应的观看
					List<DeviceGroupConfigVideoPO> videos = queryUtil.queryRoleConfigVideo(group, role.getId(), screenId);
					if(videos!=null && videos.size()>0){
						//获取角色成员对应的屏幕
						List<DeviceGroupMemberScreenPO> screens = new ArrayList<DeviceGroupMemberScreenPO>();
						for(DeviceGroupMemberPO roleMember:roleMembers){
							Set<DeviceGroupMemberScreenPO> roleScreens = roleMember.getScreens();
							if(roleScreens!=null && roleScreens.size()>0){
								for(DeviceGroupMemberScreenPO roleScreen:roleScreens){
									if(screenId.equals(roleScreen.getScreenId())){
										screens.add(roleScreen);
										break;
									}
								}
							}
						}
						
						//查找角色屏幕有效的视频配置
						List<ChannelForwardPO> forwards = queryUtil.queryForwardByScreens(group, screens);
						DeviceGroupConfigVideoPO target = null;
						if(forwards!=null && forwards.size()>0){
							for(DeviceGroupConfigVideoPO video:videos){
								boolean suitable = false;
								for(ChannelForwardPO forward:forwards){
									if(video.getUuid().equals(forward.getOriginVideoUuid())){
										suitable = true;
										break;
									}
								}
								if(suitable){
									target = video;
									break;
								} 
							}
						}
						
						if(target == null){
							Set<CombineVideoPO> combineVideos = group.getCombineVideos();
							for(DeviceGroupConfigVideoPO video: videos){
								boolean suitable = false;
								for(CombineVideoPO combineVideo: combineVideos){
									if(video.getUuid().equals(combineVideo.getUuid())){
										suitable = true;
										break;
									}
								}							
								if(suitable){
									target = video;
									break;
								}
							}
						}
						
						//设置转发
						if(target != null){
							CombineVideoPO combineVideo = queryUtil.queryCombineVideo(group, target.getUuid());
							if(member.getMemberStatus().equals(MemberStatus.CONNECT)){
								//设置转发
								logic.merge(videoServiceImpl.setVideoForwardAddScreen(group, target, combineVideo, screenId, member, false));
							}
						}
						
					}
				}
			}
			
			List<ChannelForwardPO> addForwards = queryUtil.queryForwardByMember(group, member);
			if(doForward) logic.merge(new LogicBO().setForward(addForwards, codec));	
		}else {
			
			if(group.getStatus().equals(GroupStatus.START)){
				
				if(member.getMemberStatus().equals(MemberStatus.CONNECT)){
					//删除设备视频forward转发
					List<ChannelForwardPO> oldVideoForwards = queryUtil.queryVideoForward(group, member.getId());
					group.getForwards().removeAll(oldVideoForwards);
					
					//切换节点
					List<DeviceGroupMemberPO> members = new ArrayList<DeviceGroupMemberPO>();
					members.add(member);
					logic.setRolePassby(role, members);
					
					List<ChannelForwardPO> forwards = queryUtil.queryVirtualRoleChannelForward(group, role.getId());
					logic.merge(new LogicBO().setForward(new ArrayListWrapper<ChannelForwardPO>().addAll(forwards).getList(), codec));
					
					//openBundle == 设备观看角色
					logic.setRoleForward(member, role, codec);
				}
			}

		}
		
		if(doPersistence) deviceGroupDao.save(group);
		
		if(doProtocal) executeBusiness.execute(logic, "设置角色：");
		
		return logic;
	}
	
	/**
	 * 成员移除角色<br/>
	 * <p>
	 * 	1.移除成员角色<br/>
	 * 		--移除角色观看
	 * </p>
	 * @return LogicBO 协议
	 * @throws Exception
	 */
	public LogicBO removeRole(
			DeviceGroupPO group, 
			DeviceGroupBusinessRolePO role, 
			List<DeviceGroupMemberPO> members,
			boolean doPersistence, 
			boolean doForward,
			boolean doProtocal) throws Exception{
		
		//协议
		LogicBO logic = new LogicBO();
		
		//参数模板
		DeviceGroupAvtplPO avtpl = group.getAvtpl();
		DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
		CodecParamBO codec = new CodecParamBO().set(avtpl, currentGear);
		
		//更新成员角色
		for(DeviceGroupMemberPO member: members){
			member.setRoleId(null);
			member.setRoleName(null);
		}
		
		if(group.getStatus().equals(GroupStatus.START)){
			for(DeviceGroupMemberPO member: members){
				logic.merge(new LogicBO().setRoleForwardNull(member, codec));
			}
		}
		
		//迭代4之后无用
//		Set<DeviceGroupMemberChannelPO> memberChannels = member.getChannels();
//		if(memberChannels==null || memberChannels.size()<=0){
//			if(doPersistence) deviceGroupDao.save(group);
//			return logic;
//		}
//		
//		//获取成员解码通道类型
//		Map<String, String> ScreenIdMap = queryUtil.queryMemberUnionScreenId(member);
//		if(ScreenIdMap!=null && ScreenIdMap.size()>0){
//			Set<String> screenIds = ScreenIdMap.keySet();
//			
//			List<ChannelForwardPO> allForwards = new ArrayList<ChannelForwardPO>();
//			for(String screenId:screenIds){			
//				//查找角色对应的观看
//				List<DeviceGroupConfigVideoPO> videos = queryUtil.queryRoleConfigVideo(group, role.getId(), screenId);
//				if(videos!=null && videos.size()>0){
//					//获取角色成员对应的屏幕
//					List<DeviceGroupMemberScreenPO> screens = new ArrayList<DeviceGroupMemberScreenPO>();
//					Set<DeviceGroupMemberScreenPO> roleScreens = member.getScreens();
//					if(roleScreens!=null && roleScreens.size()>0){
//						for(DeviceGroupMemberScreenPO roleScreen:roleScreens){
//							if(screenId.equals(roleScreen.getScreenId())){
//								screens.add(roleScreen);
//								break;
//							}
//						}
//					}
//
//					List<ChannelForwardPO> forwards = queryUtil.queryForwardByScreens(group, screens);
//					allForwards.addAll(forwards);													
//				}
//			}
//			
//			for(ChannelForwardPO deleteForward: allForwards){
//				deleteForward.setGroup(null);
//			}
//			
//			if(doForward) logic.merge(logic.deleteForward(allForwards, codec));
//						
//			group.getForwards().removeAll(allForwards);
//		}	
		
		if(doPersistence) deviceGroupDao.save(group);
		
		if(doProtocal) executeBusiness.execute(logic, "设备移除角色：");
		
		return logic;
	}
	
}

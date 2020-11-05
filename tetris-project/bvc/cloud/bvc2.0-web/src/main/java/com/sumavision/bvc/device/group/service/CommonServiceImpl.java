package com.sumavision.bvc.device.group.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.common.group.dao.CommonBusinessRoleDAO;
import com.sumavision.bvc.common.group.dao.CommonChannelForwardDAO;
import com.sumavision.bvc.common.group.dao.CommonCombineVideoDAO;
import com.sumavision.bvc.common.group.dao.CommonConfigDAO;
import com.sumavision.bvc.common.group.dao.CommonConfigVideoDstDAO;
import com.sumavision.bvc.common.group.dao.CommonConfigVideoSrcDAO;
import com.sumavision.bvc.common.group.dao.CommonGroupDAO;
import com.sumavision.bvc.common.group.dao.CommonMemberDAO;
import com.sumavision.bvc.common.group.po.CommonAvtplGearsPO;
import com.sumavision.bvc.common.group.po.CommonAvtplPO;
import com.sumavision.bvc.common.group.po.CommonBusinessRolePO;
import com.sumavision.bvc.common.group.po.CommonChannelForwardPO;
import com.sumavision.bvc.common.group.po.CommonCombineVideoPO;
import com.sumavision.bvc.common.group.po.CommonConfigAudioPO;
import com.sumavision.bvc.common.group.po.CommonConfigPO;
import com.sumavision.bvc.common.group.po.CommonConfigVideoDstPO;
import com.sumavision.bvc.common.group.po.CommonConfigVideoPO;
import com.sumavision.bvc.common.group.po.CommonConfigVideoPositionPO;
import com.sumavision.bvc.common.group.po.CommonGroupPO;
import com.sumavision.bvc.common.group.po.CommonMemberChannelPO;
import com.sumavision.bvc.common.group.po.CommonMemberPO;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.enumeration.AudioOperationType;
import com.sumavision.bvc.device.group.enumeration.ConfigType;
import com.sumavision.bvc.device.group.enumeration.ForwardDstType;
import com.sumavision.bvc.device.group.enumeration.ForwardMode;
import com.sumavision.bvc.device.group.enumeration.ScreenLayout;
import com.sumavision.bvc.device.group.enumeration.VideoOperationType;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.CommonQueryUtil;
import com.sumavision.bvc.device.group.service.util.CommonUtil;
import com.sumavision.bvc.device.group.service.util.ResourceQueryUtil;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.bvc.system.enumeration.BusinessRoleSpecial;
import com.sumavision.bvc.system.enumeration.BusinessRoleType;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Transactional(rollbackFor = Exception.class)
@Service
public class CommonServiceImpl {
	
	@Autowired
	private CommonGroupDAO commonGroupDao;
	
	@Autowired
	private CommonCombineVideoDAO commonCombineVideoDao;
	
	@Autowired
	private CommonChannelForwardDAO commonChannelForwardDAO;
	
	@Autowired
	private CommonBusinessRoleDAO commonBusinessRoleDao;
	
	@Autowired
	private CommonConfigDAO commonConfigDao;
	
	@Autowired
	private CommonQueryUtil commonQueryUtil;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private ResourceQueryUtil resourceQueryUtil;
	
	@Autowired
	private CommonVideoServiceImpl commonVideoServiceImpl;
	
	@Autowired
	private CommonAudioServiceImpl commonAudioServiceImpl;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private CommonMemberDAO commonMemberDao;
	
	@Autowired
	private CommonConfigVideoDstDAO commonConfigVideoDstDao;
	
	@Autowired
	private CommonConfigVideoSrcDAO commonConfigVideoSrcDao;
	
	/**
	 * 创建议程，并且不依赖会议<br/>
	 * @Title: setAgenda 
	 * @param group 设备组信息
	 * @param agendaName 议程名
	 * @param remark 备注信息
	 * @param audio 议程音频信息
	 * @param videos 议程视频信息
	 * @return DeviceGroupConfigPO
	 * @throws
	 */
	public CommonConfigPO setAgenda(
					Long userId,
//					Set<CommonMemberPO> members, 
					String agendaName, 
					String remark, 
					JSONObject audio, 
					List<JSONObject> videos,
					String forwardMode) throws Exception{

		CommonGroupPO group = commonUtil.generateVirtualGroup(userId, agendaName+"的虚拟会议", ForwardMode.fromName(forwardMode));
		
		CommonConfigPO agenda = new CommonConfigPO();
		
		//取出所有的bundleId
		Set<String> bundleIds = new HashSet<String>();
		
		agenda.setName(agendaName);
		agenda.setRemark(remark);
		agenda.setType(ConfigType.AGENDA);
		agenda.setRun(false);
		
		//音频
		String audioOperation = audio.getString("audioOperation");
		agenda.setAudioOperation(AudioOperationType.fromName(audioOperation));
		
		//从音频中取出bundleId
		if(audioOperation.equals(AudioOperationType.CUSTOM.getName())){						
		    List<String> audioSrcs = JSONArray.parseArray(audio.getJSONArray("audioSrcs").toJSONString(), String.class);		    
		    bundleIds.addAll(audioSrcs);
		}
		
		//从视频中取出bundleId
		for(JSONObject video: videos){
			
			List<JSONObject> positions = JSONArray.parseArray(video.getJSONArray("positions").toJSONString(), JSONObject.class);
			List<JSONObject> dsts = JSONArray.parseArray(video.getJSONArray("dsts").toJSONString(), JSONObject.class);
			
			//从positions中取出所有源的bundleId
			for(JSONObject position: positions){				
				List<String> srcList = JSONArray.parseArray(position.getString("src"), String.class);				
				//合屏源(区分类型)
				for(String srcString: srcList){
					if(position.getString("srcType").equals("BUNDLE")){
						bundleIds.add(srcString);
					}else if(position.getString("srcType").equals("CHANNEL")){
						String _bundleId = srcString.split("@@")[0];
						bundleIds.add(_bundleId);
					}
				}
			}
			
			//从dsts中取出所有源的bundleId
			if(null != dsts && dsts.size() > 0){
				for(JSONObject dst: dsts){
					if(dst.getString("dstType").equals("BUNDLE")){
						bundleIds.add(dst.getString("dst"));
					}else if(dst.getString("dstType").equals("SCREEN")){
						String _bundleId = dst.getString("dst").split("@@")[0];
						bundleIds.add(_bundleId);
					}
				}
			}
		}
		
		//bundleIds生成members
		List<String> bundleIdsList = new ArrayList<>(bundleIds);
		Set<CommonMemberPO> members = commonUtil.generateMembers(bundleIdsList);
		
		//group与members关联
		group.setMembers(members);
		for(CommonMemberPO member : members){
			member.setGroup(group);
		}
		
		if(audioOperation.equals(AudioOperationType.CUSTOM.getName())){
			
			int volume = audio.getIntValue("volume");
			audio.getString("audioMode");
		    List<String> audioSrcs = JSONArray.parseArray(audio.getJSONArray("audioSrcs").toJSONString(), String.class);
		    
		    agenda.setVolume(volume);
		    agenda.setAudios(new HashSet<CommonConfigAudioPO>());
		    
		    //生成audio
		    commonUtil.generateAudio(agenda, members, audioSrcs);	    
		}
		
		//视频
		agenda.setVideos(new HashSet<CommonConfigVideoPO>());
		for(JSONObject video: videos){
		
			String videoName = video.getString("videoName");
			//TODO 转发模式
			String videoMode = video.getString("videoMode");
			String websiteDraw = video.getString("layout");
			List<JSONObject> positions = JSONArray.parseArray(video.getJSONArray("positions").toJSONString(), JSONObject.class);
			List<JSONObject> dsts = JSONArray.parseArray(video.getJSONArray("dsts").toJSONString(), JSONObject.class);
			
			CommonConfigVideoPO videoPO = new CommonConfigVideoPO();
			
			videoPO.setName(videoName);
			videoPO.setLayout(ScreenLayout.SINGLE);
			videoPO.setVideoOperation(VideoOperationType.fromName(videoMode));
			videoPO.setWebsiteDraw(websiteDraw);
			videoPO.setUpdateTime(new Date());
			videoPO.setPositions(new HashSet<CommonConfigVideoPositionPO>());
			videoPO.setDsts(new HashSet<CommonConfigVideoDstPO>());
			
			//生成video
			commonUtil.generateVideo(group, videoPO, members, positions, dsts, null);
			
			videoPO.setConfig(agenda);
			agenda.getVideos().add(videoPO);
		}	
		
		agenda.setGroup(group);
		if(null == group.getConfigs()) group.setConfigs(new HashSet<CommonConfigPO>());
		group.getConfigs().add(agenda);
		
		commonGroupDao.save(group);
		
		return agenda;
	}
	
	/**
	 * 在会议中新建角色<br/>
	 * @Title: saveNewRole 
	 * @param forwardMode
	 * @param name
	 * @param special
	 * @param type
	 * @return role DeviceGroupBusinessRolePO
	 * @throws
	 */
	public CommonBusinessRolePO saveNewRole(String forwardMode, String name, String special, String type) throws Exception{
		CommonBusinessRolePO role = new CommonBusinessRolePO();
		
		role.setName(name);
		role.setSpecial(BusinessRoleSpecial.fromName(special));
		role.setType(BusinessRoleType.fromName(type));
		role.setUpdateTime(new Date());
		
		//save以生成id
		commonBusinessRoleDao.save(role);
		
		if(ForwardMode.ROLE.equals(ForwardMode.fromName(forwardMode))){
			//角色绑定虚拟设备，返回通道信息(判断角色类型)
			if(role.getSpecial().equals(BusinessRoleSpecial.AUDIENCE) || role.getSpecial().equals(BusinessRoleSpecial.CUSTOM)){
				if(role.getBundleId() == null || role.getLayerId() == null){
					//role的uuid作为虚拟设备识别的唯一标识
					BundlePO bundleInfo = resourceService.bindVirtualDev(role.getUuid() + role.getId());
					
					List<String> bundleInfoIds = new ArrayList<String>();
					bundleInfoIds.add(bundleInfo.getBundleId());
					List<ChannelSchemeDTO> channelInfos = resourceQueryUtil.queryAllChannelsByBundleIds(bundleInfoIds);
					StringBufferWrapper sBufferWrapper = new StringBufferWrapper();
					for(int i=0; i<channelInfos.size(); i++){
						if(i == channelInfos.size()-1){
							sBufferWrapper.append(channelInfos.get(i).getChannelId());
						}else{
							sBufferWrapper.append(channelInfos.get(i).getChannelId()).append("%");
						}
					}
					
					role.setBundleId(bundleInfo.getBundleId());
					role.setLayerId(bundleInfo.getAccessNodeUid());
					role.setBaseType(bundleInfo.getBundleType());
					role.setChannel(sBufferWrapper.toString());
				}
			}
		}
		
		commonBusinessRoleDao.save(role);
		
		return role;
	}
	
	/**
	 * 删除角色<br/>
	 * @Title: removeRole 
	 * @param groupId
	 * @param roleId
	 * @throws
	 */
	public void removeRole(Long roleId) throws Exception{
		CommonBusinessRolePO role = commonBusinessRoleDao.findOne(roleId);
		CommonGroupPO group = role.getGroup();//可能为null
		
//		if(group.getForwardMode().equals(ForwardMode.ROLE)){
		if(role.getSpecial().equals(BusinessRoleSpecial.AUDIENCE) || role.getSpecial().equals(BusinessRoleSpecial.CUSTOM)){

			if(role.getBundleId() != null){
				//解绑
				resourceService.unBindVirtualDev(role.getBundleId());
			}
		}
//		}

		//成员解绑该角色
//		Set<CommonMemberPO> members = group.getMembers();
		Set<Long> roleIds = new HashSet<Long>();
		roleIds.add(roleId);
		List<CommonMemberPO> members = commonMemberDao.findByRoleIdIn(roleIds);
		for(CommonMemberPO member: members){
			if(roleId.equals(member.getRoleId())){
				member.setRoleId(null);
				member.setRoleName(null);
			}
		}
		
		if(null != group){
			commonGroupDao.save(group);
		}
		
//		commonRecordSchemeDao.deleteByRoleId(roleId);
		commonConfigVideoDstDao.deleteByRoleId(roleId);
		commonConfigVideoSrcDao.deleteByRoleId(roleId);
		commonBusinessRoleDao.delete(roleId);
	}
	
	/**
	 * 删除议程<br/>
	 * @Title: removeAgenda 
	 * @param agendaId
	 * @throws
	 */
	public void removeAgenda(Long agendaId) throws Exception{
		//解关联
		CommonConfigPO agenda = commonConfigDao.findOne(agendaId);
		CommonGroupPO group = agenda.getGroup();
		
//		commonUtil.incorrectGroupUserIdHandle(group, Long.valueOf(userId), userName);
		
		if(group != null){
			group.getConfigs().remove(agenda);
		}
		agenda.setGroup(null);
		
		commonConfigDao.delete(agenda);
	}
	
	/**
	 * 执行议程 <br/>
	 * @Title: runAgenda
	 * @param agendaId 议程id
	 * @throws Exception
	 * @return 
	 */
	public CommonConfigPO runAgenda(Long agendaId) throws Exception{
		
		synchronized (agendaId) {
			
			//获取议程
			CommonConfigPO agenda = commonConfigDao.findOne(agendaId);
			CommonGroupPO group = agenda.getGroup();
			
			//查询会议中的所有拼接屏
//			queryUtil.queryCombineJv230s(group);
			
			//处理参数模板
			CommonAvtplPO avtpl = group.getAvtpl();
			CommonAvtplGearsPO currentGear = commonQueryUtil.queryCurrentGear(group);
			CodecParamBO codec = new CodecParamBO().set(avtpl, currentGear);
			
			LogicBO logic = new LogicBO();
			
			//处理视频
			Set<CommonConfigVideoPO> videos = agenda.getVideos();
			Set<String> exceptVideoUuids = new HashSet<String>();
			Set<Long> exceptChannelIds = new HashSet<Long>();
			//解码1角色
			Set<Long> exceptRoleIds1 = new HashSet<Long>();
			//解码2绝色
			Set<Long> exceptRoleIds2 = new HashSet<Long>();
			
			//虚拟源合屏加入（去掉）

			if(videos!=null && videos.size()>0){
				for(CommonConfigVideoPO video:videos){
					//获取所有的合屏uuid
					exceptVideoUuids.add(video.getUuid());
					
					LogicBO scopeLogic = commonVideoServiceImpl.setCombineVideo(group, video, false, false, false);
					logic.merge(scopeLogic);
					
					//获取所有的目的通道
					Set<CommonConfigVideoDstPO> dsts = video.getDsts();
					if(dsts!=null && dsts.size()>0){
						for(CommonConfigVideoDstPO dst:dsts){
							if(ForwardDstType.ROLE.equals(dst.getType())){
								CommonBusinessRolePO role = commonQueryUtil.queryRoleById(group, dst.getRoleId());
								if(role.getSpecial().equals(BusinessRoleSpecial.CHAIRMAN) || role.getSpecial().equals(BusinessRoleSpecial.SPOKESMAN) || group.getForwardMode().equals(ForwardMode.DEVICE)){
									//主席、发言人
									List<CommonMemberPO> dstMembers = commonQueryUtil.queryMemberByRole(group, dst.getRoleId());									
									for(CommonMemberPO dstMember: dstMembers){
										List<CommonMemberChannelPO> dstDecodeChannels = commonQueryUtil.queryUsefulMemberDecodeChannelByScreenId(group, dstMember.getId(), dst.getScreenId());
										if(dstDecodeChannels != null && dstDecodeChannels.size() > 0){
											CommonMemberChannelPO dstDecodeChannel = dstDecodeChannels.get(0);
											if(dstDecodeChannel != null) exceptChannelIds.add(dstDecodeChannel.getId());
											if(video.getSmall() != null) {
												CommonMemberChannelPO smallDstDecodeChannel = dstDecodeChannels.get(1);
												if(smallDstDecodeChannel != null) exceptChannelIds.add(smallDstDecodeChannel.getId());
											}
										}
									}
								}else{
									exceptRoleIds1.add(dst.getRoleId());
									if(video.getSmall() != null) {
										exceptRoleIds2.add(dst.getRoleId());
									}
								}
							}else{
								if(!dst.getBundleType().equals("combineJv230")){
									List<CommonMemberChannelPO> dstDecodeChannels = commonQueryUtil.queryUsefulMemberDecodeChannelByScreenId(group, dst.getMemberId(), dst.getScreenId());
									exceptChannelIds.add(dst.getMemberChannelId());
									if(video.getSmall() != null) {
										CommonMemberChannelPO smallDstDecodeChannel = dstDecodeChannels.get(1);
										if(smallDstDecodeChannel != null) exceptChannelIds.add(smallDstDecodeChannel.getId());
									}
								}else{
									exceptChannelIds.add(dst.getMemberChannelId());
								}
							}
						}
					}
				}
			}
			
			//处理需要清除的合屏
			List<CommonCombineVideoPO> needDeleteCombineVideos = commonQueryUtil.queryCombineVideoExceptUuids(group, exceptVideoUuids);
			if(needDeleteCombineVideos!=null && needDeleteCombineVideos.size()>0){
				for(CommonCombineVideoPO scopeCombineVideo:needDeleteCombineVideos){
					scopeCombineVideo.setGroup(null);
				}
				group.getCombineVideos().removeAll(needDeleteCombineVideos);
				commonCombineVideoDao.deleteInBatch(needDeleteCombineVideos);
				//处理删除合屏
				logic.setCombineVideoDel_Common(needDeleteCombineVideos);
			}
			
			//处理需要清除的转发
			List<CommonChannelForwardPO> needDeleteForwards = commonQueryUtil.queryVideoChannelForwardExceptMemberChannelId(group, exceptChannelIds);
			needDeleteForwards.addAll(commonQueryUtil.queryVideoChannelForwardExceptRoleId(group, exceptRoleIds1, exceptRoleIds2));
			if(needDeleteForwards!=null && needDeleteForwards.size()>0){
				for(CommonChannelForwardPO scopeForward:needDeleteForwards){
					scopeForward.setGroup(null);
				}
				group.getForwards().removeAll(needDeleteForwards);
				List<CommonChannelForwardPO> persistentForwards = new ArrayList<CommonChannelForwardPO>();
				for(CommonChannelForwardPO scopeForward:needDeleteForwards){
					if(scopeForward.getId() != null){
						persistentForwards.add(scopeForward);
					}
				}
				if(persistentForwards.size() > 0){
					commonChannelForwardDAO.deleteInBatch(persistentForwards);
				}
				//处理删除转发
				logic.deleteForward_Common(needDeleteForwards, codec);
			}
			
			
			//virtual空源
			Set<Long> roleIds = new HashSet<Long>();
			for(CommonChannelForwardPO channelForward: needDeleteForwards){
				if(channelForward.getForwardDstType().equals(ForwardDstType.ROLE)){
					roleIds.add(channelForward.getRoleId());
				}
			}
			
			for(Long roleId: roleIds){
				CommonBusinessRolePO role = commonQueryUtil.queryRoleById(group, roleId);
				List<CommonMemberPO> dstMembers = commonQueryUtil.queryMemberByRole(group, roleId);
				logic.merge(new LogicBO().setRolePassby(role, dstMembers));
			}
			
			//TODO:清除jv230转发
			
			//处理音频
			//先找主席和发言人
			List<BusinessRoleSpecial> specials = new ArrayList<BusinessRoleSpecial>();
			specials.add(BusinessRoleSpecial.CHAIRMAN);
			specials.add(BusinessRoleSpecial.SPOKESMAN);
			List<CommonMemberPO> spokeMembers = commonQueryUtil.queryRoleMembersBySpecials(group, specials);
			if(spokeMembers != null && spokeMembers.size()>0){
				List<Long> spokeMemberIds = new ArrayList<Long>();
				for(CommonMemberPO spokeMember: spokeMembers){
					//过滤ipc
					if(!spokeMember.getBundleType().equals("ipc")){
						spokeMemberIds.add(spokeMember.getId());
					}
				}
				logic.merge(commonAudioServiceImpl.setGroupAudio(group, spokeMemberIds, false, false, false));
			}else{
				if(AudioOperationType.CUSTOM.equals(agenda.getAudioOperation())){
					Set<CommonConfigAudioPO> audios = agenda.getAudios();
					if(audios!=null && audios.size()>0){
						Set<Long> voicedIds = new HashSet<Long>();
						for(CommonConfigAudioPO audio:audios){
							voicedIds.add(audio.getMemberId());
						}
						LogicBO scopeLogic = commonAudioServiceImpl.setGroupAudio(group, voicedIds, false, false, false);
						logic.merge(scopeLogic);
					}else{
						LogicBO scopeLogic = commonAudioServiceImpl.setGroupAudio(group, new ArrayList<Long>(), false, false, false);
						logic.merge(scopeLogic);
					}
				}else if(AudioOperationType.MUTE.equals(agenda.getAudioOperation())){
					LogicBO scopeLogic = commonAudioServiceImpl.setGroupAudio(group, new ArrayList<Long>(), false, false, false);
					logic.merge(scopeLogic);
				}
			}		
			
			//处理视频录制联动（去掉）			
			//处理音频录制联动（去掉）
			
			//处理转发
			logic.setForward_Common(group.getForwards(), codec);
			
			//持久化数据
			commonGroupDao.save(group);		
			
			//过滤协议
			logic = logic.doFilter(group);
			
			//调用逻辑层
			executeBusiness.execute(logic, "议程执行：转发");
						
			return agenda;
		}
	}
}

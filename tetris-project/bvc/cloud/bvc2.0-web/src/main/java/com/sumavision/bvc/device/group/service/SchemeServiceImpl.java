package com.sumavision.bvc.device.group.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.device.group.bo.AudioParamBO;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.RecordSetBO;
import com.sumavision.bvc.device.group.bo.RecordSourceBO;
import com.sumavision.bvc.device.group.bo.VideoParamBO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigVideoDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigVideoDstDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigVideoPositionDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupMemberDAO;
import com.sumavision.bvc.device.group.dao.RecordDAO;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.enumeration.ConfigType;
import com.sumavision.bvc.device.group.enumeration.ForwardDstType;
import com.sumavision.bvc.device.group.enumeration.ForwardSrcType;
import com.sumavision.bvc.device.group.enumeration.GroupStatus;
import com.sumavision.bvc.device.group.enumeration.GroupType;
import com.sumavision.bvc.device.group.enumeration.PictureType;
import com.sumavision.bvc.device.group.enumeration.PollingStatus;
import com.sumavision.bvc.device.group.enumeration.RecordType;
import com.sumavision.bvc.device.group.enumeration.ScreenLayout;
import com.sumavision.bvc.device.group.enumeration.SourceType;
import com.sumavision.bvc.device.group.enumeration.VideoOperationType;
import com.sumavision.bvc.device.group.exception.DeviceGroupHasNotStartedException;
import com.sumavision.bvc.device.group.exception.SchemeNameAlreadyExistedException;
import com.sumavision.bvc.device.group.po.ChannelForwardPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplPO;
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
import com.sumavision.bvc.device.group.po.RecordPO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.system.dao.DictionaryDAO;
import com.sumavision.bvc.system.enumeration.BusinessRoleSpecial;
import com.sumavision.bvc.system.enumeration.DicType;
import com.sumavision.bvc.system.po.DictionaryPO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Transactional(rollbackFor = Exception.class)
@Service
public class SchemeServiceImpl {

	@Autowired
	private DeviceGroupDAO deviceGroupDao;
	
	@Autowired
	private DeviceGroupMemberDAO deviceGroupMemberDao;
	
	@Autowired
	private DeviceGroupConfigDAO deviceGroupConfigDao;
	
	@Autowired
	private DeviceGroupConfigVideoDAO deviceGroupConfigVideoDao; 
	
	@Autowired
	private DeviceGroupConfigVideoPositionDAO deviceGroupConfigVideoPositionDao;
	
	@Autowired
	private DeviceGroupConfigVideoDstDAO deviceGroupConfigVideoDstDao;
	
	@Autowired
	private RecordDAO recordDao;
	
	@Autowired
	private DictionaryDAO dictionaryDao;
	
	@Autowired
	private QueryUtil queryUtil;
	
	@Autowired
	private VideoServiceImpl videoServiceImpl;
	
	@Autowired
	private AudioServiceImpl audioServiceImpl;
	
	@Autowired
	private RecordServiceImpl recordServiceImpl;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	/**
	 * @Title: 添加方案 
	 * @param groupId 设备组id
	 * @param name 方案名称
	 * @param remark 方案描述
	 * @throws Exception 
	 * @return DeviceGroupConfigPO 方案
	 */
	public DeviceGroupConfigPO save(
			Long groupId,
			String name,
			String remark) throws Exception{
		
		//会议方案重名校验
		DeviceGroupConfigPO existScheme = deviceGroupConfigDao.findByNameAndTypeAndGroupId(name, ConfigType.SCHEME, groupId);
		if(existScheme!=null){
			String groupName = deviceGroupDao.findOne(groupId).getName();
			throw new SchemeNameAlreadyExistedException(groupId, groupName, name);
		}
		
		DeviceGroupConfigPO scheme = new DeviceGroupConfigPO();
		//添加方案
		scheme.setName(name);
		scheme.setRemark(remark);
		scheme.setType(ConfigType.SCHEME);
		scheme.setUpdateTime(new Date());
		deviceGroupConfigDao.save(scheme);
		
		//加关联
		DeviceGroupPO group = deviceGroupDao.findOne(groupId);
		scheme.setGroup(group);
		if(group.getConfigs()==null) group.setConfigs(new HashSet<DeviceGroupConfigPO>());
		group.getConfigs().add(scheme);
		deviceGroupDao.save(group);
		
		
		return scheme;
	}
	
	/**
	 * @Title: 执行方案<br/> 
	 * @param groupId 设备组id
	 * @param videoId 方案的视频id
	 * @throws Exception
	 * @return DeviceGroupPO
	 */
	public DeviceGroupConfigVideoPO run(Long groupId, Long videoId) throws Exception{
		
		LogicBO logic = new LogicBO();
		
		//获取设备组
		DeviceGroupPO group = deviceGroupDao.findOne(groupId);
		
		if(GroupStatus.STOP.equals(group.getStatus())){
			throw new DeviceGroupHasNotStartedException(group.getId(), group.getName());
		}
		
		//查询会议中的所有拼接屏
		queryUtil.queryCombineJv230s(group);
		
		//参数模板
		DeviceGroupAvtplPO avtpl = group.getAvtpl();
		DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
		CodecParamBO codec = new CodecParamBO().set(avtpl, currentGear);
		
		//获取方案的视频
		DeviceGroupConfigVideoPO video = queryUtil.queryConfigVideo(group, videoId);
		
		logic.merge(videoServiceImpl.setCombineVideo(group, video, false, false, false));
		
		//处理方案音频
		/*Set<DeviceGroupConfigVideoPositionPO> positions = video.getPositions();
		Set<Long> memberIds = new HashSet<Long>();
		if(positions!=null && positions.size()>0){
			for(DeviceGroupConfigVideoPositionPO position:positions){
				Set<DeviceGroupConfigVideoSrcPO> srcs = position.getSrcs();
				if(srcs!=null && srcs.size()>0){
					for(DeviceGroupConfigVideoSrcPO src:srcs){
						if(!queryUtil.isCombineJv230(group, src.getMemberId())) memberIds.add(src.getMemberId());
					}
				}
			}
		}
		logic.merge(audioServiceImpl.setGroupAudio(group, memberIds, false, false, false));*/

		//先找主席和发言人
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
		
		//处理录制联动--这个地方连带音频一起联动
		if(group.isRecord() || group.hasRunningPublishStream()){
			logic.merge(recordServiceImpl.updateRecordWhenVideoRun(group, video, false, false));
		}
		
		//持久化数据
		deviceGroupDao.save(group);
		
		//设置转发--增量发送(屏幕不走这里)
//		List<ChannelForwardPO> forwards = new ArrayList<ChannelForwardPO>();
//		Set<DeviceGroupConfigVideoDstPO> dsts = video.getDsts();
//		for(DeviceGroupConfigVideoDstPO dst:dsts){
//			if(ForwardDstType.CHANNEL.equals(dst.getType())){
//				ChannelForwardPO forward = queryUtil.queryChannelForward(group, dst.getMemberChannelId());
//				//TODO 如果没有转发则是大屏设备，这个地方得改
//				if(forward != null){
//					forwards.add(forward);
//					//把解码2的联动也加进来
//					DeviceGroupMemberChannelPO dstChannel = queryUtil.queryMemberChannel(group, dst.getMemberChannelId());
//					if(ChannelType.VIDEODECODE1.equals(dstChannel.getType())){
//						DeviceGroupMemberChannelPO dst2Channel = queryUtil.queryMemberChannelByType(group, dstChannel.getMember().getId(), ChannelType.VIDEODECODE2);
//						if(dst2Channel != null){
//							ChannelForwardPO forward2 = queryUtil.queryChannelForward(group, dst2Channel.getId());
//							if(forward2 != null) forwards.add(forward2);
//						}					
//					}
//				}
//			}else if(ForwardDstType.SCREEN.equals(dst.getType())){
//				ChannelForwardPO forward = queryUtil.queryChannelForward(group, dst.getMemberChannelId());
//				if(forward != null){
//					forwards.add(forward);
//				}
//			}else if(ForwardDstType.ROLE.equals(dst.getType())){
//				ChannelForwardPO forward = queryUtil.queryChannelForward(group, dst.getMemberChannelId());
//				if(forward != null){
//					forwards.add(forward);
//				}
//			}else {
//				//不走这里（预留）
//				List<DeviceGroupMemberChannelPO> decodeChannels = queryUtil.queryMemberChannel(group, dst.getRoleId(), dst.getRoleChannelType());
//				if(decodeChannels!=null && decodeChannels.size()>0){
//					for(DeviceGroupMemberChannelPO channel:decodeChannels){
//						ChannelForwardPO forward = queryUtil.queryChannelForward(group, channel.getId());
//						if(forward != null){
//							forwards.add(forward);
//							//把解码2的联动也加进来
//							if(ChannelType.VIDEODECODE1.equals(channel.getType())){
//								DeviceGroupMemberChannelPO dst2Channel = queryUtil.queryMemberChannelByType(group, channel.getMember().getId(), ChannelType.VIDEODECODE2);
//								if(dst2Channel != null){
//									ChannelForwardPO forward2 = queryUtil.queryChannelForward(group, dst2Channel.getId());
//									if(forward2 != null) forwards.add(forward2);
//								}
//							}
//						}
//					}
//				}
//			}
//		}
		//处理转发
		logic.setForward(group.getForwards(), codec);
		
		//过滤协议
		logic = logic.doFilter(group);
		
		//调用逻辑层
		executeBusiness.execute(logic, "执行方案：");
		
		return video;
	}
	
	/**
	 * @Title: 添加默认的视频配置  这个东西现在只加不删<br/>
	 * @Description: 默认配置成2*2四分屏
	 * @param schemeId 方案id
	 * @param name 视频名称
	 * @throws Exception 
	 * @return DeviceGroupConfigVideoPO 方案视频
	 */
	public DeviceGroupConfigVideoPO addDefaultVideo(Long schemeId, String name) throws Exception{
		//默认的前端渲染
		Map<String, Object> websiteDraw = new HashMapWrapper<String, Object>().put("basic", new HashMapWrapper<String, Object>().put("column", 2)
																																.put("row", 2)
																																.getMap())
																			  .put("cellspan", new ArrayList<Object>())
																			  .getMap();
		
		//视频
		DeviceGroupConfigVideoPO video = new DeviceGroupConfigVideoPO();
		video.setName(name);
		video.setVideoOperation(VideoOperationType.COMBINE);
		video.setWebsiteDraw(JSON.toJSONString(websiteDraw));
		video.setLayout(ScreenLayout.SINGLE);
		video.setUpdateTime(new Date());
		video.setPositions(new HashSet<DeviceGroupConfigVideoPositionPO>());
		
		//默认分屏布局
		DeviceGroupConfigVideoPositionPO position_1 = new DeviceGroupConfigVideoPositionPO();
		position_1.setSerialnum(1);
		position_1.setX("0");
		position_1.setY("0");
		position_1.setW("1/2");
		position_1.setH("1/2");
		position_1.setPictureType(PictureType.STATIC);
		position_1.setUpdateTime(new Date());
		video.getPositions().add(position_1);
		position_1.setVideo(video);
		
		DeviceGroupConfigVideoPositionPO position_2 = new DeviceGroupConfigVideoPositionPO();
		position_2.setSerialnum(2);
		position_2.setX("1");
		position_2.setY("0");
		position_2.setW("1/2");
		position_2.setH("1/2");
		position_2.setPictureType(PictureType.STATIC);
		position_2.setUpdateTime(new Date());
		video.getPositions().add(position_2);
		position_2.setVideo(video);
		
		DeviceGroupConfigVideoPositionPO position_3 = new DeviceGroupConfigVideoPositionPO();
		position_3.setSerialnum(3);
		position_3.setX("0");
		position_3.setY("1");
		position_3.setW("1/2");
		position_3.setH("1/2");
		position_3.setPictureType(PictureType.STATIC);
		position_3.setUpdateTime(new Date());
		video.getPositions().add(position_3);
		position_3.setVideo(video);
		
		DeviceGroupConfigVideoPositionPO position_4 = new DeviceGroupConfigVideoPositionPO();
		position_4.setSerialnum(1);
		position_4.setX("1");
		position_4.setY("1");
		position_4.setW("1/2");
		position_4.setH("1/2");
		position_4.setPictureType(PictureType.STATIC);
		position_4.setUpdateTime(new Date());
		video.getPositions().add(position_4);
		position_4.setVideo(video);
		
		deviceGroupConfigVideoDao.save(video);
		
		//创建关联
		DeviceGroupConfigPO scheme = deviceGroupConfigDao.findOne(schemeId);
		if(scheme.getVideos() == null) scheme.setVideos(new HashSet<DeviceGroupConfigVideoPO>());
		scheme.getVideos().add(video);
		video.setConfig(scheme);
		deviceGroupConfigDao.save(scheme);
		
		return video;
	}
	
	public DeviceGroupConfigVideoPO updateVideo(
			Long videoId,
			String websiteDraw,
			String position,
			String dst,
			String roleDst,
			String layout) throws Exception{
		
		DeviceGroupConfigVideoPO video = deviceGroupConfigVideoDao.findOne(videoId);
		
		//清除所有布局
		Set<DeviceGroupConfigVideoPositionPO> oldPositions = video.getPositions();
		if(oldPositions != null){
			for(DeviceGroupConfigVideoPositionPO oldPosition:oldPositions){
				oldPosition.setVideo(null);
			}
			video.getPositions().removeAll(oldPositions);
			deviceGroupConfigVideoPositionDao.deleteInBatch(oldPositions);
		}else{
			video.setPositions(new HashSet<DeviceGroupConfigVideoPositionPO>());
		}
		
		//清除所有转发目的
		Set<DeviceGroupConfigVideoDstPO> oldDsts = video.getDsts();
		if(oldDsts != null){
			for(DeviceGroupConfigVideoDstPO oldDst:oldDsts){
				oldDst.setVideo(null);
			}
			video.getDsts().removeAll(oldDsts);
			deviceGroupConfigVideoDstDao.deleteInBatch(oldDsts);
		}else{
			video.setDsts(new HashSet<DeviceGroupConfigVideoDstPO>());
		}
		
		video.setWebsiteDraw(websiteDraw);
		video.setLayout(ScreenLayout.fromName(layout));
		
		//解析新的布局以及源
		JSONArray positionArr = JSON.parseArray(position);
		for(int i=0; i<positionArr.size(); i++){
			JSONObject positionObj = positionArr.getJSONObject(i);
			DeviceGroupConfigVideoPositionPO newPosition = new DeviceGroupConfigVideoPositionPO();
			
			//设置布局信息
			newPosition.setSerialnum(positionObj.getIntValue("serialNum"));
			newPosition.setX(positionObj.getString("x"));
			newPosition.setY(positionObj.getString("y"));
			newPosition.setW(positionObj.getString("w"));
			newPosition.setH(positionObj.getString("h"));
			
			JSONObject dataObj = positionObj.getJSONObject("data");
			if(dataObj != null){
				
				//设置画面信息
				newPosition.setPictureType(PictureType.valueOf(dataObj.getString("pictureType")));
				if(PictureType.POLLING.equals(newPosition.getPictureType())){
					newPosition.setPollingTime(dataObj.getString("pollingTime"));
					newPosition.setPollingStatus(dataObj.getString("pollingStatus")==null?null:PollingStatus.valueOf(dataObj.getString("pollingStatus")));
				}
				
				JSONArray srcArr = dataObj.getJSONArray("src");
				if(srcArr!=null && srcArr.size()>0){
					//设置源
					newPosition.setSrcs(new ArrayList<DeviceGroupConfigVideoSrcPO>());
					for(int j=0; j<srcArr.size(); j++){
						JSONObject srcObj = srcArr.getJSONObject(j);
						JSONObject paramObj = srcObj.getJSONObject("param"); 
						DeviceGroupConfigVideoSrcPO newSrc = new DeviceGroupConfigVideoSrcPO();
						if(paramObj.containsKey("roleId")){
							newSrc.setType(ForwardSrcType.ROLE);
							newSrc.setRoleId(Long.valueOf(paramObj.getLong("roleId")));
							newSrc.setRoleName(paramObj.getString("roleName"));
							newSrc.setRoleChannelType(ChannelType.valueOf(paramObj.getString("type")));
							newSrc.setMemberChannelName(srcObj.getString("name"));
						}else if(paramObj.containsKey("videoUuid")){
							newSrc.setType(ForwardSrcType.VIRTUAL);
							newSrc.setVirtualUuid(paramObj.getString("videoUuid"));
							newSrc.setVirtualName(paramObj.getString("videoName"));
						}else{
							newSrc.setType(ForwardSrcType.CHANNEL);
							newSrc.setMemberId(paramObj.getLong("memberId"));
							newSrc.setMemberChannelName(srcObj.getString("name"));
							newSrc.setBundleId(paramObj.getString("bundleId"));
							newSrc.setBundleName(paramObj.getString("bundleName"));
							newSrc.setChannelId(paramObj.getString("channelId"));
							newSrc.setChannelName(paramObj.getString("channelName"));
							newSrc.setMemberChannelId(paramObj.getLong("channelMemberId"));
							newSrc.setLayerId(paramObj.getString("nodeUid"));
						}
						
						//加关联
						newSrc.setPosition(newPosition);
						newPosition.getSrcs().add(newSrc);
					}
				}
			}else{
				newPosition.setPictureType(PictureType.STATIC);
				newPosition.setUpdateTime(new Date());
			}
			
			//加关联
			newPosition.setVideo(video);
			video.getPositions().add(newPosition);
		}
		
		//解析角色
		Set<DeviceGroupConfigVideoDstPO> newDsts = video.getDsts();
		JSONArray roleArray = JSON.parseArray(roleDst);
		Set<Long> roleIds = new HashSet<Long>(); 
		Set<Long> memberIds = new HashSet<Long>(); 
		
		if(roleArray != null && roleArray.size()>0){
			for(int j=0;j<roleArray.size();j++){
				JSONObject roleObject = roleArray.getJSONObject(j); 
				roleIds.add(roleObject.getLong("roleId"));
								
				DeviceGroupConfigVideoDstPO dstPO = new DeviceGroupConfigVideoDstPO();
				dstPO.setRoleId(roleObject.getLong("roleId"));
				dstPO.setRoleName(roleObject.getString("roleName"));
				dstPO.setType(ForwardDstType.ROLE);
				if(roleObject.getString("roleChannelType") != null && roleObject.getString("roleChannelType") != ""){
					dstPO.setRoleChannelType(ChannelType.valueOf(roleObject.getString("roleChannelType")));
				}
				if(roleObject.getString("roleScreenId") != null && roleObject.getString("roleScreenId") != ""){
					dstPO.setScreenId(roleObject.getString("roleScreenId"));
				}
				dstPO.setVideo(video);
				newDsts.add(dstPO);
			}
		}
		if(roleIds != null && roleIds.size()>0){
			List<DeviceGroupMemberPO> roleMembers = deviceGroupMemberDao.findByRoleIdIn(roleIds);
			for(DeviceGroupMemberPO roleMemberPO: roleMembers){
				memberIds.add(roleMemberPO.getId());
			}
		}
		
		//解析设备源
		JSONArray dstArr = JSON.parseArray(dst);
		
		if(dstArr != null && dstArr.size() > 0){		
			for(int i=0;i<dstArr.size();i++){
				JSONObject dstObject = dstArr.getJSONObject(i);
				Long memberId = dstObject.getLong("memberId");
				
				if(dstObject.getString("screenId") != null && dstObject.getString("screenId") != ""){
					
					if(memberIds != null && memberIds.size()>0){	
						if(!memberIds.contains(memberId)){						
							DeviceGroupConfigVideoDstPO dstPO = new DeviceGroupConfigVideoDstPO();
							dstPO.setLayerId(dstObject.getString("layerId"));
							dstPO.setBundleId(dstObject.getString("bundleId"));
							dstPO.setBundleName(dstObject.getString("bundleName"));
							dstPO.setBundleType(dstObject.getString("bundleType"));
							dstPO.setScreenId(dstObject.getString("screenId"));
							dstPO.setMemberId(dstObject.getLong("memberId"));
							dstPO.setMemberScreenId(dstObject.getLong("id"));
							dstPO.setMemberScreenName(dstObject.getString("name"));
							dstPO.setLayerId(dstObject.getString("layerId"));
							dstPO.setType(ForwardDstType.SCREEN);
							dstPO.setVideo(video);
							newDsts.add(dstPO);
						}
					}else{
						DeviceGroupConfigVideoDstPO dstPO = new DeviceGroupConfigVideoDstPO();
						dstPO.setLayerId(dstObject.getString("layerId"));
						dstPO.setBundleId(dstObject.getString("bundleId"));
						dstPO.setBundleName(dstObject.getString("bundleName"));
						dstPO.setBundleType(dstObject.getString("bundleType"));
						dstPO.setScreenId(dstObject.getString("screenId"));
						dstPO.setMemberId(dstObject.getLong("memberId"));
						dstPO.setMemberScreenId(dstObject.getLong("id"));
						dstPO.setMemberScreenName(dstObject.getString("name"));
						dstPO.setLayerId(dstObject.getString("layerId"));
						dstPO.setType(ForwardDstType.SCREEN);
						dstPO.setVideo(video);
						newDsts.add(dstPO);
					}
				}else if(dstObject.getString("channelId") != null && dstObject.getString("channelId") != ""){
					if(memberIds != null && memberIds.size()>0){
						if(!memberIds.contains(memberId)){
							DeviceGroupConfigVideoDstPO dstPO = new DeviceGroupConfigVideoDstPO();
							dstPO.setLayerId(dstObject.getString("layerId"));
							dstPO.setBundleId(dstObject.getString("bundleId"));
							dstPO.setBundleName(dstObject.getString("bundleName"));
							dstPO.setBundleType(dstObject.getString("bundleType"));
							dstPO.setChannelId(dstObject.getString("channelId"));
							dstPO.setChannelName(dstObject.getString("channelName"));
							dstPO.setLayerId(dstObject.getString("layerId"));
							dstPO.setMemberChannelId(dstObject.getLong("id"));
							dstPO.setMemberId(dstObject.getLong("memberId"));
							dstPO.setMemberChannelName(dstObject.getString("name"));
							dstPO.setType(ForwardDstType.CHANNEL);
							dstPO.setVideo(video);
							newDsts.add(dstPO);
						}
					}else{
						DeviceGroupConfigVideoDstPO dstPO = new DeviceGroupConfigVideoDstPO();
						dstPO.setLayerId(dstObject.getString("layerId"));
						dstPO.setBundleId(dstObject.getString("bundleId"));
						dstPO.setBundleName(dstObject.getString("bundleName"));
						dstPO.setBundleType(dstObject.getString("bundleType"));
						dstPO.setChannelId(dstObject.getString("channelId"));
						dstPO.setChannelName(dstObject.getString("channelName"));
						dstPO.setLayerId(dstObject.getString("layerId"));
						dstPO.setMemberChannelId(dstObject.getLong("id"));
						dstPO.setMemberId(dstObject.getLong("memberId"));
						dstPO.setType(ForwardDstType.CHANNEL);
						dstPO.setVideo(video);
						newDsts.add(dstPO);
					}
				}							
			}	
		}
		
		deviceGroupConfigVideoDao.save(video);
		
		return video;
	}
	
	/**
	 * @Title: 开始录制一个议程的视频 
	 * @param groupId 设备组id
	 * @param videoId 视频id
	 * @throws Exception
	 * @return 
	 */
	public void startRecord(Long groupId, Long videoId) throws Exception{
		
		DeviceGroupPO group = deviceGroupDao.findOne(groupId);
		
		if(GroupStatus.STOP.equals(group.getStatus())){
			throw new DeviceGroupHasNotStartedException(group.getId(), group.getName());
		}
		
		DeviceGroupAvtplPO avtpl = group.getAvtpl();
		DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
		
		Set<RecordPO> records = group.getRecords();
		for(RecordPO record:records){
			if(record.getType().equals(RecordType.VIDEO) && record.getRecordId().equals(String.valueOf(videoId)) && record.isRun()){
				//当前屏幕已经在录制了
				return;
			}
		}
		
		DeviceGroupConfigVideoPO video = queryUtil.queryConfigVideo(group, videoId);
		Set<DeviceGroupConfigVideoPositionPO> positions = video.getPositions();
		//源配置的不是单屏
		if(positions.size() != 1) return;
		
		DeviceGroupConfigVideoPositionPO position = positions.iterator().next();
		List<DeviceGroupConfigVideoSrcPO> srcs = position.getSrcs();
		//源的数量不是1
		if(srcs==null || srcs.size()!=1) return;
		
		DeviceGroupConfigVideoSrcPO src = srcs.iterator().next();
		
		RecordPO record = new RecordPO();
		record.setRun(true);
		record.setGroupType(group.getType());
		record.setVideoName(group.getName());
		record.setDescription(new StringBufferWrapper().append("会议录制：").append(DateUtil.format(new Date(), DateUtil.dateTimePattern)).toString());
		record.setType(RecordType.VIDEO);
		record.setRecordId(videoId.toString());
		//视频
		record.setVideoType(SourceType.CHANNEL);
		record.setVideoMemberId(src.getMemberId());
		record.setVideoMemberChannelId(src.getMemberChannelId());
		record.setVideoLayerId(queryUtil.queryLayerId(group, src.getMemberChannelId()));
		record.setVideoBundleId(src.getBundleId());
		record.setVideoChannelId(src.getChannelId());
		
		//获取音频编码通道
		DeviceGroupMemberChannelPO audioEncodeChannel = queryUtil.queryEncodeAudioChannel(group, src.getMemberId());
		
		//音频
		record.setAudioType(SourceType.CHANNEL);
		record.setAudioMemberId(audioEncodeChannel.getMember().getId());
		record.setAudioMemberChannelId(audioEncodeChannel.getId());
		record.setAudioLayerId(audioEncodeChannel.getMember().getLayerId());
		record.setAudioBundleId(audioEncodeChannel.getBundleId());
		record.setAudioChannelId(audioEncodeChannel.getChannelId());
		record.setGroup(group);
		group.getRecords().add(record);
		
		//设置录制状态
		video.setRecord(true);
		deviceGroupDao.save(group);
		
		RecordSetBO recordSetBO = new RecordSetBO().setUuid(record.getUuid())
													.setVideoType("1")
													.setVideoName(record.getVideoName())
													.setDescription(record.getDescription())
													.setCodec_param(new CodecParamBO().setAudio_param(new AudioParamBO().setCodec(avtpl.getAudioFormat().getName()))
																					  .setVideo_param(new VideoParamBO().setCodec(avtpl.getVideoFormat().getName())
											    		 								   						        .setResolution(currentGear.getVideoResolution().getName())
											    		 								   						        .setBitrate(currentGear.getVideoBitRate())))
													.setVideo_source(new RecordSourceBO().setType("channel")
																				   .setLayer_id(record.getVideoLayerId())
																				   .setBundle_id(record.getVideoBundleId())
																				   .setChannel_id(record.getVideoChannelId()))
													.setAudio_source(new RecordSourceBO().setType("channel")
																				   .setLayer_id(record.getAudioLayerId())
																				   .setBundle_id(record.getAudioBundleId())
																				   .setChannel_id(record.getAudioChannelId()));
		
		//地区
		String regionId = group.getDicRegionId();
		recordSetBO.setLocationID(regionId);
		
		//栏目
		String programId = group.getDicProgramId();
		recordSetBO.setCategoryID(programId);
//		String liveBoId = group.getDicCategoryLiveId();
//		recordSetBO.setCategoryLiveID(liveBoId);
		//自动选择直播栏目
		if(group.getType().equals(GroupType.MEETING)){
			List<DictionaryPO> dicConference = dictionaryDao.findByContentPrefixAndDicType("会议", DicType.LIVE);
			if(dicConference.size() > 0){
				recordSetBO.setCategoryLiveID(dicConference.get(0).getLiveBoId());
			}else {
				recordSetBO.setCategoryLiveID("");
			}
		}else if(group.getType().equals(GroupType.MONITOR)){
			List<DictionaryPO> dicMonitor = dictionaryDao.findByContentPrefixAndDicType("监控", DicType.LIVE);
			if(dicMonitor.size() > 0){
				recordSetBO.setCategoryLiveID(dicMonitor.get(0).getLiveBoId());
			}else {
				recordSetBO.setCategoryLiveID("");
			}
		}
		
		LogicBO logic = new LogicBO().setUserId(group.getUserId().toString())
									 .setRecordSet(new ArrayListWrapper<RecordSetBO>().add(recordSetBO).getList());
		
		executeBusiness.execute(logic, "开始录制");
	}
	
	/**
	 * @Title: 停止录制一个议程的视频 
	 * @param groupId 设备组id
	 * @param videoId 视频id
	 * @throws Exception
	 * @return 
	 */
	public void stopRecord(Long groupId, Long videoId) throws Exception{
		
		DeviceGroupPO group = deviceGroupDao.findOne(groupId);
		
		if(GroupStatus.STOP.equals(group.getStatus())){
			throw new DeviceGroupHasNotStartedException(group.getId(), group.getName());
		}
		
		Set<RecordPO> records = group.getRecords();
		RecordPO currentRecord = null;
		for(RecordPO record:records){
			if(record.getType().equals(RecordType.VIDEO) && record.getRecordId().equals(String.valueOf(videoId)) && record.isRun()){
				//当前屏幕已经在录制了
				currentRecord = record;
				break;
			}
		}
		
		//这个地方没有录制
		if(currentRecord == null) return;
		
		//修改状态
		currentRecord.setRun(false);
		recordDao.save(currentRecord);
		
		//设置录制状态
		DeviceGroupConfigVideoPO video = queryUtil.queryConfigVideo(group, videoId);
		video.setRecord(false);
		deviceGroupDao.save(group);
		
		LogicBO logic = new LogicBO().setUserId(group.getUserId().toString())
									 .setRecordDel(new ArrayListWrapper<RecordSetBO>().add(new RecordSetBO().setUuid(currentRecord.getUuid())).getList());
		
		executeBusiness.execute(logic, "停止录制");
	}
	
}

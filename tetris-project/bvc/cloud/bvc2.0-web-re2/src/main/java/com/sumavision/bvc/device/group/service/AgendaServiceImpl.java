package com.sumavision.bvc.device.group.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.RecordSetBO;
import com.sumavision.bvc.device.group.dao.ChannelForwardDAO;
import com.sumavision.bvc.device.group.dao.CombineVideoDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigVideoDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigVideoDstDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigVideoPositionDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigVideoSmallSrcDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupMemberDAO;
import com.sumavision.bvc.device.group.enumeration.AudioOperationType;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.enumeration.ConfigType;
import com.sumavision.bvc.device.group.enumeration.ForwardDstType;
import com.sumavision.bvc.device.group.enumeration.ForwardMode;
import com.sumavision.bvc.device.group.enumeration.ForwardSrcType;
import com.sumavision.bvc.device.group.enumeration.GroupStatus;
import com.sumavision.bvc.device.group.enumeration.PictureType;
import com.sumavision.bvc.device.group.enumeration.PollingSourceVisible;
import com.sumavision.bvc.device.group.enumeration.PollingStatus;
import com.sumavision.bvc.device.group.enumeration.ScreenLayout;
import com.sumavision.bvc.device.group.enumeration.VideoOperationType;
import com.sumavision.bvc.device.group.exception.AgendaNameAlreadyExistedException;
import com.sumavision.bvc.device.group.exception.DeviceGroupHasNotStartedException;
import com.sumavision.bvc.device.group.po.ChannelForwardPO;
import com.sumavision.bvc.device.group.po.CombineVideoPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplPO;
import com.sumavision.bvc.device.group.po.DeviceGroupBusinessRolePO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigAudioPO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigPO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoDstPO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoPO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoPositionPO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoSmallSrcPO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoSrcPO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberChannelPO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberPO;
import com.sumavision.bvc.device.group.po.DeviceGroupPO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.system.enumeration.BusinessRoleSpecial;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;

@Transactional(rollbackFor = Exception.class)
@Service
public class AgendaServiceImpl {

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
	private DeviceGroupConfigVideoSmallSrcDAO deviceGroupConfigVideoSmallScrDao;
	
	@Autowired
	private DeviceGroupConfigVideoDstDAO deviceGroupConfigVideoDstDao;
	
	@Autowired
	private CombineVideoDAO combineVideoDao;
	
	@Autowired
	private ChannelForwardDAO channelForwardDAO;
	
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
	 * @Title: 添加议程 
	 * @param groupId 设备组id
	 * @param name 议程名称
	 * @param remark 议程描述
	 * @throws Exception 
	 * @return DeviceGroupConfigPO 议程
	 */
	public DeviceGroupConfigPO save(
			Long groupId,
			String name,
			String remark,
			String audioOperation) throws Exception{
		
		//会议议程重名校验
		DeviceGroupConfigPO existAgenda = deviceGroupConfigDao.findByNameAndTypeAndGroupId(name, ConfigType.AGENDA, groupId);
		if(existAgenda!=null){
			String groupName = deviceGroupDao.findOne(groupId).getName();
			throw new AgendaNameAlreadyExistedException(groupId, groupName, name);
		}
		
		//添加议程
		DeviceGroupConfigPO agenda = new DeviceGroupConfigPO();
		agenda.setName(name);
		agenda.setRemark(remark);
		agenda.setType(ConfigType.AGENDA);
		agenda.setRun(false);
		agenda.setAudioOperation(AudioOperationType.fromName(audioOperation));
		agenda.setUpdateTime(new Date());
		deviceGroupConfigDao.save(agenda);
		
		//加关联
		DeviceGroupPO group = deviceGroupDao.findOne(groupId);
		agenda.setGroup(group);
		if(group.getConfigs()==null) group.setConfigs(new HashSet<DeviceGroupConfigPO>());
		group.getConfigs().add(agenda);
		deviceGroupDao.save(group);
		
		return agenda;
	}
	
	/**
	 * @Title: 添加默认的视频配置  这个东西现在只加不删<br/>
	 * @Description: 默认配置成2*2四分屏
	 * @param agendaId 议程id
	 * @param name 视频名称
	 * @throws Exception 
	 * @return DeviceGroupConfigVideoPO 议程视频
	 */
	public DeviceGroupConfigVideoPO addDefaultVideo(Long agendaId, String name) throws Exception{
		//默认的前端渲染
		JSONObject websiteDraw = new JSONObject(true);
		websiteDraw.put("basic", new HashMapWrapper<String, Integer>().put("column", 2).put("row", 2).getMap());
		websiteDraw.put("cellspan", new ArrayList<Object>());
		
		//视频
		DeviceGroupConfigVideoPO video = new DeviceGroupConfigVideoPO();
		video.setName(name);
		video.setVideoOperation(VideoOperationType.COMBINE);
		video.setWebsiteDraw(websiteDraw.toJSONString());
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
		DeviceGroupConfigPO agenda = deviceGroupConfigDao.findOne(agendaId);
		if(agenda.getVideos() == null) agenda.setVideos(new HashSet<DeviceGroupConfigVideoPO>());
		agenda.getVideos().add(video);
		video.setConfig(agenda);
		deviceGroupConfigDao.save(agenda);
		
		return video;
	}
	
	/**
	 * @Title: 修改一个议程的视频配置<br/> 
	 * @param videoId 议程视频id
	 * @param websiteDraw 前端渲染
	 * @param position 分屏布局以及源
	 * @param dst 转发的设备目的
	 * @param roleDst 转发的角色目的
	 * @throws Exception 
	 * @return DeviceGroupConfigVideoPO 议程视频
	 */
	public DeviceGroupConfigVideoPO updateVideo(
			Long videoId,
			String websiteDraw,
			String position,
			String dst,
			String roleDst,
			String layout,
			String smallScreen) throws Exception{
		
		DeviceGroupConfigVideoPO video = deviceGroupConfigVideoDao.findOne(videoId);
		
		//获取该视频同一配置下的其他视频
		List<DeviceGroupConfigVideoPO> otherVideos = new ArrayList<DeviceGroupConfigVideoPO>();
		DeviceGroupConfigPO config = video.getConfig();
		for(DeviceGroupConfigVideoPO _video: config.getVideos()){
			if(!_video.getId().equals(videoId)){
				otherVideos.add(_video);
			}
		}
		
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
		
		//清除小屏源
		DeviceGroupConfigVideoSmallSrcPO oldSmallSrc = video.getSmall();
		if(oldSmallSrc != null){
			oldSmallSrc.setVideo(null);
			video.setSmall(null);
			deviceGroupConfigVideoSmallScrDao.delete(oldSmallSrc);
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
		
		//解析小屏
		JSONObject small = JSON.parseObject(smallScreen);
		if(small != null && small.size() > 0){
			DeviceGroupConfigVideoSmallSrcPO smallSrc = new DeviceGroupConfigVideoSmallSrcPO();
			JSONObject smallParam = JSON.parseObject(small.getString("param")); 
			
			if(smallParam.containsKey("roleId")){
				smallSrc.setType(ForwardSrcType.ROLE);
				smallSrc.setRoleId(Long.valueOf(smallParam.getLong("roleId")));
				smallSrc.setRoleName(smallParam.getString("roleName"));
				smallSrc.setRoleChannelType(ChannelType.valueOf(smallParam.getString("type")));
				smallSrc.setMemberChannelName(small.getString("name"));
			}else if(smallParam.containsKey("videoUuid")){
				smallSrc.setType(ForwardSrcType.VIRTUAL);
				smallSrc.setVirtualUuid(smallParam.getString("videoUuid"));
				smallSrc.setVirtualName(smallParam.getString("videoName"));
			}else{
				smallSrc.setType(ForwardSrcType.CHANNEL);
				smallSrc.setMemberId(smallParam.getLong("memberId"));
				smallSrc.setMemberChannelName(small.getString("name"));
				smallSrc.setBundleId(smallParam.getString("bundleId"));
				smallSrc.setBundleName(smallParam.getString("bundleName"));
				smallSrc.setChannelId(smallParam.getString("channelId"));
				smallSrc.setChannelName(smallParam.getString("channelName"));
				smallSrc.setMemberChannelId(smallParam.getLong("channelMemberId"));
				smallSrc.setLayerId(smallParam.getString("nodeUid"));
			}
			smallSrc.setUpdateTime(new Date());
			smallSrc.setVideo(video);
			video.setSmall(smallSrc);
		}
		
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
						JSONObject paramObj = JSON.parseObject(srcObj.getString("param"));//srcObj.getJSONObject("param"); 
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
							if(srcObj.getString("visible") != null){
								newSrc.setVisible(PollingSourceVisible.valueOf(srcObj.getString("visible")));
							}
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
		
		//解析新的目的(设备+角色)
		Set<DeviceGroupConfigVideoDstPO> newDsts = video.getDsts();
		
		//解析角色
		JSONArray roleArray = JSON.parseArray(roleDst);
		Set<Long> roleIds = new HashSet<Long>(); 
		Set<Long> memberIds = new HashSet<Long>(); 
		
		List<DeviceGroupConfigVideoDstPO> allRemoveDsts = new ArrayList<DeviceGroupConfigVideoDstPO>();
		if(roleArray != null && roleArray.size()>0){
			for(int j=0;j<roleArray.size();j++){
				JSONObject roleObject = roleArray.getJSONObject(j); 
				roleIds.add(roleObject.getLong("roleId"));
				
				//清除其余video已有的该dst，保证唯一性
				if(otherVideos != null && otherVideos.size() > 0){
					for(DeviceGroupConfigVideoPO _video: otherVideos){
						List<DeviceGroupConfigVideoDstPO> needRemoveDsts = new ArrayList<DeviceGroupConfigVideoDstPO>();
						if(_video.getDsts() != null && _video.getDsts().size() > 0){
							for(DeviceGroupConfigVideoDstPO _dst: _video.getDsts()){
								if(_dst.getRoleId() != null && _dst.getRoleId().equals(roleObject.getLong("roleId")) && _dst.getScreenId().equals(roleObject.getString("roleScreenId"))){
									_dst.setVideo(null);
									needRemoveDsts.add(_dst);
								}
							}
						}
						allRemoveDsts.addAll(needRemoveDsts);
						video.getDsts().removeAll(needRemoveDsts);
					}
				}
				
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
					
					//清除其余video已有的该dst，保证唯一性
					if(otherVideos != null && otherVideos.size() > 0){
						for(DeviceGroupConfigVideoPO _video: otherVideos){
							List<DeviceGroupConfigVideoDstPO> needRemoveDsts = new ArrayList<DeviceGroupConfigVideoDstPO>();
							if(_video.getDsts() != null && _video.getDsts().size() > 0){
								for(DeviceGroupConfigVideoDstPO _dst: _video.getDsts()){
									if(_dst.getMemberId() != null && _dst.getMemberId().equals(memberId) && _dst.getScreenId().equals(dstObject.getString("screenId") )){
										_dst.setVideo(null);
										needRemoveDsts.add(_dst);
									}
								}
							}
							allRemoveDsts.addAll(needRemoveDsts);
							video.getDsts().removeAll(needRemoveDsts);
						}
					}
					
					if(memberIds != null && memberIds.size()>0){	
						if(!memberIds.contains(memberId)){						
							DeviceGroupConfigVideoDstPO dstPO = new DeviceGroupConfigVideoDstPO();
							dstPO.setLayerId(dstObject.getString("layerId"));
							dstPO.setBundleId(dstObject.getString("bundleId"));
							dstPO.setBundleName(dstObject.getString("bundleName"));
							dstPO.setBundleType(dstObject.getString("bundleType"));
							dstPO.setMemberId(dstObject.getLong("memberId"));
							dstPO.setScreenId(dstObject.getString("screenId"));
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
						dstPO.setMemberId(dstObject.getLong("memberId"));
						dstPO.setScreenId(dstObject.getString("screenId"));
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
		
		deviceGroupConfigVideoDstDao.deleteInBatch(allRemoveDsts);
		
		deviceGroupConfigVideoDao.save(video);
		
		return video;
	} 
	
	/**
	 * @Title: 执行议程 
	 * @param groupId 设备组id
	 * @param agendaId 议程id
	 * @throws Exception
	 * @return 
	 */
	public DeviceGroupConfigPO run(Long groupId, Long agendaId) throws Exception{
		
		synchronized (groupId) {

			//获取设备组参数
			DeviceGroupPO group = deviceGroupDao.findOne(groupId);
			
			if(GroupStatus.STOP.equals(group.getStatus())){
				throw new DeviceGroupHasNotStartedException(group.getId(), group.getName());
			}
			
			//查询会议中的所有拼接屏
			queryUtil.queryCombineJv230s(group);
			
			//处理参数模板
			DeviceGroupAvtplPO avtpl = group.getAvtpl();
			DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
			CodecParamBO codec = new CodecParamBO().set(avtpl, currentGear);
			
			//获取议程
			DeviceGroupConfigPO agenda = queryUtil.queryConfig(group, agendaId, true);
			
			group.setVolume(agenda.getVolume());
			
			LogicBO logic = new LogicBO();
			
			//处理视频
			Set<DeviceGroupConfigVideoPO> videos = agenda.getVideos();
			Set<String> exceptVideoUuids = new HashSet<String>();
			Set<Long> exceptChannelIds = new HashSet<Long>();
			//解码1角色
			Set<Long> exceptRoleIds1 = new HashSet<Long>();
			//解码2绝色
			Set<Long> exceptRoleIds2 = new HashSet<Long>();
			
			//虚拟源合屏加入
			DeviceGroupConfigPO virtualConfig = queryUtil.queryVirtualConfig(group);
			Set<DeviceGroupConfigVideoPO> virtualVideos = virtualConfig.getVideos();
			if(virtualVideos != null && virtualVideos.size()>0){
				for(DeviceGroupConfigVideoPO video: virtualVideos){
					exceptVideoUuids.add(video.getUuid());
				}
			}
			if(videos!=null && videos.size()>0){
				for(DeviceGroupConfigVideoPO video:videos){
					//获取所有的合屏uuid
					exceptVideoUuids.add(video.getUuid());
					
					LogicBO scopeLogic = videoServiceImpl.setCombineVideo(group, video, false, false, false);
					logic.merge(scopeLogic);
					
					//获取所有的目的通道
					Set<DeviceGroupConfigVideoDstPO> dsts = video.getDsts();
					if(dsts!=null && dsts.size()>0){
						for(DeviceGroupConfigVideoDstPO dst:dsts){
							if(ForwardDstType.ROLE.equals(dst.getType())){
								DeviceGroupBusinessRolePO role = queryUtil.queryRoleById(group, dst.getRoleId());
								if(role.getSpecial().equals(BusinessRoleSpecial.CHAIRMAN) || role.getSpecial().equals(BusinessRoleSpecial.SPOKESMAN) || group.getForwardMode().equals(ForwardMode.DEVICE)){
									//主席、发言人
									List<DeviceGroupMemberPO> dstMembers = queryUtil.queryMemberByRole(group, dst.getRoleId());
									for(DeviceGroupMemberPO dstMember: dstMembers){
										if(!dstMember.getBundleType().equals("combineJv230")){
											List<DeviceGroupMemberChannelPO> dstDecodeChannels = queryUtil.queryUsefulMemberDecodeChannelByScreenId(group, dstMember.getId(), dst.getScreenId());
											if(dstDecodeChannels != null && dstDecodeChannels.size() > 0){
												DeviceGroupMemberChannelPO dstDecodeChannel = dstDecodeChannels.get(0);
												if(dstDecodeChannel != null) exceptChannelIds.add(dstDecodeChannel.getId());
												if(video.getSmall() != null) {
													DeviceGroupMemberChannelPO smallDstDecodeChannel = dstDecodeChannels.get(1);
													if(smallDstDecodeChannel != null) exceptChannelIds.add(smallDstDecodeChannel.getId());
												}
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
									List<DeviceGroupMemberChannelPO> dstDecodeChannels = queryUtil.queryUsefulMemberDecodeChannelByScreenId(group, dst.getMemberId(), dst.getScreenId());
									exceptChannelIds.add(dst.getMemberChannelId());
									if(video.getSmall() != null) {
										DeviceGroupMemberChannelPO smallDstDecodeChannel = dstDecodeChannels.get(1);
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
			List<CombineVideoPO> needDeleteCombineVideos = queryUtil.queryCombineVideoExceptUuids(group, exceptVideoUuids);
			if(needDeleteCombineVideos!=null && needDeleteCombineVideos.size()>0){
				for(CombineVideoPO scopeCombineVideo:needDeleteCombineVideos){
					scopeCombineVideo.setGroup(null);
				}
				group.getCombineVideos().removeAll(needDeleteCombineVideos);
				combineVideoDao.deleteInBatch(needDeleteCombineVideos);
				//处理删除合屏
				logic.setCombineVideoDel(needDeleteCombineVideos);
			}
			
			//处理需要清除的转发
			List<ChannelForwardPO> needDeleteForwards = queryUtil.queryVideoChannelForwardExceptMemberChannelId(group, exceptChannelIds);
			needDeleteForwards.addAll(queryUtil.queryVideoChannelForwardExceptRoleId(group, exceptRoleIds1, exceptRoleIds2));
			if(needDeleteForwards!=null && needDeleteForwards.size()>0){
				for(ChannelForwardPO scopeForward:needDeleteForwards){
					scopeForward.setGroup(null);
				}
				group.getForwards().removeAll(needDeleteForwards);
				List<ChannelForwardPO> persistentForwards = new ArrayList<ChannelForwardPO>();
				for(ChannelForwardPO scopeForward:needDeleteForwards){
					if(scopeForward.getId() != null){
						persistentForwards.add(scopeForward);
					}
				}
				if(persistentForwards.size() > 0){
					channelForwardDAO.deleteInBatch(persistentForwards);
				}
				//处理删除转发
				logic.deleteForward(needDeleteForwards, codec);
			}
			
			
			//virtual空源
			Set<Long> roleIds = new HashSet<Long>();
			for(ChannelForwardPO channelForward: needDeleteForwards){
				if(channelForward.getForwardDstType().equals(ForwardDstType.ROLE)){
					roleIds.add(channelForward.getRoleId());
				}
			}
			
			for(Long roleId: roleIds){
				DeviceGroupBusinessRolePO role = queryUtil.queryRoleById(group, roleId);
				List<DeviceGroupMemberPO> dstMembers = queryUtil.queryMemberByRole(group, roleId);
				logic.merge(new LogicBO().setRolePassby(role, dstMembers));
			}
			
			//TODO:清除jv230转发
			
			//处理音频
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
			}else{
				if(AudioOperationType.CUSTOM.equals(agenda.getAudioOperation())){
					Set<DeviceGroupConfigAudioPO> audios = agenda.getAudios();
					if(audios!=null && audios.size()>0){
						Set<Long> voicedIds = new HashSet<Long>();
						for(DeviceGroupConfigAudioPO audio:audios){
							voicedIds.add(audio.getMemberId());
						}
						LogicBO scopeLogic = audioServiceImpl.setGroupAudio(group, voicedIds, false, false, false);
						logic.merge(scopeLogic);
					}else{
						LogicBO scopeLogic = audioServiceImpl.setGroupAudio(group, new ArrayList<Long>(), false, false, false);
						logic.merge(scopeLogic);
					}
				}else if(AudioOperationType.MUTE.equals(agenda.getAudioOperation())){
					LogicBO scopeLogic = audioServiceImpl.setGroupAudio(group, new ArrayList<Long>(), false, false, false);
					logic.merge(scopeLogic);
				}
			}		
			
			//处理视频录制联动
			if(group.isRecord() || group.hasRunningPublishStream()){
				if(videos!=null && videos.size()>0){
					for(DeviceGroupConfigVideoPO video:videos){
						logic.merge(recordServiceImpl.updateRecordWhenVideoRun(group, video, false, false));
					}
				}
			}
			
			//处理音频录制联动
			if(group.isRecord() && 
					(AudioOperationType.CUSTOM.equals(agenda.getAudioOperation()) || 
							AudioOperationType.MUTE.equals(agenda.getAudioOperation()))){
				//这个地方在视频联动的时候就处理过音频，所以处理过视频的录制这里不要重复处理
				List<String> except = new ArrayList<String>();
				List<RecordSetBO> records = logic.getRecordUpdate();
				if(records!=null && records.size()>0){
					for(RecordSetBO record:records){
						except.add(record.getUuid());
					}
				}
				logic.merge(recordServiceImpl.updateRecordWhenAudioChange(group, except, false, false));
			}
			
			//处理转发
			logic.setForward(group.getForwards(), codec);
			
			//持久化数据
			deviceGroupDao.save(group);		
			
			//过滤协议
			logic = logic.doFilter(group);
			
			//调用逻辑层
			executeBusiness.execute(logic, "议程执行：转发");
						
			return agenda;
		}
	}
	
}

package com.sumavision.bvc.device.group.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.dao.DeviceGroupDAO;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.enumeration.ForwardDstType;
import com.sumavision.bvc.device.group.enumeration.ForwardMode;
import com.sumavision.bvc.device.group.enumeration.ForwardSourceType;
import com.sumavision.bvc.device.group.enumeration.ForwardSrcType;
import com.sumavision.bvc.device.group.enumeration.GroupStatus;
import com.sumavision.bvc.device.group.enumeration.GroupType;
import com.sumavision.bvc.device.group.enumeration.RecordType;
import com.sumavision.bvc.device.group.enumeration.SourceType;
import com.sumavision.bvc.device.group.exception.DeviceGroupHasNotStartedException;
import com.sumavision.bvc.device.group.po.ChannelForwardPO;
import com.sumavision.bvc.device.group.po.CombineAudioPO;
import com.sumavision.bvc.device.group.po.CombineAudioSrcPO;
import com.sumavision.bvc.device.group.po.CombineVideoPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplPO;
import com.sumavision.bvc.device.group.po.DeviceGroupBusinessRolePO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoDstPO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoPO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoSrcPO;
import com.sumavision.bvc.device.group.po.DeviceGroupPO;
import com.sumavision.bvc.device.group.po.DeviceGroupRecordSchemePO;
import com.sumavision.bvc.device.group.po.PublishStreamPO;
import com.sumavision.bvc.device.group.po.RecordPO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.meeting.logic.record.mims.MimsService;
import com.sumavision.bvc.system.dao.DictionaryDAO;
import com.sumavision.bvc.system.enumeration.BusinessRoleSpecial;
import com.sumavision.bvc.system.enumeration.DicType;
import com.sumavision.bvc.system.po.DictionaryPO;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Transactional(rollbackFor = Exception.class)
@Service
public class RecordServiceImpl {
	
	@Autowired
	private DeviceGroupDAO deviceGroupDao;
	
	@Autowired
	private DictionaryDAO dictionaryDao;
	
	@Autowired
	private QueryUtil queryUtil;
	
	@Autowired
	private AudioServiceImpl audioServiceImpl;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	@Autowired
	private MimsService mimsService;

	public List<RecordPO> runRecordScheme(DeviceGroupPO group, String name, String describe) throws Exception{
		return runRecordScheme(group, name, describe, true, true, null);
	}
	
	/**
	 * @Title: 执行设备组录制方案<br/> 
	 * @param group 设备组
	 * @param doPersistence 是否做数据持久化
	 * @param doProtocal 是否调用逻辑层
	 * @param recordType 在发布直播(rtmp)的时候需要设置为RecordType.PUBLISH，其他时候不需要
	 * @throws Exception  
	 * @return LogicBO 协议数据 
	 */
	public List<RecordPO> runRecordScheme(
			DeviceGroupPO group,
			String name,
			String describe,
			boolean doPersistence,
			boolean doProtocal,
			RecordType recordType) throws Exception{
		
		if(GroupStatus.STOP.equals(group.getStatus())){
			throw new DeviceGroupHasNotStartedException(group.getId(), group.getName());
		}
		
		//协议数据
		LogicBO logic = new LogicBO();
		logic.setUserId(group.getUserId().toString());
		
		Set<DeviceGroupRecordSchemePO> schemes = group.getRecordSchemes();
		if(schemes==null || schemes.size()<=0) return new ArrayList<RecordPO>();
		
		//地区分类
		String regionId = group.getDicRegionId();
		String programId = group.getDicProgramId();
		String liveBoId = group.getDicCategoryLiveId();

		
		//参数模板
		DeviceGroupAvtplPO avtpl = group.getAvtpl();
		DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
		CodecParamBO codec = new CodecParamBO().set(avtpl, currentGear);
		
		//创建音频
		logic.merge(audioServiceImpl.createFullCombineAudio(group, false, false));
		CombineAudioPO fullAudio = queryUtil.queryFullAudio(group);
		
		List<RecordPO> needAddRecords = new ArrayList<RecordPO>();
		
		for(DeviceGroupRecordSchemePO scopeScheme:schemes){
			if(scopeScheme.getRoleId() == null) continue;
			
			Set<ChannelType> types = null;
			if(RecordType.PUBLISH.equals(recordType)){
				List<DeviceGroupBusinessRolePO> audiencePOs = queryUtil.queryRoleBySpecial(group, BusinessRoleSpecial.AUDIENCE);
				if(audiencePOs==null || audiencePOs.size()<=0) continue;
				Set<ChannelType> audienceTypes = queryUtil.queryUnionChannelTypesByRole(group, audiencePOs.get(0).getId());
				if(audienceTypes.contains(ChannelType.VIDEODECODE1)){
					types = new HashSet<ChannelType>();
					types.add(ChannelType.VIDEODECODE1);
				}
			}else{
				types = queryUtil.queryUnionChannelTypesByRole(group, scopeScheme.getRoleId());
			}

			if(types==null || types.size()<=0) continue;
			for(ChannelType type:types){
				
				ChannelForwardPO forward = null;
				if(ForwardMode.DEVICE.equals(group.getForwardMode())){
					//原：通道转发
					forward = queryUtil.queryRoleChannelForward(group, scopeScheme.getRoleId(), type);
				}else{
					//角色转发
					forward = queryUtil.queryVirtualRoleChannelForward(group, scopeScheme.getRoleId(), type);
				}
				
				//创建录制
				RecordPO record = new RecordPO();
				record.setRun(true);
				record.setGroupType(group.getType());
				
				//设置录制名称
				String videoName = new StringBufferWrapper().append(name)
															.append("-")
															.append(scopeScheme.getRoleName())
															.append("-")
															.append(type.getName())
															.toString();
				record.setVideoName(videoName);
				record.setDescription(describe);				
				
				if(RecordType.PUBLISH.equals(recordType)){
					record.setType(RecordType.PUBLISH);
				}else{
					record.setType(RecordType.SCHEME);
				}
				record.setRecordId(new StringBufferWrapper().append(scopeScheme.getId()).append("@@").append(type.toString()).toString());
				
				//处理视频
				if(forward != null){
					if(ForwardSourceType.FORWARVIDEO.equals(forward.getForwardSourceType())){
						//录制视频通道
						record.setVideoType(SourceType.CHANNEL);
						record.setVideoMemberId(forward.getSourceMemberId());
						record.setVideoMemberChannelId(forward.getSourceMemberChannelId());
						record.setVideoLayerId(forward.getSourceLayerId());
						record.setVideoBundleId(forward.getSourceBundleId());
						record.setVideoChannelId(forward.getSourceChannelId());
						record.setCombineVideoUuid(null);
					}else if(ForwardSourceType.COMBINEVIDEO.equals(forward.getForwardSourceType())){
						//录制合屏
						record.setVideoType(SourceType.COMBINEVIDEO);
						record.setCombineVideoUuid(forward.getCombineUuid());
						record.setVideoMemberId(null);
						record.setVideoMemberChannelId(null);
						record.setVideoLayerId(null);
						record.setVideoBundleId(null);
						record.setVideoChannelId(null);
					}
				}
				
				//处理音频
				if(fullAudio != null){
					if(fullAudio.getSrcs().size() == 1){
						//录制音频通道
						CombineAudioSrcPO src = fullAudio.getSrcs().iterator().next();
						record.setAudioType(SourceType.CHANNEL);
						record.setAudioMemberId(src.getMemberId());
						record.setAudioMemberChannelId(src.getMemberChannelId());
						record.setAudioLayerId(src.getLayerId());
						record.setAudioBundleId(src.getBundleId());
						record.setAudioChannelId(src.getChannelId());
						record.setCombineAudioUuid(null);
					}else {
						//录制混音
						record.setAudioType(SourceType.COMBINEAUDIO);
						record.setCombineAudioUuid(fullAudio.getUuid());
						record.setAudioMemberId(null);
						record.setAudioMemberChannelId(null);
						record.setAudioLayerId(null);
						record.setAudioBundleId(null);
						record.setAudioChannelId(null);
					}
				}
				
				//建立关联
				record.setGroup(group);
				if(group.getRecords() == null) group.setRecords(new HashSet<RecordPO>());
				group.getRecords().add(record);
				
				needAddRecords.add(record);
			}
		}
		
		//修改录制状态
		if(RecordType.PUBLISH.equals(recordType)){
			group.setRecord(false);
		}else{
			group.setRecord(true);
		}
		
		//持久化数据
		if(doPersistence) deviceGroupDao.save(group);	
		
		//自动选择直播栏目
		if(group.getType().equals(GroupType.MEETING)){
			List<DictionaryPO> dicConference = dictionaryDao.findByContentPrefixAndDicType("会议_", DicType.LIVE);
			if(dicConference.size() > 0){
				liveBoId = dicConference.get(0).getLiveBoId();
			}else {
				liveBoId = "";
			}
		}
		
		//创建录制协议
		logic.setRecordSet(needAddRecords, codec, regionId, programId, liveBoId);
		
		if(doProtocal){
			executeBusiness.execute(logic, "开始录制方案录制：");
		}
		
		return needAddRecords;
	}
	
	public LogicBO stopRecordScheme(DeviceGroupPO group, String transferToVod) throws Exception{
		return stopRecordScheme(group, transferToVod, true, true);
	}
	
	/**
	 * @Title: 停止设备组录制方案<br/> 
	 * @param group 设备组
	 * @param doPersistence 是否做数据持久化
	 * @param doProtocal 是否调用逻辑层
	 * @throws Exception
	 * @return LogicBO 协议数据
	 */
	public LogicBO stopRecordScheme(
			DeviceGroupPO group,
			String transferToVod,
			boolean doPersistence,
			boolean doProtocal) throws Exception{
		
		if(GroupStatus.STOP.equals(group.getStatus())){
			throw new DeviceGroupHasNotStartedException(group.getId(), group.getName());
		}
		
		//协议数据
		LogicBO logic = new LogicBO();
		logic.setUserId(group.getUserId().toString());
		
		//查询当前设备组中执行的方案录制
		List<RecordPO> records = queryUtil.queryRunSchemeRecord(group);
		
		List<RecordPO> needStopRecords = new ArrayList<RecordPO>();
		List<CombineVideoPO> needDeleteCombineVideo = new ArrayList<CombineVideoPO>();
		List<CombineAudioPO> needDeleteCombineAudio = new ArrayList<CombineAudioPO>();
		List<PublishStreamPO> needDeletePublishStreams = new ArrayList<PublishStreamPO>();
		
		if(records!=null && records.size()>0){
			for(RecordPO record:records){
				record.setRun(false);
				needStopRecords.add(record);
				
				if(SourceType.COMBINEVIDEO.equals(record.getVideoType())){
					String combineVideoUuid = record.getCombineVideoUuid();
					boolean hasForward = queryUtil.hasCombineVideoForward(group, combineVideoUuid);
					if(!hasForward){
						CombineVideoPO combineVideo = queryUtil.queryCombineVideo(group, combineVideoUuid);
						if(combineVideo != null){
							combineVideo.setGroup(null);
							group.getCombineVideos().remove(combineVideo);
							needDeleteCombineVideo.add(combineVideo);
						}
					}
				}
				
				//录制方案中所有录制中的音频是相同的
				if(needDeleteCombineAudio.size()==0 && SourceType.COMBINEAUDIO.equals(record.getAudioType())){
					String combineAudioUuid = record.getCombineAudioUuid();
					boolean hasForward = queryUtil.hasCombineAudioForward(group, combineAudioUuid);
					if(!hasForward){
						CombineAudioPO combineAudio = queryUtil.queryCombineAudio(group, combineAudioUuid);
						if(combineAudio != null){
							combineAudio.setGroup(null);
							group.getCombineAudios().remove(combineAudio);
							needDeleteCombineAudio.add(combineAudio);
						}
					}
				}
				
				//清除直播媒资
				Set<PublishStreamPO> publishStreams = record.getPublishStreams();
				if(publishStreams != null && publishStreams.size() > 0){
					for(PublishStreamPO publish: publishStreams){
						publish.setRecord(null);
						needDeletePublishStreams.add(publish);
					}
					record.getPublishStreams().removeAll(publishStreams);
				}
				
			}
		}
		
		//清除媒资接口
		mimsService.removeMimsResource(needDeletePublishStreams);
		
		//修改录制状态
		group.setRecord(false);
		
		//持久化数据
		if(doPersistence) deviceGroupDao.save(group);
		
		//创建协议
		logic.setRecordDel(needStopRecords, transferToVod)
			 .setCombineVideoDel(needDeleteCombineVideo)
			 .setCombineAudioDel(needDeleteCombineAudio);
		
		if(doProtocal){
			executeBusiness.execute(logic, "结束录制方案录制：");
		}
		
		return logic;
	}
	
	public LogicBO updateRecordWhenVideoRun(DeviceGroupPO group, DeviceGroupConfigVideoPO video) throws Exception{
		return updateRecordWhenVideoRun(group, video, true, true);
	}
	
	/**
	 * @Title: 执行配置视频时更新录制<br/> 
	 * @param group 设备组
	 * @param video 配置视频
	 * @param doPersistence 是否持久化数据
	 * @param doProtocal 是否调用逻辑层
	 * @throws Exception  
	 * @return LogicBO 协议数据
	 */
	public LogicBO updateRecordWhenVideoRun(
			DeviceGroupPO group,
			DeviceGroupConfigVideoPO video,
			boolean doPersistence,
			boolean doProtocal) throws Exception{
		
		//协议数据
		LogicBO logic = new LogicBO();
		logic.setUserId(group.getUserId().toString());
		
		Set<DeviceGroupConfigVideoDstPO> dsts = video.getDsts();
		if(dsts==null || dsts.size()<=0) return logic;
		
		//地区
		String regionId = group.getDicRegionId();
		
		//栏目
		String programId = group.getDicProgramId();
		
		//直播栏目
		String liveBoId = group.getDicCategoryLiveId();
		
		//参数模板
		DeviceGroupAvtplPO avtpl = group.getAvtpl();
		DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
		CodecParamBO codec = new CodecParamBO().set(avtpl, currentGear);
		
		Set<DeviceGroupConfigVideoDstPO> willChanged = new HashSet<DeviceGroupConfigVideoDstPO>();
		for(DeviceGroupConfigVideoDstPO dst:dsts){
			if(ForwardDstType.ROLE.equals(dst.getType()) && 
					queryUtil.isRecordScheme(group, dst.getRoleId())){
				willChanged.add(dst);
			}
		}
		
		if(willChanged.size() > 0){
			
			List<RecordPO> needUpdateRecord = new ArrayList<RecordPO>();
			
			//创建音频
			logic.merge(audioServiceImpl.createFullCombineAudio(group, false, false));
			CombineAudioPO fullAudio = queryUtil.queryFullAudio(group);
			
			for(DeviceGroupConfigVideoDstPO scopeDst:willChanged){
				ChannelType roleChannelType = queryUtil.queryDstRoleChannelType(group, scopeDst);
				if(roleChannelType==null){
					continue;
				}
				RecordPO record = queryUtil.queryRunRecord(group, scopeDst.getRoleId(), roleChannelType);
				if(record==null){
					continue;
				}
				needUpdateRecord.add(record);
				//更新视频
				if(video.hasSrc()){
					CombineVideoPO combineVideo = queryUtil.queryCombineVideo(group, video.getUuid());
					if(combineVideo==null || !combineVideo.isEffective()){
						DeviceGroupConfigVideoSrcPO src = video.getPositions().iterator().next().getSrcs().iterator().next();
						//CombineVideoSrcPO src = combineVideo.getPositions().iterator().next().getSrcs().iterator().next();
						if(src.getType().equals(ForwardSrcType.CHANNEL)){
							//录制视频通道
							record.setVideoType(SourceType.CHANNEL);
							record.setVideoMemberId(src.getMemberId());
							record.setVideoMemberChannelId(src.getMemberChannelId());
							record.setVideoLayerId(src.getLayerId());
							record.setVideoBundleId(src.getBundleId());
							record.setVideoChannelId(src.getChannelId());
							record.setCombineVideoUuid(null);
						}else if(src.getType().equals(ForwardSrcType.ROLE)){
							ChannelForwardPO forward = queryUtil.queryRoleChannelForward(group, scopeDst.getRoleId(), roleChannelType);
							if(forward != null){
								//录制视频通道
								record.setVideoType(SourceType.CHANNEL);
								record.setVideoMemberId(forward.getSourceMemberId());
								record.setVideoMemberChannelId(forward.getSourceMemberChannelId());
								record.setVideoLayerId(forward.getSourceLayerId());
								record.setVideoBundleId(forward.getSourceBundleId());
								record.setVideoChannelId(forward.getSourceChannelId());
								record.setCombineVideoUuid(null);
							}
						}else if(src.getType().equals(ForwardSrcType.VIRTUAL)){
							record.setVideoType(SourceType.COMBINEVIDEO);
							record.setCombineVideoUuid(src.getVirtualUuid());
							record.setVideoMemberId(null);
							record.setVideoMemberChannelId(null);
							record.setVideoLayerId(null);
							record.setVideoBundleId(null);
							record.setVideoChannelId(null);
						}
					}else{
						//录制合屏
						record.setVideoType(SourceType.COMBINEVIDEO);
						record.setCombineVideoUuid(combineVideo.getUuid());
						record.setVideoMemberId(null);
						record.setVideoMemberChannelId(null);
						record.setVideoLayerId(null);
						record.setVideoBundleId(null);
						record.setVideoChannelId(null);
					}
				}else{
					record.setVideoType(null);
					record.setVideoMemberId(null);
					record.setVideoMemberChannelId(null);
					record.setVideoLayerId(null);
					record.setVideoBundleId(null);
					record.setVideoChannelId(null);
					record.setCombineVideoUuid(null);
				}
				
				//更新音频
				if(fullAudio != null){
					if(fullAudio.getSrcs().size() == 1){
						//录制音频通道
						CombineAudioSrcPO src = fullAudio.getSrcs().iterator().next();
						record.setAudioType(SourceType.CHANNEL);
						record.setAudioMemberId(src.getMemberId());
						record.setAudioMemberChannelId(src.getMemberChannelId());
						record.setAudioLayerId(src.getLayerId());
						record.setAudioBundleId(src.getBundleId());
						record.setAudioChannelId(src.getChannelId());
						record.setCombineAudioUuid(null);
					}else {
						//录制混音
						record.setAudioType(SourceType.COMBINEAUDIO);
						record.setCombineAudioUuid(fullAudio.getUuid());
						record.setAudioMemberId(null);
						record.setAudioMemberChannelId(null);
						record.setAudioLayerId(null);
						record.setAudioBundleId(null);
						record.setAudioChannelId(null);
					}
				}else{
					record.setAudioType(null);
					record.setAudioMemberId(null);
					record.setAudioMemberChannelId(null);
					record.setAudioLayerId(null);
					record.setAudioBundleId(null);
					record.setAudioChannelId(null);
					record.setCombineAudioUuid(null);
				}
			}
			
			//持久化数据
			if(doPersistence) deviceGroupDao.save(group);
			
			//自动选择直播栏目
			if(group.getType().equals(GroupType.MEETING)){
				List<DictionaryPO> dicConference = dictionaryDao.findByContentPrefixAndDicType("会议_", DicType.LIVE);
				if(dicConference.size() > 0){
					liveBoId = dicConference.get(0).getLiveBoId();
				}else {
					liveBoId = "";
				}
			}
			
			//处理协议
			if(needUpdateRecord.size() > 0) logic.setRecordUpdate(needUpdateRecord, codec, regionId, programId, liveBoId);
			
			//调用逻辑层
			if(doProtocal){
				executeBusiness.execute(logic, "更新录制音视频：");
			}
			
		}
		
		return logic;
	}
	
	public LogicBO updateRecordWhenAudioChange(DeviceGroupPO group) throws Exception{
		return updateRecordWhenAudioChange(group, null, true, true);
	}
	
	/**
	 * @Title: 当音频变化时更新录制<br/> 
	 * @param group 设备组
	 * @param except 不处理的录制
	 * @param doPersistence 是否做持久化
	 * @param doProtocal 是否调用逻辑层
	 * @throws Exception 
	 * @return LogicBO 协议数据 
	 */
	public LogicBO updateRecordWhenAudioChange(
			DeviceGroupPO group,
			Collection<String> except,
			boolean doPersistence,
			boolean doProtocal) throws Exception{
		
		//协议数据
		LogicBO logic = new LogicBO();
		logic.setUserId(group.getUserId().toString());
		
		//地区
		String regionId = group.getDicRegionId();
		
		//点播二级栏目
		String programId = group.getDicProgramId();
		
		//直播栏目
		String liveBoId = group.getDicCategoryLiveId();
		
		
		//参数模板
		DeviceGroupAvtplPO avtpl = group.getAvtpl();
		DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
		CodecParamBO codec = new CodecParamBO().set(avtpl, currentGear);
		
		//全量混音
		logic.merge(audioServiceImpl.createFullCombineAudio(group, false, false));
		CombineAudioPO fullAudio = queryUtil.queryFullAudio(group);
		
		List<RecordPO> needUpdateRecord = new ArrayList<RecordPO>();
		List<RecordPO> records = queryUtil.queryRunSchemeRecord(group);
		
		if(records!=null && records.size()>0){
			for(RecordPO record:records){
				if(except!=null && except.contains(record.getUuid())) continue;
				if(fullAudio != null){
					if(fullAudio.getSrcs().size() == 1){
						//录制音频通道
						CombineAudioSrcPO src = fullAudio.getSrcs().iterator().next();
						record.setAudioType(SourceType.CHANNEL);
						record.setAudioMemberId(src.getMemberId());
						record.setAudioMemberChannelId(src.getMemberChannelId());
						record.setAudioLayerId(src.getLayerId());
						record.setAudioBundleId(src.getBundleId());
						record.setAudioChannelId(src.getChannelId());
						record.setCombineAudioUuid(null);
					}else {
						//录制混音
						record.setAudioType(SourceType.COMBINEAUDIO);
						record.setCombineAudioUuid(fullAudio.getUuid());
						record.setAudioMemberId(null);
						record.setAudioMemberChannelId(null);
						record.setAudioLayerId(null);
						record.setAudioBundleId(null);
						record.setAudioChannelId(null);
					}
				}else{
					record.setAudioType(null);
					record.setAudioMemberId(null);
					record.setAudioMemberChannelId(null);
					record.setAudioLayerId(null);
					record.setAudioBundleId(null);
					record.setAudioChannelId(null);
					record.setCombineAudioUuid(null);
				}
				needUpdateRecord.add(record);
			}
		}
		
		//持久化数据
		if(doPersistence) deviceGroupDao.save(group);
		
		//自动选择直播栏目
		if(group.getType().equals(GroupType.MEETING)){
			List<DictionaryPO> dicConference = dictionaryDao.findByContentPrefixAndDicType("会议_", DicType.LIVE);
			if(dicConference.size() > 0){
				liveBoId = dicConference.get(0).getLiveBoId();
			}else {
				liveBoId = "";
			}
		}
		
		//处理协议
		if(needUpdateRecord.size() > 0) logic.setRecordUpdate(needUpdateRecord, codec, regionId, programId, liveBoId);
		
		//调用逻辑层
		if(doProtocal){
			executeBusiness.execute(logic, "更新录制音频：");
		}
		
		return logic;
	}
	
}

package com.sumavision.bvc.device.group.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.dao.CombineAudioDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupCombineAudioSrcDao;
import com.sumavision.bvc.device.group.dao.DeviceGroupDAO;
import com.sumavision.bvc.device.group.po.ChannelForwardPO;
import com.sumavision.bvc.device.group.po.CombineAudioPO;
import com.sumavision.bvc.device.group.po.CombineAudioSrcPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplPO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberChannelPO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberPO;
import com.sumavision.bvc.device.group.po.DeviceGroupPO;
import com.sumavision.bvc.device.group.service.bo.CombineAudioBO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.device.jv230.po.CombineJv230PO;
import com.sumavision.bvc.device.jv230.po.Jv230ChannelPO;
import com.sumavision.bvc.device.jv230.service.Jv230LargeScreenImpl;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;

/**
 * @ClassName: 设备组混音逻辑 
 * @author lvdeyang 
 * @date 2018年8月13日 下午5:22:38 
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class AudioServiceImpl {
	
	@Autowired
	private CombineAudioDAO combineAudioDao;
	
	@Autowired
	private DeviceGroupCombineAudioSrcDao combineAudioSrcDao;
	
	@Autowired
	private DeviceGroupDAO deviceGroupDao;
	
	@Autowired
	private QueryUtil queryUtil;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	@Autowired
	private Jv230LargeScreenImpl jv230LargeScreenImpl;
	
	public LogicBO setGroupAudio(DeviceGroupPO group, Collection<Long> totalVoicedIds) throws Exception{
		return setGroupAudio(group, totalVoicedIds, true, true, true);
	}
	
	/**
	 * @Title: 设置设备组音频<br/> 
	 * @Description: 总体思路：1.生成旧混音<br/>
	 * 						2.生成有效新混音<br/>
	 * 						3.找出没有变化的混音
	 * 					    4.应该覆盖的旧混音与新混音映射
	 * 					    5.根据以上结果对比新旧混音数量
	 *                      	--1.找到需要新增的混音--新混音有剩余
	 *                          --2.找到需要清空的混音--旧混音有剩余
	 *                      6.给新混音设置转发
	 *                      	--1.构建通道转发
	 *                      	--2.构建混音转发
	 *                      7.持久化数据
	 *                      8.协议转换   
	 * @param group 设备组
	 * @param totalVoicedIds 开声的成员
	 * @throws Exception 
	 * @return void 
	 */
	public LogicBO setGroupAudio(
			DeviceGroupPO group, 
			Collection<Long> totalVoicedIds, 
			boolean doPersistence,
			boolean doForward, 
			boolean doProtocal) throws Exception{
		
		//协议转换
		LogicBO logic = new LogicBO();
		logic.setUserId(group.getUserId().toString());
		
		//只需要已连接的成员id
		Set<Long> connectTotalVoicedIds = queryUtil.filterConnectIds(group, totalVoicedIds);
		
		DeviceGroupAvtplPO avtpl = group.getAvtpl();
		DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
		List<DeviceGroupMemberPO> members = queryUtil.queryEffectiveMembers(group);
		List<Long> effectiveMemberIds = queryUtil.queryEffectiveMemberIds(group);
		List<Long> totalMemberIds = queryUtil.queryAllMemberIds(group);
		Set<ChannelForwardPO> forwards = group.getForwards();
		Set<CombineAudioPO> audios = group.getCombineAudios();
		
		//生成旧的混音以
		List<CombineAudioBO> oldAudios = CombineAudioBO.generateFromCombineAudios(audios);
		
		//生成新的混音以及复制
		List<CombineAudioBO> newAudios = CombineAudioBO.generateFromMembers(connectTotalVoicedIds, effectiveMemberIds);
		
		//没有变化的新混音与旧混音映射
		Map<CombineAudioBO, CombineAudioBO> noChangeAudios = new HashMap<CombineAudioBO, CombineAudioBO>();
		for(CombineAudioBO newAudio:newAudios){
			for(CombineAudioBO oldAudio:oldAudios){
				if(newAudio.equals(oldAudio)){
					//记录uuid
					newAudio.setUuid(oldAudio.getUuid());
					noChangeAudios.put(newAudio, oldAudio);
					break;
				}
			}
		}
		Set<CombineAudioBO> nochangeNewAudios = noChangeAudios.keySet();
		newAudios.removeAll(nochangeNewAudios);
		Collection<CombineAudioBO> noChangeOldAudios = noChangeAudios.values();
		oldAudios.removeAll(noChangeOldAudios);
		
		//应该覆盖的新混音与旧混音映射
		Map<CombineAudioBO, CombineAudioBO> needCoverAudios = new HashMap<CombineAudioBO, CombineAudioBO>();
		
		//先简单做这个覆盖音频的策略
		int end = newAudios.size()>oldAudios.size()?oldAudios.size():newAudios.size();
		for(int i=0; i<end; i++){
			needCoverAudios.put(newAudios.get(i), oldAudios.get(i));
		}
		Set<CombineAudioBO> needCoverNewAudios = needCoverAudios.keySet();
		newAudios.removeAll(needCoverNewAudios);
		Collection<CombineAudioBO> needCoverOldAudios = needCoverAudios.values();
		oldAudios.removeAll(needCoverOldAudios);
		
		//应该增加的新混音--新混音的数量比旧混音的数量多
		List<CombineAudioBO> needAddAudios = new ArrayList<CombineAudioBO>();
		if(newAudios.size() > 0) needAddAudios.addAll(newAudios);
		
		//应该清空的旧混音--新混音的数量比旧混音的数量少
		List<CombineAudioBO> needEmptyAudios = new ArrayList<CombineAudioBO>();
		if(oldAudios.size() > 0) needEmptyAudios.addAll(oldAudios);
		
		//维护数据库混音数据
		
		//需要添加的混音
		if(needAddAudios.size() > 0){
			for(CombineAudioBO addAudio:needAddAudios){
				CombineAudioPO newAudio = new CombineAudioPO();
				//记录uuid
				addAudio.setUuid(newAudio.getUuid());
				newAudio.setSrcs(new HashSet<CombineAudioSrcPO>());
				List<Long> srcs = addAudio.getSrcs();
				for(Long src:srcs){
					DeviceGroupMemberChannelPO encodeAudioChannel = queryUtil.queryEncodeAudioChannel(group, src);
					if(null != encodeAudioChannel){
						CombineAudioSrcPO audioSrc = new CombineAudioSrcPO().set(encodeAudioChannel);
						//建立关联
						audioSrc.setCombineAudio(newAudio);
						newAudio.getSrcs().add(audioSrc);
					}
				}
				//建立关联
				newAudio.setGroup(group);
				if(group.getCombineAudios()==null) group.setCombineAudios(new HashSet<CombineAudioPO>());
				group.getCombineAudios().add(newAudio);
			}
		}
		
		//将要删除的混音源
		Set<CombineAudioSrcPO> willDeleteSrcs = new HashSet<CombineAudioSrcPO>();
		
		//需要修改的混音
		if(needCoverAudios.size() > 0){
			Set<CombineAudioBO> scopeNewAudios = needCoverAudios.keySet();
			for(CombineAudioBO scopeNewAudio:scopeNewAudios){
				CombineAudioBO scopeOldAudio = needCoverAudios.get(scopeNewAudio);
				//记录uuid
				scopeNewAudio.setUuid(scopeOldAudio.getUuid());
				CombineAudioPO scopeEntityAudio = queryUtil.queryCombineAudio(group, scopeOldAudio.getUuid());
				Set<CombineAudioSrcPO> scopeEntityAudioSrcs = scopeEntityAudio.getSrcs();
				//解关联
				for(CombineAudioSrcPO scopeEntityAudioSrc:scopeEntityAudioSrcs){
					scopeEntityAudioSrc.setCombineAudio(null);
				}
				scopeEntityAudio.getSrcs().removeAll(scopeEntityAudioSrcs);
				willDeleteSrcs.addAll(scopeEntityAudioSrcs);
				//生成新源
				List<Long> scopeNewVoicedIds = scopeNewAudio.getSrcs();
				for(Long scopeNewVoicedId:scopeNewVoicedIds){
					DeviceGroupMemberChannelPO encodeAudioChannel = queryUtil.queryEncodeAudioChannel(group, scopeNewVoicedId);
					if(null != encodeAudioChannel){
						CombineAudioSrcPO audioSrc = new CombineAudioSrcPO().set(encodeAudioChannel);
						//建立关联
						audioSrc.setCombineAudio(scopeEntityAudio);
						scopeEntityAudio.getSrcs().add(audioSrc);
					}
				}
			}			
		}
		
		//需要清空混音
		if(needEmptyAudios.size() > 0){
			for(CombineAudioBO scopeEmptyAudio:needEmptyAudios){
				CombineAudioPO scopeEntityAudio = queryUtil.queryCombineAudio(group, scopeEmptyAudio.getUuid());
				Set<CombineAudioSrcPO> scopeEntityAudioSrcs = scopeEntityAudio.getSrcs();
				//解关联
				for(CombineAudioSrcPO scopeEntityAudioSrc:scopeEntityAudioSrcs){
					scopeEntityAudioSrc.setCombineAudio(null);
				}
				scopeEntityAudio.getSrcs().removeAll(scopeEntityAudioSrcs);
				willDeleteSrcs.addAll(scopeEntityAudioSrcs);
				
			}
		}
		
		if(willDeleteSrcs.size() > 0) combineAudioSrcDao.deleteInBatch(willDeleteSrcs);
		
		//需要删除的音频转发
		List<ChannelForwardPO> willDeleteForward = new ArrayList<ChannelForwardPO>();
		
		//维护数据库转发数据
		for(Long memberId:totalMemberIds){
			DeviceGroupMemberChannelPO decodeAudioChannel = queryUtil.queryDecodeAudioChannel(group, memberId);
			if(decodeAudioChannel == null) continue;
			ChannelForwardPO audioForward = queryUtil.queryAudioForward(group, memberId);
			if(group.getForwards() == null) group.setForwards(new HashSet<ChannelForwardPO>());
			boolean completed = false;
			//扫描没有面变化的混音
			if(noChangeAudios.size() > 0){
				Collection<CombineAudioBO> scopeNochangeAudios = noChangeAudios.keySet();
				for(CombineAudioBO scopeNochangeAudio:scopeNochangeAudios){
					if(scopeNochangeAudio.isSuitableForMember(memberId)){
						if("combineJv230".equals(decodeAudioChannel.getBundleType())){
							//TODO 处理拼接屏音频转发
							CombineJv230PO combineJv230 = queryUtil.queryCombineJv230(group, decodeAudioChannel.getMember().getId());
							Jv230ChannelPO jv230Channel = queryUtil.queryJv230ChannelPO(combineJv230);							
							if(scopeNochangeAudio.getSrcs().size() == 1){
								DeviceGroupMemberChannelPO encodeAudioChannel = queryUtil.queryEncodeAudioChannel(group, scopeNochangeAudio.getSrcs().iterator().next());
								logic.merge(jv230LargeScreenImpl.generateSingleAudio(group, encodeAudioChannel, jv230Channel));
							}else{
								//这个地方走混音
								logic.merge(jv230LargeScreenImpl.generateCombineAudio(group, scopeNochangeAudio.getUuid(), jv230Channel));
							}						
						}else{
							//建关联
							if(audioForward == null){
								audioForward = new ChannelForwardPO();
								audioForward.setGroup(group);
								group.getForwards().add(audioForward);
							}
							if(scopeNochangeAudio.getSrcs().size() == 1){
								//这个地方走转发
								DeviceGroupMemberChannelPO encodeAudioChannel = queryUtil.queryEncodeAudioChannel(group, scopeNochangeAudio.getSrcs().iterator().next());
								audioForward.generateAudioForward(encodeAudioChannel, decodeAudioChannel);
							}else{
								//这个地方走混音
								audioForward.generateCombineAudioForward(scopeNochangeAudio.getUuid(), decodeAudioChannel);
							}
						}
						completed = true;
						break;
					}
				}
			}
			
			//扫描新建的混音
			if(!completed && needAddAudios.size() > 0){
				for(CombineAudioBO needAddAudio:needAddAudios){
					if(needAddAudio.isSuitableForMember(memberId)){
						if("combineJv230".equals(decodeAudioChannel.getBundleType())){
							//TODO 处理拼接屏音频转发
							CombineJv230PO combineJv230 = queryUtil.queryCombineJv230(group, decodeAudioChannel.getMember().getId());
							Jv230ChannelPO jv230Channel = queryUtil.queryJv230ChannelPO(combineJv230);							
							if(needAddAudio.getSrcs().size() == 1){
								DeviceGroupMemberChannelPO encodeAudioChannel = queryUtil.queryEncodeAudioChannel(group, needAddAudio.getSrcs().iterator().next());
								logic.merge(jv230LargeScreenImpl.generateSingleAudio(group, encodeAudioChannel, jv230Channel));
							}else{
								//这个地方走混音
								logic.merge(jv230LargeScreenImpl.generateCombineAudio(group, needAddAudio.getUuid(), jv230Channel));
							}	
						}else{
							//建关联
							if(audioForward == null){
								audioForward = new ChannelForwardPO();
								audioForward.setGroup(group);
								group.getForwards().add(audioForward);
							}
							if(needAddAudio.getSrcs().size() == 1){
								//这个地方走转发
								DeviceGroupMemberChannelPO encodeAudioChannel = queryUtil.queryEncodeAudioChannel(group, needAddAudio.getSrcs().iterator().next());
								audioForward.generateAudioForward(encodeAudioChannel, decodeAudioChannel);
							}else {
								//这个地方走混音
								audioForward.generateCombineAudioForward(needAddAudio.getUuid(), decodeAudioChannel);
							}
						}
						completed = true;
						break;
					}
				}
			}
			
			//扫描覆盖的混音
			if(!completed && needCoverAudios.size()>0){
				Collection<CombineAudioBO> scopNeedCoverNewAudios = needCoverAudios.keySet();
				for(CombineAudioBO scopNeedCoverNewAudio:scopNeedCoverNewAudios){
					if(scopNeedCoverNewAudio.isSuitableForMember(memberId)){
						if("combineJv230".equals(decodeAudioChannel.getBundleType())){
							//TODO 处理拼接屏音频转发
							CombineJv230PO combineJv230 = queryUtil.queryCombineJv230(group, decodeAudioChannel.getMember().getId());
							Jv230ChannelPO jv230Channel = queryUtil.queryJv230ChannelPO(combineJv230);							
							if(scopNeedCoverNewAudio.getSrcs().size() == 1){
								DeviceGroupMemberChannelPO encodeAudioChannel = queryUtil.queryEncodeAudioChannel(group, scopNeedCoverNewAudio.getSrcs().iterator().next());
								logic.merge(jv230LargeScreenImpl.generateSingleAudio(group, encodeAudioChannel, jv230Channel));
							}else{
								//这个地方走混音
								logic.merge(jv230LargeScreenImpl.generateCombineAudio(group, scopNeedCoverNewAudio.getUuid(), jv230Channel));
							}	
						}else{
							//建关联
							if(audioForward == null){
								audioForward = new ChannelForwardPO();
								audioForward.setGroup(group);
								group.getForwards().add(audioForward);
							}
							if(scopNeedCoverNewAudio.getSrcs().size() == 1){
								//这个地方走转发
								DeviceGroupMemberChannelPO encodeAudioChannel = queryUtil.queryEncodeAudioChannel(group, scopNeedCoverNewAudio.getSrcs().iterator().next());
								audioForward.generateAudioForward(encodeAudioChannel, decodeAudioChannel);
							}else{
								//这个地方走混音
								audioForward.generateCombineAudioForward(scopNeedCoverNewAudio.getUuid(), decodeAudioChannel);
							}
						}
						completed = true;
						break;
					}
				}
			}
			
			//不听声的需要删除转发
			if(!completed && audioForward!=null){
				//解关联
				audioForward.setGroup(null);
				group.getForwards().remove(audioForward);
				willDeleteForward.add(audioForward);
			}
		}
		
		//更新成员音频状态--这里的成员要全部，保证混音列表统一
		for(DeviceGroupMemberPO member:group.getMembers()){
			if(totalVoicedIds.contains(member.getId())){
				member.setOpenAudio(true);
			}else{
				member.setOpenAudio(false);
			}
		}
		
		//持久化数据
		if(doPersistence) deviceGroupDao.save(group);
		
		//视频参数
		CodecParamBO codec = new CodecParamBO().set(avtpl, currentGear);
		
		//加音量增益
		codecAddVolume(codec, group.getVolume());
		
		//新建混音
		if(needAddAudios!=null && needAddAudios.size()>0){
			List<String> audioAddUuids = new ArrayList<String>();
			for(CombineAudioBO scopeNeedAddAudio:needAddAudios){
				audioAddUuids.add(scopeNeedAddAudio.getUuid());
			}
			List<CombineAudioPO> scopeNeedAddAudioEntities = queryUtil.queryCombineAudio(group, audioAddUuids);
			logic.setCombineAudioSet(scopeNeedAddAudioEntities, codec);
		}
		
		//修改混音
		List<String> audioUpdateUuids = new ArrayList<String>();
		if(needCoverAudios.size() > 0){
			Collection<CombineAudioBO> scopeNeedCoverNewAudios = needCoverAudios.keySet();
			for(CombineAudioBO scopeNeedCoverNewAudio:scopeNeedCoverNewAudios){
				audioUpdateUuids.add(scopeNeedCoverNewAudio.getUuid());
			}
		}
		if(needEmptyAudios.size() > 0){
			for(CombineAudioBO scopeNeedEmptyAudio:needEmptyAudios){
				audioUpdateUuids.add(scopeNeedEmptyAudio.getUuid());
			}
		}
		if(audioUpdateUuids.size() > 0){
			List<CombineAudioPO> scopeNeedUpdateAudioEntities = queryUtil.queryCombineAudio(group, audioUpdateUuids);
			logic.setCombineAudioUpdate(scopeNeedUpdateAudioEntities, codec);
		}
		
		//清除音频转发
		if(willDeleteForward.size() > 0) logic.deleteForward(willDeleteForward, codec);
		
		//建立转发
		if(doForward) logic.setForward(forwards, codec);
		
		//调用逻辑层
		if(doProtocal){
			executeBusiness.execute(logic, "修改混音：");
		}
		
		return logic;
	}
	
	public LogicBO createFullCombineAudio(DeviceGroupPO group) throws Exception{
		return createFullCombineAudio(group, true, true);
	}
	
	/**
	 * @Title: 创建全量混音<br/> 
	 * @param group 设备组
	 * @param doPersistence 是否做数据持久化
	 * @param doForward 是否做转发
	 * @param doProtocal 是否做协议
	 * @throws Exception 
	 * @return LogicBO 协议数据
	 */
	public LogicBO createFullCombineAudio(
			DeviceGroupPO group,
			boolean doPersistence,
			boolean doProtocal) throws Exception{
		
		//协议数据
		LogicBO logic = new LogicBO();
		logic.setUserId(group.getUserId().toString());
		
		//设备组是静音的
		if(queryUtil.isMute(group)) return logic;
		
		CombineAudioPO combineAudio = queryUtil.queryFullAudio(group);
		
		if(combineAudio != null) return logic;
		
		//参数模板
		DeviceGroupAvtplPO avtpl = group.getAvtpl();
		DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
		CodecParamBO codec = new CodecParamBO().set(avtpl, currentGear);
		
		//加音量增益
		codecAddVolume(codec, group.getVolume());
		
		//创建混音
		combineAudio = queryUtil.queryUsefulCombineAudio(group);
		boolean create = false;
		if(combineAudio == null){
			create = true;
			combineAudio = new CombineAudioPO();
			combineAudio.setSrcs(new HashSet<CombineAudioSrcPO>());
		}		
		Set<DeviceGroupMemberPO> members = group.getMembers();
		for(DeviceGroupMemberPO member:members){
			if(member.isOpenAudio()){
				DeviceGroupMemberChannelPO encodeAudioChannel = queryUtil.queryEncodeAudioChannel(group, member.getId());
				CombineAudioSrcPO audioSrc = new CombineAudioSrcPO().set(encodeAudioChannel);
				//建立关联
				audioSrc.setCombineAudio(combineAudio);
				combineAudio.getSrcs().add(audioSrc);
			}
		}
		//建立关联
		combineAudio.setGroup(group);
		if(group.getCombineAudios()==null) group.setCombineAudios(new HashSet<CombineAudioPO>());
		group.getCombineAudios().add(combineAudio);
		
		//持久化数据
		if(doPersistence) deviceGroupDao.save(group);
		
		//处理混音协议
		if(create){
			logic.setCombineAudioSet(new ArrayListWrapper<CombineAudioPO>().add(combineAudio).getList(), codec);
		}else{
			logic.setCombineAudioUpdate(new ArrayListWrapper<CombineAudioPO>().add(combineAudio).getList(), codec);
		}
		
		//调用逻辑层
		if(doProtocal){
			executeBusiness.execute(logic, "创建混音：");
		} 
		
		return logic;
	}
	
	/**
	 * @Title: 单个混音创建重发<br/>
	 * @param groupId 设备组id
	 * @param combineAudioUuid 混音uuid
	 * @throws Exception
	 * @return 
	 */
	public void refreshCombineAduio(Long groupId, String combineAudioUuid) throws Exception{
		
		//协议数据
		LogicBO logic = new LogicBO();
		
		DeviceGroupPO group = deviceGroupDao.findOne(groupId);
		
		logic.setUserId(group.getUserId().toString());
		
		//处理参数模板
		DeviceGroupAvtplPO avtpl = group.getAvtpl();
		DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
		CodecParamBO codec = new CodecParamBO().set(avtpl, currentGear);
		
		//加音量增益
		codecAddVolume(codec, group.getVolume());
		
		List<CombineAudioPO> combineAudioList = new ArrayList<CombineAudioPO>();
		CombineAudioPO caudio = combineAudioDao.findByCombineUuid(combineAudioUuid);
		combineAudioList.add(caudio);
		
		logic.setCombineAudioUpdate(combineAudioList, codec);
		
		//调用逻辑层
		executeBusiness.execute(logic, "混音重发：");
	}
	
	/**
	 * @Title: 音频增益下发<br/>
	 * @param group 设备组
	 * @param combineAudioList 
	 * @throws Exception
	 * @return 
	 */
	public void setAudioVolume(
			DeviceGroupPO group,
			Set<CombineAudioPO> combineAudioList
			) throws Exception{
				
		//协议数据
		LogicBO logic = new LogicBO();
		logic.setUserId(group.getUserId().toString());
		
		//处理参数模板
		DeviceGroupAvtplPO avtpl = group.getAvtpl();
		DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
		CodecParamBO codec = new CodecParamBO().set(avtpl, currentGear);
		codecAddVolume(codec, group.getVolume());
		
		logic.setCombineAudioUpdate(combineAudioList, codec);
		
		//调用逻辑层
		executeBusiness.execute(logic, "音频增益：");		
	}
	
	/**
	 * @Title: 参数加增益<br/>
	 * @param codec 参数
	 * @param volume 音量增益
	 * @return 
	 */
	public void codecAddVolume(CodecParamBO codec, int volume){
		if(codec != null){
			codec.getAudio_param().setGain(volume);
		}
	}

}

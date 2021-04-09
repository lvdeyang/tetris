package com.sumavision.bvc.device.group.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.PositionSrcBO;
import com.sumavision.bvc.device.group.dao.ChannelForwardDAO;
import com.sumavision.bvc.device.group.dao.CombineVideoDAO;
import com.sumavision.bvc.device.group.dao.CombineVideoSrcDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupDAO;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.enumeration.CombineVideoSrcType;
import com.sumavision.bvc.device.group.enumeration.ForwardDstType;
import com.sumavision.bvc.device.group.enumeration.ForwardMode;
import com.sumavision.bvc.device.group.enumeration.ForwardSourceType;
import com.sumavision.bvc.device.group.enumeration.ForwardSrcType;
import com.sumavision.bvc.device.group.enumeration.MemberStatus;
import com.sumavision.bvc.device.group.enumeration.ScreenLayout;
import com.sumavision.bvc.device.group.po.ChannelForwardPO;
import com.sumavision.bvc.device.group.po.CombineVideoPO;
import com.sumavision.bvc.device.group.po.CombineVideoPositionPO;
import com.sumavision.bvc.device.group.po.CombineVideoSrcPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplPO;
import com.sumavision.bvc.device.group.po.DeviceGroupBusinessRolePO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoDstPO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoPO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoPositionPO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoSmallSrcPO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoSrcPO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberChannelPO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberPO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberScreenPO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberScreenRectPO;
import com.sumavision.bvc.device.group.po.DeviceGroupPO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.device.jv230.po.CombineJv230PO;
import com.sumavision.bvc.device.jv230.service.Jv230LargeScreenImpl;
import com.sumavision.bvc.system.enumeration.BusinessRoleSpecial;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;


/**
 * @ClassName: 设备组视频逻辑 
 * @author lvdeyang
 * @date 2018年8月13日 下午5:23:30 
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class VideoServiceImpl {

	@Autowired
	private DeviceGroupDAO deviceGroupDao;
	
	@Autowired
	private CombineVideoDAO combineVideoDao;
	
	@Autowired
	private CombineVideoSrcDAO combineVideoSrcDao;
	
	@Autowired
	private ChannelForwardDAO channelForwardDao;
	
	@Autowired
	private Jv230LargeScreenImpl jv230LargeScreenImpl;
	
	@Autowired
	private QueryUtil queryUtil;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	public LogicBO setCombineVideo(DeviceGroupPO group, DeviceGroupConfigVideoPO video) throws Exception{
		return setCombineVideo(group, video, true, true, true);
	}
	
	/**
	 * @Title: 设置视频转发<br/> 
	 * @Description: 1.查看合屏是否已经创建
	 *               	--没有创建
	 *                  	--新建合屏
	 *                  --已经创建
	 *                     	--布局改变
	 *                     		--删除旧合屏
	 *                     		--创建新合屏
	 *                      --布局没变
	 *                      	--更新合屏源
	 *                2.根据视频配置更新转发
	 *                	--目的是通道
	 *                		--存在转发直接覆盖
	 *                		--不存在转发新建转发
	 *                  --目的是角色-通道
	 *                  	1.查找设备组中所有该角色的成员
	 *                      --成员有配置通道
	 *                      	--存在转发直接覆盖
	 *                      	--不存在转发新建转发
	 * @param group 设备组数据
	 * @param video 生效的视频
	 * @param doPersistence 是否做设备组持久化
	 * @param doForward 是否做整体转发
	 * @param doProtocal 是否发协议
	 * @throws Exception 
	 * @return LogicBO 协议数据
	 */
	public LogicBO setCombineVideo(
			DeviceGroupPO group, 
			DeviceGroupConfigVideoPO video, 
			boolean doPersistence, 
			boolean doForward, 
			boolean doProtocal) throws Exception{
		
		//协议数据
		LogicBO logic = new LogicBO();
		logic.setUserId(group.getUserId().toString());
		
		if(!video.hasSrc() && !video.hasSmallSrc()) return removeCombineVideo(group, video.getUuid());
		
		if(group.getCombineVideos() == null) group.setCombineVideos(new HashSet<CombineVideoPO>());
		if(group.getForwards() == null) group.setForwards(new HashSet<ChannelForwardPO>());
		CombineVideoPO combineVideo = queryUtil.queryCombineVideo(group, video.getUuid());
		
		//参数模板
		DeviceGroupAvtplPO avtpl = group.getAvtpl();
		DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
		CodecParamBO codec = new CodecParamBO().set(avtpl, currentGear);
		
		List<CombineVideoPO> needUpdateVideos = new ArrayList<CombineVideoPO>();
		List<CombineVideoPO> needDeleteVideos = new ArrayList<CombineVideoPO>();
		List<CombineVideoSrcPO> willDeleteSrcs = new ArrayList<CombineVideoSrcPO>();
		
		if(combineVideo == null){
			//生成新的合屏
			logic.merge(createCombineVideo(group, video, false, false));
		}else{
			if(video.getWebsiteDraw().equals(combineVideo.getWebsiteDraw())){
				//没有改变布局
				Set<CombineVideoPositionPO> scopePositions = combineVideo.getPositions();
				Set<DeviceGroupConfigVideoPositionPO> scopeConfigPositions = video.getPositions();
				for(CombineVideoPositionPO scopePosition:scopePositions){
					DeviceGroupConfigVideoPositionPO targetConfigPosition = null;
					for(DeviceGroupConfigVideoPositionPO scopeConfigPosition:scopeConfigPositions){
						if(scopePosition.getSerialnum() == scopeConfigPosition.getSerialnum()){
							targetConfigPosition = scopeConfigPosition;
							break;
						}
					}
					scopePosition.setPictureType(targetConfigPosition.getPictureType());
					scopePosition.setPollingTime(targetConfigPosition.getPollingTime());
					scopePosition.setPollingStatus(targetConfigPosition.getPollingStatus());
					
					//解关联
					List<CombineVideoSrcPO> scopeSrcs = scopePosition.getSrcs();
					for(CombineVideoSrcPO scopeSrc:scopeSrcs){
						scopeSrc.setPosition(null);
					}
					scopePosition.getSrcs().removeAll(scopeSrcs);
					willDeleteSrcs.addAll(scopeSrcs);
					
					//创建新源
					List<DeviceGroupConfigVideoSrcPO> scopeConfigSrcs = targetConfigPosition.getSrcs();
					if(scopeConfigSrcs!=null && scopeConfigSrcs.size()>0){
						for(DeviceGroupConfigVideoSrcPO scopeConfigSrc:scopeConfigSrcs){
							if(ForwardSrcType.CHANNEL.equals(scopeConfigSrc.getType())){
								DeviceGroupMemberPO member = queryUtil.queryMemberById(group, scopeConfigSrc.getMemberId());
								//源设备是否接听判断
								if(!member.getMemberStatus().equals(MemberStatus.DISCONNECT)){
									CombineVideoSrcPO scopeSrc = new CombineVideoSrcPO().set(scopeConfigSrc);
									scopeSrc.setPosition(scopePosition);
									scopePosition.getSrcs().add(scopeSrc);
								}							
							}else if(ForwardSrcType.VIRTUAL.equals(scopeConfigSrc.getType())){
								CombineVideoSrcPO scopeSrc = new CombineVideoSrcPO().set(scopeConfigSrc);
								scopeSrc.setPosition(scopePosition);
								scopePosition.getSrcs().add(scopeSrc);
							}else{
								List<DeviceGroupMemberChannelPO> channels = queryUtil.queryMemberChannel(group, scopeConfigSrc.getRoleId(), scopeConfigSrc.getRoleChannelType());
								if(channels!=null && channels.size()>0){
									for(DeviceGroupMemberChannelPO channel:channels){
										//源设备是否接听判断
										if(!channel.getMember().getMemberStatus().equals(MemberStatus.DISCONNECT)){
											CombineVideoSrcPO scopeSrc = new CombineVideoSrcPO().set(channel);
											scopeSrc.setPosition(scopePosition);
											scopePosition.getSrcs().add(scopeSrc);
										}
									}
								}
							}
						}
					}
				}
				
				needUpdateVideos.add(combineVideo);
			}else{
				//改变了布局
				combineVideo.setGroup(null);
				group.getCombineVideos().remove(combineVideo);
				needDeleteVideos.add(combineVideo);
				
				logic.merge(createCombineVideo(group, video, false, false));
			}
		}
		
		//持久化数据
		if(willDeleteSrcs.size() > 0) combineVideoSrcDao.deleteInBatch(willDeleteSrcs);
		
		if(combineVideo == null || combineVideo.getGroup() == null) combineVideo = queryUtil.queryCombineVideo(group, video.getUuid());
		
		//设置转发
		Set<DeviceGroupConfigVideoDstPO> dsts = video.getDsts();
		if(dsts!=null && dsts.size()>0){
			for(DeviceGroupConfigVideoDstPO dst:dsts){
				if(ForwardDstType.CHANNEL.equals(dst.getType())){
					if("combineJv230".equals(dst.getBundleType())){
						//TODO 处理拼接屏转发
						CombineJv230PO combineJv230 = queryUtil.queryCombineJv230(group, dst.getMemberId());
						logic.merge(jv230LargeScreenImpl.comparedCombineVideoAndLargeScreen(group, combineVideo, combineJv230));
					}else{
						DeviceGroupMemberChannelPO dstChannel = queryUtil.queryMemberChannel(group, dst.getMemberChannelId());
						//通道转发
						logic.merge(setVideoForward(group, video, combineVideo, dstChannel, false, false, false));
					}
				}else if(ForwardDstType.SCREEN.equals(dst.getType())){
					if("combineJv230".equals(dst.getBundleType())){
						//TODO 处理拼接屏转发
						CombineJv230PO combineJv230 = queryUtil.queryCombineJv230(group, dst.getMemberId());
						logic.merge(jv230LargeScreenImpl.comparedCombineVideoAndLargeScreen(group, combineVideo, combineJv230));
					}else{
						//屏幕转发处理
						logic.merge(setVideoForwardAddScreen(group, video, combineVideo, dst));						
					}					
				}else if(ForwardDstType.ROLE.equals(dst.getType())){
					//屏幕转发处理
					logic.merge(setVideoForwardAddScreen(group, video, combineVideo, dst));						
				}else{
					//不会走到这先留着
					//处理角色转发
					List<DeviceGroupMemberChannelPO> decodeChannels = queryUtil.queryMemberChannel(group, dst.getRoleId(), dst.getRoleChannelType());
					for(DeviceGroupMemberChannelPO decodeChannel:decodeChannels){
						//通道转发
						logic.merge(setVideoForward(group, video, combineVideo, decodeChannel, false, false, false));
					}
				}
			}
		}
		
		if(doPersistence) deviceGroupDao.save(group);
		
		//处理合屏协议
		logic.setCombineVideoUpdate(needUpdateVideos, codec)
			 .setCombineVideoDel(needDeleteVideos);
		
		//转发协议
		if(doForward) logic.setForward(group.getForwards(), codec);
		
		//调用逻辑层
		if(doProtocal){
			executeBusiness.execute(logic, "合屏转发：");
		} 
		
		return logic;
	}
	
	public LogicBO createCombineVideo(DeviceGroupPO group, DeviceGroupConfigVideoPO video) throws Exception{
		return createCombineVideo(group, video, true, true);
	}
	
	/**
	 * @Title: 为生成的转发加screen信息
	 * @param @param group
	 * @param @param video
	 * @param @param combineVideo
	 * @param @param dst
	 * @return LogicBO
	 * @throws
	 */
	public LogicBO setVideoForwardAddScreen(
			DeviceGroupPO group,
			DeviceGroupConfigVideoPO video,
			CombineVideoPO combineVideo,
			DeviceGroupConfigVideoDstPO dst) throws Exception{
		
		LogicBO logic = new LogicBO();
		
		//TODO:选通道是写死的，转发模式要重构
		if(ForwardDstType.SCREEN.equals(dst.getType())){
			DeviceGroupMemberPO member = queryUtil.queryMemberById(group, dst.getMemberId());
			if(member.getMemberStatus().equals(MemberStatus.CONNECT)){
				List<DeviceGroupMemberChannelPO> dstDecodeChannels = queryUtil.queryUsefulMemberDecodeChannelByScreenId(group, dst.getMemberId(), dst.getScreenId());
				
				if(dstDecodeChannels == null || dstDecodeChannels.size() <= 0) return logic;
				
				DeviceGroupMemberChannelPO dstDecodeChannel = dstDecodeChannels.get(0);
				
				//通道转发
				logic.merge(setVideoForward(group, video, combineVideo, dstDecodeChannel, false, false, false));
				dst.setMemberChannelId(dstDecodeChannel.getId());
				dst.setMemberChannelName(dstDecodeChannel.getName());
				
				//加screen
				ChannelForwardPO forward = queryUtil.queryChannelForward(group, dst.getMemberChannelId());
				DeviceGroupMemberScreenPO screen = queryUtil.queryMemberScreen(group, dst.getMemberScreenId()); 
				
				Set<DeviceGroupMemberScreenRectPO> rects = screen.getRests();
				List<DeviceGroupMemberScreenRectPO> rectList = new ArrayList<DeviceGroupMemberScreenRectPO>();
				rectList.addAll(rects);
				Collections.sort(rectList, new DeviceGroupMemberScreenRectPO.RectComparatorFromPO());
				
				if(forward != null){
					forward.setScreenId(screen.getScreenId());
					forward.setRectId(rectList.get(0).getRectId());
					if(forward.getOverlapChannelId() != null){
						if(rectList.size() > 1){
							DeviceGroupMemberScreenRectPO rectOverlap = rectList.get(1);
							forward.setOverlapRectId(rectOverlap.getRectId());
						}else{
							forward.setOverlapChannelId(null);
							forward.setOverlapX(null);
							forward.setOverlapY(null);
							forward.setOverlapW(null);
							forward.setOverlapH(null);
						}
					}else{
						forward.setOverlapChannelId(null);
						forward.setOverlapX(null);
						forward.setOverlapY(null);
						forward.setOverlapW(null);
						forward.setOverlapH(null);
					}
				}
				
				//小屏转发
				DeviceGroupConfigVideoSmallSrcPO smallSrc = video.getSmall();
				//判断是否有--rect2
				if(smallSrc != null && rectList.size() > 1){
					DeviceGroupMemberChannelPO smallDstDecodeChannel = dstDecodeChannels.get(1);
					logic.merge(setSmallScreenForward(group, video, smallDstDecodeChannel, false, false, false));
					ChannelForwardPO smallForward = queryUtil.queryChannelForward(group, smallDstDecodeChannel.getId());
					smallForward.setScreenId(screen.getScreenId());
					smallForward.setRectId(rectList.get(1).getRectId());
				}
			}			
		}else if(ForwardDstType.ROLE.equals(dst.getType())){
			DeviceGroupBusinessRolePO role = queryUtil.queryRoleById(group, dst.getRoleId());
			List<DeviceGroupMemberPO> dstMembers = queryUtil.queryMemberByRole(group, dst.getRoleId());
			//主席发言人走通道，观众和自定义走roleBundle
			if(role.getSpecial().equals(BusinessRoleSpecial.CHAIRMAN) || role.getSpecial().equals(BusinessRoleSpecial.SPOKESMAN) || group.getForwardMode().equals(ForwardMode.DEVICE)){
				for(DeviceGroupMemberPO dstMember: dstMembers){
					if(dstMember.getMemberStatus().equals(MemberStatus.CONNECT)){
						logic.merge(setVideoForwardAddScreen(group, video, combineVideo, dst.getScreenId(), dstMember, true));
					}
				}
			}else{
				for(DeviceGroupMemberPO dstMember: dstMembers){
					if(dstMember.getMemberStatus().equals(MemberStatus.CONNECT)){
						//TODO: dstMember.getChannels().
						logic.merge(setVideoForwardAddScreen(group, video, combineVideo, dst, role, dstMember, true));
						break;
					}
				}
				
				logic.merge(new LogicBO().setRolePassby(role, dstMembers));
			}
		}
			
		return logic;
	}
	
	/**
	 * @Title: 为生成的转发加screen信息
	 * @param @param group
	 * @param @param video
	 * @param @param combineVideo
	 * @param @param dst
	 * @return LogicBO
	 * @throws
	 */
	public LogicBO setVideoForwardAddScreen(
			DeviceGroupPO group,
			DeviceGroupConfigVideoPO video,
			CombineVideoPO combineVideo,
			String screenId,
			DeviceGroupMemberPO member,
			boolean doDeleteCombineVideo) throws Exception{
		
		LogicBO logic = new LogicBO();
		
		//TODO 处理拼接屏转发
		if(member.getBundleType().equals("combineJv230")){
			
			CombineJv230PO combineJv230 = queryUtil.queryCombineJv230(group, member.getId());
			logic.merge(jv230LargeScreenImpl.comparedCombineVideoAndLargeScreen(group, combineVideo, combineJv230));
		
		}else{
			List<DeviceGroupMemberChannelPO> dstDecodeChannels = queryUtil.queryUsefulMemberDecodeChannelByScreenId(group, member.getId(), screenId);
			DeviceGroupMemberChannelPO dstDecodeChannel = new DeviceGroupMemberChannelPO();
			if(member.getBundleType().equals("combineJv230")){
				dstDecodeChannel = queryUtil.queryDecodeVideoChannel1(group, member.getId());
			}else{
				if(dstDecodeChannels == null || dstDecodeChannels.size() <= 0) return logic;
				
				dstDecodeChannel = dstDecodeChannels.get(0);
			}

			if(dstDecodeChannel == null) return logic;
			
			//通道转发
			logic.merge(setVideoForward(group, video, combineVideo, dstDecodeChannel, false, false, false, doDeleteCombineVideo));

			//加screen
			ChannelForwardPO forward = queryUtil.queryChannelForward(group,dstDecodeChannel.getId());
			DeviceGroupMemberScreenPO screen = queryUtil.queryMemberScreen(member, screenId); 
			
			Set<DeviceGroupMemberScreenRectPO> rects = screen.getRests();
			List<DeviceGroupMemberScreenRectPO> rectList = new ArrayList<DeviceGroupMemberScreenRectPO>();
			rectList.addAll(rects);
			Collections.sort(rectList, new DeviceGroupMemberScreenRectPO.RectComparatorFromPO());
			
			if(forward != null){
				forward.setScreenId(screen.getScreenId());
				forward.setRectId(rectList.get(0).getRectId());
				if(forward.getOverlapChannelId() != null){
					if(rectList.size() > 1){
						DeviceGroupMemberScreenRectPO rectOverlap = rectList.get(1);
						forward.setOverlapRectId(rectOverlap.getRectId());
					}
				}else{
					forward.setOverlapChannelId(null);
					forward.setOverlapX(null);
					forward.setOverlapY(null);
					forward.setOverlapW(null);
					forward.setOverlapH(null);
				}
			}
			
			//小屏转发
			DeviceGroupConfigVideoSmallSrcPO smallSrc = video.getSmall();
			if(smallSrc != null){
				DeviceGroupMemberChannelPO smallDstDecodeChannel = dstDecodeChannels.get(1);
				logic.merge(setSmallScreenForward(group, video, smallDstDecodeChannel, false, false, false));
				ChannelForwardPO smallForward = queryUtil.queryChannelForward(group, smallDstDecodeChannel.getId());
				smallForward.setScreenId(screen.getScreenId());
				smallForward.setRectId(rectList.get(1).getRectId());
			}
		}
		
		
		return logic;
	}
	
	/**
	 * 为生成的转发加screen信息(加入角色bundle)<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月31日 上午8:50:42
	 * @param group
	 * @param video
	 * @param combineVideo
	 * @param dst
	 * @param role
	 * @param member
	 * @param doDeleteCombineVideo
	 * @return
	 * @throws Exception
	 */
	public LogicBO setVideoForwardAddScreen(
			DeviceGroupPO group,
			DeviceGroupConfigVideoPO video,
			CombineVideoPO combineVideo,
			DeviceGroupConfigVideoDstPO dst,
			DeviceGroupBusinessRolePO role,
			DeviceGroupMemberPO member,
			boolean doDeleteCombineVideo) throws Exception{
		
		LogicBO logic = new LogicBO();
		
		List<DeviceGroupMemberChannelPO> dstDecodeChannels = queryUtil.queryUsefulMemberDecodeChannelByScreenId(group, member.getId(), dst.getScreenId());
		DeviceGroupMemberChannelPO dstDecodeChannel = new DeviceGroupMemberChannelPO();
		if(member.getBundleType().equals("cmobinejv230")){
			dstDecodeChannel = queryUtil.queryDecodeVideoChannel1(group, member.getId());
		}else{
			if(dstDecodeChannels == null || dstDecodeChannels.size() <= 0) return logic;
			
			dstDecodeChannel = dstDecodeChannels.get(0);
		}

		if(dstDecodeChannel == null) return logic;
		
		//通道转发
		logic.merge(setVideoForward(group, video, combineVideo, role, dstDecodeChannel, false, false, false, doDeleteCombineVideo));

		//加screen
		ChannelForwardPO forward = queryUtil.queryRoleForward(group, role.getId(), dstDecodeChannel.getType());
		DeviceGroupMemberScreenPO screen = queryUtil.queryMemberScreen(member, dst.getScreenId()); 
		
		Set<DeviceGroupMemberScreenRectPO> rects = screen.getRests();
		List<DeviceGroupMemberScreenRectPO> rectList = new ArrayList<DeviceGroupMemberScreenRectPO>();
		rectList.addAll(rects);
		Collections.sort(rectList, new DeviceGroupMemberScreenRectPO.RectComparatorFromPO());
		
		if(forward != null){
			forward.setScreenId(screen.getScreenId());
			forward.setRectId(rectList.get(0).getRectId());
			if(forward.getOverlapChannelId() != null){
				if(rectList.size() > 1){
					DeviceGroupMemberScreenRectPO rectOverlap = rectList.get(1);
					forward.setOverlapRectId(rectOverlap.getRectId());
				}
			}else{
				forward.setOverlapChannelId(null);
				forward.setOverlapX(null);
				forward.setOverlapY(null);
				forward.setOverlapW(null);
				forward.setOverlapH(null);
			}
		}
		
		//小屏转发
		DeviceGroupConfigVideoSmallSrcPO smallSrc = video.getSmall();
		if(smallSrc != null){
			DeviceGroupMemberChannelPO smallDstDecodeChannel = dstDecodeChannels.get(1);
			logic.merge(setSmallScreenForward(group, video, role, smallDstDecodeChannel, false, false, false));
			ChannelForwardPO smallForward = queryUtil.queryRoleForward(group, role.getId(), smallDstDecodeChannel.getType());
			smallForward.setScreenId(screen.getScreenId());
			smallForward.setRectId(rectList.get(1).getRectId());
		}
		
		return logic;
	}
	
	public LogicBO setVideoForward(
			DeviceGroupPO group,
			DeviceGroupConfigVideoPO video,
			CombineVideoPO combineVideo,
			DeviceGroupMemberChannelPO decodeChannel,
			boolean doPersistence, 
			boolean doIncrementsForward,
			boolean doProtocal) throws Exception{
		
		return setVideoForward(group, video, combineVideo, decodeChannel, doPersistence, doIncrementsForward, doProtocal, true);
	}
	
	/**
	 * 设置视频转发（只做转发不做合屏处理）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月18日 上午9:30:44
	 * @param group 设备组
	 * @param video 视频配置
	 * @param decodeChannel 目的解码通道
	 * @param doPersistence 是否做持久化
	 * @param doIncrementsForward 是否增量forward（防止转发累积）--多层调用设false，一层调用看情况吧
	 * @param doProtocal 是否发协议
	 * @return LogicBO 协议
	 * @throws Exception
	 */
	public LogicBO setVideoForward(
			DeviceGroupPO group,
			DeviceGroupConfigVideoPO video,
			CombineVideoPO combineVideo,
			DeviceGroupMemberChannelPO decodeChannel,
			boolean doPersistence, 
			boolean doIncrementsForward,
			boolean doProtocal,
			boolean doDeleteCombineVideo) throws Exception{
		
		//协议数据
		LogicBO logic = new LogicBO();
		
		//加入转发协议
		CodecParamBO codec = null;
		if(doIncrementsForward){
			//参数模板
			DeviceGroupAvtplPO avtpl = group.getAvtpl();
			DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
			codec = new CodecParamBO().set(avtpl, currentGear);
		} 
		
		//做合屏有效性校验
		Set<String> needCheckCombineVideos = new HashSet<String>();
		List<CombineVideoPO> needDeleteVideos = new ArrayList<CombineVideoPO>();
		
		if("combineJv230".equals(decodeChannel.getBundleType())){
			//TODO 处理拼接屏转发
			CombineJv230PO combineJv230 = queryUtil.queryCombineJv230(group, decodeChannel.getMember().getId());
			logic.merge(jv230LargeScreenImpl.comparedCombineVideoAndLargeScreen(group, combineVideo, combineJv230));
		}else{
			ChannelForwardPO forward = queryUtil.queryChannelForward(group, decodeChannel.getId());
			if(forward == null){
				forward = new ChannelForwardPO();
				forward.setGroup(group);
				group.getForwards().add(forward);
			}else{
				//做合屏使用校验
				if(ForwardSourceType.COMBINEVIDEO.equals(forward.getForwardSourceType())){
					needCheckCombineVideos.add(forward.getCombineUuid());
				}
			}
			forward.setOriginVideoUuid(video.getUuid());
			if(video.getPositions().size()==1 && video.getPositions().iterator().next().getSrcs().size()==1){
				DeviceGroupConfigVideoSrcPO src = video.getPositions().iterator().next().getSrcs().iterator().next();
				if(ForwardSrcType.ROLE.equals(src.getType())){
					List<DeviceGroupMemberChannelPO> channels = queryUtil.queryMemberChannel(group, Long.valueOf(src.getRoleId()), src.getRoleChannelType());
					DeviceGroupMemberChannelPO encodeChannel = null;
					if(channels!=null && channels.size()>0){
						encodeChannel = channels.get(0);
					}
					forward.generateVideoForward(encodeChannel, decodeChannel);
				}else{
					//通道转发
					forward.generateVideoForward(video.getPositions().iterator().next().getSrcs().iterator().next(), decodeChannel);
				}
			}else {
				//合屏转发
				forward.generateCombineVideoForward(combineVideo.getUuid(), decodeChannel)
					   .generateOverlap(combineVideo);
			}
			//设置屏幕布局
			forward.setLayout(video.getLayout());
			//大小屏的时候要查本地的编码通道1
			if(ScreenLayout.REMOTE_LARGE.equals(forward.getLayout()) || 
					ScreenLayout.REMOTE_SMARLL.equals(forward.getLayout())){
				forward.setOverlapChannelId(queryUtil.queryMemberChannelIdByType(group, forward.getMemberId(), ChannelType.VIDEOENCODE1));
			}
			//解码1设置为单画面、远端大，远端小要清除解码2的转发
			if(ChannelType.VIDEODECODE1.equals(decodeChannel.getType()) && !ScreenLayout.PPT_MODE.equals(video.getLayout())){
				DeviceGroupMemberChannelPO decode2Channel = queryUtil.queryMemberChannelByType(group, decodeChannel.getMember().getId(), ChannelType.VIDEODECODE2);
				if(decode2Channel != null){
					ChannelForwardPO decode2Forward = queryUtil.queryChannelForward(group, decode2Channel.getId());
					//修改--解码1时只有有解码2的转发才会清
					if(decode2Forward != null){
						DeviceGroupConfigVideoSrcPO src = null;
						decode2Forward.generateVideoForward(src, decode2Channel);
						decode2Forward.setLayout(null);
						
						//加入转发协议
						if(doIncrementsForward){
							logic.setForward(new ArrayListWrapper<ChannelForwardPO>().add(decode2Forward).getList(), codec);
						} 
					}
				}			
			}
			
			//加入转发协议
			if(doIncrementsForward){
				logic.setForward(new ArrayListWrapper<ChannelForwardPO>().add(forward).getList(), codec);
			} 
		}
		
		//检查合屏是否在看
		if(doDeleteCombineVideo){
			if(needCheckCombineVideos.size() > 0){
				List<CombineVideoPO> checkedCombineVideos = new ArrayList<CombineVideoPO>();
				for(String combineVideoUuid:needCheckCombineVideos){
					if(!queryUtil.hasCombineVideoForward(group, combineVideoUuid)){
						checkedCombineVideos.add(queryUtil.queryCombineVideo(group, combineVideoUuid));
					}
				}
				//删除数据
				if(checkedCombineVideos.size() > 0){
					for(CombineVideoPO scopeCombineVideo:checkedCombineVideos){
						scopeCombineVideo.setGroup(null);
					}
					group.getCombineVideos().removeAll(checkedCombineVideos);
					needDeleteVideos.addAll(checkedCombineVideos);
				}
			}
		}
		
		//设置删除合屏协议
		logic.setCombineVideoDel(needDeleteVideos);
		
		if(doPersistence) deviceGroupDao.save(group);
		
		if(doProtocal) executeBusiness.execute(logic, "设置转发：");
		
		return logic;
	}
	
	/**
	 * 设置角色视频转发（只做转发不做合屏处理）<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月31日 上午9:30:44
	 * @param group 设备组
	 * @param video 视频配置
	 * @param role 目的角色
	 * @param decodeChannel 目的解码通道
	 * @param doPersistence 是否做持久化
	 * @param doIncrementsForward 是否增量forward（防止转发累积）--多层调用设false，一层调用看情况吧
	 * @param doProtocal 是否发协议
	 * @return LogicBO 协议
	 * @throws Exception
	 */
	public LogicBO setVideoForward(
			DeviceGroupPO group,
			DeviceGroupConfigVideoPO video,
			CombineVideoPO combineVideo,
			DeviceGroupBusinessRolePO role,
			DeviceGroupMemberChannelPO decodeChannel,
			boolean doPersistence, 
			boolean doIncrementsForward,
			boolean doProtocal,
			boolean doDeleteCombineVideo) throws Exception{
		
		//协议数据
		LogicBO logic = new LogicBO();
		
		//加入转发协议
		CodecParamBO codec = null;
		if(doIncrementsForward){
			//参数模板
			DeviceGroupAvtplPO avtpl = group.getAvtpl();
			DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
			codec = new CodecParamBO().set(avtpl, currentGear);
		} 
		
		//做合屏有效性校验
		Set<String> needCheckCombineVideos = new HashSet<String>();
		List<CombineVideoPO> needDeleteVideos = new ArrayList<CombineVideoPO>();
		
		if("combineJv230".equals(decodeChannel.getBundleType())){
			//TODO 处理拼接屏转发
			CombineJv230PO combineJv230 = queryUtil.queryCombineJv230(group, decodeChannel.getMember().getId());
			logic.merge(jv230LargeScreenImpl.comparedCombineVideoAndLargeScreen(group, combineVideo, combineJv230));
		}else{
			ChannelForwardPO forward = queryUtil.queryRoleForward(group, role.getId(), decodeChannel.getType());
			if(forward == null){
				forward = new ChannelForwardPO();
				forward.setGroup(group);
				group.getForwards().add(forward);
			}else{
				//做合屏使用校验
				if(ForwardSourceType.COMBINEVIDEO.equals(forward.getForwardSourceType())){
					needCheckCombineVideos.add(forward.getCombineUuid());
				}
			}
			forward.setOriginVideoUuid(video.getUuid());
			if(video.getPositions().size()==1 && video.getPositions().iterator().next().getSrcs().size()==1){
				DeviceGroupConfigVideoSrcPO src = video.getPositions().iterator().next().getSrcs().iterator().next();
				if(ForwardSrcType.ROLE.equals(src.getType())){
					List<DeviceGroupMemberChannelPO> channels = queryUtil.queryMemberChannel(group, Long.valueOf(src.getRoleId()), src.getRoleChannelType());
					DeviceGroupMemberChannelPO encodeChannel = null;
					if(channels!=null && channels.size()>0){
						encodeChannel = channels.get(0);
					}
					forward.generateVideoForward(encodeChannel, role, decodeChannel);
				}else{
					//通道转发
					forward.generateVideoForward(video.getPositions().iterator().next().getSrcs().iterator().next(), role, decodeChannel);
				}
			}else {
				//合屏转发
				forward.generateCombineVideoForward(combineVideo.getUuid(), decodeChannel, role)
					   .generateOverlap(combineVideo);
			}
			//设置屏幕布局
			forward.setLayout(video.getLayout());
			//大小屏的时候要查本地的编码通道1
			if(ScreenLayout.REMOTE_LARGE.equals(forward.getLayout()) || 
					ScreenLayout.REMOTE_SMARLL.equals(forward.getLayout())){
				forward.setOverlapChannelId(queryUtil.queryMemberChannelIdByType(group, decodeChannel.getMember().getId(), ChannelType.VIDEOENCODE1));
			}
			//解码1设置为单画面、远端大，远端小要清除解码2的转发
			if(ChannelType.VIDEODECODE1.equals(decodeChannel.getType()) && !ScreenLayout.PPT_MODE.equals(video.getLayout())){
				DeviceGroupMemberChannelPO decode2Channel = queryUtil.queryMemberChannelByType(group, decodeChannel.getMember().getId(), ChannelType.VIDEODECODE2);
				if(decode2Channel != null){
					ChannelForwardPO decode2Forward = queryUtil.queryRoleForward(group, role.getId(), decode2Channel.getType());
					//修改--解码1时只有有解码2的转发才会清
					if(decode2Forward != null){
						DeviceGroupConfigVideoSrcPO src = null;
						decode2Forward.generateVideoForward(src, role, decode2Channel);
						decode2Forward.setLayout(null);
						
						//加入转发协议
						if(doIncrementsForward){
							logic.setForward(new ArrayListWrapper<ChannelForwardPO>().add(decode2Forward).getList(), codec);
						} 
					}
				}			
			}
			
			//加入转发协议
			if(doIncrementsForward){
				logic.setForward(new ArrayListWrapper<ChannelForwardPO>().add(forward).getList(), codec);
			} 
		}
		
		//检查合屏是否在看
		if(doDeleteCombineVideo){
			if(needCheckCombineVideos.size() > 0){
				List<CombineVideoPO> checkedCombineVideos = new ArrayList<CombineVideoPO>();
				for(String combineVideoUuid:needCheckCombineVideos){
					if(!queryUtil.hasCombineVideoForward(group, combineVideoUuid)){
						checkedCombineVideos.add(queryUtil.queryCombineVideo(group, combineVideoUuid));
					}
				}
				//删除数据
				if(checkedCombineVideos.size() > 0){
					for(CombineVideoPO scopeCombineVideo:checkedCombineVideos){
						scopeCombineVideo.setGroup(null);
					}
					group.getCombineVideos().removeAll(checkedCombineVideos);
					needDeleteVideos.addAll(checkedCombineVideos);
				}
			}
		}
		
		//设置删除合屏协议
		logic.setCombineVideoDel(needDeleteVideos);
		
		if(doPersistence) deviceGroupDao.save(group);
		
		if(doProtocal) executeBusiness.execute(logic, "设置转发：");
		
		return logic;
	}
	
	/**
	 * 生成小屏转发协议<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月14日 上午11:06:46
	 * @param group
	 * @param video
	 * @param decodeChannel
	 * @param doPersistence
	 * @param doProtocal
	 * @param doDeleteCombineVideo
	 * @return
	 * @throws Exception
	 */
	public LogicBO setSmallScreenForward(
			DeviceGroupPO group,
			DeviceGroupConfigVideoPO video,
			DeviceGroupMemberChannelPO decodeChannel,
			boolean doPersistence, 
			boolean doIncrementsForward,
			boolean doProtocal) throws Exception{
		
		//协议数据
		LogicBO logic = new LogicBO();
		
		//参数模板
		DeviceGroupAvtplPO avtpl = group.getAvtpl();
		DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
		CodecParamBO codec = new CodecParamBO().set(avtpl, currentGear);
		
		ChannelForwardPO forward = queryUtil.queryChannelForward(group, decodeChannel.getId());
		if(forward == null){
			forward = new ChannelForwardPO();
			forward.setGroup(group);
			group.getForwards().add(forward);
		}
		
		DeviceGroupConfigVideoSmallSrcPO smallSrc = video.getSmall();
		
		forward.setOriginVideoUuid(video.getUuid());
		
		forward.generateVideoForward(smallSrc, decodeChannel);
		
		forward.setLayout(ScreenLayout.SMALL);
		
		//加入转发协议
		if(doIncrementsForward){
			logic.setForward(new ArrayListWrapper<ChannelForwardPO>().add(forward).getList(), codec);
		} 

		if(doPersistence) deviceGroupDao.save(group);
		
		if(doProtocal) executeBusiness.execute(logic, "设置小屏转发：");
		
		return logic;		
	}
	
	/**
	 * 生成小屏转发协议--角色<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月14日 上午11:06:46
	 * @param group
	 * @param video
	 * @param decodeChannel
	 * @param doPersistence
	 * @param doProtocal
	 * @param doDeleteCombineVideo
	 * @return
	 * @throws Exception
	 */
	public LogicBO setSmallScreenForward(
			DeviceGroupPO group,
			DeviceGroupConfigVideoPO video,
			DeviceGroupBusinessRolePO role,
			DeviceGroupMemberChannelPO decodeChannel,
			boolean doPersistence, 
			boolean doIncrementsForward,
			boolean doProtocal) throws Exception{
		
		//协议数据
		LogicBO logic = new LogicBO();
		
		//参数模板
		DeviceGroupAvtplPO avtpl = group.getAvtpl();
		DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
		CodecParamBO codec = new CodecParamBO().set(avtpl, currentGear);
		
		ChannelForwardPO forward = queryUtil.queryRoleForward(group, role.getId(), decodeChannel.getType());
		if(forward == null){
			forward = new ChannelForwardPO();
			forward.setGroup(group);
			group.getForwards().add(forward);
		}
		
		DeviceGroupConfigVideoSmallSrcPO smallSrc = video.getSmall();
		
		forward.setOriginVideoUuid(video.getUuid());
		
		forward.generateVideoForward(smallSrc, role, decodeChannel);
		
		forward.setLayout(ScreenLayout.SMALL);
		
		//加入转发协议
		if(doIncrementsForward){
			logic.setForward(new ArrayListWrapper<ChannelForwardPO>().add(forward).getList(), codec);
		} 

		if(doPersistence) deviceGroupDao.save(group);
		
		if(doProtocal) executeBusiness.execute(logic, "设置小屏转发：");
		
		return logic;		
	}
	
	/**
	 * @Title: 根据配置视频创建一个合屏<br/> 
	 * @param group 设备组
	 * @param video 配置视频
	 * @throws Exception 
	 * @return CombineVideoPO 合屏数据
	 */
	public LogicBO createCombineVideo(
			DeviceGroupPO group, 
			DeviceGroupConfigVideoPO video,
			boolean doPersistence, 
			boolean doProtocal) throws Exception{
		
		//协议数据
		LogicBO logic = new LogicBO();
		logic.setUserId(group.getUserId().toString());
		
		//合屏没源，删除该合屏
		if(!video.hasSrc()) return removeCombineVideo(group, video.getUuid());
		
		//参数模板
		DeviceGroupAvtplPO avtpl = group.getAvtpl();
		DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
		CodecParamBO codec = new CodecParamBO().set(avtpl, currentGear);
		
		CombineVideoPO combineVideo = queryUtil.queryCombineVideo(group, video.getUuid());
		
		//合屏已经创建直接返回
		if(combineVideo != null) return logic;
		
		//重新生成合屏
		combineVideo = new CombineVideoPO().set(video);
		
		//再次遍历源数据--将配置成角色的源转化成通道
		Set<CombineVideoPositionPO> positions = combineVideo.getPositions();
		for(CombineVideoPositionPO position:positions){
			List<CombineVideoSrcPO> srcs = position.getSrcs();
			if(srcs!=null && srcs.size()>0){
				List<CombineVideoSrcPO> deleteSrcs = new ArrayList<CombineVideoSrcPO>();
				for(CombineVideoSrcPO src:srcs){
					if(ForwardSrcType.ROLE.toString().equals(src.getName())){
						List<DeviceGroupMemberChannelPO> channels = queryUtil.queryMemberChannel(group, Long.valueOf(src.getBundleId()), ChannelType.valueOf(src.getChannelId()));
						if(channels!=null && channels.size()>0){
							for(DeviceGroupMemberChannelPO channel:channels){
								src.set(channel);
							}
						}else{
							//没有配置角色的源要清除
							src.setPosition(null);
							deleteSrcs.add(src);
						}
					}
				}
				if(deleteSrcs.size() > 0) position.getSrcs().removeAll(deleteSrcs);
			}
		}
		
		//再次遍历源数据--将未连接的设备源过滤
		Set<CombineVideoPositionPO> _positions = combineVideo.getPositions();
		for(CombineVideoPositionPO position:_positions){
			List<CombineVideoSrcPO> srcs = position.getSrcs();
			List<CombineVideoSrcPO> deleteSrcs = new ArrayList<CombineVideoSrcPO>();
			if(srcs!=null && srcs.size()>0){
				for(CombineVideoSrcPO src:srcs){
					if(src.getType().equals(CombineVideoSrcType.CHANNEL)){
						DeviceGroupMemberPO member = queryUtil.queryMemberById(group, src.getMemberId());
						if(member.getMemberStatus().equals(MemberStatus.DISCONNECT)){
							src.setPosition(null);
							deleteSrcs.add(src);
						}						
					}
				}
				if(deleteSrcs.size() > 0) position.getSrcs().removeAll(deleteSrcs);
			}
		}
		
		combineVideo.setGroup(group);
		group.getCombineVideos().add(combineVideo);
		
		//持久化数据
		if(doPersistence) deviceGroupDao.save(group);
		
		//处理合屏协议
		logic.setCombineVideoSet(new ArrayListWrapper<CombineVideoPO>().add(combineVideo).getList(), codec);
		
		//调用逻辑层
		if(doProtocal){
			executeBusiness.execute(logic, "创建合屏：");
		} 
		
		return logic;
	}
	
	/**
	 * 修改合屏<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月17日 下午4:24:36
	 * @param group 设备组
	 * @param video 待修改的合屏
	 * @param doPersistence 是否做持久化
	 * @param doProtocal 是否发协议
	 * @return LogicBO 协议
	 * @throws Exception
	 */
	public LogicBO updateCombineVideo(
			DeviceGroupPO group, 
			DeviceGroupConfigVideoPO video, 
			boolean doPersistence, 
			boolean doProtocal) throws Exception{
		
		LogicBO logic = new LogicBO();
		
		Set<CombineVideoPO> combineVideos = group.getCombineVideos();
		if(combineVideos==null || combineVideos.size()<=0) return logic;
		
		CombineVideoPO targetCombineVideo = null;
		for(CombineVideoPO combineVideo:combineVideos){
			if(combineVideo.getUuid().equals(video.getUuid())){
				targetCombineVideo = combineVideo;
				break;
			}
		}
		
		if(targetCombineVideo == null) return logic;
		
		//参数模板
		DeviceGroupAvtplPO avtpl = group.getAvtpl();
		DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
		CodecParamBO codec = new CodecParamBO().set(avtpl, currentGear);
		
		List<CombineVideoSrcPO> willDeleteSrcs = new ArrayList<CombineVideoSrcPO>();
		
		targetCombineVideo.setUpdateTime(new Date());
		Set<CombineVideoPositionPO> scopePositions = targetCombineVideo.getPositions();
		Set<DeviceGroupConfigVideoPositionPO> scopeConfigPositions = video.getPositions();
		for(CombineVideoPositionPO scopePosition:scopePositions){
			DeviceGroupConfigVideoPositionPO targetConfigPosition = null;
			for(DeviceGroupConfigVideoPositionPO scopeConfigPosition:scopeConfigPositions){
				if(scopePosition.getSerialnum() == scopeConfigPosition.getSerialnum()){
					targetConfigPosition = scopeConfigPosition;
					break;
				}
			}
			scopePosition.setPictureType(targetConfigPosition.getPictureType());
			scopePosition.setPollingTime(targetConfigPosition.getPollingTime());
			scopePosition.setPollingStatus(targetConfigPosition.getPollingStatus());
			
			//解关联
			List<CombineVideoSrcPO> scopeSrcs = scopePosition.getSrcs();
			for(CombineVideoSrcPO scopeSrc:scopeSrcs){
				scopeSrc.setPosition(null);
			}
			scopePosition.getSrcs().removeAll(scopeSrcs);
			willDeleteSrcs.addAll(scopeSrcs);
			
			//创建新源
			List<DeviceGroupConfigVideoSrcPO> scopeConfigSrcs = targetConfigPosition.getSrcs();
			if(scopeConfigSrcs!=null && scopeConfigSrcs.size()>0){
				for(DeviceGroupConfigVideoSrcPO scopeConfigSrc:scopeConfigSrcs){
					if(ForwardSrcType.CHANNEL.equals(scopeConfigSrc.getType())){
						DeviceGroupMemberPO member = queryUtil.queryMemberById(group, scopeConfigSrc.getMemberId());
						if(!member.getMemberStatus().equals(MemberStatus.DISCONNECT)){
							CombineVideoSrcPO scopeSrc = new CombineVideoSrcPO().set(scopeConfigSrc);
							scopeSrc.setPosition(scopePosition);
							scopePosition.getSrcs().add(scopeSrc);
						}
					}else if(ForwardSrcType.VIRTUAL.equals(scopeConfigSrc.getType())){
						CombineVideoSrcPO scopeSrc = new CombineVideoSrcPO().set(scopeConfigSrc);
						scopeSrc.setPosition(scopePosition);
						scopePosition.getSrcs().add(scopeSrc);
					}else{
						List<DeviceGroupMemberChannelPO> channels = queryUtil.queryMemberChannel(group, scopeConfigSrc.getRoleId(), scopeConfigSrc.getRoleChannelType());
						if(channels!=null && channels.size()>0){
							for(DeviceGroupMemberChannelPO channel:channels){
								if(!channel.getMember().getMemberStatus().equals(MemberStatus.DISCONNECT)){
									CombineVideoSrcPO scopeSrc = new CombineVideoSrcPO().set(channel);
									scopeSrc.setPosition(scopePosition);
									scopePosition.getSrcs().add(scopeSrc);
								}
							}
						}
					}
				}
			}
		}
		
		if(willDeleteSrcs.size() > 0) combineVideoSrcDao.deleteInBatch(willDeleteSrcs);
		
		//持久化数据
		if(doPersistence) deviceGroupDao.save(group);
		
		//生成协议
		logic.setCombineVideoUpdate(new ArrayListWrapper<CombineVideoPO>().add(targetCombineVideo).getList(), codec);
		
		//调用逻辑层
		if(doProtocal){
			executeBusiness.execute(logic, "修改合屏：");
		} 
		
		return logic;
	}
	
	/**
	 * @Title: 单个合屏创建重发<br/>
	 * @param groupId 设备组id
	 * @param combineVideoUuid 合屏uuid
	 * @throws Exception
	 * @return 
	 */
	public void refreshCombineVideo(Long groupId, String combineVideoUuid) throws Exception{
		//协议数据
		LogicBO logic = new LogicBO();
		
		DeviceGroupPO group = deviceGroupDao.findOne(groupId);
		logic.setUserId(group.getUserId().toString());
		
		//处理参数模板
		DeviceGroupAvtplPO avtpl = group.getAvtpl();
		DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
		CodecParamBO codec = new CodecParamBO().set(avtpl, currentGear);
		
		List<CombineVideoPO> combineVideoList = new ArrayList<CombineVideoPO>();
		CombineVideoPO cvideo = combineVideoDao.findByUuid(combineVideoUuid);
		combineVideoList.add(cvideo);
		
		logic.setCombineVideoUpdate(combineVideoList, codec);
		
		//调用逻辑层
		executeBusiness.execute(logic, "合屏重发：");
	}
	
	/**
	 * @Title: 单个合屏重发，同时添加一个polling_index，用于轮询跳转到指定画面<br/>
	 * @param group 设备组
	 * @param combineVideo 合屏
	 * @param positionUuid 合屏分块的uuid，用来找到这个分块
	 * @param pollingIndex 轮询切换至的索引
	 * @param doProtocol 是否下发协议
	 * @return 
	 */
	public LogicBO refreshCombineVideoWithPollingIndex(
			DeviceGroupPO group,
			CombineVideoPO combineVideo,
			String positionUuid,
			int pollingIndex,
			boolean doProtocol) throws Exception{
		//协议数据
		LogicBO logic = new LogicBO();
		logic.setUserId(group.getUserId().toString());
		
		//处理参数模板
		DeviceGroupAvtplPO avtpl = group.getAvtpl();
		DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
		CodecParamBO codec = new CodecParamBO().set(avtpl, currentGear);
		
		List<CombineVideoPO> combineVideoList = new ArrayList<CombineVideoPO>();
//		CombineVideoPO cvideo = combineVideoDao.findByUuid(combineVideoUuid);
		combineVideoList.add(combineVideo);
		
		logic.setCombineVideoUpdate(combineVideoList, codec);
		List<PositionSrcBO> positions = logic.getCombineVideoUpdate().get(0).getPosition();
		for(PositionSrcBO position : positions){
			if(position.getUuid().equals(positionUuid)){
				position.setPolling_index(pollingIndex);
			}
		}
		
		//调用逻辑层
		if(doProtocol)
			executeBusiness.execute(logic, "设置轮询跳转到指定画面：");
		
		return logic;
	}
	
	/**
	 * @Title: 单个合屏删除<br/>
	 * @param groupId 设备组id
	 * @param combineVideoUuid 合屏uuid
	 * @throws Exception
	 * @return 
	 */
	public LogicBO removeCombineVideo(DeviceGroupPO group, String combineVideoUuid) throws Exception{
		
		//协议数据
		LogicBO logic = new LogicBO();
		
		logic.setUserId(group.getUserId().toString());
		
		//处理参数模板
		DeviceGroupAvtplPO avtpl = group.getAvtpl();
		DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
		CodecParamBO codec = new CodecParamBO().set(avtpl, currentGear);
		
		List<CombineVideoPO> combineVideoList = new ArrayList<CombineVideoPO>();
		CombineVideoPO cvideo = combineVideoDao.findByUuid(combineVideoUuid);
		if(cvideo != null){
			cvideo.setGroup(null);
			group.getCombineVideos().remove(cvideo);
			combineVideoList.add(cvideo);
		}
		
		List<ChannelForwardPO> forwards = channelForwardDao.findByCombineUuid(combineVideoUuid);
		if(forwards != null){
			for(ChannelForwardPO forward: forwards){
				forward.setGroup(null);
			}
			group.getForwards().removeAll(forwards);
		}	
		
		logic.setCombineVideoDel(combineVideoList)
			 .deleteForward(forwards, codec);
		
		return logic;
	}
	
}

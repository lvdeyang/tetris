package com.sumavision.bvc.device.group.service.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;
import com.suma.venus.resource.pojo.EncoderDecoderUserMap;
import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.pojo.FolderUserMap;
import com.sumavision.bvc.device.group.bo.FolderBO;
import com.sumavision.bvc.device.group.dao.DeviceGroupDAO;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.enumeration.ConfigType;
import com.sumavision.bvc.device.group.enumeration.ForwardDstType;
import com.sumavision.bvc.device.group.enumeration.ForwardSourceType;
import com.sumavision.bvc.device.group.enumeration.ForwardSrcType;
import com.sumavision.bvc.device.group.enumeration.MemberStatus;
import com.sumavision.bvc.device.group.enumeration.RecordType;
import com.sumavision.bvc.device.group.po.ChannelForwardPO;
import com.sumavision.bvc.device.group.po.CombineAudioPO;
import com.sumavision.bvc.device.group.po.CombineAudioSrcPO;
import com.sumavision.bvc.device.group.po.CombineVideoPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAuthorizationMemberPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplPO;
import com.sumavision.bvc.device.group.po.DeviceGroupBusinessRolePO;
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
import com.sumavision.bvc.device.group.po.DeviceGroupRecordSchemePO;
import com.sumavision.bvc.device.group.po.RecordPO;
import com.sumavision.bvc.device.group.service.bo.CombineAudioBO;
import com.sumavision.bvc.device.jv230.dao.CombineJv230DAO;
import com.sumavision.bvc.device.jv230.po.CombineJv230PO;
import com.sumavision.bvc.device.jv230.po.Jv230ChannelPO;
import com.sumavision.bvc.device.jv230.po.Jv230PO;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.bvc.system.enumeration.BusinessRoleSpecial;
import com.sumavision.bvc.system.enumeration.GearsLevel;
import com.sumavision.bvc.system.po.ChannelNamePO;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashSetWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;


/**
 * @ClassName: 内存查询工具类 
 * @author lvdeyang
 * @date 2018年8月8日 下午12:48:01 
 */
@Service
public class QueryUtil {

	@Autowired
	private DeviceGroupDAO deviceGroupDao;
	
	@Autowired
	private CombineJv230DAO combineJv230Dao;
	
	/**
	 * @Title: 查找设备组中的档位参数<br/>
	 * @param group:DeviceGroupPO 设备组
	 * @throws Exception 
	 * @return DeviceGroupAvtplGearsPO 档位参数
	 */
	public DeviceGroupAvtplGearsPO queryCurrentGear(DeviceGroupPO group) throws Exception{
		DeviceGroupAvtplPO avtpl = group.getAvtpl();
		Set<DeviceGroupAvtplGearsPO> gears = avtpl.getGears();
		if(group.getCurrentGearLevel() == null){
			group.setCurrentGearLevel(GearsLevel.LEVEL_3);
			deviceGroupDao.save(group);
		}
		for(DeviceGroupAvtplGearsPO gear:gears){
			if(gear.getLevel().equals(group.getCurrentGearLevel())){
				return gear;
			}
		}
		return null;
	}
	
	/**
	 * @Title: 查找一个成员的通道<br/>
	 * @param group 设备组
	 * @param memberChannelId 成员通道id
	 * @throws Exception 
	 * @return DeviceGroupMemberChannelPO 成员通道
	 */
	public DeviceGroupMemberChannelPO queryMemberChannel(DeviceGroupPO group, Long memberChannelId) throws Exception{
		Set<DeviceGroupMemberPO> members = group.getMembers();
		for(DeviceGroupMemberPO member:members){
			Set<DeviceGroupMemberChannelPO> channels = member.getChannels();
			for(DeviceGroupMemberChannelPO channel:channels){
				if(channel.getId().equals(memberChannelId)){
					return channel;
				}
			}
		}
		return null;
	}
	
	/**
	 * @Title: 查找一个成员的屏幕<br/>
	 * @param group 设备组
	 * @param memberScreenId 成员通道id
	 * @throws Exception 
	 * @return DeviceGroupMemberChannelPO 成员通道
	 */
	public DeviceGroupMemberScreenPO queryMemberScreen(DeviceGroupPO group, Long memberScreenId) throws Exception{
		Set<DeviceGroupMemberPO> members = group.getMembers();
		for(DeviceGroupMemberPO member:members){
			Set<DeviceGroupMemberScreenPO> screens = member.getScreens();
			for(DeviceGroupMemberScreenPO screen: screens){
				if(screen.getId().equals(memberScreenId)){
					return screen;
				}
			}
		}
		return null;
	}
	
	/**
	 * @Title: 查找一个成员的通道<br/>
	 * @param member 设备组成员
	 * @param channelId 成员通道id
	 * @throws Exception 
	 * @return DeviceGroupMemberChannelPO 成员屏幕
	 */
	public DeviceGroupMemberChannelPO queryMemberChannel(DeviceGroupMemberPO member, String channelId) throws Exception{
		Set<DeviceGroupMemberChannelPO> channels = member.getChannels();
		for(DeviceGroupMemberChannelPO channel: channels){
			if(channel.getChannelId().equals(channelId)){
				return channel;
			}
		}
		return null;
	}
	
	/**
	 * @Title: 查找一个成员的屏幕<br/>
	 * @param member 设备组成员
	 * @param screenId 成员通道id
	 * @throws Exception 
	 * @return DeviceGroupMemberScreenPO 成员屏幕
	 */
	public DeviceGroupMemberScreenPO queryMemberScreen(DeviceGroupMemberPO member, String screenId) throws Exception{
		Set<DeviceGroupMemberScreenPO> screens = member.getScreens();
		for(DeviceGroupMemberScreenPO screen: screens){
			if(screen.getScreenId().equals(screenId)){
				return screen;
			}
		}
		return null;
	}
	
	/**
	 * @Title: 获取设备组指定成员、指定通道类型的通道id<br/> 
	 * @param group 设备组
	 * @param memberId 设备组成员
	 * @param type 通道类型
	 * @throws Exception 
	 * @return String 通道id
	 */
	public String queryMemberChannelIdByType(DeviceGroupPO group, Long memberId, ChannelType type) throws Exception{
		Set<DeviceGroupMemberPO> members = group.getMembers();
		if(members!=null && members.size()>0){
			for(DeviceGroupMemberPO member:members){
				if(member.getId().equals(memberId)){
					Set<DeviceGroupMemberChannelPO> channels = member.getChannels();
					if(channels!=null && channels.size()>0){
						for(DeviceGroupMemberChannelPO channel:channels){
							if(type.equals(channel.getType())) return channel.getChannelId();
						}
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * @Title: 获取设备组指定成员、指定通道类型的通道<br/> 
	 * @param group 设备组
	 * @param memberId 设备组成员
	 * @param type 通道类型
	 * @throws Exception 
	 * @return DeviceGroupMemberChannelPO 通道
	 */
	public DeviceGroupMemberChannelPO queryMemberChannelByType(DeviceGroupPO group, Long memberId, ChannelType type) throws Exception{
		Set<DeviceGroupMemberPO> members = group.getMembers();
		if(members!=null && members.size()>0){
			for(DeviceGroupMemberPO member:members){
				if(member.getId().equals(memberId)){
					Set<DeviceGroupMemberChannelPO> channels = member.getChannels();
					if(channels!=null && channels.size()>0){
						for(DeviceGroupMemberChannelPO channel:channels){
							if(type.equals(channel.getType())) return channel;
						}
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * @Title: 获取设备组指定成员、指定屏幕的可用解码通道<br/> 
	 * @param group 设备组
	 * @param memberId 设备组成员
	 * @param type 通道类型
	 * @throws Exception 
	 * @return DeviceGroupMemberChannelPO 通道
	 */
	public List<DeviceGroupMemberChannelPO> queryUsefulMemberDecodeChannelByScreenId(DeviceGroupPO group, Long memberId, String screenId) throws Exception{
		Set<DeviceGroupMemberPO> members = group.getMembers();
		List<DeviceGroupMemberChannelPO> decodeChannels = new ArrayList<DeviceGroupMemberChannelPO>(); 
		if(members!=null && members.size()>0){
			for(DeviceGroupMemberPO member:members){
				if(member.getId().equals(memberId)){
					Set<DeviceGroupMemberScreenPO> screens = member.getScreens();
					Set<DeviceGroupMemberChannelPO> channels = member.getChannels();
					for(DeviceGroupMemberScreenPO screen: screens){
						if(screen.getScreenId().equals(screenId)){
							DeviceGroupMemberScreenRectPO rect = screen.getRests().iterator().next();
							List<String> channelIds = Arrays.asList(rect.getChannel().split(","));
							for(String channelId: channelIds){
								for(DeviceGroupMemberChannelPO channel: channels){
									if(channel.getChannelId().equals(channelId)){
										if(channel.getType().isVideoDecode()){
											decodeChannels.add(channel);
											break;
										}
									}
								}
							}
						}
					}
					break;
				}
			}
			
			Collections.sort(decodeChannels, new DeviceGroupMemberChannelPO.ChannelComparatorFromPO());
			
		}
		
		return decodeChannels;
	}
	
	/**
	 * @Title: 获取角色转发目的的ChannelType<br/> 
	 * @param group 设备组
	 * @param DeviceGroupMemberChannelPO 转发目的
	 * @throws Exception 
	 * @return ChannelType 通道
	 */
	public ChannelType queryDstRoleChannelType(DeviceGroupPO group, DeviceGroupConfigVideoDstPO dst) throws Exception{
		Set<DeviceGroupMemberPO> members = group.getMembers();
		if(members!=null && members.size()>0){
			for(DeviceGroupMemberPO member:members){
				if(member.getRoleId() != null && member.getRoleId().equals(dst.getRoleId())){
					DeviceGroupMemberChannelPO decode = queryUsefulMemberDecodeChannelByScreenId(group, member.getId(), dst.getScreenId()).get(0);
					if(decode == null) return null;
					return decode.getType();
				}
			}			
		}
		return null;
	}
	
	/**
	 * @Title: 获取设备组指定成员、指定屏幕id的屏幕<br/> 
	 * @param group 设备组
	 * @param memberId 设备组成员
	 * @param screenId 屏幕id
	 * @throws Exception 
	 * @return DeviceGroupMemberScreenPO 屏幕
	 */
	public DeviceGroupMemberScreenPO queryMemberScreenByScreenId(DeviceGroupPO group, Long memberId, String screenId) throws Exception{
		Set<DeviceGroupMemberPO> members = group.getMembers();
		if(members!=null && members.size()>0){
			for(DeviceGroupMemberPO member:members){
				if(member.getId().equals(memberId)){
					Set<DeviceGroupMemberScreenPO> screens = member.getScreens();
					if(screens!=null && screens.size()>0){
						for(DeviceGroupMemberScreenPO screen:screens){
							if(screenId.equals(screen.getScreenId())) return screen;
						}
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 根据唯一的角色获取成员设备名称<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月17日 下午3:35:19
	 * @param group 设备组
	 * @param roleId 角色id
	 * @return String 成员设备名称
	 * @throws Exception
	 */
	public String queryBundleNameByUniqueRole(DeviceGroupPO group, Long roleId) throws Exception{
		Set<DeviceGroupMemberPO> members = group.getMembers();
		if(members!=null && members.size()>0){
			for(DeviceGroupMemberPO member:members){
				if(roleId.equals(member.getRoleId())){
					return member.getBundleName();
				}
			}
		}
		return null;
	}
	
	/**
	 * @Title: 根据角色通道类型获取通道<br/> 
	 * @param group 设备组
	 * @param roleId 角色id
	 * @param channelType 通道类型
	 * @throws Exception   
	 * @return List<DeviceGroupMemberChannelPO> 通道列表
	 */
	public List<DeviceGroupMemberChannelPO> queryMemberChannel(DeviceGroupPO group, Long roleId, ChannelType channelType) throws Exception{
		List<DeviceGroupMemberChannelPO> channels = new ArrayList<DeviceGroupMemberChannelPO>();
		Set<DeviceGroupMemberPO> members = group.getMembers();
		if(members==null || members.size()<=0) return channels;
		for(DeviceGroupMemberPO member:members){
			if(member.getRoleId()!=null && member.getRoleId().equals(roleId)){
				Set<DeviceGroupMemberChannelPO> scopeChannels = member.getChannels();
				if(scopeChannels==null || scopeChannels.size()<=0) continue;
				for(DeviceGroupMemberChannelPO scopeChannel:scopeChannels){
					if(scopeChannel.getType().equals(channelType)){
						channels.add(scopeChannel);
					}
				}
			}
		}
		return channels;
	}
	
	/**
	 * @Title: 查找一个通道的layerId 
	 * @param group 设备组
	 * @param memberChannelId 成员通道id
	 * @throws Exception 设定文件 
	 * @return String 
	 */
	public String queryLayerId(DeviceGroupPO group, Long memberChannelId) throws Exception{
		Set<DeviceGroupMemberPO> members = group.getMembers();
		for(DeviceGroupMemberPO member:members){
			Set<DeviceGroupMemberChannelPO> channels = member.getChannels();
			for(DeviceGroupMemberChannelPO channel:channels){
				if(channel.getId().equals(memberChannelId)){
					return member.getLayerId();
				}
			}
		}
		return null;
	}
	
	/**
	 * @Title: 查询一个配置 
	 * @param group 设备组
	 * @param configId 配置id
	 * @param run 标识是否要改变配置执行状态
	 * @throws Exception 
	 * @return DeviceGroupConfigPO 
	 */
	public DeviceGroupConfigPO queryConfig(DeviceGroupPO group, Long configId, boolean run) throws Exception{
		Set<DeviceGroupConfigPO> configs = group.getConfigs();
		DeviceGroupConfigPO target = null;
		for(DeviceGroupConfigPO config:configs){
			if(config.getId().equals(configId)){
				if(run){
					config.setRun(true);
				}
				target = config;
			}else{
				if(run && ConfigType.AGENDA.equals(config.getType())){
					config.setRun(false);
				}
			}
		}
		return target;
	}
	
	/**
	 * @Title: 查询一个配置视频
	 * @param group 设备组
	 * @param videoId 视频id
	 * @throws Exception 
	 * @return  DeviceGroupConfigVideoPO 配置视频数据
	 */
	public DeviceGroupConfigVideoPO queryConfigVideo(DeviceGroupPO group, Long videoId) throws Exception{
		Set<DeviceGroupConfigPO> configs = group.getConfigs();
		for(DeviceGroupConfigPO config:configs){
			Set<DeviceGroupConfigVideoPO> videos = config.getVideos();
			for(DeviceGroupConfigVideoPO video:videos){
				if(video.getId().equals(videoId)){
					return video;
				}
			}
		}
		return null;
	}
	
	/**
	 * 查询配置了发言人并且生效的合屏配置<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月17日 下午2:36:45
	 * @param group 设备组
	 * @param role 发言人
	 * @return 生效的合屏配置
	 * @throws Exception
	 */
	public List<DeviceGroupConfigVideoPO> queryEffectiveConfigVideosSettedSpokesman(
			DeviceGroupPO group, 
			DeviceGroupBusinessRolePO role) throws Exception{
		
		List<DeviceGroupConfigVideoPO> configVideos = new ArrayList<DeviceGroupConfigVideoPO>();
		Set<CombineVideoPO> combineVideos = group.getCombineVideos();
		if(combineVideos==null || combineVideos.size()<=0) return configVideos;
		
		Set<DeviceGroupConfigPO> configs = group.getConfigs();
		if(configs!=null && configs.size()>0){
			for(DeviceGroupConfigPO config:configs){
				Set<DeviceGroupConfigVideoPO> videos = config.getVideos();
				if(videos!=null && videos.size()>0){
					for(DeviceGroupConfigVideoPO video:videos){
						boolean finded = false;
						Set<DeviceGroupConfigVideoPositionPO> positions = video.getPositions();
						if(positions!=null && positions.size()>0){
							for(DeviceGroupConfigVideoPositionPO position:positions){
								List<DeviceGroupConfigVideoSrcPO> srcs = position.getSrcs();
								if(srcs!=null && srcs.size()>0){
									for(DeviceGroupConfigVideoSrcPO src:srcs){
										if(ForwardSrcType.ROLE.equals(src.getType()) && 
												role.getId().equals(src.getRoleId())){
											finded = true;
											break;
										}
									}
									if(finded) break;
								}
							}
						}
						
						if(finded){
							boolean effective = false;
							for(CombineVideoPO combineVideo:combineVideos){
								if(combineVideo.getUuid().equals(video.getUuid())){
									effective = true;
									break;
								}
							}
							if(effective){
								configVideos.add(video);
							}
						}
					}
				}
			}
		}
		
		return configVideos;
	}
	
	/**
	 * @Title: 查询给定合屏uuid之外的合屏<br/> 
	 * @param group 设备组
	 * @param exceptUuids 给定合屏uuid
	 * @throws Exception 
	 * @return List<CombineVideoPO> 合屏
	 */
	public List<CombineVideoPO> queryCombineVideoExceptUuids(DeviceGroupPO group, Collection<String> exceptUuids) throws Exception{
		Set<CombineVideoPO> combineVideos = group.getCombineVideos();
		if(combineVideos!=null && combineVideos.size()>0){
			List<CombineVideoPO> filteredCombineVideos = new ArrayList<CombineVideoPO>();
			if(exceptUuids==null || exceptUuids.size()<=0){
				filteredCombineVideos.addAll(combineVideos);
			}else{
				for(CombineVideoPO combineVideo:combineVideos){
					if(!exceptUuids.contains(combineVideo.getUuid())){
						filteredCombineVideos.add(combineVideo);
					}
				}
			}
			return filteredCombineVideos;
		}else{
			return null;
		}
	}
	
	/**
	 * @Title: 查询一个合屏 
	 * @param group 设备组
	 * @param combineVideoUuid 合屏uuid
	 * @throws Exception
	 * @return CombineVideoPO 合屏数据
	 */
	public CombineVideoPO queryCombineVideo(DeviceGroupPO group, String combineVideoUuid) throws Exception{
		Set<CombineVideoPO> videos = group.getCombineVideos();
		if(videos != null){
			for(CombineVideoPO video:videos){
				if(video.getUuid().equals(combineVideoUuid)){
					return video;
				}
			}
		}
		return null;
	}
	
	/**
	 * 查询在合屏中src包括member的video<br/>
	 * @Title: queryVideoContainsMemberByCombineVideo 
	 * @param videos 视频列表
	 * @param combineVideos 合屏列表
	 * @param member 成员信息
	 * @return List<DeviceGroupConfigVideoPO>
	 * @throws
	 */
	public List<DeviceGroupConfigVideoPO> queryVideoContainsMemberByCombineVideo(Collection<DeviceGroupConfigVideoPO> videos, Collection<CombineVideoPO> combineVideos, DeviceGroupMemberPO member){
		
		List<DeviceGroupConfigVideoPO> SrcContainBundleIdVideos = new ArrayList<DeviceGroupConfigVideoPO>();
		for(CombineVideoPO combineVideo: combineVideos){
			for(DeviceGroupConfigVideoPO video: videos){
				if(combineVideo.getUuid().equals(video.getUuid())){
					if(video.getPositions() != null && video.getPositions().size() > 0){
						for(DeviceGroupConfigVideoPositionPO position: video.getPositions()){
							if(position.getSrcs() != null && position.getSrcs().size() > 0){
								for(DeviceGroupConfigVideoSrcPO src: position.getSrcs()){
									if((src.getType().equals(ForwardSrcType.CHANNEL) && src.getBundleId().equals(member.getBundleId())) || (src.getType().equals(ForwardSrcType.ROLE) && src.getRoleId().equals(member.getRoleId()))){
										SrcContainBundleIdVideos.add(video);
									}
								}
							}
						}
					}
				}
			}
		}
		
		return SrcContainBundleIdVideos;
	}
	
	/**
	 * 找到成员所有观看的有效合屏<br/>
	 * @Title: queryAllCombineVideoByMember 
	 * @param group 设备组信息
	 * @param member 成员信息
	 * @return List<CombineVideoPO> 合屏列表
	 * @throws
	 */
	public List<CombineVideoPO> queryAllCombineVideoByMember(Collection<DeviceGroupConfigVideoPO> videos, Collection<CombineVideoPO> combineVideos, DeviceGroupMemberPO member){
		
		List<CombineVideoPO> dstContainBundleIdVideos = new ArrayList<CombineVideoPO>();
		for(CombineVideoPO combineVideo: combineVideos){
			for(DeviceGroupConfigVideoPO video: videos){
				if(combineVideo.getUuid().equals(video.getUuid())){
					if(video.getDsts() != null && video.getDsts().size() > 0){
						for(DeviceGroupConfigVideoDstPO dst: video.getDsts()){
							if((dst.getType().equals(ForwardDstType.SCREEN) && dst.getBundleId().equals(member.getBundleId())) || (dst.getType().equals(ForwardDstType.ROLE) && dst.getRoleId().equals(member.getRoleId()))){
								dstContainBundleIdVideos.add(combineVideo);
							}
						}
					}
				}
			}
		}
		
		return dstContainBundleIdVideos;
	}
	
	/**
	 * @Title: 以新的引用获取会议中所有的合屏<br/> 
	 * @param group 设备组
	 * @throws Exception
	 * @return List<CombineVideoPO> 合屏
	 */
	public List<CombineVideoPO> queryCombineVideosAsNewPointer(DeviceGroupPO group) throws Exception{
		Set<CombineVideoPO> combineVideos = group.getCombineVideos();
		if(combineVideos==null || combineVideos.size()<=0) return null;
		return new ArrayListWrapper<CombineVideoPO>().addAll(combineVideos).getList();
	}
	
	/**
	 * @Title: 查询成员的视频频编码通道1 <br/>
	 * @param group 设备组
	 * @param memberId 成员id
	 * @throws Exception 
	 * @return DeviceGroupMemberChannelPO
	 */
	public DeviceGroupMemberChannelPO queryEncodeVideoChannel1(DeviceGroupPO group, Long memberId) throws Exception{
		Set<DeviceGroupMemberPO> members = group.getMembers();
		for(DeviceGroupMemberPO member:members){
			if(member.getId().equals(memberId)){
				Set<DeviceGroupMemberChannelPO> channels = member.getChannels();
				for(DeviceGroupMemberChannelPO channel:channels){
					if(ChannelType.VIDEOENCODE1.equals(channel.getType())){
						return channel;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * @Title: 查询成员的视频解码通道1 <br/>
	 * @param group 设备组
	 * @param memberId 成员id
	 * @throws Exception 
	 * @return DeviceGroupMemberChannelPO
	 */
	public DeviceGroupMemberChannelPO queryDecodeVideoChannel1(DeviceGroupPO group, Long memberId) throws Exception{
		Set<DeviceGroupMemberPO> members = group.getMembers();
		for(DeviceGroupMemberPO member:members){
			if(member.getId().equals(memberId)){
				Set<DeviceGroupMemberChannelPO> channels = member.getChannels();
				for(DeviceGroupMemberChannelPO channel:channels){
					if(ChannelType.VIDEODECODE1.equals(channel.getType())){
						return channel;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * @Title: 查询成员的音频编码通道 <br/>
	 * @param group 设备组
	 * @param memberId 成员id
	 * @throws Exception 
	 * @return DeviceGroupMemberChannelPO
	 */
	public DeviceGroupMemberChannelPO queryEncodeAudioChannel(DeviceGroupPO group, Long memberId) throws Exception{
		Set<DeviceGroupMemberPO> members = group.getMembers();
		for(DeviceGroupMemberPO member:members){
			if(member.getId().equals(memberId)){
				Set<DeviceGroupMemberChannelPO> channels = member.getChannels();
				for(DeviceGroupMemberChannelPO channel:channels){
					if(ChannelType.AUDIOENCODE1.equals(channel.getType())){
						return channel;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * @Title: 查询成员的音频解码通道 <br/>
	 * @param group 设备组
	 * @param memberId 成员id
	 * @throws Exception 
	 * @return DeviceGroupMemberChannelPO
	 */
	public DeviceGroupMemberChannelPO queryDecodeAudioChannel(DeviceGroupPO group, Long memberId) throws Exception{
		Set<DeviceGroupMemberPO> members = group.getMembers();
		for(DeviceGroupMemberPO member:members){
			if(member.getId().equals(memberId)){
				Set<DeviceGroupMemberChannelPO> channels = member.getChannels();
				for(DeviceGroupMemberChannelPO channel:channels){
					if(ChannelType.AUDIODECODE1.equals(channel.getType())){
						return channel;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * @Title: 查询成员的视频解码通道 <br/>
	 * @param group 设备组
	 * @param memberId 成员id
	 * @throws Exception 
	 * @return List<DeviceGroupMemberChannelPO>
	 */
	public List<DeviceGroupMemberChannelPO> queryDecodeVideoChannel(DeviceGroupPO group, Long memberId) throws Exception{
		Set<DeviceGroupMemberPO> members = group.getMembers();
		List<DeviceGroupMemberChannelPO> videoChannels = new ArrayList<DeviceGroupMemberChannelPO>();
		for(DeviceGroupMemberPO member:members){
			if(member.getId().equals(memberId)){
				Set<DeviceGroupMemberChannelPO> channels = member.getChannels();
				for(DeviceGroupMemberChannelPO channel:channels){
					if(channel.getType().isVideoDecode()){
						videoChannels.add(channel);
					}
				}
			}
		}
		return videoChannels;
	}
	
	/**
	 * @Title: 获取所有设备组成员id列表<br/> 
	 * @param group 设备组
	 * @throws Exception
	 * @return List<Long> 设备组成员id列表
	 */
	public List<Long> queryAllMemberIds(DeviceGroupPO group) throws Exception{
		List<Long> memberIds = new ArrayList<Long>();
		Set<DeviceGroupMemberPO> members = group.getMembers();
		for(DeviceGroupMemberPO member:members){
			memberIds.add(member.getId());
		}
		return memberIds;
	}
	
	/**
	 * @Title: 获取所有设备组有效成员列表<br/> 
	 * @param group 设备组
	 * @throws Exception
	 * @return List<DeviceGroupMemberPO> 设备组成员id列表
	 */
	public List<DeviceGroupMemberPO> queryEffectiveMembers(DeviceGroupPO group) throws Exception{
		List<DeviceGroupMemberPO> effectiveMembers = new ArrayList<DeviceGroupMemberPO>();
		Set<DeviceGroupMemberPO> members = group.getMembers();
		for(DeviceGroupMemberPO member:members){
			if(member.getMemberStatus().equals(MemberStatus.CONNECT)){
				effectiveMembers.add(member);
			}		
		}
		return effectiveMembers;
	}
	
	/**
	 * @Title: 获取所有设备组有效成员id列表<br/> 
	 * @param group 设备组
	 * @throws Exception
	 * @return List<Long> 设备组成员id列表
	 */
	public List<Long> queryEffectiveMemberIds(DeviceGroupPO group) throws Exception{
		List<Long> memberIds = new ArrayList<Long>();
		Set<DeviceGroupMemberPO> members = group.getMembers();
		for(DeviceGroupMemberPO member:members){
			if(member.getMemberStatus().equals(MemberStatus.CONNECT)){
				memberIds.add(member.getId());
			}		
		}
		return memberIds;
	}
	
	/**
	 * @Title: 获取所有打开音频的设备组id列表<br/> 
	 * @param group 设备组
	 * @throws Exception
	 * @return List<Long> 设备组成员id列表
	 */
	public List<Long> queryAllVoicedIds(DeviceGroupPO group) throws Exception{
		List<Long> voicedIds = new ArrayList<Long>();
		Set<DeviceGroupMemberPO> members = group.getMembers();
		for(DeviceGroupMemberPO member:members){
			if(member.isOpenAudio()) voicedIds.add(member.getId());
		}
		return voicedIds;
	}
	
	/**
	 * @Title: 查询全量音频<br/> 
	 * @param group 设备组
	 * @throws Exception 
	 * @return CombineAudioPO 
	 */
	public CombineAudioPO queryFullAudio(DeviceGroupPO group) throws Exception{
		List<Long> totalVoicedIds = queryAllVoicedIds(group);
		CombineAudioBO fullAudio = CombineAudioBO.getnerateFullAudioFromMembers(totalVoicedIds);
		if(fullAudio == null) return null;
		Set<CombineAudioPO> combineAudios = group.getCombineAudios();
		for(CombineAudioPO combineAudio:combineAudios){
			CombineAudioBO audio = new CombineAudioBO().set(combineAudio);
			if(audio.equals(fullAudio)){
				return combineAudio;
			}
		}
		return null;
	}
	
	/**
	 * @Title: 获取一个混音<br/> 
	 * @param group 设备组
	 * @param combineAudioUuid 混音uuid
	 * @throws Exception
	 * @return void 
	 */
	public CombineAudioPO queryCombineAudio(DeviceGroupPO group, String combineAudioUuid) throws Exception{
		Set<CombineAudioPO> audios = group.getCombineAudios();
		for(CombineAudioPO audio:audios){
			if(audio.getUuid().equals(combineAudioUuid)){
				return audio;
			}
		}
		return null;
	}
	
	/**
	 * @Title: 获取一个空源或单源混音(可用的混音)<br/> 
	 * @param group 设备组
	 * @throws Exception
	 * @return CombineAudioPO 
	 */
	public CombineAudioPO queryUsefulCombineAudio(DeviceGroupPO group) throws Exception{
		Set<CombineAudioPO> audios = group.getCombineAudios();
		for(CombineAudioPO audio:audios){
			if(audio.getSrcs().size() == 0){
				return audio;
			}else if(audio.getSrcs().size() == 1){
				Set<CombineAudioSrcPO> srcs = audio.getSrcs();
				//解关联
				for(CombineAudioSrcPO src:srcs){
					src.setCombineAudio(null);
				}
				audio.getSrcs().removeAll(srcs);
				return audio;
			}
		}		
		return null;
	}
	
	/**
	 * @Title: 获取多个混音<br/>  
	 * @param group 设备组
	 * @param combineAudioUuids  混音uuid列表
	 * @throws Exception 
	 * @return List<CombineAudioPO> 
	 */
	public List<CombineAudioPO> queryCombineAudio(DeviceGroupPO group, Collection<String> combineAudioUuids) throws Exception{
		List<CombineAudioPO> audios = new ArrayList<CombineAudioPO>();
		Set<CombineAudioPO> totalAudios = group.getCombineAudios();
		for(CombineAudioPO audio:totalAudios){
			if(combineAudioUuids.contains(audio.getUuid())){
				audios.add(audio);
			}
		}
		return audios;
	}
	
	/**
	 * @Title: 以新的引用获取会议中所有的混音 
	 * @param group 设备组
	 * @throws Exception
	 * @return List<ChannelFowardPO> 混音
	 */
	public List<CombineAudioPO> queryCombineAudiosAsNewPointer(DeviceGroupPO group) throws Exception{
		Set<CombineAudioPO> combineAudios = group.getCombineAudios();
		if(combineAudios==null || combineAudios.size()<=0) return null;
		return new ArrayListWrapper<CombineAudioPO>().addAll(combineAudios).getList();
	}
	
	/**
	 * @Title: 判断设备组是否静音<br/> 
	 * @param group 设备组
	 * @throws Exception 
	 * @return boolean 
	 */
	public boolean isMute(DeviceGroupPO group) throws Exception{
		Set<DeviceGroupMemberPO> members = group.getMembers();
		for(DeviceGroupMemberPO member:members){
			if(member.isOpenAudio()){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * @Title: 查找一个设备组成员的音频转发 <br/>
	 * @param group
	 * @param memberId
	 * @throws Exception 
	 * @return ChannelFowardPO 转发对象
	 */
	public ChannelForwardPO queryAudioForward(DeviceGroupPO group, Long memberId) throws Exception{
		DeviceGroupMemberChannelPO decodeChannel = queryDecodeAudioChannel(group, memberId);
		Set<ChannelForwardPO> forwards = group.getForwards();
		for(ChannelForwardPO forward:forwards){
			if(forward.getMemberChannelId() != null && forward.getMemberChannelId().equals(decodeChannel.getId())){
				return forward;
			}
		}
		return null;
	}
	
	/**
	 * @Title: 查找一个设备组成员的视频转发 <br/>
	 * @param group
	 * @param memberId
	 * @throws Exception 
	 * @return ChannelFowardPO 转发对象
	 */
	public List<ChannelForwardPO> queryVideoForward(DeviceGroupPO group, Long memberId) throws Exception{
		List<ChannelForwardPO> channelforwards = new ArrayList<ChannelForwardPO>();
		List<DeviceGroupMemberChannelPO> decodeChannels = queryDecodeVideoChannel(group, memberId);
		Set<ChannelForwardPO> forwards = group.getForwards();
		for(ChannelForwardPO forward:forwards){
			for(DeviceGroupMemberChannelPO decodeChannel:decodeChannels){
				if(forward.getMemberChannelId() != null && forward.getMemberChannelId().equals(decodeChannel.getId())){
					channelforwards.add(forward);
				}
			}
		}
		return channelforwards;
	}
	
	/**
	 * @Title: 查找一个通道转发<br/> 
	 * @param group 设备组
	 * @param memberChannelId 通道id
	 * @throws Exception 
	 * @return ChannelFowardPO 转发数据
	 */
	public ChannelForwardPO queryChannelForward(DeviceGroupPO group, Long memberChannelId) throws Exception{
		Set<ChannelForwardPO> forwards = group.getForwards();
		for(ChannelForwardPO forward:forwards){
			if(forward.getMemberChannelId() != null && forward.getMemberChannelId().equals(memberChannelId)){
				return forward;
			}
		}
		return null;
	}
	
	/**
	 * @Title: 查找一个角色转发<br/> 
	 * @param group 设备组
	 * @param roleId 通道id
	 * @throws Exception 
	 * @return ChannelFowardPO 转发数据
	 */
	public ChannelForwardPO queryRoleForward(DeviceGroupPO group, Long roleId, ChannelType type) throws Exception{
		Set<ChannelForwardPO> forwards = group.getForwards();
		for(ChannelForwardPO forward:forwards){
			if(forward.getRoleId() != null && forward.getRoleId().equals(roleId) && forward.getChannelType() != null && forward.getChannelType().equals(type)){
				return forward;
			}
		}
		return null;
	}
	
	/**
	 * @Title: 查询给定通道之外的视频转发转发，包括合屏转发以及通道转发<br/> 
	 * @param group 设备组
	 * @param exceptMemberChannelIds 成员通道id
	 * @throws Exception
	 * @return List<ChannelFowardPO> 转发
	 */
	public List<ChannelForwardPO> queryVideoChannelForwardExceptMemberChannelId(DeviceGroupPO group, Collection<Long> exceptMemberChannelIds) throws Exception{
		Set<ChannelForwardPO> forwards = group.getForwards();
		List<ChannelForwardPO> filteredForwards = new ArrayList<ChannelForwardPO>();	
		if(forwards!=null && forwards.size()>0){
			for(ChannelForwardPO forward:forwards){
				if(forward.getForwardDstType().equals(ForwardDstType.CHANNEL) && !exceptMemberChannelIds.contains(forward.getMemberChannelId()) && 
						!forward.getForwardSourceType().equals(ForwardSourceType.COMBINEAUDIO) && 
						!forward.getForwardSourceType().equals(ForwardSourceType.FORWARDAUDIO)){
					filteredForwards.add(forward);
				}
			}
			return filteredForwards;
		}else{
			return filteredForwards;
		}
		
	}
	
	public List<ChannelForwardPO> queryVideoChannelForwardExceptRoleId(DeviceGroupPO group, Collection<Long> exceptRoleIds1, Collection<Long> exceptRoleIds2) throws Exception{
		Set<ChannelForwardPO> forwards = group.getForwards();
		List<ChannelForwardPO> filteredForwards = new ArrayList<ChannelForwardPO>();
		if(forwards!=null && forwards.size()>0){
			for(ChannelForwardPO forward:forwards){
				if(forward.getForwardDstType().equals(ForwardDstType.ROLE)){
					if(!forward.getForwardSourceType().equals(ForwardSourceType.COMBINEAUDIO) && !forward.getForwardSourceType().equals(ForwardSourceType.FORWARDAUDIO)){
						if(forward.getChannelType().equals(ChannelType.VIDEODECODE1) && !exceptRoleIds1.contains(forward.getRoleId())){
							filteredForwards.add(forward);
						}
						if(forward.getChannelType().equals(ChannelType.VIDEODECODE2) && !exceptRoleIds2.contains(forward.getRoleId())){
							filteredForwards.add(forward);
						}
					}
				}
			}
			return filteredForwards;
		}else{
			return filteredForwards;
		}
		
	}
	
	/**
	 * @Title: 判断合屏是否有转发<br/> 
	 * @param group 设备组
	 * @param combineVideoUuid 合屏uuid
	 * @throws Exception
	 * @return boolean 
	 */
	public boolean hasCombineVideoForward(DeviceGroupPO group, String combineVideoUuid) throws Exception{
		Set<ChannelForwardPO> forwards = group.getForwards();
		if(forwards==null || forwards.size()<=0) return false;
		for(ChannelForwardPO forward:forwards){
			if(ForwardSourceType.COMBINEVIDEO.equals(forward.getForwardSourceType()) && forward.getCombineUuid().equals(combineVideoUuid)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @Title: 判断混音是否有转发<br/> 
	 * @param group 设备组
	 * @param combineAudioUuid 混音uuid
	 * @throws Exception
	 * @return boolean 
	 */
	public boolean hasCombineAudioForward(DeviceGroupPO group, String combineAudioUuid) throws Exception{
		Set<ChannelForwardPO> forwards = group.getForwards();
		if(forwards==null || forwards.size()<=0) return false;
		for(ChannelForwardPO forward:forwards){
			if(ForwardSourceType.COMBINEAUDIO.equals(forward.getForwardSourceType()) && forward.getCombineUuid().equals(combineAudioUuid)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 根据通道获取通道转发<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月18日 上午8:54:05
	 * @param group 设备组
	 * @param channels 通道
	 * @return List<ChannelForwardPO> 通道转发
	 * @throws Exception
	 */
	public List<ChannelForwardPO> queryForwardByChannels(DeviceGroupPO group, Collection<DeviceGroupMemberChannelPO> channels) throws Exception{
		List<ChannelForwardPO> filteredForwards = new ArrayList<ChannelForwardPO>();
		Set<ChannelForwardPO> forwards = group.getForwards();
		if(forwards!=null && forwards.size()>0){
			for(ChannelForwardPO forward:forwards){
				boolean finded = false;
				for(DeviceGroupMemberChannelPO channel:channels){
					if(forward.getChannelId().equals(channel.getChannelId()) && forward.getBundleId().equals(channel.getBundleId())){
						finded = true;
						break;
					}
				}
				if(finded) filteredForwards.add(forward);
			}
		}
		return filteredForwards;
	}
	
	/**
	 * 根据通道屏幕获取通道转发<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月14日 上午8:54:05
	 * @param group 设备组
	 * @param screens 通道
	 * @return List<ChannelForwardPO> 通道转发
	 * @throws Exception
	 */
	public List<ChannelForwardPO> queryForwardByScreens(DeviceGroupPO group, Collection<DeviceGroupMemberScreenPO> screens) throws Exception{
		List<ChannelForwardPO> filteredForwards = new ArrayList<ChannelForwardPO>();
		Set<ChannelForwardPO> forwards = group.getForwards();
		if(forwards!=null && forwards.size()>0){
			for(ChannelForwardPO forward:forwards){
				boolean finded = false;
				for(DeviceGroupMemberScreenPO screen:screens){
					if(forward.getScreenId() != null && forward.getScreenId().equals(screen.getScreenId()) && forward.getBundleId().equals(screen.getBundleId())){
						finded = true;
						break;
					}
				}
				if(finded) filteredForwards.add(forward);
			}
		}
		return filteredForwards;
	}
	
	/**
	 * 获取一个成员的通道转发
	 * @Title: queryForwardByMember 
	 * @param group 设备组 信息
	 * @param member 成员信息
	 * @return List<ChannelForwardPO> 通道转发
	 * @throws
	 */
	public List<ChannelForwardPO> queryForwardByMember(DeviceGroupPO group, DeviceGroupMemberPO member){
		List<ChannelForwardPO> filteredForwards = new ArrayList<ChannelForwardPO>();
		Set<ChannelForwardPO> forwards = group.getForwards();
		if(forwards!=null && forwards.size()>0){
			for(ChannelForwardPO forward:forwards){
				if(forward.getBundleId().equals(member.getBundleId())){
					filteredForwards.add(forward);
				}
			}
		}
		return filteredForwards;
	}
	
	/**
	 * 根据角色获取会议成员<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月12日 下午4:15:51
	 * @param group 设备组
	 * @param roleId 角色id
	 * @return List<DeviceGroupMemberPO> 设备组
	 * @throws Exception
	 */
	public List<DeviceGroupMemberPO> queryMemberByRole(DeviceGroupPO group, Long roleId) throws Exception{
		List<DeviceGroupMemberPO> selectedMembers = new ArrayList<DeviceGroupMemberPO>();
		Set<DeviceGroupMemberPO> members = group.getMembers();
		if(members!=null && members.size()>0){
			for(DeviceGroupMemberPO member:members){
				if(roleId.equals(member.getRoleId())){
					selectedMembers.add(member);
				}
			}
		}
		return selectedMembers;
	}
	
	/**
	 * 根据成员id获取成员<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月18日 上午10:08:03
	 * @param group 设备组 
	 * @param memberId 成员id
	 * @return DeviceGroupMemberPO 设备组成员
	 * @throws Exception
	 */
	public DeviceGroupMemberPO queryMemberById(DeviceGroupPO group, Long memberId) throws Exception{
		Set<DeviceGroupMemberPO> members = group.getMembers();
		if(members!=null && members.size()>0){
			for(DeviceGroupMemberPO member:members){
				if(member.getId().equals(memberId)){
					return member;
				}
			}
		}
		return null;
	}
	
	/**
	 * 根据成员ids获取成员列表<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 上午10:08:03
	 * @param group 设备组 
	 * @param memberIds 成员ids
	 * @return List<DeviceGroupMemberPO> 设备组成员
	 * @throws Exception
	 */
	public List<DeviceGroupMemberPO> queryMembersByIds(DeviceGroupPO group, Collection<Long> memberIds) throws Exception{
		
		List<DeviceGroupMemberPO> memberPOs = new ArrayList<DeviceGroupMemberPO>();
		Set<DeviceGroupMemberPO> members = group.getMembers();
		if(members!=null && members.size()>0 && memberIds != null && memberIds.size() > 0){
			for(DeviceGroupMemberPO member:members){
				for(Long memberId: memberIds){
					if(member.getId().equals(memberId)){
						memberPOs.add(member);
						break;
					}
				}
			}
		}
		return memberPOs;
	}
	
	
	/**
	 * 根据角色类型获取角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月17日 下午3:41:34
	 * @param group 设备组
	 * @param special 角色类型
	 * @return List<DeviceGroupBusinessRolePO> 角色
	 * @throws Exception
	 */
	public List<DeviceGroupBusinessRolePO> queryRoleBySpecial(DeviceGroupPO group, BusinessRoleSpecial special) throws Exception{
		List<DeviceGroupBusinessRolePO> targetRoles = new ArrayList<DeviceGroupBusinessRolePO>();
		Set<DeviceGroupBusinessRolePO> roles = group.getRoles();
		if(roles!=null && roles.size()>0){
			for(DeviceGroupBusinessRolePO role:roles){
				if(role.getSpecial().equals(special)){
					targetRoles.add(role);
				}
			}
		}
		return targetRoles;
	}
	
	/**
	 * @Title: 根据指定多个角色类型获取角色下的成员
	 * @param group 设备组
	 * @param specials 多个角色类型
	 * @return List<DeviceGroupMemberPO>
	 * @throws
	 */
	public List<DeviceGroupMemberPO> queryRoleMembersBySpecials(DeviceGroupPO group, Collection<BusinessRoleSpecial> specials) throws Exception{
		List<DeviceGroupMemberPO> members = new ArrayList<DeviceGroupMemberPO>();
		Set<DeviceGroupMemberPO> allMembers = group.getMembers();
		List<DeviceGroupBusinessRolePO> roles = new ArrayList<DeviceGroupBusinessRolePO>();
		for(BusinessRoleSpecial special:specials){
			roles.addAll(queryRoleBySpecial(group, special));
		}		
		if(roles!=null && roles.size()>0){
			for(DeviceGroupMemberPO member: allMembers){
				for(DeviceGroupBusinessRolePO role: roles){
					if(member.getRoleId()!=null && member.getRoleId().equals(role.getId())){
						members.add(member);
						break;
					}
				}
			}
		}
		
		return members;
	}
	
	/**
	 * 根据id获取角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月18日 上午10:15:45
	 * @param group 设备组
	 * @param roleId 角色id
	 * @return DeviceGroupBusinessRolePO 角色
	 * @throws Exception
	 */
	public DeviceGroupBusinessRolePO queryRoleById(DeviceGroupPO group, Long roleId) throws Exception{
		Set<DeviceGroupBusinessRolePO> roles = group.getRoles();
		if(roles!=null && roles.size()>0){
			for(DeviceGroupBusinessRolePO role:roles){
				if(role.getId().equals(roleId)){
					return role;
				}
			}
		}
		return null;
	}
	
	/**
	 * 根据channelType获取通道别名<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月17日 下午4:51:11
	 * @param List<ChannelNamePO> channelNames 所有通道别名
	 * @param String channelType 通道类型
	 * @return String 通道别名
	 */
	public String queryChannelNameByChannelType(List<ChannelNamePO> channelNames, String channelType){
		if(channelNames != null && channelNames.size() > 0){
			for(ChannelNamePO channelName: channelNames){
				if(channelName.getChannelType().equals(channelType)){
					return channelName.getName();
				}
			}
		}
		return null;
	}
	
	/**
	 * @Title: 查询一个角色通道的转发，这个方法主要用于录制角色视频解码通道的转发<br/> 
	 * @param group 设备组
	 * @param roleId 角色id
	 * @param type 视频解码通道类型
	 * @throws Exception 
	 * @return ChannelFowardPO 转发
	 */
	public ChannelForwardPO queryRoleChannelForward(DeviceGroupPO group, Long roleId, ChannelType type) throws Exception{
		DeviceGroupMemberChannelPO targetChannel = null;
		Set<DeviceGroupMemberPO> members = group.getMembers();
		for(DeviceGroupMemberPO member:members){
			boolean breakOuter = false;
			if(roleId.equals(member.getRoleId())){
				Set<DeviceGroupMemberChannelPO> channels = member.getChannels();
				for(DeviceGroupMemberChannelPO channel:channels){
					if(channel.getType().equals(type)){
						targetChannel = channel;
						breakOuter = true;
						break;
					}
				}
			}
			if(breakOuter) break;
		}
		return queryChannelForward(group, targetChannel.getId());
	}
	
	/**
	 *  查询一个角色通道的转发，这个方法主要用于录制角色视频解码通道的转发<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月18日 上午8:56:22
	 * @param DeviceGroupPO group 设备组
	 * @param Long roleId 角色id
	 * @param ChannelType type 视频解码通道类型
	 * @return ChannelForwardPO 转发
	 */
	public ChannelForwardPO queryVirtualRoleChannelForward(DeviceGroupPO group, Long roleId, ChannelType type) throws Exception{
		ChannelForwardPO channelForward = null;
		Set<ChannelForwardPO> forwards = group.getForwards();
		for(ChannelForwardPO forward: forwards){
			if(forward.getForwardDstType().equals(ForwardDstType.ROLE) && forward.getRoleId() != null && forward.getRoleId().equals(roleId) 
					&& forward.getChannelType() != null &&forward.getChannelType().equals(type)){
				channelForward = forward;
				break;
			}
		}
		
		return channelForward;
	}
	
	/**
	 *  查询一个角色通道的转发，这个方法主要用于录制角色视频解码通道的转发<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月18日 上午8:56:22
	 * @param DeviceGroupPO group 设备组
	 * @param Long roleId 角色id
	 * @return List<ChannelForwardPO> 转发
	 */
	public List<ChannelForwardPO> queryVirtualRoleChannelForward(DeviceGroupPO group, Long roleId) throws Exception{
		List<ChannelForwardPO> channelForwards = new ArrayList<ChannelForwardPO>();
		Set<ChannelForwardPO> forwards = group.getForwards();
		for(ChannelForwardPO forward: forwards){
			if(forward.getForwardDstType().equals(ForwardDstType.ROLE) && forward.getRoleId() != null && forward.getRoleId().equals(roleId)){
				channelForwards.add(forward);
			}
		}
		
		return channelForwards;
	}
	
	/**
	 * 获取会议中编码通道类型的并集<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月12日 下午4:29:53
	 * @param group
	 * @return Map<ChannelType> 编码通道类型并集
	 */
	public Map<ChannelType, String> queryUnionEncodeChannelType(DeviceGroupPO group){
		Map<ChannelType, String> types = new HashMap<ChannelType, String>();
		Set<DeviceGroupMemberPO> members = group.getMembers();
		if(members!=null && members.size()>0){
			for(DeviceGroupMemberPO member:members){
				Set<DeviceGroupMemberChannelPO> channels = member.getChannels();
				if(channels!=null && channels.size()>0){
					for(DeviceGroupMemberChannelPO channel:channels){
						if(!types.containsKey(channel.getType()) && channel.getType()!=null && channel.getType().isVideoEncode()){
							types.put(channel.getType(), channel.getName());
						}
					}
				}
			}
		}
		return types;
	}
	
	/**
	 * 查询一个成员设备的解码通道类型并集<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月17日 下午7:18:32
	 * @param member 成员设备
	 * @return Map<ChannelType, String> 成员解码通道并集
	 * @throws Exception
	 */
	public Map<ChannelType, String> queryMemberUnionDecodeType(DeviceGroupMemberPO member) throws Exception{
		Map<ChannelType, String> types = new HashMap<ChannelType, String>();
		Set<DeviceGroupMemberChannelPO> channels = member.getChannels();
		if(channels!=null && channels.size()>0){
			for(DeviceGroupMemberChannelPO channel:channels){
				if(!types.containsKey(channel.getType()) && channel.getType()!=null && channel.getType().isVideoDecode()){
					types.put(channel.getType(), channel.getName());
				}
			}
		}
		return types;
	}
	
	/**
	 * 查询一个成员设备的屏幕类型并集<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月13日 下午7:18:32
	 * @param member 成员设备
	 * @return Map<String, String> 成员解码通道并集
	 * @throws Exception
	 */
	public Map<String, String> queryMemberUnionScreenId(DeviceGroupMemberPO member) throws Exception{
		Map<String, String> types = new HashMap<String, String>();
		Set<DeviceGroupMemberScreenPO> screens = member.getScreens();
		if(screens!=null && screens.size()>0){
			for(DeviceGroupMemberScreenPO screen:screens){
				if(!types.containsKey(screen.getScreenId()) && screen.getScreenId() != null){
					types.put(screen.getScreenId(), screen.getName());
				}
			}
		}
		return types;
	}
	
	/**
	 * @Title: 查询一个角色所对应的视频解码通道类型并集<br/> 
	 * @param group 设备组
	 * @param roleId 角色id
	 * @throws Exception 
	 * @return Set<ChannelType> 通道类型并集
	 */
	public Set<ChannelType> queryUnionChannelTypesByRole(DeviceGroupPO group, Long roleId) throws Exception{
		Set<ChannelType> types = new HashSet<ChannelType>();
		Set<DeviceGroupMemberPO> members = group.getMembers();
		for(DeviceGroupMemberPO member:members){
			if(roleId!=null && roleId.equals(member.getRoleId())){
				Set<DeviceGroupMemberChannelPO> channels = member.getChannels();
				for(DeviceGroupMemberChannelPO channel:channels){
					if(channel.getType().isVideoDecode()) types.add(channel.getType());
				}
			}
		}
		return types;
	}
	
	/**
	 * @Title: 查询一个角色所对应的屏幕id并集<br/> 
	 * @param group 设备 组
	 * @param roleId 角色id
	 * @return Set<String> 屏幕id并集
	 * @throws
	 */
	public Set<String> queryUnionScreenIdsByRole(DeviceGroupPO group, Long roleId) throws Exception{
		Set<String> screenIds = new HashSet<String>();
		Set<DeviceGroupMemberPO> members = group.getMembers();
		for(DeviceGroupMemberPO member: members){
			if(roleId!=null && roleId.equals(member.getRoleId())){
				Set<DeviceGroupMemberScreenPO> screens = member.getScreens();
				for(DeviceGroupMemberScreenPO screen: screens){
					screenIds.add(screen.getScreenId());
				}
			}
		}
		return screenIds;
	}
	
	/**
	 * @Title: 查询设备组中执行中的录制 <br/>
	 * @param group 设备组
	 * @return List<RecordPO> 执行中的录制
	 */
	public List<RecordPO> queryRunRecords(DeviceGroupPO group){
		List<RecordPO> runRecords = new ArrayList<RecordPO>();
		Set<RecordPO> records = group.getRecords();
		if(records!=null && records.size()>0){
			for(RecordPO record:records){
				if(record.isRun()){
					runRecords.add(record);
				}
			}
		}
		return runRecords;	
	}
	
	/**
	 * @Title: 查询设备组中执行的方案录制<br/> 
	 * @param group 设备组
	 * @return List<RecordPO> 执行中的方案录制
	 */
	public List<RecordPO> queryRunSchemeRecord(DeviceGroupPO group){
		List<RecordPO> runRecords = new ArrayList<RecordPO>();
		Set<RecordPO> records = group.getRecords();
		if(records!=null && records.size()>0){
			for(RecordPO record:records){
				if(record.isRun() && RecordType.SCHEME.equals(record.getType())){
					runRecords.add(record);
				}
			}
		}
		return runRecords;	
	}
	
	/**
	 * @Title: 根据角色查询录制<br/> 
	 * @param group 设备组
	 * @param roleId 角色id
	 * @param channelType 通道类型
	 * @return RecordPO
	 */
	public RecordPO queryRunRecord(DeviceGroupPO group, Long roleId, ChannelType channelType){
		Set<DeviceGroupRecordSchemePO> schemes = group.getRecordSchemes();
		if(schemes==null || schemes.size()<=0) return null;
		DeviceGroupRecordSchemePO targetScheme = null;
		for(DeviceGroupRecordSchemePO scheme:schemes){
			if(scheme.getRoleId().equals(roleId)) targetScheme = scheme;
		}
		//生成录制id
		String recordId = new StringBufferWrapper().append(targetScheme.getId())
												   .append("@@")
												   .append(channelType.toString())
												   .toString();
		Set<RecordPO> records = group.getRecords();
		for(RecordPO record:records){
			if(record.getRecordId().equals(recordId) && record.isRun()){
				return record;
			}
		}
		return null;
	}
	
	/**
	 * @Title: 判断一个角色是否是录制角色 <br/>
	 * @param group 设备组
	 * @param roleId 角色id
	 * @return boolean 
	 */
	public boolean isRecordScheme(DeviceGroupPO group, Long roleId){
		Set<DeviceGroupRecordSchemePO> schemes = group.getRecordSchemes();
		if(schemes==null || schemes.size()<=0) return false;
		for(DeviceGroupRecordSchemePO scheme:schemes){
			if(scheme.getRoleId().equals(roleId)) return true;
		}
		return false;
	}
	
	/**
	 * @Title: 查询设备组中的拼接屏设备<br/> 
	 * @param group 设备组
	 * @return List<CombineJv230PO> 拼接屏
	 */
	public List<CombineJv230PO> queryCombineJv230s(DeviceGroupPO group){
		Set<DeviceGroupMemberPO> members = group.getMembers();
		Set<Long> combineJv230Ids = new HashSet<Long>();
		if(members!=null && members.size()>0){
			for(DeviceGroupMemberPO member:members){
				if("combineJv230".equals(member.getBundleType())){
					combineJv230Ids.add(Long.valueOf(member.getBundleId()));
				}
			}
		}
		if(combineJv230Ids.size() <= 0) return null;
		List<CombineJv230PO> combineJv230s = combineJv230Dao.findAll(combineJv230Ids);
		group.setCombineJv230s(new HashSetWrapper<CombineJv230PO>().addAll(combineJv230s).getSet());
		return combineJv230s;
	}
	
	/**
	 * @Title: 根据成员查询拼接屏<br/> 
	 * @param group 设备组
	 * @param memberId 设备组成员id
	 * @return CombineJv230PO 拼接屏
	 */
	public CombineJv230PO queryCombineJv230(DeviceGroupPO group, Long memberId){
		Set<DeviceGroupMemberPO> members = group.getMembers();
		Set<CombineJv230PO> combineJv230s = group.getCombineJv230s();
		if(combineJv230s==null || combineJv230s.size()<=0) return null;
		if(members!=null && members.size()>0){
			for(DeviceGroupMemberPO member:members){
				if(member.getId().equals(memberId)){
					Long combineJv230Id = Long.valueOf(member.getBundleId());
					for(CombineJv230PO combineJv230:combineJv230s){
						if(combineJv230.getId().equals(combineJv230Id)){
							return combineJv230;
						}
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * @Title: 查找拼接屏一个音频通道，默认选序号为1的屏幕<br/> 
	 * @param combineJv230 拼接屏信息
	 * @return Jv230ChannelPO Jv230音频通道
	 */
	public Jv230ChannelPO queryJv230ChannelPO(CombineJv230PO combineJv230){
		
		Jv230ChannelPO jv230ChannelPO = new Jv230ChannelPO();

		Set<Jv230PO> jv230s = combineJv230.getBundles();
		for(Jv230PO jv230: jv230s){
			if(jv230.getSerialnum() == 1){
				Set<Jv230ChannelPO> channels = jv230.getChannels();
				for(Jv230ChannelPO channel: channels){
					if(channel.getType().isAudioDecode()){
						jv230ChannelPO = channel;
					}
				}
			}
		}
		
		return jv230ChannelPO;
	}
	
	/**
	 * @Title: 判断一个成员是否是大屏<br/> 
	 * @param group
	 * @param mmeberId
	 * @return boolean 
	 */
	public boolean isCombineJv230(DeviceGroupPO group, Long memberId){
		Set<DeviceGroupMemberPO> members = group.getMembers();
		DeviceGroupMemberPO targetMember = null;
		for(DeviceGroupMemberPO member:members){
			if(member.getId().equals(memberId)){
				targetMember = member;
				break;
			}
		}
		if("combineJv230".equals(targetMember.getBundleType())){
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * @Title: 根据bundleId查询member<br/>
	 * @param members 设备组成员
	 * @param bundleId 
	 * @return DeviceGroupMemberPO
	 */
	public DeviceGroupMemberPO queryMemberPOByBundleId(Collection<DeviceGroupMemberPO> members, String bundleId){
		
		for(DeviceGroupMemberPO member: members){
			if(member.getBundleId().equals(bundleId)){
				return member;
			}
		}
		
		return null;
	}
	
	/**
	 * @Title: 根据bundleId查询看会权限成员<br/>
	 * @param members 看会权限成员
	 * @param bundleId 
	 * @return DeviceGroupAuthorizationMemberPO
	 */
	public DeviceGroupAuthorizationMemberPO queryAuthorizationMemberPOByBundleId(Collection<DeviceGroupAuthorizationMemberPO> members, String bundleId){
		
		for(DeviceGroupAuthorizationMemberPO member: members){
			if(member.getBundleId().equals(bundleId)){
				return member;
			}
		}
		
		return null;
	}
	
	/**
	 * @Title: 根据bundleIds查询members<br/>
	 * @param members 设备组成员
	 * @param bundleId 
	 * @return DeviceGroupMemberPO
	 */
	public List<DeviceGroupMemberPO> queryMembersByBundleIds(Collection<DeviceGroupMemberPO> members, List<String> bundleIds){
		
		List<DeviceGroupMemberPO> queryMembers = new ArrayList<DeviceGroupMemberPO>();
		
		for(String bundleId: bundleIds){
			for(DeviceGroupMemberPO member: members){
				if(member.getBundleId().equals(bundleId)){
					queryMembers.add(member);
					break;
				}
			}
		}
		
		return queryMembers;
	}
	
	/**
	 * @Title: 根据bundleId查询bundlePO<br/>
	 * @param bundles 资源bundlePOs
	 * @param bundleId 
	 * @return BundlePO
	 */
	public BundlePO queryBundlePOByBundleId(Collection<BundlePO> bundles, String bundleId){
		
		for(BundlePO bundle: bundles){
			if(bundle.getBundleId().equals(bundleId)){
				return bundle;
			}
		}
		
		return null;
	}
	
	/**
	 * @Title: 根据bundleId查询所有音频源<br/>  
	 * @param audios
	 * @param bundleId
	 * @return DeviceGroupConfigAudioPO
	 */
	public List<DeviceGroupConfigAudioPO> queryDeviceGroupConfigAudioPOByBundleId(Collection<DeviceGroupConfigAudioPO> audios, String bundleId){
		
		List<DeviceGroupConfigAudioPO> audioPOs = new ArrayList<DeviceGroupConfigAudioPO>();
		if(audios != null && audios.size()>0){
			for(DeviceGroupConfigAudioPO audio: audios){
				if(audio.getBundleId().equals(bundleId)){
					audioPOs.add(audio);
				}
			}
		}
		
		return audioPOs;
	}	
	
	/**
	 * @Title: 根据bundleId查询所有视频源 <br/>
	 * @param videoSrcs
	 * @param bundleId
	 * @return DeviceGroupConfigVideoSrcPO
	 */
	public List<DeviceGroupConfigVideoSrcPO> queryDeviceGroupConfigVideoSrcPOByBundleId(Collection<DeviceGroupConfigVideoSrcPO> videoSrcs, String bundleId){
		
		List<DeviceGroupConfigVideoSrcPO> videoSrcPOs = new ArrayList<DeviceGroupConfigVideoSrcPO>();
		if(videoSrcs != null && videoSrcs.size()>0){
			for(DeviceGroupConfigVideoSrcPO videoSrc: videoSrcs){
				if(videoSrc.getType().equals(ForwardSrcType.CHANNEL) && videoSrc.getBundleId().equals(bundleId)){
					videoSrcPOs.add(videoSrc);
				}
			}
		}		
		
		return videoSrcPOs;
	}
	
	/**
	 * @Title: 根据bundleId查询所有视频目的 <br/>
	 * @param videoSrcs
	 * @param bundleId
	 * @return DeviceGroupConfigVideoSrcPO
	 */
	public List<DeviceGroupConfigVideoDstPO> queryDeviceGroupConfigVideoDstPOByBundleId(Collection<DeviceGroupConfigVideoDstPO> videoDsts, String bundleId){
		
		List<DeviceGroupConfigVideoDstPO> videoDstPOs = new ArrayList<DeviceGroupConfigVideoDstPO>();
		if(videoDsts != null && videoDsts.size()>0){
			for(DeviceGroupConfigVideoDstPO videoDst: videoDsts){
				//会有角色目的，所以需要判空
				if(videoDst.getType().equals(ForwardDstType.SCREEN) && videoDst.getBundleId() != null && videoDst.getBundleId().equals(bundleId)){
					videoDstPOs.add(videoDst);
				}
			}
		}		
		
		return videoDstPOs;
	}
	
	/**
	 * 查询角色通道对应的配置<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月17日 下午7:29:38
	 * @param group 设备组
	 * @param roleId 角色id
	 * @param type 通道类型
	 * @return List<DeviceGroupConfigVideoPO> 视频配置
	 * @throws Exception
	 */
	public List<DeviceGroupConfigVideoPO> queryRoleConfigVideo(
			DeviceGroupPO group, 
			Long roleId, 
			ChannelType type) throws Exception{
		
		List<DeviceGroupConfigVideoPO> legalVidoes = new ArrayList<DeviceGroupConfigVideoPO>();
		Set<DeviceGroupConfigPO> configs = group.getConfigs();
		if(configs==null || configs.size()<=0) return legalVidoes;
		
		for(DeviceGroupConfigPO config:configs){
			Set<DeviceGroupConfigVideoPO> videos = config.getVideos();
			if(videos!=null && videos.size()>0){
				for(DeviceGroupConfigVideoPO video:videos){
					boolean legal = false;
					Set<DeviceGroupConfigVideoDstPO> dsts = video.getDsts();
					if(dsts!=null && dsts.size()>0){
						for(DeviceGroupConfigVideoDstPO dst:dsts){
							if(ForwardDstType.ROLE.equals(dst.getType()) && 
									dst.getRoleId().equals(roleId) && 
									type.equals(dst.getRoleChannelType())){
								legal = true;
								break;
							}
						}
					}
					if(legal) legalVidoes.add(video);
				}
			}
		}
		
		return legalVidoes;
	}
	
	/**
	 * 查询角色通道对应的配置<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月13日 下午7:29:38
	 * @param group 设备组
	 * @param roleId 角色id
	 * @param screenId 屏幕id
	 * @return List<DeviceGroupConfigVideoPO> 视频配置
	 * @throws Exception
	 */
	public List<DeviceGroupConfigVideoPO> queryRoleConfigVideo(
			DeviceGroupPO group, 
			Long roleId, 
			String screenId) throws Exception{
		
		List<DeviceGroupConfigVideoPO> legalVidoes = new ArrayList<DeviceGroupConfigVideoPO>();
		Set<DeviceGroupConfigPO> configs = group.getConfigs();
		if(configs==null || configs.size()<=0) return legalVidoes;
		
		for(DeviceGroupConfigPO config:configs){
			Set<DeviceGroupConfigVideoPO> videos = config.getVideos();
			if(videos!=null && videos.size()>0){
				for(DeviceGroupConfigVideoPO video:videos){
					boolean legal = false;
					Set<DeviceGroupConfigVideoDstPO> dsts = video.getDsts();
					if(dsts!=null && dsts.size()>0){
						for(DeviceGroupConfigVideoDstPO dst:dsts){
							if(ForwardDstType.ROLE.equals(dst.getType()) && 
									dst.getRoleId().equals(roleId) && 
									screenId.equals(dst.getScreenId())){
								legal = true;
								break;
							}
						}
					}
					if(legal) legalVidoes.add(video);
				}
			}
		}
		
		return legalVidoes;
	}
	
	
	/**
	 * @Title: 根据jv230BundleId查询所有jv230 <br/>
	 * @param jv230s
	 * @param jv230BundleId
	 * @return List<Jv230PO>    返回类型 
	 */
	public List<Jv230PO> queryJv230ByBundleId(Collection<Jv230PO> jv230s, String jv230BundleId){
		
		List<Jv230PO> jv230POs = new ArrayList<Jv230PO>();
		for(Jv230PO jv230: jv230s){
			if(jv230.getBundleId().equals(jv230BundleId)){
				jv230POs.add(jv230);
			}
		}
		
		return jv230POs;
	}
	
	/**
	 * @Title: 查找设备组中的虚拟配置 
	 * @param group 设备组
	 * @return DeviceGroupConfigPO
	 */
	public DeviceGroupConfigPO queryVirtualConfig(DeviceGroupPO group){
		Set<DeviceGroupConfigPO> configs = group.getConfigs();
		for(DeviceGroupConfigPO config: configs){
			if(config.getType().equals(ConfigType.VIRTUAL)){
				return config;
			}
		}
		return null;
	}
	
	/**
	 * 查询设备组中的默认议程
	 * @Title: queryDefaultConfig 
	 * @param group 设备组信息
	 * @return DeviceGroupConfigPO
	 * @throws
	 */
	public DeviceGroupConfigPO queryDefaultConfig(DeviceGroupPO group){
		Set<DeviceGroupConfigPO> configs = group.getConfigs();
		for(DeviceGroupConfigPO config: configs){
			if(config.getType().equals(ConfigType.DEFAULT)){
				return config;
			}
		}
		return null;
	}
	
	/**
	 * 查询config中音频memberId数组
	 * @Title: queryConfigAudio 
	 * @param group 设备组信息
	 * @param config 配置信息
	 * @return List<Long>
	 * @throws
	 */
	public List<Long> queryConfigAudio(DeviceGroupConfigPO config) throws Exception{
		List<Long> audioMemberIds = new ArrayList<Long>();
		Set<DeviceGroupConfigAudioPO> audioSrcs = config.getAudios();
		for(DeviceGroupConfigAudioPO audioSrc: audioSrcs){
			audioMemberIds.add(audioSrc.getMemberId());	
		}
		return audioMemberIds;
	}
	
	/**
	 * 过滤连接中和未连通的memberId数组
	 * @Title: filterConnectIds 
	 * @param group 设备信息
	 * @param ids 设备memberId数组
	 * @return Set<Long> id数组
	 * @throws
	 */
	public Set<Long> filterConnectIds(DeviceGroupPO group, Collection<Long> ids) throws Exception{
		Set<Long> filterIds = new HashSet<Long>();
		for(Long id: ids){		
			DeviceGroupMemberPO member = queryMemberById(group, id);
			if(member.getMemberStatus().equals(MemberStatus.CONNECT)){
				filterIds.add(id);
			}
		}
		return filterIds;
	}
	
	/**
	 * 获取video中某一成员的目的
	 * @Title: queryDstByMember 
	 * @param video 视频信息
	 * @param member 成员信息
	 * @return DeviceGroupConfigVideoDstPO 目的信息
	 * @throws
	 */
	public DeviceGroupConfigVideoDstPO queryDstByMember(DeviceGroupConfigVideoPO video, DeviceGroupMemberPO member){
		
		Set<DeviceGroupConfigVideoDstPO> dsts = video.getDsts();
		if(dsts.size() > 0){
			for(DeviceGroupConfigVideoDstPO dst: dsts){
				if((dst.getType().equals(ForwardDstType.SCREEN) && dst.getBundleId().equals(member.getBundleId())) || (dst.getType().equals(ForwardDstType.ROLE) && dst.getRoleId().equals(member.getRoleId()))){
					return dst;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * 获取group中某一成员的目的<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月21日 下午8:14:19
	 */
	public List<DeviceGroupConfigVideoDstPO> queryDstByMember(DeviceGroupPO group, DeviceGroupMemberPO member){
		
		List<DeviceGroupConfigVideoDstPO> _dsts = new ArrayList<DeviceGroupConfigVideoDstPO>();
		
		Set<DeviceGroupConfigPO> configs = group.getConfigs();
		if(configs != null && configs.size() > 0){
			for(DeviceGroupConfigPO config: configs){
				Set<DeviceGroupConfigVideoPO> videos = config.getVideos();
				if(videos != null && videos.size() > 0){
					for(DeviceGroupConfigVideoPO video: videos){
						Set<DeviceGroupConfigVideoDstPO> dsts = video.getDsts();
						if(dsts != null && dsts.size() > 0){
							for(DeviceGroupConfigVideoDstPO dst: dsts){
								if((dst.getType().equals(ForwardDstType.SCREEN) && dst.getBundleId().equals(member.getBundleId()))){
									_dsts.add(dst);
								}
							}
						}
					}
				}
			}
		}
		
		return _dsts;
	}
	
	public UserBO queryUserById(Collection<UserBO> users, Long id){
		if(users == null) return null;
		for(UserBO user : users){
			if(user.getId().equals(id)){
				return user;
			}
		}
		return null;
	}
	
	public FolderBO queryFolderById(Collection<FolderBO> folders, Long id){
		if(folders == null) return null;
		for(FolderBO folder : folders){
			if(folder.getId().equals(id)){
				return folder;
			}
		}
		return null;
	}
	
	public FolderPO queryFolderPOById(Collection<FolderPO> folders, Long id){
		if(folders == null) return null;
		for(FolderPO folder : folders){
			if(folder.getId().equals(id)){
				return folder;
			}
		}
		return null;
	}
	
	public List<ChannelSchemeDTO> queryChannelDTOsByBundleId(Collection<ChannelSchemeDTO> channelDTOs, String bundleId){
		if(channelDTOs==null || bundleId==null) return null;
		List<ChannelSchemeDTO> channels = new ArrayList<ChannelSchemeDTO>();
		for(ChannelSchemeDTO channelDTO : channelDTOs){
			if(bundleId.equals(channelDTO.getBundleId())){
				channels.add(channelDTO);
			}
		}
		return channels;
	}
	
	public ChannelSchemeDTO queryChannelDTOsByBundleIdAndChannelId(Collection<ChannelSchemeDTO> channelDTOs, String bundleId, String channelId){
		if(channelDTOs==null || bundleId==null || channelId==null) return null;
		for(ChannelSchemeDTO channelDTO : channelDTOs){
			if(bundleId.equals(channelDTO.getBundleId()) && channelId.equals(channelDTO.getChannelId())){
				return channelDTO;
			}
		}
		return null;
	}

	public FolderUserMap queryUserMapByUserId(Collection<FolderUserMap> folderUserMaps, Long userId){
		if(folderUserMaps == null) return null;
		for(FolderUserMap folderUserMap : folderUserMaps){
			if(folderUserMap.getUserId().equals(userId)){
				return folderUserMap;
			}
		}
		return null;
	}
	
	public boolean isLdapUser(UserBO user, Collection<FolderUserMap> folderUserMaps){
		FolderUserMap folderUserMap = queryUserMapByUserId(folderUserMaps, user.getId());
		boolean result = isLdapUser(user, folderUserMap);
		return result;
	}
	
	public boolean isLdapUser(UserBO user, FolderUserMap folderUserMap){
		if(folderUserMap!=null && "ldap".equals(folderUserMap.getCreator())){
			return true;
		}
		return false;
	}
	
	public boolean isLdapBundle(BundlePO bundle){
		if(SOURCE_TYPE.EXTERNAL.equals(bundle.getSourceType())){
			return true;
		}
		return false;
	}
	
}

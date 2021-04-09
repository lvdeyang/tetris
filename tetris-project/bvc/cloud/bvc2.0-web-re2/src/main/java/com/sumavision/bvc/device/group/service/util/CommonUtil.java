package com.sumavision.bvc.device.group.service.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.ScreenRectTemplatePO;
import com.suma.venus.resource.pojo.ScreenSchemePO;
import com.sumavision.bvc.common.group.po.CommonAvtplGearsPO;
import com.sumavision.bvc.common.group.po.CommonAvtplPO;
import com.sumavision.bvc.common.group.po.CommonBusinessRolePO;
import com.sumavision.bvc.common.group.po.CommonConfigAudioPO;
import com.sumavision.bvc.common.group.po.CommonConfigPO;
import com.sumavision.bvc.common.group.po.CommonConfigVideoDstPO;
import com.sumavision.bvc.common.group.po.CommonConfigVideoPO;
import com.sumavision.bvc.common.group.po.CommonConfigVideoPositionPO;
import com.sumavision.bvc.common.group.po.CommonConfigVideoSrcPO;
import com.sumavision.bvc.common.group.po.CommonGroupPO;
import com.sumavision.bvc.common.group.po.CommonMemberChannelPO;
import com.sumavision.bvc.common.group.po.CommonMemberPO;
import com.sumavision.bvc.common.group.po.CommonMemberScreenPO;
import com.sumavision.bvc.common.group.po.CommonMemberScreenRectPO;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.enumeration.CodecParamType;
import com.sumavision.bvc.device.group.enumeration.ForwardDstType;
import com.sumavision.bvc.device.group.enumeration.ForwardMode;
import com.sumavision.bvc.device.group.enumeration.ForwardSrcType;
import com.sumavision.bvc.device.group.enumeration.GroupStatus;
import com.sumavision.bvc.device.group.enumeration.GroupType;
import com.sumavision.bvc.device.group.enumeration.MemberStatus;
import com.sumavision.bvc.device.group.enumeration.PictureType;
import com.sumavision.bvc.device.group.enumeration.PollingStatus;
import com.sumavision.bvc.device.group.enumeration.TransmissionMode;
import com.sumavision.bvc.device.group.exception.InitiatorNotAdministratorException;
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
import com.sumavision.bvc.device.group.po.DeviceGroupPO;
import com.sumavision.bvc.device.jv230.dao.CombineJv230DAO;
import com.sumavision.bvc.device.jv230.po.CombineJv230PO;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.bvc.system.dao.AvtplDAO;
import com.sumavision.bvc.system.dao.BusinessRoleDAO;
import com.sumavision.bvc.system.dao.ChannelNameDAO;
import com.sumavision.bvc.system.enumeration.AudioFormat;
import com.sumavision.bvc.system.enumeration.GearsLevel;
import com.sumavision.bvc.system.enumeration.Resolution;
import com.sumavision.bvc.system.enumeration.VideoFormat;
import com.sumavision.bvc.system.po.AvtplGearsPO;
import com.sumavision.bvc.system.po.AvtplPO;
import com.sumavision.bvc.system.po.BusinessRolePO;
import com.sumavision.bvc.system.po.ChannelNamePO;
import com.sumavision.tetris.commons.util.wrapper.HashSetWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

/**
 * @ClassName: 会议工具类 
 * @author wjw
 * @date 2018年12月20日 上午8:59:04
 */
@Service
public class CommonUtil {
	
	@Autowired
	private AvtplDAO avtplDao;
	
	@Autowired
	private BusinessRoleDAO businessRoleDAO;
	
	@Autowired
	private ChannelNameDAO channelNameDao;
	
	@Autowired
	private CombineJv230DAO combineJv230Dao;
	
	@Autowired
	private CommonQueryUtil commonQueryUtil;
	
	@Autowired
	private ResourceQueryUtil resourceQueryUtil;
	
	@Autowired
	private MeetingUtil meetingUtil;

	/**
	 * 参数模板生成</br>
	 * @Title: generateAvtpl 
	 * @param codecParamType 参数类型
	 * @param codecParam 具体参数
	 * @return AvtplPO 参数模板 
	 * @throws
	 */
	public AvtplPO generateAvtpl(String codecParamType, String codecParam) throws Exception{
		AvtplPO avtpl = new AvtplPO();
		if(codecParamType.equals(CodecParamType.DEFAULT.getName())){			
			String line;
			StringBuilder sBuilder = new StringBuilder();
			
			InputStream jsonFile = MeetingUtil.class.getResourceAsStream("/defaultCodecTempletes.json");
			BufferedReader bReader = new BufferedReader(new InputStreamReader(jsonFile));
			
			while((line = bReader.readLine()) != null){
				sBuilder.append(line);
			}
			
	        List<JSONObject> jsonArray = JSONArray.parseArray(sBuilder.toString(), JSONObject.class);
	        for(JSONObject jsonObject: jsonArray){
	        	if(jsonObject.getString("defaultType").equals(codecParam)){
	        		avtpl.setName(jsonObject.getString("name"));
	    			avtpl.setVideoFormat(VideoFormat.fromName(jsonObject.getString("videoFormat")));
	    			avtpl.setVideoFormatSpare(VideoFormat.fromName(jsonObject.getString("videoFormat2")));
	    			avtpl.setAudioFormat(AudioFormat.fromName(jsonObject.getString("audioFormat")));
	    			avtpl.setGears(new ArrayList<AvtplGearsPO>());
	    			
	    			List<JSONObject> gearJsons = JSONArray.parseArray(jsonObject.getString("gears"), JSONObject.class);
	    			for(JSONObject gearJson: gearJsons){
	    				AvtplGearsPO gear = new AvtplGearsPO();
	    				gear.setName(gearJson.getString("name"));
	    				gear.setVideoBitRate(gearJson.getString("videoBitRate"));
	    				gear.setVideoBitRateSpare(gearJson.getString("videoBitRate2"));
	    				gear.setVideoResolution(Resolution.fromName(gearJson.getString("VideoResolution")));
	    				gear.setVideoResolutionSpare(Resolution.fromName(gearJson.getString("VideoResolution2")));
	    				gear.setAudioBitRate(gearJson.getString("AudioBitRate"));
	    				gear.setLevel(GearsLevel.fromLevel(gearJson.getIntValue("level")));
	    				gear.setAvtpl(avtpl);
	    				avtpl.getGears().add(gear);
	    			}
	        	}
	        }
		}else if(codecParamType.equals(CodecParamType.ID.getName())){
			avtpl = avtplDao.findOne(Long.valueOf(codecParam));
		}else if(codecParamType.equals(CodecParamType.PARAM.getName())){
			JSONObject paramObject = JSONObject.parseObject(codecParam);
			avtpl.setName(paramObject.getString("name"));
			avtpl.setVideoFormat(VideoFormat.fromName(paramObject.getString("videoFormat")));
			avtpl.setVideoFormatSpare(VideoFormat.fromName(paramObject.getString("videoFormat2")));
			avtpl.setAudioFormat(AudioFormat.fromName(paramObject.getString("audioFormat")));
			avtpl.setGears(new ArrayList<AvtplGearsPO>());
			
			List<JSONObject> gearJsons = JSONArray.parseArray(paramObject.getString("gears"), JSONObject.class);
			for(JSONObject gearJson: gearJsons){
				AvtplGearsPO gear = new AvtplGearsPO();
				gear.setName(gearJson.getString("name"));
				gear.setVideoBitRate(gearJson.getString("videoBitRate"));
				gear.setVideoBitRateSpare(gearJson.getString("videoBitRate2"));
				gear.setVideoResolution(Resolution.fromName(gearJson.getString("VideoResolution")));
				gear.setVideoResolutionSpare(Resolution.fromName(gearJson.getString("VideoResolution2")));
				gear.setAudioBitRate(gearJson.getString("AudioBitRate"));
				gear.setLevel(GearsLevel.fromLevel(gearJson.getIntValue("level")));
				gear.setAvtpl(avtpl);
				avtpl.getGears().add(gear);
			}
		}
		return avtpl;
	}
	
	/**
	 * @Title: 生成指定合屏video
	 * @param video video信息
	 * @param members 所有多人通话成员
	 * @param positions 合屏信息 
	 * @throws
	 */
	public void generateVideo(CommonGroupPO group, CommonConfigVideoPO video, Set<CommonMemberPO> members, List<JSONObject> positions, List<JSONObject> dsts, List<ChannelNamePO> channelNames) throws Exception{
		
		for(JSONObject position: positions){	
			CommonConfigVideoPositionPO positionPO = new CommonConfigVideoPositionPO();
			positionPO.setSerialnum(position.getIntValue("serialNum"));
			positionPO.setH(position.getString("h"));
			positionPO.setW(position.getString("w"));
			positionPO.setX(position.getString("x"));
			positionPO.setY(position.getString("y"));
			positionPO.setSrcs(new ArrayList<CommonConfigVideoSrcPO>());
			
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
					CommonMemberPO member = commonQueryUtil.queryMemberPOByBundleId(members, srcString);
					CommonMemberChannelPO channel = queryVideoEncode1(member);
					if(channel != null){
						CommonConfigVideoSrcPO src = transferSrc(member, channel);
						src.setPosition(positionPO);
						positionPO.getSrcs().add(src);
					}
				}else if(position.getString("srcType").equals("CHANNEL")){
					String _bundleId = srcString.split("@@")[0];
					String _channelId = srcString.split("@@")[1];
					
					CommonMemberPO member = commonQueryUtil.queryMemberPOByBundleId(members, _bundleId);
					CommonMemberChannelPO channel = commonQueryUtil.queryMemberChannel(member, _channelId);
					
					CommonConfigVideoSrcPO src = transferSrc(member, channel);
					src.setPosition(positionPO);
					positionPO.getSrcs().add(src);
				}else if(position.getString("srcType").equals("ROLE")){
					Long _roleId = Long.valueOf(srcString.split("@@")[0]);
					String _channelType = srcString.split("@@")[1];
					
					CommonBusinessRolePO rolePO = commonQueryUtil.queryRoleById(group, _roleId);
					String channelName = commonQueryUtil.queryChannelNameByChannelType(channelNames, _channelType);
					CommonConfigVideoSrcPO src = transferSrc(rolePO, _channelType, channelName);
					src.setPosition(positionPO);
					positionPO.getSrcs().add(src);
				}
			}
			
			positionPO.setVideo(video);
			video.getPositions().add(positionPO);					
		}

		if(dsts == null){
			//默认配置加所有人（需要区分null和空）
			List<CommonMemberPO> connectMembers = queryConnectMembers(members);
			updateDsts(video, connectMembers);
		}else{
			if(dsts.size() > 0){
				for(JSONObject dst: dsts){
					if(dst.getString("dstType").equals("BUNDLE")){		
						
						CommonMemberPO member = commonQueryUtil.queryMemberPOByBundleId(members, dst.getString("dst"));
						CommonMemberScreenPO screen = queryMemberScreen1(member);
						CommonConfigVideoDstPO dstPO = transferDst(member, screen);
						dstPO.setVideo(video);
						video.getDsts().add(dstPO);
						
					}else if(dst.getString("dstType").equals("SCREEN")){
						String _bundleId = dst.getString("dst").split("@@")[0];
						String _screenId = dst.getString("dst").split("@@")[1];
						
						CommonMemberPO member = commonQueryUtil.queryMemberPOByBundleId(members, _bundleId);
						CommonMemberScreenPO screen = commonQueryUtil.queryMemberScreen(member, _screenId);
						CommonConfigVideoDstPO dstPO = transferDst(member, screen);
						dstPO.setVideo(video);
						video.getDsts().add(dstPO);
					}else if(dst.getString("dstType").equals("ROLE")){
						Long _roleId = Long.valueOf(dst.getString("dst").split("@@")[0]);
						String _screenId = dst.getString("dst").split("@@")[1];
						
						CommonBusinessRolePO rolePO = commonQueryUtil.queryRoleById(group, _roleId);
						
						CommonConfigVideoDstPO dstPO = new CommonConfigVideoDstPO();
						dstPO.setRoleId(_roleId);
						dstPO.setRoleName(rolePO.getName());
						dstPO.setType(ForwardDstType.ROLE);
						dstPO.setScreenId(_screenId);
						dstPO.setVideo(video);
						video.getDsts().add(dstPO);
					}
				}
			}
		}

	}
	
	/**
	 * @Title: updateDsts 更新目的，连通的源加入视频目的 <br/>
	 * @param video video信息
	 * @param members 连通的成员
	 * @return void 
	 * @throws
	 */
	public void updateDsts(CommonConfigVideoPO video, List<CommonMemberPO> members) throws Exception{
		for(CommonMemberPO member:members){
			if(!isDstsContainsMember(video.getDsts(), member)){
				CommonMemberScreenPO screen = queryMemberScreen1(member);
				CommonConfigVideoDstPO dst = transferDst(member ,screen);
				dst.setVideo(video);
				video.getDsts().add(dst);
			}
		}
	}
	
	/**
	 * @Title: removeAudio 删议程音频
	 * @param defaultConfig 议程信息
	 * @param members 新加成员信息
	 * @return void 
	 * @throws
	 */
	public void removeAudio(DeviceGroupConfigPO defaultConfig, Collection<DeviceGroupMemberPO> members) throws Exception{
		
		Set<DeviceGroupConfigAudioPO> audios = defaultConfig.getAudios();
		List<DeviceGroupConfigAudioPO> removeList = new ArrayList<DeviceGroupConfigAudioPO>();
		
		if(audios != null && audios.size()>0 && members != null && members.size()>0){
			for(DeviceGroupConfigAudioPO audio: audios){
				for(DeviceGroupMemberPO member: members){
					if(member.getId().equals(audio.getMemberId())){
						removeList.add(audio);
						break;
					}
				}
			}
			
			audios.removeAll(removeList);
		}
	}
	
	/**
	 * @Title: addAudio 加议程音频
	 * @param defaultConfig 议程信息
	 * @param members 新加成员信息
	 * @return void 
	 * @throws
	 */
	public void addAudio(CommonConfigPO defaultConfig, Collection<CommonMemberPO> members) throws Exception{
		
		for(CommonMemberPO member: members){
			CommonMemberChannelPO channel = queryAudioEncode1(member); 
			CommonConfigAudioPO audioSrc = new CommonConfigAudioPO();
			audioSrc.setBundleId(member.getBundleId());
			audioSrc.setBundleName(member.getBundleName());
			audioSrc.setChannelId(channel.getChannelId());
			audioSrc.setChannelName(channel.getChannelName());
			audioSrc.setLayerId(member.getLayerId());
			audioSrc.setMemberChannelId(channel.getId());
			audioSrc.setMemberId(member.getId());
			audioSrc.setConfig(defaultConfig);
			defaultConfig.getAudios().add(audioSrc);
		}
	}
	
	/**
	 * @Title: 判断某一成员是否已经加入视频目的中 <br/>
	 * @param dsts 目的列表
	 * @param member 成员
	 * @return boolean
	 * @throws
	 */
	public boolean isDstsContainsMember(Set<CommonConfigVideoDstPO> dsts, CommonMemberPO member){
		if(dsts != null && dsts.size() > 0){
			for(CommonConfigVideoDstPO dst: dsts){
				if(dst.getBundleId().equals(member.getBundleId())){
					return true;
				}
			}
		}	
		return false;
	}
	
	/**
	 * 生成议程音频
	 * @Title: generateAudio 
	 * @param config 议程信息
	 * @param members 成员信息
	 * @param audios 音频成员信息
	 * @return void    返回类型 
	 * @throws
	 */
	public void generateAudio(CommonConfigPO config, Set<CommonMemberPO> members, List<String> audios) throws Exception{
		
		if(audios == null){
			for(CommonMemberPO member: members){
				CommonMemberChannelPO channel = queryAudioEncode1(member); 
				CommonConfigAudioPO audioSrc = new CommonConfigAudioPO();
				audioSrc.setBundleId(member.getBundleId());
				audioSrc.setBundleName(member.getBundleName());
				audioSrc.setChannelId(channel.getChannelId());
				audioSrc.setChannelName(channel.getChannelName());
				audioSrc.setLayerId(member.getLayerId());
				audioSrc.setMemberChannelId(channel.getId());
				audioSrc.setMemberId(member.getId());
				audioSrc.setConfig(config);
				config.getAudios().add(audioSrc);
			}
		}else{
			if(audios.size() > 0){
				for(String audio: audios){
					for(CommonMemberPO member: members){
						if(member.getBundleId().equals(audio)){
							CommonMemberChannelPO channel = queryAudioEncode1(member); 
							CommonConfigAudioPO audioSrc = new CommonConfigAudioPO();
							audioSrc.setBundleId(member.getBundleId());
							audioSrc.setBundleName(member.getBundleName());
							audioSrc.setChannelId(channel.getChannelId());
							audioSrc.setChannelName(channel.getChannelName());
							audioSrc.setLayerId(member.getLayerId());
							audioSrc.setMemberChannelId(channel.getId());
							audioSrc.setMemberId(member.getId());
							audioSrc.setConfig(config);
							config.getAudios().add(audioSrc);
						}
					}
				}
			}
		}
	}
	
	/**
	 * @Title: 视频源转换：将成员信息转为视频源信息 <br/> 
	 * @param member 成员
	 * @return CommonConfigVideoSrcPO 视频源
	 * @throws
	 */
	public CommonConfigVideoSrcPO transferSrc(CommonMemberPO member, CommonMemberChannelPO channel) throws Exception{
		
		CommonConfigVideoSrcPO src = new CommonConfigVideoSrcPO();
		
		src.setType(ForwardSrcType.CHANNEL);
		src.setBundleId(channel.getBundleId());
		src.setBundleName(channel.getBundleName());
		src.setChannelId(channel.getChannelId());
		src.setChannelName(channel.getChannelName());
		src.setLayerId(member.getLayerId());
		src.setMemberChannelId(channel.getId());
		src.setMemberChannelName(channel.getName());
		src.setMemberId(member.getId());
		
		return src;
		
	}
	
	/**
	 * @Title: 视频源转换：将成员信息转为视频源信息 <br/> 
	 * @param member 成员
	 * @return CommonConfigVideoSrcPO 视频源
	 * @throws
	 */
	public CommonConfigVideoSrcPO transferSrc(CommonBusinessRolePO role, String channelType, String channelName) throws Exception{
		
		CommonConfigVideoSrcPO src = new CommonConfigVideoSrcPO();
		
		src.setType(ForwardSrcType.ROLE);
		src.setRoleId(role.getId());
		src.setRoleName(role.getName());
		src.setRoleChannelType(ChannelType.valueOf(channelType));
		src.setMemberChannelName(channelName);
		
		return src;
		
	}
	
	/**
	 * @Title: 视频目的转换：将成员信息转为视频目的信息 <br/> 
	 * @param member 成员
	 * @param screen 屏幕
	 * @return DeviceGroupConfigVideoDstPO 视频目的
	 * @throws
	 */
	public CommonConfigVideoDstPO transferDst(CommonMemberPO member, CommonMemberScreenPO screen) throws Exception{
		
		CommonConfigVideoDstPO dst = new CommonConfigVideoDstPO();
		
		dst.setBundleId(member.getBundleId());
		dst.setBundleName(member.getBundleName());
		dst.setBundleType(member.getBundleType());
		dst.setLayerId(member.getLayerId());
		dst.setMemberId(member.getId());
		dst.setType(ForwardDstType.SCREEN);
		dst.setScreenId(screen.getScreenId());
		dst.setMemberScreenId(screen.getId());
		dst.setMemberScreenName(screen.getName());
		
		return dst;
	}
	
	/**
	 * @Title: 查询设备的一个视频编码通道 <br/>
	 * @param member 成员设备
	 * @return DeviceGroupMemberChannelPO 视频编码通道 
	 * @throws
	 */
	public CommonMemberChannelPO queryVideoEncode1(CommonMemberPO member) throws Exception{
		Set<CommonMemberChannelPO> channels = member.getChannels();
		for(CommonMemberChannelPO channel: channels){
			if(channel.getType().isVideoEncode() && channel.getType().equals(ChannelType.VIDEOENCODE1)){
				return channel;
			}
		}
		return null;
	}
	
	/**
	 * @Title: 查询设备的一个视频解码通道 <br/>
	 * @param member 成员设备
	 * @return DeviceGroupMemberChannelPO 视频解码通道 
	 * @throws
	 */
	public DeviceGroupMemberChannelPO queryVideoDecode1(DeviceGroupMemberPO member) throws Exception{
		Set<DeviceGroupMemberChannelPO> channels = member.getChannels();
		for(DeviceGroupMemberChannelPO channel: channels){
			if(channel.getType().isVideoDecode() && channel.getType().equals(ChannelType.VIDEODECODE1)){
				return channel;
			}
		}
		return null;
	}
	
	/**
	 * @Title: 查询设备的一个音频编码通道 <br/>
	 * @param member 成员设备
	 * @return DeviceGroupMemberChannelPO 视频编码通道 
	 * @throws
	 */
	public CommonMemberChannelPO queryAudioEncode1(CommonMemberPO member) throws Exception{
		Set<CommonMemberChannelPO> channels = member.getChannels();
		for(CommonMemberChannelPO channel: channels){
			if(channel.getType().isAudioEncode() && channel.getType().equals(ChannelType.AUDIOENCODE1)){
				return channel;
			}
		}
		return null;
	}
	
	/**
	 * @Title: 查询设备的一个屏幕 <br/>
	 * @param member 成员设备
	 * @return DeviceGroupMemberScreenPO 视频解码通道 
	 * @throws
	 */
	public CommonMemberScreenPO queryMemberScreen1(CommonMemberPO member) throws Exception{
		Set<CommonMemberScreenPO> screens = member.getScreens();
		if(screens != null && screens.size() > 0){
			List<CommonMemberScreenPO> screenList = new ArrayList<CommonMemberScreenPO>(screens);
			Collections.sort(screenList, new CommonMemberScreenPO.ScreenComparatorFromPO());
			return screenList.get(0);
		}
		return null;
	}
	
	/**
	 * @Title: 找出联通的成员设备 <br/>
	 * @param members 所有成员设备
	 * @return List<DeviceGroupMemberPO> 所有连通的成员设备
	 * @throws
	 */
	public List<CommonMemberPO> queryConnectMembers(Set<CommonMemberPO> members) throws Exception{		
		List<CommonMemberPO> connectMembers = new ArrayList<CommonMemberPO>();
		for(CommonMemberPO member: members){
			if(member.getMemberStatus().equals(MemberStatus.CONNECT)){
				connectMembers.add(member);			}
		}		
		return connectMembers;
	}

	/**
	 * 设备组userId是否正确处理
	 * @Title: incorrectGroupUserIdHandle 
	 * @param group 设备组
	 * @param userId 用户id
	 * @param userName 用户名
	 * @return void
	 * @throws
	 */
	public void incorrectGroupUserIdHandle(DeviceGroupPO group, Long userId, String userName) throws Exception{
		
		if(!group.getUserId().equals(userId)){
			throw new InitiatorNotAdministratorException(userName);
		}
	}
	
	/**
	 * 获得默认会议编解码参数</br>
	 * @Title: getDefaultMeetingCodecParam 
	 * @return CommonAvtplPO 编解码参数 
	 * @throws
	 */
	public CommonAvtplPO generateDefaultMeetingCodecParam() throws Exception{
		AvtplPO sys_avtpl = meetingUtil.generateAvtpl(CodecParamType.DEFAULT.getName(), "MEETING1");
		List<AvtplGearsPO> sys_gears = sys_avtpl.getGears();
		CommonAvtplPO g_avtpl = new CommonAvtplPO().set(sys_avtpl);
		g_avtpl.setGears(new HashSet<CommonAvtplGearsPO>());
				
		CommonAvtplGearsPO g_gear = new CommonAvtplGearsPO();
		for(AvtplGearsPO sys_gear:sys_gears){
			if(sys_gear.getLevel().equals(GearsLevel.LEVEL_3)){
				g_gear.set(sys_gear);
				g_gear.setAvtpl(g_avtpl);
				g_avtpl.getGears().add(g_gear);
			}
		}
		return g_avtpl;
	}
	
	/**
	 * @throws Exception 
	 * 生成一个虚拟的CommonGroupPO用于关联议程CommonConfigPO</br>
	 * @Title: generateGroup 
	 * @return CommonGroupPO
	 * @throws
	 */
	public CommonGroupPO generateVirtualGroup(Long userId, String name, ForwardMode forwardMode) throws Exception{
		CommonGroupPO group = new CommonGroupPO();
		CommonAvtplPO g_avtpl = generateDefaultMeetingCodecParam();
		group.setAvtpl(g_avtpl);
		group.setType(GroupType.MEETING);
		group.setName(name);//虚拟设备组
		group.setRecord(false);
		group.setTransmissionMode(TransmissionMode.UNICAST);
		group.setForwardMode(forwardMode);
		group.setStatus(GroupStatus.STOP);
		group.setCurrentGearLevel(GearsLevel.LEVEL_3);
		
		return group;
	}
	
	/**
	 * 由bundleId生成CommonMemberPO</br>
	 * @Title: generateMembers 
	 * @return List<String> bundleId
	 * @throws
	 */
	public Set<CommonMemberPO> generateMembers(List<String> sourceBundleIds) throws Exception{
		
		Set<CommonMemberPO> members = new HashSet<CommonMemberPO>();
		//通道别名
		List<ChannelNamePO> channelNamePOs = channelNameDao.findAll();
		HashMap<String, String> channelAlias = new HashMap<String, String>();
		for(ChannelNamePO channelNamePO : channelNamePOs){
			channelAlias.put(channelNamePO.getChannelType(), channelNamePO.getName());
		}		
		
//		List<String> sourceBundleIds = JSONArray.parseArray(sourceList.toJSONString(), String.class);
		
		//资源查询设备
		List<BundlePO> bundles = resourceQueryUtil.queryAllBundlesByBundleIds(sourceBundleIds);
		List<ChannelSchemeDTO> channelDTOs = resourceQueryUtil.queryAllChannelsByBundleIds(sourceBundleIds);
		List<ScreenSchemePO> screens = resourceQueryUtil.queryScreensByBundleIds(sourceBundleIds);
		
		Set<String> screenIds = new HashSet<String>();
		for(ScreenSchemePO screen: screens){
			screenIds.add(screen.getScreenId());
		}
		List<ScreenRectTemplatePO> rects = resourceQueryUtil.queryRectsByScreenIds(screenIds);
		
		//查询大屏设备
		List<Long> combineJv230Ids = new ArrayList<Long>();
		for(String bundleId: sourceBundleIds){
			try {
				combineJv230Ids.add(Long.valueOf(bundleId));
			} catch (Exception e) {
				
			}
		}
		List<CombineJv230PO> combineJv230s = combineJv230Dao.findAll(combineJv230Ids);
		
		//处理combineJv230
		if(combineJv230s != null && combineJv230s.size()>0){
//			group.setCombineJv230s(new HashSetWrapper<CombineJv230PO>().addAll(combineJv230s).getSet());
			for(CombineJv230PO combineJv230: combineJv230s){
				
				CommonMemberPO member = new CommonMemberPO();

				member.setBundleId(combineJv230.getId().toString());
				member.setBundleName(combineJv230.getName());
				member.setFolderId(-2l);
				member.setBundleType(combineJv230.getModel());
				member.setVenusBundleType(combineJv230.getType());
//				member.setGroup(group);
				member.setMemberStatus(MemberStatus.DISCONNECT);
				member.setChannels(new HashSet<CommonMemberChannelPO>());
				
				CommonMemberChannelPO videoDecodeChannelPO = new CommonMemberChannelPO();
				videoDecodeChannelPO.setBundleId(combineJv230.getId().toString());
				videoDecodeChannelPO.setBundleName(combineJv230.getName());
				videoDecodeChannelPO.setBundleType(member.getBundleType());
				videoDecodeChannelPO.setVenusBundleType(member.getVenusBundleType());
				videoDecodeChannelPO.setChannelId("1");
				videoDecodeChannelPO.setChannelType("VenusVideoOut");
				videoDecodeChannelPO.setChannelName("虚拟视频解码");
				videoDecodeChannelPO.setType(ChannelType.VIDEODECODE1);
				
				String videoKeyType = videoDecodeChannelPO.getType().toString();
				videoDecodeChannelPO.setName(channelAlias.get(videoKeyType));
				
				videoDecodeChannelPO.setMember(member);
				member.getChannels().add(videoDecodeChannelPO);
				
				CommonMemberChannelPO audioDecodeChannelPO = new CommonMemberChannelPO();
				audioDecodeChannelPO.setBundleId(combineJv230.getId().toString());
				audioDecodeChannelPO.setBundleName(combineJv230.getName());
				audioDecodeChannelPO.setBundleType(member.getBundleType());
				audioDecodeChannelPO.setVenusBundleType(member.getVenusBundleType());
				audioDecodeChannelPO.setChannelId("2");
				
				audioDecodeChannelPO.setChannelType("VenusAudioOut");
				audioDecodeChannelPO.setChannelName("虚拟音频解码");
				audioDecodeChannelPO.setType(ChannelType.AUDIODECODE1);
				
				String audioKeyType = audioDecodeChannelPO.getType().toString();
				audioDecodeChannelPO.setName(channelAlias.get(audioKeyType));
				
				audioDecodeChannelPO.setMember(member);
				member.getChannels().add(audioDecodeChannelPO);
				
				CommonMemberScreenPO combineJv230Screen = new CommonMemberScreenPO();
				combineJv230Screen.setBundleId(combineJv230.getId().toString());
				combineJv230Screen.setScreenId("screen_1");
				combineJv230Screen.setName("屏幕1");
				combineJv230Screen.setMember(member);
				member.getScreens().add(combineJv230Screen);
				
//				group.getMembers().add(member);
				members.add(member);
			}
		}
		
		//处理非combineJv230
		if(bundles != null && bundles.size()>0){
			for(BundlePO bundle: bundles){
				CommonMemberPO member = new CommonMemberPO();
				member.setBundleId(bundle.getBundleId());
				member.setBundleName(bundle.getBundleName());
				member.setLayerId(bundle.getAccessNodeUid());
				member.setFolderId(bundle.getFolderId());
				member.setBundleType(bundle.getDeviceModel());
				member.setVenusBundleType(bundle.getBundleType());
				member.setMemberStatus(MemberStatus.DISCONNECT);
				member.setChannels(new HashSet<CommonMemberChannelPO>());
				member.setScreens(new HashSet<CommonMemberScreenPO>());
//				member.setGroup(group);
				
				//屏幕				
				for(ScreenSchemePO screen: screens){
					if(screen.getBundleId().equals(bundle.getBundleId())){
						CommonMemberScreenPO screenPO = new CommonMemberScreenPO();
						screenPO.setBundleId(screen.getBundleId());
						screenPO.setScreenId(screen.getScreenId());
						screenPO.setName("屏幕"+screen.getScreenId().split("_")[1]);
						screenPO.setMember(member);
						screenPO.setRests(new HashSet<CommonMemberScreenRectPO>());
						for(ScreenRectTemplatePO rect: rects){
							if(rect.getScreenId().equals(screen.getScreenId()) && rect.getDeviceModel().equals(screen.getDeviceModel())){
								CommonMemberScreenRectPO rectPO = new CommonMemberScreenRectPO();
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
				
				//通道
				List<ChannelSchemeDTO> channels = new ArrayList<ChannelSchemeDTO>();
				for(ChannelSchemeDTO channelDTO: channelDTOs){
					if(channelDTO.getBundleId().equals(bundle.getBundleId())){
						channels.add(channelDTO);
					}
				}
					
				List<ChannelSchemeDTO> videoOuts = new ArrayList<ChannelSchemeDTO>();
				List<ChannelSchemeDTO> videoIns = new ArrayList<ChannelSchemeDTO>();
				List<ChannelSchemeDTO> audioOuts = new ArrayList<ChannelSchemeDTO>();
				List<ChannelSchemeDTO> audioIns = new ArrayList<ChannelSchemeDTO>();
				for(ChannelSchemeDTO channel: channels){
					if("VenusVideoOut".equals(channel.getBaseType())){
						videoOuts.add(channel);
					}else if("VenusVideoIn".equals(channel.getBaseType())){
						videoIns.add(channel);
					}else if("VenusAudioOut".equals(channel.getBaseType())){
						audioOuts.add(channel);
					}else if("VenusAudioIn".equals(channel.getBaseType())){
						audioIns.add(channel);
					}
				}
				
				//排序
				Collections.sort(videoOuts, new CommonMemberChannelPO.ChannelComparatorFromDTO());
				Collections.sort(videoIns, new CommonMemberChannelPO.ChannelComparatorFromDTO());
				Collections.sort(audioOuts, new CommonMemberChannelPO.ChannelComparatorFromDTO());
				Collections.sort(audioIns, new CommonMemberChannelPO.ChannelComparatorFromDTO());
				
				for(int i=0; i<videoOuts.size(); i++){
					ChannelSchemeDTO videoOut = videoOuts.get(i);
					
					CommonMemberChannelPO channelPO = new CommonMemberChannelPO();
					channelPO.setBundleId(videoOut.getBundleId());
					channelPO.setBundleName(bundle.getBundleName());
					channelPO.setBundleType(bundle.getDeviceModel());
					channelPO.setVenusBundleType(bundle.getBundleType());
					channelPO.setChannelId(videoOut.getChannelId());
					channelPO.setChannelType(videoOut.getBaseType());
					channelPO.setChannelName(videoOut.getChannelName());
					channelPO.setType(ChannelType.fromChannelType(new StringBufferWrapper().append(channelPO.getChannelType()).append("-").append(i+1).toString()));
					
					channelPO.setName(channelAlias.get(channelPO.getType().toString()) != null? channelAlias.get(channelPO.getType().toString()): channelPO.getType().toString());
					
					channelPO.setMember(member);
					member.getChannels().add(channelPO);
				}
				
				for(int i=0; i<videoIns.size(); i++){
					ChannelSchemeDTO videoIn = videoIns.get(i);
					
					CommonMemberChannelPO channelPO = new CommonMemberChannelPO();
					channelPO.setBundleId(videoIn.getBundleId());
					channelPO.setBundleName(bundle.getBundleName());
					channelPO.setBundleType(bundle.getDeviceModel());
					channelPO.setVenusBundleType(bundle.getBundleType());
					channelPO.setChannelId(videoIn.getChannelId());
					channelPO.setChannelType(videoIn.getBaseType());
					channelPO.setChannelName(videoIn.getChannelName());
					channelPO.setType(ChannelType.fromChannelType(new StringBufferWrapper().append(channelPO.getChannelType()).append("-").append(i+1).toString()));
					
					channelPO.setName(channelAlias.get(channelPO.getType().toString()) != null? channelAlias.get(channelPO.getType().toString()): channelPO.getType().toString());
					
					channelPO.setMember(member);
					member.getChannels().add(channelPO);
				}
				
				for(int i=0; i<audioOuts.size(); i++){
					ChannelSchemeDTO audioOut = audioOuts.get(i);
					
					CommonMemberChannelPO channelPO = new CommonMemberChannelPO();
					channelPO.setBundleId(audioOut.getBundleId());
					channelPO.setBundleName(bundle.getBundleName());
					channelPO.setBundleType(bundle.getDeviceModel());
					channelPO.setVenusBundleType(bundle.getBundleType());
					channelPO.setChannelId(audioOut.getChannelId());
					channelPO.setChannelType(audioOut.getBaseType());
					channelPO.setChannelName(audioOut.getChannelName());
					channelPO.setType(ChannelType.fromChannelType(new StringBufferWrapper().append(channelPO.getChannelType()).append("-").append(i+1).toString()));
					
					channelPO.setName(channelAlias.get(channelPO.getType().toString()) != null? channelAlias.get(channelPO.getType().toString()): channelPO.getType().toString());
					
					channelPO.setMember(member);
					member.getChannels().add(channelPO);
				}
				
				for(int i=0; i<audioIns.size(); i++){
					ChannelSchemeDTO audioIn = audioIns.get(i);
					
					CommonMemberChannelPO channelPO = new CommonMemberChannelPO();
					channelPO.setBundleId(audioIn.getBundleId());
					channelPO.setBundleName(bundle.getBundleName());
					channelPO.setBundleType(bundle.getDeviceModel());
					channelPO.setVenusBundleType(bundle.getBundleType());
					channelPO.setChannelId(audioIn.getChannelId());
					channelPO.setChannelType(audioIn.getBaseType());
					channelPO.setChannelName(audioIn.getChannelName());
					channelPO.setType(ChannelType.fromChannelType(new StringBufferWrapper().append(channelPO.getChannelType()).append("-").append(i+1).toString()));
					
					channelPO.setName(channelAlias.get(channelPO.getType().toString()) != null? channelAlias.get(channelPO.getType().toString()): channelPO.getType().toString());
					
					channelPO.setMember(member);
					member.getChannels().add(channelPO);
				}
				
//				group.getMembers().add(member);
				members.add(member);
			}
		}
		return members;
	}
}

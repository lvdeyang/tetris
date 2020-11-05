package com.sumavision.bvc.device.group.service.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.enumeration.CodecParamType;
import com.sumavision.bvc.device.group.enumeration.ForwardDstType;
import com.sumavision.bvc.device.group.enumeration.ForwardSrcType;
import com.sumavision.bvc.device.group.enumeration.MemberStatus;
import com.sumavision.bvc.device.group.enumeration.PictureType;
import com.sumavision.bvc.device.group.enumeration.PollingStatus;
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
import com.sumavision.bvc.system.dao.AvtplDAO;
import com.sumavision.bvc.system.dao.BusinessRoleDAO;
import com.sumavision.bvc.system.enumeration.AudioFormat;
import com.sumavision.bvc.system.enumeration.GearsLevel;
import com.sumavision.bvc.system.enumeration.Resolution;
import com.sumavision.bvc.system.enumeration.VideoFormat;
import com.sumavision.bvc.system.po.AvtplGearsPO;
import com.sumavision.bvc.system.po.AvtplPO;
import com.sumavision.bvc.system.po.ChannelNamePO;

/**
 * @ClassName: 会议工具类 
 * @author wjw
 * @date 2018年12月20日 上午8:59:04
 */
@Service
public class MeetingUtil {
	
	@Autowired
	private AvtplDAO avtplDao;
	
	@Autowired
	private BusinessRoleDAO businessRoleDAO;
	
	@Autowired
	private QueryUtil queryUtil;

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
			
			boolean has = false;
	        List<JSONObject> jsonArray = JSONArray.parseArray(sBuilder.toString(), JSONObject.class);
	        for(JSONObject jsonObject: jsonArray){
	        	if(jsonObject.getString("defaultType").equals(codecParam)){
	        		avtpl.setName(jsonObject.getString("name"));
	    			avtpl.setVideoFormat(VideoFormat.fromName(jsonObject.getString("videoFormat")));
	    			avtpl.setVideoFormatSpare(VideoFormat.fromName(jsonObject.getString("videoFormat2")));
	    			avtpl.setAudioFormat(AudioFormat.fromName(jsonObject.getString("audioFormat")));
	    			avtpl.setGears(new HashSet<AvtplGearsPO>());
	    			
	    			List<JSONObject> gearJsons = JSONArray.parseArray(jsonObject.getString("gears"), JSONObject.class);
	    			for(JSONObject gearJson: gearJsons){
	    				AvtplGearsPO gear = new AvtplGearsPO();
	    				gear.setName(gearJson.getString("name"));
	    				gear.setVideoBitRate(gearJson.getString("videoBitRate"));
	    				gear.setVideoBitRateSpare(gearJson.getString("videoBitRate2"));
	    				gear.setVideoResolution(Resolution.fromName(gearJson.getString("VideoResolution")));
	    				gear.setVideoResolutionSpare(Resolution.fromName(gearJson.getString("VideoResolution2")));
	    				gear.setFps(gearJson.getString("fps"));
	    				gear.setAudioBitRate(gearJson.getString("AudioBitRate"));
	    				gear.setLevel(GearsLevel.fromLevel(gearJson.getIntValue("level")));
	    				gear.setAvtpl(avtpl);
	    				avtpl.getGears().add(gear);
	    			}
	    			has = true;
	    			break;
	        	}
	        }
	        if(!has) return null;
		}else if(codecParamType.equals(CodecParamType.ID.getName())){
			avtpl = avtplDao.findOne(Long.valueOf(codecParam));
		}else if(codecParamType.equals(CodecParamType.PARAM.getName())){
			JSONObject paramObject = JSONObject.parseObject(codecParam);
			avtpl.setName(paramObject.getString("name"));
			avtpl.setVideoFormat(VideoFormat.fromName(paramObject.getString("videoFormat")));
			avtpl.setVideoFormatSpare(VideoFormat.fromName(paramObject.getString("videoFormat2")));
			avtpl.setAudioFormat(AudioFormat.fromName(paramObject.getString("audioFormat")));
			avtpl.setGears(new HashSet<AvtplGearsPO>());
			
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
	public void generateVideo(DeviceGroupPO group, DeviceGroupConfigVideoPO video, Set<DeviceGroupMemberPO> members, List<JSONObject> positions, List<JSONObject> dsts, List<ChannelNamePO> channelNames) throws Exception{
		
		for(JSONObject position: positions){	
			DeviceGroupConfigVideoPositionPO positionPO = new DeviceGroupConfigVideoPositionPO();
			positionPO.setSerialnum(position.getIntValue("serialNum"));
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
					DeviceGroupMemberChannelPO channel = queryVideoEncode1(member); 
					if(channel != null){
						DeviceGroupConfigVideoSrcPO src = transferSrc(member, channel);
						src.setPosition(positionPO);
						positionPO.getSrcs().add(src);
					}
				}else if(position.getString("srcType").equals("CHANNEL")){
					String _bundleId = srcString.split("@@")[0];
					String _channelId = srcString.split("@@")[1];
					
					DeviceGroupMemberPO member = queryUtil.queryMemberPOByBundleId(members, _bundleId);
					DeviceGroupMemberChannelPO channel = queryUtil.queryMemberChannel(member, _channelId);
					
					DeviceGroupConfigVideoSrcPO src = transferSrc(member, channel);
					src.setPosition(positionPO);
					positionPO.getSrcs().add(src);
				}else if(position.getString("srcType").equals("ROLE")){
					Long _roleId = Long.valueOf(srcString.split("@@")[0]);
					String _channelType = srcString.split("@@")[1];
					
					DeviceGroupBusinessRolePO rolePO = queryUtil.queryRoleById(group, _roleId);
					String channelName = queryUtil.queryChannelNameByChannelType(channelNames, _channelType);
					DeviceGroupConfigVideoSrcPO src = transferSrc(rolePO, _channelType, channelName);
					src.setPosition(positionPO);
					positionPO.getSrcs().add(src);
				}
			}
			
			positionPO.setVideo(video);
			video.getPositions().add(positionPO);					
		}

		if(dsts == null){
			//默认配置加所有人（需要区分null和空）
			List<DeviceGroupMemberPO> connectMembers = queryConnectMembers(members);
			updateDsts(video, connectMembers);
		}else{
			if(dsts.size() > 0){
				for(JSONObject dst: dsts){
					if(dst.getString("dstType").equals("BUNDLE")){		
						
						DeviceGroupMemberPO member = queryUtil.queryMemberPOByBundleId(members, dst.getString("dst"));
						DeviceGroupMemberScreenPO screen = queryMemberScreen1(member);
						DeviceGroupConfigVideoDstPO dstPO = transferDst(member, screen);
						dstPO.setVideo(video);
						video.getDsts().add(dstPO);
						
					}else if(dst.getString("dstType").equals("SCREEN")){
						String _bundleId = dst.getString("dst").split("@@")[0];
						String _screenId = dst.getString("dst").split("@@")[1];
						
						DeviceGroupMemberPO member = queryUtil.queryMemberPOByBundleId(members, _bundleId);
						DeviceGroupMemberScreenPO screen = queryUtil.queryMemberScreen(member, _screenId);
						DeviceGroupConfigVideoDstPO dstPO = transferDst(member, screen);
						dstPO.setVideo(video);
						video.getDsts().add(dstPO);
					}else if(dst.getString("dstType").equals("ROLE")){
						Long _roleId = Long.valueOf(dst.getString("dst").split("@@")[0]);
						String _screenId = dst.getString("dst").split("@@")[1];
						
						DeviceGroupBusinessRolePO rolePO = queryUtil.queryRoleById(group, _roleId);
						
						DeviceGroupConfigVideoDstPO dstPO = new DeviceGroupConfigVideoDstPO();
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
	public void updateDsts(DeviceGroupConfigVideoPO video, List<DeviceGroupMemberPO> members) throws Exception{
		for(DeviceGroupMemberPO member:members){
			if(!isDstsContainsMember(video.getDsts(), member)){
				DeviceGroupMemberScreenPO screen = queryMemberScreen1(member);
				DeviceGroupConfigVideoDstPO dst = transferDst(member ,screen);
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
	public void addAudio(DeviceGroupConfigPO defaultConfig, Collection<DeviceGroupMemberPO> members) throws Exception{
		
		for(DeviceGroupMemberPO member: members){
			DeviceGroupMemberChannelPO channel = queryAudioEncode1(member); 
			DeviceGroupConfigAudioPO audioSrc = new DeviceGroupConfigAudioPO();
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
	public boolean isDstsContainsMember(Set<DeviceGroupConfigVideoDstPO> dsts, DeviceGroupMemberPO member){
		if(dsts != null && dsts.size() > 0){
			for(DeviceGroupConfigVideoDstPO dst: dsts){
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
	public void generateAudio(DeviceGroupConfigPO config, Set<DeviceGroupMemberPO> members, List<String> audios) throws Exception{
		
		if(audios == null){
			for(DeviceGroupMemberPO member: members){
				DeviceGroupMemberChannelPO channel = queryAudioEncode1(member); 
				DeviceGroupConfigAudioPO audioSrc = new DeviceGroupConfigAudioPO();
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
					for(DeviceGroupMemberPO member: members){
						if(member.getBundleId().equals(audio)){
							DeviceGroupMemberChannelPO channel = queryAudioEncode1(member); 
							DeviceGroupConfigAudioPO audioSrc = new DeviceGroupConfigAudioPO();
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
	 * @return DeviceGroupConfigVideoSrcPO 视频源
	 * @throws
	 */
	public DeviceGroupConfigVideoSrcPO transferSrc(DeviceGroupMemberPO member, DeviceGroupMemberChannelPO channel) throws Exception{
		
		DeviceGroupConfigVideoSrcPO src = new DeviceGroupConfigVideoSrcPO();
		
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
	 * @return DeviceGroupConfigVideoSrcPO 视频源
	 * @throws
	 */
	public DeviceGroupConfigVideoSrcPO transferSrc(DeviceGroupBusinessRolePO role, String channelType, String channelName) throws Exception{
		
		DeviceGroupConfigVideoSrcPO src = new DeviceGroupConfigVideoSrcPO();
		
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
	public DeviceGroupConfigVideoDstPO transferDst(DeviceGroupMemberPO member, DeviceGroupMemberScreenPO screen) throws Exception{
		
		DeviceGroupConfigVideoDstPO dst = new DeviceGroupConfigVideoDstPO();
		
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
	public DeviceGroupMemberChannelPO queryVideoEncode1(DeviceGroupMemberPO member) throws Exception{
		Set<DeviceGroupMemberChannelPO> channels = member.getChannels();
		for(DeviceGroupMemberChannelPO channel: channels){
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
	public DeviceGroupMemberChannelPO queryAudioEncode1(DeviceGroupMemberPO member) throws Exception{
		Set<DeviceGroupMemberChannelPO> channels = member.getChannels();
		for(DeviceGroupMemberChannelPO channel: channels){
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
	public DeviceGroupMemberScreenPO queryMemberScreen1(DeviceGroupMemberPO member) throws Exception{
		Set<DeviceGroupMemberScreenPO> screens = member.getScreens();
		if(screens != null && screens.size() > 0){
			List<DeviceGroupMemberScreenPO> screenList = new ArrayList<DeviceGroupMemberScreenPO>(screens);
			Collections.sort(screenList, new DeviceGroupMemberScreenPO.ScreenComparatorFromPO());
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
	public List<DeviceGroupMemberPO> queryConnectMembers(Set<DeviceGroupMemberPO> members) throws Exception{		
		List<DeviceGroupMemberPO> connectMembers = new ArrayList<DeviceGroupMemberPO>();
		for(DeviceGroupMemberPO member: members){
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
	
}

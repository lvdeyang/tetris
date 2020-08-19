package com.sumavision.bvc.device.group.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.ScreenRectTemplatePO;
import com.suma.venus.resource.pojo.ScreenSchemePO;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.ConnectBundleBO;
import com.sumavision.bvc.device.group.bo.DisconnectBundleBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.RecordSetBO;
import com.sumavision.bvc.device.group.dao.ChannelForwardDAO;
import com.sumavision.bvc.device.group.dao.CombineAudioDAO;
import com.sumavision.bvc.device.group.dao.CombineVideoDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigAudioDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigVideoDstDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigVideoSrcDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupMemberDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupRecordSchemeDAO;
import com.sumavision.bvc.device.group.enumeration.AudioOperationType;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.enumeration.ConfigType;
import com.sumavision.bvc.device.group.enumeration.ForwardMode;
import com.sumavision.bvc.device.group.enumeration.GroupStatus;
import com.sumavision.bvc.device.group.enumeration.GroupType;
import com.sumavision.bvc.device.group.enumeration.MemberStatus;
import com.sumavision.bvc.device.group.enumeration.ScreenLayout;
import com.sumavision.bvc.device.group.enumeration.TransmissionMode;
import com.sumavision.bvc.device.group.enumeration.VideoOperationType;
import com.sumavision.bvc.device.group.exception.DeviceGroupHasNotStartedException;
import com.sumavision.bvc.device.group.exception.DeviceGroupNameAlreadyExistedException;
import com.sumavision.bvc.device.group.exception.TplIsNullException;
import com.sumavision.bvc.device.group.po.ChannelForwardPO;
import com.sumavision.bvc.device.group.po.CombineAudioPO;
import com.sumavision.bvc.device.group.po.CombineAudioSrcPO;
import com.sumavision.bvc.device.group.po.CombineVideoPO;
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
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.MeetingUtil;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.device.group.service.util.ResourceQueryUtil;
import com.sumavision.bvc.device.jv230.dao.CombineJv230DAO;
import com.sumavision.bvc.device.jv230.dao.Jv230ChannelDAO;
import com.sumavision.bvc.device.jv230.dto.Jv230ChannelDTO;
import com.sumavision.bvc.device.jv230.po.CombineJv230PO;
import com.sumavision.bvc.device.jv230.po.Jv230ChannelPO;
import com.sumavision.bvc.device.jv230.po.Jv230PO;
import com.sumavision.bvc.meeting.logic.ExecuteBusinessReturnBO;
import com.sumavision.bvc.meeting.logic.ExecuteBusinessReturnBO.ResultDstBO;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.bvc.system.dao.BusinessRoleDAO;
import com.sumavision.bvc.system.dao.ChannelNameDAO;
import com.sumavision.bvc.system.dao.DictionaryDAO;
import com.sumavision.bvc.system.dao.RecordSchemeDAO;
import com.sumavision.bvc.system.dao.ScreenLayoutDAO;
import com.sumavision.bvc.system.dao.TplDAO;
import com.sumavision.bvc.system.enumeration.BusinessRoleSpecial;
import com.sumavision.bvc.system.enumeration.BusinessRoleType;
import com.sumavision.bvc.system.enumeration.GearsLevel;
import com.sumavision.bvc.system.enumeration.TplContentType;
import com.sumavision.bvc.system.po.AvtplGearsPO;
import com.sumavision.bvc.system.po.AvtplPO;
import com.sumavision.bvc.system.po.BusinessRolePO;
import com.sumavision.bvc.system.po.ChannelNamePO;
import com.sumavision.bvc.system.po.RecordSchemePO;
import com.sumavision.bvc.system.po.ScreenLayoutPO;
import com.sumavision.bvc.system.po.ScreenPositionPO;
import com.sumavision.bvc.system.po.TplContentPO;
import com.sumavision.bvc.system.po.TplPO;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.HashSetWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Transactional(rollbackFor = Exception.class)
@Service
public class MeetingServiceImpl {
	
	@Autowired
	private DeviceGroupDAO deviceGroupDao;
	
	@Autowired
	private ChannelNameDAO channelNameDao;
	
	@Autowired
	private TplDAO sysTplDao;
	
	@Autowired
	private BusinessRoleDAO sysBusinessRoleDao;
	
	@Autowired
	private ScreenLayoutDAO sysScreenLayoutDao;
	
	@Autowired
	private RecordSchemeDAO sysRecordSchemeDao;
	
	@Autowired
	private DictionaryDAO dictionaryDao;
	
	@Autowired
	private CombineJv230DAO combineJv230Dao;
	
	@Autowired
	private CombineVideoDAO combineVideoDao;
	
	@Autowired
	private CombineAudioDAO combineAudioDao;
	
	@Autowired
	private ChannelForwardDAO channelForwardDao;
	
	@Autowired
	private Jv230ChannelDAO jv230ChannelDao;
	
	@Autowired
	private com.sumavision.bvc.device.group.dao.DeviceGroupBusinessRoleDAO gBusinessRoleDao;
	
	@Autowired
	private DeviceGroupMemberDAO deviceGroupMemberDao;
	
	@Autowired
	private DeviceGroupConfigDAO deviceGroupConfigDao;
	
	@Autowired
	private DeviceGroupConfigVideoDstDAO deviceGroupConfigVideoDstDao;
	
	@Autowired
	private DeviceGroupConfigVideoSrcDAO deviceGroupConfigVideoSrcDao;
	
	@Autowired
	private DeviceGroupConfigAudioDAO deviceGroupConfigAudioDao;
	
	@Autowired
	private QueryUtil queryUtil;
	
	@Autowired
	private MeetingUtil meetingUtil;
	
	@Autowired
	private ResourceQueryUtil resourceQueryUtil;
	
	@Autowired
	private VideoServiceImpl videoServiceImpl;
	
	@Autowired
	private RoleServiceImpl roleServiceImpl;
	
	@Autowired
	private AudioServiceImpl audioServiceImpl;
	
	@Autowired
	private MultiplayerChatServiceImpl multiplayerChatServiceImpl;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	@Autowired
	private NumberOfMembersServiceImpl numberOfMembersServiceImpl;
	
	@Autowired
	private AutoBuildAgendaServiceImpl autoBuildAgendaServiceImpl;
	
	@Autowired
	private DeviceGroupRecordSchemeDAO deviceGroupRecordSchemeDao;
	
	@Autowired
	private ResourceService resourceService;
	
	/**
	 * openBundle回复处理：离线
	 * @Title: openBundleResponseOffline 
	 * @param groupUuid 会议唯一标识
	 * @param bundleId 设备标识
	 * @return void
	 * @throws
	 */
	public void openBundleResponseOffline(String groupUuid, String bundleId) throws Exception{
		
		LogicBO logic = new LogicBO();
		
		DeviceGroupPO group = deviceGroupDao.findByUuid(groupUuid);
		
		//处理参数模板
		DeviceGroupAvtplPO avtpl = group.getAvtpl();
		DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
		CodecParamBO codec = new CodecParamBO().set(avtpl, currentGear);
		
		DeviceGroupMemberPO member = queryUtil.queryMemberPOByBundleId(group.getMembers(), bundleId);
		
		if(member.getMemberStatus().equals(MemberStatus.CONNECTING)){
			
			if(group.getType().equals(GroupType.MULTIPLAYERAUDIO) || group.getType().equals(GroupType.MULTIPLAYERVIDEO)){
				
				List<String> bundleIds = new ArrayList<String>();
				bundleIds.add(bundleId);
				
				List<DeviceGroupMemberPO> members = new ArrayList<DeviceGroupMemberPO>();
				members.add(member);
				
				//多人通话按照拒绝处理
				logic.merge(multiplayerChatServiceImpl.updateGroupMember(group, bundleIds, false, false, false));
				
				logic.setDisconnectBundle(group, members, codec);
			
			}else if(group.getType().equals(GroupType.MEETING)){
			
				//大会按照自动接听处理
				logic.merge(updateGroupMemberStatus(group, bundleId, true, false));
				
			}
			
			executeBusiness.execute(logic, "openBundle回复处理--离线：");
		}
	}
	
	/**
	 * @Title: 接听/拒绝请求反馈  <br/> 
	 * @param group 设备组
	 * @param bundleId 反馈设备uuid
	 * @param accept 反馈是否接受（true/false）
	 * @return void
	 * @throws
	 */
	public void incomingCallResponse(DeviceGroupPO group, String bundleId, boolean accept) throws Exception{
		
		List<String> bundleIds = new ArrayList<String>();
		bundleIds.add(bundleId);
		
		if(group.getType().equals(GroupType.MULTIPLAYERAUDIO) || group.getType().equals(GroupType.MULTIPLAYERVIDEO)){
			
			multiplayerChatServiceImpl.updateGroupMember(group, bundleIds, accept, false, true);
		
		}else if(group.getType().equals(GroupType.MEETING)){
		
			updateGroupMemberStatus(group, bundleId, accept, true);
			
		}
	}
	
	/**
	 * @Title: 添加一个设备组(会议室)数据 <br/>
	 * @Description: 多次数据库操作需要事务<br/>
	 * @param name 设备组名称
	 * @param type 设备组类型
	 * @param transmissionMode 设备组发流类型
	 * @param forwardMode 设备组转发模式
	 * @param codecParamType 参数类型
	 * @param codecParam 参数值
	 * @param systemTplId 选择的会议模板id
	 * @param sourceList 会议成员
	 * @param chairmanId 主席bundleId
	 * @throws Exception 
	 * @return DeviceGroupPO 设备组
	 */
	public DeviceGroupPO save(
			Long userId,
			String username,
			String name,
			String type,
			String transmissionMode,
			String forwardMode,
			String codecParamType,
			String codecParam,
			Long systemTplId,
			JSONArray sourceList,
			String chairmanId
			) throws Exception{
		
		//会议模板校验
		if(codecParam==null || systemTplId==null){
			throw new TplIsNullException();
		}
		
		//人数校验
		numberOfMembersServiceImpl.checkOneMeeting(GroupType.fromName(type), sourceList.size());
		
		//设备组重名校验
		DeviceGroupPO existGroup = deviceGroupDao.findByName(name);
		if(existGroup!=null){
			throw new DeviceGroupNameAlreadyExistedException(name);
		}
		
		DeviceGroupPO group = new DeviceGroupPO();
		group.setName(name);
		group.setType(GroupType.fromName(type));
		group.setTransmissionMode(TransmissionMode.fromName(transmissionMode));
		group.setForwardMode(ForwardMode.fromName(forwardMode));
		
		group.setUserId(userId);
		group.setUserName(username);
		group.setCreatetime(new Date());
		
		group.setRecord(false);
		group.setStatus(GroupStatus.STOP);
		
		deviceGroupDao.save(group);
		
		group.setUpdateTime(new Date());
		
		//添加会议成员
		group.setMembers(new HashSet<DeviceGroupMemberPO>());
		
		//通道别名
		List<ChannelNamePO> channelNamePOs = channelNameDao.findAll();
		HashMap<String, String> channelAlias = new HashMap<String, String>();
		for(ChannelNamePO channelNamePO : channelNamePOs){
			channelAlias.put(channelNamePO.getChannelType(), channelNamePO.getName());
		}		
		
		List<String> sourceBundleIds = JSONArray.parseArray(sourceList.toJSONString(), String.class);
		
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
			group.setCombineJv230s(new HashSetWrapper<CombineJv230PO>().addAll(combineJv230s).getSet());
			for(CombineJv230PO combineJv230: combineJv230s){
				
				DeviceGroupMemberPO member = new DeviceGroupMemberPO();

				member.setBundleId(combineJv230.getId().toString());
				member.setBundleName(combineJv230.getName());
				member.setFolderId(-2l);
				member.setBundleType(combineJv230.getModel());
				member.setVenusBundleType(combineJv230.getType());
				member.setGroup(group);
				member.setMemberStatus(MemberStatus.DISCONNECT);
				member.setChannels(new HashSet<DeviceGroupMemberChannelPO>());
				
				DeviceGroupMemberChannelPO videoDecodeChannelPO = new DeviceGroupMemberChannelPO();
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
				
				DeviceGroupMemberChannelPO audioDecodeChannelPO = new DeviceGroupMemberChannelPO();
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
				
				DeviceGroupMemberScreenPO combineJv230Screen = new DeviceGroupMemberScreenPO();
				combineJv230Screen.setBundleId(combineJv230.getId().toString());
				combineJv230Screen.setScreenId("screen_1");
				combineJv230Screen.setName("屏幕1");
				combineJv230Screen.setMember(member);
				member.getScreens().add(combineJv230Screen);
				
				group.getMembers().add(member);
			}
		}
		
		//处理非combineJv230
		if(bundles != null && bundles.size()>0){
			for(BundlePO bundle: bundles){
				DeviceGroupMemberPO member = new DeviceGroupMemberPO();
				member.setBundleId(bundle.getBundleId());
				member.setBundleName(bundle.getBundleName());
				member.setLayerId(bundle.getAccessNodeUid());
				member.setFolderId(bundle.getFolderId());
				member.setBundleType(bundle.getDeviceModel());
				member.setVenusBundleType(bundle.getBundleType());
				member.setMemberStatus(MemberStatus.DISCONNECT);
				member.setChannels(new HashSet<DeviceGroupMemberChannelPO>());
				member.setScreens(new HashSet<DeviceGroupMemberScreenPO>());
				member.setGroup(group);
				
				//屏幕				
				for(ScreenSchemePO screen: screens){
					if(screen.getBundleId().equals(bundle.getBundleId())){
						DeviceGroupMemberScreenPO screenPO = new DeviceGroupMemberScreenPO();
						screenPO.setBundleId(screen.getBundleId());
						screenPO.setScreenId(screen.getScreenId());
						screenPO.setName("屏幕"+screen.getScreenId().split("_")[1]);
						screenPO.setMember(member);
						screenPO.setRests(new HashSet<DeviceGroupMemberScreenRectPO>());
						for(ScreenRectTemplatePO rect: rects){
							if(rect.getScreenId().equals(screen.getScreenId()) && rect.getDeviceModel().equals(screen.getDeviceModel())){
								DeviceGroupMemberScreenRectPO rectPO = new DeviceGroupMemberScreenRectPO();
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
				Collections.sort(videoOuts, new DeviceGroupMemberChannelPO.ChannelComparatorFromDTO());
				Collections.sort(videoIns, new DeviceGroupMemberChannelPO.ChannelComparatorFromDTO());
				Collections.sort(audioOuts, new DeviceGroupMemberChannelPO.ChannelComparatorFromDTO());
				Collections.sort(audioIns, new DeviceGroupMemberChannelPO.ChannelComparatorFromDTO());
				
				for(int i=0; i<videoOuts.size(); i++){
					ChannelSchemeDTO videoOut = videoOuts.get(i);
					
					DeviceGroupMemberChannelPO channelPO = new DeviceGroupMemberChannelPO();
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
					
					DeviceGroupMemberChannelPO channelPO = new DeviceGroupMemberChannelPO();
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
					
					DeviceGroupMemberChannelPO channelPO = new DeviceGroupMemberChannelPO();
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
					
					DeviceGroupMemberChannelPO channelPO = new DeviceGroupMemberChannelPO();
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
				
				group.getMembers().add(member);
			}
		}	
	
		//拷贝参数模板
		AvtplPO sys_avtpl = meetingUtil.generateAvtpl(codecParamType, codecParam);
		Set<AvtplGearsPO> sys_gears = sys_avtpl.getGears();
		com.sumavision.bvc.device.group.po.DeviceGroupAvtplPO g_avtpl = new com.sumavision.bvc.device.group.po.DeviceGroupAvtplPO().set(sys_avtpl);
		g_avtpl.setGears(new HashSet<com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO>());
		for(AvtplGearsPO sys_gear:sys_gears){
			com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO g_gear = new com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO().set(sys_gear);
			g_avtpl.getGears().add(g_gear);
			g_gear.setAvtpl(g_avtpl);
		}
		group.setAvtpl(g_avtpl);
		g_avtpl.setGroup(group);
		
		//拷贝会议模板
		TplPO sys_tpl = sysTplDao.findOne(systemTplId);
		group.setSystemTplId(sys_tpl.getId());
		group.setSystemTplName(sys_tpl.getName());
		
		Set<TplContentPO> contents = sys_tpl.getContents();
		Set<Long> ids = new HashSet<Long>();
		
		//拷贝业务角色
		for(TplContentPO content:contents){
			if(content.getType().equals(TplContentType.BUSINESSROLE)){
				ids.add(content.getContentId());
			}
		}
		List<BusinessRolePO> sys_roles = sysBusinessRoleDao.findAll(ids);
		Set<com.sumavision.bvc.device.group.po.DeviceGroupBusinessRolePO> g_roles = new HashSet<com.sumavision.bvc.device.group.po.DeviceGroupBusinessRolePO>();
		group.setRoles(new HashSet<com.sumavision.bvc.device.group.po.DeviceGroupBusinessRolePO>());
		for(BusinessRolePO sys_role:sys_roles){
			com.sumavision.bvc.device.group.po.DeviceGroupBusinessRolePO g_role = new com.sumavision.bvc.device.group.po.DeviceGroupBusinessRolePO().set(sys_role);
			g_roles.add(g_role);
		}
		
		//创建系统默认的角色
		//创建一个观众
		com.sumavision.bvc.device.group.po.DeviceGroupBusinessRolePO audience = new com.sumavision.bvc.device.group.po.DeviceGroupBusinessRolePO();
		audience.setName("观众");
		audience.setSpecial(BusinessRoleSpecial.AUDIENCE);
		audience.setType(BusinessRoleType.RECORDABLE);
		g_roles.add(audience);
		
		//创建一个主席
		com.sumavision.bvc.device.group.po.DeviceGroupBusinessRolePO chairman = new com.sumavision.bvc.device.group.po.DeviceGroupBusinessRolePO();
		chairman.setName("主席");
		chairman.setSpecial(BusinessRoleSpecial.CHAIRMAN);
		chairman.setType(BusinessRoleType.DEFAULT);
		g_roles.add(chairman);
		
		//创建三个发言人
		for(int i=1; i<=3; i++){
			com.sumavision.bvc.device.group.po.DeviceGroupBusinessRolePO spokesman = new com.sumavision.bvc.device.group.po.DeviceGroupBusinessRolePO();
			spokesman.setName(new StringBufferWrapper().append("发言人").append(i).toString());
			spokesman.setSpecial(BusinessRoleSpecial.SPOKESMAN);
			spokesman.setType(BusinessRoleType.DEFAULT);
			g_roles.add(spokesman);
		}
		
		//先保存一下获取数据库id
		gBusinessRoleDao.save(g_roles);
		
		for(com.sumavision.bvc.device.group.po.DeviceGroupBusinessRolePO g_role:g_roles){
			group.getRoles().add(g_role);
			g_role.setGroup(group);
		}
		
		//设备成员设为观众
		List<DeviceGroupBusinessRolePO> roles = queryUtil.queryRoleBySpecial(group, BusinessRoleSpecial.AUDIENCE);
		DeviceGroupBusinessRolePO audienceRole = roles.get(0);
		Set<DeviceGroupMemberPO> members = group.getMembers();
		for(DeviceGroupMemberPO member: members){
			member.setRoleId(audienceRole.getId());
			member.setRoleName(audienceRole.getName());
		}
		
		//根据chairmanId设主席
		if(chairmanId != null && chairmanId != ""){
			List<DeviceGroupBusinessRolePO> chairmanRoles = queryUtil.queryRoleBySpecial(group, BusinessRoleSpecial.CHAIRMAN);
			DeviceGroupBusinessRolePO chairmanRole = chairmanRoles.get(0);
			for(DeviceGroupMemberPO member: members){
				if(member.getBundleId().equals(chairmanId)){
					member.setRoleId(chairmanRole.getId());
					member.setRoleName(chairmanRole.getName());
				}
			}
		}

		//拷贝布局方案
		ids.clear();
		for(TplContentPO content:contents){
			if(content.getType().equals(TplContentType.SCREENLAYOUT)){
				ids.add(content.getContentId());
			}
		}
		List<ScreenLayoutPO> sys_layouts = sysScreenLayoutDao.findAll(ids);
		group.setLayouts(new HashSet<com.sumavision.bvc.device.group.po.DeviceGroupScreenLayoutPO>());
		for(ScreenLayoutPO sys_layout:sys_layouts){
			com.sumavision.bvc.device.group.po.DeviceGroupScreenLayoutPO g_Layout = new com.sumavision.bvc.device.group.po.DeviceGroupScreenLayoutPO().set(sys_layout);
			g_Layout.setPositions(new HashSet<com.sumavision.bvc.device.group.po.DeviceGroupScreenPositionPO>());
			g_Layout.setGroup(group);
			group.getLayouts().add(g_Layout);
			Set<ScreenPositionPO> sys_positions = sys_layout.getPositions();
			for(ScreenPositionPO sys_position:sys_positions){
				com.sumavision.bvc.device.group.po.DeviceGroupScreenPositionPO g_position = new com.sumavision.bvc.device.group.po.DeviceGroupScreenPositionPO().set(sys_position);
				g_position.setLayout(g_Layout);
				g_Layout.getPositions().add(g_position);
			}
		}
		
		//拷贝录制方案
		ids.clear();
		for(TplContentPO content:contents){
			if(content.getType().equals(TplContentType.RECORDSCHEME)){
				ids.add(content.getContentId());
			}
		}
		List<RecordSchemePO> sys_records = sysRecordSchemeDao.findAll(ids);
		group.setRecordSchemes(new HashSet<com.sumavision.bvc.device.group.po.DeviceGroupRecordSchemePO>());
		for(RecordSchemePO sys_record:sys_records){
			com.sumavision.bvc.device.group.po.DeviceGroupRecordSchemePO g_record = new com.sumavision.bvc.device.group.po.DeviceGroupRecordSchemePO().set(sys_record);
			g_record.setGroup(group);
			group.getRecordSchemes().add(g_record);
			BusinessRolePO sys_role = sys_record.getRole();
			for(com.sumavision.bvc.device.group.po.DeviceGroupBusinessRolePO g_role:group.getRoles()){
				if(g_role.getUuid().equals(sys_role.getUuid())){
					g_record.setRoleId(g_role.getId());
					g_record.setRoleName(g_role.getName());
					break;
				}
			}
		}
		//观众默认加入录制方案
		com.sumavision.bvc.device.group.po.DeviceGroupRecordSchemePO g_audience_record = new com.sumavision.bvc.device.group.po.DeviceGroupRecordSchemePO();
		g_audience_record.setUpdateTime(new Date());
		g_audience_record.setName("默认观众录制");
		g_audience_record.setRoleId(audienceRole.getId());
		g_audience_record.setRoleName(audienceRole.getName());
		g_audience_record.setGroup(group);
		group.getRecordSchemes().add(g_audience_record);
		
		//关联地区
		group.setDicRegionId("");
		group.setDicRegionContent("默认");
		
		//关联直播栏目
		group.setDicProgramId("<default>");
		group.setDicProgramContent("默认");
		
		//关联点播二级栏目
		group.setDicProgramId("<default>");
		group.setDicProgramContent("默认");
		
		//创建一个虚拟配置,用来处理虚拟源
		DeviceGroupConfigPO virtualConfig = new DeviceGroupConfigPO();
		virtualConfig.setName("唯一虚拟配置");
		virtualConfig.setType(ConfigType.VIRTUAL);
		virtualConfig.setVideos(new HashSet<DeviceGroupConfigVideoPO>());
		virtualConfig.setGroup(group);
		group.setConfigs(new HashSet<DeviceGroupConfigPO>());
		group.getConfigs().add(virtualConfig);
		
		if(forwardMode.equals(ForwardMode.ROLE.getName())){
			//TODO:资源创建虚拟资源，返回通道信息
			Set<DeviceGroupBusinessRolePO> groupRoles = group.getRoles();
			
			for(DeviceGroupBusinessRolePO role: groupRoles){
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
		}
		
		deviceGroupDao.save(group);
		
		return group;
	}
	
	/**
	 * @Title: 设备组启动<br/> 
	 * @Description: 打开并占用所有的设备组成员通道<br/> 
	 * @param groupId 设备组id
	 * @throws Exception
	 * @return 
	 */
	public DeviceGroupPO start(Long groupId, Long userId, String userName) throws Exception{
			
		DeviceGroupPO group = new DeviceGroupPO();
		
		synchronized (groupId) {
			
			group = deviceGroupDao.findOne(groupId);
			
			meetingUtil.incorrectGroupUserIdHandle(group, userId, userName);
						
			if(!GroupType.MONITOR.equals(group.getType())){
				//处理参数档位
				if (group.getCurrentGearLevel() == null) {
					group.setCurrentGearLevel(GearsLevel.LEVEL_3);
				}
				
				//处理参数模板
				DeviceGroupAvtplPO avtpl = group.getAvtpl();
				DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
				CodecParamBO codec = new CodecParamBO().set(avtpl, currentGear);
				
				Set<DeviceGroupMemberPO> members = group.getMembers();
				
				//人数校验
				int number = group.getMembers().size();
				numberOfMembersServiceImpl.checkOneMeeting(group.getType(), number);
				numberOfMembersServiceImpl.checkAllMeeting(group.getType(), number);
				
				//TODO 先暂时这么拿吧，这个地方性能不好
				List<DeviceGroupMemberPO> totalBundles = new ArrayList<DeviceGroupMemberPO>();
				List<DeviceGroupMemberChannelPO> totalChannels = new ArrayList<DeviceGroupMemberChannelPO>();
				for(DeviceGroupMemberPO member:members){
					if(group.getType().equals(GroupType.MONITOR)){
						continue;
					}
					if(!"combineJv230".equals(member.getBundleType())){
						if("ipc".equals(member.getBundleType())){
							totalChannels.addAll(member.getChannels());
							member.setMemberStatus(MemberStatus.CONNECT);
						}else{
							totalBundles.add(member);
						}
					}
				}

				//呼叫会议中所有的jv230(根据大屏分开呼)
				List<CombineJv230PO> combineJv230s = queryUtil.queryCombineJv230s(group);		
				if(combineJv230s!=null && combineJv230s.size()>0){
					for(CombineJv230PO combineJv230:combineJv230s){
						Set<Jv230ChannelPO> totalJv230Channels = new HashSet<Jv230ChannelPO>();
						Set<Jv230PO> bundles = combineJv230.getBundles();
						if(bundles!=null && bundles.size()>0){
							for(Jv230PO bundle:bundles){
								totalJv230Channels.addAll(bundle.getChannels());
							}
						}
						
						DeviceGroupMemberPO jv230Member = queryUtil.queryMemberPOByBundleId(members, combineJv230.getId().toString());
						
						if(group.getStatus().equals(GroupStatus.STOP) || jv230Member.getMemberStatus().equals(MemberStatus.DISCONNECT)){
							try {
								executeBusiness.execute(new LogicBO().setUserId(group.getUserId().toString()).setJv230Connect(totalJv230Channels), "呼叫"+combineJv230.getName()+"设备下的jv230：");
								jv230Member.setMemberStatus(MemberStatus.CONNECT);
							}catch (Exception e) {
								e.printStackTrace();
								jv230Member.setMemberStatus(MemberStatus.DISCONNECT);
							}
						}
					}
				}		
				
				//map用于快速查找DeviceGroupMemberPO
				HashMap<String, DeviceGroupMemberPO> uuidMemberMap = new HashMap<String, DeviceGroupMemberPO>();
				List<DeviceGroupMemberPO> connectBundles = new ArrayList<DeviceGroupMemberPO>();
				LogicBO connectBundlesLogic = new LogicBO();
				connectBundlesLogic.setUserId(group.getUserId().toString());
				//新参数，不需要锁定全部bundle
				connectBundlesLogic.setMustLockAllBundle(false);
				
				for(DeviceGroupMemberPO bundle: totalBundles){
					uuidMemberMap.put(bundle.getBundleId(), bundle);
					DeviceGroupBusinessRolePO role = queryUtil.queryRoleById(group, bundle.getRoleId());
					//“连接中”则不发命令
					if(!MemberStatus.CONNECTING.equals(bundle.getMemberStatus())){
						connectBundles.add(bundle);
						if(group.getForwardMode().equals(ForwardMode.ROLE)){
							connectBundlesLogic.setRoleForward(bundle, role, codec);
						}
					}
				}
				connectBundlesLogic.setConnectBundle(group, connectBundles, codec);
				try{
					
					//首次开会，需要计数+1
					if(GroupStatus.STOP.equals(group.getStatus())){
						List<ConnectBundleBO> connectBundleBOs = connectBundlesLogic.getConnectBundle();
						for(ConnectBundleBO connectBundle : connectBundleBOs){
							connectBundle.setBusinessType("vod");
							connectBundle.setOperateType("start");
						}				
					}
					
					ExecuteBusinessReturnBO executeBusinessReturnBO = executeBusiness.execute(connectBundlesLogic, "呼叫会议成员：");
					List<ResultDstBO> connectBundleResults = executeBusinessReturnBO.getConnectBundle();
					for(ResultDstBO connectBundleResult : connectBundleResults){
						//从map中取出锁定成功的DeviceGroupMemberPO
						DeviceGroupMemberPO bundle = uuidMemberMap.get(connectBundleResult.getBundleId());
						String bundleType = bundle.getBundleType();
						if("tvos".equals(bundleType) || "jv210".equals(bundleType) || "sip".equals(bundleType) || "proxy".equals(bundleType) || "jv220".equals(bundleType)){
							bundle.setMemberStatus(MemberStatus.CONNECT);
						}else{
							//如果已经CONNECT，则不修改
							if(!MemberStatus.CONNECT.equals(bundle.getMemberStatus())){
								bundle.setMemberStatus(MemberStatus.CONNECTING);
							}
						}
					}
				}catch (Exception e){
					e.printStackTrace();
				}
				
				//增加openRole,转发器用来切换节点--现在不通过openRole下发
//				LogicBO roleLogic = new LogicBO();
//				Set<DeviceGroupBusinessRolePO> roles = group.getRoles();
//				for(DeviceGroupBusinessRolePO role: roles){
//					if(role.getSpecial().equals(BusinessRoleSpecial.AUDIENCE) || role.getSpecial().equals(BusinessRoleSpecial.CUSTOM)){
//						List<DeviceGroupMemberPO> roleMembers = new ArrayList<DeviceGroupMemberPO>();
//						for(DeviceGroupMemberPO member: members){
//							if(member.getRoleId() != null && member.getRoleId().equals(role.getId())){
//								roleMembers.add(member);
//							}
//						}
//						roleLogic.merge(new LogicBO().setUserId(group.getUserId().toString())
//								 					 .setRole(role, roleMembers, codec));
//					}
//				}
				
				//重新呼叫：下转发
				LogicBO logic = new LogicBO().setUserId(group.getUserId().toString())
											 .setConnect(totalChannels, codec)
											 .setForward(group.getForwards(), codec);
				
				//虚拟源创建合屏
				DeviceGroupConfigPO virtualConfig = queryUtil.queryVirtualConfig(group);
				if(virtualConfig.getVideos() != null && virtualConfig.getVideos().size() > 0){
					for(DeviceGroupConfigVideoPO video:virtualConfig.getVideos()){
						logic.merge(videoServiceImpl.setCombineVideo(group, video, false, false, false));
					}
				}
				
				//调用逻辑层
				executeBusiness.execute(logic, "逻辑层交互：设备组启动");
			}
			
			//修改会议状态
			group.setStatus(GroupStatus.START);
			
			//保存设备组状态
			deviceGroupDao.save(group);		
		}
				
		return group;
	}
	
	/**
	 * @Title: 设备组停止
	 * @Description: 关闭所有的设备组成员通道<br/> 
	 * @param @param 设备组id--groupId
	 * @return DeviceGroupPO    返回类型 
	 * @throws Exception
	 */
	public DeviceGroupPO stop(Long groupId, Long userId, String userName) throws Exception{
		
		//协议数据
		LogicBO logic = new LogicBO();
		
		DeviceGroupPO group = deviceGroupDao.findOne(groupId);
		
		meetingUtil.incorrectGroupUserIdHandle(group, userId, userName);
		
		if(!GroupType.MONITOR.equals(group.getType())){
			//修改状态--1.议程停止 2.方案录制停止 
			Set<DeviceGroupConfigPO> configs = group.getConfigs();
			if(configs!=null && configs.size()>0){
				for(DeviceGroupConfigPO config:configs){
					if(ConfigType.AGENDA.equals(config.getType()) && config.getRun()==true){
						config.setRun(false);
					}else if(ConfigType.SCHEME.equals(config.getType())){
						Set<DeviceGroupConfigVideoPO> videos = config.getVideos();
						if(videos!=null && videos.size()>0){
							for(DeviceGroupConfigVideoPO video:videos){
								video.setRecord(false);
							}
						}
					}
				}
			}
			
			//处理参数模板
			DeviceGroupAvtplPO avtpl = group.getAvtpl();
			DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
			CodecParamBO codec = new CodecParamBO().set(avtpl, currentGear);
			
			//处理挂断
			Set<DeviceGroupMemberPO> members = group.getMembers();
			List<DeviceGroupMemberPO> totalBundles = new ArrayList<DeviceGroupMemberPO>();
			List<DeviceGroupMemberChannelPO> totalChannels = new ArrayList<DeviceGroupMemberChannelPO>();
			Set<Long> combineJv230Ids = new HashSet<Long>();
			for(DeviceGroupMemberPO member:members){
				//成员所有音频都关闭
				member.setOpenAudio(false);
				
				//已经DISCONNECT则不发挂断
				if(MemberStatus.DISCONNECT.equals(member.getMemberStatus())){
					continue;
				}
				
				member.setMemberStatus(MemberStatus.DISCONNECT);				
				if(!"combineJv230".equals(member.getBundleType())){
					if("ipc".equals(member.getBundleType())){
						totalChannels.addAll(member.getChannels());
					}else{
						totalBundles.add(member);
					}
				}else{
					combineJv230Ids.add(Long.valueOf(member.getBundleId()));
				}
			}
			
			//清理录制--协议
			logic.setRecordDel(new ArrayList<RecordSetBO>());
			List<RecordPO> records = queryUtil.queryRunRecords(group);
			if(records!=null && records.size()>0){
				for(RecordPO record:records){
					logic.getRecordDel().add(new RecordSetBO().setUuid(record.getUuid()));
					record.setRun(false);
				}
			}
			
			group.setRecord(false);
			
			//清除合屏
			List<CombineVideoPO> combineVideos = queryUtil.queryCombineVideosAsNewPointer(group);
			if(combineVideos != null){
				for(CombineVideoPO video:combineVideos){
					video.setGroup(null);
				}
				group.getCombineVideos().removeAll(combineVideos);
				combineVideoDao.deleteInBatch(combineVideos);
			}
			
			//清除混音
			List<CombineAudioPO> combineAudios = queryUtil.queryCombineAudiosAsNewPointer(group);
			if(combineAudios != null){
				for(CombineAudioPO audio:combineAudios){
					audio.setGroup(null);
				}
				group.getCombineAudios().removeAll(combineAudios);
				combineAudioDao.deleteInBatch(combineAudios);
			}
			
			//清除转发
			Set<ChannelForwardPO> forwards = group.getForwards();
			if(forwards != null){
				for(ChannelForwardPO forward:forwards){
					forward.setGroup(null);
				}
				group.getForwards().removeAll(forwards);
				channelForwardDao.deleteInBatch(forwards);
			}
			
			//获取所有的jv230通道
			List<Jv230ChannelDTO> jv230Channels = null;
			if(combineJv230Ids!=null && combineJv230Ids.size()>0) jv230Channels = jv230ChannelDao.findByCombineJv230Ids(combineJv230Ids);
			
			//处理协议
			logic.setUserId(group.getUserId().toString())
				 .setDisconnect(totalChannels, codec)
				 .setDisconnectBundle(group, totalBundles, codec)
				 .setJv230Disconnect(jv230Channels)
				 .setCombineVideoDel(combineVideos)
				 .setCombineAudioDel(combineAudios)
				 .deleteForward(forwards, codec);			
			
			//停会，计数-1
			if(GroupStatus.START.equals(group.getStatus())){
				List<DisconnectBundleBO> disconnectBundleBOs = logic.getDisconnectBundle();
				for(DisconnectBundleBO disconnectBundle : disconnectBundleBOs){
					disconnectBundle.setBusinessType("vod");
					disconnectBundle.setOperateType("stop");
				}
			}
			
			//修改会议状态
			group.setStatus(GroupStatus.STOP);
		}	
		
		//保存设备组状态
		deviceGroupDao.save(group);
		
		//调用逻辑层
		executeBusiness.execute(logic, "逻辑层交互：设备组停止");
		
		return group;
	}
	
	/**
	 * 添加成员
	 * @Title: addMembers 
	 * @param group 设备组信息
	 * @param addBundleIdList 成员bundleId数组
	 * @return DeviceGroupPO
	 * @throws
	 */
	public DeviceGroupPO addMembers(DeviceGroupPO _group, List<String> addBundleIdList) throws Exception{
		
		LogicBO logic = new LogicBO();
		Long groupId = _group.getId();
		
		synchronized (groupId) {
			
			DeviceGroupPO group = deviceGroupDao.findOne(groupId);
				
			//检查是否已经添加
			Set<String> bundleIdsSet = new HashSet<>(addBundleIdList);
			List<DeviceGroupMemberPO> checkMembers = deviceGroupMemberDao.findByGroupIdAndBundleIdIn(groupId, bundleIdsSet);
			if(checkMembers.size()>0){
				throw new BaseException(StatusCode.FORBIDDEN, "请不要重复添加");
			}
	
			if(group.getMembers() == null) group.setMembers(new HashSet<DeviceGroupMemberPO>());
			
			//人数校验
			numberOfMembersServiceImpl.checkOneMeetingAddMember(group, addBundleIdList.size());
			if(group.getStatus().equals(GroupStatus.START))	numberOfMembersServiceImpl.checkAllMeeting(group.getType(), addBundleIdList.size());
					
			//处理参数模板
			DeviceGroupAvtplPO avtpl = group.getAvtpl();
			DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
			CodecParamBO codec = new CodecParamBO().set(avtpl, currentGear);
			
			//资源查询设备
			List<BundlePO> bundles = resourceQueryUtil.queryAllBundlesByBundleIds(addBundleIdList);
			List<ChannelSchemeDTO> channelDTOs = resourceQueryUtil.queryAllChannelsByBundleIds(addBundleIdList);
			List<ScreenSchemePO> screens = resourceQueryUtil.queryScreensByBundleIds(addBundleIdList);
			
			Set<String> screenIds = new HashSet<String>();
			for(ScreenSchemePO screen: screens){
				screenIds.add(screen.getScreenId());
			}
			List<ScreenRectTemplatePO> rects = resourceQueryUtil.queryRectsByScreenIds(screenIds);
			
			//查询大屏设备
			List<Long> combineJv230Ids = new ArrayList<Long>();
			for(String bundleId: addBundleIdList){
				try {
					combineJv230Ids.add(Long.valueOf(bundleId));
				} catch (Exception e) {
					
				}
			}
			List<CombineJv230PO> combineJv230s = combineJv230Dao.findAll(combineJv230Ids);
			
			//获取角色--观众(添加的成员设为观众)
			List<DeviceGroupBusinessRolePO> roles = queryUtil.queryRoleBySpecial(group, BusinessRoleSpecial.AUDIENCE);
			DeviceGroupBusinessRolePO role = roles.get(0);
			
			//查询通道别名
			List<ChannelNamePO> channelNamePOs = channelNameDao.findAll();
			HashMap<String, String> channelAlias = new HashMap<String, String>();
			for(ChannelNamePO channelNamePO : channelNamePOs){
				channelAlias.put(channelNamePO.getChannelType(), channelNamePO.getName());
			}	
			
			//处理combineJv230
			if(combineJv230s != null && combineJv230s.size()>0){
				group.setCombineJv230s(new HashSetWrapper<CombineJv230PO>().addAll(combineJv230s).getSet());
				for(CombineJv230PO combineJv230: combineJv230s){
					
					DeviceGroupMemberPO member = new DeviceGroupMemberPO();
	
					member.setBundleId(combineJv230.getId().toString());
					member.setBundleName(combineJv230.getName());
					member.setFolderId(-2l);
					member.setBundleType(combineJv230.getModel());
					member.setVenusBundleType(combineJv230.getType());
					member.setMemberStatus(MemberStatus.DISCONNECT);
					member.setGroup(group);
					member.setChannels(new HashSet<DeviceGroupMemberChannelPO>());
					member.setScreens(new HashSet<DeviceGroupMemberScreenPO>());
					
					DeviceGroupMemberChannelPO videoDecodeChannelPO = new DeviceGroupMemberChannelPO();
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
					
					DeviceGroupMemberChannelPO audioDecodeChannelPO = new DeviceGroupMemberChannelPO();
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
					
					DeviceGroupMemberScreenPO combineJv230Screen = new DeviceGroupMemberScreenPO();
					combineJv230Screen.setBundleId(combineJv230.getId().toString());
					combineJv230Screen.setScreenId("screen_1");
					combineJv230Screen.setName("屏幕1");
					combineJv230Screen.setMember(member);
					member.getScreens().add(combineJv230Screen);
					
					group.getMembers().add(member);
				}
			}
			
			//处理非combineJv230
			if(bundles != null && addBundleIdList.size()>0){
				for(BundlePO bundle: bundles){
					DeviceGroupMemberPO member = new DeviceGroupMemberPO();
					member.setBundleId(bundle.getBundleId());
					member.setBundleName(bundle.getBundleName());
					member.setLayerId(bundle.getAccessNodeUid());
					member.setFolderId(bundle.getFolderId());
					member.setBundleType(bundle.getDeviceModel());
					member.setVenusBundleType(bundle.getBundleType());
					member.setMemberStatus(MemberStatus.DISCONNECT);
					member.setChannels(new HashSet<DeviceGroupMemberChannelPO>());
					member.setScreens(new HashSet<DeviceGroupMemberScreenPO>());
					member.setGroup(group);
					
					//屏幕				
					for(ScreenSchemePO screen: screens){
						if(screen.getBundleId().equals(bundle.getBundleId())){
							DeviceGroupMemberScreenPO screenPO = new DeviceGroupMemberScreenPO();
							screenPO.setBundleId(screen.getBundleId());
							screenPO.setScreenId(screen.getScreenId());
							screenPO.setName("屏幕"+screen.getScreenId().split("_")[1]);
							screenPO.setMember(member);
							screenPO.setRests(new HashSet<DeviceGroupMemberScreenRectPO>());
							for(ScreenRectTemplatePO rect: rects){
								if(rect.getScreenId().equals(screen.getScreenId()) && rect.getDeviceModel().equals(screen.getDeviceModel())){
									DeviceGroupMemberScreenRectPO rectPO = new DeviceGroupMemberScreenRectPO();
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
					Collections.sort(videoOuts, new DeviceGroupMemberChannelPO.ChannelComparatorFromDTO());
					Collections.sort(videoIns, new DeviceGroupMemberChannelPO.ChannelComparatorFromDTO());
					Collections.sort(audioOuts, new DeviceGroupMemberChannelPO.ChannelComparatorFromDTO());
					Collections.sort(audioIns, new DeviceGroupMemberChannelPO.ChannelComparatorFromDTO());
					
					for(int i=0; i<videoOuts.size(); i++){
						ChannelSchemeDTO videoOut = videoOuts.get(i);
						
						DeviceGroupMemberChannelPO channelPO = new DeviceGroupMemberChannelPO();
						channelPO.setBundleId(videoOut.getBundleId());
						channelPO.setBundleName(bundle.getBundleName());
						channelPO.setBundleType(bundle.getDeviceModel());
						channelPO.setVenusBundleType(bundle.getBundleType());
						channelPO.setChannelId(videoOut.getChannelId());
						channelPO.setChannelType(videoOut.getBaseType());
						channelPO.setChannelName(videoOut.getChannelName());
						channelPO.setType(ChannelType.fromChannelType(new StringBufferWrapper().append(channelPO.getChannelType()).append("-").append(i+1).toString()));
						
						channelPO.setName(channelAlias.get(channelPO.getType().toString()));
						
						channelPO.setMember(member);
						member.getChannels().add(channelPO);
					}
					
					for(int i=0; i<videoIns.size(); i++){
						ChannelSchemeDTO videoIn = videoIns.get(i);
						
						DeviceGroupMemberChannelPO channelPO = new DeviceGroupMemberChannelPO();
						channelPO.setBundleId(videoIn.getBundleId());
						channelPO.setBundleName(bundle.getBundleName());
						channelPO.setBundleType(bundle.getDeviceModel());
						channelPO.setVenusBundleType(bundle.getBundleType());
						channelPO.setChannelId(videoIn.getChannelId());
						channelPO.setChannelType(videoIn.getBaseType());
						channelPO.setChannelName(videoIn.getChannelName());
						channelPO.setType(ChannelType.fromChannelType(new StringBufferWrapper().append(channelPO.getChannelType()).append("-").append(i+1).toString()));
						
						channelPO.setName(channelAlias.get(channelPO.getType().toString()));
						
						channelPO.setMember(member);
						member.getChannels().add(channelPO);
					}
					
					for(int i=0; i<audioOuts.size(); i++){
						ChannelSchemeDTO audioOut = audioOuts.get(i);
						
						DeviceGroupMemberChannelPO channelPO = new DeviceGroupMemberChannelPO();
						channelPO.setBundleId(audioOut.getBundleId());
						channelPO.setBundleName(bundle.getBundleName());
						channelPO.setBundleType(bundle.getDeviceModel());
						channelPO.setVenusBundleType(bundle.getBundleType());
						channelPO.setChannelId(audioOut.getChannelId());
						channelPO.setChannelType(audioOut.getBaseType());
						channelPO.setChannelName(audioOut.getChannelName());
						channelPO.setType(ChannelType.fromChannelType(new StringBufferWrapper().append(channelPO.getChannelType()).append("-").append(i+1).toString()));
						
						channelPO.setName(channelAlias.get(channelPO.getType().toString()));
						
						channelPO.setMember(member);
						member.getChannels().add(channelPO);
					}
					
					for(int i=0; i<audioIns.size(); i++){
						ChannelSchemeDTO audioIn = audioIns.get(i);
						
						DeviceGroupMemberChannelPO channelPO = new DeviceGroupMemberChannelPO();
						channelPO.setBundleId(audioIn.getBundleId());
						channelPO.setBundleName(bundle.getBundleName());
						channelPO.setBundleType(bundle.getDeviceModel());
						channelPO.setVenusBundleType(bundle.getBundleType());
						channelPO.setChannelId(audioIn.getChannelId());
						channelPO.setChannelType(audioIn.getBaseType());
						channelPO.setChannelName(audioIn.getChannelName());
						channelPO.setType(ChannelType.fromChannelType(new StringBufferWrapper().append(channelPO.getChannelType()).append("-").append(i+1).toString()));
						
						channelPO.setName(channelAlias.get(channelPO.getType().toString()));
						
						channelPO.setMember(member);
						member.getChannels().add(channelPO);
					}
					
					group.getMembers().add(member);
				}
			}
			
			deviceGroupDao.save(group);
			
			if(group.getStatus().equals(GroupStatus.START)){
				Set<DeviceGroupMemberPO> allMembers = group.getMembers();
				
				//map用于快速查找DeviceGroupMemberPO
				HashMap<String, DeviceGroupMemberPO> uuidMemberMap = new HashMap<String, DeviceGroupMemberPO>();
				List<DeviceGroupMemberPO> connectBundles = new ArrayList<DeviceGroupMemberPO>();
				LogicBO connectBundlesLogic = new LogicBO();
				connectBundlesLogic.setUserId(group.getUserId().toString());
				//新参数，不需要锁定全部bundle
				connectBundlesLogic.setMustLockAllBundle(false);
				
				for(String bundleId: addBundleIdList){
					DeviceGroupMemberPO member = queryUtil.queryMemberPOByBundleId(allMembers, bundleId);
					
					if("combineJv230".equals(member.getBundleType())){
						for(CombineJv230PO combineJv230: combineJv230s){
							Set<Jv230ChannelPO> totalJv230Channels = new HashSet<Jv230ChannelPO>();
							Set<Jv230PO> jv230Bundles = combineJv230.getBundles();
							if(jv230Bundles!=null && jv230Bundles.size()>0){
								for(Jv230PO bundle:jv230Bundles){
									totalJv230Channels.addAll(bundle.getChannels());
								}
							}
							
							try {
								executeBusiness.execute(new LogicBO().setUserId(group.getUserId().toString()).setJv230Connect(totalJv230Channels), "呼叫"+combineJv230.getName()+"设备下的jv230：");
								
								member.setMemberStatus(MemberStatus.CONNECT);
							}catch (Exception e) {
								e.printStackTrace();
								member.setMemberStatus(MemberStatus.DISCONNECT);
							}
						}
					}else{
						if(group.getType().equals(GroupType.MEETING)){
							if(!"ipc".equals(member.getBundleType())){							
								//处理非ipc，connectBundle
								uuidMemberMap.put(member.getBundleId(), member);
								connectBundles.add(member);
							}else{
								//处理ipc类，connect
								List<DeviceGroupMemberChannelPO> connectChannels = new ArrayList<DeviceGroupMemberChannelPO>();
								connectChannels.addAll(member.getChannels());
								try{
									executeBusiness.execute(new LogicBO().setUserId(group.getUserId().toString()).setConnect(connectChannels, codec), "呼叫"+member.getBundleName()+"设备：");
									
									member.setMemberStatus(MemberStatus.CONNECT);
									
								}catch (Exception e){
									e.printStackTrace();
									member.setMemberStatus(MemberStatus.DISCONNECT);
								}
							}
						}
					}
				}
				
				connectBundlesLogic.setConnectBundle(group, connectBundles, codec);
				try{
					ExecuteBusinessReturnBO executeBusinessReturnBO = executeBusiness.execute(connectBundlesLogic, "呼叫会议成员：");
					List<ResultDstBO> connectBundleResults = executeBusinessReturnBO.getConnectBundle();
					for(ResultDstBO connectBundleResult : connectBundleResults){
						//从map中取出锁定成功的DeviceGroupMemberPO
						DeviceGroupMemberPO bundle = uuidMemberMap.get(connectBundleResult.getBundleId());
						String bundleType = bundle.getBundleType();
						if("jv210".equals(bundleType) || "sip".equals(bundleType) || "proxy".equals(bundleType) || "jv220".equals(bundleType)){
							bundle.setMemberStatus(MemberStatus.CONNECT);
						}else{
							//如果已经CONNECT，则不修改
							if(!MemberStatus.CONNECT.equals(bundle.getMemberStatus())){
								bundle.setMemberStatus(MemberStatus.CONNECTING);
							}
						}
					}
				}catch (Exception e){
					e.printStackTrace();
				}
				
				//设置观众观看
				for(DeviceGroupMemberPO member : connectBundles){
					logic.merge(roleServiceImpl.setRole(group, role, member, false, true, false));
				}
				
				deviceGroupDao.save(group);
				
				logic.setUserId(group.getUserId().toString());			
				executeBusiness.execute(logic, "添加成员：");
					
			}			
			return group;			
		}
	}
	
	/**
	 * 删除成员
	 * @Title: removeMembers 
	 * @param group 设备组信息
	 * @param removeBundleIds 删除成员的bundleId数组
	 * @return DeviceGroupPO 
	 * @throws
	 */
	public DeviceGroupPO removeMembers(DeviceGroupPO group, List<String> removeBundleIds) throws Exception{
		
		LogicBO logic = new LogicBO();
		
		//处理参数模板
		DeviceGroupAvtplPO avtpl = group.getAvtpl();
		DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
		CodecParamBO codec = new CodecParamBO().set(avtpl, currentGear);
		
		//查询会议中的所有拼接屏
		queryUtil.queryCombineJv230s(group);
		
		//成员
		Set<DeviceGroupMemberPO> members = group.getMembers();
		
		//配置
		Set<DeviceGroupConfigPO> configs = group.getConfigs();
		
		//转发
		Set<ChannelForwardPO> forwards = group.getForwards();
		
		//合屏
		Set<CombineVideoPO> combineVideos = group.getCombineVideos();
		
		//混音
		Set<CombineAudioPO> combineAudios = group.getCombineAudios();
		
		List<DeviceGroupMemberPO> needRemoveMembers = new ArrayList<DeviceGroupMemberPO>();
		Set<DeviceGroupConfigVideoPO> needUpdateVideos = new HashSet<DeviceGroupConfigVideoPO>(); 
		List<DeviceGroupConfigVideoDstPO> needRemoveDst = new ArrayList<DeviceGroupConfigVideoDstPO>();
		List<DeviceGroupConfigVideoSrcPO> needRemoveSrc = new ArrayList<DeviceGroupConfigVideoSrcPO>(); 
		List<DeviceGroupConfigAudioPO> needRemoveAudio = new ArrayList<DeviceGroupConfigAudioPO>();
		List<ChannelForwardPO> needRemoveForward = new ArrayList<ChannelForwardPO>();
		Set<Long> audioMemberIds = new HashSet<Long>();
		Set<Long> combineJv230Ids = new HashSet<Long>();
		
		List<DeviceGroupMemberPO> needDisconnectMembers = new ArrayList<DeviceGroupMemberPO>();
		List<DeviceGroupMemberChannelPO> needDisconnectChannels = new ArrayList<DeviceGroupMemberChannelPO>();
		
		if(combineAudios != null && combineAudios.size() > 0){
			for(CombineAudioPO combineAudio: combineAudios){
				Set<CombineAudioSrcPO> combineAudioSrcs = combineAudio.getSrcs();
				for(CombineAudioSrcPO combineAudioSrc: combineAudioSrcs){
					audioMemberIds.add(combineAudioSrc.getMemberId());
				}
			}
		}
		
		int oldAudioCount = audioMemberIds.size(); 
		
		for(String bundleId: removeBundleIds){
			DeviceGroupMemberPO member = queryUtil.queryMemberPOByBundleId(members, bundleId);
			Long memberId = member.getId();
			if("combineJv230".equals(member.getBundleType())){
				//大屏处理
				if(configs != null && configs.size() >0){
					for(DeviceGroupConfigPO config: configs){
						Set<DeviceGroupConfigVideoPO> videos = config.getVideos();
						if(videos != null && videos.size() > 0){
							for(DeviceGroupConfigVideoPO video: videos){
								Set<DeviceGroupConfigVideoDstPO> dsts = video.getDsts();
								if(dsts != null && dsts.size() > 0){
									for(DeviceGroupConfigVideoDstPO dst:dsts){
										if(dst.getMemberId()!=null && dst.getMemberId().equals(memberId)){
											
											//解关联
											dst.setVideo(video);
											needRemoveDst.add(dst);
										}
									}
									video.getDsts().removeAll(needRemoveDst);
								}						
							}
						}					
					}
				}
				
				combineJv230Ids.add(Long.valueOf(member.getBundleId()));
			}else{				
				//处理config
				if(configs != null && configs.size() >0){
					for(DeviceGroupConfigPO config: configs){
						Set<DeviceGroupConfigVideoPO> videos = config.getVideos();
						Set<DeviceGroupConfigAudioPO> audios = config.getAudios();
						if(videos != null && videos.size() > 0){
							for(DeviceGroupConfigVideoPO video: videos){
								Set<DeviceGroupConfigVideoPositionPO> positions = video.getPositions();
								Set<DeviceGroupConfigVideoDstPO> dsts = video.getDsts();
								if(positions != null && positions.size() > 0){
									for(DeviceGroupConfigVideoPositionPO position: positions){
										List<DeviceGroupConfigVideoSrcPO> srcs = position.getSrcs();
										for(DeviceGroupConfigVideoSrcPO src: srcs){
											if(src.getMemberId() != null && src.getMemberId().equals(memberId)){									
												
												//解关联
												src.setPosition(null);
													
												needRemoveSrc.add(src);
												needUpdateVideos.add(video);
											}else if(src.getRoleId() != null && src.getRoleId().equals(member.getRoleId())){
												needUpdateVideos.add(video);
											}
										}
										position.getSrcs().removeAll(needRemoveSrc);
									}
								}
								if(dsts != null && dsts.size() > 0){
									for(DeviceGroupConfigVideoDstPO dst:dsts){
										if(dst.getMemberId() != null && dst.getMemberId().equals(memberId)){
											
											//解关联
											dst.setVideo(video);
											needRemoveDst.add(dst);
										}
									}
									video.getDsts().removeAll(needRemoveDst);
								}						
							}
						}
						
						if(audios != null && audios.size() > 0){
							for(DeviceGroupConfigAudioPO audio:audios){
								if(audio.getMemberId() != null && audio.getMemberId().equals(memberId)){
									
									//解关联
									audio.setConfig(null);
									needRemoveAudio.add(audio);
								}
							}
							config.getAudios().removeAll(needRemoveAudio);
						}						
					}
				}	
				
				//处理混音
				audioMemberIds.remove(memberId);
				
				//处理转发
				if(forwards != null && forwards.size() > 0){
					for(ChannelForwardPO forward: forwards){
						if((forward.getMemberId() != null && forward.getMemberId().equals(memberId)) || (forward.getSourceMemberId() != null && forward.getSourceMemberId().equals(memberId))){
							forward.setGroup(null);
							needRemoveForward.add(forward);
						}
					}
					group.getForwards().removeAll(needRemoveForward);
				}
				
				if(group.getType().equals(GroupType.MEETING)){
					if(!"ipc".equals(member.getBundleType())){
						//已经DISCONNECT则不发挂断
						if(!MemberStatus.DISCONNECT.equals(member.getMemberStatus())){
							needDisconnectMembers.add(member);
						}
					}else{
						needDisconnectChannels.addAll(member.getChannels());
					}
				}
			}
			
			member.setGroup(null);
			needRemoveMembers.add(member);
		}
		group.getMembers().removeAll(needRemoveMembers);
		
		//挂断设备
		if(needDisconnectMembers!=null && needDisconnectMembers.size()>0){
			logic.merge(new LogicBO().setDisconnectBundle(group, needDisconnectMembers, codec));
		}
		if(needDisconnectChannels!= null && needDisconnectChannels.size()>0){
			logic.merge(new LogicBO().setDisconnect(needDisconnectChannels, codec));
		}
		
		//大屏处理
		if(combineJv230Ids!=null && combineJv230Ids.size()>0) {
			List<Jv230ChannelDTO> jv230Channels = null;
			jv230Channels = jv230ChannelDao.findByCombineJv230Ids(combineJv230Ids);
			logic.merge(new LogicBO().setJv230Disconnect(jv230Channels));
		}
		
		//修改合屏
		if(needUpdateVideos != null && needUpdateVideos.size() > 0 && combineVideos != null && combineVideos.size() > 0){
			for(DeviceGroupConfigVideoPO video: needUpdateVideos){
				for(CombineVideoPO combineVideo: combineVideos){
					if(combineVideo.getUuid().equals(video.getUuid())){
						logic.merge(videoServiceImpl.updateCombineVideo(group, video, false, false));
						break;
					}
				}
			}
		}
		
		//修改混音
		if(audioMemberIds != null && audioMemberIds.size() >= 0 && audioMemberIds.size() < oldAudioCount){
			logic.merge(audioServiceImpl.setGroupAudio(group, audioMemberIds, false, true, false));
		}
		
		//删除转发
		if(needRemoveForward != null && needRemoveForward.size() > 0){
			logic.merge(new LogicBO().deleteForward(needRemoveForward, codec));
		}
		
		//持久化
		deviceGroupMemberDao.deleteInBatch(needRemoveMembers);
		deviceGroupConfigVideoDstDao.deleteInBatch(needRemoveDst);
		deviceGroupConfigVideoSrcDao.deleteInBatch(needRemoveSrc);
		deviceGroupConfigAudioDao.deleteInBatch(needRemoveAudio);
		channelForwardDao.deleteInBatch(needRemoveForward);
		
		deviceGroupDao.save(group);
		
		logic.setUserId(group.getUserId().toString());
		
		//开会时要处理协议下发
		if(group.getStatus().equals(GroupStatus.START)) executeBusiness.execute(logic, "删除成员：");
		
		return group;	
	}
	
	/**
	 * 创建议程<br/>
	 * @Title: setAgenda 
	 * @param group 设备组信息
	 * @param agendaName 议程名
	 * @param remark 备注信息
	 * @param audio 议程音频信息
	 * @param videos 议程视频信息
	 * @return DeviceGroupConfigPO
	 * @throws
	 */
	public DeviceGroupConfigPO setAgenda(
					DeviceGroupPO group, 
					String agendaName, 
					String remark, 
					JSONObject audio, 
					List<JSONObject> videos,
					List<ChannelNamePO> channelNames) throws Exception{
		
		Set<DeviceGroupMemberPO> members = group.getMembers();
		
		DeviceGroupConfigPO agenda = new DeviceGroupConfigPO();
		
		agenda.setName(agendaName);
		agenda.setRemark(remark);
		agenda.setType(ConfigType.AGENDA);
		agenda.setRun(false);
		
		//音频
		String audioOperation = audio.getString("audioOperation");
		agenda.setAudioOperation(AudioOperationType.fromName(audioOperation));
		
		if(audioOperation.equals(AudioOperationType.CUSTOM.getName())){
			
			int volume = audio.getIntValue("volume");
			//TODO
			String audioMode = audio.getString("audioMode");
		    List<String> audioSrcs = JSONArray.parseArray(audio.getJSONArray("audioSrcs").toJSONString(), String.class);
		    
		    agenda.setVolume(volume);
		    agenda.setAudios(new HashSet<DeviceGroupConfigAudioPO>());
		    
		    //生成audio
		    meetingUtil.generateAudio(agenda, members, audioSrcs);	    
		}
		
		//视频
		agenda.setVideos(new HashSet<DeviceGroupConfigVideoPO>());
		for(JSONObject video: videos){
		
			String videoName = video.getString("videoName");
			//TODO 转发模式
			String videoMode = video.getString("videoMode");
			String websiteDraw = video.getString("layout");
			List<JSONObject> positions = JSONArray.parseArray(video.getJSONArray("positions").toJSONString(), JSONObject.class);
			List<JSONObject> dsts = JSONArray.parseArray(video.getJSONArray("dsts").toJSONString(), JSONObject.class);
			
			DeviceGroupConfigVideoPO videoPO = new DeviceGroupConfigVideoPO();
			
			videoPO.setName(videoName);
			videoPO.setLayout(ScreenLayout.SINGLE);
			videoPO.setVideoOperation(VideoOperationType.fromName(videoMode));
			videoPO.setWebsiteDraw(websiteDraw);
			videoPO.setUpdateTime(new Date());
			videoPO.setPositions(new HashSet<DeviceGroupConfigVideoPositionPO>());
			videoPO.setDsts(new HashSet<DeviceGroupConfigVideoDstPO>());
			
			//生成video
			meetingUtil.generateVideo(group, videoPO, members, positions, dsts, channelNames);
			
			videoPO.setConfig(agenda);
			agenda.getVideos().add(videoPO);
		}	
		
		agenda.setGroup(group);
		
		deviceGroupConfigDao.save(agenda);
		
		return agenda;
	}
	
	/**
	 * 设备是否接听通知请求处理（更新成员状态）<br/>
	 * @Title: updateGroupMemberStatus 
	 * @param groupUuid 设备组标识
	 * @param bundleId 成员bundleId
	 * @param accept true：接听/false：拒绝
	 * @return LogicBO
	 * @throws
	 */
	public LogicBO updateGroupMemberStatus(
			DeviceGroupPO _group, 
			String bundleId, 
			boolean accept,
			boolean doProtocol) throws Exception{
		
		LogicBO logic = new LogicBO();
		
		if(_group == null) throw new Exception("该设备组不存在！");

		logic.setUserId(_group.getUserId().toString());
		
		//TODO:暂时先这么写（并发）
		Long lockId = _group.getId();		
		synchronized (lockId) {

			//保证同步
			DeviceGroupPO group = deviceGroupDao.findOne(lockId);
			
			//处理参数模板
			DeviceGroupAvtplPO avtpl = group.getAvtpl();
			DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
			CodecParamBO codec = new CodecParamBO().set(avtpl, currentGear);
			
			//查询会议中的所有拼接屏
			queryUtil.queryCombineJv230s(group);
			
			//处理设备组成员
			Set<DeviceGroupMemberPO> members = group.getMembers();
			DeviceGroupMemberPO _member = queryUtil.queryMemberPOByBundleId(members, bundleId);
//			if(null == _member){
//				Thread.sleep(2000);
//				_member = deviceGroupMemberDao.findByGroupIdAndBundleId(group.getId(), bundleId);
//				if(null == _member){
//					System.out.println("deviceGroupMemberDao.findByGroupIdAndBundleId 2次都是null");
//				}else{
//					System.out.println("deviceGroupMemberDao.findByGroupIdAndBundleId 第2次查询到结果");
//				}
//			}
			
			//议程
			Set<DeviceGroupConfigPO> configs = group.getConfigs();
			
			//视频
			List<DeviceGroupConfigVideoPO> videos = new ArrayList<DeviceGroupConfigVideoPO>();
			for(DeviceGroupConfigPO config: configs){
				videos.addAll(config.getVideos());
			}
			
			//合屏
			Set<CombineVideoPO> combineVideos = group.getCombineVideos();
			
			if(accept){
				
				_member.setMemberStatus(MemberStatus.CONNECT);
				//接听处理：合屏不处理，转发处理，混音处理
				List<CombineVideoPO> dstContainBundleIdVideos = queryUtil.queryAllCombineVideoByMember(videos, combineVideos, _member);
				
				//找到观看的合屏
				if(dstContainBundleIdVideos.size() > 0){
					CombineVideoPO bundleIdLookCombineVideo = new CombineVideoPO();
					DeviceGroupConfigVideoPO bundleIdLookVideo = new DeviceGroupConfigVideoPO();
					
					Collections.sort(dstContainBundleIdVideos, new CombineVideoPO.DateComparatorFromPO());
					bundleIdLookCombineVideo = dstContainBundleIdVideos.get(0);
					for(DeviceGroupConfigVideoPO video: videos){
						if(bundleIdLookCombineVideo.getUuid().equals(video.getUuid())){
							bundleIdLookVideo = video;
						}
					}
					
					DeviceGroupConfigVideoDstPO bundleIdDst = queryUtil.queryDstByMember(bundleIdLookVideo, _member);
					
					//转发处理（不适合角色转发模式）
					if(bundleIdDst != null){
						if(ForwardMode.DEVICE.equals(group.getForwardMode())){
							logic.merge(videoServiceImpl.setVideoForwardAddScreen(group, bundleIdLookVideo, bundleIdLookCombineVideo, bundleIdDst.getScreenId(), _member, false));
						}else if(ForwardMode.ROLE.equals(group.getForwardMode())){
							DeviceGroupBusinessRolePO _role = queryUtil.queryRoleById(group, _member.getRoleId());
							logic.setRoleForward(_member, _role, codec);
						}
					}
				}
				
				//判断是否在音频列表（根据openAudio判断）
				if(_member.isOpenAudio()){
					List<Long> totalVoicedIds = queryUtil.queryAllVoicedIds(group);
					logic.merge(audioServiceImpl.setGroupAudio(group, totalVoicedIds, false, false, false));
				}else{
					//给该设备下一次音频（可能是会议中，新添加的设备）
					List<Long> totalVoicedIds = queryUtil.queryAllVoicedIds(group);
					audioServiceImpl.setGroupAudio(group, totalVoicedIds, false, false, false);
//					ChannelForwardPO newAudiochannelForwardPO = queryUtil.queryAudioForward(group, _member.getId());
//					if(null != newAudiochannelForwardPO){
//						List<ChannelForwardPO> audiosChannelForwardPOs = new ArrayList<ChannelForwardPO>();
//						audiosChannelForwardPOs.add(newAudiochannelForwardPO);
//						LogicBO logicTempBo = new LogicBO();
//						logicTempBo.setForward(audiosChannelForwardPOs, codec);
//						logic.merge(logicTempBo);
//					}
				}
				
				//已有的音视频转发（角色转发模式下不会查到视频）
				List<ChannelForwardPO> forwardPOs = queryUtil.queryForwardByMember(group, _member);
				logic.setForward(forwardPOs, codec);
				
				//添加会议中所有的转发（角色转发不需要）
				//logic.merge(logic.setForward(group.getForwards(), codec));
				
				if(doProtocol) executeBusiness.execute(logic, "设备：" + _member.getBundleName() + "接听：");
				
			}else{
				
				//拒绝处理:处理合屏,不处理混音，转发删除
				_member.setMemberStatus(MemberStatus.DISCONNECT);
				
				//查询在合屏中src包括member的video
				List<DeviceGroupConfigVideoPO> SrcContainBundleIdVideos = queryUtil.queryVideoContainsMemberByCombineVideo(videos, combineVideos, _member);
				
				//修改合屏
				if(SrcContainBundleIdVideos.size() > 0){
					for(DeviceGroupConfigVideoPO updateVideo: SrcContainBundleIdVideos){
						logic.merge(videoServiceImpl.setCombineVideo(group, updateVideo, false, false, false));
					}
				}
				
				//有转发则删除
				List<ChannelForwardPO> delForwards = queryUtil.queryForwardByMember(group, _member);
				if(delForwards.size() > 0){
					for(ChannelForwardPO forward: delForwards){
						forward.setGroup(null);
					}
					group.getForwards().removeAll(delForwards);
					
					//拒绝接听删转发不用下协议
					logic.deleteForward(delForwards, codec);
				}
								
				logic.setForward(group.getForwards(), codec);
				
				if(doProtocol) executeBusiness.execute(logic, "设备：" + _member.getBundleName() + "拒绝：");				
			}
			
			deviceGroupDao.save(group);
		}
		
		return logic;
	}
	
	/**
	 * 重新入会</br>
	 * @Title: reenterMeeting 
	 * @param groupUuid 会议标识
	 * @param bundleId 设备bundleId
	 * @return void  
	 * @throws
	 */
	public void reenterMeeting(String groupUuid, String bundleId) throws Exception{

		DeviceGroupPO group = deviceGroupDao.findByUuid(groupUuid);		
		if(group == null) throw new Exception("该会议不存在！");
		
		//会议是否开始
		if(GroupStatus.STOP.equals(group.getStatus())){
			throw new DeviceGroupHasNotStartedException(group.getId(), group.getName());
		}
		
		//是否会议成员	
		Set<DeviceGroupMemberPO> members = group.getMembers();
		boolean flag = false;
		for(DeviceGroupMemberPO member: members){
			if(member.getBundleId().equals(bundleId)){
				//如果加入会议已经接听，则不处理
				if(member.getMemberStatus().equals(MemberStatus.CONNECT)) return;
				flag = true;
				break;
			}
		}
		if(!flag) throw new Exception(bundleId + "不是会议成员!");	
		
		LogicBO logic = new LogicBO();
		
		Long lockId = group.getId();
		
		synchronized (lockId) {
			
			group = deviceGroupDao.findOne(lockId);
			
			//member需要重新拿
			DeviceGroupMemberPO member = queryUtil.queryMemberPOByBundleId(group.getMembers(), bundleId);
			
			//处理参数模板
			DeviceGroupAvtplPO avtpl = group.getAvtpl();
			DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
			CodecParamBO codec = new CodecParamBO().set(avtpl, currentGear);
			
			//查询会议中的所有拼接屏
			queryUtil.queryCombineJv230s(group);
			
			//议程
			Set<DeviceGroupConfigPO> configs = group.getConfigs();
			
			//视频
			List<DeviceGroupConfigVideoPO> videos = new ArrayList<DeviceGroupConfigVideoPO>();
			for(DeviceGroupConfigPO config: configs){
				videos.addAll(config.getVideos());
			}
			
			//合屏
			Set<CombineVideoPO> combineVideos = group.getCombineVideos();
			
			//重新呼叫，不带incomingcall,状态置为CONNECT
			ConnectBundleBO connect = new ConnectBundleBO().set(group, member, codec);
			logic.setConnectBundle(new ArrayList<ConnectBundleBO>());
			logic.getConnectBundle().add(connect);
			member.setMemberStatus(MemberStatus.CONNECT);
			
			//合屏
			List<DeviceGroupConfigVideoPO> needUpdateVideos = queryUtil.queryVideoContainsMemberByCombineVideo(videos, combineVideos, member);
			
			//修改合屏
			if(needUpdateVideos.size() > 0){
				for(DeviceGroupConfigVideoPO updateVideo: needUpdateVideos){
					logic.merge(videoServiceImpl.setCombineVideo(group, updateVideo, false, false, false));
				}
			}
			
			//混音--判断是否在音频列表（根据openAudio判断）
			if(member.isOpenAudio()){
				List<Long> totalVoicedIds = queryUtil.queryAllVoicedIds(group);
				logic.merge(audioServiceImpl.setGroupAudio(group, totalVoicedIds, false, false, false));
			}
			
			//转发(有无存在合屏观看)
			List<CombineVideoPO> dstContainBundleIdVideos = queryUtil.queryAllCombineVideoByMember(videos, combineVideos, member);
			
			//找到观看的合屏
			if(dstContainBundleIdVideos.size() > 0){
				CombineVideoPO bundleIdLookCombineVideo = new CombineVideoPO();
				DeviceGroupConfigVideoPO bundleIdLookVideo = new DeviceGroupConfigVideoPO();
				
				Collections.sort(dstContainBundleIdVideos, new CombineVideoPO.DateComparatorFromPO());
				bundleIdLookCombineVideo = dstContainBundleIdVideos.get(0);
				for(DeviceGroupConfigVideoPO video: videos){
					if(bundleIdLookCombineVideo.getUuid().equals(video.getUuid())){
						bundleIdLookVideo = video;
					}
				}
				
				DeviceGroupConfigVideoDstPO bundleIdDst = queryUtil.queryDstByMember(bundleIdLookVideo, member);
				
				//转发处理
				if(bundleIdDst != null){
					logic.merge(videoServiceImpl.setVideoForwardAddScreen(group, bundleIdLookVideo, bundleIdLookCombineVideo, bundleIdDst.getScreenId(), member, false));
				}
			}
			
			deviceGroupDao.save(group);

			logic.setUserId(group.getUserId().toString());
			executeBusiness.execute(logic, "设备：" + member.getBundleName() + "重新入会：");	
		}		
	}
	
	/**
	 * 成员主动挂断<br/>
	 * @Title: exitMeeting 
	 * @param groupUuid 设备组标识
	 * @param bundleId 成员bundleId
	 * @return void
	 * @throws
	 */
	public void exitMeeting(DeviceGroupPO group, String bundleId) throws Exception{
		
		LogicBO logic = new LogicBO(); 
		
		Long lockId = group.getId();
		
		synchronized (lockId) {
			
			group = deviceGroupDao.findOne(lockId);
			
			DeviceGroupMemberPO member = queryUtil.queryMemberPOByBundleId(group.getMembers(), bundleId);
			
			member.setMemberStatus(MemberStatus.DISCONNECT);
			
			//处理参数模板
			DeviceGroupAvtplPO avtpl = group.getAvtpl();
			DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
			CodecParamBO codec = new CodecParamBO().set(avtpl, currentGear);
			
			//查询会议中的所有拼接屏
			queryUtil.queryCombineJv230s(group);
			
			//议程
			Set<DeviceGroupConfigPO> configs = group.getConfigs();
			
			//视频
			List<DeviceGroupConfigVideoPO> videos = new ArrayList<DeviceGroupConfigVideoPO>();
			for(DeviceGroupConfigPO config: configs){
				videos.addAll(config.getVideos());
			}
			
			//合屏
			Set<CombineVideoPO> combineVideos = group.getCombineVideos();
			
			//混音
			Set<CombineAudioPO> combineAudios = group.getCombineAudios();
			
			//查询在合屏中src包括member的video
			List<DeviceGroupConfigVideoPO> SrcContainBundleIdVideos = queryUtil.queryVideoContainsMemberByCombineVideo(videos, combineVideos, member);
			
			//修改合屏
			if(SrcContainBundleIdVideos.size() > 0){
				for(DeviceGroupConfigVideoPO updateVideo: SrcContainBundleIdVideos){
					logic.merge(videoServiceImpl.setCombineVideo(group, updateVideo, false, false, false));
				}
			}
			
			//有转发则删除
			List<ChannelForwardPO> delForwards = queryUtil.queryForwardByMember(group, member);
			if(delForwards.size() > 0){
				for(ChannelForwardPO forward: delForwards){
					forward.setGroup(null);
				}
				group.getForwards().removeAll(delForwards);
				logic.deleteForward(delForwards, codec);
			}
			
			//查混音列表
			Set<CombineAudioSrcPO> combineAudioSrcList = new HashSet<CombineAudioSrcPO>();
			for(CombineAudioPO combineAudio: combineAudios){
				combineAudioSrcList.addAll(combineAudio.getSrcs());
			}
			
			boolean updateCombineAudio = false;
			if(combineAudioSrcList.size() > 0){
				for(CombineAudioSrcPO combineAudioSrc: combineAudioSrcList){
					if(combineAudioSrc.getBundleId().equals(member.getBundleId())){
						updateCombineAudio = true;
						break;
					}
				}
			}
			
			//判断是否在音频列表（根据openAudio判断）
			if(updateCombineAudio){
				List<Long> totalVoicedIds = queryUtil.queryAllVoicedIds(group);
				logic.merge(audioServiceImpl.setGroupAudio(group, totalVoicedIds, false, false, false));
			}
			
			if("ipc".equals(member.getBundleType())){
				logic.setDisconnect(member.getChannels(), codec);
			}else{
				List<DeviceGroupMemberPO> members = new ArrayList<DeviceGroupMemberPO>();
				members.add(member);
				logic.setDisconnectBundle(group, members, codec);
			}
			
			logic.setForward(group.getForwards(),codec);
			
			deviceGroupDao.save(group);

			logic.setUserId(group.getUserId().toString());
			executeBusiness.execute(logic, "设备：" + member.getBundleName() + "主动挂断：");	
		}
	}
	
	/**
	 * 在会议中新建角色<br/>
	 * @Title: saveNewRole 
	 * @param groupId
	 * @param name
	 * @param special
	 * @param type
	 * @return role DeviceGroupBusinessRolePO
	 * @throws
	 */
	public DeviceGroupBusinessRolePO saveNewRole(Long groupId, String name, String special, String type) throws Exception{
		DeviceGroupBusinessRolePO role = new DeviceGroupBusinessRolePO();
		DeviceGroupPO deviceGroupPO = deviceGroupDao.findOne(groupId);
		
		role.setGroup(deviceGroupPO);
		role.setName(name);
		role.setSpecial(BusinessRoleSpecial.fromName(special));
		role.setType(BusinessRoleType.fromName(type));
		role.setUpdateTime(new Date());
		
		//save以生成id
		gBusinessRoleDao.save(role);
		
		if(deviceGroupPO.getForwardMode().equals(ForwardMode.ROLE)){
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
		
		gBusinessRoleDao.save(role);
		
		return role;
	}
	
	/**
	 * 删除会议中的角色<br/>
	 * @Title: removeRole 
	 * @param groupId
	 * @param roleId
	 * @throws
	 */
	public void removeRole(Long groupId, Long roleId) throws Exception{
		DeviceGroupPO group = deviceGroupDao.findOne(groupId);
		DeviceGroupBusinessRolePO role = gBusinessRoleDao.findOne(roleId);
		
		if(group.getForwardMode().equals(ForwardMode.ROLE)){
			if(role.getSpecial().equals(BusinessRoleSpecial.AUDIENCE) || role.getSpecial().equals(BusinessRoleSpecial.CUSTOM)){

				if(role.getBundleId() != null){
					//解绑
					resourceService.unBindVirtualDev(role.getBundleId());
				}
			}
		}

		//成员解绑该角色
		Set<DeviceGroupMemberPO> members = group.getMembers();
		for(DeviceGroupMemberPO member: members){
			if(roleId.equals(member.getRoleId())){
				member.setRoleId(null);
				member.setRoleName(null);
			}
		}
		
		deviceGroupDao.save(group);
		
		deviceGroupRecordSchemeDao.deleteByRoleId(roleId);
		deviceGroupConfigVideoDstDao.deleteByRoleId(roleId);
		deviceGroupConfigVideoSrcDao.deleteByRoleId(roleId);
		gBusinessRoleDao.delete(roleId);
	}
	
	/**
	 * 修改会议中的角色<br/>
	 * @Title: updateRole 
	 * @param groupId
	 * @param roleId
	 * @param name
	 * @param special
	 * @param type
	 * @return role DeviceGroupBusinessRolePO
	 * @throws
	 */
	public DeviceGroupBusinessRolePO updateRole(Long groupId, Long roleId, String name, String special, String type) throws Exception{
		DeviceGroupPO group = deviceGroupDao.findOne(groupId);
		DeviceGroupBusinessRolePO role = gBusinessRoleDao.findOne(roleId);
		
		//新旧角色名称对比
		String oldName = role.getName();
		boolean update = true;
		if(oldName.equals(name)){
			update = false;
		}else{
			update = true;
		}
		
		role.setName(name);
		role.setSpecial(BusinessRoleSpecial.fromName(special));
		role.setType(BusinessRoleType.fromName(type));
		role.setUpdateTime(new Date());
		
		if(update){
			
			//会议成员角色更新
			Set<DeviceGroupMemberPO> members = group.getMembers();
			for(DeviceGroupMemberPO member: members){
				if(member.getRoleId() != null && member.getRoleId().equals(roleId)){
					member.setRoleName(name);
				}
			}
			
			//录制方案更新
			Set<DeviceGroupRecordSchemePO> recordSchemes = group.getRecordSchemes();
			for(DeviceGroupRecordSchemePO recordScheme: recordSchemes){
				if(recordScheme.getRoleId() != null && recordScheme.getRoleId().equals(roleId)){
					recordScheme.setRoleName(name);
				}
			}
			
			//视频源和目的更新
			List<DeviceGroupConfigVideoDstPO> updateDsts = new ArrayList<DeviceGroupConfigVideoDstPO>();
			List<DeviceGroupConfigVideoSrcPO> updateSrcs = new ArrayList<DeviceGroupConfigVideoSrcPO>();
			
			Set<DeviceGroupConfigPO> configs = group.getConfigs();
			for(DeviceGroupConfigPO config: configs){
				Set<DeviceGroupConfigVideoPO> videos = config.getVideos();
				for(DeviceGroupConfigVideoPO video: videos){
					Set<DeviceGroupConfigVideoDstPO> dsts = video.getDsts();
					updateDsts.addAll(dsts);
					Set<DeviceGroupConfigVideoPositionPO> positions = video.getPositions();
					for(DeviceGroupConfigVideoPositionPO position: positions){
						List<DeviceGroupConfigVideoSrcPO> srcs = position.getSrcs();
						updateSrcs.addAll(srcs);
					}
				}
			}
			
			for(DeviceGroupConfigVideoDstPO updateDst: updateDsts){
				if(updateDst.getRoleId() != null && updateDst.getRoleId().equals(roleId)){
					updateDst.setRoleName(name);
				}
			}
			for(DeviceGroupConfigVideoSrcPO updateSrc: updateSrcs){
				if(updateSrc.getRoleId() != null && updateSrc.getRoleId().equals(roleId)){
					updateSrc.setRoleName(name);
				}
			}
			
			deviceGroupDao.save(group);
		}		
		
		gBusinessRoleDao.save(role);
		
		return role;
	}
}

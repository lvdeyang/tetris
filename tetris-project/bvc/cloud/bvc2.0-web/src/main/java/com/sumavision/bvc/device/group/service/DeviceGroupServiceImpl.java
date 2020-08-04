package com.sumavision.bvc.device.group.service;

import java.util.ArrayList;
import java.util.Collection;
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
import com.suma.venus.resource.pojo.ExtraInfoPO;
import com.suma.venus.resource.pojo.ScreenRectTemplatePO;
import com.suma.venus.resource.pojo.ScreenSchemePO;
import com.suma.venus.resource.service.ExtraInfoService;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.config.Constant;
import com.sumavision.bvc.device.group.bo.AudioParamBO;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.ConnectBundleBO;
import com.sumavision.bvc.device.group.bo.DisconnectBundleBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.PassByBO;
import com.sumavision.bvc.device.group.bo.PassByContentBO;
import com.sumavision.bvc.device.group.bo.RecordSetBO;
import com.sumavision.bvc.device.group.bo.RecordSourceBO;
import com.sumavision.bvc.device.group.bo.VideoParamBO;
import com.sumavision.bvc.device.group.dao.ChannelForwardDAO;
import com.sumavision.bvc.device.group.dao.CombineAudioDAO;
import com.sumavision.bvc.device.group.dao.CombineVideoDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupAuthorizationDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigAudioDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigVideoDstDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigVideoSrcDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupMemberDAO;
import com.sumavision.bvc.device.group.dao.RecordDAO;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.enumeration.ConfigType;
import com.sumavision.bvc.device.group.enumeration.ForwardMode;
import com.sumavision.bvc.device.group.enumeration.GroupStatus;
import com.sumavision.bvc.device.group.enumeration.GroupType;
import com.sumavision.bvc.device.group.enumeration.MemberStatus;
import com.sumavision.bvc.device.group.enumeration.RecordToVodType;
import com.sumavision.bvc.device.group.enumeration.RecordType;
import com.sumavision.bvc.device.group.enumeration.SourceType;
import com.sumavision.bvc.device.group.enumeration.TransmissionMode;
import com.sumavision.bvc.device.group.exception.DeviceGroupAlreadyStartedException;
import com.sumavision.bvc.device.group.exception.DeviceGroupHasNotStartedException;
import com.sumavision.bvc.device.group.exception.DeviceGroupNameAlreadyExistedException;
import com.sumavision.bvc.device.group.exception.TplIsNullException;
import com.sumavision.bvc.device.group.po.ChannelForwardPO;
import com.sumavision.bvc.device.group.po.CombineAudioPO;
import com.sumavision.bvc.device.group.po.CombineAudioSrcPO;
import com.sumavision.bvc.device.group.po.CombineVideoPO;
import com.sumavision.bvc.device.group.po.CombineVideoPositionPO;
import com.sumavision.bvc.device.group.po.CombineVideoSrcPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAuthorizationMemberPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAuthorizationPO;
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
import com.sumavision.bvc.device.group.po.PublishStreamPO;
import com.sumavision.bvc.device.group.po.RecordPO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
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
import com.sumavision.bvc.meeting.logic.record.mims.MimsService;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.bvc.system.dao.AuthorizationDAO;
import com.sumavision.bvc.system.dao.AvtplDAO;
import com.sumavision.bvc.system.dao.BusinessRoleDAO;
import com.sumavision.bvc.system.dao.ChannelNameDAO;
import com.sumavision.bvc.system.dao.DictionaryDAO;
import com.sumavision.bvc.system.dao.RecordSchemeDAO;
import com.sumavision.bvc.system.dao.ScreenLayoutDAO;
import com.sumavision.bvc.system.dao.TplDAO;
import com.sumavision.bvc.system.enumeration.BusinessRoleSpecial;
import com.sumavision.bvc.system.enumeration.BusinessRoleType;
import com.sumavision.bvc.system.enumeration.DicType;
import com.sumavision.bvc.system.enumeration.GearsLevel;
import com.sumavision.bvc.system.enumeration.ServLevel;
import com.sumavision.bvc.system.enumeration.TplContentType;
import com.sumavision.bvc.system.po.AuthorizationMemberPO;
import com.sumavision.bvc.system.po.AuthorizationPO;
import com.sumavision.bvc.system.po.AvtplGearsPO;
import com.sumavision.bvc.system.po.AvtplPO;
import com.sumavision.bvc.system.po.BusinessRolePO;
import com.sumavision.bvc.system.po.ChannelNamePO;
import com.sumavision.bvc.system.po.DictionaryPO;
import com.sumavision.bvc.system.po.RecordSchemePO;
import com.sumavision.bvc.system.po.ScreenLayoutPO;
import com.sumavision.bvc.system.po.ScreenPositionPO;
import com.sumavision.bvc.system.po.TplContentPO;
import com.sumavision.bvc.system.po.TplPO;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashSetWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Transactional(rollbackFor = Exception.class)
@Service("com.sumavision.bvc.device.group.DeviceGroupServiceImpl")
public class DeviceGroupServiceImpl {

	@Autowired
	private DeviceGroupDAO deviceGroupDao;
	
	@Autowired
	private DeviceGroupMemberDAO deviceGroupMemberDAO;
	
	@Autowired
	private AvtplDAO sysAvtplDao;
	
	@Autowired
	private TplDAO sysTplDao;
	
	@Autowired
	private BusinessRoleDAO sysBusinessRoleDao;
	
	@Autowired
	private ScreenLayoutDAO sysScreenLayoutDao;
	
	@Autowired
	private RecordSchemeDAO sysRecordSchemeDao;
	
	@Autowired
	private com.sumavision.bvc.device.group.dao.DeviceGroupBusinessRoleDAO gBusinessRoleDao;
	
	@Autowired
	private CombineVideoDAO combineVideoDao;
	
	@Autowired
	private CombineAudioDAO combineAudioDao;
	
	@Autowired
	private ChannelForwardDAO channelForwardDao;
	
	@Autowired
	private DeviceGroupConfigAudioDAO deviceGroupConfigAudioDao;
	
	@Autowired
	private DeviceGroupConfigVideoSrcDAO deviceGroupConfigVideoSrcDao;
	
	@Autowired
	private DeviceGroupConfigVideoDstDAO deviceGroupConfigVideoDstDao;
	
	@Autowired
	private CombineJv230DAO combineJv230Dao;
	
	@Autowired
	private Jv230ChannelDAO jv230ChannelDao;
	
	@Autowired
	private RoleServiceImpl roleServiceImpl;
	
	@Autowired
	private RecordDAO recordDao;
	
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
	
	@Autowired
	private ChannelNameDAO channelNameDAO;
	
	@Autowired
	private DictionaryDAO dictionaryDao;
	
	@Autowired
	private ResourceQueryUtil resourceQueryUtil;
	
	@Autowired
	private ExtraInfoService extraInfoService;
	
	@Autowired
	private DictionaryDAO conn_dictionary;
	
	@Autowired
	private AuthorizationDAO authorizationDao;
	
	@Autowired
	private DeviceGroupAuthorizationDAO deviceGroupAuthorizationDao;
	
	@Autowired
	private AuthorizationServiceImpl authorizationServiceImpl;
	
	@Autowired
	private NumberOfMembersServiceImpl numberOfMembersServiceImpl;
	
	@Autowired
	private AutoBuildAgendaServiceImpl autoBuildAgendaServiceImpl;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private MimsService mimsService;
	
	@Autowired
	private AgendaServiceImpl agendaServiceImpl;
	
	/**
	 * @Title: 添加一个设备组数据 <br/>
	 * @Description: 多次数据库操作需要事务<br/>
	 * @param name 设备组名称
	 * @param type 设备组类型
	 * @param transmissionMode 设备组发流类型
	 * @param avtplId 选择的参数模板id
	 * @param systemTplId 选择的会议模板id
	 * @param region 会议地区
	 * @param classify 会议分类
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
			Long avtplId,
			Long systemTplId,
			Long authtplId,
			String regionId,
			String regionContent,
			String dicCategoryLiveId,
			String dicProgramId,
			String dicStorageLocationCode,
			String sourceList
			) throws Exception{
		
		//校验是否已建立直播栏目
		if(GroupType.MEETING.equals(GroupType.fromName(type))){
			List<DictionaryPO> dicPO = conn_dictionary.findByContentPrefixAndDicType("会议_", DicType.LIVE);
			if(dicPO.size() == 0){
//				throw new BaseException(StatusCode.FORBIDDEN, "请先建立“会议_”开头的直播栏目");
			}
		}else if(GroupType.MONITOR.equals(GroupType.fromName(type))){
			List<DictionaryPO> dicPO = conn_dictionary.findByContentPrefixAndDicType("监控_", DicType.LIVE);
			if(dicPO.size() == 0){
//				throw new BaseException(StatusCode.FORBIDDEN, "请先建立“监控_”开头的直播栏目");
			}
		}
		
		//会议模板校验
		if(avtplId==null || systemTplId==null){
			throw new TplIsNullException();
		}
		
		List<JSONObject> sourceArray = JSONArray.parseArray(sourceList, JSONObject.class);
		//人数校验
		numberOfMembersServiceImpl.checkOneMeeting(GroupType.fromName(type), sourceArray.size());
		
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
		
		List<ChannelNamePO> channelNamePOs = channelNameDAO.findAll();
		HashMap<String, String> channelAlias = new HashMap<String, String>();
		for(ChannelNamePO channelNamePO : channelNamePOs){
			channelAlias.put(channelNamePO.getChannelType(), channelNamePO.getName());
		}		
		
		for(JSONObject source: sourceArray){
			DeviceGroupMemberPO member = new DeviceGroupMemberPO();			
			JSONObject sourceParam = JSONObject.parseObject(source.getString("param"));
			
			member.setBundleId(source.getString("id"));
			member.setBundleName(source.getString("name"));
			member.setLayerId(sourceParam.getString("nodeUid"));
			member.setFolderId(sourceParam.getLong("folderId"));
			member.setBundleType(sourceParam.getString("bundleType"));
			member.setVenusBundleType(sourceParam.getString("venusBundleType"));
			member.setGroup(group);
			member.setChannels(new HashSet<DeviceGroupMemberChannelPO>());
			member.setScreens(new HashSet<DeviceGroupMemberScreenPO>());
			if("combineJv230".equals(member.getBundleType())){
				DeviceGroupMemberChannelPO videoDecodeChannelPO = new DeviceGroupMemberChannelPO();
				videoDecodeChannelPO.setBundleId(source.getString("id"));
				videoDecodeChannelPO.setBundleName(source.getString("name"));
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
				audioDecodeChannelPO.setBundleId(source.getString("id"));
				audioDecodeChannelPO.setBundleName(source.getString("name"));
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
				combineJv230Screen.setBundleId(source.getString("id"));
				combineJv230Screen.setScreenId("screen_1");
				combineJv230Screen.setName("屏幕1");
				combineJv230Screen.setMember(member);
				member.getScreens().add(combineJv230Screen);
			}else{
				//屏幕screen
				List<JSONObject> screenArray = JSONArray.parseArray(source.getString("screens"), JSONObject.class);
				if(screenArray != null && screenArray.size()>0){
					for(JSONObject screen: screenArray){
						JSONObject screenParam = JSONObject.parseObject(screen.getString("param"));
						DeviceGroupMemberScreenPO screenPo = new DeviceGroupMemberScreenPO();
						screenPo.setBundleId(screenParam.getString("bundleId"));
						screenPo.setName(screen.getString("name"));
						screenPo.setScreenId(screenParam.getString("screenId"));
						screenPo.setMember(member);
						screenPo.setRests(new HashSet<DeviceGroupMemberScreenRectPO>());
						
						List<JSONObject> rectArray = JSONArray.parseArray(screenParam.getString("rects"), JSONObject.class);
						if(rectArray != null && rectArray.size()>0){
							for(JSONObject rect: rectArray){
								DeviceGroupMemberScreenRectPO rectPO = new DeviceGroupMemberScreenRectPO();
								rectPO.setBundleId(screenPo.getBundleId());
								rectPO.setScreenId(rect.getString("screenId"));
								rectPO.setParam(rect.getString("param"));
								rectPO.setChannel(rect.getString("channel"));
								rectPO.setRectId(rect.getString("rectId"));
								rectPO.setScreen(screenPo);
								screenPo.getRests().add(rectPO);
							}
						}
						
						member.getScreens().add(screenPo);
					}
				}
				
				//通道channel
				List<JSONObject> channelArray = JSONArray.parseArray(source.getString("children"), JSONObject.class);
				List<JSONObject> videoOut = new ArrayList<JSONObject>();
				List<JSONObject> videoIn = new ArrayList<JSONObject>();
				List<JSONObject> audioOut = new ArrayList<JSONObject>();
				List<JSONObject> audioIn = new ArrayList<JSONObject>();
				for(JSONObject channel:channelArray){
					JSONObject channelParam = JSONObject.parseObject(channel.getString("param"));
					if("VenusVideoOut".equals(channelParam.getString("channelType"))){
						videoOut.add(channel);
					}else if("VenusVideoIn".equals(channelParam.getString("channelType"))){
						videoIn.add(channel);
					}else if("VenusAudioOut".equals(channelParam.getString("channelType"))){
						audioOut.add(channel);
					}else if("VenusAudioIn".equals(channelParam.getString("channelType"))){
						audioIn.add(channel);
					}
				}
				//排序
				Collections.sort(videoOut, new DeviceGroupMemberChannelPO.ChannelComparatorFromJSON());
				Collections.sort(videoIn, new DeviceGroupMemberChannelPO.ChannelComparatorFromJSON());
				Collections.sort(audioOut, new DeviceGroupMemberChannelPO.ChannelComparatorFromJSON());
				Collections.sort(audioIn, new DeviceGroupMemberChannelPO.ChannelComparatorFromJSON());
				
				for(int i=0; i<videoOut.size(); i++){
					JSONObject channel = videoOut.get(i);
					JSONObject channelParam = JSONObject.parseObject(channel.getString("param"));
					DeviceGroupMemberChannelPO channelPO = new DeviceGroupMemberChannelPO();
					channelPO.setBundleId(source.getString("id"));
					channelPO.setBundleName(source.getString("name"));
					channelPO.setBundleType(member.getBundleType());
					channelPO.setVenusBundleType(member.getVenusBundleType());
					channelPO.setChannelId(channel.getString("id"));
					//channelPO.setName(channel.getString("name"));
					channelPO.setChannelType(channelParam.getString("channelType"));
					channelPO.setChannelName(channelParam.getString("channelName"));
					channelPO.setType(ChannelType.fromChannelType(new StringBufferWrapper().append(channelPO.getChannelType()).append("-").append(i+1).toString()));
					
					channelPO.setName(channelAlias.get(channelPO.getType().toString()));
					
					channelPO.setMember(member);
					member.getChannels().add(channelPO);
				}
				
				for(int i=0; i<videoIn.size(); i++){
					JSONObject channel = videoIn.get(i);
					JSONObject channelParam = JSONObject.parseObject(channel.getString("param"));
					DeviceGroupMemberChannelPO channelPO = new DeviceGroupMemberChannelPO();
					channelPO.setBundleId(source.getString("id"));
					channelPO.setBundleName(source.getString("name"));
					channelPO.setBundleType(member.getBundleType());
					channelPO.setVenusBundleType(member.getVenusBundleType());
					channelPO.setChannelId(channel.getString("id"));
					//channelPO.setName(channel.getString("name"));
					channelPO.setChannelType(channelParam.getString("channelType"));
					channelPO.setChannelName(channelParam.getString("channelName"));
					channelPO.setType(ChannelType.fromChannelType(new StringBufferWrapper().append(channelPO.getChannelType()).append("-").append(i+1).toString()));
					
					channelPO.setName(channelAlias.get(channelPO.getType().toString()));
					
					channelPO.setMember(member);
					member.getChannels().add(channelPO);
				}
				
				for(int i=0; i<audioOut.size(); i++){
					JSONObject channel = audioOut.get(i);
					JSONObject channelParam = JSONObject.parseObject(channel.getString("param"));
					DeviceGroupMemberChannelPO channelPO = new DeviceGroupMemberChannelPO();
					channelPO.setBundleId(source.getString("id"));
					channelPO.setBundleName(source.getString("name"));
					channelPO.setBundleType(member.getBundleType());
					channelPO.setVenusBundleType(member.getVenusBundleType());
					channelPO.setChannelId(channel.getString("id"));
					//channelPO.setName(channel.getString("name"));
					channelPO.setChannelType(channelParam.getString("channelType"));
					channelPO.setChannelName(channelParam.getString("channelName"));
					channelPO.setType(ChannelType.fromChannelType(new StringBufferWrapper().append(channelPO.getChannelType()).append("-").append(i+1).toString()));
					
					channelPO.setName(channelAlias.get(channelPO.getType().toString()));
					
					channelPO.setMember(member);
					member.getChannels().add(channelPO);
				}
				
				for(int i=0; i<audioIn.size(); i++){
					JSONObject channel = audioIn.get(i);
					JSONObject channelParam = JSONObject.parseObject(channel.getString("param"));
					DeviceGroupMemberChannelPO channelPO = new DeviceGroupMemberChannelPO();
					channelPO.setBundleId(source.getString("id"));
					channelPO.setBundleName(source.getString("name"));
					channelPO.setBundleType(member.getBundleType());
					channelPO.setVenusBundleType(member.getVenusBundleType());
					channelPO.setChannelId(channel.getString("id"));
					//channelPO.setName(channel.getString("name"));
					channelPO.setChannelType(channelParam.getString("channelType"));
					channelPO.setChannelName(channelParam.getString("channelName"));
					channelPO.setType(ChannelType.fromChannelType(new StringBufferWrapper().append(channelPO.getChannelType()).append("-").append(i+1).toString()));
					
					channelPO.setName(channelAlias.get(channelPO.getType().toString()));
					
					channelPO.setMember(member);
					member.getChannels().add(channelPO);
				}
			}
			group.getMembers().add(member);
		}
	
		//拷贝参数模板
		AvtplPO sys_avtpl = sysAvtplDao.findOne(avtplId);
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
		
		//拷贝权限模板
		if(authtplId != null){
			AuthorizationPO systemAuthPO = authorizationDao.findOne(authtplId);
			Set<AuthorizationMemberPO> members = systemAuthPO.getAuthorizationMembers();
			List<String> bundleIdArray = new ArrayList<String>();
			for(AuthorizationMemberPO memberPO : members){
				bundleIdArray.add(memberPO.getBundleId());
			}
			authorizationServiceImpl.save(group.getUuid(), bundleIdArray);
		}
		
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
		group.setDicRegionId(regionId);
		if(null == regionContent || regionContent.equals("")){
			group.setDicRegionContent("默认");
		}else{
			group.setDicRegionContent(regionContent);
		}
		
		//关联直播栏目（去掉，改为自动选）
//		if(dicCategoryLiveId.equals("<default>")){
//			group.setDicCategoryLiveId(dicCategoryLiveId);
//			group.setDicCategoryLiveContent("默认");
//		}else {
//			DictionaryPO dicPO = dictionaryDao.findByLiveBoId(dicCategoryLiveId);
//			if(dicPO == null){
//				throw new BaseException(StatusCode.FORBIDDEN, DicType.LIVE.getName() + "已失效，请重新选择");
//			}
//			String dicCategoryLiveContent = dicPO.getContent();
//			group.setDicCategoryLiveId(dicCategoryLiveId);
//			group.setDicCategoryLiveContent(dicCategoryLiveContent);
//		}
		
		//关联点播二级栏目
		if(null==dicProgramId || dicProgramId.equals("<default>")){
			group.setDicProgramId("<default>");
			group.setDicProgramContent("默认");
		}else {
			DictionaryPO dicPO = dictionaryDao.findByBoId(dicProgramId);
			if(dicPO == null){
				throw new BaseException(StatusCode.FORBIDDEN, ServLevel.LEVEL_TWO.getName() + "已失效，请重新选择");
			}
			String dicProgramContent = dicPO.getContent();
			group.setDicProgramId(dicProgramId);
			group.setDicProgramContent(dicProgramContent);
		}
		
		//关联存储位置
		if(null==dicStorageLocationCode || dicStorageLocationCode.equals("<default>")){
			group.setDicStorageLocationCode("<default>");
			group.setDicStorageLocationContent("默认");
		}else {
			DictionaryPO dicPO = dictionaryDao.findByDicTypeAndCode(DicType.STORAGE_LOCATION, dicStorageLocationCode);
			if(dicPO == null){
				throw new BaseException(StatusCode.FORBIDDEN, DicType.STORAGE_LOCATION.getName() + "已失效，请重新选择");
			}
			String content = dicPO.getContent();
			group.setDicStorageLocationCode(dicStorageLocationCode);
			group.setDicStorageLocationContent(content);
		}
		
		//创建一个虚拟配置,用来处理虚拟源
		DeviceGroupConfigPO virtualConfig = new DeviceGroupConfigPO();
		virtualConfig.setName("唯一虚拟配置");
		virtualConfig.setType(ConfigType.VIRTUAL);
		virtualConfig.setVideos(new HashSet<DeviceGroupConfigVideoPO>());
		virtualConfig.setGroup(group);
		group.setConfigs(new HashSet<DeviceGroupConfigPO>());
		group.getConfigs().add(virtualConfig);
		
		deviceGroupDao.save(group);
		
		autoBuildAgendaServiceImpl.buildAgendas(group, sys_tpl.getAutoBuildAgendaIds());
		
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
	 * @Title: 修改一个设备组数据 <br/>
	 * @Description: 多次数据库操作需要事务<br/>
	 * @param groupId 设备组id
	 * @param name 设备组名称
	 * @param type 设备组类型
	 * @param transmissionMode 设备组发流类型
	 * @param avtplId 选择的参数模板id
	 * @param systemTplId 选择的会议模板id
	 * @param region 会议地区
	 * @param classify 会议分类
	 * @throws Exception 
	 * @return DeviceGroupPO 设备组
	 */
	public DeviceGroupPO update(
			Long groupId,
			String name,
			String type,
			String transmissionMode,
			String forwardMode,
			Long avtplId,
			Long systemTplId,
			String sourceList) throws Exception{
		
		DeviceGroupPO group = deviceGroupDao.findOne(groupId);
		
		//设备组重名校验
		if(!group.getName().equals(name)){
			DeviceGroupPO existGroup = deviceGroupDao.findByName(name);
			if(existGroup!=null){
				throw new DeviceGroupNameAlreadyExistedException(name);
			}
		}
		
		if(group.getStatus().equals(GroupStatus.START)) 
			throw new DeviceGroupAlreadyStartedException(group.getId(), group.getName());
		
		//新选择的成员（所有成员，非增量）
		List<JSONObject> sourceArray = JSONArray.parseArray(sourceList, JSONObject.class);
		
		//人数校验
		numberOfMembersServiceImpl.checkOneMeeting(GroupType.fromName(type), sourceArray.size());
		
		group.setName(name);
		group.setType(GroupType.fromName(type));
		group.setTransmissionMode(TransmissionMode.fromName(transmissionMode));
		group.setForwardMode(ForwardMode.fromName(forwardMode));
		group.setRecord(false);
		group.setStatus(GroupStatus.STOP);
		
		group.setUpdateTime(new Date());
		
		/************
		 * 修改会议成员
		 ************/
				
		//删除旧成员
		Set<DeviceGroupMemberPO> g_oldMembers = group.getMembers();
		
		//增量对比
		List<JSONObject> needAddSourceArray = new ArrayList<JSONObject>();
		for(JSONObject source:sourceArray){
			String bundleId = source.getString("id");
			boolean finded = false;
			for(DeviceGroupMemberPO member: g_oldMembers){
				if(member.getBundleId().equals(bundleId)){
					finded = true;
					break;
				}
			}
			if(!finded) needAddSourceArray.add(source);
		}
		
		List<DeviceGroupMemberPO> needRemoveMembers = new ArrayList<DeviceGroupMemberPO>();
		for(DeviceGroupMemberPO member:g_oldMembers){
			boolean finded = false;
			for(JSONObject source:sourceArray){
				String bundleId = source.getString("id");
				if(bundleId.equals(member.getBundleId())){
					finded = true;
					break;
				}
			}
			if(!finded) needRemoveMembers.add(member);
		}
		
		//清除成员
		Set<Long> deletedMemberChannelIds = new HashSet<Long>();
		Set<Long> deletedMemberIds = new HashSet<Long>();
		for(DeviceGroupMemberPO member: needRemoveMembers){
			deletedMemberIds.add(member.getId());
			Set<DeviceGroupMemberChannelPO> channels = member.getChannels();
			for(DeviceGroupMemberChannelPO channel:channels){
				deletedMemberChannelIds.add(channel.getId());
			}
			member.setGroup(null);
		}
		group.getMembers().removeAll(needRemoveMembers);
		for(DeviceGroupMemberPO member: needRemoveMembers){
			deviceGroupMemberDAO.delete(member);
		}
		
		//清除配置中的音频源
		List<DeviceGroupConfigAudioPO> audios = deviceGroupConfigAudioDao.findByMemberChannelIdIn(deletedMemberChannelIds);
		if(audios!=null && audios.size()>0){
			for(DeviceGroupConfigAudioPO audio:audios){
				audio.getConfig().getAudios().remove(audio);
				audio.setConfig(null);
			}
			deviceGroupConfigAudioDao.deleteInBatch(audios);
		}
		
		//清除分屏布局源
		List<DeviceGroupConfigVideoSrcPO> videoSrcs = deviceGroupConfigVideoSrcDao.findByMemberChannelIdIn(deletedMemberChannelIds);
		if(videoSrcs!=null && videoSrcs.size()>0){
			for(DeviceGroupConfigVideoSrcPO videoSrc:videoSrcs){
				videoSrc.getPosition().getSrcs().remove(videoSrc);
				videoSrc.setPosition(null);
			}
			deviceGroupConfigVideoSrcDao.deleteInBatch(videoSrcs);
		}
		
		//清除分屏转发目的
//		List<DeviceGroupConfigVideoDstPO> videoDsts = deviceGroupConfigVideoDstDao.findByMemberChannelIdIn(deletedMemberChannelIds);
		List<DeviceGroupConfigVideoDstPO> videoDsts = deviceGroupConfigVideoDstDao.findByMemberIdIn(deletedMemberIds);
		if(videoDsts!=null && videoDsts.size()>0){
			for(DeviceGroupConfigVideoDstPO videoDst:videoDsts){
				videoDst.getVideo().getDsts().remove(videoDst);
				videoDst.setVideo(null);
			}
			deviceGroupConfigVideoDstDao.deleteInBatch(videoDsts);
		}

		//查询通道别名
		List<ChannelNamePO> channelNamePOs = channelNameDAO.findAll();
		HashMap<String, String> channelAlias = new HashMap<String, String>();
		for(ChannelNamePO channelNamePO : channelNamePOs){
			channelAlias.put(channelNamePO.getChannelType(), channelNamePO.getName());
		}
		
		//新增成员,设为观众
		List<DeviceGroupBusinessRolePO> roles = queryUtil.queryRoleBySpecial(group, BusinessRoleSpecial.AUDIENCE);
		DeviceGroupBusinessRolePO audienceRole = roles.get(0);
		for(JSONObject source: needAddSourceArray){
			DeviceGroupMemberPO member = new DeviceGroupMemberPO();			
			JSONObject sourceParam = JSONObject.parseObject(source.getString("param"));
			
			member.setBundleId(source.getString("id"));
			member.setBundleName(source.getString("name"));
			member.setLayerId(sourceParam.getString("nodeUid"));
			member.setFolderId(sourceParam.getLong("folderId"));
			member.setBundleType(sourceParam.getString("bundleType"));
			member.setVenusBundleType(sourceParam.getString("venusBundleType"));
			member.setRoleId(audienceRole.getId());
			member.setRoleName(audienceRole.getName());
			member.setGroup(group);
			member.setChannels(new HashSet<DeviceGroupMemberChannelPO>());
			member.setScreens(new HashSet<DeviceGroupMemberScreenPO>());
			if("combineJv230".equals(member.getBundleType())){
				DeviceGroupMemberChannelPO videoDecodeChannelPO = new DeviceGroupMemberChannelPO();
				videoDecodeChannelPO.setBundleId(source.getString("id"));
				videoDecodeChannelPO.setBundleName(source.getString("name"));
				videoDecodeChannelPO.setBundleType(member.getBundleType());
				videoDecodeChannelPO.setVenusBundleType(member.getVenusBundleType());
				videoDecodeChannelPO.setChannelId("1");
				//videoDecodeChannelPO.setName("虚拟视频解码");
				videoDecodeChannelPO.setChannelType("VenusVideoOut");
				videoDecodeChannelPO.setChannelName("虚拟视频解码");
				videoDecodeChannelPO.setType(ChannelType.VIDEODECODE1);
				
				String videoKeyType = videoDecodeChannelPO.getType().toString();
				videoDecodeChannelPO.setName(channelAlias.get(videoKeyType));
				
				videoDecodeChannelPO.setMember(member);
				member.getChannels().add(videoDecodeChannelPO);
				
				DeviceGroupMemberChannelPO audioDecodeChannelPO = new DeviceGroupMemberChannelPO();
				audioDecodeChannelPO.setBundleId(source.getString("id"));
				audioDecodeChannelPO.setBundleName(source.getString("name"));
				audioDecodeChannelPO.setBundleType(member.getBundleType());
				audioDecodeChannelPO.setVenusBundleType(member.getVenusBundleType());
				audioDecodeChannelPO.setChannelId("2");
				//audioDecodeChannelPO.setName("虚拟音频解码");
				audioDecodeChannelPO.setChannelType("VenusAudioOut");
				audioDecodeChannelPO.setChannelName("虚拟音频解码");
				audioDecodeChannelPO.setType(ChannelType.AUDIODECODE1);
				
				String audioKeyType = audioDecodeChannelPO.getType().toString();
				audioDecodeChannelPO.setName(channelAlias.get(audioKeyType));
				
				audioDecodeChannelPO.setMember(member);
				member.getChannels().add(audioDecodeChannelPO);
				
				DeviceGroupMemberScreenPO combineJv230Screen = new DeviceGroupMemberScreenPO();
				combineJv230Screen.setBundleId(source.getString("id"));
				combineJv230Screen.setScreenId("screen_1");
				combineJv230Screen.setName("屏幕1");
				combineJv230Screen.setMember(member);
				member.getScreens().add(combineJv230Screen);
			}else{
				//屏幕screen
				List<JSONObject> screenArray = JSONArray.parseArray(source.getString("screens"), JSONObject.class);
				if(screenArray != null && screenArray.size()>0){
					for(JSONObject screen: screenArray){
						JSONObject screenParam = JSONObject.parseObject(screen.getString("param"));
						DeviceGroupMemberScreenPO screenPo = new DeviceGroupMemberScreenPO();
						screenPo.setBundleId(screenParam.getString("bundleId"));
						screenPo.setName(screen.getString("name"));
						screenPo.setScreenId(screenParam.getString("screenId"));
						screenPo.setMember(member);
						screenPo.setRests(new HashSet<DeviceGroupMemberScreenRectPO>());
						
						List<JSONObject> rectArray = JSONArray.parseArray(screenParam.getString("rects"), JSONObject.class);
						if(rectArray != null && rectArray.size()>0){
							for(JSONObject rect: rectArray){
								DeviceGroupMemberScreenRectPO rectPO = new DeviceGroupMemberScreenRectPO();
								rectPO.setBundleId(screenPo.getBundleId());
								rectPO.setScreenId(rect.getString("screenId"));
								rectPO.setParam(rect.getString("param"));
								rectPO.setChannel(rect.getString("channel"));
								rectPO.setRectId(rect.getString("rectId"));
								rectPO.setScreen(screenPo);
								screenPo.getRests().add(rectPO);
							}
						}
						
						member.getScreens().add(screenPo);
					}
				}
				
				//通道channel
				List<JSONObject> channelArray = JSONArray.parseArray(source.getString("children"), JSONObject.class);
				List<JSONObject> videoOut = new ArrayList<JSONObject>();
				List<JSONObject> videoIn = new ArrayList<JSONObject>();
				List<JSONObject> audioOut = new ArrayList<JSONObject>();
				List<JSONObject> audioIn = new ArrayList<JSONObject>();
				for(JSONObject channel:channelArray){
					JSONObject channelParam = JSONObject.parseObject(channel.getString("param"));
					if("VenusVideoOut".equals(channelParam.getString("channelType"))){
						videoOut.add(channel);
					}else if("VenusVideoIn".equals(channelParam.getString("channelType"))){
						videoIn.add(channel);
					}else if("VenusAudioOut".equals(channelParam.getString("channelType"))){
						audioOut.add(channel);
					}else if("VenusAudioIn".equals(channelParam.getString("channelType"))){
						audioIn.add(channel);
					}
				}
				//排序
				Collections.sort(videoOut, new DeviceGroupMemberChannelPO.ChannelComparatorFromJSON());
				Collections.sort(videoIn, new DeviceGroupMemberChannelPO.ChannelComparatorFromJSON());
				Collections.sort(audioOut, new DeviceGroupMemberChannelPO.ChannelComparatorFromJSON());
				Collections.sort(audioIn, new DeviceGroupMemberChannelPO.ChannelComparatorFromJSON());
				
				for(int i=0; i<videoOut.size(); i++){
					JSONObject channel = videoOut.get(i);
					JSONObject channelParam = JSONObject.parseObject(channel.getString("param"));
					DeviceGroupMemberChannelPO channelPO = new DeviceGroupMemberChannelPO();
					channelPO.setBundleId(source.getString("id"));
					channelPO.setBundleName(source.getString("name"));
					channelPO.setBundleType(member.getBundleType());
					channelPO.setVenusBundleType(member.getVenusBundleType());
					channelPO.setChannelId(channel.getString("id"));
					//channelPO.setName(channel.getString("name"));
					channelPO.setChannelType(channelParam.getString("channelType"));
					channelPO.setChannelName(channelParam.getString("channelName"));
					channelPO.setType(ChannelType.fromChannelType(new StringBufferWrapper().append(channelPO.getChannelType()).append("-").append(i+1).toString()));
					
					channelPO.setName(channelAlias.get(channelPO.getType().toString()));
					
					channelPO.setMember(member);
					member.getChannels().add(channelPO);
				}
				
				for(int i=0; i<videoIn.size(); i++){
					JSONObject channel = videoIn.get(i);
					JSONObject channelParam = JSONObject.parseObject(channel.getString("param"));
					DeviceGroupMemberChannelPO channelPO = new DeviceGroupMemberChannelPO();
					channelPO.setBundleId(source.getString("id"));
					channelPO.setBundleName(source.getString("name"));
					channelPO.setBundleType(member.getBundleType());
					channelPO.setVenusBundleType(member.getVenusBundleType());
					channelPO.setChannelId(channel.getString("id"));
					//channelPO.setName(channel.getString("name"));
					channelPO.setChannelType(channelParam.getString("channelType"));
					channelPO.setChannelName(channelParam.getString("channelName"));
					channelPO.setType(ChannelType.fromChannelType(new StringBufferWrapper().append(channelPO.getChannelType()).append("-").append(i+1).toString()));
					
					channelPO.setName(channelAlias.get(channelPO.getType().toString()));
					
					channelPO.setMember(member);
					member.getChannels().add(channelPO);
				}
				
				for(int i=0; i<audioOut.size(); i++){
					JSONObject channel = audioOut.get(i);
					JSONObject channelParam = JSONObject.parseObject(channel.getString("param"));
					DeviceGroupMemberChannelPO channelPO = new DeviceGroupMemberChannelPO();
					channelPO.setBundleId(source.getString("id"));
					channelPO.setBundleName(source.getString("name"));
					channelPO.setBundleType(member.getBundleType());
					channelPO.setVenusBundleType(member.getVenusBundleType());
					channelPO.setChannelId(channel.getString("id"));
					//channelPO.setName(channel.getString("name"));
					channelPO.setChannelType(channelParam.getString("channelType"));
					channelPO.setChannelName(channelParam.getString("channelName"));
					channelPO.setType(ChannelType.fromChannelType(new StringBufferWrapper().append(channelPO.getChannelType()).append("-").append(i+1).toString()));
					
					channelPO.setName(channelAlias.get(channelPO.getType().toString()));
					
					channelPO.setMember(member);
					member.getChannels().add(channelPO);
				}
				
				for(int i=0; i<audioIn.size(); i++){
					JSONObject channel = audioIn.get(i);
					JSONObject channelParam = JSONObject.parseObject(channel.getString("param"));
					DeviceGroupMemberChannelPO channelPO = new DeviceGroupMemberChannelPO();
					channelPO.setBundleId(source.getString("id"));
					channelPO.setBundleName(source.getString("name"));
					channelPO.setBundleType(member.getBundleType());
					channelPO.setVenusBundleType(member.getVenusBundleType());
					channelPO.setChannelId(channel.getString("id"));
					//channelPO.setName(channel.getString("name"));
					channelPO.setChannelType(channelParam.getString("channelType"));
					channelPO.setChannelName(channelParam.getString("channelName"));
					channelPO.setType(ChannelType.fromChannelType(new StringBufferWrapper().append(channelPO.getChannelType()).append("-").append(i+1).toString()));
					
					channelPO.setName(channelAlias.get(channelPO.getType().toString()));
					
					channelPO.setMember(member);
					member.getChannels().add(channelPO);
				}
			}		
			group.getMembers().add(member);
		}
		
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
		}else if(forwardMode.equals(ForwardMode.DEVICE.getName())){
			//TODO:资源创建虚拟资源，返回通道信息
			Set<DeviceGroupBusinessRolePO> groupRoles = group.getRoles();
			
			for(DeviceGroupBusinessRolePO role: groupRoles){
				if(role.getSpecial().equals(BusinessRoleSpecial.AUDIENCE) || role.getSpecial().equals(BusinessRoleSpecial.CUSTOM)){
					
					if(role.getBundleId() != null || role.getLayerId() != null){
						//解绑
						resourceService.unBindVirtualDev(role.getBundleId());
						
						role.setBundleId(null);
						role.setLayerId(null);
						role.setBaseType(null);
						role.setChannel(null);
					}
				}
			}		
		}
		
		deviceGroupDao.save(group);
		
		return group;
	}	
	
	/**
	 * @Title: 添加成员
	 * @param groupId
	 * @param bundleIds
	 * @throws
	 */
	@Deprecated
	public DeviceGroupPO addMembers(Long groupId, List<String> bundleIds) throws Exception{
		
		LogicBO logic = new LogicBO();
		DeviceGroupPO group = new DeviceGroupPO();
				
		synchronized (groupId) {
			
		//检查是否已经添加
		Set<String> bundleIdsSet = new HashSet<>(bundleIds);
		List<DeviceGroupMemberPO> checkMembers = deviceGroupMemberDAO.findByGroupIdAndBundleIdIn(groupId, bundleIdsSet);
		if(checkMembers.size()>0){
			throw new BaseException(StatusCode.FORBIDDEN, "请不要重复添加");
		}

		group = deviceGroupDao.findOne(groupId);
		if(group.getMembers() == null || group.getMembers().size() == 0) group.setMembers(new HashSet<DeviceGroupMemberPO>());
		
		//人数校验
		numberOfMembersServiceImpl.checkOneMeetingAddMember(group, bundleIds.size());
		if(group.getStatus().equals(GroupStatus.START))	numberOfMembersServiceImpl.checkAllMeeting(group.getType(), bundleIds.size());
		
		//处理参数模板
		DeviceGroupAvtplPO avtpl = group.getAvtpl();
		DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
		CodecParamBO codec = new CodecParamBO().set(avtpl, currentGear);
		
		//查询会议中的所有拼接屏
		queryUtil.queryCombineJv230s(group);
		
		//资源查询设备
		List<BundlePO> bundles = resourceQueryUtil.queryAllBundlesByBundleIds(bundleIds);
		List<ChannelSchemeDTO> channelDTOs = resourceQueryUtil.queryAllChannelsByBundleIds(bundleIds);
		List<ScreenSchemePO> screens = resourceQueryUtil.queryScreensByBundleIds(bundleIds);
		
		Set<String> screenIds = new HashSet<String>();
		for(ScreenSchemePO screen: screens){
			screenIds.add(screen.getScreenId());
		}
		List<ScreenRectTemplatePO> rects = resourceQueryUtil.queryRectsByScreenIds(screenIds);
		
		//查询大屏设备
		List<Long> combineJv230Ids = new ArrayList<Long>();
		for(String bundleId: bundleIds){
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
		List<ChannelNamePO> channelNamePOs = channelNameDAO.findAll();
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
		if(bundles != null && bundleIds.size()>0){
			for(BundlePO bundle: bundles){
				DeviceGroupMemberPO member = new DeviceGroupMemberPO();
				member.setBundleId(bundle.getBundleId());
				member.setBundleName(bundle.getBundleName());
				member.setLayerId(bundle.getAccessNodeUid());
				member.setFolderId(bundle.getFolderId());
				member.setBundleType(bundle.getDeviceModel());
				member.setVenusBundleType(bundle.getBundleType());
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
		
		Set<DeviceGroupMemberPO> allMembers = group.getMembers();
		List<DeviceGroupMemberPO> totalMembers = new ArrayList<DeviceGroupMemberPO>();
		List<Jv230ChannelPO> totalJv230Channels = new ArrayList<Jv230ChannelPO>();
		
		for(String bundleId: bundleIds){
			DeviceGroupMemberPO member = queryUtil.queryMemberPOByBundleId(allMembers, bundleId);
			
			logic.merge(roleServiceImpl.setRole(group, role, member, false, true, false));
			
			if("combineJv230".equals(member.getBundleType())){
				for(CombineJv230PO combineJv230: combineJv230s){
					if(Long.valueOf(member.getBundleId()).equals(combineJv230.getId())){					
						Set<Jv230PO> Jv230POs = combineJv230.getBundles();
						if(Jv230POs!=null && Jv230POs.size()>0){
							for(Jv230PO Jv230PO:Jv230POs){
								totalJv230Channels.addAll(Jv230PO.getChannels());
							}
						}					
						break;
					}
				}
			}else{
				if(!group.getType().equals(GroupType.MONITOR)){
					totalMembers.add(member);
				}
			}
		}		
	
		logic.merge(new LogicBO().setJv230Connect(totalJv230Channels));
		logic.merge(new LogicBO().setConnectBundle(group, totalMembers, codec));
		
		//音频
		Set<Long> totalAudioMemberIds = new HashSet<Long>();
		Set<CombineAudioPO> combineAudios = group.getCombineAudios();
		for(CombineAudioPO combineAudio: combineAudios){
			Set<CombineAudioSrcPO> audioSrcs = combineAudio.getSrcs();
			for(CombineAudioSrcPO src: audioSrcs){
				totalAudioMemberIds.add(src.getMemberId());
			}
		}
		
		if(totalAudioMemberIds != null && totalAudioMemberIds.size() > 0){
			logic.merge(audioServiceImpl.setGroupAudio(group, totalAudioMemberIds, false, true, false));
		}
		
		deviceGroupDao.save(group);
		
		logic.setUserId(group.getUserId().toString());
		//会议在开启状态下加设备要处理协议
		if(group.getStatus().equals(GroupStatus.START)) executeBusiness.execute(logic, "添加成员：");
		
		}		
		return group;
	}
	
	/**
	 * @Title: 删除成员逻辑  
	 * @param groupId 设备组id
	 * @param memberIds 删除成员ids
	 * @return DeviceGroupPO
	 * @throws
	 */
	public DeviceGroupPO removeMembers(DeviceGroupPO group, List<Long> memberIds) throws Exception{
		
		LogicBO logic = new LogicBO();
		
		//处理参数模板
		DeviceGroupAvtplPO avtpl = group.getAvtpl();
		DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
		CodecParamBO codec = new CodecParamBO().set(avtpl, currentGear);
		
		//查询会议中的所有拼接屏
		queryUtil.queryCombineJv230s(group);
		
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
		
		for(Long memberId: memberIds){
			DeviceGroupMemberPO member = queryUtil.queryMemberById(group, memberId);
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
				
				if(!group.getType().equals(GroupType.MONITOR)){
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
		deviceGroupMemberDAO.deleteInBatch(needRemoveMembers);
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
	 * @Title: 设备组启动<br/> 
	 * @Description: 打开并占用所有的设备组成员通道<br/> 
	 * @param groupId 设备组id
	 * @throws Exception
	 * @return 
	 */
	public DeviceGroupPO start(Long groupId) throws Exception{
		
		LogicBO logic = new LogicBO();
		
		synchronized (groupId) {
			

			DeviceGroupPO group = deviceGroupDao.findOne(groupId);
					
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
					if(!"combineJv230".equals(member.getBundleType())){
						if("ipc".equals(member.getBundleType())){
							totalChannels.addAll(member.getChannels());
						}else{
							//“连接中”则不发命令
							if(!MemberStatus.CONNECTING.equals(member.getMemberStatus())){
								totalBundles.add(member);
							}
						}
					}
				}
				
				Set<Jv230ChannelPO> totalJv230Channels = new HashSet<Jv230ChannelPO>();
				if(group.getStatus().equals(GroupStatus.STOP)){
					//呼叫会议中所有的jv230
					List<CombineJv230PO> combineJv230s = queryUtil.queryCombineJv230s(group);		
					if(combineJv230s!=null && combineJv230s.size()>0){
						for(CombineJv230PO combineJv230:combineJv230s){
							Set<Jv230PO> bundles = combineJv230.getBundles();
							if(bundles!=null && bundles.size()>0){
								for(Jv230PO bundle:bundles){
									totalJv230Channels.addAll(bundle.getChannels());
								}
							}
						}
					}
				}		
				
				//生成协议--这里把转发也重新发一遍
				logic = new LogicBO().setUserId(group.getUserId().toString())
										     .setConnect(totalChannels, codec)
											 .setConnectBundle(group, totalBundles, codec)
											 .setForward(group.getForwards(), codec)
											 .setJv230Connect(totalJv230Channels);
				
				//虚拟源创建合屏
				DeviceGroupConfigPO virtualConfig = queryUtil.queryVirtualConfig(group);
				for(DeviceGroupConfigVideoPO video:virtualConfig.getVideos()){
					logic.merge(videoServiceImpl.createCombineVideo(group, video, false, false));
				}	
			}
			
			//首次开会，需要计数+1
			if(GroupStatus.STOP.equals(group.getStatus())){
				List<ConnectBundleBO> connectBundleBOs = logic.getConnectBundle();
				for(ConnectBundleBO connectBundle : connectBundleBOs){
					connectBundle.setBusinessType("vod");
					connectBundle.setOperateType("start");
				}				
			}
			
			//修改会议状态
			group.setStatus(GroupStatus.START);
			
			//保存设备组状态
			deviceGroupDao.save(group);
			
			//调用逻辑层
			executeBusiness.execute(logic, "逻辑层交互：设备组启动");
			
			return group;
		}
		
	}
	
	public DeviceGroupPO stop(Long groupId) throws Exception{
		DeviceGroupPO po = stop(groupId, RecordToVodType.NOTTOVOD.getProtocalId());
		return po;
	}
	
	/**
	 * @Title: 设备组停止
	 * @Description: 关闭所有的设备组成员通道<br/> 
	 * @param groupId 设备组id
	 * @param transferToVod 是否将录制转换成点播，默认0不准换；1转换
	 * @return DeviceGroupPO    返回类型 
	 * @throws Exception
	 */
	public DeviceGroupPO stop(Long groupId, String transferToVod) throws Exception{
		
		//协议数据
		LogicBO logic = new LogicBO();
		
		synchronized (groupId) {
			
			DeviceGroupPO group = deviceGroupDao.findOne(groupId);
			
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
				List<PublishStreamPO> needDeletePublishStreams = new ArrayList<PublishStreamPO>();
				List<RecordPO> records = queryUtil.queryRunRecords(group);
				if(records!=null && records.size()>0){
					for(RecordPO record:records){
						logic.getRecordDel().add(new RecordSetBO().setUuid(record.getUuid()).setTransferToVod(transferToVod).setGroupUuid(group.getUuid()));
						record.setRun(false);
						
						//清除直播
						Set<PublishStreamPO> publishStreams = record.getPublishStreams();
						if(publishStreams != null && publishStreams.size() > 0){
							for(PublishStreamPO publish: publishStreams){
								publish.setRecord(null);
								needDeletePublishStreams.add(publish);
							}
							record.getPublishStreams().removeAll(publishStreams);
						}
					}
					
					//调用媒资
					mimsService.removeMimsResource(needDeletePublishStreams);
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
				
				//获取所有观众和自定义角色
				if(group.getForwardMode().equals(ForwardMode.ROLE)){
					Set<DeviceGroupBusinessRolePO> roles = group.getRoles();
					List<DeviceGroupBusinessRolePO> closeRoles = new ArrayList<DeviceGroupBusinessRolePO>();
					for(DeviceGroupBusinessRolePO role: roles){
						if(role.getSpecial().equals(BusinessRoleSpecial.AUDIENCE) || role.getSpecial().equals(BusinessRoleSpecial.CUSTOM)){
							closeRoles.add(role);
						}
					}
					logic.setDisconnectRole(closeRoles);
				}
				
				//处理协议
				logic.setUserId(group.getUserId().toString())
				     .setDisconnect(totalChannels, codec)
					 .setDisconnectBundle(group, totalBundles, codec)
					 .setJv230Disconnect(jv230Channels)
					 .setCombineVideoDel(combineVideos)
					 .setCombineAudioDel(combineAudios)
					 .deleteForward(forwards, codec);
			
			}
			
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
			
			//保存设备组状态
			deviceGroupDao.save(group);
			
			//调用逻辑层
			executeBusiness.execute(logic, "逻辑层交互：设备组停止");
			
			return group;
		
		}
	}
	
	/**
	 * @Title: 设置设备组音频<br/> 
	 * @param groupId 设备组id
	 * @param voicedIds 打开音频的设备组成员id数组
	 * @throws Exception
	 * @return DeviceGroupPO 设备组
	 */
	public DeviceGroupPO setGroupAudio(Long groupId, Collection<Long> voicedIds) throws Exception{
		
		DeviceGroupPO group = deviceGroupDao.findOne(groupId);
		
		if(GroupStatus.STOP.equals(group.getStatus())){
			throw new DeviceGroupHasNotStartedException(group.getId(), group.getName());
		}
		
		queryUtil.queryCombineJv230s(group);
		
		//参数模板
		DeviceGroupAvtplPO avtpl = group.getAvtpl();
		DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
		CodecParamBO codec = new CodecParamBO().set(avtpl, currentGear);
		
		LogicBO logic = new LogicBO();
		
		//设置混音
		logic.merge(audioServiceImpl.setGroupAudio(group, voicedIds, false, false, false));
		
		//音频修改录制联动
		if(group.isRecord() || group.hasRunningPublishStream()) logic.merge(recordServiceImpl.updateRecordWhenAudioChange(group, null, false, false));
		
		//持久化数据
		deviceGroupDao.save(group);
		
		//设置转发
		logic.setForward(group.getForwards(), codec);
		
		//调用逻辑层
		logic.setUserId(group.getUserId().toString());
		executeBusiness.execute(logic, "设置音频：");
		
		return group;
	}
	
	/**
	 * @Title: updateRole 保存配置角色<br/>
	 * @param members 成员列表，里面包含roleId和roleName<br/>
	 * @throws Exception
	 * @return List<DeviceGroupMemberPO>
	 */
	public void updateRole(String members) throws Exception{
		List<JSONObject> memberArray = JSONArray.parseArray(members, JSONObject.class);
		List<Long> memberIds = new ArrayList<Long>();
		for(JSONObject member: memberArray){
			Long memberId = member.getLong("id");			
			memberIds.add(memberId);
		}
		
		List<DeviceGroupMemberPO> memberPOs = deviceGroupMemberDAO.findAll(memberIds);
		
		for(DeviceGroupMemberPO memberPO: memberPOs){
			for(JSONObject member: memberArray){
				Long memberId = member.getLong("id");
				Long roleId = member.getLong("roleId");
				String roleName = member.getString("roleName");
				
				if(memberId.equals(memberPO.getId())){
					memberPO.setRoleId(roleId);
					memberPO.setRoleName(roleName);
					break;
				}
			}
		}
		
		deviceGroupMemberDAO.save(memberPOs);
	}
	
	/**
	 * 设置发言人<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月17日 下午1:32:47
	 * @param groupId 设备组id
	 * @param memberId 成员id
	 * @param roleId 发言人角色id
	 * @throws Exception
	 */
	public void spokesmanSet(Long groupId, Long memberId, Long roleId) throws Exception{
		
		DeviceGroupPO group = deviceGroupDao.findOne(groupId);
		
		DeviceGroupBusinessRolePO role = queryUtil.queryRoleById(group, roleId);
		
		DeviceGroupMemberPO newMember = queryUtil.queryMemberById(group, memberId);
		
		List<DeviceGroupMemberPO> oldMembers = queryUtil.queryMemberByRole(group, roleId);
		DeviceGroupMemberPO oldMember = null;
		if(oldMembers!=null && oldMembers.size()>0){
			oldMember = oldMembers.get(0);
		}
		
		LogicBO logic = roleServiceImpl.setSpokenman(group, role, newMember, oldMember, false, true, false);
		
		deviceGroupDao.save(group);
		
		logic.setUserId(group.getUserId().toString());
		executeBusiness.execute(logic, "设置发言人：");
	}
	
	/**
	 * 设置角色<br/>
	 * @param groupId 设备组id
	 * @param memberIds 成员ids
	 * @param roleId 发言人角色id
	 * @throws Exception
	 */
	public void rolesSet(Long groupId, List<Long> memberIds, Long roleId) throws Exception{
		
		LogicBO logic = new LogicBO();

		DeviceGroupPO group = deviceGroupDao.findOne(groupId);
		
		DeviceGroupBusinessRolePO role = queryUtil.queryRoleById(group, roleId);
	
		for(Long memberId: memberIds){
			
			DeviceGroupMemberPO member = queryUtil.queryMemberById(group, memberId);
			logic.merge(roleServiceImpl.setRole(group, role, member, false, true, false));
		}
		
		deviceGroupDao.save(group);
		
		logic.setUserId(group.getUserId().toString());
		executeBusiness.execute(logic, "设置角色：");
	}
	
	/**
	 * 移除角色设备<br/>
	 * @param groupId 设备组id
	 * @param memberIds 成员ids
	 * @param roleId 发言人角色id
	 * @throws Exception
	 */
	public void rolesRemove(Long groupId, List<Long> memberIds, Long roleId) throws Exception{
		
		LogicBO logic = new LogicBO();

		DeviceGroupPO group = deviceGroupDao.findOne(groupId);
		
		DeviceGroupBusinessRolePO role = queryUtil.queryRoleById(group, roleId);
	
		List<DeviceGroupMemberPO> members = queryUtil.queryMembersByIds(group, memberIds);
		logic.merge(roleServiceImpl.removeRole(group, role, members, false, true, false));
		
		deviceGroupDao.save(group);
		
		logic.setUserId(group.getUserId().toString());
		executeBusiness.execute(logic, "移除角色设备：");
	}
	
	/**
	 * 移除角色所有设备<br/>
	 * @param groupId 设备组id
	 * @param roleId 发言人角色id
	 * @throws Exception
	 */
	public void roleMembersRemove(Long groupId, Long roleId) throws Exception{
		
		LogicBO logic = new LogicBO();

		DeviceGroupPO group = deviceGroupDao.findOne(groupId);
		
		DeviceGroupBusinessRolePO role = queryUtil.queryRoleById(group, roleId);
		
		List<DeviceGroupMemberPO> members = queryUtil.queryMemberByRole(group, roleId);
	
		logic.merge(roleServiceImpl.removeRole(group, role, members, false, true, false));
		
		deviceGroupDao.save(group);
		
		logic.setUserId(group.getUserId().toString());
		executeBusiness.execute(logic, "移除角色设备：");
	}
	
	/**
	 * 移除发言人所有设备<br/>
	 * @param groupId 设备组id
	 * @param roleId 发言人角色id
	 * @throws Exception
	 */
	public void spokesmanMembersRemove(Long groupId, Long roleId) throws Exception{

		DeviceGroupPO group = deviceGroupDao.findOne(groupId);
		
		DeviceGroupBusinessRolePO role = queryUtil.queryRoleById(group, roleId);
		
		List<DeviceGroupMemberPO> oldMembers = queryUtil.queryMemberByRole(group, roleId);
		DeviceGroupMemberPO oldMember = null;
		if(oldMembers!=null && oldMembers.size()>0){
			oldMember = oldMembers.get(0);
		}
		
		LogicBO logic = roleServiceImpl.setSpokenman(group, role, null, oldMember, false, true, false);
		
		deviceGroupDao.save(group);
		
		logic.setUserId(group.getUserId().toString());
		executeBusiness.execute(logic, "移除发言人设备：");
	}
	
	/**
	 * @Title: 删除单个转发<br/>
	 * @param groupId 设备组id
	 * @param forwardId 转发id
	 * @throws Exception
	 * @return 
	 */
	public void removeForward(Long groupId, Long forwardId) throws Exception{
		//协议数据
		LogicBO logic = new LogicBO();
		
		DeviceGroupPO group = deviceGroupDao.findOne(groupId);
		
		//处理参数模板
		DeviceGroupAvtplPO avtpl = group.getAvtpl();
		DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
		CodecParamBO codec = new CodecParamBO().set(avtpl, currentGear);
		
		List<ChannelForwardPO> forwardList = new ArrayList<ChannelForwardPO>();
		ChannelForwardPO forward = channelForwardDao.findOne(forwardId);
		forwardList.add(forward);
		
		logic.setUserId(group.getUserId().toString())
			 .deleteForward(forwardList, codec);
		
		channelForwardDao.delete(forward);
		
		//调用逻辑层
		executeBusiness.execute(logic, "删除转发：");
	}
	
	/**
	 * @Title: 单个转发重发<br/>
	 * @param groupId 设备组id
	 * @param forwardId 转发id
	 * @throws Exception
	 * @return 
	 */
	public void refreshForward(Long groupId, Long forwardId) throws Exception{
		//协议数据
		LogicBO logic = new LogicBO();
		
		DeviceGroupPO group = deviceGroupDao.findOne(groupId);
		
		//处理参数模板
		DeviceGroupAvtplPO avtpl = group.getAvtpl();
		DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
		CodecParamBO codec = new CodecParamBO().set(avtpl, currentGear);
		
		List<ChannelForwardPO> forwardList = new ArrayList<ChannelForwardPO>();
		ChannelForwardPO forward = channelForwardDao.findOne(forwardId);
		forwardList.add(forward);
		
		logic.setUserId(group.getUserId().toString())
			 .setForward(forwardList, codec);
		
		//调用逻辑层
		executeBusiness.execute(logic, "转发重发：");
	}
	
	/**
	 * @Title: 单个合屏删除<br/>
	 * @param groupId 设备组id
	 * @param combineVideoUuid 合屏uuid
	 * @throws Exception
	 * @return 
	 */
	public void removeCombineVideo(Long groupId, String combineVideoUuid) throws Exception{
		
		DeviceGroupPO group = deviceGroupDao.findOne(groupId);
		LogicBO logic = videoServiceImpl.removeCombineVideo(group, combineVideoUuid);
		
		deviceGroupDao.save(group);
		
		//调用逻辑层
		executeBusiness.execute(logic, "删除合屏：");
	}
	
	/**
	 * @Title: 设备组刷新设备信息<br/>
	 * @description: 只刷新bundleName、folderId和layerId
	 * @param groupId 设备组id
	 * @throws Exception
	 * @return 
	 */
	public DeviceGroupPO refreshDevice(Long groupId) throws Exception{
		
		DeviceGroupPO group = deviceGroupDao.findOne(groupId);
		
		//去掉是否开会校验
//		if(GroupStatus.START.equals(group.getStatus())){
//			throw new DeviceGroupAlreadyStartedException(group.getId(), group.getName());
//		}
		
		//获取设备组成员
		List<String> bundleIds = new ArrayList<String>();
		List<Long> combineJv230BundleIds = new ArrayList<Long>();
		Set<String> jv230BundleIds = new HashSet<String>();
		Set<DeviceGroupMemberPO> members = group.getMembers();
		if(members != null && members.size() > 0){
			for(DeviceGroupMemberPO member:members){
				if(!"combineJv230".equals(member.getBundleType())){
					bundleIds.add(member.getBundleId());
				}else{
					combineJv230BundleIds.add(Long.valueOf(member.getBundleId()));
				}
			}
		}			
		
		//获取拼接屏信息
		List<CombineJv230PO> combineJv230s = combineJv230Dao.findAll(combineJv230BundleIds);
		List<Jv230PO> allJv230s = new ArrayList<Jv230PO>();
		for(CombineJv230PO combineJv230: combineJv230s){
			Set<Jv230PO> jv230s = combineJv230.getBundles();
			allJv230s.addAll(jv230s);
			for(Jv230PO jv230: jv230s){
				jv230BundleIds.add(jv230.getBundleId());
			}
		}
		
		//获取配置音频源、视频源、视频目的
		List<DeviceGroupConfigAudioPO> allAudios = new ArrayList<DeviceGroupConfigAudioPO>();
		List<DeviceGroupConfigVideoSrcPO> allVideoSrcs = new ArrayList<DeviceGroupConfigVideoSrcPO>();
		List<DeviceGroupConfigVideoDstPO> allVideoDsts = new ArrayList<DeviceGroupConfigVideoDstPO>();
				
		Set<DeviceGroupConfigPO> configs = group.getConfigs();
		for(DeviceGroupConfigPO config: configs){
			Set<DeviceGroupConfigAudioPO> audios = config.getAudios();
			allAudios.addAll(audios);
			
			Set<DeviceGroupConfigVideoPO> videos = config.getVideos();
			for(DeviceGroupConfigVideoPO video: videos){
				Set<DeviceGroupConfigVideoDstPO> dsts = video.getDsts();
				allVideoDsts.addAll(dsts);
				
				Set<DeviceGroupConfigVideoPositionPO> positions = video.getPositions();
				for(DeviceGroupConfigVideoPositionPO position: positions){
					List<DeviceGroupConfigVideoSrcPO> srcs = position.getSrcs();
					allVideoSrcs.addAll(srcs);
				}
			}		
		}			
		
		//处理combineJv230
		List<BundlePO> queryJv230Bundles = resourceQueryUtil.queryAllBundlesByBundleIds(jv230BundleIds);
		
		if(queryJv230Bundles != null && queryJv230Bundles.size() > 0){
			for(String jv230BundleId: jv230BundleIds){
				BundlePO jv230BundlePO = queryUtil.queryBundlePOByBundleId(queryJv230Bundles, jv230BundleId);
				if(jv230BundlePO != null){
					String bundleName = jv230BundlePO.getBundleName();
					String layerId = jv230BundlePO.getAccessNodeUid(); 
					
					//jv230 + 通道刷新
					List<Jv230PO> jv230POs = queryUtil.queryJv230ByBundleId(allJv230s, jv230BundleId);
					for(Jv230PO jv230PO: jv230POs){
						if(!bundleName.equals(jv230PO.getBundleName()) || !layerId.equals(jv230PO.getLayerId())){
							jv230PO.setBundleName(bundleName);
							jv230PO.setLayerId(layerId);
							Set<Jv230ChannelPO> jv230Channels = jv230PO.getChannels();
							for(Jv230ChannelPO jv230Channel: jv230Channels){
								jv230Channel.setBundleName(bundleName);
								jv230Channel.setLayerId(layerId);
							}
						}
					}
				}
			}
		}
		
		//处理资源设备(非combineJv230)
		List<BundlePO> queryBundles = resourceQueryUtil.queryAllBundlesByBundleIds(bundleIds);
		
		//需要删除
		List<Long> removeMemberIds = new ArrayList<Long>();
		
		//有变化的设备
		List<DeviceGroupMemberPO> updateMembers = new ArrayList<DeviceGroupMemberPO>();
		
		if(queryBundles != null && queryBundles.size() > 0){
			for(String bundleId: bundleIds){
				BundlePO bundlePO = queryUtil.queryBundlePOByBundleId(queryBundles, bundleId);
				//BundlePO bundlePO = bundleService.findByBundleId(bundleId);
				if(bundlePO != null){
					String bundleName = bundlePO.getBundleName();
					Long folderId = bundlePO.getFolderId();
					String layerId = bundlePO.getAccessNodeUid();
					
					//设备组成员 + 通道刷新
					DeviceGroupMemberPO memberPO = queryUtil.queryMemberPOByBundleId(members, bundleId);	
					//设备组音频源刷新
					List<DeviceGroupConfigAudioPO> audioPOs = queryUtil.queryDeviceGroupConfigAudioPOByBundleId(allAudios, bundleId);
					//设备组视频源刷新
					List<DeviceGroupConfigVideoSrcPO> videoSrcPOs = queryUtil.queryDeviceGroupConfigVideoSrcPOByBundleId(allVideoSrcs, bundleId);
					//设备组视频目的刷新
					List<DeviceGroupConfigVideoDstPO> videoDstPOs = queryUtil.queryDeviceGroupConfigVideoDstPOByBundleId(allVideoDsts, bundleId);
					
					if(memberPO != null){
						
						//资源层bundleName变化 或 资源层layerId变化
						if(!bundleName.equals(memberPO.getBundleName()) || memberPO.getLayerId()==null || !layerId.equals(memberPO.getLayerId())){
							
							if(!bundleName.equals(memberPO.getBundleName())){
								Set<DeviceGroupMemberChannelPO> channels = memberPO.getChannels();
								for(DeviceGroupMemberChannelPO channel: channels){
									channel.setBundleName(bundleName);
								}
							}
							
							memberPO.setBundleName(bundleName);
							memberPO.setLayerId(layerId);
																
							if(audioPOs != null && audioPOs.size() > 0){
								for(DeviceGroupConfigAudioPO audioPO: audioPOs){
									audioPO.setBundleName(bundleName);
									audioPO.setLayerId(layerId);
								}
							}
							
							if(videoSrcPOs != null && videoSrcPOs.size() > 0){
								for(DeviceGroupConfigVideoSrcPO videoSrcPO: videoSrcPOs){
									videoSrcPO.setBundleName(bundleName);
									videoSrcPO.setLayerId(layerId);
								}
							}
							
							if(videoDstPOs != null && videoDstPOs.size() > 0){
								for(DeviceGroupConfigVideoDstPO videoDstPO: videoDstPOs){
									videoDstPO.setBundleName(bundleName);
									videoDstPO.setLayerId(layerId);
								}
							}
							
							updateMembers.add(memberPO);
						}
						
						//资源层folderId变化
						if(!folderId.equals(memberPO.getFolderId())){
							memberPO.setFolderId(folderId);
						}
					}
				} else {
					DeviceGroupMemberPO memberPO = queryUtil.queryMemberPOByBundleId(members, bundleId);
					removeMemberIds.add(memberPO.getId());
				}
			}
		}	
		
		//角色转发模式--角色刷新
		if(group.getForwardMode().equals(ForwardMode.ROLE)){
			
			Set<DeviceGroupBusinessRolePO> roles = group.getRoles();
			for(DeviceGroupBusinessRolePO role: roles){
				if(role.getSpecial().equals(BusinessRoleSpecial.AUDIENCE) || role.getSpecial().equals(BusinessRoleSpecial.CUSTOM)){
					
					//role的uuid作为虚拟设备识别的唯一标识
					BundlePO bundleInfo = resourceService.bindVirtualDev(role.getUuid());
					
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
		
		combineJv230Dao.save(combineJv230s);
		deviceGroupDao.save(group);
		
		if(removeMemberIds.size() > 0){
			//需要删除的设备
			removeMembers(group, removeMemberIds);
		}
		
		//开会中 呼叫 + 重新执行议程
		if(group.getType().equals(GroupType.MEETING) && group.getStatus().equals(GroupStatus.START)){
			
			if(updateMembers.size() > 0){
				
				//处理参数模板
				DeviceGroupAvtplPO avtpl = group.getAvtpl();
				DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
				CodecParamBO codec = new CodecParamBO().set(avtpl, currentGear);
				
				//TODO：需要呼叫的设备（Jv230和角色暂时不考虑）
				//map用于快速查找DeviceGroupMemberPO
				HashMap<String, DeviceGroupMemberPO> uuidMemberMap = new HashMap<String, DeviceGroupMemberPO>();
				List<DeviceGroupMemberPO> connectBundles = new ArrayList<DeviceGroupMemberPO>();
				LogicBO connectBundlesLogic = new LogicBO();
				connectBundlesLogic.setUserId(group.getUserId().toString());
				//新参数，不需要锁定全部bundle
				connectBundlesLogic.setMustLockAllBundle(false);
				
				for(DeviceGroupMemberPO bundle: updateMembers){
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
					ExecuteBusinessReturnBO executeBusinessReturnBO = executeBusiness.execute(connectBundlesLogic, "更新的会议成员呼叫：");
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
				
				LogicBO updateLogic = new LogicBO(); 
				
				//转发
				Set<ChannelForwardPO> forwards = group.getForwards();
				Set<ChannelForwardPO> needUpdateForwards = new HashSet<ChannelForwardPO>();
				for(ChannelForwardPO forward: forwards){
					for(DeviceGroupMemberPO member: updateMembers){
						if(member.getId().equals(forward.getMemberId())){
							if(forward.getLayerId() == null || !forward.getLayerId().equals(member.getLayerId())){
								forward.setLayerId(member.getLayerId());
								needUpdateForwards.add(forward);
							}
							if(forward.getBundleName() == null || !forward.getBundleName().equals(member.getBundleName())){
								forward.setBundleName(member.getBundleName());
							}
						}
						if(member.getId().equals(forward.getSourceMemberId())){
							if(forward.getSourceLayerId() == null || !forward.getSourceLayerId().equals(member.getLayerId())){
								forward.setSourceLayerId(member.getLayerId());
								needUpdateForwards.add(forward);
							}
							if(forward.getSourceBundleName() == null || !forward.getSourceBundleName().equals(member.getBundleName())){
								forward.setSourceBundleName(member.getBundleName());
							}
						}
					}
				}
				
				//合屏
				Set<CombineVideoPO> combineVideos = group.getCombineVideos();
				Set<CombineVideoPO> needUpdateCombineVideos = new HashSet<CombineVideoPO>();
				for(CombineVideoPO combineVideo: combineVideos){
					for(CombineVideoPositionPO position: combineVideo.getPositions()){
						for(CombineVideoSrcPO src: position.getSrcs()){
							for(DeviceGroupMemberPO member: updateMembers){
								 if(src.getMemberId().equals(member.getId())){
									 if(src.getLayerId() == null || !src.getLayerId().equals(member.getLayerId())){
										 src.setLayerId(member.getLayerId());
										 needUpdateCombineVideos.add(combineVideo);
									 }
									 if(src.getBundleName() == null || !src.getBundleName().equals(member.getBundleName())){
										 src.setBundleName(member.getBundleName());
									 }
								 }
							}
						}
					}
				}
				
				//混音
				Set<CombineAudioPO> combineAudios = group.getCombineAudios();
				Set<CombineAudioPO> needUpdateCombineAudios = new HashSet<CombineAudioPO>();
				for(CombineAudioPO combineAudio: combineAudios){
					for(CombineAudioSrcPO src: combineAudio.getSrcs()){
						for(DeviceGroupMemberPO member: updateMembers){
							if(src.getMemberId().equals(member.getId())){
								if(src.getLayerId() == null || !src.getLayerId().equals(member.getLayerId())){
									src.setLayerId(member.getLayerId());
									needUpdateCombineAudios.add(combineAudio);
								}
								if(src.getBundleName() == null || !src.getBundleName().equals(member.getBundleName())){
									src.setBundleName(member.getBundleName());
								}
							}
						}
					}
				}
				
				deviceGroupDao.save(group);
				
				//转发协议
				if(needUpdateForwards.size() > 0){
					updateLogic.merge(new LogicBO().setForward(needUpdateForwards, codec));
				}

				//合屏协议
				if(needUpdateCombineVideos.size() > 0){
					updateLogic.merge(new LogicBO().setCombineVideoUpdate(needUpdateCombineVideos, codec));
				}

				//混音协议
				if(needUpdateCombineAudios.size() > 0){
					updateLogic.merge(new LogicBO().setCombineAudioUpdate(needUpdateCombineAudios, codec));
				}
				
				executeBusiness.execute(updateLogic, "刷新设备组开会中成员：");
			}
		}
		
		//获取看会权限的成员
		DeviceGroupAuthorizationPO authorizationPO = deviceGroupAuthorizationDao.findByGroupUuid(group.getUuid());
		if(null != authorizationPO){
			Set<DeviceGroupAuthorizationMemberPO> authMembers = authorizationPO.getAuthorizationMembers();
			List<String> authBundleIds = new ArrayList<String>();
			for(DeviceGroupAuthorizationMemberPO authMember : authMembers){
				authBundleIds.add(authMember.getBundleId());
			}
			List<BundlePO> queryAuthBundles = resourceQueryUtil.queryAllBundlesByBundleIds(authBundleIds);
			if(null!=queryAuthBundles && queryAuthBundles.size()>0){
				LogicBO logic = new LogicBO();
				for(String authBundleId: authBundleIds){
					BundlePO queryAuthBundlePO = queryUtil.queryBundlePOByBundleId(queryAuthBundles, authBundleId);
					if(queryAuthBundlePO != null){
						String layerId = queryAuthBundlePO.getAccessNodeUid();
						
						DeviceGroupAuthorizationMemberPO authMemberPO = queryUtil.queryAuthorizationMemberPOByBundleId(authMembers, authBundleId);
						
						if(authMemberPO != null){
							//layerId变化
							if(authMemberPO.getLayerId()==null || !layerId.equals(authMemberPO.getLayerId())){
								if(null != layerId){
									authMemberPO.setLayerId(layerId);
									
									logic.setPass_by(new ArrayList<PassByBO>());
									PassByBO passBy = new PassByBO().setType("playlist_change_notify")
											.setLayer_id(layerId)
											.setBundle_id(authBundleId)
											.setPass_by_content(new PassByContentBO());
									logic.getPass_by().add(passBy);
								}
							}
						}
					}
				}
				deviceGroupAuthorizationDao.save(authorizationPO);
				//推送
				if(authorizationPO.getLiveChannels().size()>0 || authorizationPO.getAssets().size()>0){
					executeBusiness.execute(logic, "看会权限设置后推送：");
				}
			}
		}
		
		return group;
	}
	
	/**
	 * 同步角色<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月21日 下午3:38:01
	 * @param Long groupId 设备组id
	 */
	public DeviceGroupPO roleUpdate(Long groupId) throws Exception{
		
		LogicBO logic = new LogicBO();
		
		//TODO:同步角色功能
		DeviceGroupPO group = deviceGroupDao.findOne(groupId);
		
		Set<DeviceGroupBusinessRolePO> roles = group.getRoles();
		for(DeviceGroupBusinessRolePO role: roles){
			if(role.getSpecial().equals(BusinessRoleSpecial.CUSTOM) || role.getSpecial().equals(BusinessRoleSpecial.AUDIENCE)){
				if(role.getLayerId() == null || role.getBaseType() == null || role.getBundleId() == null){
					//向资源添加虚拟设备资源
					BundlePO bundleInfo = resourceService.createVirtualDev(group.getName() + "-" + role.getName(), Constant.SIGNAL_CONTROL_LAYER_ID);
					
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
				
				logic.merge(new LogicBO().createRole(role));
			}
		}
		
		executeBusiness.execute(logic, "同步角色流转发器节点");
		
		deviceGroupDao.save(group);
		
		return group;
	}
	
	/**
	 * @Title: 开始监控录制 
	 * @param groupId
	 * @param memberId
	 * @return void    返回类型 
	 * @throws
	 */
	public DeviceGroupPO startMonitorRecord(Long groupId, Long memberId) throws Exception{
		
		DeviceGroupPO group = deviceGroupDao.findOne(groupId);
		
		if(GroupStatus.STOP.equals(group.getStatus())){
			throw new DeviceGroupHasNotStartedException(group.getId(), group.getName());
		}
		
		//参数
		DeviceGroupAvtplPO avtpl = group.getAvtpl();
		DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
		
		Set<RecordPO> records = group.getRecords();
		for(RecordPO record:records){
			if(record.getType().equals(RecordType.BUNDLE) && record.getVideoMemberId().equals(memberId) && record.isRun()){
				//当前设备已经在录制了
				return group;
			}
		}
		
		RecordPO record = new RecordPO();
		record.setRun(true);
		record.setGroupType(group.getType());
		record.setDescription(new StringBufferWrapper().append("监控录制：").append(DateUtil.format(new Date(), DateUtil.dateTimePattern)).toString());
		record.setType(RecordType.BUNDLE);
		
		//获取视频编码通道1
		DeviceGroupMemberChannelPO videoEncodeChannel = queryUtil.queryEncodeVideoChannel1(group, memberId);
		
		//TODO: 获取bundle的额外信息，找到“地区”，作为录制名的前缀
		String bundleId = deviceGroupMemberDAO.findOne(memberId).getBundleId();
		List<ExtraInfoPO> infoPOs = extraInfoService.findByBundleId(bundleId);
		String districtStr = "";
		for(ExtraInfoPO infoPO : infoPOs){
			if("地区".equals(infoPO.getName())){
				districtStr = infoPO.getValue() + "_";
			}
		}
		
		if(videoEncodeChannel != null){
			//视频
			record.setVideoType(SourceType.CHANNEL);
			record.setVideoName(districtStr + group.getName() + "-" + videoEncodeChannel.getBundleName());
			record.setVideoMemberId(memberId);
			record.setVideoMemberChannelId(videoEncodeChannel.getId());
			record.setVideoLayerId(videoEncodeChannel.getMember().getLayerId());
			record.setVideoBundleId(videoEncodeChannel.getBundleId());
			record.setVideoChannelId(videoEncodeChannel.getChannelId());
		}	
		
		//获取音频编码通道
		DeviceGroupMemberChannelPO audioEncodeChannel = queryUtil.queryEncodeAudioChannel(group, memberId);
		
		if(audioEncodeChannel != null){
			//音频
			record.setAudioType(SourceType.CHANNEL);
			record.setAudioMemberId(audioEncodeChannel.getMember().getId());
			record.setAudioMemberChannelId(audioEncodeChannel.getId());
			record.setAudioLayerId(audioEncodeChannel.getMember().getLayerId());
			record.setAudioBundleId(audioEncodeChannel.getBundleId());
			record.setAudioChannelId(audioEncodeChannel.getChannelId());
		}
			
		record.setGroup(group);
		group.getRecords().add(record);
		
		//设置录制状态
		deviceGroupDao.save(group);
		
		RecordSetBO recordSetBO = new RecordSetBO().setUuid(record.getUuid())
				   									.setGroupUuid(group.getUuid())
												   .setVideoType("2")
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
			List<DictionaryPO> dicConference = dictionaryDao.findByContentPrefixAndDicType("会议_", DicType.LIVE);
			if(dicConference.size() > 0){
				recordSetBO.setCategoryLiveID(dicConference.get(0).getLiveBoId());
			}else {
				recordSetBO.setCategoryLiveID("");
			}
		}else if(group.getType().equals(GroupType.MONITOR)){
			List<DictionaryPO> dicMonitor = dictionaryDao.findByContentPrefixAndDicType("监控_", DicType.LIVE);
			if(dicMonitor.size() > 0){
				recordSetBO.setCategoryLiveID(dicMonitor.get(0).getLiveBoId());
			}else {
				recordSetBO.setCategoryLiveID("");
			}
		}
			
		LogicBO logic = new LogicBO().setUserId(group.getUserId().toString())
									 .setRecordSet(new ArrayListWrapper<RecordSetBO>().add(recordSetBO).getList());
		
		executeBusiness.execute(logic, "开始监控录制");	
		
		return group;
	}
	
	/**
	 * @Title: 停止监控录制
	 * @param groupId
	 * @param memberId
	 * @return void    返回类型 
	 * @throws
	 */
	public DeviceGroupPO stopMonitorRecord(Long groupId, Long memberId) throws Exception{
		
		DeviceGroupPO group = deviceGroupDao.findOne(groupId);
		
		if(GroupStatus.STOP.equals(group.getStatus())){
			throw new DeviceGroupHasNotStartedException(group.getId(), group.getName());
		}
		
		Set<RecordPO> records = group.getRecords();
		RecordPO currentRecord = null;
		for(RecordPO record:records){
			if(record.getType().equals(RecordType.BUNDLE) && record.getVideoMemberId().equals(memberId) && record.isRun()){
				//找到设备录制信息
				currentRecord = record;
				break;
			}
		}
		
		//这个地方没有录制
		if(currentRecord == null) return group;
		
		//修改状态
		currentRecord.setRun(false);
		recordDao.save(currentRecord);
		
		deviceGroupDao.save(group);
		
		LogicBO logic = new LogicBO().setUserId(group.getUserId().toString())
									 .setRecordDel(new ArrayListWrapper<RecordSetBO>().add(new RecordSetBO().setUuid(currentRecord.getUuid()).setGroupUuid(group.getUuid())).getList());
		
		executeBusiness.execute(logic, "停止监控录制");
		
		return group;
	}
	
}

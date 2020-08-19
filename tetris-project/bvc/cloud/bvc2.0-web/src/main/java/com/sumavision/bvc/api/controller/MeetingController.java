package com.sumavision.bvc.api.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.config.Constant;
import com.sumavision.bvc.control.device.group.vo.DeviceGroupAgendaVO;
import com.sumavision.bvc.control.device.group.vo.DeviceGroupBusinessRoleParamVO;
import com.sumavision.bvc.control.device.group.vo.DeviceGroupVO;
import com.sumavision.bvc.control.device.group.vo.GroupMemberChannelVO;
import com.sumavision.bvc.control.device.group.vo.tree.TreeNodeVO;
import com.sumavision.bvc.control.system.vo.BusinessRoleVO;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.control.welcome.UserVO;
import com.sumavision.bvc.device.group.bo.BundleBO;
import com.sumavision.bvc.device.group.bo.BundleScreenBO;
import com.sumavision.bvc.device.group.bo.ChannelBO;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.FolderBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.PassByBO;
import com.sumavision.bvc.device.group.dao.DeviceGroupBusinessRoleDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigVideoDstDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigVideoSrcDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupMemberDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupRecordSchemeDAO;
import com.sumavision.bvc.device.group.enumeration.GroupStatus;
import com.sumavision.bvc.device.group.enumeration.GroupType;
import com.sumavision.bvc.device.group.exception.CommonNameAlreadyExistedException;
import com.sumavision.bvc.device.group.exception.DeviceGroupAlreadyStartedException;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplPO;
import com.sumavision.bvc.device.group.po.DeviceGroupBusinessRolePO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigPO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberChannelPO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberPO;
import com.sumavision.bvc.device.group.po.DeviceGroupPO;
import com.sumavision.bvc.device.group.service.AgendaServiceImpl;
import com.sumavision.bvc.device.group.service.DeviceGroupServiceImpl;
import com.sumavision.bvc.device.group.service.MeetingServiceImpl;
import com.sumavision.bvc.device.group.service.MultiplayerChatServiceImpl;
import com.sumavision.bvc.device.group.service.log.LogService;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.MeetingUtil;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.device.group.service.util.ResourceQueryUtil;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.bvc.system.dao.BusinessRoleDAO;
import com.sumavision.bvc.system.dao.DictionaryDAO;
import com.sumavision.bvc.system.enumeration.BusinessRoleSpecial;
import com.sumavision.bvc.system.enumeration.BusinessRoleType;
import com.sumavision.bvc.system.po.BusinessRolePO;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

/**
 * @ClassName: API的业务（会议、通话等）接口
 * @author zsy
 * @date 2018年12月8日 下午2:27:00
 */
@Controller
@RequestMapping(value = "/api/meeting")
public class MeetingController {
	
	@Autowired
	private DeviceGroupDAO deviceGroupDao;
	
	@Autowired
	private DeviceGroupConfigDAO deviceGroupConfigDao;
	
	@Autowired
	private AgendaServiceImpl agendaServiceImpl;
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private QueryUtil queryUtil;
	
	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private MeetingUtil meetingUtil;
	
	@Autowired
	private MultiplayerChatServiceImpl multiplayerChatServiceImpl;
	
	@Autowired
	private MeetingServiceImpl meetingServiceImpl;
	
	@Autowired
	private ResourceQueryUtil resourceQueryUtil;
	
	@Autowired
	private DeviceGroupServiceImpl deviceGroupServiceImpl;
	
	@Autowired
	private DeviceGroupBusinessRoleDAO deviceGroupBusinessRoleDao;
	
	@Autowired
	private DeviceGroupRecordSchemeDAO deviceGroupRecordSchemeDao;
	
	@Autowired
	private DeviceGroupConfigVideoDstDAO deviceGroupConfigVideoDstDao;
	
	@Autowired
	private DeviceGroupConfigVideoSrcDAO deviceGroupConfigVideoSrcDao;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	@Autowired
	private ResourceService resourceService;
	
	/**
	 * @Title: 新建会议/通话
	 * @param jsonParam 参数
	 * @throws Exception
	 * @return 结果
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/new")
	public Object saveNewMeeting(
			@RequestBody JSONObject jsonParam,
			HttpServletRequest request) throws Exception{		
		
		/** 共用参数  */
		String userId = jsonParam.getString("userId");
		String userName = jsonParam.getString("userName");
		String groupName = jsonParam.getString("groupName");
		String groupType = jsonParam.getString("groupType");
		String codecParamType = jsonParam.getString("codecParamType");
		String codecParam = jsonParam.getString("codecParam");
		JSONArray sourceList = jsonParam.getJSONArray("memberList");
		
		/** 会议室参数 */
		Long systemTplId = jsonParam.getLong("groupTemplate");
		
		/** 会议室/监控室 参数 */
		String transmissionMode = jsonParam.getString("transmissionMode");
		String forwardMode = jsonParam.getString("forwardMode");
		
		/** 设置主席 */
		String chairmanId = jsonParam.getString("chairmanId");
		
		/** 多人视频通话/多人语音通话 参数 */
		String initiatorId = jsonParam.getString("initiatorId");

		/** 预约会议参数 */
		String groupController = jsonParam.getString("groupController");
		String startTime = jsonParam.getString("startTime");
		String stopTime = jsonParam.getString("stopTime");
		
		/** 预约会议/多人视频通话 参数 */
		JSONObject defaultAgenda = jsonParam.getJSONObject("defaultAgenda");
		
		DeviceGroupPO group = new DeviceGroupPO();
		
		if(GroupType.fromName(groupType).equals(GroupType.MEETING)){
			
			if(userId == null || userName == null){
				UserVO user = userUtils.getUserFromSession(request);
				userId = user.getId().toString();
				userName = user.getName();
			}
			
			group = meetingServiceImpl.save(
					Long.valueOf(userId),
					userName,
					groupName,
					groupType, 
					transmissionMode, 
					forwardMode,
					codecParamType,
					codecParam,
					systemTplId, 
					sourceList,
					chairmanId);			
			
			DeviceGroupVO _group = new DeviceGroupVO().set(group);
			
			return _group;
			
		}else if(GroupType.fromName(groupType).equals(GroupType.MONITOR)){
			
		}else if(GroupType.fromName(groupType).equals(GroupType.MULTIPLAYERAUDIO) || GroupType.fromName(groupType).equals(GroupType.MULTIPLAYERVIDEO)){
			
			List<String> targetBundleIdList = JSONArray.parseArray(sourceList.toString(), String.class);
			String websiteDraw = defaultAgenda.getString("layout");
			List<JSONObject> positions = JSONArray.parseArray(defaultAgenda.getString("positions"), JSONObject.class);
			//多人通话业务
			group = multiplayerChatServiceImpl.startChat(groupName, groupType, codecParamType, codecParam, initiatorId, targetBundleIdList, Long.valueOf(userId), userName, websiteDraw, positions);	

			logService.logsHandle(userName, "新建会议", "名称：" + groupName);
			
			JSONObject session = new JSONObject();
			session.put("groupUuid", group.getUuid());
			session.put("name", groupName);
			session.put("callType", groupName);
			
			DeviceGroupAvtplGearsPO gear = queryUtil.queryCurrentGear(group);
			
			JSONObject tpl = new JSONObject();
			tpl.put("videoFormat", group.getAvtpl().getVideoFormat().getName());
			tpl.put("audioFormat", group.getAvtpl().getAudioFormat().getName());
			tpl.put("resolution", gear.getVideoResolution());
			tpl.put("videoBitRate", gear.getVideoBitRate());
			tpl.put("audioBitRate", gear.getAudioBitRate());
			
			JSONObject data = new JSONObject();
			data.put("session", session);
			data.put("avtpl", tpl);
			
			return data;
		}
	
		return null;
	}
	
	/**
	 * @Title:删除会议
	 * @param jsonParam 参数
	 * @param request
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/remove")
	public Object removeMeeting(
			@RequestBody JSONObject jsonParam,
			HttpServletRequest request) throws Exception{
		
		Long id = jsonParam.getLong("groupId");
		Long userId = jsonParam.getLong("userId");
		String userName = jsonParam.getString("userName");
		
		if(userId == null || userName == null){
			UserVO user = userUtils.getUserFromSession(request);
			userId = user.getId();
			userName = user.getName();
		}
		
		DeviceGroupPO group = deviceGroupDao.findOne(id);
		
		meetingUtil.incorrectGroupUserIdHandle(group, userId, userName);
		
		if(GroupStatus.START.equals(group.getStatus())){
			throw new DeviceGroupAlreadyStartedException(group.getId(), group.getName());
		}
		
		deviceGroupDao.delete(group);
		
		logService.logsHandle(userName, "删除设备组", "设备组名称："+group.getName());
				
		return null;
	}
	
	/**
	 * 设备组启动<br/> 
	 * @Description: 打开并占用所有的设备组成员通道<br/> 
	 * @param jsonParam 参数
	 * @throws Exception 
	 * @return 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/start")
	public Object start(
			@RequestBody JSONObject jsonParam,
			HttpServletRequest request) throws Exception{
		
		Long groupId = jsonParam.getLong("groupId");
		Long userId = jsonParam.getLong("userId");
		String userName = jsonParam.getString("userName");
		
		if(userId == null || userName == null){
			UserVO user = userUtils.getUserFromSession(request);
			userId = user.getId();
			userName = user.getName();
		}
		
		DeviceGroupPO group = meetingServiceImpl.start(groupId, userId, userName);
		
		logService.logsHandle(userName, "设备组启动", "设备组名称："+group.getName());
		
		return new DeviceGroupVO().set(group);
	}
	
	/**
	 * 设备组停止<br/> 
	 * @Description: 关闭所有的设备组成员通道<br/> 
	 * @param groupId 设备组id
	 * @throws Exception 
	 * @return 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/stop")
	public Object stop(
			@RequestBody JSONObject jsonParam,
			HttpServletRequest request) throws Exception{
		
		Long groupId = jsonParam.getLong("groupId");
		Long userId = jsonParam.getLong("userId");
		String userName = jsonParam.getString("userName");
		
		if(userId == null || userName == null){
			UserVO user = userUtils.getUserFromSession(request);
			userId = user.getId();
			userName = user.getName();
		}
		
		DeviceGroupPO group = meetingServiceImpl.stop(groupId, userId, userName);
		
		logService.logsHandle(userName, "设备组停止", "设备组名称："+group.getName());
		
		return new DeviceGroupVO().set(group);
	}
	
	/**
	 * @Title: 控制者修改默认议程
	 * @param jsonParam 参数
	 * @throws Exception
	 * @return 结果
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/setDefaultAgenda")
	public Object setDefaultAgenda(
			@RequestBody JSONObject jsonParam,
			HttpServletRequest request) throws Exception{
		
		String userId = jsonParam.getString("userId");
		String userName = jsonParam.getString("userName");
		String groupUuid = jsonParam.getString("groupUuid");
		String websiteDraw = jsonParam.getString("layout");
		List<JSONObject> positions = JSONArray.parseArray(JSONObject.toJSONString(jsonParam.get("positions")), JSONObject.class);
		List<JSONObject> audios = JSONArray.parseArray(jsonParam.getString("audio"), JSONObject.class);	
		
	    multiplayerChatServiceImpl.updateCombineVideoAndAudio(groupUuid, userId, userName, websiteDraw, positions, audios);
		
		return null;
	}
	
	/**
	 * @Title: 大会中创建普通议程
	 * @param jsonParam 参数
	 * @throws Exception
	 * @return 结果
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/setAgenda")
	public Object setAgenda(
			@RequestBody JSONObject jsonParam,
			HttpServletRequest request) throws Exception{
		
		String userId = jsonParam.getString("userId");
		String userName = jsonParam.getString("userName");
		String groupUuid = jsonParam.getString("groupUuid");
		String agendaName = jsonParam.getString("name");
		String remark = jsonParam.getString("remark");
		JSONObject audio = jsonParam.getJSONObject("audio");
		List<JSONObject> videos = JSONObject.parseArray(jsonParam.getJSONArray("videos").toString(), JSONObject.class);
	    
	    DeviceGroupPO group = deviceGroupDao.findByUuid(groupUuid);
	    
	    meetingUtil.incorrectGroupUserIdHandle(group, Long.valueOf(userId), userName);
	    
	    DeviceGroupConfigPO agenda = meetingServiceImpl.setAgenda(group, agendaName, remark, audio, videos, null);
		
		return new DeviceGroupAgendaVO().set(agenda);
	}
	
	/**
	 * @Title: 删除议程
	 * @param jsonParam 参数
	 * @throws Exception
	 * @return 结果
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/removeAgenda")
	public Object removeAgenda(
			@RequestBody JSONObject jsonParam,
			HttpServletRequest request) throws Exception{
		
		String userId = jsonParam.getString("userId");
		String userName = jsonParam.getString("userName");
		Long agendaId = jsonParam.getLong("agendaId");
		
		//解关联
		DeviceGroupConfigPO agenda = deviceGroupConfigDao.findOne(agendaId);
		DeviceGroupPO group = agenda.getGroup();
		
		meetingUtil.incorrectGroupUserIdHandle(group, Long.valueOf(userId), userName);
		
		group.getConfigs().remove(agenda);
		agenda.setGroup(null);
				
		logService.logsHandle(userName, "删除议程", "设备组名称："+ group.getName()+"议程名称："+ agenda.getName());
		
		deviceGroupConfigDao.delete(agenda);
		
		return null;
	}
	
	/**
	 * @Title: 执行议程
	 * @param jsonParam 参数
	 * @throws Exception
	 * @return 结果
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/runAgenda")
	public Object runAgenda(
			@RequestBody JSONObject jsonParam,
			HttpServletRequest request) throws Exception{
		
		String userId = jsonParam.getString("userId");
		String userName = jsonParam.getString("userName");
		Long groupId = jsonParam.getLong("groupId");
		Long agendaId = jsonParam.getLong("agendaId");
		
		DeviceGroupConfigPO agenda = agendaServiceImpl.run(groupId, agendaId);
		
		logService.logsHandle(userName, "执行议程", "议程名称："+agenda.getName());
		
		return null;
	}
	
	/**
	 * @Title: 添加成员
	 * @param jsonParam 参数
	 * @throws Exception
	 * @return 结果
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/addMembers")
	public Object addMembers(
			@RequestBody JSONObject jsonParam,
			HttpServletRequest request) throws Exception{
		
		String groupUuid = jsonParam.getString("groupUuid");
		String userId = jsonParam.getString("userId");
		String userName = jsonParam.getString("userName");
		List<String> targetBundleIdList = JSONArray.parseArray(JSONObject.toJSONString(jsonParam.get("targetBundleIdList")), String.class);
		
		DeviceGroupPO group = deviceGroupDao.findByUuid(groupUuid);
		
		if(group == null) throw new Exception("该会议不存在！");
		
		if(group.getType().equals(GroupType.MULTIPLAYERAUDIO) || group.getType().equals(GroupType.MULTIPLAYERVIDEO)){
			//判断是否是管理员发起
			meetingUtil.incorrectGroupUserIdHandle(group, Long.valueOf(userId), userName);
			
			//更新设备组
			multiplayerChatServiceImpl.updateChat(group, targetBundleIdList);
			
			//处理参数模板
			DeviceGroupAvtplPO avtpl = group.getAvtpl();
			DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
			CodecParamBO codec = new CodecParamBO().set(avtpl, currentGear);
			
			//被添加设备呼叫
			for(String targetBundleId: targetBundleIdList){
				multiplayerChatServiceImpl.connectBundle(group, targetBundleId, codec);
			}
			
			logService.logsHandle(userName, "设备组添加成员", "设备组名称："+group.getName());
			
		}else if(group.getType().equals(GroupType.MEETING) || group.getType().equals(GroupType.MONITOR)){
			
			group = meetingServiceImpl.addMembers(group, targetBundleIdList);
			
			Set<DeviceGroupMemberPO> allMembers = group.getMembers();
			
			List<DeviceGroupMemberPO> members = new ArrayList<DeviceGroupMemberPO>();
			List<DeviceGroupMemberChannelPO> addChannels = new ArrayList<DeviceGroupMemberChannelPO>();
			for(String bundleId: targetBundleIdList){
				DeviceGroupMemberPO memberPO = queryUtil.queryMemberPOByBundleId(allMembers, bundleId);
				if(!memberPO.getBundleType().equals("combineJv230")){
					members.add(memberPO);
					addChannels.addAll(memberPO.getChannels());
				}
			}
			List<GroupMemberChannelVO> _channels = GroupMemberChannelVO.getConverter(GroupMemberChannelVO.class).convert(addChannels, GroupMemberChannelVO.class);
			
			List<FolderBO> folders = new ArrayList<FolderBO>();
			List<BundleBO> bundles = new ArrayList<BundleBO>();
			List<ChannelBO> channels = new ArrayList<ChannelBO>();
			List<TreeNodeVO> _roots = new ArrayList<TreeNodeVO>();
			
			Set<Long> folderIds = new HashSet<Long>();
			for(DeviceGroupMemberPO member: members){
				Set<DeviceGroupMemberChannelPO> channelsPO = member.getChannels();
				
				for(DeviceGroupMemberChannelPO channelPO:channelsPO){
					ChannelBO channelBO = new ChannelBO().set(channelPO)
														.setMemberId(member.getId());
					channels.add(channelBO);
				}
				folderIds.add(member.getFolderId());
				bundles.add(new BundleBO().set(member));
			}
			
			//根据新folderIds查询所有层级（文件夹）
			List<FolderPO> allFolders = resourceQueryUtil.queryFoldersTree(folderIds);

			for(FolderPO allFolderPO: allFolders){
				if(allFolderPO == null) continue;
				FolderBO folderBO = new FolderBO().set(allFolderPO);
				folders.add(folderBO);			
			}
			
			//加入大屏文件夹
			for(BundleBO bundle:bundles){
				if("combineJv230".equals(bundle.getModel())){
					folders.add(new FolderBO().setId(TreeNodeVO.FOLDERID_COMBINEJV230).setName("拼接屏设备"));
					break;
				}
			}
			
			//排序
			Collections.sort(bundles, new BundleBO.BundleIdComparator());
			Collections.sort(bundles, new BundleBO.BundleStatusComparator());
			
			//找所有的根
			List<FolderBO> roots = findRoots(folders);
			for(FolderBO root:roots){
				TreeNodeVO _root = new TreeNodeVO().set(root)
												   .setChildren(new ArrayList<TreeNodeVO>());
				_roots.add(_root);
				recursionFolder(_root, folders, bundles, channels, null);
			}
			
			logService.logsHandle(userName, "管理员添加成员", "设备组名称："+group.getName());
			
			return new HashMapWrapper<String, Object>().put("membersTree", _roots)
												  	   .put("members", _channels)
													   .getMap();
		}
		
		return null;
	}
	
	/**
	 * @Title: 删除成员
	 * @param jsonParam 参数
	 * @throws Exception
	 * @return 结果
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/removeMembers")
	public Object removeMembers(
			@RequestBody JSONObject jsonParam,
			HttpServletRequest request) throws Exception{
		
		String groupUuid = jsonParam.getString("groupUuid");
		String userId = jsonParam.getString("userId");
		String userName = jsonParam.getString("userName");
		List<String> targetBundleIdList = JSONArray.parseArray(JSONObject.toJSONString(jsonParam.get("targetBundleIdList")), String.class);
		
		DeviceGroupPO group = deviceGroupDao.findByUuid(groupUuid);
		
		meetingUtil.incorrectGroupUserIdHandle(group, Long.valueOf(userId), userName);
		
		if(group.getType().equals(GroupType.MULTIPLAYERAUDIO) || group.getType().equals(GroupType.MULTIPLAYERVIDEO)){
			multiplayerChatServiceImpl.removeMembers(group, targetBundleIdList);
		}else if(group.getType().equals(GroupType.MEETING) || group.getType().equals(GroupType.MONITOR)){
			meetingServiceImpl.removeMembers(group, targetBundleIdList);
		}

		logService.logsHandle(userName, "管理员删除成员", "设备组名称："+group.getName());
		
		return null;
	}
	
	/**
	 * @Title: 申请重新入会（限会议列表中的成员使用） <br/>
	 * @param jsonParam 参数
	 * @throws Exception
	 * @return 结果
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/backToMeeting")
	public Object reenterMeeting(
			@RequestBody JSONObject jsonParam,
			HttpServletRequest request) throws Exception{
		
		String groupUuid = jsonParam.getString("groupUuid");
		String bundleId = jsonParam.getString("bundleId");
		
		DeviceGroupPO group = deviceGroupDao.findByUuid(groupUuid);
		
		if(group.getType().equals(GroupType.MULTIPLAYERAUDIO) || group.getType().equals(GroupType.MULTIPLAYERVIDEO)){
			multiplayerChatServiceImpl.reenterMeeting(groupUuid, bundleId);
		}else if(group.getType().equals(GroupType.MEETING) || group.getType().equals(GroupType.MONITOR)){
			meetingServiceImpl.reenterMeeting(groupUuid, bundleId);
		}		
		
		return null;
	}
	
	/**
	 * 主动挂断<br/>
	 * @Title: exitMeeting 
	 * @param jsonParam 参数
	 * @return 结果
	 * @throws
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/exitMeeting")
	public Object exitMeeting(
			@RequestBody JSONObject jsonParam,
			HttpServletRequest request) throws Exception{
		
		String groupUuid = jsonParam.getString("groupUuid");
		String bundleId = jsonParam.getString("bundleId");
		
		DeviceGroupPO group = deviceGroupDao.findByUuid(groupUuid);		
		if(group == null) throw new Exception("该会议不存在！");
		
		//是否会议成员	
		Set<DeviceGroupMemberPO> members = group.getMembers();
		boolean flag = false;
		for(DeviceGroupMemberPO member: members){
			if(member.getBundleId().equals(bundleId)){
				flag = true;
				break;
			}
		}
		if(!flag) throw new Exception(bundleId + "不是会议成员!");	
		
		if(group.getType().equals(GroupType.MEETING)){
			meetingServiceImpl.exitMeeting(group, bundleId);
		}else if(group.getType().equals(GroupType.MULTIPLAYERAUDIO) || group.getType().equals(GroupType.MULTIPLAYERVIDEO)){
			multiplayerChatServiceImpl.exitGroup(groupUuid, bundleId);
		}
		
		return null;
	}
	
	/**
	 * 把成员设置为发言人<br/>
	 * @param groupId 设备组id
	 * @param memberId 成员id
	 * @param roleId 角色id
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/spokesman/set")
	public Object spokesmanSet(
			@RequestBody JSONObject jsonParam,
			HttpServletRequest request) throws Exception{
		
		Long groupId = Long.parseLong(jsonParam.getString("groupId"));
		Long memberId = Long.parseLong(jsonParam.getString("memberId"));
		Long roleId = Long.parseLong(jsonParam.getString("roleId"));
		
		deviceGroupServiceImpl.spokesmanSet(groupId, memberId, roleId);
		return null;		
	}
	
	/**
	 * 把成员设置为观众类角色<br/>
	 * @param groupId 设备组id
	 * @param memberIds 成员ids
	 * @param roleId 角色id
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/roles/set")
	public Object rolesSet(
			@RequestBody JSONObject jsonParam,
			HttpServletRequest request) throws Exception{
		
		Long groupId = Long.parseLong(jsonParam.getString("groupId"));
		String memberIds = jsonParam.getString("memberIds");
		List<Long> memberArray = JSONArray.parseArray(memberIds, Long.class);
		Long roleId = Long.parseLong(jsonParam.getString("roleId"));
		
		deviceGroupServiceImpl.rolesSet(groupId, memberArray, roleId);
		return null;		
	}
	
	/**
	 * 移除角色中的一个成员<br/>
	 * @param groupId 设备组id
	 * @param memberId 成员id
	 * @param roleId 角色id
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/roles/member/remove")
	public Object rolesRemove(
			@RequestBody JSONObject jsonParam,
			HttpServletRequest request) throws Exception{
		
		Long groupId = Long.parseLong(jsonParam.getString("groupId"));
		Long memberId = Long.parseLong(jsonParam.getString("memberId"));
		Long roleId = Long.parseLong(jsonParam.getString("roleId"));
		
		List<Long> memberIds = new ArrayList<Long>();
		memberIds.add(memberId);
		deviceGroupServiceImpl.rolesRemove(groupId, memberIds, roleId);
		
		return null;
	}
	
	/**
	 * 移除角色中的所有成员<br/>
	 * @param groupId 设备组id
	 * @param roleId 角色id
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/roles/remove")
	public Object roleMembersRemove(
			@RequestBody JSONObject jsonParam,
			HttpServletRequest request) throws Exception{
		
		Long groupId = Long.parseLong(jsonParam.getString("groupId"));
		Long roleId = Long.parseLong(jsonParam.getString("roleId"));
		
		deviceGroupServiceImpl.roleMembersRemove(groupId, roleId);
		
		return null;
	}
	
	/**
	 * 移发言人所有成员<br/>
	 * @param groupId 设备组id
	 * @param roleId 角色id
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/spokesman/remove")
	public Object spokesmanMembersRemove(
			@RequestBody JSONObject jsonParam,
			HttpServletRequest request) throws Exception{
		
		Long groupId = Long.parseLong(jsonParam.getString("groupId"));
		Long roleId = Long.parseLong(jsonParam.getString("roleId"));
		
		deviceGroupServiceImpl.spokesmanMembersRemove(groupId, roleId);
		
		return null;
	}
	
	/**
	 * @Title: 在会议中新建角色
	 * @param groupId
	 * @param name
	 * @param special
	 * @param type
	 * @param request
	 * @throws Exception
	 * @return DeviceGroupBusinessRoleParamVO
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/save/new/role")
	public Object saveNewRole(
			@RequestBody JSONObject jsonParam,
			HttpServletRequest request) throws Exception{
		
		Long groupId = Long.parseLong(jsonParam.getString("groupId"));
		String name = jsonParam.getString("name");
		String special = jsonParam.getString("special");
		String type = jsonParam.getString("type");
		
		DeviceGroupBusinessRolePO role = meetingServiceImpl.saveNewRole(groupId, name, special, type);
		
		DeviceGroupBusinessRoleParamVO _role = new DeviceGroupBusinessRoleParamVO().set(role);
		
		return _role;
	}
	
	/**
	 * @Title: 删除会议中的角色
	 * @param groupId
	 * @param roleId
	 * @param request
	 * @throws Exception
	 * @return 结果
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/remove/role")
	public Object removeRole(
			@RequestBody JSONObject jsonParam,
			HttpServletRequest request) throws Exception{
		
		Long groupId = Long.parseLong(jsonParam.getString("groupId"));
		Long roleId = Long.parseLong(jsonParam.getString("roleId"));
		
		meetingServiceImpl.removeRole(groupId, roleId);
		
		return null;
	}
	
	/**
	 * @Title: 修改会议中的角色
	 * @param groupId
	 * @param roleId
	 * @param name
	 * @param special
	 * @param type
	 * @param request
	 * @throws Exception
	 * @return DeviceGroupBusinessRoleParamVO
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/update/role")
	public Object updateRole(
			@RequestBody JSONObject jsonParam,
			HttpServletRequest request) throws Exception{
		
		Long groupId = Long.parseLong(jsonParam.getString("groupId"));
		Long roleId = Long.parseLong(jsonParam.getString("roleId"));
		String name = jsonParam.getString("name");
		String special = jsonParam.getString("special");
		String type = jsonParam.getString("type");
		
		DeviceGroupBusinessRolePO role = meetingServiceImpl.updateRole(groupId, roleId, name, special, type);
		
		DeviceGroupBusinessRoleParamVO _role = new DeviceGroupBusinessRoleParamVO().set(role);
		
		return _role;
	}
	
	/**找到根节点*/
	public List<FolderBO> findRoots(List<FolderBO> folders){
		List<FolderBO> roots = new ArrayList<FolderBO>();
		for(FolderBO folder:folders){
			if(folder!=null && (folder.getParentId()==null || folder.getParentId()==TreeNodeVO.FOLDERID_ROOT)){
				roots.add(folder);
			}
		}
		return roots;
	}
	
	/**递归组文件夹层级*/
	public void recursionFolder(
			TreeNodeVO root, 
			List<FolderBO> folders, 
			List<BundleBO> bundles, 
			List<ChannelBO> channels,
			List<BundleScreenBO> screens){
		
		//往里装文件夹
		for(FolderBO folder:folders){
			if(folder.getParentId()!=null && folder.getParentId().toString().equals(root.getId())){
				TreeNodeVO folderNode = new TreeNodeVO().set(folder)
														.setChildren(new ArrayList<TreeNodeVO>());
				root.getChildren().add(folderNode);
				recursionFolder(folderNode, folders, bundles, channels, screens);
			}
		}
		
		//往里装设备
		for(BundleBO bundle:bundles){
			if(bundle.getFolderId().toString().equals(root.getId())){
				TreeNodeVO bundleNode = new TreeNodeVO().set(bundle)
														.setChildren(new ArrayList<TreeNodeVO>())
														.setScreens(new ArrayList<TreeNodeVO>());
				root.getChildren().add(bundleNode);
				if(channels!=null && channels.size()>0){
					for(ChannelBO channel:channels){
						if(channel.getBundleId().equals(bundleNode.getId())){
							TreeNodeVO channelNode = new TreeNodeVO().set(channel, bundle);
							bundleNode.getChildren().add(channelNode);
						}
					}
				}
				if(screens != null && screens.size()>0){
					for(BundleScreenBO screen: screens){
						if(screen.getBundleId().equals(bundleNode.getId())){
							TreeNodeVO screenNode = new TreeNodeVO().set(screen);
							bundleNode.getScreens().add(screenNode);
						}
					}
				}
			}
		}
	}
}

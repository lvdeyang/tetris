package com.sumavision.tetris.bvc.business.call;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.PlayerBundleBO;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.dao.FolderUserMapDAO;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.FolderUserMap;
import com.suma.venus.resource.service.ResourceRemoteService;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.command.group.dao.CommandGroupUserInfoDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupUserPlayerDAO;
import com.sumavision.bvc.command.group.dao.CommandVodDAO;
import com.sumavision.bvc.command.group.dao.UserLiveCallDAO;
import com.sumavision.bvc.command.group.enumeration.CallStatus;
import com.sumavision.bvc.command.group.enumeration.CallType;
import com.sumavision.bvc.command.group.enumeration.SchemeType;
import com.sumavision.bvc.command.group.enumeration.UserCallType;
import com.sumavision.bvc.command.group.enumeration.VodType;
import com.sumavision.bvc.command.group.user.CommandGroupUserInfoPO;
import com.sumavision.bvc.command.group.user.UserLiveCallPO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.command.group.user.layout.player.PlayerBusinessType;
import com.sumavision.bvc.command.group.user.layout.scheme.CommandGroupUserLayoutShemePO;
import com.sumavision.bvc.command.group.user.layout.scheme.PlayerSplitLayout;
import com.sumavision.bvc.command.group.vod.CommandVodPO;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.device.command.cast.CommandCastServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonConstant;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.command.exception.PlayerIsBeingUsedException;
import com.sumavision.bvc.device.command.exception.UserDoesNotLoginException;
import com.sumavision.bvc.device.command.exception.UserHasNoAvailableEncoderException;
import com.sumavision.bvc.device.command.exception.UserNotMatchBusinessException;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.ConnectBO;
import com.sumavision.bvc.device.group.bo.ConnectBundleBO;
import com.sumavision.bvc.device.group.bo.DisconnectBundleBO;
import com.sumavision.bvc.device.group.bo.ForwardSetSrcBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.PassByBO;
import com.sumavision.bvc.device.group.bo.XtBusinessPassByContentBO;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplPO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.CommonQueryUtil;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.device.group.service.util.ResourceQueryUtil;
import com.sumavision.bvc.device.monitor.live.user.MonitorLiveUserDAO;
import com.sumavision.bvc.device.monitor.live.user.MonitorLiveUserPO;
import com.sumavision.bvc.feign.ResourceServiceClient;
import com.sumavision.bvc.resource.dao.ResourceBundleDAO;
import com.sumavision.bvc.resource.dao.ResourceChannelDAO;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.bvc.system.po.AvtplGearsPO;
import com.sumavision.bvc.system.po.AvtplPO;
import com.sumavision.tetris.auth.token.TerminalType;
import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.bvc.business.ExecuteStatus;
import com.sumavision.tetris.bvc.business.bo.SourceBO;
import com.sumavision.tetris.bvc.business.common.BusinessCommonService;
import com.sumavision.tetris.bvc.business.dao.CommonForwardDAO;
import com.sumavision.tetris.bvc.business.dao.GroupDAO;
import com.sumavision.tetris.bvc.business.dao.GroupMemberDAO;
import com.sumavision.tetris.bvc.business.dao.GroupMemberRolePermissionDAO;
import com.sumavision.tetris.bvc.business.dao.UserCallDAO;
import com.sumavision.tetris.bvc.business.forward.CommonForwardPO;
import com.sumavision.tetris.bvc.business.group.BusinessType;
import com.sumavision.tetris.bvc.business.group.GroupMemberPO;
import com.sumavision.tetris.bvc.business.group.GroupMemberRolePermissionPO;
import com.sumavision.tetris.bvc.business.group.GroupMemberStatus;
import com.sumavision.tetris.bvc.business.group.GroupMemberType;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.bvc.business.group.GroupService;
import com.sumavision.tetris.bvc.business.group.GroupStatus;
import com.sumavision.tetris.bvc.model.agenda.AgendaDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaExecuteService;
import com.sumavision.tetris.bvc.model.agenda.AgendaPO;
import com.sumavision.tetris.bvc.model.agenda.AgendaService;
import com.sumavision.tetris.bvc.model.role.InternalRoleType;
import com.sumavision.tetris.bvc.model.role.RoleDAO;
import com.sumavision.tetris.bvc.model.role.RolePO;
import com.sumavision.tetris.bvc.model.terminal.TerminalDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalPO;
import com.sumavision.tetris.bvc.page.PageTaskDAO;
import com.sumavision.tetris.bvc.page.PageTaskPO;
import com.sumavision.tetris.bvc.page.PageTaskService;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;
import com.sumavision.tetris.websocket.message.WebsocketMessageType;
import com.sumavision.tetris.websocket.message.WebsocketMessageVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserCallService {
	
	@Autowired 
	private CommandCommonUtil commandCommonUtil;
	
	@Autowired 
	private TerminalDAO terminalDao;
	
	@Autowired 
	private PageTaskDAO pageTaskDao;
	
	@Autowired 
	private CommonForwardDAO commonForwardDao;
	
	@Autowired 
	private CommandGroupUserInfoDAO commandGroupUserInfoDao;
	
	@Autowired 
	private MonitorLiveUserDAO monitorLiveUserDao;
	
	@Autowired 
	private AgendaExecuteService agendaExecuteService;
	
	@Autowired 
	private GroupService groupService;
	
	@Autowired 
	private ResourceService resourceService;
	
	@Autowired
	private CommonQueryUtil commonQueryUtil;
	
	@Autowired
	private ResourceQueryUtil resourceQueryUtil;
	
	@Autowired
	private QueryUtil queryUtil;
	
	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private GroupDAO groupDao;
	
	@Autowired
	private GroupMemberDAO groupMemberDao;
	
	@Autowired
	private UserCallDAO userCallDao;
	
	@Autowired
	private RoleDAO roleDao;
	
	@Autowired
	private GroupMemberRolePermissionDAO groupMemberRolePermissionDao;
	
	@Autowired
	private ResourceBundleDAO resourceBundleDao;
	
	@Autowired
	private ResourceChannelDAO resourceChannelDao;
	
	@Autowired
	private PageTaskService pageTaskService;	
	
	@Autowired
	private BusinessCommonService businessCommonService;
	
	@Autowired
	private CommandCastServiceImpl commandCastServiceImpl;
	
	@Autowired
	private CommandCommonServiceImpl commandCommonServiceImpl;
	
	@Autowired
	private CommandGroupUserPlayerDAO commandGroupUserPlayerDao;
	
	@Autowired
	private AgendaDAO agendaDao;	
	
	@Autowired
	private UserLiveCallDAO userLiveCallDao;
	
	@Autowired
	private CommandVodDAO commandVodDao;
	
	@Autowired
	private FolderUserMapDAO folderUserMapDao;
	
	@Autowired
	private WebsocketMessageService websocketMessageService;
	
	@Autowired
	private ResourceRemoteService resourceRemoteService;
	
	@Autowired
	private ResourceServiceClient resourceServiceClient;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	/** 重构呼叫 */
	@Transactional(rollbackFor = Exception.class)
	public void userCallUser(UserBO callUser, UserBO calledUser, int locationIndex, String uuid, Long formerVodId) throws Exception{
				
		if(callUser.getId().equals(calledUser.getId())){
			throw new BaseException(StatusCode.FORBIDDEN, "请选择其他成员进行呼叫");
		}
		
		//被呼叫校验
		if(!calledUser.isLogined()){
//			throw new UserDoesNotLoginException(calledUser.getName());
		}
		
		FolderUserMap callUserfolderUserMap = folderUserMapDao.findByUserId(callUser.getId());
		boolean bCallUserLdap = queryUtil.isLdapUser(callUser, callUserfolderUserMap);
		
		FolderUserMap calledUserfolderUserMap = folderUserMapDao.findByUserId(calledUser.getId());
		boolean bCalledUserLdap = queryUtil.isLdapUser(callUser, calledUserfolderUserMap);
		
		CallType callType = null;
		if(!bCallUserLdap && !bCalledUserLdap){
			callType = CallType.LOCAL_LOCAL;
		}else if(!bCallUserLdap && bCalledUserLdap){
			callType = CallType.LOCAL_OUTER;
		}else if(bCallUserLdap && !bCalledUserLdap){
			callType = CallType.OUTER_LOCAL;//外部系统呼入
		}else{
			throw new BaseException(StatusCode.FORBIDDEN, "不支持外部用户呼叫外部用户");
		}
				
		//校验呼叫业务是否已经存在
		UserCallPO exsitCall = userCallDao.findByCalledUserIdAndCallUserId(calledUser.getId(), callUser.getId());
		if(exsitCall != null){
			
			if(uuid != null){
				exsitCall.setUuid(uuid);
			}
			userCallDao.save(exsitCall);
			
			if(exsitCall.getType().equals(UserCallType.VOICE)){
				throw new BaseException(StatusCode.FORBIDDEN, "双方正在语音对讲！");
			}
			
			//消息重发
			if(exsitCall.getMessageId() != null){
				websocketMessageService.resend(exsitCall.getMessageId());
			}
			
			return;
		}
		
		//建立业务组
		GroupPO group = new GroupPO();
		if(uuid != null){
			group.setUuid(uuid);
		}
		group.setName(callUser.getName() + "呼叫" + calledUser.getName());
		group.setBusinessType(BusinessType.CALL);
//				group.setOriginType(originType);		
		group.setUserId(callUser.getId());
		group.setUserName(callUser.getName());
		group.setCreatetime(new Date());
		group.setStatus(GroupStatus.PAUSE);
		groupDao.save(group);
		
		UserCallPO call = new UserCallPO();
		call.setCallType(callType);
		call.setType(UserCallType.CALL);
		call.setCallUserId(callUser.getId());
		call.setCallUsername(callUser.getName());
		call.setCallUserNo(callUser.getUserNo());
		call.setCalledUserId(calledUser.getId());
		call.setCalledUsername(calledUser.getName());
		call.setCalledUserNo(calledUser.getUserNo());
		call.setFormerVodId(formerVodId);
		call.setGroupId(group.getId());
//		if(uuid != null){
//			call.setUuid(uuid);
//		}
		userCallDao.save(call);
		
		//建立成员
		TerminalPO terminal = terminalDao.findByType(com.sumavision.tetris.bvc.model.terminal.TerminalType.QT_ZK);
		
		//主叫用户作为成员
//		UserBO callUser = userUtils.queryUserById(call.getCallUserId());
		GroupMemberPO callMemberPO = new GroupMemberPO();
		callMemberPO.setName(callUser.getName());
		callMemberPO.setGroupMemberType(GroupMemberType.MEMBER_USER);
		callMemberPO.setOriginId(callUser.getId().toString());
		callMemberPO.setTerminalId(terminal.getId());
		callMemberPO.setFolderId(callUser.getFolderId());
		callMemberPO.setGroupMemberStatus(GroupMemberStatus.CONNECT);
		callMemberPO.setGroupId(group.getId());
		groupMemberDao.save(callMemberPO);
		
		//被叫用户作为成员
		GroupMemberPO calledMemberPO = new GroupMemberPO();
		calledMemberPO.setName(calledUser.getName());
		calledMemberPO.setGroupMemberType(GroupMemberType.MEMBER_USER);
		calledMemberPO.setOriginId(calledUser.getId().toString());
		calledMemberPO.setTerminalId(terminal.getId());
		calledMemberPO.setFolderId(calledUser.getFolderId());
		calledMemberPO.setGroupMemberStatus(GroupMemberStatus.CONNECT);
		calledMemberPO.setGroupId(group.getId());
		groupMemberDao.save(calledMemberPO);
		
		//把成员授权给角色
		RolePO callRole = roleDao.findByInternalRoleType(InternalRoleType.CALL_USER);
		GroupMemberRolePermissionPO callRolePermission = new GroupMemberRolePermissionPO(callRole.getId(), callMemberPO.getId());
		groupMemberRolePermissionDao.save(callRolePermission);
//				RolePO srcRole = roleDao.findByInternalRoleType(InternalRoleType.CALL_USER);
		GroupMemberRolePermissionPO calledRolePermission = new GroupMemberRolePermissionPO(callRole.getId(), calledMemberPO.getId());
		groupMemberRolePermissionDao.save(calledRolePermission);
		
		//呼叫编码
		List<SourceBO> sourceBOs = agendaExecuteService.obtainSource(new ArrayListWrapper<GroupMemberPO>().add(callMemberPO).add(calledMemberPO).getList(), group.getId().toString(), BusinessInfoType.PLAY_VOD);
		CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();
		LogicBO logic = groupService.openEncoder(sourceBOs, codec, -1L);
		executeBusiness.execute(logic, group.getName() + "接听，打开编码");
		
		//执行议程
		AgendaPO agenda = agendaDao.findByBusinessInfoType(BusinessInfoType.USER_CALL);
		agendaExecuteService.runAndStopAgenda(group.getId(), new ArrayListWrapper<Long>().add(agenda.getId()).getList(), null);//所有业务都使用groupPO
		
		if(!bCalledUserLdap){
			
			//发通知
			JSONObject message = new JSONObject();
			message.put("businessType", CommandCommonConstant.MESSAGE_CALL);
			message.put("fromUserId", callUser.getId());
			message.put("fromUserName", callUser.getName());
			message.put("businessId", group.getId());
			message.put("businessInfo", callUser.getName() + "邀请你视频通话");
						
			//发送呼叫消息
			WebsocketMessageVO ws = websocketMessageService.send(calledUser.getId(), message.toJSONString(), WebsocketMessageType.COMMAND);
			call.setMessageId(ws.getId());
			userCallDao.save(call);
		}else{
			
			/*LogicBO logic = new LogicBO().setUserId("-1")
			 		 .setPass_by(new ArrayList<PassByBO>());
			
			//参数模板
			CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();
			
			String localLayerId = resourceRemoteService.queryLocalLayerId();
			XtBusinessPassByContentBO passByContent = new XtBusinessPassByContentBO().setCmd(XtBusinessPassByContentBO.CMD_LOCAL_CALL_XT_USER)
								 .setOperate(XtBusinessPassByContentBO.OPERATE_START)
								 .setUuid(call.getUuid())
								 .setSrc_user(call.getCallUserNo())
								 .setLocal_encoder(new HashMapWrapper<String, String>().put("layerid", business.getCallEncoderLayerId())
										 											   .put("bundleid", business.getCallEncoderBundleId())
										                         					   .put("video_channelid", business.getCallEncoderVideoChannelId())
										                         					   .put("audio_channelid", business.getCallEncoderAudioChannelId())
										 											   .getMap())
								 .setXt_encoder(new HashMapWrapper<String, String>().put("layerid", business.getCalledEncoderLayerId())
										 											.put("bundleid", business.getCalledEncoderBundleId())
										 											.put("video_channelid", business.getCalledEncoderVideoChannelId())
										 											.put("audio_channelid", business.getCalledEncoderAudioChannelId())
										 											.getMap())
								 .setDst_number(business.getCalledUserno())
								 .setVparam(codec);
			
			PassByBO passby = new PassByBO().setLayer_id(localLayerId)
			.setType(XtBusinessPassByContentBO.CMD_LOCAL_CALL_XT_USER)
			.setPass_by_content(passByContent);
			
			logic.getPass_by().add(passby);
			
			executeBusiness.execute(logic, "本地用户呼叫外部系统用户");*/
		}
	}
	
	/** 在controller加锁 */
	@Transactional(rollbackFor = Exception.class)
	public void acceptCall(UserBO user, Long businessId, String uuid) throws Exception{
		
		GroupPO group = null;
		if(businessId != null){
			group = groupDao.findOne(businessId);
		}else{
			group = groupDao.findByUuid(uuid);
			if(group == null) return;
			businessId = group.getId();
		}
		
		if(group == null){
			throw new BaseException(StatusCode.FORBIDDEN, "对方已挂断");
		}
		UserCallPO call = userCallDao.findByGroupId(group.getId());
		
		
		//校验操作人是否是被叫用户
		if(!user.getId().equals(call.getCalledUserId())){
			throw new UserNotMatchBusinessException(user.getName(), businessId, PlayerBusinessType.USER_CALL.getName());
		}
		
		UserBO admin = new UserBO(); admin.setId(-1L);
		
		//消息消费
		try{
			websocketMessageService.consume(call.getMessageId());
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("消息消费异常，id：" + call.getMessageId());
		}
		
		List<GroupMemberPO> members = groupMemberDao.findByGroupId(businessId);

		/*
		//建立成员
		//主叫用户作为成员
		UserBO callUser = userUtils.queryUserById(call.getCallUserId());
		GroupMemberPO callMemberPO = new GroupMemberPO();
		callMemberPO.setName(callUser.getName());
		callMemberPO.setGroupMemberType(GroupMemberType.MEMBER_USER);
		callMemberPO.setOriginId(callUser.getId().toString());
//				userMemberPO.setTerminalId(terminalId);//TODO
		callMemberPO.setFolderId(callUser.getFolderId());
		callMemberPO.setGroupMemberStatus(GroupMemberStatus.CONNECT);
		callMemberPO.setGroupId(group.getId());
		groupMemberDao.save(callMemberPO);
		
		//被叫用户作为成员
		GroupMemberPO calledMemberPO = new GroupMemberPO();
		calledMemberPO.setName(user.getName());
		calledMemberPO.setGroupMemberType(GroupMemberType.MEMBER_USER);
		calledMemberPO.setOriginId(user.getId().toString());
//		vodUserMemberPO.setTerminalId(terminalId);//TODO
		calledMemberPO.setFolderId(user.getFolderId());
		calledMemberPO.setGroupMemberStatus(GroupMemberStatus.CONNECT);
		calledMemberPO.setGroupId(group.getId());
		groupMemberDao.save(calledMemberPO);
		
		//把成员授权给角色
		RolePO callRole = roleDao.findByInternalRoleType(InternalRoleType.CALL_USER);
		GroupMemberRolePermissionPO callRolePermission = new GroupMemberRolePermissionPO(callRole.getId(), callMemberPO.getId());
		groupMemberRolePermissionDao.save(callRolePermission);
//		RolePO srcRole = roleDao.findByInternalRoleType(InternalRoleType.CALL_USER);
		GroupMemberRolePermissionPO calledRolePermission = new GroupMemberRolePermissionPO(callRole.getId(), calledMemberPO.getId());
		groupMemberRolePermissionDao.save(calledRolePermission);*/
		
		//呼叫编码
//		List<SourceBO> sourceBOs = agendaService.obtainSource(new ArrayListWrapper<GroupMemberPO>().add(callMemberPO).add(calledMemberPO).getList(), group.getId().toString(), BusinessInfoType.PLAY_VOD);		
		List<SourceBO> sourceBOs = agendaExecuteService.obtainSource(members, group.getId().toString(), BusinessInfoType.PLAY_VOD);
		CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();
		LogicBO logic = groupService.openEncoder(sourceBOs, codec, -1L);
		executeBusiness.execute(logic, group.getName() + "接听，打开编码");
		
//		//执行议程
//		AgendaPO agenda = null;//TODO
//		agendaService.runAndStopAgenda(group.getId(), new ArrayListWrapper<Long>().add(agenda.getId()).getList(), null);//所有业务都使用groupPO
		
		//查出所有分页任务，执行状态都置为DONE，查出所有forward，执行状态置为DONE，分页处理
		List<PageTaskPO> changeTasks = new ArrayList<PageTaskPO>();
		List<PageTaskPO> tasks = pageTaskDao.findByBusinessId(businessId.toString());
		List<CommonForwardPO> forwards = commonForwardDao.findByBusinessId(businessId.toString());
		for(CommonForwardPO forward : forwards){
			forward.setVideoStatus(ExecuteStatus.DONE);
			forward.setAudioStatus(ExecuteStatus.DONE);
		}
		for(PageTaskPO task : tasks){
			boolean change = false;
			if(ExecuteStatus.UNDONE.equals(task.getAudioStatus())){
				task.setAudioStatus(ExecuteStatus.DONE);
				change = true;
			}
			if(ExecuteStatus.UNDONE.equals(task.getVideoStatus())){
				task.setVideoStatus(ExecuteStatus.DONE);
				change = true;
			}
			if(change){
				changeTasks.add(task);
			}
		}
		
		//持久化
		group.setStatus(GroupStatus.START);
		groupDao.save(group);
		pageTaskDao.save(tasks);
		commonForwardDao.save(forwards);		
		
//		List<GroupMemberPO> members = groupMemberDao.findByGroupId(groupId);
//		List<GroupMemberPO> connectMembers = tetrisBvcQueryUtil.queryConnectMembers(members);
		
		pageTaskService.reExecute(changeTasks, true);
		
		
//		group.setStatus(GroupStatus.START);
//		groupDao.save(group);
		
		call.setGroupId(group.getId());
		call.setStatus(CallStatus.ONGOING);
		userCallDao.save(call);		
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void stopCall(UserBO user, Long businessId, String uuid) throws Exception{
		
		GroupPO group = null;
		if(businessId != null){
			group = groupDao.findOne(businessId);
		}else{
			group = groupDao.findByUuid(uuid);
			if(group == null) return;
			businessId = group.getId();
		}
		
		if(group == null){
			throw new BaseException(StatusCode.FORBIDDEN, "通话已挂断");
		}
		UserCallPO call = userCallDao.findByGroupId(group.getId());
				
		//判断发起人是不是通话中的任何一个人
		if(!user.getId().equals(call.getCalledUserId()) && !user.getId().equals(call.getCallUserId())){
			throw new UserNotMatchBusinessException(user.getName(), businessId, PlayerBusinessType.USER_CALL.getName());
		}
		
		//根据接听与否来处理
//		if(call.getStatus().equals(CallStatus.WAITING_FOR_RESPONSE)){
//			userCallDao.delete(call);
//			return;
//		}
		
		//查出所有数据
		List<GroupMemberPO> members = groupMemberDao.findByGroupId(group.getId());
		List<Long> memberIds = businessCommonService.obtainMemberIds(members);
		List<GroupMemberRolePermissionPO> ps = groupMemberRolePermissionDao.findByGroupMemberIdIn(memberIds);
		CallType callType = call.getCallType();
		
		//如果被叫是本系统用户
		if(CallType.LOCAL_LOCAL.equals(callType)
				|| CallType.OUTER_LOCAL.equals(callType)){
			
			//如果用户是呼叫方
			if(user.getId().equals(call.getCallUserId())){
				
				//给被叫方发消息
				JSONObject message = new JSONObject();
				message.put("businessType", CommandCommonConstant.MESSAGE_CALL_STOP);
				message.put("fromUserId", user.getId());
				message.put("fromUserName", user.getName());
				message.put("businessInfo", user.getName() + "挂断了视频通话");
				message.put("serial", -1);
				
				WebsocketMessageVO ws = websocketMessageService.send(call.getCalledUserId(), message.toJSONString(), WebsocketMessageType.COMMAND);
				websocketMessageService.consume(ws.getId());
			}
		}

		//如果呼叫方是本系统用户
		if(CallType.LOCAL_LOCAL.equals(callType)
				|| CallType.LOCAL_OUTER.equals(callType)){
			
			//如果用户是被呼叫方
			if(user.getId().equals(call.getCalledUserId())){
				
				//给呼叫方发消息
				JSONObject message = new JSONObject();
				message.put("businessType", CommandCommonConstant.MESSAGE_CALL_STOP);
				message.put("fromUserId", user.getId());
				message.put("fromUserName", user.getName());
				message.put("businessInfo", user.getName() + "挂断了视频通话");
				message.put("serial", -1);
				
				WebsocketMessageVO ws = websocketMessageService.send(call.getCallUserId(), message.toJSONString(), WebsocketMessageType.COMMAND);
				websocketMessageService.consume(ws.getId());
			}
		}
		
		//停止编码
		if(call.getStatus().equals(CallStatus.ONGOING) || call.getStatus().equals(CallStatus.PAUSE)){
			List<SourceBO> sourceBOs = agendaExecuteService.obtainSource(new ArrayListWrapper<GroupMemberPO>().add(members.get(0)).add(members.get(1)).getList(), group.getId().toString(), BusinessInfoType.PLAY_VOD);
			CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();
			LogicBO logic = groupService.closeEncoder(sourceBOs, codec, -1L);
			executeBusiness.execute(logic, group.getName() + "停止，关闭编码");
		}
		
		//停止议程
		AgendaPO agenda = agendaDao.findByBusinessInfoType(BusinessInfoType.USER_CALL);
		agendaExecuteService.runAndStopAgenda(group.getId(), null, new ArrayListWrapper<Long>().add(agenda.getId()).getList());//所有业务都使用groupPO
		
		//删除数据		
		userCallDao.delete(call);
		groupDao.delete(group);
		groupMemberDao.deleteInBatch(members);
		groupMemberRolePermissionDao.deleteInBatch(ps);
	}
	
	/**
	 * 由本系统的客户端发起，将点播用户转为呼叫<br/>
	 * <p>点播转呼叫注意事项：<br/>
		 * 		同意后，不呼被叫编码，可以不用呼叫主叫解码（体现在openBundle）<br/>
		 * 		停止（拒绝也是停止）：如果已经开始，那么正常停止；如果没有开始，则需要挂断被叫编码，挂断主叫解码（体现在closeBundle）</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月30日 下午1:15:22
	 * @param vodId 点播id
	 * @param locationIndex 播放器序号
	 * @return
	 * @throws Exception
	 */
	public CommandGroupUserPlayerPO transVodToCall(Long vodId, int locationIndex) throws Exception{
		
		/**
		 * 点播转呼叫注意事项：
		 * 		同意后，不呼被叫编码，可以不用呼叫主叫解码（体现在openBundle）
		 * 		停止（拒绝也是停止）：如果已经开始，那么正常停止；如果没有开始，则需要挂断被叫编码，挂断主叫解码（体现在closeBundle）
		 */
				
		if(vodId == null){
			throw new BaseException(StatusCode.FORBIDDEN, "用户点播id有误");
		}
		
		CommandVodPO vod = commandVodDao.findOne(vodId);
		if(vod == null){
			throw new BaseException(StatusCode.FORBIDDEN, "用户点播不存在！id：" + vodId);
		}
		
		//加一个vodType校验
		VodType vodType = vod.getVodType();
		if(VodType.USER.equals(vodType) || VodType.USER_ONESELF.equals(vodType) || VodType.LOCAL_SEE_OUTER_USER.equals(vodType)){			
		}else{
			throw new BaseException(StatusCode.FORBIDDEN, "请选择用户进行呼叫");
		}
		//加一个播放器业务校验
		
		UserBO callUser = userUtils.queryUserById(vod.getDstUserId());
		UserBO calledUser = userUtils.queryUserById(vod.getSourceUserId());
		
		CommandGroupUserPlayerPO callUserPlayer = userCallUser_Cascade(callUser, calledUser, locationIndex, vod.getUuid(), vodId);

		commandVodDao.delete(vod);
		
		log.info(callUser.getName() + " 点播 " + calledUser.getName() + " 用户，被转换为呼叫，等待对方接听");
		
		return callUserPlayer;
		
	}
	
	/**
	 * 被联网触发。把“被外部系统的点播”，转为“被外部系统呼叫”<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月30日 下午1:14:46
	 * @param uuid 点播uuid
	 * @param calledUser 本地被叫用户
	 * @param callUser 外部系统主叫用户
	 * @throws Exception
	 */
	public void transOuterVodInnerToCall(String uuid, UserBO calledUser, UserBO callUser) throws Exception{
		
		log.info("外部系统用户 " + callUser.getName() + "点播本系统用户 " + calledUser.getName() + " ，被转换为呼叫");
		
		//去92的表里找到点播用户，直接删掉
//		CommandVodPO vod = commandVodDao.findByUuid(uuid);
		MonitorLiveUserPO liveUser = monitorLiveUserDao.findByUuid(uuid);
		monitorLiveUserDao.delete(liveUser);
		
		//校验
		
		userCallUser_Cascade(callUser, calledUser, -1, uuid);
	}
	
	public CommandGroupUserPlayerPO userCallUser_Cascade(UserBO callUser, UserBO calledUser, int locationIndex, String uuid) throws Exception{
		return userCallUser_Cascade(callUser, calledUser, locationIndex, uuid, null);
	}
	
	/**
	 * 用户呼叫用户 - 支持级联<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月6日 下午3:43:19
	 * @param callUser 呼叫方用户
	 * @param calledUser 被呼叫用户
	 * @param locationIndex 仅当被叫用户为本地用户时，可以指定播放器序号，序号从0起始；-1为自动选择，当被叫用户为外部系统用户时使用-1
	 * @param uuid 外部系统呼入时，联网生成uuid；内部系统使用必须为null
	 * @param formerVodId 默认null。这是从点播转为呼叫时，记录点播的id
	 * @return CommandGroupUserPlayerPO 播放器占用信息
	 * @throws Exception
	 */
	public CommandGroupUserPlayerPO userCallUser_Cascade(UserBO callUser, UserBO calledUser, int locationIndex, String uuid, Long formerVodId) throws Exception{
		
//		commandCommonServiceImpl.authorizeUser(calledUser.getId(), callUser.getId(), BUSINESS_OPR_TYPE.CALL);
		
		if(callUser.getId().equals(calledUser.getId())){
			throw new BaseException(StatusCode.FORBIDDEN, "请选择其他成员进行呼叫");
		}
		
		//被呼叫校验
		if(!calledUser.isLogined()){
			throw new UserDoesNotLoginException(calledUser.getName());
		}
		
		FolderUserMap callUserfolderUserMap = folderUserMapDao.findByUserId(callUser.getId());
		boolean bCallUserLdap = queryUtil.isLdapUser(callUser, callUserfolderUserMap);
		
		FolderUserMap calledUserfolderUserMap = folderUserMapDao.findByUserId(calledUser.getId());
		boolean bCalledUserLdap = queryUtil.isLdapUser(callUser, calledUserfolderUserMap);
		
		CallType callType = null;
		if(!bCallUserLdap && !bCalledUserLdap){
			callType = CallType.LOCAL_LOCAL;
		}else if(!bCallUserLdap && bCalledUserLdap){
			callType = CallType.LOCAL_OUTER;
		}else if(bCallUserLdap && !bCalledUserLdap){
			callType = CallType.OUTER_LOCAL;//外部系统呼入
		}else{
			throw new BaseException(StatusCode.FORBIDDEN, "不支持外部用户呼叫外部用户");
		}
				
		//校验呼叫业务是否已经存在
		UserLiveCallPO exsitCall = userLiveCallDao.findByCalledUserIdAndCallUserId(calledUser.getId(), callUser.getId());
		if(exsitCall != null){
			
			if(uuid != null){
				exsitCall.setUuid(uuid);
			}
			userLiveCallDao.save(exsitCall);
			
			if(exsitCall.getType().equals(UserCallType.VOICE)){
				throw new BaseException(StatusCode.FORBIDDEN, "双方正在语音对讲！");
			}
			
			//消息重发
			if(exsitCall.getMessageId() != null){
				websocketMessageService.resend(exsitCall.getMessageId());
			}
			if(CallType.OUTER_LOCAL.equals(callType)) return null;
			CommandGroupUserInfoPO callUserInfo = commandGroupUserInfoDao.findByUserId(exsitCall.getCallUserId());
			
			CommandGroupUserPlayerPO exsitCallUserPlayer = commandCommonServiceImpl.queryPlayerByBusiness(callUserInfo, PlayerBusinessType.USER_CALL, exsitCall.getId().toString());
			
			return exsitCallUserPlayer;
		}
		
		//呼叫方
		//解码--播放器
		CommandGroupUserPlayerPO callUserPlayer = null;
		BundlePO callEncoderBundleEntity = null;
		ChannelSchemeDTO callEncoderVideoChannel = null;
		ChannelSchemeDTO callEncoderAudioChannel = null;
		if(!bCallUserLdap){
			if(locationIndex == -1){
				callUserPlayer = commandCommonServiceImpl.userChoseUsefulPlayer(callUser.getId(), PlayerBusinessType.USER_CALL);
			}else{
				callUserPlayer = commandCommonServiceImpl.userChosePlayerByLocationIndex(callUser.getId(), PlayerBusinessType.USER_CALL, locationIndex, true);
				//校验是否可以用来点播转呼叫
				if(!isPlayerIdleOrVodUser(callUserPlayer, calledUser)){
					throw new PlayerIsBeingUsedException();
				}
				//此时PlayerBusinessType可能为PLAY_USER，需要改成USER_CALL
				callUserPlayer.setPlayerBusinessType(PlayerBusinessType.USER_CALL);
			}
			
			//呼叫方
			//编码
			List<BundlePO> callEncoderBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(commonQueryUtil.queryExternalOrLocalEncoderIdFromUserBO(callUser)).getList());
			if(callEncoderBundleEntities.size() == 0) throw new UserHasNoAvailableEncoderException(callUser.getName());
			callEncoderBundleEntity = callEncoderBundleEntities.get(0);
			
			List<ChannelSchemeDTO> callEncoderVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(callEncoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_VIDEO);
			if(callEncoderVideoChannels.size() == 0) throw new UserHasNoAvailableEncoderException(callUser.getName());
			callEncoderVideoChannel = callEncoderVideoChannels.get(0);
			
			List<ChannelSchemeDTO> callEncoderAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(callEncoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_AUDIO);
			if(callEncoderAudioChannels.size() == 0) throw new UserHasNoAvailableEncoderException(callUser.getName());
			callEncoderAudioChannel = callEncoderAudioChannels.get(0);
			
		}else{
			//new一个，补充必要参数
			String localLayerId = resourceRemoteService.queryLocalLayerId();
			callUserPlayer = new CommandGroupUserPlayerPO();
			callEncoderBundleEntity = new BundlePO();
			callEncoderBundleEntity.setBundleId(UUID.randomUUID().toString().replace("-", ""));
			callEncoderBundleEntity.setAccessNodeUid(localLayerId);
			callEncoderVideoChannel = new ChannelSchemeDTO().setChannelId(ChannelType.VIDEOENCODE1.getChannelId());
			callEncoderAudioChannel = new ChannelSchemeDTO().setChannelId(ChannelType.AUDIOENCODE1.getChannelId());
		}
		
		//被呼叫
		//解码--播放器
		CommandGroupUserPlayerPO calledUserPlayer = null;
		BundlePO calledEncoderBundleEntity = null;
		ChannelSchemeDTO calledEncoderVideoChannel = null;
		ChannelSchemeDTO calledEncoderAudioChannel = null;
		if(!bCalledUserLdap){
			try{
				calledUserPlayer = commandCommonServiceImpl.userChoseUsefulPlayer(calledUser.getId(), PlayerBusinessType.USER_CALL);
			}catch(Exception e){
				throw new BaseException(StatusCode.FORBIDDEN, "对方用户没有可用的播放器");
			}
			
			//被呼叫
			//编码
			List<BundlePO> calledEncoderBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(commonQueryUtil.queryExternalOrLocalEncoderIdFromUserBO(calledUser)).getList());
			if(calledEncoderBundleEntities.size() == 0) throw new UserHasNoAvailableEncoderException(calledUser.getName());
			calledEncoderBundleEntity = calledEncoderBundleEntities.get(0);
			
			List<ChannelSchemeDTO> calledEncoderVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(calledEncoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_VIDEO);
			if(calledEncoderVideoChannels.size() == 0) throw new UserHasNoAvailableEncoderException(calledUser.getName());
			calledEncoderVideoChannel = calledEncoderVideoChannels.get(0);
			
			List<ChannelSchemeDTO> calledEncoderAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(calledEncoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_AUDIO);
			if(calledEncoderAudioChannels.size() == 0) throw new UserHasNoAvailableEncoderException(calledUser.getName());
			calledEncoderAudioChannel = calledEncoderAudioChannels.get(0);
			
		}else{
			//new一个
			String localLayerId = resourceRemoteService.queryLocalLayerId();
			calledUserPlayer = new CommandGroupUserPlayerPO();
			calledEncoderBundleEntity = new BundlePO();
			calledEncoderBundleEntity.setBundleId(UUID.randomUUID().toString().replace("-", ""));
			calledEncoderBundleEntity.setAccessNodeUid(localLayerId);
			calledEncoderVideoChannel = new ChannelSchemeDTO().setChannelId(ChannelType.VIDEOENCODE1.getChannelId());
			calledEncoderAudioChannel = new ChannelSchemeDTO().setChannelId(ChannelType.AUDIOENCODE1.getChannelId());
		}
		
		UserLiveCallPO business = new UserLiveCallPO(
				calledUser.getId(), calledUser.getUserNo(), calledUser.getName(),
				calledEncoderBundleEntity.getBundleId(), calledEncoderBundleEntity.getBundleName(), calledEncoderBundleEntity.getBundleType(),
				calledEncoderBundleEntity.getAccessNodeUid(), calledEncoderVideoChannel.getChannelId(), calledEncoderVideoChannel.getBaseType(),
				calledEncoderAudioChannel.getChannelId(), calledEncoderAudioChannel.getBaseType(), calledUserPlayer.getBundleId(),
				calledUserPlayer.getBundleName(), calledUserPlayer.getBundleType(), calledUserPlayer.getLayerId(),
				calledUserPlayer.getVideoChannelId(), calledUserPlayer.getVideoBaseType(), calledUserPlayer.getAudioChannelId(),
				calledUserPlayer.getAudioBaseType(), callUser.getId(), callUser.getUserNo(),
				callUser.getName(),callEncoderBundleEntity.getBundleId(), callEncoderBundleEntity.getBundleName(),
				callEncoderBundleEntity.getBundleType(), callEncoderBundleEntity.getAccessNodeUid(), callEncoderVideoChannel.getChannelId(),
				callEncoderVideoChannel.getBaseType(), callEncoderAudioChannel.getChannelId(), callEncoderAudioChannel.getBaseType(),
				callUserPlayer.getBundleId(), callUserPlayer.getBundleName(), callUserPlayer.getBundleType(),
				callUserPlayer.getLayerId(), callUserPlayer.getVideoChannelId(), callUserPlayer.getVideoBaseType(),
				callUserPlayer.getAudioChannelId(), callUserPlayer.getAudioBaseType());
		
		business.setCallType(callType);
		business.setType(UserCallType.CALL);
		business.setFormerVodId(formerVodId);
		if(uuid != null){
			business.setUuid(uuid);
		}
		userLiveCallDao.save(business);
		
		if(!bCallUserLdap){		
			callUserPlayer.setBusinessId(business.getId().toString());
			callUserPlayer.setBusinessName("与" + calledUser.getName() + "双向通话");
			commandGroupUserPlayerDao.save(callUserPlayer);
		}
		
		if(!bCalledUserLdap){
			calledUserPlayer.setBusinessId(business.getId().toString());
			calledUserPlayer.setBusinessName(callUser.getName() + "邀请你视频通话");
			commandGroupUserPlayerDao.save(calledUserPlayer);
			
			JSONObject message = new JSONObject();
			message.put("businessType", CommandCommonConstant.MESSAGE_CALL);
			message.put("fromUserId", callUser.getId());
			message.put("fromUserName", callUser.getName());
			message.put("businessId", business.getId());
			message.put("businessInfo", calledUserPlayer.getBusinessName());
		
			//发送呼叫消息
			WebsocketMessageVO ws = websocketMessageService.send(calledUser.getId(), message.toJSONString(), WebsocketMessageType.COMMAND, callUser.getId(), callUser.getName());
			business.setMessageId(ws.getId());
			userLiveCallDao.save(business);
		}else{
			
			LogicBO logic = new LogicBO().setUserId("-1")
			 		 .setPass_by(new ArrayList<PassByBO>());
			
			//参数模板
			CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();
			
			String localLayerId = resourceRemoteService.queryLocalLayerId();
			XtBusinessPassByContentBO passByContent = new XtBusinessPassByContentBO().setCmd(XtBusinessPassByContentBO.CMD_LOCAL_CALL_XT_USER)
								 .setOperate(XtBusinessPassByContentBO.OPERATE_START)
								 .setUuid(business.getUuid())
								 .setSrc_user(business.getCallUserno())
								 .setLocal_encoder(new HashMapWrapper<String, String>().put("layerid", business.getCallEncoderLayerId())
										 											   .put("bundleid", business.getCallEncoderBundleId())
										                         					   .put("video_channelid", business.getCallEncoderVideoChannelId())
										                         					   .put("audio_channelid", business.getCallEncoderAudioChannelId())
										 											   .getMap())
								 .setXt_encoder(new HashMapWrapper<String, String>().put("layerid", business.getCalledEncoderLayerId())
										 											.put("bundleid", business.getCalledEncoderBundleId())
										 											.put("video_channelid", business.getCalledEncoderVideoChannelId())
										 											.put("audio_channelid", business.getCalledEncoderAudioChannelId())
										 											.getMap())
								 .setDst_number(business.getCalledUserno())
								 .setVparam(codec);
			
			PassByBO passby = new PassByBO().setLayer_id(localLayerId)
			.setType(XtBusinessPassByContentBO.CMD_LOCAL_CALL_XT_USER)
			.setPass_by_content(passByContent);
			
			logic.getPass_by().add(passby);
			
			executeBusiness.execute(logic, "本地用户呼叫外部系统用户");
		}
		
		return callUserPlayer;
	}

	/**
	 * 用户语音对讲<br/>
	 * <b>作者:</b>sm<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月20日 上午11:42:19
	 * @param callUser
	 * @param calledUser
	 * @param locationIndex 指定播放器序号，序号从0起始；-1为自动选择
	 * @return CommandGroupUserPlayerPO 播放器信息
	 */
	public CommandGroupUserPlayerPO userVoiceUser(UserBO callUser, UserBO calledUser, int locationIndex) throws Exception{
		
//		commandCommonServiceImpl.authorizeUser(calledUser.getId(), callUser.getId(), BUSINESS_OPR_TYPE.CALL);
		
		if(callUser.getId().equals(calledUser.getId())){
			throw new BaseException(StatusCode.FORBIDDEN, "请选择其他成员进行呼叫");
		}
		
		//被呼叫校验
		if(!calledUser.isLogined()){
			throw new UserDoesNotLoginException(calledUser.getName());
		}
		
		//校验呼叫业务是否已经存在
		UserLiveCallPO exsitCall = userLiveCallDao.findByCalledUserIdAndCallUserId(calledUser.getId(), callUser.getId());
		if(exsitCall != null){
			
			if(exsitCall.getType().equals(UserCallType.CALL)){
				throw new BaseException(StatusCode.FORBIDDEN, "双方正在视频通话！");
			}
			
			//消息重发
			websocketMessageService.resend(exsitCall.getMessageId());
			
			CommandGroupUserInfoPO callUserInfo = commandGroupUserInfoDao.findByUserId(exsitCall.getCallUserId());
			
			CommandGroupUserPlayerPO exsitCallUserPlayer = commandCommonServiceImpl.queryPlayerByBusiness(callUserInfo, PlayerBusinessType.USER_VOICE, exsitCall.getId().toString());
			
			return exsitCallUserPlayer;
		}
		
		FolderUserMap calledUserfolderUserMap = folderUserMapDao.findByUserId(calledUser.getId());
		boolean bCalledUserLdap = queryUtil.isLdapUser(callUser, calledUserfolderUserMap);
		if(bCalledUserLdap){
			throw new BaseException(StatusCode.FORBIDDEN, "跨系统用户请使用“视频呼叫”");
		}
		
		//呼叫方
		//解码--播放器
		CommandGroupUserPlayerPO callUserPlayer = null;
		if(locationIndex == -1){
			callUserPlayer = commandCommonServiceImpl.userChoseUsefulPlayer(callUser.getId(), PlayerBusinessType.USER_VOICE);
		}else{
			callUserPlayer = commandCommonServiceImpl.userChosePlayerByLocationIndex(callUser.getId(), PlayerBusinessType.USER_VOICE, locationIndex);
		}
		
		//被呼叫
		//解码--播放器
		CommandGroupUserPlayerPO calledUserPlayer = null;
		try{
			calledUserPlayer = commandCommonServiceImpl.userChoseUsefulPlayer(calledUser.getId(), PlayerBusinessType.USER_VOICE);
		}catch(Exception e){
			throw new BaseException(StatusCode.FORBIDDEN, "对方用户没有可用的播放器");
		}
		
		//被呼叫
		//编码
		List<BundlePO> calledEncoderBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(commonQueryUtil.queryExternalOrLocalEncoderIdFromUserBO(calledUser)).getList());
		if(calledEncoderBundleEntities.size() == 0) throw new UserHasNoAvailableEncoderException(calledUser.getName());
		BundlePO calledEncoderBundleEntity = calledEncoderBundleEntities.get(0);
		
		List<ChannelSchemeDTO> calledEncoderAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(calledEncoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_AUDIO);
		if(calledEncoderAudioChannels.size() == 0) throw new UserHasNoAvailableEncoderException(calledUser.getName());
		ChannelSchemeDTO calledEncoderAudioChannel = calledEncoderAudioChannels.get(0);
		
		//呼叫方
		//编码
		List<BundlePO> callEncoderBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(commonQueryUtil.queryExternalOrLocalEncoderIdFromUserBO(callUser)).getList());
		if(callEncoderBundleEntities.size() == 0) throw new UserHasNoAvailableEncoderException(callUser.getName());
		BundlePO callEncoderBundleEntity = callEncoderBundleEntities.get(0);
				
		List<ChannelSchemeDTO> callEncoderAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(callEncoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_AUDIO);
		if(callEncoderAudioChannels.size() == 0) throw new UserHasNoAvailableEncoderException(callUser.getName());
		ChannelSchemeDTO callEncoderAudioChannel = callEncoderAudioChannels.get(0);
		
		UserLiveCallPO business = new UserLiveCallPO(
				calledUser.getId(), calledUser.getUserNo(), calledUser.getName(),
				calledEncoderBundleEntity.getBundleId(), calledEncoderBundleEntity.getBundleName(), calledEncoderBundleEntity.getBundleType(),
				calledEncoderBundleEntity.getAccessNodeUid(), null, null,
				calledEncoderAudioChannel.getChannelId(), calledEncoderAudioChannel.getBaseType(), calledUserPlayer.getBundleId(),
				calledUserPlayer.getBundleName(), calledUserPlayer.getBundleType(), calledUserPlayer.getLayerId(),
				null, null, calledUserPlayer.getAudioChannelId(),
				calledUserPlayer.getAudioBaseType(), callUser.getId(), callUser.getUserNo(),
				callUser.getName(),callEncoderBundleEntity.getBundleId(), callEncoderBundleEntity.getBundleName(),
				callEncoderBundleEntity.getBundleType(), callEncoderBundleEntity.getAccessNodeUid(), null,
				null, callEncoderAudioChannel.getChannelId(), callEncoderAudioChannel.getBaseType(),
				callUserPlayer.getBundleId(), callUserPlayer.getBundleName(), callUserPlayer.getBundleType(),
				callUserPlayer.getLayerId(), null, null,
				callUserPlayer.getAudioChannelId(), callUserPlayer.getAudioBaseType());
		
		business.setType(UserCallType.VOICE);
		userLiveCallDao.save(business);
		
		calledUserPlayer.setBusinessId(business.getId().toString());
		calledUserPlayer.setBusinessName(callUser.getName() + "邀请你语音对讲");
		commandGroupUserPlayerDao.save(calledUserPlayer);
		
		callUserPlayer.setBusinessId(business.getId().toString());
		callUserPlayer.setBusinessName("与" + calledUser.getName() + "语音对讲");
		commandGroupUserPlayerDao.save(callUserPlayer);
		
		JSONObject message = new JSONObject();
		message.put("businessType", CommandCommonConstant.MESSAGE_VOICE);
		message.put("fromUserId", callUser.getId());
		message.put("fromUserName", callUser.getName());
		message.put("businessId", business.getId());
		message.put("businessInfo", calledUserPlayer.getBusinessName());
		
		//发送呼叫消息
		WebsocketMessageVO ws = websocketMessageService.send(calledUser.getId(), message.toJSONString(), WebsocketMessageType.COMMAND, callUser.getId(), callUser.getName());
		business.setMessageId(ws.getId());
		userLiveCallDao.save(business);
		
		return callUserPlayer;
	}
	
	/**
	 * 同意呼叫<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月17日 上午11:18:13
	 * @param Long businessId 业务id
	 * @return CommandGroupUserPlayerPO 播放器信息
	 */
	/*public CommandGroupUserPlayerPO acceptCall(UserBO user, Long businessId, UserBO admin) throws Exception{
		
		UserLiveCallPO call = userLiveCallDao.findOne(businessId);
		
		if(call == null){
			throw new BaseException(StatusCode.FORBIDDEN, "对方已挂断");
		}
		
		if(!user.getId().equals(call.getCalledUserId())){
			throw new UserNotMatchBusinessException(user.getName(), businessId, PlayerBusinessType.USER_CALL.getName());
		}
		
		//消息消费
		try{
			websocketMessageService.consume(call.getMessageId());
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("消息消费异常，id：" + call.getMessageId());
		}
		
		//参数模板
		Map<String, Object> result = commandCommonServiceImpl.queryDefaultAvCodec();
		AvtplPO targetAvtpl = (AvtplPO)result.get("avtpl");
		AvtplGearsPO targetGear = (AvtplGearsPO)result.get("gear");
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
		
		CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(call.getCalledUserId());
		CommandGroupUserPlayerPO player = commandCommonServiceImpl.queryPlayerByBusiness(userInfo, PlayerBusinessType.USER_CALL, businessId.toString());
		
		call.setStatus(CallStatus.ONGOING);
		userLiveCallDao.save(call);
		
		//协议
		LogicBO logic = openBundle(call, codec, admin.getId());
		LogicBO logicCast = commandCastServiceImpl.openBundleCastDevice(null, null, null, null, null, new ArrayListWrapper<UserLiveCallPO>().add(call).getList(), codec, user.getId());
		logic.merge(logicCast);
		
		executeBusiness.execute(logic, call.getCalledUsername() + "接听与" + call.getCallUsername() + "通话");
		
		return player;
	}*/
	
	/** 级联 */
	/*public CommandGroupUserPlayerPO acceptCall_CascadeById(UserBO user, Long id, UserBO admin) throws Exception{
		
		synchronized (new StringBuffer().append(lockProcessPrefix).append(id).toString().intern()) {
			return acceptCall_Cascade(user, id, admin);
		}
	}*/
	
	/** 通常在外部系统接听时调用 */
	/*public CommandGroupUserPlayerPO acceptCall_CascadeByUuid(UserBO user, String uuid) throws Exception{
		
		UserLiveCallPO call = userLiveCallDao.findByUuid(uuid);
		if(call == null){
			throw new BaseException(StatusCode.FORBIDDEN, "呼叫不存在！");
		}
		
		synchronized (new StringBuffer().append(lockProcessPrefix).append(call.getId()).toString().intern()) {
			UserBO admin = new UserBO(); admin.setId(-1L);
			return acceptCall_Cascade(user, call.getId(), admin);
		}
	}*/
	
	/**
	 * 同意呼叫<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月6日 下午5:18:35
	 * @param user 操作人
	 * @param businessId 业务id，与uuid必须有一个null
	 * @param uuid 业务uuid，与businessId必须有一个null
	 * @return CommandGroupUserPlayerPO 播放器信息
	 * @throws Exception
	 */
	public CommandGroupUserPlayerPO acceptCall_Cascade(UserBO user, Long businessId, String uuid) throws Exception{
		
		UserLiveCallPO call = null;
		if(businessId != null){
			call = userLiveCallDao.findOne(businessId);
		}else{
			call = userLiveCallDao.findByUuid(uuid);
			if(call == null) return null;
			businessId = call.getId();
		}
		
		if(call == null){
			throw new BaseException(StatusCode.FORBIDDEN, "对方已挂断");
		}
		
		//校验操作人是否是被叫用户
		if(!user.getId().equals(call.getCalledUserId())){
			throw new UserNotMatchBusinessException(user.getName(), businessId, PlayerBusinessType.USER_CALL.getName());
		}
		
		UserBO admin = new UserBO(); admin.setId(-1L);
		
		//消息消费
		try{
			websocketMessageService.consume(call.getMessageId());
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("消息消费异常，id：" + call.getMessageId());
		}
		
		//参数模板
		Map<String, Object> result = commandCommonServiceImpl.queryDefaultAvCodec();
		AvtplPO targetAvtpl = (AvtplPO)result.get("avtpl");
		AvtplGearsPO targetGear = (AvtplGearsPO)result.get("gear");
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
		
		CallType callType = call.getCallType();
		CommandGroupUserPlayerPO player = null;
		if(callType==null || callType.equals(CallType.LOCAL_LOCAL) || callType.equals(CallType.OUTER_LOCAL)){
			CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(call.getCalledUserId());
			player = commandCommonServiceImpl.queryPlayerByBusiness(userInfo, PlayerBusinessType.USER_CALL, businessId.toString());
		}
		
		call.setStatus(CallStatus.ONGOING);
		userLiveCallDao.save(call);
		
		//协议
		LogicBO logic = openBundle(call, codec, admin.getId());
		LogicBO logicCast = commandCastServiceImpl.openBundleCastDevice(null, null, null, null, null, new ArrayListWrapper<UserLiveCallPO>().add(call).getList(), codec, user.getId());
		logic.merge(logicCast);
		
		executeBusiness.execute(logic, call.getCalledUsername() + "接听与" + call.getCallUsername() + "通话");
		
		return player;
	}


	/**
	 * 拒绝呼叫<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月17日 上午11:25:25
	 * @param user 操作人
	 * @param businessId 业务id
	 */
	/*public void refuseCall(UserBO user, Long businessId) throws Exception{
		
		UserLiveCallPO call = userLiveCallDao.findOne(businessId);
		
		if(call == null){
			throw new BaseException(StatusCode.FORBIDDEN, "对方已挂断");
		}
		
		if(!user.getId().equals(call.getCalledUserId())){
			throw new UserNotMatchBusinessException(user.getName(), businessId, PlayerBusinessType.USER_CALL.getName());
		}
		
		//呼叫方发送的消息消费
		try{
			websocketMessageService.consume(call.getMessageId());
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("消息消费异常，id：" + call.getMessageId());
		}
		
		//外部呼本地用户，直接按照关闭处理
		CallType callType = call.getCallType();
		if(CallType.OUTER_LOCAL.equals(callType)){
			UserBO admin = new UserBO(); admin.setId(-1L);
			stopCall_Cascade(user, businessId, admin);
			return;
		}
		
		//被呼叫方
		CommandGroupUserInfoPO calledUserInfo = commandGroupUserInfoDao.findByUserId(call.getCalledUserId());
		CommandGroupUserPlayerPO calledPlayer = commandCommonServiceImpl.queryPlayerByBusiness(calledUserInfo, PlayerBusinessType.USER_CALL, businessId.toString());
		calledPlayer.setPlayerBusinessType(PlayerBusinessType.NONE);
		calledPlayer.setBusinessId(null);
		calledPlayer.setBusinessName(null);
		commandGroupUserPlayerDao.save(calledPlayer);
		
		//呼叫方
		CommandGroupUserInfoPO callUserInfo = commandGroupUserInfoDao.findByUserId(call.getCallUserId());
		CommandGroupUserPlayerPO callPlayer = commandCommonServiceImpl.queryPlayerByBusiness(callUserInfo, PlayerBusinessType.USER_CALL, businessId.toString());
		callPlayer.setPlayerBusinessType(PlayerBusinessType.NONE);
		callPlayer.setBusinessId(null);
		callPlayer.setBusinessName(null);
		commandGroupUserPlayerDao.save(callPlayer);
		
		userLiveCallDao.delete(call);
		
		//被叫方给呼叫方发消息
		JSONObject message = new JSONObject();
		message.put("businessType", CommandCommonConstant.MESSAGE_CALL_REFUSE);
		message.put("fromUserId", user.getId());
		message.put("fromUserName", user.getName());
		message.put("businessInfo", user.getName() + "拒绝你视频通话");
		message.put("serial", callPlayer.getLocationIndex());
		
		WebsocketMessageVO ws = websocketMessageService.send(callUserInfo.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, user.getId(), user.getName());
		websocketMessageService.consume(ws.getId());
		
	}*/
	
	/**
	 * 停止呼叫<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月17日 下午2:13:13
	 * @param UserBO user 用户信息
	 * @param Long businessId 业务id
	 * @param UserBO admin admin
	 * @return CommandGroupUserPlayerPO 用户占用播放器信息
	 */
	/*public CommandGroupUserPlayerPO stopCall(UserBO user, Long businessId, UserBO admin) throws Exception{
		
		UserLiveCallPO call = userLiveCallDao.findOne(businessId);
		
		if(call == null){
			throw new BaseException(StatusCode.FORBIDDEN, "呼叫不存在！");
		}
		
		if(!user.getId().equals(call.getCalledUserId()) && !user.getId().equals(call.getCallUserId())){
			throw new UserNotMatchBusinessException(user.getName(), businessId, PlayerBusinessType.USER_CALL.getName());
		}
		
		//参数模板
		Map<String, Object> result = commandCommonServiceImpl.queryDefaultAvCodec();
		AvtplPO targetAvtpl = (AvtplPO)result.get("avtpl");
		AvtplGearsPO targetGear = (AvtplGearsPO)result.get("gear");
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
		
		if(CallStatus.ONGOING.equals(call.getStatus()) || CallStatus.PAUSE.equals(call.getStatus())){
			LogicBO logic = closeBundle(call, codec, admin.getId());
			List<CommandGroupUserPlayerPO> players = getPlayers(call);
			LogicBO logicCast = commandCastServiceImpl.closeBundleCastDevice(
					null, null, new ArrayListWrapper<UserLiveCallPO>().add(call).getList(), 
					players, null, user.getId());
			logic.merge(logicCast);
			executeBusiness.execute(logic, user.getName() + "停止通话");
		}
		
		//被呼叫方
		CommandGroupUserInfoPO calledUserInfo = commandGroupUserInfoDao.findByUserId(call.getCalledUserId());
		CommandGroupUserPlayerPO calledPlayer = commandCommonServiceImpl.queryPlayerByBusiness(calledUserInfo, PlayerBusinessType.USER_CALL, businessId.toString());
		calledPlayer.setPlayerBusinessType(PlayerBusinessType.NONE);
		calledPlayer.setBusinessId(null);
		calledPlayer.setBusinessName(null);
		commandGroupUserPlayerDao.save(calledPlayer);
		
		//呼叫方
		CommandGroupUserInfoPO callUserInfo = commandGroupUserInfoDao.findByUserId(call.getCallUserId());
		CommandGroupUserPlayerPO callPlayer = commandCommonServiceImpl.queryPlayerByBusiness(callUserInfo, PlayerBusinessType.USER_CALL, businessId.toString());
		callPlayer.setPlayerBusinessType(PlayerBusinessType.NONE);
		callPlayer.setBusinessId(null);
		callPlayer.setBusinessName(null);
		commandGroupUserPlayerDao.save(callPlayer);
		
		userLiveCallDao.delete(call);
		
		CommandGroupUserPlayerPO returnPlayer = null;
		
		//如果用户是被呼叫方
		if(user.getId().equals(calledUserInfo.getUserId())){
			//被叫方给呼叫方发消息
			JSONObject message = new JSONObject();
			message.put("businessType", CommandCommonConstant.MESSAGE_CALL_STOP);
			message.put("fromUserId", user.getId());
			message.put("fromUserName", user.getName());
			message.put("businessInfo", user.getName() + "停止了视频通话");
			message.put("serial", callPlayer.getLocationIndex());
			
			WebsocketMessageVO ws = websocketMessageService.send(callUserInfo.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, user.getId(), user.getName());
			websocketMessageService.consume(ws.getId());
			
			returnPlayer = calledPlayer;
		}
		
		//如果用户是呼叫方
		if(user.getId().equals(callUserInfo.getUserId())){
			//被叫方给呼叫方发消息
			JSONObject message = new JSONObject();
			message.put("businessType", CommandCommonConstant.MESSAGE_CALL_STOP);
			message.put("fromUserId", user.getId());
			message.put("fromUserName", user.getName());
			message.put("businessInfo", user.getName() + "停止了视频通话");
			message.put("serial", calledPlayer.getLocationIndex());
			
			WebsocketMessageVO ws = websocketMessageService.send(calledUserInfo.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, user.getId(), user.getName());
			websocketMessageService.consume(ws.getId());
			
			returnPlayer = callPlayer;
		}
		
		return returnPlayer;
		
	}*/
	
	/*public CommandGroupUserPlayerPO stopCallById(UserBO user, Long businessId, UserBO admin) throws Exception{
		synchronized (new StringBuffer().append(lockProcessPrefix).append(businessId).toString().intern()) {
			return stopCall_Cascade(user, businessId, admin);
		}
	}
	
	public CommandGroupUserPlayerPO stopCallByUuid(UserBO user, String uuid) throws Exception{
		UserLiveCallPO call = userLiveCallDao.findByUuid(uuid);
		if(call == null){
			throw new BaseException(StatusCode.FORBIDDEN, "呼叫不存在！");
		}
		
		synchronized (new StringBuffer().append(lockProcessPrefix).append(call.getId()).toString().intern()) {
			UserBO admin = new UserBO(); admin.setId(-1L);
			return stopCall_Cascade(user, call.getId(), admin);
		}
	}*/

	/**
	 * 停止呼叫 - 支持级联<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月6日 下午3:45:53
	 * @param user 操作人
	 * @param businessId 业务id，与uuid必须有一个null
	 * @param uuid 业务uuid，与businessId必须有一个null
	 * @return CommandGroupUserPlayerPO 用户释放播放器信息
	 * @throws Exception
	 */
	public CommandGroupUserPlayerPO stopCall_Cascade(UserBO user, Long businessId, String uuid) throws Exception{
		
		UserLiveCallPO call = null;
		if(businessId != null){
			call = userLiveCallDao.findOne(businessId);
		}else{
			call = userLiveCallDao.findByUuid(uuid);
			if(call == null){
//				log.info("根据uuid：" + uuid + "没有找到呼叫");
				return null;
			}
			businessId = call.getId();
		}
		
		if(call == null){
			throw new BaseException(StatusCode.FORBIDDEN, "通话已挂断");
		}
		
		//判断发起人是不是通话中的任何一个人
		if(!user.getId().equals(call.getCalledUserId()) && !user.getId().equals(call.getCallUserId())){
			throw new UserNotMatchBusinessException(user.getName(), businessId, PlayerBusinessType.USER_CALL.getName());
		}
		
		UserBO admin = new UserBO(); admin.setId(-1L);
		
		//参数模板
		CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();
		
		LogicBO logic = closeBundle(call, codec, admin.getId());
		if(CallStatus.ONGOING.equals(call.getStatus()) || CallStatus.PAUSE.equals(call.getStatus())){
						
			List<CommandGroupUserPlayerPO> players = getPlayers(call);
			LogicBO logicCast = commandCastServiceImpl.closeBundleCastDevice(
					null, null, new ArrayListWrapper<UserLiveCallPO>().add(call).getList(), 
					players, null, user.getId());
			logic.merge(logicCast);
		}
		executeBusiness.execute(logic, user.getName() + "停止通话");
		
		CallType callType = call.getCallType();
		CommandGroupUserPlayerPO returnPlayer = null;
		
		//如果被叫是本系统用户
		CommandGroupUserPlayerPO calledPlayer = null;
		if(CallType.LOCAL_LOCAL.equals(callType)
				|| CallType.OUTER_LOCAL.equals(callType)){
			CommandGroupUserInfoPO calledUserInfo = commandGroupUserInfoDao.findByUserId(call.getCalledUserId());
			calledPlayer = commandCommonServiceImpl.queryPlayerByBusiness(calledUserInfo, PlayerBusinessType.USER_CALL, businessId.toString());
			calledPlayer.setFree();
			commandGroupUserPlayerDao.save(calledPlayer);
			
			//如果用户是被呼叫方，返回callPlayer
			returnPlayer = calledPlayer;
			
			//如果用户是呼叫方
			if(user.getId().equals(call.getCallUserId())){
				
				//给被叫方发消息
				JSONObject message = new JSONObject();
				message.put("businessType", CommandCommonConstant.MESSAGE_CALL_STOP);
				message.put("fromUserId", user.getId());
				message.put("fromUserName", user.getName());
				message.put("businessInfo", user.getName() + "挂断了视频通话");
				message.put("serial", calledPlayer.getLocationIndex());
				
				WebsocketMessageVO ws = websocketMessageService.send(call.getCalledUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, user.getId(), user.getName());
				websocketMessageService.consume(ws.getId());
				
//				returnPlayer = callPlayer;
			}
		}

		//如果呼叫方是本系统用户
		CommandGroupUserPlayerPO callPlayer = null;
		if(CallType.LOCAL_LOCAL.equals(callType)
				|| CallType.LOCAL_OUTER.equals(callType)){
			CommandGroupUserInfoPO callUserInfo = commandGroupUserInfoDao.findByUserId(call.getCallUserId());
			callPlayer = commandCommonServiceImpl.queryPlayerByBusiness(callUserInfo, PlayerBusinessType.USER_CALL, businessId.toString());
			callPlayer.setFree();
			commandGroupUserPlayerDao.save(callPlayer);
			
			//如果用户是呼叫方，应返回callPlayer
			returnPlayer = callPlayer;
			
			//如果用户是被呼叫方
			if(user.getId().equals(call.getCalledUserId())){
				
				//给呼叫方发消息
				JSONObject message = new JSONObject();
				message.put("businessType", CommandCommonConstant.MESSAGE_CALL_STOP);
				message.put("fromUserId", user.getId());
				message.put("fromUserName", user.getName());
				message.put("businessInfo", user.getName() + "挂断了视频通话");
				message.put("serial", callPlayer.getLocationIndex());
				
				WebsocketMessageVO ws = websocketMessageService.send(call.getCallUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, user.getId(), user.getName());
				websocketMessageService.consume(ws.getId());
				
				returnPlayer = calledPlayer;
			}
		}
		
		userLiveCallDao.delete(call);
		
		return returnPlayer;
		
	}

	/**
	 * 同意语音对讲<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月20日 下午4:30:30
	 * @param UserBO user 用户信息
	 * @param Long businessId 业务id
	 * @param UserBO admin 管理员
	 * @return CommandGroupUserPlayerPO 播放器信息
	 */
	public CommandGroupUserPlayerPO acceptVoice(UserBO user, Long businessId, UserBO admin) throws Exception{
		
		UserLiveCallPO call = userLiveCallDao.findOne(businessId);
		
		if(call == null){
			throw new BaseException(StatusCode.FORBIDDEN, "对方已挂断");
		}
		
		if(!user.getId().equals(call.getCalledUserId())){
			throw new UserNotMatchBusinessException(user.getName(), businessId, PlayerBusinessType.USER_VOICE.getName());
		}
		
		//消息消费
		try{
			websocketMessageService.consume(call.getMessageId());
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("消息消费异常，id：" + call.getMessageId());
		}
		
		//参数模板
		Map<String, Object> result = commandCommonServiceImpl.queryDefaultAvCodec();
		AvtplPO targetAvtpl = (AvtplPO)result.get("avtpl");
		AvtplGearsPO targetGear = (AvtplGearsPO)result.get("gear");
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
		
		CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(call.getCalledUserId());
		CommandGroupUserPlayerPO player = commandCommonServiceImpl.queryPlayerByBusiness(userInfo, PlayerBusinessType.USER_VOICE, businessId.toString());
		
		call.setStatus(CallStatus.ONGOING);
		userLiveCallDao.save(call);
		
		//协议
		LogicBO logic = openBundle(call, codec, admin.getId());
		LogicBO logicCast = commandCastServiceImpl.openBundleCastDevice(null, null, null, null, null, new ArrayListWrapper<UserLiveCallPO>().add(call).getList(), codec, user.getId());
		logic.merge(logicCast);
		
		executeBusiness.execute(logic, call.getCalledUsername() + "接听与" + call.getCallUsername() + "语音对讲");
		
		return player;
	}
	
	/**
	 * 拒绝语音对讲<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月20日 下午4:29:32
	 * @param UserBO user 用户
	 * @param Long businessId 业务id
	 */
	public void refuseVoice(UserBO user, Long businessId) throws Exception{
		
		UserLiveCallPO call = userLiveCallDao.findOne(businessId);
		
		if(call == null){
			throw new BaseException(StatusCode.FORBIDDEN, "对方已挂断！");
		}
		
		if(!user.getId().equals(call.getCalledUserId())){
			throw new UserNotMatchBusinessException(user.getName(), businessId, PlayerBusinessType.USER_VOICE.getName());
		}
		
		//呼叫方发送的消息消费
		try{
			websocketMessageService.consume(call.getMessageId());
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("消息消费异常，id：" + call.getMessageId());
		}
		
		//被呼叫方
		CommandGroupUserInfoPO calledUserInfo = commandGroupUserInfoDao.findByUserId(call.getCalledUserId());
		CommandGroupUserPlayerPO calledPlayer = commandCommonServiceImpl.queryPlayerByBusiness(calledUserInfo, PlayerBusinessType.USER_VOICE, businessId.toString());
		calledPlayer.setFree();
		commandGroupUserPlayerDao.save(calledPlayer);
		
		//呼叫方
		CommandGroupUserInfoPO callUserInfo = commandGroupUserInfoDao.findByUserId(call.getCallUserId());
		CommandGroupUserPlayerPO callPlayer = commandCommonServiceImpl.queryPlayerByBusiness(callUserInfo, PlayerBusinessType.USER_VOICE, businessId.toString());
		callPlayer.setFree();
		commandGroupUserPlayerDao.save(callPlayer);
		
		userLiveCallDao.delete(call);
		
		//被叫方给呼叫方发消息
		JSONObject message = new JSONObject();
		message.put("businessType", CommandCommonConstant.MESSAGE_VOICE_REFUSE);
		message.put("fromUserId", user.getId());
		message.put("fromUserName", user.getName());
		message.put("businessInfo", user.getName() + "拒绝你语音对讲");
		message.put("serial", callPlayer.getLocationIndex());
		
		WebsocketMessageVO ws = websocketMessageService.send(callUserInfo.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, user.getId(), user.getName());
		websocketMessageService.consume(ws.getId());
		
	}
	
	/**
	 * 停止语音对讲<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月20日 下午3:40:03
	 * @param UserBO user 用户
	 * @param Long businessId 业务id
	 * @param UserBO admin 管理员用户
	 */
	public CommandGroupUserPlayerPO stopVoice(UserBO user, Long businessId, UserBO admin) throws Exception{
		
		UserLiveCallPO call = userLiveCallDao.findOne(businessId);
		
		if(call == null){
			throw new BaseException(StatusCode.FORBIDDEN, "对方已挂断！");
		}
		
		if(!user.getId().equals(call.getCalledUserId()) && !user.getId().equals(call.getCallUserId())){
			throw new UserNotMatchBusinessException(user.getName(), businessId, PlayerBusinessType.USER_VOICE.getName());
		}
		
		//被呼叫方
		CommandGroupUserInfoPO calledUserInfo = commandGroupUserInfoDao.findByUserId(call.getCalledUserId());
		CommandGroupUserPlayerPO calledPlayer = commandCommonServiceImpl.queryPlayerByBusiness(calledUserInfo, PlayerBusinessType.USER_VOICE, businessId.toString());
		calledPlayer.setFree();
		commandGroupUserPlayerDao.save(calledPlayer);
		
		//呼叫方
		CommandGroupUserInfoPO callUserInfo = commandGroupUserInfoDao.findByUserId(call.getCallUserId());
		CommandGroupUserPlayerPO callPlayer = commandCommonServiceImpl.queryPlayerByBusiness(callUserInfo, PlayerBusinessType.USER_VOICE, businessId.toString());
		callPlayer.setFree();
		commandGroupUserPlayerDao.save(callPlayer);
		
		userLiveCallDao.delete(call);
		
		//通话进行中则发协议
		if(CallStatus.ONGOING.equals(call.getStatus()) || CallStatus.PAUSE.equals(call.getStatus())){
			CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();
			LogicBO logic = closeBundle(call, codec, admin.getId());
			List<CommandGroupUserPlayerPO> players = new ArrayListWrapper<CommandGroupUserPlayerPO>().add(callPlayer).add(calledPlayer).getList();
			LogicBO logicCast = commandCastServiceImpl.closeBundleCastDevice(
					null, null, new ArrayListWrapper<UserLiveCallPO>().add(call).getList(), 
					players, null, user.getId());
			logic.merge(logicCast);
			executeBusiness.execute(logic, user.getName() + "停止语音对讲");
		}
		
		CommandGroupUserPlayerPO returnPlayer = null;
		
		//如果用户是被呼叫方
		if(user.getId().equals(calledUserInfo.getUserId())){
			//被叫方给呼叫方发消息
			JSONObject message = new JSONObject();
			message.put("businessType", CommandCommonConstant.MESSAGE_VOICE_STOP);
			message.put("fromUserId", user.getId());
			message.put("fromUserName", user.getName());
			message.put("businessInfo", user.getName() + "停止了语音对讲");
			message.put("serial", callPlayer.getLocationIndex());
			
			WebsocketMessageVO ws = websocketMessageService.send(callUserInfo.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, user.getId(), user.getName());
			websocketMessageService.consume(ws.getId());
			
			returnPlayer = calledPlayer;
		}
		
		//如果用户是呼叫方
		if(user.getId().equals(callUserInfo.getUserId())){
			//被叫方给呼叫方发消息
			JSONObject message = new JSONObject();
			message.put("businessType", CommandCommonConstant.MESSAGE_VOICE_STOP);
			message.put("fromUserId", user.getId());
			message.put("fromUserName", user.getName());
			message.put("businessInfo", user.getName() + "停止了语音对讲");
			message.put("serial", calledPlayer.getLocationIndex());
			
			WebsocketMessageVO ws = websocketMessageService.send(calledUserInfo.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, user.getId(), user.getName());
			websocketMessageService.consume(ws.getId());
			
			returnPlayer = callPlayer;
		}
		
		return returnPlayer;
		
	}
	
	/** 一个播放器是否可以用来将点播用户转为呼叫，即该播放器是否空闲，或者正在点播特定用户 */
	private boolean isPlayerIdleOrVodUser(CommandGroupUserPlayerPO player, UserBO calledUser){
		
		PlayerBusinessType type = player.getPlayerBusinessType();
		
		//空闲则true
		if(PlayerBusinessType.NONE.equals(type)){
			return true;
		}
		
		//USER_CALL时，没有业务则true，有业务则false
		String id = player.getBusinessId();
		if(PlayerBusinessType.USER_CALL.equals(type)){
			if(id == null) return true;
			else return false;
		}
		
		//不在点播用户则false
		if(!PlayerBusinessType.PLAY_USER.equals(type)){
			return false;
		}
		
		//在点播calledUser则true，否则false
		CommandVodPO vod = commandVodDao.findOne(Long.parseLong(id));
		if(vod == null){
			log.warn("点播转呼叫，未查到点播任务，id：" + id + "播放器序号：" + player.getLocationIndex());
			return false;
		}
		if(calledUser.getId().equals(vod.getSourceUserId())){
			return true;
		}
		return false;		
	}
	
	/** USER_CALL，只能获取呼叫的，不能获取语音的2个播放器（主叫和被叫的） */
	private List<CommandGroupUserPlayerPO> getPlayers(UserLiveCallPO call) throws Exception{
		
		CallType callType = call.getCallType();
		List<CommandGroupUserPlayerPO> players = new ArrayList<CommandGroupUserPlayerPO>();
		
		//找呼叫方的player
		if(callType==null || callType.equals(CallType.LOCAL_LOCAL) || callType.equals(CallType.LOCAL_OUTER)){
			CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(call.getCallUserId());
			CommandGroupUserPlayerPO callPlayer = commandCommonServiceImpl.queryPlayerByBusiness(userInfo, PlayerBusinessType.USER_CALL, call.getId().toString());
			players.add(callPlayer);
		}
		
		//找被呼叫方的player
		if(callType==null || callType.equals(CallType.LOCAL_LOCAL) || callType.equals(CallType.OUTER_LOCAL)){
			CommandGroupUserInfoPO calledUserInfo = commandGroupUserInfoDao.findByUserId(call.getCalledUserId());
			CommandGroupUserPlayerPO calledPlayer = commandCommonServiceImpl.queryPlayerByBusiness(calledUserInfo, PlayerBusinessType.USER_CALL, call.getId().toString());
			players.add(calledPlayer);
		}
		
		return players;		
	}

	
	/**
	 * 用户通话协议处理 -- 业务数据库可以控制音视频<br/>
	 * <p>需要在接听后才能调用，该方法不会判断通话是否在进行</p>
	 * <p>级联被呼叫时，不生成给呼叫方的passby，因为已经在主叫方发起业务的时候(userCallUser_Cascade)就发过passby了，此时呼叫已经建立</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月17日 上午9:55:27
	 * @param UserCallPO call 用户呼叫业务信息
	 * @param CodecParamBO codec 参数
	 * @param Long userId 用户
	 * @return LogicBO 协议
	 */
	private LogicBO openBundle(
			UserLiveCallPO call,
			CodecParamBO codec,
			Long userId) throws Exception{		
		
		CallType callType = call.getCallType();
				
		//呼叫设备
		LogicBO logic = new LogicBO().setUserId(userId.toString())
				 			 		 .setConnectBundle(new ArrayList<ConnectBundleBO>())
				 			 		 .setPass_by(new ArrayList<PassByBO>());
		
		if(callType==null || callType.equals(CallType.LOCAL_LOCAL) || callType.equals(CallType.OUTER_LOCAL)){
			
			//呼叫被叫编码：如果是“点播转换的呼叫”，则不呼
			if(call.getFormerVodId() == null){
				ConnectBundleBO connectCalledEncoderBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
																		          .setOperateType(ConnectBundleBO.OPERATE_TYPE)
																				  .setLock_type("write")
																				  .setBundleId(call.getCalledEncoderBundleId())
																				  .setLayerId(call.getCalledEncoderLayerId())
																				  .setBundle_type(call.getCalledEncoderBundleType());
				
				List<ConnectBO> calledEncodeConnectBOs = new ArrayList<ConnectBO>();
				if(call.getCalledEncoderVideoChannelId() != null){
					ConnectBO connectCalledEncoderVideoChannel = new ConnectBO().setChannelId(call.getCalledEncoderVideoChannelId())
																			    .setChannel_status("Open")
																			    .setBase_type(call.getCalledEncoderVideoBaseType())
																			    .setCodec_param(codec);
					calledEncodeConnectBOs.add(connectCalledEncoderVideoChannel);
				}
	
				if(call.getCalledEncoderAudioChannelId() != null){
					ConnectBO connectCalledEncoderAudioChannel = new ConnectBO().setChannelId(call.getCalledEncoderAudioChannelId())
																			    .setChannel_status("Open")
																			    .setBase_type(call.getCalledEncoderAudioBaseType())
																			    .setCodec_param(codec);
					calledEncodeConnectBOs.add(connectCalledEncoderAudioChannel);
				}
	
				
				connectCalledEncoderBundle.setChannels(new ArrayListWrapper<ConnectBO>().addAll(calledEncodeConnectBOs).getList());
				logic.getConnectBundle().add(connectCalledEncoderBundle);
			}
			
			//呼叫被叫解码，看主叫编码
			ConnectBundleBO connectCalledDecoderBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
//					  													      .setOperateType(ConnectBundleBO.OPERATE_TYPE)
																			  .setLock_type("write")
																		      .setBundleId(call.getCalledDecoderBundleId())
																		      .setLayerId(call.getCalledDecoderLayerId())
																		      .setBundle_type(call.getCalledDecoderBundleType());
			List<ConnectBO> calledDecodeConnectBOs = new ArrayList<ConnectBO>();
			if(call.getCalledDecoderVideoChannelId() != null){
				ForwardSetSrcBO calledDecoderVideoForwardSet = new ForwardSetSrcBO().setType("channel")
																			 	    .setBundleId(call.getCallEncoderBundleId())
																			 	    .setLayerId(call.getCallEncoderLayerId())
																			 	    .setChannelId(call.getCallEncoderVideoChannelId());
				ConnectBO connectCalledDecoderVideoChannel = new ConnectBO().setChannelId(call.getCalledDecoderVideoChannelId())
																	        .setChannel_status("Open")
																	        .setBase_type(call.getCalledDecoderVideoBaseType())
																	        .setCodec_param(codec)
																	        .setSource_param(calledDecoderVideoForwardSet);
				calledDecodeConnectBOs.add(connectCalledDecoderVideoChannel);
			}

			if(call.getCalledDecoderAudioChannelId() != null){
				ForwardSetSrcBO calledDecoderAudioForwardSet = new ForwardSetSrcBO().setType("channel")
																			 	    .setBundleId(call.getCallEncoderBundleId())
																			 	    .setLayerId(call.getCallEncoderLayerId())
																			 	    .setChannelId(call.getCallEncoderAudioChannelId());
				ConnectBO connectCalledDecoderAudioChannel = new ConnectBO().setChannelId(call.getCalledDecoderAudioChannelId())
																	        .setChannel_status("Open")
																	        .setBase_type(call.getCalledDecoderAudioBaseType())
																	        .setCodec_param(codec)
																	        .setSource_param(calledDecoderAudioForwardSet);
				calledDecodeConnectBOs.add(connectCalledDecoderAudioChannel);
			}
			
			connectCalledDecoderBundle.setChannels(new ArrayListWrapper<ConnectBO>().addAll(calledDecodeConnectBOs).getList());
			logic.getConnectBundle().add(connectCalledDecoderBundle);
		}else{
			//LOCAL_OUTER，被叫是外部用户。这里不再生成给呼叫方的passby，因为已经在主叫方发起业务的时候(userCallUser_Cascade)就发过passby了，此时呼叫已经建立
			//相当于打开被叫编码
			/*String localLayerId = resourceRemoteService.queryLocalLayerId();
			XtBusinessPassByContentBO passByContent = new XtBusinessPassByContentBO().setCmd(XtBusinessPassByContentBO.CMD_LOCAL_CALL_XT_USER)
								 .setOperate(XtBusinessPassByContentBO.OPERATE_START)
								 .setUuid(call.getUuid())
								 .setSrc_user(call.getCallUserno())
								 .setLocal_encoder(new HashMapWrapper<String, String>().put("layerid", call.getCallEncoderLayerId())
										 											   .put("bundleid", call.getCallEncoderBundleId())
										                         					   .put("video_channelid", call.getCallEncoderVideoChannelId())
										                         					   .put("audio_channelid", call.getCallEncoderAudioChannelId())
										 											   .getMap())
								 .setXt_encoder(new HashMapWrapper<String, String>().put("layerid", call.getCalledEncoderLayerId())
										 											.put("bundleid", call.getCalledEncoderBundleId())
										 											.put("video_channelid", call.getCalledEncoderVideoChannelId())
										 											.put("audio_channelid", call.getCalledEncoderAudioChannelId())
										 											.getMap())
								 .setDst_number(call.getCalledUserno())
								 .setVparam(codec);
			
			PassByBO passby = new PassByBO().setLayer_id(localLayerId)
			.setType(XtBusinessPassByContentBO.CMD_LOCAL_CALL_XT_USER)
			.setPass_by_content(passByContent);
			
			logic.getPass_by().add(passby);*/
		}
		
		if(callType==null || callType.equals(CallType.LOCAL_LOCAL) || callType.equals(CallType.LOCAL_OUTER)){
			
			//呼叫主叫编码
			ConnectBundleBO connectCallEncoderBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
																            .setOperateType(ConnectBundleBO.OPERATE_TYPE)
																		    .setLock_type("write")
																		    .setBundleId(call.getCallEncoderBundleId())
																		    .setLayerId(call.getCallEncoderLayerId())
																		    .setBundle_type(call.getCallEncoderBundleType());
			List<ConnectBO> callEncodeConnectBOs = new ArrayList<ConnectBO>();
			if(call.getCallEncoderVideoChannelId() != null){
				ConnectBO connectCallEncoderVideoChannel = new ConnectBO().setChannelId(call.getCallEncoderVideoChannelId())
																		  .setChannel_status("Open")
																		  .setBase_type(call.getCallEncoderVideoBaseType())
																		  .setCodec_param(codec);
				callEncodeConnectBOs.add(connectCallEncoderVideoChannel);
			}
			if(call.getCallEncoderAudioChannelId() != null){
				ConnectBO connectCallEncoderAudioChannel = new ConnectBO().setChannelId(call.getCallEncoderAudioChannelId())
																	      .setChannel_status("Open")
																	      .setBase_type(call.getCallEncoderAudioBaseType())
																	      .setCodec_param(codec);
				callEncodeConnectBOs.add(connectCallEncoderAudioChannel);
			}
			
			connectCallEncoderBundle.setChannels(new ArrayListWrapper<ConnectBO>().addAll(callEncodeConnectBOs).getList());
			logic.getConnectBundle().add(connectCallEncoderBundle);

			//呼叫主叫解码，看被叫编码
			ConnectBundleBO connectCallDecoderBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
//					  													    .setOperateType(ConnectBundleBO.OPERATE_TYPE)
																			.setLock_type("write")
																		    .setBundleId(call.getCallDecoderBundleId())
																		    .setLayerId(call.getCallDecoderLayerId())
																		    .setBundle_type(call.getCallDecoderBundleType());
			List<ConnectBO> callDecodeConnectBOs = new ArrayList<ConnectBO>();
			if(call.getCallDecoderVideoChannelId() != null){
				ForwardSetSrcBO callDecoderVideoForwardSet = new ForwardSetSrcBO().setType("channel")
																			 	  .setBundleId(call.getCalledEncoderBundleId())
																			 	  .setLayerId(call.getCalledEncoderLayerId())
																			 	  .setChannelId(call.getCalledEncoderVideoChannelId());
				ConnectBO connectCallDecoderVideoChannel = new ConnectBO().setChannelId(call.getCallDecoderVideoChannelId())
																	      .setChannel_status("Open")
																	      .setBase_type(call.getCallDecoderVideoBaseType())
																	      .setCodec_param(codec)
																	      .setSource_param(callDecoderVideoForwardSet);
				callDecodeConnectBOs.add(connectCallDecoderVideoChannel);
			}
			if(call.getCallDecoderAudioChannelId() != null){
				ForwardSetSrcBO callDecoderAudioForwardSet = new ForwardSetSrcBO().setType("channel")
																			 	  .setBundleId(call.getCalledEncoderBundleId())
																			 	  .setLayerId(call.getCalledEncoderLayerId())
																			 	  .setChannelId(call.getCalledEncoderAudioChannelId());
				ConnectBO connectCallDecoderAudioChannel = new ConnectBO().setChannelId(call.getCallDecoderAudioChannelId())
																	      .setChannel_status("Open")
																	      .setBase_type(call.getCallDecoderAudioBaseType())
																	      .setCodec_param(codec)
																	      .setSource_param(callDecoderAudioForwardSet);
				callDecodeConnectBOs.add(connectCallDecoderAudioChannel);
			}
			
			connectCallDecoderBundle.setChannels(new ArrayListWrapper<ConnectBO>().addAll(callDecodeConnectBOs).getList());
			logic.getConnectBundle().add(connectCallDecoderBundle);
		}else{
			//OUTER_LOCAL，主叫是外部用户，生成passby，相当于打开主叫编码
			//查询本联网layerid
			String localLayerId = resourceRemoteService.queryLocalLayerId();
			XtBusinessPassByContentBO passByContent = new XtBusinessPassByContentBO().setCmd(XtBusinessPassByContentBO.CMD_XT_CALL_LOCAL_USER)
								 .setOperate(XtBusinessPassByContentBO.OPERATE_START)
								 .setUuid(call.getUuid())
								 .setSrc_user(call.getCallUserno())
								 .setLocal_encoder(new HashMapWrapper<String, String>().put("layerid", call.getCalledEncoderLayerId())
																					   .put("bundleid", call.getCalledEncoderBundleId())
											                     					   .put("video_channelid", call.getCalledEncoderVideoChannelId())
											                     					   .put("audio_channelid", call.getCalledEncoderAudioChannelId())
										 											   .getMap())
								 .setXt_encoder(new HashMapWrapper<String, String>().put("layerid", call.getCallEncoderLayerId())
																					.put("bundleid", call.getCallEncoderBundleId())
																					.put("video_channelid", call.getCallEncoderVideoChannelId())
																					.put("audio_channelid", call.getCallEncoderAudioChannelId())
										                                            .getMap())
								 .setDst_number(call.getCalledUserno())
								 .setVparam(codec);
			
			PassByBO passby = new PassByBO().setLayer_id(localLayerId)
			.setType(XtBusinessPassByContentBO.CMD_XT_CALL_LOCAL_USER)
			.setPass_by_content(passByContent);
			
			logic.getPass_by().add(passby);
		}
			
		return logic;
	}
	
	/**
	 * 用户通话挂断协议<br/>
	 * <p>该方法会判断通话是否在进行，如果没有进行，则只生成passby给级联使用</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月17日 下午2:02:19
	 * @param UserCallPO call 用户呼叫业务信息
	 * @param Long userId 用户id
	 * @return LogicBO 协议
	 */
	private LogicBO closeBundle(
				UserLiveCallPO call, 
				CodecParamBO codec,
				Long userId) throws Exception{
				
		CallType callType = call.getCallType();
		
		LogicBO logic = new LogicBO().setUserId(userId.toString())
									 .setDisconnectBundle(new ArrayList<DisconnectBundleBO>())
									 .setPass_by(new ArrayList<PassByBO>());
		
		if(callType==null || callType.equals(CallType.LOCAL_LOCAL) || callType.equals(CallType.OUTER_LOCAL)){
			if(CallStatus.ONGOING.equals(call.getStatus()) || CallStatus.PAUSE.equals(call.getStatus())
					|| call.getFormerVodId() != null){
				//关闭被叫编码：如果已经开始，或者“该呼叫是点播转换来的”，即call.getFormerVodId()!=null
				DisconnectBundleBO disconnectCalledEncoderBundle = new DisconnectBundleBO().setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
																				           .setOperateType(DisconnectBundleBO.OPERATE_TYPE)
																					       .setBundleId(call.getCalledEncoderBundleId())
																					       .setBundle_type(call.getCalledEncoderBundleType())
																					       .setLayerId(call.getCalledEncoderLayerId());
				logic.getDisconnectBundle().add(disconnectCalledEncoderBundle);
			}
			if(CallStatus.ONGOING.equals(call.getStatus()) || CallStatus.PAUSE.equals(call.getStatus())){
				//关闭被叫解码
				DisconnectBundleBO disconnectCalledDecoderBundle = new DisconnectBundleBO().setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
		//																		           .setOperateType(DisconnectBundleBO.OPERATE_TYPE)
																					       .setBundleId(call.getCalledDecoderBundleId())
																					       .setBundle_type(call.getCalledDecoderBundleType())
																					       .setLayerId(call.getCalledDecoderLayerId());
				
				
				logic.getDisconnectBundle().add(disconnectCalledDecoderBundle);
				
				//清除资源层上的字幕
				resourceServiceClient.removeLianwangPassby(call.getCalledDecoderBundleId());
			}
		}else{
			//LOCAL_OUTER，生成passby
			//相当于关闭被叫编码
			String localLayerId = resourceRemoteService.queryLocalLayerId();
			XtBusinessPassByContentBO passByContent = new XtBusinessPassByContentBO().setCmd(XtBusinessPassByContentBO.CMD_LOCAL_CALL_XT_USER)
																					 .setOperate(XtBusinessPassByContentBO.OPERATE_STOP)
																					 .setUuid(call.getUuid())
																					 .setSrc_user(call.getCallUserno())//主叫用户号码
																					 .setLocal_encoder(new HashMapWrapper<String, String>().put("layerid", call.getCallEncoderLayerId())
																							 											   .put("bundleid", call.getCallEncoderBundleId())
																							                         					   .put("video_channelid", call.getCallEncoderVideoChannelId())
																							                         					   .put("audio_channelid", call.getCallEncoderAudioChannelId())
																							 											   .getMap())
																					 .setXt_encoder(new HashMapWrapper<String, String>().put("layerid", localLayerId)
																							 											.put("bundleid", call.getCalledEncoderBundleId())
																							 											.put("video_channelid", call.getCalledEncoderVideoChannelId())
																							 											.put("audio_channelid", call.getCalledEncoderAudioChannelId())
																							 											.getMap())
																					 .setDst_number(call.getCalledUserno())
																					 .setVparam(codec);
			
			PassByBO passby = new PassByBO().setLayer_id(localLayerId)
											.setType(XtBusinessPassByContentBO.CMD_LOCAL_CALL_XT_USER)
											.setPass_by_content(passByContent);

			logic.getPass_by().add(passby);
		}
		
		if(callType==null || callType.equals(CallType.LOCAL_LOCAL) || callType.equals(CallType.LOCAL_OUTER)){
			if(CallStatus.ONGOING.equals(call.getStatus()) || CallStatus.PAUSE.equals(call.getStatus())){
				//关闭主叫编码：如果已经开始
				DisconnectBundleBO disconnectCallEncoderBundle = new DisconnectBundleBO().setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
																				         .setOperateType(DisconnectBundleBO.OPERATE_TYPE)
																					     .setBundleId(call.getCallEncoderBundleId())
																					     .setBundle_type(call.getCallEncoderBundleType())
																					     .setLayerId(call.getCallEncoderLayerId());
				logic.getDisconnectBundle().add(disconnectCallEncoderBundle);
			}
			if(CallStatus.ONGOING.equals(call.getStatus()) || CallStatus.PAUSE.equals(call.getStatus())
					|| call.getFormerVodId() != null){
				//关闭主叫解码：如果已经开始，或者“该呼叫是点播转换来的”，即call.getFormerVodId()!=null
				DisconnectBundleBO disconnectCallDecoderBundle = new DisconnectBundleBO().setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
		//																		         .setOperateType(DisconnectBundleBO.OPERATE_TYPE)
																					     .setBundleId(call.getCallDecoderBundleId())
																					     .setBundle_type(call.getCallDecoderBundleType())
																					     .setLayerId(call.getCallDecoderLayerId());
				logic.getDisconnectBundle().add(disconnectCallDecoderBundle);
				
				//清除资源层上的字幕
				resourceServiceClient.removeLianwangPassby(call.getCallDecoderBundleId());
			}
		}else{
			//OUTER_LOCAL，生成passby
			//相当于关闭主叫编码
			String localLayerId = resourceRemoteService.queryLocalLayerId();
			XtBusinessPassByContentBO passByContent = new XtBusinessPassByContentBO().setCmd(XtBusinessPassByContentBO.CMD_XT_CALL_LOCAL_USER)
					 .setOperate(XtBusinessPassByContentBO.OPERATE_STOP)
					 .setUuid(call.getUuid())
					 .setSrc_user(call.getCallUserno())
					 .setLocal_encoder(new HashMapWrapper<String, String>().put("layerid", call.getCalledEncoderLayerId())
							 											   .put("bundleid", call.getCalledEncoderBundleId())
							 											   .put("video_channelid", call.getCalledEncoderVideoChannelId())
							 											   .put("audio_channelid", call.getCalledEncoderAudioChannelId())
							 											   .getMap())
					 .setXt_encoder(new HashMapWrapper<String, String>().put("layerid", localLayerId)
							 											.put("bundleid", call.getCallEncoderBundleId())
							 											.put("video_channelid", call.getCallEncoderVideoChannelId())
							 											.put("audio_channelid", call.getCallEncoderAudioChannelId())
							                                            .getMap())
					 .setDst_number(call.getCalledUserno())
					 .setVparam(codec);
			
			PassByBO passby = new PassByBO().setLayer_id(localLayerId)
			.setType(XtBusinessPassByContentBO.CMD_XT_CALL_LOCAL_USER)
			.setPass_by_content(passByContent);
			
			logic.getPass_by().add(passby);
			
		}
		
		return logic;
	}
}

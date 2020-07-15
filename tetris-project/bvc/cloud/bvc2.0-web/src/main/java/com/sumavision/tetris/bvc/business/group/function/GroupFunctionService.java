package com.sumavision.tetris.bvc.business.group.function;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.FolderUserMapDAO;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.pojo.FolderUserMap;
import com.suma.venus.resource.service.ResourceRemoteService;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.command.group.basic.CommandGroupAvtplGearsPO;
import com.sumavision.bvc.command.group.basic.CommandGroupAvtplPO;
import com.sumavision.bvc.command.group.basic.CommandGroupMemberPO;
import com.sumavision.bvc.command.group.basic.CommandGroupPO;
import com.sumavision.bvc.command.group.dao.CommandGroupDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupMemberDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupUserPlayerDAO;
import com.sumavision.bvc.command.group.enumeration.EditStatus;
import com.sumavision.bvc.command.group.enumeration.ForwardBusinessType;
import com.sumavision.bvc.command.group.enumeration.ForwardDemandBusinessType;
import com.sumavision.bvc.command.group.enumeration.ForwardDemandStatus;
import com.sumavision.bvc.command.group.enumeration.ForwardDstType;
import com.sumavision.bvc.command.group.enumeration.GroupSpeakType;
import com.sumavision.bvc.command.group.enumeration.GroupType;
import com.sumavision.bvc.command.group.enumeration.MediaType;
import com.sumavision.bvc.command.group.forward.CommandGroupForwardDemandPO;
import com.sumavision.bvc.command.group.forward.CommandGroupForwardPO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.command.group.user.layout.player.PlayerBusinessType;
import com.sumavision.bvc.control.device.command.group.vo.BusinessPlayerVO;
import com.sumavision.bvc.device.command.bo.MessageSendCacheBO;
import com.sumavision.bvc.device.command.cascade.util.CommandCascadeUtil;
import com.sumavision.bvc.device.command.cast.CommandCastServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.command.exception.CommandGroupNameAlreadyExistedException;
import com.sumavision.bvc.device.command.exception.HasNotUsefulPlayerException;
import com.sumavision.bvc.device.command.exception.UserHasNoAvailableEncoderException;
import com.sumavision.bvc.device.command.exception.UserHasNoFolderException;
import com.sumavision.bvc.device.command.meeting.CommandMeetingSpeakServiceImpl;
import com.sumavision.bvc.device.command.record.CommandRecordServiceImpl;
import com.sumavision.bvc.device.command.time.CommandFightTimeServiceImpl;
import com.sumavision.bvc.device.command.vod.CommandVodService;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.ConnectBO;
import com.sumavision.bvc.device.group.bo.ConnectBundleBO;
import com.sumavision.bvc.device.group.bo.DisconnectBundleBO;
import com.sumavision.bvc.device.group.bo.ForwardDelBO;
import com.sumavision.bvc.device.group.bo.ForwardSetBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.MediaPushSetBO;
import com.sumavision.bvc.device.group.bo.PassByBO;
import com.sumavision.bvc.device.group.bo.XtBusinessPassByContentBO;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.CommonQueryUtil;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.device.monitor.live.DstDeviceType;
import com.sumavision.bvc.feign.ResourceServiceClient;
import com.sumavision.bvc.log.OperationLogService;
import com.sumavision.bvc.meeting.logic.ExecuteBusinessReturnBO;
import com.sumavision.bvc.resource.dao.ResourceBundleDAO;
import com.sumavision.bvc.resource.dao.ResourceChannelDAO;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.bvc.system.po.AvtplGearsPO;
import com.sumavision.bvc.system.po.AvtplPO;
import com.sumavision.tetris.auth.token.TerminalType;
import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.bvc.business.ExecuteStatus;
import com.sumavision.tetris.bvc.business.OriginType;
import com.sumavision.tetris.bvc.business.bo.MemberTerminalBO;
import com.sumavision.tetris.bvc.business.bo.SourceBO;
import com.sumavision.tetris.bvc.business.bo.UserTerminalBO;
import com.sumavision.tetris.bvc.business.common.BusinessCommonService;
import com.sumavision.tetris.bvc.business.dao.CommonForwardDAO;
import com.sumavision.tetris.bvc.business.dao.GroupDAO;
import com.sumavision.tetris.bvc.business.dao.GroupDemandDAO;
import com.sumavision.tetris.bvc.business.dao.GroupMemberDAO;
import com.sumavision.tetris.bvc.business.dao.GroupMemberRolePermissionDAO;
import com.sumavision.tetris.bvc.business.forward.CommonForwardPO;
import com.sumavision.tetris.bvc.business.group.BusinessType;
import com.sumavision.tetris.bvc.business.group.GroupMemberPO;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.bvc.business.group.GroupStatus;
import com.sumavision.tetris.bvc.business.group.demand.GroupDemandPO;
import com.sumavision.tetris.bvc.business.group.demand.GroupDemandService;
import com.sumavision.tetris.bvc.cascade.CommandCascadeService;
import com.sumavision.tetris.bvc.cascade.ConferenceCascadeService;
import com.sumavision.tetris.bvc.cascade.bo.GroupBO;
import com.sumavision.tetris.bvc.cascade.bo.MinfoBO;
import com.sumavision.tetris.bvc.model.agenda.AgendaPO;
import com.sumavision.tetris.bvc.model.agenda.AgendaService;
import com.sumavision.tetris.bvc.model.dao.AgendaDAO;
import com.sumavision.tetris.bvc.model.role.RolePO;
import com.sumavision.tetris.bvc.page.PageTaskDAO;
import com.sumavision.tetris.bvc.page.PageTaskPO;
import com.sumavision.tetris.bvc.page.PageTaskService;
import com.sumavision.tetris.bvc.util.TetrisBvcQueryUtil;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashSetWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;
import com.sumavision.tetris.websocket.message.WebsocketMessageType;
import com.sumavision.tetris.websocket.message.WebsocketMessageVO;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class GroupFunctionService {
	
	/** 是否强制接听进入指挥和会议，false需要点击同意 */
	private static boolean autoEnter = true;
	
	/** synchronized锁的前缀 */
	private static final String lockProcessPrefix = "tetris-group-";
	
	@Autowired
	private AgendaDAO agendaDao;
	
	@Autowired
	private GroupDAO groupDao;
	
	@Autowired
	private GroupMemberDAO groupMemberDao;
	
	@Autowired
	private GroupDemandDAO groupDemandDao;
	
	@Autowired
	private GroupMemberRolePermissionDAO groupMemberRolePermissionDao;
	
	@Autowired
	private CommonForwardDAO commonForwardDao;
	
	@Autowired
	private CommandGroupDAO commandGroupDao;
	
	@Autowired
	private PageTaskDAO pageTaskDao;
	
	@Autowired
	private CommandGroupUserPlayerDAO commandGroupUserPlayerDao;
	
	@Autowired
	private CommandGroupMemberDAO commandGroupMemberDao;
	
	@Autowired
	private ResourceBundleDAO resourceBundleDao;
	
	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private ResourceChannelDAO resourceChannelDao;
	
	@Autowired
	private FolderUserMapDAO folderUserMapDao;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private AgendaService agendaService;
	
	@Autowired
	private BusinessCommonService businessCommonService;
	
	@Autowired
	private CommandCommonServiceImpl commandCommonServiceImpl;
	
	@Autowired
	private CommandCastServiceImpl commandCastServiceImpl;
	
	@Autowired
	private CommandRecordServiceImpl commandRecordServiceImpl;
	
	@Autowired
	private CommandFightTimeServiceImpl commandFightTimeServiceImpl;
	
	@Autowired
	private CommandMeetingSpeakServiceImpl commandMeetingSpeakServiceImpl;
	
	@Autowired
	private CommandVodService commandVodService;
	
	@Autowired
	private PageTaskService pageTaskService;
	
	@Autowired
	private GroupDemandService groupDemandService;
	
	@Autowired
	private ResourceServiceClient resourceServiceClient;
	
	@Autowired
	private ResourceRemoteService resourceRemoteService;

	@Autowired
	private WebsocketMessageService websocketMessageService;
	
	@Autowired
	private TetrisBvcQueryUtil tetrisBvcQueryUtil;
	
	@Autowired
	private QueryUtil queryUtil;
	
	@Autowired
	private CommandCommonUtil commandCommonUtil;
	
	@Autowired
	private CommonQueryUtil commonQueryUtil;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;	
	
	@Autowired
	private OperationLogService operationLogService;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private CommandCascadeUtil commandCascadeUtil;
	
	@Autowired
	private CommandCascadeService commandCascadeService;
	
	@Autowired
	private ConferenceCascadeService conferenceCascadeService;
	
	public void pause(Long groupId) throws Exception{
		UserVO user = userQuery.current();
		
		if(groupId==null || groupId.equals("")){
			log.info("暂停会议，会议id有误");
			return;
		}
		
		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
			
			GroupPO group = groupDao.findOne(groupId);
			if(group.getStatus().equals(GroupStatus.STOP)){
				if(!OriginType.OUTER.equals(group.getOriginType())){
					throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已停止，无法操作，id: " + group.getId());
				}else{
					return;
				}
			}
			String commandString = commandCommonUtil.generateCommandString(group.getBusinessType());
			if(group.getStatus().equals(GroupStatus.REMIND)){
				if(group.getBusinessType().equals(BusinessType.COMMAND)){
					throw new BaseException(StatusCode.FORBIDDEN, "请先关闭" + commandString + "提醒");
				}else if(group.getBusinessType().equals(BusinessType.MEETING_QT)){
					throw new BaseException(StatusCode.FORBIDDEN, "请先关闭" + commandString + "提醒");
				}
			}
			group.setStatus(GroupStatus.PAUSE);
			
//			JSONArray chairSplits = new JSONArray();
			
			//查出所有分页任务，执行状态都置为UNDONE，查出所有forward，执行状态置为UNDONE，分页处理
			List<PageTaskPO> changeTasks = new ArrayList<PageTaskPO>();
			List<PageTaskPO> tasks = pageTaskDao.findByBusinessId(groupId.toString());
			List<CommonForwardPO> forwards = commonForwardDao.findByBusinessId(groupId.toString());
			for(CommonForwardPO forward : forwards){
				forward.setExecuteStatus(ExecuteStatus.UNDONE);
			}
			for(PageTaskPO task : tasks){
				boolean change = false;
				if(ExecuteStatus.DONE.equals(task.getAudioStatus())){
					task.setAudioStatus(ExecuteStatus.UNDONE);
					change = true;
				}
				if(ExecuteStatus.DONE.equals(task.getVideoStatus())){
					task.setVideoStatus(ExecuteStatus.UNDONE);
					change = true;
				}
				if(change){
					changeTasks.add(task);
				}
			}
			
			//持久化
			groupDao.save(group);
			pageTaskDao.save(tasks);
			commonForwardDao.save(forwards);
			
			
			List<GroupMemberPO> members = groupMemberDao.findByGroupId(groupId);
			List<GroupMemberPO> connectMembers = tetrisBvcQueryUtil.queryConnectMembers(members);
			
			pageTaskService.reExecute(changeTasks, true);
			
			
			/*
			Set<CommandGroupForwardPO> needDelForwards = new HashSet<CommandGroupForwardPO>();
			List<CommandGroupForwardPO> forwards = group.getForwards();
			for(CommandGroupForwardPO forward : forwards){
				if(forward.getExecuteStatus().equals(ExecuteStatus.DONE)){
					forward.setExecuteStatus(ExecuteStatus.UNDONE);
					needDelForwards.add(forward);
				}
			}
			
			commandGroupDao.save(group);*/
			
			//级联
			/*GroupType groupType = group.getType();
			if(!OriginType.OUTER.equals(group.getOriginType())){
				if(GroupType.BASIC.equals(groupType)){
					GroupBO groupBO = commandCascadeUtil.pauseCommand(group);
					commandCascadeService.pause(groupBO);
				}else if(GroupType.MEETING.equals(groupType)){
					GroupBO groupBO = commandCascadeUtil.pauseMeeting(group);
					conferenceCascadeService.pause(groupBO);
				}
			}*/
			
			//给成员推送message
			List<Long> consumeIds = new ArrayList<Long>();
			List<MessageSendCacheBO> messageCaches = new ArrayList<MessageSendCacheBO>();
			JSONArray splits = new JSONArray();
			JSONObject message = new JSONObject();
			message.put("businessType", "commandPause");
			message.put("businessInfo", group.getName() + " 暂停");
			message.put("businessId", group.getId().toString());
			message.put("splits", splits);
			for(GroupMemberPO member : connectMembers){
				if(member.getIsAdministrator()){
					continue;
				}else{
					messageCaches.add(new MessageSendCacheBO(Long.parseLong(member.getOriginId()), message.toJSONString(), WebsocketMessageType.COMMAND));
				}
			}
			
			//发消息
			for(MessageSendCacheBO cache : messageCaches){
				WebsocketMessageVO ws = websocketMessageService.send(cache.getUserId(), cache.getMessage(), cache.getType(), cache.getFromUserId(), cache.getFromUsername());
				consumeIds.add(ws.getId());
			}
			websocketMessageService.consumeAll(consumeIds);			
		}
	}
	
	public void pauseRecover(Long groupId) throws Exception{
		UserVO user = userQuery.current();
		
		if(groupId==null || groupId.equals("")){
			log.info("会议从暂停中恢复，会议id有误");
			return;
		}
		
		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
			
			GroupPO group = groupDao.findOne(groupId);
			if(group.getStatus().equals(GroupStatus.STOP)){
				if(!OriginType.OUTER.equals(group.getOriginType())){
					throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已停止，无法操作，id: " + group.getId());
				}else{
					return;
				}
			}
			group.setStatus(GroupStatus.START);
			groupDao.save(group);//需要吗？
			
			//级联
			/*GroupType groupType = group.getType();
			if(!OriginType.OUTER.equals(group.getOriginType())){
				if(GroupType.BASIC.equals(groupType)){
					GroupBO groupBO = commandCascadeUtil.resumeCommand(group);
					commandCascadeService.resume(groupBO);
				}else if(GroupType.MEETING.equals(groupType)){
					GroupBO groupBO = commandCascadeUtil.resumeMeeting(group);
					conferenceCascadeService.resume(groupBO);
				}
			}*/
			
			//查出所有分页任务，执行状态都置为DONE，查出所有forward，执行状态置为DONE，分页处理
			List<PageTaskPO> changeTasks = new ArrayList<PageTaskPO>();
			List<PageTaskPO> tasks = pageTaskDao.findByBusinessId(groupId.toString());
			List<CommonForwardPO> forwards = commonForwardDao.findByBusinessId(groupId.toString());
			for(CommonForwardPO forward : forwards){
				forward.setExecuteStatus(ExecuteStatus.DONE);
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
			groupDao.save(group);
			pageTaskDao.save(tasks);
			commonForwardDao.save(forwards);
			
			
			List<GroupMemberPO> members = groupMemberDao.findByGroupId(groupId);
			List<GroupMemberPO> connectMembers = tetrisBvcQueryUtil.queryConnectMembers(members);
			
			pageTaskService.reExecute(changeTasks, true);
			
			//后续：恢复会中的转发
//			startGroupForwards(group, true, true);
			
			//给成员推送message
			List<Long> consumeIds = new ArrayList<Long>();
			List<MessageSendCacheBO> messageCaches = new ArrayList<MessageSendCacheBO>();
			JSONObject message = new JSONObject();
			message.put("businessType", "commandPause");
			message.put("businessInfo", group.getName() + " 暂停恢复");
			message.put("businessId", group.getId().toString());
			message.put("splits", new JSONArray());
			for(GroupMemberPO member : connectMembers){
				if(member.getIsAdministrator()){
					continue;
				}else{
					messageCaches.add(new MessageSendCacheBO(Long.parseLong(member.getOriginId()), message.toJSONString(), WebsocketMessageType.COMMAND));
				}
			}
			
			//发消息
			for(MessageSendCacheBO cache : messageCaches){
				WebsocketMessageVO ws = websocketMessageService.send(cache.getUserId(), cache.getMessage(), cache.getType(), cache.getFromUserId(), cache.getFromUsername());
				consumeIds.add(ws.getId());
			}
			websocketMessageService.consumeAll(consumeIds);
			
			log.info(group.getName() + " 会议取消暂停");
			operationLogService.send(user.getNickname(), "恢复指挥", user.getNickname() + "恢复指挥" + groupId);
			return;
		}
	}
	
	/**
	 * 判断一条转发是否因为会议的暂停而暂停<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 下午3:16:37
	 * @param forward
	 * @return
	 */
	public boolean whetherStopForCommandPause(CommandGroupForwardPO forward){		
		if(forward.getGroup().getStatus().equals(GroupStatus.PAUSE)){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 执行一个会议中所有可以执行的转发<br/>
	 * <p>线程不安全，调用处必须使用 command-group-{groupId} 加锁</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月7日 下午1:24:43
	 * @param group 注意传入前应先把group、members、forwards的状态save正确
	 * @param doPersistence 是否持久化转发的执行状态为UNDONE，通常使用true
	 * @param doProtocol 是否下发协议
	 * @return
	 * @throws Exception
	 *//*
	public LogicBO startGroupForwards(CommandGroupPO group, boolean doPersistence, boolean doProtocol) throws Exception{
		List<GroupMemberPO> members = group.getMembers();
		GroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		List<CommandGroupForwardPO> forwards = group.getForwards();
		Set<CommandGroupForwardPO> needForwards = commandCommonUtil.queryForwardsReadyAndCanBeDone(members, forwards);
		for(CommandGroupForwardPO needForward : needForwards){
			needForward.setExecuteStatus(ExecuteStatus.DONE);
		}
		
		if(doPersistence) commandGroupDao.save(group);
		
		//生成forwardSet的logic
		CommandGroupAvtplGearsPO currentGear = commandCommonUtil.queryCurrentGear(group);
		CodecParamBO codec = new CodecParamBO().set(group.getAvtpl(), currentGear);
		LogicBO logic = openBundle(null, null, null, needForwards, null, codec, chairmanMember.getUserNum());		
		LogicBO logicCastDevice = commandCastServiceImpl.openBundleCastDevice(null, null, needForwards, null, null, null, codec, group.getUserId());
		logic.merge(logicCastDevice);
		
		//录制更新
		LogicBO logicRecord = commandRecordServiceImpl.update(group.getUserId(), group, 1, false);
		logic.merge(logicRecord);
				
		if(doProtocol){
			ExecuteBusinessReturnBO returnBO = executeBusiness.execute(logic, "执行 " + group.getName() + " 会议中的转发，共" + needForwards.size() + "个");
			commandRecordServiceImpl.saveStoreInfo(returnBO, group.getId());
		}
		
		return logic;
	}*/
	
	/**
	 * 执行所有会议中所有可以执行的转发<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月7日 下午2:04:47
	 * @param exceptGroupIds 排除的groupId列表，防止停止自己会议的转发
	 * @param doPersistence 是否持久化转发的执行状态为UNDONE，通常使用true
	 * @param doProtocol 是否下发协议：因为每个会议需要单独下发协议，不能合并，所以此处应使用true
	 * @throws Exception
	 *//*
	public void startAllGroupForwards(List<Long> exceptGroupIds, boolean doPersistence, boolean doProtocol) throws Exception{
		
		if(null == exceptGroupIds) exceptGroupIds = new ArrayList<Long>();
		List<Long> ids = commandGroupDao.findAllIds();
		for(Long groupId : ids){
			
			if(exceptGroupIds.contains(groupId)){
				continue;
			}
			
			synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {				
				CommandGroupPO group = commandGroupDao.findOne(groupId);
				GroupStatus status = group.getStatus();
				if(status.equals(GroupStatus.START) || status.equals(GroupStatus.REMIND)){
					startGroupForwards(group, doPersistence, doProtocol);				
				}
			}
		}
	}*/

	/**
	 * 停止所有会议中，特定源的转发，同时也会停止相关录制<br/>
	 * <p>通常用于专向会议建立时，专向会议的2个成员不能被其它业务看到</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月7日 上午9:43:29
	 * @param exceptGroupIds 排除的groupId列表，防止停止自己会议的转发
	 * @param srcMemberIds 源成员id列表
	 * @param doPersistence 是否持久化转发的执行状态为UNDONE，通常使用true
	 * @param doProtocol 是否下发协议：因为每个会议需要单独下发协议，不能合并，所以此处应使用true
	 * @return
	 * @throws Exception
	 *//*
	public void stopAllGroupForwardsBySrcMemberIds(List<Long> exceptGroupIds, List<Long> srcUserIds, boolean doPersistence, boolean doProtocol) throws Exception{
		
		if(null == exceptGroupIds) exceptGroupIds = new ArrayList<Long>();
		List<Long> ids = commandGroupDao.findAllIds();
		for(Long groupId : ids){
			
			if(exceptGroupIds.contains(groupId)){
				continue;
			}
			
			synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
				
				CommandGroupPO group = commandGroupDao.findOne(groupId);
				if(group.getStatus().equals(GroupStatus.PAUSE) || group.getStatus().equals(GroupStatus.STOP)){
					continue;
				}
				
				//查找需要停止的转发
				List<CommandGroupForwardPO> forwards = group.getForwards();
				List<GroupMemberPO> members = group.getMembers();
				GroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
				List<GroupMemberPO> srcMembers = commandCommonUtil.queryMembersByUserIds(members, srcUserIds);
				List<Long> srcMemberIds = new ArrayList<Long>();
				for(GroupMemberPO srcMember : srcMembers){
					srcMemberIds.add(srcMember.getId());
				}
				Set<CommandGroupForwardPO> needDelForwards = commandCommonUtil.queryForwardsBySrcmemberIds(forwards, srcMemberIds, null, ExecuteStatus.DONE);
				if(needDelForwards.size() == 0){
					continue;
				}
				for(CommandGroupForwardPO needDelForward : needDelForwards){
					needDelForward.setExecuteStatus(ExecuteStatus.UNDONE);
				}
				
				CommandGroupAvtplGearsPO currentGear = commandCommonUtil.queryCurrentGear(group);
				CodecParamBO codec = new CodecParamBO().set(group.getAvtpl(), currentGear);
				LogicBO logic1 = openBundle(null, null, null, null, needDelForwards, codec, chairmanMember.getUserNum());
				LogicBO logicCastDevice = commandCastServiceImpl.openBundleCastDevice(null, null, null, needDelForwards, null, null, codec, group.getUserId());
				logic1.merge(logicCastDevice);
//				logic.merge(logic1);
				
				//录制更新
				LogicBO logicRecord = commandRecordServiceImpl.update(group.getUserId(), group, 1, false);
				logic1.merge(logicRecord);
				
				if(doPersistence) commandGroupDao.save(group);
				
				if(doProtocol){
					ExecuteBusinessReturnBO returnBO = executeBusiness.execute(logic1, "停止 " + group.getName() + "会议 特定源的转发，共" + needDelForwards.size() + "个");
					commandRecordServiceImpl.saveStoreInfo(returnBO, group.getId());
				}
			}
		}
//		if(doProtocol){
//			executeBusiness.execute(logic, "停止所有会议中的 特定源的转发");
//		}		
//		return logic;
	}*/
}

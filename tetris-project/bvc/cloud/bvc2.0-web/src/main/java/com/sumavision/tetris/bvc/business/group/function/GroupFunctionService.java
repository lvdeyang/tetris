package com.sumavision.tetris.bvc.business.group.function;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.FolderUserMapDAO;
import com.suma.venus.resource.service.ResourceRemoteService;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.command.group.dao.CommandGroupDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupMemberDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupUserPlayerDAO;
import com.sumavision.bvc.command.group.forward.CommandGroupForwardPO;
import com.sumavision.bvc.device.command.bo.MessageSendCacheBO;
import com.sumavision.bvc.device.command.cascade.util.CommandCascadeUtil;
import com.sumavision.bvc.device.command.cast.CommandCastServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.command.meeting.CommandMeetingSpeakServiceImpl;
import com.sumavision.bvc.device.command.record.CommandRecordServiceImpl;
import com.sumavision.bvc.device.command.time.CommandFightTimeServiceImpl;
import com.sumavision.bvc.device.command.vod.CommandVodService;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.CommonQueryUtil;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.feign.ResourceServiceClient;
import com.sumavision.bvc.log.OperationLogService;
import com.sumavision.bvc.resource.dao.ResourceBundleDAO;
import com.sumavision.bvc.resource.dao.ResourceChannelDAO;
import com.sumavision.tetris.bvc.business.ExecuteStatus;
import com.sumavision.tetris.bvc.business.OriginType;
import com.sumavision.tetris.bvc.business.common.BusinessCommonService;
import com.sumavision.tetris.bvc.business.common.BusinessReturnService;
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
import com.sumavision.tetris.bvc.business.group.demand.GroupDemandService;
import com.sumavision.tetris.bvc.cascade.CommandCascadeService;
import com.sumavision.tetris.bvc.cascade.ConferenceCascadeService;
import com.sumavision.tetris.bvc.model.agenda.AgendaDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaService;
import com.sumavision.tetris.bvc.page.PageTaskDAO;
import com.sumavision.tetris.bvc.page.PageTaskPO;
import com.sumavision.tetris.bvc.page.PageTaskService;
import com.sumavision.tetris.bvc.util.TetrisBvcQueryUtil;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
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
	
	@Autowired
	private BusinessReturnService businessReturnService;
	
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
				forward.setVideoStatus(ExecuteStatus.UNDONE);
				forward.setAudioStatus(ExecuteStatus.UNDONE);
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
			if(businessReturnService.getSegmentedExecute()){
				for(MessageSendCacheBO cache : messageCaches){
					businessReturnService.add(null, cache, null);
				}
				businessReturnService.execute();
			}else{
				for(MessageSendCacheBO cache : messageCaches){
					WebsocketMessageVO ws = websocketMessageService.send(cache.getUserId(), cache.getMessage(), cache.getType(), cache.getFromUserId(), cache.getFromUsername());
					consumeIds.add(ws.getId());
				}
				websocketMessageService.consumeAll(consumeIds);		
			}
				
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
			
			//开启所有只因暂停原因停止的转发，
			businessCommonService.startGroupForwards(group, true, true);
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
//			List<PageTaskPO> changeTasks = new ArrayList<PageTaskPO>();
//			List<PageTaskPO> tasks = pageTaskDao.findByBusinessId(groupId.toString());
//			List<CommonForwardPO> forwards = commonForwardDao.findByBusinessId(groupId.toString());
//			for(CommonForwardPO forward : forwards){
//				forward.setVideoStatus(ExecuteStatus.DONE);
//				forward.setAudioStatus(ExecuteStatus.DONE);
//			}
//			for(PageTaskPO task : tasks){
//				boolean change = false;
//				if(ExecuteStatus.UNDONE.equals(task.getAudioStatus())){
//					task.setAudioStatus(ExecuteStatus.DONE);
//					change = true;
//				}
//				if(ExecuteStatus.UNDONE.equals(task.getVideoStatus())){
//					task.setVideoStatus(ExecuteStatus.DONE);
//					change = true;
//				}
//				if(change){
//					changeTasks.add(task);
//				}
//			}
//			
//			//持久化
//			groupDao.save(group);
//			pageTaskDao.save(tasks);
//			commonForwardDao.save(forwards);
//			
//			
		
//			
//			pageTaskService.reExecute(changeTasks, true);
			
			//后续：恢复会中的转发
//			startGroupForwards(group, true, true);
			
			//给成员推送message
			List<Long> consumeIds = new ArrayList<Long>();
			List<MessageSendCacheBO> messageCaches = new ArrayList<MessageSendCacheBO>();
			List<GroupMemberPO> members = groupMemberDao.findByGroupId(groupId);
			List<GroupMemberPO> connectMembers = tetrisBvcQueryUtil.queryConnectMembers(members);
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
			if(businessReturnService.getSegmentedExecute()){
				for(MessageSendCacheBO cache : messageCaches){
					businessReturnService.add(null, cache, group.getName() + " 会议取消暂停");
				}
				businessReturnService.execute();
			}else{
				for(MessageSendCacheBO cache : messageCaches){
					WebsocketMessageVO ws = websocketMessageService.send(cache.getUserId(), cache.getMessage(), cache.getType(), cache.getFromUserId(), cache.getFromUsername());
					consumeIds.add(ws.getId());
				}
				websocketMessageService.consumeAll(consumeIds);
				
				log.info(group.getName() + " 会议取消暂停");
			}
			
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
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月26日 上午9:55:15
	 * @param group 注意传入前应先把group、members、forwards的状态save正确
	 * @param doPersistence 是否持久化转发的执行状态为UNDONE，通常使用true
	 * @param doProtocol 是否下发协议
	 * @return
	 * @throws Exception
	 */
//	public LogicBO startGroupForwards(CommandGroupPO group, boolean doPersistence, boolean doProtocol) throws Exception{
//		List<GroupMemberPO> members = group.getMembers();
//		GroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
//		List<CommandGroupForwardPO> forwards = group.getForwards();
//		Set<CommandGroupForwardPO> needForwards = commandCommonUtil.queryForwardsReadyAndCanBeDone(members, forwards);
//		for(CommandGroupForwardPO needForward : needForwards){
//			needForward.setExecuteStatus(ExecuteStatus.DONE);
//		}
//		
//		if(doPersistence) commandGroupDao.save(group);
//		
//		return pageTaskService.reExecute(changeTasks, true);
//	}
	
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
	
	/** 会议中的一个成员开启静默<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月25日 上午11:24:04
	 * @param groupId
	 * @param userId 操作成员的userId
	 * @param silenceToHigher 开启对上静默，false表示保持原状，不表示关闭
	 * @param silenceToLower 开启对下静默
	 * @throws Exception
	 */
	public void startSilence(Long groupId, Long userId, boolean silenceToHigher, boolean silenceToLower) throws Exception{
		
		if(groupId == null){
			log.warn("开启静默，groupId有误");
			return;
		}
		
		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
					
			GroupPO group = groupDao.findOne(groupId);
			//CommandGroupPO group = commandGroupDao.findOne(groupId);
			if(group == null){
				log.warn("开启静默，没有查到group，groupId：" + groupId);
				return;
			}
			
			if(group.getStatus().equals(GroupStatus.STOP)){
				if(!OriginType.OUTER.equals(group.getOriginType())){
					throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已停止，无法操作，id: " + group.getId());
				}else{
					return;
				}
			}
			
			//修改当前人员对上/对下静默
			List<GroupMemberPO> members = groupMemberDao.findByGroupId(groupId);
			GroupMemberPO operateMember = tetrisBvcQueryUtil.queryMemberByUserId(members, userId);
			if(silenceToHigher) operateMember.setSilenceToHigher(true);
			if(silenceToLower) operateMember.setSilenceToLower(true);
			
			Set<CommonForwardPO> needDelForwards = new HashSet<CommonForwardPO>();
			List<CommonForwardPO> forwards = commonForwardDao.findByBusinessId(groupId.toString());
			List<Long> srcMemberIds = new ArrayListWrapper<Long>().add(operateMember.getId()).getList();
			Set<CommonForwardPO> relativeForwards = tetrisBvcQueryUtil.queryForwardsBySrcmemberIds(forwards, srcMemberIds, null);
			
			
			//从源和目的判断是否需要静默，是的话删除转发
			for(CommonForwardPO forward : relativeForwards){
				
				//自己给自己的转发不处理
				if(operateMember.getId().equals(forward.getDstMemberId())){
					continue;
				}
				
				GroupMemberPO dstMember = tetrisBvcQueryUtil.queryMemberById(members, forward.getDstMemberId());
				int levelCompare = businessCommonService.compareLevelByMemberIsChairman(operateMember, dstMember);
				if(silenceToHigher && levelCompare<0){
					//对上静默
//					forward.setExecuteStatus(ExecuteStatus.UNDONE);
					forward.setVideoStatus(ExecuteStatus.UNDONE);
					forward.setAudioStatus(ExecuteStatus.UNDONE);
					needDelForwards.add(forward);
				}else if(silenceToLower && levelCompare>0){
					//对下静默
					forward.setVideoStatus(ExecuteStatus.UNDONE);
					forward.setAudioStatus(ExecuteStatus.UNDONE);
					needDelForwards.add(forward);
				}
			}
			
			//有需要删除的转发就重新发起
			if(needDelForwards.size()>0){
				//从forwardUuid查找对应的pagetask，执行状态都置为UNDONE，分页处理
				List<PageTaskPO> changeTasks = new ArrayList<PageTaskPO>();
				List<String> forwardUuids=new ArrayList<String>();
				for(CommonForwardPO forward :needDelForwards){
					forwardUuids.add(forward.getUuid());
				}
				List<PageTaskPO> tasks = pageTaskDao.findByForwardUuidIn(forwardUuids);
				
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
				commonForwardDao.save(relativeForwards);
				
				pageTaskService.reExecute(changeTasks, true);
			}
			
			
			//级联
			/*if(!OriginType.OUTER.equals(operateMember.getOriginType())){
				if(silenceToHigher){
					GroupBO groupBO = commandCascadeUtil.becomeSilence(group, operateMember, "silencehigherstart");
					commandCascadeService.becomeSilence(groupBO);
				}
				if(silenceToLower){
					GroupBO groupBO = commandCascadeUtil.becomeSilence(group, operateMember, "silencelowerstart");
					commandCascadeService.becomeSilence(groupBO);
				}
			}*/
			if(businessReturnService.getSegmentedExecute()){
				businessReturnService.execute();
			}
		}
	}
	
//	/**
//	 * 会议中一个成员停止静默<br/>
//	 * <b>作者:</b>lx<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2020年8月26日 上午9:36:08
//	 * @param groupId 组id
//	 * @param userId 操作成员的userId
//	 * @param stopSilenceToHigher 关闭对上静默，false表示保持原状，不表示关闭
//	 * @param stopSilenceToLower 关闭对下静默
//	 * @throws Exception
//	 */
//	public void stopSilence(Long groupId, Long userId, boolean stopSilenceToHigher, boolean stopSilenceToLower) throws Exception{
//		
//		if(groupId == null){
//			log.warn("停止静默，groupId有误");
//			return;
//		}
//		
//		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
//			
//			GroupPO group = groupDao.findOne(groupId);
//			if(group == null){
//				log.warn("停止静默，没有查到group，groupId：" + groupId);
//				return;
//			}
//			
//			if(group.getStatus().equals(GroupStatus.STOP)){
//				if(!OriginType.OUTER.equals(group.getOriginType())){
//					throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已停止，无法操作，id: " + group.getId());
//				}else{
//					return;
//				}
//			}
//			
//			List<GroupMemberPO> members = groupMemberDao.findByGroupId(groupId);
//			GroupMemberPO operateMember = tetrisBvcQueryUtil.queryMemberByUserId(members, userId);
//			//1.组成员取消对上对下静默属性，对应的Pagetask、forwards设置为DONE。
//			if(stopSilenceToHigher) operateMember.setSilenceToHigher(false);
//			if(stopSilenceToLower) operateMember.setSilenceToLower(false);
//			
//			//这里可以优化
//			List<CommonForwardPO> forwards = commonForwardDao.findByBusinessId(groupId.toString());
//			List<Long> srcMemberIds = new ArrayListWrapper<Long>().add(operateMember.getId()).getList();
//			Set<CommonForwardPO> relativeForwards = tetrisBvcQueryUtil.queryForwardsBySrcmemberIds(forwards, srcMemberIds, null);
//			List<CommonForwardPO> needAddForwards =new ArrayList<CommonForwardPO>();
//			
//			//从源和目的判断是否需要停止静默，是的话添加转发
//			for(CommonForwardPO forward : relativeForwards){
//				
//				//自己给自己的转发不处理
//				if(operateMember.getId().equals(forward.getDstMemberId())){
//					continue;
//				}
//				
//				GroupMemberPO dstMember = tetrisBvcQueryUtil.queryMemberById(members, forward.getDstMemberId());
//				int levelCompare = tetrisBvcQueryUtil.compareLevelByMemberIsChairman(operateMember, dstMember);
//				if(stopSilenceToHigher && levelCompare<0){
//					//停止对上静默
////					forward.setExecuteStatus(ExecuteStatus.UNDONE);
//					forward.setVideoStatus(ExecuteStatus.UNDONE);
//					forward.setAudioStatus(ExecuteStatus.UNDONE);
//					needAddForwards.add(forward);
//				}else if(stopSilenceToLower && levelCompare>0){
//					//停止对下静默
//					forward.setVideoStatus(ExecuteStatus.UNDONE);
//					forward.setAudioStatus(ExecuteStatus.UNDONE);
//					needAddForwards.add(forward);
//				}
//			}
//			
//			//有需要添加的转发
//			if(needAddForwards.size()>0){
//				//从forwardUuid查找对应的pagetask，执行状态都置为DONE，分页处理
//				List<PageTaskPO> changeTasks = new ArrayList<PageTaskPO>();
//				List<String> forwardUuids=new ArrayList<String>();
//				for(CommonForwardPO forward :needAddForwards){
//					forwardUuids.add(forward.getUuid());
//				}
//				List<PageTaskPO> tasks = pageTaskDao.findByForwardUuidIn(forwardUuids);
//				
//				for(PageTaskPO task : tasks){
//					boolean change = false;
//					if(ExecuteStatus.UNDONE.equals(task.getAudioStatus())){
//						task.setAudioStatus(ExecuteStatus.DONE);
//						change = true;
//					}
//					if(ExecuteStatus.UNDONE.equals(task.getVideoStatus())){
//						task.setVideoStatus(ExecuteStatus.DONE);
//						change = true;
//					}
//					if(change){
//						changeTasks.add(task);
//					}
//				}
//				
//				//持久化
//				groupDao.save(group);
//				pageTaskDao.save(tasks);
//				commonForwardDao.save(relativeForwards);
//				
//				pageTaskService.reExecute(changeTasks, true);
//				//2.重新查询会中的转发，并重新发送协议
////				commandBasicServiceImpl.startGroupForwards(group, true, true);
//			}
//			
//			//级联
////			if(!OriginType.OUTER.equals(operateMember.getOriginType())){
////				if(stopSilenceToHigher){
////					GroupBO groupBO = commandCascadeUtil.becomeSilence(group, operateMember, "silencehigherstop");
////					commandCascadeService.becomeSilence(groupBO);
////				}
////				if(stopSilenceToLower){
////					GroupBO groupBO = commandCascadeUtil.becomeSilence(group, operateMember, "silencelowerstop");
////					commandCascadeService.becomeSilence(groupBO);
////				}
////			}
//		}
//	}
	
	/**
	 * 会议中一个成员停止静默<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月26日 上午9:36:08
	 * @param groupId 组id
	 * @param userId 操作成员的userId
	 * @param stopSilenceToHigher 关闭对上静默，false表示保持原状，不表示关闭
	 * @param stopSilenceToLower 关闭对下静默
	 * @throws Exception
	 */
	public void stopSilence(Long groupId, Long userId, boolean stopSilenceToHigher, boolean stopSilenceToLower) throws Exception{
		
		if(groupId == null){
			log.warn("停止静默，groupId有误");
			return;
		}
		
		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
			
			GroupPO group = groupDao.findOne(groupId);
			if(group == null){
				log.warn("停止静默，没有查到group，groupId：" + groupId);
				return;
			}
			
			if(group.getStatus().equals(GroupStatus.STOP)){
				if(!OriginType.OUTER.equals(group.getOriginType())){
					throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已停止，无法操作，id: " + group.getId());
				}else{
					return;
				}
			}
			
			List<GroupMemberPO> members = groupMemberDao.findByGroupId(groupId);
			GroupMemberPO operateMember = tetrisBvcQueryUtil.queryMemberByUserId(members, userId);
			//1.组成员取消对上对下静默属性。作用：跳过下面调用方法startGroupForwards对静默导致转发关闭的判断。
			if(stopSilenceToHigher) operateMember.setSilenceToHigher(false);
			if(stopSilenceToLower) operateMember.setSilenceToLower(false);
			
		
			//持久化
			groupMemberDao.save(operateMember);
			
			//2.重新查询开始转发，并重新发送协议
			businessCommonService.startGroupForwards(group, true, true);
			//级联
//			if(!OriginType.OUTER.equals(operateMember.getOriginType())){
//				if(stopSilenceToHigher){
//					GroupBO groupBO = commandCascadeUtil.becomeSilence(group, operateMember, "silencehigherstop");
//					commandCascadeService.becomeSilence(groupBO);
//				}
//				if(stopSilenceToLower){
//					GroupBO groupBO = commandCascadeUtil.becomeSilence(group, operateMember, "silencelowerstop");
//					commandCascadeService.becomeSilence(groupBO);
//				}
//			}
			if(businessReturnService.getSegmentedExecute()){
				businessReturnService.execute();
			}
		}
		
	}
}

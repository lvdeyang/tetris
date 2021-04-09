package com.sumavision.tetris.bvc.business.group.secret;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.FolderUserMapDAO;
import com.suma.venus.resource.service.ResourceRemoteService;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.command.group.dao.CommandGroupDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupMemberDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupUserPlayerDAO;
import com.sumavision.bvc.device.command.bo.MessageSendCacheBO;
import com.sumavision.bvc.device.command.cascade.util.CommandCascadeUtil;
import com.sumavision.bvc.device.command.cascade.util.GroupCascadeUtil;
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
import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.bvc.business.OriginType;
import com.sumavision.tetris.bvc.business.bo.BusinessDeliverBO;
import com.sumavision.tetris.bvc.business.common.BusinessCommonService;
import com.sumavision.tetris.bvc.business.common.BusinessReturnService;
import com.sumavision.tetris.bvc.business.dao.CommonForwardDAO;
import com.sumavision.tetris.bvc.business.dao.GroupDAO;
import com.sumavision.tetris.bvc.business.dao.GroupDemandDAO;
import com.sumavision.tetris.bvc.business.dao.GroupMemberRolePermissionDAO;
import com.sumavision.tetris.bvc.business.deliver.DeliverExecuteService;
import com.sumavision.tetris.bvc.business.group.BusinessType;
import com.sumavision.tetris.bvc.business.group.GroupMemberStatus;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.bvc.business.group.GroupStatus;
import com.sumavision.tetris.bvc.business.po.info.GroupCommandInfoDAO;
import com.sumavision.tetris.bvc.business.po.info.GroupCommandInfoPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberPO;
import com.sumavision.tetris.bvc.business.special.GroupSpecialCommonService;
import com.sumavision.tetris.bvc.business.special.GroupSpecialCommonService.MemberForward;
import com.sumavision.tetris.bvc.cascade.CommandCascadeService;
import com.sumavision.tetris.bvc.cascade.ConferenceCascadeService;
import com.sumavision.tetris.bvc.cascade.bo.GroupBO;
import com.sumavision.tetris.bvc.model.agenda.AgendaExecuteService;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardPO;
import com.sumavision.tetris.bvc.page.PageTaskDAO;
import com.sumavision.tetris.bvc.page.PageTaskService;
import com.sumavision.tetris.bvc.util.TetrisBvcQueryUtil;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.mvc.util.BaseUtils;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;
import com.sumavision.tetris.websocket.message.WebsocketMessageType;
import com.sumavision.tetris.websocket.message.WebsocketMessageVO;

import lombok.extern.slf4j.Slf4j;

/**
 * 专向指挥<br/>
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2021年2月23日 上午11:29:00
 */
@Slf4j
@Service
public class GroupSecretService {
	
	/** synchronized锁的前缀 */
	private static final String lockProcessPrefix = "tetris-group-";
	
	@Autowired
	private AgendaForwardDAO agendaForwardDao;
	
	@Autowired
	private GroupDAO groupDao;
	
	@Autowired
	private GroupCascadeUtil groupCascadeUtil;
	
	@Autowired
	private GroupCommandInfoDAO groupCommandInfoDao;
	
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
	
	//@Autowired
	//private AgendaService agendaService;
	
	@Autowired
	private BusinessCommonService businessCommonService;
	
	@Autowired
	private GroupSpecialCommonService groupSpecialCommonService;
	
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
	
//	@Autowired
//	private GroupDemandService groupDemandService;
	
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
	private AgendaExecuteService agendaExecuteService;
	
	@Autowired
	private DeliverExecuteService deliverExecuteService;
	
	@Autowired
	private CommandCascadeService commandCascadeService;
	
	@Autowired
	private ConferenceCascadeService conferenceCascadeService;
	
	@Autowired
	private BusinessReturnService businessReturnService;
	
	/**
	 * 发起专向指挥<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月23日 下午4:49:51
	 * @param groupId
	 * @param userId 操作人
	 * @param highUserId 专向上级成员
	 * @param lowUserId 专向下级成员
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void startSecret(Long groupId, Long userId, Long highUserId, Long lowUserId) throws Exception{
		UserVO user = userQuery.current();
		
		if(groupId==null || groupId.equals("")){
			log.info("发起专向会议，会议id有误");
			return;
		}
		
		synchronized (new StringBuffer().append(lockProcessPrefix).append(groupId).toString().intern()) {
			
			GroupPO group = groupDao.findOne(groupId);
			if(group.getStatus().equals(GroupStatus.STOP)){
				if(!OriginType.OUTER.equals(group.getOriginType())){
					throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已停止，无法操作，id: " + group.getId());
				}else{
					return;
				}
			}
			
			GroupCommandInfoPO commandInfo = groupCommandInfoDao.findByGroupId(groupId);
			if(commandInfo == null){
				commandInfo = new GroupCommandInfoPO(groupId);
			}
			
			//校验是否已有特殊指挥
			groupSpecialCommonService.checkHasSpecialCommand(commandInfo, true);
			
			List<BusinessGroupMemberPO> members = group.getMembers();
			BusinessGroupMemberPO highMember = tetrisBvcQueryUtil.queryBusinessMemberByUserId(members, highUserId);
			BusinessGroupMemberPO lowMember = tetrisBvcQueryUtil.queryBusinessMemberByUserId(members, lowUserId);
			
			if(highMember.getId().equals(lowMember.getId())){
				throw new BaseException(StatusCode.FORBIDDEN, "请选择其他成员");
			}
			
			if(!highMember.getGroupMemberStatus().equals(GroupMemberStatus.CONNECT)){
				throw new BaseException(StatusCode.FORBIDDEN, highMember.getName() + " 没有进入指挥");
			}
			if(!lowMember.getGroupMemberStatus().equals(GroupMemberStatus.CONNECT)){
				throw new BaseException(StatusCode.FORBIDDEN, lowMember.getName() + " 没有进入指挥");
			}
			
			commandInfo.setHasSecret(true);
			commandInfo.setSecretHighMemberId(highMember.getId());
			commandInfo.setSecretLowMemberId(lowMember.getId());
			groupCommandInfoDao.save(commandInfo);
			
			//生成转发源-转发目的对应关系
			List<BusinessGroupMemberPO> secretMembers = new ArrayListWrapper<BusinessGroupMemberPO>().add(lowMember).add(highMember).getList();
			List<MemberForward> memberForwards = new ArrayList<MemberForward>();
			for(BusinessGroupMemberPO srcMember : secretMembers){
				for (BusinessGroupMemberPO dstMember : secretMembers) {
					if(srcMember != dstMember){
						MemberForward memberForward = new GroupSpecialCommonService.MemberForward();
						memberForward.setSrcMember(srcMember);
						memberForward.setDstMember(dstMember);
						memberForwards.add(memberForward);
					}
				}
			}
			
			//生成议程转发
			List<AgendaForwardPO> agendaForwardList = groupSpecialCommonService.createGroupForward(groupId, memberForwards);
			agendaForwardList.forEach(forward -> forward.setAgendaForwardBusinessType(BusinessInfoType.SECRET_COMMAND));
			agendaForwardDao.save(agendaForwardList);
			List<String> forwardIds = agendaForwardList.stream().map(AgendaForwardPO::getId).map(String::valueOf).collect(Collectors.toList());
			if(BaseUtils.stringIsNotBlank(commandInfo.getSecretAgendaForwardIds())){
				String secretforwardIds = commandInfo.getSecretAgendaForwardIds()+","+String.join(",", forwardIds);
				commandInfo.setSecretAgendaForwardIds(secretforwardIds);
			}else{
				commandInfo.setSecretAgendaForwardIds(String.join(",", forwardIds));
			}
			groupCommandInfoDao.save(commandInfo);
			
			//重新执行议程。考虑优化成重新设置ExecuteStatus状态
			BusinessDeliverBO businessDeliverBO = new BusinessDeliverBO().setGroup(group).setUserId(group.getUserId().toString());
			agendaExecuteService.reRunAgenda(groupId, businessDeliverBO, false);
			
			//给成员推送message
			List<Long> consumeIds = new ArrayList<Long>();
			List<MessageSendCacheBO> messageCaches = new ArrayList<MessageSendCacheBO>();
			JSONArray splits = new JSONArray();
			JSONObject message = new JSONObject();
			message.put("businessType", "secretStop");
			message.put("businessId", group.getId().toString());
			message.put("splits", splits);
			message.put("businessInfo", highMember.getName() + " 与您开始了专向指挥");
			messageCaches.add(new MessageSendCacheBO(Long.parseLong(lowMember.getOriginId()), message.toJSONString(), WebsocketMessageType.COMMAND));
			message.put("businessInfo", lowMember.getName() + " 与您开始了专向指挥");
			messageCaches.add(new MessageSendCacheBO(Long.parseLong(highMember.getOriginId()), message.toJSONString(), WebsocketMessageType.COMMAND));
			
			//发消息
			if(businessReturnService.getSegmentedExecute()){
				for(MessageSendCacheBO cache : messageCaches){
					businessReturnService.add(null, cache, null);
				}
//				businessReturnService.execute();
			}else{
				for(MessageSendCacheBO cache : messageCaches){
					WebsocketMessageVO ws = websocketMessageService.send(cache.getUserId(), cache.getMessage(), cache.getType(), cache.getFromUserId(), cache.getFromUsername());
					consumeIds.add(ws.getId());
				}
				websocketMessageService.consumeAll(consumeIds);		
			}
			
			deliverExecuteService.execute(businessDeliverBO, highMember.getName() + " 与 " + lowMember.getName() + " 开始专向指挥", true);
			
			//级联
			BusinessType businessType = group.getBusinessType();
			if(!OriginType.OUTER.equals(group.getOriginType())){
				GroupBO groupBO = groupCascadeUtil.secretGroupBO(group, highMember.getCode(), lowMember.getCode());
				if(BusinessType.COMMAND.equals(businessType)){
					commandCascadeService.startSecret(groupBO);
				}else if(BusinessType.MEETING_BVC.equals(businessType)){
					conferenceCascadeService.startSecret(groupBO);
				}
			}
			
			operationLogService.send(user.getNickname(), "专向指挥", highMember.getName() + " 与 " + lowMember.getName() + " 开始专向指挥");
		}
	}

	/**
	 * 停止专向指挥<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月23日 下午4:50:48
	 * @param groupId
	 * @param userId 操作人
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void stopSecret(Long groupId, Long userId) throws Exception{
		UserVO user = userQuery.current();
		
		if(groupId==null || groupId.equals("")){
			log.info("停止专向会议，会议id有误");
			return;
		}
		
		synchronized (new StringBuffer().append(lockProcessPrefix).append(groupId).toString().intern()) {
			
			GroupPO group = groupDao.findOne(groupId);
			if(group.getStatus().equals(GroupStatus.STOP)){
				return;
			}
			
			GroupCommandInfoPO commandInfo = groupCommandInfoDao.findByGroupId(groupId);
			if(commandInfo == null){
				commandInfo = new GroupCommandInfoPO(groupId);
				groupCommandInfoDao.save(commandInfo);
			}
			
			if(!commandInfo.isHasSecret()){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 中没有专向指挥");
//				log.warn(group.getName() + " 进行停止专向指挥操作，此时没有专向指挥可以停止");
//				return;
			}
			
			List<BusinessGroupMemberPO> members = group.getMembers();
			BusinessGroupMemberPO highMember = tetrisBvcQueryUtil.queryMemberById(members, commandInfo.getSecretHighMemberId());
			BusinessGroupMemberPO lowMember = tetrisBvcQueryUtil.queryMemberById(members, commandInfo.getSecretLowMemberId());
			
			/*if(!highMember.getGroupMemberStatus().equals(GroupMemberStatus.CONNECT)){
				throw new BaseException(StatusCode.FORBIDDEN, highMember.getName() + " 没有进入指挥");
			}			
			if(!lowMember.getGroupMemberStatus().equals(GroupMemberStatus.CONNECT)){
				throw new BaseException(StatusCode.FORBIDDEN, lowMember.getName() + " 没有进入指挥");
			}*/
			
			//删除议程转发
			List<Long> allForwardIds = new ArrayList<Long>();
			String deleteAgendaIds = commandInfo.getSecretAgendaForwardIds();
			if(BaseUtils.stringIsNotBlank(deleteAgendaIds)){
				allForwardIds = Stream.of(deleteAgendaIds.split(",")).map(Long::valueOf).collect(Collectors.toList());
			}
			List<AgendaForwardPO> agendaforawrds = agendaForwardDao.findAll(allForwardIds);
			groupSpecialCommonService.clearGroupForward(agendaforawrds);
			
			commandInfo.clearSecret();
			groupCommandInfoDao.save(commandInfo);
			
			//重新执行议程。考虑优化成重新设置ExecuteStatus状态
			BusinessDeliverBO businessDeliverBO = new BusinessDeliverBO().setGroup(group).setUserId(group.getUserId().toString());
			agendaExecuteService.reRunAgenda(groupId, businessDeliverBO, false);
			
			//给成员推送message
			List<Long> consumeIds = new ArrayList<Long>();
			List<MessageSendCacheBO> messageCaches = new ArrayList<MessageSendCacheBO>();
			JSONArray splits = new JSONArray();
			JSONObject message = new JSONObject();
			message.put("businessType", "secretStop");
			message.put("businessId", group.getId().toString());
			message.put("splits", splits);
			message.put("businessInfo", highMember.getName() + " 与您的专向指挥已结束");
			messageCaches.add(new MessageSendCacheBO(Long.parseLong(lowMember.getOriginId()), message.toJSONString(), WebsocketMessageType.COMMAND));
			message.put("businessInfo", lowMember.getName() + " 与您的专向指挥已结束");
			messageCaches.add(new MessageSendCacheBO(Long.parseLong(highMember.getOriginId()), message.toJSONString(), WebsocketMessageType.COMMAND));
			
			//发消息
			if(businessReturnService.getSegmentedExecute()){
				for(MessageSendCacheBO cache : messageCaches){
					businessReturnService.add(null, cache, null);
				}
			}else{
				for(MessageSendCacheBO cache : messageCaches){
					WebsocketMessageVO ws = websocketMessageService.send(cache.getUserId(), cache.getMessage(), cache.getType(), cache.getFromUserId(), cache.getFromUsername());
					consumeIds.add(ws.getId());
				}
				websocketMessageService.consumeAll(consumeIds);		
			}
			
			deliverExecuteService.execute(businessDeliverBO, highMember.getName() + " 与 " + lowMember.getName() + " 停止专向指挥", true);
			
			//级联
			BusinessType businessType = group.getBusinessType();
			if(!OriginType.OUTER.equals(group.getOriginType())){
				GroupBO groupBO = groupCascadeUtil.secretGroupBO(group, highMember.getCode(), lowMember.getCode());
				if(BusinessType.COMMAND.equals(businessType)){
					commandCascadeService.stopSecret(groupBO);
				}else if(BusinessType.MEETING_BVC.equals(businessType)){
					conferenceCascadeService.stopSecret(groupBO);
				}
			}
			
			operationLogService.send(user.getNickname(), "停止专向指挥", highMember.getName() + " 与 " + lowMember.getName() + " 专向指挥停止");
		}
	}
}

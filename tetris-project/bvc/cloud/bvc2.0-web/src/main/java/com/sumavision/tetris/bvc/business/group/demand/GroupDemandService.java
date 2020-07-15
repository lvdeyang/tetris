package com.sumavision.tetris.bvc.business.group.demand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.dao.FolderUserMapDAO;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.FolderUserMap;
import com.suma.venus.resource.service.ResourceRemoteService;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.command.group.basic.CommandGroupAvtplGearsPO;
import com.sumavision.bvc.command.group.basic.CommandGroupAvtplPO;
import com.sumavision.bvc.command.group.basic.CommandGroupMemberPO;
import com.sumavision.bvc.command.group.basic.CommandGroupPO;
import com.sumavision.bvc.command.group.dao.CommandGroupDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupForwardDemandDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupUserPlayerDAO;
import com.sumavision.bvc.command.group.enumeration.ExecuteStatus;
import com.sumavision.bvc.command.group.enumeration.ForwardDemandBusinessType;
import com.sumavision.bvc.command.group.enumeration.ForwardDemandStatus;
import com.sumavision.bvc.command.group.enumeration.ForwardDstType;
import com.sumavision.bvc.command.group.enumeration.GroupStatus;
import com.sumavision.bvc.command.group.enumeration.GroupType;
import com.sumavision.bvc.command.group.enumeration.MemberStatus;
import com.sumavision.bvc.command.group.forward.CommandGroupForwardDemandPO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.command.group.user.layout.player.PlayerBusinessType;
import com.sumavision.bvc.control.device.command.group.vo.BusinessPlayerVO;
import com.sumavision.bvc.device.command.basic.CommandBasicServiceImpl;
import com.sumavision.bvc.device.command.basic.forward.ForwardReturnBO;
import com.sumavision.bvc.device.command.bo.MessageSendCacheBO;
import com.sumavision.bvc.device.command.cascade.util.CommandCascadeUtil;
import com.sumavision.bvc.device.command.cast.CommandCastServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.command.exception.UserHasNoAvailableEncoderException;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.CommonQueryUtil;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.device.monitor.live.DstDeviceType;
import com.sumavision.bvc.device.monitor.playback.exception.ResourceNotExistException;
import com.sumavision.bvc.resource.dao.ResourceBundleDAO;
import com.sumavision.bvc.resource.dao.ResourceChannelDAO;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.tetris.auth.token.TerminalType;
import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.bvc.business.OriginType;
import com.sumavision.tetris.bvc.business.bo.SourceBO;
import com.sumavision.tetris.bvc.business.dao.GroupDAO;
import com.sumavision.tetris.bvc.business.dao.GroupDemandDAO;
import com.sumavision.tetris.bvc.business.dao.GroupMemberDAO;
import com.sumavision.tetris.bvc.business.dao.GroupMemberRolePermissionDAO;
import com.sumavision.tetris.bvc.business.group.GroupMemberPO;
import com.sumavision.tetris.bvc.business.group.GroupMemberRolePermissionPO;
import com.sumavision.tetris.bvc.business.group.GroupMemberStatus;
import com.sumavision.tetris.bvc.business.group.GroupMemberType;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.bvc.cascade.CommandCascadeService;
import com.sumavision.tetris.bvc.cascade.ConferenceCascadeService;
import com.sumavision.tetris.bvc.cascade.bo.GroupBO;
import com.sumavision.tetris.bvc.model.agenda.AgendaDestinationType;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardPO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardType;
import com.sumavision.tetris.bvc.model.agenda.AgendaPO;
import com.sumavision.tetris.bvc.model.agenda.AgendaService;
import com.sumavision.tetris.bvc.model.agenda.AgendaSourceType;
import com.sumavision.tetris.bvc.model.dao.AgendaDAO;
import com.sumavision.tetris.bvc.model.dao.AgendaForwardDAO;
import com.sumavision.tetris.bvc.model.dao.RoleDAO;
import com.sumavision.tetris.bvc.model.role.RolePO;
import com.sumavision.tetris.bvc.model.role.RoleUserMappingType;
import com.sumavision.tetris.bvc.util.TetrisBvcQueryUtil;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;
import com.sumavision.tetris.websocket.message.WebsocketMessageType;
import com.sumavision.tetris.websocket.message.WebsocketMessageVO;

import lombok.extern.slf4j.Slf4j;

/**
 * 会议转发业务<br/>
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年7月6日 下午2:27:51
 */
@Slf4j
@Transactional(rollbackFor = Exception.class)
@Service
public class GroupDemandService {
	
	/** synchronized锁的前缀 */
	private static final String lockPrefix = "command-group-";
	
	@Autowired
	private AgendaDAO agendaDao;
	
	@Autowired
	private RoleDAO roleDao;
	
	@Autowired
	private GroupDAO groupDao;
	
	@Autowired
	private GroupMemberDAO groupMemberDao;
	
	@Autowired
	private GroupDemandDAO groupDemandDao;
	
	@Autowired
	private AgendaForwardDAO agendaForwardDao;
	@Autowired
	private GroupMemberRolePermissionDAO groupMemberRolePermissionDao;
	
	@Autowired
	private CommandGroupDAO commandGroupDao;
	
	@Autowired
	private CommandGroupUserPlayerDAO commandGroupUserPlayerDao;
	
	@Autowired
	private CommandGroupForwardDemandDAO commandGroupForwardDemandDao;
	
	@Autowired
	private FolderUserMapDAO folderUserMapDao;
	
	@Autowired
	private CommandCommonServiceImpl commandCommonServiceImpl;
	
	@Autowired
	private CommandBasicServiceImpl commandBasicServiceImpl;
	
	@Autowired
	private CommandCastServiceImpl commandCastServiceImpl;
	
	@Autowired
	private TetrisBvcQueryUtil tetrisBvcQueryUtil;
	
	@Autowired
	private CommandCommonUtil commandCommonUtil;
	
	@Autowired
	private CommonQueryUtil commonQueryUtil;
	
	@Autowired
	private QueryUtil queryUtil;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	@Autowired
	private ResourceBundleDAO resourceBundleDao;
	
	@Autowired
	private ResourceChannelDAO resourceChannelDao;
	
	@Autowired
	private AgendaService agendaService;
	
	@Autowired
	private WebsocketMessageService websocketMessageService;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private CommandCascadeUtil commandCascadeUtil;
	
	@Autowired
	private ResourceRemoteService resourceRemoteService;
	
	@Autowired
	private CommandCascadeService commandCascadeService;
	
	@Autowired
	private ConferenceCascadeService conferenceCascadeService;

	/**
	 * 转发设备、用户，强制同意，不需要选择<br/>
	 * <p>1.0协议中另有一个“转发授权”的意义尚不明确</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月21日 上午8:45:57
	 * @param groupId
	 * @param srcUserIds 源用户
	 * @param bundleIds 源设备
	 * @param memberIds 目的成员
	 * @return 
	 * @throws Exception
	 */
	public List<ForwardReturnBO> forward(Long groupId, List<Long> srcUserIds, List<String> bundleIds, List<Long> memberIds) throws Exception{
		
		if(groupId == null || "".equals(groupId)){
			log.warn("媒体转发，会议id有误");
			return new ArrayList<ForwardReturnBO>();
		}
		
		synchronized (new StringBuffer().append(lockPrefix).append(groupId).toString().intern()) {
			
			if(srcUserIds == null) srcUserIds = new ArrayList<Long>();
			if(bundleIds == null) bundleIds = new ArrayList<String>();
			
			GroupPO group = groupDao.findOne(groupId);
			if(group == null){
				log.info("进行媒体转发的会议不存在，id：" + groupId);
				return new ArrayList<ForwardReturnBO>();
			}
			if(group.getStatus().equals(GroupStatus.STOP)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已停止，无法操作，id: " + group.getId());
			}
			
			List<Long> consumeIds = new ArrayList<Long>();
			List<MessageSendCacheBO> messageCaches = new ArrayList<MessageSendCacheBO>();
//			String localLayerId = null;//联网id
			
//			GroupType groupType = group.getType();
			List<GroupMemberPO> members = groupMemberDao.findByGroupId(groupId);
//			GroupMemberPO chairmanMember = tetrisBvcQueryUtil.queryChairmanMember(members);
//			Long creatorUserId = group.getUserId();
//			CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();
			
			//校验转发目的成员是否进会
			List<GroupMemberPO> dstMembers = new ArrayList<GroupMemberPO>();
			List<GroupMemberPO> _dstMembers = tetrisBvcQueryUtil.queryMembersByMemberIds(members, memberIds);
			for(GroupMemberPO dstMember : _dstMembers){
				if(!dstMember.getGroupMemberStatus().equals(GroupMemberStatus.CONNECT)){
//					if(!OriginType.OUTER.equals(group.getOriginType())){
						throw new BaseException(StatusCode.FORBIDDEN, dstMember.getName() + " 还未进入");
//					}
				}else{
					dstMembers.add(dstMember);
				}
			}
			
			/*
			List<CommandGroupForwardDemandPO> demands = group.getForwardDemands();
			List<CommandGroupForwardDemandPO> newDemands = new ArrayList<CommandGroupForwardDemandPO>();//新增的
			List<CommandGroupForwardDemandPO> needDemands = new ArrayList<CommandGroupForwardDemandPO>();//需要执行的
			List<CommandGroupUserPlayerPO> needPlayers = new ArrayList<CommandGroupUserPlayerPO>();
			if(demands == null){
				group.setForwardDemands(new ArrayList<CommandGroupForwardDemandPO>());
				demands = group.getForwardDemands();
			}*/
			
			//从bundleId列表查询所有的bundlePO
			List<BundlePO> srcBundleEntities = resourceBundleDao.findByBundleIds(bundleIds);
			if(srcBundleEntities == null) srcBundleEntities = new ArrayList<BundlePO>();
			
//			//从bundleId列表查询所有的视频编码1通道
//			List<ChannelSchemeDTO> videoEncode1Channels = resourceChannelDao.findByBundleIdsAndChannelId(bundleIds, ChannelType.VIDEOENCODE1.getChannelId());
//			if(videoEncode1Channels == null) videoEncode1Channels = new ArrayList<ChannelSchemeDTO>();
//			
//			//从bundleId列表查询所有的音频编码1通道
//			List<ChannelSchemeDTO> audioEncode1Channels = resourceChannelDao.findByBundleIdsAndChannelId(bundleIds, ChannelType.AUDIOENCODE1.getChannelId());
//			if(audioEncode1Channels == null) audioEncode1Channels = new ArrayList<ChannelSchemeDTO>();
			
			//查询被转发的用户
			String userIdListStr = StringUtils.join(srcUserIds.toArray(), ",");
			List<UserBO> srcUserBos = resourceService.queryUserListByIds(userIdListStr, TerminalType.QT_ZK);
			if(srcUserBos == null) srcUserBos = new ArrayList<UserBO>();
//			List<FolderUserMap> folderUserMaps = folderUserMapDao.findByUserIdIn(srcUserIds);
						
			//新的议程，需要执行
//			List<AgendaPO> agendas = new ArrayList<AgendaPO>();
			List<Long> agendaIds = new ArrayList<Long>();
			
			List<GroupDemandPO> newDemands = new ArrayList<GroupDemandPO>();
			List<GroupMemberRolePermissionPO> newPermissions = new ArrayList<GroupMemberRolePermissionPO>();
			
			//按目标成员循环，生成GroupDemandPO
			for(GroupMemberPO dstMember : dstMembers){
				
				OriginType dstOriginType = dstMember.getOriginType()==null?OriginType.INNER:dstMember.getOriginType();
				
				for(BundlePO srcBundle : srcBundleEntities){
					
					GroupDemandPO demand = new GroupDemandPO();//TODO
					demand.setDemandType(ForwardDemandBusinessType.FORWARD_DEVICE);
					demand.setDstName(dstMember.getName());
					demand.setDstMemberId(dstMember.getId());
					GroupMemberType memberType = dstMember.getGroupMemberType();
					if(GroupMemberType.MEMBER_USER.equals(memberType)){
						demand.setDstUserId(Long.parseLong(dstMember.getOriginId()));
					}else{
						demand.setDstUserId(-1L);
					}
					demand.setDstCode(dstMember.getCode());
					demand.setBusinessId(groupId.toString());
					demand.setSrcName(srcBundle.getBundleName());
					demand.setSrcCode(srcBundle.getBundleNum());
					groupDemandDao.save(demand);
										
					RolePO srcRolePO = new RolePO();
					srcRolePO.setName("媒体转发源");
					srcRolePO.setBusinessId(groupId);//???
					srcRolePO.setRoleUserMappingType(RoleUserMappingType.ONE_TO_ONE);
					roleDao.save(srcRolePO);
					
					RolePO dstRolePO = new RolePO();
					dstRolePO.setName("媒体转发目的");
					dstRolePO.setBusinessId(groupId);
					dstRolePO.setRoleUserMappingType(RoleUserMappingType.ONE_TO_ONE);
					roleDao.save(dstRolePO);
					
					GroupMemberPO srcMember = new GroupMemberPO();
					srcMember.setName(srcBundle.getBundleName());
					srcMember.setGroupMemberType(GroupMemberType.RESOURCE_ENCODER);
					srcMember.setCode(srcBundle.getUsername());
					srcMember.setOriginId(srcBundle.getBundleId());
//					srcMember.setTerminalId(terminalId);???
					srcMember.setGroupMemberStatus(GroupMemberStatus.CONNECT);
//					srcMember.setGroupId(groupId);???
					srcMember.setBusinessId(demand.getId().toString());
//					srcMembers.add(srcMember);
					groupMemberDao.save(srcMember);
					
					GroupMemberRolePermissionPO srcPer = new GroupMemberRolePermissionPO(srcRolePO.getId(), srcMember.getId());
					newPermissions.add(srcPer);
					GroupMemberRolePermissionPO dstPer = new GroupMemberRolePermissionPO(dstRolePO.getId(), dstMember.getId());
					newPermissions.add(dstPer);

					AgendaPO agenda = new AgendaPO();
					agenda.setName(group.getName() + " 转发 " + srcBundle.getBundleName());
					agenda.setBusinessId(groupId);
					agenda.setBusinessInfoType(BusinessInfoType.COMMAND_FORWARD_DEVICE);
					agendaDao.save(agenda);
					AgendaForwardPO agendaVideoForward = new AgendaForwardPO();
					agendaVideoForward.setBusinessInfoType(BusinessInfoType.COMMAND_FORWARD);
					agendaVideoForward.setType(AgendaForwardType.VIDEO);
					agendaVideoForward.setSourceId(srcRolePO.getId());
					agendaVideoForward.setSourceType(AgendaSourceType.ROLE);
					agendaVideoForward.setDestinationId(dstRolePO.getId());
					agendaVideoForward.setDestinationType(AgendaDestinationType.ROLE);
					agendaVideoForward.setAgendaId(agenda.getId());					
					AgendaForwardPO agendaAudioForward = new AgendaForwardPO();
					//TODO:
					
					demand.setAgendaId(agenda.getId());
					demand.setSrcMemberId(srcMember.getId());
					
//					agendas.add(agenda);
					agendaIds.add(agenda.getId());
					newDemands.add(demand);
				}
				
				if(GroupMemberType.MEMBER_USER.equals(dstMember.getGroupMemberType())){
					Map<String, Object> map = new HashMapWrapper<String, Object>()
							.put("businessType", "commandForward")
							.put("businessId", group.getId().toString())
							.put("businessInfo", group.getName() + " 进行了媒体转发")
							.put("splits", new JSONArray())
							.getMap();
					messageCaches.add(new MessageSendCacheBO(Long.parseLong(dstMember.getOriginId()), JSON.toJSONString(map), WebsocketMessageType.COMMAND));
				}
				
			}
			
			//持久化
			groupMemberRolePermissionDao.save(newPermissions);	
			groupDemandDao.save(newDemands);//持久化agendaId和srcMemberId
			
			//执行议程
			agendaService.runAndStopAgenda(groupId, agendaIds, null);
			
			/*//logic
			LogicBO logic = commandBasicServiceImpl.openBundle(null, needDemands, needPlayers, null, null, codec, chairmanMember.getUserNum());
			LogicBO logicCastDevice = commandCastServiceImpl.openBundleCastDevice(needDemands, null, null, null, null, null, codec, group.getUserId());
			logic.merge(logicCastDevice);
			executeBusiness.execute(logic, group.getName() + " 进行了媒体转发");*/
			
			//级联
			if(!OriginType.OUTER.equals(group.getOriginType())){
				/*if(GroupType.BASIC.equals(groupType)){
					GroupBO groupBO = commandCascadeUtil.startCommandDeviceForward(group, srcBundleEntities, srcUserBos, dstMembers);
					commandCascadeService.startDeviceForward(groupBO);
				}else if(GroupType.MEETING.equals(groupType)){
					GroupBO groupBO = commandCascadeUtil.startCommandDeviceForward(group, srcBundleEntities, srcUserBos, dstMembers);
					conferenceCascadeService.startDeviceForward(groupBO);
				}*/
			}
			
			//发消息
			for(MessageSendCacheBO cache : messageCaches){
				WebsocketMessageVO ws = websocketMessageService.send(cache.getUserId(), cache.getMessage(), cache.getType(), cache.getFromUserId(), cache.getFromUsername());
				consumeIds.add(ws.getId());
			}
			websocketMessageService.consumeAll(consumeIds);
			
			List<ForwardReturnBO> result = new ArrayList<ForwardReturnBO>();
			for(GroupDemandPO demand : newDemands){
				result.add(new ForwardReturnBO().setByGroupDemand(demand));
			}
			return result;
		}
	}

	/**
	 * 这是一个级联被调用的方法，不再向外发送协议。按标准协议，根据源号码和目的号码，停止转发调度<br/>
	 * <p>相同[源-目的]的多个任务，1次只能停1个，需要调用2次来停止</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月4日 下午2:20:12
	 * @param userId 操作人，暂时没用
	 * @param groupUuid
	 * @param srcCodes
	 * @param dstCodes
	 * @throws Exception
	 */
	public void stopBySrcAndDstCodes(Long userId1, String groupUuid, List<String> srcCodes, List<String> dstCodes) throws Exception{
		
		GroupPO group1 = groupDao.findByUuid(groupUuid);
		if(group1 == null){
			log.info("停止媒体转发的会议不存在，uuid：" + groupUuid);
			return;
		}
		
		synchronized (new StringBuffer().append(lockPrefix).append(group1.getId()).toString().intern()) {
			
			GroupPO group = groupDao.findByUuid(groupUuid);
			log.info(group.getName() + " 停止转发，源号码：" + srcCodes + "，目的号码" + dstCodes);
			
			List<GroupDemandPO> demands = groupDemandDao.findByBusinessId(group.getId().toString());
			List<GroupDemandPO> stopDdemands = new ArrayList<GroupDemandPO>();
			for(String srcCode : srcCodes){
				for(String dstCode : dstCodes){
					GroupDemandPO demand = tetrisBvcQueryUtil.queryForwardDemandBySrcAndDstCode(demands, srcCode, dstCode);
					if(demand != null){
						stopDdemands.add(demand);
					}
				}
			}
			
			//<userId, [{serial:屏幕序号}]>
			HashMap<Long, JSONArray> result = stopDemands(group, stopDdemands, false);
			
			//发消息
			List<Long> consumeIds = new ArrayList<Long>();
			for(Map.Entry<Long, JSONArray> entry: result.entrySet()){
				System.out.println("Key: "+ entry.getKey()+ " Value: "+entry.getValue());
				Long thisUserId = entry.getKey();
//				JSONArray splits = entry.getValue();
				JSONObject message = new JSONObject();
				message.put("businessType", "commandForwardStop");
				message.put("businessId", group.getId().toString());
				message.put("businessInfo", group.getName() + " 停止了转发");
				message.put("splits", new JSONArray());
				WebsocketMessageVO ws = websocketMessageService.send(thisUserId, message.toJSONString(), WebsocketMessageType.COMMAND, group.getUserId(), group.getUserName());
				consumeIds.add(ws.getId());
			}
			websocketMessageService.consumeAll(consumeIds);
		}
	}

	/**
	 * 主席停止一个会议中的多个转发<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月15日 下午2:05:55
	 * @param groupId
	 * @param demandIds
	 * @throws Exception
	 */
	public void stopByChairman(Long groupId, List<Long> demandIds) throws Exception{
		
		synchronized (new StringBuffer().append(lockPrefix).append(groupId).toString().intern()) {
		
			GroupPO group = groupDao.findOne(groupId);
			List<GroupDemandPO> stopDemands = groupDemandDao.findByIdIn(demandIds);
			
			//<userId, [{serial:屏幕序号}]>
			HashMap<Long, JSONArray> result = stopDemands(group, stopDemands, true);
					
			//发消息
			List<Long> consumeIds = new ArrayList<Long>();
			for(Map.Entry<Long, JSONArray> entry: result.entrySet()){
				System.out.println("Key: "+ entry.getKey()+ " Value: "+entry.getValue());
				Long userId = entry.getKey();
//				JSONArray splits = entry.getValue();
				JSONObject message = new JSONObject();
				message.put("businessType", "commandForwardStop");
				message.put("businessId", group.getId().toString());
				message.put("businessInfo", group.getName() + " 停止了转发");
				message.put("splits", new JSONArray());
				WebsocketMessageVO ws = websocketMessageService.send(userId, message.toJSONString(), WebsocketMessageType.COMMAND, group.getUserId(), group.getUserName());
				consumeIds.add(ws.getId());
			}
			websocketMessageService.consumeAll(consumeIds);
		}
	}

	/**
	 * 成员停止多个转发<br/>
	 * <p>支持不同会议中的转发</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月15日 下午2:07:38
	 * @param userId
	 * @param demandIds
	 * @return
	 * @throws Exception
	 */
	public JSONArray stopByMember(Long userId, List<Long> demandIds) throws Exception{
		
		JSONArray splits = new JSONArray();
		List<Long> groupIds = groupDao.findAllIdsByDemandIds(demandIds);
		
		for(Long groupId : groupIds){
			
			synchronized (new StringBuffer().append(lockPrefix).append(groupId).toString().intern()) {
				
				//这里把demandIds全部传入，因为有groupId限制，所以demandIds传多了也没事
				GroupPO group = groupDao.findOne(groupId);
				List<GroupDemandPO> demands = groupDemandDao.findByBusinessId(groupId.toString());
				List<GroupDemandPO> stopDemands = tetrisBvcQueryUtil.queryForwardDemandsByIds(demands, demandIds);
				HashMap<Long, JSONArray> result = stopDemands(group, stopDemands, true);
				JSONArray splist1 = result.get(userId);
				if(splist1 != null){
					splits.addAll(splist1);
				}
				
			}
		}
		
		return splits;
	}	
	
	/**
	 * 停止一个会议中的多个转发点播<br/>
	 * <p>可能涉及多个成员用户，所以返回map</p>
	 * <p>包含级联</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月15日 上午10:08:43
	 * @param group
	 * @param stopDemands
	 * @param cascade 是否给其它级联节点发送协议，主动操作的应使用true；被动响应的，例如被级联调用的应使用false
	 * @return HashMap<Long, JSONArray> 格式为：<userId, [{serial:屏幕序号}]>
	 * @throws Exception
	 */
	public HashMap<Long, JSONArray> stopDemands(GroupPO group, List<GroupDemandPO> stopDemands, boolean cascade) throws Exception{
		
		if(group.getStatus().equals(GroupStatus.STOP)){
			throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已停止，无法操作，id: " + group.getId());
		}
		
		//<userId, [{serial:'屏幕序号'}]>
		HashMap<Long, JSONArray> result = new HashMap<Long, JSONArray>();
		List<GroupMemberPO> members = groupMemberDao.findByGroupId(group.getId());
		
		List<GroupBO> groupBOs = new ArrayList<GroupBO>();
		
		for(GroupDemandPO demand : stopDemands){
			
			//统计被停止转发的用户
			GroupMemberPO dstMember = tetrisBvcQueryUtil.queryMemberById(members, demand.getDstMemberId());
			if(dstMember.getGroupMemberType().equals(GroupMemberType.MEMBER_USER)){
				result.put(Long.parseLong(dstMember.getOriginId()), null);
			}
			
			//停止议程，后续修改成批量
			Long agendaId = demand.getAgendaId();
			agendaService.runAndStopAgenda(group.getId(), null, new ArrayListWrapper<Long>().add(agendaId).getList());
			
			AgendaPO agenda = agendaDao.findOne(agendaId);
			List<AgendaForwardPO> forwards = agendaForwardDao.findByAgendaId(agendaId);
			List<Long> roleIds = new ArrayListWrapper<Long>().add(forwards.get(0).getSourceId()).add(forwards.get(0).getDestinationId()).getList();
			List<RolePO> roles = roleDao.findAll(roleIds);
			GroupMemberPO srcMember = groupMemberDao.findOne(demand.getSrcMemberId());
			List<GroupMemberRolePermissionPO> ps = groupMemberRolePermissionDao.findByRoleIdIn(roleIds);
			
			agendaDao.delete(agenda);
			agendaForwardDao.deleteInBatch(forwards);
			roleDao.deleteInBatch(roles);
			groupMemberDao.delete(srcMember);
			groupMemberRolePermissionDao.deleteInBatch(ps);			
		}
		
		groupDemandDao.deleteInBatch(stopDemands);
		
		/*
		//发协议
		CommandGroupAvtplGearsPO currentGear = commandCommonUtil.queryCurrentGear(group);
		CodecParamBO codec = new CodecParamBO().set(group.getAvtpl(), currentGear);
		LogicBO logic = commandBasicServiceImpl.closeBundle(null, needStopDemands, allNeedClosePlayers, codec, chairmanMember.getUserNum());
		LogicBO logicCastDevice = commandCastServiceImpl.closeBundleCastDevice(playFilePlayers, null, null, allNeedClosePlayers, codec, group.getUserId());
		logic.merge(logicCastDevice);
		executeBusiness.execute(logic, group.getName() + " 会议停止多个转发，个数：" + needStopDemands.size());*/
		
		//级联，循环groupBOs发多个协议
		if(cascade){
			/*for(GroupBO groupBO : groupBOs){
				if(GroupType.BASIC.equals(groupType)){
					commandCascadeService.stopDeviceForward(groupBO);
				}else if(GroupType.MEETING.equals(groupType)){
					conferenceCascadeService.stopDeviceForward(groupBO);
				}
			}*/
		}
		
		return result;
	}
}

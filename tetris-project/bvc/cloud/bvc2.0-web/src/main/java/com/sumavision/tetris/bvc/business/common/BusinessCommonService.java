package com.sumavision.tetris.bvc.business.common;

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
import com.netflix.infix.lang.infix.antlr.EventFilterParser.boolean_expr_return;
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
import com.sumavision.bvc.command.group.enumeration.ExecuteStatus;
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
import com.sumavision.tetris.bvc.business.OriginType;
import com.sumavision.tetris.bvc.business.bo.MemberTerminalBO;
import com.sumavision.tetris.bvc.business.bo.UserTerminalBO;
import com.sumavision.tetris.bvc.business.dao.GroupDAO;
import com.sumavision.tetris.bvc.business.dao.GroupMemberDAO;
import com.sumavision.tetris.bvc.business.forward.CommonForwardPO;
import com.sumavision.tetris.bvc.business.group.GroupMemberPO;
import com.sumavision.tetris.bvc.business.group.GroupMemberRolePermissionPO;
import com.sumavision.tetris.bvc.business.group.GroupMemberStatus;
import com.sumavision.tetris.bvc.business.group.GroupMemberType;
import com.sumavision.tetris.bvc.cascade.CommandCascadeService;
import com.sumavision.tetris.bvc.cascade.ConferenceCascadeService;
import com.sumavision.tetris.bvc.cascade.bo.GroupBO;
import com.sumavision.tetris.bvc.cascade.bo.MinfoBO;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundleChannelPO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelPO;
import com.sumavision.tetris.bvc.page.PageTaskPO;
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
public class BusinessCommonService {
	
	@Autowired
	private GroupDAO groupDao;
	
	@Autowired
	private GroupMemberDAO groupMemberDao;
	
	@Autowired
	private CommandGroupDAO commandGroupDao;
	
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
	
	public List<Long> fromUserIdsToMemberIds(Long groupId, List<Long> userIds){
		List<String> originIds = new ArrayList<String>();
		for(Long userId : userIds){
			originIds.add(userId.toString());
		}
		List<Long> memberIds = groupMemberDao.findIdsByGroupIdAndOriginIds(groupId, originIds);
		return memberIds;
	}
	
	public Set<Long> obtainTerminalBundleIdsFromTerminalChannel(List<TerminalChannelPO> channels){
		Set<Long> ids = new HashSet<Long>();
		for(TerminalChannelPO channel : channels){
			ids.add(channel.getTerminalBundleId());
		}
		return ids;
	}
	
	public Set<String> obtainChannelIdsFromTerminalChannel(List<TerminalChannelPO> channels){
		Set<String> ids = new HashSet<String>();
		for(TerminalChannelPO channel : channels){
			ids.add(channel.getRealChannelId());
		}
		return ids;
	}
	
	public Set<Long> obtainTerminalBundleIdsFromTerminalBundleChannel(List<TerminalBundleChannelPO> channels){
		Set<Long> ids = new HashSet<Long>();
		for(TerminalBundleChannelPO channel : channels){
			ids.add(channel.getTerminalBundleId());
		}
		return ids;
	}
	
	public Set<String> obtainChannelIdsFromTerminalBundleChannel(List<TerminalBundleChannelPO> channels){
		Set<String> ids = new HashSet<String>();
		for(TerminalBundleChannelPO channel : channels){
			ids.add(channel.getChannelId());
		}
		return ids;
	}
	
	public List<String> obtainMemberNames(List<GroupMemberPO> members){
		List<String> memberNames = new ArrayList<String>();
		for(GroupMemberPO member : members){
			memberNames.add(member.getName());
		}
		return memberNames;
	}
	
	public List<Long> obtainMemberIds(Long groupId, boolean connected, boolean includingChairman){
		List<Long> memberIds = new ArrayList<Long>();
		List<GroupMemberPO> members = groupMemberDao.findByGroupId(groupId);
		for(GroupMemberPO member : members){
			boolean fit = true;
			if(connected){
				if(!GroupMemberStatus.CONNECT.equals(member.getGroupMemberStatus())){
					fit = false;
				}
			}
			if(!includingChairman){
				if(member.getIsAdministrator()){
					fit = false;
				}
			}
			if(fit){
				memberIds.add(member.getId());
			}
		}
		return memberIds;
	}
	
	public List<Long> obtainGroupMemberRolePermissionPOIds(List<GroupMemberRolePermissionPO> ps){
		List<Long> ids = new ArrayList<Long>();
		for(GroupMemberRolePermissionPO p : ps){
			ids.add(p.getId());
		}
		return ids;
	}

	public List<Long> obtainMemberIds(List<GroupMemberPO> members){
		List<Long> memberIds = new ArrayList<Long>();
		for(GroupMemberPO member : members){
			memberIds.add(member.getId());
		}
		return memberIds;
	}

	public void notifyUserInfo(
			List<Long> userIds,
			String message,
			WebsocketMessageType type) throws Exception{
		List<Long> consumeIds = new ArrayList<Long>();
		for(Long userId : userIds){
			//这里缺少判断该userID是否是本系统的
			WebsocketMessageVO ws = websocketMessageService.send(userId, message, type);
			consumeIds.add(ws.getId());
		}
		websocketMessageService.consumeAll(consumeIds);
	}
	
	public void notifyMemberInfo(
			List<Long> memberIds,
			String message,
			WebsocketMessageType type) throws Exception{
		
		List<GroupMemberPO> members = groupMemberDao.findAll(memberIds);
		List<Long> userIds = new ArrayList<Long>();
		for(GroupMemberPO member : members){
			if(GroupMemberType.MEMBER_USER.equals(member.getGroupMemberType())
					&& !OriginType.OUTER.equals(member.getOriginType())){
				userIds.add(Long.parseLong(member.getOriginId()));
			}
		}
		
		List<Long> consumeIds = new ArrayList<Long>();		
		for(Long userId : userIds){
			WebsocketMessageVO ws = websocketMessageService.send(userId, message, type);
			consumeIds.add(ws.getId());
		}
		websocketMessageService.consumeAll(consumeIds);
	}
}

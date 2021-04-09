package com.sumavision.tetris.bvc.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.FolderUserMapDAO;
import com.suma.venus.resource.service.ResourceRemoteService;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.device.command.cast.CommandCastServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.command.user.CommandUserServiceImpl;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.resource.dao.ResourceBundleDAO;
import com.sumavision.bvc.resource.dao.ResourceChannelDAO;
import com.sumavision.tetris.bvc.business.common.BusinessCommonService;
import com.sumavision.tetris.bvc.business.common.BusinessReturnService;
import com.sumavision.tetris.bvc.business.common.MulticastService;
import com.sumavision.tetris.bvc.business.dao.BusinessCombineAudioDAO;
import com.sumavision.tetris.bvc.business.dao.BusinessCombineVideoDAO;
import com.sumavision.tetris.bvc.business.dao.BusinessGroupMemberDAO;
import com.sumavision.tetris.bvc.business.dao.CombineAudioPermissionDAO;
import com.sumavision.tetris.bvc.business.dao.CombineTemplateGroupAgendeForwardPermissionDAO;
import com.sumavision.tetris.bvc.business.dao.CommonForwardDAO;
import com.sumavision.tetris.bvc.business.dao.GroupDAO;
import com.sumavision.tetris.bvc.business.dao.GroupMemberRolePermissionDAO;
import com.sumavision.tetris.bvc.business.dao.RunningAgendaDAO;
import com.sumavision.tetris.bvc.business.group.GroupMemberStatus;
import com.sumavision.tetris.bvc.business.group.combine.video.CombineVideoService;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberTerminalBundleChannelPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberTerminalChannelPO;
import com.sumavision.tetris.bvc.business.po.member.dao.BusinessGroupMemberTerminalChannelDAO;
import com.sumavision.tetris.bvc.business.terminal.hall.TerminalBundleConferenceHallPermissionDAO;
import com.sumavision.tetris.bvc.business.terminal.user.TerminalBundleUserPermissionDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardDestinationDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardDestinationPO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardPO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardSourceDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardSourcePO;
import com.sumavision.tetris.bvc.model.agenda.DestinationType;
import com.sumavision.tetris.bvc.model.agenda.LayoutScopeDAO;
import com.sumavision.tetris.bvc.model.agenda.LayoutScopePO;
import com.sumavision.tetris.bvc.model.agenda.LayoutSelectionType;
import com.sumavision.tetris.bvc.model.agenda.SourceType;
import com.sumavision.tetris.bvc.model.layout.LayoutDAO;
import com.sumavision.tetris.bvc.model.layout.LayoutPO;
import com.sumavision.tetris.bvc.model.layout.LayoutPositionDAO;
import com.sumavision.tetris.bvc.model.layout.forward.CombineTemplatePositionDAO;
import com.sumavision.tetris.bvc.model.layout.forward.LayoutForwardDAO;
import com.sumavision.tetris.bvc.model.layout.forward.LayoutForwardPO;
import com.sumavision.tetris.bvc.model.role.RoleChannelDAO;
import com.sumavision.tetris.bvc.model.role.RoleChannelPO;
import com.sumavision.tetris.bvc.model.role.RoleChannelTerminalChannelPermissionDAO;
import com.sumavision.tetris.bvc.model.role.RoleChannelTerminalChannelPermissionPO;
import com.sumavision.tetris.bvc.model.role.RoleDAO;
import com.sumavision.tetris.bvc.model.role.RolePO;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundleDAO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelDAO;
import com.sumavision.tetris.bvc.page.PageInfoDAO;
import com.sumavision.tetris.bvc.page.PageTaskDAO;
import com.sumavision.tetris.bvc.page.PageTaskService;
import com.sumavision.tetris.bvc.util.MultiRateUtil;
import com.sumavision.tetris.bvc.util.TetrisBvcQueryUtil;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AgendaRoleMemberUtil {
	
	@Autowired
	private com.sumavision.bvc.device.group.dao.CombineVideoDAO deviceGroupCombineVideoDao;
	
	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private ResourceBundleDAO resourceBundleDao;
	
	@Autowired
	private ResourceChannelDAO resourceChannelDao;
	
	@Autowired
	private GroupDAO groupDao;
	
	@Autowired
	private RoleChannelDAO roleChannelDao;
	
//	@Autowired
//	private TerminalBundleChannelDAO terminalBundleChannelDao;
	
	@Autowired
	private TerminalChannelDAO terminalChannelDao;
	
	@Autowired
	private PageInfoDAO pageInfoDao;
	
	@Autowired
	private PageTaskDAO pageTaskDao;
	
	@Autowired
	private CommonForwardDAO commonForwardDao;
	
	@Autowired
	private TerminalBundleDAO terminalBundleDao;
	
	@Autowired
	private AgendaForwardDAO agendaForwardDao;
	
	@Autowired
	private GroupMemberRolePermissionDAO groupMemberRolePermissionDao;
	
	@Autowired
	private RoleDAO roleDao;
	
	@Autowired
	private TerminalBundleUserPermissionDAO terminalBundleUserPermissionDao;
	
	@Autowired
	private TerminalBundleConferenceHallPermissionDAO terminalBundleConferenceHallPermissionDao;
	
	@Autowired
	private RunningAgendaDAO runningAgendaDao;
	
	@Autowired
	private FolderUserMapDAO folderUserMapDao;
	
//	@Autowired
//	private AutoCombineService autoCombineService;
	
	@Autowired
	private PageTaskService pageTaskService;
	
	@Autowired
	private MulticastService multicastService;
	
	@Autowired
	private BusinessCommonService businessCommonService;
	
	@Autowired
	private CommandCommonServiceImpl commandCommonServiceImpl;
	
	@Autowired
	private WebsocketMessageService websocketMessageService;
	
	@Autowired
	private CommandUserServiceImpl commandUserServiceImpl;
	
	@Autowired
	private CommandCastServiceImpl commandCastServiceImpl;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private ResourceRemoteService resourceRemoteService;

//	@Autowired
//	private CombineVideoUtil combineVideoUtil;

	@Autowired
	private CommandCommonUtil commandCommonUtil;

	@Autowired
	private QueryUtil queryUtil;

	@Autowired
	private TetrisBvcQueryUtil tetrisBvcQueryUtil;

	@Autowired
	private MultiRateUtil multiRateUtil;

	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	@Autowired
	private AgendaDAO agendaDao;
	
	@Autowired
	private LayoutDAO layoutDao;
	
	@Autowired
	private LayoutPositionDAO layoutPositionDao;
	
	@Autowired
	private LayoutForwardDAO layoutForwardDao;
	
	@Autowired
	private BusinessGroupMemberDAO businessGroupMemberDao;
	
	@Autowired
	private AgendaForwardSourceDAO agendaForwardSourceDao;
	
	@Autowired
	private AgendaForwardDestinationDAO agendaForwardDestinationDao;
	
	@Autowired
	private CombineTemplatePositionDAO combineTemplatePositionDao;
	
	@Autowired
	private BusinessCombineVideoDAO businessCombineVideoDao;
	
	@Autowired
	private BusinessCombineAudioDAO businessCombineAudioDao;
	
	@Autowired
	private LayoutScopeDAO layoutScopeDao;
	
	@Autowired
	private BusinessGroupMemberTerminalChannelDAO businessGroupMemberTerminalChannelDao;	
	
	@Autowired
	private RoleChannelTerminalChannelPermissionDAO roleChannelTerminalChannelPermissionDao;
	
	@Autowired
	private CombineAudioPermissionDAO combineAudioPermissionDao;
	
	@Autowired
	private CombineTemplateGroupAgendeForwardPermissionDAO combineTemplateGroupAgendeForwardPermissionDao;
	
	@Autowired
	private BusinessReturnService businessReturnService;
	
	@Autowired
	private CombineVideoService combineVideoService;
	
	/** 级联使用：联网接入层id */
	private String localLayerId = null;
	
//	private Map<String, TerminalBundlePO> terminalBundleMap = new HashMap<String, TerminalBundlePO>();
	
	/**
	 * 建立一个AgendaForwardPO与目的成员的map（仅在线成员）。在目的角色中，排除掉了在别的转发中被设置的目的成员<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月17日 上午11:34:42
	 * @param forwards
	 * @param members 所有涉及到的成员，可由group.getMembers()得到
	 * @return （仅在线成员的）
	 */
	public Map<AgendaForwardPO, Set<BusinessGroupMemberPO>> obtainAgendaForwardDstMemberMap(List<AgendaForwardPO> forwards, Collection<BusinessGroupMemberPO> members){
		return obtainAgendaForwardDstMemberMap(forwards, members, 0);
	}
	
	/**
	 * 建立一个AgendaForwardPO与目的成员的map（仅在线成员）<br/>
	 * <p>按mode参数返回不同配置的目的</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月28日 下午3:49:14
	 * @param forwards
	 * @param members 所有涉及到的成员，可由group.getMembers()得到
	 * @param mode 0获取最终的目的；1获取按照成员配置的目的；2获取按照角色配置的目的，这里去除了按照成员配置的
	 * @return （仅在线成员的）
	 */
	public Map<AgendaForwardPO, Set<BusinessGroupMemberPO>> obtainAgendaForwardDstMemberMap(List<AgendaForwardPO> forwards, Collection<BusinessGroupMemberPO> members, int mode){
		List<BusinessGroupMemberPO> spcificMembers = new ArrayList<BusinessGroupMemberPO>();
		Map<AgendaForwardPO, Set<BusinessGroupMemberPO>> agendaForwardMembersMap = new HashMap<AgendaForwardPO, Set<BusinessGroupMemberPO>>();
		Map<AgendaForwardPO, Set<BusinessGroupMemberPO>> agendaForwardRoleMembersMap = new HashMap<AgendaForwardPO, Set<BusinessGroupMemberPO>>();
		for(AgendaForwardPO forward : forwards){
			
			List<AgendaForwardDestinationPO> agendaForwardDestinations = agendaForwardDestinationDao.findByAgendaForwardId(forward.getId());
			Set<BusinessGroupMemberPO> dstMembers = new HashSet<BusinessGroupMemberPO>();
			Set<BusinessGroupMemberPO> dstRoleMembers = new HashSet<BusinessGroupMemberPO>();
			
			for(AgendaForwardDestinationPO dst : agendaForwardDestinations){
				DestinationType destinationType = dst.getDestinationType();
				if(DestinationType.GROUP_MEMBER.equals(destinationType)){
					BusinessGroupMemberPO dstMember = tetrisBvcQueryUtil.queryGroupMemberById(members, dst.getDestinationId());
					if(dstMember != null){
						dstMembers.add(dstMember);
						spcificMembers.add(dstMember);
					}
				}else if(DestinationType.ROLE.equals(destinationType)){
					List<BusinessGroupMemberPO> dstMs = tetrisBvcQueryUtil.queryGroupMembersByRoleId(members, dst.getDestinationId());
					dstRoleMembers.addAll(dstMs);
				}
			}

			agendaForwardMembersMap.put(forward, dstMembers);
			agendaForwardRoleMembersMap.put(forward, dstRoleMembers);
		}
		
		//获取按照成员配置的目的，返回
		if(mode == 1){
			filterConnectMembers(agendaForwardMembersMap);
			return agendaForwardMembersMap;
		}
		
		//对每个forward的dstRoleMembers，如果spcificMembers里边有相同的，即成员是别的目的，那么从dstRoleMembers中删除
		for(AgendaForwardPO forward : agendaForwardRoleMembersMap.keySet()){
			Set<BusinessGroupMemberPO> dstRoleMembers = agendaForwardRoleMembersMap.get(forward);
			dstRoleMembers.removeAll(spcificMembers);
		}
		
		//获取按照角色配置的目的，返回（这里已经去掉了按照成员配置的目的）
		if(mode == 2){
			filterConnectMembers(agendaForwardMembersMap);
			return agendaForwardRoleMembersMap;
		}
		
		//最后把两个map合并，agendaForwardMembersMap为最终的map
		for(AgendaForwardPO forward : agendaForwardMembersMap.keySet()){
			Set<BusinessGroupMemberPO> dstMembers = agendaForwardMembersMap.get(forward);
			Set<BusinessGroupMemberPO> dstRoleMembers = agendaForwardRoleMembersMap.get(forward);
			dstMembers.addAll(dstRoleMembers);
		}

		filterConnectMembers(agendaForwardMembersMap);
		return agendaForwardMembersMap;
	}
	
	/**
	 * 建立与业务组直接关联的AgendaForwardPO与目的成员的map（仅在线成员）<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月1日 下午3:58:20
	 * @param groupId 业务组id
	 */
	public Map<AgendaForwardPO, Set<BusinessGroupMemberPO>> extraAgendaForawrdMembersMap(Long groupId, Collection<BusinessGroupMemberPO> members, List<AgendaForwardPO> forwards){
		
		//与业务组有直接关系的议程转发（例如：媒体转发业务）
		List<AgendaForwardPO> businessForwards = agendaForwardDao.findByBusinessId(groupId);
		List<Long> forwardIds = businessForwards.stream().map(AgendaForwardPO::getId).collect(Collectors.toList());
		forwards.addAll(businessForwards);
		
//		//与业务组有直接关系的成员
//		List<BusinessGroupMemberPO> groupMembers = businessGroupMemberDao.findByBusinessId(groupId.toString());
		
		//议程转发对应的目的成员
		List<AgendaForwardDestinationPO> agendaForwardDestinations = agendaForwardDestinationDao.findByAgendaForwardIdIn(forwardIds);
		Map<Long, List<AgendaForwardDestinationPO>> agendaForwardIdAndDestinationMap = agendaForwardDestinations.stream().collect(Collectors.groupingBy(AgendaForwardDestinationPO::getAgendaForwardId));
		
		Map<AgendaForwardPO, Set<BusinessGroupMemberPO>> agendaForwardMembersMap = new HashMap<AgendaForwardPO, Set<BusinessGroupMemberPO>>();
		
		for(AgendaForwardPO forward : businessForwards){
			
			List<AgendaForwardDestinationPO> destinations = agendaForwardIdAndDestinationMap.get(forward.getId());
			Set<BusinessGroupMemberPO> dstMembers = new HashSet<BusinessGroupMemberPO>();
			
			for(AgendaForwardDestinationPO dst : destinations){
				BusinessGroupMemberPO dstMember = tetrisBvcQueryUtil.queryGroupMemberById(members, dst.getDestinationId());
				if(dstMember != null){
					dstMembers.add(dstMember);
				}
			}

			agendaForwardMembersMap.put(forward, dstMembers);
		}
		
		return agendaForwardMembersMap;
	}
	
	/** 过滤掉不在线的成员 */
	private void filterConnectMembers(Map<AgendaForwardPO, Set<BusinessGroupMemberPO>> map){
		for(AgendaForwardPO agendaForward : map.keySet()){
			Set<BusinessGroupMemberPO> members = map.get(agendaForward);
//			members.stream().filter(
//					member -> GroupMemberStatus.CONNECT.equals(member.getGroupMemberStatus()));
			Set<BusinessGroupMemberPO> connectMembers = new HashSet<BusinessGroupMemberPO>();
			for(BusinessGroupMemberPO member : members){
				if(GroupMemberStatus.CONNECT.equals(member.getGroupMemberStatus())){
					connectMembers.add(member);
				}
			}
			map.put(agendaForward, connectMembers);
		}
//		for(Set<BusinessGroupMemberPO> members : map.values()){
//			members.stream().filter(
//					member -> GroupMemberStatus.CONNECT.equals(member.getGroupMemberStatus()));
//		}
	}
	
	/**
	 * 建立一个AgendaForwardPO与源成员的map。<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月9日 下午4:37:36
	 * @param forwards 议程转发集合
	 * @param members 所有涉及到的成员，可由group.getMembers()得到
	 * @return
	 */
	@Deprecated
	public Map<AgendaForwardPO, Set<BusinessGroupMemberPO>> obtainAgendaForwardSrcMemberMap(List<AgendaForwardPO> forwards, Collection<BusinessGroupMemberPO> members){
		
		//将成员按照角色id或者会场id进行分组
		//再根据议程中有哪些角色通道查找对应角色，有哪些成员通道查找成员
		Map<Long, List<BusinessGroupMemberPO>> roleIdAndMember = new HashMap<Long, List<BusinessGroupMemberPO>>();
		Map<Long, BusinessGroupMemberPO> groupMemberIdAndMember = new HashMap<Long, BusinessGroupMemberPO>();
		
		for(BusinessGroupMemberPO member : members){
//			if(GroupMemberType.MEMBER_USER.equals(member.getGroupMemberType())){
//				if(roleIdAndMember.get(member.getRoleId()) == null){
//					roleIdAndMember.put(member.getRoleId(), new ArrayList<BusinessGroupMemberPO>());
//				}
//				roleIdAndMember.get(member.getRoleId()).add(member);
//			}else if(GroupMemberType.MEMBER_HALL.equals(member.getGroupMemberType())){
			if(roleIdAndMember.get(member.getRoleId()) == null){
				roleIdAndMember.put(member.getRoleId(), new ArrayList<BusinessGroupMemberPO>());
			}
			roleIdAndMember.get(member.getRoleId()).add(member);
//			if(groupMemberIdAndMember.get(member.getId()) == null){
//				groupMemberIdAndMember.put(Long.valueOf(member.getOriginId()), new ArrayList<BusinessGroupMemberPO>());
//			}
			groupMemberIdAndMember.put(member.getId(), member);
//			}
		}
		
		Map<AgendaForwardPO, Set<BusinessGroupMemberPO>> agendaForwardSrcMembersMap = new HashMap<AgendaForwardPO, Set<BusinessGroupMemberPO>>();
	
		for(AgendaForwardPO forward : forwards){
			agendaForwardSrcMembersMap.put(forward, new HashSet<BusinessGroupMemberPO>());
			List<AgendaForwardSourcePO>  sources = agendaForwardSourceDao.findByAgendaForwardId(forward.getId());
			for(AgendaForwardSourcePO source : sources){
				if(SourceType.GROUP_MEMBER_CHANNEL.equals(source.getSourceType())){
					BusinessGroupMemberTerminalChannelPO businessGroupMemberTerminalChannel = businessGroupMemberTerminalChannelDao.findOne(source.getSourceId());
					if(businessGroupMemberTerminalChannel != null){
						agendaForwardSrcMembersMap.get(forward).add(groupMemberIdAndMember.get(businessGroupMemberTerminalChannel.getMember().getId()));
					}
				}else if(SourceType.ROLE_CHANNEL.equals(source.getSourceType())){
					RoleChannelPO roleChannel = roleChannelDao.findOne(source.getSourceId());
					if(roleChannel != null && roleChannel.getRoleId() != null && roleIdAndMember.get(roleChannel.getRoleId()) != null){
						agendaForwardSrcMembersMap.get(forward).addAll(roleIdAndMember.get(roleChannel.getRoleId()));
					}
				}
			}
		}
		
		return agendaForwardSrcMembersMap;
	}
	
	/**
	 * 获取虚拟源转发的目标成员（仅入会的）（并未使用）<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月15日 下午2:11:47
	 * @param layoutForward
	 * @param agendaForward
	 * @param members
	 * @return
	 */
	@Deprecated
	public List<BusinessGroupMemberPO> obtainLayoutForwardDstMembers(
			LayoutForwardPO layoutForward,
			AgendaForwardPO agendaForward,
			Collection<BusinessGroupMemberPO> members){
		Map<AgendaForwardPO, Set<BusinessGroupMemberPO>> agendaForwardMembersMap = obtainAgendaForwardDstMemberMap(
				new ArrayListWrapper<AgendaForwardPO>().add(agendaForward).getList(),
				members);
		Set<BusinessGroupMemberPO> ms = agendaForwardMembersMap.get(agendaForward);
		List<BusinessGroupMemberPO> connectMembers = tetrisBvcQueryUtil.queryGroupMembersByGroupMemberStatus(ms, GroupMemberStatus.CONNECT);
		List<BusinessGroupMemberPO> layoutForwardAllDstMembers = tetrisBvcQueryUtil.queryGroupMembersByTerminalId(connectMembers, layoutForward.getTerminalId());
		return layoutForwardAllDstMembers;
	}

	@Deprecated
	public Map<Long, Set<BusinessGroupMemberPO>> obtainTerminalIdMembersMap(Collection<BusinessGroupMemberPO> members){
		Map<Long, Set<BusinessGroupMemberPO>> resultMap = new HashMap<Long, Set<BusinessGroupMemberPO>>();
		for(BusinessGroupMemberPO member : members){
			Long terminalId = member.getTerminalId();
			Set<BusinessGroupMemberPO> s = resultMap.get(terminalId);
			if(s == null){
				s = new HashSet<BusinessGroupMemberPO>();
				resultMap.put(terminalId, s);
			}
			s.add(member);
		}
		
		return resultMap;
	}
	
	/**
	 * 将虚拟源转发划分为 [terminalId, 虚拟源转发列表] <br/>
	 * <p>通常在每个虚拟源中使用</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月17日 下午4:07:08
	 * @param layoutForwards
	 * @return
	 */
	public Map<Long, List<LayoutForwardPO>> obtainTerminalIdLayoutForwardMap(Collection<LayoutForwardPO> layoutForwards){
		
		Map<Long, List<LayoutForwardPO>> terminalIdLayoutForwardMap = new HashMap<Long, List<LayoutForwardPO>>();
		
		for(LayoutForwardPO layoutForward : layoutForwards){
			if(terminalIdLayoutForwardMap.get(layoutForward.getTerminalId()) == null){
				terminalIdLayoutForwardMap.put(layoutForward.getTerminalId(), new ArrayList<LayoutForwardPO>());
			}
			terminalIdLayoutForwardMap.get(layoutForward.getTerminalId()).add(layoutForward);
		}
		
		return terminalIdLayoutForwardMap;
	}
	
	/**
	 * 从成员列表中获取终端通道（成员不在线则返回null）<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月25日 下午3:57:07
	 * @param sourceId 成员的终端通道id/角色通道id
	 * @param sourceType 源类型：成员通道/角色通道
	 * @param members 成员列表
	 * @return BusinessGroupMemberTerminalChannelPO 成员不在线则返回null
	 */
	public BusinessGroupMemberTerminalChannelPO obtainMemberTerminalChannelFromAgendaForwardSource(
			Long sourceId, SourceType sourceType, Collection<BusinessGroupMemberPO> members){
		
		if(SourceType.GROUP_MEMBER_CHANNEL.equals(sourceType)){
			
			BusinessGroupMemberTerminalChannelPO businessGroupMemberTerminalChannel = businessGroupMemberTerminalChannelDao.findOne(sourceId);
			//如果成员未连接，则返回null
			if(businessGroupMemberTerminalChannel!=null && !businessGroupMemberTerminalChannel.getMember().getGroupMemberStatus().equals(GroupMemberStatus.CONNECT)){
				log.info(businessGroupMemberTerminalChannel.getMember().getName() + " 没有入会，不作为源");
				return null;
			}
			return businessGroupMemberTerminalChannel;
			
		}else if(SourceType.ROLE_CHANNEL.equals(sourceType)){
			//应该拿到的是角色通道
			RoleChannelPO roleChannel = roleChannelDao.findOne(sourceId);
			List<BusinessGroupMemberPO> sourceMembers = tetrisBvcQueryUtil.queryGroupMembersByRoleId(members, roleChannel.getRoleId());
//					businessGroupMemberDao.findByGroupIdAndRoleId(groupId, role.getId());
			BusinessGroupMemberPO srcMember = null;
			if(sourceMembers.size() == 0){
				RolePO role = roleDao.findOne(roleChannel.getRoleId());
				log.info(role.getName() + "角色没有找到成员作为源");
			}else{
				if(sourceMembers.size() > 1){
					RolePO role = roleDao.findOne(roleChannel.getRoleId());
					log.warn(role.getName() + "角色找到了" + sourceMembers.size() + "个成员，设置有误，取第1个作为源");												
				}
				srcMember = sourceMembers.get(0);
			}
			if(srcMember != null){
				
				if(!srcMember.getGroupMemberStatus().equals(GroupMemberStatus.CONNECT)){
					RolePO role = roleDao.findOne(roleChannel.getRoleId());
					log.info(role.getName() + " 角色的成员：" + srcMember.getName() + " 没有入会，不作为源");
					return null;
				}
				
				//通过角色通道找到终端通道srcChannel
				List<BusinessGroupMemberTerminalChannelPO> channels = srcMember.getChannels();
				RoleChannelTerminalChannelPermissionPO per = roleChannelTerminalChannelPermissionDao.findByTerminalIdAndRoleChannelId(srcMember.getTerminalId(), sourceId);
				if(per != null){
					BusinessGroupMemberTerminalChannelPO srcChannel = tetrisBvcQueryUtil.queryBusinessGroupMemberTerminalChannelByTerminalChannelId(channels, per.getTerminalChannelId());
					
					return srcChannel;
				}
				
			}
		}
		return null;
	}
	
	/**
	 * 从成员列表中获取终端通道列表List（仅成员在线的）<br/>
	 * <p>跟上一个方法基本相同，主要用于观看1:n角色时使用，例如看观众、看发言人</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月25日 下午3:57:07
	 * @param sourceId 成员的终端通道id/角色通道id
	 * @param sourceType 源类型：成员通道/角色通道
	 * @param members 成员列表
	 * @return 终端通道列表（仅成员在线的）
	 */
	public List<BusinessGroupMemberTerminalChannelPO> obtainMemberTerminalChannelsFromAgendaForwardSource(
			Long sourceId, SourceType sourceType, Collection<BusinessGroupMemberPO> members){
		
		List<BusinessGroupMemberTerminalChannelPO> result = new ArrayList<BusinessGroupMemberTerminalChannelPO>();
		if(SourceType.GROUP_MEMBER_CHANNEL.equals(sourceType)){
			
			BusinessGroupMemberTerminalChannelPO businessGroupMemberTerminalChannel = businessGroupMemberTerminalChannelDao.findOne(sourceId);
			if(businessGroupMemberTerminalChannel != null
					&& businessGroupMemberTerminalChannel.getMember().getGroupMemberStatus().equals(GroupMemberStatus.CONNECT)){
				result.add(businessGroupMemberTerminalChannel);
			}
			return result;
			
		}else if(SourceType.ROLE_CHANNEL.equals(sourceType)){
			//应该拿到的是角色通道
			RoleChannelPO roleChannel = roleChannelDao.findOne(sourceId);
			List<BusinessGroupMemberPO> sourceMembers = tetrisBvcQueryUtil.queryGroupMembersByRoleId(members, roleChannel.getRoleId());
			for(BusinessGroupMemberPO srcMember : sourceMembers){
				if(!srcMember.getGroupMemberStatus().equals(GroupMemberStatus.CONNECT)) continue;
				//通过角色通道找到终端通道srcChannel
				List<BusinessGroupMemberTerminalChannelPO> channels = srcMember.getChannels();
				RoleChannelTerminalChannelPermissionPO per = roleChannelTerminalChannelPermissionDao.findByTerminalIdAndRoleChannelId(srcMember.getTerminalId(), sourceId);
				if(per != null){
					BusinessGroupMemberTerminalChannelPO srcChannel = tetrisBvcQueryUtil.queryBusinessGroupMemberTerminalChannelByTerminalChannelId(channels, per.getTerminalChannelId());
					
					if(srcChannel != null) result.add(srcChannel);
				}
			}
			return result;
		}
		return null;
	}

	/**
	 * 从AgendaForwardPO获取所有的LayoutForwardPO列表<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月4日 上午10:43:08
	 * @param agendaForward
	 * @return LayoutForwardPO列表
	 * @throws BaseException 
	 */
	public List<LayoutForwardPO> obtainLayoutForwardsFromAgendaForward(AgendaForwardPO agendaForward) throws BaseException{
		
		//找到对应的LayoutPO，应该有且仅有1个（可以提取公共方法）
		LayoutPO layout = null;
		LayoutSelectionType layoutSelectioon = agendaForward.getLayoutSelectionType();
		if(LayoutSelectionType.ADAPTABLE.equals(layoutSelectioon)){
			//从源数量通过 LayoutScopePO 找到LayoutPO
			List<AgendaForwardSourcePO> agendaForwardSources = agendaForwardSourceDao.findByAgendaForwardId(agendaForward.getId());
			int size = agendaForwardSources.size();
			if(size > 16) size = 16;
			if(size < 1) size = 1;
			LayoutScopePO scope = layoutScopeDao.findByAgendaForwardIdAndSourceNumber(agendaForward.getId(), size);
			layout = layoutDao.findOne(scope.getLayoutId());
			//TODO:自动合屏
		}else{
			Long layoutId = agendaForward.getLayoutId();
			if(layoutId != null){
				layout = layoutDao.findOne(layoutId);
			}else {
				throw new BaseException(StatusCode.FORBIDDEN, "请先给议程转发："+agendaForward.getName()+" 配置一个分屏布局");
			}
			
		}
		
		//找到该AgendaForwardPO下所有的LayoutForwardPO
		List<LayoutForwardPO> agendaForwardLayoutForwards = layoutForwardDao.findByLayoutId(layout.getId());
		return agendaForwardLayoutForwards;
	}
	
	/**
	 * 从AgendaForwardPO中获取目的被配置为角色的Map<AgendaForwardId, List<AgendaForwardDestinationPO>>集合<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月25日 下午1:56:50
	 * @param forwards 议程转发
	 * @param onlyRole 只要目的为角色
	 * @return
	 */
	@Deprecated
	public Map<Long, List<AgendaForwardDestinationPO>> obtainAgendaForwardAndAgendaForwardDestinationMap(Collection<AgendaForwardPO> forwards, Boolean onlyRole){
		
		List<Long> forwardIds = forwards.stream().map(AgendaForwardPO::getId).collect(Collectors.toList());
		List<AgendaForwardDestinationPO> allForwardDst= agendaForwardDestinationDao.findByAgendaForwardIdIn(forwardIds);
		Map<Long, List<AgendaForwardDestinationPO>> forwardAndForwardDstMap = new HashMap<Long, List<AgendaForwardDestinationPO>>(); 
		if(onlyRole){
			forwardAndForwardDstMap = allForwardDst.stream().filter(forwardDst -> DestinationType.ROLE.equals(forwardDst.getDestinationType())).collect(Collectors.groupingBy(AgendaForwardDestinationPO::getAgendaForwardId));
		}else{
			forwardAndForwardDstMap = allForwardDst.stream().collect(Collectors.groupingBy(AgendaForwardDestinationPO::getAgendaForwardId));
		}
		return forwardAndForwardDstMap;
	}
	
	/**
	 * 从议程和角色获取转发源通道。通常用来给级联的passby赋值<br/>
	 * <p>仅适用于点播、呼叫这种简单转发的议程。且源通道有多个时，会get(0)</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月4日 下午2:39:59
	 * @param agendaId
	 * @param role 源角色
	 * @param members 必须包含源角色对应的成员
	 * @return
	 */
	public BusinessGroupMemberTerminalBundleChannelPO obtainEncodeChannel(Long agendaId, RolePO role, List<BusinessGroupMemberPO> members){
		
		List<AgendaForwardPO> agendaForwards = agendaForwardDao.findByAgendaId(agendaId);
		AgendaForwardSourcePO agendaForwardSource = null;
		
		//找AgendaForwardSourcePO，sourceType==ROLE_CHANNEL，sourceId对应的RoleChannel的roleId是role的id
		for(AgendaForwardPO agendaForward : agendaForwards){
			List<AgendaForwardSourcePO> agendaForwardSources = agendaForwardSourceDao.findByAgendaForwardId(agendaForward.getId());
			AgendaForwardSourcePO _agendaForwardSource = agendaForwardSources.get(0);
			if(_agendaForwardSource.getSourceType().equals(SourceType.ROLE_CHANNEL)){
				RoleChannelPO roleChannel = roleChannelDao.findOne(_agendaForwardSource.getSourceId());
				if(roleChannel.getRoleId().equals(role.getId())){
					agendaForwardSource = _agendaForwardSource;
					break;
				}
			}
		}
		
		List<BusinessGroupMemberTerminalChannelPO> videoEncodeTerminalChannels = obtainMemberTerminalChannelsFromAgendaForwardSource(
				agendaForwardSource.getSourceId(), agendaForwardSource.getSourceType(), members);
		BusinessGroupMemberTerminalChannelPO videoEncodeTerminalChannel = null;
		if(videoEncodeTerminalChannels.size() > 0) videoEncodeTerminalChannel = videoEncodeTerminalChannels.get(0);
		
		//选择能匹配分辨率码率的编码通道
		BusinessGroupMemberTerminalBundleChannelPO videoEncodeBundleChannel = multiRateUtil.queryDefultEncodeChannel(
				videoEncodeTerminalChannel.getMemberTerminalBundleChannels());
		return videoEncodeBundleChannel;
	}
}

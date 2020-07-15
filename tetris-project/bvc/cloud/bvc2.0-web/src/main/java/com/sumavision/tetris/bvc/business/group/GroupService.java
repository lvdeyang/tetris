package com.sumavision.tetris.bvc.business.group;

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
import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.bvc.business.OriginType;
import com.sumavision.tetris.bvc.business.bo.MemberTerminalBO;
import com.sumavision.tetris.bvc.business.bo.SourceBO;
import com.sumavision.tetris.bvc.business.bo.UserTerminalBO;
import com.sumavision.tetris.bvc.business.common.BusinessCommonService;
import com.sumavision.tetris.bvc.business.dao.GroupDAO;
import com.sumavision.tetris.bvc.business.dao.GroupDemandDAO;
import com.sumavision.tetris.bvc.business.dao.GroupMemberDAO;
import com.sumavision.tetris.bvc.business.dao.GroupMemberRolePermissionDAO;
import com.sumavision.tetris.bvc.business.forward.CommonForwardPO;
import com.sumavision.tetris.bvc.business.group.demand.GroupDemandPO;
import com.sumavision.tetris.bvc.business.group.demand.GroupDemandService;
import com.sumavision.tetris.bvc.cascade.CommandCascadeService;
import com.sumavision.tetris.bvc.cascade.ConferenceCascadeService;
import com.sumavision.tetris.bvc.cascade.bo.GroupBO;
import com.sumavision.tetris.bvc.cascade.bo.MinfoBO;
import com.sumavision.tetris.bvc.model.agenda.AgendaDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaPO;
import com.sumavision.tetris.bvc.model.agenda.AgendaService;
import com.sumavision.tetris.bvc.model.role.RolePO;
import com.sumavision.tetris.bvc.model.terminal.TerminalDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalPO;
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
public class GroupService {
	
	/** 是否强制接听进入指挥和会议，false需要点击同意 */
	private static boolean autoEnter = true;
	
	/** synchronized锁的前缀 */
	private static final String lockProcessPrefix = "tetris-group-";
	
	@Autowired
	private TerminalDAO terminalDao;
	
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
	
	/*public GroupPO saveById(
			Long creatorUserId,
			MemberTerminalBO chairmanBO,
			String name,
			String subject,
			BusinessType groupBusinessType,
			OriginType originType,
			List<MemberTerminalBO> memberTerminalBOs,
//			List<String> memberBundleIds,
			String uuid
			) throws Exception{
		TerminalPO terminalPO = terminalDao.findByType(com.sumavision.tetris.bvc.model.terminal.TerminalType.QT_ZK);
	}*/
	
	/** 重构新建业务组 */
	@Transactional(rollbackFor = Exception.class)
	public GroupPO save(
			Long creatorUserId,
			MemberTerminalBO chairmanBO,
			String name,
			String subject,
			BusinessType groupBusinessType,
			OriginType originType,
			List<MemberTerminalBO> memberTerminalBOs,
//			List<String> memberBundleIds,
			String uuid
			) throws Exception{
		
		GroupPO group1 = groupDao.findByUuid(uuid);
		if(group1 != null){
			log.warn("级联建会，uuid已经存在：" + uuid);
			return group1;
		}
		
		/*List<Long> userIdList = new ArrayList<Long>();
		List<String> memberBundleIds = new ArrayList<String>();
		for(MemberTerminalBO userTerminalBO : memberTerminalBOs){
			GroupMemberType groupMemberType = userTerminalBO.getGroupMemberType();
			if(GroupMemberType.MEMBER_USER.equals(groupMemberType)){
				userIdList.add(Long.parseLong(userTerminalBO.getOriginId()));
			}else if(GroupMemberType.MEMBER_DEVICE.equals(groupMemberType)){
				memberBundleIds.add(userTerminalBO.getOriginId());
			}
		}*/
		
		//校验
		UserBO creatorUserBo = resourceService.queryUserById(creatorUserId, TerminalType.QT_ZK);
		if(creatorUserBo == null){
			throw new BaseException(StatusCode.FORBIDDEN, "当前用户已失效，请重新登录");
		}
		if(creatorUserBo.getFolderUuid() == null){
			throw new UserHasNoFolderException(creatorUserBo.getName());
		}
		
		//确保成员中有创建者
//		if(!userIdList.contains(creatorUserId)){
//			throw new BaseException(StatusCode.FORBIDDEN, "当前用户已失效，请重新登录");
//		}
		
		//本系统创建，则鉴权，则校验主席有编码器
		if(!OriginType.OUTER.equals(originType)){
			
			//鉴权，区分指挥与会议
//			if(type.equals(GroupType.BASIC)){
//				commandCommonServiceImpl.authorizeUsers(userIdList, chairmanUserId, BUSINESS_OPR_TYPE.ZK);
//			}else if(type.equals(GroupType.MEETING)){
//				commandCommonServiceImpl.authorizeUsers(userIdList, chairmanUserId, BUSINESS_OPR_TYPE.HY);
//			}
			
			String creatorEncoderId = commonQueryUtil.queryExternalOrLocalEncoderIdFromUserBO(creatorUserBo);
			List<BundlePO> creatorBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(creatorEncoderId).getList());
			if(creatorBundleEntities.size() == 0){
				throw new UserHasNoAvailableEncoderException(creatorUserBo.getName());
			}
		}
		
		//会议组重名校验
		if(!OriginType.OUTER.equals(originType)){
			List<CommandGroupPO> existGroups = commandGroupDao.findByName(name);
			if(existGroups!=null && existGroups.size()>0){
				List<CommandGroupPO> likeGroups = commandGroupDao.findByNameLike(name + "-%");
				String recommendedName0 = name + "-";
				String recommendedName = null;
				boolean ok = false;
				for(int i=2; ; i++){
					ok = true;
					recommendedName = recommendedName0 + i;
					for(CommandGroupPO likeGroup : likeGroups){
						if(recommendedName.equals(likeGroup.getName())){
							ok = false;
							break;
						}
					}
					if(ok){
						break;
					}
				}
				throw new CommandGroupNameAlreadyExistedException(name, recommendedName);
			}
		}
		
		GroupPO group = new GroupPO();
		if(uuid != null){
			group.setUuid(uuid);
		}
		group.setName(name);
		group.setSubject(subject);
		group.setBusinessType(groupBusinessType);
		group.setOriginType(originType);
		
		group.setUserId(creatorUserId);
		group.setUserName(creatorUserBo.getName());
		group.setCreatetime(new Date());
		
//		group.setRecord(false);
		group.setStatus(GroupStatus.STOP);
		
		//参数模板
		Map<String, Object> result = commandCommonServiceImpl.queryDefaultAvCodec();
		AvtplPO sys_avtpl = (AvtplPO)result.get("avtpl");
		Set<AvtplGearsPO> sys_gears = sys_avtpl.getGears();
		CommandGroupAvtplPO g_avtpl = new CommandGroupAvtplPO().set(sys_avtpl);
		g_avtpl.setGears(new HashSet<CommandGroupAvtplGearsPO>());
		CommandGroupAvtplGearsPO currentGear = null;
		for(AvtplGearsPO sys_gear:sys_gears){
			CommandGroupAvtplGearsPO g_gear = new CommandGroupAvtplGearsPO().set(sys_gear);
			g_avtpl.getGears().add(g_gear);
			g_gear.setAvtpl(g_avtpl);
			currentGear = g_gear;
			break;
		}
//		group.setAvtpl(g_avtpl);
//		g_avtpl.setGroup(group);
//		group.setCurrentGearLevel(currentGear.getLevel());
		
		//保存以获得id
		groupDao.save(group);
		
		
		
		List<GroupMemberPO> members = generateMembers(group.getId(), memberTerminalBOs, chairmanBO);
		groupMemberDao.save(members);
		
		//级联
		if(!OriginType.OUTER.equals(originType)){
//			if(GroupType.BASIC.equals(type)){
//				GroupBO groupBO = commandCascadeUtil.createCommand(group);
//				commandCascadeService.create(groupBO);
//			}else if(GroupType.MEETING.equals(type)){
//				GroupBO groupBO = commandCascadeUtil.createMeeting(group);
//				conferenceCascadeService.create(groupBO);
//			}
		}
		
		log.info(name + " 创建完成");
		operationLogService.send(creatorUserBo.getName(), "新建指挥", creatorUserBo.getName() + "新建指挥groupId:" + group.getId());
		return group;
	}
		
		
	
	/**
	 * 刷新一个会议信息<br/>
	 * <p>刷新内容：用户换编码器，编码器换接入层，重建参数模板</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月13日 下午6:50:51
	 * @param group
	 * @return
	 * @throws Exception
	 */
	/*private CommandGroupPO refresh(CommandGroupPO group) throws Exception{
		
		Long creatorUserId = group.getUserId();
		UserBO creatorUserBo = resourceService.queryUserById(group.getUserId(), TerminalType.QT_ZK);
		
		//清除需要重新生成的数据
		List<CommandGroupForwardPO> forwards = group.getForwards();		
		forwards.removeAll(forwards);
		
		//参数模板
		Map<String, Object> result = commandCommonServiceImpl.queryDefaultAvCodec();
		AvtplPO sys_avtpl = (AvtplPO)result.get("avtpl");
		Set<AvtplGearsPO> sys_gears = sys_avtpl.getGears();
//		g_avtpl.setGears(new HashSet<CommandGroupAvtplGearsPO>());
		CommandGroupAvtplPO g_avtpl = group.getAvtpl().set(sys_avtpl);//new CommandGroupAvtplPO().set(sys_avtpl);
		g_avtpl.getGears().removeAll(g_avtpl.getGears());
		CommandGroupAvtplGearsPO currentGear = null;
		for(AvtplGearsPO sys_gear:sys_gears){
			CommandGroupAvtplGearsPO g_gear = new CommandGroupAvtplGearsPO().set(sys_gear);
			g_avtpl.getGears().add(g_gear);
			g_gear.setAvtpl(g_avtpl);
			currentGear = g_gear;
			break;
		}
		group.setAvtpl(g_avtpl);
		g_avtpl.setGroup(group);
		group.setCurrentGearLevel(currentGear.getLevel());
		group.setSpeakType(GroupSpeakType.CHAIRMAN);
		
		//保存以获得id
//		commandGroupDao.save(group);
		
		List<Long> userIdList = new ArrayList<Long>();
		List<GroupMemberPO> members = group.getMembers();
		GroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		for(GroupMemberPO member : members){
			userIdList.add(member.getUserId());
		}
		String userIdListStr = StringUtils.join(userIdList.toArray(), ",");
		List<UserBO> commandUserBos = resourceService.queryUserListByIds(userIdListStr, TerminalType.QT_ZK);
		if(commandUserBos == null) commandUserBos = new ArrayList<UserBO>();
		List<FolderPO> allFolders = resourceService.queryAllFolders();
		
		List<FolderUserMap> folderUserMaps = folderUserMapDao.findByUserIdIn(userIdList);
		
		if(creatorUserBo.getFolderUuid() == null){
			throw new UserHasNoFolderException(creatorUserBo.getName());
		}
		//从List<UserBO>取出bundleId列表，注意判空；给UserBO中的folderId赋值
		List<String> bundleIds = new ArrayList<String>();
		for(UserBO user : commandUserBos){
			String bundleId = commonQueryUtil.queryExternalOrLocalEncoderIdFromUserBO(user);
			if(bundleId != null){
				bundleIds.add(bundleId);
			}
			for(FolderPO folder : allFolders){
				if(folder.getUuid().equals(user.getFolderUuid())){
					user.setFolderId(folder.getId());
					break;
				}
			}
		}
		
		//从bundleId列表查询所有的bundlePO
		List<BundlePO> srcBundleEntities = resourceBundleDao.findByBundleIds(bundleIds);
		if(srcBundleEntities == null) srcBundleEntities = new ArrayList<BundlePO>();
		
		//从bundleId列表查询所有的视频编码1通道
		List<ChannelSchemeDTO> videoEncode1Channels = resourceChannelDao.findByBundleIdsAndChannelId(bundleIds, ChannelType.VIDEOENCODE1.getChannelId());
		if(videoEncode1Channels == null) videoEncode1Channels = new ArrayList<ChannelSchemeDTO>();
		//通过视频编码通道来校验编码器是否可用
		if(videoEncode1Channels.size() < commandUserBos.size()){
			for(UserBO user : commandUserBos){
				
				//外部系统用户则跳过
				if(queryUtil.isLdapUser(user, folderUserMaps)){
					continue;
				}
				
				boolean hasChannel = false;
				for(ChannelSchemeDTO channel : videoEncode1Channels){
					String encoderId = commonQueryUtil.queryExternalOrLocalEncoderIdFromUserBO(user);
					if(encoderId!=null && channel.getBundleId().equals(encoderId)){
						hasChannel = true;
						break;
					}
				}
				if(!hasChannel){
					throw new UserHasNoAvailableEncoderException(user.getName());
				}
			}
		}
		
		//从bundleId列表查询所有的音频编码1通道
		List<ChannelSchemeDTO> audioEncode1Channels = resourceChannelDao.findByBundleIdsAndChannelId(bundleIds, ChannelType.AUDIOENCODE1.getChannelId());
		if(audioEncode1Channels == null) audioEncode1Channels = new ArrayList<ChannelSchemeDTO>();
					
		for(GroupMemberPO member : members){
			UserBO user = queryUtil.queryUserById(commandUserBos, member.getUserId());
			if(user == null){
				continue;//后续考虑删除成员
			}
			
			//重设folderId
			if(user.getFolderId() == null){
				throw new BaseException(StatusCode.FORBIDDEN, member.getUserName() + " 没有组织机构！");
			}
			member.setFolderId(user.getFolderId());
			
			//跳过ldap用户
			if(queryUtil.isLdapUser(user, folderUserMaps)){
//				member.setOriginType(OriginType.OUTER);
				continue;
			}
			
			String encoderId = commonQueryUtil.queryExternalOrLocalEncoderIdFromUserBO(user);
			BundlePO bundle = queryUtil.queryBundlePOByBundleId(srcBundleEntities, encoderId);
			//校验编码器是否存在
			if(bundle == null){
				throw new BaseException(StatusCode.FORBIDDEN, member.getUserName() + " 的编码器异常！");
			}
			//校验编码器是否有layerId
			if(bundle.getAccessNodeUid() == null || "".equals(bundle.getAccessNodeUid())){
				throw new BaseException(StatusCode.FORBIDDEN, member.getUserName() + " 的编码器未上线！");
			}
			if(member.getSrcBundleId().equals(encoderId)){
				if(!bundle.getAccessNodeUid().equals(member.getSrcLayerId())){
					//bundle不变，layer变了
					member.setSrcLayerId(bundle.getAccessNodeUid());
				}
			}else{
				//bundle变了
				member.setSrcBundleId(bundle.getBundleId());
				member.setSrcBundleName(bundle.getBundleName());
				member.setSrcBundleType(bundle.getDeviceModel());
				member.setSrcVenusBundleType(bundle.getBundleType());
				member.setSrcLayerId(bundle.getAccessNodeUid());
				
				//遍历视频通道
				for(ChannelSchemeDTO videoChannel : videoEncode1Channels){
					if(videoChannel.getBundleId().equals(encoderId)){
						member.setSrcVideoChannelId(videoChannel.getChannelId());
						break;
					}
				}
				
				//遍历音频通道
				for(ChannelSchemeDTO audioChannel : audioEncode1Channels){
					if(audioChannel.getBundleId().equals(encoderId)){
						member.setSrcAudioChannelId(audioChannel.getChannelId());
						break;
					}
				}
			}			
		}
		
		for(GroupMemberPO member : members){
			if(member.isAdministrator()){
				//建立所有成员到主席的转发（在下边）				
			}else{
				//建立主席到成员的转发
				CommandGroupForwardPO c2m_forward = new CommandGroupForwardPO(
						ForwardBusinessType.BASIC_COMMAND,
						ExecuteStatus.UNDONE,
						ForwardDstType.USER,
						member.getId(),
						chairmanMember.getId(),
						chairmanMember.getSrcBundleId(),
						chairmanMember.getSrcBundleName(),
						chairmanMember.getSrcVenusBundleType(),
						chairmanMember.getSrcLayerId(),
						chairmanMember.getSrcVideoChannelId(),
						"VenusVideoIn",//videoBaseType,
						chairmanMember.getSrcBundleId(),
						chairmanMember.getSrcBundleName(),
						chairmanMember.getSrcBundleType(),
						chairmanMember.getSrcLayerId(),
						chairmanMember.getSrcAudioChannelId(),
						"VenusAudioIn",//String audioBaseType,
						null,//member.getDstBundleId(),
						null,//member.getDstBundleName(),
						null,//member.getDstBundleType(),
						null,//member.getDstLayerId(),
						null,//member.getDstVideoChannelId(),
						"VenusVideoOut",//String dstVideoBaseType,
						null,//member.getDstAudioChannelId(),
						null,//member.getDstBundleName(),
						null,//member.getDstBundleType(),
						null,//member.getDstLayerId(),
						null,//member.getDstAudioChannelId(),
						"VenusAudioOut",//String dstAudioBaseType,
						creatorUserId,
						g_avtpl.getId(),//Long avTplId,
						currentGear.getId(),//Long gearId,
						DstDeviceType.WEBSITE_PLAYER,
						null,//LiveType type,
						null,//Long osdId,
						null//String osdUsername);
						);
				c2m_forward.setGroup(group);
				forwards.add(c2m_forward);
				
				//建立成员到主席的转发
				CommandGroupForwardPO m2c_forward = new CommandGroupForwardPO(
						ForwardBusinessType.BASIC_COMMAND,
						ExecuteStatus.UNDONE,
						ForwardDstType.USER,
						chairmanMember.getId(),
						member.getId(),
						member.getSrcBundleId(),
						member.getSrcBundleName(),
						member.getSrcVenusBundleType(),
						member.getSrcLayerId(),
						member.getSrcVideoChannelId(),
						"VenusVideoIn",//videoBaseType,
						member.getSrcBundleId(),
						member.getSrcBundleName(),
						member.getSrcBundleType(),
						member.getSrcLayerId(),
						member.getSrcAudioChannelId(),
						"VenusAudioIn",//String audioBaseType,
						null,//chairmanMember.getDstBundleId(),
						null,//chairmanMember.getDstBundleName(),
						null,//chairmanMember.getDstBundleType(),
						null,//chairmanMember.getDstLayerId(),
						null,//chairmanMember.getDstVideoChannelId(),
						null,//String dstVideoBaseType,
						null,//chairmanMember.getDstAudioChannelId(),
						null,//chairmanMember.getDstBundleName(),
						null,//chairmanMember.getDstBundleType(),
						null,//chairmanMember.getDstLayerId(),
						null,//chairmanMember.getDstAudioChannelId(),
						null,//String dstAudioBaseType,
						creatorUserId,
						g_avtpl.getId(),//Long avTplId,
						currentGear.getId(),//Long gearId,
						DstDeviceType.WEBSITE_PLAYER,
						null,//LiveType type,
						null,//Long osdId,
						null//String osdUsername);
						);
				m2c_forward.setGroup(group);
				forwards.add(m2c_forward);
				
			}
		}
		
		group.setForwards(forwards);		
		commandGroupDao.save(group);
		
		log.info(group.getName() + " 刷新完成");		
		return group;
	}*/

	/**
	 * 修改会议名称<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月16日 上午9:35:29
	 * @param userId
	 * @param groupId
	 * @param name
	 * @throws Exception
	 */
	/*public void modifyName(Long userId, Long groupId, String name) throws Exception{
		UserVO user = userQuery.current();		
		if(name==null || name.equals("")){
			throw new BaseException(StatusCode.FORBIDDEN, "请输入名称");
		}
		
		List<CommandGroupPO> existGroups = commandGroupDao.findByName(name);
		if(existGroups!=null && existGroups.size()>0){
			throw new CommandGroupNameAlreadyExistedException(name);
		}
		
		CommandGroupPO group = commandGroupDao.findOne(groupId);
		if(!GroupStatus.STOP.equals(group.getStatus())){
			throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已经开始，请停止后再删除。id: " + group.getId());
		}
		if(!group.getUserId().equals(userId)){
			throw new BaseException(StatusCode.FORBIDDEN, "只有主席才能修改");
		}
		group.setName(name);
		commandGroupDao.save(group);
		
		//级联 groupUpdate
		if(!OriginType.OUTER.equals(group.getOriginType())){
			GroupType groupType = group.getType();
			if(GroupStatus.STOP.equals(group.getStatus())){
				if(GroupType.BASIC.equals(groupType)){
					GroupBO groupBO = commandCascadeUtil.updateCommand(group);
					commandCascadeService.update(groupBO);						
				}else if(GroupType.MEETING.equals(groupType)){
					GroupBO groupBO = commandCascadeUtil.updateMeeting(group);
					conferenceCascadeService.update(groupBO);			
				}
			}
		}
		operationLogService.send(user.getNickname(), "修改指挥名称", user.getNickname() + "修改指挥名称" + group.getId());
	}*/
	
	/**
	 * 
	 * 删除指挥<br/>
	 * <p>非创建者不能删，已经开始的指挥不能删</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月24日 下午1:26:09
	 * @param userId 操作人
	 * @param groupIds
	 * @throws Exception
	 */
	public void remove(Long userId, List<Long> groupIds) throws Exception{
		UserVO user = userQuery.current();
		groupIds.remove(null);
		List<CommandGroupPO> groups = commandGroupDao.findAll(groupIds);
		StringBuffer dis = new StringBuffer();
		
		//校验
		for(CommandGroupPO group : groups){
			if(!userId.equals(group.getUserId()) && !group.getType().equals(GroupType.SECRET)){
				if(!OriginType.OUTER.equals(group.getOriginType())){
					throw new BaseException(StatusCode.FORBIDDEN, "只有创建者能删除 " + group.getName());
				}
			}
			if(!GroupStatus.STOP.equals(group.getStatus())){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已经开始，请停止后再删除。id: " + group.getId());
			}
			dis.append(group.getName() + "，");
			
			GroupType type = group.getType();
			if(!OriginType.OUTER.equals(group.getOriginType())){
				if(GroupType.BASIC.equals(type)){
					GroupBO groupBO = commandCascadeUtil.deleteCommand(group);
					commandCascadeService.delete(groupBO);
				}else if(GroupType.MEETING.equals(type)){
					GroupBO groupBO = commandCascadeUtil.deleteMeeting(group);
					conferenceCascadeService.delete(groupBO);
				}
			}
		}
		
		commandGroupDao.deleteByIdIn(groupIds);
		
		log.info(dis.toString() + "被删除");
		operationLogService.send(user.getNickname(), "删除指挥", user.getNickname() + "删除指挥groupIds:" + groupIds.toString());
	}
	
	/**
	 * 
	 * 开启会议<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月24日 上午11:03:19
	 * @param groupId
	 * @param locationIndex 指定播放器序号，序号从0起始；-1为自动选择。仅在专向会议中可以指定序号；普通会议中必须为-1。
	 * @return result 主席的播放器信息
	 * @throws Exception
	 */	
	public Object start(Long groupId, int locationIndex) throws Exception{
		return start(groupId, locationIndex, true, null, null, null);
	}
	
	/**
	 * 开启指挥/会议<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月25日 上午10:03:50
	 * @param groupId
	 * @param locationIndex 指定播放器序号，序号从0起始；-1为自动选择。仅在专向会议中可以指定序号；普通会议中必须为-1。
	 * @param refresh 是否刷新，默认true
	 * @param enterMemberIds 默认null。指定进入会议的userId列表，注意应该包含主席
	 * @param startTime 默认null。指定会议开启时间（通常在全量信息同步时）
	 * @param groupStatus 默认null。指定会议当前状态，取值为START/PAUSE，其它不支持（通常在全量信息同步时）
	 * @return
	 * @throws Exception
	 */
	public Object start(Long groupId, int locationIndex, boolean refresh, List<Long> enterMemberIds, Date startTime, GroupStatus groupStatus) throws Exception{
		
		UserVO user = userQuery.current();
		JSONObject result = new JSONObject();
		JSONArray chairSplits = new JSONArray();
		
		if(groupId == null || "".equals(groupId)){
			log.warn("开始会议，会议id有误");
			return result;
		}
		
		synchronized (new StringBuffer().append(lockProcessPrefix).append(groupId).toString().intern()) {
					
		GroupPO group = groupDao.findOne(groupId);
		BusinessType groupType = group.getBusinessType();
//		List<Long> userIdList = new ArrayList<Long>();
		List<GroupMemberPO> members = groupMemberDao.findByGroupId(groupId);
		for(GroupMemberPO member : members){
//			userIdList.add(member.getUserId());
		}
		
		//本系统创建的，则鉴权，区分指挥与会议
//		if(!OriginType.OUTER.equals(group.getOriginType())){
//			if(groupType.equals(GroupType.BASIC)){
//				commandCommonServiceImpl.authorizeUsers(userIdList, group.getUserId(), BUSINESS_OPR_TYPE.ZK);
//			}else if(groupType.equals(GroupType.MEETING)){
//				commandCommonServiceImpl.authorizeUsers(userIdList, group.getUserId(), BUSINESS_OPR_TYPE.HY);
//			}
//		}
		
		//普通指挥、会议，刷新会议数据
		if(!groupType.equals(GroupType.SECRET) && refresh){
//			refresh(group);
		}
		
		//后需考虑支持重复开始
		if(!group.getStatus().equals(GroupStatus.STOP)){
			result.put("splits", chairSplits);
			return result;
		}
		String commandString = tetrisBvcQueryUtil.generateCommandString(groupType);
		if(GroupStatus.PAUSE.equals(groupStatus)){
			group.setStatus(groupStatus);
		}else{
			group.setStatus(GroupStatus.START);
		}
		if(startTime == null) startTime = new Date();
		group.setStartTime(startTime);		
		
		//处理主席，置为CONNECT，绑定主席角色。其它成员的绑定角色放在membersResponse里边
		GroupMemberPO chairman = tetrisBvcQueryUtil.queryChairmanMember(members);
		chairman.setGroupMemberStatus(GroupMemberStatus.CONNECT);
		RolePO chairmanRole = null;//TODO
		GroupMemberRolePermissionPO memberRolePermission = new GroupMemberRolePermissionPO(chairmanRole.getId(), chairman.getId());
		groupMemberRolePermissionDao.save(memberRolePermission);
		
		//处理其它成员		
		/*for(GroupMemberPO member : members){
			
			if(member.isAdministrator()){
				continue;
			}
			
			member.setGroupMemberStatus(GroupMemberStatus.CONNECTING);
			
			JSONObject message = new JSONObject();
			message.put("fromUserId", chairman.getUserId());
			message.put("fromUserName", chairman.getUserName());
			message.put("businessId", group.getId().toString());
			if(group.getType().equals(GroupType.BASIC) || group.getType().equals(GroupType.MEETING)){
				if(!autoEnter){
					String businessType = null;
					if(GroupType.MEETING.equals(group.getType())){
						businessType = "meetingStart";//自动接听使用meetingStartNow
					}else{
						businessType = "commandStart";//自动接听使用commandStartNow
					}
					message.put("businessType", businessType);
					message.put("businessInfo", "接受到 " + group.getName() + " 邀请，主席：" + chairman.getUserName() + "，是否进入？");
					
					//发送消息
					WebsocketMessageVO ws = websocketMessageService.send(member.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, chairman.getUserId(), chairman.getUserName());
					member.setMessageId(ws.getId());						
				}
			}else if(group.getType().equals(GroupType.SECRET)){
				message.put("businessType", "secretStart");
				message.put("businessInfo", chairman.getUserName() + " 邀请你专向" + commandString);
				
				//发送消息
				WebsocketMessageVO ws = websocketMessageService.send(member.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, chairman.getUserId(), chairman.getUserName());
				member.setMessageId(ws.getId());
			}
		}*/
		
		//自动接听则所有在线的人接听，否则只有主席接听；专向指挥只有主席接听
//		String userIdListStr = StringUtils.join(userIdList.toArray(), ",");
//		List<UserBO> commandUserBos = resourceService.queryUserListByIds(userIdListStr, TerminalType.QT_ZK);
		List<GroupMemberPO> acceptMembers = null;
		if(enterMemberIds != null){
			//如果指定了enterUserIds（通常是级联的全量信息同步），则使用
			acceptMembers = new ArrayListWrapper<GroupMemberPO>().getList();
			for(GroupMemberPO member : members){
				if(enterMemberIds.contains(member.getId())){
					acceptMembers.add(member);
				}else{
					//不进入的，置为DISCONNECT
					member.setGroupMemberStatus(GroupMemberStatus.DISCONNECT);
				}
			}
		}
		if(autoEnter && !group.getBusinessType().equals(GroupType.SECRET)){
			//只给在线的成员自动上线
			acceptMembers = new ArrayListWrapper<GroupMemberPO>().add(chairman).getList();
			for(GroupMemberPO member : members){
				if(member.getIsAdministrator()) continue;
//				if(OriginType.OUTER.equals(member.getOriginType())){member.setMemberStatus(MemberStatus.DISCONNECT);continue;}//优化思路，外部系统的由其所在系统对其进行“进会”
//				UserBO commandUserBo = queryUtil.queryUserById(commandUserBos, member.getUserId());
//				if(commandUserBo.isLogined()){//TODO:该成员如果对应终端登录，则进会
					acceptMembers.add(member);
//				}else{
//					//没在线的，置为DISCONNECT。不用按照“拒绝”处理
//					member.setMemberStatus(MemberStatus.DISCONNECT);
//				}
			}
		}else{
			acceptMembers = new ArrayList<GroupMemberPO>();
			acceptMembers.add(chairman);
		}
		
		groupDao.save(group);
		
		//执行默认议程
		AgendaPO commandAgenda = null;//TODO
		agendaService.runAndStopAgenda(group.getId(), new ArrayListWrapper<Long>().add(commandAgenda.getId()).getList(), null);
		
		membersResponse(group, acceptMembers, null);
		
		//级联
		if(!OriginType.OUTER.equals(group.getOriginType())){
//			if(GroupType.BASIC.equals(groupType)){
//				GroupBO groupBO = commandCascadeUtil.startCommand(group);
//				commandCascadeService.start(groupBO);
//			}else if(GroupType.MEETING.equals(groupType)){
//				GroupBO groupBO = commandCascadeUtil.startMeeting(group);
//				conferenceCascadeService.start(groupBO);
//			}
		}
		
		/*boolean hasOuterMember = false;
		for(GroupMemberPO member : members){
			if(OriginType.OUTER.equals(member.getOriginType())){
				hasOuterMember = true;
				break;
			}
		}
		if(hasOuterMember){
			if(GroupType.BASIC.equals(groupType)){
				Thread.sleep(300);//延时确保其它节点开会已完成
				GroupBO groupBO = commandCascadeUtil.joinCommand(group, null, acceptMembers);//需要把主席去掉吗？
				commandCascadeService.join(groupBO);
			}else if(GroupType.MEETING.equals(groupType)){
				Thread.sleep(300);//延时确保其它节点开会已完成
				GroupBO groupBO = commandCascadeUtil.joinMeeting(group, null, acceptMembers);//需要把主席去掉吗？
				conferenceCascadeService.join(groupBO);
			}
		}*/
		
		result.put("splits", chairSplits);
		operationLogService.send(user.getNickname(), "开启指挥", user.getNickname() + "开启指挥groupId:" + groupId);
		return result;

		}
	}	
	
	/**
	 * 
	 * 停止会议<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月24日 上午11:04:05
	 * @param userId 操作人
	 * @param groupId
	 * @param stopMode 0为正常停止，1为拒绝专向会议导致的停止，该参数用来在专向会议中发送不同的消息
	 * @return chairSplits 主席需要关闭的播放器序号
	 * @throws Exception
	 */
	public JSONArray stop(Long userId, Long groupId, int stopMode) throws Exception{
		
		UserVO user = userQuery.current();
		
		//通常返回主席的屏幕；在专向会议中，由对方成员停止或拒绝，返回对方成员的屏幕
		JSONArray returnSplits = new JSONArray();
		JSONArray chairSplits = new JSONArray();
		JSONArray secretSplits = new JSONArray();
		
		if(groupId==null || groupId.equals("")){
			log.info("停会操作，会议id有误");
			return chairSplits;
//			throw new BaseException(StatusCode.FORBIDDEN, "停会操作，会议id有误");
		}
		
		synchronized (new StringBuffer().append(lockProcessPrefix).append(groupId).toString().intern()) {
						
			GroupPO group = groupDao.findOne(groupId);
			if(group == null){
	//			throw new BaseException(StatusCode.FORBIDDEN, "会议不存在，id: " + groupId);
				log.info("进行停止操作的会议不存在，id：" + groupId);
				return new JSONArray();
			}
			BusinessType groupType = group.getBusinessType();
			String commandString = tetrisBvcQueryUtil.generateCommandString(groupType);
			if(group.getStatus().equals(GroupStatus.REMIND)){
				throw new BaseException(StatusCode.FORBIDDEN, "请先关闭" + commandString + "提醒");
			}
			
			group.setStatus(GroupStatus.STOP);
	//		group.setSpeakType(GroupSpeakType.CHAIRMAN);
			group.setStartTime(null);
			Date endTime = new Date();
			group.setEndTime(endTime);
			List<GroupMemberPO> members = groupMemberDao.findByGroupId(groupId);
	//		List<CommandGroupForwardPO> forwards = group.getForwards();
	//		List<CommandGroupForwardDemandPO> demands = group.getForwardDemands();
			List<GroupMemberPO> connectMembers = new ArrayList<GroupMemberPO>();
	//		List<CommandGroupUserPlayerPO> needClosePlayers = new ArrayList<CommandGroupUserPlayerPO>();
	//		List<CommandGroupUserPlayerPO> playFilePlayers = new ArrayList<CommandGroupUserPlayerPO>();
			List<Long> consumeIds = new ArrayList<Long>();
			List<MessageSendCacheBO> messageCaches = new ArrayList<MessageSendCacheBO>();
			GroupMemberPO chairmanMember = tetrisBvcQueryUtil.queryChairmanMember(members);
			
			//专向会议中，先获取主席和专向成员的分屏
			GroupMemberPO secretMember = null;
			/*if(group.getBusinessType().equals(BusinessType.SECRET)){
				for(GroupMemberPO member : members){
					if(!member.isAdministrator()){
						secretMember = member;
						break;
					}
				}
				
				if(!OriginType.OUTER.equals(chairmanMember.getOriginType())){
					for(CommandGroupUserPlayerPO player : chairmanMember.getPlayers()){
						JSONObject split = new JSONObject();
						split.put("serial", player.getLocationIndex());
						chairSplits.add(split);
					}
					returnSplits = chairSplits;
				}
				
				if(!OriginType.OUTER.equals(secretMember.getOriginType())){
					for(CommandGroupUserPlayerPO player : secretMember.getPlayers()){
						JSONObject split = new JSONObject();
						split.put("serial", player.getLocationIndex());
						secretSplits.add(split);
					}
				}
			}*/
			
			//处理所有成员，包括主席
			for(GroupMemberPO member : members){
				if(member.getGroupMemberStatus().equals(GroupMemberStatus.CONNECT)){
					connectMembers.add(member);
				}
				
				//处理协同会议状态
//				if(member.getCooperateStatus().equals(MemberStatus.CONNECTING)){
//					consumeIds.add(member.getMessageCoId());
//					member.setMessageCoId(null);
//				}			
				
				if(OriginType.OUTER.equals(member.getOriginType())){
					//状态重置（本系统用户的状态重置写在下头）
					member.setGroupMemberStatus(GroupMemberStatus.DISCONNECT);
					member.setSilenceToHigher(false);
					member.setSilenceToLower(false);
					continue;
				}
				
				if(GroupMemberType.MEMBER_USER.equals(member.getGroupMemberType())){
					continue;
				}
								
				//普通指挥/会议，给其它 会议中的 成员发通知
				if(!member.getIsAdministrator() && group.getBusinessType().equals(BusinessType.COMMAND)
						|| !member.getIsAdministrator() && group.getBusinessType().equals(BusinessType.MEETING_QT)){
					
					if(member.getGroupMemberStatus().equals(GroupMemberStatus.CONNECT)){
						JSONObject message = new JSONObject();
						message.put("businessType", "commandStop");
						message.put("fromUserId", Long.parseLong(chairmanMember.getOriginId()));
						message.put("fromUserName", chairmanMember.getName());
						message.put("businessId", group.getId().toString());
						message.put("businessInfo", group.getName() + " 停止了");
						message.put("splits", new JSONArray());
						
						//发送消息
						messageCaches.add(new MessageSendCacheBO(Long.parseLong(member.getOriginId()), message.toJSONString(), WebsocketMessageType.COMMAND));
					}
				}
				
				//专向会议的通知
				/*if(group.getType().equals(GroupType.SECRET)){
					
					JSONObject message = new JSONObject();
					boolean send = false;
					if(member.isAdministrator()){
						//当前正在处理主席
						//对方成员拒绝导致的停止
						if(stopMode==1 && userId.equals(secretMember.getUserId())){
							message.put("businessType", "secretRefuse");
							message.put("fromUserId", secretMember.getUserId());
							message.put("fromUserName", secretMember.getUserName());
	//						message.put("businessId", group.getId().toString());
							message.put("businessInfo", secretMember.getUserName() + " 拒绝与你专向" + commandString);
							if(splits.size() > 0){
								message.put("serial", splits.getJSONObject(0).getInteger("serial"));
							}
							//返回操作人的屏幕
							returnSplits = secretSplits;
							send = true;
						}
						//对方成员停止
						if(stopMode==0 && userId.equals(secretMember.getUserId())){
							message.put("businessType", "secretStop");
							message.put("fromUserId", secretMember.getUserId());
							message.put("fromUserName", secretMember.getUserName());
	//						message.put("businessId", group.getId().toString());
							message.put("businessInfo", secretMember.getUserName() + " 停止了专向" + commandString);
							if(splits.size() > 0){
								message.put("serial", splits.getJSONObject(0).getInteger("serial"));
							}
							//返回操作人的屏幕
							returnSplits = secretSplits;
							send = true;
						}
					}else{
						//当前正在处理对方成员
						//主席停止，给对方发通知
						if(stopMode==0 && userId.equals(chairmanMember.getUserId())){
							message.put("businessType", "secretStop");
							message.put("fromUserId", chairmanMember.getUserId());
							message.put("fromUserName", chairmanMember.getUserName());
	//						message.put("businessId", group.getId().toString());
							message.put("businessInfo", chairmanMember.getUserName() + " 停止了专向" + commandString);
							if(splits.size() > 0){
								message.put("serial", splits.getJSONObject(0).getInteger("serial"));
							}
							//返回操作人的屏幕
							returnSplits = chairSplits;
							send = true;
						}
					}
					//发送消息
					if(send){
						messageCaches.add(new MessageSendCacheBO(member.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, chairmanMember.getUserId(), chairmanMember.getUserName()));
					}
				}*/
				
				//状态重置（外部系统用户的状态重置写在上头）
				member.setGroupMemberStatus(GroupMemberStatus.DISCONNECT);
				member.setSilenceToHigher(false);
				member.setSilenceToLower(false);
			}
			
			groupDao.save(group);
			groupMemberDao.save(members);
			
			//停止所有媒体转发
			List<GroupDemandPO> demands = groupDemandDao.findByBusinessId(groupId.toString());
			groupDemandService.stopDemands(group, demands, false);
			
			//停止所有的议程
			List<Long> runningAgendaIds = agendaDao.findRunningAgendaIdsByGroupId(groupId);
			agendaService.runAndStopAgenda(groupId, null, runningAgendaIds);
			
			//删除角色 TODO:这个查询可能漏掉一些媒体转发的角色
			List<Long> memberIds = businessCommonService.obtainMemberIds(members);
			List<GroupMemberRolePermissionPO> ps = groupMemberRolePermissionDao.findByGroupMemberIdIn(memberIds);
			groupMemberRolePermissionDao.deleteInBatch(ps);
			
			//关闭编码通道
			List<SourceBO> sourceBOs = agendaService.obtainSource(connectMembers, group.getId().toString(), BusinessInfoType.BASIC_COMMAND);
			CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();
			LogicBO logic = closeEncoder(sourceBOs, codec, -1L);
			executeBusiness.execute(logic, group.getName() + " 会议停止");
			
			/*
			//删除协同会议的forwardPO
			List<CommandGroupForwardPO> cooperateForwards = new ArrayList<CommandGroupForwardPO>();
			for(CommandGroupForwardPO forward : forwards){
				if(forward.getForwardBusinessType().equals(ForwardBusinessType.COOPERATE_COMMAND)){
					cooperateForwards.add(forward);
				}
			}
			forwards.removeAll(cooperateForwards);
			
			//清楚剩余普通转发的目的，把状态置为UNDONE
			for(CommandGroupForwardPO forward : forwards){
				forward.clearDst();
				forward.setExecuteStatus(ExecuteStatus.UNDONE);
			}
			
			//停止会议转发点播
			List<CommandGroupForwardDemandPO> needDelDemands = new ArrayList<CommandGroupForwardDemandPO>();
			for(CommandGroupForwardDemandPO demand : demands){
				if(demand.getExecuteStatus().equals(ForwardDemandStatus.DONE)){
					needDelDemands.add(demand);
				}
			}
			demands.clear();//全部清除
			
			commandGroupUserPlayerDao.save(needClosePlayers);
			commandGroupDao.save(group);
			
			//disconnect，考虑怎么给不需要挂断的编码器计数
			CommandGroupAvtplGearsPO currentGear = commandCommonUtil.queryCurrentGear(group);
			CodecParamBO codec = new CodecParamBO().set(group.getAvtpl(), currentGear);
			LogicBO logic = closeBundle(connectMembers, needDelDemands, needClosePlayers, codec, chairmanMember.getUserNum());
			LogicBO logicCastDevice = commandCastServiceImpl.closeBundleCastDevice(playFilePlayers, null, null, needClosePlayers, codec, group.getUserId());
			logic.merge(logicCastDevice);
			LogicBO logicStopRecord = commandRecordServiceImpl.stop(null, groupId, false);
			logic.merge(logicStopRecord);
			executeBusiness.execute(logic, group.getName() + " 会议停止");*/
			
			//TODO:停止录制
			
			//级联
			if(!OriginType.OUTER.equals(group.getOriginType())){
//				if(GroupType.BASIC.equals(groupType)){
//					GroupBO groupBO = commandCascadeUtil.stopCommand(group);
//					commandCascadeService.stop(groupBO);
//				}else if(GroupType.MEETING.equals(groupType)){
//					GroupBO groupBO = commandCascadeUtil.stopMeeting(group);
//					conferenceCascadeService.stop(groupBO);
//				}
			}
			
			//发消息
			for(MessageSendCacheBO cache : messageCaches){
				WebsocketMessageVO ws = websocketMessageService.send(cache.getUserId(), cache.getMessage(), cache.getType(), cache.getFromUserId(), cache.getFromUsername());
				consumeIds.add(ws.getId());
			}
			websocketMessageService.consumeAll(consumeIds);
			
			operationLogService.send(user.getNickname(), "停止指挥", user.getNickname() + "停止指挥groupId:" + groupId);
			return returnSplits;
		}
	}
	
	public Object addOrEnterMembers(Long groupId, List<MemberTerminalBO> memberTerminalBOs) throws Exception{
		UserVO self = userQuery.current();
		JSONArray chairSplits = new JSONArray();
		
		if(groupId==null || groupId.equals("")){
			log.info("会议加人或进入，会议id有误");
			return chairSplits;
		}
		
		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
					
			GroupPO group = groupDao.findOne(groupId);
			BusinessType businessType = group.getBusinessType();
			List<GroupMemberPO> members = groupMemberDao.findByGroupId(groupId);
			GroupMemberPO chairmanMember = tetrisBvcQueryUtil.queryChairmanMember(members);
			List<GroupMemberPO> enterMembers = new ArrayList<GroupMemberPO>();
			
			//本系统创建的，则鉴权，区分指挥与会议
//			if(!OriginType.OUTER.equals(group.getOriginType())){
//				if(groupType.equals(GroupType.BASIC)){
//					commandCommonServiceImpl.authorizeUsers(userIdList, group.getUserId(), BUSINESS_OPR_TYPE.ZK);
//				}else if(groupType.equals(GroupType.MEETING)){
//					commandCommonServiceImpl.authorizeUsers(userIdList, group.getUserId(), BUSINESS_OPR_TYPE.HY);
//				}
//			}
			
			//记录加人之前的用户列表
//			List<MinfoBO> oldMemberInfos = commandCascadeUtil.generateMinfoBOList(members);
			
			//区分出新用户和进入用户
//			List<Long> newUserIdList = new ArrayList<Long>();
//			List<Long> enterUserIdList = new ArrayList<Long>();
			List<MemberTerminalBO> newMemberTerminalBOs = new ArrayList<MemberTerminalBO>();
			List<String> newOriginIdList = new ArrayList<String>();
			List<String> enterOriginIdList = new ArrayList<String>();
			
			List<String> existedOriginIdList = groupMemberDao.findOriginIdsByGroupId(groupId);
			for(MemberTerminalBO memberTerminalBO : memberTerminalBOs){
				String originId = memberTerminalBO.getOriginId();
				if(existedOriginIdList.contains(originId)){
					
					GroupMemberPO member = tetrisBvcQueryUtil.queryMemberByOriginId(enterMembers, originId);
					if(member.getGroupMemberStatus().equals(GroupMemberStatus.CONNECT)){
						//已经进会的成员，不用再处理
					}else{
						enterOriginIdList.add(originId);
						enterMembers.add(member);
					}
					
				}else{
					newOriginIdList.add(originId);
					newMemberTerminalBOs.add(memberTerminalBO);
				}
			}
			
			List<GroupMemberPO> newMembers = generateMembers(group.getId(), memberTerminalBOs, null);
			groupMemberDao.save(newMembers);
			
			/*
			//用户管理层的批量接口，根据userIds查询List<UserBO>，由于缺少folderId，所以额外查询queryAllFolders，给UserBO中的folderId赋值
			String newUserIdListStr = StringUtils.join(newUserIdList.toArray(), ",");
			List<UserBO> commandUserBos = resourceService.queryUserListByIds(newUserIdListStr, TerminalType.QT_ZK);
			if(commandUserBos == null) commandUserBos = new ArrayList<UserBO>();
			List<FolderPO> allFolders = resourceService.queryAllFolders();
			List<FolderUserMap> folderUserMaps = folderUserMapDao.findByUserIdIn(newUserIdList);
			String localLayerId = null;
		
			//从List<UserBO>取出bundleId列表，注意判空；给UserBO中的folderId赋值
			List<String> bundleIds = new ArrayList<String>();
			for(UserBO user : commandUserBos){
				String encoderId = commonQueryUtil.queryExternalOrLocalEncoderIdFromUserBO(user);
				if(encoderId != null){
					bundleIds.add(encoderId);
				}
				for(FolderPO folder : allFolders){
					if(folder.getUuid().equals(user.getFolderUuid())){
						user.setFolderId(folder.getId());
						break;
					}
				}
			}
			
			//从bundleId列表查询所有的bundlePO
			List<BundlePO> srcBundleEntities = resourceBundleDao.findByBundleIds(bundleIds);
			if(srcBundleEntities == null) srcBundleEntities = new ArrayList<BundlePO>();
			
			//从bundleId列表查询所有的视频编码1通道
			List<ChannelSchemeDTO> videoEncode1Channels = resourceChannelDao.findByBundleIdsAndChannelId(bundleIds, ChannelType.VIDEOENCODE1.getChannelId());
			if(videoEncode1Channels == null) videoEncode1Channels = new ArrayList<ChannelSchemeDTO>();
			//通过视频编码通道来校验编码器是否可用
			if(videoEncode1Channels.size() < commandUserBos.size()){
				for(UserBO user : commandUserBos){
					
					//外部系统用户则跳过
					if(queryUtil.isLdapUser(user, folderUserMaps)){
						continue;
					}

					boolean hasChannel = false;
					String encoderId = commonQueryUtil.queryExternalOrLocalEncoderIdFromUserBO(user);
					for(ChannelSchemeDTO channel : videoEncode1Channels){
						if(channel.getBundleId().equals(encoderId)){
							hasChannel = true;
							break;
						}
					}
					if(!hasChannel){
						throw new UserHasNoAvailableEncoderException(user.getName());
					}
				}
			}
						
			//从bundleId列表查询所有的音频编码1通道
			List<ChannelSchemeDTO> audioEncode1Channels = resourceChannelDao.findByBundleIdsAndChannelId(bundleIds, ChannelType.AUDIOENCODE1.getChannelId());
			if(audioEncode1Channels == null) audioEncode1Channels = new ArrayList<ChannelSchemeDTO>();
			
//			Set<CommandGroupForwardPO> forwards = group.getForwards();
			GroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
			Long creatorUserId = chairmanMember.getUserId();
			CommandGroupAvtplPO g_avtpl = group.getAvtpl();
			CommandGroupAvtplGearsPO currentGear = commandCommonUtil.queryCurrentGear(group);
			
			List<GroupMemberPO> newMembers = new ArrayList<GroupMemberPO>();
			List<CommandGroupForwardPO> newForwards = new ArrayList<CommandGroupForwardPO>();
			
//			BasicRolePO chairmanRole = basicRoleDao.findByName("主席");
//			BasicRolePO memberRole = basicRoleDao.findByName("会议员");
			for(UserBO user : commandUserBos){
				GroupMemberPO memberPO = new GroupMemberPO();
				//关联会议员角色
//				memberPO.setRoleId(memberRole.getId());
//				memberPO.setRoleName(memberRole.getName());
				
				memberPO.setUserId(user.getId());
				memberPO.setUserName(user.getName());
				memberPO.setUserNum(user.getUserNo());
				memberPO.setGroup(group);
				if(user.getFolderId() == null){
					throw new BaseException(StatusCode.FORBIDDEN, memberPO.getUserName() + " 没有组织机构！");
				}
				memberPO.setFolderId(user.getFolderId());
				newMembers.add(memberPO);
				
				//ldap用户，生成一套参数id
				if(queryUtil.isLdapUser(user, folderUserMaps)){
					if(localLayerId == null){
						localLayerId = resourceRemoteService.queryLocalLayerId();
					}
					memberPO.setOriginType(OriginType.OUTER);
					memberPO.setSrcBundleId(memberPO.getUserNum() + "user" + UUID.randomUUID().toString().replace("-", ""));
					memberPO.setSrcLayerId(localLayerId);
					memberPO.setSrcVideoChannelId(ChannelType.VIDEOENCODE1.getChannelId());
					memberPO.setSrcAudioChannelId(ChannelType.AUDIOENCODE1.getChannelId());
					continue;
				}
				
				//遍历bundle
				String encoderId = commonQueryUtil.queryExternalOrLocalEncoderIdFromUserBO(user);
				for(BundlePO bundle : srcBundleEntities){
					if(bundle.getBundleId().equals(encoderId)){
						memberPO.setSrcBundleId(bundle.getBundleId());
						memberPO.setSrcBundleName(bundle.getBundleName());
						memberPO.setSrcBundleType(bundle.getDeviceModel());
						memberPO.setSrcVenusBundleType(bundle.getBundleType());
						memberPO.setSrcLayerId(bundle.getAccessNodeUid());
						break;
					}
				}
				
				//遍历视频通道
				for(ChannelSchemeDTO videoChannel : videoEncode1Channels){
					if(videoChannel.getBundleId().equals(encoderId)){
						memberPO.setSrcVideoChannelId(videoChannel.getChannelId());
						break;
					}
				}			
				
				//遍历音频通道
				for(ChannelSchemeDTO audioChannel : audioEncode1Channels){
					if(audioChannel.getBundleId().equals(encoderId)){
						memberPO.setSrcAudioChannelId(audioChannel.getChannelId());
						break;
					}
				}
			}
			
			//保存以获得member的id
			members.addAll(newMembers);
			commandGroupMemberDao.save(newMembers);
			commandGroupDao.save(group);
			
			List<Long> newMemberIds = new ArrayList<Long>();
			for(GroupMemberPO member : newMembers){
				newMemberIds.add(member.getId());
				if(member.isAdministrator()){
					//建立所有成员到主席的转发（在下边）
					
				}else{
					//建立主席到成员的转发
					CommandGroupForwardPO c2m_forward = new CommandGroupForwardPO(
							ForwardBusinessType.BASIC_COMMAND,
							ExecuteStatus.UNDONE,
							ForwardDstType.USER,
							member.getId(),
							chairmanMember.getId(),
							chairmanMember.getSrcBundleId(),
							chairmanMember.getSrcBundleName(),
							chairmanMember.getSrcVenusBundleType(),
							chairmanMember.getSrcLayerId(),
							chairmanMember.getSrcVideoChannelId(),
							"VenusVideoIn",//videoBaseType,
							chairmanMember.getSrcBundleId(),
							chairmanMember.getSrcBundleName(),
							chairmanMember.getSrcBundleType(),
							chairmanMember.getSrcLayerId(),
							chairmanMember.getSrcAudioChannelId(),
							"VenusAudioIn",//String audioBaseType,
							null,//member.getDstBundleId(),
							null,//member.getDstBundleName(),
							null,//member.getDstBundleType(),
							null,//member.getDstLayerId(),
							null,//member.getDstVideoChannelId(),
							"VenusVideoOut",//String dstVideoBaseType,
							null,//member.getDstAudioChannelId(),
							null,//member.getDstBundleName(),
							null,//member.getDstBundleType(),
							null,//member.getDstLayerId(),
							null,//member.getDstAudioChannelId(),
							"VenusAudioOut",//String dstAudioBaseType,
							creatorUserId,
							g_avtpl.getId(),//Long avTplId,
							currentGear.getId(),//Long gearId,
							DstDeviceType.WEBSITE_PLAYER,
							null,//LiveType type,
							null,//Long osdId,
							null//String osdUsername);
							);
					c2m_forward.setGroup(group);
					newForwards.add(c2m_forward);
					
					//建立成员到主席的转发
					CommandGroupForwardPO m2c_forward = new CommandGroupForwardPO(
							ForwardBusinessType.BASIC_COMMAND,
							ExecuteStatus.UNDONE,
							ForwardDstType.USER,
							chairmanMember.getId(),
							member.getId(),
							member.getSrcBundleId(),
							member.getSrcBundleName(),
							member.getSrcVenusBundleType(),
							member.getSrcLayerId(),
							member.getSrcVideoChannelId(),
							"VenusVideoIn",//videoBaseType,
							member.getSrcBundleId(),
							member.getSrcBundleName(),
							member.getSrcBundleType(),
							member.getSrcLayerId(),
							member.getSrcAudioChannelId(),
							"VenusAudioIn",//String audioBaseType,
							null,//chairmanMember.getDstBundleId(),
							null,//chairmanMember.getDstBundleName(),
							null,//chairmanMember.getDstBundleType(),
							null,//chairmanMember.getDstLayerId(),
							null,//chairmanMember.getDstVideoChannelId(),
							null,//String dstVideoBaseType,
							null,//chairmanMember.getDstAudioChannelId(),
							null,//chairmanMember.getDstBundleName(),
							null,//chairmanMember.getDstBundleType(),
							null,//chairmanMember.getDstLayerId(),
							null,//chairmanMember.getDstAudioChannelId(),
							null,//String dstAudioBaseType,
							creatorUserId,
							g_avtpl.getId(),//Long avTplId,
							currentGear.getId(),//Long gearId,
							DstDeviceType.WEBSITE_PLAYER,
							null,//LiveType type,
							null,//Long osdId,
							null//String osdUsername);
							);
					m2c_forward.setGroup(group);
					newForwards.add(m2c_forward);
					
				}
			}*/
			
			/*//协同或发言人
			Set<GroupMemberPO> cooperateMembers = new HashSet<GroupMemberPO>();
			GroupSpeakType speakType = group.getSpeakType();
			if(GroupSpeakType.DISCUSS.equals(speakType)){
				//讨论模式下，除了主席，所有进会成员都是“发言人”
				for(GroupMemberPO member : members){
					if(member.isAdministrator()) continue;
					if(member.getMemberStatus().equals(MemberStatus.CONNECT)){
						cooperateMembers.add(member);
					}
				}
			}else{
				for(GroupMemberPO member : members){
					if(member.getCooperateStatus().equals(MemberStatus.CONNECT) || member.getCooperateStatus().equals(MemberStatus.CONNECTING)){
						cooperateMembers.add(member);
					}
				}
			}
			
			//生成协同或发言的转发
			for(GroupMemberPO member : newMembers){
				
				if(member.isAdministrator()){
					continue;
				}
				
				for(GroupMemberPO cooperateMember : cooperateMembers){
					
					//避免自己看自己
					if(member.getUuid().equals(cooperateMember.getUuid())){
						continue;
					}
					
					//协同给新成员的转发
					CommandGroupForwardPO c2m_forward = new CommandGroupForwardPO(
							ForwardBusinessType.COOPERATE_COMMAND,
							ExecuteStatus.UNDONE,
							ForwardDstType.USER,
							member.getId(),
							cooperateMember.getId(),
							cooperateMember.getSrcBundleId(),
							cooperateMember.getSrcBundleName(),
							cooperateMember.getSrcVenusBundleType(),
							cooperateMember.getSrcLayerId(),
							cooperateMember.getSrcVideoChannelId(),
							"VenusVideoIn",//videoBaseType,
							cooperateMember.getSrcBundleId(),
							cooperateMember.getSrcBundleName(),
							cooperateMember.getSrcBundleType(),
							cooperateMember.getSrcLayerId(),
							cooperateMember.getSrcAudioChannelId(),
							"VenusAudioIn",//String audioBaseType,
							null,//member.getDstBundleId(),
							null,//member.getDstBundleName(),
							null,//member.getDstBundleType(),
							null,//member.getDstLayerId(),
							null,//member.getDstVideoChannelId(),
							"VenusVideoOut",//String dstVideoBaseType,
							null,//member.getDstAudioChannelId(),
							null,//member.getDstBundleName(),
							null,//member.getDstBundleType(),
							null,//member.getDstLayerId(),
							null,//member.getDstAudioChannelId(),
							"VenusAudioOut",//String dstAudioBaseType,
							group.getUserId(),
							group.getAvtpl().getId(),//g_avtpl.getId(),//Long avTplId,
							currentGear.getId(),//Long gearId,
							DstDeviceType.WEBSITE_PLAYER,
							null,//LiveType type,
							null,//Long osdId,
							null//String osdUsername);
							);
					c2m_forward.setGroup(group);
					newForwards.add(c2m_forward);
					
					//会议模式下，发言人不用看新成员
					if(GroupType.MEETING.equals(businessType)){
						continue;
					}
					
					//新成员给协同成员的转发
					CommandGroupForwardPO m2c_forward = new CommandGroupForwardPO(
							ForwardBusinessType.COOPERATE_COMMAND,
							ExecuteStatus.UNDONE,
							ForwardDstType.USER,
							cooperateMember.getId(),
							member.getId(),
							member.getSrcBundleId(),
							member.getSrcBundleName(),
							member.getSrcVenusBundleType(),
							member.getSrcLayerId(),
							member.getSrcVideoChannelId(),
							"VenusVideoIn",//videoBaseType,
							member.getSrcBundleId(),
							member.getSrcBundleName(),
							member.getSrcBundleType(),
							member.getSrcLayerId(),
							member.getSrcAudioChannelId(),
							"VenusAudioIn",//String audioBaseType,
							null,//cooperateMember.getDstBundleId(),
							null,//cooperateMember.getDstBundleName(),
							null,//cooperateMember.getDstBundleType(),
							null,//cooperateMember.getDstLayerId(),
							null,//cooperateMember.getDstVideoChannelId(),
							"VenusVideoOut",//String dstVideoBaseType,
							null,//cooperateMember.getDstAudioChannelId(),
							null,//cooperateMember.getDstBundleName(),
							null,//cooperateMember.getDstBundleType(),
							null,//cooperateMember.getDstLayerId(),
							null,//cooperateMember.getDstAudioChannelId(),
							"VenusAudioOut",//String dstAudioBaseType,
							group.getUserId(),
							group.getAvtpl().getId(),//g_avtpl.getId(),//Long avTplId,
							currentGear.getId(),//Long gearId,
							DstDeviceType.WEBSITE_PLAYER,
							null,//LiveType type,
							null,//Long osdId,
							null//String osdUsername);
							);
					m2c_forward.setGroup(group);
					newForwards.add(m2c_forward);
				}
			}
			
			group.getForwards().addAll(newForwards);
			
			commandGroupDao.save(group);*/
			
			//这两个变量用来生成message用
			StringBufferWrapper newMembersNames = new StringBufferWrapper();
			JSONArray newMemberIdsJSONArray = new JSONArray();
			for(GroupMemberPO newMember : newMembers){
				newMembersNames.append(newMember.getName()).append("，");
				newMemberIdsJSONArray.add(newMember.getId().toString());
			}
			newMembersNames.append("被邀请到 ").append(group.getName());//.append(" 会议");			
			List<Long> consumeIds = new ArrayList<Long>();
			List<MessageSendCacheBO> messageCaches = new ArrayList<MessageSendCacheBO>();
			
			//此处不呼叫主席的播放器，待成员接听后通过forward呼叫	
			if(!group.getStatus().equals(GroupStatus.STOP)){
				
				//新的在线的成员自动上线，还有“进入”的成员
				List<GroupMemberPO> loginNewAndEnterMembers = new ArrayList<GroupMemberPO>();
				
				//发消息给新成员（这里不用考虑专向指挥）
				if(!autoEnter){
					for(GroupMemberPO member : newMembers){
						
						if(OriginType.OUTER.equals(group.getOriginType())){
							continue;
						}
						
						if(!GroupMemberType.MEMBER_USER.equals(member.getGroupMemberType())){
							continue;
						}
						
						String businessTypeStr = null;
						if(GroupType.MEETING.equals(businessType)){
							businessTypeStr = "meetingStart";
						}else{
							businessTypeStr = "commandStart";
						}
						JSONObject message = new JSONObject();
						message.put("businessType", businessTypeStr);
						message.put("fromUserId", group.getUserName());
						message.put("fromUserName", chairmanMember.getName());
						message.put("businessId", group.getId().toString());
						message.put("businessInfo", "接受到 " + group.getName() + " 邀请，主席：" + chairmanMember.getName() + "，是否进入？");
						
						//发送消息
						messageCaches.add(new MessageSendCacheBO(Long.parseLong(member.getOriginId()), message.toJSONString(), WebsocketMessageType.COMMAND));
					}
				}else{
					//自动接听
					for(GroupMemberPO newMember : newMembers){
						if(newMember.getIsAdministrator()) continue;
//						UserBO commandUserBo = queryUtil.queryUserById(commandUserBos, newMember.getUserId());
//						if(commandUserBo.isLogined()){//TODO:该成员如果对应终端登录，则进会
							loginNewAndEnterMembers.add(newMember);
//						}else{
//							//没在线的，置为DISCONNECT。不用按照“拒绝”处理
//							newMember.setMemberStatus(MemberStatus.DISCONNECT);
//						}
					}
					
					loginNewAndEnterMembers.addAll(enterMembers);				
					membersResponse(group, loginNewAndEnterMembers, null);
				}
				
//				//把新成员/进入的成员加入讨论
//				if(GroupSpeakType.DISCUSS.equals(speakType)){
//					commandMeetingSpeakServiceImpl.speakStart(group, loginNewAndEnterMembers, 2);
//				}
			}
			
			//级联，必须在membersResponse之后，这样MemberStatus才是对的
			if(!OriginType.OUTER.equals(group.getOriginType())){
				/*
				//停会则 groupUpdate
				if(GroupStatus.STOP.equals(group.getStatus())){
					if(GroupType.BASIC.equals(businessType)){
						GroupBO groupBO = commandCascadeUtil.updateCommand(group);
						commandCascadeService.update(groupBO);						
					}else if(GroupType.MEETING.equals(businessType)){
						GroupBO groupBO = commandCascadeUtil.updateMeeting(group);
						conferenceCascadeService.update(groupBO);			
					}
				}
				//开会中则 maddinc maddfull
				else{					
					List<GroupMemberPO> newAndEnterMembers = new ArrayList<GroupMemberPO>();
					newAndEnterMembers.addAll(newMembers);
					newAndEnterMembers.addAll(enterMembers);
					
					//记录新加入的用户列表
					List<MinfoBO> newMemberInfos = commandCascadeUtil.generateMinfoBOList(newAndEnterMembers, chairmanMember);
					//得到位于新节点上的用户列表
					List<MinfoBO> newNodeMemberInfos = commandCascadeUtil.filterAddedNodeMinfo(oldMemberInfos, newMemberInfos);
					
					if(GroupType.BASIC.equals(businessType)){
						
						GroupBO groupBO = commandCascadeUtil.joinCommand(group, oldMemberInfos, newAndEnterMembers);
						commandCascadeService.join(groupBO);
						
						//如果有新节点，则要发maddfull
						if(newNodeMemberInfos.size() > 0){
							GroupBO maddfullBO = commandCascadeUtil.maddfullCommand(group, newNodeMemberInfos);
							commandCascadeService.info(maddfullBO);
							log.info(newNodeMemberInfos.size() + "个成员所在的节点新参与指挥，全量同步该指挥：" + group.getName());
						}
						
					}else if(GroupType.MEETING.equals(businessType)){
						
						GroupBO groupBO = commandCascadeUtil.joinMeeting(group, oldMemberInfos, newAndEnterMembers);
						conferenceCascadeService.join(groupBO);
						
						//如果有新节点，则要发maddfull
						if(newNodeMemberInfos.size() > 0){
							GroupBO maddfullBO = commandCascadeUtil.maddfullMeeting(group, newNodeMemberInfos);
							conferenceCascadeService.info(maddfullBO);
							log.info(newNodeMemberInfos.size() + "个成员所在的节点新参与会议，全量同步该会议：" + group.getName());
						}
					}
				}*/
			}
			
			//发消息，主要是“邀请”消息，自动接听则没有
			for(MessageSendCacheBO cache : messageCaches){
				WebsocketMessageVO ws = websocketMessageService.send(cache.getUserId(), cache.getMessage(), cache.getType(), cache.getFromUserId(), cache.getFromUsername());
				consumeIds.add(ws.getId());
			}
			websocketMessageService.consumeAll(consumeIds);
			
			//呼叫主席（此处不呼叫主席的播放器，待成员接听后通过forward呼叫，测试是否可行）
//			List<GroupMemberPO> acceptMembers = new ArrayList<GroupMemberPO>();
//			acceptMembers.add(chairman);
//			membersResponse(group, acceptMembers, null);
			operationLogService.send(self.getNickname(), "成员加入", self.getNickname()
					+ "添加成员originIds:" + newOriginIdList.toString()
					+ "进入成员originIds:" + enterOriginIdList.toString());
			return chairSplits;			
		}
	}
	
	/**
	 * 
	 * 删除成员/成员退出<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月12日 下午6:14:57
	 * @param groupId
	 * @param userIdList
	 * @param mode 0为主席同意的主动退出（列表和转发都保留），2为主席强退，删人，1为保留字段
	 * @return 1删人时给主席返回chairSplits；0退出时给退出成员返回exitMemberSplits；后续优化
	 * @throws Exception
	 */
	public Object removeMembers2(Long groupId, List<Long> memberIdList, int mode) throws Exception{
		UserVO user = userQuery.current();
		//“重复退出会再次挂断编码器”已改好
		
		if(groupId==null || groupId.equals("")){
			log.info("退出或删人，会议id有误");
			return new JSONArray();
		}
		
		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
			
			GroupPO group = groupDao.findOne(groupId);
			
			if(group == null){
				log.info("进行退出/删人操作的会议不存在，id：" + groupId);
				return new JSONArray();
			}
			
			//会议停止状态下不需要“退出”
			if(mode == 0){
				if(group.getStatus().equals(GroupStatus.STOP)){
//					throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已停止，不需退出，id: " + group.getId());
					return new JSONArray();
				}
			}
			
//			//防止专向指挥中调用此方法
//			if(group.getType().equals(GroupType.SECRET)){
//				throw new BaseException(StatusCode.FORBIDDEN, "正在专项，不能退出，只能“停止”");
//			}
			
			JSONArray chairSplits = new JSONArray();
			JSONArray exitMemberSplits = new JSONArray();
			List<Long> consumeIds = new ArrayList<Long>();
			List<MessageSendCacheBO> messageCaches = new ArrayList<MessageSendCacheBO>();
			
			BusinessType businessType = group.getBusinessType();
			List<GroupMemberPO> members = groupMemberDao.findByGroupId(groupId);
			GroupMemberPO chairmanMember = tetrisBvcQueryUtil.queryChairmanMember(members);
			
			//记录删人之前的用户列表
//			List<MinfoBO> orgMemberInfos = commandCascadeUtil.generateMinfoBOList(members);
			
			if(memberIdList.contains(chairmanMember.getId())){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "不能删除主席");
			}
			
			List<GroupMemberPO> removeMembers = new ArrayList<GroupMemberPO>();
//			List<Long> removeMemberIds = new ArrayList<Long>();
			for(GroupMemberPO member : members){
				if(memberIdList.contains(member.getId())){
					removeMembers.add(member);
//					removeMemberIds.add(member.getId());
				}
			}
			
			/*
			//以这些成员为源和目的的转发
			Set<CommandGroupForwardPO> needDelForwards = commandCommonUtil.queryForwardsByMemberIds(forwards, removeMemberIds, null, null);
			
			//以这些成员为目的的转发点播
			List<CommandGroupForwardDemandPO> needDelDemands = commandCommonUtil.queryForwardDemandsByDstmemberIds(demands, removeMemberIds, null, null);
			//已成功，需要关闭编码器的转发点播
			List<CommandGroupForwardDemandPO> needDelDemandsForEncoder = commandCommonUtil.queryForwardDemandsByDstmemberIds(demands, removeMemberIds, null, ForwardDemandStatus.DONE);
			//直接删除转发点播
			demands.removeAll(needDelDemands);*/
			
			//这两个变量用来生成message用
			StringBufferWrapper removeMembersNames = new StringBufferWrapper();
			JSONArray removeMemberIdsJSONArray = new JSONArray();
			for(GroupMemberPO removeMember : removeMembers){
				removeMembersNames.append(removeMember.getName()).append("，");
				removeMemberIdsJSONArray.add(removeMember.getId().toString());
			}
			removeMembersNames.append("被从 ").append(group.getName()).append(" 中移除");
			
			//释放这些退出或删除成员的播放器，同时如果是删人就给被删的成员发消息
//			List<CommandGroupUserPlayerPO> needFreePlayers = new ArrayList<CommandGroupUserPlayerPO>();
//			List<CommandGroupUserPlayerPO> playFilePlayers = new ArrayList<CommandGroupUserPlayerPO>();
			//进会的成员，需要关闭编码
			List<GroupMemberPO> connectRemoveMembers = new ArrayList<GroupMemberPO>();
			for(GroupMemberPO removeMember : removeMembers){
				
				if(GroupMemberStatus.CONNECT.equals(removeMember.getGroupMemberStatus())){
					connectRemoveMembers.add(removeMember);
				}
				
				//会议进行中，统计播放器，给退出成员发消息
				if(!group.getStatus().equals(GroupStatus.STOP)){
					
//					//如果操作人在本系统
//					if(!OriginType.OUTER.equals(chairmanMember.getOriginType())){
						
						//如果退出人在本系统，统计它的播放器，websocket通知
						if(!OriginType.OUTER.equals(removeMember.getOriginType())){
							JSONArray splits = new JSONArray();
							
							JSONObject message = new JSONObject();
							if(mode == 2){
								message.put("businessInfo", "您已被移出 " + group.getName());
								message.put("businessType", "commandMemberDelete");
							}else{
								message.put("businessInfo", group.getName() + " 主席同意，您已退出");
								message.put("businessType", "exitApplyAgree");
							}
							message.put("businessId", group.getId().toString());
//							message.put("memberIds", removeMemberIdsJSONArray);
							message.put("splits", splits);
							if(removeMember.getGroupMemberType().equals(GroupMemberType.MEMBER_USER)){
								messageCaches.add(new MessageSendCacheBO(Long.parseLong(removeMember.getOriginId()), message.toJSONString(), WebsocketMessageType.COMMAND));								
							}
						}
						
						//如果业务进行中，且操作人在本系统
						if(!OriginType.OUTER.equals(chairmanMember.getOriginType())
								&& !GroupStatus.STOP.equals(group.getStatus())){
							
							//如果是申请退出被同意
							if(mode == 0){							
							
								//如果退出人在外部系统，级联通知发言人所在系统（该通知只发送，收到后不处理，通过“成员主动退出通知”来处理）							
								if(OriginType.OUTER.equals(removeMember.getOriginType())){
									if(BusinessType.COMMAND.equals(businessType)){
//										GroupBO groupBO = commandCascadeUtil.exitCommandResponse(group, removeMember, "1");
//										commandCascadeService.exitResponse(groupBO);
									}else if(BusinessType.MEETING_QT.equals(businessType)){
//										GroupBO groupBO = commandCascadeUtil.exitMeetingResponse(group, removeMember, "1");
//										conferenceCascadeService.exitResponse(groupBO);
									}
								}
								
								// “成员主动退出通知”给所有其它系统
								if(BusinessType.COMMAND.equals(businessType)){
//									GroupBO groupBO = commandCascadeUtil.exitCommand(group, removeMember);
//									commandCascadeService.exit(groupBO);
								}else if(BusinessType.MEETING_QT.equals(businessType)){
//									GroupBO groupBO = commandCascadeUtil.exitMeeting(group, removeMember);
//									conferenceCascadeService.exit(groupBO);
								}
							}
							//如果是主席强退
							else{
								// “主席强制退出通知”给所有其它系统
								if(BusinessType.COMMAND.equals(businessType)){
//									GroupBO groupBO = commandCascadeUtil.exitCommand(group, removeMember);
//									commandCascadeService.kikout(groupBO);
								}else if(BusinessType.MEETING_QT.equals(businessType)){
//									GroupBO groupBO = commandCascadeUtil.exitMeeting(group, removeMember);
//									conferenceCascadeService.kikout(groupBO);
								}
							}
						}
//					}
				}
			}
			
			//发送成员下线消息，这里默认认为removeMembers只有一个元素
			JSONObject message = new JSONObject();
			if(mode ==2){
				message.put("businessInfo", removeMembersNames.toString());
			}else{
				message.put("businessInfo", removeMembers.get(0).getName() + " 成员退出");
			}
			message.put("businessType", "commandMemberOffline");
			message.put("businessId", group.getId().toString());
			message.put("memberId", removeMembers.get(0).getId().toString());
			message.put("splits", new JSONArray());
			if(!group.getStatus().equals(GroupStatus.STOP)){
							
				for(GroupMemberPO member : members){
					
					if(OriginType.OUTER.equals(member.getOriginType())){
						continue;
					}
					
					if(!GroupMemberType.MEMBER_USER.equals(member.getGroupMemberType())){
						continue;
					}

					//不给退出和删除的成员发
					if(memberIdList.contains(member.getId())){
						continue;
					}
						
					//不给主席发
					if(member.getIsAdministrator()){
						continue;
					}
						
					messageCaches.add(new MessageSendCacheBO(Long.parseLong(member.getOriginId()), message.toJSONString(), WebsocketMessageType.COMMAND));
				}
			}
			
			//会议进行中，发协议（删转发协议不用发，通过挂断播放器来删）
			if(!group.getStatus().equals(GroupStatus.STOP)){
				
				StringBufferWrapper description = new StringBufferWrapper().append(group.getName());
				if(mode == 0){//退出
					description.append(removeMembers.get(0).getName()).append(" 成员退出");
				}else if(mode == 2){//删人
					description = removeMembersNames;
				}
				
				//关闭编码通道
				List<SourceBO> sourceBOs = agendaService.obtainSource(connectRemoveMembers, group.getId().toString(), BusinessInfoType.BASIC_COMMAND);
				CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();
				LogicBO logic = closeEncoder(sourceBOs, codec, -1L);
				executeBusiness.execute(logic, group.getName() + " " + description);
								
				//录制更新
//				LogicBO logicRecord = commandRecordServiceImpl.update(group.getUserId(), group, 1, false);
//				logic.merge(logicRecord);
//				
//				ExecuteBusinessReturnBO returnBO = executeBusiness.execute(logic, description.toString());
//				commandRecordServiceImpl.saveStoreInfo(returnBO, group.getId());
				
				//停止所有给这些成员的媒体转发
				List<GroupDemandPO> demands = groupDemandDao.findByDstMemberIdIn(memberIdList);
				groupDemandService.stopDemands(group, demands, false);
				
				//给退出成员解绑所有的角色。后续改成批量
				for(GroupMemberPO member : connectRemoveMembers){
					List<GroupMemberRolePermissionPO> ps = groupMemberRolePermissionDao.findByGroupMemberId(member.getId());
					List<Long> removeRoleIds = businessCommonService.obtainGroupMemberRolePermissionPOIds(ps);
					agendaService.modifyMemberRole(groupId, member.getId(), null, removeRoleIds);
				}
			}
			
			if(mode == 0){
				//成员主动退出
				for(GroupMemberPO member : connectRemoveMembers){
					member.setGroupMemberStatus(GroupMemberStatus.DISCONNECT);
				}
				groupMemberDao.save(connectRemoveMembers);
			}else if(mode == 2){
				//主席强退，删人
				groupMemberDao.deleteInBatch(removeMembers);
			}
			
			//级联：如果有老节点，则给它停会删会；业务停止时给剩余成员的所有节点发update
			if(!OriginType.OUTER.equals(group.getOriginType())){
				/*
				//最终的用户列表
				List<MinfoBO> finalMemberInfos = commandCascadeUtil.generateMinfoBOList(members);
				//不再参与业务的节点上的用户列表
				List<MinfoBO> oldNodeMemberInfos = commandCascadeUtil.filterAddedNodeMinfo(finalMemberInfos, orgMemberInfos);
				
				if(GroupType.BASIC.equals(groupType)){
					
					//业务停止时给剩余成员的所有节点发update
					if(group.getStatus().equals(GroupStatus.STOP)){
						GroupBO groupBO = commandCascadeUtil.updateCommand(group);
						commandCascadeService.update(groupBO);
					}
					
					//如果有老节点，则给它停会删会
					if(oldNodeMemberInfos.size() > 0){
						
						if(!group.getStatus().equals(GroupStatus.STOP)){
							//停指挥
							Thread.sleep(300);//延时一下，确保其它节点删人操作已完成
							GroupBO groupBO1 = commandCascadeUtil.stopCommand(group);
							groupBO1.setMlist(oldNodeMemberInfos);
							commandCascadeService.stop(groupBO1);
						}
						
						//删除
						Thread.sleep(300);//延时一下，确保其它节点上一步操作已完成
						GroupBO groupBO = commandCascadeUtil.deleteCommand(group);
						groupBO.setMlist(oldNodeMemberInfos);
						commandCascadeService.delete(groupBO);
						log.info(oldNodeMemberInfos.size() + "个成员所在的节点不再参与指挥，已删除节点上的指挥：" + group.getName());
					}					
				}else if(GroupType.MEETING.equals(groupType)){
					
					//业务停止时给剩余成员的所有节点发update
					if(group.getStatus().equals(GroupStatus.STOP)){						
						GroupBO groupBO = commandCascadeUtil.updateMeeting(group);
						conferenceCascadeService.update(groupBO);
					}
			
					//如果有老节点，则给它停会删会
					if(oldNodeMemberInfos.size() > 0){
						
						if(!group.getStatus().equals(GroupStatus.STOP)){
							//停会
							Thread.sleep(300);//延时一下，确保其它节点删人操作已完成
							GroupBO groupBO2 = commandCascadeUtil.stopMeeting(group);
							groupBO2.setMlist(oldNodeMemberInfos);
							conferenceCascadeService.stop(groupBO2);
						}
						
						//删除
						Thread.sleep(300);//延时一下，确保其它节点上一步操作已完成
						GroupBO groupBO = commandCascadeUtil.deleteMeeting(group);
						groupBO.setMlist(oldNodeMemberInfos);
						conferenceCascadeService.delete(groupBO);
						log.info(oldNodeMemberInfos.size() + "个成员所在的节点不再参与会议，已删除节点上的会议：" + group.getName());
					}					
				}*/
			}

			//发消息
			for(MessageSendCacheBO cache : messageCaches){
				WebsocketMessageVO ws = websocketMessageService.send(cache.getUserId(), cache.getMessage(), cache.getType(), cache.getFromUserId(), cache.getFromUsername());
				consumeIds.add(ws.getId());
			}
			websocketMessageService.consumeAll(consumeIds);
			operationLogService.send(user.getNickname(), "成员退出", user.getNickname() + "成员退出groupId:" + groupId + "memberIdList:" + memberIdList.toString());
			//所有情况都给主席返回chairSplits
			//if(mode == 0) return exitMemberSplits;
			return chairSplits;
		}
	}

	/*public void exitApply(Long userId, Long groupId) throws Exception{
		
		UserVO user = userQuery.current();
		if(groupId==null || groupId.equals("")){
			log.info("申请退出，会议id有误");
			return;
		}
		
		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
			
			CommandGroupPO group = commandGroupDao.findOne(groupId);
			GroupType groupType = group.getType();
			
			if(group.getStatus().equals(GroupStatus.STOP)){
				if(!OriginType.OUTER.equals(group.getOriginType())){
					throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已停止");
				}else{
					return;
				}
			}
			
			if(group.getUserId().equals(userId)){
				throw new BaseException(StatusCode.FORBIDDEN, "主席不能退出");
			}
			
			List<GroupMemberPO> members = group.getMembers();
			GroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
			GroupMemberPO exitMember = commandCommonUtil.queryMemberByUserId(members, userId);
			
			//如果主席和申请人都不在该系统，则不需要处理（正常不会出现）
			if(OriginType.OUTER.equals(chairmanMember.getOriginType())
					&& OriginType.OUTER.equals(exitMember.getOriginType())){
				log.info("主席和申请退出的人都不是该系统用户，主席：" + chairmanMember.getUserName() + " 退出用户：" + exitMember.getUserName());
				return;
			}
			
			if(exitMember.getMemberStatus().equals(MemberStatus.DISCONNECT)){
				throw new BaseException(StatusCode.FORBIDDEN, "您已经退出");
			}
			
			//级联
			if(!OriginType.OUTER.equals(chairmanMember.getOriginType())){
				//主席在该系统
				JSONObject message = new JSONObject();
				message.put("businessType", "exitApply");
				message.put("businessInfo", exitMember.getUserName() + "申请退出" + group.getName());
				message.put("businessId", group.getId().toString() + "-" + exitMember.getUserId());
				
				WebsocketMessageVO ws = websocketMessageService.send(chairmanMember.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND);
				websocketMessageService.consume(ws.getId());
			}else{
				//主席在外部系统（那么申请人在该系统）
				if(GroupType.BASIC.equals(groupType)){
					GroupBO groupBO = commandCascadeUtil.exitCommandRequest(group, exitMember);
					commandCascadeService.exitRequest(groupBO);
				}else if(GroupType.MEETING.equals(groupType)){
					GroupBO groupBO = commandCascadeUtil.exitMeetingRequest(group, exitMember);
					conferenceCascadeService.exitRequest(groupBO);
				}
			}
			
			log.info(group.getName() + "申请退出");
		}
		operationLogService.send(user.getNickname(), "申请退出", user.getNickname() + "申请退出groupId:" + groupId);
	}*/
	
	/*public void exitApplyDisagree(Long userId, Long groupId, List<Long> userIds) throws Exception{
		UserVO user = userQuery.current();
		
		if(groupId==null || groupId.equals("")){
			log.info("拒绝成员退出，会议id有误");
			return;
		}
		
		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
			
			CommandGroupPO group = commandGroupDao.findOne(groupId);
			GroupType groupType = group.getType();
			
			if(group.getStatus().equals(GroupStatus.STOP) || userIds.size()==0){
				return;
			}
			
			List<Long> consumeIds = new ArrayList<Long>();
			List<MessageSendCacheBO> messageCaches = new ArrayList<MessageSendCacheBO>();			
			List<GroupMemberPO> members = group.getMembers();
			GroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
			List<GroupMemberPO> exitMembers = commandCommonUtil.queryMembersByUserIds(members, userIds);
			JSONObject message = new JSONObject();
			message.put("businessType", "exitApplyDisagree");
			message.put("businessInfo", "主席不同意您退出");
			message.put("businessId", group.getId().toString());
			for(GroupMemberPO exitMember : exitMembers){
				
				//如果退出人在本系统，websocket通知
				if(!OriginType.OUTER.equals(exitMember.getOriginType())){
					messageCaches.add(new MessageSendCacheBO(exitMember.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND));								
				}
				
				//如果操作人在本系统
				if(!OriginType.OUTER.equals(chairmanMember.getOriginType())){
					
					//如果退出人在外部系统，级联通知
					if(OriginType.OUTER.equals(exitMember.getOriginType())){
						if(GroupType.BASIC.equals(groupType)){
							GroupBO groupBO = commandCascadeUtil.exitCommandResponse(group, exitMember, "0");
							commandCascadeService.exitResponse(groupBO);
						}else if(GroupType.MEETING.equals(groupType)){
							GroupBO groupBO = commandCascadeUtil.exitMeetingResponse(group, exitMember, "0");
							conferenceCascadeService.exitResponse(groupBO);
						}
					}
				}
			}			
			
			for(MessageSendCacheBO cache : messageCaches){
				WebsocketMessageVO ws = websocketMessageService.send(cache.getUserId(), cache.getMessage(), cache.getType());
				consumeIds.add(ws.getId());
			}
			websocketMessageService.consumeAll(consumeIds);
			
			log.info(group.getName() + " 主席拒绝了 " + exitMembers.get(0).getUserName() + " 等人退出");
		}
		operationLogService.send(user.getNickname(), "拒绝申请退出", user.getNickname() + "拒绝申请退出groupId:" + groupId + ", userIds" + userIds.toString());
	}*/
	
	/**
	 * 批量处理成员的“接听”和“拒绝”<br/>
	 * <p>注意不能选择自己看自己的播放器，例如主席看主席</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月24日 上午11:13:43
	 * @param group
	 * @param acceptMembers
	 * @param refuseMembers
	 * @throws Exception
	 */
	private void membersResponse(GroupPO group, List<GroupMemberPO> allMembers, List<GroupMemberPO> acceptMembers) throws Exception{
		
		if(null == acceptMembers) acceptMembers = new ArrayList<GroupMemberPO>();
		List<Long> consumeIds = new ArrayList<Long>();
		List<MessageSendCacheBO> messageCaches = new ArrayList<MessageSendCacheBO>();
		
		//考虑如果停会之后执行，有没有问题
		
		//判断是否在进行
		if(GroupStatus.STOP.equals(group.getStatus())) {
			return;
		}
		
		//处理同意用户，呼叫转发目标成员的播放器
		GroupMemberPO chairmanMember = tetrisBvcQueryUtil.queryChairmanMember(allMembers);
		List<Long> acceptMemberIds = new ArrayList<Long>();
//		List<CommandGroupUserPlayerPO> allPlayers = new ArrayList<CommandGroupUserPlayerPO>();
		
		List<String> acceptMemberNamesList = businessCommonService.obtainMemberNames(acceptMembers);
		
		//自动接听：给新进的人发消息，通知其开会。在会议开启时，也会给主席发，通知split信息。【专向指挥除外】
		if(autoEnter && !group.getBusinessType().equals(GroupType.SECRET)){
			for(GroupMemberPO acceptMember : acceptMembers){
				
				if(OriginType.OUTER.equals(acceptMember.getOriginType())){
					continue;
				}
				
				JSONObject message = new JSONObject();
				message.put("id", group.getId());
				message.put("name", group.getName());
				message.put("status", group.getStatus().getCode());
				message.put("commander", chairmanMember.getOriginId());
				message.put("creator", chairmanMember.getOriginId());
				message.put("splits", new JSONArray());
				message.put("businessId", group.getId().toString());
				String businessType = null;
				if(BusinessType.MEETING_QT.equals(group.getBusinessType())){
					businessType = "meetingStartNow";//自动接听
				}else{
					businessType = "commandStartNow";//自动接听
				}
				message.put("businessType", businessType);
				message.put("businessInfo", group.getName() + " 开始了，主席：" + chairmanMember.getName());
				messageCaches.add(new MessageSendCacheBO(Long.parseLong(acceptMember.getOriginId()), message.toJSONString(), WebsocketMessageType.COMMAND));
			}
		}		
		
		//给已经进会的人发送消息
		String acceptMemberNames = StringUtils.join(acceptMemberNamesList.toArray(), ",");
		if(acceptMembers.size() > 0){
			for(GroupMemberPO member : allMembers){
				
				if(OriginType.OUTER.equals(member.getOriginType())){
					continue;
				}
				
				if(!GroupMemberStatus.CONNECT.equals(member.getGroupMemberStatus())){
					continue;
				}
				
				if(GroupMemberType.MEMBER_USER.equals(member.getGroupMemberType())){
					continue;
				}
				
				//接听成员有主席时（通常是开启会议时），不给主席发
				if(acceptMemberIds.contains(chairmanMember.getId()) && member.getIsAdministrator()){
					continue;
				}
				
				JSONObject message = new JSONObject();
				message.put("businessType", "commandMemberOnline");
				message.put("businessId", group.getId().toString());
				message.put("businessInfo", acceptMemberNames + " 进入" + group.getName());
				message.put("splits", new JSONArray());
				messageCaches.add(new MessageSendCacheBO(Long.parseLong(member.getOriginId()), message.toJSONString(), WebsocketMessageType.COMMAND));
			}
		}
		
		//发完消息再把成员状态置为CONNECT
		for(GroupMemberPO acceptMember : acceptMembers){
			acceptMember.setGroupMemberStatus(GroupMemberStatus.CONNECT);
		}		
		groupMemberDao.save(acceptMembers);
		
//		//查询接听用户的转发：源和目的成员都CONNECT的，且状态UNDONE的，生成logic.forwardSet
//		Set<CommandGroupForwardPO> relativeForwards = commandCommonUtil.queryForwardsByMemberIds(forwards, acceptMemberIds, null, ExecuteStatus.UNDONE);
//		Set<CommandGroupForwardPO> needForwards = commandCommonUtil.queryForwardsReadyAndCanBeDone(members, relativeForwards);
//		
//		for(CommandGroupForwardPO needForward : needForwards){
//			needForward.setExecuteStatus(ExecuteStatus.DONE);
//		}
//		
//		commandGroupDao.save(group);
		
		List<SourceBO> sourceBOs = agendaService.obtainSource(acceptMembers, group.getId().toString(), BusinessInfoType.BASIC_COMMAND);
		CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();
		LogicBO logic = openEncoder(sourceBOs, codec, -1L);
		
		//执行logic，打开编码通道
		executeBusiness.execute(logic, group.getName() + " 会议成员进会，打开编码");
		
		//生成connectBundle和disconnectBundle，携带转发信息
//		CommandGroupAvtplGearsPO currentGear = commandCommonUtil.queryCurrentGear(group);
//		CodecParamBO codec = new CodecParamBO().set(group.getAvtpl(), currentGear);
//		LogicBO logic = openBundle(acceptMembers, null, allPlayers, needForwards, null, codec, chairmanMember.getUserNum());
//		LogicBO logicDis = closeBundle(null, null, needFreePlayers, codec, chairmanMember.getUserNum());
//		LogicBO logicCastDevice = commandCastServiceImpl.openBundleCastDevice(null, null, needForwards, null, null, null, codec, group.getUserId());
//		logic.merge(logicDis);
//		logic.merge(logicCastDevice);
		
		//授予角色
		RolePO memberRole = null;//TODO
		List<GroupMemberRolePermissionPO> ps = new ArrayList<GroupMemberRolePermissionPO>();
		for(GroupMemberPO acceptMember : acceptMembers){
			List<Long> addRoleIds = new ArrayListWrapper<Long>().add(memberRole.getId()).getList();
			if(acceptMember.getIsAdministrator()){
				RolePO chairmanRolePO = null;//TODO
				addRoleIds.add(chairmanRolePO.getId());
			}
			agendaService.modifyMemberRole(group.getId(), acceptMember.getId(), null, null);
		}
		
		
		
		//停止其它业务观看专向会议的2个成员，在 CommandSecretServiceImpl.accept() 中
		
		//录制更新
//		LogicBO logicRecord = commandRecordServiceImpl.update(group.getUserId(), group, 1, false);
//		logic.merge(logicRecord);
//		
//		ExecuteBusinessReturnBO returnBO = executeBusiness.execute(logic, group.getName() + " 会议成员接听和拒绝");
//		commandRecordServiceImpl.saveStoreInfo(returnBO, group.getId());

		//发消息
		for(MessageSendCacheBO cache : messageCaches){
			WebsocketMessageVO ws = websocketMessageService.send(cache.getUserId(), cache.getMessage(), cache.getType(), cache.getFromUserId(), cache.getFromUsername());
			consumeIds.add(ws.getId());
		}
		websocketMessageService.consumeAll(consumeIds);
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
	 */
	/*public LogicBO startGroupForwards(CommandGroupPO group, boolean doPersistence, boolean doProtocol) throws Exception{
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
	 */
	/*public void startAllGroupForwards(List<Long> exceptGroupIds, boolean doPersistence, boolean doProtocol) throws Exception{
		
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
	 */
	/*public void stopAllGroupForwardsBySrcMemberIds(List<Long> exceptGroupIds, List<Long> srcUserIds, boolean doPersistence, boolean doProtocol) throws Exception{
		
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
	
	private List<GroupMemberPO> generateMembers(Long groupId, List<MemberTerminalBO> memberTerminalBOs, MemberTerminalBO chairmanBO){
			
			List<Long> userIdList = new ArrayList<Long>();
			List<String> memberBundleIds = new ArrayList<String>();
			for(MemberTerminalBO userTerminalBO : memberTerminalBOs){
				GroupMemberType groupMemberType = userTerminalBO.getGroupMemberType();
				if(GroupMemberType.MEMBER_USER.equals(groupMemberType)){
					userIdList.add(Long.parseLong(userTerminalBO.getOriginId()));
				}else if(GroupMemberType.MEMBER_DEVICE.equals(groupMemberType)){
					memberBundleIds.add(userTerminalBO.getOriginId());
				}
			}
			
			List<BundlePO> memberBundles = resourceBundleDao.findByBundleIds(memberBundleIds);
			String userIdListStr = StringUtils.join(userIdList.toArray(), ",");
			List<UserBO> memberUserBos = resourceService.queryUserListByIds(userIdListStr, TerminalType.QT_ZK);
			if(memberUserBos == null) memberUserBos = new ArrayList<UserBO>();
			List<FolderPO> allFolders = resourceService.queryAllFolders();
			
			List<FolderUserMap> folderUserMaps = folderUserMapDao.findByUserIdIn(userIdList);
			String localLayerId = null;
			
			
			//从List<UserBO>取出bundleId列表，注意判空；给UserBO中的folderId赋值
			for(UserBO user : memberUserBos){
				for(FolderPO folder : allFolders){
					if(folder.getUuid().equals(user.getFolderUuid())){
						user.setFolderId(folder.getId());
						break;
					}
				}
			}
			
			List<GroupMemberPO> members = new ArrayList<GroupMemberPO>();
	//		List<PageTaskPO> tasks = new ArrayList<PageTaskPO>();
	//		GroupMemberPO chairmanMember = null;
			
			for(MemberTerminalBO memberTerminalBO : memberTerminalBOs){
				GroupMemberPO memberPO = new GroupMemberPO();
				if(memberTerminalBO.equals(chairmanBO)){
					memberPO.setIsAdministrator(true);
	//				chairmanMember = memberPO;
					//关联主席角色
				}else{
					//关联会议员角色
				}
							
				GroupMemberType groupMemberType = memberTerminalBO.getGroupMemberType();
				memberPO.setGroupMemberType(groupMemberType);
				memberPO.setOriginId(memberTerminalBO.getOriginId());
				memberPO.setTerminalId(memberTerminalBO.getTerminalId());
				
				if(GroupMemberType.MEMBER_USER.equals(groupMemberType)){
					UserBO user = queryUtil.queryUserById(memberUserBos, Long.parseLong(memberTerminalBO.getOriginId()));
					if(user == null) continue;
					memberPO.setName(user.getName());
					memberPO.setFolderId(user.getFolderId());
					if(queryUtil.isLdapUser(user, folderUserMaps)){
						memberPO.setOriginType(OriginType.OUTER);
	//					continue;
					}				
				}else if(GroupMemberType.MEMBER_DEVICE.equals(groupMemberType)){
					BundlePO bundle = queryUtil.queryBundlePOByBundleId(memberBundles, memberTerminalBO.getOriginId());
					if(bundle == null) continue;
					memberPO.setName(bundle.getBundleName());
					memberPO.setFolderId(bundle.getFolderId());
					if(queryUtil.isLdapBundle(bundle)){
						memberPO.setOriginType(OriginType.OUTER);
	//					continue;
					}
				}
				
				memberPO.setGroupId(groupId);
				members.add(memberPO);
			}
			return members;
		}

	/**
	 * 打开编码器<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月24日 下午1:16:42
	 * @param videoAudioMap
	 * @param codec
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public LogicBO openEncoder(
			List<SourceBO> sourceBOs,
			CodecParamBO codec,
			Long userId) throws Exception{
		
		LogicBO logic = new LogicBO().setUserId(userId.toString())
				 			 		 .setConnectBundle(new ArrayList<ConnectBundleBO>())
				 			 		 .setPass_by(new ArrayList<PassByBO>());
		
		for(SourceBO sourceBO : sourceBOs){
			ChannelSchemeDTO video = sourceBO.getVideoSource();
			BundlePO bundlePO = bundleDao.findByBundleId(video.getBundleId());
			ConnectBundleBO connectEncoderBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
					            .setOperateType(ConnectBundleBO.OPERATE_TYPE)
							    .setLock_type("write")
							    .setBundleId(video.getBundleId())
							    .setLayerId(bundlePO.getAccessNodeUid())
							    .setBundle_type(bundlePO.getBundleType());
			ConnectBO connectEncoderVideoChannel = new ConnectBO().setChannelId(video.getChannelId())
					      .setChannel_status("Open")
					      .setBase_type(video.getBaseType())
					      .setCodec_param(codec);
			connectEncoderBundle.getChannels().add(connectEncoderVideoChannel);
			ChannelSchemeDTO audio = sourceBO.getAudioSource();
			if(audio != null){
				ConnectBO connectEncoderAudioChannel = new ConnectBO().setChannelId(audio.getChannelId())
					      .setChannel_status("Open")
					      .setBase_type(audio.getBaseType())
					      .setCodec_param(codec);
				connectEncoderBundle.getChannels().add(connectEncoderAudioChannel);
			}
//			connectEncoderBundle.setChannels(new ArrayListWrapper<ConnectBO>().add(connectEncoderVideoChannel).add(connectEncoderAudioChannel).getList());
			logic.getConnectBundle().add(connectEncoderBundle);
		}
		
		return logic;
	}

	/**
	 * 点播挂断协议<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月24日 下午5:23:22
	 * @param vod 点播信息
	 * @param codec 点播信息
	 * @param userId adminId
	 * @param closeDecoder 是否关闭播放器/解码器，通常true，在换源业务时使用false
	 * @return LogicBO 协议
	 */
	//TODO:检索设备或通道是否还在使用
	public LogicBO closeEncoder(
			List<SourceBO> sourceBOs,
			CodecParamBO codec,
			Long userId) throws Exception{
	
		LogicBO logic = new LogicBO().setUserId(userId.toString())
									 .setDisconnectBundle(new ArrayList<DisconnectBundleBO>())
									 .setMediaPushDel(new ArrayList<MediaPushSetBO>())
									 .setPass_by(new ArrayList<PassByBO>());
		
		for(SourceBO sourceBO : sourceBOs){
			ChannelSchemeDTO video = sourceBO.getVideoSource();
			BundlePO bundlePO = bundleDao.findByBundleId(video.getBundleId());			
			DisconnectBundleBO disconnectEncoderBundle = new DisconnectBundleBO().setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
					             .setOperateType(DisconnectBundleBO.OPERATE_TYPE)
					             .setBundleId(video.getBundleId())
					             .setBundle_type(bundlePO.getBundleType())
					             .setLayerId(bundlePO.getAccessNodeUid());
			logic.getDisconnectBundle().add(disconnectEncoderBundle);
		}
		
		return logic;
	
	}
}

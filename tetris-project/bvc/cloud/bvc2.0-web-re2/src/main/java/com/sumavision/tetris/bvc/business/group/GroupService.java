package com.sumavision.tetris.bvc.business.group;

import java.util.*;
import java.util.stream.Collectors;

import com.sumavision.bvc.device.group.po.DeviceGroupAvtplPO;
import com.sumavision.tetris.bvc.business.dao.*;
import com.sumavision.tetris.bvc.business.group.combine.video.CombineVideoService;
import com.sumavision.tetris.bvc.business.group.speak.GroupSpeakService;
import com.sumavision.tetris.bvc.business.po.combine.video.BusinessCombineVideoPO;
import com.sumavision.tetris.bvc.business.po.info.GroupCommandInfoDAO;
import com.sumavision.tetris.bvc.cascade.bo.AuthCommandBO;
import com.sumavision.tetris.bvc.cascade.bo.CrossCommandBO;
import com.sumavision.tetris.bvc.cascade.bo.GroupBO;
import com.sumavision.tetris.bvc.cascade.bo.MinfoBO;
import com.sumavision.tetris.bvc.cascade.bo.ReplaceCommandBO;
import com.sumavision.tetris.bvc.cascade.bo.SecretCommandBO;
import com.sumavision.tetris.bvc.model.agenda.*;
import com.sumavision.tetris.bvc.model.layout.LayoutDAO;
import com.sumavision.tetris.bvc.page.PageTaskPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.FolderUserMapDAO;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.service.ResourceRemoteService;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.command.group.dao.AgendaForwardDemandDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupMemberDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupUserInfoDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupUserPlayerDAO;
import com.sumavision.bvc.command.group.enumeration.EditStatus;
import com.sumavision.bvc.command.group.enumeration.GroupType;
import com.sumavision.bvc.command.group.forward.AgendaForwardDemandPO;
import com.sumavision.bvc.command.group.forward.CommandGroupForwardPO;
import com.sumavision.bvc.device.command.bo.MessageSendCacheBO;
import com.sumavision.bvc.device.command.cascade.util.CommandCascadeUtil;
import com.sumavision.bvc.device.command.cascade.util.GroupCascadeUtil;
import com.sumavision.bvc.device.command.cast.CommandCastServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.command.exception.CommandGroupNameAlreadyExistedException;
import com.sumavision.bvc.device.command.meeting.CommandMeetingSpeakServiceImpl;
import com.sumavision.bvc.device.command.record.CommandRecordServiceImpl;
import com.sumavision.bvc.device.command.time.CommandFightTimeServiceImpl;
import com.sumavision.bvc.device.command.user.CommandUserServiceImpl;
import com.sumavision.bvc.device.command.vod.CommandVodService;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.dao.CombineAudioDAO;
import com.sumavision.bvc.device.group.dao.CombineVideoDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupProceedRecordDAO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.CommonQueryUtil;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.feign.ResourceServiceClient;
import com.sumavision.bvc.log.OperationLogService;
import com.sumavision.bvc.resource.dao.ResourceBundleDAO;
import com.sumavision.bvc.resource.dao.ResourceChannelDAO;
import com.sumavision.bvc.system.dao.AvtplDAO;
import com.sumavision.bvc.system.enumeration.AvtplUsageType;
import com.sumavision.bvc.system.po.AvtplGearsPO;
import com.sumavision.bvc.system.po.AvtplPO;
import com.sumavision.tetris.auth.token.TerminalType;
import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.bvc.business.OriginType;
import com.sumavision.tetris.bvc.business.bo.BusinessDeliverBO;
import com.sumavision.tetris.bvc.business.bo.MemberTerminalBO;
import com.sumavision.tetris.bvc.business.common.BusinessCommonService;
import com.sumavision.tetris.bvc.business.common.BusinessReturnService;
import com.sumavision.tetris.bvc.business.common.MulticastService;
import com.sumavision.tetris.bvc.business.deliver.DeliverExecuteService;
import com.sumavision.tetris.bvc.business.group.process.GroupProceedRecordServiceImpl;
//import com.sumavision.tetris.bvc.business.group.demand.GroupDemandService;
import com.sumavision.tetris.bvc.business.group.record.GroupRecordService;
import com.sumavision.tetris.bvc.business.group.secret.GroupSecretService;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberPO;
import com.sumavision.tetris.bvc.business.special.GroupSpecialCommonService;
import com.sumavision.tetris.bvc.business.special.authorize.GroupAuthorizeService;
import com.sumavision.tetris.bvc.business.special.cooperate.GroupCooperateService;
import com.sumavision.tetris.bvc.business.special.cross.GroupCrossService;
import com.sumavision.tetris.bvc.business.special.replace.GroupReplaceService;
import com.sumavision.tetris.bvc.business.terminal.hall.ConferenceHallDAO;
import com.sumavision.tetris.bvc.business.terminal.hall.ConferenceHallPO;
import com.sumavision.tetris.bvc.business.terminal.hall.ConferenceHallService;
import com.sumavision.tetris.bvc.business.terminal.hall.TerminalBundleConferenceHallPermissionDAO;
import com.sumavision.tetris.bvc.business.terminal.hall.TerminalBundleConferenceHallPermissionPO;
import com.sumavision.tetris.bvc.cascade.CommandCascadeService;
import com.sumavision.tetris.bvc.cascade.ConferenceCascadeService;
import com.sumavision.tetris.bvc.model.role.InternalRoleType;
import com.sumavision.tetris.bvc.model.role.RoleDAO;
import com.sumavision.tetris.bvc.model.role.RoleExecuteService;
import com.sumavision.tetris.bvc.model.role.RolePO;
import com.sumavision.tetris.bvc.model.terminal.TerminalDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalPO;
import com.sumavision.tetris.bvc.page.PageInfoDAO;
import com.sumavision.tetris.bvc.page.PageTaskDAO;
import com.sumavision.tetris.bvc.page.PageTaskService;
import com.sumavision.tetris.bvc.util.TetrisBvcQueryUtil;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mvc.util.BaseUtils;
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
	public static boolean autoEnter = true;
	
	private Long preNum = 123L;
	
	/** synchronized锁的前缀 */
	private static final String lockProcessPrefix = "tetris-group-";

	@Autowired
	private CombineVideoDAO deviceGroupCombineVideoDao;
	
	@Autowired
	private LayoutDAO layoutDao;

	@Autowired
	private GroupCommandInfoDAO groupCommandInfoDao;
	
	@Autowired
	private AgendaForwardDAO agendaForwardDao;
	
	@Autowired
	private AgendaForwardDemandDAO agendaForwardDemandDao;
	
	@Autowired
	private DeviceGroupProceedRecordDAO deviceGroupProceedRecordDao;

	@Autowired
	private BusinessGroupMemberDAO businessGroupMemberDao;

	@Autowired
	private AgendaForwardSourceDAO agendaForwardSourceDao;

	@Autowired
	private CustomAudioDAO customAudioDao;

	@Autowired
	private AgendaForwardDestinationDAO agendaForwardDestinationDao;

	@Autowired
	private CombineVideoDAO combineVideoDao;

	@Autowired
	private CombineAudioDAO combineAudioDao;
	
	@Autowired
	private CommandGroupUserInfoDAO commandGroupUserInfoDao;
	
	@Autowired
	private ConferenceHallService conferenceHallService;
	
	@Autowired
	private PageInfoDAO pageInfoDAO;
	
	@Autowired
	private AgendaForwardDAO agendaForwardDAO;
	
	@Autowired
	private ConferenceHallDAO conferenceHallDao;
	
	@Autowired
	private TerminalDAO terminalDao;
	
	@Autowired
	private AgendaDAO agendaDao;
	
	@Autowired
	private RunningAgendaDAO runningAgendaDao;
	
	@Autowired
	private RoleDAO roleDao;
	
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
	private AvtplDAO sysAvtplDao;
	
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
	private TerminalBundleConferenceHallPermissionDAO terminalBundleConferenceHallPermissionDao;
	
	//@Autowired
	//private AutoCombineService autoCombineService;
	
	@Autowired
	private MulticastService multicastService;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private GroupRecordService groupRecordService;
	
	@Autowired
	private AgendaExecuteService agendaExecuteService;
	
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
	private CommandUserServiceImpl commandUserServiceImpl;
	
	@Autowired
	private CommandVodService commandVodService;
	
	@Autowired
	private PageTaskService pageTaskService;
	
	@Autowired
	private GroupMemberService groupMemberService;
	
	@Autowired
	private GroupSpecialCommonService groupSpecialCommonService;
	
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
	private RoleExecuteService roleExecuteService;
	
	@Autowired
	private BusinessReturnService businessReturnService;
	
	@Autowired
	private GroupCascadeUtil groupCascadeUtil;

	@Autowired
	private AgendaService agendaService;
	
	@Autowired
	private DeliverExecuteService deliverExecuteService;
	
	@Autowired
	private GroupProceedRecordServiceImpl groupProceedRecordServiceImpl;

	@Autowired
	private CombineVideoService combineVideoService;

	@Autowired
	private BusinessCombineVideoDAO combineVideoDAO;
	
	@Autowired
	private GroupSpeakService groupSpeakService;
    
	@Autowired
	private GroupCooperateService groupCooperateService;
	
	@Autowired
	private GroupCrossService groupCrossService;
	
	@Autowired
	private GroupAuthorizeService groupAuthorizeService;
	
	@Autowired
	private GroupReplaceService groupReplaceService;
	
	@Autowired
	private GroupSecretService groupSecretService;
	
	/**
	 * 根据用户id和bundleType查找会场id<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月25日 下午4:06:58
	 * @param userId
	 * @param bundleType 对应BundlePO的deviceModel，取值如tvos
	 * @return 会场id
	 * @throws BaseException
	 */
	private Long findHallId(Long userId, String bundleType) throws BaseException{
		BundlePO bundle = bundleDao.findByUserIdAndDeviceModel(userId, bundleType);
		String bundleId = bundle.getBundleId();
		List<TerminalBundleConferenceHallPermissionPO> ps = terminalBundleConferenceHallPermissionDao.findByBundleTypeAndBundleId(bundleType, bundleId);
		if(ps.size() == 0){
			throw new BaseException(StatusCode.FORBIDDEN, "请配置您的设备");
		}
		if(ps.size() > 1){
			throw new BaseException(StatusCode.FORBIDDEN, "您的设备配置有重复，请修改");
		}
		TerminalBundleConferenceHallPermissionPO p = ps.get(0);
		Long hallId = p.getConferenceHallId();
		return hallId;
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
		List<BusinessGroupMemberPO> members = group.getMembers();
		BusinessGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		for(BusinessGroupMemberPO member : members){
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
					
		for(BusinessGroupMemberPO member : members){
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
		
		for(BusinessGroupMemberPO member : members){
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
	@Transactional
	public void remove(Long userId, List<Long> groupIds) throws Exception{
		UserVO user = userQuery.current();
		groupIds.remove(null);
		List<GroupPO> groups = groupDao.findAll(groupIds);
		StringBuffer dis = new StringBuffer();
		
		//校验
		for(GroupPO group : groups){
			if(!userId.equals(group.getUserId()) && !user.getIsGroupCreator()){// && !group.getBusinessType().equals(BusinessType.SECRET)){
				if(!OriginType.OUTER.equals(group.getOriginType())){
					throw new BaseException(StatusCode.FORBIDDEN, "只有创建者能删除 " + group.getName());
				}
			}
			if(!GroupStatus.STOP.equals(group.getStatus())){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已经开始，请停止后再删除。id: " + group.getId());
			}
			dis.append(group.getName() + "，");
			

			if(!OriginType.OUTER.equals(group.getOriginType())){
				if(BusinessType.COMMAND.equals(group.getBusinessType())){
					GroupBO groupBO = commandCascadeUtil.deleteAgendaCommand(group);
					commandCascadeService.delete(groupBO);
				}else if(BusinessType.MEETING_BVC.equals(group.getBusinessType())){
					GroupBO groupBO = commandCascadeUtil.deleteAgendaCommand(group);
					conferenceCascadeService.delete(groupBO);
				}
			}
			//删除自定义议程
			List<AgendaPO> agendaPOS=agendaDao.findByBusinessId(group.getId());
			for (AgendaPO agendaPo:agendaPOS) {
				agendaService.delete(agendaPo.getId());
			}
			//删除关联角色
			roleDao.deleteByBusinessId(group.getId());
			//删除角色关联成员
			for (BusinessGroupMemberPO memberPO:group.getMembers()) {
				List<Long> delList=new ArrayList<>();
				if(memberPO.getRoleId()!=null){
					delList.add(memberPO.getRoleId());
					groupMemberRolePermissionDao.deleteByRoleIdInAndGroupMemberId(delList,memberPO.getId());
				}
			}

		}

		groupDao.deleteByIdIn(groupIds);
//		businessGroupMemberDao.deleteByGroupIdIn(groupIds);
		
		log.info(dis.toString() + "被删除");
		operationLogService.send(user.getNickname(), "删除指挥", user.getNickname() + "删除指挥groupIds:" + groupIds.toString());
	}
	
	public JSONArray enterGroups(Long userId, List<Long> groupIds) throws Exception{
		
		UserVO user = userQuery.current();
		JSONArray groupInfos = new JSONArray();
		
		List<GroupPO> groups = groupDao.findAll(groupIds);
		
		//校验是否都在进行中，否则抛错
		for(GroupPO group : groups){
			if(userId.equals(group.getUserId())){
				//主席不抛错
			}else if(group.getStatus().equals(GroupStatus.STOP)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已停止，无法进入，id: " + group.getId());
			}
		}
		
		for(Long groupId : groupIds){
			
			if(groupId==null || groupId.equals("")){
				log.info("进会操作，会议id有误，groupIds: " + groupIds.toString());
				continue;
			}
			
			synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
			
				//判断是否进入其它会议，建议在commandGroupDao写一个新方法，不查自己建的会
//				List<CommandGroupPO> commands = commandGroupDao.findEnteredGroupByMemberUserId(userId);
//				if(commands!=null && commands.size()>0){
//					for(CommandGroupPO command : commands){
//						if(!command.getUserId().equals(userId)){
//							//已经进入其它会议
//						}
//					}
//				}
				
				addOrEnterMembers(groupId, new ArrayListWrapper<Long>().add(userId).getList(), null, null);
				
				
				GroupPO group = groupDao.findOne(groupId);
//				GroupType groupType = group.getType();
				List<BusinessGroupMemberPO> members = businessGroupMemberDao.findByGroupId(groupId);
				
				//主席member
				BusinessGroupMemberPO chairmanMember = tetrisBvcQueryUtil.queryChairmanMember(members);
				
				//该用户的member
				BusinessGroupMemberPO thisMember = tetrisBvcQueryUtil.queryMemberByOriginIdAndGroupMemberType(members, userId.toString(), GroupMemberType.MEMBER_USER);
				if(thisMember == null) continue;
				
				/*//不是主席，进行“接听”处理
				if(!thisMember.isAdministrator()){
					List<CommandBusinessGroupMemberPO> acceptMembers = new ArrayList<CommandBusinessGroupMemberPO>();
					acceptMembers.add(thisMember);
					//如果该成员状态为CONNECT则不需处理，否则按接听处理
					if(thisMember.getMemberStatus().equals(MemberStatus.CONNECT)){
						
					}else{
						
						//级联，如果该“进入”成员是本系统成员，则通知外部系统
						if(!OriginType.OUTER.equals(thisMember.getOriginType())){
							if(GroupType.BASIC.equals(groupType)){
								GroupBO groupBO = commandCascadeUtil.joinCommand(group, null, acceptMembers);
								commandCascadeService.join(groupBO);
							}else if(GroupType.MEETING.equals(groupType)){
								GroupBO groupBO = commandCascadeUtil.joinMeeting(group, null, acceptMembers);
								conferenceCascadeService.join(groupBO);
							}
						}
						
	//					chosePlayersForMembers(group, acceptMembers);//后选播放器:放进membersResponse
						membersResponse(group, acceptMembers, null);
					}
				}
				
				JSONArray splits = new JSONArray();
				for(CommandGroupUserPlayerPO player : thisMember.getPlayers()){
					
					if(group.getType().equals(GroupType.BASIC) || group.getType().equals(GroupType.MEETING)){
						JSONObject split = new JSONObject();
						split.put("serial", player.getLocationIndex());
						split.put("bundleId", player.getBundleId());
						split.put("bundleNo", player.getCode());
						split.put("businessType", player.getPlayerBusinessType().getCode());
						split.put("businessId", group.getId().toString());
						split.put("businessInfo", player.getBusinessName());
						split.put("status", group.getStatus().getCode());
						splits.add(split);
					}else if(group.getType().equals(GroupType.SECRET)){
						JSONObject split = new JSONObject();
						split.put("serial", player.getLocationIndex());
						split.put("bundleId", player.getBundleId());
						split.put("bundleNo", player.getCode());
						split.put("businessType", player.getPlayerBusinessType().getCode());
						split.put("businessId", group.getId().toString());
						split.put("businessInfo", player.getBusinessName());
						splits.add(split);
					}
				}*/
				
				JSONObject message = new JSONObject();
				message.put("id", group.getId());
				message.put("name", group.getName());
				message.put("status", group.getStatus().getCode());
				message.put("commander", Long.parseLong(chairmanMember.getOriginId()));
				message.put("creator", Long.parseLong(chairmanMember.getOriginId()));
				message.put("splits", new JSONArray());
				/*//已经启动的会议，添加作战时间
				GroupStatus groupStatus = group.getStatus();
				if(!groupStatus.equals(GroupStatus.STOP)){
					Date fightTime = commandFightTimeServiceImpl.calculateCurrentFightTime(group);
					if(fightTime != null){
						message.put("fightTime", DateUtil.format(fightTime, DateUtil.dateTimePattern));
					}
				}*/
				
				groupInfos.add(message);
				
			}
		}
		operationLogService.send(user.getNickname(), "进入指挥", user.getNickname() + "进入指挥groupIds:" + groupIds.toString());
		return groupInfos;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public Object addOrEnterMembers(Long groupId, List<Long> userIdList, List<Long> hallIds, List<String> bundleIdList) throws Exception{
		
		if(userIdList == null) userIdList = new ArrayList<Long>();
		if(hallIds == null) hallIds = new ArrayList<Long>();
		if(bundleIdList == null) bundleIdList = new ArrayList<String>();
		
		TerminalPO terminal = terminalDao.findByType(com.sumavision.tetris.bvc.model.terminal.TerminalType.QT_ZK);
		
		List<MemberTerminalBO> memberTerminalBOs = new ArrayList<MemberTerminalBO>();
		for(Long userId : userIdList){
			MemberTerminalBO memberBO = new MemberTerminalBO()
					.setGroupMemberType(GroupMemberType.MEMBER_USER)
					.setOriginId(userId.toString())
					.setTerminalId(terminal.getId());
			memberTerminalBOs.add(memberBO);
		}

		//会场
		List<ConferenceHallPO> halls = conferenceHallDao.findAll(hallIds);
		for(ConferenceHallPO hall : halls){
			MemberTerminalBO memberBO = new MemberTerminalBO()
					.setGroupMemberType(GroupMemberType.MEMBER_HALL)
					.setOriginId(hall.getId().toString())
					.setTerminalId(hall.getTerminalId())
					.setFromDevice(hall.getFromDevice());
			memberTerminalBOs.add(memberBO);
		}
		
		return addOrEnterMembersByBO(groupId, memberTerminalBOs);
	}
	
	private Object addOrEnterMembersByBO(Long groupId, List<MemberTerminalBO> memberTerminalBOs) throws Exception{
		UserVO self = userQuery.current();
		JSONArray chairSplits = new JSONArray();
		
		if(groupId==null || groupId.equals("")){
			log.info("会议加人或进入，会议id有误");
			return chairSplits;
		}
		
		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
								
			GroupPO group = groupDao.findOne(groupId);
			BusinessType businessType = group.getBusinessType();
			List<BusinessGroupMemberPO> members = businessGroupMemberDao.findByGroupId(groupId);
			BusinessGroupMemberPO chairmanMember = tetrisBvcQueryUtil.queryChairmanMember(members);
			List<BusinessGroupMemberPO> enterMembers = new ArrayList<BusinessGroupMemberPO>();
			
			BusinessDeliverBO businessDeliverBO = new BusinessDeliverBO().setGroup(group).setUserId(group.getUserId().toString());
			
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
			
			for(MemberTerminalBO memberTerminalBO : memberTerminalBOs){
				String originId = memberTerminalBO.getOriginId();
				GroupMemberType groupMemberType = memberTerminalBO.getGroupMemberType();
//				if(existedOriginIdList.contains(originId)){
					
				BusinessGroupMemberPO member = tetrisBvcQueryUtil.queryMemberByOriginIdAndGroupMemberType(members, originId, groupMemberType);
				if(member!=null){
					//已经存在的成员，进入处理
					if(member.getGroupMemberStatus().equals(GroupMemberStatus.CONNECT)){
						//已经进会的成员，不用再处理
					}else{
						enterOriginIdList.add(originId);
						enterMembers.add(member);
					}
				}else if(member == null){
					//不存在的成员，加入处理
					newOriginIdList.add(originId);
					newMemberTerminalBOs.add(memberTerminalBO);
				}
			}
			
			List<BusinessGroupMemberPO> newMembers = groupMemberService.generateMembers(group, newMemberTerminalBOs, null);
			businessGroupMemberDao.save(newMembers);
			groupMemberService.fullfillGroupMember(newMembers);
			
			//授予角色
//			RolePO memberRole = businessCommonService.queryGroupMemberRole(group);
			/*List<GroupMemberRolePermissionPO> pers = new ArrayList<GroupMemberRolePermissionPO>();
			for(BusinessGroupMemberPO newMember : newMembers){
				//主席成员只有主席角色
				if(newMember.getIsAdministrator()){
					RolePO chairmanRole = businessCommonService.queryGroupChairmanRole(group);
					newMember.setRoleId(chairmanRole.getId());
					newMember.setRoleName(chairmanRole.getName());
					GroupMemberRolePermissionPO memberRolePermission = new GroupMemberRolePermissionPO(chairmanRole.getId(), newMember.getId());
					pers.add(memberRolePermission);
				}else{
					if(newMember.getRoleId() == null){
						newMember.setGroupMemberStatus(GroupMemberStatus.CONNECT);
						newMember.setRoleId(memberRole.getId());
						newMember.setRoleName(memberRole.getName());
						GroupMemberRolePermissionPO memberRolePermission = new GroupMemberRolePermissionPO(memberRole.getId(), newMember.getId());
						pers.add(memberRolePermission);
					}
				}
			}
			groupMemberRolePermissionDao.save(pers);*/
			
			//给所有成员，没有PageInfoPO的，建立PageInfoPO
			groupMemberService.generateMembersPageInfo(newMembers);
						
			//这两个变量用来生成message用
			StringBufferWrapper newMembersNames = new StringBufferWrapper();
			JSONArray newMemberIdsJSONArray = new JSONArray();
			for(BusinessGroupMemberPO newMember : newMembers){
				newMembersNames.append(newMember.getName()).append("，");
				newMemberIdsJSONArray.add(newMember.getId().toString());
			}
			newMembersNames.append("被邀请到 ").append(group.getName());//.append(" 会议");	
			List<MessageSendCacheBO> messageCaches = new ArrayList<MessageSendCacheBO>();
			
			//此处不呼叫主席的播放器，待成员接听后通过forward呼叫	
			if(!group.getStatus().equals(GroupStatus.STOP)){
				
				//新的在线的成员自动上线，还有“进入”的成员
				List<BusinessGroupMemberPO> loginNewAndEnterMembers = new ArrayList<BusinessGroupMemberPO>();
				
				
				if(!autoEnter){
					//不自动接听：发消息给新成员（本系统、用户类型）
					for(BusinessGroupMemberPO member : newMembers){
						
						if(OriginType.OUTER.equals(group.getOriginType())){
							continue;
						}
						
						if(!GroupMemberType.MEMBER_USER.equals(member.getGroupMemberType())){
							//本系统其它设备自动进入
							loginNewAndEnterMembers.add(member);
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
					//自动接听（本系统的）
					for(BusinessGroupMemberPO newMember : newMembers){
						if(newMember.getIsAdministrator()) continue;
//						UserBO commandUserBo = queryUtil.queryUserById(commandUserBos, newMember.getUserId());
//						if(commandUserBo.isLogined()){//该成员如果对应终端登录，则进会
						//if(OriginType.INNER.equals(newMember.getOriginType())){
							loginNewAndEnterMembers.add(newMember);
						//}
//						}else{
//							//没在线的，置为DISCONNECT。不用按照“拒绝”处理
//							newMember.setMemberStatus(MemberStatus.DISCONNECT);
//						}
					}
				}
				
				loginNewAndEnterMembers.addAll(enterMembers);//enterMembers是确定要进入的成员，可能是级联系统的
				groupMemberService.membersResponse(group, members, loginNewAndEnterMembers);
				
//				//处理角色
//				Map<Long, List<BusinessGroupMemberPO>> roleIdAddMembersMap = new HashMap<Long, List<BusinessGroupMemberPO>>();
//				roleIdAddMembersMap.put(memberRole.getId(), loginNewAndEnterMembers);
//				List<GroupMemberRolePermissionPO> permissionList = new ArrayList<GroupMemberRolePermissionPO>();
//				for(BusinessGroupMemberPO groupMember : loginNewAndEnterMembers){
//					GroupMemberRolePermissionPO permission = new GroupMemberRolePermissionPO();
//					permission.setGroupMemberId(groupMember.getId());
//					permission.setRoleId(memberRole.getId());
//					permissionList.add(permission);
//					//modifyRoleMembers()方法要求：成员的roleId必须为改变角色之前的值
//					groupMember.setRoleId(null);
//				}
				
				Map<Long, List<BusinessGroupMemberPO>> roleIdAddMembersMap = new HashMap<Long, List<BusinessGroupMemberPO>>();
				List<GroupMemberRolePermissionPO> permissionList = new ArrayList<GroupMemberRolePermissionPO>();
				if(BusinessType.MEETING_BVC.equals(businessType)) {
					RolePO memberRole = roleDao.findByInternalRoleType(InternalRoleType.MEETING_AUDIENCE);
					roleIdAddMembersMap.put(memberRole.getId(), loginNewAndEnterMembers);
					for(BusinessGroupMemberPO groupMember : loginNewAndEnterMembers){
						GroupMemberRolePermissionPO permission = new GroupMemberRolePermissionPO();
						permission.setGroupMemberId(groupMember.getId());
						permission.setRoleId(memberRole.getId());
						permissionList.add(permission);
						//modifyRoleMembers()方法要求：成员的roleId必须为改变角色之前的值
						groupMember.setRoleId(null);
					}
				}else if(BusinessType.COMMAND.equals(businessType)){
					List<RolePO> allRole = roleDao.findAll();
					List<RolePO> levelRoles = allRole.stream().filter(role ->{
						return role.getInternalRoleType() != null;
					}).filter(role ->{
						return BaseUtils.stringIsNotBlank(role.getInternalRoleType().getLevel());
					}).collect(Collectors.toList());
					Map<String, RolePO> levelAndRoleMap = levelRoles.stream().filter(role->{
						return BaseUtils.stringIsNotBlank(role.getInternalRoleType().getLevel());
					}).collect(Collectors.toMap(role-> (String)role.getInternalRoleType().getLevel(), role-> role));
					for(BusinessGroupMemberPO groupMember : loginNewAndEnterMembers){
						RolePO memberRole = levelAndRoleMap.get(groupMember.getLevel().toString());
						if(roleIdAddMembersMap.get(memberRole.getId()) ==null){
							roleIdAddMembersMap.put(memberRole.getId(), new ArrayList<BusinessGroupMemberPO>());
						}
						roleIdAddMembersMap.get(memberRole.getId()).add(groupMember);
						GroupMemberRolePermissionPO permission = new GroupMemberRolePermissionPO();
						permission.setGroupMemberId(groupMember.getId());
						permission.setRoleId(memberRole.getId());
						permissionList.add(permission);
						//modifyRoleMembers()方法要求：成员的roleId必须为改变角色之前的值
						groupMember.setRoleId(null);
					}
				}
				
				groupMemberRolePermissionDao.save(permissionList);//关联表已经存在，这里不应保存
				groupDao.save(group);
				roleExecuteService.modifyRoleMembers(group, roleIdAddMembersMap, businessDeliverBO, true);
				RunningAgendaPO runningAgenda = runningAgendaDao.findByGroupId(groupId);
				if(runningAgenda != null){
					agendaExecuteService.runAgenda(groupId, runningAgenda.getAgendaId(), businessDeliverBO, false);
				}
			}else{
				//处理角色
//				Map<Long, List<BusinessGroupMemberPO>> roleIdAddMembersMap = new HashMap<Long, List<BusinessGroupMemberPO>>();
//				roleIdAddMembersMap.put(memberRole.getId(), newMembers);
//				List<GroupMemberRolePermissionPO> permissionList = new ArrayList<GroupMemberRolePermissionPO>();
//				for(BusinessGroupMemberPO groupMember :newMembers){
//					GroupMemberRolePermissionPO permission = new GroupMemberRolePermissionPO();
//					permission.setGroupMemberId(groupMember.getId());
//					permission.setRoleId(memberRole.getId());
//					permissionList.add(permission);
//				}
				
				Map<Long, List<BusinessGroupMemberPO>> roleIdAddMembersMap = new HashMap<Long, List<BusinessGroupMemberPO>>();
				List<GroupMemberRolePermissionPO> permissionList = new ArrayList<GroupMemberRolePermissionPO>();
				if(BusinessType.MEETING_BVC.equals(businessType)) {
					RolePO memberRole = roleDao.findByInternalRoleType(InternalRoleType.MEETING_AUDIENCE);
					roleIdAddMembersMap.put(memberRole.getId(), newMembers);
					for(BusinessGroupMemberPO groupMember : newMembers){
						GroupMemberRolePermissionPO permission = new GroupMemberRolePermissionPO();
						permission.setGroupMemberId(groupMember.getId());
						permission.setRoleId(memberRole.getId());
						permissionList.add(permission);
						//modifyRoleMembers()方法要求：成员的roleId必须为改变角色之前的值
						groupMember.setRoleId(null);
					}
				}else if(BusinessType.COMMAND.equals(businessType)){
					List<RolePO> allRole = roleDao.findAll();
					List<RolePO> levelRoles = allRole.stream().filter(role ->{
						return BaseUtils.stringIsNotBlank(role.getInternalRoleType().getLevel());
					}).collect(Collectors.toList());
					Map<String, RolePO> levelAndRoleMap = levelRoles.stream().filter(role->{
						return BaseUtils.stringIsNotBlank(role.getInternalRoleType().getLevel());
					}).collect(Collectors.toMap(role-> (String)role.getInternalRoleType().getLevel(), role-> role));
					for(BusinessGroupMemberPO groupMember : newMembers){
						RolePO memberRole = levelAndRoleMap.get(groupMember.getLevel().toString());
						if(roleIdAddMembersMap.get(memberRole.getId()) ==null){
							roleIdAddMembersMap.put(memberRole.getId(), new ArrayList<BusinessGroupMemberPO>());
						}
						roleIdAddMembersMap.get(memberRole.getId()).add(groupMember);
						GroupMemberRolePermissionPO permission = new GroupMemberRolePermissionPO();
						permission.setGroupMemberId(groupMember.getId());
						permission.setRoleId(memberRole.getId());
						permissionList.add(permission);
						//modifyRoleMembers()方法要求：成员的roleId必须为改变角色之前的值
						groupMember.setRoleId(null);
					}
				}
				
				groupMemberRolePermissionDao.save(permissionList);
				roleExecuteService.modifyRoleMembers(group, roleIdAddMembersMap, businessDeliverBO, true);
			}			
			
			//级联，必须在membersResponse之后，这样MemberStatus才是对的
			if(!OriginType.OUTER.equals(group.getOriginType())){

				//停会则 groupUpdate
				if(GroupStatus.STOP.equals(group.getStatus())){
					if(BusinessType.COMMAND.equals(group.getBusinessType())){
						GroupBO groupBO = commandCascadeUtil.updateAgendaCommand(group);
						commandCascadeService.update(groupBO);						
					}else if(BusinessType.MEETING_BVC.equals(businessType)){
						GroupBO groupBO = commandCascadeUtil.updateAgendaMeeting(group);
						conferenceCascadeService.update(groupBO);			
					}
				}
				//开会中则 maddinc maddfull
				else{
					List<BusinessGroupMemberPO> newAndEnterMembers = new ArrayList<BusinessGroupMemberPO>();
					newAndEnterMembers.addAll(newMembers);
					newAndEnterMembers.addAll(enterMembers);
					
					//记录新加入的用户列表
					List<MinfoBO> newMemberInfos = commandCascadeUtil.generateAgendaMinfoBOList(newAndEnterMembers, chairmanMember);
					List<MinfoBO> oldMemberInfos=commandCascadeUtil.generateAgendaMinfoBOList(members);
					//得到位于新节点上的用户列表
					List<MinfoBO> newNodeMemberInfos = commandCascadeUtil.filterAddedNodeMinfo(oldMemberInfos, newMemberInfos);
					
					if(BusinessType.COMMAND.equals(group.getBusinessType())){
						
						GroupBO groupBO = commandCascadeUtil.joinAgendaCommand(group, oldMemberInfos, newAndEnterMembers);
						commandCascadeService.join(groupBO);
						
						//如果有新节点，则要发maddfull
						if(newNodeMemberInfos.size() > 0){
							GroupBO maddfullBO = commandCascadeUtil.maddfullAgendaCommand(group, newNodeMemberInfos);
							commandCascadeService.info(maddfullBO);
							log.info(newNodeMemberInfos.size() + "个成员所在的节点新参与指挥，全量同步该指挥：" + group.getName());
						}
						
					}else if(BusinessType.MEETING_BVC.equals(group.getBusinessType())){
						
						GroupBO groupBO = commandCascadeUtil.joinAgendaMeeting(group, oldMemberInfos, newAndEnterMembers);
						conferenceCascadeService.join(groupBO);
						
						//如果有新节点，则要发maddfull
						if(newNodeMemberInfos.size() > 0){
							GroupBO maddfullBO = commandCascadeUtil.maddfullAgendaMeeting(group, newNodeMemberInfos);
							conferenceCascadeService.info(maddfullBO);
							log.info(newNodeMemberInfos.size() + "个成员所在的节点新参与会议，全量同步该会议：" + group.getName());
						}
					}
				}
			}
			
			if(self != null){
				operationLogService.send(self.getNickname(), "成员加入", self.getNickname()
						+ "添加成员originIds:" + newOriginIdList.toString()
						+ "进入成员originIds:" + enterOriginIdList.toString());
			}
			
			//发消息或存入businessReturn
			businessReturnService.dealWebsocket(messageCaches);
			
			//发消息，主要是“邀请”消息，自动接听则没有
			businessReturnService.dealWebsocket(messageCaches);
			
			deliverExecuteService.execute(businessDeliverBO, "添加成员", true);
			
			//呼叫主席（此处不呼叫主席的播放器，待成员接听后通过forward呼叫，测试是否可行）
//			List<BusinessGroupMemberPO> acceptMembers = new ArrayList<BusinessGroupMemberPO>();
//			acceptMembers.add(chairman);
//			membersResponse(group, acceptMembers, null);
			
			return chairSplits;			
		}
	}
	
	@Transactional(rollbackFor = Exception.class)
	public Object removeMembersU(Long groupId, List<Long> userIdList, int mode) throws Exception{
		List<BusinessGroupMemberPO> members = businessGroupMemberDao.findByGroupId(groupId);
		List<Long> memberIdList = new ArrayList<Long>();
		for(BusinessGroupMemberPO member : members){
			if(member.getGroupMemberType().equals(GroupMemberType.MEMBER_USER)
					&& userIdList.contains(Long.parseLong(member.getOriginId()))){
				memberIdList.add(member.getId());
			}
		}
		return removeMembersByMemberIds(groupId, memberIdList, mode);
	}
	
	public void exitApply(Long userId, Long groupId) throws Exception{

		UserVO user = userQuery.current();
		if(groupId==null || groupId.equals("")){
			log.info("申请退出，会议id有误");
			return;
		}

		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {

			GroupPO group = groupDao.findOne(groupId);
			BusinessType groupType = group.getBusinessType();

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

			List<BusinessGroupMemberPO> members = businessGroupMemberDao.findByGroupId(groupId);
			BusinessGroupMemberPO chairmanMember = tetrisBvcQueryUtil.queryChairmanMember(members);
			BusinessGroupMemberPO exitMember = tetrisBvcQueryUtil.queryMemberByOriginIdAndGroupMemberType(members, userId.toString(), GroupMemberType.MEMBER_USER);

			//如果主席和申请人都不在该系统，则不需要处理（正常不会出现）
			if(OriginType.OUTER.equals(chairmanMember.getOriginType())
					&& OriginType.OUTER.equals(exitMember.getOriginType())){
				log.info("主席和申请退出的人都不是该系统用户，主席：" + chairmanMember.getName() + " 退出用户：" + exitMember.getName());
				return;
			}

			if(exitMember.getGroupMemberStatus().equals(GroupMemberStatus.DISCONNECT)){
				throw new BaseException(StatusCode.FORBIDDEN, "您已经退出");
			}

			//级联
			if(!OriginType.OUTER.equals(chairmanMember.getOriginType())){
				//主席在该系统
				JSONObject message = new JSONObject();
				message.put("businessType", "exitApply");
				message.put("businessInfo", exitMember.getName() + "申请退出" + group.getName());
				message.put("businessId", group.getId().toString() + "-" + exitMember.getId());

				if(businessReturnService.getSegmentedExecute()){
					businessReturnService.add(null, new MessageSendCacheBO(Long.parseLong(chairmanMember.getOriginId()), message.toJSONString(), WebsocketMessageType.COMMAND), group.getName() + "申请退出");
					businessReturnService.execute();
				}else{
					WebsocketMessageVO ws = websocketMessageService.send(Long.parseLong(chairmanMember.getOriginId()), message.toJSONString(), WebsocketMessageType.COMMAND);
					websocketMessageService.consume(ws.getId());
					log.info(group.getName() + "申请退出");
				}
			}else{
				//主席在外部系统（那么申请人在该系统）
				/*if(GroupType.BASIC.equals(groupType)){
					GroupBO groupBO = commandCascadeUtil.exitCommandRequest(group, exitMember);
					commandCascadeService.exitRequest(groupBO);
				}else if(GroupType.MEETING.equals(groupType)){
					GroupBO groupBO = commandCascadeUtil.exitMeetingRequest(group, exitMember);
					conferenceCascadeService.exitRequest(groupBO);
				}*/
			}

//			log.info(group.getName() + "申请退出");
		}
		operationLogService.send(user.getNickname(), "申请退出", user.getNickname() + "申请退出groupId:" + groupId);
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
	 * @param mode 0为主席同意的主动退出（成员数据和做源都保留）；2为主席强退，删人；1为保留字段暂时无用
	 * @return 1删人时给主席返回chairSplits；0退出时给退出成员返回exitMemberSplits；后续优化
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public Object removeMembersByMemberIds(Long groupId, List<Long> memberIdList, int mode) throws Exception{

		UserVO user = userQuery.current();

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

			BusinessDeliverBO businessDeliverBO = new BusinessDeliverBO().setGroup(group);

//			//防止专向指挥中调用此方法
//			if(group.getType().equals(GroupType.SECRET)){
//				throw new BaseException(StatusCode.FORBIDDEN, "正在专项，不能退出，只能“停止”");
//			}

			JSONArray chairSplits = new JSONArray();
			List<Long> consumeIds = new ArrayList<Long>();
			List<MessageSendCacheBO> messageCaches = new ArrayList<MessageSendCacheBO>();

			BusinessType businessType = group.getBusinessType();
			List<BusinessGroupMemberPO> members = businessGroupMemberDao.findByGroupId(groupId);
			BusinessGroupMemberPO chairmanMember = tetrisBvcQueryUtil.queryChairmanMember(members);

			//记录删人之前的用户列表
//			List<MinfoBO> orgMemberInfos = commandCascadeUtil.generateMinfoBOList(members);

			if(memberIdList.contains(chairmanMember.getId())){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "不能删除主席");
			}

			List<BusinessGroupMemberPO> removeMembers = new ArrayList<BusinessGroupMemberPO>();
			List<Long> removeMemberIds = new ArrayList<Long>();
			for(BusinessGroupMemberPO member : members){
				if(memberIdList.contains(member.getId())){
					removeMembers.add(member);
					removeMemberIds.add(member.getId());
				}else if(memberIdList.contains(Long.parseLong(member.getOriginId()))){
					removeMembers.add(member);
					removeMemberIds.add(member.getId());
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
			for(BusinessGroupMemberPO removeMember : removeMembers){
				removeMembersNames.append(removeMember.getName()).append("，");
				removeMemberIdsJSONArray.add(removeMember.getId().toString());
			}
			removeMembersNames.append("被从 ").append(group.getName()).append(" 中移除");

			//如果是删人就给被删的成员发消息
			//进会的成员，需要关闭编码
			List<BusinessGroupMemberPO> connectRemoveMembers = new ArrayList<BusinessGroupMemberPO>();
			for(BusinessGroupMemberPO removeMember : removeMembers){

				if(GroupMemberStatus.CONNECT.equals(removeMember.getGroupMemberStatus())){
					connectRemoveMembers.add(removeMember);
				}

				//会议进行中，给退出成员发消息
				if(!group.getStatus().equals(GroupStatus.STOP)){

//					//如果操作人在本系统
//					if(!OriginType.OUTER.equals(chairmanMember.getOriginType())){

					//如果退出人在本系统，websocket通知
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
										GroupBO groupBO = commandCascadeUtil.exitAgendaCommandResponse(group, removeMember, "1");
										commandCascadeService.exitResponse(groupBO);
								}else if(BusinessType.MEETING_BVC.equals(businessType)
										|| BusinessType.MEETING_BVC.equals(businessType)){
										GroupBO groupBO = commandCascadeUtil.exitAgendaMeetingResponse(group, removeMember, "1");
										conferenceCascadeService.exitResponse(groupBO);
								}
							}

							// “成员主动退出通知”给所有其它系统
							if(BusinessType.COMMAND.equals(businessType)){
									GroupBO groupBO = commandCascadeUtil.exitAgendaCommand(group, removeMember);
									commandCascadeService.exit(groupBO);
							}else if(BusinessType.MEETING_QT.equals(businessType)
									|| BusinessType.MEETING_BVC.equals(businessType)){
									GroupBO groupBO = commandCascadeUtil.exitAgendaMeeting(group, removeMember);
									conferenceCascadeService.exit(groupBO);
							}
						}
						//如果是主席强退
						else{
							// “主席强制退出通知”给所有其它系统
							if(BusinessType.COMMAND.equals(businessType)){
									GroupBO groupBO = commandCascadeUtil.exitAgendaCommand(group, removeMember);
									commandCascadeService.kikout(groupBO);
							}else if(BusinessType.MEETING_QT.equals(businessType)
									|| BusinessType.MEETING_BVC.equals(businessType)){
									GroupBO groupBO = commandCascadeUtil.exitAgendaMeeting(group, removeMember);
									conferenceCascadeService.kikout(groupBO);
							}
						}
					}
//					}
				}
			}

			//描述信息，打印日志用
			StringBufferWrapper description = new StringBufferWrapper().append(group.getName());
			if(mode == 0){//退出
				description.append(removeMembers.get(0).getName()).append(" 成员退出");
			}else if(mode == 2){//删人
				description = removeMembersNames;
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

				for(BusinessGroupMemberPO member : members){

					//不给外部成员发
					if(OriginType.OUTER.equals(member.getOriginType())){
						continue;
					}

					//不给非用户成员发
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

				//关闭编码通道（必须在持久化之前，因为closeEncoder用到了member.getGroup()）
				CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();
				LogicBO logic = groupMemberService.closeEncoder(connectRemoveMembers, codec, -1L, chairmanMember.getCode());

				//处理角色
//				Map<Long, List<BusinessGroupMemberPO>> roleIdDeleteMembersMap = new HashMap<Long, List<BusinessGroupMemberPO>>();
//				roleIdDeleteMembersMap.put(null, removeMembers);
//				roleExecuteService.modifyRoleMembers(group, roleIdDeleteMembersMap, businessDeliverBO, false);

				if(businessReturnService.getSegmentedExecute()){
					businessReturnService.add(logic, null, null);
				}else{
					executeBusiness.execute(logic, group.getName() + " " + description);
				}
			}

			//持久化
			if(mode == 0){
				//成员主动退出
				for(BusinessGroupMemberPO member : connectRemoveMembers){
					member.setGroupMemberStatus(GroupMemberStatus.DISCONNECT);
				}
				businessGroupMemberDao.save(connectRemoveMembers);
			}else if(mode == 2){
				//重新执行一次议程
				RunningAgendaPO runningAgenda = runningAgendaDao.findByGroupId(groupId);
				if(runningAgenda!=null){
					//删除内置议程中的轮询
					List<AgendaForwardPO> agendaForwardPOs=agendaForwardDAO.findByAgendaId(runningAgenda.getAgendaId());
					for (AgendaForwardPO agendaForwardPO:agendaForwardPOs) {
						List<AgendaForwardSourcePO> agendaForwardSources=agendaForwardSourceDao.findByAgendaForwardId(agendaForwardPO.getId());
						if(agendaForwardSources!=null&&agendaForwardSources.size()==1){
							AgendaForwardSourcePO sourcePO=agendaForwardSources.get(0);
							if(sourcePO.getIsLoop()){
								String uid=combineVideoService.generateCombineVideoUid(groupId,agendaForwardSources,null,2);
								List<BusinessCombineVideoPO> combineVideoPOs=combineVideoDAO.findByCombineVideoUid(uid);
								if(combineVideoPOs!=null&&combineVideoPOs.size()>0){
									if(businessDeliverBO.getStopCombineVideos()==null){
										businessDeliverBO.setStopCombineVideos(new HashSet<>());
									}
									businessDeliverBO.getStopCombineVideos().add(combineVideoPOs.get(0));
									combineVideoDAO.delete(combineVideoPOs.get(0).getId());
									//删除对应的转发任务
									List<PageTaskPO> pageTaskPOs=pageTaskDao.findByCombineVideoUuid(uid);
									if(businessDeliverBO.getStopPageTasks()==null){
										businessDeliverBO.setStopPageTasks(new HashSet<>());
									}
									businessDeliverBO.getStopPageTasks().addAll(pageTaskPOs);
									List<Long> deleteTaskIds=pageTaskPOs.stream().map(pageTask->{
										return pageTask.getId();
									}).collect(Collectors.toList());
									pageTaskDao.deleteByIdIn(deleteTaskIds);
								}

							}
						}
					}
				}


				//主席强退：删角色关系，删合屏源，删混音源，删目的，最后删人（否则可能影响查询）

				groupMemberRolePermissionDao.deleteByGroupMemberIdIn(removeMemberIds);

				List<AgendaForwardSourcePO> agendaForwardSources = agendaForwardSourceDao.findAgendaForwardSourcesByMemberIds(removeMemberIds);
				agendaForwardSourceDao.deleteInBatch(agendaForwardSources);

				List<CustomAudioPO> customAudios = customAudioDao.findCustomAudiosByMemberIds(removeMemberIds);
				customAudioDao.deleteInBatch(customAudios);

				List<AgendaForwardDestinationPO> dsts = agendaForwardDestinationDao.findByDestinationIdInAndDestinationType(removeMemberIds, DestinationType.GROUP_MEMBER);
				agendaForwardDestinationDao.deleteInBatch(dsts);

				for(BusinessGroupMemberPO member : removeMembers){
					member.setGroup(null);
				}
				group.getMembers().removeAll(removeMembers);
				groupDao.save(group);
			}

			//会议进行中，发协议
			if(!group.getStatus().equals(GroupStatus.STOP)){

				//录制更新
//				LogicBO logicRecord = commandRecordServiceImpl.update(group.getUserId(), group, 1, false);
//				logic.merge(logicRecord);
//
//				ExecuteBusinessReturnBO returnBO = executeBusiness.execute(logic, description.toString());
//				commandRecordServiceImpl.saveStoreInfo(returnBO, group.getId());

				//停止所有给这些成员的媒体转发
//				List<GroupDemandPO> demands = groupDemandDao.findByDstMemberIdIn(memberIdList);
//				groupDemandService.stopDemands(group, demands, false);

				//给退出成员解绑所有的角色。后续改成批量
//				for(BusinessGroupMemberPO member : connectRemoveMembers){
//					List<GroupMemberRolePermissionPO> ps = groupMemberRolePermissionDao.findByGroupMemberId(member.getId());
//					List<Long> removeRoleIds = businessCommonService.obtainGroupMemberRolePermissionPOIds(ps);
//					agendaExecuteService.modifyMemberRole(groupId, member.getId(), null, removeRoleIds, false);
//				}

				//重新执行一次议程
				RunningAgendaPO runningAgenda = runningAgendaDao.findByGroupId(groupId);
				if(runningAgenda != null){
					agendaExecuteService.runAgenda(groupId, runningAgenda.getAgendaId(), businessDeliverBO, false);
				}
			}

			//级联：如果有老节点，则给它停会删会；业务停止时给剩余成员的所有节点发update
			if(!OriginType.OUTER.equals(group.getOriginType())){

				//最终的用户列表

				List<MinfoBO> orgMemberInfos = commandCascadeUtil.generateAgendaMinfoBOList(members);

				List<MinfoBO> finalMemberInfos =new ArrayList<MinfoBO>();
				for(MinfoBO minfo : orgMemberInfos){
					boolean needRemove = false;
					for(BusinessGroupMemberPO removerMember : removeMembers){
						if(removerMember.getCode().equals(minfo.getMid())){
							needRemove = true;
						}
					}
					if(!needRemove){
						finalMemberInfos.add(minfo);
					}
				}
				//不再参与业务的节点上的用户列表
				List<MinfoBO> oldNodeMemberInfos = commandCascadeUtil.filterAddedNodeMinfo(finalMemberInfos, orgMemberInfos);

				if(BusinessType.COMMAND.equals(group.getBusinessType())){

					//业务停止时给剩余成员的所有节点发update
					if(group.getStatus().equals(GroupStatus.STOP)){
						GroupBO groupBO = commandCascadeUtil.updateAgendaCommand(group);
						commandCascadeService.update(groupBO);
					}

					//如果有老节点，则给它停会删会
					if(oldNodeMemberInfos.size() > 0){

						if(!group.getStatus().equals(GroupStatus.STOP)){
							//停指挥
							Thread.sleep(300);//延时一下，确保其它节点删人操作已完成
							GroupBO groupBO1 = commandCascadeUtil.stopAgendaCommand(group);
							groupBO1.setMlist(oldNodeMemberInfos);
							commandCascadeService.stop(groupBO1);
						}

						//删除
						Thread.sleep(300);//延时一下，确保其它节点上一步操作已完成
						GroupBO groupBO = commandCascadeUtil.deleteAgendaCommand(group);
						groupBO.setMlist(oldNodeMemberInfos);
						commandCascadeService.delete(groupBO);
						log.info(oldNodeMemberInfos.size() + "个成员所在的节点不再参与指挥，已删除节点上的指挥：" + group.getName());
					}
				}else if(BusinessType.MEETING_BVC.equals(group.getBusinessType())){

					//业务停止时给剩余成员的所有节点发update
					if(group.getStatus().equals(GroupStatus.STOP)){
						GroupBO groupBO = commandCascadeUtil.updateAgendaMeeting(group);
						conferenceCascadeService.update(groupBO);
					}

					//如果有老节点，则给它停会删会
					if(oldNodeMemberInfos.size() > 0){

						if(!group.getStatus().equals(GroupStatus.STOP)){
							//停会
							Thread.sleep(300);//延时一下，确保其它节点删人操作已完成
							GroupBO groupBO2 = commandCascadeUtil.stopAgendaMeeting(group);
							groupBO2.setMlist(oldNodeMemberInfos);
							conferenceCascadeService.stop(groupBO2);
						}

						//删除
						Thread.sleep(300);//延时一下，确保其它节点上一步操作已完成
						GroupBO groupBO = commandCascadeUtil.deleteAgendaCommand(group);
						groupBO.setMlist(oldNodeMemberInfos);
						conferenceCascadeService.delete(groupBO);
						log.info(oldNodeMemberInfos.size() + "个成员所在的节点不再参与会议，已删除节点上的会议：" + group.getName());
					}
				}
			}

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

			String desc = null;
			if(mode ==2){
				desc = removeMembersNames.toString();
			}else{
				desc = removeMembers.get(0).getName() + " 成员退出";
			}
			deliverExecuteService.execute(businessDeliverBO, desc, true);
			operationLogService.send(user.getNickname(), "成员退出", user.getNickname() + "成员退出groupId:" + groupId + "memberIdList:" + memberIdList.toString());
			//所有情况都给主席返回chairSplits
			//if(mode == 0) return exitMemberSplits;
			return chairSplits;
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void exitApplyDisagree(Long userId, Long groupId, List<Long> memberIds) throws Exception{
		UserVO user = userQuery.current();
		
		if(groupId==null || groupId.equals("")){
			log.info("拒绝成员退出，会议id有误");
			return;
		}
		
		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
			
			GroupPO group = groupDao.findOne(groupId);
			BusinessType groupType = group.getBusinessType();
			
			if(group.getStatus().equals(GroupStatus.STOP) || memberIds.size()==0){
				return;
			}
			
			List<Long> consumeIds = new ArrayList<Long>();
			List<MessageSendCacheBO> messageCaches = new ArrayList<MessageSendCacheBO>();			
			List<BusinessGroupMemberPO> members = group.getMembers();
			BusinessGroupMemberPO chairmanMember = tetrisBvcQueryUtil.queryChairmanMember(members);
			List<BusinessGroupMemberPO> exitMembers = tetrisBvcQueryUtil.queryMembersByIds(members, memberIds);
			JSONObject message = new JSONObject();
			message.put("businessType", "exitApplyDisagree");
			message.put("businessInfo", "主席不同意您退出");
			message.put("businessId", group.getId().toString());
			for(BusinessGroupMemberPO exitMember : exitMembers){
				
				//如果退出人在本系统，websocket通知
				if(!OriginType.OUTER.equals(exitMember.getOriginType())){
					messageCaches.add(new MessageSendCacheBO(Long.parseLong(exitMember.getOriginId()), message.toJSONString(), WebsocketMessageType.COMMAND));								
				}
				
				//如果操作人在本系统
				if(!OriginType.OUTER.equals(chairmanMember.getOriginType())){
					
					//如果退出人在外部系统，级联通知
					/*if(OriginType.OUTER.equals(exitMember.getOriginType())){
						if(GroupType.BASIC.equals(groupType)){
							GroupBO groupBO = commandCascadeUtil.exitCommandResponse(group, exitMember, "0");
							commandCascadeService.exitResponse(groupBO);
						}else if(GroupType.MEETING.equals(groupType)){
							GroupBO groupBO = commandCascadeUtil.exitMeetingResponse(group, exitMember, "0");
							conferenceCascadeService.exitResponse(groupBO);
						}
					}*/
				}
			}			
			
			if(businessReturnService.getSegmentedExecute()){
				for(MessageSendCacheBO cache : messageCaches){
					businessReturnService.add(null, cache, group.getName() + " 主席拒绝了 " + exitMembers.get(0).getName() + " 等人退出");
				}
				
				businessReturnService.execute();
			}else{
				for(MessageSendCacheBO cache : messageCaches){
					WebsocketMessageVO ws = websocketMessageService.send(cache.getUserId(), cache.getMessage(), cache.getType());
					consumeIds.add(ws.getId());
				}
				websocketMessageService.consumeAll(consumeIds);
				
				log.info(group.getName() + " 主席拒绝了 " + exitMembers.get(0).getName() + " 等人退出");
			}
			
		}
		operationLogService.send(user.getNickname(), "拒绝申请退出", user.getNickname() + "拒绝申请退出groupId:" + groupId + ", memberIds" + memberIds.toString());
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
	 * 创建会议/指挥<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月25日 下午4:46:46
	 * @param creatorUserId 创建人id
	 * @param creatorUsername 创建人名称
	 * @param chairmanType null或self为默认，以创建人的用户作为主席；user指定用户为主席；hall指定会场为主席；tvosOfUser以创建人的机顶盒为主席
	 * @param chairmanId 用户id/会场id
	 * @param name 会议名称
	 * @param subject 主题，可以与name相同
	 * @param groupBusinessType
	 * @param originType
	 * @param userIdList 用户成员id列表
	 * @param hallIds 会场成员id列表
	 * @param bundleIdList 设备成员（暂不支持）
	 * @param uuid 级联调用会传递uuid
	 * @param orderGroupType 预约会议类型BusinessOrderGroupType
	 * @param orderBeginTime 预约开始时间
	 * @param orderEndTime 预约结束时间
	 * @param groupLock 锁定会议 GroupLock
	 */
	@Transactional(rollbackFor = Exception.class)
	public GroupPO saveAndStartTemporary(
			Long creatorUserId,
			String creatorUsername,
			String chairmanType,
			String chairmanId,
			String name,
			String subject,
			BusinessType groupBusinessType,
			OriginType originType,
			List<Long> userIdList,
			List<Long> hallIds,
			List<String> bundleIdList,
			String uuid,
			String orderGroupType,
			String orderBeginTime,
			String orderEndTime,
			String groupLock
			) throws Exception{
		
		//创建和执行两个地方只需要一个地方发起级联
		GroupPO group = saveAndStartTemporaryCommand(creatorUserId, creatorUsername, chairmanType, chairmanId, name, subject, groupBusinessType, originType, userIdList, hallIds,
													 bundleIdList, uuid, orderGroupType, orderBeginTime, orderEndTime, groupLock, EditStatus.TEMP);
		
		return group;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public GroupPO saveCommand_TS(
			Long creatorUserId,
			String creatorUsername,
			String chairmanType,
			String chairmanId,
			String name,
			String subject,
			BusinessType groupBusinessType,
			OriginType originType,
			List<Long> userIdList,
			List<Long> hallIds,
			List<String> bundleIdList,
			String uuid,
			String orderGroupType,
			String orderBeginTime,
			String orderEndTime,
			String groupLock,
			EditStatus editStatus
			) throws Exception{
		if(editStatus == null)editStatus = EditStatus.NORMAL;
		return saveCommand(creatorUserId, creatorUsername, chairmanType, chairmanId, name, subject, groupBusinessType, originType, userIdList, hallIds, bundleIdList, uuid, 
						   orderGroupType, orderBeginTime, orderEndTime, groupLock, editStatus);
	}
	
	/**
	 * 创建会议/指挥<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月25日 下午4:46:46
	 * @param creatorUserId 创建人id
	 * @param creatorUsername 创建人名称
	 * @param chairmanType null或self为默认，以创建人的用户作为主席；user指定用户为主席；hall指定会场为主席；tvosOfUser以创建人的机顶盒为主席
	 * @param chairmanId 用户id/会场id
	 * @param name 会议名称
	 * @param subject 主题，可以与name相同
	 * @param groupBusinessType
	 * @param originType
	 * @param userIdList 用户成员id列表
	 * @param hallIds 会场成员id列表
	 * @param bundleIdList 设备成员（暂不支持）
	 * @param uuid 级联调用会传递uuid
	 * @param orderGroupType 预约会议类型BusinessOrderGroupType
	 * @param orderBeginTime 预约开始时间
	 * @param orderEndTime 预约结束时间
	 * @param groupLock 锁定会议 GroupLock
	 * @param EditStatus枚举类参数区别是否是临时会议/指挥
	 */
	public GroupPO saveCommand(
			Long creatorUserId,
			String creatorUsername,
			String chairmanType,
			String chairmanId,
			String name,
			String subject,
			BusinessType groupBusinessType,
			OriginType originType,
			List<Long> userIdList,
			List<Long> hallIds,
			List<String> bundleIdList,
			String uuid,
			String orderGroupType,
			String orderBeginTime,
			String orderEndTime,
			String groupLock,
			EditStatus editStatus
			) throws Exception{
		
		TerminalPO terminal = terminalDao.findByType(com.sumavision.tetris.bvc.model.terminal.TerminalType.QT_ZK);
		
		//确定主席
		MemberTerminalBO chairmanBO = null;		
		if(chairmanType == null) chairmanType = "self";
		switch (chairmanType){
		case "user":
			chairmanBO = new MemberTerminalBO()
				.setGroupMemberType(GroupMemberType.MEMBER_USER)
				.setOriginId(chairmanId)
				.setTerminalId(terminal.getId());
			break;
		case "hall":
			ConferenceHallPO conferenceHall = conferenceHallDao.findOne(Long.valueOf(chairmanId));
			chairmanBO = new MemberTerminalBO()
				.setGroupMemberType(GroupMemberType.MEMBER_HALL)
				.setOriginId(chairmanId)
				.setTerminalId(conferenceHall.getTerminalId());
			break;
		case "tvosOfUser":
			TerminalPO tvosT = terminalDao.findByType(com.sumavision.tetris.bvc.model.terminal.TerminalType.ANDROID_TVOS);
			Long hallId = findHallId(creatorUserId, "tvos");
			chairmanBO = new MemberTerminalBO()
					.setGroupMemberType(GroupMemberType.MEMBER_HALL)
					.setOriginId(hallId.toString())
					.setTerminalId(tvosT.getId());
			break;
		case "self":
		default:
			chairmanBO = new MemberTerminalBO()
				.setGroupMemberType(GroupMemberType.MEMBER_USER)
				.setOriginId(creatorUserId.toString())
				.setTerminalId(terminal.getId());
			break;			
		}		
		
		//用户成员
		List<MemberTerminalBO> memberTerminalBOs = new ArrayList<MemberTerminalBO>();
		for(Long userId : userIdList){
			MemberTerminalBO memberBO = new MemberTerminalBO()
					.setGroupMemberType(GroupMemberType.MEMBER_USER)
					.setOriginId(userId.toString())
					.setTerminalId(terminal.getId());
			memberTerminalBOs.add(memberBO);
		}
		
		//会场成员和设备成员
		List<ConferenceHallPO> halls = conferenceHallDao.findAll(hallIds);
		List<BundlePO> bundlePOs = bundleDao.findByBundleIdIn(bundleIdList);
		List<ConferenceHallPO> bundleHalls = conferenceHallService.bundleExchangeToHall(bundlePOs);
		halls.addAll(bundleHalls);
		
		for(ConferenceHallPO hall : halls){
			MemberTerminalBO memberBO = new MemberTerminalBO()
					.setGroupMemberType(GroupMemberType.MEMBER_HALL)
					.setOriginId(hall.getId().toString())
					.setTerminalId(hall.getTerminalId())
					.setFromDevice(hall.getFromDevice());
			memberTerminalBOs.add(memberBO);
		}
		
		return save(
				creatorUserId,
				chairmanBO,
				name,
				subject,
				groupBusinessType,
				originType,
				memberTerminalBOs,
				uuid,
				orderGroupType,
				orderBeginTime,
				orderEndTime,
				groupLock,
				editStatus
				);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public GroupPO save_TS(
			Long creatorUserId,
			MemberTerminalBO chairmanBO,
			String name,
			String subject,
			BusinessType groupBusinessType,
			OriginType originType,
			List<MemberTerminalBO> memberTerminalBOs,
			String uuid,
			String orderGroupType,
			String orderBeginTime,
			String orderEndTime,
			String groupLock,
			EditStatus editStatus
			) throws Exception{
		if(editStatus == null)editStatus = EditStatus.NORMAL;
		return save(creatorUserId, chairmanBO, name, subject, groupBusinessType, originType, memberTerminalBOs, uuid, orderGroupType, orderBeginTime, orderEndTime, groupLock, editStatus);
	}
	
	/**
	 * 建会<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月13日 下午1:20:13
	 * @param creatorUserId 创建人id
	 * @param chairmanBO 会议主席
	 * @param name 会议名称
	 * @param subject 主题，可以与name相同
	 * @param groupBusinessType 会议类型
	 * @param originType 内部系统/外部系统
	 * @param memberTerminalBOs 参会成员和会场
	 * @param uuid 级联调用会传递uuid
	 * @param orderGroupType 预约会议类型BusinessOrderGroupType
	 * @param orderBeginTime 预约开始时间
	 * @param orderEndTime 预约结束时间
	 * @param groupLock 锁定会议 GroupLock
	 */
	public GroupPO save(
			Long creatorUserId,
			MemberTerminalBO chairmanBO,
			String name,
			String subject,
			BusinessType groupBusinessType,
			OriginType originType,
			List<MemberTerminalBO> memberTerminalBOs,
			String uuid,
			String orderGroupType,
			String orderBeginTime,
			String orderEndTime,
			String groupLock,
			EditStatus editStatus
			) throws Exception{
//		groupDao.deleteAll();
		GroupPO group1 = groupDao.findByUuid(uuid);
		if(group1 != null){
			log.warn("级联建会，uuid已经存在：" + uuid);
			return group1;
		}
		
		//校验
		UserBO creatorUserBo = resourceService.queryUserById(creatorUserId, TerminalType.QT_ZK);
		if(creatorUserBo == null){
			throw new BaseException(StatusCode.FORBIDDEN, "当前用户已失效，请重新登录");
		}
		if(creatorUserBo.getFolderUuid() == null){
//			throw new UserHasNoFolderException(creatorUserBo.getName());
		}
		
		//确保成员中有创建者
		if(!memberTerminalBOs.contains(chairmanBO)){
			memberTerminalBOs.add(chairmanBO);
		}
		
		//本系统创建，则鉴权，则校验主席有编码器
		if(!OriginType.OUTER.equals(originType)){
			
			//鉴权，区分指挥与会议
//			if(type.equals(GroupType.BASIC)){
//				commandCommonServiceImpl.authorizeUsers(userIdList, chairmanUserId, BUSINESS_OPR_TYPE.ZK);
//			}else if(type.equals(GroupType.MEETING)){
//				commandCommonServiceImpl.authorizeUsers(userIdList, chairmanUserId, BUSINESS_OPR_TYPE.HY);
//			}
			
//			String creatorEncoderId = commonQueryUtil.queryExternalOrLocalEncoderIdFromUserBO(creatorUserBo);
//			List<BundlePO> creatorBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(creatorEncoderId).getList());
//			if(creatorBundleEntities.size() == 0){
//				throw new UserHasNoAvailableEncoderException(creatorUserBo.getName());
//			}
		}
		
		//会议组重名校验 
		//出现重名时解决策略是找到一个没有被使用的"name-数字"。
		if(!OriginType.OUTER.equals(originType)){
			List<GroupPO> existGroups = groupDao.findByName(name);
			if(existGroups!=null && existGroups.size()>0){
				List<String> likeGroupNames = groupDao.findByNameLike(name + "-%").stream().map(GroupPO::getName).collect(Collectors.toList());
				String recommendedName0 = name + "-";
				String recommendedName = null;
				for(int i=2; ; i++){
					recommendedName = recommendedName0 + i;
					if(!likeGroupNames.contains(recommendedName)){
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
		group.setUserCode(creatorUserBo.getUserNo());
		group.setCreatetime(new Date());
		if(groupLock != null){
			group.setGroupLock(GroupLock.valueOf(groupLock));
		}
		group.setGroupNumber(generateGroupNumber());
		
		group.setOrderGroupType(orderGroupType==null?null:BusinessOrderGroupType.valueOf(orderGroupType));
		if(group.getOrderGroupType() != null){
			group.setOrderBeginTime(DateUtil.parse(orderBeginTime, DateUtil.dateTimePattern));
			group.setOrderEndTime(DateUtil.parse(orderEndTime, DateUtil.dateTimePattern));
			group.setExecuteGroup(true);
		}
		
		group.setStatus(GroupStatus.STOP);
		if(editStatus == null){
			editStatus = EditStatus.NORMAL;
		}
		group.setEditStatus(editStatus);
		
		groupDao.save(group);
		
		//组建会议成员
		List<BusinessGroupMemberPO> members = groupMemberService.generateMembers(group, memberTerminalBOs, chairmanBO);
		businessGroupMemberDao.save(members);
		groupMemberService.fullfillGroupMember(members);
		
		//授予角色
		BusinessType businessType = group.getBusinessType();
		List<GroupMemberRolePermissionPO> pers = new ArrayList<GroupMemberRolePermissionPO>();
		if(BusinessType.MEETING_BVC.equals(businessType)) {
			RolePO memberRole = roleDao.findByInternalRoleType(InternalRoleType.MEETING_AUDIENCE);
			for(BusinessGroupMemberPO member : members){
				//主席成员只有主席角色
				if(member.getIsAdministrator()){
					RolePO chairmanRole = businessCommonService.queryGroupChairmanRole(group);
					member.setRoleId(chairmanRole.getId());
					member.setRoleName(chairmanRole.getName());
					GroupMemberRolePermissionPO memberRolePermission = new GroupMemberRolePermissionPO(chairmanRole.getId(), member.getId());
					pers.add(memberRolePermission);
				}else{
					if(member.getRoleId() == null){
						member.setGroupMemberStatus(GroupMemberStatus.DISCONNECT);
						member.setRoleId(memberRole.getId());
						member.setRoleName(memberRole.getName());
						GroupMemberRolePermissionPO memberRolePermission = new GroupMemberRolePermissionPO(memberRole.getId(), member.getId());
						pers.add(memberRolePermission);
					}
				}
			}
		}else if (BusinessType.COMMAND.equals(businessType)) {
			List<RolePO> allRole = roleDao.findAll();
			List<RolePO> levelRoles = allRole.stream().filter(role ->{
				return role.getInternalRoleType() != null && BaseUtils.stringIsNotBlank(role.getInternalRoleType().getLevel());
			}).collect(Collectors.toList());
			Map<String, RolePO> levelAndRoleMap = levelRoles.stream().filter(role->{
				return BaseUtils.stringIsNotBlank(role.getInternalRoleType().getLevel());
			}).collect(Collectors.toMap(role-> (String)role.getInternalRoleType().getLevel(), role-> role));
			for(BusinessGroupMemberPO member : members){
//				//主席成员只有主席角色
//				if(member.getIsAdministrator()){
//					RolePO chairmanRole = businessCommonService.queryGroupChairmanRole(group);
//					member.setRoleId(chairmanRole.getId());
//					member.setRoleName(chairmanRole.getName());
//					GroupMemberRolePermissionPO memberRolePermission = new GroupMemberRolePermissionPO(chairmanRole.getId(), member.getId());
//					pers.add(memberRolePermission);
//				}else{
//					if(member.getRoleId() == null){
//						member.setGroupMemberStatus(GroupMemberStatus.DISCONNECT);
//						member.setRoleId(memberRole.getId());
//						member.setRoleName(memberRole.getName());
//						GroupMemberRolePermissionPO memberRolePermission = new GroupMemberRolePermissionPO(memberRole.getId(), member.getId());
//						pers.add(memberRolePermission);
//					}
//				}
				RolePO memberRole = levelAndRoleMap.get(member.getLevel().toString());
				member.setGroupMemberStatus(GroupMemberStatus.DISCONNECT);
				member.setRoleId(memberRole.getId());
				member.setRoleName(memberRole.getName());
				GroupMemberRolePermissionPO memberRolePermission = new GroupMemberRolePermissionPO(memberRole.getId(), member.getId());
				pers.add(memberRolePermission);
			}
		}
		
		groupMemberRolePermissionDao.save(pers);
		
		//给所有成员，没有PageInfoPO的，建立PageInfoPO
		groupMemberService.generateMembersPageInfo(members);
		
		/*//查询所有成员pageInfo是否存在，否为成员创建pageInfo
		List <PageInfoPO> addPageInfos = new ArrayList<PageInfoPO>();
		for(MemberTerminalBO memberTerminalBO : memberTerminalBOs){
			if(null == pageInfoDAO.findByOriginIdAndTerminalIdAndGroupMemberType(
					memberTerminalBO.getOriginId(),
					memberTerminalBO.getTerminalId(),
					memberTerminalBO.getGroupMemberType())){
				PageInfoPO pageInfo = new PageInfoPO(
						memberTerminalBO.getOriginId(), 
						memberTerminalBO.getTerminalId(),
						memberTerminalBO.getGroupMemberType());
				if(memberTerminalBO.getGroupMemberType().equals(GroupMemberType.MEMBER_USER)){
					pageInfo.setPageSize(16);
				}
				addPageInfos.add(pageInfo);
			}
		}		
		pageInfoDAO.save(addPageInfos);*/
		
		//参数模板
		/*Map<String, Object> result = commandCommonServiceImpl.queryDefaultAvCodec();
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
//		group.setCurrentGearLevel(currentGear.getLevel());*/
		
		//拷贝参数模板
		DeviceGroupAvtplPO g_avtpl = generateDeviceGroupAvtpl();
		group.setAvtpl(g_avtpl);
		g_avtpl.setBusinessGroup(group);
		
		//级联
		if(!OriginType.OUTER.equals(originType)){
		    if(BusinessType.COMMAND.equals(groupBusinessType)){
				GroupBO groupBO = commandCascadeUtil.createAgendaCommand(group);
				commandCascadeService.create(groupBO);
			}else if(BusinessType.MEETING_BVC.equals(groupBusinessType)){
				GroupBO groupBO = commandCascadeUtil.createAgendaCommand(group);
				conferenceCascadeService.create(groupBO);
			}
		}
		
		groupDao.save(group);
		
		log.info(name + " 创建完成");
		operationLogService.send(creatorUserBo.getName(), "新建指挥", creatorUserBo.getName() + "新建指挥groupId:" + group.getId());
		return group;
	}
	
	/**
	 * 修改会议组信息名称<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月5日 下午2:24:36
	 * @param groupId 会议组id
	 * @param name 名称
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public GroupPO edit(Long groupId,String name,String groupLock){
		GroupPO groupPO=groupDao.findOne(groupId);
		if(name != null){
			groupPO.setName(name);
		}
		if(groupLock != null){
			groupPO.setGroupLock(GroupLock.valueOf(groupLock));
		}else{
			groupPO.setGroupLock(null);
		}
		
		groupDao.save(groupPO);
		return groupPO;
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
	@Transactional(rollbackFor = Exception.class)
	public Object start(Long groupId, int locationIndex) throws Exception{
		return start(groupId, locationIndex, false, null, null, null);
	}
	
	/**
	 * 开启指挥/会议<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月25日 上午10:03:50
	 * @param groupId
	 * @param locationIndex 指定播放器序号，序号从0起始；-1为自动选择。仅在专向会议中可以指定序号；普通会议中必须为-1。
	 * @param refresh 是否刷新，默认false
	 * @param enterMemberIds 默认null。指定进入会议的userId列表，注意应该包含主席
	 * @param startTime 默认null。指定会议开启时间（通常在全量信息同步时）
	 * @param groupStatus 默认null。指定会议当前状态，取值为START/PAUSE，其它不支持（通常在全量信息同步时）
	 * @return
	 * @throws Exception
	 */
	public Object start(Long groupId, int locationIndex, boolean refresh, List<Long> enterMemberIds, Date startTime, GroupStatus groupStatus) throws Exception{
		
		JSONObject result = new JSONObject();
		JSONArray chairSplits = new JSONArray();
		
		if(groupId == null){
			log.warn("开始会议，会议id有误");
			return result;
		}
		
		synchronized (new StringBuffer().append(lockProcessPrefix).append(groupId).toString().intern()) {
						
			GroupPO group = groupDao.findOne(groupId);
			
//			//根据version判断是否需要执行
//			if(BusinessOrderGroupType.COMMON_ORDER.equals(group.getOrderGroupType())){
//				if(group.getVersion() != 0){
//					return result;
//				}else{
//					group.setVersion(1);
//					groupDao.save(group);
//				}
//			}
			
			BusinessType groupType = group.getBusinessType();
			List<BusinessGroupMemberPO> members = businessGroupMemberDao.findByGroupId(groupId);
			BusinessDeliverBO businessDeliverBO = new BusinessDeliverBO().setUserId(group.getUserId().toString()).setGroup(group);
			List<MessageSendCacheBO> messageCaches = new ArrayList<MessageSendCacheBO>();
			
			//1.成员设置为QT,断开组成员的所有关联关系,重新组建关系（取消，后续做重置功能）
			/*Long terminalId = terminalDao.findByType(com.sumavision.tetris.bvc.model.terminal.TerminalType.QT_ZK).getId();
			for(BusinessGroupMemberPO member : members){
				if(GroupMemberType.MEMBER_USER.equals(member.getGroupMemberType())){
					member.setTerminalId(terminalId);
					if(BaseUtils.collectionIsNotBlank(member.getTerminalBundles())){
						member.getTerminalBundles().clear();
					}
					if(BaseUtils.collectionIsNotBlank(member.getChannels())){
						member.getChannels().clear();
					}
					if(BaseUtils.collectionIsNotBlank(member.getScreens())){
						member.getScreens().clear();
					}
					if(BaseUtils.collectionIsNotBlank(member.getAudioOutputs())){
						member.getAudioOutputs().clear();
					}
				}
			}
			fullfillGroupMember(members);*/
			
			
			//已修改为重构后逻辑
//			List<Long> userIdList = new ArrayList<Long>();
//			for(BusinessGroupMemberPO member : members){
//				if(GroupMemberType.MEMBER_USER.equals(member.getGroupMemberType())){
//					userIdList.add(Long.valueOf(member.getOriginId()));
//				}
//			}
//			
//			//本系统创建的，则鉴权，区分指挥与会议
//			if(!OriginType.OUTER.equals(group.getOriginType())){
//				if(groupType.equals(GroupType.BASIC)){
//					commandCommonServiceImpl.authorizeUsers(userIdList, group.getUserId(), BUSINESS_OPR_TYPE.ZK);
//				}else if(groupType.equals(GroupType.MEETING)){
//					commandCommonServiceImpl.authorizeUsers(userIdList, group.getUserId(), BUSINESS_OPR_TYPE.HY);
//				}
//			}
			
			//普通指挥、会议，刷新会议数据
			if(!groupType.equals(GroupType.SECRET) && refresh){
	//			refresh(group);
				groupMemberService.fullfillGroupMember(members);
			}
			
			//后需考虑支持重复开始
			if(!group.getStatus().equals(GroupStatus.STOP)){
				result.put("splits", chairSplits);
				return result;
			}
			
			groupMemberService.resetGroupMember(groupId);
			
			if(GroupStatus.PAUSE.equals(groupStatus)){
				group.setStatus(groupStatus);
			}else{
				group.setStatus(GroupStatus.START);
			}
			if(startTime == null) startTime = new Date();
			group.setStartTime(startTime);		
			group.setExecuteGroup(false);
			
			//处理主席，置为CONNECT，绑定主席角色。其它成员的绑定角色放在membersResponse里边
			BusinessGroupMemberPO chairman = tetrisBvcQueryUtil.queryChairmanMember(members);
			chairman.setGroupMemberStatus(GroupMemberStatus.CONNECT);
			if(chairman.getRoleId() == null){
				RolePO chairmanRole = businessCommonService.queryGroupChairmanRole(group);
				chairman.setRoleId(chairmanRole.getId());
				chairman.setRoleName(chairmanRole.getName());
				GroupMemberRolePermissionPO memberRolePermission = new GroupMemberRolePermissionPO(chairmanRole.getId(), chairman.getId());
				groupMemberRolePermissionDao.save(memberRolePermission);
			}
									
			//自动接听则所有在线的人接听，否则只有主席接听；专向指挥只有主席接听
	//		String userIdListStr = StringUtils.join(userIdList.toArray(), ",");
	//		List<UserBO> commandUserBos = resourceService.queryUserListByIds(userIdListStr, TerminalType.QT_ZK);
			List<BusinessGroupMemberPO> acceptMembers = null;
			if(enterMemberIds != null){
				//如果指定了enterUserIds（通常是级联的全量信息同步），则使用
				acceptMembers = new ArrayList<BusinessGroupMemberPO>();
				for(BusinessGroupMemberPO member : members){
					if(enterMemberIds.contains(member.getId())){
						acceptMembers.add(member);
					}else{
						//不进入的，置为DISCONNECT
						member.setGroupMemberStatus(GroupMemberStatus.DISCONNECT);
					}
				}
			}
			if(autoEnter && !group.getBusinessType().equals(GroupType.SECRET)){
				//自动进入：给所有本系统成员自动进入（memberRespones方法中会推送meetingStartNow通知）
				acceptMembers = new ArrayListWrapper<BusinessGroupMemberPO>().add(chairman).getList();
				for(BusinessGroupMemberPO member : members){
					if(member.getIsAdministrator()) continue;
					//if(OriginType.INNER.equals(member.getOriginType())){
						acceptMembers.add(member);
					//}
	//				if(OriginType.OUTER.equals(member.getOriginType())){member.setMemberStatus(MemberStatus.DISCONNECT);continue;}//优化思路，外部系统的由其所在系统对其进行“进会”
	//				UserBO commandUserBo = queryUtil.queryUserById(commandUserBos, member.getUserId());
	//				if(commandUserBo.isLogined()){//TODO :该成员如果对应终端登录，则进会
//						acceptMembers.add(member);
	//				}else{
	//					//没在线的，置为DISCONNECT。不用按照“拒绝”处理
	//					member.setMemberStatus(MemberStatus.DISCONNECT);
	//				}
				}
			}else{
				//不自动进入：
				//给主席、设备成员自动进入
				acceptMembers = new ArrayList<BusinessGroupMemberPO>();
				for(BusinessGroupMemberPO member : members){
					if(chairman.equals(member)){
						acceptMembers.add(member);
					}else if(GroupMemberType.MEMBER_DEVICE.equals(member.getGroupMemberType())
							|| GroupMemberType.MEMBER_DEVICE.equals(member.getGroupMemberType())){
						acceptMembers.add(member);
					}
				}
				
				//给本系统其它成员推送meetingStart通知
				JSONObject message = new JSONObject();
				message.put("fromUserId", chairman.getOriginId());
				message.put("fromUserName", chairman.getName());
				message.put("businessId", group.getId().toString());
				String businessType = null;
				if(BusinessType.COMMAND.equals(group.getBusinessType())){
					businessType = "commandStart";//自动接听使用commandStartNow
				}else{
					businessType = "meetingStart";//自动接听使用meetingStartNow
				}
				message.put("businessType", businessType);
				message.put("businessInfo", "接受到 " + group.getName() + " 邀请，主席：" + chairman.getName() + "，是否进入？");
				
				//发送消息
				for(BusinessGroupMemberPO member : members){
					if(chairman.equals(member)){
						continue;
					}else if(GroupMemberType.MEMBER_USER.equals(member.getGroupMemberType())){
						messageCaches.add(new MessageSendCacheBO(Long.parseLong(member.getOriginId()), message.toJSONString(), WebsocketMessageType.COMMAND));
					}
				}				
			}
			groupDao.save(group);
			
			//呼叫编码器
			groupMemberService.membersResponse(group, members, acceptMembers);
			
			//执行默认议程
			AgendaPO commandAgenda = null;
			BusinessType businessType = group.getBusinessType();
			if(BusinessType.COMMAND.equals(businessType)){
				commandAgenda = agendaDao.findByBusinessInfoType(BusinessInfoType.BASIC_COMMAND);
			}else if(BusinessType.MEETING_QT.equals(businessType)
					|| BusinessType.MEETING_BVC.equals(businessType)){
				commandAgenda = agendaDao.findByBusinessInfoType(BusinessInfoType.BASIC_MEETING);
			}
			agendaExecuteService.runAgenda(groupId, commandAgenda.getId(), businessDeliverBO, false);
			
			//记录会议的信息
			groupProceedRecordServiceImpl.saveStart(group);
			
			//级联
			if(!OriginType.OUTER.equals(group.getOriginType())){
				if(BusinessType.COMMAND.equals(group.getBusinessType())){
					GroupBO groupBO = commandCascadeUtil.startAgendaCommand(group);
					commandCascadeService.start(groupBO);
				}else if(BusinessType.MEETING_BVC.equals(group.getBusinessType())){
					GroupBO groupBO = commandCascadeUtil.startAgendaCommand(group);
					conferenceCascadeService.start(groupBO);
				}
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
			
			String userName = "";
			try{
				UserVO user = userQuery.current();
				userName = user.getNickname();
			}catch(Exception e){
				List<UserVO> user = userQuery.findByIdIn(new ArrayListWrapper<Long>().add(group.getUserId()).getList());
				if(user != null && user.size() > 0){
					userName = user.get(0).getNickname();
				}
			}
			operationLogService.send(userName, "开启指挥", userName + "开启指挥groupId:" + groupId);
			
			//发消息，主要是“邀请”消息，自动接听则没有
			businessReturnService.dealWebsocket(messageCaches);
			
			deliverExecuteService.execute(businessDeliverBO, "开始会议：" + group.getName(), true);
			
			return result;
			
		}
	}
	
	

	/**
	 * 停止会议<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月16日 下午2:53:02
	 * @param userId 操作人
	 * @param groupId 业务组id
	 * @param stopMode 0为正常停止，1为拒绝专向会议导致的停止，该参数用来在专向会议中发送不同的消息
	 * @return chairSplits 主席需要关闭的播放器序号
	 */
	@Transactional(rollbackFor = Exception.class)
	public JSONArray stop(Long userId, Long groupId, int stopMode) throws Exception{
		
//		UserVO user = userQuery.current();
		
		//通常返回主席的屏幕；在专向会议中，由对方成员停止或拒绝，返回对方成员的屏幕
		JSONArray returnSplits = new JSONArray();
		JSONArray chairSplits = new JSONArray();
		
		if(groupId==null){
			log.info("停会操作，会议id有误");
			return chairSplits;
		}
		
		synchronized (new StringBuffer().append(lockProcessPrefix).append(groupId).toString().intern()) {
						
			GroupPO group = groupDao.findOne(groupId);
			if(group == null){
				log.info("进行停止操作的会议不存在，id：" + groupId);
				return new JSONArray();
			}
			
			BusinessDeliverBO businessDeliverBO = new BusinessDeliverBO().setUserId(group.getUserId().toString()).setGroup(group);
			
			BusinessType groupType = group.getBusinessType();
			String commandString = tetrisBvcQueryUtil.generateCommandString(groupType);
			if(group.getStatus().equals(GroupStatus.REMIND)){
				throw new BaseException(StatusCode.FORBIDDEN, "请先关闭" + commandString + "提醒");
			}
			
			if(group.getStatus().equals(GroupStatus.STOP)){
				log.warn(group.getName() + "被重复停止，直接退出");
				return new JSONArray();
			}
			
//			if(group.getEditStatus()==null || EditStatus.NORMAL.equals(group.getEditStatus())){
				group.setStatus(GroupStatus.STOP);
				Date endTime = new Date();
				group.setEndTime(endTime);
				List<BusinessGroupMemberPO> members = businessGroupMemberDao.findByGroupId(groupId);

				List<BusinessGroupMemberPO> connectMembers = new ArrayList<BusinessGroupMemberPO>();
				List<Long> consumeIds = new ArrayList<Long>();
				List<MessageSendCacheBO> messageCaches = new ArrayList<MessageSendCacheBO>();
				BusinessGroupMemberPO chairmanMember = tetrisBvcQueryUtil.queryChairmanMember(members);

				//处理所有成员，包括主席
				for(BusinessGroupMemberPO member : members){
					if(member.getGroupMemberStatus().equals(GroupMemberStatus.CONNECT)){
						connectMembers.add(member);
					}

					if(OriginType.OUTER.equals(member.getOriginType())
							|| !GroupMemberType.MEMBER_USER.equals(member.getGroupMemberType())){
						//状态重置（本系统用户的状态重置写在下头）
						member.setGroupMemberStatus(GroupMemberStatus.DISCONNECT);
						member.setSilenceToHigher(false);
						member.setSilenceToLower(false);
						continue;
					}

//					if(!GroupMemberType.MEMBER_USER.equals(member.getGroupMemberType())){
//						continue;
//					}

					//普通指挥/会议，给其它 会议中的 成员发通知
					if(!member.getIsAdministrator() && group.getBusinessType().equals(BusinessType.COMMAND)
							|| !member.getIsAdministrator() && group.getBusinessType().equals(BusinessType.MEETING_QT)
							|| !member.getIsAdministrator() && group.getBusinessType().equals(BusinessType.MEETING_BVC)){

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

					//状态重置（外部系统用户的状态重置写在上头）
					member.setGroupMemberStatus(GroupMemberStatus.DISCONNECT);
					member.setSilenceToHigher(false);
					member.setSilenceToLower(false);
				}

				//临时指挥不需要save
				if(group.getEditStatus()==null || EditStatus.NORMAL.equals(group.getEditStatus())){
					groupDao.save(group);
					businessGroupMemberDao.save(members);
				}
				
				//清除专向等特殊指挥
				groupSpecialCommonService.clearAllSpecialCommand(group);
//				GroupCommandInfoPO commandInfo = groupCommandInfoDao.findByGroupId(groupId);
//				if(commandInfo != null){
//					commandInfo.clearAll();
//					groupCommandInfoDao.save(commandInfo);
//				}

				//特殊业务（如媒体转发）的处理
				stopBusinessForwardByChairman(groupId);

				//停止所有的议程（最多只有1个）
				RunningAgendaPO runningAgenda = runningAgendaDao.findByGroupId(groupId);
				if(runningAgenda != null){
					agendaExecuteService.stopAllSrcAndDst(group, businessDeliverBO);
					runningAgendaDao.delete(runningAgenda);
				}

				//关闭编码通道,关闭录像、直播
				CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();
				LogicBO logic = groupMemberService.closeEncoder(connectMembers, codec, -1L, chairmanMember.getCode());
				LogicBO logicStopRecord = groupRecordService.stop(null, groupId, false);
				logic.merge(logicStopRecord);
				if(businessReturnService.getSegmentedExecute()){
					businessReturnService.add(logic, null, null);
				}else{
					executeBusiness.execute(logic, group.getName() + " 会议停止");
				}

				//记录会议的信息
				groupProceedRecordServiceImpl.saveStop(group);

				//级联
				BusinessGroupMemberPO opMember = tetrisBvcQueryUtil.queryBusinessMemberByUserId(members, userId);
				if(opMember != null && !OriginType.OUTER.equals(opMember.getOriginType())){
					if(BusinessType.COMMAND.equals(group.getBusinessType())){
						GroupBO groupBO = commandCascadeUtil.stopAgendaCommand(group);
						groupBO.setOp(opMember.getCode());
						commandCascadeService.stop(groupBO);
					}else if(BusinessType.MEETING_BVC.equals(group.getBusinessType())){
						GroupBO groupBO = commandCascadeUtil.stopAgendaMeeting(group);
						groupBO.setOp(opMember.getCode());
						conferenceCascadeService.stop(groupBO);
					}
				}
				
				String userName = "";
				try{
					UserVO user = userQuery.current();
					userName = user.getNickname();
				}catch(Exception e){
					List<UserVO> user = userQuery.findByIdIn(new ArrayListWrapper<Long>().add(group.getUserId()).getList());
					if(user != null && user.size() > 0){
						userName = user.get(0).getNickname();
					}
				}
				
				//临时指挥删除用户组和成员
				if(EditStatus.TEMP.equals(group.getEditStatus())){
					groupDao.delete(group);
					List<Long> memberIdList= members.stream().map(BusinessGroupMemberPO::getId).collect(Collectors.toList());
					businessGroupMemberDao.deleteByIdIn(memberIdList);
					operationLogService.send(userName, "停止临时指挥", userName+ "停止临时指挥groupId:" + groupId);
				}else{
					operationLogService.send(userName, "停止指挥", userName+ "停止指挥groupId:" + groupId);
				}

				if(group.getIsRecord()){
					groupRecordService.stopRecordGroup(group.getId(), false, businessDeliverBO);
				}

				//发消息或存入businessReturn
				businessReturnService.dealWebsocket(messageCaches);

				deliverExecuteService.execute(businessDeliverBO, "停止会议：" + group.getName(), true);
				return returnSplits;
			/*}else if(EditStatus.TEMP.equals(group.getEditStatus())){

				List<BusinessGroupMemberPO> connectMembers = new ArrayList<BusinessGroupMemberPO>();
				List<BusinessGroupMemberPO> members = businessGroupMemberDao.findByGroupId(groupId);
				List<MessageSendCacheBO> messageCaches = new ArrayList<MessageSendCacheBO>();
				BusinessGroupMemberPO chairmanMember = tetrisBvcQueryUtil.queryChairmanMember(members);

				for(BusinessGroupMemberPO member : members){
					if(member.getGroupMemberStatus().equals(GroupMemberStatus.CONNECT)){
						connectMembers.add(member);
					}
				}
				
				//清除专向等特殊指挥
				groupSpecialCommonService.clearAllSpecialCommand(group);
//				GroupCommandInfoPO commandInfo = groupCommandInfoDao.findByGroupId(groupId);
//				if(commandInfo != null){
//					commandInfo.clearAll();
//					groupCommandInfoDao.save(commandInfo);
//				}

				//特殊业务（如媒体转发）的处理
				stopBusinessForwardByChairman(groupId);

				//停止所有的议程（最多只有1个）
				RunningAgendaPO runningAgenda = runningAgendaDao.findByGroupId(groupId);
				if(runningAgenda != null){
					agendaExecuteService.stopAllSrcAndDst(group, businessDeliverBO);
					runningAgendaDao.delete(runningAgenda);
				}

				//关闭编码通道,关闭录像
				CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();
				LogicBO logic = groupMemberService.closeEncoder(connectMembers, codec, -1L, chairmanMember.getCode());
				LogicBO logicStopRecord = groupRecordService.stop(null, groupId, false);
				logic.merge(logicStopRecord);
				if(businessReturnService.getSegmentedExecute()){
					businessReturnService.add(logic, null, null);
				}else{
					executeBusiness.execute(logic, group.getName() + " 会议停止");
				}

				//记录会议的信息
				groupProceedRecordServiceImpl.saveStop(group);

				//级联
				BusinessGroupMemberPO opMember = tetrisBvcQueryUtil.queryBusinessMemberByUserId(members, userId);
				if(!OriginType.OUTER.equals(opMember.getOriginType())){
					if(BusinessType.COMMAND.equals(group.getBusinessType())){
						GroupBO groupBO = commandCascadeUtil.stopAgendaCommand(group);
						groupBO.setOp(opMember.getCode());
						commandCascadeService.stop(groupBO);
					}else if(BusinessType.MEETING_BVC.equals(group.getBusinessType())){
						GroupBO groupBO = commandCascadeUtil.stopAgendaMeeting(group);
						groupBO.setOp(opMember.getCode());
						conferenceCascadeService.stop(groupBO);
					}
				}

				//删除用户组和成员
				groupDao.delete(group);
				List<Long> memberIdList= members.stream().map(BusinessGroupMemberPO::getId).collect(Collectors.toList());
				businessGroupMemberDao.deleteByIdIn(memberIdList);

				String userName = "";
				try{
					UserVO user = userQuery.current();
					userName = user.getNickname();
				}catch(Exception e){
					List<UserVO> user = userQuery.findByIdIn(new ArrayListWrapper<Long>().add(group.getUserId()).getList());
					if(user != null && user.size() > 0){
						userName = user.get(0).getNickname();
					}
				}
				operationLogService.send(userName, "停止临时指挥", userName+ "停止临时指挥groupId:" + groupId);

				if(group.getIsRecord()){
					groupRecordService.stopRecordGroup(group.getId(), false, businessDeliverBO);
				}

				//发消息或存入businessReturn
				businessReturnService.dealWebsocket(messageCaches);

				deliverExecuteService.execute(businessDeliverBO, "停止会议：" + group.getName(), true);
				return returnSplits;
			}*/
		}
//		return secretSplits;
	}
	
	private void stopBusinessForwardByChairman(Long groupId) throws Exception{
		
			GroupPO group = groupDao.findOne(groupId);
			BusinessGroupMemberPO chairmanMember = tetrisBvcQueryUtil.queryChairmanMember(group.getMembers());
			
			//清理议程转发、议程转发源、议程转发目的、转发扩展信息
			List<AgendaForwardPO> needStopforwards = agendaForwardDAO.findByBusinessId(groupId);
			List<Long> forwardIds = needStopforwards.stream().map(AgendaForwardPO::getId).collect(Collectors.toList());
			List<AgendaForwardDemandPO> stopDemands = agendaForwardDemandDao.findByAgendaForwardIdIn(forwardIds);
			List<String> srcCodeList = stopDemands.stream().map(AgendaForwardDemandPO::getSrcCode).collect(Collectors.toList());
			//按businessId查出所有的member，是媒体转发源，后续可能再定义其它的
			List<BusinessGroupMemberPO> groupMemberList = businessGroupMemberDao.findByBusinessId(groupId.toString());
//			List<BusinessGroupMemberPO> groupMemberList = businessGroupMemberDao.findByGroupIdAndCodeIn(groupId, srcCodeList);
			
			List<AgendaForwardSourcePO> sources = agendaForwardSourceDao.findByAgendaForwardIdIn(forwardIds);
			List<AgendaForwardDestinationPO> destinations = agendaForwardDestinationDao.findByAgendaForwardIdIn(forwardIds);
			
			List<Long> memberIds = groupMemberList.stream().map(BusinessGroupMemberPO::getId).collect(Collectors.toList());
			businessGroupMemberDao.deleteByIdIn(memberIds);
			agendaForwardDemandDao.deleteInBatch(stopDemands);
			agendaForwardSourceDao.deleteInBatch(sources);
			agendaForwardDestinationDao.deleteInBatch(destinations);
			agendaForwardDao.deleteByIdIn(forwardIds);
			
			RunningAgendaPO runningAgenda = runningAgendaDao.findByGroupId(groupId);
			if(runningAgenda == null){
//				throw new BaseException(StatusCode.FORBIDDEN, group.getName()+"没有正在执行的议程");
				log.warn(group.getName()+"没有正在执行的议程");
			}
			
			//关闭 CONNECT的 编码器			
			List<BusinessGroupMemberPO> onlineGroupMemberList = groupMemberList.stream()
					.filter(member -> member.getGroupMemberStatus().equals(GroupMemberStatus.CONNECT)).collect(Collectors.toList());
			CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();
			LogicBO logic = groupMemberService.closeEncoder(onlineGroupMemberList, codec, -1L, chairmanMember.getCode());
			businessReturnService.add(logic, null, "媒体转发关闭编码器");
	}
	
	/**
	 * 生成唯一的会议号码<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月11日 下午1:35:22
	 * @return Long 会议号码
	 */
	private Long generateGroupNumber() throws BaseException{
		
		//TODO：groupNumber字段加上索引
		boolean repetition = true;
		List<Long> groupNumberList = new ArrayList<Long>();
		while(repetition){
			for (int i = 0; i < 10; i++) {
				Long numTemp = (long)(Math.random()*1000000);
				groupNumberList.add(preNum*1000000+numTemp);
			}
			List<Long> exitGroupNumberList= groupDao.findGroupNumberByGroupNumberIn(groupNumberList);
			for(Long num : groupNumberList){
				if(!exitGroupNumberList.contains(num)){
					return num;
				}
			}
		}
		throw new BaseException(StatusCode.FORBIDDEN, "没有拿到号码！");
	}
	
	/** 根据系统参数模板拷贝一个DeviceGroupAvtplPO，用于关联GroupPO */
	public DeviceGroupAvtplPO generateDeviceGroupAvtpl(){
		List<AvtplPO> sys_avtpls = sysAvtplDao.findByUsageType(AvtplUsageType.DEVICE_GROUP);
		AvtplPO sys_avtpl = sys_avtpls.get(0);
		List<AvtplGearsPO> sys_gears = sys_avtpl.getGears();
		DeviceGroupAvtplPO g_avtpl = new DeviceGroupAvtplPO().set(sys_avtpl);
		g_avtpl.setGears(new ArrayList<com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO>());
		for(AvtplGearsPO sys_gear:sys_gears){
			com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO g_gear = new com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO().set(sys_gear);
			g_avtpl.getGears().add(g_gear);
			g_gear.setAvtpl(g_avtpl);
		}
		return g_avtpl;
	}
	
	//------------------------------预约会议相关功能开始-------------------------------
	/**
	 * 预约会议的调度<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月5日 下午2:31:24
	 */
	public void orderGroupScheduling(Map<String, List<GroupPO>> needDisposeRecords) throws Exception{
		//查找要开会/关闭的会议，创建线程，提前一段时间开启/晚一会关闭会议。
		//对于错过的会议，完全错过的会议将executeGroup设置为false。
// 		int timeOffset = 60000;
//		Date now = new Date();
		//高频查询可以创建索引
		//BusinessOrderGroupType参数需要变为数组么
		List<GroupPO> needStartGroupList = needDisposeRecords.get("start");
//				groupDao.findByOrderGroupTypeAndOrderBeginTimeBeforeAndStatusAndExecuteGroup(BusinessOrderGroupType.COMMON_ORDER, 
//																														DateUtil.addMilliSecond(now, timeOffset), 
//																														GroupStatus.STOP,
//																														true);
//		List<GroupPO> noMoerNeedStartGroupList = new ArrayList<GroupPO>();
//		List<GroupPO> reallyNeedStartGroupList = new ArrayList<GroupPO>();
//		for(GroupPO group : needStartGroupList){
//			if(now.before(group.getOrderEndTime())){
//				reallyNeedStartGroupList.add(group);
//			}else{
//				group.setExecuteGroup(false);
//				noMoerNeedStartGroupList.add(group);
//			}
//		}
		if(needStartGroupList != null){
			for(GroupPO  group : needStartGroupList){
				start(group.getId(), -1);
				group.setVersion(0);
			}
			groupDao.save(needStartGroupList);
		}
		
//		groupDao.save(noMoerNeedStartGroupList);

		List<GroupPO> needStopGroupList = needDisposeRecords.get("stop");
//				groupDao.findByOrderGroupTypeAndOrderEndTimeBeforeAndStatus(BusinessOrderGroupType.COMMON_ORDER, DateUtil.addMilliSecond(now, -timeOffset), GroupStatus.START);
		if(needStopGroupList != null){
			for(GroupPO group : needStopGroupList){
				stop(-1L, group.getId(), 0);
				group.setVersion(0);
			}
			groupDao.save(needStopGroupList);
		}
		
	}
	
	/**
	 * 修改预约会议的预约结束时间<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月6日 下午2:05:28
	 * @param minute 要延长的分钟
	 * @param groupId 预约会议id
	 */
	public void changeOrderEndTime(Long minute,Long groupId) throws Exception{
		GroupPO group = groupDao.findOne(groupId);
		Date orderEndTime = group.getOrderEndTime();
		if(orderEndTime == null){
			throw new BaseException(StatusCode.FORBIDDEN, "该预约会议没有设置预约结束时间");
		}
		Date newOrderEndTime = DateUtil.addMilliSecond(orderEndTime, (int)(minute*60000));
		group.setOrderEndTime(newOrderEndTime);
		groupDao.save(group);
	}
	
	//------------------------------预约会议相关功能结束-------------------------------
  
	/**
	 * 全量信息同步<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月25日 下午4:14:23
	 * @param groupBO
	 * @param type
	 * @throws Exception
	 */
	public void memberAddFull(GroupBO groupBO, GroupType type) throws Exception{

		log.warn(groupBO.getGid() + " 全量信息同步：" + groupBO.getBizname());
		UserVO user = userQuery.findByUserno(groupBO.getOp());
		String uuid = groupBO.getGid();
		GroupPO group = groupDao.findByUuid(uuid);
		if(group != null){

			log.warn(uuid + " 全量信息同步，可能是联网发了多次请求，本次忽略：" + group.getName());
			return;
			
			//如果有之前没删掉的，则停会，删会
//			log.warn(uuid + " 全量信息同步，可能是重启恢复中，查到uuid相同的会议/指挥，删除重建：" + group.getName());
//			this.stop(user.getId(), group.getId(), 0);
//			this.remove(user.getId(), new ArrayListWrapper<Long>().add(group.getId()).getList());
		}


		//建会
		List<MinfoBO> mAddList = groupBO.getmAddList();
		Set<String> usernos = new HashSet<String>();
		Set<String> enterUsernos = new HashSet<String>();
		for(MinfoBO m : mAddList){
			//“已退出”的成员不用加入成员列表。1表示正在业务、2表示暂时离开、3表示已退出
			if("1".equals(m.getMstatus()) || "2".equals(m.getMstatus())){
				usernos.add(m.getMid());
			}
			//统计进会人
			if("1".equals(m.getMstatus())){
				enterUsernos.add(m.getMid());
			}
		}
		//成员id列表
		List<Long> userIdList = commandCascadeUtil.fromUserNosToIds(usernos);
		List<Long> hallIds=new ArrayList<>();
		List<String> bundleIds=new ArrayList<>();
		if(!EditStatus.TEMP.getCode().equals(groupBO.getGroupType())){
			if(GroupType.BASIC.equals(type)){
				group = this.saveCommand(user.getId(),user.getUsername(),"user",user.getId()+"",groupBO.getBizname(),
						groupBO.getSubject(), BusinessType.COMMAND, com.sumavision.tetris.bvc.business.OriginType.OUTER,userIdList,hallIds,bundleIds,groupBO.getGid(),
						null,null,null,null,EditStatus.NORMAL);
			}else if(GroupType.MEETING.equals(type)){
				group = this.saveCommand(user.getId(),user.getUsername(),"user",user.getId()+"",groupBO.getBizname(),
						groupBO.getSubject(), BusinessType.MEETING_BVC, com.sumavision.tetris.bvc.business.OriginType.OUTER,userIdList,hallIds,bundleIds,groupBO.getGid(),
						null,null,null,null,EditStatus.NORMAL);
			}


			//如果有开始时间，则开会。否则认为停会
			//开会，“正在业务”的成员才能进入
			String mtime = groupBO.getStime();
			String status = groupBO.getStatus();
			if(mtime==null || "".equals(mtime)){
				//认为已停会
				log.info(type + " 业务全量同步，业务停止状态，成员总数 " + userIdList.size());
			}else{
				GroupStatus groupStatus = null;
				if("1".equals(status)){
					groupStatus = GroupStatus.PAUSE;
				}else{
					groupStatus = GroupStatus.START;
				}
				Date startTime = DateUtil.parse(mtime, DateUtil.dateTimePattern);
				List<Long> enterUserIdList = commandCascadeUtil.fromUserNosToIds(enterUsernos);
				//测测能否暂停
				this.start(group.getId(), -1, false, enterUserIdList, startTime, groupStatus);
				log.info(type + " 业务全量同步，业务开始，状态：" + groupStatus + " , 成员总数 " + userIdList.size() + " , 进入人数 " + enterUserIdList.size());
			}
		}else {
			if(GroupType.BASIC.equals(type)){
				group = saveAndStartTemporary(user.getId(),user.getUsername(),"user",user.getId()+"",groupBO.getBizname(),
						groupBO.getSubject(), BusinessType.COMMAND, com.sumavision.tetris.bvc.business.OriginType.OUTER,userIdList,hallIds,bundleIds,groupBO.getGid(),
						null,null,null,null);
			}else if(GroupType.MEETING.equals(type)){
				group = saveAndStartTemporary(user.getId(),user.getUsername(),"user",user.getId()+"",groupBO.getBizname(),
						groupBO.getSubject(), BusinessType.MEETING_BVC, com.sumavision.tetris.bvc.business.OriginType.OUTER,userIdList,hallIds,bundleIds,groupBO.getGid(),
						null,null,null,null);
			}
		}

		//协同或发言、讨论（持久化，logic协议）
		if(GroupType.BASIC.equals(type)){
			//获取协同列表
			List<String> croplist = groupBO.getCroplist();
			List<CrossCommandBO> croslist = groupBO.getCroslist();
			List<SecretCommandBO> secretlist = groupBO.getSecretlist();
			ReplaceCommandBO replaceitem = groupBO.getReplaceitem();
			AuthCommandBO authitem = groupBO.getAuthitem();
			
			//协同
			if(croplist!=null && croplist.size()>0){
				List<Long> userIdArray = commandCascadeUtil.fromUserNosToIds(croplist);
				//开始协同
				//commandCooperateServiceImpl.start(group.getId(), userIdArray);
				groupCooperateService.startU(group.getId(),userIdArray);
				log.info("全量同步，开始协同，协同人数：" + userIdArray.size());
			}
			//越级
			if(croslist!=null && croslist.size()>0){
				
				CrossCommandBO crossCommand = croslist.get(0);
				List<String> userCodes = new ArrayListWrapper<String>().add(crossCommand.getUpid()).add(crossCommand.getDownid()).getList();
				List<Long> userIdArray = commandCascadeUtil.fromUserNosToIds(userCodes);
				groupCrossService.startU(group.getId(), userIdArray);
			}
			//专向
			if(secretlist!=null && secretlist.size()>0){
				
				SecretCommandBO secretCommand = secretlist.get(0);
				List<String> secretCodes = new ArrayListWrapper<String>().add(groupBO.getOp()).add(secretCommand.getUpid()).add(secretCommand.getDownid()).getList();
				List<UserVO> userArray = commandCascadeUtil.fromUserNosToUsers(secretCodes);
				Long userId = null;
				Long highUserId = null;
				Long lowUserId = null;
				for(UserVO userVo : userArray){
					if(userVo.getUserno().equals(groupBO.getOp())){
						userId = userVo.getId();
					}
					if(userVo.getUserno().equals(secretCommand.getUpid())){
						highUserId = userVo.getId();
					}
					if(userVo.getUserno().equals(secretCommand.getDownid())){
						lowUserId = userVo.getId();
					}
				}
				groupSecretService.startSecret(group.getId(), userId, highUserId, lowUserId);
			}
			//接替
			if(replaceitem!=null){
				
				List<String> replaceCodes = new ArrayListWrapper<String>().add(replaceitem.getOp()).add(replaceitem.getTargid()).getList();
				List<UserVO> userArray = commandCascadeUtil.fromUserNosToUsers(replaceCodes);
				Long opUserId = null;
				Long targetUserId = null;
				for(UserVO userVo : userArray){
					if(userVo.getUserno().equals(replaceitem.getOp())){
						opUserId = userVo.getId();
					}
					if(userVo.getUserno().equals(replaceitem.getTargid())){
						targetUserId = userVo.getId();
					}
				}
				groupReplaceService.startU(group.getId(), opUserId, targetUserId);
			}
			//授权
			if(authitem!=null){
				
				List<String> authitmCodes = new ArrayListWrapper<String>().add(authitem.getOp()).add(authitem.getAccepauthid()).add(authitem.getCmdedid()).getList();
				List<UserVO> userArray = commandCascadeUtil.fromUserNosToUsers(authitmCodes);
				Long userId = null;
				Long acceptUserId = null;
				Long cmdUserId = null;
				for(UserVO userVo : userArray){
					if(userVo.getUserno().equals(authitem.getOp())){
						userId = userVo.getId();
					}
					if(userVo.getUserno().equals(authitem.getAccepauthid())){
						acceptUserId = userVo.getId();
					}
					if(userVo.getUserno().equals(authitem.getCmdedid())){
						cmdUserId = userVo.getId();
					}
				}
				groupAuthorizeService.startU(group.getId(), userId, acceptUserId, cmdUserId);
			}
		}else if(GroupType.MEETING.equals(type)){
			//获取发言列表。后续改成只能1人发言
			String mode = groupBO.getMode();
			if("1".equals(mode)){
				//commandMeetingSpeakServiceImpl.discussStart(group.getUserId(), group.getId());
                groupSpeakService.discussStart(group.getUserId(),group.getId());
				log.info("全量同步，开始全员讨论");
			}else{
				String spkid = groupBO.getSpkid();
				if(spkid!=null && !"".equals(spkid)){
					String[] s = spkid.split(",");//这里早期写成逗号分隔的多个成员，实际在业务加了限制，最多只会有一个
					List<String> spkidList = Arrays.asList(s);
					List<Long> userIdArray = commandCascadeUtil.fromUserNosToIds(spkidList);
					if(userIdArray.size() > 0){
						//开始发言
						//commandMeetingSpeakServiceImpl.speakAppoint(group.getUserId(), group.getId(), userIdArray);
						groupSpeakService.speakAppointU(group.getId(),userIdArray);
						log.info("全量同步，开始发言，发言人数：" + userIdArray.size());
					}
				}
			}
		}
	}
	
	/**
	 * 创建临时会议/指挥<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月25日 下午4:46:46
	 * @param creatorUserId 创建人id
	 * @param creatorUsername 创建人名称
	 * @param chairmanType null或self为默认，以创建人的用户作为主席；user指定用户为主席；hall指定会场为主席；tvosOfUser以创建人的机顶盒为主席
	 * @param chairmanId 用户id/会场id
	 * @param name 会议名称
	 * @param subject 主题，可以与name相同
	 * @param groupBusinessType
	 * @param originType
	 * @param userIdList 用户成员id列表
	 * @param hallIds 会场成员id列表
	 * @param bundleIdList 设备成员（暂不支持）
	 * @param uuid 级联调用会传递uuid
	 * @param orderGroupType 预约会议类型BusinessOrderGroupType
	 * @param orderBeginTime 预约开始时间
	 * @param orderEndTime 预约结束时间
	 * @param groupLock 锁定会议 GroupLock
	 */
	private GroupPO saveAndStartTemporaryCommand(
			Long creatorUserId,
			String creatorUsername,
			String chairmanType,
			String chairmanId,
			String name,
			String subject,
			BusinessType groupBusinessType,
			OriginType originType,
			List<Long> userIdList,
			List<Long> hallIds,
			List<String> bundleIdList,
			String uuid,
			String orderGroupType,
			String orderBeginTime,
			String orderEndTime,
			String groupLock,
			EditStatus editStatus
			) throws Exception{
		
		//------------------------本类的saveCommand()方法移植而来
		TerminalPO terminal = terminalDao.findByType(com.sumavision.tetris.bvc.model.terminal.TerminalType.QT_ZK);
		
		//确定主席
		MemberTerminalBO chairmanBO = null;		
		if(chairmanType == null) chairmanType = "self";
		switch (chairmanType){
		case "user":
			chairmanBO = new MemberTerminalBO()
				.setGroupMemberType(GroupMemberType.MEMBER_USER)
				.setOriginId(chairmanId)
				.setTerminalId(terminal.getId());
			break;
		case "hall":
			ConferenceHallPO conferenceHall = conferenceHallDao.findOne(Long.valueOf(chairmanId));
			chairmanBO = new MemberTerminalBO()
				.setGroupMemberType(GroupMemberType.MEMBER_HALL)
				.setOriginId(chairmanId)
				.setTerminalId(conferenceHall.getTerminalId());
			break;
		case "tvosOfUser":
			TerminalPO tvosT = terminalDao.findByType(com.sumavision.tetris.bvc.model.terminal.TerminalType.ANDROID_TVOS);
			Long hallId = findHallId(creatorUserId, "tvos");
			chairmanBO = new MemberTerminalBO()
					.setGroupMemberType(GroupMemberType.MEMBER_HALL)
					.setOriginId(hallId.toString())
					.setTerminalId(tvosT.getId());
			break;
		case "self":
		default:
			chairmanBO = new MemberTerminalBO()
				.setGroupMemberType(GroupMemberType.MEMBER_USER)
				.setOriginId(creatorUserId.toString())
				.setTerminalId(terminal.getId());
			break;			
		}		
		
		//用户成员
		List<MemberTerminalBO> memberTerminalBOs = new ArrayList<MemberTerminalBO>();
		for(Long userId : userIdList){
			MemberTerminalBO memberBO = new MemberTerminalBO()
					.setGroupMemberType(GroupMemberType.MEMBER_USER)
					.setOriginId(userId.toString())
					.setTerminalId(terminal.getId());
			memberTerminalBOs.add(memberBO);
		}
		
		//会场成员和设备成员
		List<ConferenceHallPO> halls = conferenceHallDao.findAll(hallIds);
		List<BundlePO> bundlePOs = bundleDao.findByBundleIdIn(bundleIdList);
		List<ConferenceHallPO> bundleHalls = conferenceHallService.bundleExchangeToHall(bundlePOs);
		halls.addAll(bundleHalls);
		
		for(ConferenceHallPO hall : halls){
			MemberTerminalBO memberBO = new MemberTerminalBO()
					.setGroupMemberType(GroupMemberType.MEMBER_HALL)
					.setOriginId(hall.getId().toString())
					.setTerminalId(hall.getTerminalId())
					.setFromDevice(hall.getFromDevice());
			memberTerminalBOs.add(memberBO);
		}
		//------------------------本类的saveCommand()方法移植而来结束
		
		//------------------------本类的save()方法移植而来结束
		GroupPO group1 = groupDao.findByUuid(uuid);
		if(group1 != null){
			log.warn("级联建会，uuid已经存在：" + uuid);
			return group1;
		}
		
		//校验
		UserBO creatorUserBo = resourceService.queryUserById(creatorUserId, TerminalType.QT_ZK);
		if(creatorUserBo == null){
			throw new BaseException(StatusCode.FORBIDDEN, "当前用户已失效，请重新登录");
		}
		if(creatorUserBo.getFolderUuid() == null){
//			throw new UserHasNoFolderException(creatorUserBo.getName());
		}
		
		//确保成员中有创建者
		if(!memberTerminalBOs.contains(chairmanBO)){
			memberTerminalBOs.add(chairmanBO);
		}
		
		//本系统创建，则鉴权，则校验主席有编码器
		if(!OriginType.OUTER.equals(originType)){
			
			//鉴权，区分指挥与会议
//			if(type.equals(GroupType.BASIC)){
//				commandCommonServiceImpl.authorizeUsers(userIdList, chairmanUserId, BUSINESS_OPR_TYPE.ZK);
//			}else if(type.equals(GroupType.MEETING)){
//				commandCommonServiceImpl.authorizeUsers(userIdList, chairmanUserId, BUSINESS_OPR_TYPE.HY);
//			}
			
//			String creatorEncoderId = commonQueryUtil.queryExternalOrLocalEncoderIdFromUserBO(creatorUserBo);
//			List<BundlePO> creatorBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(creatorEncoderId).getList());
//			if(creatorBundleEntities.size() == 0){
//				throw new UserHasNoAvailableEncoderException(creatorUserBo.getName());
//			}
		}
		
		//会议组重名校验 
		//出现重名时解决策略是找到一个没有被使用的"name-数字"。
		if(!OriginType.OUTER.equals(originType)){
			List<GroupPO> existGroups = groupDao.findByName(name);
			if(existGroups!=null && existGroups.size()>0){
				List<String> likeGroupNames = groupDao.findByNameLike(name + "-%").stream().map(GroupPO::getName).collect(Collectors.toList());
				String recommendedName0 = name + "-";
				String recommendedName = null;
				for(int i=2; ; i++){
					recommendedName = recommendedName0 + i;
					if(!likeGroupNames.contains(recommendedName)){
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
		group.setUserCode(creatorUserBo.getUserNo());
		group.setCreatetime(new Date());
		if(groupLock != null){
			group.setGroupLock(GroupLock.valueOf(groupLock));
		}
		group.setGroupNumber(generateGroupNumber());
		
		group.setOrderGroupType(orderGroupType==null?null:BusinessOrderGroupType.valueOf(orderGroupType));
		if(group.getOrderGroupType() != null){
			group.setOrderBeginTime(DateUtil.parse(orderBeginTime, DateUtil.dateTimePattern));
			group.setOrderEndTime(DateUtil.parse(orderEndTime, DateUtil.dateTimePattern));
			group.setExecuteGroup(true);
		}
		
		group.setStatus(GroupStatus.STOP);
		group.setEditStatus(editStatus);
		
		groupDao.save(group);
		
		//组建会议成员
		List<BusinessGroupMemberPO> members = groupMemberService.generateMembers(group, memberTerminalBOs, chairmanBO);
		businessGroupMemberDao.save(members);
		groupMemberService.fullfillGroupMember(members);
		
		//授予角色
		BusinessType businessType = group.getBusinessType();
		List<GroupMemberRolePermissionPO> pers = new ArrayList<GroupMemberRolePermissionPO>();
		if(BusinessType.MEETING_BVC.equals(businessType)) {
			RolePO memberRole = roleDao.findByInternalRoleType(InternalRoleType.MEETING_AUDIENCE);
			for(BusinessGroupMemberPO member : members){
				//主席成员只有主席角色
				if(member.getIsAdministrator()){
					RolePO chairmanRole = businessCommonService.queryGroupChairmanRole(group);
					member.setRoleId(chairmanRole.getId());
					member.setRoleName(chairmanRole.getName());
					GroupMemberRolePermissionPO memberRolePermission = new GroupMemberRolePermissionPO(chairmanRole.getId(), member.getId());
					pers.add(memberRolePermission);
				}else{
					if(member.getRoleId() == null){
						member.setGroupMemberStatus(GroupMemberStatus.DISCONNECT);
						member.setRoleId(memberRole.getId());
						member.setRoleName(memberRole.getName());
						GroupMemberRolePermissionPO memberRolePermission = new GroupMemberRolePermissionPO(memberRole.getId(), member.getId());
						pers.add(memberRolePermission);
					}
				}
			}
		}else if (BusinessType.COMMAND.equals(businessType)) {
			List<RolePO> allRole = roleDao.findAll();
			List<RolePO> levelRoles = allRole.stream().filter(role ->{
				return role.getInternalRoleType() != null && BaseUtils.stringIsNotBlank(role.getInternalRoleType().getLevel());
			}).collect(Collectors.toList());
			Map<String, RolePO> levelAndRoleMap = levelRoles.stream().filter(role->{
				return BaseUtils.stringIsNotBlank(role.getInternalRoleType().getLevel());
			}).collect(Collectors.toMap(role-> (String)role.getInternalRoleType().getLevel(), role-> role));
			for(BusinessGroupMemberPO member : members){
				RolePO memberRole = levelAndRoleMap.get(member.getLevel().toString());
				member.setGroupMemberStatus(GroupMemberStatus.DISCONNECT);
				member.setRoleId(memberRole.getId());
				member.setRoleName(memberRole.getName());
				GroupMemberRolePermissionPO memberRolePermission = new GroupMemberRolePermissionPO(memberRole.getId(), member.getId());
				pers.add(memberRolePermission);
			}
		}
		groupMemberRolePermissionDao.save(pers);
		
		//给所有成员，没有PageInfoPO的，建立PageInfoPO
		groupMemberService.generateMembersPageInfo(members);
		//拷贝参数模板
		DeviceGroupAvtplPO g_avtpl = generateDeviceGroupAvtpl();
		group.setAvtpl(g_avtpl);
		g_avtpl.setBusinessGroup(group);
		groupDao.save(group);
		log.info(name + " 创建完成");
		operationLogService.send(creatorUserBo.getName(), "新建指挥", creatorUserBo.getName() + "新建指挥groupId:" + group.getId());
	//------------------------本类的save()方法移植而来结束
		
	//------------------------本类的start()方法移植而来
		synchronized (new StringBuffer().append(lockProcessPrefix).append(group.getId()).toString().intern()) {
						
			Long groupId = group.getId();
			BusinessDeliverBO businessDeliverBO = new BusinessDeliverBO().setUserId(group.getUserId().toString()).setGroup(group);
			List<MessageSendCacheBO> messageCaches = new ArrayList<MessageSendCacheBO>();
			
			group.setStatus(GroupStatus.START);
			group.setStartTime(new Date());		
			group.setExecuteGroup(false);
			
			//处理主席，置为CONNECT，绑定主席角色。其它成员的绑定角色放在membersResponse里边
			BusinessGroupMemberPO chairman = tetrisBvcQueryUtil.queryChairmanMember(members);
			chairman.setGroupMemberStatus(GroupMemberStatus.CONNECT);
			if(chairman.getRoleId() == null){
				RolePO chairmanRole = businessCommonService.queryGroupChairmanRole(group);
				chairman.setRoleId(chairmanRole.getId());
				chairman.setRoleName(chairmanRole.getName());
				GroupMemberRolePermissionPO memberRolePermission = new GroupMemberRolePermissionPO(chairmanRole.getId(), chairman.getId());
				groupMemberRolePermissionDao.save(memberRolePermission);
			}
									
			//自动接听则所有在线的人接听，否则只有主席接听；专向指挥只有主席接听
			List<BusinessGroupMemberPO> acceptMembers = null;
			if(autoEnter && !group.getBusinessType().equals(GroupType.SECRET)){
				//自动进入：给所有本系统成员自动进入（memberRespones方法中会推送meetingStartNow通知）
				acceptMembers = new ArrayListWrapper<BusinessGroupMemberPO>().add(chairman).getList();
				for(BusinessGroupMemberPO member : members){
					if(member.getIsAdministrator()) continue;
						acceptMembers.add(member);
				}
			}else{
				//不自动进入：
				//给主席、设备成员自动进入
				acceptMembers = new ArrayList<BusinessGroupMemberPO>();
				for(BusinessGroupMemberPO member : members){
					if(chairman.equals(member)){
						acceptMembers.add(member);
					}else if(GroupMemberType.MEMBER_DEVICE.equals(member.getGroupMemberType())
							|| GroupMemberType.MEMBER_DEVICE.equals(member.getGroupMemberType())){
						acceptMembers.add(member);
					}
				}
				
				//给本系统其它成员推送meetingStart通知
				JSONObject message = new JSONObject();
				message.put("fromUserId", chairman.getOriginId());
				message.put("fromUserName", chairman.getName());
				message.put("businessId", group.getId().toString());
				String messageBusinessType = null;
				if(BusinessType.COMMAND.equals(group.getBusinessType())){
					messageBusinessType = "commandStart";//自动接听使用commandStartNow
				}else{
					messageBusinessType = "meetingStart";//自动接听使用meetingStartNow
				}
				message.put("businessType", messageBusinessType);
				message.put("businessInfo", "接受到 " + group.getName() + " 邀请，主席：" + chairman.getName() + "，是否进入？");
				
				//发送消息
				for(BusinessGroupMemberPO member : members){
					if(chairman.equals(member)){
						continue;
					}else if(GroupMemberType.MEMBER_USER.equals(member.getGroupMemberType())){
						messageCaches.add(new MessageSendCacheBO(Long.parseLong(member.getOriginId()), message.toJSONString(), WebsocketMessageType.COMMAND));
					}
				}				
			}
			
			groupDao.save(group);
			
			//呼叫编码器
			groupMemberService.membersResponse(group, members, acceptMembers);
			
			//执行默认议程
			AgendaPO commandAgenda = null;
			if(BusinessType.COMMAND.equals(businessType)){
				commandAgenda = agendaDao.findByBusinessInfoType(BusinessInfoType.BASIC_COMMAND);
			}else if(BusinessType.MEETING_QT.equals(businessType)
					|| BusinessType.MEETING_BVC.equals(businessType)){
				commandAgenda = agendaDao.findByBusinessInfoType(BusinessInfoType.BASIC_MEETING);
			}
			agendaExecuteService.runAgenda(groupId, commandAgenda.getId(), businessDeliverBO, false);
			
			//记录会议的信息
			groupProceedRecordServiceImpl.saveStart(group);
			
			//级联
			if(!OriginType.OUTER.equals(group.getOriginType())){
				if(BusinessType.COMMAND.equals(group.getBusinessType())){
					GroupBO groupBO = groupCascadeUtil.saveAndStartTemporary(group);
					commandCascadeService.startTemp(groupBO);
				}else if(BusinessType.MEETING_BVC.equals(group.getBusinessType())){
					GroupBO groupBO = groupCascadeUtil.saveAndStartTemporary(group);
					conferenceCascadeService.startTemp(groupBO);
				}
			}
			
			String userName = "";
			try{
				UserVO user = userQuery.current();
				userName = user.getNickname();
			}catch(Exception e){
				List<UserVO> user = userQuery.findByIdIn(new ArrayListWrapper<Long>().add(group.getUserId()).getList());
				if(user != null && user.size() > 0){
					userName = user.get(0).getNickname();
				}
			}
			operationLogService.send(userName, "开启指挥", userName + "开启指挥groupId:" + groupId);
			
			//发消息，主要是“邀请”消息，自动接听则没有
			businessReturnService.dealWebsocket(messageCaches);
			deliverExecuteService.execute(businessDeliverBO, "开始会议：" + group.getName(), true);
			return group;
		}
		//------------------------本类的start()方法移植而来结束
}

	/**
	 * 修改version，用于判断时候已经被其他客户端处理<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年4月2日 上午11:09:03
	 * @return
	 */
	public Map<String, List<GroupPO>> saveVersion() {

		Map<String, List<GroupPO>> needDisposeGroups = new HashMap<String, List<GroupPO>>();
		int timeOffset = 60000;
		Date now = new Date();
		List<GroupPO> needStartGroupList = groupDao.findByOrderGroupTypeAndOrderBeginTimeBeforeAndStatusAndExecuteGroup(BusinessOrderGroupType.COMMON_ORDER, 
																														DateUtil.addMilliSecond(now, timeOffset), 
																														GroupStatus.STOP,
																														true);
		List<GroupPO> noMoerNeedStartGroupList = new ArrayList<GroupPO>();
		List<GroupPO> reallyNeedStartGroupList = new ArrayList<GroupPO>();
		for(GroupPO group : needStartGroupList){
			if(group.getVersion() != null && group.getVersion() == 0){
				if(now.before(group.getOrderEndTime())){
					group.setVersion(1);
					reallyNeedStartGroupList.add(group);
				}else{
					group.setExecuteGroup(false);
					noMoerNeedStartGroupList.add(group);
				}
			}
		}
		
		if(reallyNeedStartGroupList.size() > 0){
			for(GroupPO group : reallyNeedStartGroupList){
				groupDao.saveAndFlush(group);
			}
			needDisposeGroups.put("start", reallyNeedStartGroupList);
		}
		
		if(noMoerNeedStartGroupList.size() > 0){
			groupDao.save(noMoerNeedStartGroupList);
		}
		
		List<GroupPO> needStopGroupList = groupDao.findByOrderGroupTypeAndOrderEndTimeBeforeAndStatus(BusinessOrderGroupType.COMMON_ORDER, DateUtil.addMilliSecond(now, -timeOffset), GroupStatus.START);
		for(GroupPO group : needStopGroupList){
			if(group.getVersion() != null && group.getVersion() == 0){
					group.setVersion(1);
			}
		}
		
		if(needStopGroupList.size() > 0){
			for(GroupPO group :needStopGroupList){
				groupDao.saveAndFlush(group);
			}
			needDisposeGroups.put("stop", needStopGroupList);
		}
		
		return needDisposeGroups;
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
		List<BusinessGroupMemberPO> members = group.getMembers();
		BusinessGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
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
				List<BusinessGroupMemberPO> members = group.getMembers();
				BusinessGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
				List<BusinessGroupMemberPO> srcMembers = commandCommonUtil.queryMembersByUserIds(members, srcUserIds);
				List<Long> srcMemberIds = new ArrayList<Long>();
				for(BusinessGroupMemberPO srcMember : srcMembers){
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
	
	/**
	 * 打开编码器<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月20日 下午3:23:26
	 * @param group
	 * @param sourceBOs
	 * @param codec
	 * @param userId
	 * @return
	 * @throws Exception
	 */
//	public LogicBO openEncoder(
//			GroupPO group,
//			List<SourceBO> sourceBOs,
//			CodecParamBO codec,
//			Long userId) throws Exception{
//		
//		String localLayerId = null;
//		LogicBO logic = new LogicBO().setUserId(userId.toString())
//				 			 		 .setConnectBundle(new ArrayList<ConnectBundleBO>())
//				 			 		 .setPass_by(new ArrayList<PassByBO>());
//		
//		for(SourceBO sourceBO : sourceBOs){
//			OriginType originType = sourceBO.getOriginType();
//			if(OriginType.INNER.equals(originType)){
//				ChannelSchemeDTO video = sourceBO.getVideoSourceChannel();
////				BundlePO bundlePO = bundleDao.findByBundleId(video.getBundleId());
//				BundlePO bundlePO = sourceBO.getVideoBundle();//后续改成这个
//				PassByBO passBy = new PassByBO().setIncomingCall(group, video.getBundleId() , bundlePO.getAccessNodeUid());
//				ConnectBundleBO connectEncoderBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
//						            .setOperateType(ConnectBundleBO.OPERATE_TYPE)
//								    .setLock_type("write")
//								    .setBundleId(video.getBundleId())
//								    .setLayerId(bundlePO.getAccessNodeUid())
//								    .setBundle_type(bundlePO.getBundleType())
//								    .setPass_by_str(passBy);
//				ConnectBO connectEncoderVideoChannel = new ConnectBO().setChannelId(video.getChannelId())
//						      .setChannel_status("Open")
//						      .setBase_type(video.getBaseType())
//						      .setCodec_param(codec);
//				if(Boolean.TRUE.equals(bundlePO.getMulticastEncode())){
//					String videoAddr = multicastService.addrAddPort(bundlePO.getMulticastEncodeAddr(), 2);
//					connectEncoderVideoChannel.setMode(TransmissionMode.MULTICAST.getCode()).setMulti_addr(videoAddr).setSrc_multi_ip(bundlePO.getMulticastSourceIp());
//				}
//				connectEncoderBundle.getChannels().add(connectEncoderVideoChannel);
//				ChannelSchemeDTO audio = sourceBO.getAudioSourceChannel();
//				if(audio != null){
//					ConnectBO connectEncoderAudioChannel = new ConnectBO().setChannelId(audio.getChannelId())
//						      .setChannel_status("Open")
//						      .setBase_type(audio.getBaseType())
//						      .setCodec_param(codec);
//					if(Boolean.TRUE.equals(bundlePO.getMulticastEncode())){
//						String audioAddr = multicastService.addrAddPort(bundlePO.getMulticastEncodeAddr(), 4);
//						connectEncoderAudioChannel.setMode(TransmissionMode.MULTICAST.getCode()).setMulti_addr(audioAddr).setSrc_multi_ip(bundlePO.getMulticastSourceIp());
//					}
//					connectEncoderBundle.getChannels().add(connectEncoderAudioChannel);
//				}
//	//			connectEncoderBundle.setChannels(new ArrayListWrapper<ConnectBO>().add(connectEncoderVideoChannel).add(connectEncoderAudioChannel).getList());
//				logic.getConnectBundle().add(connectEncoderBundle);
//			}else{
//				GroupMemberType groupMemberType = sourceBO.getGroupMemberType();
//				//后续再支持级联用户；呼叫的实现方式也不同
//				if(GroupMemberType.MEMBER_DEVICE.equals(groupMemberType)){
//					//点播外部设备，passby拉流
//					if(localLayerId == null) localLayerId = resourceRemoteService.queryLocalLayerId();
//					BundlePO bundlePO = sourceBO.getVideoBundle();
//					ChannelSchemeDTO video = sourceBO.getVideoSourceChannel();
//					ChannelSchemeDTO audio = sourceBO.getAudioSourceChannel();
//					XtBusinessPassByContentBO passByContent = new XtBusinessPassByContentBO().setCmd(XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_ENCODER)
//										 .setOperate(XtBusinessPassByContentBO.OPERATE_START)
//										 .setUuid(sourceBO.getMemberUuid())
//										 .setSrc_user(group.getUserCode())//发起人、目的号码
//										 .setXt_encoder(new HashMapWrapper<String, String>().put("layerid", localLayerId)
//												 											.put("bundleid", bundlePO.getBundleId())
//												 											.put("video_channelid", video.getChannelId())
//												 											.put("audio_channelid", audio.getChannelId())
//												 											.getMap())
//										 .setDst_number(bundlePO.getUsername())//被点播、源号码
//										 .setVparam(codec);
//					
//					PassByBO passby = new PassByBO().setLayer_id(localLayerId)
//					.setType(XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_ENCODER)
//					.setPass_by_content(passByContent);
//					
//					logic.getPass_by().add(passby);
//				}else if(GroupMemberType.MEMBER_USER.equals(groupMemberType)){
//					//点播外部用户，passby拉流
//					if(localLayerId == null) localLayerId = resourceRemoteService.queryLocalLayerId();
//					BundlePO bundlePO = sourceBO.getVideoBundle();
//					ChannelSchemeDTO video = sourceBO.getVideoSourceChannel();
//					ChannelSchemeDTO audio = sourceBO.getAudioSourceChannel();
//					XtBusinessPassByContentBO passByContent = new XtBusinessPassByContentBO().setCmd(XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_USER)
//										 .setOperate(XtBusinessPassByContentBO.OPERATE_START)
//										 .setUuid(sourceBO.getMemberUuid())
//										 .setSrc_user(group.getUserCode())//发起人、目的号码
//										 .setXt_encoder(new HashMapWrapper<String, String>().put("layerid", localLayerId)
//												 											.put("bundleid", bundlePO.getBundleId())
//												 											.put("video_channelid", video.getChannelId())
//												 											.put("audio_channelid", audio.getChannelId())
//												 											.getMap())
//										 .setDst_number(bundlePO.getUsername())//被点播、源号码
//										 .setVparam(codec);
//					
//					PassByBO passby = new PassByBO().setLayer_id(localLayerId)
//					.setType(XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_USER)
//					.setPass_by_content(passByContent);
//					
//					logic.getPass_by().add(passby);
//				}
//			}
//		}
//		
//		return logic;
//	}
//	
//	/**
//	 * 关闭编码器<br/>
//	 * <p>详细描述</p>
//	 * <b>作者:</b>zsy<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2020年10月20日 下午3:23:44
//	 * @param group
//	 * @param sourceBOs
//	 * @param codec
//	 * @param userId
//	 * @return
//	 * @throws Exception
//	 */
//	//TODO:检索设备或通道是否还在使用
//	public LogicBO closeEncoder(
//			GroupPO group,
//			List<SourceBO> sourceBOs,
//			CodecParamBO codec,
//			Long userId) throws Exception{
//		
//		String localLayerId = null;
//		LogicBO logic = new LogicBO().setUserId(userId.toString())
//									 .setDisconnectBundle(new ArrayList<DisconnectBundleBO>())
//									 .setMediaPushDel(new ArrayList<MediaPushSetBO>())
//									 .setPass_by(new ArrayList<PassByBO>());
//		
//		for(SourceBO sourceBO : sourceBOs){
//			OriginType originType = sourceBO.getOriginType();
//			if(OriginType.INNER.equals(originType)){
//				ChannelSchemeDTO video = sourceBO.getVideoSourceChannel();
//				BundlePO bundlePO = bundleDao.findByBundleId(video.getBundleId());
//				PassByBO passBy = new PassByBO().setHangUp(group, video.getBundleId() , bundlePO.getAccessNodeUid());
//				DisconnectBundleBO disconnectEncoderBundle = new DisconnectBundleBO().setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
//						             .setOperateType(DisconnectBundleBO.OPERATE_TYPE)
//						             .setBundleId(video.getBundleId())
//						             .setBundle_type(bundlePO.getBundleType())
//						             .setLayerId(bundlePO.getAccessNodeUid())
//						             .setPass_by_str(passBy);
//				logic.getDisconnectBundle().add(disconnectEncoderBundle);
//			}else{
//				GroupMemberType groupMemberType = sourceBO.getGroupMemberType();
//				//后续再支持级联用户；呼叫的实现方式也不同
//				if(GroupMemberType.MEMBER_DEVICE.equals(groupMemberType)){
//					if(localLayerId == null) localLayerId = resourceRemoteService.queryLocalLayerId();
//					BundlePO bundlePO = sourceBO.getVideoBundle();
//					ChannelSchemeDTO video = sourceBO.getVideoSourceChannel();
//					ChannelSchemeDTO audio = sourceBO.getAudioSourceChannel();
//					XtBusinessPassByContentBO passByContent = new XtBusinessPassByContentBO().setCmd(XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_ENCODER)
//										 .setOperate(XtBusinessPassByContentBO.OPERATE_STOP)
//										 .setUuid(sourceBO.getMemberUuid())
//										 .setSrc_user(group.getUserCode())//发起人、目的号码
//										 .setXt_encoder(new HashMapWrapper<String, String>().put("layerid", localLayerId)
//												 											.put("bundleid", bundlePO.getBundleId())
//												 											.put("video_channelid", video.getChannelId())
//												 											.put("audio_channelid", audio.getChannelId())
//												 											.getMap())
//										 .setDst_number(bundlePO.getUsername())//被点播、源号码
//										 .setVparam(codec);
//					
//					PassByBO passby = new PassByBO().setLayer_id(localLayerId)
//					.setType(XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_ENCODER)
//					.setPass_by_content(passByContent);
//					
//					logic.getPass_by().add(passby);
//				}else if(GroupMemberType.MEMBER_USER.equals(groupMemberType)){
//					if(localLayerId == null) localLayerId = resourceRemoteService.queryLocalLayerId();
//					BundlePO bundlePO = sourceBO.getVideoBundle();
//					ChannelSchemeDTO video = sourceBO.getVideoSourceChannel();
//					ChannelSchemeDTO audio = sourceBO.getAudioSourceChannel();
//					XtBusinessPassByContentBO passByContent = new XtBusinessPassByContentBO().setCmd(XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_ENCODER)
//										 .setOperate(XtBusinessPassByContentBO.OPERATE_STOP)
//										 .setUuid(sourceBO.getMemberUuid())
//										 .setSrc_user(group.getUserCode())//发起人、目的号码
//										 .setXt_encoder(new HashMapWrapper<String, String>().put("layerid", localLayerId)
//												 											.put("bundleid", bundlePO.getBundleId())
//												 											.put("video_channelid", video.getChannelId())
//												 											.put("audio_channelid", audio.getChannelId())
//												 											.getMap())
//										 .setDst_number(bundlePO.getUsername())//被点播、源号码
//										 .setVparam(codec);
//					
//					PassByBO passby = new PassByBO().setLayer_id(localLayerId)
//					.setType(XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_USER)
//					.setPass_by_content(passByContent);
//					
//					logic.getPass_by().add(passby);
//				}
//			}
//		}
//		
//		return logic;
//	
//	}
}

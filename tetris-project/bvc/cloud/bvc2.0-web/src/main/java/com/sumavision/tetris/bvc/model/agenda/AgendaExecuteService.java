package com.sumavision.tetris.bvc.model.agenda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sumavision.tetris.bvc.model.agenda.combine.AutoCombineService;
import com.sumavision.tetris.bvc.model.agenda.combine.CombineAudioDAO;
import com.sumavision.tetris.bvc.model.agenda.combine.CombineVideoDAO;
import com.sumavision.tetris.bvc.model.agenda.combine.CombineVideoPO;
import com.sumavision.tetris.bvc.model.agenda.combine.CombineVideoUtil;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.FolderUserMapDAO;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.FolderUserMap;
import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;
import com.suma.venus.resource.service.ResourceRemoteService;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.command.group.enumeration.MemberStatus;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.device.command.cast.CommandCastServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.command.user.CommandUserServiceImpl;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.ConnectBO;
import com.sumavision.bvc.device.group.bo.ConnectBundleBO;
import com.sumavision.bvc.device.group.bo.DisconnectBundleBO;
import com.sumavision.bvc.device.group.bo.ForwardSetSrcBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.PassByBO;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.resource.dao.ResourceBundleDAO;
import com.sumavision.bvc.resource.dao.ResourceChannelDAO;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.tetris.auth.token.TerminalType;
import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.bvc.business.ExecuteStatus;
import com.sumavision.tetris.bvc.business.OriginType;
import com.sumavision.tetris.bvc.business.bo.MemberChangedTaskBO;
import com.sumavision.tetris.bvc.business.bo.ModifyMemberRoleBO;
import com.sumavision.tetris.bvc.business.bo.SourceBO;
import com.sumavision.tetris.bvc.business.common.BusinessCommonService;
import com.sumavision.tetris.bvc.business.common.BusinessReturnService;
import com.sumavision.tetris.bvc.business.common.MulticastService;
import com.sumavision.tetris.bvc.business.dao.CommonForwardDAO;
import com.sumavision.tetris.bvc.business.dao.GroupDAO;
import com.sumavision.tetris.bvc.business.dao.GroupMemberDAO;
import com.sumavision.tetris.bvc.business.dao.GroupMemberRolePermissionDAO;
import com.sumavision.tetris.bvc.business.dao.RunningAgendaDAO;
import com.sumavision.tetris.bvc.business.forward.CommonForwardPO;
import com.sumavision.tetris.bvc.business.group.GroupMemberPO;
import com.sumavision.tetris.bvc.business.group.GroupMemberRolePermissionPO;
import com.sumavision.tetris.bvc.business.group.GroupMemberStatus;
import com.sumavision.tetris.bvc.business.group.GroupMemberType;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.bvc.business.group.GroupStatus;
import com.sumavision.tetris.bvc.business.group.RunningAgendaPO;
import com.sumavision.tetris.bvc.business.group.TransmissionMode;
import com.sumavision.tetris.bvc.business.terminal.hall.TerminalBundleConferenceHallPermissionDAO;
import com.sumavision.tetris.bvc.business.terminal.hall.TerminalBundleConferenceHallPermissionPO;
import com.sumavision.tetris.bvc.business.terminal.user.TerminalBundleUserPermissionDAO;
import com.sumavision.tetris.bvc.business.terminal.user.TerminalBundleUserPermissionPO;
import com.sumavision.tetris.bvc.model.agenda.combine.AutoCombineService;
import com.sumavision.tetris.bvc.model.agenda.combine.CombineAudioDAO;
import com.sumavision.tetris.bvc.model.agenda.combine.CombineVideoDAO;
import com.sumavision.tetris.bvc.model.agenda.combine.CombineVideoPO;
import com.sumavision.tetris.bvc.model.agenda.combine.CombineVideoUtil;
import com.sumavision.tetris.bvc.model.role.RoleChannelDAO;
import com.sumavision.tetris.bvc.model.role.RoleChannelPO;
import com.sumavision.tetris.bvc.model.role.RoleDAO;
import com.sumavision.tetris.bvc.model.role.RolePO;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundleDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundlePO;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundleType;
import com.sumavision.tetris.bvc.model.terminal.TerminalPO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelDAO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelPO;
import com.sumavision.tetris.bvc.model.terminal.layout.LayoutPositionDAO;
import com.sumavision.tetris.bvc.model.terminal.layout.LayoutPositionPO;
import com.sumavision.tetris.bvc.page.PageInfoDAO;
import com.sumavision.tetris.bvc.page.PageInfoPO;
import com.sumavision.tetris.bvc.page.PageTaskDAO;
import com.sumavision.tetris.bvc.page.PageTaskPO;
import com.sumavision.tetris.bvc.page.PageTaskService;
import com.sumavision.tetris.bvc.util.TetrisBvcQueryUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;

import lombok.extern.slf4j.Slf4j;

/**
 * AgendaExecuteService<br/>
 * <p>执行议程、修改角色</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年5月22日 上午10:03:43
 */
@Slf4j
@Service
public class AgendaExecuteService {
	
	@Autowired
	private LayoutPositionDAO layoutPositionDao;
	
	@Autowired
	private AgendaLayoutTemplateDAO agendaLayoutTemplateDao;
	
	@Autowired
	private LayoutVirtualSourceTemplateDAO layoutVirtualSourceTemplateDao;
	
	@Autowired
	private CombineVideoDAO combineVideoDao;
	
	@Autowired
	private com.sumavision.bvc.device.group.dao.CombineVideoDAO deviceGroupCombineVideoDao;
	
	@Autowired
	private CombineAudioDAO combineAudioDao;
	
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
	private GroupMemberDAO groupMemberDao;
	
	@Autowired
	private TerminalBundleUserPermissionDAO terminalBundleUserPermissionDao;
	
	@Autowired
	private TerminalBundleConferenceHallPermissionDAO terminalBundleConferenceHallPermissionDao;
	
	@Autowired
	private RunningAgendaDAO runningAgendaDao;
	
	@Autowired
	private FolderUserMapDAO folderUserMapDao;
	
	@Autowired
	private AutoCombineService autoCombineService;
	
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

	@Autowired
	private CombineVideoUtil combineVideoUtil;

	@Autowired
	private CommandCommonUtil commandCommonUtil;

	@Autowired
	private QueryUtil queryUtil;

	@Autowired
	private TetrisBvcQueryUtil tetrisBvcQueryUtil;

	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	private Map<String, TerminalPO> terminalPOMap = new HashMap<String, TerminalPO>();
	
	@Autowired
	private AgendaDAO agendaDao;
	
	@Autowired
	private BusinessReturnService businessReturnService;
	
	/** 级联使用：联网接入层id */
	private String localLayerId = null;
	
//	private Map<String, TerminalBundlePO> terminalBundleMap = new HashMap<String, TerminalBundlePO>();
	
	/**
	 * 执行及停止议程<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月18日 下午2:44:58
	 * @param groupId
	 * @param runAgendaIds 执行的议程
	 * @param stopAgendaIds 停止的议程
	 * @throws Exception
	 */
	public void runAndStopAgenda(Long groupId, List<Long> runAgendaIds, List<Long> stopAgendaIds) throws Exception{
		
		if(runAgendaIds == null) runAgendaIds = new ArrayList<Long>();
		if(stopAgendaIds == null) stopAgendaIds = new ArrayList<Long>();		
		
		//先获取原来的转发关系
//		List<CommonForwardPO> oldForwards = commonForwardDao.findByBusinessId(groupId.toString());
		
		//持久化agenda执行状态。TODO:先校验是否已经执行，也就是判重
		List<RunningAgendaPO> newRunnings = new ArrayList<RunningAgendaPO>();
		for(Long runAgendaId : runAgendaIds){
			RunningAgendaPO r = new RunningAgendaPO(groupId, runAgendaId);
			newRunnings.add(r);
		}
		runningAgendaDao.save(newRunnings);
		
		List<RunningAgendaPO> stopRunnings = runningAgendaDao.findByGroupIdAndAgendaIdIn(groupId, stopAgendaIds);
		runningAgendaDao.deleteInBatch(stopRunnings);
								
		executeToFinal(groupId);
	}

	/**
	 * 从成员获取音视频源<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月22日 下午4:19:57
	 * @param sourceMembers
	 * @param businessId 用于写入SourceBO
	 * @param businessInfoType 用于写入SourceBO
	 * @param agendaForwardType 用于写入SourceBO
	 * @return List<SourceBO>
	 * @throws Exception 
	 */
	public List<SourceBO> obtainSource(
			List<GroupMemberPO> sourceMembers,
			String businessId,
			BusinessInfoType businessInfoType) throws Exception{
		return obtainSource(sourceMembers, businessId, businessInfoType, null);
	}
	
//	/**
//	 * 从成员获取音视频源<br/>
//	 * <p>详细描述</p>
//	 * <b>作者:</b>zsy<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2020年7月22日 下午4:19:57
//	 * @param sourceMembers
//	 * @param businessId 用于写入SourceBO
//	 * @param businessInfoType 用于写入SourceBO
//	 * @param agendaForwardType 用于写入SourceBO
//	 * @return List<SourceBO>
//	 * @throws Exception 
//	 */
//	public List<SourceBO> obtainForeignSource(
//			List<GroupMemberPO> sourceMembers,
//			String businessId,
//			BusinessInfoType businessInfoType) throws Exception{
//		return obtainForeignSource(sourceMembers, businessId, businessInfoType, null);
//	}
//	
//	public List<SourceBO> obtainForeignSource(
//			List<GroupMemberPO> sourceMembers,
//			String businessId,
//			BusinessInfoType businessInfoType,
//				AgendaForwardType agendaForwardType) throws Exception{
//		
//		//先把用户查出来
//		List<Long> userIds = new ArrayList<Long>();
//		for(GroupMemberPO member : sourceMembers){
//			GroupMemberType groupMemberType = member.getGroupMemberType();
//			if(groupMemberType.equals(GroupMemberType.MEMBER_USER)){
//				userIds.add(Long.parseLong(member.getOriginId()));
//			}
//		}
//		String userIdListStr = StringUtils.join(userIds.toArray(), ",");
//		List<UserBO> allUserBos = resourceService.queryUserListByIds(userIdListStr, TerminalType.QT_ZK);
//		if(allUserBos == null) allUserBos = new ArrayList<UserBO>();
//		List<FolderUserMap> userMaps = folderUserMapDao.findByUserIdIn(userIds);
//		
//		//确认源（可能多个）
//		List<SourceBO> sourceBOs = new ArrayList<SourceBO>();
//		//	List<ChannelSchemeDTO> srcChannels = new ArrayList<ChannelSchemeDTO>();
//		//	Map<ChannelSchemeDTO, ChannelSchemeDTO> videoAudioMap = new HashMap<ChannelSchemeDTO, ChannelSchemeDTO>();
//		for(GroupMemberPO member : sourceMembers){
//			if(!member.getGroupMemberStatus().equals(GroupMemberStatus.CONNECT)){
//				continue;
//			}
//			//按当前登录的设备 terminalId 找
//			String originId = member.getOriginId();
//			Long terminalId = member.getTerminalId();
//			GroupMemberType groupMemberType = member.getGroupMemberType();
//			OriginType originType = member.getOriginType();
//			List<String> bundleIds = new ArrayList<String>();
//			
//			if(groupMemberType.equals(GroupMemberType.MEMBER_USER)){
//				
//				if(originType.equals(OriginType.OUTER)){
//					//级联用户，bundleId用成员的uuid
//					SourceBO sourceBO = ldapUserMemberToSource(member, businessId, businessInfoType, agendaForwardType);
//					sourceBOs.add(sourceBO);
//					continue;
//				}else{					
//					List<TerminalBundleUserPermissionPO> ps = terminalBundleUserPermissionDao.findByUserIdAndTerminalId(originId, terminalId);
//					//从terminalBundleId找到终端设备，确认类型，找到这里的编码器id
//					for(TerminalBundleUserPermissionPO p : ps){
//						Long terminalBundleId = p.getTerminalBundleId();
//						TerminalBundlePO terminalBundlePO = terminalBundleDao.findOne(terminalBundleId);//后续优化成for外头批量查询
//						TerminalBundleType type = terminalBundlePO.getType();
//						if(TerminalBundleType.ENCODER.equals(type) || TerminalBundleType.ENCODER_DECODER.equals(type)){
//							String bundleId = p.getBundleId();
//							bundleIds.add(bundleId);
//						}
//					}
//				}
//				
//			}
//			else if(groupMemberType.equals(GroupMemberType.MEMBER_HALL)){
//				
//				Long hallId = Long.parseLong(member.getOriginId());
//				List<TerminalBundleConferenceHallPermissionPO> hps = terminalBundleConferenceHallPermissionDao.findByConferenceHallId(hallId);
//				for(TerminalBundleConferenceHallPermissionPO hp : hps){
//					
//					Long terminalBundleId = hp.getTerminalBundleId();
//					TerminalBundlePO terminalBundlePO = terminalBundleDao.findOne(terminalBundleId);//后续优化成for外头批量查询
//					TerminalBundleType type = terminalBundlePO.getType();
//					if(TerminalBundleType.ENCODER.equals(type) || TerminalBundleType.ENCODER_DECODER.equals(type)){
//						String bundleId = hp.getBundleId();
//						bundleIds.add(bundleId);
//					}
//				}
//				
//			}
//			else if(groupMemberType.equals(GroupMemberType.MEMBER_DEVICE)){
//				
//				if(originType.equals(OriginType.OUTER)){
//					String videoChannelId = ChannelType.VIDEOENCODE1.getChannelId();
//					String audioChannelId = ChannelType.AUDIOENCODE1.getChannelId();
//					//造数据，videoEncodeChannel,audioEncodeChannel: bundleId channelId baseType channelName
//					ChannelSchemeDTO videoEncodeChannel = new ChannelSchemeDTO()
//							.setBundleId(originId)
//							.setChannelId(videoChannelId)
//							.setBaseType("outer_no_base_type")
//							.setChannelName("outer_no_video_channel_name");
//					ChannelSchemeDTO audioEncodeChannel = new ChannelSchemeDTO()
//							.setBundleId(originId)
//							.setChannelId(audioChannelId)
//							.setBaseType("outer_no_base_type")
//							.setChannelName("outer_no_video_channel_name");
//					SourceBO sourceBO = new SourceBO()
//							.setOriginType(OriginType.OUTER)
//							.setGroupMemberType(member.getGroupMemberType())
//							.setMemberUuid(member.getUuid())
//							.setBusinessId(businessId)
//							.setBusinessInfoType(businessInfoType)
//							.setAgendaForwardType(agendaForwardType)//音/视频，如果没有音频，这里是否应该写成“视频”？
//							.setSrcVideoId(member.getOriginId())
//							.setSrcVideoMemberId(member.getId())
//							.setSrcVideoName(member.getName())
//							.setSrcVideoCode(member.getCode())
//							.setVideoSourceChannel(videoEncodeChannel)
//							.setAudioSourceChannel(audioEncodeChannel)
//							.setSrcAudioId(member.getOriginId())
//							.setSrcAudioMemberId(member.getId())
//							.setSrcAudioName(member.getName())
//							.setSrcAudioCode(member.getCode())
//							.setBundleId(originId);
//					sourceBOs.add(sourceBO);
//				}else{
//					bundleIds.add(originId);
//				}
//				
//			}
//			//查出bundle
////			List<BundlePO> srcBundlePOs = resourceBundleDao.findByBundleIds(bundleIds);
//			
//			//查出视频编码通道
//			List<ChannelSchemeDTO> videoEncodeChannels = resourceChannelDao.findByBundleIdsAndChannelType(bundleIds, ResourceChannelDAO.ENCODE_VIDEO);
//			if(videoEncodeChannels == null) videoEncodeChannels = new ArrayList<ChannelSchemeDTO>();
//			
//			//查出音频编码通道
//			List<ChannelSchemeDTO> audioEncodeChannels = resourceChannelDao.findByBundleIdsAndChannelType(bundleIds, ResourceChannelDAO.ENCODE_AUDIO);
//			if(audioEncodeChannels == null) audioEncodeChannels = new ArrayList<ChannelSchemeDTO>();
//			
//		//		//查出编码1通道
//		//		List<ChannelSchemeDTO> videoEncode1Channels = resourceChannelDao.findByBundleIdsAndChannelId(bundleIds, ChannelType.VIDEOENCODE1.getChannelId());
//		//		if(videoEncode1Channels == null) videoEncode1Channels = new ArrayList<ChannelSchemeDTO>();
//		//		
//		//		List<ChannelSchemeDTO> audioEncode1Channels = resourceChannelDao.findByBundleIdsAndChannelId(bundleIds, ChannelType.AUDIOENCODE1.getChannelId());
//		//		if(audioEncode1Channels == null) audioEncode1Channels = new ArrayList<ChannelSchemeDTO>();
//						
//			//级联设备，bundleId用设备的bundleId
//			for(String bundleId : bundleIds){
//				if(originType.equals(OriginType.OUTER)){
//					String videoChannelId = ChannelType.VIDEOENCODE1.getChannelId();
//					String audioChannelId = ChannelType.AUDIOENCODE1.getChannelId();
//					//造数据，videoEncodeChannel,audioEncodeChannel: bundleId channelId baseType channelName
//					ChannelSchemeDTO videoEncodeChannel = new ChannelSchemeDTO()
//							.setBundleId(bundleId)
//							.setChannelId(videoChannelId)
//							.setBaseType("outer_no_base_type")
//							.setChannelName("outer_no_video_channel_name");
//					ChannelSchemeDTO audioEncodeChannel = new ChannelSchemeDTO()
//							.setBundleId(bundleId)
//							.setChannelId(audioChannelId)
//							.setBaseType("outer_no_base_type")
//							.setChannelName("outer_no_video_channel_name");
//					SourceBO sourceBO = new SourceBO()
//							.setOriginType(OriginType.OUTER)
//							.setGroupMemberType(member.getGroupMemberType())
//							.setMemberUuid(member.getUuid())
//							.setBusinessId(businessId)
//							.setBusinessInfoType(businessInfoType)
//							.setAgendaForwardType(agendaForwardType)//音/视频，如果没有音频，这里是否应该写成“视频”？
//							.setSrcVideoId(member.getOriginId())
//							.setSrcVideoMemberId(member.getId())
//							.setSrcVideoName(member.getName())
//							.setSrcVideoCode(member.getCode())
//							.setVideoSourceChannel(videoEncodeChannel)
//							.setAudioSourceChannel(audioEncodeChannel)
//							.setSrcAudioId(member.getOriginId())
//							.setSrcAudioMemberId(member.getId())
//							.setSrcAudioName(member.getName())
//							.setSrcAudioCode(member.getCode())
//							.setBundleId(bundleId);
//					sourceBOs.add(sourceBO);
//				}
//			}
//			
//		}
//		return sourceBOs;
//		
//	}
	
	public List<SourceBO> obtainSource(
			List<GroupMemberPO> sourceMembers,
			String businessId,
			BusinessInfoType businessInfoType,
			AgendaForwardType agendaForwardType) throws Exception{
		
		//先把用户查出来
		List<Long> userIds = new ArrayList<Long>();
		for(GroupMemberPO member : sourceMembers){
			GroupMemberType groupMemberType = member.getGroupMemberType();
			if(groupMemberType.equals(GroupMemberType.MEMBER_USER)){
				userIds.add(Long.parseLong(member.getOriginId()));
			}
		}
		String userIdListStr = StringUtils.join(userIds.toArray(), ",");
		List<UserBO> allUserBos = resourceService.queryUserListByIds(userIdListStr, TerminalType.QT_ZK);
		if(allUserBos == null) allUserBos = new ArrayList<UserBO>();
		List<FolderUserMap> userMaps = folderUserMapDao.findByUserIdIn(userIds);
		
		//确认源（可能多个）
		List<SourceBO> sourceBOs = new ArrayList<SourceBO>();
//		List<ChannelSchemeDTO> srcChannels = new ArrayList<ChannelSchemeDTO>();
//		Map<ChannelSchemeDTO, ChannelSchemeDTO> videoAudioMap = new HashMap<ChannelSchemeDTO, ChannelSchemeDTO>();
		for(GroupMemberPO member : sourceMembers){
			if(!member.getGroupMemberStatus().equals(GroupMemberStatus.CONNECT)){
				continue;
			}
			//按当前登录的设备 terminalId 找
			String originId = member.getOriginId();
			Long terminalId = member.getTerminalId();
			GroupMemberType groupMemberType = member.getGroupMemberType();
			OriginType originType = member.getOriginType();
			List<String> bundleIds = new ArrayList<String>();
			
			if(groupMemberType.equals(GroupMemberType.MEMBER_USER)){
				
				if(originType.equals(OriginType.OUTER)){
					//级联用户，bundleId用成员的uuid
					SourceBO sourceBO = ldapUserMemberToSource(member, businessId, businessInfoType, agendaForwardType);
					sourceBOs.add(sourceBO);
					continue;
				}else{					
					List<TerminalBundleUserPermissionPO> ps = terminalBundleUserPermissionDao.findByUserIdAndTerminalId(originId, terminalId);
					//从terminalBundleId找到终端设备，确认类型，找到这里的编码器id
					for(TerminalBundleUserPermissionPO p : ps){
						Long terminalBundleId = p.getTerminalBundleId();
						TerminalBundlePO terminalBundlePO = terminalBundleDao.findOne(terminalBundleId);//后续优化成for外头批量查询
						TerminalBundleType type = terminalBundlePO.getType();
						if(TerminalBundleType.ENCODER.equals(type) || TerminalBundleType.ENCODER_DECODER.equals(type)){
							String bundleId = p.getBundleId();
							bundleIds.add(bundleId);
						}
					}
				}
				
			}else if(groupMemberType.equals(GroupMemberType.MEMBER_HALL)){
				
				Long hallId = Long.parseLong(member.getOriginId());
				List<TerminalBundleConferenceHallPermissionPO> hps = terminalBundleConferenceHallPermissionDao.findByConferenceHallId(hallId);
				for(TerminalBundleConferenceHallPermissionPO hp : hps){
					
					Long terminalBundleId = hp.getTerminalBundleId();
					TerminalBundlePO terminalBundlePO = terminalBundleDao.findOne(terminalBundleId);//后续优化成for外头批量查询
					TerminalBundleType type = terminalBundlePO.getType();
					if(TerminalBundleType.ENCODER.equals(type) || TerminalBundleType.ENCODER_DECODER.equals(type)){
						String bundleId = hp.getBundleId();
						bundleIds.add(bundleId);
					}
				}
				
			}else if(groupMemberType.equals(GroupMemberType.MEMBER_DEVICE)){
				
				if(originType.equals(OriginType.OUTER)){
					String videoChannelId = ChannelType.VIDEOENCODE1.getChannelId();
					String audioChannelId = ChannelType.AUDIOENCODE1.getChannelId();
					//造数据，videoEncodeChannel,audioEncodeChannel: bundleId channelId baseType channelName
					ChannelSchemeDTO videoEncodeChannel = new ChannelSchemeDTO()
							.setBundleId(originId)
							.setChannelId(videoChannelId)
							.setBaseType("outer_no_base_type")
							.setChannelName("outer_no_video_channel_name");
					ChannelSchemeDTO audioEncodeChannel = new ChannelSchemeDTO()
							.setBundleId(originId)
							.setChannelId(audioChannelId)
							.setBaseType("outer_no_base_type")
							.setChannelName("outer_no_video_channel_name");
					SourceBO sourceBO = new SourceBO()
							.setOriginType(OriginType.OUTER)
							.setGroupMemberType(member.getGroupMemberType())
							.setMemberUuid(member.getUuid())
							.setBusinessId(businessId)
							.setBusinessInfoType(businessInfoType)
							.setAgendaForwardType(agendaForwardType)//音/视频，如果没有音频，这里是否应该写成“视频”？
							.setSrcVideoId(member.getOriginId())
							.setSrcVideoMemberId(member.getId())
							.setSrcVideoName(member.getName())
							.setSrcVideoCode(member.getCode())
							.setVideoSourceChannel(videoEncodeChannel)
							.setAudioSourceChannel(audioEncodeChannel)
							.setSrcAudioId(member.getOriginId())
							.setSrcAudioMemberId(member.getId())
							.setSrcAudioName(member.getName())
							.setSrcAudioCode(member.getCode())
							.setBundleId(originId);
					sourceBOs.add(sourceBO);
				}else{
					bundleIds.add(originId);
				}
			}
			//查出bundle
			List<BundlePO> srcBundlePOs = resourceBundleDao.findByBundleIds(bundleIds);
			if(srcBundlePOs == null) srcBundlePOs = new ArrayList<BundlePO>();
			
			//查出视频编码通道
			List<ChannelSchemeDTO> videoEncodeChannels = resourceChannelDao.findByBundleIdsAndChannelType(bundleIds, ResourceChannelDAO.ENCODE_VIDEO);
			if(videoEncodeChannels == null) videoEncodeChannels = new ArrayList<ChannelSchemeDTO>();
			
			//查出音频编码通道
			List<ChannelSchemeDTO> audioEncodeChannels = resourceChannelDao.findByBundleIdsAndChannelType(bundleIds, ResourceChannelDAO.ENCODE_AUDIO);
			if(audioEncodeChannels == null) audioEncodeChannels = new ArrayList<ChannelSchemeDTO>();
			
//			//查出编码1通道
//			List<ChannelSchemeDTO> videoEncode1Channels = resourceChannelDao.findByBundleIdsAndChannelId(bundleIds, ChannelType.VIDEOENCODE1.getChannelId());
//			if(videoEncode1Channels == null) videoEncode1Channels = new ArrayList<ChannelSchemeDTO>();
//			
//			List<ChannelSchemeDTO> audioEncode1Channels = resourceChannelDao.findByBundleIdsAndChannelId(bundleIds, ChannelType.AUDIOENCODE1.getChannelId());
//			if(audioEncode1Channels == null) audioEncode1Channels = new ArrayList<ChannelSchemeDTO>();
						
			//级联来的以后没有bundle了
			//级联设备，bundleId用设备的bundleId
			for(BundlePO srcBundlePO : srcBundlePOs){
				if(originType.equals(OriginType.OUTER)){
					String videoChannelId = ChannelType.VIDEOENCODE1.getChannelId();
					String audioChannelId = ChannelType.AUDIOENCODE1.getChannelId();
					//造数据，videoEncodeChannel,audioEncodeChannel: bundleId channelId baseType channelName
					ChannelSchemeDTO videoEncodeChannel = new ChannelSchemeDTO()
							.setBundleId(srcBundlePO.getBundleId())
							.setChannelId(videoChannelId)
							.setBaseType("outer_no_base_type")
							.setChannelName("outer_no_video_channel_name");
					ChannelSchemeDTO audioEncodeChannel = new ChannelSchemeDTO()
							.setBundleId(srcBundlePO.getBundleId())
							.setChannelId(audioChannelId)
							.setBaseType("outer_no_base_type")
							.setChannelName("outer_no_video_channel_name");
					SourceBO sourceBO = new SourceBO()
							.setOriginType(OriginType.OUTER)
							.setGroupMemberType(member.getGroupMemberType())
							.setMemberUuid(member.getUuid())
							.setBusinessId(businessId)
							.setBusinessInfoType(businessInfoType)
							.setAgendaForwardType(agendaForwardType)//音/视频，如果没有音频，这里是否应该写成“视频”？
							.setSrcVideoId(member.getOriginId())
							.setSrcVideoMemberId(member.getId())
							.setSrcVideoName(member.getName())
							.setSrcVideoCode(member.getCode())
							.setVideoSourceChannel(videoEncodeChannel)
							.setVideoBundle(srcBundlePO);
					sourceBO.setAudioSourceChannel(audioEncodeChannel)
							.setAudioBundle(srcBundlePO)
							.setSrcAudioId(member.getOriginId())
							.setSrcAudioMemberId(member.getId())
							.setSrcAudioName(member.getName())
							.setSrcAudioCode(member.getCode());
					sourceBOs.add(sourceBO);
				}
			}
			
			//本系统设备：遍历视频通道，找到对应的音频通道
			for(ChannelSchemeDTO videoEncode1Channel : videoEncodeChannels){
				BundlePO videoBundle = queryUtil.queryBundlePOByBundleId(srcBundlePOs, videoEncode1Channel.getBundleId());
				if(queryUtil.isLdapBundle(videoBundle)) continue;
				SourceBO sourceBO = new SourceBO()
						.setBusinessId(businessId)
						.setBusinessInfoType(businessInfoType)
						.setAgendaForwardType(agendaForwardType)//音/视频，如果没有音频，这里是否应该写成“视频”？
						.setSrcVideoId(member.getOriginId())
						.setSrcVideoMemberId(member.getId())
						.setSrcVideoName(member.getName())
						.setSrcVideoCode(member.getCode())
						.setVideoSourceChannel(videoEncode1Channel)
						.setVideoBundle(videoBundle);
				String bundleId = videoEncode1Channel.getBundleId();
				List<ChannelSchemeDTO> audios = queryUtil.queryChannelDTOsByBundleId(audioEncodeChannels, bundleId);
				if(audios.size() > 0){
//					videoAudioMap.put(videoEncode1Channel, audios.get(0));
					BundlePO audioBundle = queryUtil.queryBundlePOByBundleId(srcBundlePOs, audios.get(0).getBundleId());
					sourceBO.setAudioSourceChannel(audios.get(0))
						.setAudioBundle(audioBundle)
						.setSrcAudioId(member.getOriginId())
						.setSrcAudioMemberId(member.getId())
						.setSrcAudioName(member.getName())
						.setSrcAudioCode(member.getCode());
				}else{
//					videoAudioMap.put(videoEncode1Channel, null);
				}
				sourceBOs.add(sourceBO);
			}
			
			//遍历srcBundlePOs，处理外部系统
		}
		return sourceBOs;
	}
	
	/*
	 public List<SourceBO> obtainSource(
			List<GroupMemberPO> sourceMembers,
			String businessId,
			BusinessInfoType businessInfoType,
			AgendaForwardType agendaForwardType) throws Exception{
		
		//先把用户查出来
		List<Long> userIds = new ArrayList<Long>();
		for(GroupMemberPO member : sourceMembers){
			GroupMemberType groupMemberType = member.getGroupMemberType();
			if(groupMemberType.equals(GroupMemberType.MEMBER_USER)){
				userIds.add(Long.parseLong(member.getOriginId()));
			}
		}
		String userIdListStr = StringUtils.join(userIds.toArray(), ",");
		List<UserBO> allUserBos = resourceService.queryUserListByIds(userIdListStr, TerminalType.QT_ZK);
		if(allUserBos == null) allUserBos = new ArrayList<UserBO>();
		List<FolderUserMap> userMaps = folderUserMapDao.findByUserIdIn(userIds);
		
		//确认源（可能多个）
		List<SourceBO> sourceBOs = new ArrayList<SourceBO>();
//		List<ChannelSchemeDTO> srcChannels = new ArrayList<ChannelSchemeDTO>();
//		Map<ChannelSchemeDTO, ChannelSchemeDTO> videoAudioMap = new HashMap<ChannelSchemeDTO, ChannelSchemeDTO>();
		for(GroupMemberPO member : sourceMembers){
			if(!member.getGroupMemberStatus().equals(GroupMemberStatus.CONNECT)){
				continue;
			}
			//按当前登录的设备 terminalId 找
			String originId = member.getOriginId();
			Long terminalId = member.getTerminalId();
			GroupMemberType groupMemberType = member.getGroupMemberType();
			OriginType originType = member.getOriginType();
			List<String> bundleIds = new ArrayList<String>();
			
			if(groupMemberType.equals(GroupMemberType.MEMBER_USER)){
				
				if(originType.equals(OriginType.OUTER)){
					//级联用户，bundleId用成员的uuid
					SourceBO sourceBO = ldapUserMemberToSource(member, businessId, businessInfoType, agendaForwardType);
					sourceBOs.add(sourceBO);
					continue;
				}else{					
					List<TerminalBundleUserPermissionPO> ps = terminalBundleUserPermissionDao.findByUserIdAndTerminalId(originId, terminalId);
					//从terminalBundleId找到终端设备，确认类型，找到这里的编码器id
					for(TerminalBundleUserPermissionPO p : ps){
						Long terminalBundleId = p.getTerminalBundleId();
						TerminalBundlePO terminalBundlePO = terminalBundleDao.findOne(terminalBundleId);//后续优化成for外头批量查询
						TerminalBundleType type = terminalBundlePO.getType();
						if(TerminalBundleType.ENCODER.equals(type) || TerminalBundleType.ENCODER_DECODER.equals(type)){
							String bundleId = p.getBundleId();
							bundleIds.add(bundleId);
						}
					}
				}
				
			}else if(groupMemberType.equals(GroupMemberType.MEMBER_HALL)){
				
				Long hallId = Long.parseLong(member.getOriginId());
				List<TerminalBundleConferenceHallPermissionPO> hps = terminalBundleConferenceHallPermissionDao.findByConferenceHallId(hallId);
				for(TerminalBundleConferenceHallPermissionPO hp : hps){
					
					Long terminalBundleId = hp.getTerminalBundleId();
					TerminalBundlePO terminalBundlePO = terminalBundleDao.findOne(terminalBundleId);//后续优化成for外头批量查询
					TerminalBundleType type = terminalBundlePO.getType();
					if(TerminalBundleType.ENCODER.equals(type) || TerminalBundleType.ENCODER_DECODER.equals(type)){
						String bundleId = hp.getBundleId();
						bundleIds.add(bundleId);
					}
				}
				
			}else if(groupMemberType.equals(GroupMemberType.MEMBER_DEVICE)){
				
				if(originType.equals(OriginType.OUTER)){
//					
				}
				
				bundleIds.add(originId);
				
			}
			//查出bundle
			List<BundlePO> srcBundlePOs = resourceBundleDao.findByBundleIds(bundleIds);
			if(srcBundlePOs == null) srcBundlePOs = new ArrayList<BundlePO>();
			
			//查出视频编码通道
			List<ChannelSchemeDTO> videoEncodeChannels = resourceChannelDao.findByBundleIdsAndChannelType(bundleIds, ResourceChannelDAO.ENCODE_VIDEO);
			if(videoEncodeChannels == null) videoEncodeChannels = new ArrayList<ChannelSchemeDTO>();
			
			//查出音频编码通道
			List<ChannelSchemeDTO> audioEncodeChannels = resourceChannelDao.findByBundleIdsAndChannelType(bundleIds, ResourceChannelDAO.ENCODE_AUDIO);
			if(audioEncodeChannels == null) audioEncodeChannels = new ArrayList<ChannelSchemeDTO>();
			
//			//查出编码1通道
//			List<ChannelSchemeDTO> videoEncode1Channels = resourceChannelDao.findByBundleIdsAndChannelId(bundleIds, ChannelType.VIDEOENCODE1.getChannelId());
//			if(videoEncode1Channels == null) videoEncode1Channels = new ArrayList<ChannelSchemeDTO>();
//			
//			List<ChannelSchemeDTO> audioEncode1Channels = resourceChannelDao.findByBundleIdsAndChannelId(bundleIds, ChannelType.AUDIOENCODE1.getChannelId());
//			if(audioEncode1Channels == null) audioEncode1Channels = new ArrayList<ChannelSchemeDTO>();
						
			//级联来的以后没有bundle了
			//级联设备，bundleId用设备的bundleId
			for(BundlePO srcBundlePO : srcBundlePOs){
				if(originType.equals(OriginType.OUTER)){
					String videoChannelId = ChannelType.VIDEOENCODE1.getChannelId();
					String audioChannelId = ChannelType.AUDIOENCODE1.getChannelId();
					//造数据，videoEncodeChannel,audioEncodeChannel: bundleId channelId baseType channelName
					ChannelSchemeDTO videoEncodeChannel = new ChannelSchemeDTO()
							.setBundleId(srcBundlePO.getBundleId())
							.setChannelId(videoChannelId)
							.setBaseType("outer_no_base_type")
							.setChannelName("outer_no_video_channel_name");
					ChannelSchemeDTO audioEncodeChannel = new ChannelSchemeDTO()
							.setBundleId(srcBundlePO.getBundleId())
							.setChannelId(audioChannelId)
							.setBaseType("outer_no_base_type")
							.setChannelName("outer_no_video_channel_name");
					SourceBO sourceBO = new SourceBO()
							.setOriginType(OriginType.OUTER)
							.setGroupMemberType(member.getGroupMemberType())
							.setMemberUuid(member.getUuid())
							.setBusinessId(businessId)
							.setBusinessInfoType(businessInfoType)
							.setAgendaForwardType(agendaForwardType)//音/视频，如果没有音频，这里是否应该写成“视频”？
							.setSrcVideoId(member.getOriginId())
							.setSrcVideoMemberId(member.getId())
							.setSrcVideoName(member.getName())
							.setSrcVideoCode(member.getCode())
							.setVideoSourceChannel(videoEncodeChannel)
							.setVideoBundle(srcBundlePO);
					sourceBO.setAudioSourceChannel(audioEncodeChannel)
							.setAudioBundle(srcBundlePO)
							.setSrcAudioId(member.getOriginId())
							.setSrcAudioMemberId(member.getId())
							.setSrcAudioName(member.getName())
							.setSrcAudioCode(member.getCode());
					sourceBOs.add(sourceBO);
				}
			}
			
			//本系统设备：遍历视频通道，找到对应的音频通道
			for(ChannelSchemeDTO videoEncode1Channel : videoEncodeChannels){
				BundlePO videoBundle = queryUtil.queryBundlePOByBundleId(srcBundlePOs, videoEncode1Channel.getBundleId());
				if(queryUtil.isLdapBundle(videoBundle)) continue;
				SourceBO sourceBO = new SourceBO()
						.setBusinessId(businessId)
						.setBusinessInfoType(businessInfoType)
						.setAgendaForwardType(agendaForwardType)//音/视频，如果没有音频，这里是否应该写成“视频”？
						.setSrcVideoId(member.getOriginId())
						.setSrcVideoMemberId(member.getId())
						.setSrcVideoName(member.getName())
						.setSrcVideoCode(member.getCode())
						.setVideoSourceChannel(videoEncode1Channel)
						.setVideoBundle(videoBundle);
				String bundleId = videoEncode1Channel.getBundleId();
				List<ChannelSchemeDTO> audios = queryUtil.queryChannelDTOsByBundleId(audioEncodeChannels, bundleId);
				if(audios.size() > 0){
//					videoAudioMap.put(videoEncode1Channel, audios.get(0));
					BundlePO audioBundle = queryUtil.queryBundlePOByBundleId(srcBundlePOs, audios.get(0).getBundleId());
					sourceBO.setAudioSourceChannel(audios.get(0))
						.setAudioBundle(audioBundle)
						.setSrcAudioId(member.getOriginId())
						.setSrcAudioMemberId(member.getId())
						.setSrcAudioName(member.getName())
						.setSrcAudioCode(member.getCode());
				}else{
//					videoAudioMap.put(videoEncode1Channel, null);
				}
				sourceBOs.add(sourceBO);
			}
			
			//遍历srcBundlePOs，处理外部系统
		}
		return sourceBOs;
	}
	 */
	
	/**
	 * 从成员和终端通道获取音视频源<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月18日 下午2:46:14
	 * @param sourceMembers
	 * @param channels 终端通道
	 * @param businessId 用于写入SourceBO
	 * @param businessInfoType 用于写入SourceBO
	 * @param agendaForwardType 音/视频类型
	 * @return
	 */
	public List<SourceBO> obtainSourceFromMembersAndChannels(
			List<GroupMemberPO> sourceMembers, List<TerminalChannelPO> channels,
			String businessId, BusinessInfoType businessInfoType, AgendaForwardType agendaForwardType){
		
		//从通道获得相关的终端id
		Set<Long> terminalBundleIds = businessCommonService.obtainTerminalBundleIdsFromTerminalChannel(channels);
//		Set<String> channelIds = businessCommonService.obtainChannelIds(channels);
		
		//确认源，每个成员应该有1个
		List<SourceBO> sourceBOs = new ArrayList<SourceBO>();
		for(GroupMemberPO member : sourceMembers){
			if(!member.getGroupMemberStatus().equals(GroupMemberStatus.CONNECT)){
				continue;
			}
			//按当前登录的设备 terminalId 找
			String originId = member.getOriginId();
			Long terminalId = member.getTerminalId();
			GroupMemberType groupMemberType = member.getGroupMemberType();
			OriginType originType = member.getOriginType();
			List<String> bundleIds = new ArrayList<String>();
			
			//该用户成员绑定的多个设备
			List<TerminalBundleUserPermissionPO> ps = null;
			//该会场成员绑定的多个设备
			List<TerminalBundleConferenceHallPermissionPO> hps = null;
						
			if(groupMemberType.equals(GroupMemberType.MEMBER_USER)){
				
				if(originType.equals(OriginType.OUTER)){
					//级联用户，bundleId用成员的uuid
					SourceBO sourceBO = ldapUserMemberToSource(member, businessId, businessInfoType, agendaForwardType);
					sourceBOs.add(sourceBO);
					continue;
				}else{
					//该用户成员绑定的多个设备
					ps = terminalBundleUserPermissionDao.findByUserIdAndTerminalId(originId, terminalId);
					
					//从terminalBundleId找到终端设备，确认类型，找到这里的编码器id
					for(TerminalBundleUserPermissionPO p : ps){
						
						//如果通道不包含该终端，则跳过
						if(!terminalBundleIds.contains(p.getTerminalBundleId())){
							continue;
						}					
						
						//以下判断目前不必要。查询出来的一定是编码设备
						Long terminalBundleId = p.getTerminalBundleId();
						TerminalBundlePO terminalBundlePO = terminalBundleDao.findOne(terminalBundleId);//后续优化成批量，缓存
						TerminalBundleType type = terminalBundlePO.getType();
						if(TerminalBundleType.ENCODER.equals(type) || TerminalBundleType.ENCODER_DECODER.equals(type)){
							String bundleId = p.getBundleId();
							bundleIds.add(bundleId);
						}
					}
				}
				
			}if(groupMemberType.equals(GroupMemberType.MEMBER_HALL)){
								
				Long hallId = Long.parseLong(member.getOriginId());
				hps = terminalBundleConferenceHallPermissionDao.findByConferenceHallId(hallId);
				for(TerminalBundleConferenceHallPermissionPO hp : hps){
					
					//如果通道不包含该终端，则跳过
					if(!terminalBundleIds.contains(hp.getTerminalBundleId())){
						continue;
					}
					
					Long terminalBundleId = hp.getTerminalBundleId();
					TerminalBundlePO terminalBundlePO = terminalBundleDao.findOne(terminalBundleId);//后续优化成批量，缓存
					TerminalBundleType type = terminalBundlePO.getType();
					if(TerminalBundleType.ENCODER.equals(type) || TerminalBundleType.ENCODER_DECODER.equals(type)){
						String bundleId = hp.getBundleId();
						bundleIds.add(bundleId);
					}
				}
				
			}else if(groupMemberType.equals(GroupMemberType.MEMBER_DEVICE)){
				bundleIds.add(originId);
			}
			
			//查出bundle，该成员与所需通道相关的bundle
			List<BundlePO> srcBundlePOs = resourceBundleDao.findByBundleIds(bundleIds);
			if(srcBundlePOs == null) srcBundlePOs = new ArrayList<BundlePO>();
			
			//查出编码通道
			List<ChannelSchemeDTO> encodeChannels = resourceChannelDao.findByBundleIdsAndChannelType(bundleIds, 0);
			if(encodeChannels == null) encodeChannels = new ArrayList<ChannelSchemeDTO>();
			
			//获取需要角色通道对应的ChannelSchemeDTO
			List<ChannelSchemeDTO> useEncodeChannels = new ArrayList<ChannelSchemeDTO>();
			for(TerminalChannelPO channel : channels){
				Long terminalBundleId = channel.getTerminalBundleId();
				//该成员对应的该TerminalBundlePO只有一个
				TerminalBundleUserPermissionPO p = tetrisBvcQueryUtil.queryTerminalBundleUserPermissionByTerminalBundleId(ps, terminalBundleId);
				if(p != null){
					String bundleId = p.getBundleId();
					ChannelSchemeDTO channelDTO = queryUtil.queryChannelDTOsByBundleIdAndChannelId(encodeChannels, bundleId, channel.getRealChannelId());
					if(channelDTO != null){
						useEncodeChannels.add(channelDTO);
					}
				}
				
				TerminalBundleConferenceHallPermissionPO hp = tetrisBvcQueryUtil.queryTerminalBundleConferenceHallPermissionByTerminalBundleId(hps, terminalBundleId);
				if(hp != null){
					String bundleId = hp.getBundleId();
					ChannelSchemeDTO channelDTO = queryUtil.queryChannelDTOsByBundleIdAndChannelId(encodeChannels, bundleId, channel.getRealChannelId());
					if(channelDTO != null){
						useEncodeChannels.add(channelDTO);
					}
				}
			}
			
			//遍历useEncodeChannels，找到对应的音频通道
			for(ChannelSchemeDTO encodeChannel : useEncodeChannels){
				
				if(encodeChannel.getChannelId().toLowerCase().contains("video")){					
					BundlePO videoBundle = queryUtil.queryBundlePOByBundleId(srcBundlePOs, encodeChannel.getBundleId());
					SourceBO sourceBO = new SourceBO()
							.setBusinessId(businessId)
							.setBusinessInfoType(businessInfoType)
							.setAgendaForwardType(agendaForwardType)
							.setSrcVideoId(member.getOriginId())
							.setSrcVideoMemberId(member.getId())
							.setSrcVideoName(member.getName())
							.setSrcVideoCode(member.getCode())
							.setVideoSourceChannel(encodeChannel)
							.setVideoBundle(videoBundle);
					String bundleId = encodeChannel.getBundleId();
					List<ChannelSchemeDTO> bundleChannels = queryUtil.queryChannelDTOsByBundleId(useEncodeChannels, bundleId);
					if(bundleChannels!=null && bundleChannels.size() > 0){
						for(ChannelSchemeDTO channelDTO : bundleChannels){
							if(channelDTO.getChannelId().toLowerCase().contains("audio")){
								BundlePO audioBundle = queryUtil.queryBundlePOByBundleId(srcBundlePOs, bundleId);
								sourceBO.setAudioSourceChannel(channelDTO)
									.setAudioBundle(audioBundle)
									.setSrcAudioId(member.getOriginId())
									.setSrcAudioMemberId(member.getId())
									.setSrcAudioName(member.getName())
									.setSrcAudioCode(member.getCode());
								break;
							}
						}
					}else{
	//					videoAudioMap.put(videoEncode1Channel, null);
					}
					sourceBOs.add(sourceBO);
				}
			}
		}
		return sourceBOs;
	}

	public List<SourceBO> obtainVideoSourceFromRoleChannelId(
			Long groupId,
			Long roleChannelId,
			BusinessInfoType businessInfoType,
			AgendaForwardType agendaForwardType){
		List<Long> roleChannelIds = new ArrayList<Long>();
//		if(AgendaForwardType.AUDIO_VIDEO.equals(type) || AgendaForwardType.VIDEO.equals(type)){
			RoleChannelPO sourceRoleVideoChannel = roleChannelDao.findOne(roleChannelId);
			roleChannelIds.add(sourceRoleVideoChannel.getId());
//		}
//		if(AgendaForwardType.AUDIO_VIDEO.equals(type) || AgendaForwardType.AUDIO.equals(type)){
//			String audioSourceId = agendaForward.getAudioSourceId();
//			RoleChannelPO sourceRoleAudioChannel = roleChannelDao.findOne(Long.valueOf(audioSourceId));
//			roleChannelIds.add(sourceRoleAudioChannel.getId());
//		}
//		List<TerminalBundleChannelPO> channels = terminalBundleChannelDao.findByRoleChannelIdIn(new ArrayListWrapper<Long>().add(sourceRoleChannel.getId()).getList());
		List<TerminalChannelPO> channels = terminalChannelDao.findByRoleChannelIdIn(roleChannelIds);//只有1或2个？
		
		RoleChannelPO sourceRoleChannel = roleChannelDao.findOne(roleChannelId);
		List<GroupMemberPO> sourceMembers2 = groupMemberDao.findByGroupIdAndRoleId(groupId, sourceRoleChannel.getRoleId());								
		List<SourceBO> sourceBOs = obtainSourceFromMembersAndChannels(sourceMembers2, channels, groupId.toString(), businessInfoType, agendaForwardType);
		return sourceBOs;
	}

	/**
	 * 从源和目的生成成员转发
	 * 方法概述<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月18日 下午2:51:50
	 * @param dstMembers
	 * @param sourceBOs
	 * @param outerGroup 是否时外部系统创建的
	 * @return 
	 * @throws Exception 
	 */
	public List<CommonForwardPO> obtainCommonForwardsFromSource(List<GroupMemberPO> dstMembers, List<SourceBO> sourceBOs, Boolean outerGroup) throws Exception{
		List<CommonForwardPO> forwards = new ArrayList<CommonForwardPO>();
		for(GroupMemberPO dstMember : dstMembers){
			if(!dstMember.getGroupMemberStatus().equals(GroupMemberStatus.CONNECT)){
				continue;
			}
			
//			//先过滤掉自己
//			List<SourceBO> thisMemberSourceBOs = new ArrayList<SourceBO>();
//			for(SourceBO sourceBO : sourceBOs){
//				if(!dstMember.getId().equals(sourceBO.getSrcVideoMemberId())){
//					thisMemberSourceBOs.add(sourceBO);
//				}
//			}
			
			for(SourceBO sourceBO : sourceBOs){
				
				//过滤掉自己看自己
				if(dstMember.getId().equals(sourceBO.getSrcVideoMemberId())) continue;
				
				CommonForwardPO forward = new CommonForwardPO();
				forward.setBusinessId(sourceBO.getBusinessId());
				forward.setBusinessInfoType(sourceBO.getBusinessInfoType());
				forward.setType(sourceBO.getAgendaForwardType());
				forward.setVideoSourceType(sourceBO.getVideoSourceType());
				forward.setAudioSourceType(sourceBO.getAudioSourceType());
				forward.setDstMemberId(dstMember.getId());				
				
				AgendaForwardType agendaForwardType = sourceBO.getAgendaForwardType();
				if(AgendaForwardType.AUDIO_VIDEO.equals(agendaForwardType) || AgendaForwardType.VIDEO.equals(agendaForwardType)){
															
					AgendaSourceType videoSourceType = sourceBO.getVideoSourceType();
					if(AgendaSourceType.COMBINE_VIDEO.equals(videoSourceType)){
						forward.setSrcId(sourceBO.getSrcVideoId());
						forward.setSrcName(sourceBO.getSrcVideoName());
						forward.setSrcMemberId(sourceBO.getSrcVideoMemberId());
					}else{
						if(outerGroup){
							ChannelSchemeDTO video = sourceBO.getVideoSourceChannel();
							forward.setSrcId(sourceBO.getSrcVideoId());
							forward.setSrcName(sourceBO.getSrcVideoName());
							forward.setSrcMemberId(sourceBO.getSrcVideoMemberId());
//							forward.setSrcCode(bundlePO.getUsername());
//							forward.setSrcBundleName(bundlePO.getBundleName());
//							forward.setSrcBundleType(bundlePO.getBundleType());
							forward.setSrcBundleId(sourceBO.getBundleId());
							localLayerId = resourceRemoteService.queryLocalLayerId();
							forward.setSrcLayerId(localLayerId);
							forward.setSrcChannelId(video.getChannelId());
							forward.setSrcBaseType(video.getBaseType());
							forward.setSrcChannelName(video.getChannelName());
						}else{
							ChannelSchemeDTO video = sourceBO.getVideoSourceChannel();
//							BundlePO bundlePO = bundleDao.findByBundleId(video.getBundleId());
							BundlePO bundlePO = sourceBO.getVideoBundle();
							forward.setSrcId(sourceBO.getSrcVideoId());
							forward.setSrcName(sourceBO.getSrcVideoName());
							forward.setSrcMemberId(sourceBO.getSrcVideoMemberId());
							forward.setSrcCode(bundlePO.getUsername());
							forward.setSrcBundleName(bundlePO.getBundleName());
							forward.setSrcBundleType(bundlePO.getBundleType());
							forward.setSrcBundleId(bundlePO.getBundleId());
							forward.setSrcLayerId(bundlePO.getAccessNodeUid());
							if(queryUtil.isLdapBundle(bundlePO)){
								if(localLayerId == null) localLayerId = resourceRemoteService.queryLocalLayerId();
								forward.setSrcLayerId(localLayerId);
							}
							forward.setSrcChannelId(video.getChannelId());
							forward.setSrcBaseType(video.getBaseType());
							forward.setSrcChannelName(video.getChannelName());
							if(Boolean.TRUE.equals(bundlePO.getMulticastEncode())){
								String addr = multicastService.addrAddPort(bundlePO.getMulticastEncodeAddr(), 0);
								forward.setVideoTransmissionMode(TransmissionMode.MULTICAST);
								forward.setVideoMultiAddr(addr);
								forward.setVideoMultiSrcAddr(bundlePO.getMulticastSourceIp());
							}

						}
					}
				}
				if(AgendaForwardType.AUDIO_VIDEO.equals(agendaForwardType) || AgendaForwardType.AUDIO.equals(agendaForwardType)){
					
					AgendaSourceType audioSourceType = sourceBO.getAudioSourceType();
					if(AgendaSourceType.COMBINE_AUDIO.equals(audioSourceType)){
						forward.setSrcAudioId(sourceBO.getSrcAudioId());
						forward.setSrcAudioName(sourceBO.getSrcAudioName());
						forward.setSrcAudioMemberId(sourceBO.getSrcAudioMemberId());
					}else{
						if(outerGroup){
							ChannelSchemeDTO audio = sourceBO.getAudioSourceChannel();
							if(audio != null){
//								BundlePO audioBundlePO = bundleDao.findByBundleId(audio.getBundleId());
//								BundlePO audioBundlePO = sourceBO.getAudioBundle();
								forward.setSrcAudioId(sourceBO.getSrcAudioId());
								forward.setSrcAudioName(sourceBO.getSrcAudioName());
								forward.setSrcAudioMemberId(sourceBO.getSrcAudioMemberId());
//								forward.setSrcAudioCode(audioBundlePO.getUsername());
//								forward.setSrcAudioBundleName(audioBundlePO.getBundleName());
//								forward.setSrcAudioBundleType(audioBundlePO.getBundleType());
								forward.setSrcAudioBundleId(sourceBO.getBundleId());
								forward.setSrcAudioLayerId(localLayerId);
								forward.setSrcAudioChannelId(audio.getChannelId());
								forward.setSrcAudioBaseType(audio.getBaseType());
								forward.setSrcAudioChannelName(audio.getChannelName());
							}
						}else{
							ChannelSchemeDTO audio = sourceBO.getAudioSourceChannel();
							if(audio != null){
//								BundlePO audioBundlePO = bundleDao.findByBundleId(audio.getBundleId());
								BundlePO audioBundlePO = sourceBO.getAudioBundle();
								forward.setSrcAudioId(sourceBO.getSrcAudioId());
								forward.setSrcAudioName(sourceBO.getSrcAudioName());
								forward.setSrcAudioMemberId(sourceBO.getSrcAudioMemberId());
								forward.setSrcAudioCode(audioBundlePO.getUsername());
								forward.setSrcAudioBundleName(audioBundlePO.getBundleName());
								forward.setSrcAudioBundleType(audioBundlePO.getBundleType());
								forward.setSrcAudioBundleId(audioBundlePO.getBundleId());
								forward.setSrcAudioLayerId(audioBundlePO.getAccessNodeUid());
								if(queryUtil.isLdapBundle(audioBundlePO)){
									if(localLayerId == null) localLayerId = resourceRemoteService.queryLocalLayerId();
									forward.setSrcAudioLayerId(localLayerId);
								}
								forward.setSrcAudioChannelId(audio.getChannelId());
								forward.setSrcAudioBaseType(audio.getBaseType());
								forward.setSrcAudioChannelName(audio.getChannelName());
								if(Boolean.TRUE.equals(audioBundlePO.getMulticastEncode())){
									String addr = multicastService.addrAddPort(audioBundlePO.getMulticastEncodeAddr(), 2);
									forward.setAudioTransmissionMode(TransmissionMode.MULTICAST);
									forward.setAudioMultiAddr(addr);
									forward.setAudioMultiSrcAddr(audioBundlePO.getMulticastSourceIp());
								}
							}
						}
						
						
					}
				}
				
//				videoForward.setDstDeviceType(DstDeviceType.DEVICE);
				//TODO:set srcName relativeUuid businessType businessId dstId DstDeviceType
				
				forwards.add(forward);
				
				//TODO:校验该转发是否已经存在
				//TODO:分别校验音、视频的转发是否能执行
//				PageTaskBO taskBO = new PageTaskBO();
				//TODO:set
//				newTasks.add(taskBO);
			}
//			pageTaskService.addAndRemoveTasks(pageInfo, newTasks, null);
		}
		
		return forwards;
	}

	/**
	 * 从议程生成转发的整体方法<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月18日 下午3:27:16
	 * @param agenda
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	public List<CommonForwardPO> obtainCommonForwards(AgendaPO agenda, Long groupId) throws Exception{

		UserVO user = userQuery.current();
		GroupPO group = groupDao.findOne(groupId);
		GroupStatus groupStatus = group.getStatus();
		boolean outerGroup = false;
		if(OriginType.OUTER.equals(group.getOriginType())){
			outerGroup = true;
		}
		
		List<CommonForwardPO> result = new ArrayList<CommonForwardPO>();
		//TODO:这个List<SourceBO>应该改成Set，否则无法判重，还需要给SourceBO写hashCode和equals方法
		Map<GroupMemberPO, List<SourceBO>> memberSourceMap = new HashMap<GroupMemberPO, List<SourceBO>>();
		
		//查找该议程的转发
		List<AgendaForwardPO> agendaForwards = agendaForwardDao.findByAgendaId(agenda.getId());
		
		//遍历转发
		for(AgendaForwardPO agendaForward : agendaForwards){
			
			if(agendaForward.getType().equals(AgendaForwardType.AUDIO)) continue;
			
			//业务类型
			BusinessInfoType businessInfoType = agendaForward.getBusinessInfoType();
			if(businessInfoType == null){
				businessInfoType = agenda.getBusinessInfoType();
			}
			if(businessInfoType == null){
				//TODO:根据业务组类型确定
			}
			
			//转发业务类型
			AgendaForwardType type = agendaForward.getType();
			
			//----------确认源（可能多个）----------
			List<SourceBO> sourceBOs = new ArrayList<SourceBO>();
			AgendaSourceType sourceType = agendaForward.getSourceType();
			String sourceId = agendaForward.getSourceId();
			switch (sourceType) {
			case ROLE:
				RolePO sourceRole = roleDao.findOne(Long.valueOf(sourceId));
				List<GroupMemberPO> sourceMembers = groupMemberDao.findByGroupIdAndRoleId(groupId, sourceRole.getId());
								
				//这里直接获取了音视频，没有根据议程下的转发来获取。后续完善：查询该视频转发是否有对应的音频，如果没有则不生成音频源
				sourceBOs = obtainSource(sourceMembers, groupId.toString(), businessInfoType, type);
				
				break;
				
			case ROLE_CHANNEL:
				List<Long> roleChannelIds = new ArrayList<Long>();
				if(AgendaForwardType.AUDIO_VIDEO.equals(type) || AgendaForwardType.VIDEO.equals(type)){
					RoleChannelPO sourceRoleVideoChannel = roleChannelDao.findOne(Long.valueOf(sourceId));
					roleChannelIds.add(sourceRoleVideoChannel.getId());
				}
				if(AgendaForwardType.AUDIO_VIDEO.equals(type) || AgendaForwardType.AUDIO.equals(type)){
					String audioSourceId = agendaForward.getAudioSourceId();
					RoleChannelPO sourceRoleAudioChannel = roleChannelDao.findOne(Long.valueOf(audioSourceId));
					roleChannelIds.add(sourceRoleAudioChannel.getId());
				}
//				List<TerminalBundleChannelPO> channels = terminalBundleChannelDao.findByRoleChannelIdIn(new ArrayListWrapper<Long>().add(sourceRoleChannel.getId()).getList());
				List<TerminalChannelPO> channels = terminalChannelDao.findByRoleChannelIdIn(roleChannelIds);//只有1或2个？
				
				//后续完善：这里默认认为转发的音视频源都来源于同一个成员；这里没有处理对于“只有音频、没有视频”的转发
				RoleChannelPO sourceRoleChannel = roleChannelDao.findOne(Long.valueOf(sourceId));
				List<GroupMemberPO> sourceMembers2 = groupMemberDao.findByGroupIdAndRoleId(groupId, sourceRoleChannel.getRoleId());								
				sourceBOs = obtainSourceFromMembersAndChannels(sourceMembers2, channels, groupId.toString(), businessInfoType, type);
				
				break;
			default:
				break;
			}
			
			
			//----------确认目的----------
			AgendaDestinationType destinationType = agendaForward.getDestinationType();
			String destinationId = agendaForward.getDestinationId();
			List<CommonForwardPO> forwards = new ArrayList<CommonForwardPO>();
			switch (destinationType) {
			case ROLE:
				RolePO dstRole = roleDao.findOne(Long.valueOf(destinationId));
				List<GroupMemberPO> dstMembers = groupMemberDao.findByGroupIdAndRoleId(groupId, dstRole.getId());
				
				for(GroupMemberPO dstMember : dstMembers){
					List<SourceBO> memberSourceBOs = memberSourceMap.get(dstMember);
					if(memberSourceBOs == null){
						memberSourceBOs = new ArrayList<SourceBO>();
						memberSourceMap.put(dstMember, memberSourceBOs);
					}
					memberSourceBOs.addAll(sourceBOs);
				}
				
				//从源和目的生成转发
//				forwards = obtainCommonForwardsFromSource(pageDstMembers, sourceBOs);
//				result.addAll(forwards);
				
				break;
			default:
				break;
			}
		}
		
		//----------统一合屏管理
		List<com.sumavision.bvc.device.group.po.CombineVideoPO> allCombineVideos = deviceGroupCombineVideoDao.findByReconGroupId(groupId);
		List<com.sumavision.bvc.device.group.po.CombineVideoPO> stopCombineVideos = deviceGroupCombineVideoDao.findByReconGroupId(groupId);//要停止和删除
//		Set<com.sumavision.bvc.device.group.po.CombineVideoPO> deleteCombineVideos = deviceGroupCombineVideoDao.findByReconGroupId(groupId);//仅删除数据库
		Set<com.sumavision.bvc.device.group.po.CombineVideoPO> deleteCombineVideos = new HashSet<com.sumavision.bvc.device.group.po.CombineVideoPO>();//仅删除数据库
		Set<com.sumavision.bvc.device.group.po.CombineVideoPO> usingCombineVideos = new HashSet<com.sumavision.bvc.device.group.po.CombineVideoPO>();
		
		//----------虚拟源----------
		Set<CombineVideoPO> needCombineVirtualSources = new HashSet<CombineVideoPO>();
		Set<CombineVideoPO> needUpdateVirtualSources = new HashSet<CombineVideoPO>();
		for(GroupMemberPO member : memberSourceMap.keySet()){
//			List<SourceBO> memberSourceBOs = memberSourceMap.get(member);
			if(!GroupMemberType.MEMBER_USER.equals(member.getGroupMemberType())){
				GroupMemberRolePermissionPO groupMemberRolePermission = groupMemberRolePermissionDao.findByGroupMemberId(member.getId());
				if(groupMemberRolePermission != null){
					//agendaLayout是对某一类终端配置的布局
					AgendaLayoutTemplatePO agendaLayout = agendaLayoutTemplateDao.findByAgendaIdAndRoleIdAndTerminalId(agenda.getId(), groupMemberRolePermission.getRoleId(), member.getTerminalId());
					if(agendaLayout != null){
						//layoutVirtualSources是对某一类终端配置的N分屏下的N个虚拟源
						List<LayoutVirtualSourceTemplatePO> layoutVirtualSources = layoutVirtualSourceTemplateDao.findByAgendaLayoutTemplateId(agendaLayout.getId());
						for(LayoutVirtualSourceTemplatePO layoutVirtualSourceTemplate : layoutVirtualSources){
							LayoutPositionPO layoutPosition = layoutPositionDao.findOne(layoutVirtualSourceTemplate.getLayoutPositionId());
							CombineVideoPO virtualSource = combineVideoDao.findOne(layoutVirtualSourceTemplate.getVirtualSourceId());
							//是否需要合屏
							boolean need = combineVideoUtil.needCombineVideo(virtualSource);
							if(need){
								//如果需要合屏，则查找是否创建了合屏
								com.sumavision.bvc.device.group.po.CombineVideoPO combineVideo = deviceGroupCombineVideoDao.findByUuid(virtualSource.getUuid());
								if(combineVideo == null){
									//需要新建合屏
									needCombineVirtualSources.add(virtualSource);
								}else{
									//更新合屏
									needUpdateVirtualSources.add(virtualSource);
									usingCombineVideos.add(combineVideo);
								}
								//建立转发关系，把虚拟源给对应的通道
								//TODO:支持视音频
								SourceBO combineSource = new SourceBO()
										.setAgendaForwardType(AgendaForwardType.VIDEO)
										.setVideoSourceType(AgendaSourceType.COMBINE_VIDEO)
										.setBusinessInfoType(BusinessInfoType.COMMON)
										.setBusinessId(groupId.toString())
//										.setBusinessId(UUID.randomUUID().toString().replaceAll("-", ""))
										.setSrcVideoId(virtualSource.getUuid());
								List<CommonForwardPO> forwards = obtainCommonForwardsFromSource(
										new ArrayListWrapper<GroupMemberPO>().add(member).getList(),
										new ArrayListWrapper<SourceBO>().add(combineSource).getList(),
										outerGroup);
								for(CommonForwardPO forward : forwards){
									forward.setPositionId(layoutPosition.getId());
								}
								result.addAll(forwards);
							}else{
								//查找单个源
								SourceBO sourceBO = combineVideoUtil.getSingleSource(groupId, virtualSource);
								List<CommonForwardPO> forwards = obtainCommonForwardsFromSource(new ArrayListWrapper<GroupMemberPO>().add(member).getList(), new ArrayListWrapper<SourceBO>().add(sourceBO).getList(),outerGroup);
								for(CommonForwardPO forward : forwards){
									forward.setPositionId(layoutPosition.getId());
								}
								result.addAll(forwards);
							}
						}
					}
				}
			}else{
				//用户直接看画面
			}
		}
				
		//----------遍历memberSourceMap，确认是否需要给成员及主席合屏
		/*List<SourceBO> sourceBOs4Member = null;//用于给成员看的N个源
		List<SourceBO> sourceBOs4Chairman = null;//用于给主席看的N个源
		if(BusinessInfoType.MEETING_DISCUSS.equals(agenda.getBusinessInfoType())					
				|| BusinessInfoType.BASIC_MEETING.equals(agenda.getBusinessInfoType())){
			for(GroupMemberPO member : memberSourceMap.keySet()){
				
				if(member.getIsAdministrator()
						&& sourceBOs4Chairman == null
						&& !GroupMemberType.MEMBER_USER.equals(member.getGroupMemberType())){
					List<SourceBO> memberSourceBOs = memberSourceMap.get(member);
					List<SourceBO> temp = new ArrayList<SourceBO>();
					for(SourceBO source : memberSourceBOs){
						if(!member.getId().equals(source.getSrcVideoMemberId())){
							temp.add(source);
						}
					}
					if(temp.size() > 1){
						//如果源数量大于1，则需要给主席合屏
						sourceBOs4Chairman = temp;
					}
				}
				
				else if(!member.getIsAdministrator()
						&& sourceBOs4Member == null
						&& !GroupMemberType.MEMBER_USER.equals(member.getGroupMemberType())){
					List<SourceBO> memberSourceBOs = memberSourceMap.get(member);
					if(memberSourceBOs.size() > 1){
						//如果源数量大于1，且存在非用户成员，则需要合屏。认为这些成员的源列表都是一样的
						sourceBOs4Member = new ArrayList<SourceBO>(memberSourceBOs);
					}
				}
			}
		}*/
		
		//----------生成自动合屏
		SourceBO combineSourceBO4Member = null;//给成员的合屏
		SourceBO combineSourceBO4Chairman = null;//给主席的合屏
		//会议中判断是否需要合屏
		/*if(BusinessInfoType.MEETING_DISCUSS.equals(agenda.getBusinessInfoType())					
				|| BusinessInfoType.BASIC_MEETING.equals(agenda.getBusinessInfoType())){
			List<CombineVideoPO> combineVideoPOs = combineVideoDao.findByBusinessIdAndBusinessType(groupId, CombineBusinessType.GROUP);
			List<CombineAudioPO> combineAudioPOs = combineAudioDao.findByBusinessIdAndBusinessType(groupId, CombineBusinessType.GROUP);
						
			//是否需要给主席合屏
			CombineVideoPO combineVideo4ChairmanPO = tetrisBvcQueryUtil.queryCombineVideoPOByCombineContentType(combineVideoPOs, CombineContentType.FOR_CHAIRMAN);
			CombineAudioPO combineAudio4ChairmanPO = tetrisBvcQueryUtil.queryCombineAudioPOByCombineContentType(combineAudioPOs, CombineContentType.FOR_CHAIRMAN);
			if(sourceBOs4Chairman != null){
				//需要合屏，判断是否已经存在，存在则更新
				combineSourceBO4Chairman = autoCombineService.autoCombine(sourceBOs4Chairman, combineVideo4ChairmanPO, combineAudio4ChairmanPO, CombineContentType.FOR_CHAIRMAN);
			}else{
				//不需要合屏，则将已有的删除
				List<CombineVideoPO> videos = new ArrayList<CombineVideoPO>();
				if(combineVideo4ChairmanPO != null) videos.add(combineVideo4ChairmanPO);
				List<CombineAudioPO> audios = new ArrayList<CombineAudioPO>();
				if(combineAudio4ChairmanPO != null) audios.add(combineAudio4ChairmanPO);
				autoCombineService.deleteCombine(videos, audios, true);
			}
			
			//是否需要给成员合屏
			CombineVideoPO combineVideo4MemberPO = tetrisBvcQueryUtil.queryCombineVideoPOByCombineContentType(combineVideoPOs, CombineContentType.FOR_MEMBER);
			CombineAudioPO combineAudio4MemberPO = tetrisBvcQueryUtil.queryCombineAudioPOByCombineContentType(combineAudioPOs, CombineContentType.FOR_MEMBER);
			if(sourceBOs4Member != null){
				//需要合屏，判断是否已经存在，存在则更新
				combineSourceBO4Member = autoCombineService.autoCombine(sourceBOs4Member, combineVideo4MemberPO, combineAudio4MemberPO, CombineContentType.FOR_MEMBER);
			}else{
				//不需要合屏，则将已有的删除
				List<CombineVideoPO> videos = new ArrayList<CombineVideoPO>();
				if(combineVideo4MemberPO != null) videos.add(combineVideo4MemberPO);
				List<CombineAudioPO> audios = new ArrayList<CombineAudioPO>();
				if(combineAudio4MemberPO != null) audios.add(combineAudio4MemberPO);
				autoCombineService.deleteCombine(videos, audios, true);
			}			
		}*/

		//给虚拟源建立合屏
		CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();
		LogicBO combineVideoLogic = new LogicBO().setUserId(user.getId().toString());
		List<com.sumavision.bvc.device.group.po.CombineVideoPO> newAndUpdatecombineVideos = new ArrayList<com.sumavision.bvc.device.group.po.CombineVideoPO>();
		if(needCombineVirtualSources.size() > 0){
			log.info("给虚拟源创建" + needCombineVirtualSources.size() + "个合屏");
			List<com.sumavision.bvc.device.group.po.CombineVideoPO> vs = new ArrayList<com.sumavision.bvc.device.group.po.CombineVideoPO>();
			for(CombineVideoPO virtualSource : needCombineVirtualSources){
				com.sumavision.bvc.device.group.po.CombineVideoPO combineVideo = new com.sumavision.bvc.device.group.po.CombineVideoPO().set(virtualSource);
				combineVideo.setReconGroupId(groupId);
				combineVideoUtil.transferSrcs(groupId, combineVideo);
				newAndUpdatecombineVideos.add(combineVideo);
				vs.add(combineVideo);				
			}
			//处理合屏协议
			combineVideoLogic.setCombineVideoSet(vs, codec);
		}
		if(needUpdateVirtualSources.size() > 0){
			log.info("给虚拟源更新" + needUpdateVirtualSources.size() + "个合屏");
			List<com.sumavision.bvc.device.group.po.CombineVideoPO> vs = new ArrayList<com.sumavision.bvc.device.group.po.CombineVideoPO>();
			for(CombineVideoPO virtualSource : needUpdateVirtualSources){
				
				com.sumavision.bvc.device.group.po.CombineVideoPO oldCombineVideo = queryUtil.queryCombineVideo(allCombineVideos, virtualSource.getUuid());
				deleteCombineVideos.add(oldCombineVideo);
				com.sumavision.bvc.device.group.po.CombineVideoPO combineVideo = new com.sumavision.bvc.device.group.po.CombineVideoPO().set(virtualSource);
				combineVideo.setReconGroupId(groupId);
				combineVideoUtil.transferSrcs(groupId, combineVideo);
				newAndUpdatecombineVideos.add(combineVideo);
				vs.add(combineVideo);				
			}
			//处理合屏协议				
			combineVideoLogic.setCombineVideoUpdate(vs, codec);
		}
		//----------统一删除无用的合屏----------
		stopCombineVideos.removeAll(usingCombineVideos);
		if(stopCombineVideos.size() > 0){
			log.info("给虚拟源删除" + needUpdateVirtualSources.size() + "个合屏");
			for(com.sumavision.bvc.device.group.po.CombineVideoPO deleteCombineVideo : stopCombineVideos){
				combineVideoLogic.setCombineVideoDel(new ArrayListWrapper<com.sumavision.bvc.device.group.po.CombineVideoPO>().add(deleteCombineVideo).getList());
				deviceGroupCombineVideoDao.delete(deleteCombineVideo);
			}
		}
		for(com.sumavision.bvc.device.group.po.CombineVideoPO deleteCombineVideo : deleteCombineVideos){
			deviceGroupCombineVideoDao.delete(deleteCombineVideo);
		}
		deviceGroupCombineVideoDao.save(newAndUpdatecombineVideos);
		
		if(businessReturnService.getSegmentedExecute()){
			businessReturnService.add(combineVideoLogic, null, null);
		}else{
			executeBusiness.execute(combineVideoLogic, "给虚拟源创建/更新/停止合屏");
		}
		
		
		//----------再次遍历memberSourceMap，非用户的成员观看合屏，用户看单画面，生成CommonForwardPO
		for(GroupMemberPO member : memberSourceMap.keySet()){
			
			//如果主席看合屏
			if(member.getIsAdministrator() && combineSourceBO4Chairman != null){
				List<CommonForwardPO> forwards = obtainCommonForwardsFromSource(
						new ArrayListWrapper<GroupMemberPO>().add(member).getList(), 
						new ArrayListWrapper<SourceBO>().add(combineSourceBO4Chairman).getList(),
						outerGroup);
				result.addAll(forwards);
			}
			//如果成员看合屏
			else if(!member.getIsAdministrator()
					&& combineSourceBO4Member != null
					&& !GroupMemberType.MEMBER_USER.equals(member.getGroupMemberType())){
				List<CommonForwardPO> forwards = obtainCommonForwardsFromSource(
						new ArrayListWrapper<GroupMemberPO>().add(member).getList(), 
						new ArrayListWrapper<SourceBO>().add(combineSourceBO4Member).getList(),
						outerGroup);
				result.addAll(forwards);
			}
			//用户直接看画面
			else if(GroupMemberType.MEMBER_USER.equals(member.getGroupMemberType())){
				List<SourceBO> memberSourceBOs = memberSourceMap.get(member);			
				List<CommonForwardPO> forwards = obtainCommonForwardsFromSource(new ArrayListWrapper<GroupMemberPO>().add(member).getList(), memberSourceBOs, outerGroup);
				result.addAll(forwards);
			}
		}
		
		//对于暂停的会议，将转发状态置为UNDONE。后续优化拓展到静默、专向业务
		if(GroupStatus.PAUSE.equals(groupStatus)){
			for(CommonForwardPO forward : result){
				forward.setVideoStatus(ExecuteStatus.UNDONE);
				forward.setAudioStatus(ExecuteStatus.UNDONE);
			}
		}
		
		return result;
	}
	
	/**
	 * 修改成员的角色<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月5日 下午4:15:31
	 * @param groupId
	 * @param memberId
	 * @param addRoleIds 新增角色的id
	 * @param removeRoleIds 解绑角色的id
	 * @param executeToFinal 是否执行，通常用false，在对所有成员的绑定都完成之后，统一执行一次，否则executeToFinal会被执行多次，效率低
	 * @throws Exception
	 */
	public void modifyMemberRole(Long groupId, Long memberId, List<Long> addRoleIds, List<Long> removeRoleIds, boolean executeToFinal) throws Exception{
		
		if(addRoleIds == null) addRoleIds = new ArrayList<Long>();
		if(removeRoleIds == null) removeRoleIds = new ArrayList<Long>();
		
		GroupMemberPO member = groupMemberDao.findOne(memberId);
		
		//先获取原来的转发关系
//		List<CommonForwardPO> oldForwards = commonForwardDao.findByBusinessId(groupId.toString());
		
		//得到最终成员角色状态。TODO:先校验是否已经有角色，也就是判重
		List<RolePO> addRoles = roleDao.findAll(addRoleIds);
		List<GroupMemberRolePermissionPO> ps = new ArrayList<GroupMemberRolePermissionPO>();
		for(RolePO addRole : addRoles){
			GroupMemberRolePermissionPO memberRolePermission = new GroupMemberRolePermissionPO(addRole.getId(), member.getId());
			ps.add(memberRolePermission);
		}
		groupMemberRolePermissionDao.save(ps);
		List<GroupMemberRolePermissionPO> dps = groupMemberRolePermissionDao.findByRoleIdInAndGroupMemberId(removeRoleIds, memberId);
		groupMemberRolePermissionDao.deleteInBatch(dps);
		
		if(executeToFinal){
			executeToFinal(groupId);
		}
		
	}
	
	/**
	 * （成员与角色1：1）修改保存成员的角色<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月1日 上午11:15:16
	 * @param groupId
	 * @param memberId
	 * @param modifyRoleId 待修改角色的id
	 * @param isDelete 是否删除成员角色对应关系  true是，false否
	 * @param executeToFinal 是否执行，通常用false，在对所有成员的绑定都完成之后，统一执行一次，否则executeToFinal会被执行多次，效率低
	 * @throws Exception
	 */
	public void modifySoleMemberRole(Long groupId, Long memberId, Long modifyRoleId,boolean isDelete,boolean executeToFinal) throws Exception{
		if(!isDelete){
			if(modifyRoleId == null) {
				return ;
			}
			
			GroupMemberRolePermissionPO groupMemberRolePermission= groupMemberRolePermissionDao.findByGroupMemberId(memberId);
			
			//不为空直接赋值，否则创建赋值。
			if(groupMemberRolePermission!=null){
				groupMemberRolePermission.setRoleId(modifyRoleId);
				groupMemberRolePermissionDao.save(groupMemberRolePermission);
			}else{
				groupMemberRolePermission=new GroupMemberRolePermissionPO(modifyRoleId, memberId);
				groupMemberRolePermissionDao.save(groupMemberRolePermission);
			}
			
			if(executeToFinal){
				executeToFinal(groupId);
			}
		}else{
			GroupMemberRolePermissionPO groupMemberRolePermission= groupMemberRolePermissionDao.findByGroupMemberId(memberId);
			
			if(groupMemberRolePermission!=null){
				groupMemberRolePermissionDao.delete(groupMemberRolePermission);
			}
			
			
			if(executeToFinal){
				executeToFinal(groupId);
			}
		}
		
		
	}
	
	//TODO:
	public void modifyMemberRoleBatch(Long groupId, List<ModifyMemberRoleBO> memberRoleBOs) throws Exception{
		
		/*
		//增加角色，查找需要的转发，判重
		List<RolePO> addRoles = roleDao.findByRoleIdIn(addRoleIds);
		//TODO:需要知道这个议程是否在执行
		//TODO:还有dstId
		List<AgendaForwardPO> addForwards = agendaForwardDao.findBySourceTypeAndSourceIdIn(AgendaSourceType.ROLE, addRoleIds);
		List<Long> agendaIds = new ArrayList<Long>();
		for(AgendaForwardPO addForward : addForwards){
			agendaIds.add(addForward.getAgendaId());
		}
		List<AgendaPO> addAgendas = agendaDao.findAll(agendaIds);
		for(AgendaForwardPO addForward : addForwards){
			AgendaPO agenda = tetrisBvcQueryUtil.queryAgendaById(addAgendas, addForward.getAgendaId());
//			if(agenda没有执行) continue; TODO:
			//执行议程
		}
		
		
		//减少角色，查找相关的转发，判断是否需要删掉
		List<RolePO> removeRoles = roleDao.findByRoleIdIn(removeRoleIds);
		//TODO:还有dstId
		List<AgendaForwardPO> removeForwards = agendaForwardDao.findBySourceTypeAndSourceIdIn(AgendaSourceType.ROLE, addRoleIds);
		
		List<GroupMemberPO> members = new ArrayListWrapper<GroupMemberPO>().add(member).getList();
		Map<ChannelSchemeDTO, ChannelSchemeDTO> videoAudioMap = obtainSourceChannels(members);
		List<CommonForwardPO> fs = obtainCommonForwards(members, videoAudioMap);
		//对比fs是否需要删除。全量对比更简单？
		*/
		
		
		
		//先获取原来的转发关系
		List<CommonForwardPO> oldForwards = commonForwardDao.findByBusinessId(groupId.toString());
		
		for(ModifyMemberRoleBO memberRoleBO : memberRoleBOs){
			
			Long memberId = memberRoleBO.getMemberId();
			List<Long> addRoleIds = memberRoleBO.getAddRoleIds();
			List<Long> removeRoleIds = memberRoleBO.getRemoveRoleIds();			
			
			if(addRoleIds == null) addRoleIds = new ArrayList<Long>();
			if(removeRoleIds == null) removeRoleIds = new ArrayList<Long>();
			
			GroupMemberPO member = groupMemberDao.findOne(memberId);
		
			//得到最终成员角色状态。考虑判重，也就是重复操作
			List<RolePO> addRoles = roleDao.findAll(addRoleIds);
			List<GroupMemberRolePermissionPO> ps = new ArrayList<GroupMemberRolePermissionPO>();
			for(RolePO addRole : addRoles){
				GroupMemberRolePermissionPO memberRolePermission = new GroupMemberRolePermissionPO(addRole.getId(), member.getId());
				ps.add(memberRolePermission);
			}
			groupMemberRolePermissionDao.save(ps);
			groupMemberRolePermissionDao.deleteByRoleIdInAndGroupMemberId(removeRoleIds, memberId);
					
			//获取到所有需要执行的议程，通过每个议程获取到最终的转发关系，求并集
			List<CommonForwardPO> newForwards = new ArrayList<CommonForwardPO>();
			List<AgendaPO> agendas = agendaDao.findRunningAgendasByGroupId(groupId);
			for(AgendaPO agenda : agendas){
				List<CommonForwardPO> forwards = obtainCommonForwards(agenda, groupId);
				//取并集
				newForwards.removeAll(forwards);
				newForwards.addAll(forwards);
			}
			
			//与原来的转发关系做对比，得到新增addForwards与删除的removeForwards
			List<CommonForwardPO> addForwards = new ArrayList<CommonForwardPO>(newForwards);
			addForwards.removeAll(oldForwards);
			List<CommonForwardPO> removeForwards = new ArrayList<CommonForwardPO>(oldForwards);
			removeForwards.removeAll(newForwards);
			
			//新增的添加到分页任务
			List<PageTaskPO> newTasks = forwardToPageTaskPO(addForwards);
			
			//删除的从分页任务中删除
			List<String> forwardUuids = new ArrayList<String>();
			for(CommonForwardPO removeForward : removeForwards){
				forwardUuids.add(removeForward.getUuid());
			}
			List<PageTaskPO> removeTasks =  pageTaskDao.findByForwardUuidIn(forwardUuids);
			
			String originId = member.getOriginId();
			Long terminalId = member.getTerminalId();
			GroupMemberType groupMemberType = member.getGroupMemberType();
			PageInfoPO pageInfo = pageInfoDao.findByOriginIdAndTerminalIdAndGroupMemberType(originId, terminalId, groupMemberType);
			
			pageTaskService.addAndRemoveTasks(pageInfo, newTasks, removeTasks);
		}
		
	}
	
	/**
	 * 将group的转发关系执行到最终状态<br/>
	 * <p>分析出当前需要执行的转发，并与已有的转发做对比，比较出新增和需要删除的转发</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月18日 下午3:30:02
	 * @param groupId
	 * @throws Exception
	 */
	public void executeToFinal(Long groupId) throws Exception{
		
		List<CommonForwardPO> oldForwards = commonForwardDao.findByBusinessId(groupId.toString());
		List<GroupMemberPO> members = groupMemberDao.findByGroupId(groupId);
		
		//获取到所有需要执行的议程，通过每个议程获取到最终的转发关系，求并集
		List<CommonForwardPO> newForwards = new ArrayList<CommonForwardPO>();
		List<AgendaPO> agendas = agendaDao.findRunningAgendasByGroupId(groupId);
		for(AgendaPO agenda : agendas){
			List<CommonForwardPO> forwards = obtainCommonForwards(agenda, groupId); //TODO 合并命令 （已完成）
			//取并集
			newForwards.removeAll(forwards);
			newForwards.addAll(forwards);
		}
		
		//与原来的转发关系做对比，得到新增addForwards与删除的removeForwards
		Set<CommonForwardPO> addForwards = new HashSet<CommonForwardPO>(newForwards);
		addForwards.removeAll(oldForwards);
		List<CommonForwardPO> removeForwards = new ArrayList<CommonForwardPO>(oldForwards);
		removeForwards.removeAll(newForwards);
		
//		（废弃）因为会场的任务会直接清除，所以把会场的转发加回来
//		for(CommonForwardPO newForward : newForwards){
//			Long memberId = newForward.getDstMemberId();
//			GroupMemberPO member = tetrisBvcQueryUtil.queryMemberById(members, memberId);
//			if(member != null && member.getGroupMemberType().equals(GroupMemberType.MEMBER_HALL)){
//				addForwards.add(newForward);
//			}
//		}
		
		//删除的从分页任务中删除
		List<String> forwardUuids = new ArrayList<String>();
		for(CommonForwardPO removeForward : removeForwards){
			forwardUuids.add(removeForward.getUuid());
		}
		//所有需要删除的分页任务
		List<PageTaskPO> removeTasks =  pageTaskDao.findByForwardUuidIn(forwardUuids);
		
		
		//按照成员来划分分页
		Map<Long, MemberChangedTaskBO> memberTaskMap = new HashMap<Long, MemberChangedTaskBO>();
		
		for(CommonForwardPO addForward : addForwards){
			Long dstMemberId = addForward.getDstMemberId();
			MemberChangedTaskBO memberChangedTask = memberTaskMap.get(dstMemberId);
			if(memberChangedTask == null){
				memberChangedTask = new MemberChangedTaskBO();
				memberTaskMap.put(dstMemberId, memberChangedTask);
			}
			memberChangedTask.getAddForwards().add(addForward);
		}
		
		for(PageTaskPO removeTask : removeTasks){
			Long dstMemberId = removeTask.getDstMemberId();
			MemberChangedTaskBO memberChangedTask = memberTaskMap.get(dstMemberId);
			if(memberChangedTask == null){
				memberChangedTask = new MemberChangedTaskBO();
				memberTaskMap.put(dstMemberId, memberChangedTask);
			}
			memberChangedTask.getRemoveTasks().add(removeTask);
		}
		
		for(Long id : memberTaskMap.keySet()){
			MemberChangedTaskBO memberChangedTask = memberTaskMap.get(id);
			List<CommonForwardPO> thisAddForwards = memberChangedTask.getAddForwards();
			List<PageTaskPO> newTasks = forwardToPageTaskPO(thisAddForwards);
			List<PageTaskPO> thisRemoveTasks = memberChangedTask.getRemoveTasks();
			
			GroupMemberPO thisMember = tetrisBvcQueryUtil.queryMemberById(members, id);
			String originId = thisMember.getOriginId();
			Long terminalId = thisMember.getTerminalId();		
			GroupMemberType groupMemberType = thisMember.getGroupMemberType();
			PageInfoPO pageInfo = pageInfoDao.findByOriginIdAndTerminalIdAndGroupMemberType(originId, terminalId, groupMemberType);
			
			//对于会场，不分页，直接删除前边的task
			if(GroupMemberType.MEMBER_HALL.equals(groupMemberType)){
				pageInfo.getPageTasks().removeAll(thisRemoveTasks);
				pageInfo.setCurrentPage(1);
				thisRemoveTasks.clear();
				pageInfoDao.save(pageInfo);
			}
			
			pageTaskService.addAndRemoveTasks(pageInfo, newTasks, thisRemoveTasks);
		}
		
		//持久化 oldForwards不需要吧？
		commonForwardDao.save(addForwards);
		commonForwardDao.deleteInBatch(removeForwards);
	}
	
	private SourceBO ldapUserMemberToSource(
			GroupMemberPO ldapMember,
			String businessId,
			BusinessInfoType businessInfoType,
			AgendaForwardType agendaForwardType
			){
		String videoChannelId = ChannelType.VIDEOENCODE1.getChannelId();
		String audioChannelId = ChannelType.AUDIOENCODE1.getChannelId();
		BundlePO srcBundlePO = new BundlePO();
		srcBundlePO.setSourceType(SOURCE_TYPE.EXTERNAL);
		srcBundlePO.setBundleId(ldapMember.getUuid());
		srcBundlePO.setUsername(ldapMember.getCode());
//		srcBundlePO.setAccessNodeUid(accessNodeUid);//实际上这里设了也没用
		//造数据，videoEncodeChannel,audioEncodeChannel: bundleId channelId baseType channelName
		ChannelSchemeDTO videoEncodeChannel = new ChannelSchemeDTO()
				.setBundleId(ldapMember.getUuid())
				.setChannelId(videoChannelId)
				.setBaseType("outer_no_base_type")
				.setChannelName("outer_no_video_channel_name");
		ChannelSchemeDTO audioEncodeChannel = new ChannelSchemeDTO()
				.setBundleId(ldapMember.getUuid())
				.setChannelId(audioChannelId)
				.setBaseType("outer_no_base_type")
				.setChannelName("outer_no_video_channel_name");
		SourceBO sourceBO = new SourceBO()
				.setOriginType(OriginType.OUTER)
				.setGroupMemberType(ldapMember.getGroupMemberType())
				.setMemberUuid(ldapMember.getUuid())
				.setBusinessId(businessId)
				.setBusinessInfoType(businessInfoType)
				.setAgendaForwardType(agendaForwardType)//音/视频，如果没有音频，这里是否应该写成“视频”？
				.setSrcVideoId(ldapMember.getOriginId())
				.setSrcVideoMemberId(ldapMember.getId())
				.setSrcVideoName(ldapMember.getName())
				.setSrcVideoCode(ldapMember.getCode())
				.setVideoSourceChannel(videoEncodeChannel)
				.setVideoBundle(srcBundlePO);
		sourceBO.setAudioSourceChannel(audioEncodeChannel)
				.setAudioBundle(srcBundlePO)
				.setSrcAudioId(ldapMember.getOriginId())
				.setSrcAudioMemberId(ldapMember.getId())
				.setSrcAudioName(ldapMember.getName())
				.setSrcAudioCode(ldapMember.getCode());
		return sourceBO;
	}

	/**
	 * 将转发转换到为分页任务<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月18日 下午3:31:39
	 * @param forwards
	 * @return
	 */
	private List<PageTaskPO> forwardToPageTaskPO(List<CommonForwardPO> forwards){
		List<PageTaskPO> pageTaskPOs = new ArrayList<PageTaskPO>();
		for(CommonForwardPO forward : forwards){
			
			PageTaskPO task = new PageTaskPO();
			task.setBusinessInfoType(forward.getBusinessInfoType());
			task.setBusinessId(forward.getBusinessId());
			task.setBusinessName(forward.getBusinessInfoType().getInfo() + "：" + forward.getSrcName());//业务描述
			task.setVideoStatus(ExecuteStatus.UNDONE);
			task.setAudioStatus(ExecuteStatus.UNDONE);
			task.setForwardUuid(forward.getUuid());
			task.setDstMemberId(forward.getDstMemberId());
			task.setPositionId(forward.getPositionId());
			
			AgendaForwardType type = forward.getType();
			if(AgendaForwardType.AUDIO_VIDEO.equals(type) || AgendaForwardType.VIDEO.equals(type)){
				task.setVideoSourceType(forward.getVideoSourceType());
//				task.setCombineVideoUuid(forward.getCombineVideoUuid());
				task.setSrcVideoId(forward.getSrcId());
				task.setSrcVideoBundleId(forward.getSrcBundleId());
				task.setSrcVideoLayerId(forward.getSrcLayerId());
				task.setSrcVideoChannelId(forward.getSrcChannelId());
				task.setSrcVideoName(forward.getSrcName());
				task.setSrcVideoCode(forward.getSrcCode());
				task.setVideoStatus(forward.getVideoStatus());
				task.setVideoTransmissionMode(forward.getVideoTransmissionMode());
				task.setVideoMultiAddr(forward.getVideoMultiAddr());
				task.setVideoMultiSrcAddr(forward.getVideoMultiSrcAddr());
			}			
			if(AgendaForwardType.AUDIO_VIDEO.equals(type) || AgendaForwardType.AUDIO.equals(type)){
				task.setAudioSourceType(forward.getAudioSourceType());
//				task.setCombineAudioUuid(forward.getCombineAudioUuid());
				task.setSrcAudioId(forward.getSrcAudioId());
				task.setSrcAudioBundleId(forward.getSrcAudioBundleId());
				task.setSrcAudioLayerId(forward.getSrcAudioLayerId());
				task.setSrcAudioChannelId(forward.getSrcAudioChannelId());
				task.setSrcAudioName(forward.getSrcAudioName());			
				task.setSrcAudioCode(forward.getSrcAudioCode());
				task.setAudioStatus(forward.getAudioStatus());
				task.setAudioTransmissionMode(forward.getAudioTransmissionMode());
				task.setAudioMultiAddr(forward.getAudioMultiAddr());
				task.setAudioMultiSrcAddr(forward.getAudioMultiSrcAddr());
			}
			pageTaskPOs.add(task);
		}
		return pageTaskPOs;
	}


	private LogicBO openAndCloseDecoder(
			List<CommandGroupUserPlayerPO> openPlayers,
			List<CommandGroupUserPlayerPO> closePlayers,
			CodecParamBO codec){
		
		if(openPlayers == null) openPlayers = new ArrayList<CommandGroupUserPlayerPO>();
		if(closePlayers == null) closePlayers = new ArrayList<CommandGroupUserPlayerPO>();
		
		LogicBO logic = new LogicBO().setUserId("-1")
		 		 .setConnectBundle(new ArrayList<ConnectBundleBO>())
		 		 .setDisconnectBundle(new ArrayList<DisconnectBundleBO>())
		 		 .setPass_by(new ArrayList<PassByBO>());
		
		for(CommandGroupUserPlayerPO openPlayer : openPlayers){
			ConnectBundleBO connectDecoderBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
//			  													        .setOperateType(ConnectBundleBO.OPERATE_TYPE)
			  													        .setLock_type("write")
			  													        .setBundleId(openPlayer.getBundleId())
			  													        .setLayerId(openPlayer.getLayerId())
			  													        .setBundle_type(openPlayer.getBundleType());
			ForwardSetSrcBO decoderVideoForwardSet = null;
			if(openPlayer.getSrcVideoBundleId() != null){
				decoderVideoForwardSet = new ForwardSetSrcBO().setType("channel")
						.setBundleId(openPlayer.getSrcVideoBundleId())
						.setLayerId(openPlayer.getSrcVideoLayerId())
						.setChannelId(openPlayer.getSrcVideoChannelId());
			}
			ConnectBO connectDecoderVideoChannel = new ConnectBO().setChannelId(openPlayer.getVideoChannelId())
														          .setChannel_status("Open")
														          .setBase_type(openPlayer.getVideoBaseType())
														          .setCodec_param(codec)
														          .setSource_param(decoderVideoForwardSet);
			ForwardSetSrcBO decoderAudioForwardSet = null;
			if(openPlayer.getSrcAudioBundleId() != null){
				decoderAudioForwardSet = new ForwardSetSrcBO().setType("channel")
						.setBundleId(openPlayer.getSrcAudioBundleId())
						.setLayerId(openPlayer.getSrcAudioLayerId())
						.setChannelId(openPlayer.getSrcAudioChannelId());
			}
			ConnectBO connectDecoderAudioChannel = new ConnectBO().setChannelId(openPlayer.getAudioChannelId())
																  .setChannel_status("Open")
																  .setBase_type(openPlayer.getAudioBaseType())
																  .setCodec_param(codec)
																  .setSource_param(decoderAudioForwardSet);
			
			connectDecoderBundle.setChannels(new ArrayListWrapper<ConnectBO>().add(connectDecoderVideoChannel).add(connectDecoderAudioChannel).getList());
			logic.getConnectBundle().add(connectDecoderBundle);
		}
		
		for(CommandGroupUserPlayerPO closePlayer : closePlayers){
			DisconnectBundleBO disconnectDecoderBundle = new DisconnectBundleBO().setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
//																           	 .setOperateType(DisconnectBundleBO.OPERATE_TYPE)
																           	 .setBundleId(closePlayer.getBundleId())
																           	 .setBundle_type(closePlayer.getBundleType())
																           	 .setLayerId(closePlayer.getLayerId());
			logic.getDisconnectBundle().add(disconnectDecoderBundle);
		}
		
		return logic;
	}
	
}

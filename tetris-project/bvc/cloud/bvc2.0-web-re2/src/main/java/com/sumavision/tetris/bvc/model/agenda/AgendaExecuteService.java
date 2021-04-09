package com.sumavision.tetris.bvc.model.agenda;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.FolderUserMapDAO;
import com.suma.venus.resource.service.ResourceRemoteService;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.command.group.dao.AgendaForwardDemandDAO;
import com.sumavision.bvc.command.group.forward.AgendaForwardDemandPO;
import com.sumavision.bvc.device.command.cast.CommandCastServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.command.user.CommandUserServiceImpl;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.resource.dao.ResourceBundleDAO;
import com.sumavision.bvc.resource.dao.ResourceChannelDAO;
import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.bvc.business.ExecuteStatus;
import com.sumavision.tetris.bvc.business.OriginType;
import com.sumavision.tetris.bvc.business.bo.BusinessDeliverBO;
//import com.sumavision.tetris.bvc.business.bo.SourceBO;
import com.sumavision.tetris.bvc.business.common.BusinessCommonService;
import com.sumavision.tetris.bvc.business.common.BusinessReturnService;
import com.sumavision.tetris.bvc.business.common.MulticastService;
import com.sumavision.tetris.bvc.business.dao.BusinessCombineAudioDAO;
import com.sumavision.tetris.bvc.business.dao.BusinessCombineAudioSrcDAO;
import com.sumavision.tetris.bvc.business.dao.BusinessCombineVideoDAO;
import com.sumavision.tetris.bvc.business.dao.BusinessGroupMemberDAO;
import com.sumavision.tetris.bvc.business.dao.CombineAudioPermissionDAO;
import com.sumavision.tetris.bvc.business.dao.CombineTemplateGroupAgendeForwardPermissionDAO;
import com.sumavision.tetris.bvc.business.dao.CommonForwardDAO;
import com.sumavision.tetris.bvc.business.dao.GroupDAO;
import com.sumavision.tetris.bvc.business.dao.GroupMemberDAO;
import com.sumavision.tetris.bvc.business.dao.GroupMemberRolePermissionDAO;
import com.sumavision.tetris.bvc.business.dao.RunningAgendaDAO;
import com.sumavision.tetris.bvc.business.deliver.DeliverExecuteService;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.bvc.business.group.GroupStatus;
import com.sumavision.tetris.bvc.business.group.RunningAgendaPO;
import com.sumavision.tetris.bvc.business.group.TransmissionMode;
import com.sumavision.tetris.bvc.business.group.combine.audio.CombineAudioService;
import com.sumavision.tetris.bvc.business.group.combine.video.CombineVideoService;
import com.sumavision.tetris.bvc.business.group.record.GroupRecordService;
import com.sumavision.tetris.bvc.business.group.stop.GroupStopService;
import com.sumavision.tetris.bvc.business.po.combine.video.CombineTemplateGroupAgendeForwardPermissionPO;
import com.sumavision.tetris.bvc.business.po.combine.audio.BusinessCombineAudioPO;
import com.sumavision.tetris.bvc.business.po.combine.audio.BusinessCombineAudioSrcPO;
import com.sumavision.tetris.bvc.business.po.combine.audio.CombineAudioPermissionPO;
import com.sumavision.tetris.bvc.business.po.combine.video.BusinessCombineVideoPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberTerminalBundleChannelPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberTerminalBundlePO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberTerminalChannelPO;
import com.sumavision.tetris.bvc.business.terminal.hall.TerminalBundleConferenceHallPermissionDAO;
import com.sumavision.tetris.bvc.business.terminal.user.TerminalBundleUserPermissionDAO;
import com.sumavision.tetris.bvc.model.agenda.combine.VirtualSourceService;
import com.sumavision.tetris.bvc.model.layout.LayoutDAO;
import com.sumavision.tetris.bvc.model.layout.LayoutPositionDAO;
import com.sumavision.tetris.bvc.model.layout.LayoutPositionPO;
import com.sumavision.tetris.bvc.model.layout.forward.CombineTemplatePositionDAO;
import com.sumavision.tetris.bvc.model.layout.forward.CombineTemplatePositionPO;
import com.sumavision.tetris.bvc.model.layout.forward.LayoutForwardDAO;
import com.sumavision.tetris.bvc.model.layout.forward.LayoutForwardPO;
import com.sumavision.tetris.bvc.model.layout.forward.LayoutForwardSourceType;
//import com.sumavision.tetris.bvc.model.agenda.combine.AutoCombineService;
//import com.sumavision.tetris.bvc.model.agenda.combine.CombineAudioDAO;
//import com.sumavision.tetris.bvc.model.agenda.combine.CombineVideoDAO;
//import com.sumavision.tetris.bvc.model.agenda.combine.CombineVideoPO;
//import com.sumavision.tetris.bvc.model.agenda.combine.CombineVideoUtil;
import com.sumavision.tetris.bvc.model.role.RoleChannelDAO;
import com.sumavision.tetris.bvc.model.role.RoleChannelPO;
import com.sumavision.tetris.bvc.model.role.RoleChannelTerminalChannelPermissionDAO;
import com.sumavision.tetris.bvc.model.role.RoleDAO;
import com.sumavision.tetris.bvc.model.role.RolePO;
import com.sumavision.tetris.bvc.model.role.RoleUserMappingType;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundleDAO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelDAO;
//import com.sumavision.tetris.bvc.model.terminal.layout.LayoutPositionDAO;
//import com.sumavision.tetris.bvc.model.terminal.layout.LayoutPositionPO;
import com.sumavision.tetris.bvc.page.PageInfoDAO;
import com.sumavision.tetris.bvc.page.PageInfoPO;
import com.sumavision.tetris.bvc.page.PageTaskDAO;
import com.sumavision.tetris.bvc.page.PageTaskPO;
import com.sumavision.tetris.bvc.page.PageTaskService;
import com.sumavision.tetris.bvc.util.AgendaRoleMemberUtil;
import com.sumavision.tetris.bvc.util.MultiRateUtil;
import com.sumavision.tetris.bvc.util.TetrisBvcQueryUtil;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.user.UserQuery;
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
	
	/** synchronized锁的前缀 */
	private static final String lockProcessPrefix = "tetris-group-";
	
	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private CustomAudioDAO customAudioDao;
	
	@Autowired
	private ResourceBundleDAO resourceBundleDao;
	
	@Autowired
	private ResourceChannelDAO resourceChannelDao;
	
	@Autowired
	private GroupDAO groupDao;
	
	@Autowired
	private RoleChannelDAO roleChannelDao;
	
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
	private AgendaForwardDemandDAO agendaForwardDemandDao;
	
	@Autowired
	private GroupMemberRolePermissionDAO groupMemberRolePermissionDao;
	
	@Autowired
	private RoleDAO roleDao;
	
	@Autowired
	private GroupMemberDAO groupMemberDao;
	
	@Autowired
	private BusinessCombineVideoDAO combineVideoDao;
	
	@Autowired
	private TerminalBundleUserPermissionDAO terminalBundleUserPermissionDao;
	
	@Autowired
	private TerminalBundleConferenceHallPermissionDAO terminalBundleConferenceHallPermissionDao;
	
	@Autowired
	private RunningAgendaDAO runningAgendaDao;
	
	@Autowired
	private FolderUserMapDAO folderUserMapDao;
	
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
	private RoleChannelTerminalChannelPermissionDAO roleChannelTerminalChannelPermissionDao;
	
	@Autowired
	private CombineAudioPermissionDAO combineAudioPermissionDao;
	
	@Autowired
	private CombineTemplateGroupAgendeForwardPermissionDAO combineTemplateGroupAgendeForwardPermissionDao;
	
	@Autowired
	private BusinessReturnService businessReturnService;
	
	@Autowired
	private VirtualSourceService virtualSourceService;
	
	@Autowired
	private CombineVideoService combineVideoService;
	
	@Autowired
	private CombineAudioService combineAudioService;
	
	@Autowired
	private DeliverExecuteService deliverExecuteService;
	
	@Autowired
	private AgendaRoleMemberUtil agendaRoleMemberUtil;
	
	@Autowired
	private BusinessCombineAudioSrcDAO businessCombineAudioSrcDao;
	
	@Autowired
	private GroupRecordService groupRecordService;
	
	@Autowired
	private GroupStopService groupStopService;
	
	/** 级联使用：联网接入层id */
	private String localLayerId = null;

	@Transactional(rollbackFor = Exception.class)
	public void runAgenda_ST(Long groupId, Long agendaId, BusinessDeliverBO businessDeliverBO, boolean doProtocol) throws Exception{
		synchronized (new StringBuffer().append(lockProcessPrefix).append(groupId).toString().intern()) {
			runAgenda(groupId, agendaId, businessDeliverBO, doProtocol);
		}
	}

	public void reRunAgenda(Long groupId, BusinessDeliverBO businessDeliverBO, boolean doProtocol) throws Exception{
		//找出正在执行的议程，再次执行（最多只有1个）
		RunningAgendaPO runningAgenda = runningAgendaDao.findByGroupId(groupId);
		if(runningAgenda != null){
			runAgenda(groupId, runningAgenda.getAgendaId(), businessDeliverBO, doProtocol);
		}
	}
	
	public void runAgenda(Long groupId, Long agendaId, BusinessDeliverBO businessDeliverBO, boolean doProtocol) throws Exception{
				
		GroupPO group = groupDao.findOne(groupId);
		
		if(group.getStatus().equals(GroupStatus.STOP)){
			if(!OriginType.OUTER.equals(group.getOriginType())){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已停止，无法操作，id: " + group.getId());
			}else{
				return;
			}
		}
		
		if(businessDeliverBO == null) businessDeliverBO = new BusinessDeliverBO().setGroup(group).setUserId(group.getUserId().toString());
		
		List<BusinessGroupMemberPO> members = group.getMembers();
		
		AgendaPO agenda = agendaDao.findOne(agendaId);
		
		//找出正在执行的议程，停止（最多只有1个）
		RunningAgendaPO runningAgenda = runningAgendaDao.findByGroupId(groupId);
		if(runningAgenda == null){
			runningAgenda = new RunningAgendaPO();
			runningAgenda.setGroupId(groupId);
		}else{			
			//停止议程
//			AgendaPO stopAgenda = agendaDao.findOne(runningAgenda.getAgendaId());
			stopAllDst(group, businessDeliverBO);
		}
		Long oldAgendaId = runningAgenda.getAgendaId()==null?-1L:runningAgenda.getAgendaId();
		runningAgenda.setAgendaId(agendaId);
		runningAgendaDao.save(runningAgenda);
		
		Set<Long> memberRoleIds = group.getMembers().stream().map(BusinessGroupMemberPO::getRoleId).collect(Collectors.toSet());
		List<RoleChannelPO> memberRoleChannels = roleChannelDao.findByRoleIdIn(memberRoleIds);
		List<Long> roleChannelIds = memberRoleChannels.stream().map(RoleChannelPO::getId).collect(Collectors.toList());
		
		//更新合屏混音
		combineVideoService.updateVideo(group, agenda, roleChannelIds, businessDeliverBO);
		combineAudioService.updateCombineAudio(group, agenda, businessDeliverBO, oldAgendaId);
		
		//该议程所有的议程转发
		List<AgendaForwardPO> forwards = agendaForwardDao.findByAgendaId(agendaId);		
		
		//AgendaForwardPO与目的成员的map，在目的角色中，排除掉了在别的转发中被设置的目的成员
		Map<AgendaForwardPO, Set<BusinessGroupMemberPO>> agendaForwardMembersMap = agendaRoleMemberUtil.obtainAgendaForwardDstMemberMap(forwards, members);
		
		//与业务组有关的额外转发对应关系。
		Map<AgendaForwardPO, Set<BusinessGroupMemberPO>> extraAgendaForwardMembersMap = agendaRoleMemberUtil.extraAgendaForawrdMembersMap(groupId, members, forwards);
		agendaForwardMembersMap.putAll(extraAgendaForwardMembersMap);
		
		
		//清理议程全局音频
//		combineAudioService.clearAutoCombineAudio(group.getId());
		
		//处理音频。分为议程/议程转发和自动配置。
//		if(AudioPriority.AGENDA_FIRST.equals(agenda.getAudioPriority())){
//			combineAudioService.agendaAudio(group, agendaId, businessDeliverBO);
//		}else if(AudioPriority.GLOBAL_FIRST.equals(agenda.getAudioPriority())){
//			combineAudioService.generateAutoCombineAudio(group, agendaId, businessDeliverBO);
//		}
		
		//遍历AgendaForwardPO
		for(AgendaForwardPO agendaForward : forwards){
			
			//找到该AgendaForwardPO下所有的LayoutForwardPO
			List<LayoutForwardPO> agendaForwardLayoutForwards = agendaRoleMemberUtil.obtainLayoutForwardsFromAgendaForward(agendaForward);
			
			//map：<terminalId, 虚拟源转发列表>
			Map<Long, List<LayoutForwardPO>> terminalIdLayoutForwardMap = agendaRoleMemberUtil.obtainTerminalIdLayoutForwardMap(agendaForwardLayoutForwards);
			
			//该AgendaForwardPO下的所有目的成员
			Set<BusinessGroupMemberPO> dstMembers = agendaForwardMembersMap.get(agendaForward);
			
			//对每种terminal遍历LayoutForwardPO
			for(Long terminalId : terminalIdLayoutForwardMap.keySet()){
				
				List<LayoutForwardPO> layoutForwards = terminalIdLayoutForwardMap.get(terminalId);
				
				//遍历每种terminal的LayoutForwardPO，除qt外，如果有多个LayoutForwardPO则是对多个通道转发
				for(LayoutForwardPO layoutForward : layoutForwards){
					
					//先判断一下这种terminal有没有成员
					Long layoutForwardTerminalId = layoutForward.getTerminalId();
					List<BusinessGroupMemberPO> layoutForwardDstMembers = tetrisBvcQueryUtil.queryGroupMembersByTerminalId(dstMembers, layoutForwardTerminalId);
					if(layoutForwardDstMembers.size() == 0){
						//这种terminal没有成员，不需要处理
						continue;
					}
					
					executeLayoutForward(layoutForward, layoutForwardDstMembers, group, agenda, agendaForward, roleChannelIds, businessDeliverBO);					
					
				}//遍历每种terminal的layoutForwards结束
			}
		}
		
		if(group.getIsRecord()){
			groupRecordService.updateRecord(group, false, businessDeliverBO);
		}
		
		if(doProtocol){
			deliverExecuteService.execute(businessDeliverBO, "执行议程：" + agenda.getName(), true);
		}
	}
	
	/**
	 * 停止所有源和目的，与议程无关<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月16日 上午10:49:34
	 * @param group
	 * @param businessDeliverBO
	 * @throws BaseException
	 */
	public void stopAllSrcAndDst(GroupPO group, BusinessDeliverBO businessDeliverBO) throws BaseException{
		
		if(businessDeliverBO == null) businessDeliverBO = new BusinessDeliverBO().setGroup(group);
		
		//该group包含的所有task
		String businessId = group.getId().toString();
		String _businessId = businessId+"-";
		List<PageTaskPO> tasks = pageTaskDao.findByBusinessIdStartingWithOrBusinessId(_businessId, businessId);
		businessDeliverBO.getStopPageTasks().addAll(tasks);
		
		//删视频
		List<BusinessCombineVideoPO> combineVideos=combineVideoDao.findByCombineVideoUidLike(group.getId()+"_%");
		Set<Long> vIds = new HashSet<Long>();
		for (BusinessCombineVideoPO combineVideo:combineVideos) {
			businessDeliverBO.getStopCombineVideos().add(combineVideo);
			vIds.add(combineVideo.getId());
		}
		/*List<CombineTemplateGroupAgendeForwardPermissionPO> videoPers = combineTemplateGroupAgendeForwardPermissionDao.findByGroupId(group.getId());
		Set<Long> vIds = new HashSet<Long>();
		for(CombineTemplateGroupAgendeForwardPermissionPO videoPer : videoPers){
			BusinessCombineVideoPO combineVideo = videoPer.getCombineVideo();
			businessDeliverBO.getStopCombineVideos().add(combineVideo);
			vIds.add(combineVideo.getId());
		}*/
		combineVideoDao.deleteByIdIn(vIds);
		
		//删音频
		List<CombineAudioPermissionPO> audioPers = combineAudioPermissionDao.findByGroupId(group.getId());
		Set<Long> aIds = new HashSet<Long>();
		for(CombineAudioPermissionPO audioPer : audioPers){
			BusinessCombineAudioPO allAudio = audioPer.getAllAudio();
			if(allAudio != null && allAudio.isHasSource()){
				businessDeliverBO.getStopCombineAudios().add(allAudio);
				aIds.add(allAudio.getId());
				List<BusinessCombineAudioPO> memberAudios = allAudio.getMemberAudios();
				if(memberAudios != null){
					businessDeliverBO.getStopCombineAudios().addAll(memberAudios);
				}
			}else{
				aIds.add(allAudio.getId());
			}
		}
		businessCombineAudioDao.deleteByIdIn(aIds);		
		
	}
	
	/** 停止所有的目的，也就是PageTask，在“重新执行议程”使用 */
	public void stopAllDst(GroupPO group, BusinessDeliverBO businessDeliverBO) throws BaseException{
		
		if(businessDeliverBO == null) businessDeliverBO = new BusinessDeliverBO().setGroup(group);
		
		//该group包含的所有task
		String businessId = group.getId().toString();
		String _businessId = businessId+"-";
		List<PageTaskPO> tasks = pageTaskDao.findByBusinessIdStartingWithAndBusinessInfoTypeNotOrBusinessIdAndBusinessInfoTypeNot(_businessId, BusinessInfoType.COMMAND_FORWARD_FILE, businessId, BusinessInfoType.COMMAND_FORWARD_FILE);
		businessDeliverBO.getStopPageTasks().addAll(tasks);
		
	}

	/**
	 * 停止议程的源<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月4日 上午8:50:57
	 * @param groupId
	 * @param agendaId
	 * @param businessDeliverBO
	 * @throws BaseException
	 */
	@Deprecated
	public void stopAgenda(Long groupId, Long agendaId, BusinessDeliverBO businessDeliverBO) throws BaseException{
				
		GroupPO group = groupDao.findOne(groupId);
		List<BusinessGroupMemberPO> members = group.getMembers();
		
		if(businessDeliverBO == null) businessDeliverBO = new BusinessDeliverBO().setGroup(group);
		
//		AgendaPO agenda = agendaDao.findOne(agendaId);
//		
//		//找出正在执行的议程，停止（最多只有1个）
//		RunningAgendaPO runningAgenda = runningAgendaDao.findByGroupId(groupId);
//		if(runningAgenda == null){
//			runningAgenda = new RunningAgendaPO();
//			runningAgenda.setGroupId(groupId);
//		}
//		else{			
//			//停止议程
//			AgendaPO stopAgenda = agendaDao.findOne(runningAgenda.getAgendaId());
//			stopAgenda(groupId, stopAgenda.getId(), businessDeliverBO);
//		}
//		runningAgenda.setAgendaId(agendaId);
//		runningAgendaDao.save(runningAgenda);
		
		//该议程所有的议程转发
		List<AgendaForwardPO> forwards = agendaForwardDao.findByAgendaId(agendaId);
		
		//AgendaForwardPO与目的成员的map，在目的角色中，排除掉了在别的转发中被设置的目的成员
		Map<AgendaForwardPO, Set<BusinessGroupMemberPO>> agendaForwardMembersMap = agendaRoleMemberUtil.obtainAgendaForwardDstMemberMap(forwards, members);
		
		//处理音频
//		combineAudioService.agendaAudio(group, agendaId, businessDeliverBO);		
		
		//遍历AgendaForwardPO
		for(AgendaForwardPO agendaForward : forwards){
			
			//找到该AgendaForwardPO下所有的LayoutForwardPO
			List<LayoutForwardPO> agendaForwardLayoutForwards = agendaRoleMemberUtil.obtainLayoutForwardsFromAgendaForward(agendaForward);
			
			//map：<terminalId, 虚拟源转发列表>
			Map<Long, List<LayoutForwardPO>> terminalIdLayoutForwardMap = agendaRoleMemberUtil.obtainTerminalIdLayoutForwardMap(agendaForwardLayoutForwards);
			
			//该AgendaForwardPO下的所有目的成员
			Set<BusinessGroupMemberPO> dstMembers = agendaForwardMembersMap.get(agendaForward);
			
			//对每种terminal遍历LayoutForwardPO
			for(Long terminalId : terminalIdLayoutForwardMap.keySet()){
				
				List<LayoutForwardPO> layoutForwards = terminalIdLayoutForwardMap.get(terminalId);
				
				//遍历每种terminal的LayoutForwardPO，除qt外，如果有多个LayoutForwardPO则是对多个通道转发
				for(LayoutForwardPO layoutForward : layoutForwards){
					
					//先判断一下这种terminal有没有成员
					Long layoutForwardTerminalId = layoutForward.getTerminalId();
					List<BusinessGroupMemberPO> layoutForwardDstMembers = tetrisBvcQueryUtil.queryGroupMembersByTerminalId(dstMembers, layoutForwardTerminalId);
					if(layoutForwardDstMembers.size() == 0){
						//这种terminal没有成员，不需要处理
						continue;
					}
					
					stopLayoutForward(layoutForward, layoutForwardDstMembers, group, agendaForward, businessDeliverBO);
					
				}//遍历每种terminal的layoutForwards结束
			}
		}		
		
		combineAudioService.clearCombineAudio(group, businessDeliverBO);
	}

	/**
	 * 处理虚拟源转发（给某种终端类型）<br/>
	 * <p>生成转发关系PageTask，生成合屏</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月26日 下午2:19:19
	 * @param layoutForward 虚拟源转发
	 * @param layoutForwardDstMembers 目的成员，可能是该虚拟源转发的所有目的成员，也有可能是某个角色新增的该虚拟源转发的目的成员
	 * @param group
	 * @param agendaForward
	 * @param roleChannelIds 角色通道id
	 * @param businessDeliverBO
	 */
	public void executeLayoutForward(
			LayoutForwardPO layoutForward,
			Collection<BusinessGroupMemberPO> layoutForwardDstMembers,
			GroupPO group,
			AgendaPO agenda,
			AgendaForwardPO agendaForward,
			List<Long> roleChannelIds,
			BusinessDeliverBO businessDeliverBO
			) throws Exception{
		
		List<AgendaForwardSourcePO> agendaForwardSources = agendaForwardSourceDao.findByAgendaForwardId(agendaForward.getId());
		List<CombineAudioPermissionPO> audioPers = combineAudioPermissionDao.findByGroupIdAndAgendaId(group.getId(), agendaForward.getAgendaId());
		
		//查出该group可能包含的所有PageInfoPO
		List<String> originIds = businessGroupMemberDao.findOriginIdsByGroupId(group.getId());
		List<PageInfoPO> pageInfos = pageInfoDao.findByOriginIdIn(originIds);
		
		LayoutForwardSourceType layoutForwardSourceType = layoutForward.getSourceType();
		if(LayoutForwardSourceType.LAYOUT_POSITION.equals(layoutForwardSourceType)){
			
			//转发虚拟源分屏，注意区分terminalDecodeChannelId==-1
			Long terminalDecodeChannelId = layoutForward.getTerminalDecodeChannelId();
			boolean enablePosition = layoutForward.getEnablePosition();
//			Long layoutForwardId = layoutForward.getSourceId();
//			LayoutPositionPO layoutPosition = layoutPositionDao.findOne(layoutForwardId);
			Integer serialNum = layoutForward.getSourceId().intValue();
			
			
			//从agendaForwardSources找相同的serialNum，应该只有1个
			List<AgendaForwardSourcePO> ss = tetrisBvcQueryUtil.queryAgendaForwardSourcesBySerialNum(agendaForwardSources, serialNum, roleChannelIds);
			AgendaForwardSourcePO agendaForwardSource = null;
			if(ss.size() == 0){
				log.info("在layoutPosition的 serialNum = " + serialNum + " 上没有找到配置的源，不需要转发");
				return;
			}else if(ss.size() > 1){
				log.info("在layoutPosition的 serialNum = " + serialNum + " 上找到了多个源，配置可能有误，取第1个");
			}
			agendaForwardSource = ss.get(0);
			//轮询的话建一个轮询合屏（这里后边不需要了，代码挪到下边COMBINE_TEMPLATE里，单画面的虚拟源转发都要配成COMBINE_TEMPLATE类型）
			BusinessCombineVideoPO sinaleLoopCombineVideo =null;
			if(agendaForwardSource.getIsLoop()){
				String uid = combineVideoService.generateCombineVideoUid(group.getId(), ss, null, 2);
				List<BusinessCombineVideoPO> cs = businessCombineVideoDao.findByCombineVideoUid(uid);

				if(cs.size() == 0){
					//新建合屏
					sinaleLoopCombineVideo = combineVideoService.templateCombineVideo(group, null, null, ss, roleChannelIds, 2, businessDeliverBO);
					businessCombineVideoDao.save(sinaleLoopCombineVideo);
					businessDeliverBO.getStartCombineVideos().add(sinaleLoopCombineVideo);
				}else{
					if(cs.size() > 1) log.warn("根据唯一标识串找到了多个合屏，取第1个使用：" + uid);
					sinaleLoopCombineVideo = cs.get(0);
				}
			}

			
			//给指定通道转发单画面
			if(terminalDecodeChannelId != -1){
				List<BusinessGroupMemberTerminalChannelPO> videoEncodeTerminalChannels = agendaRoleMemberUtil.obtainMemberTerminalChannelsFromAgendaForwardSource(
						agendaForwardSource.getSourceId(), agendaForwardSource.getSourceType(), group.getMembers());
				BusinessGroupMemberTerminalChannelPO videoEncodeTerminalChannel = null;
				if(videoEncodeTerminalChannels.size() > 0) videoEncodeTerminalChannel = videoEncodeTerminalChannels.get(0);
				
				for(BusinessGroupMemberPO member : layoutForwardDstMembers){
					
					if(OriginType.OUTER.equals(member.getOriginType())) continue;
					
					//选择设备视频解码通道
					BusinessGroupMemberTerminalChannelPO videoDecodeTerminalChannel = tetrisBvcQueryUtil.queryBusinessGroupMemberTerminalChannelByTerminalChannelId(member.getChannels(), terminalDecodeChannelId);
					BusinessGroupMemberTerminalBundleChannelPO videoDecodeBundleChannel = multiRateUtil.queryDefultDecodeChannel(videoDecodeTerminalChannel.getMemberTerminalBundleChannels());
					if(videoDecodeBundleChannel == null || videoEncodeTerminalChannel == null) continue;
					
					//选择能匹配分辨率码率的编码通道
					BusinessGroupMemberTerminalBundleChannelPO videoEncodeBundleChannel = multiRateUtil.queryEncodeChannel(
							videoEncodeTerminalChannel.getMemberTerminalBundleChannels(),
							videoDecodeBundleChannel.getChannelParamsType());								
					if(videoEncodeBundleChannel == null){
						log.info("给成员" + member.getName() + "成员转发 " + videoEncodeTerminalChannel.getMember().getName() + " 时，未能找到匹配的编码通道");
						continue;
					}
					
					//构建 PageTaskPO
					PageTaskPO task = new PageTaskPO(ExecuteStatus.UNDONE, ExecuteStatus.UNDONE);
					setVideoToPageTaskByChannel(task, member, layoutForward, agendaForwardSource, videoEncodeBundleChannel, videoDecodeBundleChannel,sinaleLoopCombineVideo);
					if(agenda.getBusinessInfoType() != null){
						task.setBusinessInfoType(agenda.getBusinessInfoType());
					}

					//找到这个成员的pageInfo，新的task与之关联，放入businessDeliverBO
					PageInfoPO pageInfo = tetrisBvcQueryUtil.queryPageInfoByOriginIdAndTerminalId(pageInfos, member.getOriginId(), member.getTerminalId());
					task.setPageInfo(pageInfo);
					businessDeliverBO.getStartPageTasks().add(task);
					
					//找到该成员是否有要听的声音
					BusinessCombineAudioPO combineAudio = combineAudioService.queryAudioForMember(audioPers, agenda, agendaForward.getId(), member.getId(), group);
					setAudioToPageTask(task, combineAudio, member, videoDecodeBundleChannel);					
										
					//视、音频是否能够转发
					if(!groupStopService.whetherVideoCanBeDone(task)){
						task.setVideoStatus(ExecuteStatus.UNDONE);
					}
					if(!groupStopService.whetherAudioCanBeDone(task)){
						task.setAudioStatus(ExecuteStatus.UNDONE);
					}
				}
			}
			
			//给随机通道（qt）转发单画面
			else if(terminalDecodeChannelId == -1){
                //qt终端所有议程转发源n个都需要处理
				for (AgendaForwardSourcePO agendaFordSourceqt:ss) {
					List<BusinessGroupMemberTerminalChannelPO> videoEncodeTerminalChannels = agendaRoleMemberUtil.obtainMemberTerminalChannelsFromAgendaForwardSource(
							agendaFordSourceqt.getSourceId(), agendaFordSourceqt.getSourceType(), group.getMembers());
					BusinessGroupMemberTerminalChannelPO videoEncodeTerminalChannel = null;
					if(videoEncodeTerminalChannels.size() > 0) videoEncodeTerminalChannel = videoEncodeTerminalChannels.get(0);
					//随机选择解码进行转发（通常是给qt）
					for(BusinessGroupMemberPO member : layoutForwardDstMembers){

						if(OriginType.OUTER.equals(member.getOriginType())) continue;

						//可能观看1:n角色的源（如看观众、看发言人）
						for(BusinessGroupMemberTerminalChannelPO videoEncodeTerminalChannel1 : videoEncodeTerminalChannels){

							//给qt，按默认规则选择 高清编码通道
							BusinessGroupMemberTerminalBundleChannelPO videoEncodeBundleChannel = multiRateUtil.queryDefultEncodeChannel(videoEncodeTerminalChannel1.getMemberTerminalBundleChannels());

							if(videoEncodeBundleChannel == null){
								continue;
							}

							//构建 PageTaskPO
							PageTaskPO task = new PageTaskPO(ExecuteStatus.UNDONE, ExecuteStatus.UNDONE);
							setVideoToPageTaskByChannel(task, member, layoutForward, agendaFordSourceqt, videoEncodeBundleChannel, null,null);
							if(agenda.getBusinessInfoType() != null){
								task.setBusinessInfoType(agenda.getBusinessInfoType());
							}
							
							//特殊业务，例如：媒体转发。媒体转发以AgendaForwardDemandPO的id为后缀；其它业务后续再定，先写成agendaForward.getId()
							if(agendaForward.getBusinessId() != null){
								BusinessInfoType type = agendaForward.getAgendaForwardBusinessType();
								task.setBusinessInfoType(type);
								if(BusinessInfoType.COMMAND_FORWARD.equals(type) || BusinessInfoType.COMMAND_FORWARD_DEVICE.equals(type) || BusinessInfoType.COMMAND_FORWARD_USER.equals(type) ){
									AgendaForwardDemandPO demand = agendaForwardDemandDao.findByAgendaForwardId(agendaForward.getId());
									task.setBusinessId(group.getId() + "-" + demand.getId());
								}else{
									task.setBusinessId(group.getId() + "-" + agendaForward.getId());
								}
							}
							
							//视频是否能够转发
							if(!groupStopService.whetherVideoCanBeDone(task)){
								task.setVideoStatus(ExecuteStatus.UNDONE);
							}

							//找到这个成员的pageInfo，新的task与之关联，放入businessDeliverBO
							PageInfoPO pageInfo = tetrisBvcQueryUtil.queryPageInfoByOriginIdAndTerminalId(pageInfos, member.getOriginId(), member.getTerminalId());
							task.setPageInfo(pageInfo);
							businessDeliverBO.getStartPageTasks().add(task);

							//这里（一般是qt）直接听对应的声音，忽略议程的配置
							BusinessGroupMemberTerminalChannelPO audioEncodeTerminalChannel = videoEncodeTerminalChannel1.getAudioEncodeChannel();
							if(audioEncodeTerminalChannel != null){
								BusinessGroupMemberTerminalBundleChannelPO audioEncodeBundleChannel = multiRateUtil.queryDefultEncodeChannel(audioEncodeTerminalChannel.getMemberTerminalBundleChannels());
								if(audioEncodeBundleChannel != null){

									BusinessGroupMemberTerminalBundlePO audioEncodeBundle = videoEncodeBundleChannel.getMemberTerminalBundle();
									task.setAudioSourceType(com.sumavision.tetris.bvc.page.SourceType.CHANNEL);
//									task.setCombineAudioUuid(combineAudio.getUuid());
									task.setSrcAudioId(audioEncodeBundleChannel.getBundleId());
									task.setSrcAudioBundleId(audioEncodeBundleChannel.getBundleId());
									task.setSrcAudioLayerId(audioEncodeBundle.getLayerId());
									task.setSrcAudioChannelId(audioEncodeBundleChannel.getChannelId());
									task.setSrcAudioName(audioEncodeBundle.getBundleName());
									task.setSrcAudioMemberId(audioEncodeBundleChannel.getMemberTerminalChannel().getMember().getId());
									task.setSrcAudioCode(audioEncodeBundle.getUsername());
									task.setAudioStatus(ExecuteStatus.DONE);
									
									/*if(GroupStatus.PAUSE.equals(group.getStatus())){
										task.setVideoStatus(ExecuteStatus.UNDONE);
										task.setAudioStatus(ExecuteStatus.UNDONE);
									}else{
										task.setVideoStatus(ExecuteStatus.DONE);
										task.setAudioStatus(ExecuteStatus.DONE);
									}*/
									
									//音频是否能够转发
									if(!groupStopService.whetherAudioCanBeDone(task)){
										task.setAudioStatus(ExecuteStatus.UNDONE);
									}

									if(Boolean.TRUE.equals(audioEncodeBundle.getMulticastEncode())){
										String addr = multicastService.addrAddPort(audioEncodeBundle.getMulticastEncodeAddr(), 4);
										task.setAudioTransmissionMode(TransmissionMode.MULTICAST);
										task.setAudioMultiAddr(addr);
										task.setAudioMultiSrcAddr(audioEncodeBundle.getMulticastSourceIp());
									}
								}
							}
						}
					}
				}

				

			}
		}
		else if(LayoutForwardSourceType.COMBINE_TEMPLATE.equals(layoutForwardSourceType)){
			//合屏
			
			//如果没有目的成员，则返回，不创建合屏。合屏条件是目的成员数大于0，无论成员是否有通道看合屏
			if(layoutForwardDstMembers.size() == 0) return;
			
			//单画面则为null，合屏则有值
			BusinessCombineVideoPO combineVideo = null;
			
			//单画面则有值，合屏为null
			BusinessGroupMemberTerminalChannelPO videoEncodeTerminalChannel = null;
			
			Long combineTemplateId = layoutForward.getSourceId();
			List<CombineTemplatePositionPO> combineTemplatePositions = combineTemplatePositionDao.findByCombineTemplateIdIn(
					new ArrayListWrapper<Long>().add(combineTemplateId).getList());
			
			if(combineTemplatePositions.size() == 0){
				log.warn("合屏模板没有找到分屏，配置有误！id: " + combineTemplateId);
				return;
			}
			
			//初步判断是否需要合屏
			boolean bCombineVideo = true;
			AgendaForwardSourcePO agendaForwardSource = null;//该变量用来记录转发源
			if(combineTemplatePositions.size() == 1){
				
				Integer serialNum = combineTemplatePositions.get(0).getLayoutPositionSerialNum();
				
				List<AgendaForwardSourcePO> ss = tetrisBvcQueryUtil.queryAgendaForwardSourcesBySerialNum(agendaForwardSources, serialNum, roleChannelIds);
				
				if(ss.size() == 0){
					log.info("在议程转发 " + agendaForward.getName() + "layoutPosition的 serialNum = " + serialNum + " 上没有找到配置的源，不需要转发");
					return;
				}
				if(ss.size() == 1){
					//转发，不合屏
					agendaForwardSource = ss.get(0);
					bCombineVideo = false;
				}
			}
			
			//如果是单画面，此时可能需要自动轮询，或者不需要合屏
			if(!bCombineVideo){
				
				//如果是合屏虚拟源，combineVideo不再为null
				if(agendaForwardSource.getSourceType().equals(SourceType.COMBINE_VIDEO_VIRTUAL_SOURCE)){
					AgendaPO virtualSourceAgenda = agendaDao.findOne(agendaForwardSource.getSourceId());
					combineVideo = combineVideoDao.findByUuid(virtualSourceAgenda.getUuid());
					if(combineVideo == null){
						AgendaForwardPO virtualAgendaForward = agendaForwardDao.findByAgendaId(virtualSourceAgenda.getId()).get(0);
						combineVideo = virtualSourceService.combineVideo(group, virtualSourceAgenda, virtualAgendaForward, businessDeliverBO);
					}else{
						Set<CombineTemplateGroupAgendeForwardPermissionPO> cts = combineVideo.getCombineTemplates();
						Set<CombineTemplateGroupAgendeForwardPermissionPO> ucts = businessDeliverBO.getUnusefulVideoPermissions();
						//这么写无效
						//ucts.removeAll(cts);
						//必须先拷贝。或者可以在循环中逐个remove
						Set<CombineTemplateGroupAgendeForwardPermissionPO> cts_copy = new HashSet<CombineTemplateGroupAgendeForwardPermissionPO>(cts);
						ucts.removeAll(cts_copy);
					}
				}
				else{
									
					//只有1个源，找到源编码通道TerminalChannel（注意：后边还要根据目的通道的解码类型来选择最终的BundleChannel）
	//				AgendaForwardSourcePO agendaForwardSource = agendaForwardSources.get(0);
					videoEncodeTerminalChannel = agendaRoleMemberUtil.obtainMemberTerminalChannelFromAgendaForwardSource(
							agendaForwardSource.getSourceId(), agendaForwardSource.getSourceType(), group.getMembers());				
	
					//只有1个源，但他是1:n的角色，且配置了轮询，那么进行自动轮询，建一个轮询合屏
					BusinessCombineVideoPO sinaleLoopCombineVideo =null;
					if(agendaForwardSource.getIsLoop()
							&& SourceType.ROLE_CHANNEL.equals(agendaForwardSource.getSourceType())){					
						Long roleChannelId = agendaForwardSource.getSourceId();
						RoleChannelPO roleChannel = roleChannelDao.findOne(roleChannelId);
						Long roleId = roleChannel.getRoleId();
						RolePO role = roleDao.findOne(roleId);
						if(role.getRoleUserMappingType().equals(RoleUserMappingType.ONE_TO_MANY)){
							String uid = combineVideoService.generateCombineVideoUid(group.getId(), agendaForwardSources, null, 2);
							List<BusinessCombineVideoPO> cs = businessCombineVideoDao.findByCombineVideoUid(uid);
	
							if(cs.size() == 0){
								//新建轮询合屏
								sinaleLoopCombineVideo = combineVideoService.templateCombineVideo(group, null, null, agendaForwardSources, roleChannelIds, 2, businessDeliverBO);
								businessCombineVideoDao.save(sinaleLoopCombineVideo);
								businessDeliverBO.getStartCombineVideos().add(sinaleLoopCombineVideo);
							}else{
								if(cs.size() > 1) log.warn("根据唯一标识串找到了多个合屏，取第1个使用：" + uid);
								sinaleLoopCombineVideo = cs.get(0);
							}
							combineVideo = sinaleLoopCombineVideo;
						}
					}
				}
			}
			//合屏，combineVideo不再为null
			else{
				
				//找出关联关系
	//			Long combineTemplateId = layoutForward.getSourceId();
				CombineTemplateGroupAgendeForwardPermissionPO p = combineTemplateGroupAgendeForwardPermissionDao.
						findByGroupIdAndCombineTemplateIdAndAgendaForwardId(group.getId(), combineTemplateId, agendaForward.getId());
				if(p == null){
					p = new CombineTemplateGroupAgendeForwardPermissionPO();
					p.setGroupId(group.getId());
					p.setCombineTemplateId(combineTemplateId);
					p.setTerminalId(layoutForward.getTerminalId());
					p.setAgendaId(agendaForward.getAgendaId());
					p.setAgendaForwardId(agendaForward.getId());
					p.setLayoutId(layoutForward.getLayoutId());
					combineTemplateGroupAgendeForwardPermissionDao.save(p);
				}
				businessDeliverBO.getUnusefulVideoPermissions().remove(p);
				businessDeliverBO.getUsefulVideoPermissions().add(p);
				
				//看是否已有合屏
				combineVideo = p.getCombineVideo();
				if(combineVideo == null){
					
					//寻找是否可以使用其他合屏，判重
	//				List<CombineTemplatePositionPO> combineTemplatePositions = combineTemplatePositionDao.findByCombineTemplateIdIn(
	//						new ArrayListWrapper<Long>().add(combineTemplateId).getList());
					String uid = combineVideoService.generateCombineVideoUid(group.getId(), agendaForwardSources, combineTemplatePositions, 1);
					List<BusinessCombineVideoPO> cs = businessCombineVideoDao.findByCombineVideoUid(uid);
					if(cs.size() == 0){
						
						//新建合屏
						combineVideo = combineVideoService.templateCombineVideo(group, null, combineTemplatePositions, agendaForwardSources, roleChannelIds, 1, businessDeliverBO);
						//与合屏模板互相关联
						combineVideo.setCombineTemplates(new HashSet<CombineTemplateGroupAgendeForwardPermissionPO>());
						combineVideo.getCombineTemplates().add(p);
						p.setCombineVideo(combineVideo);					
						businessCombineVideoDao.save(combineVideo);
						
						businessDeliverBO.getStartCombineVideos().add(combineVideo);
						
					}else{
						
						if(cs.size() > 1) log.warn("根据唯一标识串找到了多个合屏，取第1个使用：" + uid);
						combineVideo = cs.get(0);
						//与合屏模板互相关联
						combineVideo.getCombineTemplates().add(p);
						p.setCombineVideo(combineVideo);
						
						businessCombineVideoDao.save(combineVideo);
						
					}
				}
			}
			
			Long terminalDecodeChannelId = layoutForward.getTerminalDecodeChannelId();
			
			//给这种终端类型的成员forwardDstMembers转发合屏
			for(BusinessGroupMemberPO member : layoutForwardDstMembers){
				
				if(OriginType.OUTER.equals(member.getOriginType())) continue;
				
				//找到这个成员的解码通道
				BusinessGroupMemberTerminalChannelPO videoDecodeTerminalChannel = tetrisBvcQueryUtil.queryBusinessGroupMemberTerminalChannelByTerminalChannelId(member.getChannels(), terminalDecodeChannelId);
				BusinessGroupMemberTerminalBundleChannelPO videoDecodeBundleChannel = multiRateUtil.queryDefultDecodeChannel(videoDecodeTerminalChannel.getMemberTerminalBundleChannels());
				if(videoDecodeBundleChannel == null) continue;
				
				//单画面时，需要agendaForwardSource、videoEncodeBundleChannel
//				AgendaForwardSourcePO agendaForwardSource = null;
				BusinessGroupMemberTerminalBundleChannelPO videoEncodeBundleChannel = null;
				if(combineVideo == null){
					//选择能匹配分辨率码率的编码通道
//					agendaForwardSource = agendaForwardSources.get(0);
					videoEncodeBundleChannel = multiRateUtil.queryEncodeChannel(
							videoEncodeTerminalChannel.getMemberTerminalBundleChannels(),
							videoDecodeBundleChannel.getChannelParamsType());
					if(videoEncodeBundleChannel == null){
						log.info("给成员" + member.getName() + "成员转发 " + videoEncodeTerminalChannel.getMember().getName() + " 时，未能找到匹配的编码通道");
						continue;
					}
				}
								
				//转发给bundleChannel，构建 PageTaskPO				
				PageTaskPO task = new PageTaskPO();
				setVideoToPageTaskByChannel(task, member, layoutForward, agendaForwardSource, videoEncodeBundleChannel, videoDecodeBundleChannel, combineVideo);
				if(agenda.getBusinessInfoType() != null){
					task.setBusinessInfoType(agenda.getBusinessInfoType());
				}
				
				//找到这个成员的pageInfo，新的task与之关联，放入businessDeliverBO
				PageInfoPO pageInfo = tetrisBvcQueryUtil.queryPageInfoByOriginIdAndTerminalId(pageInfos, member.getOriginId(), member.getTerminalId());
				task.setPageInfo(pageInfo);
				businessDeliverBO.getStartPageTasks().add(task);
				
				//找到该成员是否有要听的声音
				BusinessCombineAudioPO combineAudio = combineAudioService.queryAudioForMember(audioPers, agenda, agendaForward.getId(), member.getId(), group);
				setAudioToPageTask(task, combineAudio, member, videoDecodeBundleChannel);
			}
		}
	}
	
	/**
	 * 给一部分成员停止虚拟源转发（给某种终端类型）<br/>
	 * <p>可能会删除合屏、混音</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月26日 下午2:19:19
	 * @param layoutForward 虚拟源转发
	 * @param layoutFforwardDstMembers 需要停止的目的成员
	 * @param group
	 * @param agendaForward
	 * @param businessDeliverBO
	 */
	@Deprecated
	public void stopLayoutForward(
			LayoutForwardPO layoutForward,
			Collection<BusinessGroupMemberPO> layoutForwardDeleteMembers,
			GroupPO group,
			AgendaForwardPO agendaForward,
			BusinessDeliverBO businessDeliverBO
			){

		List<AgendaForwardSourcePO> agendaForwardSources = agendaForwardSourceDao.findByAgendaForwardId(agendaForward.getId());
		
		//先查出该group包含的所有task
		String businessId = group.getId().toString();
		String _businessId = businessId+"-";
		List<PageTaskPO> tasks = pageTaskDao.findByBusinessIdStartingWithOrBusinessId(_businessId, businessId);
		
		LayoutForwardSourceType layoutForwardSourceType = layoutForward.getSourceType();
		if(LayoutForwardSourceType.LAYOUT_POSITION.equals(layoutForwardSourceType)){
			
			//转发虚拟源分屏，注意区分terminalDecodeChannelId==-1
			Long terminalDecodeChannelId = layoutForward.getTerminalDecodeChannelId();
			boolean enablePosition = layoutForward.getEnablePosition();
			Long layoutForwardId = layoutForward.getSourceId();
			LayoutPositionPO layoutPosition = layoutPositionDao.findOne(layoutForwardId);
			Integer serialNum = layoutPosition.getSerialNum();
			
			
			//从agendaForwardSources找相同的serialNum，应该只有1个
			List<AgendaForwardSourcePO> ss = tetrisBvcQueryUtil.queryAgendaForwardSourcesBySerialNum(agendaForwardSources, serialNum, null);
			AgendaForwardSourcePO agendaForwardSource = null;
			if(ss.size() == 0){
				log.info("在layoutPosition的 serialNum = " + serialNum + " 上没有找到配置的源，不需要转发");
				return;
			}else if(ss.size() > 1){
				log.info("在layoutPosition的 serialNum = " + serialNum + " 上找到了多个源，配置可能有误，取第1个");
			}
			agendaForwardSource = ss.get(0);
			
			//找到源编码通道TerminalChannel（注意：后边还要根据目的通道的解码类型来选择最终的BundleChannel）
			BusinessGroupMemberTerminalChannelPO videoEncodeTerminalChannel = agendaRoleMemberUtil.obtainMemberTerminalChannelFromAgendaForwardSource(
					agendaForwardSource.getSourceId(), agendaForwardSource.getSourceType(), group.getMembers());
			
			
			//停止给指定通道转发单画面
			if(terminalDecodeChannelId != -1){
				
				//给这种终端类型的成员layoutForwardDeleteMembers删除观看
				for(BusinessGroupMemberPO member : layoutForwardDeleteMembers){
										
					//根据 dstMemberId terminalDecodeChannelId 找到PageTaskPO，传到外边去，“删除分页任务”
					PageTaskPO task = tetrisBvcQueryUtil.queryPageTaskByDstMemberIdAndTerminalVideoDecodeChannelId(tasks, member.getId(), terminalDecodeChannelId);
					businessDeliverBO.getStopPageTasks().add(task);
					
					//声音一起停止？
					
				}
			}
			
			//停止给随机通道（qt）转发单画面
			else if(terminalDecodeChannelId == -1){
				
				//给这种终端类型的成员layoutForwardDeleteMembers删除观看
				for(BusinessGroupMemberPO member : layoutForwardDeleteMembers){
					
					//根据 dstMemberId srcVideoMemberTerminalChannelId 找到PageTaskPO，传到外边去，“删除分页任务”
					PageTaskPO task = tetrisBvcQueryUtil.queryPageTaskByDstMemberIdAndSrcVideoMemberTerminalChannelId(tasks, member.getId(), videoEncodeTerminalChannel.getId());
					businessDeliverBO.getStopPageTasks().add(task);
					
				}											
			}
		}else if(LayoutForwardSourceType.COMBINE_TEMPLATE.equals(layoutForwardSourceType)){
			//停止看合屏
			
			List<BusinessGroupMemberPO> layoutForwardAllDstMembers = agendaRoleMemberUtil.obtainLayoutForwardDstMembers(layoutForward, agendaForward, group.getMembers());
						
			//如果该LayoutForwardPO下的目的成员全部清除，则可以消除关联关系CombineTemplateGroupAgendeForwardPermissionPO，进一步看看是否删除合屏
			if(layoutForwardAllDstMembers.size() == 0){
				Long combineTemplateId = layoutForward.getSourceId();
				CombineTemplateGroupAgendeForwardPermissionPO p = combineTemplateGroupAgendeForwardPermissionDao.
						findByGroupIdAndCombineTemplateIdAndAgendaForwardId(group.getId(), combineTemplateId, agendaForward.getId());
				if(p != null){
					BusinessCombineVideoPO combineVideo = p.getCombineVideo();
					if(combineVideo != null){
						Set<CombineTemplateGroupAgendeForwardPermissionPO> ps = combineVideo.getCombineTemplates();
						if(ps.size() == 1){
							log.info("虚拟源不再有成员观看，删除合屏");
							businessCombineVideoDao.delete(combineVideo);
							businessDeliverBO.getStopCombineVideos().add(combineVideo);
						}else{
							log.info("虚拟源不再有成员观看，删除合屏关联关系，不删合屏");
							ps.remove(p);
							businessCombineVideoDao.save(combineVideo);
							combineTemplateGroupAgendeForwardPermissionDao.delete(p);
						}
					}
				}
			}
			
			Long terminalDecodeChannelId = layoutForward.getTerminalDecodeChannelId();
			
			//给这种终端类型的成员layoutForwardDeleteMembers删除观看
			for(BusinessGroupMemberPO member : layoutForwardDeleteMembers){
				//找到这个成员的解码通道
				BusinessGroupMemberTerminalChannelPO videoDecodeTerminalChannel = tetrisBvcQueryUtil.queryBusinessGroupMemberTerminalChannelByTerminalChannelId(member.getChannels(), terminalDecodeChannelId);
				BusinessGroupMemberTerminalBundleChannelPO videoDecodeBundleChannel = multiRateUtil.queryDefultDecodeChannel(videoDecodeTerminalChannel.getMemberTerminalBundleChannels());
				if(videoDecodeBundleChannel == null) continue;
				
				
				//根据 dstMemberId terminalDecodeChannelId 找到PageTaskPO，传到外边去，“删除分页任务”
				PageTaskPO task = tetrisBvcQueryUtil.queryPageTaskByDstMemberIdAndTerminalVideoDecodeChannelId(tasks, member.getId(), terminalDecodeChannelId);
				businessDeliverBO.getStopPageTasks().add(task);
				
				//声音一起停止？
				
			}
		}
	}

	/**
	 * 给PageTask设置单画面/合屏<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月1日 下午2:05:02
	 * @param task
	 * @param member 目标成员
	 * @param layoutForwardId
	 * @param agendaForwardSource 单画面时指明源；合屏时没有用
	 * @param videoEncodeBundleChannel 提供编码通道的信息，合屏时没有用
	 * @param videoDecodeBundleChannel 解码通道，随机选择解码时可以为null
	 * @param combineVideoPO null表示单画面，否则表示合屏
	 */
	private void setVideoToPageTaskByChannel(			
			PageTaskPO task,
			BusinessGroupMemberPO member,
			LayoutForwardPO layoutForward,
			AgendaForwardSourcePO agendaForwardSource,
			BusinessGroupMemberTerminalBundleChannelPO videoEncodeBundleChannel,
			BusinessGroupMemberTerminalBundleChannelPO videoDecodeBundleChannel,
            BusinessCombineVideoPO combineVideoPO
//			BusinessGroupMemberTerminalChannelPO videoEncodeTerminalChannel,
			){
		
		GroupPO group = member.getGroup();
		
		task.setBusinessInfoType(BusinessInfoType.BASIC_MEETING);
		task.setBusinessId(group.getId().toString());
		
		task.setVideoStatus(ExecuteStatus.UNDONE);
		task.setAudioStatus(ExecuteStatus.UNDONE);
		task.setDstMemberId(member.getId());
		task.setLayoutForwardId(layoutForward.getId());
		task.setEnablePosition(layoutForward.getEnablePosition());
//		if(agendaForwardSource.getIsLoop() && combineVideoPO!=null){
		if(combineVideoPO != null){
			task.setBusinessName(group.getName()+ "：合屏");//业务描述
			task.setVideoSourceType(com.sumavision.tetris.bvc.page.SourceType.COMBINE_VIDEO);
			task.setCombineVideoUuid(combineVideoPO.getUuid());
			task.setSrcVideoId(combineVideoPO.getUuid());
		}else{
			task.setBusinessName(group.getName()+ "：" + videoEncodeBundleChannel.getMemberTerminalChannel().getMember().getName());//业务描述
			BusinessGroupMemberTerminalBundlePO videoEncodeBundle = videoEncodeBundleChannel.getMemberTerminalBundle();
			task.setVideoSourceType(com.sumavision.tetris.bvc.page.SourceType.CHANNEL);
			task.setSrcVideoId(videoEncodeBundleChannel.getBundleId());
			task.setSrcVideoBundleId(videoEncodeBundleChannel.getBundleId());
			task.setSrcVideoLayerId(videoEncodeBundle.getLayerId());
			task.setSrcVideoChannelId(videoEncodeBundleChannel.getChannelId());
			task.setSrcVideoName(videoEncodeBundle.getBundleName());
			task.setSrcVideoCode(videoEncodeBundle.getUsername());
			task.setSrcVideoMemberId(videoEncodeBundleChannel.getMemberTerminalChannel().getMember().getId());
			task.setSrcVideoMemberTerminalChannelId(videoEncodeBundleChannel.getMemberTerminalChannel().getId());
			task.setAgendaForwardSourceId(agendaForwardSource.getId());
			if(Boolean.TRUE.equals(videoEncodeBundle.getMulticastEncode())){
				String addr = multicastService.addrAddPort(videoEncodeBundle.getMulticastEncodeAddr(), 2);
				task.setVideoTransmissionMode(TransmissionMode.MULTICAST);
				task.setVideoMultiAddr(addr);
				task.setVideoMultiSrcAddr(videoEncodeBundle.getMulticastSourceIp());
			}
			
			//根据编码通道，找到参数档位DeviceGroupAvtplGearsPO，记录它的id
			List<DeviceGroupAvtplGearsPO> gears = group.getAvtpl().getGears();
			DeviceGroupAvtplGearsPO gear = null;
			for(DeviceGroupAvtplGearsPO _gear : gears){
				if(_gear.getChannelParamsType().equals(videoEncodeBundleChannel.getChannelParamsType())){
					gear = _gear;
					break;
				}
			}
			if(gear == null){
				log.warn("没有找到与编码通道类型相同的gear，对PageTask将采用默认参数呼叫");
			}else{
				task.setGearId(gear.getId());
			}
		}
		task.setVideoStatus(ExecuteStatus.DONE);
		
		if(videoDecodeBundleChannel != null){
			task.setDstLayerId(videoDecodeBundleChannel.getMemberTerminalBundle().getLayerId());
			task.setDstBundleType(videoDecodeBundleChannel.getBundleType());
			task.setDstBundleName(videoDecodeBundleChannel.getBundleName());
			task.setDstBundleId(videoDecodeBundleChannel.getBundleId());
			task.setDstVideoChannelId(videoDecodeBundleChannel.getChannelId());
			task.setDstVideoBaseType(videoDecodeBundleChannel.getBaseType());
			task.setDstVideoChannelName(videoDecodeBundleChannel.getChannelName());
			task.setTerminalVideoDecodeChannelId(videoDecodeBundleChannel.getMemberTerminalChannel().getTerminalChannelId());
		}
	}
	
	/**
	 * 给PageTask设置音频源<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月1日 下午1:58:11
	 * @param task
	 * @param combineAudio 可能是单音频，也可能是混音
	 * @param member
	 * @param videoDecodeBundleChannel 视频解码设备通道，用来查找音频解码终端通道
	 */
	private void setAudioToPageTask(
			PageTaskPO task,
			BusinessCombineAudioPO combineAudio,
			BusinessGroupMemberPO member,
			BusinessGroupMemberTerminalBundleChannelPO videoDecodeBundleChannel
//			BusinessGroupMemberTerminalChannelPO videoDecodeTerminalChannel
			){
		
		if(combineAudio == null) return;
		if(!combineAudio.isHasSource()) return;
		
		//从视频解码通道查找音频解码通道s
		BusinessGroupMemberTerminalChannelPO videoDecodeTerminalChannel = videoDecodeBundleChannel.getMemberTerminalChannel();
		
		List<BusinessGroupMemberTerminalChannelPO> audioDecodeTerminalChannels = null;
		try{
			audioDecodeTerminalChannels = videoDecodeTerminalChannel.getScreen().getAudioOutput().getChannels();
		}catch(Exception e){
			e.printStackTrace();
			log.info(member.getName() + "成员从视频解码通道没有找到音频解码终端通道，关联关系不全");
			return;
		}
		if(audioDecodeTerminalChannels == null || audioDecodeTerminalChannels.size() == 0){
			log.info(member.getName() + "成员从视频解码通道没有找到音频解码终端通道");
			return;
		}
		
		//查找用来音频解码的设备通道。条件是与视频解码在同一bundle
		BusinessGroupMemberTerminalBundleChannelPO audioDecodeBundleChannel = null;
		for(BusinessGroupMemberTerminalChannelPO audioDecodeTerminalChannel : audioDecodeTerminalChannels){
			BusinessGroupMemberTerminalBundleChannelPO aDBundleChannel = multiRateUtil.queryDefultDecodeChannel(audioDecodeTerminalChannel.getMemberTerminalBundleChannels());
			if(aDBundleChannel == null) continue;
			if(aDBundleChannel.getBundleId().equals(videoDecodeBundleChannel.getBundleId())){
				audioDecodeBundleChannel = aDBundleChannel;
				break;
			}
		}
		if(audioDecodeBundleChannel == null){
			log.info(member.getName() + "成员没有找到与视频解码同一bundle的音频解码设备通道，不转发音频");
			return;
		}
		
		//根据BusinessCombineAudioPO设置音频转发（注意区分单音频/混音）
		if(combineAudio.isMix()){
			
			task.setAudioSourceType(com.sumavision.tetris.bvc.page.SourceType.COMBINE_AUDIO);
			task.setCombineAudioUuid(combineAudio.getUuid());
			task.setSrcAudioId(combineAudio.getUuid());
			task.setAudioStatus(ExecuteStatus.DONE);
			
			task.setDstAudioChannelId(audioDecodeBundleChannel.getChannelId());
			task.setDstAudioBaseType(audioDecodeBundleChannel.getBaseType());
			task.setDstAudioChannelName(audioDecodeBundleChannel.getChannelName());
			
		}else{
			task.setAudioSourceType(com.sumavision.tetris.bvc.page.SourceType.CHANNEL);
			for(BusinessCombineAudioSrcPO src : combineAudio.getSrcs()){
				if(src.isHasSource()){
					task.setSrcAudioId(src.getBundleId());
					task.setSrcAudioBundleId(src.getBundleId());
					task.setSrcAudioLayerId(src.getLayerId());
					task.setSrcAudioChannelId(src.getChannelId());
					task.setAudioStatus(ExecuteStatus.DONE);
					break;
				}
			}
			
//			task.setSrcAudioName(forward.getSrcAudioName());			
//			task.setSrcAudioCode(forward.getSrcAudioCode());
//			task.setAudioTransmissionMode(forward.getAudioTransmissionMode());
//			task.setAudioMultiAddr(forward.getAudioMultiAddr());
//			task.setAudioMultiSrcAddr(forward.getAudioMultiSrcAddr());
//			if(Boolean.TRUE.equals(audioEncodeBundle.getMulticastEncode())){
//				String addr = multicastService.addrAddPort(audioEncodeBundle.getMulticastEncodeAddr(), 4);
//				task.setAudioTransmissionMode(TransmissionMode.MULTICAST);
//				task.setAudioMultiAddr(addr);
//				task.setAudioMultiSrcAddr(audioEncodeBundle.getMulticastSourceIp());
//			}
		}
		
		task.setDstAudioChannelId(audioDecodeBundleChannel.getChannelId());
		task.setDstAudioBaseType(audioDecodeBundleChannel.getBaseType());
		task.setDstAudioChannelName(audioDecodeBundleChannel.getChannelName());
	}
	
}

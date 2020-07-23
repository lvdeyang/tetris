package com.sumavision.tetris.bvc.model.agenda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import com.sumavision.tetris.bvc.model.agenda.exception.AgendaNotFoundException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.sumavision.bvc.command.group.basic.CommandGroupPO;
import com.sumavision.bvc.command.group.dao.CommandGroupUserInfoDAO;
import com.sumavision.bvc.command.group.enumeration.ForwardBusinessType;
import com.sumavision.bvc.command.group.user.CommandGroupUserInfoPO;
import com.sumavision.bvc.command.group.user.layout.page.CommandPlayerTaskBO;
import com.sumavision.bvc.command.group.user.layout.page.CommandPlayerTaskPO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerCastDevicePO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.command.group.user.layout.player.PlayerBusinessType;
import com.sumavision.bvc.command.group.user.layout.scheme.CommandGroupUserLayoutShemePO;
import com.sumavision.bvc.command.group.user.layout.scheme.PlayerSplitLayout;
import com.sumavision.bvc.control.device.command.group.vo.user.CommandGroupUserLayoutShemeVO;
import com.sumavision.bvc.device.command.bo.MessageSendCacheBO;
import com.sumavision.bvc.device.command.bo.PlayerInfoBO;
import com.sumavision.bvc.device.command.cast.CommandCastServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonConstant;
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
import com.sumavision.bvc.device.monitor.live.DstDeviceType;
import com.sumavision.bvc.resource.dao.ResourceBundleDAO;
import com.sumavision.bvc.resource.dao.ResourceChannelDAO;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.bvc.system.po.AvtplGearsPO;
import com.sumavision.bvc.system.po.AvtplPO;
import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.bvc.business.ExecuteStatus;
import com.sumavision.tetris.bvc.business.bo.MemberChangedTaskBO;
import com.sumavision.tetris.bvc.business.bo.ModifyMemberRoleBO;
import com.sumavision.tetris.bvc.business.bo.SourceBO;
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
import com.sumavision.tetris.bvc.business.terminal.user.TerminalBundleUserPermissionDAO;
import com.sumavision.tetris.bvc.business.terminal.user.TerminalBundleUserPermissionPO;
import com.sumavision.tetris.bvc.model.role.InternalRoleType;
import com.sumavision.tetris.bvc.model.role.RoleChannelDAO;
import com.sumavision.tetris.bvc.model.role.RoleChannelPO;
import com.sumavision.tetris.bvc.model.role.RoleDAO;
import com.sumavision.tetris.bvc.model.role.RolePO;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundleDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundlePO;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundleType;
import com.sumavision.tetris.bvc.model.terminal.TerminalPO;
import com.sumavision.tetris.bvc.page.PageInfoDAO;
import com.sumavision.tetris.bvc.page.PageInfoPO;
import com.sumavision.tetris.bvc.page.PageTaskBO;
import com.sumavision.tetris.bvc.page.PageTaskDAO;
import com.sumavision.tetris.bvc.page.PageTaskPO;
import com.sumavision.tetris.bvc.page.PageTaskService;
import com.sumavision.tetris.bvc.util.TetrisBvcQueryUtil;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.orm.po.AbstractBasePO;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;
import com.sumavision.tetris.websocket.message.WebsocketMessageType;
import com.sumavision.tetris.websocket.message.WebsocketMessageVO;

import lombok.extern.slf4j.Slf4j;

/**
 * CommandSplitPagingServiceImpl<br/>
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年5月22日 上午10:03:43
 */
@Slf4j
//@Transactional(rollbackFor = Exception.class)
@Service
public class AgendaService {
	
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
	private RunningAgendaDAO runningAgendaDao;
	
	@Autowired
	private PageTaskService pageTaskService;
	
	@Autowired
	private CommandCommonServiceImpl commandCommonServiceImpl;
	
	@Autowired
	private WebsocketMessageService websocketMessageService;
	
	@Autowired
	private CommandUserServiceImpl commandUserServiceImpl;
	
	@Autowired
	private CommandCastServiceImpl commandCastServiceImpl;

	@Autowired
	private CommandCommonUtil commandCommonUtil;

	@Autowired
	private QueryUtil queryUtil;

	@Autowired
	private TetrisBvcQueryUtil tetrisBvcQueryUtil;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	private Map<String, TerminalPO> terminalPOMap = new HashMap<String, TerminalPO>();
	
	@Autowired
	private AgendaDAO agendaDao;
	
	/**
	 * 添加议程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月30日 下午4:38:25
	 * @param String name 议程名称
	 * @param String remark 备注
	 * @param Integer volume 音量
	 * @param String audioOperationType 音频操作类型
	 * @return AgendaVO 议程
	 */
	@Transactional(rollbackFor = Exception.class)
	public AgendaVO add(
			String name,
			String remark,
			Integer volume,
			String audioOperationType) throws Exception{
		AgendaPO agenda = new AgendaPO();
		agenda.setName(name);
		agenda.setRemark(remark);
		agenda.setVolume(volume);
		agenda.setAudioOperationType(AudioOperationType.valueOf(audioOperationType));
		agenda.setUpdateTime(new Date());
		agendaDao.save(agenda);
		return new AgendaVO().set(agenda);
	}
	
	/**
	 * 修改议程信息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月30日 下午4:42:26
	 * @param Long id 议程id
	 * @param String name 议程名称
	 * @param String remark 备注
	 * @param Integer volume 音量
	 * @param String audioOperationType 音频操作类型
	 * @return AgendaVO 议程
	 */
	@Transactional(rollbackFor = Exception.class)
	public AgendaVO edit(
			Long id,
			String name,
			String remark,
			Integer volume,
			String audioOperationType) throws Exception{
		AgendaPO agenda = agendaDao.findOne(id);
		if(agenda == null){
			throw new AgendaNotFoundException(id);
		}
		agenda.setName(name);
		agenda.setRemark(remark);
		agenda.setVolume(volume);
		agenda.setAudioOperationType(AudioOperationType.valueOf(audioOperationType));
		agenda.setUpdateTime(new Date());
		agendaDao.save(agenda);
		return new AgendaVO().set(agenda);
	}
	
	/**
	 * 删除议程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月30日 下午4:43:30
	 * @param Long id 议程id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long id) throws Exception{
		AgendaPO agenda = agendaDao.findOne(id);
		if(agenda != null){
			agendaDao.delete(agenda);
		}
	}
	
	private Map<String, TerminalBundlePO> terminalBundleMap = new HashMap<String, TerminalBundlePO>();
	
	/*public void runAgenda(AgendaPO agenda, Long groupId){
		List<CommonForwardPO> allForwards = obtainCommonForwardsFromSource(agenda, groupId);
		//持久化CommonForwardPO
		//pageTaskService.addAndRemoveTasks(pageInfo, newTasks, null);
	}*/
	
	public void runAndStopAgenda(Long groupId, List<Long> runAgendaIds, List<Long> stopAgendaIds) throws Exception{
		
		if(runAgendaIds == null) runAgendaIds = new ArrayList<Long>();
		if(stopAgendaIds == null) stopAgendaIds = new ArrayList<Long>();		
		
		//先获取原来的转发关系
		List<CommonForwardPO> oldForwards = commonForwardDao.findByBusinessId(groupId.toString());
		
		//持久化agenda执行状态。TODO:先校验是否已经执行，也就是判重
		List<RunningAgendaPO> newRunnings = new ArrayList<RunningAgendaPO>();
		for(Long runAgendaId : runAgendaIds){
			RunningAgendaPO r = new RunningAgendaPO(groupId, runAgendaId);
			newRunnings.add(r);
		}
		runningAgendaDao.save(newRunnings);
		
		List<RunningAgendaPO> stopRunnings = runningAgendaDao.findByGroupIdAndAgendaIdIn(groupId, stopAgendaIds);
		runningAgendaDao.deleteInBatch(stopRunnings);
								
		executeToFinal(groupId, oldForwards);
	}

	/**
	 * 获取成员的音视频源<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月22日 下午4:19:57
	 * @param sourceMembers
	 * @param businessId 用于写入SourceBO
	 * @param businessInfoType 用于写入SourceBO
	 * @return List<SourceBO>
	 */
	public List<SourceBO> obtainSource(List<GroupMemberPO> sourceMembers, String businessId, BusinessInfoType businessInfoType){
		
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
			List<String> bundleIds = new ArrayList<String>();
			
			if(groupMemberType.equals(GroupMemberType.MEMBER_USER)){
				
				List<TerminalBundleUserPermissionPO> ps = terminalBundleUserPermissionDao.findByUserIdAndTerminalId(originId, terminalId);
				//从terminalBundleId找到终端设备，确认类型，找到这里的编码器id
				for(TerminalBundleUserPermissionPO p : ps){
					Long terminalBundleId = p.getTerminalBundleId();
					TerminalBundlePO terminalBundlePO = terminalBundleDao.findOne(terminalBundleId);//后续优化成批量，缓存
					TerminalBundleType type = terminalBundlePO.getType();
					if(TerminalBundleType.ENCODER.equals(type) || TerminalBundleType.ENCODER_DECODER.equals(type)){
						String bundleId = p.getBundleId();
						bundleIds.add(bundleId);
					}
				}
			}else if(groupMemberType.equals(GroupMemberType.MEMBER_DEVICE)){
				bundleIds.add(originId);
			}
			//查出bundle
			List<BundlePO> srcBundlePOs = resourceBundleDao.findByBundleIds(bundleIds);
			//查出编码1通道
			List<ChannelSchemeDTO> videoEncode1Channels = resourceChannelDao.findByBundleIdsAndChannelId(bundleIds, ChannelType.VIDEOENCODE1.getChannelId());
			if(videoEncode1Channels == null) videoEncode1Channels = new ArrayList<ChannelSchemeDTO>();
			
			List<ChannelSchemeDTO> audioEncode1Channels = resourceChannelDao.findByBundleIdsAndChannelId(bundleIds, ChannelType.AUDIOENCODE1.getChannelId());
			if(audioEncode1Channels == null) audioEncode1Channels = new ArrayList<ChannelSchemeDTO>();
			
//					List<ChannelSchemeDTO> srcChannels = new ArrayList<ChannelSchemeDTO>();
//			srcChannels = new ArrayListWrapper<ChannelSchemeDTO>().addAll(videoEncode1Channels).addAll(audioEncode1Channels).getList();
			
			//遍历视频通道，找到对应的音频通道					
			for(ChannelSchemeDTO videoEncode1Channel : videoEncode1Channels){
				BundlePO videoBundle = queryUtil.queryBundlePOByBundleId(srcBundlePOs, videoEncode1Channel.getBundleId());
				SourceBO sourceBO = new SourceBO()
						.setBusinessId(businessId)
						.setBusinessInfoType(businessInfoType)
						.setMemberId(member.getId())
						.setSrcName(member.getName())
						.setSrcCode(member.getCode())
						.setVideoSource(videoEncode1Channel)
						.setVideoBundle(videoBundle);
				String bundleId = videoEncode1Channel.getBundleId();
				List<ChannelSchemeDTO> audios = queryUtil.queryChannelDTOsByBundleId(audioEncode1Channels, bundleId);
				if(audios.size() > 0){
//					videoAudioMap.put(videoEncode1Channel, audios.get(0));
					BundlePO audioBundle = queryUtil.queryBundlePOByBundleId(srcBundlePOs, audios.get(0).getBundleId());
					sourceBO.setAudioSource(audios.get(0)).setAudioBundle(audioBundle);
				}else{
//					videoAudioMap.put(videoEncode1Channel, null);
				}
				sourceBOs.add(sourceBO);
			}
		}
		return sourceBOs;
	}

	public List<CommonForwardPO> obtainCommonForwardsFromSource(List<GroupMemberPO> dstMembers, List<SourceBO> sourceBOs){
		List<CommonForwardPO> forwards = new ArrayList<CommonForwardPO>();
		for(GroupMemberPO dstMember : dstMembers){
			if(!dstMember.getGroupMemberStatus().equals(GroupMemberStatus.CONNECT)){
				continue;
			}
			String originId = dstMember.getOriginId();
			Long terminalId = dstMember.getTerminalId();
			
			PageInfoPO pageInfo = pageInfoDao.findByOriginIdAndTerminalId(originId, terminalId);
			if(pageInfo == null) pageInfo = new PageInfoPO(originId, terminalId);
			
//			List<PageTaskBO> newTasks = new ArrayList<PageTaskBO>();
			for(SourceBO sourceBO : sourceBOs){
				
				//过滤掉自己看自己
				if(sourceBO.getMemberId().equals(dstMember.getId())) continue;
				
				ChannelSchemeDTO video = sourceBO.getVideoSource();
				BundlePO bundlePO = bundleDao.findByBundleId(video.getBundleId());
				CommonForwardPO videoForward = new CommonForwardPO();
				videoForward.setBusinessId(sourceBO.getBusinessId());
				videoForward.setBusinessInfoType(sourceBO.getBusinessInfoType());
				videoForward.setType(AgendaForwardType.VIDEO);
				videoForward.setDstMemberId(dstMember.getId());
				videoForward.setSrcId(bundlePO.getBundleId());
				videoForward.setSrcName(sourceBO.getSrcName());
				videoForward.setSrcCode(bundlePO.getUsername());
				videoForward.setSrcBundleName(bundlePO.getBundleName());
				videoForward.setSrcBundleType(bundlePO.getBundleType());
				videoForward.setSrcBundleId(bundlePO.getBundleId());
				videoForward.setSrcLayerId(bundlePO.getAccessNodeUid());
				videoForward.setSrcChannelId(video.getChannelId());
				videoForward.setSrcBaseType(video.getBaseType());
				videoForward.setSrcChannelName(video.getChannelName());
				videoForward.setDstMemberId(dstMember.getId());
//				videoForward.setDstDeviceType(DstDeviceType.DEVICE);
				//TODO:set srcName relativeUuid businessType businessId dstId DstDeviceType
				forwards.add(videoForward);
				ChannelSchemeDTO audio = sourceBO.getAudioSource();
				if(audio != null){
					BundlePO bundlePO2 = bundleDao.findByBundleId(video.getBundleId());
					CommonForwardPO audioForward = new CommonForwardPO();
					audioForward.setBusinessId(sourceBO.getBusinessId());
					audioForward.setBusinessInfoType(sourceBO.getBusinessInfoType());
					audioForward.setType(AgendaForwardType.AUDIO);
					audioForward.setDstMemberId(dstMember.getId());
					audioForward.setSrcId(bundlePO2.getBundleId());
					audioForward.setSrcName(sourceBO.getSrcName());
					audioForward.setSrcCode(bundlePO2.getUsername());
					audioForward.setSrcBundleName(bundlePO2.getBundleName());
					audioForward.setSrcBundleType(bundlePO2.getBundleType());
					audioForward.setSrcBundleId(bundlePO2.getBundleId());
					audioForward.setSrcLayerId(bundlePO2.getAccessNodeUid());
					audioForward.setSrcChannelId(audio.getChannelId());
					audioForward.setSrcBaseType(audio.getBaseType());
					audioForward.setSrcChannelName(audio.getChannelName());
					audioForward.setDstMemberId(dstMember.getId());
//					audioForward.setDstDeviceType(DstDeviceType.DEVICE);
					//TODO:set srcName businessId dstId DstDeviceType
					//TODO:set
					videoForward.setRelativeUuid(audioForward.getUuid());
					audioForward.setRelativeUuid(videoForward.getUuid());
					forwards.add(audioForward);
				}
				//TODO:校验该转发是否已经存在
				//TODO:分别校验音、视频的转发是否能执行
//				PageTaskBO taskBO = new PageTaskBO();
				//TODO:set
//				newTasks.add(taskBO);
			}
//			pageTaskService.addAndRemoveTasks(pageInfo, newTasks, null);
			
			//TODO:持久化CommonForwardPO
		}
		
		return forwards;
	}

	public List<CommonForwardPO> obtainCommonForwards(AgendaPO agenda, Long groupId){
		
		GroupPO group = groupDao.findOne(groupId);
		GroupStatus groupStatus = group.getStatus();
		
		List<CommonForwardPO> result = new ArrayList<CommonForwardPO>();
		
		//查找该议程的转发
		List<AgendaForwardPO> agendaForwards = agendaForwardDao.findByAgendaId(agenda.getId());
		
		//遍历转发
		for(AgendaForwardPO agendaForward : agendaForwards){
			
			if(agendaForward.getType().equals(AgendaForwardType.AUDIO)) continue;
			
			//确认源（可能多个）
			List<SourceBO> sourceBOs = new ArrayList<SourceBO>();
			AgendaSourceType sourceType = agendaForward.getSourceType();
			String sourceId = agendaForward.getSourceId();
			switch (sourceType) {
			case ROLE:
				RolePO sourceRole = roleDao.findOne(Long.valueOf(sourceId));
				List<GroupMemberPO> sourceMembers = groupMemberDao.findByGroupIdAndRoleId(groupId, sourceRole.getId());
				BusinessInfoType businessInfoType = agendaForward.getBusinessInfoType();
				if(businessInfoType == null){
					businessInfoType = agenda.getBusinessInfoType();
				}
				if(businessInfoType == null){
					//TODO:根据业务组类型确定
				}
				
				//这里直接获取了音视频，没有根据议程下的转发来获取。后续完善：查询该视频转发是否有对应的音频，如果没有则不生成音频源
				sourceBOs = obtainSource(sourceMembers, groupId.toString(), businessInfoType);
				
				break;
				
			case ROLE_CHANNEL:
				RoleChannelPO sourceRoleChannel = roleChannelDao.findOne(Long.valueOf(sourceId));
				
				
				break;
			default:
				break;
			}
			
			
			//确认目的
			AgendaDestinationType destinationType = agendaForward.getDestinationType();
			String destinationId = agendaForward.getDestinationId();
			List<CommonForwardPO> forwards = new ArrayList<CommonForwardPO>();
			switch (destinationType) {
			case ROLE:
				RolePO dstRole = roleDao.findOne(Long.valueOf(destinationId));
				List<GroupMemberPO> dstMembers = groupMemberDao.findByGroupIdAndRoleId(groupId, dstRole.getId());
				//从源和目的生成转发
				forwards = obtainCommonForwardsFromSource(dstMembers, sourceBOs);
				result.addAll(forwards);
				
				break;
			default:
				break;
			}
		}
		
		//对于暂停的会议，将转发状态置为UNDONE。后续优化拓展到静默、专向业务
		if(GroupStatus.PAUSE.equals(groupStatus)){
			for(CommonForwardPO forward : result){
				forward.setExecuteStatus(ExecuteStatus.UNDONE);
			}
		}
		
		return result;
	}
	
	public void modifyMemberRole(Long groupId, Long memberId, List<Long> addRoleIds, List<Long> removeRoleIds) throws Exception{
		
		if(addRoleIds == null) addRoleIds = new ArrayList<Long>();
		if(removeRoleIds == null) removeRoleIds = new ArrayList<Long>();
		
		GroupMemberPO member = groupMemberDao.findOne(memberId);
//		List<GroupMemberPO> members = groupMemberDao.findByGroupId(groupId);
		
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
		
		//得到最终成员角色状态。TODO:先校验是否已经有角色，也就是判重
		List<RolePO> addRoles = roleDao.findAll(addRoleIds);
		List<GroupMemberRolePermissionPO> ps = new ArrayList<GroupMemberRolePermissionPO>();
		for(RolePO addRole : addRoles){
			GroupMemberRolePermissionPO memberRolePermission = new GroupMemberRolePermissionPO(addRole.getId(), member.getId());
			ps.add(memberRolePermission);
		}
		groupMemberRolePermissionDao.save(ps);
		groupMemberRolePermissionDao.deleteByRoleIdInAndGroupMemberId(removeRoleIds, memberId);
				
		executeToFinal(groupId, oldForwards);
		
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
			List<String> videoForwardUuids = new ArrayList<String>();
			for(CommonForwardPO removeForward : removeForwards){
				videoForwardUuids.add(removeForward.getUuid());
			}
			List<PageTaskPO> removeTasks =  pageTaskDao.findByVideoForwardUuidIn(videoForwardUuids);
			
			String originId = member.getOriginId();
			Long terminalId = member.getTerminalId();		
			PageInfoPO pageInfo = pageInfoDao.findByOriginIdAndTerminalId(originId, terminalId);
			
			pageTaskService.addAndRemoveTasks(pageInfo, newTasks, removeTasks);
		}
		
	}
	
	private void executeToFinal(Long groupId, List<CommonForwardPO> oldForwards) throws Exception{
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
		
		
		
		//删除的从分页任务中删除
		List<String> videoForwardUuids = new ArrayList<String>();
		for(CommonForwardPO removeForward : removeForwards){
			videoForwardUuids.add(removeForward.getUuid());
		}
		//所有需要删除的分页任务
		List<PageTaskPO> removeTasks =  pageTaskDao.findByVideoForwardUuidIn(videoForwardUuids);
		
		
		
		
		//按照成员来划分分页
		List<GroupMemberPO> members = groupMemberDao.findByGroupId(groupId);
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
			PageInfoPO pageInfo = pageInfoDao.findByOriginIdAndTerminalId(originId, terminalId);
			
			pageTaskService.addAndRemoveTasks(pageInfo, newTasks, thisRemoveTasks);
		}
		
		//持久化 oldForwards不需要吧？
		commonForwardDao.save(addForwards);
		commonForwardDao.deleteInBatch(removeForwards);		
	}

	/*
	private List<PageTaskBO> forwardToPageTask(List<CommonForwardPO> forwards){
		List<PageTaskBO> pageTaskBOs = new ArrayList<PageTaskBO>();
		for(CommonForwardPO forward : forwards){
			if(forward.getType().equals(AgendaForwardType.AUDIO)) continue;			
			String audioUuid = forward.getRelativeUuid();
//			if(audioUuid != null){
//				CommonForwardPO audioForward = tetrisBvcQueryUtil.queryForwardByUuid(forwards, audioUuid);
//			}
			
			PageTaskBO task = new PageTaskBO();
			task.setBusinessInfoType(forward.getBusinessInfoType());
			task.setBusinessId(forward.getBusinessId());
			task.setVideoForwardUuid(forward.getUuid());
			task.setAudioForwardUuid(audioUuid);
		}
		return pageTaskBOs;
	}*/

	private List<PageTaskPO> forwardToPageTaskPO(List<CommonForwardPO> forwards){
		List<PageTaskPO> pageTaskPOs = new ArrayList<PageTaskPO>();
		for(CommonForwardPO forward : forwards){
			if(forward.getType().equals(AgendaForwardType.AUDIO)) continue;			
			String audioUuid = forward.getRelativeUuid();
			
			PageTaskPO task = new PageTaskPO();
			task.setBusinessInfoType(forward.getBusinessInfoType());
			task.setBusinessId(forward.getBusinessId());
			task.setBusinessName(forward.getSrcName());//TODO:添加业务描述
			task.setSrcVideoId(forward.getSrcId());
			task.setSrcVideoBundleId(forward.getSrcBundleId());
			task.setSrcVideoLayerId(forward.getSrcLayerId());
			task.setSrcVideoChannelId(forward.getSrcChannelId());
			task.setSrcVideoName(forward.getSrcName());
			task.setSrcVideoCode(forward.getSrcCode());
			task.setVideoStatus(forward.getExecuteStatus());
			task.setDstMemberId(forward.getDstMemberId());
			task.setVideoForwardUuid(forward.getUuid());
			if(audioUuid != null){
				CommonForwardPO audioForward = tetrisBvcQueryUtil.queryForwardByUuid(forwards, audioUuid);
				task.setSrcAudioName(audioForward.getSrcName());
				task.setSrcAudioId(audioForward.getSrcId());
				task.setSrcAudioBundleId(audioForward.getSrcBundleId());
				task.setSrcAudioLayerId(audioForward.getSrcLayerId());
				task.setSrcAudioChannelId(audioForward.getSrcChannelId());				
				task.setSrcAudioCode(audioForward.getSrcCode());
				task.setAudioStatus(audioForward.getExecuteStatus());
				task.setAudioForwardUuid(audioUuid);		
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

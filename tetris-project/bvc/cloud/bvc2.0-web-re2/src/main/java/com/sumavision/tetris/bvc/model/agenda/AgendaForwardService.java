package com.sumavision.tetris.bvc.model.agenda;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.sumavision.bvc.command.group.dao.AgendaForwardDemandDAO;
import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.bvc.business.bo.BusinessDeliverBO;
import com.sumavision.tetris.bvc.business.dao.BusinessCombineVideoDAO;
import com.sumavision.tetris.bvc.business.dao.BusinessGroupMemberDAO;
import com.sumavision.tetris.bvc.business.dao.CombineTemplateGroupAgendeForwardPermissionDAO;
import com.sumavision.tetris.bvc.business.dao.GroupDAO;
import com.sumavision.tetris.bvc.business.dao.RunningAgendaDAO;
import com.sumavision.tetris.bvc.business.deliver.DeliverExecuteService;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.bvc.business.group.GroupStatus;
import com.sumavision.tetris.bvc.business.group.RunningAgendaPO;
import com.sumavision.tetris.bvc.business.group.combine.video.CombineVideoService;
import com.sumavision.tetris.bvc.business.po.combine.video.BusinessCombineVideoPO;
import com.sumavision.tetris.bvc.business.po.combine.video.CombineTemplateGroupAgendeForwardPermissionPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberTerminalChannelPO;
import com.sumavision.tetris.bvc.business.po.member.dao.BusinessGroupMemberTerminalChannelDAO;
import com.sumavision.tetris.bvc.model.layout.LayoutDAO;
import com.sumavision.tetris.bvc.model.layout.LayoutPO;
import com.sumavision.tetris.bvc.model.layout.LayoutPositionDAO;
import com.sumavision.tetris.bvc.model.layout.LayoutPositionPO;
import com.sumavision.tetris.bvc.model.layout.LayoutPositionVO;
import com.sumavision.tetris.bvc.model.layout.LayoutVO;
import com.sumavision.tetris.bvc.model.layout.forward.CombineTemplatePositionDAO;
import com.sumavision.tetris.bvc.model.layout.forward.CombineTemplatePositionPO;
import com.sumavision.tetris.bvc.model.layout.forward.LayoutForwardPO;
import com.sumavision.tetris.bvc.model.role.RoleChannelDAO;
import com.sumavision.tetris.bvc.model.role.RoleChannelPO;
import com.sumavision.tetris.bvc.model.role.RoleDAO;
import com.sumavision.tetris.bvc.model.role.RolePO;
import com.sumavision.tetris.bvc.model.terminal.TerminalDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalPO;
import com.sumavision.tetris.bvc.model.terminal.TerminalType;
import com.sumavision.tetris.bvc.util.AgendaRoleMemberUtil;
import com.sumavision.tetris.bvc.util.TetrisBvcQueryUtil;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashSetWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Service
public class AgendaForwardService {

	@Autowired
	private BusinessCombineVideoDAO combineVideoDao;

	@Autowired
	private CombineTemplatePositionDAO combineTemplatePositionDao;

	@Autowired
	private AgendaDAO agendaDao;
	
	@Autowired
	private AgendaExecuteService agendaExecuteService;
	
	@Autowired
	private AgendaForwardDemandDAO agendaForwardDemandDao;
	
	@Autowired
	private GroupDAO groupDao;
	
	@Autowired
	private AgendaForwardDAO agendaForwardDao;
	
	@Autowired
	private AgendaForwardSourceDAO agendaForwardSourceDao;
	
	@Autowired
	private AgendaForwardDestinationDAO agendaForwardDestinationDao;
	
	@Autowired
	private CustomAudioDAO customAudioDao;
	
	@Autowired
	private RoleDAO roleDao;
	
	@Autowired
	private RoleChannelDAO roleChannelDao;
	
	@Autowired
	private LayoutDAO layoutDao;
	
	@Autowired
	private LayoutPositionDAO layoutPositionDao;
	
	@Autowired
	private LayoutScopeDAO layoutScopeDao;
	
	@Autowired
	private BusinessGroupMemberTerminalChannelDAO businessGroupMemberTerminalChannelDao;
	
	@Autowired
	private CombineTemplateGroupAgendeForwardPermissionDAO combineTemplateGroupAgendeForwardPermissionDao;
	
	@Autowired
	private BusinessGroupMemberDAO businessGroupMemberDao;
	
	@Autowired
	private RunningAgendaDAO runningAgendaDao;
	
	@Autowired
	private AgendaRoleMemberUtil agendaRoleMemberUtil;
	
	@Autowired
	private TerminalDAO terminalDao;
	
	@Autowired
	private TetrisBvcQueryUtil tetrisBvcQueryUtil;
	
	@Autowired
	private CombineVideoService combineVideoService;
	
	@Autowired
	private DeliverExecuteService deliverExecuteService;
	
	/**
	 * 添加内置议程转发<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月22日 下午3:34:35
	 * @param Long agendaId 议程id
	 * @return AgendaForwardVO 议程转发
	 */
	@Transactional(rollbackFor = Exception.class)
	public AgendaForwardVO addInternal(Long agendaId) throws Exception{
		AgendaPO agenda = agendaDao.findOne(agendaId);
		AgendaForwardPO agendaForward = new AgendaForwardPO();
		agendaForward.setAgendaId(agendaId);
		agendaForward.setLayoutSelectionType(LayoutSelectionType.ADAPTABLE);
		if(!agenda.getGlobalCustomAudio()){
			agendaForward.setAudioType(AudioType.CUSTOM);
			agendaForward.setVolume(50);
		}
		agendaForwardDao.save(agendaForward);
		return new AgendaForwardVO().set(agendaForward);
	}
	
	/**
	 * 添加自定义议程转发<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月27日 下午5:04:55
	 * @param String name 议程转发名称
	 * @param Long agendaId 议程id
	 * @return AgendaForwardVO 议程转发
	 */
	@Transactional(rollbackFor = Exception.class)
	public AgendaForwardVO addCustom(
			String name,
			Long agendaId) throws Exception{
		AgendaPO agenda = agendaDao.findOne(agendaId);
		AgendaForwardPO agendaForward = new AgendaForwardPO();
		agendaForward.setName(name);
		agendaForward.setAgendaId(agendaId);
		agendaForward.setLayoutSelectionType(LayoutSelectionType.CONFIRM);
		if(!agenda.getGlobalCustomAudio()){
			agendaForward.setAudioType(AudioType.CUSTOM);
			agendaForward.setVolume(50);
		}
		List<LayoutPO> layoutEntities = layoutDao.findAll();
		if(layoutEntities!=null && layoutEntities.size()>0){
			agendaForward.setLayoutId(layoutEntities.get(0).getId());
		}
		agendaForwardDao.save(agendaForward);
		return new AgendaForwardVO().set(agendaForward);
	}
	
	/**
	 * 修改自定义转发名称<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月29日 下午3:30:26
	 * @param Long id 转发id
	 * @param String name 自定义转发名称
	 */
	@Transactional(rollbackFor = Exception.class)
	public void editCustom(
			Long id,
			String name) throws Exception{
		AgendaForwardPO agendaForward = agendaForwardDao.findOne(id);
		if(agendaForward != null){
			agendaForward.setName(name);
			agendaForwardDao.save(agendaForward);
		}
	}
	
	/**
	 * 删除议程转发<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月23日 下午5:34:53
	 * @param Long id 议程转发id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void remove(Long id) throws Exception{
		AgendaForwardPO agendaForward = agendaForwardDao.findOne(id);
		if(agendaForward != null){
			agendaForwardDao.delete(agendaForward);
		}
		
		List<Long> agendaForwardIds = new ArrayListWrapper<Long>().add(id).getList();
		
		List<AgendaForwardSourcePO> agendaForwardSourceEntities = agendaForwardSourceDao.findByAgendaForwardIdIn(agendaForwardIds);
		if(agendaForwardSourceEntities!=null && agendaForwardSourceEntities.size()>0){
			agendaForwardSourceDao.deleteInBatch(agendaForwardSourceEntities);
		}
		List<AgendaForwardDestinationPO> agendaForwardDestinationEntities = agendaForwardDestinationDao.findByAgendaForwardIdIn(agendaForwardIds);
		if(agendaForwardDestinationEntities!=null && agendaForwardDestinationEntities.size()>0){
			agendaForwardDestinationDao.deleteInBatch(agendaForwardDestinationEntities);
		}
		List<LayoutScopePO> layoutScopeEntities = layoutScopeDao.findByAgendaForwardIdIn(agendaForwardIds);
		if(layoutScopeEntities!=null && layoutScopeEntities.size()>0){
			layoutScopeDao.deleteInBatch(layoutScopeEntities);
		}
		List<CustomAudioPO> customAudioEntities = customAudioDao.findByPermissionIdInAndPermissionType(agendaForwardIds, CustomAudioPermissionType.AGENDA_FORWARD);
		if(customAudioEntities!=null && customAudioEntities.size()>0){
			customAudioDao.deleteInBatch(customAudioEntities);
		}
	}
	
	/**
	 * 修改音频类型<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月22日 下午5:16:12
	 * @param Long id 内置议程转发id
	 * @param String audioType 音频类型
	 */
	@Transactional(rollbackFor = Exception.class)
	public void handleAudioTypeChange(
			Long id,
			String audioType) throws Exception{
		AgendaForwardPO agendaForward = agendaForwardDao.findOne(id);
		agendaForward.setAudioType(AudioType.valueOf(audioType));
		agendaForwardDao.save(agendaForward);
		if(!AudioType.CUSTOM.equals(agendaForward.getAudioType())){
			List<CustomAudioPO> customAudios = customAudioDao.findByPermissionIdInAndPermissionType(new ArrayListWrapper<Long>().add(agendaForward.getId()).getList(), CustomAudioPermissionType.AGENDA_FORWARD);
			if(customAudios!=null && customAudios.size()>0){
				customAudioDao.deleteInBatch(customAudios);
			}
		}
	}
	
	/**
	 * 修改音量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月22日 下午5:25:44
	 * @param Long id 议程转发id
	 * @param Integer volume 音量
	 */
	@Transactional(rollbackFor = Exception.class)
	public void handleVolumeChange(
			Long id,
			Integer volume) throws Exception{
		AgendaForwardPO agendaForward = agendaForwardDao.findOne(id);
		agendaForward.setVolume(volume);
		agendaForwardDao.save(agendaForward);
	}
	
	/**
	 * 添加内置议程转发源<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月22日 下午6:36:08
	 * @param Long agendaForwardId 内置议程转发id
	 * @param JSONString roleChannelIds 角色通道id列表
	 * @return List<AgendaForwardSourceVO> 内置议程转发源列表
	 */
	@Transactional(rollbackFor = Exception.class)
	public List<AgendaForwardSourceVO> addInternalSource(
			Long agendaForwardId,
			String roleChannelIds) throws Exception{
		
		List<Long> parsedRoleChannelIds = JSON.parseArray(roleChannelIds, Long.class);
		List<RoleChannelPO> roleChannelEntities = roleChannelDao.findAll(parsedRoleChannelIds);
		Set<Long> roleIds = new HashSetWrapper<Long>().add(-1l).getSet();
		if(roleChannelEntities!=null &&roleChannelEntities.size()>0){
			for(RoleChannelPO roleChannel:roleChannelEntities){
				roleIds.add(roleChannel.getRoleId());
			}
		}
		List<RolePO> roleEntities = roleDao.findAll(roleIds);
		
		List<AgendaForwardSourcePO> agendaForwardSourceEntities = new ArrayList<AgendaForwardSourcePO>();
		for(Long roleChannelId:parsedRoleChannelIds){
			AgendaForwardSourcePO agendaForwardSourceEntity = new AgendaForwardSourcePO();
			agendaForwardSourceEntity.setSourceId(roleChannelId);
			agendaForwardSourceEntity.setSourceType(SourceType.ROLE_CHANNEL);
			agendaForwardSourceEntity.setLayoutPositionSelectionType(LayoutPositionSelectionType.AUTO_INCREMENT);
			agendaForwardSourceEntity.setIsLoop(false);
			agendaForwardSourceEntity.setUpdateTime(new Date());
			agendaForwardSourceEntity.setAgendaForwardId(agendaForwardId);
			agendaForwardSourceEntities.add(agendaForwardSourceEntity);
		}
		agendaForwardSourceDao.save(agendaForwardSourceEntities);
		
		List<AgendaForwardSourceVO> sources = new ArrayList<AgendaForwardSourceVO>();
		
		for(AgendaForwardSourcePO agendaForwardSourceEntity:agendaForwardSourceEntities){
			AgendaForwardSourceVO agendaForwardSource = new AgendaForwardSourceVO().set(agendaForwardSourceEntity);
			if(roleChannelEntities!=null && 
				roleChannelEntities.size()>0 &&
				roleEntities!=null &&
				roleEntities.size()>0){
				for(RoleChannelPO roleChannel:roleChannelEntities){
					if(roleChannel.getId().equals(agendaForwardSourceEntity.getSourceId())){
						for(RolePO role:roleEntities){
							if(roleChannel.getRoleId().equals(role.getId())){
								agendaForwardSource.setSourceName(new StringBufferWrapper().append(role.getName()).append(" - ").append(roleChannel.getName()).toString());
								break;
							}
						}
						break;
					}
				}
			}
			sources.add(agendaForwardSource);
		}
		
		return sources;
	}
	
	/**
	 * 添加自定义议程转发源<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月30日 下午4:14:34
	 * @param Long agendaForwardId 议程转发id
	 * @param JSONString sources 源列表 {id:"", type:""}
	 * @param Integer serialNum 分屏序号
	 * @param Boolean isLoop 是否轮询
	 * @param Integer loopTime 轮询时间
	 * @return List<AgendaForwardSourceVO> 转发源列表
	 */
	@Transactional(rollbackFor = Exception.class)
	public List<AgendaForwardSourceVO> addCustomSource(
			Long agendaForwardId,
			String sources,
			Integer serialNum,
			Boolean isLoop,
			Integer loopTime) throws Exception{
		
		List<AgendaForwardSourcePO> existSources = agendaForwardSourceDao.findByAgendaForwardIdAndSerialNum(agendaForwardId, serialNum);
		if(existSources!=null && existSources.size()>0){
			agendaForwardSourceDao.deleteInBatch(existSources);
		}
		
		JSONArray parsedSources = JSON.parseArray(sources);
		List<AgendaForwardSourcePO> agendaForwardSourceEntities = new ArrayList<AgendaForwardSourcePO>();
		for(int i=0; i<parsedSources.size(); i++){
			AgendaForwardSourcePO agendaForwardSourceEntity = new AgendaForwardSourcePO();
			agendaForwardSourceEntity.setAgendaForwardId(agendaForwardId);
			agendaForwardSourceEntity.setSourceId(parsedSources.getJSONObject(i).getLong("id"));
			agendaForwardSourceEntity.setSourceType(SourceType.valueOf(parsedSources.getJSONObject(i).getString("type")));
			agendaForwardSourceEntity.setSerialNum(serialNum);
			agendaForwardSourceEntity.setLayoutPositionSelectionType(LayoutPositionSelectionType.CONFIRM);
			agendaForwardSourceEntity.setIsLoop(isLoop);
			if(isLoop){
				agendaForwardSourceEntity.setLoopTime(loopTime);
			}else{
				agendaForwardSourceEntity.setLoopTime(null);
			}
			agendaForwardSourceEntities.add(agendaForwardSourceEntity);
		}
		agendaForwardSourceDao.save(agendaForwardSourceEntities);
		List<AgendaForwardSourceVO> agendaForwardSources = AgendaForwardSourceVO.getConverter(AgendaForwardSourceVO.class).convert(agendaForwardSourceEntities, AgendaForwardSourceVO.class);
		
		List<Long> roleChannelIds = new ArrayListWrapper<Long>().add(-1l).getList();
		List<Long> groupMemberChannelIds = new ArrayListWrapper<Long>().add(-1l).getList();
		if(agendaForwardSources!=null && agendaForwardSources.size()>0){
			for(AgendaForwardSourceVO agendaForwardSource:agendaForwardSources){
				if(agendaForwardSource.getSourceType().equals(SourceType.ROLE_CHANNEL.toString())){
					roleChannelIds.add(agendaForwardSource.getSourceId());
				}else if(agendaForwardSource.getSourceType().equals(SourceType.GROUP_MEMBER_CHANNEL.toString())){
					groupMemberChannelIds.add(agendaForwardSource.getSourceId());
				}
			}
		}
		
		List<RoleChannelPO> roleChannelEntities = roleChannelDao.findAll(roleChannelIds);
		Set<Long> roleIds = new HashSet<Long>();
		for(RoleChannelPO roleChannelEntity:roleChannelEntities){
			roleIds.add(roleChannelEntity.getRoleId());
		}
		List<RolePO> roleEntities = roleDao.findAll(roleIds);
		
		List<BusinessGroupMemberTerminalChannelPO> memberChannels = businessGroupMemberTerminalChannelDao.findAll(groupMemberChannelIds);
		List<Object[]> originMemberIdMaps = businessGroupMemberTerminalChannelDao.findGroupMemberIdByIdIn(groupMemberChannelIds);
		Map<Long, Long> memberIdMaps = null;
		if(originMemberIdMaps!=null && originMemberIdMaps.size()>0){
			memberIdMaps = new HashMap<Long, Long>();
			for(Object[] originMemberIdMap:originMemberIdMaps){
				memberIdMaps.put(Long.valueOf(originMemberIdMap[0].toString()), Long.valueOf(originMemberIdMap[1].toString()));
			}
		}
		List<BusinessGroupMemberPO> members = ((memberIdMaps==null||memberIdMaps.size()<=0)?null:businessGroupMemberDao.findAll(memberIdMaps.values()));
		
		for(AgendaForwardSourceVO agendaForwardSource:agendaForwardSources){
			if(agendaForwardSource.getSourceType().equals(SourceType.ROLE_CHANNEL.toString())){
				for(RoleChannelPO roleChannelEntity:roleChannelEntities){
					if(roleChannelEntity.getId().equals(agendaForwardSource.getSourceId())){
						for(RolePO roleEntity:roleEntities){
							if(roleEntity.getId().equals(roleChannelEntity.getRoleId())){
								agendaForwardSource.setSourceName(new StringBufferWrapper().append(roleEntity.getName()).append(" - ").append(roleChannelEntity.getName()).toString());
								break;
							}
						}
						break;
					}
				}
			}else if(agendaForwardSource.getSourceType().equals(SourceType.GROUP_MEMBER_CHANNEL.toString())){
				for(BusinessGroupMemberTerminalChannelPO memberChannel:memberChannels){
					if(memberChannel.getId().equals(agendaForwardSource.getSourceId())){
						for(BusinessGroupMemberPO member:members){
							if(member.getId().equals(memberIdMaps.get(memberChannel.getId()))){
								agendaForwardSource.setSourceName(new StringBufferWrapper().append(member.getName()).append(" - ").append(memberChannel.getTerminalChannelname()).toString());
								break;
							}
						}
						break;
					}
				}
			}else if(agendaForwardSource.getSourceType().equals(SourceType.COMBINE_VIDEO_VIRTUAL_SOURCE.toString())){
				AgendaPO agenda = agendaDao.findOne(agendaForwardSource.getSourceId());//这个agenda是虚拟源
				agendaForwardSource.setSourceName(new StringBufferWrapper().append("虚拟源").append(" - ").append(agenda.getName()).toString());				
			}
			
		}
		
		//如果正在运行此线程，重新执行议程
		AgendaForwardPO agendaForward = agendaForwardDao.findOne(agendaForwardId);
		if(agendaForward == null){
			throw new BaseException(StatusCode.FORBIDDEN, "没有找到该议程转发");
		}		
		RunningAgendaPO runningAgenda = runningAgendaDao.findByAgendaId(agendaForward.getAgendaId());
		if(runningAgenda != null){
			agendaExecuteService.runAgenda(runningAgenda.getGroupId(), runningAgenda.getAgendaId(), null, true);
		}
		
		//如果这是合屏虚拟源，且能找到合屏，则更新合屏
		updateVirtualCombineVideo(agendaForward);
		
		return agendaForwardSources;
	}
	
	/**
	 * 删除音频转发源<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月23日 上午9:56:58
	 * @param Long id 音频转发源id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void removeSource(Long id) throws Exception{
		AgendaForwardSourcePO agendaForwardSource = agendaForwardSourceDao.findOne(id);
		if(agendaForwardSource != null){
			agendaForwardSourceDao.delete(agendaForwardSource);
		}
	}
	
	/**
	 * 删除自定义议程转发源<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月1日 上午10:20:50
	 * @param Long agendaForwardId 议程转发id
	 * @param Integer serialNum 源序号
	 */
	@Transactional(rollbackFor = Exception.class)
	public void removeCustomSource(
			Long agendaForwardId,
			Integer serialNum) throws Exception{
		List<AgendaForwardSourcePO> agendaForwardSourceEntities = agendaForwardSourceDao.findByAgendaForwardIdAndSerialNum(agendaForwardId, serialNum);
		if(agendaForwardSourceEntities!=null && agendaForwardSourceEntities.size()>0){
			agendaForwardSourceDao.deleteInBatch(agendaForwardSourceEntities);
		}
		
		//如果正在运行此线程，重新执行议程
		AgendaForwardPO agendaForward = agendaForwardDao.findOne(agendaForwardId);
		if(agendaForward == null){
			throw new BaseException(StatusCode.FORBIDDEN, "没有找到该议程转发");
		}		
		RunningAgendaPO runningAgenda = runningAgendaDao.findByAgendaId(agendaForward.getAgendaId());
		if(runningAgenda != null){
			agendaExecuteService.runAgenda(runningAgenda.getGroupId(), runningAgenda.getAgendaId(), null, true);
		}
		
		//如果这是合屏虚拟源，且能找到合屏，则更新合屏
		updateVirtualCombineVideo(agendaForward);
	}

	
	private void updateVirtualCombineVideo(AgendaForwardPO agendaForward) throws Exception{
		
		//如果这是合屏虚拟源，且能找到合屏，则更新合屏
		AgendaPO agenda = agendaDao.findOne(agendaForward.getAgendaId());
		if(BusinessInfoType.COMBINE_VIDEO_VIRTUAL_SOURCE.equals(agenda.getBusinessInfoType())){
			GroupPO group = groupDao.findOne(agenda.getBusinessId());
			if(group.getStatus().equals(GroupStatus.START)){
				List<LayoutForwardPO> layoutForwards = agendaRoleMemberUtil.obtainLayoutForwardsFromAgendaForward(agendaForward);
				TerminalPO tvosTerminal = terminalDao.findByType(TerminalType.ANDROID_TVOS);
				LayoutForwardPO layoutForward = tetrisBvcQueryUtil.queryLayoutForwardByTerminalId(layoutForwards, tvosTerminal.getId());
				Long combineTemplateId = layoutForward.getSourceId();
				CombineTemplateGroupAgendeForwardPermissionPO p = combineTemplateGroupAgendeForwardPermissionDao.
						findByGroupIdAndCombineTemplateIdAndAgendaForwardId(agenda.getBusinessId(), combineTemplateId, agendaForward.getId());
				if(p == null) return;
				BusinessCombineVideoPO combineVideo = p.getCombineVideo();
				List<CombineTemplatePositionPO> combineTemplatePositions = combineTemplatePositionDao.findByCombineTemplateIdIn(
						new ArrayListWrapper<Long>().add(p.getCombineTemplateId()).getList());
				List<AgendaForwardSourcePO> agendaForwardSources1 = agendaForwardSourceDao.findByAgendaForwardId(agendaForward.getId());

				BusinessDeliverBO businessDeliverBO = new BusinessDeliverBO().setGroup(group).setUserId(group.getUserId().toString());
				combineVideo = combineVideoService.templateCombineVideo(group, combineVideo, combineTemplatePositions, agendaForwardSources1, null, 3, businessDeliverBO);				
				combineVideoDao.save(combineVideo);
				
				//先放入“需要更新的合屏”，后边DeliverExecuteService.execute也可能被删掉
				businessDeliverBO.getUpdateCombineVideos().add(combineVideo);
				deliverExecuteService.execute(businessDeliverBO, agendaForward.getName() + " 合屏虚拟源修改，更新合屏", true);
			}
		}
	}

	/**
	 * 修改源与虚拟源布局序号对应方式<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月23日 下午6:09:31
	 * @param Long 议程转发源id
	 * @param String layoutPositionSelectionType 源与虚拟源布局序号对应方式
	 * @param serialNum 虚拟源布局序号
	 */
	@Transactional(rollbackFor = Exception.class)
	public void handleLayoutPositionSelectionTypeChange(
			Long id,
			String layoutPositionSelectionType,
			Integer serialNum,
			Boolean isLoop,
			Integer loopTime) throws Exception{
		AgendaForwardSourcePO agendaForwardSource = agendaForwardSourceDao.findOne(id);
		agendaForwardSource.setLayoutPositionSelectionType(LayoutPositionSelectionType.valueOf(layoutPositionSelectionType));
		if(agendaForwardSource.getLayoutPositionSelectionType().equals(LayoutPositionSelectionType.AUTO_INCREMENT)){
			agendaForwardSource.setSerialNum(null);
			agendaForwardSource.setIsLoop(false);
			agendaForwardSource.setLoopTime(null);
		}else{
			agendaForwardSource.setSerialNum(serialNum);
			agendaForwardSource.setIsLoop(isLoop);
			if(isLoop){
				agendaForwardSource.setLoopTime(loopTime);
			}else{
				agendaForwardSource.setLoopTime(null);
			}
		}
		agendaForwardSourceDao.save(agendaForwardSource);
	}
	
	/**
	 * 添加议程转发目的<br/>
	 * <p>同一个议程中同一转发目的只存在一个转发中</p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月23日 下午1:09:52
	 * @param Long agendaForwardId 内置议程转发id
	 * @param JSONString roleIds 角色id
	 * @param JSONString groupMemberIds 会议成员id列表
	 * @return List<AgendaForwardDestinationVO> 议程转发目的
	 */
	@Transactional(rollbackFor = Exception.class)
	public List<AgendaForwardDestinationVO> addDestination(
			Long agendaForwardId,
			String roleIds,
			String groupMemberIds) throws Exception{
		
		List<Long> parsedRoleIds = null;
		if(roleIds!=null && !"".equals(roleIds)) parsedRoleIds = JSON.parseArray(roleIds, Long.class);
		List<Long> parsedGroupMemberIds = null;
		if(groupMemberIds!=null && !"".equals(groupMemberIds)) parsedGroupMemberIds = JSON.parseArray(groupMemberIds, Long.class);
		
		List<RolePO> roleEntities = roleDao.findAll(new ArrayListWrapper<Long>().addAll(parsedRoleIds).add(-1l).getList());
		List<BusinessGroupMemberPO> members = null;
		if(parsedGroupMemberIds!=null && parsedGroupMemberIds.size()>0){
			members = businessGroupMemberDao.findAll(new ArrayListWrapper<Long>().addAll(parsedGroupMemberIds).add(-1l).getList());
		}
		
		AgendaForwardPO agendaForward = agendaForwardDao.findOne(agendaForwardId);
		List<AgendaForwardPO> agendaForwards = agendaForwardDao.findByAgendaId(agendaForward.getAgendaId());
		List<Long> agendaForwardIds = new ArrayList<Long>();
		for(AgendaForwardPO af:agendaForwards){
			agendaForwardIds.add(af.getId());
		}
		
		List<AgendaForwardDestinationPO> existAgendaForwardDestinations = new ArrayList<AgendaForwardDestinationPO>();
		if(parsedRoleIds!=null && parsedRoleIds.size()>0){
			List<AgendaForwardDestinationPO> existRoleDestinations = agendaForwardDestinationDao.findByAgendaForwardIdInAndDestinationIdInAndDestinationType(agendaForwardIds, parsedRoleIds, DestinationType.ROLE);
			if(existRoleDestinations!=null && existRoleDestinations.size()>0){
				List<Long> exitRoleIds = new ArrayList<Long>();
				existAgendaForwardDestinations.addAll(existRoleDestinations);
				for(AgendaForwardDestinationPO existRoleDestination:existRoleDestinations){
					exitRoleIds.add(existRoleDestination.getDestinationId());
				}
				parsedRoleIds.removeAll(exitRoleIds);
			}		
		}
		if(parsedGroupMemberIds!=null && parsedGroupMemberIds.size()>0){
			List<AgendaForwardDestinationPO> existGroupMemberDestinations = agendaForwardDestinationDao.findByAgendaForwardIdInAndDestinationIdInAndDestinationType(agendaForwardIds, parsedGroupMemberIds, DestinationType.GROUP_MEMBER);
			if(existGroupMemberDestinations!=null && existGroupMemberDestinations.size()>0){
				List<Long> existGroupMemberIds = new ArrayList<Long>();
				existAgendaForwardDestinations.addAll(existGroupMemberDestinations);
				for(AgendaForwardDestinationPO existGroupMemberDestination:existGroupMemberDestinations){
					existGroupMemberIds.add(existGroupMemberDestination.getDestinationId());
				}
				parsedGroupMemberIds.removeAll(existGroupMemberIds);
			}
		}
		
		if(existAgendaForwardDestinations.size() > 0){
			for(AgendaForwardDestinationPO existAgendaForwardDestination:existAgendaForwardDestinations){
				existAgendaForwardDestination.setAgendaForwardId(agendaForwardId);
			}
			agendaForwardDestinationDao.save(existAgendaForwardDestinations);
		}
		
		List<AgendaForwardDestinationPO> agendaForwardDestinationEntities = new ArrayList<AgendaForwardDestinationPO>();
		if(parsedRoleIds!=null && parsedRoleIds.size()>0){
			for(Long roleId:parsedRoleIds){
				AgendaForwardDestinationPO agendaForwardDestinationEntity = new AgendaForwardDestinationPO();
				agendaForwardDestinationEntity.setDestinationId(roleId);
				agendaForwardDestinationEntity.setDestinationType(DestinationType.ROLE);
				agendaForwardDestinationEntity.setAgendaForwardId(agendaForwardId);
				agendaForwardDestinationEntity.setUpdateTime(new Date());
				agendaForwardDestinationEntities.add(agendaForwardDestinationEntity);
			}
		}
		
		if(parsedGroupMemberIds!=null && parsedGroupMemberIds.size()>0){
			for(Long groupMemberId:parsedGroupMemberIds){
				AgendaForwardDestinationPO agendaForwardDestinationEntity = new AgendaForwardDestinationPO();
				agendaForwardDestinationEntity.setDestinationId(groupMemberId);
				agendaForwardDestinationEntity.setDestinationType(DestinationType.GROUP_MEMBER);
				agendaForwardDestinationEntity.setAgendaForwardId(agendaForwardId);
				agendaForwardDestinationEntity.setUpdateTime(new Date());
				agendaForwardDestinationEntities.add(agendaForwardDestinationEntity);
			}
		}
		
		agendaForwardDestinationDao.save(agendaForwardDestinationEntities);
		
		if(existAgendaForwardDestinations.size() > 0){
			agendaForwardDestinationEntities.addAll(existAgendaForwardDestinations);
		}
		
		List<AgendaForwardDestinationVO> agendaForwardDestinations = AgendaForwardDestinationVO.getConverter(AgendaForwardDestinationVO.class).convert(agendaForwardDestinationEntities, AgendaForwardDestinationVO.class);
		
		for(AgendaForwardDestinationVO agendaForwardDestination:agendaForwardDestinations){
			if(agendaForwardDestination.getDestinationType().equals(DestinationType.ROLE.toString())){
				if(roleEntities!=null && roleEntities.size()>0){
					for(RolePO roleEntity:roleEntities){
						if(agendaForwardDestination.getDestinationId().equals(roleEntity.getId())){
							agendaForwardDestination.setDestinationName(roleEntity.getName());
							break;
						}
					}
				}
			}else if(agendaForwardDestination.getDestinationType().equals(DestinationType.GROUP_MEMBER.toString())){
				if(members!=null && members.size()>0){
					for(BusinessGroupMemberPO member:members){
						if(agendaForwardDestination.getDestinationId().equals(member.getId())){
							agendaForwardDestination.setDestinationName(member.getName());
							break;
						}
					}
				}
			}
		}
		
		return agendaForwardDestinations;
	}
	
	/**
	 * 删除议程转发目的<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月23日 下午2:19:12
	 * @param Long id 议程转发目的id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void removeDestination(Long id) throws Exception{
		AgendaForwardDestinationPO agendaForwardDestination = agendaForwardDestinationDao.findOne(id);
		if(agendaForwardDestination != null){
			agendaForwardDestinationDao.delete(agendaForwardDestination);
		}
	}
	
	/**
	 * 添加虚拟源设置<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月23日 下午5:02:41
	 * @param Long id 议程转发id
	 * @param Integer min 源数目下限
	 * @param Integer max 源数目上限
	 * @param Long layoutId 虚拟源id
	 * @return List<LayoutScopeVO> 虚拟源设置列表
	 */
	@Transactional(rollbackFor = Exception.class)
	public List<LayoutScopeVO> addLayoutScope(
			Long id,
			Integer min,
			Integer max,
			Long layoutId) throws Exception{
		
		LayoutPO layout = layoutDao.findOne(layoutId);
		
		List<LayoutScopePO> layoutScopeEntities = new ArrayList<LayoutScopePO>();
		for(int i=min; i<=max; i++){
			LayoutScopePO layoutScopeEntity = new LayoutScopePO();
			layoutScopeEntity.setSourceNumber(i);
			layoutScopeEntity.setLayoutId(layoutId);
			layoutScopeEntity.setAgendaForwardId(id);
			layoutScopeEntity.setUpdateTime(new Date());
			layoutScopeEntities.add(layoutScopeEntity);
		}
		layoutScopeDao.save(layoutScopeEntities);
		
		List<LayoutScopeVO> layoutScopes = new ArrayList<LayoutScopeVO>();
		for(LayoutScopePO layoutScopeEntity:layoutScopeEntities){
			LayoutScopeVO layoutScope = new LayoutScopeVO().set(layoutScopeEntity)
														   .setLayoutName(layout.getName());
			layoutScopes.add(layoutScope);
		}
		
		return layoutScopes;
	}
	
	/**
	 * 删除虚拟源设置<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月23日 下午5:07:33
	 * @param Long id 虚拟源设置id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void removeLayoutScope(Long id) throws Exception{
		LayoutScopePO layoutScope = layoutScopeDao.findOne(id);
		if(layoutScope != null){
			layoutScopeDao.delete(layoutScope);
		}
	}
	
	/**
	 * 为自定义议程转发设置虚拟源<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月30日 上午11:51:58
	 * @param Long id 议程转发id
	 * @param Long layoutId 虚拟源id
	 * @return LayoutVO 虚拟源带布局
	 */
	@Transactional(rollbackFor = Exception.class)
	public LayoutVO setLayout(
			Long id,
			Long layoutId) throws Exception{
		AgendaForwardPO agendaForwardEntity = agendaForwardDao.findOne(id);
		if(agendaForwardEntity != null){
			agendaForwardEntity.setLayoutId(layoutId);
			agendaForwardDao.save(agendaForwardEntity);
		}
		
		LayoutPO layoutEntity = layoutDao.findOne(layoutId);
		if(layoutEntity == null) return null;
		List<LayoutPositionPO> layoutPositionEntities = layoutPositionDao.findByLayoutIdOrderBySerialNum(layoutId);
		LayoutVO layout = new LayoutVO().set(layoutEntity).setPositions(new ArrayList<LayoutPositionVO>());
		if(layoutPositionEntities!=null && layoutPositionEntities.size()>0){
			for(LayoutPositionPO layoutPositionEntity:layoutPositionEntities){
				layout.getPositions().add(new LayoutPositionVO().set(layoutPositionEntity));
			}
		}
		return layout;
	}
	
	/**
	 * 添加议程转发自定义音频<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月23日 下午2:58:34
	 * @param Long agendaForwardId 议程转发id
	 * @param JSONString roleChannelIds 角色通道id
	 * @param JSONString groupMemberChannelIds 会议成员通道id
	 * @return List<CustomAudioVO> 音频列表
	 */
	@Transactional(rollbackFor = Exception.class)
	public List<CustomAudioVO> addCustomAudio(
			Long agendaForwardId,
			String roleChannelIds,
			String groupMemberChannelIds) throws Exception{
		
		List<Long> parsedRoleChannelIds = null;
		if(roleChannelIds!=null && !"".equals(roleChannelIds)) parsedRoleChannelIds = JSON.parseArray(roleChannelIds, Long.class);
		List<Long> parsedGroupMemberChannelIds = null; 
		if(groupMemberChannelIds!=null && !"".equals(groupMemberChannelIds)) parsedGroupMemberChannelIds = JSON.parseArray(groupMemberChannelIds, Long.class);
		
		List<RoleChannelPO> roleChannelEntities = roleChannelDao.findAll(parsedRoleChannelIds);
		Set<Long> roleIds = new HashSetWrapper<Long>().add(-1l).getSet();
		if(roleChannelEntities!=null &&roleChannelEntities.size()>0){
			for(RoleChannelPO roleChannel:roleChannelEntities){
				roleIds.add(roleChannel.getRoleId());
			}
		}
		List<RolePO> roleEntities = roleDao.findAll(roleIds);
		
		List<BusinessGroupMemberTerminalChannelPO> memberChannels = ((parsedGroupMemberChannelIds==null||parsedGroupMemberChannelIds.size()<=0)?null:businessGroupMemberTerminalChannelDao.findAll(parsedGroupMemberChannelIds));
		List<Object[]> originMemberIdMaps = ((parsedGroupMemberChannelIds==null||parsedGroupMemberChannelIds.size()<=0)?null:businessGroupMemberTerminalChannelDao.findGroupMemberIdByIdIn(parsedGroupMemberChannelIds));
		Map<Long, Long> memberIdMaps = null;
		if(originMemberIdMaps!=null && originMemberIdMaps.size()>0){
			memberIdMaps = new HashMap<Long, Long>();
			for(Object[] originMemberIdMap:originMemberIdMaps){
				memberIdMaps.put(Long.valueOf(originMemberIdMap[0].toString()), Long.valueOf(originMemberIdMap[1].toString()));
			}
		}
		List<BusinessGroupMemberPO> members = (memberIdMaps==null||memberIdMaps.size()<=0)?null:businessGroupMemberDao.findAll(memberIdMaps.values());
		
		List<CustomAudioPO> customAudioEntities = new ArrayList<CustomAudioPO>();
		if(parsedRoleChannelIds!=null && parsedRoleChannelIds.size()>0){
			for(Long roleChannelId:parsedRoleChannelIds){
				CustomAudioPO customAudioEntity = new CustomAudioPO();
				customAudioEntity.setSourceId(roleChannelId);
				customAudioEntity.setSourceType(SourceType.ROLE_CHANNEL);
				customAudioEntity.setPermissionId(agendaForwardId);
				customAudioEntity.setPermissionType(CustomAudioPermissionType.AGENDA_FORWARD);
				customAudioEntity.setUpdateTime(new Date());
				customAudioEntities.add(customAudioEntity);
			}
		}
		if(parsedGroupMemberChannelIds!=null && parsedGroupMemberChannelIds.size()>0){
			for(Long groupMemberChannelId:parsedGroupMemberChannelIds){
				CustomAudioPO customAudioEntity = new CustomAudioPO();
				customAudioEntity.setSourceId(groupMemberChannelId);
				customAudioEntity.setSourceType(SourceType.GROUP_MEMBER_CHANNEL);
				customAudioEntity.setPermissionId(agendaForwardId);
				customAudioEntity.setPermissionType(CustomAudioPermissionType.AGENDA_FORWARD);
				customAudioEntity.setUpdateTime(new Date());
				customAudioEntities.add(customAudioEntity);
			}
		}
		customAudioDao.save(customAudioEntities);
		
		List<CustomAudioVO> customAudios = new ArrayList<CustomAudioVO>();
		for(CustomAudioPO customAudioEntity:customAudioEntities){
			CustomAudioVO customAudio = new CustomAudioVO().set(customAudioEntity);
			if(customAudioEntity.getSourceType().equals(SourceType.ROLE_CHANNEL) && 
				roleChannelEntities!=null && 
				roleChannelEntities.size()>0 &&
				roleEntities!=null &&
				roleEntities.size()>0){
				for(RoleChannelPO roleChannel:roleChannelEntities){
					if(roleChannel.getId().equals(customAudioEntity.getSourceId())){
						for(RolePO role:roleEntities){
							if(roleChannel.getRoleId().equals(role.getId())){
								customAudio.setSourceName(new StringBufferWrapper().append(role.getName()).append(" - ").append(roleChannel.getName()).toString());
								break;
							}
						}
						break;
					}
				}
			}else if(customAudioEntity.getSourceType().equals(SourceType.GROUP_MEMBER_CHANNEL) &&
					memberChannels!=null &&
					memberChannels.size()>0 &&
					members!=null &&
					members.size()>0){
				for(BusinessGroupMemberTerminalChannelPO memberChannel:memberChannels){
					if(memberChannel.getId().equals(customAudioEntity.getSourceId())){
						for(BusinessGroupMemberPO member:members){
							if(member.getId().equals(memberIdMaps.get(memberChannel.getId()))){
								customAudio.setSourceName(new StringBufferWrapper().append(member.getName()).append(" - ").append(memberChannel.getTerminalChannelname()).toString());
								break;
							}
						}
						break;
					}
				}
			}
			customAudios.add(customAudio);
		}
		
		return customAudios;
	}
	
	/**
	 * 删除议程转发自定义音频<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月23日 下午3:03:42
	 * @param Long id 自定义音频id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void removeCustomAudio(Long id) throws Exception{
		CustomAudioPO customAudio = customAudioDao.findOne(id);
		if(customAudio != null){
			customAudioDao.delete(customAudio);
		}
	}
	
}

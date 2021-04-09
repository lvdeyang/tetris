package com.sumavision.tetris.bvc.model.agenda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.bvc.business.dao.BusinessGroupMemberDAO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberTerminalChannelPO;
import com.sumavision.tetris.bvc.business.po.member.dao.BusinessGroupMemberTerminalChannelDAO;
import com.sumavision.tetris.bvc.model.layout.LayoutDAO;
import com.sumavision.tetris.bvc.model.layout.LayoutPO;
import com.sumavision.tetris.bvc.model.layout.LayoutPositionDAO;
import com.sumavision.tetris.bvc.model.layout.LayoutPositionPO;
import com.sumavision.tetris.bvc.model.layout.LayoutPositionVO;
import com.sumavision.tetris.bvc.model.layout.LayoutVO;
import com.sumavision.tetris.bvc.model.role.RoleChannelDAO;
import com.sumavision.tetris.bvc.model.role.RoleChannelPO;
import com.sumavision.tetris.bvc.model.role.RoleDAO;
import com.sumavision.tetris.bvc.model.role.RolePO;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashSetWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Component
public class AgendaForwardQuery {

	@Autowired
	private AgendaDAO agendaDao;

	@Autowired
	private AgendaForwardDAO agendaForwardDao;
	
	@Autowired
	private AgendaForwardSourceDAO agendaForwardSourceDao;
	
	@Autowired
	private AgendaForwardDestinationDAO agendaForwardDestinationDao;
	
	@Autowired
	private LayoutScopeDAO layoutScopeDao;
	
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
	private BusinessGroupMemberTerminalChannelDAO businessGroupMemberTerminalChannelDao;
	
	@Autowired
	private BusinessGroupMemberDAO businessGroupMemberDao;
	
	/**
	 * 查询音频类型列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月22日 下午4:04:42
	 * @return List<Map<String, String>> 音频类型列表
	 */
	public List<Map<String, String>> queryAutioTypes() throws Exception{
		List<Map<String, String>> audioTypes = new ArrayList<Map<String,String>>();
		AudioType[] values = AudioType.values();
		for(AudioType value:values){
			audioTypes.add(new HashMapWrapper<String, String>().put("key", value.toString())
															   .put("value", value.getName())
															   .getMap());
		}
		return audioTypes;
	}
	
	/**
	 * 查询虚拟源布局设置类型<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月22日 下午6:22:41
	 * @return List<Map<String, String>> 虚拟源布局设置类型
	 */
	public List<Map<String, String>> queryLayoutPositionSelectionType() throws Exception{
		List<Map<String, String>> audioTypes = new ArrayList<Map<String,String>>();
		LayoutPositionSelectionType[] values = LayoutPositionSelectionType.values();
		for(LayoutPositionSelectionType value:values){
			audioTypes.add(new HashMapWrapper<String, String>().put("key", value.toString())
															   .put("value", value.getName())
															   .getMap());
		}
		return audioTypes;
	}
	
	/**
	 * 查询内置议程转发<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月22日 下午2:22:11
	 * @param Long agendaId 议程id
	 * @return List<AgendaForwardVO> 议程转发列表
	 */
	public List<AgendaForwardVO> loadInternal(Long agendaId) throws Exception{
		List<AgendaForwardPO> agendaForwardEntities = agendaForwardDao.findByAgendaId(agendaId);
		List<AgendaForwardVO> agendaForwards = AgendaForwardVO.getConverter(AgendaForwardVO.class).convert(agendaForwardEntities, AgendaForwardVO.class);
		if(agendaForwardEntities!=null && agendaForwardEntities.size()>0){
			List<Long> agendaForwardIds = new ArrayList<Long>();
			for(AgendaForwardPO agendaForwardEntity:agendaForwardEntities){
				agendaForwardIds.add(agendaForwardEntity.getId());
			}
			
			Set<Long> roleIds = new HashSetWrapper<Long>().add(-1l).getSet();
			Set<Long> roleChannelIds = new HashSetWrapper<Long>().add(-1l).getSet();
			Set<Long> layoutIds = new HashSetWrapper<Long>().add(-1l).getSet();
			
			List<AgendaForwardSourcePO> agendaForwardSourceEntities = agendaForwardSourceDao.findByAgendaForwardIdIn(agendaForwardIds);
			if(agendaForwardSourceEntities!=null && agendaForwardSourceEntities.size()>0){
				for(AgendaForwardSourcePO agendaForwardSourceEntity:agendaForwardSourceEntities){
					if(agendaForwardSourceEntity.getSourceType().equals(SourceType.ROLE_CHANNEL)) roleChannelIds.add(agendaForwardSourceEntity.getSourceId());
				}
			}
			List<AgendaForwardDestinationPO> agendaForwardDestinationEntities = agendaForwardDestinationDao.findByAgendaForwardIdIn(agendaForwardIds);
			if(agendaForwardDestinationEntities!=null && agendaForwardDestinationEntities.size()>0){
				for(AgendaForwardDestinationPO agendaForwardDestinationEntity:agendaForwardDestinationEntities){
					if(agendaForwardDestinationEntity.getDestinationType().equals(DestinationType.ROLE)) roleIds.add(agendaForwardDestinationEntity.getDestinationId());
				}
			}
			List<LayoutScopePO> layoutScopeEntities = layoutScopeDao.findByAgendaForwardIdIn(agendaForwardIds);
			if(layoutScopeEntities!=null && layoutScopeEntities.size()>0){
				for(LayoutScopePO layoutScopeEntity:layoutScopeEntities){
					layoutIds.add(layoutScopeEntity.getLayoutId());
				}
			}
			List<CustomAudioPO> customAudioEntities = customAudioDao.findByPermissionIdInAndPermissionType(agendaForwardIds, CustomAudioPermissionType.AGENDA_FORWARD);
			if(customAudioEntities!=null && customAudioEntities.size()>0){
				for(CustomAudioPO customAudioEntity:customAudioEntities){
					if(customAudioEntity.getSourceType().equals(SourceType.ROLE_CHANNEL)) roleChannelIds.add(customAudioEntity.getSourceId());
				}
			}
			
			List<RoleChannelPO> roleChannelEntities = roleChannelDao.findAll(roleChannelIds);
			if(roleChannelEntities!=null && roleChannelEntities.size()>0){
				for(RoleChannelPO roleChannelEntity:roleChannelEntities){
					roleIds.add(roleChannelEntity.getRoleId());
				}
			}
			List<RolePO> roleEntities = roleDao.findAll(roleIds);
			List<LayoutPO> layoutEntities = layoutDao.findAll(layoutIds);
			
			for(AgendaForwardVO agendaForward:agendaForwards){
				if(agendaForwardSourceEntities!=null && agendaForwardSourceEntities.size()>0){
					for(AgendaForwardSourcePO agendaForwardSourceEntity:agendaForwardSourceEntities){
						if(agendaForwardSourceEntity.getAgendaForwardId().equals(agendaForward.getId())){
							AgendaForwardSourceVO agendaForwardSource = new AgendaForwardSourceVO().set(agendaForwardSourceEntity);
							if(agendaForwardSourceEntity.getSourceType().equals(SourceType.ROLE_CHANNEL) && 
									roleChannelEntities!=null && 
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
							agendaForward.getSources().add(agendaForwardSource);
						}
					}
				}
				
				if(agendaForwardDestinationEntities!=null && agendaForwardDestinationEntities.size()>0){
					for(AgendaForwardDestinationPO agendaForwardDestinationEntity:agendaForwardDestinationEntities){
						if(agendaForwardDestinationEntity.getAgendaForwardId().equals(agendaForward.getId())){
							AgendaForwardDestinationVO agendaForwardDestination = new AgendaForwardDestinationVO().set(agendaForwardDestinationEntity);
							if(agendaForwardDestinationEntity.getDestinationType().equals(DestinationType.ROLE) &&
									roleEntities!=null &&
									roleEntities.size()>0){
								for(RolePO role:roleEntities){
									if(agendaForwardDestinationEntity.getDestinationId().equals(role.getId())){
										agendaForwardDestination.setDestinationName(role.getName());
										break;
									}
								}
							}
							agendaForward.getDestinations().add(agendaForwardDestination);
						}
					}
				}
				
				if(layoutScopeEntities!=null && layoutScopeEntities.size()>0){
					for(LayoutScopePO layoutScopeEntity:layoutScopeEntities){
						if(agendaForward.getId().equals(layoutScopeEntity.getAgendaForwardId())){
							LayoutScopeVO layoutScope = new LayoutScopeVO().set(layoutScopeEntity);
							if(layoutEntities!=null && layoutEntities.size()>0){
								for(LayoutPO layoutEntity:layoutEntities){
									if(layoutScopeEntity.getLayoutId().equals(layoutEntity.getId())){
										layoutScope.setLayoutName(layoutEntity.getName());
										break;
									}
								}
							}
							agendaForward.getLayouts().add(layoutScope);
						}
					}
				}
				
				if(customAudioEntities!=null && customAudioEntities.size()>0){
					for(CustomAudioPO customAudioEntity:customAudioEntities){
						if(customAudioEntity.getPermissionId().equals(agendaForward.getId())){
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
							}
							agendaForward.getAudios().add(customAudio);
						}
					}
				}
				
			}
			
		}
		return agendaForwards;
	}
	
	/**
	 * 查询自定义议程转发<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月22日 下午2:22:11
	 * @param Long agendaId 议程id
	 * @return List<AgendaForwardVO> 议程转发列表
	 */
	public List<AgendaForwardVO> loadCustom(Long agendaId) throws Exception{
		List<AgendaForwardPO> agendaForwardEntities = agendaForwardDao.findByAgendaId(agendaId);
		List<AgendaForwardVO> agendaForwards = AgendaForwardVO.getConverter(AgendaForwardVO.class).convert(agendaForwardEntities, AgendaForwardVO.class);
		return agendaForwards;
	}
	
	/**
	 * 查询议程转发中的配置<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月29日 下午5:21:22
	 * @param Long id 议程转发id
	 * @return Map<String, Object> 议程转发中的配置
	 * 	layout LayoutVO 布局信息
	 *  sources List<AgendaForwardSourceVO> 转发源
	 *  destinations List<AgendaForwardDestinationVO> 转发目的
	 *  audios List<CustomAudioVO> 音频列表
	 */
	public Map<String, Object> loadSettings(Long id) throws Exception{
		Map<String, Object> settings = new HashMap<String, Object>();
		AgendaForwardPO agendaForwardEntity = agendaForwardDao.findOne(id);
		if(agendaForwardEntity.getLayoutId() != null){
			LayoutPO layoutEntity = layoutDao.findOne(agendaForwardEntity.getLayoutId());
			List<LayoutPositionPO> layoutPositionEntities = layoutPositionDao.findByLayoutIdOrderBySerialNum(agendaForwardEntity.getLayoutId());
			LayoutVO layout = new LayoutVO().set(layoutEntity).setPositions(new ArrayList<LayoutPositionVO>());
			if(layoutPositionEntities!=null && layoutPositionEntities.size()>0){
				for(LayoutPositionPO layoutPositionEntity:layoutPositionEntities){
					layout.getPositions().add(new LayoutPositionVO().set(layoutPositionEntity));
				}
			}
			settings.put("layout", layout);
		}
		
		Set<Long> roleChannelIds = new HashSetWrapper<Long>().add(-1l).getSet();
		Set<Long> roleIds = new HashSetWrapper<Long>().add(-1l).getSet();
		Set<Long> groupMemberChannelIds = new HashSetWrapper<Long>().add(-1l).getSet();
		Set<Long> groupMemberIds = new HashSetWrapper<Long>().add(-1l).getSet();
		
		List<AgendaForwardSourcePO> layoutForwardSourceEntities =  agendaForwardSourceDao.findByAgendaForwardId(id);
		if(layoutForwardSourceEntities!=null && layoutForwardSourceEntities.size()>0){
			for(AgendaForwardSourcePO layoutForwardSourceEntity:layoutForwardSourceEntities){
				if(layoutForwardSourceEntity.getSourceType().equals(SourceType.ROLE_CHANNEL)){
					roleChannelIds.add(layoutForwardSourceEntity.getSourceId());
				}else if(layoutForwardSourceEntity.getSourceType().equals(SourceType.GROUP_MEMBER_CHANNEL)){
					groupMemberChannelIds.add(layoutForwardSourceEntity.getSourceId());
				}
			}
		}
		
		List<AgendaForwardDestinationPO> layoutForwardDestinationEntities = agendaForwardDestinationDao.findByAgendaForwardId(id);
		if(layoutForwardDestinationEntities!=null && layoutForwardDestinationEntities.size()>0){
			for(AgendaForwardDestinationPO layoutForwardDestinationEntity:layoutForwardDestinationEntities){
				if(layoutForwardDestinationEntity.getDestinationType().equals(DestinationType.ROLE)){
					roleIds.add(layoutForwardDestinationEntity.getDestinationId());
				}else if(layoutForwardDestinationEntity.getDestinationType().equals(DestinationType.GROUP_MEMBER)){
					groupMemberIds.add(layoutForwardDestinationEntity.getDestinationId());
				}
			}
		}
		
		List<CustomAudioPO> customAudioEntities = customAudioDao.findByPermissionIdInAndPermissionType(new ArrayListWrapper<Long>().add(id).getList(), CustomAudioPermissionType.AGENDA_FORWARD);
		if(customAudioEntities!=null && customAudioEntities.size()>0){
			for(CustomAudioPO customAudioEntity:customAudioEntities){
				if(customAudioEntity.getSourceType().equals(SourceType.ROLE_CHANNEL)){
					roleChannelIds.add(customAudioEntity.getSourceId());
				}else if(customAudioEntity.getSourceType().equals(SourceType.GROUP_MEMBER_CHANNEL)){
					groupMemberChannelIds.add(customAudioEntity.getSourceId());
				}
			}
		}
		
		List<RoleChannelPO> roleChannelEntities = roleChannelDao.findAll(roleChannelIds);
		if(roleChannelEntities!=null && roleChannelEntities.size()>0){
			for(RoleChannelPO roleChannelEntity:roleChannelEntities){
				roleIds.add(roleChannelEntity.getRoleId());
			}
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
		if(memberIdMaps!=null && memberIdMaps.size()>0) groupMemberIds.addAll(memberIdMaps.values());
		List<BusinessGroupMemberPO> members = businessGroupMemberDao.findAll(groupMemberIds);
		
		if(layoutForwardSourceEntities!=null && layoutForwardSourceEntities.size()>0){
			List<AgendaForwardSourceVO> layoutForwardSources = new ArrayList<AgendaForwardSourceVO>();
			for(AgendaForwardSourcePO layoutForwardSourceEntity:layoutForwardSourceEntities){
				AgendaForwardSourceVO layoutForwardSource = new AgendaForwardSourceVO().set(layoutForwardSourceEntity);
				if(layoutForwardSourceEntity.getSourceType().equals(SourceType.ROLE_CHANNEL)){
					if(roleChannelEntities!=null && roleChannelEntities.size()>0 && 
							roleEntities!=null && roleEntities.size()>0){
						for(RoleChannelPO roleChannelEntity:roleChannelEntities){
							if(roleChannelEntity.getId().equals(layoutForwardSourceEntity.getSourceId())){
								for(RolePO roleEntity:roleEntities){
									if(roleChannelEntity.getRoleId().equals(roleEntity.getId())){
										layoutForwardSource.setSourceName(new StringBufferWrapper().append(roleEntity.getName()).append(" - ").append(roleChannelEntity.getName()).toString());
										break;
									}
								}
								break;
							}
						}
					}
				}else if(layoutForwardSourceEntity.getSourceType().equals(SourceType.GROUP_MEMBER_CHANNEL)){
					if(memberChannels!=null && memberChannels.size()>0 && 
							members!=null && members.size()>0){
						for(BusinessGroupMemberTerminalChannelPO memberChannel:memberChannels){
							if(memberChannel.getId().equals(layoutForwardSourceEntity.getSourceId())){
								for(BusinessGroupMemberPO member:members){
									if(member.getId().equals(memberIdMaps.get(memberChannel.getId()))){
										layoutForwardSource.setSourceName(new StringBufferWrapper().append(member.getName()).append(" - ").append(memberChannel.getTerminalChannelname()).toString());
										break;
									}
								}
								break;
							}
						}
					}
				}else if(layoutForwardSource.getSourceType().equals(SourceType.COMBINE_VIDEO_VIRTUAL_SOURCE.toString())){
					AgendaPO agenda = agendaDao.findOne(layoutForwardSource.getSourceId());
					layoutForwardSource.setSourceName(new StringBufferWrapper().append("虚拟源").append(" - ").append(agenda.getName()).toString());				
				}
				layoutForwardSources.add(layoutForwardSource);
			}
			settings.put("sources", layoutForwardSources);
		}
		
		if(layoutForwardDestinationEntities!=null && layoutForwardDestinationEntities.size()>0){
			List<AgendaForwardDestinationVO> layoutForwardDestinations = new ArrayList<AgendaForwardDestinationVO>();
			for(AgendaForwardDestinationPO layoutForwardDestinationEntity:layoutForwardDestinationEntities){
				AgendaForwardDestinationVO layoutForwardDestination = new AgendaForwardDestinationVO().set(layoutForwardDestinationEntity);
				if(layoutForwardDestinationEntity.getDestinationType().equals(DestinationType.ROLE)){
					if(roleEntities!=null && roleEntities.size()>0){
						for(RolePO roleEntity:roleEntities){
							if(roleEntity.getId().equals(layoutForwardDestinationEntity.getDestinationId())){
								layoutForwardDestination.setDestinationName(roleEntity.getName());
								break;
							}
						}
					}
				}else if(layoutForwardDestinationEntity.getDestinationType().equals(DestinationType.GROUP_MEMBER)){
					if(members!=null && members.size()>0){
						for(BusinessGroupMemberPO member:members){
							if(member.getId().equals(layoutForwardDestinationEntity.getDestinationId())){
								layoutForwardDestination.setDestinationName(member.getName());
								break;
							}
						}
					}
				}
				layoutForwardDestinations.add(layoutForwardDestination);
			}
			settings.put("destinations", layoutForwardDestinations);
		}
		
		if(customAudioEntities!=null && customAudioEntities.size()>0){
			List<CustomAudioVO> customAudios = new ArrayList<CustomAudioVO>();
			for(CustomAudioPO customAudioEntity:customAudioEntities){
				CustomAudioVO customAudio = new CustomAudioVO().set(customAudioEntity);
				if(customAudioEntity.getSourceType().equals(SourceType.ROLE_CHANNEL)){
					if(roleChannelEntities!=null && roleChannelEntities.size()>0 && 
							roleEntities!=null && roleEntities.size()>0){
						for(RoleChannelPO roleChannelEntity:roleChannelEntities){
							if(roleChannelEntity.getId().equals(customAudioEntity.getSourceId())){
								for(RolePO roleEntity:roleEntities){
									if(roleChannelEntity.getRoleId().equals(roleEntity.getId())){
										customAudio.setSourceName(new StringBufferWrapper().append(roleEntity.getName()).append(" - ").append(roleChannelEntity.getName()).toString());
										break;
									}
								}
								break;
							}
						}
					}
				}else if(customAudioEntity.getSourceType().equals(SourceType.GROUP_MEMBER_CHANNEL)){
					if(memberChannels!=null && memberChannels.size()>0 &&
							members!=null && members.size()>0){
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
				}
				customAudios.add(customAudio);
			}
			settings.put("audios", customAudios);
		}
		
		return settings;
	}
	
}

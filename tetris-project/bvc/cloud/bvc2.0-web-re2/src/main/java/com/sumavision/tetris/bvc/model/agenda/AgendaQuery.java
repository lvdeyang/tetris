package com.sumavision.tetris.bvc.model.agenda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.bvc.business.dao.BusinessGroupMemberDAO;
import com.sumavision.tetris.bvc.business.dao.RunningAgendaDAO;
import com.sumavision.tetris.bvc.business.group.RunningAgendaPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberTerminalChannelPO;
import com.sumavision.tetris.bvc.business.po.member.dao.BusinessGroupMemberTerminalChannelDAO;
import com.sumavision.tetris.bvc.model.role.RoleChannelDAO;
import com.sumavision.tetris.bvc.model.role.RoleChannelWithRolePermissionDTO;
import com.sumavision.tetris.bvc.model.role.RoleDAO;
import com.sumavision.tetris.bvc.model.role.RolePO;
import com.sumavision.tetris.bvc.model.role.RoleVO;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Component
public class AgendaQuery {

	@Autowired
	private AgendaDAO agendaDao;
	
	@Autowired
	private RoleAgendaPermissionDAO roleAgendaPermissionDao;
	
	@Autowired
	private RoleDAO roleDao;
	
	@Autowired
	private RoleChannelDAO roleChannelDao;
	
	@Autowired
	private CustomAudioDAO customAudioDao;
	
	@Autowired
	private BusinessGroupMemberTerminalChannelDAO businessGroupMemberTerminalChannelDao;
	
	@Autowired
	private BusinessGroupMemberDAO businessGroupMemberDao;
	
	@Autowired
	private RunningAgendaDAO runningAgendaDao; 
	
	/**
	 * 查询议程业务类型<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月30日 上午8:45:29
	 * @return Set<String> 业务类型
	 */
	public Set<String> queryBusinessTypes() throws Exception{
		BusinessInfoType[] values = BusinessInfoType.values();
		Set<String> types = new HashSet<String>();
		for(BusinessInfoType value:values){
			if(value.isShow()){
				types.add(value.getName());
			}
		}
		return types;
	}
	
	/**
	 * 查询议程音频优先级<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月27日 上午10:51:49
	 * @return List<Map<String, String>> 议程音频优先级列表
	 */
	public List<Map<String, String>> queryAudioPriorities() throws Exception{
		List<Map<String, String>> audioPriorities = new ArrayList<Map<String,String>>();
		AudioPriority[] values = AudioPriority.values();
		for(AudioPriority value:values){
			audioPriorities.add(new HashMapWrapper<String, String>().put("key", value.toString())
					                                                .put("value", value.getName())
					                                                .getMap());
		}
		return audioPriorities;
	}
	
	/**
	 * 根据id查询议程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月22日 下午3:52:41
	 * @param Long id 议程id
	 * @return AgendaVO 议程信息
	 */
	public AgendaVO queryById(Long id) throws Exception{
		AgendaPO agenda = agendaDao.findOne(id);
		return new AgendaVO().set(agenda);
	}
	
	/**
	 * 分页查询内置议程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月30日 下午4:32:18
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return total long 总数据量
	 * @return rows List<AgendaVO> 议程列表
	 */
	public Map<String, Object> loadInternalAgenda(
			int currentPage, 
			int pageSize) throws Exception{
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<AgendaPO> pagedEntities = agendaDao.findByBusinessIdNull(page);
		long total = pagedEntities.getTotalElements();
		List<AgendaVO> agendas = load(pagedEntities.getContent());
		
		return new HashMapWrapper<String, Object>().put("total", total)
												   .put("rows", agendas)
												   .getMap();
	}
	
	/**
	 * 根据业务查询议程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月25日 上午11:46:04
	 * @param Long businessId 业务id
	 * @param BusinessInfoType businessInfoType 业务类型
	 * @return List<AgendaVO> 议程列表
	 */
	public List<AgendaVO> loadByBusinessIdAndBusinessInfoType(
			Long businessId,
			String businessInfoType) throws Exception{
		
		List<AgendaPO> agendaEntities = null;
		if(businessInfoType == null){
			agendaEntities = agendaDao.findByBusinessIdAndBusinessInfoTypeNull(businessId);
			List<AgendaVO> agendas = load(agendaEntities);
			RunningAgendaPO runningAgenda = runningAgendaDao.findByGroupId(businessId);
			if(agendas!=null && agendas.size()>0){
				for(AgendaVO agenda:agendas){
					if(runningAgenda == null){
						agenda.setIsRun(false);
					}else if(runningAgenda.getAgendaId().equals(agenda.getId())){
						agenda.setIsRun(true);
					}else{
						agenda.setIsRun(false);
					}
				}
			}
			return agendas;
		}else{
			agendaEntities = agendaDao.findByBusinessIdAndBusinessInfoType(businessId, BusinessInfoType.valueOf(businessInfoType));
			return load(agendaEntities);
		}
	}
	
	/**
	 * 查询议程详细信息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月25日 上午11:18:37
	 * @param List<AgendaPO> agendaEntities 议程列表
	 * @return List<AgendaVO> 带详细信息的议程列表
	 */
	public List<AgendaVO> load(List<AgendaPO> agendaEntities) throws Exception{
		List<AgendaVO> agendas = AgendaVO.getConverter(AgendaVO.class).convert(agendaEntities, AgendaVO.class);
		
		if(agendas!=null && agendas.size()>0){
			List<Long> agendaIds = new ArrayList<Long>();
			for(AgendaVO agenda:agendas){
				agendaIds.add(agenda.getId());
			}
			List<RoleAgendaPermissionPO> permissions = roleAgendaPermissionDao.findByAgendaIdIn(agendaIds);
			if(permissions!=null && permissions.size()>0){
				List<Long> roleIds = new ArrayList<Long>();
				for(RoleAgendaPermissionPO permission:permissions){
					roleIds.add(permission.getRoleId());
				}
				List<RolePO> roles = roleDao.findAll(roleIds);
				if(roles!=null && roles.size()>0){
					for(AgendaVO agenda:agendas){
						agenda.setRoles(new ArrayList<RoleVO>());
						for(RoleAgendaPermissionPO permission:permissions){
							if(!permission.getAgendaId().equals(agenda.getId())) continue;
							for(RolePO role:roles){
								if(permission.getRoleId().equals(role.getId())){
									agenda.getRoles().add(new RoleVO().set(role));
									break;
								}
							}
						}
					}
				}
			}
			
			List<CustomAudioPO> audioPermissions = customAudioDao.findByPermissionIdInAndPermissionType(agendaIds, CustomAudioPermissionType.AGENDA);
			if(audioPermissions!=null && audioPermissions.size()>0){
				List<CustomAudioVO> audios = new ArrayList<CustomAudioVO>();
				List<Long> roleChannelIds = new ArrayList<Long>();
				List<Long> memberChannelIds = new ArrayList<Long>();
				for(CustomAudioPO audioPermission:audioPermissions){
					audios.add(new CustomAudioVO().set(audioPermission));
					if(SourceType.ROLE_CHANNEL.equals(audioPermission.getSourceType())){
						roleChannelIds.add(audioPermission.getSourceId());
					}else if(SourceType.GROUP_MEMBER_CHANNEL.equals(audioPermission.getSourceType())){
						memberChannelIds.add(audioPermission.getSourceId());
					}
				}
				if(roleChannelIds.size() > 0){
					List<RoleChannelWithRolePermissionDTO> roleChanels = roleChannelDao.findRoleChannelWithRolePermissionByIdIn(roleChannelIds);
					if(roleChanels!=null && roleChanels.size()>0){
						for(RoleChannelWithRolePermissionDTO roleChannel:roleChanels){
							for(CustomAudioVO audio:audios){
								if(audio.getSourceType().equals(SourceType.ROLE_CHANNEL.toString()) &&
										audio.getSourceId().equals(roleChannel.getId())){
									audio.setSourceName(new StringBufferWrapper().append(roleChannel.getRoleName()).append(" - ").append(roleChannel.getName()).toString());
									break;
								}
							}
						}
					}
				}
				if(memberChannelIds.size() > 0){
					List<BusinessGroupMemberTerminalChannelPO> memberChannels = businessGroupMemberTerminalChannelDao.findAll(memberChannelIds);
					List<Object[]> originMemberIdMaps = businessGroupMemberTerminalChannelDao.findGroupMemberIdByIdIn(memberChannelIds);
					Map<Long, Long> memberIdMaps = null;
					if(originMemberIdMaps!=null && originMemberIdMaps.size()>0){
						memberIdMaps = new HashMap<Long, Long>();
						for(Object[] originMemberIdMap:originMemberIdMaps){
							memberIdMaps.put(Long.valueOf(originMemberIdMap[0].toString()), Long.valueOf(originMemberIdMap[1].toString()));
						}
					}
					List<BusinessGroupMemberPO> members = (memberIdMaps==null||memberIdMaps.size()<=0?null:businessGroupMemberDao.findAll(memberIdMaps.values()));
					if(memberChannels!=null && memberChannels.size()>0){
						for(BusinessGroupMemberTerminalChannelPO memberChannel:memberChannels){
							for(CustomAudioVO audio:audios){
								if(audio.getSourceType().equals(SourceType.GROUP_MEMBER_CHANNEL.toString()) &&
										audio.getSourceId().equals(memberChannel.getId())){
									BusinessGroupMemberPO targetMember = null;
									if(members!=null && members.size()>0){
										for(BusinessGroupMemberPO member:members){
											if(member.getId().equals(memberIdMaps.get(memberChannel.getId()))){
												targetMember = member;
												break;
											}
										}
									}
									audio.setSourceName(new StringBufferWrapper().append(targetMember==null?"":targetMember.getName()).append(" - ").append(memberChannel.getTerminalChannelname()).toString());
									break;
								}
							}
						}
					}
				}
				for(AgendaVO agenda:agendas){
					agenda.setCustomAudios(new ArrayList<CustomAudioVO>());
					for(CustomAudioVO audio:audios){
						if(audio.getPermissionId().equals(agenda.getId())){
							agenda.getCustomAudios().add(audio);
						}
					}
				}
			}
		}
		return agendas;
	}
	
}

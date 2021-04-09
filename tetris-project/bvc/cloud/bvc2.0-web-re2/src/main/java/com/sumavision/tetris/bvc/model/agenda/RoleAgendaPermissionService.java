package com.sumavision.tetris.bvc.model.agenda;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.bvc.model.agenda.exception.AgendaNotFoundException;
import com.sumavision.tetris.bvc.model.role.RoleChannelDAO;
import com.sumavision.tetris.bvc.model.role.RoleChannelPO;
import com.sumavision.tetris.bvc.model.role.RoleDAO;
import com.sumavision.tetris.bvc.model.role.RolePO;
import com.sumavision.tetris.bvc.model.role.RoleVO;

@Service
public class RoleAgendaPermissionService {

	@Autowired
	private RoleAgendaPermissionDAO roleAgendaPermissionDao;
	
	@Autowired
	private RoleDAO roleDao;
	
	@Autowired
	private RoleChannelDAO roleChannelDao;
	
	@Autowired
	private AgendaDAO agendaDao;
	
	@Autowired
	private CustomAudioDAO customAudioDao;
	
	@Autowired
	private AgendaForwardDAO agendaForwardDao;
	
	@Autowired
	private AgendaForwardSourceDAO agendaForwardSourceDao;
	
	@Autowired
	private AgendaForwardDestinationDAO agendaForwardDestinationDao;
	
	/**
	 * 向议程中添加角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月1日 上午10:21:03
	 * @param Long agendaId 议程id
	 * @param JSONString roleIds 角色id列表
	 * @return List<RoleVO> 角色列表
	 */
	@Transactional(rollbackFor = Exception.class)
	public List<RoleVO> add(
			Long agendaId,
			String roleIds) throws Exception{
		AgendaPO agenda = agendaDao.findOne(agendaId);
		if(agenda == null){
			throw new AgendaNotFoundException(agendaId);
		}
		List<Long> parsedRoleIds = JSON.parseArray(roleIds, Long.class);
		List<RolePO> roles = null;
		if(parsedRoleIds.size() > 0){
			roles = roleDao.findAll(parsedRoleIds);
		}
		List<RoleAgendaPermissionPO> entities = new ArrayList<RoleAgendaPermissionPO>();
		if(roles!=null && roles.size()>0){
			for(RolePO role:roles){
				RoleAgendaPermissionPO entity = new RoleAgendaPermissionPO();
				entity.setAgendaId(agendaId);
				entity.setRoleId(role.getId());
				entity.setUpdateTime(new Date());
				entities.add(entity);
			}
			roleAgendaPermissionDao.save(entities);
		}
		return RoleVO.getConverter(RoleVO.class).convert(roles, RoleVO.class);
	}
	
	/**
	 * 议程中删除角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月1日 上午10:27:26
	 * @param Long roleId 角色id
	 * @param Long agendaId 议程id
	 * @return List<CustomAudioVO> 删除的议程音频
	 */
	@Transactional(rollbackFor = Exception.class)
	public List<CustomAudioVO> delete(
			Long roleId, 
			Long agendaId) throws Exception{
		List<RoleAgendaPermissionPO> permissions = roleAgendaPermissionDao.findByAgendaIdAndRoleId(agendaId, roleId);
		if(permissions!=null && permissions.size()>0){
			roleAgendaPermissionDao.deleteInBatch(permissions);
		}
		
		List<RoleChannelPO> channels = roleChannelDao.findByRoleId(roleId);
		List<Long> roleIds = new ArrayList<Long>();
		if(channels!=null && channels.size()>0){
			for(RoleChannelPO channel:channels){
				roleIds.add(channel.getId());
			}
		}
		
		//删除议程音频
		List<CustomAudioVO> removedAgendaAudios = null;
		if(roleIds.size() > 0){
			List<CustomAudioPO> agendaAudios = customAudioDao.findByPermissionIdAndPermissionTypeAndSourceIdInAndSourceType(agendaId, CustomAudioPermissionType.AGENDA, roleIds, SourceType.ROLE_CHANNEL);
			removedAgendaAudios = CustomAudioVO.getConverter(CustomAudioVO.class).convert(agendaAudios, CustomAudioVO.class);
			if(agendaAudios!=null && agendaAudios.size()>0){
				customAudioDao.deleteInBatch(agendaAudios);
			}
		}
		
		List<AgendaForwardPO> agendaFrowards = agendaForwardDao.findByAgendaId(agendaId);
		if(agendaFrowards!=null && agendaFrowards.size()>0){
			List<Long> agendaForwardIds = new ArrayList<Long>();
			for(AgendaForwardPO agendaForward:agendaFrowards){
				agendaForwardIds.add(agendaForward.getId());
			}
			//删除议程转发音频
			List<CustomAudioPO> agendaForwardAudios = customAudioDao.findByPermissionIdInAndPermissionTypeAndSourceIdInAndSourceType(agendaForwardIds, CustomAudioPermissionType.AGENDA_FORWARD, roleIds, SourceType.ROLE_CHANNEL);
			if(agendaForwardAudios!=null && agendaForwardAudios.size()>0){
				customAudioDao.deleteInBatch(agendaForwardAudios);
			}
			//删除议程转发源
			List<AgendaForwardSourcePO> forwardSources = agendaForwardSourceDao.findByAgendaForwardIdInAndSourceIdInAndSourceType(agendaForwardIds, roleIds, SourceType.ROLE_CHANNEL);
			if(forwardSources!=null && forwardSources.size()>0){
				agendaForwardSourceDao.deleteInBatch(forwardSources);
			}
			//删除议程转发目的
			List<AgendaForwardDestinationPO> forwardDestinations = agendaForwardDestinationDao.findByAgendaForwardIdInAndDestinationIdAndDestinationType(agendaForwardIds, roleId, DestinationType.ROLE);
			if(forwardDestinations!=null && forwardDestinations.size()>0){
				agendaForwardDestinationDao.deleteInBatch(forwardDestinations);
			}
		}
		
		return removedAgendaAudios;
	}
}

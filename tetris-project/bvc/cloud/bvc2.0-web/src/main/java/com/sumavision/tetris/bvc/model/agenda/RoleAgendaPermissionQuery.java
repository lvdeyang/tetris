package com.sumavision.tetris.bvc.model.agenda;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.bvc.model.role.RoleChannelDAO;
import com.sumavision.tetris.bvc.model.role.RoleChannelPO;
import com.sumavision.tetris.bvc.model.role.RoleChannelType;
import com.sumavision.tetris.bvc.model.role.RoleChannelVO;
import com.sumavision.tetris.bvc.model.role.RoleDAO;
import com.sumavision.tetris.bvc.model.role.RolePO;

@Component
public class RoleAgendaPermissionQuery {

	@Autowired
	private RoleAgendaPermissionDAO roleAgendaPermissionDao;
	
	@Autowired
	private RoleDAO roleDao;
	
	@Autowired
	private RoleChannelDAO roleChannelDao;
	
	/**
	 * 查询议程关联的角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月1日 上午8:59:38
	 * @param Long agendaId 议程id
	 * @return List<RoleAgendaPermissionVO> 角色列表
	 */
	public List<RoleAgendaPermissionVO> load(Long agendaId) throws Exception{
		List<RoleAgendaPermissionVO> permissions = new ArrayList<RoleAgendaPermissionVO>();
		List<RoleAgendaPermissionPO> permissionEntities = roleAgendaPermissionDao.findByAgendaId(agendaId);
		if(permissionEntities!=null && permissionEntities.size()>0){
			List<Long> roleIds = new ArrayList<Long>();
			for(RoleAgendaPermissionPO permissionEntity:permissionEntities){
				roleIds.add(permissionEntity.getRoleId());
			}
			List<RolePO> roles = roleDao.findAll(roleIds);
			for(RoleAgendaPermissionPO permissionEntity:permissionEntities){
				RoleAgendaPermissionVO permission = new RoleAgendaPermissionVO().set(permissionEntity);
				if(roles!=null && roles.size()>0){
					for(RolePO role:roles){
						if(permission.getRoleId().equals(role.getId())){
							permission.setRoleName(role.getName())
									  .setRoleUserMappingType(role.getRoleUserMappingType().toString());
							break;
						}
					}
				}
				permissions.add(permission);
			}
		}
		return permissions;
	}
	
	/**
	 * 查询议程关联的角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月1日 上午8:59:38
	 * @param Long agendaId 议程id
	 * @param String channelType 通道类型VIDEO_ENCODE[|AUDIO_ENCODE][|VIDEO_DECODE][|AUDIO_DECODE][|ENCODE][|DECODE][|ALL]
	 * @return List<RoleAgendaPermissionVO> 角色列表
	 */
	public List<RoleAgendaPermissionVO> loadWithChannel(
			Long agendaId,
			String channelType) throws Exception{
		List<RoleAgendaPermissionVO> permissions = new ArrayList<RoleAgendaPermissionVO>();
		List<RoleAgendaPermissionPO> permissionEntities = roleAgendaPermissionDao.findByAgendaId(agendaId);
		if(permissionEntities!=null && permissionEntities.size()>0){
			List<Long> roleIds = new ArrayList<Long>();
			for(RoleAgendaPermissionPO permissionEntity:permissionEntities){
				roleIds.add(permissionEntity.getRoleId());
			}
			List<RolePO> roles = roleDao.findAll(roleIds);
			List<RoleChannelType> types = new ArrayList<RoleChannelType>();
			if(channelType.equals("VIDEO_ENCODE")){
				types.add(RoleChannelType.VIDEO_ENCODE);
			}else if(channelType.equals("AUDIO_ENCODE")){
				types.add(RoleChannelType.AUDIO_ENCODE);
			}else if(channelType.equals("VIDEO_DECODE")){
				types.add(RoleChannelType.VIDEO_DECODE);
			}else if(channelType.equals("AUDIO_DECODE")){
				types.add(RoleChannelType.AUDIO_DECODE);
			}else if(channelType.equals("ENCODE")){
				types.add(RoleChannelType.VIDEO_ENCODE);
				types.add(RoleChannelType.AUDIO_ENCODE);
			}else if(channelType.equals("DECODE")){
				types.add(RoleChannelType.VIDEO_DECODE);
				types.add(RoleChannelType.AUDIO_DECODE);
			}else if(channelType.equals("ALL")){
				types.add(RoleChannelType.VIDEO_ENCODE);
				types.add(RoleChannelType.AUDIO_ENCODE);
				types.add(RoleChannelType.VIDEO_DECODE);
				types.add(RoleChannelType.AUDIO_DECODE);
			}
			List<RoleChannelPO> channels = roleChannelDao.findByRoleIdInAndTypeIn(roleIds, types);
			for(RoleAgendaPermissionPO permissionEntity:permissionEntities){
				RoleAgendaPermissionVO permission = new RoleAgendaPermissionVO().set(permissionEntity);
				if(roles!=null && roles.size()>0){
					for(RolePO role:roles){
						if(permission.getRoleId().equals(role.getId())){
							permission.setRoleName(role.getName())
									  .setRoleUserMappingType(role.getRoleUserMappingType().toString());
							if(channels!=null && channels.size()>0){
								for(RoleChannelPO channel:channels){
									if (channel.getRoleId().equals(permission.getRoleId())) {
										permission.addChannel(new RoleChannelVO().set(channel).setRoleName(permission.getRoleName()));
									}
								}
							}
							break;
						}
					}
				}
				permissions.add(permission);
			}
		}
		return permissions;
	}
	
}
